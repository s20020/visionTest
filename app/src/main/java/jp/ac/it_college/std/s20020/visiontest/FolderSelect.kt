package jp.ac.it_college.std.s20020.visiontest

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import jp.ac.it_college.std.s20020.visiontest.databinding.ActivityFolderSelectBinding

class FolderSelect : AppCompatActivity() {
    private lateinit var binding: ActivityFolderSelectBinding

    private lateinit var _helper: DatabaseHelper

    var FolderItems = arrayListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFolderSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var folder_name = ""

        //DatabaseHelperオブジェクトを生成
        _helper = DatabaseHelper(applicationContext)

        val select_folder_name = """
            SELECT distinct folder_name FROM main
        """.trimIndent()


        binding.backBtn2.setOnClickListener{
            val intent = Intent(this, StudyOrCreate::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }



        val db = _helper.writableDatabase
        val c = db.rawQuery(select_folder_name, null)

        while(c.moveToNext()){
            val data = c.getColumnIndex("folder_name")
            FolderItems.add(c.getString(data))
        }

        binding.folderSelectList.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            FolderItems
        )

        binding.folderSelectList.setOnItemClickListener{ adapterView, view, position, id ->
            val intent = Intent(this, FileSelect::class.java)
            val select_folder = binding.folderSelectList.getItemAtPosition(position).toString()
            intent.putExtra("Folder", select_folder)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        binding.folderSelectList.setOnItemLongClickListener { parent, view, position, id ->
            val dialog = AlertDialog.Builder(this)

            folder_name = binding.folderSelectList.getItemAtPosition(position).toString()

            dialog.setTitle("${folder_name}を削除しますか？\nフォルダごと削除されます。")
            dialog.setPositiveButton("確定") { dialog, which ->

                //削除を完了すると、folder_nameが一致するものをすべて消去する
                db.delete("main", "folder_name = ?", arrayOf(folder_name))
                FolderItems.remove(folder_name)

                binding.folderSelectList.adapter = ArrayAdapter(
                    this,
                    android.R.layout.simple_list_item_1,
                    FolderItems
                )
            }
            dialog.setNegativeButton("キャンセル", null)

            dialog.show()
            true
        }


    }

    override fun onDestroy() {
        _helper.close()
        super.onDestroy()
    }
}