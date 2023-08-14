package app.slyworks.coursecorrect.models

data class QuizResultEntity(val correctCount:Int,
                            val totalCount:Int,
                            val failedQuestions:List<QuestionEntity>)