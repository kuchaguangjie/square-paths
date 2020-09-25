package search

import util.SquareCache

// graph - via adjacency list,
class SquareGraph(val number: Int, squareCache: SquareCache = SquareCache(2 * number)) {

    private val edges: Array<IntArray>

    init {
        val squares = (1 until 2 * number).filter(squareCache::contains) // find all possible squares for given number range,
        // build-up adjacency list,
        edges = Array(number) { index ->
            val startNode = index + 1
            val neighbors = mutableListOf<Int>()
            squares
                    .filter { it != startNode }
                    .forEach { s ->
                        val endNode = s - startNode
                        if (endNode in 1..number) neighbors += endNode
                    }
            neighbors.sortedDescending().toIntArray()
        }
    }

    fun getNeighbors(vertex: Int): IntArray = edges[vertex - 1]

    fun degree(vertex: Int): Int = edges[vertex - 1].size
}
