package ru.sikuda.mobile.todo_compose.model

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.sikuda.mobile.todo_compose.BuildConfig
import java.io.File


class MainModel(application: Application) : AndroidViewModel(application) {

    private var _list: MutableLiveData<ArrayList<Note>> = MutableLiveData<ArrayList<Note>>()
    val list: LiveData<ArrayList<Note>>
        get() = _list

    //context
    private val context by lazy { getApplication<Application>().applicationContext }
    private val myDB = NoteDatabaseHelper(context)

    //current
    var index: Int = -1
    var tmpFile: File? = null

    init {
        _list.value = ArrayList<Note>()
        getAllNotes()
    }

    fun getNote(id: Int): Note {
        return _list.value?.get(id)!!
    }

    fun size(): Int{
        return _list.value!!.size
    }

    //ArrayList<Note>
    fun getAllNotes() {
        viewModelScope.launch {

            val notes: ArrayList<Note> = ArrayList<Note>()
            val cursor = myDB.readAllData()
            notes.clear()
            while (cursor!!.moveToNext()) {

                val note: Note = Note( cursor.getLong(0), cursor.getString(1),
                    cursor.getString(2), cursor.getString(3), cursor.getString(4))
                notes.add(note)
            }
            _list.value = notes
            return@launch
        }
    }

    fun updateNote(index: Int, id: String, date: String, content: String, detail: String, imagefile: String){

        _list.value?.set(index, Note(id.toLong(), date, content, detail, imagefile))
        viewModelScope.launch {
            myDB.updateNote( id, date, content, detail, imagefile )
        }
    }

    fun insertNote(date: String, content: String, detail: String, imagefile: String){
        viewModelScope.launch {
            myDB.addNote(date, content, detail, imagefile )
            getAllNotes()
        }
    }

    fun deleteNote(index: Int, id: Long){

        _list.value?.removeAt(index)
        viewModelScope.launch {
            myDB.deleteNote(id.toString())
        }
    }

    fun deleteAllNotes(){
        _list.value?.clear()
        viewModelScope.launch {
            myDB.deleteAllData()
        }
    }

    fun deleteTmpFile(){
        tmpFile?.delete()
        tmpFile = null;
    }

    fun getTmpFileUri(): Uri {
        tmpFile?.delete()
        tmpFile = File.createTempFile("tmp_image_file", ".png").apply {
            createNewFile()
            deleteOnExit()
        }
        return FileProvider.getUriForFile( context, "${BuildConfig.APPLICATION_ID}.provider", tmpFile!!)
    }

}


