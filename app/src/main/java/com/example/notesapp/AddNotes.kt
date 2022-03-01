package com.example.notesapp

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_notes.*

class AddNotes : AppCompatActivity() {
    val dbTable = "Notes"
    var id=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_notes)

        var bundle:Bundle? = intent.extras
        try {
            id=bundle!!.getInt("ID")
            if(id!=0) {
                etTitle.setText (bundle!!.getString("name").toString())
                etDes.setText (bundle!!.getString("des").toString())
            }
        }catch(e:Exception){}
    }

    fun buAdd(view: View) {
        var dbManager = DBManager(this)
        var values = ContentValues()
        values.put("Title", etTitle.text.toString())
        values.put("Description", etDes.text.toString())
        if(id==0)
        {
            var ID = dbManager.insert(values)
            if(ID > 0){
                Toast.makeText(this,"note is added",Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this,"cannot add notes",Toast.LENGTH_LONG).show()
            }
        }
        else{
            var selectionArgs=arrayOf(id.toString())
            val ID = dbManager.update(values,"Id=?",selectionArgs)
        }

        finish()
    }
}