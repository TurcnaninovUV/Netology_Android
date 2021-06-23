package ru.netology.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.google.android.gms.common.ConnectionResult
import com.google.firebase.iid.FirebaseInstanceId
//import com.google.android.gms.common.GoogleApiAvailability
//import com.google.firebase.iid.FirebaseInstanceId
import ru.netology.R
import ru.netology.activity.NewPostFragment.Companion.textArg
import ru.netology.auth.AppAuth
import ru.netology.viewmodel.AuthViewModel


class AppActivity : AppCompatActivity(R.layout.activity_app) {

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent?.let {
            if (it.action != Intent.ACTION_SEND) {
                return@let
            }

            val text = it.getStringExtra(Intent.EXTRA_TEXT)
            if (text?.isNotBlank() != true) {
                return@let
            }

            intent.removeExtra(Intent.EXTRA_TEXT)
            findNavController(R.id.nav_host_fragment)
                .navigate(
                    R.id.action_fragmentFeed_to_newPostFragment,
                    Bundle().apply {
                        textArg = text
                    }
                )
        }

//        checkGoogleApiAvailability()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        menu?.let {
            it.setGroupVisible(R.id.unauthenticated, !viewModel.authenticated)
            it.setGroupVisible(R.id.authenticated, viewModel.authenticated)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.signin -> {
                findNavController(R.id.nav_host_fragment).navigate(R.id.action_fragmentFeed_to_fragmentSignIn)
                true
            }
            R.id.signup -> {
                findNavController(R.id.nav_host_fragment).navigate(R.id.action_fragmentFeed_to_fragmentSignUp)
                true
            }
            R.id.signout -> {
                AlertDialog.Builder(this)
                        .setMessage(R.string.sign_out_dialog)
                        .setPositiveButton(R.string.positive_button) { dialog, id ->
                            AppAuth.getInstance().removeAuth()
                            findNavController(R.id.nav_host_fragment).navigateUp()
                        }
                        .setNegativeButton(R.string.negative_button) {dialog, id ->
                            return@setNegativeButton
                        }
                        .show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

//    private fun checkGoogleApiAvailability() {
//        with(GoogleApiAvailability.getInstance()) {
//            val code = isGooglePlayServicesAvailable(this@AppActivity)
//            if (code == ConnectionResult.SUCCESS) {
//                return@with
//            }
//            if (isUserResolvableError(code)) {
//                getErrorDialog(this@AppActivity, code, 9000).show()
//                return
//            }
//            Toast.makeText(this@AppActivity, R.string.google_play_unavailable, Toast.LENGTH_LONG)
//                .show()
//        }
//
//        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
//            println(it.token)
//        }
//    }
}
