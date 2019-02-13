package com.example.barvius.learn2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TTS.getInstance().init(this);
        setContentView(R.layout.activity_main);
    }

    protected void loadDBFromFile(){
        final int gallery=12;
        String type="*/*";
        Intent i=new Intent(Intent.ACTION_GET_CONTENT);
        i.setType(type);
        startActivityForResult(Intent.createChooser(i,"select file") ,gallery);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            for (DBItems i: CSV.parseCSV(new File(data.getData().getPath()))) {
                LoadURL.getInstance().addItems(i);
            }
        }
    }

    protected void addDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Добавление слова в словарь");

        final EditText ru = new EditText(this);
        ru.setHint("ru");
        ru.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() > 0) {
                    if (!s.toString().matches("[а-я]+")) {
                        s.delete(s.length()-1,s.length());
                    }
                }
            }
        });

        final EditText en = new EditText(this);
        en.setHint("en");
        en.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() > 0) {
                    if (!s.toString().matches("[a-z]+")) {
                        s.delete(s.length()-1,s.length());
                    }
                }
            }
        });

        LinearLayout lay = new LinearLayout(this);
        lay.setOrientation(LinearLayout.VERTICAL);
        lay.addView(ru);
        lay.addView(en);
        builder.setView(lay);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LoadURL.getInstance().addItems(new DBItems(ru.getText().toString(),en.getText().toString()));
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, "Добавить слово").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                addDialog();
                return true;
            }
        });

        menu.add(0, 2, 0, "Загрузить словарь").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                loadDBFromFile();
                return true;
            }
        });

        menu.add(0, 3, 0, "drop").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                LoadURL.getInstance().truncateArchive();
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public void startBtn(View v){
        final String[] list = {"РУС -> ENG", "ENG -> РУС"};

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Язык");

        builder.setSingleChoiceItems(list, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(MainActivity.this, TestActivity.class);
                intent.putExtra("direction", list[which]);
                startActivity(intent);
            }
        });

        builder.setCancelable(true);
        builder.create().show();
    }
}
