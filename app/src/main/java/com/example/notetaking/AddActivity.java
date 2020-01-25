package com.example.notetaking;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.zip.Inflater;

public class AddActivity extends AppCompatActivity implements View.OnClickListener,declareList {
    static final String SHARED_PREFERENCES_NAME="node_app";
    private Button buttonSave;
    private Button buttonCancel;
    private EditText editHeader;
    private EditText editTag;
    private EditText editContent;

    private int array_id;
    private String header;
    private String tag;
    private String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Mapping(); //Ánh xạ

        Bundle bundle=getIntent().getExtras();
        array_id=bundle.getInt("array_id");
        if(array_id !=-1) {
            //Edit note: take data from arrayNote through array_id
            header = arrayNote.get(array_id).getHeader();
            tag = arrayNote.get(array_id).getTag();
            content = arrayNote.get(array_id).getContent();
            editHeader.setText(header);
            editTag.setText(tag);
            editContent.setText(content);
        }
        else {
            //Add note: There's nothing in note, so it's empty
            header = "";
            tag = "";
            content = "";
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

    private void Mapping() {
        buttonSave=findViewById(R.id.bttSave);
        buttonCancel=findViewById(R.id.bttCancel);
        editHeader=findViewById(R.id.edtHeader);
        editTag=findViewById(R.id.edtTag);
        editContent=findViewById(R.id.edtContent);
        buttonSave.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bttSave: {
                if (editHeader.getText().toString().length()==0) {
                    //Check if header is empty
                    Toast.makeText(AddActivity.this, "Header is empty", Toast.LENGTH_SHORT).show();
                } else {
                    //header is not empty, start doing something
                    Toast.makeText(AddActivity.this, "Note saved", Toast.LENGTH_SHORT).show();
                    SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
                    Editor editor = sharedPreferences.edit();
                    int current_id = sharedPreferences.getInt("count", 0);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy, HH:mm:ss", Locale.getDefault());
                    String currentDateandTime = dateFormat.format(new Date());
                    if (array_id == -1) {
                        //Add note
                        editor.putString("header" + current_id, editHeader.getText().toString());
                        editor.putString("date" + current_id, currentDateandTime);
                        editor.putString("tag" + current_id, editTag.getText().toString());
                        editor.putString("content" + current_id, editContent.getText().toString());
                        editor.putInt("count", current_id + 1);
                        editor.apply();
                        Intent intent;
                        intent = new Intent(AddActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        //Edit note
                        int id_temp = current_id - 1 - array_id; //Reverse cái noteArray để hiện lên đầu
                        editor.putString("header" + id_temp, editHeader.getText().toString());
                        editor.putString("date" + id_temp, currentDateandTime);
                        editor.putString("tag" + id_temp, editTag.getText().toString());
                        editor.putString("content" + id_temp, editContent.getText().toString());
                        editor.apply();
                        Intent intent;
                        intent = new Intent(AddActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
                break;
            }
            case R.id.bttCancel: {
                DiscardConfirm();
                break;
            }
        }
    }

    private void DiscardConfirm() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Discard any change?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent;
                intent = new Intent(AddActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alertDialog.show();
    }
}
