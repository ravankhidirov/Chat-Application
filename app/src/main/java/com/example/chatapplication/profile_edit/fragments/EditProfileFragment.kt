package com.example.chatapplication.profile_edit.fragments

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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class EditProfileFragment : Fragment() {

    private val args: EditProfileFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dbRef = FirebaseDatabase.getInstance().reference
        val NAME = 0
        val SURNAME = 1


        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        val progressBarLayout: ConstraintLayout = view.findViewById(R.id.progressbarConstraintLayout)


        val doneButton = view.findViewById<AppCompatButton>(R.id.doneButton)
        val nameOrSurname = view.findViewById<EditText>(R.id.nameOrSurname)
        nameOrSurname.hint = args.nameOrSurname

        val forWhat = args.forWhat
        val auth = FirebaseAuth.getInstance()



        doneButton.setOnClickListener {
            val action = EditProfileFragmentDirections.actionFromEditToCurrent()
            if (nameOrSurname.text.toString() != args.nameOrSurname){
                progressBar.visibility = View.VISIBLE
                progressBarLayout.setBackgroundColor(R.color.progressbar_background)

                if (forWhat == NAME){
                    dbRef.child("user").child(auth.currentUser?.uid!!).child("name").setValue(nameOrSurname.text.toString()).addOnCompleteListener {
                        progressBar.visibility = View.GONE
                        progressBarLayout.setBackgroundColor(0)
                        findNavController().navigate(action)
                    }
                }else{
                    dbRef.child("user").child(auth.currentUser?.uid!!).child("username").setValue(nameOrSurname.text.toString()).addOnCompleteListener {
                        progressBar.visibility = View.GONE
                        progressBarLayout.setBackgroundColor(0)
                        findNavController().navigate(action)
                    }
                }
            }else{
                Toast.makeText(this@EditProfileFragment.requireContext(),"Nothing changed!",Toast.LENGTH_LONG).show()
                findNavController().navigate(action)
            }
        }
    }
}