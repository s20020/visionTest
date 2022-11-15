package jp.ac.it_college.std.s20020.visiontest

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jp.ac.it_college.std.s20020.visiontest.databinding.FragmentEnTextBinding
import jp.ac.it_college.std.s20020.visiontest.databinding.FragmentJaTextBinding

class JaText : Fragment() {

    private var _binding: FragmentJaTextBinding? = null
    private val binding get() = _binding!!

    var text = ""

    val test1 = requireArguments().getString("key")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentJaTextBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textView2.text = test1
//        putText()

    }

    override fun onDestroyView(){

        super.onDestroyView()
        _binding = null

    }

//    fun putText(){
//        val test1 = requireArguments().getString("key")
//        binding.textView2.text = test1
//    }




}