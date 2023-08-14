package app.slyworks.coursecorrect.base

import java.text.SimpleDateFormat
import java.util.*


/**
 *Created by Joshua Sylvanus, 9:24 AM, 08-Mar-23.
 */
object TimeOfDayHelper {
   fun getTimeOfDay():String{
       val hourSDF:SimpleDateFormat = SimpleDateFormat("HH")

       val c:Calendar = Calendar.getInstance()

       val _hour:String = hourSDF.format(c.time)
       val hour:Int = Integer.valueOf(_hour)

       if(hour in 0..11)
           return "Good Morning"
       if(hour in 12..16)
           return "Good Afternoon"
       if(hour in 17..23)
           return "Good Evening"
       else throw UnsupportedOperationException()
    }
}