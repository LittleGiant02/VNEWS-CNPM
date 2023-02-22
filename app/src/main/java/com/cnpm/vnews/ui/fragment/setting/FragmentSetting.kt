package com.cnpm.vnews.ui.fragment.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.cnpm.vnews.R
import com.cnpm.vnews.const_util.backPressDispatcher
import com.cnpm.vnews.databinding.FragmentSettingBinding

class FragmentSetting : Fragment() {
    companion object {
        private fun newInstance() = FragmentSetting()
        fun openWith(fragmentManager: FragmentManager) {
            fragmentManager.beginTransaction()
                .replace(
                    R.id.frame_child,
                    newInstance(), "setting"
                )
                .addToBackStack(null).commit()
        }
    }

    private lateinit var binding: FragmentSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonClose.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        backPressDispatcher(requireActivity(), viewLifecycleOwner)
    }
}
