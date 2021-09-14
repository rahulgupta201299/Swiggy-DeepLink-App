package com.example.swiggydeeplinkapp

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.swiggydeeplinkapp.Model.DeepLinks
import com.example.swiggydeeplinkapp.Model.FirebaseObject
import com.google.firebase.database.*

class AppLinkFragment(private val BottomNavName:String):Fragment() {
    private lateinit var recyclerView:RecyclerView
    private lateinit var database:FirebaseDatabase
    private lateinit var reference:DatabaseReference
    private lateinit var header:ArrayList<String>
    private lateinit var final:HashMap<Int,ArrayList<DeepLinks>>
    private lateinit var item:MenuItem
    private lateinit var searchView:SearchView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_app_link,container,false)
        setHasOptionsMenu(true)
        retainInstance = true
        recyclerView = view.findViewById(R.id.recyclerView_app_link)
        //val dividerItemDecoration = DividerItemDecoration(context,0)
        //recyclerView.addItemDecoration(dividerItemDecoration)
        database = FirebaseObject.database
        reference = database.getReference("parentData").child(BottomNavName).child("data")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                header = ArrayList()
                var z=0
                final = hashMapOf()
                for(e in dataSnapshot.children){
                    val head = e.child("header").child("title").value.toString()
                    header.add(head)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate((R.menu.main_menu),menu)
        item = menu.findItem(R.id.action_search)
        searchView = item.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {

                var search = HashMap<Int,ArrayList<DeepLinks>>()
                var z = 0
                for(i in final) {
                    var deeplink = ArrayList<DeepLinks>()
                    for(j in i.value){
                        if(j.title.contains(query!!,ignoreCase = true)){
                            deeplink.add(j)
                        }
                    }
                    search[z] = deeplink
                    z++
                }

                initRecyclerView(header,search)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

        super.onCreateOptionsMenu(menu, inflater)
    }

}