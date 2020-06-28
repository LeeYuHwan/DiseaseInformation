package com.example.disease_informationteamwork;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DiseaseAdd extends AppCompatActivity {
    private EditText v0;
    private EditText v1;
    private EditText disease_type; //질병종류의 EditText id를 그대로 따 왔습니다.
    private EditText disease_name; //질병이름의 EditText id를 그대로 따 왔습니다.
    private EditText disease_person; //질병제공자의 EditText id를 그대로 따 왔습니다.
    private EditText disease_city; // 질병 시/도명의 EditText id를 그대로 따 왔습니다.
    private EditText disease_img_url;
    private String[] shelter_data;
    private double[] shelter_v_data;
    private double[] shelter_v1_data;
    private int[] shelter_id_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_add);

        v0 = (EditText)findViewById(R.id.v);
        v1 = (EditText)findViewById(R.id.v1);
        disease_type = (EditText) findViewById(R.id.disease_type);
        disease_name = (EditText) findViewById(R.id.disease_name);
        disease_person = (EditText) findViewById(R.id.disease_person);
        disease_city = (EditText)findViewById(R.id.disease_city);
        disease_img_url = (EditText)findViewById(R.id.disease_img_url);

        Intent intent = getIntent();
        shelter_data = intent.getStringArrayExtra("shelter_data");
        shelter_v_data = intent.getDoubleArrayExtra("shelter_v_data");
        shelter_v1_data = intent.getDoubleArrayExtra("shelter_v1_data");
        shelter_id_data = intent.getIntArrayExtra("shelter_id_data");

    }

    public void mOnClick(View v) {
        switch (v.getId()) {
            case R.id.BtnAdd://'추가' 버튼 클릭하면
                try{
                    int sw = 0; // 질병 추가 임을 알려주는 스위치
                    double v_inform = Double.parseDouble(v0.getText().toString());
                    double v1_inform = Double.parseDouble(v1.getText().toString());
                    String disease_type_inform = disease_type.getText().toString();
                    String disease_name_inform = disease_name.getText().toString();
                    String disease_person_inform = disease_person.getText().toString();
                    String disease_city_inform = disease_city.getText().toString();
                    String img_url = disease_img_url.getText().toString();
                    Intent data = new Intent();
                    data.putExtra("add_v", v_inform);
                    data.putExtra("add_v1", v1_inform);
                    data.putExtra("add_type", disease_type_inform);
                    data.putExtra("add_name", disease_name_inform);
                    data.putExtra("add_person", disease_person_inform);
                    data.putExtra("add_city", disease_city_inform);
                    data.putExtra("img_url", img_url);
                    data.putExtra("sw",sw);
                    setResult(RESULT_OK, data);
                    finish();
                    break;
                }catch (Exception e) {
                    Toast.makeText(this, "위도 경도를 숫자로 제대로 입력해주세요.", Toast.LENGTH_SHORT).show();
                    break;
                }

            case R.id.BtnEdit://'대피소 편집' 버튼 클릭하면
                int[] index = new int[shelter_data.length];
                for(int i = 0; i < index.length; i++){
                    index[i] = i;
                }
                Intent intent_Add = new Intent(this, shelterListChoiceActivity.class);
                intent_Add.putExtra("shelter_data", shelter_data);
                intent_Add.putExtra("shelter_v_data", shelter_v_data);
                intent_Add.putExtra("shelter_v1_data", shelter_v1_data);
                intent_Add.putExtra("shelter_id_data", shelter_id_data);
                intent_Add.putExtra("index", index);
                startActivityForResult(intent_Add,0);
                break;
            case R.id.BtnAddImg: // '기본 이미지 추가' 버튼 클릭하면
                Intent intent_imgAdd = new Intent(this, imgActivity.class);
                startActivityForResult(intent_imgAdd,1);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        int edit_sw = data.getIntExtra("edit_sw", 0);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            if(edit_sw == 0){
                double e_v = data.getDoubleExtra("e_v", 0);
                double e_v1 = data.getDoubleExtra("e_v1", 0);
                String edit_shelter_name = data.getStringExtra("edit_shelter_name");
                int index = data.getIntExtra("index", 0);
                int e_id = data.getIntExtra("shelter_id_data", 0);
                int sw = 1; // 대피소 편집임을 알려주는 스위치
                data.putExtra("e_v", e_v);
                data.putExtra("e_v1", e_v1);
                data.putExtra("edit_shelter_name", edit_shelter_name);
                data.putExtra("index", index);
                data.putExtra("shelter_id_data", e_id);
                data.putExtra("sw",sw);
                setResult(RESULT_OK, data);
                finish();
            }
            else if(edit_sw == 1){
                data.putExtra("addS_v", data.getDoubleExtra("addS_v", 0));
                data.putExtra("addS_v1", data.getDoubleExtra("addS_v1", 0));
                data.putExtra("add_shelter_name", data.getStringExtra("add_shelter_name"));
                data.putExtra("sw", 2);
                setResult(RESULT_OK, data);
                finish();
            }
            else if(edit_sw == 2){
                data.putExtra("sw",3);
                data.putExtra("shelter_id_data",data.getIntExtra("shelter_id_data", 0));
                data.putExtra("index", data.getIntExtra("index", 0));
                setResult(RESULT_OK, data);
                finish();
            }
            else if(edit_sw == 3){
                finish();
            }

        }
        else if (requestCode == 1 && resultCode == RESULT_OK) {
            boolean img_edit_sw = data.getBooleanExtra("img_edit_sw", false);
            if(img_edit_sw == true){
                String img_url = data.getStringExtra("img_url");
                disease_img_url.setText(img_url);
            }
        }

        else
            super.onActivityResult(requestCode, resultCode, data);

    }


}
