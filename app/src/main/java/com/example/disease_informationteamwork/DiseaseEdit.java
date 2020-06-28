package com.example.disease_informationteamwork;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class DiseaseEdit extends AppCompatActivity {

    private String inform_data;
    private String person_data;
    private String city_data;
    private String type_data;
    private String img_url;
    private double v_data;
    private double v1_data;
    private int index;
    private int id_data;

    private EditText informText;
    private EditText personText;
    private EditText cityText;
    private EditText typeText;
    private EditText vText;
    private EditText v1Text;
    private EditText imgText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_edit);

        informText = (EditText) findViewById(R.id.edit_disease_name);
        personText = (EditText) findViewById(R.id.edit_disease_person);
        cityText = (EditText) findViewById(R.id.edit_disease_city);
        typeText = (EditText) findViewById(R.id.edit_disease_type);
        vText = (EditText) findViewById(R.id.edit_v);
        v1Text = (EditText) findViewById(R.id.edit_v1);
        imgText = (EditText) findViewById(R.id.edit_disease_img_url);

        Intent intent = getIntent();
        inform_data = intent.getStringExtra("inform_data");
        person_data = intent.getStringExtra("person_data");
        city_data = intent.getStringExtra("city_data");
        type_data = intent.getStringExtra("type_data");
        v_data = intent.getDoubleExtra("v_data",0);
        v1_data = intent.getDoubleExtra("v1_data",0);
        img_url = intent.getStringExtra("img_url");
        index = intent.getIntExtra("index", 0);
        id_data = intent.getIntExtra("id_data", 0);

        vText.setText(v_data+"");
        v1Text.setText(v1_data+"");
        informText.setText(inform_data);
        personText.setText(person_data);
        cityText.setText(city_data);
        typeText.setText(type_data);
        imgText.setText(img_url);
    }

    public void editDiseaseClick(View v){
        Intent data = new Intent();
        data.putExtra("edit_v", Double.parseDouble(vText.getText().toString()));
        data.putExtra("edit_v1", Double.parseDouble(v1Text.getText().toString()));
        data.putExtra("edit_inform", informText.getText().toString());
        data.putExtra("edit_person", personText.getText().toString());
        data.putExtra("edit_city", cityText.getText().toString());
        data.putExtra("edit_type", typeText.getText().toString());
        data.putExtra("edit_img_url", imgText.getText().toString());
        data.putExtra("edit_index", index);
        data.putExtra("edit_id", id_data);
        setResult(RESULT_OK, data);
        finish();
    }
}
