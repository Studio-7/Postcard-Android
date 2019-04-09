package com.studioseven.postcard.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;
import com.studioseven.postcard.Network.RestAPI;
import com.studioseven.postcard.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.HashMap;
import java.util.Map;

public class SignInActivity extends AppCompatActivity {
    Button gbtn,obtn;
    String IdToken,user,fname,lname,email,result,token;
    private static final int RC_SIGN_IN=9001;
    GoogleApiClient mGoogleApiClient;
    // FirebaseAuth mAuth;
   // FirebaseAuth.AuthStateListener mAuthListener;

    /*@Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        gbtn=findViewById(R.id.googleLogin);


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




        // mAuth = FirebaseAuth.getInstance();

       /* mAuthListener=new FirebaseAuth.AuthStateListener() {
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
                HandleSignIn(account);

            }
            else {
                Toast.makeText(this, "Auth Went Wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void  HandleSignIn(final GoogleSignInAccount account) {
        String IdToken= account.getIdToken();
        Log.d("DD",IdToken);
        lname=account.getFamilyName();
        fname=account.getGivenName();
        email=account.getEmail();
        String[] arr=email.split("@");
        user=arr[0];

        RestAPI.Companion.getAppService().signUp(user,IdToken,fname,lname,email).enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                Map<String, String> hm = new HashMap<>();
                hm = response.body();
                result=hm.get("result");
                token=hm.get("jwt");
                Toast.makeText(SignInActivity.this, " REsult is "+result+ "  Token is "+token , Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {

            }
        });
        Toast.makeText(this, "Result is "+ result+ " jwt: "+ token, Toast.LENGTH_SHORT).show();

     // Toast.makeText(this, " Username "+ user+" Lname : "+lname+" fname: "+fname+" email: "+email , Toast.LENGTH_LONG).show();
        Intent i=new Intent(SignInActivity.this,IntroActivity.class);
        startActivity(i);


       /* AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                // Name, email address, and profile photo Url
                                String name = user.getDisplayName();
                                String email = user.getEmail();


                                // The user's ID, unique to the Firebase project. Do NOT use this value to
                                // authenticate with your backend server, if you have one. Use
                                // FirebaseUser.getIdToken() instead.
                                String token= account.getIdToken();
                                String uid = user.getUid();
                                Toast.makeText(SignInActivity.this, " name" + name + " \nUID " + uid +" \nToken "+ token, Toast.LENGTH_SHORT).show();
                                // updateUI(user);
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            Toast.makeText(SignInActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                            // updateUI(null);
                        }
                    }
                });*/
    }
}

