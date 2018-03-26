package cs.justnew;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener{

    EditText user,pass;
    ProgressBar p1;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        findViewById(R.id.textViewSignup).setOnClickListener(this);
        findViewById(R.id.buttonLogin).setOnClickListener(this);

        user =  (EditText) findViewById(R.id.editTextEmail);
        pass = (EditText) findViewById(R.id.editTextPassword);
        p1 = (ProgressBar)findViewById(R.id.progressbar);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.textViewSignup:
                finish();
                startActivity(new Intent(this,MainActivity.class));
                break;
            case R.id.buttonLogin:
                login();
                break;
        }
    }

    private void login() {
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

        mAuth.signInWithEmailAndPassword(userna, passa).addOnCompleteListener(new OnCompleteListener <AuthResult>() {
            @Override
            public void onComplete(@NonNull Task <AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Login successfull",Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
                else {
                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        })
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            finish();
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(Main2Activity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });

    }

    private void updateUI(FirebaseUser user) {
        Intent i = new  Intent(Main2Activity.this,somerandom_activity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!= null){
            finish();
            updateUI(currentUser);
        }

    }
}
