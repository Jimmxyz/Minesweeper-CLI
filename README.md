# Minesweeper-CLI
A simple minesweeper in the CLI :

- **Color Support** : number, flags, score, cursor and undiscovered area are colored.
- **Easy to use** : every keys are show and the control are intuitive.
- **Customisable** : you can change the size of the grid and the percentage of mine.

You can also try the GUI version (web) : <a href='https://jimmxyz.github.io/Simple-Minesweeper/'>https://jimmxyz.github.io/Simple-Minesweeper/</a> or see the <a href='https://github.com/Jimmxyz/Simple-Minesweeper'>repository</a>.

# Installation
First download the zip file and then unzip it.
>[!CAUTION]
>You need a cli which support RGB ANSI color (if you have a modern cli it will be supported)

>[!CAUTION]
>You also need to have java installed.

>[!TIP]
>On debian, ubuntu, fedora, suse and arch based distribution java will be automaticly installed.
# Run it
## Linux :
Change the terminal locaion to be in the folder 'minesweeper_install'.
Then use this command : `bash ./install.sh`.
Then follow the instruction.
## Mac OS :
>[!WARNING]
>The program was not tested on macos.
Install java.
Then change the terminal locaion to be in the folder 'minesweeper_install'.
Then use this command : `chmod +x install.sh`.
Then use this command : `sudo xattr -d com.apple.quarantine install.sh`.
The use this command : `./install.sh`. 
Then follow the instruction.

## Windows :
>[!WARNING]
>This cli program don't work on windows cmd or powershell.
### Install with WSL
1. On your powershell type `wsl --install`.

    By default the distribution will be ubuntu wich is a debian based so you don't have to change it.
   
3. Run wsl on your powershell by type `wsl.exe`.

4. Install java on wsl.

5. Change the terminal location to the folder 'minesweeper_install/src/'

6. Use this command `chmod +x launch.sh`

7. Then use this command to launch the game `./launch.sh`.

>[!IMPORTANT]
>You have to be in the folder 'minesweeper_install/src/' or indicate the path.

# Credit
Created by @Jimmxyz on github

# License
This project is under MIT license
