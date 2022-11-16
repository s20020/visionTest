package jp.ac.it_college.std.s20020.visiontest

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jp.ac.it_college.std.s20020.visiontest.databinding.FragmentEnTextBinding
import jp.ac.it_college.std.s20020.visiontest.databinding.FragmentSpeechBinding

class Speech : Fragment() {

    private var _binding: FragmentSpeechBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSpeechBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageButton.setOnClickListener{
            println("unko")
            val studyEnglish = StudyEnglish()
            studyEnglish.speechText()
        }

    }


}