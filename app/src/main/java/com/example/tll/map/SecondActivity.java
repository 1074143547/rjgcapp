package com.example.tll.map;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

public class SecondActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.re) ;
        LayoutInflater inflater =  LayoutInflater.from(getApplicationContext());
        View layout = inflater.inflate(R.layout.appwidget_provider,relativeLayout,true);
       // setContentView(R.layout.activity_second);
        setContentView(R.layout.appwidget_provider);


    }
}
