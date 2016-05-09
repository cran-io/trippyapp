package io.cran.trippy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import io.cran.trippy.R;
import io.cran.trippy.utils.AppPreferences;

public class SignInActivity extends AppCompatActivity {

    private String name;
    private String picUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final EditText emailAddress = (EditText) findViewById(R.id.emailAddressSignIn);
        final EditText password = (EditText) findViewById(R.id.passwordSignIn);

        ImageView signInBtn = (ImageView) findViewById(R.id.signInButton);
        assert signInBtn != null;
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String emailAddressString = emailAddress.getText().toString();
                String passwordString = password.getText().toString();


                ParseUser.logInInBackground(emailAddressString, passwordString, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (e == null) {
                            Intent i = new Intent(SignInActivity.this, MainActivity.class);
                            AppPreferences.instance(getApplication()).saveUserMail(emailAddressString);
                            AppPreferences.instance(getApplication()).saveUserImage(picUrl);
                            startActivity(i);
                            finish();
                        } else {
                            Toast.makeText(SignInActivity.this, "Incorrect username or password", Toast.LENGTH_LONG).show();
                        }
                    }

                });

            }
        });


    }

}
