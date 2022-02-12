package com.binar.sciroper.ui.fragments.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.binar.sciroper.data.local.AppSharedPreference
import com.binar.sciroper.databinding.FragmentSettingBinding
import com.binar.sciroper.ui.activity.MainActivity
import com.binar.sciroper.util.App


class SettingFragment : Fragment() {
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!
    private val settingVm: SettingVm by viewModels {
        SettingVmFactory(App.appDb.getUserDao(), AppSharedPreference)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            vm = settingVm
            lifecycleOwner = viewLifecycleOwner
            settingFragment = this@SettingFragment
        }

        settingVm.isChecked.observe(viewLifecycleOwner) {
            if (it) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                settingVm.setTheme(it)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                settingVm.setTheme(it)
            }
        }

        settingVm.isCheckedMusic.observe(viewLifecycleOwner) {
            if (it && !(activity as MainActivity).isMusicPlay) {
                playMusic(requireContext())
                Toast.makeText(requireContext(), "music ON", Toast.LENGTH_SHORT).show()
                settingVm.setMusic(it)
                (activity as MainActivity).isMusicPlay = true

            } else if (!it && (activity as MainActivity).isMusicPlay) {
                pausePlay()
                settingVm.setMusic(it)
                (activity as MainActivity).isMusicPlay = false
            }
        }



        settingVm.isCheckedNotif.observe(viewLifecycleOwner) {
            if (it) {
                settingVm.setNotif(it)
            } else {
                settingVm.setNotif(it)
            }
        }

    }

    fun navToMenu() {
        val action = SettingFragmentDirections.actionSettingFragmentToMenuFragment()
        findNavController().navigate(action)
    }

    fun navToProfile() {
        val action = SettingFragmentDirections.actionSettingFragmentToProfileFragment()
        findNavController().navigate(action)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}