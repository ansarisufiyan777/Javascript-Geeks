package info.infiniteloops.jgeeks.network

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor

import java.util.HashMap

import info.infiniteloops.jgeeks.MainActivity


class SessionManager(internal var _context: Context) {
    // Shared Preferences
    internal var pref: SharedPreferences

    // Editor for Shared preferences
    internal var editor: Editor

    // Shared pref mode
    internal var PRIVATE_MODE = 0


    /**
     * Get stored session data
     */
    // user name
    // user email id
    // return user
    val userDetails: HashMap<String, String>
        get() {
            val user = HashMap<String, String>()
            user[KEY_EMAIL] = pref.getString(KEY_EMAIL, null)
            user[KEY_DOCUMENT_ID] = pref.getString(KEY_DOCUMENT_ID, null)
            user[KEY_NAME] = pref.getString(KEY_NAME, null)
            return user
        }


    /**
     * Quick check for login
     */
    // Get GoogleLogin State
    val isLoggedIn: Boolean
        get() = pref.getBoolean(IS_LOGIN, false)
    val email: String
        get() {
            return pref.getString(KEY_EMAIL, null)

        }
    val uid: String
        get() {
            return pref.getString(KEY_DOCUMENT_ID, null)

        }
    val name: String
        get() {
            return pref.getString(KEY_NAME, null)

        }
    init {
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()
    }


    /**
     * Create login session
     */
    fun createLoginSession(email: String, document_id: String,name:String) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true)

        // Storing email in pref
        editor.putString(KEY_EMAIL, email)

        // Storing document_id in pref
        editor.putString(KEY_DOCUMENT_ID, document_id)
        editor.putString(KEY_NAME, name)

        // commit changes
        editor.commit()
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */
    fun checkLogin() {
        // Check login status
        if (!this.isLoggedIn) {
            // user is not logged in redirect him to GoogleLogin Activity
            val i = Intent(_context, MainActivity::class.java)
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            // Add new Flag to start new Activity
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK

            // Staring GoogleLogin Activity
            _context.startActivity(i)
        }

    }

    /**
     * Clear session details
     */
    fun logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear()
        editor.commit()

        // After logout redirect user to Loing Activity
        val i = Intent(_context, MainActivity::class.java)
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        // Add new Flag to start new Activity
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        // Staring GoogleLogin Activity
        _context.startActivity(i)
    }

    companion object {

        // Sharedpref file name
        private val PREF_NAME = "AndroidHivePref"

        // All Shared Preferences Keys
        private val IS_LOGIN = "IsLoggedIn"

        // User name (make variable public to access from outside)
        val KEY_EMAIL = "email"

        // Email address (make variable public to access from outside)
        val KEY_DOCUMENT_ID = "document_id"
        val KEY_NAME = "name"
    }
}