#!/bin/bash

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
APP_NAME="minesweeper"
INSTALL_DIR="/opt/$APP_NAME"
BIN_PATH="/usr/local/bin/$APP_NAME"
JAR_FILE="$SCRIPT_DIR/src/minesweeper.jar"
LAUNCH_SCRIPT="$SCRIPT_DIR/src/launch.sh"
LICENSE_FILE="$SCRIPT_DIR/LICENSE"

# Vérifier si le programme existe déjà
if [ -e "$BIN_PATH" ]; then
    echo "⚠️ The program '$APP_NAME' is already insatalled in $BIN_PATH."
    read -p "Do you want to continue and update it ? (y/n): " choice
    if [[ "$choice" != "y" ]]; then
        echo "Installation/update canceled."
        exit 1
    fi
    sudo rm -f "$BIN_PATH"
fi

# Installation
echo "📦 Installation of $APP_NAME in $INSTALL_DIR..."
echo "🔐 Your password is required..."
sudo mkdir -p "$INSTALL_DIR"
sudo cp "$LICENSE_FILE" "$JAR_FILE" "$LAUNCH_SCRIPT" "$INSTALL_DIR"
sudo chmod +x "$INSTALL_DIR/$(basename "$LAUNCH_SCRIPT")"

# Lien symbolique
echo "🔗 Creating command link '$APP_NAME' in /usr/local/bin/ ..."
sudo ln -s "$INSTALL_DIR/$(basename "$LAUNCH_SCRIPT")" "$BIN_PATH"

# Nettoyage (optionnel)
read -p "Do you want to delete temporary file ? (y/n): " choice
if [[ "$choice" == "y" ]]; then
    echo "🧹 Deleting temporary file..."
    sudo rm -f "$SCRIPT_DIR/src/"*
    sudo rm -f "$SCRIPT_DIR/LICENSE"
    sudo rm -f "$SCRIPT_DIR/guide.md"
fi

echo "✅ Instattation succesfully completed"
echo "➡️ Run game with : $APP_NAME"

# Auto-suppression du script
if $choice == "y"; then
    echo "🗑️ Deleting installation script..."
    sudo rm -- "$0"
fi