package com.example.diary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditUserActivity extends AppCompatActivity implements View.OnClickListener{
    Button btn_back,btn_edit;
    TextView user_Id;
    EditText pass,surepass,userName;
    String userId;
    SharedPreferences userPrefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
        btn_edit = findViewById(R.id.btn_edit);
        btn_edit.setOnClickListener(this);

        user_Id = findViewById(R.id.edit_userID);
        pass = findViewById(R.id.edit_pass);
        surepass = findViewById(R.id.edit_surepass);
        userName = findViewById(R.id.edit_userName);

        userId = getIntent().getStringExtra("userId");
        userPrefs = getSharedPreferences(userId,MODE_PRIVATE);
        user_Id.setText(userPrefs.getString("userId",""));
        userName.setText(userPrefs.getString("userName",""));
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        String oldPass = userPrefs.getString("pass","");
        if(id==R.id.btn_edit){
            if(userName.getText().toString().equals("")){
                Toast.makeText(this,"用户姓名不能为空",Toast.LENGTH_SHORT).show();
            }
            else if(pass.getText().toString().equals("")){
                Toast.makeText(this,"密码不能为空",Toast.LENGTH_SHORT).show();
            }
            else if(surepass.getText().toString().equals("")){
                Toast.makeText(this,"确认密码不能为空",Toast.LENGTH_SHORT).show();
            }
            else if(!pass.getText().toString().equals(surepass.getText().toString())){
                Toast.makeText(this,"两次密码不一样",Toast.LENGTH_SHORT).show();
            }
            else if(pass.getText().toString().equals(oldPass)){
                Toast.makeText(this,"新密码不能与旧密码一样",Toast.LENGTH_SHORT).show();
            }
            else{
                SharedPreferences.Editor editor = userPrefs.edit();
                editor.putString("userName",userName.getText().toString());
                editor.putString("pass",pass.getText().toString());
                editor.commit();
                Toast.makeText(this,"修改成功",Toast.LENGTH_SHORT).show();
            }
        }
        else if(id==R.id.btn_back){
            finish();
        }
    }
}