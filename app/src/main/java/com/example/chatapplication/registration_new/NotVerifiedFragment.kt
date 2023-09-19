package com.example.chatapplication.registration_new

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.chatapplication.MainActivity
import com.example.chatapplication.R
import com.google.firebase.auth.FirebaseAuth


class NotVerifiedFragment : Fragment() {

    private val args: NotVerifiedFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_not_verified, container, false)
    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sendEmailTextView = view.findViewById<TextView>(R.id.sendEmailTextView)
        val verifiedButton = view.findViewById<AppCompatButton>(R.id.verifiedButton)


        val progressBarLayout: ConstraintLayout = view.findViewById(R.id.progressbarConstraintLayout)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)


        sendEmailTextView.setOnClickListener {
            progressBarLayout.setBackgroundColor(R.color.progressbar_background)
            progressBar.visibility = View.VISIBLE
            verifiedButton.isClickable = false
            val auth = FirebaseAuth.getInstance()
            val firebaseUser = auth.currentUser!!
            if (!firebaseUser.isEmailVerified) {
                firebaseUser.sendEmailVerification().addOnCompleteListener(this.requireActivity()) { task ->
                    if (task.isSuccessful) {
                        progressBarLayout.setBackgroundColor(0)
                        progressBar.visibility = View.GONE
                        verifiedButton.isClickable = true
                        Toast.makeText(this.requireContext(),"An email has been sent to email!",
                            Toast.LENGTH_LONG).show()
                    } else {
                        progressBarLayout.setBackgroundColor(0)
                        progressBar.visibility = View.GONE
                        verifiedButton.isClickable = true
                        Toast.makeText(this.requireContext(),"Completed unsuccessfully!", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        verifiedButton.setOnClickListener {
            progressBarLayout.setBackgroundColor(R.color.progressbar_background)
            progressBar.visibility = View.VISIBLE
            verifiedButton.isClickable = false

            val auth = FirebaseAuth.getInstance()

            val email = args.email
            val password = args.password

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this.requireActivity()) { task ->
                    if (task.isSuccessful) {
                        val firebaseUser = auth.currentUser!!
                        if (firebaseUser.isEmailVerified) {
                            val action = NotVerifiedFragmentDirections.actionFromNotVerifiedToProfile()
                            findNavController().navigate(action)
                        } else {
                            progressBarLayout.setBackgroundColor(0)
                            progressBar.visibility = View.GONE
                            verifiedButton.isClickable = true
                            Toast.makeText(this.requireContext(), "Not confirmed!", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        progressBarLayout.setBackgroundColor(0)
                        progressBar.visibility = View.GONE
                        verifiedButton.isClickable = true
                        Toast.makeText(this.requireContext(), "Try again!", Toast.LENGTH_LONG).show()
                    }
                }
        }


    }

}