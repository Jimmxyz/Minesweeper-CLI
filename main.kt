import kotlin.random.Random
import java.io.BufferedReader
import java.io.InputStreamReader

fun main() {
    println("=== Minesweeper CLI ===")
    val width_input = ""
    val height_input = ""
    val cursor_x = 0
    val cursor_y = 0
    //blocking touch
    Runtime.getRuntime().exec(arrayOf("sh","-c","stty raw -echo < /dev/tty")).waitFor()
    val (width,height) = choose_size()
    var grid = gen_grid(height, width, 20)
    grid_readdy(height, width)
    val visual_grid = visual_grid_gen(height, width)
    print_grid(visual_grid,height,width,cursor_x,cursor_y)
    loop_main(width, height, cursor_x, cursor_y, grid, visual_grid)
    Runtime.getRuntime().exec(arrayOf("sh","-c","stty sane < /dev/tty")).waitFor()
    print("\u001b[2K\r")
    println("Thank you for playing Minesweeper CLI!")
}

fun gen_grid(cols: Int, rows: Int, luck:Int): Array<Array<Int>> {
    return Array(cols) { Array(rows) { if (Random.nextInt(100) < luck) 1 else 0 } }
}

fun visual_grid_gen(rows: Int, cols: Int): Array<Array<Int>> {
    return Array(rows) { Array(cols) { 0 } }
}

fun print_grid(grid_to_display: Array<Array<Int>>,height: Int = 9,width: Int = 9,cursor_i: Int = 0,cursor_j: Int = 0) {
    repeat(height) {
        print("\u001b[1A") 
        print("\u001b[2K\r")
    }

    for (i in 0 until height) {
        print("\u001b[2K\r")
        for (j in 0 until width) {
            val bck_color = if (grid_to_display[i][j] == 0 || grid_to_display[i][j] == 10) {
                if ((i + j) % 2 == 0) "\u001b[48;2;88;168;88m" else "\u001b[48;2;82;153;82m"
            } else {
                if ((i + j) % 2 == 0) "\u001b[48;2;231;164;133m" else "\u001b[48;2;214;140;105m"
            }

            val font_color = when (grid_to_display[i][j]) {
                1 -> "\u001b[38;2;73;74;210m"
                2 -> "\u001b[38;2;64;120;67m"
                3 -> "\u001b[38;2;210;73;73m"
                4 -> "\u001b[38;2;140;57;161m"
                5 -> "\u001b[38;2;243;237;38m"
                6 -> "\u001b[38;2;106;232;226m"
                7 -> "\u001b[38;2;133;133;131m"
                10 -> "\u001b[38;2;210;0;0m"
                else -> "\u001b[38;2;0;0;0m"
            }

            val cursor_color = "\u001b[38;2;255;255;255m"
            if (i == cursor_i && j == cursor_j) {
                val content = when (grid_to_display[i][j]) {
                    0, 9 -> " "
                    10 -> "F"
                    else -> grid_to_display[i][j].toString()
                }
                print("\u001B[1m$bck_color$cursor_color[$font_color$content$cursor_color$bck_color\u001B[1m]\u001b[0m")
            }
            else {
                val content = when (grid_to_display[i][j]) {
                    0, 9 -> "   "
                    10 -> " F "
                    else -> " ${grid_to_display[i][j]} "
                }
                print("\u001B[1m$bck_color$font_color$content\u001b[0m")
            }
        }
        println()
    }

    // Dernière ligne d’instruction
    print("\u001b[2K\r")
    println("Press Q to quit, arrow to move, enter to select, and space to mark a mine.")
}


fun grid_readdy(height: Int, width: Int) {
    for (i in 0..height -1) {
        print("\u001b[2K\r")
        println(i)
    }
}

