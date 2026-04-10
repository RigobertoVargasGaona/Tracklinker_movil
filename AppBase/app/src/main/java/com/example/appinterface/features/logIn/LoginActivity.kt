package com.example.appinterface.features.logIn

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import android.provider.Settings
import com.example.appinterface.Api.RetrofitInstance
import com.example.appinterface.MainActivity
import com.example.appinterface.R
import com.example.appinterface.helpers.TokenManager

class LoginActivity : AppCompatActivity() {

    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    private val viewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RetrofitInstance.init(this)
        setContentView(R.layout.login_activity)

        val tokenManager = TokenManager(this)
        if (tokenManager.isLoggedIn()) {
            navigateToHome()
            return
        }

        val etEmail    = findViewById<EditText>(R.id.inputUser)
        val etPassword = findViewById<EditText>(R.id.inputPass)
        val btnLogIn   = findViewById<Button>(R.id.btnLogin)
        val btnHuella  = findViewById<ImageButton>(R.id.btnFingerprint)

        btnLogIn.setOnClickListener {
            viewModel.login(
                etEmail.text.toString().trim(),
                etPassword.text.toString().trim()
            )
        }

        viewModel.loginState.observe(this) { state ->
            when (state) {
                is LoginUiState.Loading -> btnLogIn.isEnabled = false
                is LoginUiState.Success -> navigateToHome()
                is LoginUiState.Error   -> {
                    btnLogIn.isEnabled = true
                    Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
                }
                else -> btnLogIn.isEnabled = true
            }
        }

        setupBiometricPrompt()

        btnHuella.setOnClickListener {
            val biometricManager = BiometricManager.from(this)
            if (checkBiometricAvailability(this)) {
                biometricPrompt.authenticate(promptInfo)
            } else {
                Toast.makeText(this, "Registrar huella", Toast.LENGTH_SHORT).show()
                if (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)
                    == BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED) {
                    val intent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                        putExtra(
                            Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                            BiometricManager.Authenticators.BIOMETRIC_STRONG
                        )
                    }
                    startActivity(intent)
                }
            }
        }
    }

    fun checkBiometricAvailability(context: Context): Boolean {
        val biometricManager = BiometricManager.from(context)
        return when (biometricManager.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> true
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Log.d("Biometric", "Sin sensor"); false
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                Log.d("Biometric", "Sin huellas"); false
            }
            else -> false
        }
    }

    private fun setupBiometricPrompt() {
        val executor = ContextCompat.getMainExecutor(this)
        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                navigateToHome()
            }
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(this@LoginActivity, "Error: $errString", Toast.LENGTH_SHORT).show()
            }
            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(this@LoginActivity, "Huella no reconocida", Toast.LENGTH_SHORT).show()
            }
        }
        biometricPrompt = BiometricPrompt(this, executor, callback)
        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Iniciar sesión")
            .setSubtitle("Usa tu huella digital")
            .setNegativeButtonText("Cancelar")
            .build()
    }

    private fun navigateToHome() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}