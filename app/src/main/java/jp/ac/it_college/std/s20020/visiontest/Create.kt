package jp.ac.it_college.std.s20020.visiontest

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.content.Intent.ACTION_OPEN_DOCUMENT
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.gson.*
import jp.ac.it_college.std.s20020.visiontest.databinding.ActivityCreateBinding
import jp.ac.it_college.std.s20020.visiontest.databinding.ActivityMainBinding
import java.io.ByteArrayOutputStream
import java.util.ArrayList

class Create : AppCompatActivity() {
    private lateinit var binding: ActivityCreateBinding

    lateinit var list : ArrayList<String>

    private var filepathUri: Uri? = null

    lateinit var imageBitmap: Bitmap


    private lateinit var functions: FirebaseFunctions
    //認証　FirebaseAuth インスタンスを宣言します。
    private lateinit var auth: FirebaseAuth

    @SuppressLint("WrongThread")


    //カメラでとって、つくるとき
    val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode != RESULT_OK) return@registerForActivityResult

        //////// ファイルパスを取ってくる ///////

        // 後でファイルパス情報のカラム名を指定する必要があるので用意(固定)
        val columns = arrayOf(MediaStore.Images.Media.DATA)

        // カーソルでやり取りする形式で、ファイルの情報を取得依頼
        val cursor = contentResolver.query(
            filepathUri ?: throw IllegalStateException("hoge"), columns, null, null, null
        ) ?: return@registerForActivityResult

        // 1件だけだけど、共通インタフェースのため一応 moveToFirst 必須
        cursor.moveToFirst()
        // カラム名を指定して string 型でファイルパスを取り出す
        val filepath = cursor.getString(
            cursor.getColumnIndexOrThrow(
                MediaStore.Images.Media.DATA
            )
        )
        // お作法的に必ず close する
        cursor.close()

        // ファイルパス(String) を使って、画像を読み込んで Bitmap データをつくる
        imageBitmap = BitmapFactory.decodeFile(filepath)
        binding.imageView.setImageBitmap(imageBitmap) // 本当なら OCR に回す
        buttonClicked()
    }



    //フォルダから写真を選択して、作るとき
    private val fileLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        // ファイル情報は result.data.data にある。

        // ContentResolver に uri を渡して、InputStream を取得する → use 関数内でそのまま使用する
        contentResolver.openInputStream(result?.data?.data ?: throw IllegalStateException("hoge")).use {
            // ラムダ式のデフォルトパラメータ名(it) で ファイルへの InputStream を参照できるので
            // BitmapFactory の decodeStream で Bitmap データを生成してもらう
            imageBitmap = BitmapFactory.decodeStream(it)
            binding.imageView.setImageBitmap(imageBitmap) // OCRなどにデータを回す
            buttonClicked()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        list = arrayListOf<String>()

        /* 認証　onCreate() メソッドで、FirebaseAuth インスタンスを初期化します。 */
        auth = Firebase.auth
        functions = Firebase.functions


        binding.backBtn.setOnClickListener{
            finish()
        }

        binding.mycreateBtn.setOnClickListener{
            val intent = Intent(this, MyCreate::class.java)
            startActivity(intent)
        }



        //写真を取る処理
        binding.takeBtn.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("写真をとりますか？")

            // OKボタン押したときの処理
            dialog.setPositiveButton("はい", DialogInterface.OnClickListener { _, _ ->
                val options = ContentValues()
                // ファイル名?っぽいけど効いていない
                options.put(MediaStore.Images.Media.TITLE, "ocr_target.jpeg")
                // ファイルタイプ
                options.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                // ContentResolver にお願いして、ファイルの保存場所の確保
                filepathUri =
                    contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, options)

                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                    putExtra(MediaStore.EXTRA_OUTPUT, filepathUri)
                }
                cameraLauncher.launch(intent)


            })


            dialog.setNegativeButton("キャンセル", null)
            dialog.show()
        }


        //画像から選ぶ処理
        binding.chooseBtn.setOnClickListener{
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("画像を選択しますか？")

            // OKボタン押したときの処理
            dialog.setPositiveButton("はい", DialogInterface.OnClickListener { _, _ ->

                val intent = Intent(ACTION_OPEN_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)   // 読み込み可能なものだけ?
                    type = "image/jpeg" // タイプ指定で画像の種類を絞れる?
                }
                fileLauncher.launch(intent)
            })


            dialog.setNegativeButton("キャンセル", null)
            dialog.show()
        }




    }

    //現在の認証を確認する
    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser

        //現在のユーザーがいないときｓ２００２０アカウントでログインする
        if (currentUser == null) {
            auth.signInWithEmailAndPassword("s20020@std.it-college.ac.jp", "taiga6666")
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d(ContentValues.TAG, "signInWithEmail: success")
                    } else {
                        Log.d(ContentValues.TAG, "signInWithEmail: login failure")
                    }
                }
        }

    }

    fun buttonClicked() {
        //認識する画像を入れ込む
//        var bitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.my_photo_11)
        val byteArrayOutputStream = ByteArrayOutputStream()
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val imageBytes: ByteArray = byteArrayOutputStream.toByteArray()
        val base64encoded = Base64.encodeToString(imageBytes, Base64.NO_WRAP)

        val request = JsonObject()
        val image = JsonObject()
        image.add("content", JsonPrimitive(base64encoded))
        request.add("image", image)
        val feature = JsonObject()
        feature.add("type", JsonPrimitive("TEXT_DETECTION"))
        val features = JsonArray()
        features.add(feature)
        request.add("features", features)
        val imageContext = JsonObject()
        val languageHints = JsonArray()
        languageHints.add("en")
        languageHints.add("ja")
        imageContext.add("languageHints", languageHints)
        request.add("imageContext", imageContext)

        annotateImage(request.toString())
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.d("s", "sippai")
                    return@addOnCompleteListener
                } else {
                    val annotation =
                        task.result!!.asJsonArray[0].asJsonObject["fullTextAnnotation"].asJsonObject
//                    binding.resultText.text = annotation["text"].asString
                    println(annotation["text"].asString)

                    for (page in annotation["pages"].asJsonArray) {
                        var pageText = ""
                        for (block in page.asJsonObject["blocks"].asJsonArray) {
                            var blockText = ""
                            for (para in block.asJsonObject["paragraphs"].asJsonArray) {
                                var paraText = ""
                                for (word in para.asJsonObject["words"].asJsonArray) {
                                    var wordText = ""
                                    for (symbol in word.asJsonObject["symbols"].asJsonArray) {
                                        wordText += symbol.asJsonObject["text"].asString
                                        System.out.format(
                                            "Symbol text: %s (confidence: %f)%n",
                                            symbol.asJsonObject["text"].asString,
                                            symbol.asJsonObject["confidence"].asFloat
                                        )
                                    }
                                    System.out.format(
                                        "Word text: %s (confidence: %f)%n%n", wordText,
                                        word.asJsonObject["confidence"].asFloat
                                    )
                                    System.out.format(
                                        "Word bounding box: %s%n",
                                        word.asJsonObject["boundingBox"]
                                    )
                                    paraText = String.format("%s%s ", paraText, wordText)
                                }
                                System.out.format("%nParagraph: %n%s%n", paraText)
                                val a = paraText.replace("\\s+".toRegex(), " ")
                                list.add(a)
                                println(list)


                                System.out.format(
                                    "Paragraph bounding box: %s%n",
                                    para.asJsonObject["boundingBox"]
                                )
                                System.out.format(
                                    "Paragraph Confidence: %f%n",
                                    para.asJsonObject["confidence"].asFloat
                                )
                                blockText += paraText
                            }
                            pageText += blockText

                            Log.d("Functions", pageText)
                        }
                    }
                }
            }

    }

    private fun annotateImage(requestJson: String): Task<JsonElement> {
        return functions
            .getHttpsCallable("annotateImage")
            .call(requestJson)
            .continueWith { task ->
                val result = task.result?.data
                JsonParser.parseString(Gson().toJson(result))
            }
    }
}