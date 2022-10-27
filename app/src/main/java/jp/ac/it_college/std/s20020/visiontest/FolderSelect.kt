package jp.ac.it_college.std.s20020.visiontest

import android.content.Intent
import android.os.Bundle
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
            finish()
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
        }

        binding.folderSelectList.setOnItemLongClickListener { parent, view, position, id ->
            val dialog = AlertDialog.Builder(this)

            folder_name = binding.folderSelectList.getItemAtPosition(position).toString()

            dialog.setTitle("フォルダを削除すると、フォルダ内のファイルもすべて削除されます。\nよろしいですか。")
            dialog.setPositiveButton("消去") { dialog, which ->

                //削除を完了すると、folder_nameが一致するものをすべて消去する
                db.delete("main", "folder_name = ?", arrayOf(folder_name))
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