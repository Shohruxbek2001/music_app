# Music app

## Features

### Core Functionalities

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/Shohruxbek2001/music_app
   ```
2. Install the Nix package manager by selecting your OS in the official guide. Don't forget to reopen the terminal!
   ```bash
   sh <(curl -L https://nixos.org/nix/install) --daemon
   ```
3. Enable the flakes feature:
   ```bash
    mkdir -p ~/.config/nix
    echo 'experimental-features = nix-command flakes' >> ~/.config/nix/nix.conf
   ```
4. Install direnv if not installed. Once the hook is configured, restart your shell for direnv to be activated.

    ```bash
    sudo apt-get install direnv
    ```
   #### BASH

   Add the following line at the end of the **~/.bashrc** file:
   ```bash
   eval "$(direnv hook bash)"
   ```

   #### ZSH

   Add the following line at the end of the **~/.zshrc** file:
   ```bash
   eval "$(direnv hook zsh)"
   ```
   Allow direnv in project root directory:
    ```bash
    direnv allow
    ```
4. Launch the application:
    ```bash
    sbt
    project endpoints-runner
    ~reStart
    ```

   For API documentation, open http://localhost:8000/swagger.


