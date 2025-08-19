#!/bin/bash
for arg in "$@"; do
    case "$arg" in
        -d|--delete)
            read -p "Do you want to delete minesweeper app ? (Y/n): " choice
            if [[ "$choice" != "y" || "$choice" != "Y" ]]; then
                echo "Uninstallation aborted."
                exit 1
            fi
            echo "🔍 Searching for the program..."
            if [ -e "/usr/local/bin/minesweeper" ]; then
                echo "✅ Program link found."
            else
                echo "❌ Program link not found."
                exit 1
            fi

            if [ -e "/opt/minesweeper/launch.sh" ]; then
                echo "✅ Program folder found."
            else
                echo "❌ Program folder not found."
                echo "🗑️ Deleting the program link..."
                echo "🔐 Your password is required..."
                sudo rm -f /usr/local/bin/minesweeper
                exit 1
            fi
            echo "🗑️ Deleting the program link..."
            echo "🔐 Your password is required..."
            ls /opt/minesweeper/
            sudo rm -f /usr/local/bin/minesweeper
            echo "🗑️ Deleting the program files..."
            sudo rm -f /opt/minesweeper/minesweeper.jar
            sudo rm -f /opt/minesweeper/LICENSE
            echo "✅ Uninstallation completed successfully."
            echo "🗑️ Deleting the final file..."
            echo "👋 Thanks to have play this minesweeper"
            sudo rm -f /opt/minesweeper/launch.sh
            exit 0
            ;;
        *)
            echo "❓ Unknow arg : $arg"
            ;;
    esac
done

SCRIPT_DIR="$(cd "$(dirname "$(realpath "$0")")" && pwd)"
java -jar "$SCRIPT_DIR/minesweeper.jar"