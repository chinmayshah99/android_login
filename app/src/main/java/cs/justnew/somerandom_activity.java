package cs.justnew;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class somerandom_activity extends AppCompatActivity {

    TextView t1;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_somerandom_activity);
        mAuth = FirebaseAuth.getInstance();
        t1 = (TextView)findViewById(R.id.textView);
        display_info();
        
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null)
        {
            finish();
            startActivity(new Intent(somerandom_activity.this,MainActivity.class));
        }

    }

    private void display_info() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String email = currentUser.getEmail();
        t1.setText(email);

    }
}
