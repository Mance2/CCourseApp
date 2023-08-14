package app.slyworks.coursecorrect.quiz

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import app.slyworks.coursecorrect.models.QuestionEntity
import app.slyworks.coursecorrect.base.BaseViewModel
import app.slyworks.coursecorrect.base.utils.NOT_ANSWERED
import app.slyworks.coursecorrect.models.QuizResultEntity


/**
 * Created by Joshua Sylvanus, 6:05 AM, 12-Apr-23.
 */

sealed class QuizUIState{
    object Default : QuizUIState()
    data class NextQuestion(val currentProgress:Int, val currentQuestionString:String) : QuizUIState()
    object CountDownFinished : QuizUIState()
    object QuizFinished : QuizUIState()
    data class QuizResults(val result:QuizResultEntity) : QuizUIState()
}

class QuizViewModel : BaseViewModel() {
    private val questionList:List<QuestionEntity> =
        listOf(
            QuestionEntity(
                "Halogens constitute what group in the periodic table",
                listOf("14","17","19","21"),
                0 ),
            QuestionEntity(
                "Which of the following elements is not a halogen",
                listOf("Chlorine","Iodine","Bromine","Magnessium"),
                1 ),
            QuestionEntity(
                "The most reactive halogen element is",
                listOf("Flourine","Sodium","Tennessine","Astatine"),
                1 ),
            QuestionEntity(
                "Which of the following options is a halide",
                listOf("Astatides","Lithium","Helides","Bolium"),
                0 ),
            QuestionEntity(
                "Halogens do not exhibit resemblance to each other in the properties of their compounds with other elements.",
                listOf("True","False"),
                0 ),
            QuestionEntity(
                "Halogen elements show progressive change in properties.",
                listOf("True","False"),
                0 ),
            QuestionEntity(
                "Flourine has properties that distinguishes it from other elements.",
                listOf("True","False"),
                0 ),
            QuestionEntity(
                "The most important generalization that can be made about halogen elements is that they are oxidizing agents.",
                listOf("True","False"),
                0 ),
            QuestionEntity(
                "Halogen elements show great resemblances to one another in their general physical behaviour and in the properties of their compounds with other elements.",
                listOf("True","False"),
                0 ),
            QuestionEntity(
                "Halogens can combine with other elements to form compounds known as halides.",
                listOf("True","False"),
                0 ),
        )

    private val _uiStateLD:MutableLiveData<QuizUIState> = MutableLiveData(QuizUIState.Default)
    val uiStateLD:LiveData<QuizUIState> = _uiStateLD

    private val _countDownLD:MutableLiveData<String> = MutableLiveData("5:00")
    val countDownLD:LiveData<String> = _countDownLD

    private var countDownTimerMins:Long = 5
    private var countDownTimerSecs:Long = 0
    private var currentTimer:CountDownTimer? = null


    private var currentProgress:Int = 0
    private var currentQuestionIndex:Int = 0

    private val progressPercent:Int
    get(){
        return (questionList.size / 10) * 10
    }

    fun resetUIState(){ _uiStateLD.value = QuizUIState.Default }

    fun getCurrentProgress():Int{
        currentProgress += progressPercent
        return currentProgress
    }

    fun initQuiz(){
        if(currentQuestionIndex == questionList.size - 1){
            _uiStateLD.setValue(QuizUIState.QuizFinished)
            return
        }

        currentProgress += progressPercent
        _uiStateLD.setValue(
            QuizUIState.NextQuestion(
                currentProgress,
                "${currentQuestionIndex}/${questionList.size}" ) )

        countDownTimerMins = 5
        countDownTimerSecs = 0
        _countDownLD.setValue("5:00")
        currentTimer?.cancel()
        currentTimer = QuizCountDownTimer().start()
    }

    fun getCurrentQuestion():QuestionEntity = questionList[currentQuestionIndex++]

    fun setAnswerForCurrentQuestion(answerIndex:Int){
        questionList[currentQuestionIndex].userAnswerIndex = answerIndex
    }

    fun hasCurrentQuestionBeenAnswered():Boolean =
        questionList[currentQuestionIndex].userAnswerIndex != NOT_ANSWERED

    fun getQuizResults(){
        val totalQuestions:Int = questionList.size
        var correctCount:Int = 0
        val l:MutableList<QuestionEntity> = mutableListOf()
        questionList.forEach {
            if(!it.isCorrect)
                l.add(it)
            else
                ++correctCount
        }

        val r:QuizResultEntity = QuizResultEntity(correctCount,totalQuestions, l)
        _uiStateLD.setValue(QuizUIState.QuizResults(r))
    }

    override fun onCleared() {
        super.onCleared()
        currentTimer?.cancel()
        currentTimer = null
    }

    inner class QuizCountDownTimer : CountDownTimer((5 * 60 * 1_000),1_000){
        override fun onTick(p0: Long) {
            val mins:String
            var secs:String
            if(countDownTimerSecs == 0L) {
                countDownTimerSecs = 59
                mins = "${--countDownTimerMins}"
                secs = "$countDownTimerSecs"
            }else {
               mins = "$countDownTimerMins"
               secs = "${--countDownTimerSecs}"
                if(countDownTimerSecs < 10)
                    secs = "0$countDownTimerSecs"
            }
            _countDownLD.postValue("$mins:$secs")
        }

        override fun onFinish() {
            _uiStateLD.postValue(QuizUIState.CountDownFinished)
        }
    }
}