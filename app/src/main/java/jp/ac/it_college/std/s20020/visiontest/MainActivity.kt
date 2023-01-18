package jp.ac.it_college.std.s20020.visiontest

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.gson.*
import jp.ac.it_college.std.s20020.visiontest.databinding.ActivityMainBinding
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
//    //認証　FirebaseAuth インスタンスを宣言します。
//    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        // 1.おすすめ
        val intent = Intent(this, StudyOrCreate::class.java)
        startActivity(intent)
        overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out)

        return true
    }
}

