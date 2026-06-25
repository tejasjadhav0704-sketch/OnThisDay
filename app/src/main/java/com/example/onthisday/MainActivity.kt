package com.example.onthisday

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    lateinit var progressBar : ProgressBar
    lateinit var emptyText : TextView
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar = findViewById<Toolbar>(R.id.toolBar_id)
        setSupportActionBar(toolbar)

        val textView = findViewById<TextView>(R.id.textView_id)
        val textViewData = findViewById<TextView>(R.id.textViewData)
        emptyText = findViewById<TextView>(R.id.emptyText)
        progressBar = findViewById<ProgressBar>(R.id.progressBar)

        var calender = Calendar.getInstance()
        var day = calender.get(Calendar.DAY_OF_MONTH)
        var month = calender.get(Calendar.MONTH)
        var year = calender.get(Calendar.YEAR)

        progressBar.visibility = View.VISIBLE

        getWikiData(day,month)


        textViewData.setText("Todays Date : $day / ${month+1} / $year")


        textView.setOnClickListener {

            DatePickerDialog(this,{
                _,selectedYear,selectedMonth,selectedDay ->
                textViewData.setText("Selected Date : $selectedDay / ${selectedMonth+1}")
                getWikiData(selectedDay,selectedMonth)
            },year,month,day).show()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.tool_bar_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.profile_id -> startActivity(Intent( Intent.ACTION_VIEW, Uri.parse("https://github.com/tejasjadhav0704-sketch")))
            R.id.about_id -> startActivity(Intent( Intent.ACTION_VIEW, Uri.parse("https://github.com/tejasjadhav0704-sketch")))
        }
        return true
    }

    private fun getWikiData(day: Int?, month: Int?)
    {
        val retroFit = Retrofit.Builder()
            .baseUrl("https://byabbe.se/on-this-day/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(wiki_interface::class.java)

        val retroData = retroFit.getWikiData(month?.plus(1),day)

        retroData.enqueue(object : retrofit2.Callback<wiki_data>{
            override fun onResponse(p0: Call<wiki_data?>?, p1: Response<wiki_data?>?) {
                val responseBody = p1?.body()

                val recycleView = findViewById<RecyclerView>(R.id.recycleView_id)

                progressBar.visibility = View.GONE

                val events = responseBody?.events ?: emptyList()

                if (events.isEmpty()) {
                    emptyText.visibility = View.VISIBLE
                    recycleView.visibility = View.GONE

                } else {

                    emptyText.visibility = View.GONE
                    recycleView.visibility = View.VISIBLE

                    recycleView.adapter = MyAdapter_for_recyclerview(this@MainActivity, events, responseBody?.date)
                }

                recycleView.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL,false)
                recycleView.adapter = MyAdapter_for_recyclerview(this@MainActivity,responseBody!!.events,responseBody.date)


            }

            override fun onFailure(p0: Call<wiki_data?>?, p1: Throwable?) {
                Log.d("Main_Activity", "onFailure: ${p1?.message}")
                Toast.makeText(this@MainActivity, "Check Internet Connection", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