fun loop_main(width: Int, height: Int, cursor_x_imported: Int, cursor_y_imported: Int, grid: Array<Array<Int>>, visual_grid_imported: Array<Array<Int>>) {
    var first = true
    var cursor_x = cursor_x_imported 
    var cursor_y = cursor_y_imported
    var visual_grid = visual_grid_imported
    val reader = InputStreamReader(System.`in`)
    loop@ while (true) {
        val ch = reader.read()
        if (ch == - 1) break
        print("\u001b[1A")
        print("\u001b[2K\r")
        when (ch) {
            27 -> {
                val nextChar = reader.read()
                if (nextChar == 91) {
                    when (reader.read()) {
                        65 -> {
                            cursor_x = if (cursor_x > 0) cursor_x - 1 else height - 1
                            print_grid(visual_grid,height,width,cursor_x,cursor_y)
                        }
                        66 -> {
                            cursor_x = if (cursor_x < height - 1) cursor_x + 1 else 0
                            print_grid(visual_grid,height,width,cursor_x,cursor_y)
                        }
                        67 -> {
                            cursor_y = if (cursor_y < width - 1) cursor_y + 1 else 0
                            print_grid(visual_grid,height,width,cursor_x,cursor_y)    
                        }
                        68 -> {
                            cursor_y = if (cursor_y > 0) cursor_y - 1 else width - 1
                            print_grid(visual_grid,height,width,cursor_x,cursor_y)
                        }
                        else -> {
                            println("Invalid arrow key input.")    
                        }
                    }
                }
            }
            13 -> {
                if(first == true){
                    first = false
                    remove_mine(grid,height,width,cursor_x,cursor_y)
                }
                if (grid[cursor_x][cursor_y] == 1) {
                    println("Game Over! You hit a mine!")
                    break@loop
                    } else {
                        update_visual_grid(visual_grid, grid, cursor_x, cursor_y, width, height)
                        print_grid(visual_grid,height,width,cursor_x,cursor_y)
                        if (win_check(grid, visual_grid, height, width)) {
                            print("\u001b[1A")
                            print("\u001b[2K\r")
                            println("Congratulations! You've cleared the minefield!")
                            break@loop
                        }
                    }
            }
            32 -> {
                if (visual_grid[cursor_x][cursor_y] == 0) {
                    visual_grid[cursor_x][cursor_y] = 10 // Marking a mine
                } else if (visual_grid[cursor_x][cursor_y] == 10) {
                    visual_grid[cursor_x][cursor_y] = 0 // Unmarking a mine
                }
                print_grid(visual_grid,height,width,cursor_x,cursor_y)
            }
            'q'.code, 'Q'.code -> {
                println("Exiting Minesweeper CLI...")
                break@loop
            }
            else -> {
                println("Invalid input. Please try again.")
            }
        }
    }
}


fun get_adjacent_mines(x: Int, y: Int, grid: Array<Array<Int>>,width:Int,height:Int): Int {
    var count = 0
    for (i in -1..1) {
        for (j in -1..1) {
            if (i == 0 && j == 0) continue
            val newX = x + i
            val newY = y + j
            if(newX < 0 || newX >= height || newY < 0 || newY >= width) continue
            if (grid[newX][newY] == 1){
                count += 1
            }
        }
    }
    if (count == 0){
        count = 9
    }
    return count
}

fun update_visual_grid(visual_grid: Array<Array<Int>>, grid: Array<Array<Int>>, cursor_x: Int, cursor_y: Int, width: Int, height: Int) {
        var cursor_x_imported = cursor_x
        var cursor_y_imported = cursor_y
        if (cursor_x_imported < 0 || cursor_x_imported >= height || cursor_y_imported < 0 || cursor_y_imported >= width) {
            return
        }
        if (visual_grid[cursor_x_imported][cursor_y_imported] != 0) {
            return
        }
        visual_grid[cursor_x_imported][cursor_y_imported] = get_adjacent_mines(cursor_x_imported, cursor_y_imported, grid, width, height)
        if(visual_grid[cursor_x_imported][cursor_y_imported] == 9) {
            update_visual_grid(visual_grid, grid, cursor_x_imported - 1, cursor_y_imported - 1, width, height)
            update_visual_grid(visual_grid, grid, cursor_x_imported - 1, cursor_y_imported, width, height)
            update_visual_grid(visual_grid, grid, cursor_x_imported - 1, cursor_y_imported + 1, width, height)
            update_visual_grid(visual_grid, grid, cursor_x_imported, cursor_y_imported -1, width, height)
            update_visual_grid(visual_grid, grid, cursor_x_imported, cursor_y_imported +1, width, height)
            update_visual_grid(visual_grid, grid, cursor_x_imported +1, cursor_y_imported -1, width, height)
            update_visual_grid(visual_grid, grid, cursor_x_imported +1, cursor_y_imported, width, height)
            update_visual_grid(visual_grid, grid, cursor_x_imported +1, cursor_y_imported +1, width, height)
        }
}

fun win_check(grid: Array<Array<Int>>, visual_grid: Array<Array<Int>>, height: Int, width: Int): Boolean {
    for (i in 0 until height) {
        for (j in 0 until width) {
            if ((visual_grid[i][j] == 0 || visual_grid[i][j] == 10) && grid[i][j] != 1) {
                return false
            }
        }
    }
    return true
}


