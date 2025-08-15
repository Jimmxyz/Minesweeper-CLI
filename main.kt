// TEST will be removed
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
    val grid = gen_grid(height, width, 20)
    val visual_grid = visual_grid_gen(height, width)
    print_grid(visual_grid,height,width)
    //blocking touch
    Runtime.getRuntime().exec(arrayOf("sh","-c","stty raw -echo < /dev/tty")).waitFor()
    loop_main()
    Runtime.getRuntime().exec(arrayOf("sh","-c","stty sane < /dev/tty")).waitFor()
    println()
}

fun gen_grid(cols: Int, rows: Int, luck:Int): Array<Array<Int>> {
    return Array(cols) { Array(rows) { if (Random.nextInt(100) < luck) 1 else 0 } }
}

fun visual_grid_gen(rows: Int, cols: Int): Array<Array<Int>> {
    return Array(rows) { Array(cols) { 0 } }
}

fun print_grid(grid_to_display: Array<Array<Int>>,height: Int = 9, width: Int = 9) {
    //Grid size 10 x 10 (test)
    for (i in 0..height -1) {
        for (j in 0..width - 1) {
            if (grid_to_display[i][j] == 0) {
                print(" - ")
            } else {
                print(" ${grid_to_display[i][j]} ")
            }
        }
        println()
    }
    println("Press Q to quit, arrow to move, enter to select, and space to mark a mine.")
}

fun loop_main(){
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
                        65 -> println("Up arrow pressed") // Up
                        66 -> println("Down arrow pressed") // Down
                        67 -> println("Right arrow pressed") // Right
                        68 -> println("Left arrow pressed") // Left
                    }
                }
            }
            'q'.code, 'Q'.code -> {
                println("Exiting Minesweeper CLI. Goodbye!")
                break@loop
            }
            else -> {
                println("Invalid input. Please try again.")
            }
        }
    }
}