package com.studioseven.postcard.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.studioseven.postcard.Network.RestAPI;
import com.studioseven.postcard.R;
import com.studioseven.postcard.Utils.LocalStorageHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.Map;

public class SignInActivity extends AppCompatActivity {
    Button oBtn;
    ImageView gBtn;
    EditText username,pass;
    ShimmerFrameLayout logoShimmer;

    String idToken, userId,fname,lname,email,result,token, displayName;
    boolean isGoogle = false;

    private static final int RC_SIGN_IN=9001;

    GoogleApiClient mGoogleApiClient;

    LocalStorageHelper localStorageHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        gBtn =findViewById(R.id.googleLogin);
        oBtn =findViewById(R.id.Login);
        username=findViewById(R.id.Username);
        pass=findViewById(R.id.password);
        logoShimmer=findViewById(R.id.logoShimmerSignIn);

        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_clientid))
                .requestEmail()
                .build();

        localStorageHelper = new LocalStorageHelper(this);


        gBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }


        });

        /*mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser()!=null)
                {
                    Intent i=new Intent(SignInActivity.this,IntroActivity.class);
                    handleResponse(i);
                    finish();
                }
            }
        };*/

        oBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userId =username.getText().toString();
                idToken =pass.getText().toString();
                fname="fname";
                lname="lname";
                email="email";
                signUpApiCal(userId, idToken,fname,lname,email);
            }
        });

        mGoogleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(SignInActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        //startActivity(new Intent(this, MainActivity.class));
    }


    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result=Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess())
            {
                GoogleSignInAccount account=result.getSignInAccount();
                isGoogle = true;
                handleSignIn(account);
            }
            else {
                Toast.makeText(this, "Auth Went Wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void handleSignIn(final GoogleSignInAccount account) {
        String idToken= account.getIdToken();
        Log.d("DD",idToken);
        lname=account.getFamilyName();
        fname=account.getGivenName();
        email=account.getEmail();
        displayName = account.getDisplayName();
        userId=email.split("@")[0];

        // Save profile info locally
        localStorageHelper.updateToken(idToken);
        localStorageHelper.updateFname(fname);
        localStorageHelper.updateLname(lname);
        localStorageHelper.updateEmail(email);
        localStorageHelper.updateUserId(userId);
        localStorageHelper.updateUserName(displayName);

        // Sign up for the first time, else sign in
        SharedPreferences sharedPreferences = getSharedPreferences("Auth", Context.MODE_PRIVATE);
        if(!sharedPreferences.getBoolean("SIGNEDUP", false)){
            sharedPreferences.edit().putBoolean("SIGNEDUP", true).apply();
            signUpApiCal(userId,idToken,fname,lname,email);
        }
        else signInApiCall(userId,idToken);

    }

    private void signUpApiCal(final String user, final String idToken, String fname, String lname, final String email) {
        startShimmer();
        Toast.makeText(this, "Signing Up...", Toast.LENGTH_SHORT).show();

        RestAPI.Companion.getAppService().signUp(user,idToken,fname,lname, String.valueOf(isGoogle), email).enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(@NonNull Call<Map<String, String>> call, @NonNull Response<Map<String, String>> response) {
                if(response.body().get("error") != null)  signInApiCall(user,idToken);
                else handleResponse(response);
            }

            @Override
            public void onFailure(@NonNull Call<Map<String, String>> call, @NonNull Throwable t) {
                Toast.makeText(SignInActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void signInApiCall(String user,String idToken) {
        startShimmer();
        Toast.makeText(this, "Signing In...", Toast.LENGTH_SHORT).show();

        RestAPI.Companion.getAppService().signIn(user, String.valueOf(isGoogle), idToken).enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(@NonNull Call<Map<String, String>> call, @NonNull Response<Map<String, String>> response) {
                handleResponse(response);
            }

            @Override
            public void onFailure(@NonNull Call<Map<String, String>> call, @NonNull Throwable t) {
                Toast.makeText(SignInActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startShimmer() {
        findViewById(R.id.signInLogo).setVisibility(View.GONE);
        logoShimmer.setVisibility(View.VISIBLE);
    }

    private void handleResponse(Response<Map<String, String>> response) {
        Map<String, String> hm;
        hm = response.body();
        result=hm.get("result");
        token=hm.get("token");

        stopShimmer();

        if(hm.get("error") == null){
            //update token in local storage
            localStorageHelper.updateToken(token);

            //Toast.makeText(SignInActivity.this, " Result is "+result+ "  Token is "+token , Toast.LENGTH_SHORT).show();

            Intent i;
            i=new Intent(SignInActivity.this, MainActivity.class);

            startActivity(i);
        }else{
            Toast.makeText(getApplicationContext(), hm.get("error"), Toast.LENGTH_SHORT).show();
        }
    }

    private void stopShimmer() {
        logoShimmer.setVisibility(View.GONE);
        findViewById(R.id.signInLogo).setVisibility(View.VISIBLE);
    }
}

