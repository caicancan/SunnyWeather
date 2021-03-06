package com.sunnyweather.android.logic.model

import com.google.gson.annotations.SerializedName
import javax.xml.transform.Result

data class RealtimeRespons(val status:String,val result:Result){
    data class Result(val realtime:Realtime)
    data class Realtime(val skycon:String,val temperature:Float,@SerializedName("air_quality") val airquality:AirQuality)
    data class AirQuality(val aqi:AQI)
    data class AQI(val chn :Float)
}
