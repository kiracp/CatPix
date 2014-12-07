package com.kiraprentice.catpix;

/**
 * Created by kiraprentice on 12/6/14.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;

public class PhotoStreamActivity extends Activity implements View.OnClickListener {

    private int mActivePointerId;
    private ImageButton image;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        System.out.println("Entered onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_stream);

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));


        /*
        // This adds buttons to all cat images! disabled for secret key
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent intent = new Intent(PhotoStreamActivity.this, CipherButtons.class);
                startActivity(intent);
                Toast.makeText(PhotoStreamActivity.this, "" + position, Toast.LENGTH_SHORT).show();
            }

        });
        */


        Button toolbarButton =  (Button) findViewById(R.id.toolbarButton);

        toolbarButton.setOnClickListener(PhotoStreamActivity.this);

    }

    public void onClick(View v) {
        Intent intent = new Intent(PhotoStreamActivity.this, CipherButtons.class);
        startActivity(intent);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
