package com.example.listapplication333.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.listapplication333.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_mainfragment.*
import kotlinx.android.synthetic.main.fragment_notloginnfragment.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [notloginnfragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class notloginnfragment : Fragment() {

    //firebase Auth
    private lateinit var firebaseAuth: FirebaseAuth
    //google client
    private var googleSignInClient: GoogleSignInClient?=null
    lateinit var auth: FirebaseAuth

    //private const val TAG = "GoogleActivity"
    private val RC_SIGN_IN = 9001

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var navController : NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notloginnfragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        btn_back1.setOnClickListener {
            navController.navigate(R.id.action_notloginnfragment_to_mainfragment)
        }

        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()



        btn_googleSignIn.setOnClickListener {
            googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
            val signInIntent = googleSignInClient!!.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)


        }



    }

    override fun onDestroy() {
        super.onDestroy()

    }



    private fun firebaseAuthWithGoogle(idT0ken: String) {
        val credentical = GoogleAuthProvider.getCredential(idT0ken, null)
        auth.signInWithCredential(credentical)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) { //성공
                    val user = auth.currentUser
                    user?.let {
                        val name = user.displayName
                        val email = user.email
                        val displayName = user.photoUrl
                        val emailVerified = user.isEmailVerified
                        val uid = user.uid
                        Log.d("xxxx name", name.toString())
                        Log.d("xxxx email", email.toString())
                        Log.d("xxxx displayName", email.toString())
                        toMainActivity(firebaseAuth?.currentUser)
                    }
                } else {//실패
                    Log.d("xxxx", "signInWithCredentical:failure", task.exception)

                }

            }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firestoreSettings { account.idToken }
            }catch (e: ApiException) {

            }
        }
    }

    fun toMainActivity(user:FirebaseUser?) {
        if (user != null) {
            startActivity(Intent(requireContext(), Mainfragment::class.java))

        }
    }






    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment notloginnfragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            notloginnfragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}



