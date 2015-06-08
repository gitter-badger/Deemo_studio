package com.sevenre.trackre.driver.network;

import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    //https://developers.google.com/maps/documentation/staticmaps/
	ImageView bmImage;
    double lat, lng;
	public DownloadImageTask(ImageView bmImage, double lat, double lng) {
        this.bmImage = bmImage;
        this.lat = lat;
        this.lng = lng;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = "http://maps.googleapis.com/maps/api/staticmap?zoom=18&size=560x240&markers=size:mid|color:red|" +
        		lat + "," + lng + "&zoom=13";
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}