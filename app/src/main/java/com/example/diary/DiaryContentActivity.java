package com.example.diary;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;

public class DiaryContentActivity extends AppCompatActivity implements View.OnClickListener{
    Button btn_back,btn_save;
    EditText diary_title,diary_content;
    TextView diary_time;
    private MyDatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private String oldTitle,oldContent;  //判断是否点击了保存

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_content);

        String userId = getIntent().getStringExtra("diary_author");
        dbHelper = new MyDatabaseHelper(this,userId+".db",null,1);
        db = dbHelper.getWritableDatabase();

        btn_back = findViewById(R.id.btn_back);
        btn_save = findViewById(R.id.btn_save);
        btn_back.setOnClickListener(this);
        btn_save.setOnClickListener(this);

        diary_time = findViewById(R.id.diary_createTime);
        diary_title = findViewById(R.id.diary_title);
        diary_content = findViewById(R.id.diary_content);
        String diaryTitle = getIntent().getStringExtra("diary_title");
        String diaryContent = getIntent().getStringExtra("diary_content");
        String diaryTime = getIntent().getStringExtra("diary_creatTime");
        diary_time.setText("创建时间："+diaryTime);
        if(!diaryTitle.equals("")){
            diary_title.setText(diaryTitle);
        }
        if(!diaryContent.equals("")){
            diary_content.setText(diaryContent);
        }
        oldTitle = diaryTitle;
        oldContent = diaryContent;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        String newTitle = diary_title.getText().toString();
        String newContent = diary_content.getText().toString();
        if(id==R.id.btn_back){
            if(!newTitle.equals(oldTitle)||!newContent.equals(oldContent)){
                // 确认保存对话框构建
                AlertDialog.Builder builder = new AlertDialog.Builder(DiaryContentActivity.this);
                builder.setMessage("你还未保存，是否保存?");
                // 点击对话框的 确认 按钮后的操作
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveData();     //保存数据
                        backToMain(newTitle,newContent);   //然后返回
                    }
                });
                // 点击对话框的 取消 按钮后的操作
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        backToMain(oldTitle,oldContent);   //直接返回
                    }
                });
                builder.create().show();
            }
            else{
                backToMain(newTitle,newContent);
            }
        }
        else if(id==R.id.btn_save){
            oldTitle = diary_title.getText().toString();
            oldContent = diary_content.getText().toString();
            saveData();
        }
    }

    //保存数据到数据库
    public void saveData(){
        String diaryId = getIntent().getStringExtra("diary_id");
        ContentValues values = new ContentValues();
        values.put("title",diary_title.getText().toString());
        values.put("content",diary_content.getText().toString());
        db.update("diary",values,"id = ?",new String[]{diaryId});
        Toast.makeText(this,"保存成功",Toast.LENGTH_SHORT).show();
    }

    //返回到主界面操作
    public void backToMain(String title,String content){
        String position = getIntent().getStringExtra("position");
        Intent intent = new Intent();
        intent.putExtra("title_return",title);
        intent.putExtra("content_return",content);
        intent.putExtra("position_return",position);
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            //取得数据
            Uri uri = data.getData();
            ContentResolver cr = this.getContentResolver();
            Bitmap bitmap = null;
            Bundle extras = null;
            //如果是选择照片
            if(requestCode == 1){
                try {
                    //将对象存入Bitmap中
                    bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            int imgWidth = bitmap.getWidth();
            int imgHeight = bitmap.getHeight();
            double partion = imgWidth*1.0/imgHeight;
            double sqrtLength = Math.sqrt(partion*partion + 1);
            //新的缩略图大小
            double newImgW = 480*(partion / sqrtLength);
            double newImgH = 480*(1 / sqrtLength);
            float scaleW = (float) (newImgW/imgWidth);
            float scaleH = (float) (newImgH/imgHeight);

            Matrix mx = new Matrix();
            //对原图片进行缩放
            mx.postScale(scaleW, scaleH);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, imgWidth, imgHeight, mx, true);
            final ImageSpan imageSpan = new ImageSpan(this,bitmap);
            SpannableString spannableString = new SpannableString("test");
            spannableString.setSpan(imageSpan, 0, spannableString.length(), SpannableString.SPAN_MARK_MARK);
            //光标移到下一行
            diary_content.append("\n");
            Editable editable = diary_content.getEditableText();
            int selectionIndex = diary_content.getSelectionStart();
            spannableString.getSpans(0, spannableString.length(), ImageSpan.class);
            //将图片添加进EditText中
            editable.insert(selectionIndex, spannableString);
            //添加图片后自动空出两行
            diary_content.append("\n\n");
        }
    }
}