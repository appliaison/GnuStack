package app.niftyapp.mobile.android.niftyapp.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import app.niftyapp.mobile.android.niftyapp.R
import app.niftyapp.mobile.android.niftyapp.models.User
import app.niftyapp.mobile.android.niftyapp.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val mFireStore = FirebaseFirestore.getInstance();
    private var user: User = User("", "", "", "")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userId = intent.getStringExtra(Constants.USERID)
        mFireStore.collection(Constants.USERS)
                // The document id to get the Fields of user.
                .document(userId)
                .get()
                .addOnSuccessListener { document ->

                    Log.i(this.javaClass.simpleName, document.toString())

                    // Here we have received the document snapshot which is converted into the User Data model object.
                    user = document.toObject(User::class.java)!!
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

        val sharedPreferences = getSharedPreferences(Constants.APP_PREFS, Context.MODE_PRIVATE)
        val name = sharedPreferences.getString(Constants.LOGGED_IN_NAME, "")!!


        // val firstName = intent.getStringExtra(Constants.FIRSTNAME)
        // val lastName = intent.getStringExtra(Constants.LASTNAME)
        val firstName = user.firstName
        val lastName = user.lastName
        val emailId = intent.getStringExtra(Constants.EMAILID)
        tvUserName.text = "Name: $name"
        tvEmailId.text = "Email:: $emailId"


        btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()

            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish()

        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}