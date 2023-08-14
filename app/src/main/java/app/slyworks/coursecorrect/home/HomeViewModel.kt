package app.slyworks.coursecorrect.home

import app.slyworks.coursecorrect.Repository
import app.slyworks.coursecorrect.base.BaseViewModel
import app.slyworks.coursecorrect.base.TimeOfDayHelper


/**
 * Created by Joshua Sylvanus, 10:11 AM, 12-Apr-23.
 */
class HomeViewModel : BaseViewModel() {
    fun getTimeOfDay():String = TimeOfDayHelper.getTimeOfDay()
    fun getUsername():String = Repository.getUsername()
}