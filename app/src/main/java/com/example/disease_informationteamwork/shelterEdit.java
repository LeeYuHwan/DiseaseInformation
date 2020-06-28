package com.example.disease_informationteamwork;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class shelterEdit extends AppCompatActivity {

    private EditText s_v;
    private EditText s_v1;
    private EditText edit_shelter_name;

    private String shelter_data;
    private double shelter_v_data;
    private double shelter_v1_data;
    private int shelter_id_data;
    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelter_edit);

        Intent intent = getIntent();
        s_v = (EditText)findViewById(R.id.s_v);
        s_v1 = (EditText)findViewById(R.id.s_v1);
        edit_shelter_name = (EditText)findViewById(R.id.edit_shelter_name);
        shelter_data = intent.getStringExtra("shelter_data");
        shelter_v_data = intent.getDoubleExtra("shelter_v_data", 0);
        shelter_v1_data = intent.getDoubleExtra("shelter_v1_data",0);
        shelter_id_data = intent.getIntExtra("shelter_id_data", 0);
        index = intent.getIntExtra("index", 0);


        s_v.setText(shelter_v_data+"");
        s_v1.setText(shelter_v1_data+"");
        edit_shelter_name.setText(shelter_data);
    }

    public void editClick(View v){
        Intent data = new Intent();
        switch(v.getId()){
            case R.id.shelter_del:
                data.putExtra("edit_sw",0);
                data.putExtra("shelter_id_data",shelter_id_data);
                data.putExtra("index", index);
                setResult(RESULT_OK, data);
                finish();
                break;
            case R.id.shelter_edit:
                try{
                    double s_v_inform = Double.parseDouble(s_v.getText().toString());
                    double s_v1_inform = Double.parseDouble(s_v1.getText().toString());
                    String edit_shelter_name_inform = edit_shelter_name.getText().toString();

                    data.putExtra("edit_sw",1);
                    data.putExtra("e_v", s_v_inform);
                    data.putExtra("e_v1", s_v1_inform);
                    data.putExtra("edit_shelter_name", edit_shelter_name_inform);
                    data.putExtra("shelter_id_data",shelter_id_data);
                    data.putExtra("index", index);
                    setResult(RESULT_OK, data);
                    finish();
                }catch(Exception e){
                    Toast.makeText(this, "위도 경도를 숫자로 제대로 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }
}
