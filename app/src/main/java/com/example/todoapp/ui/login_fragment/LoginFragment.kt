package com.example.todoapp.ui.login_fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.todoapp.databinding.FragmentLoginBinding
import com.example.todoapp.shared_preferences.SharedPreferencesHelper
import com.example.todoapp.ui.MainViewModel
import com.example.todoapp.utils.factory
import com.example.todoapp.utils.internet_connection.ConnectivityObserver
import com.example.todoapp.utils.internet_connection.NetworkConnectivityObserver
import com.example.todoapp.utils.localeLazy
import com.yandex.authsdk.YandexAuthException
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthSdk
import com.yandex.authsdk.internal.strategy.LoginType


class LoginFragment : Fragment() {


    private val sharedPreferencesHelper: SharedPreferencesHelper by localeLazy()

    private val viewModel: MainViewModel by activityViewModels { factory() }

    private var binding: FragmentLoginBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentLoginBinding.inflate(layoutInflater).also { binding = it }.root


    private lateinit var sdk: YandexAuthSdk
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sdk = YandexAuthSdk(
            requireContext(), YandexAuthOptions(requireContext(), true)
        )
        val sdk = YandexAuthSdk(requireContext(), YandexAuthOptions(requireContext(), true))

        val loginOptionsBuilder = YandexAuthLoginOptions.Builder().setLoginType(LoginType.NATIVE)
        val intent = sdk.createLoginIntent(loginOptionsBuilder.build())

        views {
            loginWithYandexButton.setOnClickListener {
                startActivityForResult(intent, 1)
            }
            loginButton.setOnClickListener {
                if(sharedPreferencesHelper.getToken() != "unaffordable") {
                    viewModel.deleteAll()
                    sharedPreferencesHelper.putToken("unaffordable")
                    sharedPreferencesHelper.putRevision(0)
                }
                moveToTasks()
            }
        }
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        if (requestCode == 1) {
            try {
                val yandexAuthToken = sdk.extractToken(resultCode, data)
                if (yandexAuthToken != null) {
                    val curToken = yandexAuthToken.value
                    if(curToken != sharedPreferencesHelper.getToken()) {
                        viewModel.deleteAll()
                        sharedPreferencesHelper.putToken(yandexAuthToken.value)
                        sharedPreferencesHelper.putRevision(0)
                    }
                    moveToTasks()
                }
            } catch (e: YandexAuthException) {
                Toast.makeText(requireContext(), e.message.toString(), Toast.LENGTH_SHORT).show()
            }
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun moveToTasks() {
        val action = LoginFragmentDirections.actionMainTasks()
        findNavController().navigate(action)
    }

    private fun <T : Any> views(block: FragmentLoginBinding.() -> T): T? = binding?.block()


}