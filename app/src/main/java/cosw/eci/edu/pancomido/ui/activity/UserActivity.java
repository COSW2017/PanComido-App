package cosw.eci.edu.pancomido.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cosw.eci.edu.pancomido.R;
import cosw.eci.edu.pancomido.misc.SessionManager;

public class UserActivity extends AppCompatActivity {

    private static final String TAG = "UserActivity";
    private static final String TAKE_PHOTO = "Take a new photo";
    private static final int TAKE_PHOTO_OPTION = 1;

    private static final String CHOOSE_GALLERY = "Choose from Gallery";
    private static final int CHOOSE_GALLERY_OPTION = 2;

    private static final String CANCEL = "Cancel";

    EditText phoneText;
    EditText passwordText;
    Button newImageButton;
    Spinner citySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        phoneText = (EditText) findViewById(R.id.et_new_phone);
        passwordText = (EditText) findViewById(R.id.et_new_password);
        newImageButton = (Button) findViewById(R.id.btn_change_image);
        citySpinner = (Spinner) findViewById(R.id.sp_new_city);

        SessionManager session = new SessionManager(this);
        passwordText.setText(session.getPassword());
        phoneText.setText(session.getPhone());

        List<String> spinnerArray = new ArrayList<>();
        spinnerArray.add("Bogotá");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(adapter);
    }

    public void updatePhoto(View view){
        final CharSequence[] options = {TAKE_PHOTO, CHOOSE_GALLERY, CANCEL};
        AlertDialog.Builder builder = new AlertDialog.Builder(UserActivity.this);
        builder.setTitle("Change your profile image");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (options[i].equals(TAKE_PHOTO)){
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, TAKE_PHOTO_OPTION);
                } else if (options[i].equals(CHOOSE_GALLERY)){
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, CHOOSE_GALLERY_OPTION);
                } else if (options[i].equals(CANCEL)){
                    dialogInterface.dismiss();
                }
            }
        });
        builder.create();
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        ImageView view = (ImageView) findViewById(R.id.imageView);

        if (resultCode == RESULT_OK){
            switch (requestCode){
                case TAKE_PHOTO_OPTION:
                    Bitmap picture = null;
                    try {
                         picture = (Bitmap) data.getExtras().get("data");
                    }catch (NullPointerException e){
                        Log.e(TAG, "Error onActivityResult: ", e.getCause());
                    }
                    view.setImageBitmap(picture);
                    break;
                case CHOOSE_GALLERY_OPTION:
                    Uri imageUri = data.getData();
                    InputStream imageStream = null;
                    if (imageUri != null) {
                        try {
                            imageStream = getContentResolver().openInputStream(imageUri);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            Toast.makeText(UserActivity.this, "Something went worng choosing the image", Toast.LENGTH_LONG).show();
                        }
                    }
                    Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    view.setImageBitmap(selectedImage);
                    break;
            }
        }
    }

    public void save(View view){
        EditText newPhone = (EditText) findViewById(R.id.et_new_phone);
        EditText newPass = (EditText) findViewById(R.id.et_new_password);
        ImageView photo = (ImageView) findViewById(R.id.imageView);
        TextWatcher textWatcherPh = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() < 10){
                    Toast.makeText(UserActivity.this, "Phone number needs to have at least 10 digits", Toast.LENGTH_LONG).show();
                }
            }
        };
        newPhone.addTextChangedListener(textWatcherPh);
        TextWatcher textWatcherPw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() < 8){
                    Toast.makeText(UserActivity.this, "Password must be at least 8 characters", Toast.LENGTH_LONG).show();
                }
            }
        };
        newPass.addTextChangedListener(textWatcherPw);

        Bitmap image = ((BitmapDrawable) photo.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bytes = stream.toByteArray();

        //send image and data to server w/ retrofit
    }
}

