package com.example.chatapplication.registration_new

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.chatapplication.R
import com.example.chatapplication.registration.user.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class DetailsFragment : Fragment() {


    private val args: DetailsFragmentArgs by navArgs()
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().reference

        val email = args.email


        val name = view.findViewById<EditText>(R.id.name)
        val surname = view.findViewById<EditText>(R.id.surname)
        val password = view.findViewById<EditText>(R.id.password)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        val progressBarLayout: ConstraintLayout = view.findViewById(R.id.progressbarConstraintLayout)
        val nextButton = view.findViewById<AppCompatButton>(R.id.nextButton)
        nextButton.setOnClickListener {

            if (name.text.toString().isNullOrEmpty() || surname.text.toString().isNullOrEmpty() || password.text.toString().isNullOrEmpty()){
                Toast.makeText(this.requireContext(),"Please, fill all!", Toast.LENGTH_LONG).show()
            }else{
                progressBar.visibility = View.VISIBLE
                progressBarLayout.setBackgroundColor(R.color.progressbar_background)
                nextButton.isClickable = false
                auth.createUserWithEmailAndPassword(email, password.text.toString())
                    .addOnCompleteListener(this.requireActivity()) { task ->
                        if (task.isSuccessful) {
                            val user: User = User.UserBuilder()
                                .setName(name.text.toString())
                                .setSurname(surname.text.toString())
                                .setEmail(email)
                                .setUid(auth.currentUser?.uid!!)
                                .build()
                            dbRef.child("user").child(auth.currentUser?.uid!!).setValue(user)
                            val action = DetailsFragmentDirections.actionFromDetailsToVerification(email,password.text.toString())
                            findNavController().navigate(action)
                        } else {
                            Toast.makeText(this.requireContext(),"Account couldn't be created! Check your internet connection or try again!", Toast.LENGTH_LONG).show()
                            nextButton.isClickable = true
                            progressBar.visibility = View.GONE
                            progressBarLayout.setBackgroundColor(0)
                            nextButton.isClickable = true
                        }
                    }
            }


        }
    }





}