package com.example.swiggydeeplinkapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.FrameLayout
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.swiggydeeplinkapp.Model.DeepLinks
import com.example.swiggydeeplinkapp.Model.FirebaseObject
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.*

class MainActivity : AppCompatActivity(){
    private lateinit var bottomNav:BottomNavigationView
    private lateinit var toolbar:Toolbar
    private lateinit var firebaseDatabase:FirebaseDatabase
    private lateinit var reference:DatabaseReference
    private lateinit var menu:Menu
    private lateinit var ListOfBottomNav:ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ListOfBottomNav = ArrayList()
        toolbar = findViewById(R.id.toolbarMainActivity)
        setSupportActionBar(toolbar)
        toolbar.setTitle(R.string.app_heading)
        bottomNav = findViewById(R.id.bottom_navigation)
        bottomNav.itemIconSize = 80
        firebaseDatabase = FirebaseObject.database
        reference = firebaseDatabase.getReference("parentData")
        reference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                menu = bottomNav.menu
                menu.clear()
                var cnt=0
                for(i in snapshot.children){
                    ListOfBottomNav.add("${i.key}")
                    menu.add(Menu.NONE,cnt,Menu.NONE,"${i.key}")
                    when(cnt){
                        0->{
                            Log.d("rahul","0")
                            menu.getItem(0).setIcon(R.drawable.ic_launcher_circular1)
                        }
                        1->{
                            Log.d("rahul","1")
                            menu.getItem(1).setIcon(R.drawable.ic_launcher_circular2)
                        }
                        2->{
                            menu.getItem(2).setIcon(R.drawable.ic_launcher_circular3)
                        }
                        3->{
                            Log.d("rahul","4")
                            menu.getItem(3).setIcon(R.drawable.ic_launcher_circular4)
                        }
                        4->{
                            menu.getItem(4).setIcon(R.drawable.ic_launcher_circular5)
                        }
                    }
                    cnt++
                }
                initBottomNavView(ListOfBottomNav)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Failed", "onCancelled: ")
            }
        })


        bottomNav.setOnItemSelectedListener {
            setCurrentFragment(AppLinkFragment(ListOfBottomNav.get(it.itemId)))
            true
        }
    }
    private fun setCurrentFragment(fragment:Fragment){
        if(fragment!=null){
            supportFragmentManager.beginTransaction().apply{
                replace(R.id.fragment_contaier,fragment).commit()
            }
        }
    }
    private fun initBottomNavView(arr:ArrayList<String>){
        setCurrentFragment(AppLinkFragment(arr.get(0)))
    }
}