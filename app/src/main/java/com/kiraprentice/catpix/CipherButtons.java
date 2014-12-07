package com.kiraprentice.catpix;

import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static java.net.URLConnection.guessContentTypeFromStream;

/**
 * Created by kiraprentice on 12/6/14.
 */
public class CipherButtons extends ActionBarActivity implements View.OnClickListener {
        List<Map<String, String>> fileList = new ArrayList<Map<String,String>>();
        ListAdapter simpleAdapter;
        static final long decoySize = 32693;
        static InputStream mInputStream = null;

        static final int PICK_IMG_ENCRYPT_REQUEST = 1;
        static final int PICK_IMG_DECRYPT_REQUEST = 2;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            Button encrypt =  (Button) findViewById(R.id.imageToEncrypt);
            Button decrypt =  (Button) findViewById(R.id.imageToDecrypt);

            encrypt.setOnClickListener(this);
            decrypt.setOnClickListener(this);
        }

    public void onClick(View v) {
        Intent imageChooser = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        switch(v.getId()) {
            case R.id.imageToEncrypt:
                // reference a function that would house your current onClick behavior, such as:
                System.out.println("img to encrypt clicked");
                startActivityForResult(imageChooser, PICK_IMG_ENCRYPT_REQUEST);
                break;
            case R.id.imageToDecrypt:
                System.out.println("img to decrypt clicked");
                startActivityForResult(imageChooser, PICK_IMG_DECRYPT_REQUEST);
                break;
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo aInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;

        // We know that each row in the adapter is a Map
        HashMap map = (HashMap) simpleAdapter.getItem(aInfo.position);

        menu.setHeaderTitle("Options for " + map.get("file"));
        menu.add(1, 1, 1, "Decrypt");
        menu.add(1, 2, 2, "Delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        Toast.makeText(this, "Item id [" + itemId + "]", Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == PICK_IMG_ENCRYPT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // Find a random decoy image
                InputStream decoy = getDecoyImage();

                // Get sensitive image.
                Uri selectedImage = data.getData();
                InputStream inputStream;
                try {
                    inputStream = getContentResolver().openInputStream(selectedImage);
                    mInputStream = inputStream;
                    Cipher.encryptImage(decoy, inputStream, getApplicationContext());
                } catch (FileNotFoundException e) {
                    System.err.println("Could not open file as stream :(");
                }

                // Delete sensitive photo
            }
        } else if (requestCode == PICK_IMG_DECRYPT_REQUEST) {
            if (resultCode == RESULT_OK) {
                // Get decrypted photo
                // Split from decoy
                // Save sensitive image to Filesystem

                Uri selectedImage = data.getData();
                System.out.printf("Selected image path: %s\n", selectedImage);
                InputStream fileAsStream;
                try {
                    fileAsStream = getContentResolver().openInputStream(selectedImage);
                    boolean success = Cipher.decryptImage(fileAsStream, getApplicationContext());
                    System.out.println("Decrypt image success?: " + success);
                }
                catch (FileNotFoundException e) {
                    System.err.println("Could not open file as stream :(");
                }
            }
        }
    }

    private InputStream getDecoyImage() {
        try {
            return getAssets().open("cat1.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean isImage(InputStream file) {
        String guess;
        try {
            guess = guessContentTypeFromStream(file);
            if (guess == null) return false;
            return guess.equals("image");
        } catch (IOException e) {
            System.err.println("Could not determine content type of file.");
            return false;
        }
    }
}
