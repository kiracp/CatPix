package com.kiraprentice.catpix;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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

    /**
     * Conceal a sensitive image inside of another image.
     *
     * @param decoy:     the innocuous container image. precondition: must be an image.
     * @param sensitive: the sensitive image to conceal inside of the container.
     * @return an innocuous image containing the sensitive image.
     */
    public static boolean encryptImage(File decoy, File sensitive, Context ctx) {
        final int BUFFER_SIZE = 4096;

        String filename = decoy.getName() + sensitive.getName();
        try {
            ArrayList<FileInputStream> files;
            files = new ArrayList<>(Arrays.asList(new FileInputStream(decoy),
                    getSentinelStream(ctx),
                    new FileInputStream(sensitive)));

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
    public static boolean decryptImage(FileInputStream containerImage) {
        return false;
    }

    private static FileInputStream getSentinelStream(Context ctx) {
        try {
            return ctx.openFileInput(SENTINEL_FILENAME);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}