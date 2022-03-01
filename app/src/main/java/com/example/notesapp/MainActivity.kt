package com.example.notesapp

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.SearchView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.ticket.view.*

class MainActivity : AppCompatActivity() {
    var listNotes=ArrayList<Note>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //listNotes.add(Note(1,"meet professor","This is the content text and it should be larger in order to see it's way of displaying on multiple rows"))
        //listNotes.add(Note(2,"meet doctor","This is the content text and it should be larger in order to see it's way of displaying on multiple rows"))
        //listNotes.add(Note(3,"meet friend","This is the content text and it should be larger in order to see it's way of displaying on multiple rows"))

        loadQuery("%")

    }

    override fun onResume() {
        super.onResume()
        loadQuery("%")
    }

    fun loadQuery(title:String){
        var dbManager = DBManager(this)
        val projections = arrayOf("ID","Title", "Description")
        val selectionArgs = arrayOf(title)
        val cursor = dbManager.query(projections, "Title like ?", selectionArgs, "Title")
        listNotes.clear()
        if(cursor.moveToFirst()){
            do {
                val ID = cursor.getInt(cursor.getColumnIndexOrThrow("ID"))
                val title = cursor.getString(cursor.getColumnIndexOrThrow("Title"))
                val description = cursor.getString(cursor.getColumnIndexOrThrow("Description"))
                listNotes.add(Note(ID,title,description))
                System.out.println(ID.toString()+title+description)
            }while (cursor.moveToNext())
        }
        var myNotesAdapter = MyNotesAdapter(this, listNotes)
        lvNotes.adapter = myNotesAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val sv=menu!!.findItem(R.id.app_bar_search).actionView as SearchView
        val sm=getSystemService(Context.SEARCH_SERVICE) as SearchManager
        sv.setSearchableInfo(sm.getSearchableInfo(componentName))
        sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                loadQuery("%"+query+"%")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId) {
            R.id.addNote->{
                var intent = Intent(this,AddNotes::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    inner class MyNotesAdapter : BaseAdapter {
        var listNotesAdapter = ArrayList<Note>()
        var context: Context? = null
        constructor(context:Context, listNotesAdapter:ArrayList<Note>):super(){
            this.listNotesAdapter = listNotesAdapter
            this.context = context
        }

        override fun getCount(): Int {
            return listNotes.size
        }

        override fun getItem(position: Int): Any {
            return listNotes[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var myView = layoutInflater.inflate(R.layout.ticket, null)
            var myNote = listNotes[position]
            myView.tvTitle.text = myNote.noteName
            myView.tvDes.text = myNote.noteDes
            myView.ivDelete.setOnClickListener (View.OnClickListener {
                var dbManager = DBManager(this.context!!)
                val selectionArgs = arrayOf(myNote.noteID.toString())
                dbManager.delete("ID=?",selectionArgs)
                loadQuery("%")
            })
            myView.ivEdit.setOnClickListener (View.OnClickListener {
                goToUpdate(myNote)
            })
            return myView
        }

    }

    fun goToUpdate(note:Note){
        var intent = Intent(this, AddNotes::class.java)
        intent.putExtra("ID",note.noteID)
        intent.putExtra("name",note.noteName)
        intent.putExtra("des",note.noteDes)
        startActivity(intent)
    }
}