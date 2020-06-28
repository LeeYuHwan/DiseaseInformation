package com.example.disease_informationteamwork;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class choiceActivity extends AppCompatActivity {
    private String[] type_data;
    private ArrayList<String> type_a = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);

        Intent intent = getIntent();
        type_data = intent.getStringArrayExtra("type_data");

        for(int i = 0; i < type_data.length; i++){
            if(type_data[i] != null)
                type_a.add(type_data[i]);
        } // null 값 제거
        ListView t_list = (ListView)findViewById(R.id.c_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,type_a);
        t_list.setAdapter(adapter);
        t_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String result = type_a.get(position);
                Intent data = new Intent();
                data.putExtra("choice_result", result);
                setResult(RESULT_OK, data);
                finish();
            }
        });


}
    public void mOnClick(View v){
        Intent data = new Intent();
        switch (v.getId()){
            case R.id.btnDisease_inform:
                data.putExtra("choice_result", "질병 정보들만 표시");
                setResult(RESULT_OK, data);
                finish();
                break;
            case R.id.btnShelter:
                data.putExtra("choice_result", "대피소만 표시");
                setResult(RESULT_OK, data);
                finish();
                break;
            case R.id.btnAll:
                data.putExtra("choice_result", "질병 정보 + 대피소 모두 표시");
                setResult(RESULT_OK, data);
                finish();
                break;
        }

    }
}
