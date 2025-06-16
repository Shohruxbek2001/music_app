package uz.scala.shared

import uz.scala.Language
import uz.scala.Language._

object ResponseMessages {
  val FILE_NOT_FOUND: Map[Language, String] = Map(
    En -> "File not found",
    Ru -> "Файл не найден",
    Uz -> "Fayl topilmadi",
  )

  val FILE_CREATED: Map[Language, String] = Map(
    En -> "File successfully created",
    Ru -> "Файл успешно создан",
    Uz -> "Fayl yaratildi",
  )

  val USER_NOT_FOUND: Map[Language, String] = Map(
    En -> "User not found",
    Ru -> "Пользователь не найден",
    Uz -> "Foydalanuvchi topilmadi",
  )

  val PHONE_ALREADY_EXISTS: Map[Language, String] = Map(
    En -> "This phone number already exists",
    Ru -> "Этот номер телефона уже существует",
    Uz -> "Ushbu telefon raqam allaqachon mavjud",
  )

  val USER_CREATED: Map[Language, String] = Map(
    En -> "User successfully created",
    Ru -> "Пользователь успешно создан",
    Uz -> "Foydalanuvchi yaratildi",
  )

  val USER_UPDATED: Map[Language, String] = Map(
    En -> "User updated",
    Ru -> "Пользователь обновлен",
    Uz -> "Foydalanuvchi yangilandi",
  )

  val USER_DELETED: Map[Language, String] = Map(
    En -> "User deleted",
    Ru -> "Пользователь удален",
    Uz -> "Foydalanuvchi o'chirildi",
  )

  val PASSWORD_UPDATED: Map[Language, String] = Map(
    En -> "Password updated",
    Ru -> "Пароль обновлен",
    Uz -> "Parol yangilandi",
  )

  val WRONG_PASSWORD: Map[Language, String] = Map(
    En -> "Wrong password",
    Ru -> "Неверный пароль",
    Uz -> "Noto'g'ri parol",
  )

  val PRIVILEGE_CREATE_USER: Map[Language, String] = Map(
    En -> "You have no privileges to create user",
    Ru -> "У вас нет привилегий создавать пользователя",
    Uz -> "Foydalanuvchi yaratish uchun ruxsat yo'q",
  )

  val CREATE_SUPER_USER: Map[Language, String] = Map(
    En -> "Can't create super user",
    Ru -> "Нельзя создавать суперпользователя",
    Uz -> "Super foydalanuvchi yaratish uchun ruxsat yo'q",
  )

  val PRIVILEGE_CREATE_SUPER_USER: Map[Language, String] = Map(
    En -> "You have no privileges to create super user",
    Ru -> "У вас нет привилегий создавать суперпользователя",
    Uz -> "Sizda super foydalanuvchi yaratish uchun ruxsat yo'q",
  )

  val PASSWORD_DOES_NOT_MATCH: Map[Language, String] = Map(
    En -> "Sms code does not match",
    Ru -> "Код подтверждения не совпадает",
    Uz -> "SMS kodi mos kelmadi",
  )

  val INSUFFICIENT_PRIVILEGES: Map[Language, String] = Map(
    En -> "Forbidden. Insufficient privileges",
    Ru -> "Запрещено. Недостаточно привилегий",
    Uz -> "Taqiqlangan. Imtiyozlar yetarli emas",
  )

  val AUTHENTICATION_REQUIRED: Map[Language, String] = Map(
    En -> "Authentication required",
    Ru -> "Требуется аутентификация",
    Uz -> "Autentifikatsiya talab qilinadi",
  )

  val INVALID_TOKEN: Map[Language, String] = Map(
    En -> "Invalid token or expired",
    Ru -> "Неверный токен или токен устарел",
    Uz -> "Yaroqsiz yoki eskirgan token",
  )

  val BEARER_TOKEN_NOT_FOUND: Map[Language, String] = Map(
    En -> "Bearer token not found",
    Ru -> "Токен не найден",
    Uz -> "Bearer token topilmadi",
  )

