package com.example.disease_informationteamwork;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private TextView id;
    private TextView password;
    private adminData check_admin = new adminData();
    private String admin_id = check_admin.getAdmin_id(); // 관리자 검사용 id
    private int admin_password = check_admin.getAdmin_password(); // 관리자 검사용 password

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        id = (TextView)findViewById(R.id.login_id);
        password = (TextView)findViewById(R.id.login_password);

    }

    public void loginClick(View v){
        try{
        String check_id = id.getText().toString();
        int check_password = Integer.parseInt(password.getText().toString());
        SharedPreferences prefs = getSharedPreferences("login",0);
        SharedPreferences.Editor editor = prefs.edit();

        Intent data = new Intent();
            if(!check_id.equals("")){
                if(admin_id.equals(check_id) && admin_password == check_password){
                    editor.putBoolean("admin_sw", true);
                    editor.putString("login_id", check_id);
                    editor.putInt("login_password", check_password);
                    editor.apply();
                    setResult(RESULT_OK, data);
                    finish();
                }
                else {
                    editor.putBoolean("admin_sw", false);
                    editor.putString("login_id", check_id);
                    editor.putInt("login_password", check_password);
                    editor.apply();
                    setResult(RESULT_OK, data);
                    finish();
                }
            }
        }catch (Exception e){
            Toast.makeText(this,"아이디 제대로 입력해주세요.",Toast.LENGTH_SHORT).show();
        }

    }
}
