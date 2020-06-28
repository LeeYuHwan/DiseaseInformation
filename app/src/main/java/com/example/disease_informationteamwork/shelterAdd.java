package com.example.disease_informationteamwork;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class shelterAdd extends AppCompatActivity {

    private EditText addS_v;
    private EditText addS_v1;
    private EditText add_shelter_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelter_add);

        addS_v = (EditText)findViewById(R.id.addS_v);
        addS_v1 = (EditText)findViewById(R.id.addS_v1);
        add_shelter_name = (EditText)findViewById(R.id.add_shelter_name);
    }

    public void addShelterClick(View v){
        try{
            double addS_p_v = Double.parseDouble(addS_v.getText().toString());
            double addS_p_v1 = Double.parseDouble(addS_v1.getText().toString());
            String add_p_shelter_name = add_shelter_name.getText().toString();
            Intent data = new Intent();
            data.putExtra("addS_v", addS_p_v);
            data.putExtra("addS_v1", addS_p_v1);
            data.putExtra("add_shelter_name", add_p_shelter_name);
            setResult(RESULT_OK, data);
            finish();
        }catch (Exception e) {
            Toast.makeText(this, "위도 경도를 숫자로 제대로 입력해주세요.", Toast.LENGTH_SHORT).show();
        }
    }
}
