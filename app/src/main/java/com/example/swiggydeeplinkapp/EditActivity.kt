package com.example.swiggydeeplinkapp

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.text.style.UnderlineSpan
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class EditActivity:AppCompatActivity() {

    private lateinit var editLink:EditText
    private lateinit var editButton: Button
    private lateinit var testButton: Button
    private lateinit var doneButton: Button
    private lateinit var back:TextView
    private lateinit var textViewLink:TextView
    private lateinit var titleTextView:TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_deeplink_for_testing)
        val mainLayout:ConstraintLayout = findViewById(R.id.EditLayout)
        editLink = findViewById(R.id.EditLink)
        val link = intent.extras?.get("link").toString()
        editLink.setText(link)
        editButton = findViewById(R.id.EditButton)
        doneButton = findViewById(R.id.DoneButton)
        back = findViewById(R.id.backButton)
        testButton = findViewById(R.id.TestButton)
        textViewLink = findViewById(R.id.TextViewLink)
        titleTextView = findViewById(R.id.EditTitle)
        titleTextView.setText(intent.extras?.get("title").toString())
        textViewLink.isClickable = true
        textViewLink.setMovementMethod(LinkMovementMethod.getInstance())
        textViewLink.setText(link)

        back.setOnClickListener{
            onBackPressed()
        }
        editLink.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                textViewLink.setText(s)
            }
        })
        editButton.setOnClickListener{
            editLink.visibility = View.VISIBLE
            textViewLink.visibility = View.GONE
            testButton.visibility = View.GONE
            editLink.setText(textViewLink.text)
        }
        doneButton.setOnClickListener{
            editLink.visibility = View.GONE
            textViewLink.visibility = View.VISIBLE
            testButton.visibility = View.VISIBLE
            textViewLink.isClickable = true
            textViewLink.setMovementMethod(LinkMovementMethod.getInstance())
            textViewLink.setText(editLink.text)
            val imm:InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(mainLayout.windowToken,0)
        }
        testButton.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(textViewLink.text.toString()))
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