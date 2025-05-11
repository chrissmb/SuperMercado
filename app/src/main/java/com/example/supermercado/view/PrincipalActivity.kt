package com.example.supermercado.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.supermercado.MainActivity
import com.example.supermercado.R
import com.example.supermercado.databinding.ActivityPrincipalBinding
import com.example.supermercado.util.MessageUtil
import com.example.supermercado.util.SharedViewModel
import com.example.supermercado.util.TokenCacheUtil
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PrincipalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPrincipalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_principal)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
               R.id.navigation_purchase_pending,
                R.id.navigation_purchase_cart
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        sessionControl()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_refresh) {
            refreshList()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun refreshList() {
        val sharedViewModel = ViewModelProvider(this).get(SharedViewModel::class.java)
        sharedViewModel.triggerRefreshAction()
    }

    private fun sessionControl() {
        lifecycleScope.launch {
            while (true) {
                delay(10000L)
                if (!TokenCacheUtil.isTokenExpired()) {
                    continue
                }
                val messageExpiredSession = getString(R.string.expired_session)
                MessageUtil.showMessage(messageExpiredSession, this@PrincipalActivity) {
                    val intent = Intent(this@PrincipalActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
                break
            }
        }
    }
}