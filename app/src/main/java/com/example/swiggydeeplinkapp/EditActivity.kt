package com.example.swiggydeeplinkapp

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.KeyEvent
import android.view.OrientationEventListener
import android.view.View
import android.view.ViewTreeObserver
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible

class EditActivity:AppCompatActivity(){

    private lateinit var editLink:EditText
    private lateinit var doneButton: Button
    private lateinit var titleTextView:TextView
    private lateinit var toolbar: Toolbar
    private lateinit var toolBarTitle:TextView
    private lateinit var openLink:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_deeplink_for_testing)
        openLink = findViewById(R.id.OpenLink)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener{
            onBackPressed()
        }
        toolBarTitle = findViewById(R.id.ToolBarTitle)
        toolBarTitle.setText("Edit DeepLink")
        val mainLayout:ConstraintLayout = findViewById(R.id.EditLayout)
        editLink = findViewById(R.id.EditLink)
        val link = intent.extras?.get("link").toString()
        editLink.setText(link)
        doneButton = findViewById(R.id.DoneButton)
        titleTextView = findViewById(R.id.EditTitle)
        titleTextView.setText(intent.extras?.get("title").toString())
        openLink.isClickable = true
        openLink.setMovementMethod(LinkMovementMethod.getInstance())
        editLink.setText(link)
        editLink.requestFocus()
        editLink.setOnEditorActionListener(object : TextView.OnEditorActionListener{
        override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    editLink.isCursorVisible = false
                    return false
                }
                return false
            }

        })
        editLink.setOnClickListener{
            editLink.isCursorVisible = true
        }

        val mOrientationEventListenerc = object : OrientationEventListener(this){
            override fun onOrientationChanged(orientation: Int) {
                Log.d("rahul","$orientation")
                doneButton.isVisible = (orientation!=90 && orientation!=270)
            }

        }

        if(mOrientationEventListenerc.canDetectOrientation()){
            mOrientationEventListenerc.enable()
        }

        doneButton.setOnClickListener{
            editLink.isCursorVisible = false
            val imm:InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(mainLayout.windowToken,0)
        }
        openLink.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(editLink.text.toString()))
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}
