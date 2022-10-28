package com.android.imageprocess.ui.camalb

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.android.imageprocess.R
import com.android.imageprocess.logic.model.Background

class BackgroundAdapter(private val fragment: Fragment,private val backgroundList:List<Background>) :
    RecyclerView.Adapter<BackgroundAdapter.ViewHolder>(){
        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val ivBackground = view.findViewById<ImageView>(R.id.ivBackground)
            val tvBackground = view.findViewById<TextView>(R.id.tvBackground)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.background_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val background = backgroundList[position]
        holder.tvBackground.text = background.backgroundName
        holder.ivBackground.setImageResource(background.backgroundImageId)
    }

    override fun getItemCount() = backgroundList.size

}