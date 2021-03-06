package com.sunnyweather.android.logic

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.sunnyweather.android.logic.dao.PlaceDao
import com.sunnyweather.android.logic.model.Place
import com.sunnyweather.android.logic.model.Weather
import com.sunnyweather.android.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.lang.Exception
import java.lang.RuntimeException
import kotlin.coroutines.CoroutineContext

object Repository {

//    fun searchPlaces(query:String)= liveData(Dispatchers.IO){
//        val result=try {
//            val placeResponse=SunnyWeatherNetwork.searchPlaces(query)
//            if (placeResponse.status=="ok"){
//                val places=placeResponse.places
//                Result.success(places)
//            }else{
//                Result.failure(RuntimeException("response status is ${placeResponse.status}"))
//            }
//        }catch (e :Exception){
//            Result.failure<List<Place>>(e);
//        }
//        emit(result)
//    }
fun searchPlaces(query:String)= fire(Dispatchers.IO){
        val placeResponse=SunnyWeatherNetwork.searchPlaces(query)
        if (placeResponse.status=="ok"){
            val places=placeResponse.places
            Result.success(places)
        }else{
            Result.failure(RuntimeException("response status is ${placeResponse.status}"))
        }
}
//    fun refreshWeather(lng:String,lat:String)= liveData(Dispatchers.IO) {
//        val result=try {
//            coroutineScope {
//                val deferredRealtime=async{
//                    SunnyWeatherNetwork.getRealtimeWeather(lng,lat)
//                }
//                val deferredDaily=async {
//                    SunnyWeatherNetwork.getDailyWeather(lng,lat)
//                }
//                val realtimeRespons=deferredRealtime.await()
//                val dailyResponse=deferredDaily.await()
//                if (realtimeRespons.status=="ok"&&dailyResponse.status=="ok"){
//                    val weather= Weather(realtimeRespons.result.realtime,dailyResponse.result.daily)
//                    Result.success(weather)
//                }else{
//                    Result.failure(RuntimeException("realtime response status is ${realtimeRespons.status}"
//                    +"daily response status is ${dailyResponse.status}"))
//                }
//            }
//        }catch (e:Exception){
//            Result.failure<Weather>(e)
//        }
//        emit(result)
//    }

    fun refreshWeather(lng:String,lat:String)= fire(Dispatchers.IO) {
            coroutineScope {
                val deferredRealtime=async{
                    SunnyWeatherNetwork.getRealtimeWeather(lng,lat)
                }
                val deferredDaily=async {
                    SunnyWeatherNetwork.getDailyWeather(lng,lat)
                }
                val realtimeRespons=deferredRealtime.await()
                val dailyResponse=deferredDaily.await()

                if (realtimeRespons.status=="ok"&&dailyResponse.status=="ok"){
                    val weather= Weather(realtimeRespons.result.realtime,dailyResponse.result.daily)
                    Result.success(weather)
                }else{
                    Result.failure(RuntimeException("realtime response status is ${realtimeRespons.status}"
                            +"daily response status is ${dailyResponse.status}"))
                }
            }

    }

    fun savePlace(place: Place)=PlaceDao.savePlace(place)
    fun getSavedPlace()=PlaceDao.getSavePlace()
    fun isPlaceSaved()=PlaceDao.isPlaceSaved()

    private fun<T> fire(context:CoroutineContext,block:suspend()->Result<T>)=liveData<Result<T>>(context){
        val result=try {
            block();
        }catch (e:Exception){
            Result.failure<T>(e)
        }
        emit(result)
    }
}
