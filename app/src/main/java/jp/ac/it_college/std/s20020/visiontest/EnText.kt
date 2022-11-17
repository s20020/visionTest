package jp.ac.it_college.std.s20020.visiontest

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import jp.ac.it_college.std.s20020.visiontest.databinding.FragmentEnTextBinding


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


        val intent = Intent(activity, StudyEnglish::class.java)
        intent.putExtra("speech_text", test1)

        val studyEnglish = StudyEnglish()
        studyEnglish.speech_text = test1.toString()
        if(studyEnglish.speech_text == "") println("Wow")
        println(studyEnglish.speech_text)

    }

    override fun onDestroyView(){
        super.onDestroyView()
        _binding = null

    }

    fun sendText() {

    }

}