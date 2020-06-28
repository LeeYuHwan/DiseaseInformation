package com.example.disease_informationteamwork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OptionalDataException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class imgActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 0;
    private Bitmap bitmap;
    private ListView ListView;
    private imgAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img);

        ListView = (ListView) findViewById(R.id.imgList);
        final imgData mData[] = new imgData[imgSource.IMAGE.length];
        for(int i = 0 ; i < mData.length; i++){
            mData[i] = new imgData(imgSource.IMAGE[i], imgSource.IMAGE_NAME[i]);
        }
        mAdapter = new imgAdapter(this, mData);
        ListView.setAdapter(mAdapter);

        final Intent intent = new Intent();
        ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent.putExtra("img_edit_sw", true);
                intent.putExtra("img_url", mData[position].icon);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    public void imgClick(View v){
        switch (v.getId()){
            case R.id.imgExit:
                Intent intent = new Intent();
                intent.putExtra("img_edit_sw", false);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }
}