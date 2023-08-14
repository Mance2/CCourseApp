package app.slyworks.coursecorrect.base


/**
 * Created by Joshua Sylvanus, 10:08 PM, 18/05/2022.
 */
object ActivityHelper {
    private var count:Int = 0

    fun isLastActivity():Boolean = count == 1
    fun incrementActivityCount():Int = count++
    fun decrementActivityCount():Int = count--
    fun getActivityCount():Int = count
    fun setActivityCount(count:Int){ ActivityHelper.count = count }
}

