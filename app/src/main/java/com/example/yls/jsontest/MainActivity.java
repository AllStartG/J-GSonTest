package com.example.yls.jsontest;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Button btn1,btn2,btn3,btnCall;
    private TextView tvResult;
    private String jsonStr;
    private static final int MY_PERMISSIONS_CALL_PHONE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {
        Student s1 = new Student(101,"张三",20);
        Student s2 = new Student(102,"李四",31);
        Student s3 = new Student(103,"小湘",41);
        final Student[] stus = {s1,s2,s3};

        btn1 = (Button) findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONArray array = new JSONArray();
                for (int i=0;i<stus.length;i++){
                    JSONObject stuobj = getStudentJsonObj(stus[i]);
                    array.put(stuobj);
                }
                JSONObject obj = new JSONObject();
                try {
                    obj.put("stuList",array);
                } catch (JSONException e) {
                    Log.i("MainActivity", e.toString());
                }
                jsonStr = obj.toString();
                tvResult.setText(obj.toString());
            }
        });

        btn2 = (Button) findViewById(R.id.btn2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject obj = new JSONObject(jsonStr);
                    JSONArray array = obj.getJSONArray("stuList");

                    ArrayList<Student> stuList = new ArrayList<Student>();
                    for(int i=0;i<array.length();i++){
                        JSONObject stuObj = array.getJSONObject(i);
                        int id = stuObj.getInt("id");
                        String name = stuObj.getString("name");
                        int age = stuObj.getInt("age");
                        Student s = new Student(id,name,age);
                        stuList.add(s);
                    }
                    tvResult.setText(stuList.get(0).getId() + "   " + stuList.get(0).getName() + "   " + stuList.get(0).getAge());
                   } catch (JSONException e) {
                    Log.i("MainActivity", e.toString());
                }
            }
        });
        btn3 = (Button) findViewById(R.id.btn3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String jsonStr = "{\"id\":101,\"name\":\"xiaoxiang\",\"age\":20}";
                Gson gson = new Gson();
                Student s = gson.fromJson(jsonStr, Student.class);
                tvResult.setText(s.getId() + "  " + s.getName() + "  " + s.getAge());
            }
        });
        btnCall = (Button) findViewById(R.id.btn_call);
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testCall();
            }
        });
        tvResult = (TextView) findViewById(R.id.tvResult);
    }

    private void testCall() {
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CALL_PHONE},MY_PERMISSIONS_CALL_PHONE);
        } else {
            callphone();
        }
    }

    private void callphone() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + "10086");
        intent.setData(data);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_CALL_PHONE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){

                callphone();

            }else{
                Toast.makeText(MainActivity.this,"无法拨打电话，sorry你所要拨打的电话无法拨出",Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private JSONObject getStudentJsonObj(Student s) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("id", s.getId());
            obj.put("name", s.getName());
            obj.put("age", s.getAge());
        } catch (JSONException e) {
            Log.i("MainActivity", e.toString());
        }
        return obj;
    }

}
