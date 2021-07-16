package app.niftyapp.mobile.android.niftyapp.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import app.niftyapp.mobile.android.niftyapp.R
import app.niftyapp.mobile.android.niftyapp.firestore.FirestoreUtility
import app.niftyapp.mobile.android.niftyapp.models.User
import app.niftyapp.mobile.android.niftyapp.utils.Constants
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseActivity() {
    private val mFireStore = FirebaseFirestore.getInstance();
    lateinit var email: String
    lateinit var password: String
    lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        tvLogin.setOnClickListener {
            onBackPressed()
        }

       buttonRegister.setOnClickListener {
            registerUser()
       }
    }

    private fun validateRegisterDetails(): Boolean {
        return when {
            TextUtils.isEmpty(etFirstName.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_first_name), true)
                false
            }

            TextUtils.isEmpty(etLastName.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_last_name), true)
                false
            }

            TextUtils.isEmpty(etEmail.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }

            TextUtils.isEmpty(etPassword.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }

            TextUtils.isEmpty(etConfirmPassword.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_enter_confirm_password),
                    true
                )
                false
            }

            etPassword.text.toString().trim { it <= ' ' } != etConfirmPassword.text.toString()
                .trim { it <= ' ' } -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_password_and_confirm_password_mismatch),
                    true
                )
                false
            }
            !cb_terms_and_condition.isChecked -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_agree_terms_and_condition),
                    true
                )
                false
            }
            else -> {
                showErrorSnackBar(resources.getString(R.string.registration_unsuccessful), false)
                true
            }
        }
    }

    private fun registerUser() {
        // Check with validate function if the entries are valid or not.
        if (validateRegisterDetails()) {

            // Show the progress dialog.
            showProgressDialog(resources.getString(R.string.please_wait))

            email = etEmail.text.toString().trim { it <= ' ' }
            password = etPassword.text.toString().trim { it <= ' ' }

            // Create an instance and create a register a user with email and password.
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    OnCompleteListener<AuthResult> { task ->

                        // Hide the progress dialog
                        // hideProgressDialog()

                        // If the registration is successfully done
                        if (task.isSuccessful) {

                            // Firebase registered user
                            val firebaseUser: FirebaseUser = task.result!!.user!!

                            user = User(
                                    firebaseUser.uid,
                                    etFirstName.text.toString().trim() { it <= ' ' },
                                    etLastName.text.toString().trim() { it <= ' ' },
                                    etEmail.text.toString().trim() { it <= ' ' }

                            )

                            showErrorSnackBar(
                                "You are registered successfully. Your user id is ${firebaseUser.uid}",
                                false
                            )

                            /**
                             * Here the new user registered is automatically signed-in so we just sign-out the user from firebase
                             * and send him to Login Screen.
                             */
                            FirestoreUtility().registerUser(this@RegisterActivity, user)
                            // FirebaseAuth.getInstance().signOut()
                            // Finish the Register Screen
                            // finish()
                            // The "users" is collection name. If the collection is already created then it will not create the same one again.
                            mFireStore.collection(Constants.USERS)
                                    // Document ID for users fields. Here the document it is the User ID.
                                    .document(user.id)
                                    // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge later on instead of replacing the fields.
                                    .set(user, SetOptions.merge())
                                    .addOnSuccessListener {

                                        // Here call a function of base activity for transferring the result to it.
                                        userRegistrationSuccess()
                                    }
                                    .addOnFailureListener { e ->
                                        hideProgressDialog()
                                        Log.e(
                                                this.javaClass.simpleName,
                                                "Error while registering the user.", e
                                        )
                                    }
                        } else {
                            hideProgressDialog()
                            // If the registering is not successful then show error message.
                            showErrorSnackBar(task.exception!!.message.toString(), true)
                        }
                    })
        }
    }
    /**
     * A function to notify the success result of Firestore entry when the user is registered successfully.
     */
    fun userRegistrationSuccess() {

        // Hide the progress dialog
        hideProgressDialog()

        // TODO Step 5: Replace the success message to the Toast instead of Snackbar.
        /*
        Toast.makeText(
                this@RegisterActivity,
                resources.getString(R.string.register_success),
                Toast.LENGTH_SHORT
        ).show()
        */

        /**
         * Here the new user registered is automatically signed-in so we just sign-out the user from firebase
         * and send him to Intro Screen for Sign-In
         */
        /// FirebaseAuth.getInstance().signOut()
        // Finish the Register Screen
        // finish()

        // Here we pass the collection name from which we wants the data.
        mFireStore.collection(Constants.USERS)
                // The document id to get the Fields of user.
                .document(user.id)
                .get()
                .addOnSuccessListener { document ->

                    Log.i(this.javaClass.simpleName, document.toString())

                    // Here we have received the document snapshot which is converted into the User Data model object.
                    val user = document.toObject(User::class.java)!!
                    Log.i(this.javaClass.simpleName, user.firstName)
                    Log.i(this.javaClass.simpleName, user.lastName)
                    Log.i(this.javaClass.simpleName, user.email)

                    val sharedPreferences = this.getSharedPreferences(Constants.APP_PREFS, Context.MODE_PRIVATE)
                    val editor: SharedPreferences.Editor = sharedPreferences.edit()
                    editor.putString(Constants.LOGGED_IN_NAME, "${user.firstName} ${user.lastName}")
                    editor.apply()

                }
                .addOnFailureListener { e ->
                    // Hide the progress dialog if there is any error. And print the error in log.
                    Log.e(
                            this.javaClass.simpleName,
                            "Error while getting user details.",
                            e
                    )
                }

        val intent = Intent(this@RegisterActivity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra(Constants.USERID, user.id)
        intent.putExtra(Constants.FIRSTNAME, user.firstName)
        intent.putExtra(Constants.LASTNAME, user.lastName)
        intent.putExtra(Constants.EMAILID, email)
        startActivity(intent)
        finish()
    }

}