package com.aspark.allbooks;

import static android.content.ContentValues.TAG;

import androidx.activity.result.IntentSenderRequest;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Intent;
import android.content.IntentSender;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;

public class LoginActivity extends AppCompatActivity {

    Button loginBtn;
    private SignInClient oneTapClient, oneTapClientSignUp;
    private BeginSignInRequest signInRequest, signUpRequest;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private static final int REQ_ONE_TAP_SIGN_IN = 101;
    private static final int REQ_ONE_TAP_SIGN_UP = 202;
    private boolean showOneTapUI = true;
    EditText loginEditText;
    RequestQueue requestQueue;
    Account[] account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginBtn = findViewById(R.id.loginBtn);
        loginEditText = findViewById(R.id.loginEditText);
        firebaseAuth = FirebaseAuth.getInstance();
        requestQueue = Volley.newRequestQueue(getApplicationContext());


        loginBtn.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, MainActivity.class)));
        loginEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: editText clicked");

                showOneTapSignIn();


            }
        });


    }

    private void showOneTapSignIn() {
        //setting up one tap sign in with google.

        oneTapClient = Identity.getSignInClient(this);
        signInRequest = BeginSignInRequest.builder()
                .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
                        .setSupported(true)
                        .build())
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setServerClientId("906052742414-kd8vmeo07segpllhjjpgocqjlshbhs7t.apps.googleusercontent.com")

                        .setFilterByAuthorizedAccounts(true)
                        .build())

//                // Automatically sign in when exactly one credential is retrieved.
//                .setAutoSelectEnabled(true)
                .build();

        //retrieving saved credentials with the app.
        oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener(new OnSuccessListener<BeginSignInResult>() {
                    @Override
                    public void onSuccess(BeginSignInResult beginSignInResult) {
                        //when saved credentials are found

                        Log.i(TAG, "onSuccessSignIn: found some credentials");

                        try {
                            startIntentSenderForResult(beginSignInResult.getPendingIntent().getIntentSender(), REQ_ONE_TAP_SIGN_IN, null, 0, 0, 0);

//                            IntentSenderRequest intentSenderRequest = new IntentSenderRequest.Builder(beginSignInResult.getPendingIntent().getIntentSender())
//                                    .build();


                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                // No saved credentials found.
                Log.d(TAG, e.getLocalizedMessage());
                Log.i(TAG, "onFailure: no saved credential found");

                showOneTapSignUp();
            }
        });

    }

    private void showOneTapSignUp() {

        oneTapClientSignUp = Identity.getSignInClient(this);
        signUpRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setServerClientId("906052742414-kd8vmeo07segpllhjjpgocqjlshbhs7t.apps.googleusercontent.com")
                        //show all accounts in device.
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                .build();


        oneTapClientSignUp.beginSignIn(signUpRequest)
                .addOnSuccessListener(new OnSuccessListener<BeginSignInResult>() {
                    @Override
                    public void onSuccess(BeginSignInResult beginSignInResult) {

                        Log.i(TAG, "onSuccessSignUP: got account for signUp");

                        try {
                            startIntentSenderForResult(beginSignInResult.getPendingIntent().getIntentSender(), REQ_ONE_TAP_SIGN_UP, null, 0, 0, 0);
                        } catch (IntentSender.SendIntentException e) {
                            Log.e(TAG, "Couldn't start One Tap UI: " + e.getLocalizedMessage());
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG, "onFailure: No account found for signUP");

                        // No Google Accounts found. Just continue presenting the signed-out UI.
                        Log.d(TAG, e.getLocalizedMessage());

                    }
                });


    }


    // result of one tap sign in can be retrieved from onActivityResult

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_ONE_TAP_SIGN_IN) {
            try {
                SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(data);
                String idToken = credential.getGoogleIdToken();
                String username = credential.getId();
                String password = credential.getPassword();
                account = AccountManager.get(getApplicationContext()).getAccountsByType("");
                Log.d(TAG, "onActivityResult: Username =" + username);
                if (idToken != null) {

                    Log.i(TAG, "onActivityResultSignIn: idToken =" + idToken);
                    authWithFirebase(idToken);


                } else if (password != null) {

                    Log.d(TAG, "onActivityResult: password " + password);
                }


            } catch (ApiException e) {

                switch (e.getStatusCode()) {

                    case CommonStatusCodes.CANCELED:
                        Log.d(TAG, "onActivityResult: one tap Ui closed ");

                        // Don't re-prompt the user.
                        showOneTapUI = false;
                        break;
                    case CommonStatusCodes.NETWORK_ERROR:
                        Log.d(TAG, "onActivityResult: One tap encountered a network error");
                        break;
                    default:
                        Log.d(TAG, "onActivityResult: Couldn't get credential from result " + e.getLocalizedMessage());
                        break;
                }
            }
        } else if (requestCode == REQ_ONE_TAP_SIGN_UP) {

            Log.i(TAG, "onActivityResult: success for signUp");

            SignInCredential credential = null;
            try {
                credential = oneTapClient.getSignInCredentialFromIntent(data);

                String idToken = credential.getGoogleIdToken();
                if (idToken != null) {
                    // Got an ID token from Google. Use it to authenticate
                    // with your backend.
                    Log.d(TAG, "Got ID token. " + idToken);
                    authWithFirebase(idToken);
                }

            } catch (ApiException e) {
                e.printStackTrace();
                Log.i(TAG, "onActivityResult: Couldn't get the google id");
            }


        }
    }

    private void authWithFirebase(String idToken) {

        Log.i(TAG, "authWithFirebase: Authenticating with firebase ");
        AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(firebaseCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Log.i(TAG, "onComplete: Firebase signIn with credential success ");
                            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                            if (currentUser != null) {

                                Log.i(TAG, "onComplete: currentUser " + currentUser.getEmail());
                                Log.i(TAG, "onComplete: currentUser " + currentUser.getDisplayName());



                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("UserName", currentUser.getDisplayName());
                                startActivity(intent);
                                finish();
                            }

                        } else {

                            Log.i(TAG, "SignInFailed " + task.getException());

                        }

                    }
                });


    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.i(TAG, "onStart: Starting loginActivity ");
        // checking if the user is signed in.


        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            Log.i(TAG, "onStart: Current User " + currentUser.getDisplayName());


//            String authCode= getAuthCode();
//            getAccessToken(authCode);


            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("UserName", currentUser.getDisplayName());
            startActivity(intent);
            finish();
        } else
            Log.i(TAG, "onStart: No current User found ");

    }
}

