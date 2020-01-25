package com.example.notetaking;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import java.util.Collections;
import android.widget.SearchView;


public class MainActivity extends AppCompatActivity implements declareList, OnClickListener{
    static final String SHARED_PREFERENCES_NAME="node_app";
    private ListView lvNote;
    private ImageView imgAdd;
    private SearchView svSearch;
    private NoteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences=getSharedPreferences(SHARED_PREFERENCES_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Mapping(); //Ánh xạ

        if (sharedPreferences.getBoolean("firstrun", true)) {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
            sharedPreferences.edit().putBoolean("firstrun", false).commit();
        }

        imgAdd.setOnClickListener(this);
        lvNote.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                bundle.putInt("id", i);
                Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        lvNote.setTextFilterEnabled(true);
        svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                String text = newText;
                adapter.filter(text);
                return false;
            }
        });
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

    private void Mapping() {
        lvNote = (ListView) findViewById(R.id.listviewNote);
        imgAdd = (ImageView) findViewById(R.id.imgAdd);
        svSearch = (SearchView) findViewById(R.id.searchviewSearch);
        registerForContextMenu(lvNote);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.menu_popup, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int id = info.position;
        switch(item.getItemId()) {
            case R.id.menuPopupEdit:
                edit(id);
                break;
            case R.id.menuPopupDelete:
                DeleteConfirm(id);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void DeleteConfirm(final int id) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Delete this note?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                delete(id);
            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alertDialog.show();
    }

    private void delete(int id) {
        deleteItemArray(id);
        arrayNote.clear();
        loadData();
        Collections.reverse(arrayNote);
        Toast.makeText(MainActivity.this,"Deleted",Toast.LENGTH_SHORT).show();
        adapter = new NoteAdapter(MainActivity.this, R.layout.row_note, declareList.arrayNote);
        lvNote.setAdapter(adapter);
    }

    private void deleteItemArray(int id)
    {
        SharedPreferences sharedPreferences=getSharedPreferences(SHARED_PREFERENCES_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        arrayNote.remove(id);
        Collections.reverse(arrayNote);
        editor.clear();
        editor.apply();
        for (int i=0;i<arrayNote.size();i++)
        {
            editor.putString("header"+i,arrayNote.get(i).getHeader());
            editor.putString("content"+i,arrayNote.get(i).getContent());
            editor.putString("date"+i,arrayNote.get(i).getDate());
            editor.putString("tag"+i,arrayNote.get(i).getTag());
            editor.apply();
        }
        editor.putInt("count",arrayNote.size());
        editor.apply();
    }

    private void edit(int id) {
        Bundle bundle=new Bundle();
        bundle.putInt("array_id", id); //Chuyển dữ liệu là array_id để edit note dựa trên id
        Intent intent=new Intent(MainActivity.this, AddActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void loadData()
    {
        //Load dữ liệu từ SharedPreferences lên arrayNote
        SharedPreferences sharedPreferences=getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        int count=sharedPreferences.getInt("count",0);
        for(int id=0;id<count;id++)
        {
            //Nạp từng note theo id
            Note note = new Note(
                    sharedPreferences.getString("header"+id,""),
                    sharedPreferences.getString("content"+id,""),
                    sharedPreferences.getString("tag"+id,""),
                    sharedPreferences.getString("date"+id,""));
            arrayNote.add(note);
        }
    }

    public void onClick(View view) {
        Bundle bundle=new Bundle();
        bundle.putInt("array_id",-1); //Nếu là add (thêm note) thì array_id = -1
        Intent intent=new Intent(MainActivity.this,AddActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    protected void onStop() {
        super.onStop();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    protected void onResume() {
        arrayNote.clear(); //Clear ArrayNote để làm sạch
        loadData(); //Load lại dữ liệu lên arrayNote
        //Nạp lên adapter:
        adapter = new NoteAdapter(MainActivity.this,R.layout.row_note,arrayNote);
        lvNote.setAdapter(adapter);
        Collections.reverse(arrayNote);
        super.onResume();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
    }

}
