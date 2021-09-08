package com.example.swiggydeeplinkapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.swiggydeeplinkapp.Model.DeepLinks
import com.example.swiggydeeplinkapp.Model.FirebaseObject
import com.google.firebase.database.*

class AppLinkFragment:Fragment() {
    private lateinit var recyclerView:RecyclerView
    private lateinit var database:FirebaseDatabase
    private lateinit var reference:DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_app_link,container,false)
        database = FirebaseObject.database
        reference = database.getReference("AppLinkJSON").child("data")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                recyclerView = view.findViewById(R.id.recyclerView_app_link)

                val header = ArrayList<String>()
                var z=0
                val final = hashMapOf<Int,ArrayList<DeepLinks>>()
                for(e in dataSnapshot.children){
                    header.add(e.child("header").child("title").value.toString())
                    var deep = e.child("deeplinks")
                    var deeplink = ArrayList<DeepLinks>()
                    for(i in deep.children){
                        var link = i.child("link").value.toString()
                        var title = i.child("title").value.toString()
                        var editable = i.child("editable").value
                        deeplink.add(DeepLinks(title,link,editable as Boolean))
                    }
                    final[z] = deeplink
                    z++
                }
                initRecyclerView(header,final)
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("rahul", "Failed to read value.", error.toException())
            }
        })

        return view
    }

    private fun initRecyclerView(header: ArrayList<String>,deeplink:HashMap<Int,ArrayList<DeepLinks>>){
        recyclerView.layoutManager = LinearLayoutManager(activity)
        val adapter = AppLinkAdapter(header,deeplink)
        recyclerView.adapter = adapter
    }

}