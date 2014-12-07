package com.kiraprentice.catpix;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;

/**
 * Created by bobgardner on 12/6/14.
 */
public class Cipher {
    private static final String SENTINEL_FILENAME = "sentinel.txt";
    private static final String SENTINEL_VALUE = "SENTINEL_VALUE";

    /**
     * Conceal a sensitive image inside of another image.
     *
     * @param decoy:     the innocuous container image. precondition: must be an image.
     * @param sensitive: the sensitive image to conceal inside of the container.
     * @return an innocuous image containing the sensitive image.
     */
    public static boolean encryptImage(InputStream decoy, InputStream sensitive, Context ctx) {
        final int BUFFER_SIZE = 4096;

        String filename = "test.jpg";
        try {
            ArrayList<InputStream> files =
                    files = new ArrayList<>(Arrays.asList(decoy, getSentinelStream(ctx), sensitive));

            Enumeration e = Collections.enumeration(files);
            SequenceInputStream sequenceStream = new SequenceInputStream(e);
            FileOutputStream outputStream = new FileOutputStream(filename);

            int count;
            byte[] buf = new byte[BUFFER_SIZE];
            while ((count = sequenceStream.read(buf)) != -1) {
                outputStream.write(buf, 0, count);
            }
            outputStream.close();
        } catch (IOException e) {
            System.err.printf("Unable to open file: %s.\n", filename);
            return false;
        }
        return true;
    }

    /**
     * Reveal the sensitive image contained inside of another image.
     *
     * @param containerImage: the innocuous container image secretly containing sensitive info.
     * @return the sensitive image.
     */
    public static boolean decryptImage(InputStream containerImage, Context context) {
        String filename = "output.jpg";
        boolean found = false;
        try {
            File file;
            if (isExternalStorageWritable()) {
                file = new File(getAlbumStorageDir("image"), filename);
            } else {
                file = new File(context.getFilesDir(), filename);
            }
            file.createNewFile();
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            BufferedReader br = new BufferedReader(new InputStreamReader(containerImage));
            String line;
            while ((line = br.readLine()) != null) {
                if (found) {
                    bw.write(line);
                    bw.newLine();
                } else if (line.contains(SENTINEL_VALUE)) {
                    found = true;
                }
            }
            br.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return found;
    }

    private static InputStream getSentinelStream(Context ctx) {
        try {
            return ctx.getAssets().open(SENTINEL_FILENAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /* Checks if external storage is available for read and write */
    private static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    private static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    private static File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            System.out.println("Could not mkdir");
        }
        return file;
    }
}