package com.soham.bookhub.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.soham.bookhub.R
import com.soham.bookhub.adapter.ReyclerDashboardAdapter
import com.soham.bookhub.model.Book
import com.soham.bookhub.util.ConnectionManager
import org.json.JSONException

class DashboardFragment : Fragment()
{  lateinit var recyclerDashboard:RecyclerView

    lateinit var layoutManager:RecyclerView.LayoutManager

    lateinit var recyclerAdapter:ReyclerDashboardAdapter

    lateinit var progressBarLayout:RelativeLayout

    lateinit var progressBar:ProgressBar

    val bookInfoList= arrayListOf<Book>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_dashboard2, container, false)

        recyclerDashboard= view.findViewById(R.id.recyclerDashboard)
        progressBarLayout=view.findViewById(R.id.progressBarLayout)
        progressBar=view.findViewById(R.id.progressBar)

        progressBarLayout.visibility=View.VISIBLE
        layoutManager=LinearLayoutManager(activity)
        recyclerAdapter= ReyclerDashboardAdapter(activity as Context,bookInfoList)
        recyclerDashboard.adapter=recyclerAdapter
        recyclerDashboard.layoutManager=layoutManager
        recyclerDashboard.addItemDecoration(DividerItemDecoration(
            recyclerDashboard.context,(layoutManager as LinearLayoutManager).orientation
        )
    )
        val queue=Volley.newRequestQueue(activity as Context)
        val url="http://13.235.250.119/v1/book/fetch_books/"

        if(ConnectionManager().checkConnectivity(activity as Context)) {
            val jsonObjectRequest =
                object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
                    val success = it.getBoolean("success")
                    try {
                        progressBarLayout.visibility=View.GONE
                        if (success) {
                            val data = it.getJSONArray("data")
                            for (i in 0 until data.length()) {
                                val bookJsonObject = data.getJSONObject(i)
                                val bookObject = Book(
                                    bookJsonObject.getString("book_id"),
                                    bookJsonObject.getString("name"),
                                    bookJsonObject.getString("author"),
                                    bookJsonObject.getString("rating"),
                                    bookJsonObject.getString("price"),
                                    bookJsonObject.getString("image")
                                )
                                bookInfoList.add(bookObject)
                                recyclerAdapter =
                                    ReyclerDashboardAdapter(activity as Context, bookInfoList)
                                recyclerDashboard.adapter = recyclerAdapter
                                recyclerDashboard.layoutManager = layoutManager

                            }
                        } else {
                            Toast.makeText(
                                activity as Context,
                                "Error occured!!!",
                                Toast.LENGTH_LONG
                            )
                        }
                    }catch (e:JSONException){
                        Toast.makeText(activity as Context,"unexpected Error occurred",Toast.LENGTH_LONG)
                    }
                },
                    Response.ErrorListener {
                        if(activity!=null){
                        Toast.makeText(activity as Context,"Volley error occurred!",Toast.LENGTH_LONG)
                        }
                    }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "2eedeaece46a54"
                        return headers
                    }
                }
            queue.add(jsonObjectRequest)
        }
        else{
            val dialog=AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection not Found")
            dialog.setPositiveButton("Enable Internet"){text,listener ->
                val settingsIntent= Intent(Settings.ACTION_SETTINGS)
                startActivity(settingsIntent)
                activity?.finish()
            }
            dialog.setNegativeButton("cancel"){text,listener->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()

        }
        return view
    }

}
