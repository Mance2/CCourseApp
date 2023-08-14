package app.slyworks.coursecorrect.courses

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import app.slyworks.coursecorrect.base.BaseViewModel
import app.slyworks.coursecorrect.models.CourseEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 *Created by Joshua Sylvanus, 11:24 PM, 12-Apr-23.
 */

sealed class CourseUIState {
    object Default : CourseUIState()
    object LoadingStopped : CourseUIState()
    data class CourseList(val list:List<CourseEntity>) : CourseUIState()
}

class CourseViewModel : BaseViewModel() {
    private val coursesList:List<CourseEntity> = listOf(
        CourseEntity(
            "Chemistry",
            "10",
                    "Halogens are any of the six nonmetallic elements that constitute Group 17 (Group VIIa) of the periodic table. The halogen elements are fluorine (F), chlorine (Cl), bromine (Br), iodine (I), astatine (At), and tennessine (Ts).\n" +
                    "\n" +
                    "Halogen elements exhibit great resemblances to each other in their general chemical behaviour and in the properties of their compounds with other elements. However, there is a progressive change in properties from fluorine through chlorine, bromine, and iodine to astatine.\n" +
                    "\n" +
                    "The halogen elements show great resemblances to one another in their general chemical behaviour and in the properties of their compounds with other elements.\n" +
                    "\n" +
                    "Fluorine is the most reactive of the halogens and, of all elements, it has certain other \n" +
                    "\n" +
                    "properties that distinguishes it from other halogens.\n" +
                    "\n" +
                    "Probably, the most important generalization that can be made about the halogen elements is that they are all oxidizing agents.\n" +
                    "\n" +
                    "Halogens can combine with other elements to form compounds known as halides, namely; fluorides, chlorides, bromides, iodides, and astatides.\n")
    )

    private val _uiStateLD: MutableLiveData<CourseUIState> = MutableLiveData(CourseUIState.Default)
    val uiStateLD:LiveData<CourseUIState> = _uiStateLD


    fun resetUIState(){ _uiStateLD.value = CourseUIState.Default }

    fun getCourses(){
        viewModelScope.launch {
            delay(1500)
            _uiStateLD.setValue(CourseUIState.LoadingStopped)
            _uiStateLD.setValue(CourseUIState.CourseList(coursesList))
        }
    }
}