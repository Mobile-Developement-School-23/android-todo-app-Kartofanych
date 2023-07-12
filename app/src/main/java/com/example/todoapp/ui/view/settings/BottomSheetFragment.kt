package com.example.todoapp.ui.view.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.todoapp.App
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentBottomSheetBinding
import com.example.todoapp.domain.model.Mode
import com.example.todoapp.utils.SharedPreferencesHelper
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import javax.inject.Inject


class BottomSheetFragment(private val callback: SettingsFragment.ThemeInterface) : BottomSheetDialogFragment() {

    @Inject
    lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    private lateinit var binding:FragmentBottomSheetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireContext().applicationContext as App).appComponent.inject(this)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBottomSheetBinding.inflate(layoutInflater, container, false)

        when(sharedPreferencesHelper.getMode()){
            Mode.NIGHT -> binding.darkButton.isChecked = true
            Mode.LIGHT -> binding.lightButton.isChecked = true
            Mode.SYSTEM -> binding.systemButton.isChecked = true
        }

        binding.radioGroup.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.light_button -> {
                    sharedPreferencesHelper.setMode(Mode.LIGHT)
                }
                R.id.dark_button -> {
                    sharedPreferencesHelper.setMode(Mode.NIGHT)
                }
                R.id.system_button -> {
                    sharedPreferencesHelper.setMode(Mode.SYSTEM)
                }
            }
            callback.changeTheme()
            dismiss()
        }

        return binding.root
    }

    override fun getTheme() = R.style.CustomBottomSheetDialogTheme

}