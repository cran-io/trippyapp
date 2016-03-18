package io.cran.trippy.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;

import io.cran.trippy.R;

public class ShareWithFriends extends AppCompatActivity {
    private CallbackManager sCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_with_friends);

        String AppURl = "https://fb.me/434164010126445";  //Generated from //fb developers


        sCallbackManager = CallbackManager.Factory.create();

        if (AppInviteDialog.canShow()) {
            AppInviteContent content = new AppInviteContent.Builder()
                    .setApplinkUrl(AppURl)
                    .build();

            AppInviteDialog appInviteDialog = new AppInviteDialog(this);
            appInviteDialog.registerCallback(sCallbackManager,
                    new FacebookCallback<AppInviteDialog.Result>() {
                        @Override
                        public void onSuccess(AppInviteDialog.Result result) {
                            Log.d("Invitation", "Invitation Sent Successfully");
                            finish();
                        }

                        @Override
                        public void onCancel() {
                        }

                        @Override
                        public void onError(FacebookException e) {
                            Log.d("Invitation", "Error Occured");
                        }
                    });

            appInviteDialog.show(content);
        }

    }
}
