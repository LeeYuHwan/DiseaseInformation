package com.example.disease_informationteamwork;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class informationShowActivity extends AppCompatActivity {

    private String inform_data;
    private String person_data;
    private String city_data;
    private String type_data;
    private String img_url;
    private double v_data;
    private double v1_data;
    private int index;
    private int id_data;
    private Bitmap bitmap;
    private TextView show_title;
    private TextView show_inform;
    private ImageView show_image;
    private boolean edit_sw;
    private Button editBtn;
    private Button delBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_show);
        show_title = (TextView)findViewById(R.id.show_title);
        show_inform = (TextView)findViewById(R.id.show_inform);
        show_image = (ImageView) findViewById(R.id.show_image);
        editBtn = (Button)findViewById(R.id.show_edit);
        delBtn = (Button)findViewById(R.id.show_del);

        Intent intent = getIntent();
        inform_data = intent.getStringExtra("inform_data");
        person_data = intent.getStringExtra("person_data");
        city_data = intent.getStringExtra("city_data");
        type_data = intent.getStringExtra("type_data");
        v_data = intent.getDoubleExtra("v_data",0);
        v1_data = intent.getDoubleExtra("v1_data",0);
        index = intent.getIntExtra("index", 0);
        id_data = intent.getIntExtra("id_data", 0);
        img_url = intent.getStringExtra("img_url");
        edit_sw = intent.getBooleanExtra("edit_sw",false);

        show_title.setText("질병 이름 : " + inform_data);
        Thread mThread = new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL(img_url);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        mThread.start();

        try {
            mThread.join();
            show_image.setImageBitmap(bitmap);
        } catch (InterruptedException e) {
            show_image.setImageResource(R.drawable.ic_launcher_foreground);
        }
        show_inform.setText("위치(위도) : " + v_data +"\n위치(경도) : " + v1_data + "\n질병 종류 : " + type_data + "\n질병 위치(시/도명) : " + city_data +"\n질병 제공자 : " + person_data);

        if(edit_sw == false){
            editBtn.setVisibility(View.GONE);
            delBtn.setVisibility(View.GONE);
        }
        else {
            editBtn.setVisibility(View.VISIBLE);
            delBtn.setVisibility(View.VISIBLE);
        }
    }


    public void informClick(View v){
        Intent data = new Intent();
            switch (v.getId()) {
                case R.id.show_edit:
                    Intent intent = new Intent(this, DiseaseEdit.class);
                    intent.putExtra("inform_data", inform_data);
                    intent.putExtra("person_data", person_data);
                    intent.putExtra("city_data", city_data);
                    intent.putExtra("type_data", type_data);
                    intent.putExtra("v_data", v_data);
                    intent.putExtra("v1_data", v1_data);
                    intent.putExtra("img_url", img_url);
                    intent.putExtra("index", index);
                    intent.putExtra("id_data", id_data);
                    startActivityForResult(intent, 0);
                    break;
                case R.id.show_del:
                    data.putExtra("del_inform", inform_data);
                    data.putExtra("del_index", index);
                    data.putExtra("del_id", id_data);
                    data.putExtra("sw", false);
                    setResult(RESULT_OK, data);
                    finish();
                    break;
                case R.id.show_exit:
                    setResult(RESULT_CANCELED, data);
                    finish();
                    break;
            }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 0 && resultCode == RESULT_OK){
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
        super.onActivityResult(requestCode, resultCode, data);
    }
}
