package cosw.eci.edu.pancomido.ui.activity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import cosw.eci.edu.pancomido.R;
import cosw.eci.edu.pancomido.data.model.User;
import cosw.eci.edu.pancomido.data.network.RequestCallback;
import cosw.eci.edu.pancomido.data.network.RetrofitNetwork;
import cosw.eci.edu.pancomido.exception.NetworkException;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    EditText nameText;
    EditText lastNameText;
    EditText emailText;
    EditText phoneText;
    EditText passwordText;
    CheckBox tAndCBox;

    Button signupButton;
    TextView loginLink;

    Spinner citySpinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        nameText = (EditText) findViewById(R.id.input_name);
        lastNameText = (EditText) findViewById(R.id.input_last_name);
        emailText = (EditText) findViewById(R.id.input_email);
        phoneText = (EditText) findViewById(R.id.input_phone);
        tAndCBox = (CheckBox) findViewById(R.id.input_tandc);

        signupButton = (Button) findViewById(R.id.btn_signup);
        loginLink = (TextView) findViewById(R.id.link_login);
        passwordText = (EditText) findViewById(R.id.input_password);

        // Get ListView object from xml
        citySpinner = (Spinner) findViewById(R.id.input_city);

        // Defined Array values to show in ListView
        String[] values = new String[] { "Bogotá" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);

        citySpinner.setAdapter(adapter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });


    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        final String name = nameText.getText().toString();
        final String last_name = nameText.getText().toString();
        final String email = emailText.getText().toString();
        final String phone = phoneText.getText().toString();
        final String password = passwordText.getText().toString();
        final String city = (String) citySpinner.getSelectedItem();

        // TODO: Implement your own signup logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success

                        User user = new User(email, password, name, last_name, null, city, phone);
                        RetrofitNetwork r = new RetrofitNetwork();

                        RequestCallback<User> req = new RequestCallback<User>() {
                            @Override
                            public void onSuccess(final User response) {
                                if (response == null) {
                                    onSignupFailed();
                                } else {
                                    SignupActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            onSignupSuccess();
                                        }
                                    });
                                    finish();
                                }
                            }

                            @Override
                            public void onFailed(NetworkException e) {
                                onSignupFailed();
                            }
                        };
                        r.createUser(user, req);


                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    public void onSignupSuccess() {
        signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = nameText.getText().toString();
        String last_name = nameText.getText().toString();
        String email = emailText.getText().toString();
        String phone = phoneText.getText().toString();
        String password = passwordText.getText().toString();
        Boolean tandc = tAndCBox.isChecked();
        String city = (String) citySpinner.getSelectedItem();

        if (name.isEmpty() || name.length() < 3) {
            nameText.setError("at least 3 characters");
            valid = false;
        } else {
            nameText.setError(null);
        }

        if (last_name.isEmpty() || last_name.length() < 3) {
            lastNameText.setError("at least 3 characters");
            valid = false;
        } else {
            nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("enter a valid email address");
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (phone.isEmpty() || phone.length() != 10) {
            phoneText.setError("exactly 10 digits");
            valid = false;
        } else {
            nameText.setError(null);
        }

        if (!tandc){
            tAndCBox.setError("You must accept this");
            valid = false;
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        if (city.isEmpty()) {
            TextView errorText = (TextView)citySpinner.getSelectedView();
            errorText.setError("You must select one city");
            errorText.setTextColor(Color.RED);
            valid = false;
        }

        return valid;
    }
}
