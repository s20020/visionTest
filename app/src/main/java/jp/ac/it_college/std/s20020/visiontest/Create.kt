package jp.ac.it_college.std.s20020.visiontest

import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.content.Intent.ACTION_OPEN_DOCUMENT
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import jp.ac.it_college.std.s20020.visiontest.databinding.ActivityCreateBinding
import jp.ac.it_college.std.s20020.visiontest.databinding.ActivityMainBinding

class Create : AppCompatActivity() {
    private lateinit var binding: ActivityCreateBinding

    private var filepathUri: Uri? = null

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
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
        val imageBitmap = BitmapFactory.decodeFile(filepath)
        binding.imageView.setImageBitmap(imageBitmap) // 本当なら OCR に回す
    }

    private val fileLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        // ファイル情報は result.data.data にある。

        // ContentResolver に uri を渡して、InputStream を取得する → use 関数内でそのまま使用する
        contentResolver.openInputStream(result?.data?.data ?: throw IllegalStateException("hoge")).use {
            // ラムダ式のデフォルトパラメータ名(it) で ファイルへの InputStream を参照できるので
            // BitmapFactory の decodeStream で Bitmap データを生成してもらう
            val imageBitmap = BitmapFactory.decodeStream(it)

            binding.imageView.setImageBitmap(imageBitmap) // OCRなどにデータを回す
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener{
            finish()
        }

        binding.mycreateBtn.setOnClickListener{
            val intent = Intent(this, MyCreate::class.java)
            startActivity(intent)
        }

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
}