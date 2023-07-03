package com.example.diary;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener{
    private List<Diary> diaryList= new ArrayList<>();
    private ListView listView;
    Button btn_addDiary, btn_backLogin;
    private String userId;
    private MyDatabaseHelper dbHelper;
    private SQLiteDatabase db;
    DiaryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.list_view);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        adapter = new DiaryAdapter(MainActivity.this,R.layout.diary_item,diaryList);

        btn_addDiary = findViewById(R.id.add_diary);
        btn_backLogin = findViewById(R.id.back_login);
        btn_addDiary.setOnClickListener(this);
        btn_backLogin.setOnClickListener(this);

        userId = getIntent().getStringExtra("login_userId");
        dbHelper = new MyDatabaseHelper(this,userId+".db",null,1);
        db = dbHelper.getWritableDatabase();

        refreshListView();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id==R.id.add_diary){
            Diary diary = new Diary();
            diary.setAuthor(userId);
            diaryList.add(diary);
            ContentValues values = new ContentValues();
            values.put("title",diary.getTitle());
            values.put("content",diary.getContent());
            values.put("time",diary.getTime());
            values.put("author",diary.getAuthor());
            db.insert("diary",null,values);
            values.clear();
            refreshListView();
        }
        else if(id==R.id.back_login){
            finish();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Diary diary = diaryList.get(position);
        Intent intent = new Intent(MainActivity.this, DiaryContentActivity.class);
        intent.putExtra("diary_title", diary.getTitle());
        intent.putExtra("diary_content", diary.getContent());
        intent.putExtra("diary_creatTime",diary.getTime());
        intent.putExtra("diary_author",diary.getAuthor());
        intent.putExtra("diary_id",String.valueOf(diary.getId()));
        intent.putExtra("position",String.valueOf(position));
        startActivityForResult(intent,1);
    }

    //从数据库中获取信息初始化ListView
    public void refreshListView(){
        diaryList.clear();
        Cursor cursor = db.query("diary",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex("title"));
                @SuppressLint("Range") String content = cursor.getString(cursor.getColumnIndex("content"));
                @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex("time"));
                @SuppressLint("Range") String author = cursor.getString(cursor.getColumnIndex("author"));
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                Diary diayy = new Diary(title,content,time,author,id);
                diaryList.add(diayy);
            }while(cursor.moveToNext());
        }
        else{
            //Toast.makeText(MainActivity.this,"数据库中无数据",Toast.LENGTH_SHORT).show();
        }
        listView.setAdapter(adapter);
    }

    //从diary编辑界面返回后需要跟新listview中item的title和content值
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String newtitel = data.getStringExtra("title_return");
            String newcontent = data.getStringExtra("content_return");
            int position = Integer.parseInt(data.getStringExtra("position_return"));
            diaryList.get(position).setTitle(newtitel);
            diaryList.get(position).setContent(newcontent);
            listView.setAdapter(adapter);
        }
    }
    //长按删除diary监听器
    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
        // 确认删除对话框构建
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("确认删除?");
        // 点击对话框的 确认 按钮后的操作
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Diary diary = diaryList.get(position);
                db.delete("diary","id = ?",new String[]{String.valueOf(diary.getId())});
                refreshListView();
            }
        });
        // 点击对话框的 取消 按钮后的操作
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 无操作
            }
        });
        builder.create().show();
        return true;     //如果设置成return false在长按和点击同时触发
    }

    //menu管理
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()==R.id.editUser_item){
            Intent intent = new Intent(MainActivity.this, EditUserActivity.class);
            intent.putExtra("userId",userId);
            startActivity(intent);
        }
        else if(item.getItemId()==R.id.deleteDiarys_item){
            // 确认删除对话框构建
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("是否清空所有日记?");
            // 点击对话框的 确认 按钮后的操作
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    db.delete("diary","author = ?",new String[]{userId});
                    refreshListView();
                }
            });
            // 点击对话框的 取消 按钮后的操作
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // 无操作
                }
            });
            builder.create().show();
        }
        return true;
    }
}