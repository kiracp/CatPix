package com.kiraprentice.catpix;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by kiraprentice on 12/6/14.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    public int getCount() {
                        return mThumbIds.length;
                                                }

    public Object getItem(int position) {
                                      return null;
                                                  }

    public long getItemId(int position) {
                                      return 0;
                                               }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(350,350));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }

    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.cat1, R.drawable.cat2,
            R.drawable.cat3, R.drawable.cat4,
            R.drawable.cat5, R.drawable.cat6,
    };
}
