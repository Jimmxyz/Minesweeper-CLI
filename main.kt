// TEST will be removed
fun main() {
    println("Kotlin loaded")
    print("Please enter some input: ")
    val input = readLine() ?: ""
    for (i in 1..5){
        print("\u001b[2K\r")
        print("You entered: $i")
        Thread.sleep(1000)
    }
    println("\nyou entered: $input")
}