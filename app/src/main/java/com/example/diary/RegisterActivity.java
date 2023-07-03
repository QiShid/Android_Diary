package com.example.diary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    Button btn_back,btn_register;
    EditText userId,pass,surepass,userName;
    ImageView image_pass_eye1,image_pass_eye2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btn_back = findViewById(R.id.btn_back);
        btn_register = findViewById(R.id.btn_register);
        btn_back.setOnClickListener(this);
        btn_register.setOnClickListener(this);

        //小眼睛查看密码
        image_pass_eye1 = findViewById(R.id.image_pass_eye1);
        image_pass_eye1.setOnTouchListener(new myOnTouchListener());
        image_pass_eye2 = findViewById(R.id.image_pass_eye2);
        image_pass_eye2.setOnTouchListener(new myOnTouchListener());

        userId = findViewById(R.id.register_userID);
        pass = findViewById(R.id.register_pass);
        surepass = findViewById(R.id.register_surepass);
        userName = findViewById(R.id.register_userName);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id==R.id.btn_back){
            finish();
        }
        if(id==R.id.btn_register){
            SharedPreferences prefs = getSharedPreferences(userId.getText().toString(),MODE_PRIVATE);
            if(!prefs.getString("userId","").equals("")){
                Toast.makeText(RegisterActivity.this,"该用户已存在",Toast.LENGTH_SHORT).show();
            }
            else{
                if(pass.getText().toString().equals(surepass.getText().toString())){
                    restoreUserData();
                    Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(RegisterActivity.this,"两次输入密码不一样",Toast.LENGTH_SHORT).show();
            }
        }
    }
    //存储用户信息到SharePreferences
    private void restoreUserData(){
        SharedPreferences.Editor editor = getSharedPreferences(userId.getText().toString(),MODE_PRIVATE).edit();
        editor.putString("userId",userId.getText().toString());
        editor.putString("pass",Hash.md5(pass.getText().toString()+"240240"));
        editor.putBoolean("rememberPass",false);
        if(userName.getText().toString().equals("")){
            editor.putString("userName","default");
        }
        else{
            editor.putString("userName",userName.getText().toString());
        }
        editor.commit();
    }

    //眼睛图标的触摸事件
    private class myOnTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            if (v.getId() == R.id.image_pass_eye1) {
                switch (action) {
                    case MotionEvent.ACTION_DOWN://按下（按下动作）
                        image_pass_eye1.setSelected(false);
                        //密码可见
                        pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        break;
                    case MotionEvent.ACTION_UP://抬起（抬起动作）
                        image_pass_eye1.setSelected(true);
                        //密码不可见
                        pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        break;
                }
            }
            else if (v.getId() == R.id.image_pass_eye2) {
                switch (action) {
                    case MotionEvent.ACTION_DOWN://按下（按下动作）
                        image_pass_eye2.setSelected(false);
                        //密码可见
                        surepass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        break;
                    case MotionEvent.ACTION_UP://抬起（抬起动作）
                        image_pass_eye2.setSelected(true);
                        //密码不可见
                        surepass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        break;
                }
            }
            return true;
        }
    }
}