package minesweeper

import kotlin.system.exitProcess

class Field(private val width: Int, private val height: Int) {

    private val hiddenField = MutableList(height) { MutableList(width){"/"} }
    private val gameField = MutableList(height) { MutableList(width){"."} }
    private var mineSet = mutableSetOf<Int>()
    var mineNumber = 0
    var firstTurn = true

    private fun fillMines(number: Int) {
        mineSet.clear()
        while (mineSet.size < mineNumber) mineSet.add((0 until width * height - 1).random())
    }

    private fun fillNumbers() {
        mineSet.forEach { hiddenField[it / width][it % width] = "X" }
        for (y in 0 until height) {
            for (x in 0 until width) {
                if (hiddenField[y][x] == "X") continue
                var count = 0
                for (row in y - 1 .. y + 1) {
                    for (column in x - 1 .. x + 1) {
                        if (row !in hiddenField.indices || column !in hiddenField[0].indices) continue
                        if (hiddenField[row][column] == "X") count++
                    }
                }
                if (count > 0) hiddenField[y][x] = count.toString()
            }
        }
    }

    fun print(field: MutableList<MutableList<String>> = gameField) {
        println(" |${field[0].indices.map { it + 1 }.joinToString("")}|")
        println("—|${"—".repeat(width)}|")
        field.indices.forEach { println("${it + 1}|${field[it].joinToString("")}|") }
        println("—|${"—".repeat(width)}|")
    }

    fun setMine(x: Int, y: Int) {
        val symbol = gameField[y][x]
        if (symbol == ".") gameField[y][x] = "*"
        if (symbol == "*") gameField[y][x] = "."
    }

    fun allMinesMarked(): Boolean {
        if (firstTurn) return false
        mineSet.forEach {
            if (gameField[it / width][it % width] != "*") return false
        }
        var count = 0
        gameField.forEach { row ->
            row.forEach { if (it == "*") count++ }
        }
        return count == mineSet.size
    }

    fun openSell(x: Int, y: Int) {
        if (firstTurn) {
            do fillMines(mineNumber) while (y * width + x in mineSet)
            fillNumbers()
            firstTurn = false
        }
        if (hiddenField[y][x] == "X") {
            mineSet.forEach { gameField[it / width][it % width] = "X" }
            print()
            println("You stepped on a mine and failed!")
            exitProcess(0)
        }
        gameField[y][x] = hiddenField[y][x]
        if (gameField[y][x] == "/") {
            val sellList = mutableListOf(y * width + x)
            var i = 0
            while (true) {
                if (i > sellList.size - 1) break
                for (row in sellList[i] / width - 1 .. sellList[i] / width + 1) {
                    for (column in sellList[i] % width - 1 .. sellList[i] % width + 1) {
                        if (row !in hiddenField.indices || column !in hiddenField[0].indices) continue
                        if (row * width + column in sellList) continue
                        gameField[row][column] = hiddenField[row][column]
                        if (gameField[row][column] == "/") sellList.add(row * width + column)
                    }
                }
                i++
            }
        }
    }

    fun allFreeOpened(): Boolean {
        for (y in hiddenField.indices) {
            for (x in hiddenField[0].indices)
                if (hiddenField[x][y] == "/" && gameField[x][y] != "/") return false
        }
        return true
    }
}