package com.example.disease_informationteamwork;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Vector;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MyDBHelper mDBHelper = new MyDBHelper(this);;
    private GoogleMap mMap;
    private static final int DEFAULT_ZOOM = 13;

    private Vector<Integer> id_s_v = new Vector<Integer>(); // 대피소 id 벡터
    private Vector<String> s_v = new Vector<String>(); // 대피소 이름 벡터
    private Vector<LatLng> s_l_v = new Vector<LatLng>(); // 대피소 위치 정보 벡터
    private Vector<Double> p_s_v = new Vector<Double>(); // 대피소 위치 전송용 벡터(위도)
    private Vector<Double> p_s_v1 = new Vector<Double>(); // 대피소 위치 전송용 벡터(경도)

    private Vector<Integer> id_v = new Vector<Integer>(); // 질병 id 벡터
    private Vector<String> t_v = new Vector<String>(); // 질병 종류 벡터
    private Vector<String> i_v = new Vector<String>(); // 질병 정보 벡터
    private Vector<String> p_v = new Vector<String>(); // 질병 제공자 벡터
    private Vector<String> c_v = new Vector<String>(); // 질병 시/도명 벡터
    private Vector<Double> p_l_v = new Vector<Double>(); // 질병 위치 전송용 벡터(위도)
    private Vector<Double> p_l_v1 = new Vector<Double>(); // 질병 위치 전송용 벡터(경도)
    private Vector<LatLng> l_v = new Vector<LatLng>(); // 질병 위치 정보 벡터
    private Vector<String> img_v = new Vector<String>(); // 질병 이미지 정보 벡터

    private boolean admin_sw = false; // 관리자 접속 확인용 만약 true 일경우 질병 추가 메뉴가 열린다.
    private String user_id; // 일반 사용자용 id
    private Menu mMenu; // 질별 추가 메뉴 제어용 변수

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SharedPreferences prefs = getSharedPreferences("login",0);
        String user_id = prefs.getString("login_id", "");
        if(user_id.equals("")){
            Intent intent_login = new Intent(this, LoginActivity.class);
            startActivityForResult(intent_login,2);
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        if(i_v.size() == 0 && l_v.size() == 0){
            data_insertVector();
        }

        if(s_v.size() == 0 && s_l_v.size() == 0){
            shelter_insertVector();
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        for (int i = 0; i < get_Disease_Size(); i++) {
            addMarker(get_type(i), get_inform(i), get_person(i), c_v.get(i), l_v.get(i));
        }

        for(int i = 0; i < get_shelter_Size(); i++){
            s_addMarker(get_shelter_name(i), s_l_v.get(i));
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(37.548980, 126.994020), DEFAULT_ZOOM));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        mMenu = menu;
        MenuItem diseaseAdd_menu = mMenu.findItem(R.id.add_disease);
        MenuItem login = mMenu.findItem(R.id.login);
        SharedPreferences prefs = getSharedPreferences("login",0);
        boolean admin_sw = prefs.getBoolean("admin_sw", false);
        String user_id = prefs.getString("login_id", "");
        if(!user_id.equals("")){
            if(admin_sw == true){
                diseaseAdd_menu.setVisible(true);
            }
            login.setTitle("로그아웃");
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.login: // 로그인 메뉴 requestCode 2번
                MenuItem check_login = mMenu.findItem(R.id.login);
                SharedPreferences pref = getApplicationContext().getSharedPreferences("login", 0);
                boolean admin_sw = pref.getBoolean("admin_sw", false);
                SharedPreferences.Editor editor = pref.edit();
                if(check_login.getTitle().equals("로그아웃") && admin_sw == true) {
                    MenuItem diseaseAdd_menu = mMenu.findItem(R.id.add_disease);
                    diseaseAdd_menu.setVisible(false);
                    check_login.setTitle("로그인");
                    Toast.makeText(this,"관리자모드 로그아웃",Toast.LENGTH_SHORT).show();
                    editor.remove("login_id");
                    editor.remove("login_password");
                    editor.putBoolean("admin_sw", false);
                    editor.apply();
                }
                else if(check_login.getTitle().equals("로그아웃") && admin_sw == false) {
                    check_login.setTitle("로그인");
                    Toast.makeText(this,"로그아웃",Toast.LENGTH_SHORT).show();
                    editor.remove("login_id");
                    editor.remove("login_password");
                    editor.putBoolean("admin_sw", false);
                    editor.apply();
                }
                else {
                    Intent intent_login = new Intent(this, LoginActivity.class);
                    startActivityForResult(intent_login,2);
                }
                break;
            case R.id.search_disease: // 질병 검색 메뉴 requestCode 3번
                MenuItem s_check_login = mMenu.findItem(R.id.login);

                String[] i_put_data = new String[i_v.size()];
                String[] p_put_data = new String[p_v.size()];
                String[] c_put_data = new String[c_v.size()];
                String[] t_put_data = new String[t_v.size()];
                double[] v_put_data = new double[p_l_v.size()];
                double[] v1_put_data = new double[p_l_v1.size()];
                int[] id_put_data = new int[id_v.size()];
                String[] img_url_put = new String[img_v.size()];
                for(int i = 0; i < get_Disease_Size(); i++) {
                    i_put_data[i] = i_v.get(i);
                    p_put_data[i] = p_v.get(i);
                    c_put_data[i] = c_v.get(i);
                    t_put_data[i] = t_v.get(i);
                    v_put_data[i] = p_l_v.get(i);
                    v1_put_data[i] = p_l_v1.get(i);
                    id_put_data[i] = id_v.get(i);
                    img_url_put[i] = img_v.get(i);
                }

                Intent intent_search = new Intent(this, searchActivity.class);
                intent_search.putExtra("inform_data", i_put_data);
                intent_search.putExtra("person_data", p_put_data);
                intent_search.putExtra("city_data", c_put_data);
                intent_search.putExtra("type_data",t_put_data);
                intent_search.putExtra("v_data", v_put_data);
                intent_search.putExtra("v1_data", v1_put_data);
                intent_search.putExtra("id_data", id_put_data);
                intent_search.putExtra("img_url", img_url_put);
                if(s_check_login.getTitle().equals("로그아웃")){
                    intent_search.putExtra("edit_sw", true);
                }
                else intent_search.putExtra("edit_sw", false);
                startActivityForResult(intent_search, 3);
                break;
            case R.id.choice_disease: // 질병 종류 메뉴 requestCode 0번
                String[] put_data = new String[t_v.size()];
                for(int i = 0; i < t_v.size(); i++)
                    put_data[i] = t_v.get(i);
                put_data = new HashSet<String>(Arrays.asList(put_data)).toArray(new String[0]); // 중복제거
                Intent intent_choice = new Intent(this, choiceActivity.class);
                intent_choice.putExtra("type_data", put_data);
                startActivityForResult(intent_choice, 0);
                break;
            case R.id.add_disease: // 질병 추가 메뉴 requestCode 1번
                String[] s_put_data = new String[get_shelter_Size()];
                double[] s_v_put_data = new double[get_shelter_Size()];
                double[] s_v1_put_data = new double[get_shelter_Size()];
                int[] s_id_put_data = new int[get_shelter_Size()];
                for(int i = 0; i < get_shelter_Size(); i++){
                    s_put_data[i] = s_v.get(i);
                    s_v_put_data[i] = p_s_v.get(i);
                    s_v1_put_data[i] = p_s_v1.get(i);
                    s_id_put_data[i] = id_s_v.get(i);
                }

                Intent intent_Add = new Intent(this, DiseaseAdd.class);
                intent_Add.putExtra("shelter_data", s_put_data);
                intent_Add.putExtra("shelter_v_data", s_v_put_data);
                intent_Add.putExtra("shelter_v1_data", s_v1_put_data);
                intent_Add.putExtra("shelter_id_data", s_id_put_data);
                startActivityForResult(intent_Add,1);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 0 && resultCode == RESULT_OK){ //질병 종류 리턴문
            String choice_result = data.getStringExtra("choice_result");
            Toast.makeText(this, choice_result + " 선택", Toast.LENGTH_SHORT).show();
            if(choice_result.equals("질병 정보들만 표시")){
                mMap.clear();
                for (int i = 0; i < get_Disease_Size(); i++) {
                    addMarker(get_type(i), get_inform(i), get_person(i), c_v.get(i), l_v.get(i));
                }
            }
            else if(choice_result.equals("대피소만 표시")){
                mMap.clear();
                for (int i = 0; i < get_shelter_Size(); i++) {
                    s_addMarker(get_shelter_name(i), s_l_v.get(i));
                }
            }
            else if(choice_result.equals("질병 정보 + 대피소 모두 표시")){
                mMap.clear();
                for (int i = 0; i < get_Disease_Size(); i++) {
                    addMarker(get_type(i), get_inform(i), get_person(i), c_v.get(i), l_v.get(i));
                }
                for (int i = 0; i < get_shelter_Size(); i++) {
                    s_addMarker(get_shelter_name(i), s_l_v.get(i));
                }
            }
            else {
                mMap.clear();
                for(int i = 0; i < get_Disease_Size(); i++){
                    if(t_v.get(i) != null){
                        if(t_v.get(i).equals(choice_result))
                            addMarker(get_type(i), get_inform(i), get_person(i), c_v.get(i), l_v.get(i));
                    }
                }
            }
        }
        else if(requestCode == 1 && resultCode == RESULT_OK){ // 질병 추가 리턴문
            int sw = data.getIntExtra("sw",0);
            if(sw == 0) {
                double add_v = data.getDoubleExtra("add_v",0);
                double add_v1 = data.getDoubleExtra("add_v1",0);
                String add_type = data.getStringExtra("add_type");
                String add_name = data.getStringExtra("add_name");
                String add_person = data.getStringExtra("add_person");
                String add_city = data.getStringExtra("add_city");
                String add_img = data.getStringExtra("img_url");

                set_inform(add_v, add_v1, add_type, add_name, add_person, add_city, add_img);
            }
            else if (sw == 1){
                double e_v = data.getDoubleExtra("e_v",0);
                double e_v1 = data.getDoubleExtra("e_v1",0);
                String edit_shelter_name = data.getStringExtra("edit_shelter_name");
                int index = data.getIntExtra("index", 0);
                int id = data.getIntExtra("shelter_id_data", 0);
                Toast.makeText(this, "편집을 완료하였습니다.",Toast.LENGTH_SHORT).show();
                updateShelter(id ,index, e_v, e_v1, edit_shelter_name);
            }
            else if (sw == 2){
                double addS_v = data.getDoubleExtra("addS_v", 0);
                double addS_v1 = data.getDoubleExtra("addS_v1", 0);
                String add_shelter_name = data.getStringExtra("add_shelter_name");

                set_Shelter(addS_v, addS_v1, add_shelter_name);
            }
            else if (sw == 3){
                int del_shelter_id_data = data.getIntExtra("shelter_id_data", 0);
                int del_index = data.getIntExtra("index", 0);

                del_Shelter(del_index, del_shelter_id_data);
            }

        }
        else if(requestCode == 2 && resultCode == RESULT_OK){ // 로그인 리턴문
            SharedPreferences prefs = getSharedPreferences("login",0);
            admin_sw = prefs.getBoolean("admin_sw", false);
            user_id = prefs.getString("login_id", "");
            MenuItem diseaseAdd_menu = mMenu.findItem(R.id.add_disease);
            MenuItem login = mMenu.findItem(R.id.login);

            if(admin_sw == true){
                Toast.makeText(this,"관리자모드로 진입 합니다.",Toast.LENGTH_SHORT).show();
                diseaseAdd_menu.setVisible(true);
                login.setTitle("로그아웃");
            }
            else{
                Toast.makeText(this,"사용자 모드 진입 "+ user_id +"님 환영합니다.",Toast.LENGTH_SHORT).show();
                diseaseAdd_menu.setVisible(false);
                login.setTitle("로그아웃");
            }
        }
        else if(requestCode == 3 && resultCode == RESULT_OK){ // 질병 검색 리턴문
            boolean sw = data.getBooleanExtra("sw", false);
            //T면 편집 F면 삭제
            if(sw == false){
                int del_id = data.getIntExtra("del_id", 0);
                int del_index = data.getIntExtra("del_index",0);
                del_inform(del_index, del_id);

            }
            else {
                double edit_v = data.getDoubleExtra("edit_v", 0);
                double edit_v1 = data.getDoubleExtra("edit_v1", 0);
                String edit_inform = data.getStringExtra("edit_inform");
                String edit_person = data.getStringExtra("edit_person");
                String edit_city = data.getStringExtra("edit_city");
                String edit_type = data.getStringExtra("edit_type");
                String edit_img_url = data.getStringExtra("edit_img_url");
                int edit_index = data.getIntExtra("edit_index", 0);
                int edit_id = data.getIntExtra("edit_id", 0);
                updateInform(edit_id, edit_index, edit_v, edit_v1, edit_type, edit_inform, edit_person, edit_city, edit_img_url);
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    void set_inform(double v, double v1, String type, String inform, String person, String city, String img_url) {
        SQLiteDatabase db;
        ContentValues values;
        int id = 0;
        if(id_v.size() > 0)
            id = id_v.get(id_v.size() - 1) + 1;

        db = mDBHelper.getWritableDatabase();
        values = new ContentValues();
        values.put("id", id);
        values.put("v", v);
        values.put("v1", v1);
        values.put("type", type);
        values.put("inform", inform);
        values.put("person", person);
        values.put("city", city);
        values.put("img_url", img_url);
        db.insert("information", null, values);
        mDBHelper.close();

        id_v.add(id);
        l_v.add(new LatLng(v, v1));
        t_v.add(type);
        i_v.add(inform);
        p_v.add(person);
        c_v.add(city);
        p_l_v.add(v);
        p_l_v1.add(v1);
        img_v.add(img_url);

        MarkerOptions disease = new MarkerOptions();
        disease.position(new LatLng(v, v1));
        disease.title(type + "/" + inform + "/" + person + "/" + city);
        mMap.addMarker(disease);
    }

    void set_Shelter(double v, double v1, String s_name){
        SQLiteDatabase db;
        ContentValues values;
        int s_id = 0;
        if(id_s_v.size() > 0)
            s_id = id_s_v.get(id_s_v.size() - 1) + 1;

        db = mDBHelper.getWritableDatabase();
        values = new ContentValues();
        values.put("id", s_id);
        values.put("v", v);
        values.put("v1", v1);
        values.put("s_name", s_name);
        db.insert("shelter", null, values);
        mDBHelper.close();

        id_s_v.add(s_id);
        s_v.add(s_name);
        s_l_v.add(new LatLng(v, v1));
        p_s_v.add(v);
        p_s_v1.add(v1);

        MarkerOptions s_disease = new MarkerOptions();
        s_disease.position(new LatLng(v, v1));
        s_disease.title("대피소 이름 : " + s_name);
        mMap.addMarker(s_disease);
    }

    void del_inform(int index, int id){ // 삭제 이상 방지를 위해 쿼리를 id로 진행하였습니다.
        SQLiteDatabase db;

        db = mDBHelper.getWritableDatabase();
        db.delete("information","id=" + id, null);
        mDBHelper.close();

        id_v.remove(index);
        l_v.remove(index);
        t_v.remove(index);
        i_v.remove(index);
        p_v.remove(index);
        c_v.remove(index);
        p_l_v.remove(index);
        p_l_v1.remove(index);
        img_v.remove(index);

        mMap.clear();
        for (int i = 0; i < get_Disease_Size(); i++) {
            addMarker(get_type(i), get_inform(i), get_person(i), c_v.get(i), l_v.get(i));
        }
    }
    void del_Shelter(int index, int id){// 삭제 이상 방지를 위해 쿼리를 id로 진행하였습니다.
        SQLiteDatabase db;

        db = mDBHelper.getWritableDatabase();
        db.delete("shelter", "id=" + id ,null);
        mDBHelper.close();

        id_s_v.remove(index);
        p_s_v.remove(index);
        p_s_v1.remove(index);
        s_l_v.remove(index);
        s_v.remove(index);

        mMap.clear();
        for (int i = 0; i < get_shelter_Size(); i++) {
            s_addMarker(get_shelter_name(i), s_l_v.get(i));
        }
    }

    LatLng get_location(int i) {
        return l_v.get(i);
    }

    String get_type(int i){ return t_v.get(i); }
    String get_inform(int i){
        return i_v.get(i);
    }
    String get_person(int i){ return p_v.get(i); }
    int get_Disease_Size() { return l_v.size(); }

    String get_shelter_name(int i) { return s_v.get(i); }
    int get_shelter_Size() { return s_l_v.size(); }




    void data_insertVector() {
        SQLiteDatabase db;
        Cursor cur;

        db = mDBHelper.getReadableDatabase();
        cur = db.query("information",null,null,null,null,null,null);
        if(cur != null){
            insertVector(cur);
            cur.close();
        }
        mDBHelper.close();
    }

    void shelter_insertVector(){
        SQLiteDatabase db;
        Cursor cur;

        db = mDBHelper.getReadableDatabase();
        cur = db.query("shelter",null,null,null,null,null,null);
        if(cur != null){
            s_insertVector(cur);
            cur.close();
        }
        mDBHelper.close();
    }

    void updateShelter(int index, int id, double v, double v1, String shelter_name){
        SQLiteDatabase db;
        ContentValues values;
        db = mDBHelper.getWritableDatabase();

        values = new ContentValues();

        values.put("v", v);
        values.put("v1", v1);
        values.put("s_name", shelter_name);
        db.update("shelter",values,"id=" + id ,null);
        mDBHelper.close();//갱신 이상 방지를 위해서 쿼리를 id로 진행하였습니다.

        s_v.remove(index);
        s_v.add(index, shelter_name);
        s_l_v.remove(index);
        s_l_v.add(index, new LatLng(v, v1));
        p_s_v.remove(index);
        p_s_v.add(index, v);
        p_s_v1.remove(index);
        p_s_v1.add(index, v1);
        mMap.clear();
        for (int i = 0; i < get_shelter_Size(); i++) {
            s_addMarker(get_shelter_name(i), s_l_v.get(i));
        }
    }

    void updateInform(int index, int id, double v, double v1, String type, String inform, String person, String city, String img_url){
        SQLiteDatabase db;
        ContentValues values;
        db = mDBHelper.getWritableDatabase();

        values = new ContentValues();

        values.put("v", v);
        values.put("v1", v1);
        values.put("type", type);
        values.put("inform", inform);
        values.put("person", person);
        values.put("city", city);
        values.put("img_url", img_url);
        db.update("information",values,"id=" + id ,null);
        mDBHelper.close();//갱신 이상 방지를 위해서 쿼리를 id로 진행하였습니다.

        l_v.remove(index);
        l_v.add(index, new LatLng(v, v1));
        p_l_v.remove(index);
        p_l_v.add(index, v);
        p_l_v1.remove(index);
        p_l_v1.add(index, v1);
        t_v.remove(index);
        t_v.add(index, type);
        i_v.remove(index);
        i_v.add(index, inform);
        p_v.remove(index);
        p_v.add(index, person);
        c_v.remove(index);
        c_v.add(index, city);
        img_v.remove(index);
        img_v.add(index, img_url);
        Toast.makeText(this,inform,Toast.LENGTH_SHORT).show();

        mMap.clear();
        for (int i = 0; i < get_Disease_Size(); i++) {
            addMarker(get_type(i), get_inform(i), get_person(i), c_v.get(i), l_v.get(i));
        }
    }

    private void insertVector(Cursor cur){
        int id_col = cur.getColumnIndex("id");
        int v_col = cur.getColumnIndex("v");
        int v1_col = cur.getColumnIndex("v1");
        int type_col = cur.getColumnIndex("type");
        int inform_col = cur.getColumnIndex("inform");
        int person_col = cur.getColumnIndex("person");
        int city_col = cur.getColumnIndex("city");
        int imgUrl_col = cur.getColumnIndex("img_url");

        while(cur.moveToNext()){
            id_v.add(cur.getInt(id_col));
            p_l_v.add(cur.getDouble(v_col));
            p_l_v1.add(cur.getDouble(v1_col));
            l_v.add(new LatLng(cur.getDouble(v_col), cur.getDouble(v1_col)));
            t_v.add(cur.getString(type_col));
            i_v.add(cur.getString(inform_col));
            p_v.add(cur.getString(person_col));
            c_v.add(cur.getString(city_col));
            img_v.add(cur.getString(imgUrl_col));
        }

    }

    private void s_insertVector(Cursor cur){
        int s_id_col = cur.getColumnIndex("id");
        int s_v_col = cur.getColumnIndex("v");
        int s_v1_col = cur.getColumnIndex("v1");
        int s_name_col = cur.getColumnIndex("s_name");

        while(cur.moveToNext()){
            id_s_v.add(cur.getInt(s_id_col));
            p_s_v.add(cur.getDouble(s_v_col));
            p_s_v1.add(cur.getDouble(s_v1_col));
            s_l_v.add(new LatLng(cur.getDouble(s_v_col), cur.getDouble(s_v1_col)));
            s_v.add(cur.getString(s_name_col));
        }
    }

    void addMarker(String type, String inform, String person , String city, LatLng location_data){
        String inform_data = type + "/" + inform + "/" + person + "/" + city;
        MarkerOptions disease = new MarkerOptions();
        disease.position(location_data);
        disease.title(inform_data);
        mMap.addMarker(disease);
    }

    void s_addMarker(String s_name, LatLng location_data){
        MarkerOptions s_disease = new MarkerOptions();
        s_disease.position(location_data);
        s_disease.title("대피소 이름 : " + s_name);
        mMap.addMarker(s_disease);
    }


}


class MyDBHelper extends SQLiteOpenHelper{
    public MyDBHelper(Context context){
        super(context, "DiseaseInformationAppData.db", null, 1);
    }

    @Override // 갱신이상, 삭제이상 방지를 위해 id를 넣었습니다.
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE information(id INTEGER PRIMARY KEY, v DOUBLE, v1 DOUBLE, type TEXT, inform TEXT, person TEXT, city TEXT, img_url TEXT);");
        db.execSQL("CREATE TABLE shelter(id INTEGER PRIMARY KEY, v DOUBLE, v1 DOUBLE, s_name Text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS information;");
        onCreate(db);
    }
}

