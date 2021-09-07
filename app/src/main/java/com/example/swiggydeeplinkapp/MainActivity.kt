package com.example.swiggydeeplinkapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(){
    private lateinit var bottomNav:BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNav = findViewById(R.id.bottom_navigation)
        setCurrentFragment(AppLinkFragment())
        bottomNav.setOnItemSelectedListener {

            when(it.itemId){
                R.id.AppLinks -> {
                    setCurrentFragment(AppLinkFragment())
                }
                R.id.UniversalLinks -> {
                    setCurrentFragment(UniversalLinkFragment())
                }
            }
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

}