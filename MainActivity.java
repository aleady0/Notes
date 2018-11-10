package com.example.dima.notes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NotesAdapter.ItemClickListener, NotesAdapter.ItemLongClickListener {

    private static final String SHARED_NOTES = "shared notes";
    private static final String NOTES = "notes";
    private static final String NOTE_POSITION = "note_position";
    private static final String NOTE_TEXT = "note_text";
    NotesAdapter notesAdapter;
    ArrayList<String> notes;
    RecyclerView notesList;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        notes = new ArrayList<>();
        prefs = getSharedPreferences(SHARED_NOTES, Context.MODE_PRIVATE);
        try {
            notes = (ArrayList<String>) ObjectSerializer.deserialize(prefs.getString(NOTES, ObjectSerializer.serialize(new ArrayList<String>())));
        } catch (IOException e) {
            e.printStackTrace();
        }



        notesList = findViewById(R.id.notesList);
        notesList.setLayoutManager(new LinearLayoutManager(this));
        notesAdapter = new NotesAdapter(this,notes);
        notesAdapter.setClickListener(this);
        notesAdapter.setLongClickListener(this);
        notesList.setAdapter(notesAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.new_item:
                editNote(-1);

        }


        return super.onOptionsItemSelected(item);
    }

    private void editNote(int i) {
        Intent intent = new Intent(this, ItemActivity.class);
        intent.putExtra(NOTE_POSITION, i);
        if (i >= 0) {
            intent.putExtra(NOTE_TEXT, notesAdapter.getItem(i));
        } else {
            intent.putExtra(NOTE_TEXT, "");
        }
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) return;
        String textItem = data.getStringExtra("textItem");
        int posItem = data.getIntExtra("posItem",-1);
        if (posItem < 0) {
            notes.add(textItem);
        }  else {
            notes.set(posItem,textItem);
        }
        notesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(View view, int position) {
        //   Toast.makeText(this, "You clicked " + notesAdapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
        editNote(position);
    }

    @Override
    public void onItemLongClick(View view, final int position) {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Are you?")
                .setMessage("Are you ure want to delete this item?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        notes.remove(position);
                        notesAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show()
                ;


}

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences prefs = getSharedPreferences(SHARED_NOTES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        try {
            editor.putString(NOTES, ObjectSerializer.serialize(notes));
        } catch (IOException e) {
            e.printStackTrace();
        }
        editor.apply();
    }
}
