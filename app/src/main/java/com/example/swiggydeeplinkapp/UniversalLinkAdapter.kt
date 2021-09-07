package com.example.swiggydeeplinkapp

import android.content.Context
import android.content.Intent
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.swiggydeeplinkapp.Model.DeepLinks

class UniversalLinkAdapter(private var universalLinkCategoryList:ArrayList<String>,private var universalLinkDeepLinkList:HashMap<Int,ArrayList<DeepLinks>>):RecyclerView.Adapter<UniversalLinkAdapter.ViewHolder>() {

    private lateinit var context:Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.outer_universal_link_recycler_view,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = universalLinkCategoryList[position]
        holder.title1.text = post
        val innerAdapter = universalLinkDeepLinkList.get(position)?.let { UniversalInnerAdapter(it,post) }
        val layoutManager = LinearLayoutManager(context)
        holder.recyclerView.layoutManager = layoutManager
        holder.recyclerView.adapter = innerAdapter

    }

    override fun getItemCount(): Int = universalLinkCategoryList.size

    inner class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        var title1:TextView = itemView.findViewById(R.id.title1)
        var recyclerView:RecyclerView = itemView.findViewById(R.id.recyclerView_universal_deeplink_data)

    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}

class UniversalInnerAdapter(private var deeplink:ArrayList<DeepLinks>,private var header:String):RecyclerView.Adapter<UniversalInnerAdapter.ViewHolder>(){
    private lateinit var context:Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.universallink_recycler_each_row,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = deeplink[position]
        holder.subtitle.text = post.title
        val content = SpannableString(post.link)
        content.setSpan(UnderlineSpan(),0,post.link.length,0)
        holder.link.isClickable = true
        holder.link.setMovementMethod(LinkMovementMethod.getInstance())
        holder.link.text = content
        holder.link.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(holder.link.text.toString()))
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            }
        }
        holder.launch.setOnClickListener{

        }
    }

    override fun getItemCount(): Int = deeplink.size

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var link: TextView = itemView.findViewById(R.id.UniversaltextViewlink)
        var subtitle: TextView = itemView.findViewById(R.id.UniversaltextViewtitle)
        var launch:Button = itemView.findViewById(R.id.UniversalbuttonLaunch)
        var arrow:Button = itemView.findViewById(R.id.UniversalbuttonArrow)
    }

}


