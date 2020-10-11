package com.soham.bookhub.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.soham.bookhub.R
import com.soham.bookhub.activity.BookDescription
import com.soham.bookhub.activity.MainActivity
import com.soham.bookhub.model.Book
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_dashboard_single_row.view.*

class ReyclerDashboardAdapter(val context:Context,val itemList:ArrayList<Book>):RecyclerView.Adapter<ReyclerDashboardAdapter.DashboardViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.recycler_dashboard_single_row,parent,false)
        return DashboardViewHolder(view)
    }
    
    override fun getItemCount(): Int {
       return itemList.size
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        val book=itemList[position]
        holder.bookName.text=book.bookName
        holder.authorName.text=book.bookAuthor
        holder.bookPrice.text=book.bookPrice
        holder.rating.text=book.bookRating
        Picasso.get().load(book.bookImage).error(R.drawable.bookdashboard).into(holder.imgbookImage)
        holder.dashboardContent.setOnClickListener {
            val intent=Intent(context,BookDescription::class.java)
            intent.putExtra("book_id",book.bookId)
            context.startActivity(intent)
        }
    }

    class DashboardViewHolder(view: View):RecyclerView.ViewHolder(view){
        val bookName:TextView=view.findViewById(R.id.txtBookName)
        val authorName:TextView=view.findViewById(R.id.txtnameOfAuthor)
        val imgbookImage:ImageView=view.findViewById(R.id.imgBookDashboardLogo)
        val ratingStarLogo:ImageView=view.findViewById(R.id.imgStarRating)
        val rating:TextView=view.findViewById(R.id.txtRating)
        val bookPrice:TextView=view.findViewById(R.id.txtPrice)
        val dashboardContent:RelativeLayout=view.findViewById(R.id.dashboardContent)


    }

}