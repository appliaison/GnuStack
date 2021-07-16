package app.niftyapp.mobile.android.niftyapp.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import app.niftyapp.mobile.android.niftyapp.R
import app.niftyapp.mobile.android.niftyapp.utils.Constants
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val sharedPreferences = getSharedPreferences(Constants.APP_PREFS, Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean(Constants.IS_LOGGED_IN, false)!!
        val userId = sharedPreferences.getString(Constants.FIREBASE_USER_ID, "")!!
        if (isLoggedIn) {
            val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra(Constants.USERID, userId)
            startActivity(intent)
            finish()
        }

        textViewRegister.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }

        buttonLogin.setOnClickListener {
            when {
                TextUtils.isEmpty(etLoginEmail.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@LoginActivity,
                        "Please enter email",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                TextUtils.isEmpty(etLoginPassword.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@LoginActivity,
                        "Please enter password.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    val email: String = etLoginEmail.text.toString().trim { it <= ' ' }
                    val password: String = etLoginPassword.text.toString().trim { it <= ' ' }
                    // Create an instance and create a register a user with email and password.
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(
                            OnCompleteListener<AuthResult> { task ->

                                // If the registration is successfully done
                                if (task.isSuccessful) {

                                    val firebaseUserId = FirebaseAuth.getInstance().currentUser!!.uid

                                    val sharedPreferences = this.getSharedPreferences(Constants.APP_PREFS, Context.MODE_PRIVATE)
                                    val editor: SharedPreferences.Editor = sharedPreferences.edit()
                                    editor.putBoolean(Constants.IS_LOGGED_IN, true)
                                    editor.putString(Constants.FIREBASE_USER_ID, firebaseUserId)
                                    editor.apply()

                                    Toast.makeText(
                                        this@LoginActivity,
                                        "You are logged in successfully.",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    /**
                                     * Here the new user registered is automatically signed-in so we just sign-out the user from firebase
                                     * and send him to Main Screen with user id and email that user have used for registration.
                                     */

                                    /**
                                     * Here the new user registered is automatically signed-in so we just sign-out the user from firebase
                                     * and send him to Main Screen with user id and email that user have used for registration.
                                     */
                                    /**
                                     * Here the new user registered is automatically signed-in so we just sign-out the user from firebase
                                     * and send him to Main Screen with user id and email that user have used for registration.
                                     */
                                    /**
                                     * Here the new user registered is automatically signed-in so we just sign-out the user from firebase
                                     * and send him to Main Screen with user id and email that user have used for registration.
                                     */
                                    // TODO-FIXME val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                    val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    intent.putExtra(Constants.USERID, firebaseUserId)
                                    intent.putExtra(Constants.EMAILID, email)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    // If the registering is not successful then show error message.
                                    Toast.makeText(
                                        this@LoginActivity,
                                        task.exception!!.message.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            })
                }
            }
        }


    }
}