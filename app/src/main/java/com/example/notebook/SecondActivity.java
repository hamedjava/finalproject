package com.example.notebook;;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextSwitcher;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SecondActivity extends AppCompatActivity {

    NoteDatabase myDatabase;
    RecyclerView recyclerView;
    private List<NoteModel> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        setupView();
        getDataFromDB();
        recyclerView.setAdapter(new NoteAdapter(SecondActivity.this,dataList));

      /* String username= getIntent().getExtras().getString("username");
       String pass= getIntent().getExtras().getString("pass");
       int digit=getIntent().getExtras().getInt("digit");*/


    }

    public void setupView() {
        dataList=new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.rv_second_noteList);
        recyclerView.setLayoutManager(new LinearLayoutManager(SecondActivity.this));

    }

    public void getDataFromDB() {
        myDatabase = new NoteDatabase(getApplicationContext());
        Cursor cursor = myDatabase.getInfos();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            NoteModel noteModel=new NoteModel();
            noteModel.setId(cursor.getInt(0));
            noteModel.setTitle(cursor.getString(1));
            noteModel.setDesc(cursor.getString(2));
            noteModel.setRemember(cursor.getInt(3));
            noteModel.setDate(cursor.getString(4));
            noteModel.setTime(cursor.getString(5));

            dataList.add(noteModel);

        }
    }

    public void getSomeInfoFromDB(int id){
        myDatabase.getSomeInfo(id);

    }
}
