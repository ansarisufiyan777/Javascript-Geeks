package info.infiniteloops.jgeeks.network

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GithubAuthProvider

/**
 * Created by Asna on 5/5/2019.
 */
class GithubLogin(){
    var TAG = GithubLogin::class.java.simpleName
    fun login(activity: Activity,mAuth:FirebaseAuth){
        val token = ""
        val credential = GithubAuthProvider.getCredential(token)
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity) { task ->
                    Log.d("", "signInWithCredential:onComplete:" + task.isSuccessful)

                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    if (!task.isSuccessful) {
                        Log.w(TAG, "signInWithCredential", task.exception)
                        Toast.makeText(activity, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                    }

                    // ...
                }
    }
}