fun remove_mine(grid: Array<Array<Int>>, height: Int, width: Int, cursor_x: Int, cursor_y: Int) {
    grid[cursor_x][cursor_y] = 0
    for (i in -1..1) {
        for (j in -1..1) {
            val newX = cursor_x + i
            val newY = cursor_y + j
            
            if (newX in 0 until height && newY in 0 until width) {
                grid[newX][newY] = 0
            }
        }
    }
}

fun choose_size(): Pair<Int, Int> {
    println("Please enter the width of the grid: ")
    var width_input = 10
    var width_cursor = 10
    var height_input = 10
    var height_cursor = 10
    print("\u001b[2K\r")
    println("\u001b[48;2;255;255;255m\u001b[38;2;0;0;0m[" + ("-".repeat(width_cursor -5)) + "\u001b[0m" + (" ".repeat(46 - (width_cursor -5))) + " " + width_cursor + " " .repeat(45) + "]\u001b[0m")
    print("\u001b[2K\r")
    print("Use <- and -> to adjust the width, press space to enter manually, and enter to confirm.")
    val reader = InputStreamReader(System.`in`)
    loop@ while (true) {
        val ch = reader.read()
        if (ch == - 1) break
        when (ch) {
            27 -> {
                val nextChar = reader.read()
                if (nextChar == 91) {
                    when (reader.read()) {
                        68 -> {
                            if (width_cursor > 5) {
                                width_cursor -= 1
                            }
                            else {
                                width_cursor = 5
                            }
                        }
                        67 -> {
                            if (width_cursor < 100) {
                                width_cursor += 1
                            }else {
                                width_cursor = 100
                            }
                        }
                    }
                }
            }
            13 -> {
                width_input = width_cursor
                break@loop
            }
            32 -> {
                width_input = -10
                break@loop
            }
        }
        print("\u001b[2K\r")
        print("\u001b[1A")
        if(width_cursor - 5 < 47) {
            var empty = ""
            if (width_cursor > 9){
                empty = " "
            }
            else{
                empty = "  "
            }
            print("\u001b[2K\r")
            print("\u001b[48;2;255;255;255m\u001b[38;2;0;0;0m[" + ("-".repeat(width_cursor -5)) + "\u001b[0m" + (" ".repeat(46 - (width_cursor -5))) + empty + width_cursor + " " .repeat(45) + "]\u001b[0m")
        } else if( width_cursor - 5 == 47){
            print("\u001b[2K\r")
            print("\u001b[48;2;255;255;255m\u001b[38;2;0;0;0m[" + ("-".repeat(46)) + "-\u001b[0m52" + " " .repeat(45) + "]\u001b[0m")
        }else if( width_cursor - 5 == 48){
            print("\u001b[2K\r")
            print("\u001b[48;2;255;255;255m\u001b[38;2;0;0;0m[" + ("-".repeat(46)) + "-5\u001b[0m3" + " " .repeat(45) + "]\u001b[0m")
        } 
        else if( width_cursor - 5 == 49){
            print("\u001b[2K\r")
            print("\u001b[48;2;255;255;255m\u001b[38;2;0;0;0m[" + ("-".repeat(46)) + "-54\u001b[0m" + " " .repeat(45) + "]\u001b[0m")
        } 
        else if (width_cursor - 5 > 49 && width_cursor - 5 < 95){
            print("\u001b[2K\r")
            print("\u001b[48;2;255;255;255m\u001b[38;2;0;0;0m[" + ("-".repeat(46)) + "-" + width_cursor + "-".repeat(width_cursor - 54) + "\u001b[0m" + (" ".repeat(46 - (width_cursor - 53))) + "]")
        }
        else{
            print("\u001b[2K\r")
            print("\u001b[48;2;255;255;255m\u001b[38;2;0;0;0m[" + ("-".repeat(46)) + "100" + "-".repeat(45) + "]\u001b[0m")
        }
        println()
        print("\u001b[2K\r")
        print("Use <- and -> to adjust the width, press space to enter manually, and enter to confirm.")
    }
    if (width_input == -10) {
        print("\u001b[2K\r")
        print("\u001b[1A")
        print("\u001b[2K\r")
        print("\u001b[1A")
        print("\u001b[2K\r")
        Runtime.getRuntime().exec(arrayOf("sh","-c","stty sane < /dev/tty")).waitFor()
        print("Please enter the width of the grid (manualy): ")
        width_input = readLine()?.toIntOrNull() ?: 10
        Runtime.getRuntime().exec(arrayOf("sh","-c","stty raw -echo < /dev/tty")).waitFor()
        print("\u001b[2K\r")
    }
    else{
        print("\u001b[2K\r")
        print("\u001b[1A")
        print("\u001b[2K\r")
        print("\u001b[1A")
        print("\u001b[2K\r")
        width_input = width_cursor
    }
    println("Please enter the height of the grid: ")
    print("\u001b[2K\r")
    height_cursor = width_cursor
    var empty = ""
            if (height_cursor > 9){
                empty = " "
            }
            else{
                empty = "  "
            }
    println("\u001b[48;2;255;255;255m\u001b[38;2;0;0;0m[" + ("-".repeat(height_cursor -5)) + "\u001b[0m" + (" ".repeat(46 - (height_cursor -5))) + empty + height_cursor + " " .repeat(45) + "]\u001b[0m")
    print("\u001b[2K\r")
    print("Use <- and -> to adjust the height, press space to enter manually, and enter to confirm.")
    loop@ while (true) {
        val ch = reader.read()
        if (ch == - 1) break
        when (ch) {
            27 -> {
                val nextChar = reader.read()
                if (nextChar == 91) {
                    when (reader.read()) {
                        68 -> {
                            if (height_cursor > 5) {
                                height_cursor -= 1
                            }
                            else {
                                height_cursor = 5
                            }
                        }
                        67 -> {
                            if (height_cursor < 100) {
                                height_cursor += 1
                            }else {
                                height_cursor = 100
                            }
                        }
                    }
                }
            }
            13 -> {
                height_input = height_cursor
                break@loop
            }
            32 -> {
                height_input = -10
                break@loop
            }
        }
        print("\u001b[2K\r")
        print("\u001b[1A")
        if(height_cursor - 5 < 47) {
            var empty = ""
            if (height_cursor > 9){
                empty = " "
            }
            else{
                empty = "  "
            }
            print("\u001b[2K\r")
            print("\u001b[48;2;255;255;255m\u001b[38;2;0;0;0m[" + ("-".repeat(height_cursor -5)) + "\u001b[0m" + (" ".repeat(46 - (height_cursor -5))) + empty + height_cursor + " " .repeat(45) + "]\u001b[0m")
        } else if( height_cursor - 5 == 47){
            print("\u001b[2K\r")
            print("\u001b[48;2;255;255;255m\u001b[38;2;0;0;0m[" + ("-".repeat(46)) + "-\u001b[0m52" + " " .repeat(45) + "]\u001b[0m")
        }else if( height_cursor - 5 == 48){
            print("\u001b[2K\r")
            print("\u001b[48;2;255;255;255m\u001b[38;2;0;0;0m[" + ("-".repeat(46)) + "-5\u001b[0m3" + " " .repeat(45) + "]\u001b[0m")
        } 
        else if( height_cursor - 5 == 49){
            print("\u001b[2K\r")
            print("\u001b[48;2;255;255;255m\u001b[38;2;0;0;0m[" + ("-".repeat(46)) + "-54\u001b[0m" + " " .repeat(45) + "]\u001b[0m")
        } 
        else if (height_cursor - 5 > 49 && height_cursor - 5 < 95){
            print("\u001b[2K\r")
            print("\u001b[48;2;255;255;255m\u001b[38;2;0;0;0m[" + ("-".repeat(46)) + "-" + height_cursor + "-".repeat(height_cursor - 54) + "\u001b[0m" + (" ".repeat(46 - (height_cursor - 53))) + "]")
        }
        else{
            print("\u001b[2K\r")
            print("\u001b[48;2;255;255;255m\u001b[38;2;0;0;0m[" + ("-".repeat(46)) + "100" + "-".repeat(45) + "]\u001b[0m")
        }
        println()
        print("\u001b[2K\r")
        print("Use <- and -> to adjust the height, press space to enter manually, and enter to confirm.")
    }
    if (height_input == -10) {
        print("\u001b[2K\r")
        print("\u001b[1A")
        print("\u001b[2K\r")
        print("\u001b[1A")
        print("\u001b[2K\r")
        Runtime.getRuntime().exec(arrayOf("sh","-c","stty sane < /dev/tty")).waitFor()
        print("Please enter the height of the grid (manualy): ")
        height_input = readLine()?.toIntOrNull() ?: 10
        Runtime.getRuntime().exec(arrayOf("sh","-c","stty raw -echo < /dev/tty")).waitFor()
        print("\u001b[2K\r")
    }
    else{
        print("\u001b[2K\r")
        print("\u001b[1A")
        print("\u001b[2K\r")
        print("\u001b[1A")
        print("\u001b[2K\r")
        height_input = height_cursor
    }
    val width = width_input ?: 10
    val height = height_input ?: 10
    return Pair(width, height)
}

fun main_menu(){
    // Future implementation for main menu
}