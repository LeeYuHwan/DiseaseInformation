package com.example.disease_informationteamwork;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DiseaseAdapter extends BaseAdapter {
    private Context ctx;
    private DiseaseData[] data;
    private Bitmap bitmap;

    public DiseaseAdapter(Context ctx, DiseaseData[] data){
        this.ctx = ctx;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if(view == null){
            LayoutInflater inflater = LayoutInflater.from(ctx);
            view = inflater.inflate(R.layout.disease_list, viewGroup, false);
        }

        ImageView image = (ImageView)view.findViewById(R.id.fList_image);
        Thread mThread = new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL(data[i].icon);
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
            image.setImageBitmap(bitmap);
        } catch (InterruptedException e) {
            image.setImageResource(R.drawable.ic_launcher_foreground);
        }
        TextView text = (TextView)view.findViewById(R.id.fList_inform);
        String textSet = data[i].inform + "/" +data[i].person + "/" +data[i].city;
        text.setText(textSet);

        return view;
    }
}
