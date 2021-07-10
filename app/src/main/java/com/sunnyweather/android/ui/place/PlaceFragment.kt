package com.sunnyweather.android.ui.place

import android.opengl.Visibility
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_place.*
import com.sunnyweather.android.R


class PlaceFragment:Fragment(){
    val viewmodel by lazy { ViewModelProviders.of(this).get(PlaceViewModel::class.java) }

    private lateinit var adapter: PlaceAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_place,container,false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val layoutInflater=LinearLayoutManager(activity)
        recyclerView.layoutManager=layoutInflater
        adapter= PlaceAdapter(this,viewmodel.placeList)
        recyclerView.adapter=adapter
        searchPlaceEdit.addTextChangedListener{editable->
            Log.i("ccc","变化了"+editable.toString())
            val content =editable.toString()
            if (content.isNotEmpty()){
                viewmodel.searchPlace(content)
            }else{
                recyclerView.visibility=View.GONE
                bgImageView.visibility=View.VISIBLE
                viewmodel.placeList.clear()
                adapter.notifyDataSetChanged()
            }
        }
        viewmodel.placeLiveData.observe(this, Observer { result->
            Log.i("ccc","搜索"+result);
            val places=result.getOrNull()
            if (places!=null){
                recyclerView.visibility=View.VISIBLE
                bgImageView.visibility=View.GONE
                viewmodel.placeList.clear()
                viewmodel.placeList.addAll(places);
                adapter.notifyDataSetChanged()
            }else{
                Toast.makeText(activity,"未能查询到任何地点",Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        })
    }
}