package com.example.todoapp.ui.view.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.todoapp.App
import com.example.todoapp.databinding.FragmentLoginBinding
import com.example.todoapp.ui.stateholders.LoginViewModel
import com.example.todoapp.utils.SharedPreferencesHelper
import com.yandex.authsdk.YandexAuthException
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthSdk
import javax.inject.Inject


class LoginFragment : Fragment() {


    @Inject
    lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    @Inject
    lateinit var sdk : YandexAuthSdk

    private val viewModel: LoginViewModel by viewModels {
        (requireContext().applicationContext as App).appComponent.viewModelsFactory()
    }

    private var binding: FragmentLoginBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentLoginBinding.inflate(layoutInflater).also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireContext().applicationContext as App).appComponent.loginFragmentComponentBuilder().create().inject(this)

        val register: ActivityResultLauncher<Intent> = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == -1) {
                val data: Intent? = result.data
                if (data != null) {
                    try {
                        val yandexAuthToken = sdk.extractToken(result.resultCode, data)
                        if (yandexAuthToken != null) {
                            val curToken = yandexAuthToken.value
                            if (curToken != sharedPreferencesHelper.getToken()) {
                                sharedPreferencesHelper.putToken(curToken)
                                sharedPreferencesHelper.putRevision(0)
                                viewModel.deleteCurrentItems()
                            }
                            moveToTasks()
                        }
                    } catch (exception: YandexAuthException) {
                        Toast.makeText(context, exception.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        views {
            loginWithYandexButton.setOnClickListener {
                register.launch(sdk.createLoginIntent(YandexAuthLoginOptions.Builder().build()))
            }
            loginButton.setOnClickListener {
                if(sharedPreferencesHelper.getToken() != "unaffordable") {
                    sharedPreferencesHelper.putToken("unaffordable")
                    sharedPreferencesHelper.putRevision(0)
                    viewModel.deleteCurrentItems()
                }
                moveToTasks()
            }
        }
    }

    private fun moveToTasks() {
        val action = LoginFragmentDirections.actionMainTasks()
        findNavController().navigate(action)
    }

    private fun <T : Any> views(block: FragmentLoginBinding.() -> T): T? = binding?.block()


}
