package com.kiraprentice.catpix;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class MainActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        System.out.println("main activity loaded!");
        Intent intent = new Intent(this, PhotoStreamActivity.class);
        System.out.println("Starting activity");
        startActivity(intent);
    }
}