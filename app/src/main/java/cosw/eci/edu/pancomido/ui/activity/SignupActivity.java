package cosw.eci.edu.pancomido.ui.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
    TextView tycLink;



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
        tycLink = (TextView) findViewById(R.id.input_tyc_link);

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

        tycLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                builder.setMessage("Terms and conditions\n\nLorem ipsum dolor sit amet, consectetur adipiscing elit. Sed posuere libero eros, eget mollis augue gravida quis. Donec quam massa, posuere eu eros et, dapibus tristique magna. Fusce dignissim massa ac tellus rhoncus maximus. Praesent sed viverra purus, et varius libero. Nullam molestie imperdiet lacus nec mattis. Sed vel egestas metus. Nulla quis cursus lectus. Aenean consequat mollis arcu nec fringilla. Etiam id ipsum ornare, auctor metus sit amet, hendrerit risus.\n" +
                        "\n" +
                        "Curabitur lorem erat, consequat sit amet convallis vitae, eleifend sed metus. Nulla facilisi. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec gravida semper scelerisque. Duis elementum accumsan dui at commodo. Sed egestas non tortor a vulputate. Curabitur dignissim auctor lorem, sit amet iaculis orci tristique eu. Cras sapien tellus, lobortis in tincidunt quis, venenatis ut tellus. Suspendisse sed vehicula turpis. Ut blandit mauris et finibus mattis. Curabitur eu ultricies enim, eu porttitor mi.\n" +
                        "\n" +
                        "Maecenas luctus auctor neque et semper. Sed at dapibus orci. Phasellus quis consectetur ipsum. In ultricies convallis nibh non sagittis. Ut vel enim augue. Nam sed sapien vel justo sodales blandit. Nullam a velit et neque condimentum condimentum.\n" +
                        "\n" +
                        "Duis a lectus imperdiet, suscipit magna ac, condimentum purus. Phasellus viverra ipsum ac mi pretium ultrices. Nulla quis est tincidunt, ullamcorper erat in, ultrices velit. Donec tristique non nulla non lobortis. Phasellus dui lorem, porta at dui imperdiet, semper pellentesque neque. In hac habitasse platea dictumst. Nullam pretium nisi et tempus sollicitudin. In hac habitasse platea dictumst. Aenean in dapibus ipsum, non finibus nunc. Praesent vulputate ante et tempor egestas. Morbi facilisis in neque at congue. Vivamus commodo dignissim ipsum.\n" +
                        "\n" +
                        "Cras justo mi, consequat ac facilisis at, congue porta justo. Fusce feugiat vehicula arcu sit amet condimentum. Cras a aliquam tortor. Mauris faucibus enim ac laoreet posuere. Cras nibh odio, consectetur at tellus ac, porta molestie lectus. Nam at velit aliquet augue dapibus sagittis. Nunc non euismod ligula. Proin eu magna finibus, dignissim nisi sit amet, posuere turpis. Donec lacinia risus eu tincidunt pulvinar. Donec accumsan nisl ac justo imperdiet pharetra. Fusce justo augue, tincidunt at malesuada et, vehicula ac leo. Donec tincidunt sagittis velit, sit amet euismod justo auctor eu. Nunc sed mattis ipsum. Suspendisse rhoncus mi risus, id tincidunt nibh interdum sit amet. Ut a eros sit amet magna facilisis tincidunt. Pellentesque suscipit felis feugiat magna facilisis consequat.");

                // Create the AlertDialog object and return it
                builder.show();
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
