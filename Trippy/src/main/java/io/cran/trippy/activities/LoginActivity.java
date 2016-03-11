package io.cran.trippy.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.parse.ParseObject;

import io.cran.trippy.R;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();



    }


    }



