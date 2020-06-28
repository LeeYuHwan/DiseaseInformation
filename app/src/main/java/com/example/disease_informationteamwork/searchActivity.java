package com.example.disease_informationteamwork;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Vector;

public class searchActivity extends AppCompatActivity {

    private EditText search_id;
    private String []inform_data;
    private String []person_data;
    private String []city_data;
    private String []type_data;
    private double []v_data;
    private double []v1_data;
    private int []id_data;
    private boolean edit_sw;
    private String []img_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        search_id = (EditText)findViewById(R.id.search_id);

        Intent intent = getIntent();
        inform_data = intent.getStringArrayExtra("inform_data");
        person_data = intent.getStringArrayExtra("person_data");
        city_data = intent.getStringArrayExtra("city_data");
        type_data = intent.getStringArrayExtra("type_data");
        v_data = intent.getDoubleArrayExtra("v_data");
        v1_data = intent.getDoubleArrayExtra("v1_data");
        id_data = intent.getIntArrayExtra("id_data");
        edit_sw = intent.getBooleanExtra("edit_sw", false);
        img_url = intent.getStringArrayExtra("img_url");
    }

    public void searchClick(View v){
        switch(v.getId()){
            case R.id.search:
                Vector<Integer> searchIndex = new Vector<Integer>();
                String search_inform = search_id.getText().toString();
                int count = 0;
                for(int i = 0; i < inform_data.length; i++){
                    if(inform_data[i].equals(search_inform)){
                        count++;
                        searchIndex.add(i);
                    }
                }
                if(count == 1){
                    String i_put_data = inform_data[searchIndex.get(0)];
                    String p_put_data = person_data[searchIndex.get(0)];
                    String c_put_data = city_data[searchIndex.get(0)];
                    String t_put_data = type_data[searchIndex.get(0)];
                    double v_put_data = v_data[searchIndex.get(0)];
                    double v1_put_data = v1_data[searchIndex.get(0)];
                    int id_put_data = id_data[searchIndex.get(0)];
                    String img_url_data = img_url[searchIndex.get(0)];
                    int index = searchIndex.get(0);

                    Intent intent = new Intent(this, informationShowActivity.class);
                    intent.putExtra("inform_data",i_put_data);
                    intent.putExtra("person_data", p_put_data);
                    intent.putExtra("city_data", c_put_data);
                    intent.putExtra("type_data", t_put_data);
                    intent.putExtra("v_data", v_put_data);
                    intent.putExtra("v1_data", v1_put_data);
                    intent.putExtra("index", index);
                    intent.putExtra("id_data", id_put_data);
                    intent.putExtra("img_url", img_url_data);
                    intent.putExtra("edit_sw", edit_sw);
                    startActivityForResult(intent, 0);

                }else if (count > 1){
                    int i = 0;
                    String[] i_put_data = new String[count];
                    String[] p_put_data = new String[count];
                    String[] c_put_data = new String[count];
                    String[] t_put_data = new String[count];
                    double[] v_put_data = new double[count];
                    double[] v1_put_data = new double[count];
                    int[] id_put_data = new int[count];
                    int [] index = new int[count];
                    String[] img_url_data = new String[count];
                    while(i < count){
                        i_put_data[i] = inform_data[searchIndex.get(i)];
                        p_put_data[i] = person_data[searchIndex.get(i)];
                        c_put_data[i] = city_data[searchIndex.get(i)];
                        t_put_data[i] = type_data[searchIndex.get(i)];
                        v_put_data[i] = v_data[searchIndex.get(i)];
                        v1_put_data[i] = v1_data[searchIndex.get(i)];
                        id_put_data[i] = id_data[searchIndex.get(i)];
                        img_url_data[i] = img_url[searchIndex.get(i)];
                        index[i] = searchIndex.get(i);
                        i++;
                    }
                    Intent intent = new Intent(this, searchListActivity.class);
                    intent.putExtra("inform_data",i_put_data);
                    intent.putExtra("person_data", p_put_data);
                    intent.putExtra("city_data", c_put_data);
                    intent.putExtra("type_data",t_put_data);
                    intent.putExtra("v_data",v_put_data);
                    intent.putExtra("v1_data",v1_put_data);
                    intent.putExtra("id_data", id_put_data);
                    intent.putExtra("img_url", img_url_data);
                    intent.putExtra("index", index);
                    intent.putExtra("edit_sw", edit_sw);
                    startActivityForResult(intent, 0);

                }else{
                    Toast.makeText(this,"검색한 결과가 없습니다.",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.search_exit:
                finish();
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        boolean sw = data.getBooleanExtra("sw", false);
        int exit_sw = data.getIntExtra("exit_sw",0);
        if(requestCode == 0 && resultCode == RESULT_OK){
            if(exit_sw == 0){
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
           if(exit_sw == 1){
                finish();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
