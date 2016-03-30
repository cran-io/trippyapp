package io.cran.trippy.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import io.cran.trippy.R;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    private LoginButton facebookLoginBtn;
    private CallbackManager callbackManager;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    private String email=null;
    private String birthday= null;
    private String name=null;
    private String id=null;
    private Uri imageUri=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        SignInButton signInGoogleButton = (SignInButton) findViewById(R.id.googleLogin);
        signInGoogleButton.setOnClickListener(this);

        callbackManager = CallbackManager.Factory.create();

        facebookLoginBtn = (LoginButton) findViewById(R.id.facebookLogin);
        facebookLoginBtn.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_friends"));
        facebookLoginBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());

                                try {
                                    email = object.getString("email");
                                    birthday = object.getString("birthday"); // 01/31/1980 format
                                    name= object.getString("name");
                                    id= object.getString("id");

                                    imageUri= Uri.parse("http://graph.facebook.com/"+id+"/picture?type=large");

                                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                    i.putExtra("User name",name);
                                    i.putExtra("User mail", email);
                                    i.setData(imageUri);
                                    startActivity(i);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });




                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();


            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "Cancel Log in", Toast.LENGTH_LONG).show();   // App code
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(LoginActivity.this, "" + exception.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("Error", "" + exception.getMessage());
            }
        });

        TextView signUpLink = (TextView) findViewById(R.id.signUpLink);
        assert signUpLink != null;
        signUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, SignUpActivity.class);

                startActivity(i);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Log.e("URL",""+acct.getPhotoUrl());
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            i.putExtra("User name",acct.getDisplayName());
            i.putExtra("User mail",acct.getEmail());
            i.setData(acct.getPhotoUrl());
            startActivity(i);
        } else {
            // Signed out, show unauthenticated UI.
        }
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(LoginActivity.this, "" + connectionResult.getErrorMessage(), Toast.LENGTH_LONG).show();
        Log.e("Error", "" + connectionResult.getErrorMessage());
    }

    @Override
    public void onClick(View v) {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
}



