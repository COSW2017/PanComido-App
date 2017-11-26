package cosw.eci.edu.pancomido.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import cosw.eci.edu.pancomido.R;
import cosw.eci.edu.pancomido.misc.SessionManager;

public class UserActivity extends AppCompatActivity {


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
}
