package cosw.eci.edu.pancomido.misc;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by estudiante on 3/13/17.
 */

public class loadImage extends AsyncTask<String, Void, Bitmap> {

    public ImageView image;

    public loadImage(ImageView image){
        this.image=image;
    }

    @Override
    protected Bitmap doInBackground(String... url) {
        URL imageUrl = null;
        HttpURLConnection conn = null;

        try {
            imageUrl = new URL(url[0]);
            conn = (HttpURLConnection) imageUrl.openConnection();
            conn.connect();
            return BitmapFactory.decodeStream(conn.getInputStream());
        } catch (IOException e) {

            e.printStackTrace();

            return null;
        }
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        image.setImageBitmap(bitmap);
    }
}
