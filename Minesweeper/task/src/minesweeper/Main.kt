package minesweeper

fun main() {
    val SIZE = 9
    val field = Field(SIZE, SIZE)

    print("How many mines do you want on the field? ")
    field.mineNumber = readln().toInt()
    field.print()

    while (true) {
        print("Set/unset mines marks or claim a cell as free: ")
        val inputString = readln().split(" ")
        try {
            val x = inputString[0].toInt()
            val y = inputString[1].toInt()
            val word = inputString[2]
            if (word == "free") field.openSell(x - 1, y - 1)
            if (word == "mine") field.setMine(x - 1, y - 1)
        } catch (e: Exception) {
            println("Catch!")
            continue
        }

        field.print()

        if (field.allMinesMarked() || field.allFreeOpened()) {
            println("Congratulations! You found all the mines!")
            break
        }
    }
}

