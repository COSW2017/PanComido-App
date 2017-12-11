package cosw.eci.edu.pancomido.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import net.glxn.qrgen.android.QRCode;

import cosw.eci.edu.pancomido.R;

public class CommandReadyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_command_ready);

        Intent intent = getIntent();

        String command_id = intent.getStringExtra("command_id");

        Bitmap myBitmap = QRCode.from(command_id).bitmap();
        ImageView myImage = (ImageView) findViewById(R.id.command_qr);
        myImage.setImageBitmap(myBitmap);
    }
}
