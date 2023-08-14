package app.slyworks.coursecorrect

import app.slyworks.coursecorrect.models.QuestionEntity


object Repository {
    private lateinit var username:String
    fun setUsername(username:String){ this.username = username }
    fun getUsername():String = username
}