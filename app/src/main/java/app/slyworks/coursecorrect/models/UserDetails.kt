package app.slyworks.coursecorrect.models


/**
 *Created by Joshua Sylvanus, 1:30 AM, 13-Apr-23.
 */
data class UserDetails(
    val first_name:String = "",
    val last_name:String = "") {

    constructor() : this(first_name = "", last_name = "")
}