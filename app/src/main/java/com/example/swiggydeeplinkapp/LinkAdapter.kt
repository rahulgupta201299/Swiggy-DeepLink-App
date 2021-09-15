package com.example.swiggydeeplinkapp

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.Intent.*
import android.net.Uri
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.swiggydeeplinkapp.Model.DeepLinks
import java.util.*
import kotlin.collections.ArrayList


class LinkAdapter(private var appLinkCategoryList:ArrayList<String>,private var appLinkDeepLinkList:HashMap<Int,ArrayList<DeepLinks>>):RecyclerView.Adapter<LinkAdapter.ViewHolder>() {

    private lateinit var context:Context
    var innerAdapter = InnerAdapter(ArrayList())
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.outer_applink_recycler_view,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = appLinkCategoryList[position]
        holder.title1.text = post
        innerAdapter = appLinkDeepLinkList.get(position)?.let { InnerAdapter(it) }!!
        val layoutManager = LinearLayoutManager(context)

        holder.recyclerView.addItemDecoration(
            DividerItemDecoration(
                holder.recyclerView.context,
                DividerItemDecoration.VERTICAL
            )
        )
        holder.recyclerView.layoutManager = layoutManager
        holder.recyclerView.adapter = innerAdapter
    }

    override fun getItemCount(): Int = appLinkCategoryList.size

    inner class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        var title1:TextView = itemView.findViewById(R.id.title1)
        var recyclerView:RecyclerView = itemView.findViewById(R.id.recyclerView_app_deeplink_data)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

}

class InnerAdapter(private var deeplink:ArrayList<DeepLinks>):RecyclerView.Adapter<InnerAdapter.ViewHolder>(){
    private lateinit var context:Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.applink_recycler_each_row,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = deeplink[position]
        holder.subtitle.text = post.title
        val content = SpannableString(post.link)
        post.link.let { content.setSpan(UnderlineSpan(),0, it.length,0) }
        holder.link.isClickable = true
        holder.link.setMovementMethod(LinkMovementMethod.getInstance())
        holder.link.text = content
        holder.link.setOnClickListener{
            try{
                val intent = Intent(ACTION_VIEW, Uri.parse(holder.link.text.toString()))
                intent.addCategory(CATEGORY_BROWSABLE)
                context.startActivity(intent)
            }catch (e:ActivityNotFoundException){
                Log.d("Error","Failed to open links")
            }

        }
        holder.launch.setOnClickListener{
            val intent = Intent(context,EditActivity::class.java)
            intent.putExtra("link","${holder.link.text}")
            intent.putExtra("title","${holder.subtitle.text}")
            context.startActivity(intent)
        }
        holder.arrow.setOnClickListener{
            val intent = Intent(context,EditActivity::class.java)
            intent.putExtra("link","${holder.link.text}")
            intent.putExtra("title","${holder.subtitle.text}")
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = deeplink.size

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var link: TextView = itemView.findViewById(R.id.ApptextViewlink)
        var subtitle: TextView = itemView.findViewById(R.id.ApptextViewtitle)
        var launch:Button = itemView.findViewById(R.id.AppbuttonLaunch)
        var arrow:Button = itemView.findViewById(R.id.AppbuttonArrow)
    }

}