  val ROLE_NOT_FOUND: Map[Language, String] = Map(
    En -> "Role not found",
    Ru -> "Роль не найдена",
    Uz -> "Rol topilmadi",
  )

  val ROLE_CREATED: Map[Language, String] = Map(
    En -> "Role successfully created",
    Ru -> "Роль успешно создана",
    Uz -> "Rol yaratildi",
  )

  val ROLE_UPDATED: Map[Language, String] = Map(
    En -> "Role successfully updated",
    Ru -> "Роль успешно обновлена",
    Uz -> "Rol yangilandi",
  )

  val ROLE_DELETED: Map[Language, String] = Map(
    En -> "Role successfully deleted",
    Ru -> "Роль успешно удалена",
    Uz -> "Rol o'chirildi",
  )

  val CATEGORY_DELETED: Map[Language, String] = Map(
    En -> "Category deleted",
    Ru -> "Категория удалена",
    Uz -> "Kategoriya o'chirildi",
  )

  val CATEGORY_UPDATED: Map[Language, String] = Map(
    En -> "Category updated",
    Ru -> "Категория обновлена",
    Uz -> "Kategoriya yangilandi",
  )

  val CATEGORY_CREATED: Map[Language, String] = Map(
    En -> "Category successfully created",
    Ru -> "Категория успешно создана",
    Uz -> "Kategoriya yaratildi",
  )

  val CATEGORY_NOT_FOUND: Map[Language, String] = Map(
    En -> "Category not found",
    Ru -> "Категория не найдена",
    Uz -> "Kategoriya topilmadi",
  )

  val PRODUCT_NOT_FOUND: Map[Language, String] = Map(
    En -> "Product not found",
    Ru -> "Продукт не найден",
    Uz -> "Mahsulot topilmadi",
  )

  val PRODUCT_PRICE_VALIDATION: Map[Language, String] = Map(
    En -> "Product price validation error, all prices must be set or all must be empty",
    Ru -> "Ошибка валидации цены продукта, все цены должны быть установлены или все должны быть пустыми",
    Uz -> "Mahsulot narxini tekshirish xatosi, barcha narxlar oʻrnatilishi yoki hammasi boʻsh boʻlishi kerak",
  )

  val NOT_ENOUGH_STORE_PRODUCT: Map[Language, String] = Map(
    En -> "Not enough product in store",
    Ru -> "Недостаточно товаров в магазине",
    Uz -> "Do'konda yetarli mahsulot yo'q",
  )

  val NOT_ENOUGH_STOCK_PRODUCT: Map[Language, String] = Map(
    En -> "Not enough product in stock",
    Ru -> "Недостаточно товаров в наличии",
    Uz -> "Zahirada yetarli mahsulot yo'q",
  )

  val PRODUCT_DELETED: Map[Language, String] = Map(
    En -> "Product deleted",
    Ru -> "Продукт удален",
    Uz -> "Mahsulot o'chirildi",
  )

  val PRODUCT_SPECIFICATION_DELETED: Map[Language, String] = Map(
    En -> "Product specification deleted",
    Ru -> "Продукт спецификация удален",
    Uz -> "Mahsulot spetsifikatsiya o'chirildi",
  )

  val PRODUCT_SPECIFICATION_ADDED: Map[Language, String] = Map(
    En -> "Product specification added",
    Ru -> "Продукт спецификация добавлен",
    Uz -> "Mahsulotga spetsifikatsiya qo'shildi",
  )

  val PRODUCT_CREATED: Map[Language, String] = Map(
    En -> "Product successfully created",
    Ru -> "Продукт успешно создан",
    Uz -> "Mahsulot yaratildi",
  )

  val PRODUCT_UPDATED: Map[Language, String] = Map(
    En -> "Product updated",
    Ru -> "Продукт обновлен",
    Uz -> "Mahsulot yangilandi",
  )

