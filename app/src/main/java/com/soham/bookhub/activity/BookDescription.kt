package com.soham.bookhub.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.soham.bookhub.R
import com.soham.bookhub.database.BookDatabase
import com.soham.bookhub.database.BookEntity
import com.soham.bookhub.util.ConnectionManager
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_book_description.*
import kotlinx.android.synthetic.main.drawer_header.*
import org.json.JSONObject
import java.lang.Exception

class BookDescription : AppCompatActivity() {
    lateinit var BookDescriptionToolbar:Toolbar
    lateinit var progressBarLayout:RelativeLayout
    lateinit var progressBar:ProgressBar
    lateinit var imgBookDashboardLogo:ImageView
    lateinit var txtBookName:TextView
    lateinit var txtRating:TextView
    lateinit var txtNameOfAuthor:TextView
    lateinit var txtPrice:TextView
    lateinit var description:TextView
    lateinit var imgStarRating:ImageView
    lateinit var btnAddToFav:Button

    var bookId:String?="100"
    var book_id:String?="102"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_description)
        BookDescriptionToolbar=findViewById(R.id.BookDescriptionToolbar)
        setSupportActionBar(BookDescriptionToolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        progressBarLayout=findViewById(R.id.progressBarLayout)
        progressBarLayout.visibility=View.VISIBLE
        progressBar=findViewById(R.id.progressBar)
        progressBar.visibility=View.VISIBLE
        txtBookName=findViewById(R.id.txtBookName)
        txtNameOfAuthor=findViewById(R.id.txtnameOfAuthor)
        txtRating=findViewById(R.id.txtRating)
        txtPrice=findViewById(R.id.txtPrice)
        description=findViewById(R.id.Description)
        imgBookDashboardLogo=findViewById(R.id.imgBookDashboardLogo)
        btnAddToFav=findViewById(R.id.btnAddToFav)
        btnAddToFav.setOnClickListener {
            Toast.makeText(this@BookDescription,"Added to Favourites",Toast.LENGTH_LONG)
        }
        if(intent!=null)
        {
            bookId=intent.getStringExtra("book_id")
        }
        else{
            finish()
            Toast.makeText(this@BookDescription,"Error Occurred",Toast.LENGTH_LONG).show()
        }
        if(bookId=="100"){
            Toast.makeText(this@BookDescription,"Errors Occurred",Toast.LENGTH_LONG).show()
        }

        val queue=Volley.newRequestQueue(this@BookDescription)
        var url="http://13.235.250.119/v1/book/get_book/"

        val jsonParams=JSONObject()
        jsonParams.put("book_id",bookId)

        if(ConnectionManager().checkConnectivity(this@BookDescription)){
            val jsonRequest=object :JsonObjectRequest(Request.Method.POST,url,jsonParams,
                Response.Listener {
                    try{
                        val success=it.getBoolean("success")
                        if(success){
                            val bookJsonObject=it.getJSONObject("book_data")
                            progressBarLayout.visibility=View.GONE
                            progressBar.visibility=View.GONE

                            val bookImageUrl=bookJsonObject.getString("image")
                            Picasso.get().load(bookJsonObject.getString("image")).into(imgBookDashboardLogo)
                            txtBookName.text=bookJsonObject.getString("name")
                            txtNameOfAuthor.text=bookJsonObject.getString("author")
                            txtPrice.text=bookJsonObject.getString("price")
                            txtRating.text=bookJsonObject.getString("rating")
                            description.text=bookJsonObject.getString("description")

                            val bookEntity=BookEntity(
                                bookId?.toInt() as Int,
                                txtBookName.text.toString(),
                                txtNameOfAuthor.text.toString(),
                                txtPrice.text.toString(),
                                txtRating.text.toString(),
                                description.text.toString(),
                                bookImageUrl
                            )

                            val checkFav = DBAsyncTask(applicationContext,bookEntity,1).execute()
                            val isFav = checkFav.get()

                            if(isFav){
                                btnAddToFav.text=getString(R.string.removefav)
                                val favColor=ContextCompat.getColor(applicationContext,R.color.colorAccent)
                                btnAddToFav.setBackgroundColor(favColor)
                            }else{
                                btnAddToFav.text="Add to Favourites"
                                val noFavColor = ContextCompat.getColor(applicationContext,R.color.colorPrimary)
                                btnAddToFav.setBackgroundColor(noFavColor)
                            }

                            btnAddToFav.setOnClickListener {
                                if(!DBAsyncTask(applicationContext,bookEntity,1).execute().get()){
                                    val async = DBAsyncTask(applicationContext,bookEntity,2).execute()
                                    val result = async.get()
                                    if(result){
                                        Toast.makeText(
                                            this@BookDescription,"Book Added to Favourites",Toast.LENGTH_LONG
                                        ).show()

                                        btnAddToFav.text="Remove from Favourites"
                                        val favColor=ContextCompat.getColor(applicationContext,R.color.colorAccent)
                                        btnAddToFav.setBackgroundColor(favColor)
                                    }
                                    else{
                                        Toast.makeText(this@BookDescription,"some unexpected Error occurred",Toast.LENGTH_LONG).show()
                                    }
                                }
                                else{
                                    val async=DBAsyncTask(applicationContext,bookEntity,3).execute()
                                    val result=async.get()

                                    if(result){
                                        Toast.makeText(this@BookDescription,"Book removed from Favourites",Toast.LENGTH_LONG).show()

                                        btnAddToFav.text="Add to Favourites"
                                        val noFavColor=ContextCompat.getColor(applicationContext,R.color.colorPrimary)
                                        btnAddToFav.setBackgroundColor(noFavColor)
                                    }
                                    else{
                                        Toast.makeText(this@BookDescription,"Some unexpected error occurred",Toast.LENGTH_LONG).show()
                                    }
                                }
                            }
                        }
                        else{
                            Toast.makeText(this@BookDescription,"Error Occurred",Toast.LENGTH_LONG).show()
                        }
                    }catch (e:Exception){
                        Toast.makeText(this@BookDescription,"Error Occurred",Toast.LENGTH_LONG).show()
                    }
                },
                Response.ErrorListener {
                    Toast.makeText(this@BookDescription,"Volley Error $it",Toast.LENGTH_LONG).show()
                }){
                override fun getHeaders(): MutableMap<String, String> {
                    val headers=HashMap<String,String>()
                    headers["Content-type"]="application/json"
                    headers["token"]="2eedeaece46a54"
                    return headers
                }
            }
            queue.add(jsonRequest)
        }
        else{
            val dialog= AlertDialog.Builder(this@BookDescription)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection not Found")
            dialog.setPositiveButton("Enable Internet"){text,listener ->
                val settingsIntent= Intent(Settings.ACTION_SETTINGS)
                startActivity(settingsIntent)
                finish()
            }
            dialog.setNegativeButton("cancel"){text,listener->
                ActivityCompat.finishAffinity(this@BookDescription)
            }
            dialog.create()
            dialog.show()
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        this.finish()
        return true
    }

    class DBAsyncTask(val context:Context,val bookEntity:BookEntity,val mode:Int):AsyncTask<Void, Void, Boolean>(){

        /*
        Mode 1 -> Check DB if the book is favourite or not
        Mode 2 -> Save the book into DB as favourite
        Mode 3 -> Remove the favourite book
         */

        val db = Room.databaseBuilder(context,BookDatabase::class.java,"books-db").build()
        override fun doInBackground(vararg params: Void?): Boolean {

            when(mode){
                1 -> {
                    //check DB if book is fav or not
                    val book:BookEntity?=db.bookDao().getBookById(bookEntity.book_id.toString())
                    db.close()
                    return book != null
                }
                2 -> {
                    //save book in DB as fav
                    db.bookDao().insertBook(bookEntity)
                    db.close()
                    return true
                }
                3 -> {
                    //remove book from fav
                    db.bookDao().deleteBook(bookEntity)
                    db.close()
                    return true
                }
            }
            return false
        }

    }
}