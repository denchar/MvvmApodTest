package com.example.mvvmApodTest

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.mvvmApodTest.databinding.FragmentExampleBinding.*
import com.example.mvvmApodTest.databinding.FragmentSecondBinding


class SecondFragment : Fragment(R.layout.fragment_second) {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.button.setOnClickListener {
           // Navigation.findNavController(view).navigate(R.id.navigateToExampleFragment)
            findNavController().navigate(R.id.action_secondFragment_to_exampleFragment)
        }
        // Inflate the layout for this fragment
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}