  val SETTING_NOT_FOUND: Map[Language, String] = Map(
    En -> "Setting not found",
    Ru -> "Параметр не найден",
    Uz -> "Sozlama topilmadi",
  )

  val SETTING_UPDATED: Map[Language, String] = Map(
    En -> "Setting updated",
    Ru -> "Параметр обновлен",
    Uz -> "Sozlama yangilandi",
  )

  val GIVEN_EMPTY_LIST: Map[Language, String] = Map(
    En -> "Given empty list",
    Ru -> "Дан пустой список",
    Uz -> "Bo'sh ro'yxat berilgan",
  )

  val PRODUCT_DISPATCHED: Map[Language, String] = Map(
    En -> "Product dispatched",
    Ru -> "Продукт отправлен",
    Uz -> "Mahsulot jo'natildi",
  )

  val PRODUCT_DISPOSED: Map[Language, String] = Map(
    En -> "Product disposed",
    Ru -> "Продукт утилизирован",
    Uz -> "Mahsulot utilizatsiya qilindi",
  )

  val PARTNER_DELETED: Map[Language, String] = Map(
    En -> "Partner deleted",
    Ru -> "Партнер удален",
    Uz -> "Hamkor o'chirildi",
  )

  val PARTNER_CREATED: Map[Language, String] = Map(
    En -> "Partner successfully created",
    Ru -> "Партнер успешно создан",
    Uz -> "Hamkor yaratildi",
  )

  val PARTNER_UPDATED: Map[Language, String] = Map(
    En -> "Partner updated",
    Ru -> "Партнер обновлен",
    Uz -> "Hamkor yangilandi",
  )

  val PARTNER_NOT_FOUND: Map[Language, String] = Map(
    En -> "Partner not found",
    Ru -> "Партнер не найден",
    Uz -> "Hamkor topilmadi",
  )

  val ORDER_DELETED: Map[Language, String] = Map(
    En -> "Order deleted",
    Ru -> "Заказ удален",
    Uz -> "Buyurtma o'chirildi",
  )

  val DELETE_ORDER_NOT_ALLOWED: Map[Language, String] = Map(
    En -> "Delete order not allowed, because order already paid",
    Ru -> "Заказ нельзя удалять, потому что он уже оплачен",
    Uz -> "Buyurtmani o'chirishni mumkin emas, chunki buyurta allaqachon to'langan",
  )

  val ORDER_NOT_FOUND: Map[Language, String] = Map(
    En -> "Order not found",
    Ru -> "Заказ не найден",
    Uz -> "Buyurtma topilmadi",
  )

  val ORDER_ITEM_NOT_FOUND: Map[Language, String] = Map(
    En -> "Order item not found",
    Ru -> "Товар заказа не найден",
    Uz -> "Buyurtma elementi topilmadi",
  )

  val SPECIFICATION_NOT_FOUND: Map[Language, String] = Map(
    En -> "Specification not found",
    Ru -> "Спецификация не найден",
    Uz -> "Spetsifikatsiya topilmadi",
  )

  val SPECIFICATION_VALUE_NOT_FOUND: Map[Language, String] = Map(
    En -> "Specification value not found",
    Ru -> "Значение спецификации не найден",
    Uz -> "Spetsifikatsiya qiymati topilmadi",
  )

  val SPECIFICATION_CREATED: Map[Language, String] = Map(
    En -> "Specification successfully created",
    Ru -> "Спецификация успешно создана",
    Uz -> "Spetsifikatsiya yaratildi",
  )

  val SPECIFICATION_UPDATED: Map[Language, String] = Map(
    En -> "Specification updated",
    Ru -> "Спецификация обновлен",
    Uz -> "Spetsifikatsiya yangilandi",
  )

  val SPECIFICATION_VALUE_UPDATED: Map[Language, String] = Map(
    En -> "Specification value updated",
    Ru -> "Значение спецификации обновлен",
    Uz -> "Spetsifikatsiya qiymati yangilandi",
  )

