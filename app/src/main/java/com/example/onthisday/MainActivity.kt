package com.example.onthisday

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
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
import androidx.appcompat.app.AlertDialog
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
    private lateinit var progressBar: ProgressBar
    private lateinit var emptyText: TextView

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
        toolbar.overflowIcon?.setTint(Color.WHITE)

        toolbar.setNavigationOnClickListener {
            AlertDialog.Builder(this)
                .setTitle(R.string.app_name)
                .setMessage("Do u want to Exit ?")
                .setPositiveButton("Yes"){ _, which ->
                    finish()
                }
                .setNegativeButton("No"){_, which ->
                }
                .setIcon(R.drawable.outline_exit_to_app_24)
                .show()

        }

        val textView = findViewById<TextView>(R.id.textView_id)
        val textViewData = findViewById<TextView>(R.id.textViewData)
        emptyText = findViewById<TextView>(R.id.emptyText)
        progressBar = findViewById<ProgressBar>(R.id.progressBar)

        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)

        textViewData.text = "Today's Date : $day / ${month + 1} / $year"

        getWikiData(day, month)

        textView.setOnClickListener {
            DatePickerDialog(this, { _, _, selectedMonth, selectedDay ->
                textViewData.text = "Selected Date : $selectedDay / ${selectedMonth + 1}"
                getWikiData(selectedDay, selectedMonth)
            }, year, month, day).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.tool_bar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.profile_id, R.id.about_id -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/tejasjadhav0704-sketch")))

            R.id.about_id -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/tejasjadhav0704-sketch")))

            R.id.logout_id -> {
                AlertDialog.Builder(this)
                    .setTitle(R.string.app_name)
                    .setMessage("Do u want to Log-out ?")
                    .setPositiveButton("Yes"){ _, which ->
                        FireBase_Instance.auth.signOut()
                        val intent = Intent(this, Login_Activity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    }
                    .setNegativeButton("No"){_, which ->
                    }
                    .setIcon(R.drawable.outline_exit_to_app_24)
                    .show()
            }
        }
        return true
    }

    private fun getWikiData(day: Int?, month: Int?) {
        progressBar.visibility = View.VISIBLE
        val retrofit = Retrofit.Builder()
            .baseUrl("https://byabbe.se/on-this-day/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(wiki_interface::class.java)

        val retroData = retrofit.getWikiData(month?.plus(1), day)

        retroData.enqueue(object : retrofit2.Callback<wiki_data?> {
            override fun onResponse(call: Call<wiki_data?>, response: Response<wiki_data?>) {
                progressBar.visibility = View.GONE
                val responseBody = response.body()
                val recyclerView = findViewById<RecyclerView>(R.id.recycleView_id)
                recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)

                if (response.isSuccessful && responseBody != null) {
                    val events = responseBody.events
                    if (events.isEmpty()) {
                        emptyText.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE
                    } else {
                        emptyText.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE
                        recyclerView.adapter = MyAdapter_for_recyclerview(this@MainActivity, events, responseBody.date)
                    }
                } else {
                    emptyText.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                    Log.e("MainActivity", "Error code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<wiki_data?>, t: Throwable) {
                progressBar.visibility = View.GONE
                Log.e("MainActivity", "onFailure: ${t.message}")
                Toast.makeText(this@MainActivity, "Check Internet Connection", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
