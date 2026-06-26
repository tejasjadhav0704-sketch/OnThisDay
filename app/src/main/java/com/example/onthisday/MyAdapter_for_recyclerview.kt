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
        val title = itemView.findViewById<TextView>(R.id.wikiTitle)
        val label = itemView.findViewById<TextView>(R.id.wikiLabel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = context.layoutInflater.inflate(R.layout.custom_layout_for_recyclerview, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = wikiData[position]
        holder.date.text = dates
        holder.year.text = event.year
        holder.description.text = event.description
        
        if (event.wikipedia.isNotEmpty()) {
            val wiki = event.wikipedia[0]
            holder.title.text = wiki.title
            holder.label.text = wiki.wikipedia
            holder.title.visibility = View.VISIBLE
            holder.label.visibility = View.VISIBLE

            holder.label.setOnClickListener {
                val url = wiki.wikipedia
                if (url.isNotEmpty()) {
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                }
            }
        } else {
            holder.title.visibility = View.GONE
            holder.label.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return wikiData.size
    }
}
