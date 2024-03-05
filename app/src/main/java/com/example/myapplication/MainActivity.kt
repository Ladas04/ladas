package ru.btpit.nmedia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import ru.btpit.nmedia.databinding.ActivityMain2Binding
import ru.btpit.nmedia.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var button : Button = findViewById(R.id.button)
        button.setOnClickListener{
            var myIntent = Intent(this,MainActivity2::class.java)
            startActivity(myIntent)
        }
    }
}