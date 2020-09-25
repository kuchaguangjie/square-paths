package util

// verify a path,
class Verifier(private val nodes: List<Int>, private val squareCache: SquareCache) {
    // verify as path,
    fun isHamiltonianPath(length: Int): Boolean {
        if (nodes.size != length) return false
        val allNodes = containsEachNodeOnce()
        val allSquares = (0 until nodes.size - 1).all { v -> squareCache.contains(nodes[v] + nodes[v + 1]) }
        return allNodes && allSquares
    }

    // check each number occur once,
    private fun containsEachNodeOnce(): Boolean {
        val seen = IntArray(nodes.size)
        for (node in nodes) {
            val nodeIdx = node - 1
            seen[nodeIdx] += 1
            if (seen[nodeIdx] > 1) return false
        }
        return true
    }

    // verify as cycle,
    fun isHamiltonianCycle(length: Int): Boolean =
            isHamiltonianPath(length) && squareCache.contains(nodes[0] + nodes[nodes.size - 1])

}
