package com.example.chatapplication.registration_new

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import com.example.chatapplication.MainActivity
import com.example.chatapplication.R
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }
    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*
        if (auth.currentUser != null){
            val intent = Intent(this@LoginActivity,MainActivity::class.java)
            finish()
            startActivity(intent)
        }
         */

        val email = view.findViewById<EditText>(R.id.email)
        val password = view.findViewById<EditText>(R.id.password)
        val loginButton = view.findViewById<AppCompatButton>(R.id.loginButton)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        val signupRedirect = view.findViewById<TextView>(R.id.signupTextView)

        val progressBarLayout: ConstraintLayout = view.findViewById(R.id.progressbarConstraintLayout)

        signupRedirect.setOnClickListener {
            val action = LoginFragmentDirections.actionFromLoginToEmail()
            findNavController().navigate(action)
        }


        loginButton.setOnClickListener {

            progressBarLayout.setBackgroundColor(R.color.progressbar_background)
            progressBar.visibility = View.VISIBLE
            loginButton.isClickable = false

            if (email.text.toString().isNullOrEmpty() || password.text.toString().isNullOrEmpty()){
                progressBarLayout.setBackgroundColor(0)
                progressBar.visibility = View.GONE
                loginButton.isClickable = true
                Toast.makeText(this.requireContext(),"Email and password can not be empty!", Toast.LENGTH_LONG).show()
            }else{
                val auth = FirebaseAuth.getInstance()
                auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                    .addOnCompleteListener(this.requireActivity()) { task ->
                        if (task.isSuccessful) {

                            val firebaseUser = auth.currentUser!!
                            if (!firebaseUser.isEmailVerified) {
                                progressBarLayout.setBackgroundColor(0)
                                progressBar.visibility = View.GONE
                                val action = LoginFragmentDirections.actionFromLoginToNotVerified(email.text.toString(), password.text.toString())
                                findNavController().navigate(action)
                            }else{
                                val intent = Intent(this.requireContext(),MainActivity::class.java)
                                progressBarLayout.setBackgroundColor(0)
                                progressBar.visibility = View.GONE
                                startActivity(intent)
                            }
                        } else {
                            progressBarLayout.setBackgroundColor(0)
                            progressBar.visibility = View.GONE
                            loginButton.isClickable = true
                            Toast.makeText(this.requireContext(),"User does not exist! Try again!", Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }
    }
}