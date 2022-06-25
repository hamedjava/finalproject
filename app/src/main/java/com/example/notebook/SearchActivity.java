package com.example.notebook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    private static final String TAG  = "LOG====>";
    EditText edt_search_note;
    Button btn_search_note;
    RecyclerView rv_search_note;
    NoteDatabase noteDatabase;
    NoteAdapter adapter;
    NoteModel noteModel;
    ArrayList<NoteModel> notes;
    Thread thread;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setupViews();




        btn_search_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Cursor cursor = noteDatabase.searchInfo(Integer.parseInt(edt_search_note.getText().toString()));

                for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
                    noteModel = new NoteModel();
                    noteModel.setId(cursor.getInt(0));
                    noteModel.setTitle(cursor.getString(1));
                    noteModel.setDesc(cursor.getString(2));
                    noteModel.setRemember(cursor.getInt(3));
                    noteModel.setDate(cursor.getString(4));
                    noteModel.setTime(cursor.getString(5));
                    notes.add(noteModel);
                    Log.i(TAG, ">>>>>>>>>>>");
                }
                adapter = new NoteAdapter(SearchActivity.this,notes);
                rv_search_note.setLayoutManager(new LinearLayoutManager(SearchActivity.this,RecyclerView.VERTICAL,false));
                rv_search_note.setAdapter(adapter);
            }
        });


    }
    public void setupViews(){
        noteDatabase = new NoteDatabase(this);
        btn_search_note = (Button) findViewById(R.id.btn_search_note);
        edt_search_note = (EditText) findViewById(R.id.edt_search_note);
        rv_search_note = (RecyclerView) findViewById(R.id.rv_search_note);
        notes = new ArrayList<>();

    }
}