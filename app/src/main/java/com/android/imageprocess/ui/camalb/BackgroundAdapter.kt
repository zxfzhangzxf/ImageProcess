package com.android.imageprocess.ui.camalb

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.android.imageprocess.CameraAlbumActivity
import com.android.imageprocess.R
import com.android.imageprocess.logic.model.Background
import com.android.imageprocess.logic.util.showToast

class BackgroundAdapter(private val fragment: Fragment,private val backgroundList:List<Background>) :
    RecyclerView.Adapter<BackgroundAdapter.ViewHolder>(){


    private val colorList = listOf<Int>(R.color.淡黄,R.color.象牙,R.color.粉红,R.color.银灰,R.color.灰色
        ,R.color.淡蓝,R.color.亮蓝,R.color.棕色,R.color.淡紫,R.color.瑰红,R.color.碧绿,R.color.弱绿)

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val ivBackground = view.findViewById<ImageView>(R.id.ivBackground)
            val tvBackground = view.findViewById<TextView>(R.id.tvBackground)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.background_item,parent,false)
        val viewHolder = ViewHolder(view)
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.absoluteAdapterPosition
            val background = backgroundList[position]
            val colorId = colorList[position]
            "切换平台颜色为${background.backgroundName}".showToast()
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val background = backgroundList[position]
        holder.tvBackground.text = background.backgroundName
        holder.ivBackground.setImageResource(background.backgroundImageId)
    }

    override fun getItemCount() = backgroundList.size

}