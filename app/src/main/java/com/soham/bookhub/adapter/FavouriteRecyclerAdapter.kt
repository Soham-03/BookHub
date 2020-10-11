package com.soham.bookhub.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.soham.bookhub.R
import com.soham.bookhub.activity.BookDescription
import com.soham.bookhub.database.BookEntity
import com.soham.bookhub.model.Book
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_favorite_single_row.view.*

class FavouriteRecyclerAdapter(val context:Context,val bookList:List<BookEntity>):
    RecyclerView.Adapter<FavouriteRecyclerAdapter.FavouriteViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.recycler_favorite_single_row,parent,false)

        return FavouriteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {

        val book=bookList[position]
        holder.txtBookName.text=book.bookName
        holder.txtNameOfAuthor.text=book.bookAuthor
        holder.txtBookPrice.text=book.bookPrice
        holder.txtBookRating.text=book.bookRating
        Picasso.get().load(book.bookImage).error(R.drawable.bookdashboard).into(holder.imgBookImaage)
        holder.favcontent.setOnClickListener {
            val intent=Intent(context,BookDescription::class.java)
            context.startActivity(intent)
        }
    }
    class FavouriteViewHolder (view:View):RecyclerView.ViewHolder(view){
        val txtBookName:TextView=view.findViewById(R.id.txtFavBookTitle)
        val txtNameOfAuthor:TextView=view.findViewById(R.id.txtFavBookAuthor)
        val txtBookPrice:TextView=view.findViewById(R.id.txtFavBookPrice)
        val txtBookRating:TextView=view.findViewById(R.id.txtFavBookRating)
        val imgBookImaage:ImageView=view.findViewById(R.id.imgFavBookImage)
        val favcontent:LinearLayout=view.findViewById(R.id.llFavContent)

    }
}