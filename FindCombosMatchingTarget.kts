/**
 * A mark-up algorithm that finds all possible combinations of elements that will add up to a given total.
 *
 * Image there is a stick with a length of T, which is the given total. And you're going to make a lot of marks on this stick.
 *
 * Iterating through the list of numbers, and for each number x:
 * 1. For every mark that is already on the stick, make yet another mark that is further by x (approaching point T)
 * 2. Also make a mark at point x
 * 3. If a new mark is about to be beyond point T, no need to make that mark
 * 4. On each mark point you'll record which number(s) has directly led to making that mark. E.g. 1 + 2 + 3 leads to mark 6 and we only need to record 3 to that mark.
 *
 * If there is a mark made on point T that means there is at least a combination that will sum up to T
 * And if that happens, we'd backtrack from the mark at point T, all the way back to point 0.
 */
fun findCombosMatchingTarget(target: Int, elements: List<Int>): List<List<Int>> {
    val marks = HashMap<Int, ArrayList<Int>>()
    fun addMark(valuePoint: Int, index: Int) {
        val l = marks[valuePoint] ?: ArrayList()
        l.add(index)
        marks[valuePoint] = l
    }
    elements.forEachIndexed { index, e ->
        for (k in marks.keys.toHashSet()) {
            if (k + e <= target) {
                addMark(k + e, index)
            }
        }
        addMark(e, index)
    }

    val combinations = mutableListOf<List<Int>>()
    val ongoingBacktraces = mutableListOf<MutableList<Int>>()
    marks[target]?.forEach {
        ongoingBacktraces.add(mutableListOf(it))
    }
    while (true) {
        ongoingBacktraces.toList().forEach { backtrace ->
            val priorMarkPoint = target - backtrace.sumOf { elements[it] }
            if (priorMarkPoint == 0) {
                combinations.add(backtrace)
                ongoingBacktraces.remove(backtrace)
                return@forEach
            }
            val indicesAtPriorMarkPoint = requireNotNull(marks[priorMarkPoint])
            val clonedTrace = backtrace.toMutableList()
            indicesAtPriorMarkPoint.forEachIndexed { index, i ->
                if (index == 0) {
                    backtrace.add(0, i)
                } else if (i < clonedTrace.first()) { // The indices for a combo will be in ascending order
                    ongoingBacktraces.add(clonedTrace.toMutableList().also {
                        it.add(0, i)
                    })
                }
            }
        }
        if (ongoingBacktraces.isEmpty()) {
            break
        }
    }

    return combinations
}

println(findCombosMatchingTarget(100, listOf(
    8, 91, 9, 10, 1, 99, 1, 103
)))
// Will output [[1, 2], [4, 5], [5, 6], [0, 1, 4], [0, 1, 6]]

