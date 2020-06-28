package com.example.disease_informationteamwork;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class shelterListChoiceActivity extends AppCompatActivity {

    private String[] shelter_data;
    private double[] shelter_v_data;
    private double[] shelter_v1_data;
    private int[] shelter_id_data;
    private int[] index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelter_list_choice);

        Intent intent = getIntent();
        shelter_data = intent.getStringArrayExtra("shelter_data");
        shelter_v_data = intent.getDoubleArrayExtra("shelter_v_data");
        shelter_v1_data = intent.getDoubleArrayExtra("shelter_v1_data");
        shelter_id_data = intent.getIntArrayExtra("shelter_id_data");
        index = intent.getIntArrayExtra("index");

        ListView list = (ListView)findViewById(R.id.c_s_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,shelter_data);
        list.setAdapter(adapter);
        final Intent e_intent = new Intent(this, shelterEdit.class);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                e_intent.putExtra("index", index[position]);
                e_intent.putExtra("shelter_v_data" , shelter_v_data[position]);
                e_intent.putExtra("shelter_v1_data" , shelter_v1_data[position]);
                e_intent.putExtra("shelter_data" , shelter_data[position]);
                e_intent.putExtra("shelter_id_data", shelter_id_data[position]);
                startActivityForResult(e_intent,0);
            }
        });

    }
    public void exitClick(View v){
        switch (v.getId()){
            case R.id.btn_s_add:
                Intent intent = new Intent(this, shelterAdd.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.btn_s_exit:
                Intent data = new Intent();
                data.putExtra("edit_sw", 3);
                setResult(RESULT_OK, data);
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        int edit_sw = data.getIntExtra("edit_sw", 0);
        if (requestCode == 0 && resultCode == RESULT_OK) { // 편집 및 삭제
            if(edit_sw == 0){
                data.putExtra("edit_sw",2);
                data.putExtra("shelter_id_data",data.getIntExtra("shelter_id_data", 0));
                data.putExtra("index", data.getIntExtra("index", 0));
                setResult(RESULT_OK, data);
                finish();
            }
            else{
                data.putExtra("edit_sw",2);
                double e_v = data.getDoubleExtra("e_v", 0);
                double e_v1 = data.getDoubleExtra("e_v1", 0);
                String edit_shelter_name = data.getStringExtra("edit_shelter_name");
                int index = data.getIntExtra("index", 0);
                int e_id = data.getIntExtra("shelter_id_data", 0);
                data.putExtra("e_v", e_v);
                data.putExtra("e_v1", e_v1);
                data.putExtra("edit_shelter_name", edit_shelter_name);
                data.putExtra("index", index);
                data.putExtra("shelter_id_data", e_id);
                data.putExtra("edit_sw", 0);
                setResult(RESULT_OK, data);
                finish();
            }
        }
        else if(requestCode == 1 && resultCode == RESULT_OK){ // 추가
            data.putExtra("addS_v", data.getDoubleExtra("addS_v", 0));
            data.putExtra("addS_v1", data.getDoubleExtra("addS_v1", 0));
            data.putExtra("add_shelter_name", data.getStringExtra("add_shelter_name"));
            data.putExtra("edit_sw", 1);
            setResult(RESULT_OK, data);
            finish();
        }
            super.onActivityResult(requestCode, resultCode, data);

    }

}
