package com.example.diary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_login,btn_register;
    EditText userId,pass;
    RadioGroup rememberPass;
    SharedPreferences lastUserPrefs,userPrefs;
    RadioButton rBtn_remember,rBten_noremember;
    String oldpass;

    public LoginActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_login = findViewById(R.id.btn_sign_in);
        btn_register = findViewById(R.id.btn_sign_up);
        btn_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);

        rBtn_remember = findViewById(R.id.remember);
        rBten_noremember = findViewById(R.id.noremember);

        userId = findViewById(R.id.login_userId);
        pass = findViewById(R.id.login_pass);
        rememberPass = findViewById(R.id.remember_pass);

        lastUserPrefs = getSharedPreferences("lastUser",MODE_PRIVATE);
        String lastUserId = lastUserPrefs.getString("userId","");
        if(!lastUserId.equals("")){
            userId.setText(lastUserId);
            userPrefs = getSharedPreferences(userId.getText().toString(),MODE_PRIVATE);
            if(userPrefs.getBoolean("rememberPass",false)){
                oldpass = userPrefs.getString("pass","");
                pass.setText(oldpass);
                rBtn_remember.setChecked(true);                         // 如果上一次选记住密码，本次是否记住密码默认“是”
            }
        }
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id==R.id.btn_sign_in){
            if(userId.getText().toString().equals("")||pass.getText().toString().equals("")){
                Toast.makeText(LoginActivity.this,"账号或密码不能为空",Toast.LENGTH_SHORT).show();
            }
            else{
                userPrefs = getSharedPreferences(userId.getText().toString(),MODE_PRIVATE);
                if(userPrefs.getString("userId","").equals("")){
                    Toast.makeText(LoginActivity.this,"用户不存在，请注册",Toast.LENGTH_SHORT).show();
                }
                else{
                    if(userPrefs.getBoolean("rememberPass",true)){
                        if(oldpass.equals(pass.getText().toString())){
                            gotoMain();
                        }
                        else{
                            if(!userPrefs.getString("pass","").equals(Hash.md5(pass.getText().toString()+"240240"))){
                                Toast.makeText(LoginActivity.this,"用户id或密码错误",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                gotoMain();
                            }
                        }
                    }
                    else{
                        if(!userPrefs.getString("pass","").equals(Hash.md5(pass.getText().toString()+"240240"))){
                            Toast.makeText(LoginActivity.this,"用户id或密码错误",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            gotoMain();
                        }
                    }
                }
            }
        }
        else if(id==R.id.btn_sign_up){
            Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
            startActivity(intent);
        }
    }

    public void gotoMain(){
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        intent.putExtra("login_userId", userId.getText().toString());
        SharedPreferences.Editor editor = lastUserPrefs.edit();
        editor.putString("userId",userId.getText().toString());
        editor.commit();
        remPass();
        startActivityForResult(intent,1);
    }

    public void remPass(){
        SharedPreferences.Editor editor = userPrefs.edit();
        if(rememberPass.getCheckedRadioButtonId()==R.id.remember){
            editor.putBoolean("rememberPass",true);
        }
        else{
            editor.putBoolean("rememberPass",false);
        }
        editor.commit();
    }

    //从主界面放回登录界面处理
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        userPrefs = getSharedPreferences(userId.getText().toString(),MODE_PRIVATE);
        if(userPrefs.getBoolean("rememberPass",false)){
            pass.setText(userPrefs.getString("pass",""));
            rBtn_remember.setChecked(true);
        }
        else{
            pass.setText("");
            rBten_noremember.setChecked(true);
        }
    }
}