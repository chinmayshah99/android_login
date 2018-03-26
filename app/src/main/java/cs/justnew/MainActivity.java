package cs.justnew;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth mAuth;
    ProgressBar p1;
    GoogleSignInClient mGoogleSignInClient;
    //Button l1;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user =  (EditText) findViewById(R.id.editTextEmail);
        pass = (EditText) findViewById(R.id.editTextPassword);
        p1 = (ProgressBar)findViewById(R.id.progressbar);
        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.button1111).setOnClickListener(this);
        findViewById(R.id.buttonSignUp).setOnClickListener(this);
      //  findViewById(R.id.textViewLogin)
        findViewById(R.id.textViewLogin).setOnClickListener(this);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient= GoogleSignIn.getClient(this, gso);



    }


    EditText user,pass;
    //Button

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.textViewLogin:
                startActivity(new Intent(this,Main2Activity.class));
                break;

            case R.id.buttonSignUp:
                registerUser();
                break;
            case R.id.button1111:
                signIn();
        }
    }

    private void registerUser() {
        String userna = user.getText().toString().trim();
        String passa = pass.getText().toString().trim();
        if(userna.isEmpty()){
            user.setError("EMaIL!!");
            user.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(userna).matches()){
            user.setError("EMaIL!!");
            user.requestFocus();
            return;
        }


        if(passa.isEmpty())
        {
            pass.setError("PPASAsds");
            pass.requestFocus();
            return;
        }
        if(pass.length()<6)
        {
            pass.setError("PPASAsds length 6");
            pass.requestFocus();
            return;
        }
        p1.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(userna, passa).addOnCompleteListener(new OnCompleteListener <AuthResult>() {
            @Override
            public void onComplete(@NonNull Task <AuthResult> task) {
                p1.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    finish();
                    Toast.makeText(getApplicationContext(),"succ",Toast.LENGTH_SHORT).show();
                    p1.setVisibility(View.GONE);
                    updateUI(null);
                }
                else {
                    if(task.getException()instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(getApplicationContext(),"EMAIL registered",Toast.LENGTH_SHORT).show();
                        user.setError("change mail!!");
                        user.requestFocus();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                    return;
                }

            }
        })
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            finish();
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText( MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });

    }

    private void updateUI(Object o) {
        Intent i = new  Intent(MainActivity.this,somerandom_activity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            @SuppressLint("RestrictedApi") Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                //Log.w(TAG, "Google sign in failed", e);
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }
        }
    }
    // [END onactivityresult]

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        //Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        //showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                           // Log.w(TAG, "signInWithCredential:failure", task.getException());
                            //Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END auth_with_google]

    // [START signin]
    private void signIn() {
        @SuppressLint("RestrictedApi") Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signin]
}
