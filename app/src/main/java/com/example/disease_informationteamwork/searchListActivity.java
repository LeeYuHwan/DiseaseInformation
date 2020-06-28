package com.example.disease_informationteamwork;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class searchListActivity extends AppCompatActivity {

    private String[] inform_data;
    private String[] person_data;
    private String[] city_data;
    private String[] type_data;
    private String[] img_url;
    private Bitmap bitmap;
    private double[] v_data;
    private double[] v1_data;
    private int[] id_data;
    private int[] index;
    private DiseaseAdapter mAdapter;
    private ListView fragment_lv;
    private boolean edit_sw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);

        Intent intent = getIntent();
        inform_data = intent.getStringArrayExtra("inform_data");
        person_data = intent.getStringArrayExtra("person_data");
        city_data = intent.getStringArrayExtra("city_data");
        type_data = intent.getStringArrayExtra("type_data");
        v_data = intent.getDoubleArrayExtra("v_data");
        v1_data = intent.getDoubleArrayExtra("v1_data");
        img_url = intent.getStringArrayExtra("img_url");
        id_data = intent.getIntArrayExtra("id_data");
        index = intent.getIntArrayExtra("index");
        edit_sw = intent.getBooleanExtra("edit_sw", false);

        fragment_lv = (ListView) findViewById(R.id.fList);

        DiseaseData[] mData = new DiseaseData[inform_data.length];
        for(int i = 0; i < mData.length; i++){
            mData[i] = new DiseaseData(img_url[i],inform_data[i],person_data[i],city_data[i]);
        }

        mAdapter = new DiseaseAdapter(this, mData);
        fragment_lv.setAdapter(mAdapter);

        final Intent put_intent = new Intent(this, informationShowActivity.class);
        fragment_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            put_intent.putExtra("inform_data",inform_data[position]);
            put_intent.putExtra("person_data", person_data[position]);
            put_intent.putExtra("city_data", city_data[position]);
            put_intent.putExtra("type_data",type_data[position]);
            put_intent.putExtra("v_data", v_data[position]);
            put_intent.putExtra("v1_data", v1_data[position]);
            put_intent.putExtra("index", index[position]);
            put_intent.putExtra("id_data",id_data[position]);
            put_intent.putExtra("img_url", img_url[position]);
            put_intent.putExtra("edit_sw", edit_sw);
            startActivityForResult(put_intent, 0);
        }
    });
    }

    public void fragmentExit(View v){
        Intent data = new Intent();
        data.putExtra("exit_sw", 1);
        setResult(RESULT_OK, data);
        finish();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        boolean sw = data.getBooleanExtra("sw", false);
        if(requestCode == 0 && resultCode == RESULT_OK){
            if(sw == false){
                String del_inform = data.getStringExtra("del_inform");
                int del_index = data.getIntExtra("del_index",0);
                int del_id = data.getIntExtra("del_id", 0);
                data.putExtra("del_inform", del_inform);
                data.putExtra("del_index", del_index);
                data.putExtra("del_id", del_id);
                data.putExtra("sw", false);
                setResult(RESULT_OK, data);
                finish();
            }
            else {
                data.putExtra("sw", true);
                data.putExtra("edit_v", data.getDoubleExtra("edit_v", 0));
                data.putExtra("edit_v1", data.getDoubleExtra("edit_v1", 0));
                data.putExtra("edit_inform", data.getStringExtra("edit_inform"));
                data.putExtra("edit_person", data.getStringExtra("edit_person"));
                data.putExtra("edit_city", data.getStringExtra("edit_city"));
                data.putExtra("edit_type", data.getStringExtra("edit_type"));
                data.putExtra("edit_img_url", data.getStringExtra("edit_img_url"));
                data.putExtra("edit_index", data.getIntExtra("edit_index", 0));
                data.putExtra("edit_id", data.getIntExtra("edit_id", 0));
                setResult(RESULT_OK, data);
                finish();
            }
        }
        else if(requestCode == 0 && resultCode == RESULT_CANCELED){
            setResult(RESULT_CANCELED, data);
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
