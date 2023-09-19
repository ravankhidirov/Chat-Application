package com.example.chatapplication.registration_new

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
import androidx.navigation.fragment.findNavController
import com.example.chatapplication.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class EmailFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_email, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val email = view.findViewById<EditText>(R.id.email)
        val loginRedirect = view.findViewById<TextView>(R.id.loginTextView)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        val nextButton = view.findViewById<AppCompatButton>(R.id.nextButton)



        loginRedirect.setOnClickListener {
            val action = EmailFragmentDirections.actionFromEmailToLogin()
            findNavController().navigate(action)
        }

        nextButton.setOnClickListener {

            if (email.text.toString().isNullOrEmpty()){
                Toast.makeText(this.requireContext(),"Email can not be empty!", Toast.LENGTH_LONG).show()
            }else{
                val action = EmailFragmentDirections.actionFromEmailToDetails(email.text.toString())
                findNavController().navigate(action)
            }

        }


    }
}