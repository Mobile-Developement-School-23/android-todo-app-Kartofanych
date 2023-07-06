package com.example.todoapp.ui.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.todoapp.App
import com.example.todoapp.R
import com.example.todoapp.utils.MyWorkManager
import com.example.todoapp.utils.SharedPreferencesHelper
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class MainActivity : AppCompatActivity() {


    private lateinit var controller: NavController


    @Inject
    lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (applicationContext as App).appComponent.inject(this)
        controller = getRootNavController()


        if (savedInstanceState != null) {
            controller.restoreState(savedInstanceState.getBundle("state"))
        } else {
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.fragment_container)
                        as NavHostFragment
            val graphInflater = navHostFragment.navController.navInflater
            val navGraph = graphInflater.inflate(R.navigation.tasks_navigation)

            controller = navHostFragment.navController
            val destination = if (sharedPreferencesHelper.getToken() == "no_token"
            ) R.id.loginFragment else R.id.tasks_fragment

            navGraph.setStartDestination(destination)

            controller.graph = navGraph
        }
    }

    private fun getRootNavController(): NavController {
        val navHost = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        return navHost.navController
    }

    override fun onDestroy() {
        super.onDestroy()
        periodicUpdate()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBundle("state",controller.saveState())
    }

    private fun periodicUpdate() {
        val constraints: Constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()


        val myWorkRequest = PeriodicWorkRequest.Builder(
            MyWorkManager::class.java,
            8,
            TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .addTag("update_data")
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "update_data",
            ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
            myWorkRequest
        )
    }


}