#!/bin/bash

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
APP_NAME="minesweeper"
INSTALL_DIR="/opt/$APP_NAME"
BIN_PATH="/usr/local/bin/$APP_NAME"
JAR_FILE="$SCRIPT_DIR/src/minesweeper.jar"
LAUNCH_SCRIPT="$SCRIPT_DIR/src/launch.sh"
LICENSE_FILE="$SCRIPT_DIR/LICENSE"

if [ -e "$BIN_PATH" ]; then
    echo "⚠️ The program '$APP_NAME' is already insatalled in $BIN_PATH."
    read -p "Do you want to continue and update it ? (y/n): " choice
    if [[ "$choice" != "y" ]]; then
        echo "Installation/update canceled."
        exit 1
    fi
    sudo rm -f "$BIN_PATH"
fi

echo "📦 Installation of $APP_NAME in $INSTALL_DIR..."
echo "🔐 Your password is required..."
sudo mkdir -p "$INSTALL_DIR"
sudo cp "$LICENSE_FILE" "$JAR_FILE" "$LAUNCH_SCRIPT" "$INSTALL_DIR"
sudo chmod +x "$INSTALL_DIR/$(basename "$LAUNCH_SCRIPT")"

echo "🔗 Creating command link '$APP_NAME' in /usr/local/bin/ ..."
sudo ln -s "$INSTALL_DIR/$(basename "$LAUNCH_SCRIPT")" "$BIN_PATH"

echo "🔍 Search for Java installation..."
if ! java --version >/dev/null 2>&1; then
    echo "⚠️ Java is not installed."
    echo "⚙️ Java will be installed now."
    read -p "Do you want to install java (game can't be installed without java) ? (Y/n): " choice
    if [[ "$choice" == "y" || "$choice" == "Y" ]]; then
        if [[ "$OSTYPE" == "darwin"* ]]; then
            # macOS
            echo "⛔️ Installation imposible : Please install Java manually on macOS."
        elif [[ -f /etc/debian_version ]]; then
            # Debian / Ubuntu
            echo "📦 Installing Java..."
            sudo apt update >/dev/null 2>&1
            sudo apt install -y default-jre >/dev/null 2>&1
            echo "✅ Java has been installed."

        elif [[ -f /etc/fedora-release ]]; then
            # Fedora
            echo "📦 Installing Java..."
            sudo dnf install -y java-17-openjdk >/dev/null 2>&1
            echo "✅ Java has been installed."

        elif [[ -f /etc/arch-release ]]; then
            # Arch Linux
            echo "📦 Installing Java..."
            sudo pacman -Sy --noconfirm jre-openjdk >/dev/null 2>&1
            echo "✅ Java has been installed."

        elif [[ -f /etc/SuSE-release || -f /etc/SUSE-brand ]]; then
            # openSUSE
            echo "📦 Installing Java..."
            sudo zypper install -y java-17-openjdk >/dev/null 2>&1
            echo "✅ Java has been installed."

        else
            echo "❌ Unsupported OS or distribution. Please install Java manually."
            exit 1
        fi
    else
            echo "Installation aborted."
            exit 1
    fi
fi

read -p "Do you want to delete temporary file ? (y/n): " choice
if [[ "$choice" == "y" ]]; then
    echo "🧹 Deleting temporary file..."
    sudo rm -f "$SCRIPT_DIR/src/"*
    sudo rm -f "$SCRIPT_DIR/LICENSE"
    sudo rm -f "$SCRIPT_DIR/guide.md"

    echo "✅ Instattation succesfully completed"
    echo "➡️ Run game with : $APP_NAME"

    echo "🗑️ Deleting installation script..."
    sudo rm -- "$0"
else
    echo "✅ Instattation succesfully completed"
    echo "➡️ Run game with : $APP_NAME"
fi