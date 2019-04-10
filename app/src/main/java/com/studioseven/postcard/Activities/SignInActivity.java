package com.studioseven.postcard.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.studioseven.postcard.Network.RestAPI;
import com.studioseven.postcard.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.Map;

public class SignInActivity extends AppCompatActivity {
    Button gbtn,obtn;
    EditText username,pass;
    ProgressBar progressBar;
    String IdToken,user,fname,lname,email,result,token;
    private static final int RC_SIGN_IN=9001;
    GoogleApiClient mGoogleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        gbtn=findViewById(R.id.googleLogin);
        obtn=findViewById(R.id.Login);
        username=findViewById(R.id.Username);
        pass=findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar);

        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_clientid))
                .requestEmail()
                .build();


        // RestAPI Code

        gbtn.setOnClickListener(new View.OnClickListener() {
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
                    startActivity(i);
                    finish();
                }
            }
        };*/

        obtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user=username.getText().toString();
                IdToken=pass.getText().toString();
                fname="fname";
                lname="lname";
                email="email";
                api_call(user,IdToken,fname,lname,email);
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
        user=email.split("@")[0];

        api_call(user,idToken,fname,lname,email);

    }

    void api_call(String user,String IdToken,String fname,String lname,String email)
    {
        progressBar.setVisibility(View.VISIBLE);

        RestAPI.Companion.getAppService().signUp(user,IdToken,fname,lname,email).enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                Map<String, String> hm;
                hm = response.body();
                result=hm.get("result");
                token=hm.get("jwt");

                progressBar.setVisibility(View.GONE);
                Toast.makeText(SignInActivity.this, " REsult is "+result+ "  Token is "+token , Toast.LENGTH_SHORT).show();

                Intent i=new Intent(SignInActivity.this,MainActivity.class);
                startActivity(i);
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                Toast.makeText(SignInActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