  val SPECIFICATION_VALUE_CREATED: Map[Language, String] = Map(
    En -> "Specification value successfully created",
    Ru -> "Значение спецификации успешно создано",
    Uz -> "Spetsifikatsiya qiymati yaratildi",
  )

  val SPECIFICATION_DELETED: Map[Language, String] = Map(
    En -> "Specification deleted",
    Ru -> "Спецификация удален",
    Uz -> "Spetsifikatsiya o'chirildi",
  )

  val SPECIFICATION_VALUE_DELETED: Map[Language, String] = Map(
    En -> "Specification value deleted",
    Ru -> "Значение спецификации удален",
    Uz -> "Spetsifikatsiya qiymati o'chirildi",
  )

  val MARKET_NOT_FOUND: Map[Language, String] = Map(
    En -> "Market not found",
    Ru -> "Магазин не найден",
    Uz -> "Magazin topilmadi",
  )

  val MARKET_CREATED: Map[Language, String] = Map(
    En -> "Market successfully created",
    Ru -> "Магазин успешно создан",
    Uz -> "Magazin yaratildi",
  )

  val MARKET_UPDATED: Map[Language, String] = Map(
    En -> "Market successfully updated",
    Ru -> "Магазин успешно обновлен",
    Uz -> "Magazin yangilandi",
  )

  val MARKET_DELETED: Map[Language, String] = Map(
    En -> "Market successfully deleted",
    Ru -> "Магазин успешно удален",
    Uz -> "Magazin o'chirildi",
  )

  val NO_ACCESS_TO_MARKET: Map[Language, String] = Map(
    En -> "You have no access to this market",
    Ru -> "У вас нет доступа к этому маркету",
    Uz -> "Sizda bu magazinni ko'rishga ruxsat yo'q",
  )

  val PAYMENT_CREATED: Map[Language, String] = Map(
    En -> "Payment successfully created",
    Ru -> "Оплата успешно создана",
    Uz -> "Payment yaratildi",
  )

  val ORDER_NOT_APPROVED: Map[Language, String] = Map(
    En -> "Please approve the order first",
    Ru -> "Сначала пожалуйста, одобрите заказ",
    Uz -> "Iltimos oldin buyurtmani tasdiqlang",
  )

  val ORDER_ITEMS_NOT_FOUND: Map[Language, String] = Map(
    En -> "Order items not found",
    Ru -> "Товары заказа не найдены",
    Uz -> "Buyurtma elementi topilmadi",
  )

  val APPROVE_ORDER_NOT_ALLOWED: Map[Language, String] = Map(
    En -> "Order approval is not allowed",
    Ru -> "Одобрение заказа не разрешено",
    Uz -> "Buyurtmani tasdiqlashga ruxsat yo'q",
  )

  val PAYMENT_ITEMS_NOT_FOUND: Map[Language, String] = Map(
    En -> "Please add a payment method first.",
    Ru -> "Сначала пожалуйста, добавьте способ оплаты.",
    Uz -> "Iltimos, avval toʻlov usulini kiriting.",
  )

  val ORDER_CLOSED: Map[Language, String] = Map(
    En -> "Order already closed",
    Ru -> "Заказ уже закрыт",
    Uz -> "Buyurtma allaqachon yopilgan",
  )

  val ORDER_LIMIT_EXCEEDED: Map[Language, String] = Map(
    En -> "Order limit exceeded",
    Ru -> "Превышен лимит заказов",
    Uz -> "Buyurtmalar maximal chegaraga yetdi",
  )

  val PAYMENT_AMOUNT_NOT_EQUAL_TO_ORDER: Map[Language, String] = Map(
    En -> "Payment amount is not equal to order amount",
    Ru -> "Сумма оплаты не равна сумме заказа",
    Uz -> "To'lov miqdori buyurtma miqdoriga teng emas",
  )
}
