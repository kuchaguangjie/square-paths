import extend.CutAndInsertExtender
import extend.Cycle
import extend.GerbiczExtender
import search.Pathfinder
import search.SquareGraph
import util.*

object App {

    @JvmStatic
    fun main(args: Array<String>) {
        val startTime = System.currentTimeMillis()

        extendLoop(1_400, graphSearch(32)!!) // start with known first cycle 32, then extend on it,

        println("time in seconds: ${(System.currentTimeMillis() - startTime) / 1000}")
    }

    // extend base on a start cycle, until given max vertex,
    private fun extendLoop(max_vertex_number: Int, starter: Cycle) {
        println(
                if (starter.isHamiltonian())
                    "starter of size ${starter.maxNumber} verified!"
                else
                    throw RuntimeException("invalid starter of size ${starter.maxNumber}")
        )

        var cycle = starter
        while (cycle.maxNumber < max_vertex_number) {
            cycle = extend(cycle) ?: throw IllegalStateException("no extension found!")
            print("${cycle.maxNumber.toString().padStart(6, '>')} ")
            println(cycle)
            when {
                cycle.isHamiltonian().not() -> throw RuntimeException("verify error for ${cycle.maxNumber}")
                cycle.maxNumber % 10 == 0 -> println()
                cycle.maxNumber % 10_000 == 0 -> persist(cycle)
            }
        }
    }

    // search graph, via brute force, with incremental timeout,
    private fun graphSearch(number: Int): Cycle? {
        val graph = SquareGraph(number)
        var done = false
        var searchDurationMs = 1
        while (!done) { // search in incremental timeout,
            searchDurationMs *= 2
            val solution = Pathfinder(graph).search(searchDurationMs.toLong())  // search path, with timeout,
            val verified = solution.isNotEmpty() && Verifier(solution, squareCache).isHamiltonianCycle(number) // verify path,
            done = verified || searchDurationMs >= 1000 // give up after given max timeout,
            if (done) return Cycle(number, solution.toIntArray()) // tips: this may be empty, on timeout,
        }
        return null
    }

    private fun extend(extendee: Cycle): Cycle? =
            GerbiczExtender.extend(extendee)
                    ?: CutAndInsertExtender.extend(extendee)
                    ?: graphSearch(extendee.maxNumber + 1)

    private fun persist(cycle: Cycle) {
        try {
            writeSolutionToFile("solution_${cycle.maxNumber}.txt", cycle.toList())
        } catch (e: Exception) {
            println(e.message)
        }
    }

}
