#!/bin/bash
if ! java --version >/dev/null 2>&1; then
    echo "Java is not installed."
    echo "🛠 Java will be installed now."
    read -p "Do you want to continue? (Y/n): " choice
    if [[ "$choice" == "y" || "$choice" == "Y" ]]; then
        sudo apt update >/dev/null 2>&1 && sudo apt install -y default-jre >/dev/null 2>&1
        echo "Java has been installed."
    else
        echo "Installation aborted."
        exit 1
    fi
fi
java -jar ./minesweeper.jar