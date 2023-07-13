package com.example.todoapp.ui.view.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.todoapp.App
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentSettingsBinding
import com.example.todoapp.domain.model.Mode
import com.example.todoapp.utils.SharedPreferencesHelper
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import javax.inject.Inject

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding


    @Inject
    lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireContext().applicationContext as App).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)

        when(sharedPreferencesHelper.getMode()){
            Mode.NIGHT -> binding.themeName.text = "Тёмная"
            Mode.LIGHT -> binding.themeName.text = "Светлая"
            Mode.SYSTEM -> binding.themeName.text = "Из настроек системы"
        }

        binding.theme.setOnClickListener {
            BottomSheetFragment(object : ThemeInterface{
                override fun changeTheme() {
                    when(sharedPreferencesHelper.getMode()){
                        Mode.NIGHT -> binding.themeName.text = "Тёмная"
                        Mode.LIGHT -> binding.themeName.text = "Светлая"
                        Mode.SYSTEM -> binding.themeName.text = "Из настроек системы"
                    }
                }

            }).show(parentFragmentManager, "change_task")
        }

        binding.logOut.setOnClickListener {
            val builder = MaterialAlertDialogBuilder(
                ContextThemeWrapper(
                    context,
                    R.style.AlertDialogCustom
                )
            )
            builder.apply {
                val title = "Вы уверены, что хотите выйти? Возможна потеря данных оффлайн режима."
                setMessage(title)
                setPositiveButton(
                    "Выйти"
                ) { _, _ ->

                    val action = SettingsFragmentDirections.actionLogOut()
                    findNavController().navigate(action)
                }
            }
            builder.show()
                .create()
        }

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }

    interface ThemeInterface {
        fun changeTheme()
    }

}