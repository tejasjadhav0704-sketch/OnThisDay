package com.example.onthisday

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface wiki_interface {
    @GET("{month}/{day}/events.json")
    fun getWikiData(@Path("month") month: Int?, @Path("day") day: Int?)  :  Call<wiki_data>
}