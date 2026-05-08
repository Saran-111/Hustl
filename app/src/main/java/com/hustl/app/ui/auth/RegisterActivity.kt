package com.hustl.app.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.hustl.app.data.repository.AuthRepository
import com.hustl.app.databinding.ActivityRegisterBinding
import com.hustl.app.ui.home.MainActivity
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var authRepo: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authRepo = AuthRepository(this)

        binding.btnRegister.setOnClickListener { doRegister() }
        binding.tvLogin.setOnClickListener { finish() }
    }

    private fun doRegister() {
        val name = binding.etName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        val role = if (binding.rbSeller.isChecked) "hustlr" else "buyer"

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }
        if (password.length < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
            return
        }

        binding.btnRegister.isEnabled = false
        binding.progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            val result = authRepo.register(name, email, password, role)
            result.fold(
                onSuccess = {
                    startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
                    finishAffinity()
                },
                onFailure = {
                    Toast.makeText(this@RegisterActivity, it.message, Toast.LENGTH_LONG).show()
                    binding.btnRegister.isEnabled = true
                    binding.progressBar.visibility = View.GONE
                }
            )
        }
    }
}
