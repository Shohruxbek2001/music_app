CREATE TYPE USER_STATUS AS ENUM (
	'ACTIVE',
	'INACTIVE',
	'EXPIRED'
);

CREATE TYPE ORDER_STATUS AS ENUM (
	'NEW',
	'PENDING',
	'PENDING_PAYMENT',
	'PAID',
	'CANCELLED'
);

CREATE TYPE PAYMENT_METHOD AS ENUM (
    'CARD',
    'CASH',
    'P2P'
);

CREATE TYPE AUTH_PROVIDER AS ENUM ('LOCAL', 'GOOGLE', 'APPLE', 'FACEBOOK');

CREATE TABLE IF NOT EXISTS privileges (
  name VARCHAR NOT NULL PRIMARY KEY,
  group_name VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS roles (
  id UUID NOT NULL PRIMARY KEY,
  name VARCHAR NOT NULL UNIQUE
);

INSERT INTO
  roles (id, name)
VALUES
  (
    '7aa5ba51-5f32-4123-b88c-aca7c8e7b033',
    'TECH_ADMIN'
  );

CREATE TABLE IF NOT EXISTS role_privileges (
  role_id UUID NOT NULL CONSTRAINT fk_role_id REFERENCES roles (id) ON DELETE CASCADE,
  privilege VARCHAR NOT NULL CONSTRAINT fk_role_privilege REFERENCES privileges (name) ON DELETE CASCADE,
  UNIQUE (role_id, privilege)
);

CREATE TABLE IF NOT EXISTS markets (
  id UUID NOT NULL PRIMARY KEY,
  name VARCHAR NOT NULL,
  description VARCHAR NULL,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE NULL,
  deleted_at TIMESTAMP WITH TIME ZONE NULL
);

CREATE TABLE IF NOT EXISTS users (
  id UUID NOT NULL PRIMARY KEY,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL,
  name VARCHAR NOT NULL,
  email VARCHAR UNIQUE NOT NULL,
  role_id UUID NOT NULL CONSTRAINT fk_user_role REFERENCES roles (id) ON DELETE CASCADE,
  status USER_STATUS NOT NULL,
  phone VARCHAR,
  password VARCHAR NOT NULL,
  market_id UUID NULL CONSTRAINT fk_market_id REFERENCES markets (id) ON DELETE CASCADE,
  updated_at TIMESTAMP WITH TIME ZONE NULL,
  deleted_at TIMESTAMP WITH TIME ZONE NULL
);

CREATE TABLE IF NOT EXISTS categories (
  id UUID NOT NULL PRIMARY KEY,
  name VARCHAR NOT NULL UNIQUE,
  description TEXT,
  parent_id UUID NULL CONSTRAINT fk_parent_category REFERENCES categories (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS products (
    id UUID NOT NULL PRIMARY KEY,
    slug VARCHAR NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(12, 2) NOT NULL,
    discount_price DECIMAL(12, 2),
    stock_quantity INT NOT NULL DEFAULT 0 CHECK (stock_quantity >= 0),
    category_id UUID NOT NULL CONSTRAINT fk_product_category REFERENCES categories (id) ON DELETE CASCADE,
    market_id UUID NOT NULL CONSTRAINT fk_market_product REFERENCES markets (id) ON DELETE CASCADE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NULL,
    deleted_at TIMESTAMP WITH TIME ZONE NULL
);

CREATE TABLE product_variants (
    id UUID PRIMARY KEY,
    product_id UUID NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    name VARCHAR NOT NULL,
    price DECIMAL(12,2) NOT NULL,
    discount_price DECIMAL(12,2),
    stock_quantity INT NOT NULL DEFAULT 0 CHECK (stock_quantity >= 0),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    updated_at TIMESTAMP WITH TIME ZONE NULL,
    deleted_at TIMESTAMP WITH TIME ZONE NULL
);


CREATE TABLE product_images (
    id UUID PRIMARY KEY,
    product_id UUID REFERENCES products(id) ON DELETE CASCADE,
    image_url VARCHAR NOT NULL,
    position INT DEFAULT 0,
    is_main BOOLEAN DEFAULT false
);

CREATE TABLE customers (
    id UUID PRIMARY KEY,
    email VARCHAR UNIQUE NOT NULL,
    name VARCHAR NOT NULL,
    phone VARCHAR,
    auth_provider AUTH_PROVIDER DEFAULT 'LOCAL',
    provider_user_id VARCHAR,
    password_hash VARCHAR,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    updated_at TIMESTAMP WITH TIME ZONE NULL,
    deleted_at TIMESTAMP WITH TIME ZONE NULL
);

CREATE TABLE addresses (
    id UUID PRIMARY KEY,
    customer_id UUID REFERENCES customers(id) ON DELETE CASCADE,
    street VARCHAR NOT NULL,
    city VARCHAR NOT NULL,
    region VARCHAR NOT NULL,
    postal_code VARCHAR,
    country VARCHAR DEFAULT 'Uzbekistan',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    updated_at TIMESTAMP WITH TIME ZONE NULL,
    deleted_at TIMESTAMP WITH TIME ZONE NULL
);

CREATE TABLE cart_items (
    id UUID PRIMARY KEY,
    customer_id UUID REFERENCES customers(id) ON DELETE CASCADE,
    product_variant_id UUID REFERENCES product_variants(id) ON DELETE CASCADE,
    quantity INT NOT NULL CHECK (quantity > 0),
    added_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

CREATE TABLE favorites (
    id UUID PRIMARY KEY,
    customer_id UUID REFERENCES customers(id) ON DELETE CASCADE,
    product_id UUID REFERENCES products(id) ON DELETE CASCADE,
    added_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

CREATE TABLE discounts (
    id UUID PRIMARY KEY,
    code VARCHAR UNIQUE NOT NULL,
    description TEXT,
    discount_percent DECIMAL(5,2) NOT NULL,
    valid_from TIMESTAMP WITH TIME ZONE,
    valid_until TIMESTAMP WITH TIME ZONE,
    max_uses INTEGER,
    times_used INTEGER DEFAULT 0
);

CREATE TABLE orders (
    id UUID PRIMARY KEY,
    customer_id UUID NOT NULL REFERENCES customers(id) ON DELETE CASCADE,
    address_id UUID REFERENCES addresses(id) ON DELETE SET NULL,
    total_price DECIMAL(12,2) NOT NULL,
    discount_id UUID REFERENCES discounts(id) ON DELETE SET NULL,
    status ORDER_STATUS DEFAULT 'PENDING',
    order_number VARCHAR UNIQUE,
    address_snapshot JSONB,
    payment_method PAYMENT_METHOD DEFAULT 'CARD',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    updated_at TIMESTAMP WITH TIME ZONE NULL,
    deleted_at TIMESTAMP WITH TIME ZONE NULL
);

CREATE TABLE order_items (
    id UUID PRIMARY KEY,
    order_id UUID NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    product_variant_id UUID NOT NULL REFERENCES product_variants(id) ON DELETE CASCADE,
    quantity INTEGER NOT NULL,
    unit_price DECIMAL(12,2) NOT NULL,
    unit_price_after_discount DECIMAL(12,2) NOT NULL,
    total_price DECIMAL(12,2) NOT NULL
);

CREATE TABLE discount_usages (
    id UUID PRIMARY KEY,
    discount_id UUID NOT NULL REFERENCES discounts(id) ON DELETE CASCADE,
    customer_id UUID NOT NULL REFERENCES customers(id) ON DELETE CASCADE,
    order_id UUID NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    used_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

CREATE TABLE ratings (
    id UUID PRIMARY KEY,
    customer_id UUID NOT NULL REFERENCES customers(id) ON DELETE CASCADE,
    product_id UUID NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    rating INTEGER CHECK (rating >= 1 AND rating <= 5),
    comment TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    updated_at TIMESTAMP WITH TIME ZONE NULL
);

CREATE TABLE IF NOT EXISTS specifications (
    id UUID NOT NULL PRIMARY KEY,
    name VARCHAR NOT NULL,
    category_id UUID NULL CONSTRAINT fk_category REFERENCES categories (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS specification_values (
    id UUID NOT NULL PRIMARY KEY,
    value VARCHAR NOT NULL,
    specification_id UUID NOT NULL CONSTRAINT fk_specification REFERENCES specifications (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS product_specifications (
    product_id UUID NOT NULL
        CONSTRAINT fk_product REFERENCES products (id) ON DELETE CASCADE,
    specification_id UUID NOT NULL
        CONSTRAINT fk_specification REFERENCES specifications (id) ON DELETE CASCADE,
    specification_value_id UUID NOT NULL
        CONSTRAINT fk_specification_value REFERENCES specification_values (id) ON DELETE CASCADE,
    UNIQUE (product_id, specification_id)
);


CREATE TABLE audit_logs (
    id UUID PRIMARY KEY,
    user_id UUID REFERENCES users(id) ON DELETE SET NULL,
    action VARCHAR NOT NULL, -- 'create_product', 'delete_user', 'change_status'
    target_table VARCHAR,    -- 'products', 'orders', ...
    target_id UUID,          -- ID
    metadata JSONB,          -- info
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

INSERT INTO
  "users" (
    "id",
    "created_at",
    "name",
    "email",
    "role_id",
    "status",
    "password"
  )
VALUES
  (
    '72a911c8-ad24-4e2d-8930-9c3ba51741df',
    '2025-01-09T10:19:00.000Z',
    'Tech Admin',
    'babayevshahruh@mail.ru',
    '7aa5ba51-5f32-4123-b88c-aca7c8e7b033',
    'ACTIVE',
    '$s0$e0801$5JK3Ogs35C2h5htbXQoeEQ==$N7HgNieSnOajn1FuEB7l4PhC6puBSq+e1E8WUaSJcGY='
  );

  -- Create the trigger function
CREATE OR REPLACE FUNCTION fill_admin_role_trigger()
RETURNS TRIGGER AS $$
BEGIN
  -- Insert a row into role_privileges for the admin role and the new privilege
  INSERT INTO role_privileges (role_id, privilege)
  SELECT '7aa5ba51-5f32-4123-b88c-aca7c8e7b033'::UUID, NEW.name
  WHERE NOT EXISTS (
    SELECT 1 FROM role_privileges
    WHERE role_id = '7aa5ba51-5f32-4123-b88c-aca7c8e7b033'::UUID AND privilege = NEW.name
  );

  RETURN NULL; -- Since this is an AFTER trigger, we don't need to return anything
END;
$$ LANGUAGE plpgsql;

-- Create the trigger
CREATE TRIGGER fill_admin_role_trigger
AFTER INSERT ON privileges
FOR EACH ROW
EXECUTE FUNCTION fill_admin_role_trigger();


CREATE OR REPLACE VIEW role_privileges_view AS
SELECT
    r.id AS role_id,
    r.name AS role_name,
    ARRAY_AGG(p.name) AS privileges
FROM
    roles r
LEFT JOIN
    role_privileges rp ON r.id = rp.role_id
LEFT JOIN
    privileges p ON rp.privilege = p.name
GROUP BY
    r.id, r.name;

CREATE OR REPLACE VIEW product_specifications_view AS
SELECT
    ps.product_id,
    JSON_AGG(
        JSON_BUILD_OBJECT(
            'id', s.id,
            'name', s.name,
            'value_id', sv.id,
            'value', sv.value
        )
    ) AS specifications
FROM product_specifications ps
JOIN specifications s ON ps.specification_id = s.id
JOIN specification_values sv ON ps.specification_value_id = sv.id
GROUP BY ps.product_id;

CREATE OR REPLACE FUNCTION increment_discount_usage()
RETURNS TRIGGER AS $$
BEGIN
  IF NEW.discount_id IS NOT NULL THEN
    UPDATE discounts SET times_used = times_used + 1 WHERE id = NEW.discount_id;
  END IF;
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_increment_discount_usage
AFTER INSERT ON orders
FOR EACH ROW
EXECUTE FUNCTION increment_discount_usage();


CREATE OR REPLACE VIEW cart_items_detailed_view AS
SELECT
    ci.id AS cart_item_id,
    ci.customer_id,
    ci.quantity,
    ci.added_at,

    pv.id AS product_variant_id,
    pv.name AS variant_name,
    pv.price,
    pv.discount_price,
    pv.stock_quantity,

    p.id AS product_id,
    p.name AS product_name,
    p.slug,
    p.category_id,
    p.description,

    img.image_url AS main_image_url,

    COALESCE(pv.discount_price, pv.price) * ci.quantity AS total_price

FROM cart_items ci
JOIN product_variants pv ON ci.product_variant_id = pv.id
JOIN products p ON pv.product_id = p.id
LEFT JOIN product_images img
    ON img.product_id = p.id AND img.is_main = true;


CREATE OR REPLACE VIEW order_detailed_view AS
SELECT
    o.id AS order_id,
    o.customer_id,
    o.status AS order_status,
    o.total_price AS order_total_price,
    o.created_at AS order_created_at,
    o.payment_method,
    o.address_snapshot,

    JSON_AGG(
      JSON_BUILD_OBJECT(
        'order_item_id', oi.id,
        'quantity', oi.quantity,
        'unit_price', oi.unit_price,
        'unit_price_after_discount', oi.unit_price_after_discount,
        'total_price', oi.total_price,
        'product_variant_id', pv.id,
        'variant_name', pv.name,
        'product_id', p.id,
        'product_name', p.name,
        'slug', p.slug,
        'description', p.description,
        'main_image_url', img.image_url
      )
    ) AS items

FROM orders o
JOIN order_items oi ON o.id = oi.order_id
JOIN product_variants pv ON oi.product_variant_id = pv.id
JOIN products p ON pv.product_id = p.id
LEFT JOIN product_images img ON img.product_id = p.id AND img.is_main = true

GROUP BY
    o.id, o.customer_id, o.status, o.total_price,
    o.created_at, o.payment_method, o.address_snapshot;

CREATE SEQUENCE orders_order_number_seq START 1 INCREMENT 1;

