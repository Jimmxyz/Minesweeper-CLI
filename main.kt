import kotlin.random.Random
import java.io.InputStreamReader

fun main() {
    println("=== Minesweeper CLI ===")
    print("Please enter the width of the grid: ")
    val width_input = readLine() ?: ""
    print("\u001b[1A")
    print("\u001b[2K\r")
    print("Please enter the height of the grid: ")
    val height_input = readLine() ?: ""
    print("\u001b[1A")
    print("\u001b[2K\r")
    val width = width_input.toIntOrNull() ?: 10
    val height = height_input.toIntOrNull() ?: 10
    var grid = gen_grid(height, width, 20)
    val cursor_x = 0
    val cursor_y = 0
    grid_readdy(height, width)
    val visual_grid = visual_grid_gen(height, width)
    print_grid(visual_grid,height,width,cursor_x,cursor_y)
    //blocking touch
    Runtime.getRuntime().exec(arrayOf("sh","-c","stty raw -echo < /dev/tty")).waitFor()
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

fun print_grid(grid_to_display: Array<Array<Int>>,height: Int = 9, width: Int = 9, cursor_i: Int = 0, cursor_j: Int = 0) {
    //Grid size 10 x 10 (test)
    for (i in 0..height - 1) {
        print("\u001b[1A")
    }
    for (i in 0 until height) {
        print("\u001b[2K\r")
        for (j in 0 until width) {
            //Bck color
            var bck_color = "none"
            if(grid_to_display[i][j] == 0 || grid_to_display[i][j] == 10) {
                if((i % 2 == 0 && j % 2 == 0) || (i % 2 != 0 && j % 2 != 0)) {
                    bck_color = "\u001b[48;2;88;168;88m"
                } else {
                    bck_color = "\u001b[48;2;82;153;82m"
                }
            } else {
                if((i % 2 == 0 && j % 2 == 0) || (i % 2 != 0 && j % 2 != 0)) {
                    bck_color = "\u001b[48;2;231;164;133m"
                } else {
                    bck_color = "\u001b[48;2;214;140;105m"
                }
            }
            // Cursor color
            var cursor_color = "\u001b[38;2;255;255;255m"
            
            // Font color
            var font_color = "none"
            if (grid_to_display[i][j] == 1) {
                font_color = "\u001b[38;2;73;74;210m"
            } 
            else if (grid_to_display[i][j] == 2) {
                font_color = "\u001b[38;2;64;120;67m"
            } 
            else if (grid_to_display[i][j] == 3) {
                font_color = "\u001b[38;2;210;73;73m"
            } 
            else if (grid_to_display[i][j] == 4) {
                font_color = "\u001b[38;2;140;57;161m"
            } 
            else if (grid_to_display[i][j] == 5) {
                font_color = "\u001b[38;2;243;237;38m"
            } 
            else if (grid_to_display[i][j] == 6) {
                font_color = "\u001b[38;2;106;232;226m"
            } 
            else if (grid_to_display[i][j] == 7) {
                font_color = "\u001b[38;2;133;133;131m"
            } 
            else if (grid_to_display[i][j] == 10) {
                font_color = "\u001b[38;2;210;0;0m"
            } 
            else{
                font_color = "\u001b[38;2;0;0;0m"
            }
            // Print the cell
            if(i == cursor_i && j == cursor_j && grid_to_display[i][j] != 0 && grid_to_display[i][j] != 9 && grid_to_display[i][j] != 10) {
                print("\u001B[1m"+ bck_color + cursor_color + "[" +  font_color  + grid_to_display[i][j] + "\u001b[0m" + cursor_color + bck_color + "\u001B[1m]\u001b[0m")
            } else if(i == cursor_i && j == cursor_j && (grid_to_display[i][j] == 0 || grid_to_display[i][j] == 9)) {
                print("\u001B[1m"+bck_color + cursor_color + "[ " + font_color + "\u001b[0m" + cursor_color + bck_color + "\u001B[1m]\u001b[0m")
            } else if(i == cursor_i && j == cursor_j && grid_to_display[i][j] == 10) {
                print("\u001B[1m"+bck_color + cursor_color + "[" + font_color + "F\u001b[0m" + cursor_color + bck_color + "\u001B[1m]\u001b[0m")
            }
            else if (grid_to_display[i][j] == 9 || grid_to_display[i][j] == 0) {
                print("\u001B[1m"+bck_color + font_color + "   " + "\u001b[0m")
            }
            else if (grid_to_display[i][j] == 10) {
                print("\u001B[1m"+bck_color + font_color + " F " + "\u001b[0m")
            }
            else {
                print("\u001B[1m"+bck_color + font_color + " " + grid_to_display[i][j] + " " + "\u001b[0m")
            }
        }
        println()
    }
    print("\u001b[2K\r")
    println("Press Q to quit, arrow to move, enter to select, and space to mark a mine.")
}

fun grid_readdy(height: Int, width: Int) {
    for (i in 0..height -1) {
        for (j in 0..width - 1) {
            print("---")
        }
        println()
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
