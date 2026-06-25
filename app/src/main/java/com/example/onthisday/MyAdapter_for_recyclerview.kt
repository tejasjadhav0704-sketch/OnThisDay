package com.example.onthisday

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter_for_recyclerview(val context: Activity, val wikiData: List<Event>, val dates: String?) :
    RecyclerView.Adapter<MyAdapter_for_recyclerview.MyViewHolder>()
{
    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
         val date  = itemView.findViewById<TextView>(R.id.dateText)
        val year = itemView.findViewById<TextView>(R.id.yearText)
        val description = itemView.findViewById<TextView>(R.id.descriptionText)

        val Title = itemView.findViewById<TextView>(R.id.wikiTitle)

        val Label = itemView.findViewById<TextView>(R.id.wikiLabel)
     }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyAdapter_for_recyclerview.MyViewHolder {
        val view = context.layoutInflater.inflate(R.layout.custom_layout_for_recyclerview,p0,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(p0: MyAdapter_for_recyclerview.MyViewHolder, p1: Int) {
        p0.date.text = dates
        p0.year.text = wikiData[p1].year
        p0.description.text = wikiData[p1].description
        p0.Title.text = wikiData[p1].wikipedia[0].title
        p0.Label.text = wikiData[p1].wikipedia[0].wikipedia

        p0.Label.setOnClickListener {
            val url = wikiData[p1].wikipedia.getOrNull(0)?.wikipedia
            if (url != null) {
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            }        }
    }

    override fun getItemCount(): Int {
        return wikiData.size
    }

}