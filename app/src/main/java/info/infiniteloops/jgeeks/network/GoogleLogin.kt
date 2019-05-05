package info.infiniteloops.jgeeks.network

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import info.infiniteloops.jgeeks.R
import info.infiniteloops.jgeeks.Utils.Utils
import info.infiniteloops.jgeeks.models.Post
import org.jsoup.helper.StringUtil
import kotlinx.android.synthetic.main.login_loayout.view.*


/**
 * Created by Asna on 5/4/2019.
 */
class GoogleLogin(var activity: Activity) {

    fun alertForLogin() {
        var inflater: LayoutInflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater;
        var view = inflater.inflate(R.layout.login_loayout, null, false)
        view.login.setOnClickListener {
            loginWithgmail()
        }
        AlertDialog.Builder(activity).setView(view)
                .show()
    }
    fun loginWithgmail() {
        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.google_client_id))
                .build()
        var mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);
        val signInIntent = mGoogleSignInClient.signInIntent
        activity.startActivityForResult(signInIntent, Utils.RC_SIGN_IN)
    }

    fun firebaseAuthWithGoogle(auth: FirebaseAuth, data: Intent?) {
        try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)
            Log.d("LoginAct", "firebaseAuthWithGoogle:" + account!!.id!!)
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            auth.signInWithCredential(credential)
                    .addOnCompleteListener(activity) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("LoginAct", "signInWithCredential:success")
                            Toast.makeText(activity, "Authentication Success.", Toast.LENGTH_SHORT).show()
                            val user = auth.currentUser
                            SessionManager(activity).createLoginSession(user!!.email!!, user.uid, user.displayName!!)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("LoginAct", "signInWithCredential:failure", task.exception)
                            Toast.makeText(activity, "Authentication Failed.", Toast.LENGTH_SHORT).show()
                        }
                    }
        } catch (e: Exception) {
            Log.w("LoginAct", "signInWithCredential:failure")
            Toast.makeText(activity, "Authentication Failed.", Toast.LENGTH_SHORT).show()
        }
    }

    fun addToLikesIfNot(post: Post) {
        var email = SessionManager(activity).email;
        if (!StringUtil.isBlank(email)) {
            val fu = FirestoreUtils()
            post.category = SessionManager(activity).email
            fu.setPostData(post, activity)
            Toast.makeText(activity, "Added to liked posts", Toast.LENGTH_SHORT).show()
        }
    }

}
