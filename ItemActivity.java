package com.example.dima.notes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class ItemActivity extends AppCompatActivity {

    EditText editNote;
    int position;
    String note_txt;
    private static final String NOTE_POSITION = "note_position";
    private static final String NOTE_TEXT = "note_text";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        Intent intent = getIntent();
        Bundle bundle =  intent.getExtras();
        if (bundle != null) {
            position = intent.getIntExtra(NOTE_POSITION,-1);
            note_txt = intent.getStringExtra(NOTE_TEXT);
        }

        editNote = findViewById(R.id.editNote);
        editNote.setText(note_txt);
        editNote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Intent intent = new Intent();
                intent.putExtra("textItem", editNote.getText().toString());
                intent.putExtra("posItem", position);
                setResult(RESULT_OK, intent);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
