package jp.ac.it_college.std.s20020.visiontest

import android.R
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import jp.ac.it_college.std.s20020.visiontest.databinding.ActivityFileSelectBinding
import jp.ac.it_college.std.s20020.visiontest.databinding.ActivityFolderSelectBinding

class FileSelect : AppCompatActivity() {
    private lateinit var binding : ActivityFileSelectBinding

    private lateinit var _helper: DatabaseHelper

    var FileItems = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFileSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val folder_name = intent.getStringExtra("Folder")
        var file_name = ""
        var _id = 0
        println(folder_name)



        //DatabaseHelperオブジェクトを生成
        _helper = DatabaseHelper(applicationContext)

        val select_file_name = """
            SELECT file_name FROM main
            WHERE folder_name = '${folder_name}'
        """.trimIndent()

        binding.backBtn3.setOnClickListener{
            finish()
            overridePendingTransition(jp.ac.it_college.std.s20020.visiontest.R.anim.slide_in_left, jp.ac.it_college.std.s20020.visiontest.R.anim.slide_out_right)
        }


        val db = _helper.writableDatabase
        val c = db.rawQuery(select_file_name, null)

        while(c.moveToNext()){
            val data = c.getColumnIndex("file_name")
            FileItems.add(c.getString(data))
        }

        binding.fileSelectList.adapter = ArrayAdapter(
            this,
            R.layout.simple_list_item_1,
            FileItems
        )

        //ファイルのリストが長押しされたとき、編集画面へ遷移
        binding.fileSelectList.setOnItemLongClickListener { parent, view, position, id ->
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("行う操作を選択してください")
            dialog.setPositiveButton("編集") { dialog, which ->
                val intent = Intent(this, Edit::class.java)
                file_name = binding.fileSelectList.getItemAtPosition(position).toString()

                //DatabaseHelperオブジェクトを生成
                _helper = DatabaseHelper(applicationContext)


                val db = _helper.writableDatabase

                val select = """
                SELECT _id FROM main
                WHERE folder_name = '${folder_name}'
                AND file_name = '${file_name}'
                """.trimIndent()

                val c = db.rawQuery(select, null)


                while (c.moveToNext()) {
                    val c_id = c.getColumnIndex("_id")
                    _id = c.getLong(c_id).toInt()
                }

                intent.putExtra("ID", _id)
                startActivity(intent)
                overridePendingTransition(jp.ac.it_college.std.s20020.visiontest.R.anim.slide_in_right, jp.ac.it_college.std.s20020.visiontest.R.anim.slide_out_left)

            }
            dialog.setNegativeButton("キャンセル", null)

            //削除するときの再確認のdialog
            dialog.setNeutralButton("削除") { dialog, which ->
                val dialog = AlertDialog.Builder(this)
                //選択したリストの文字列を取得
                file_name = binding.fileSelectList.getItemAtPosition(position).toString()
                dialog.setTitle("${folder_name}の${file_name}を削除しますか")
                dialog.setPositiveButton("はい") { dialog, which ->

                    //はい。で、DBからそのファイルを消す
                    //一旦、そのファイルのIDを取得して、いく。
                    val select = """
                    SELECT _id FROM main
                    WHERE folder_name = '${folder_name}'
                    AND file_name = '${file_name}'
                    """.trimIndent()

                    val c = db.rawQuery(select, null)


                    while(c.moveToNext()){
                        val c_id = c.getColumnIndex("_id")
                        _id = c.getLong(c_id).toInt()
                    }

                    println(_id)

                    db.delete("main", "_id = ?", arrayOf(_id.toString()))




                    //DBから削除したあと再表示する
                   FileItems.remove(file_name)

                    binding.fileSelectList.adapter = ArrayAdapter(
                        this,
                        R.layout.simple_list_item_1,
                        FileItems
                    )



                }
                dialog.setNegativeButton("キャンセル", null)
                dialog.show()

            }
            dialog.show()
            true
        }

        binding.fileSelectList.setOnItemClickListener{ adapterView, view, position, id ->
            val intent = Intent(this, Order::class.java)
            file_name = binding.fileSelectList.getItemAtPosition(position).toString()


            //DatabaseHelperオブジェクトを生成
            _helper = DatabaseHelper(applicationContext)


            val db = _helper.writableDatabase

            val select = """
            SELECT _id FROM main
            WHERE folder_name = '${folder_name}'
            AND file_name = '${file_name}'
        """.trimIndent()

            val c = db.rawQuery(select, null)


            while(c.moveToNext()){
                val c_id = c.getColumnIndex("_id")
                _id = c.getLong(c_id).toInt()
            }

            println(_id)

            intent.putExtra("ID", _id)
            startActivity(intent)
            overridePendingTransition(jp.ac.it_college.std.s20020.visiontest.R.anim.slide_in_right, jp.ac.it_college.std.s20020.visiontest.R.anim.slide_out_left)



        }
    }

    override fun onDestroy() {
        _helper.close()
        super.onDestroy()
    }


}



