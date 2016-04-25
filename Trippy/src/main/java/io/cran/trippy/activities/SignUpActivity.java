package io.cran.trippy.activities;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import io.cran.trippy.R;
import io.cran.trippy.fragments.DatePickerFragment;

public class SignUpActivity extends AppCompatActivity {

    protected EditText mFirstName;
    protected EditText mLastName;
    protected EditText mEmailAddress;
    protected EditText mBirthDate;
    protected EditText mPassword;
    protected ImageView mSignUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mFirstName= (EditText) findViewById(R.id.firstNameET);
        mLastName=(EditText) findViewById(R.id.lastNameET);
        mEmailAddress=(EditText) findViewById(R.id.emailAddressET);
        mBirthDate=(EditText) findViewById(R.id.birthDate);
        mBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    DialogFragment newFragment = new DatePickerFragment();
                    newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });
        mPassword=(EditText)findViewById(R.id.password);
        mSignUpBtn= (ImageView) findViewById(R.id.signUpBtn);

        mSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName= mFirstName.getText().toString().trim();
                String lastName= mLastName.getText().toString().trim();
                String emailAddress= mEmailAddress.getText().toString().trim();
                String birthDate= mBirthDate.getText().toString().trim();
                String password= mPassword.getText().toString().trim();

                ParseUser newUser= new ParseUser();
                newUser.setUsername(firstName);
                newUser.put("lastname",lastName);
                newUser.setEmail(emailAddress);
                newUser.put("birthdate",birthDate);
                newUser.setPassword(password);
                newUser.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e==null){
                            Intent i= new Intent(SignUpActivity.this,MainActivity.class);
                            startActivity(i);
                        }
                        else{
                            Toast.makeText(SignUpActivity.this,""+e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
}
