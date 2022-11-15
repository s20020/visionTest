package jp.ac.it_college.std.s20020.visiontest

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jp.ac.it_college.std.s20020.visiontest.databinding.FragmentEnTextBinding
import jp.ac.it_college.std.s20020.visiontest.databinding.FragmentJaTextBinding

class EnText : Fragment() {

    private var _binding: FragmentEnTextBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEnTextBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val test1 = requireArguments().getString("key")
        binding.textView.text = test1
    }

    override fun onDestroyView(){
        super.onDestroyView()
        _binding = null

    }

}