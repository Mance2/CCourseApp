package app.slyworks.coursecorrect.models

import app.slyworks.coursecorrect.base.utils.NOT_ANSWERED

data class QuestionEntity(val question:String,
                          val options:List<String>,
                          val answerIndex:Int,
                          var userAnswerIndex:Int = NOT_ANSWERED){
    val isCorrect:Boolean
    get() = userAnswerIndex == answerIndex
}