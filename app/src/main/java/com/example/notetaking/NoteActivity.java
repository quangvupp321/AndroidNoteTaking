package com.example.notetaking;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class NoteActivity extends AppCompatActivity implements declareList, View.OnClickListener{
    private Button buttonBack;
    private Button buttonEdit;
    private TextView textViewHeader;
    private TextView textViewDate;
    private TextView textViewTag;
    private TextView textViewContent;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        Mapping();//Ánh xạ

        Bundle bundle = getIntent().getExtras();
        id = bundle.getInt("id");

        textViewHeader.setText(arrayNote.get(id).getHeader());
        textViewDate.setText("Date modified: " + arrayNote.get(id).getDate());
        textViewTag.setText("Tag: " + arrayNote.get(id).getTag());
        textViewContent.setText(arrayNote.get(id).getContent());

        buttonBack.setOnClickListener(this);
        buttonEdit.setOnClickListener(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

    private void Mapping() {
        buttonBack = (Button) findViewById(R.id.bttBack);
        buttonEdit = (Button) findViewById(R.id.bttEdit);
        textViewHeader = (TextView) findViewById(R.id.txtHeader);
        textViewDate = (TextView) findViewById(R.id.txtDate);
        textViewTag = (TextView) findViewById(R.id.txtTag);
        textViewContent = (TextView) findViewById(R.id.txtContent);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch(view.getId()) {
            case R.id.bttBack: {
                intent = new Intent(NoteActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.bttEdit: {
                intent = new Intent(NoteActivity.this, AddActivity.class);
                Bundle bundle=new Bundle();
                bundle.putInt("array_id",id);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            }
        }
    }
}
