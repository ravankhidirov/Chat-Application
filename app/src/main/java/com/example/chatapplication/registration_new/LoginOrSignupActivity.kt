package com.example.chatapplication.registration_new

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.chatapplication.databinding.ActivityLoginOrSignupBinding

class LoginOrSignupActivity : AppCompatActivity() {
    private lateinit var binding:ActivityLoginOrSignupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginOrSignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}