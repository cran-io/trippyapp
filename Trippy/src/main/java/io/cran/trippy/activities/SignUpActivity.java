package io.cran.trippy.activities;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.media.Image;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import io.cran.trippy.R;
import io.cran.trippy.fragments.DatePickerFragment;
import io.cran.trippy.utils.AppPreferences;

public class SignUpActivity extends AppCompatActivity implements DatePickerFragment.DatePickerListener {

    protected EditText mFirstName;
    protected EditText mLastName;
    protected EditText mEmailAddress;
    protected TextView mBirthDate;
    protected EditText mPassword;
    protected EditText mRepeatPass;
    protected ImageView mSignUpBtn;
    private ObjectAnimator mAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mFirstName= (EditText) findViewById(R.id.firstNameET);
        mLastName=(EditText) findViewById(R.id.lastNameET);
        mEmailAddress=(EditText) findViewById(R.id.emailAddressET);
        mBirthDate = (TextView) findViewById(R.id.birthDate);
        mBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    DialogFragment newFragment = new DatePickerFragment();
                    newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });
        mPassword=(EditText)findViewById(R.id.password);
        mRepeatPass = (EditText) findViewById(R.id.confirmPassword);
        mSignUpBtn= (ImageView) findViewById(R.id.signUpBtn);

        mSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String firstName = mFirstName.getText().toString().trim();
                final String lastName = mLastName.getText().toString().trim();
                final String emailAddress = mEmailAddress.getText().toString().trim();
                String birthDate= mBirthDate.getText().toString().trim();
                String password= mPassword.getText().toString().trim();
                String repeatPass = mRepeatPass.getText().toString().trim();

                if (password.equals(repeatPass)) {
                ParseUser newUser= new ParseUser();
                    newUser.setUsername(emailAddress);
                    newUser.put("firstname",firstName);
                    newUser.put("lastname",lastName);
                    newUser.setEmail(emailAddress);
                    newUser.put("birthdate",birthDate);
                    newUser.setPassword(password);
                    newUser.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e==null){

                            makeAssetsInvisible();
                            AppPreferences.instance(getApplication()).saveUsername(firstName + " " + lastName);
                            AppPreferences.instance(getApplication()).saveUserMail(emailAddress);
                            AppPreferences.instance(getApplication()).saveUserImage("");
                            TextView allsetText = (TextView) findViewById(R.id.allsetText);
                            assert allsetText != null;
                            allsetText.setVisibility(View.VISIBLE);
                            ImageView allsetImage = (ImageView) findViewById(R.id.allset);
                            assert allsetImage != null;
                            allsetImage.setVisibility(View.VISIBLE);
                            allsetImage.setImageResource(R.drawable.allset_animation);
                            mAnimator = ObjectAnimator.ofInt(allsetImage, "ImageLevel", 0, 2);
                            mAnimator.setDuration(500);
                            Animator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    Intent i = new Intent(SignUpActivity.this, MainActivity.class);
                                    startActivity(i);
                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animation) {

                                }
                            };
                            mAnimator.addListener(animatorListener);
                            mAnimator.start();
                        }
                        else{
                            Toast.makeText(SignUpActivity.this,""+e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
                } else {
                    Toast.makeText(SignUpActivity.this, "Passwords don't match", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void makeAssetsInvisible() {
        mFirstName.setVisibility(View.GONE);
        mLastName.setVisibility(View.GONE);
        mEmailAddress.setVisibility(View.GONE);
        mBirthDate.setVisibility(View.GONE);
        mPassword.setVisibility(View.GONE);
        mRepeatPass.setVisibility(View.GONE);
        mSignUpBtn.setVisibility(View.GONE);

        ImageView divider1 = (ImageView) findViewById(R.id.divider1);
        ImageView divider2 = (ImageView) findViewById(R.id.divider2);
        ImageView divider3 = (ImageView) findViewById(R.id.divider3);
        ImageView divider4 = (ImageView) findViewById(R.id.divider4);
        ImageView divider5 = (ImageView) findViewById(R.id.divider5);
        ImageView divider6 = (ImageView) findViewById(R.id.divider6);
        assert divider1 != null;
        divider1.setVisibility(View.GONE);
        assert divider2 != null;
        divider2.setVisibility(View.GONE);
        assert divider3 != null;
        divider3.setVisibility(View.GONE);
        assert divider4 != null;
        divider4.setVisibility(View.GONE);
        assert divider5 != null;
        divider5.setVisibility(View.GONE);
        assert divider6 != null;
        divider6.setVisibility(View.GONE);
    }

    @Override
    public void selectedDate(int year, int month, int day) {
        mBirthDate.setText("" + day + "/" + month + "/" + year);
    }
}
