package com.aspark.allbooks.Activity;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.aspark.allbooks.FireStore;
import com.aspark.allbooks.R;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.SignInButton;
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
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private  final String TAG = "LoginActivity";
    Button loginBtn ;
    SignInButton googleSignInBtn;
    private SignInClient oneTapClient, oneTapClientSignUp;
    private BeginSignInRequest signInRequest, signUpRequest;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private static final int REQ_ONE_TAP_SIGN_IN = 101;
    private static final int REQ_ONE_TAP_SIGN_UP = 202;
    private boolean showOneTapUI = true;
    EditText loginEditText;
    RequestQueue requestQueue;
    Account[] account;
    String email;
    TextView signUpTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginBtn = findViewById(R.id.loginBtn);
        loginEditText = findViewById(R.id.loginEditText);
        googleSignInBtn = findViewById(R.id.signInGoogleBtn);
        signUpTextView = findViewById(R.id.signUpTextView);

        firebaseAuth = FirebaseAuth.getInstance();
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        loginEditText.setFocusedByDefault(false);
        loginEditText.clearFocus();

        email = getIntent().getStringExtra("email");
        if (email !=null)
            loginEditText.setText(email);

        loginBtn.setOnClickListener(view -> {

           email  = loginEditText.getText().toString().trim();

            if (! email.isEmpty())
                verifyEmail();

            else
                loginEditText.setError("Please enter a valid email or phone number");

        });

        googleSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i(TAG, "onClick: sign in with google clicked");

                showOneTapSignIn();
            }
        });

        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Signing up new user ");

                startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
            }
        });


    }

    private void verifyEmail() {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.fetchSignInMethodsForEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                            @Override
                            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {

                                if (task.isSuccessful()) {

                                    boolean emailEmpty = Objects.requireNonNull(task.getResult().getSignInMethods()).isEmpty();
                                    if (! emailEmpty) {
                                        Log.d(TAG, "onComplete: email exists");

                                        Intent intent = new Intent(LoginActivity.this, LoginActivity2.class);
                                        intent.putExtra("email", email);
                                        startActivity(intent);
                                    }
                                    else
                                        Toast.makeText(LoginActivity.this, "Email not registered", Toast.LENGTH_SHORT).show();

                                }
                                else
                                    Log.d(TAG, "onComplete: fetching email methods Task failed");


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(LoginActivity.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
                    }
                });



//        firebaseAuth.signInWithEmailAndPassword()
//
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        Query query = db.collection("users")
//                .whereEqualTo("email",email);
//
//        query.get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//
//                        if (task.isSuccessful()) {
//
//                            QuerySnapshot documentSnapshot = task.getResult();
//                            if (!documentSnapshot.isEmpty()) {
//                                Log.d(TAG, "onComplete: User verified");
//
//
//
//                            } else {
//                                Log.i(TAG, "onComplete: New User, SignUp!");
//                                Toast.makeText(LoginActivity.this, "SignUp", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                        else
//                            Toast.makeText(LoginActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.e(TAG, "onFailure: Something went wrong, cannot verify user." );
//                    }
//                });
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

        SharedPreferences preferences = getBaseContext().getSharedPreferences(getBaseContext().getPackageName(), MODE_PRIVATE);

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

                                if (preferences.getString("refresh_token", null) == null) {

                                    Log.i(TAG, "Seems like its your first time ;)");

                                    FireStore fireStore = new FireStore(getApplicationContext());
                                   // fireStore.createCollections(userId);
                                    fireStore.addUser(null,currentUser.getDisplayName(),null, currentUser.getPhoneNumber()
                                            ,currentUser.getEmail(),null,null);

//                                    getAuthCode();
                                } else {
                                    Log.i(TAG, "Hey! you are back :)");

                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("UserName", currentUser.getDisplayName());
                                    startActivity(intent);
                                    finish();
                                }
                            }

                        } else {
                            Log.i(TAG, "SignInFailed " + task.getException());
                        }
                    }
                });


    }

    private void getAuthCode() {

//        Log.i(TAG, "getAuthCode: Creating custom  tab");

        String url = "https://accounts.google.com/o/oauth2/v2/auth?" +
                "scope=https://www.googleapis.com/auth/books&" +
                "response_type=code&" +
                "access_type=offline&" +     // this parameter is added to get the refresh token. refresh token will only be generated for the first time new user sign in
                // for testing purposes remove the access of the app from your google account and sign in again.
                "state=security_token%3D138r5719ru3e1%26url%3Dhttps%3A%2F%2Foauth2.aspark.com%2Ftoken&" +

                "redirect_uri=http://localhost:5000&" +
//                "redirect_uri=com.googleusercontent.apps.906052742414-4jn3rbh19drr791el78uun1di9i7hs21/oauth2redirect&"+
//                "redirect_uri=aspark://906052742414-4jn3rbh19drr791el78uun1di9i7hs21.apps.googleusercontent.com/oauth2redirect&" +
                //     "redirect_uri=book-review-347211.firebaseapp.com&"+
//              "redirect_uri=com.aspark.allBooks%3A/oauth2redirect&" +

                "client_id=906052742414-kd8vmeo07segpllhjjpgocqjlshbhs7t.apps.googleusercontent.com&"

//                "client_id=906052742414-4jn3rbh19drr791el78uun1di9i7hs21.apps.googleusercontent.com&" +
//                "login_hint=ashutoshgupta1422@gmail.com"
                ;

        //TODO implement custom tabs , it is not redirecting to the app after getting auth code
//        // Creating custom tab
//        CustomTabsIntent.Builder customTab = new CustomTabsIntent.Builder();
//
//        customTab.setColorScheme(CustomTabsIntent.COLOR_SCHEME_SYSTEM);
//        customTab.build().launchUrl(LoginActivity.this,Uri.parse(url));

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//        intent.putExtra("url",url);
        Log.i(TAG, "getAuthToken: url " + url);
        startActivity(intent);

    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.i(TAG, "onStart: Starting loginActivity ");

        SharedPreferences preferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);

        String refreshToken = preferences.getString("refresh_token", null);
        Log.i(TAG, "onStart: REFRESH_TOKEN " + refreshToken);
        Log.i(TAG, "onStart: ACCESS_TOKEN " + preferences.getString("access_token", "Not found"));


//        // checking if the user is signed in and if the user has given access to google account after installing app.
//        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
//        if (currentUser != null ) {
//
//            Log.i(TAG, "onStart: Current User " + currentUser.getDisplayName());
//
//            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//            startActivity(intent);
//            finish();
//        } else
//            Log.i(TAG, "onStart: No current User found ");


    }

}

