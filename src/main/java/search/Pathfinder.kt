package search

import java.time.Instant

// search path, via brute force, with timeout,
class Pathfinder(private val graph: SquareGraph) {
    private lateinit var endTime: Instant

    // search path, return list, or empty list if not found,
    fun search(searchDurationPerVertexInMs: Long): List<Int> {
        for (vertex in 1..graph.number) { // try each vertex as start,
            // we give up for each vertex after some short time period,
            // turns out it is long enough for some vertex eventually
            endTime = Instant.now().plusMillis(searchDurationPerVertexInMs) // end time for current start vertex,

            val path = Path(graph.number)
            path.append(vertex) // add first number,
            if (extend(path)) return path.toList()
        }
        return emptyList() // not found,
    }

    // extend path, return boolean to indicate whether succeed,
    private fun extend(path: Path): Boolean {
        if (path.isHamiltonian && path.canBeClosed()) return true // done
        if (Instant.now().isAfter(endTime)) return false // timeout

        for (neighbor in graph.getNeighbors(path.last())) { // check last node's each neighbor,
            if (path.contains(neighbor)) continue // already used,
            path.append(neighbor) // append,
            if (extend(path)) return true // check recursively, found,
            else path.removeLast() // not found, go back 1 step,
        }
        return false // not found,
    }
}
