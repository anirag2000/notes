package com.example.anirudh.notes;

import android.arch.core.executor.TaskExecutor;
import android.content.Intent;
import android.graphics.Paint;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;
import java.util.prefs.PreferenceChangeEvent;

public class Main2Activity extends AppCompatActivity {
    String vid;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        ProgressBar progressBar=(ProgressBar)findViewById(R.id.progressBar);
        setContentView(R.layout.activity_main2);
        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        String numbe = intent.getStringExtra("code");
        try {
            PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + numbe,        // Phone number to verify
                    60,                 // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    Main2Activity.this,               // Activity (for callback binding)
                    mCallbacks);        // OnVerificationStateChangedCallbacks
        }
        catch(Exception e)
        {
            Toast.makeText(Main2Activity.this,e.getMessage(),Toast.LENGTH_LONG).show();
        }

    }
    public void finish() {
        super.finish();
        overridePendingTransition(R.xml.slide_in_left, R.xml.slide_out_right);
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            vid=s;


        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code=phoneAuthCredential.getSmsCode();
            if(code!=null)
            {
                check(code);
            }

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(Main2Activity.this, e.getMessage(), Toast.LENGTH_LONG).show();

            Intent intentp=new Intent(Main2Activity.this,MainActivity.class);
            startActivity(intentp);

        }
    };


    public void signin(View view)

    {
        EditText ed1=(EditText)findViewById(R.id.code);
        String code=ed1.getText().toString();
        check(code);
        TextView tv1=(TextView)findViewById(R.id.textView2);

        EditText ed2=(EditText)findViewById(R.id.code);
        ImageButton button=(ImageButton)findViewById(R.id.button);
        ProgressBar pb=(ProgressBar)findViewById(R.id.progressBar);
        pb.setVisibility(View.VISIBLE);
        tv1.setVisibility(View.INVISIBLE);
        ed1.setVisibility(View.INVISIBLE);
        button.setVisibility(View.INVISIBLE);



    }


    private void check(String code)
    {

        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(vid,code);
        signinwithcredential(credential);
    }

    private void signinwithcredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            boolean isNew = task.getResult().getAdditionalUserInfo().isNewUser();
                            if(isNew)
                            {
                                Intent intent1 = new Intent(Main2Activity.this, FirstTime.class);
                                intent1.putExtra("base","otp");
                                startActivity(intent1);
                                overridePendingTransition(R.xml.slide_in_right, R.xml.slide_out_left);

                            }
                            else {


                                Intent intent1 = new Intent(Main2Activity.this, nav.class);
                                startActivity(intent1);
                                FirebaseUser user = mAuth.getCurrentUser();

                                Toast.makeText(Main2Activity.this, "VERIFICATION SUCCESSFUL", Toast.LENGTH_SHORT).show();
                            }
                            }

                        else
                        {
                            Toast.makeText(Main2Activity.this,"ERROR ",Toast.LENGTH_SHORT).show();
                            Intent intentp=new Intent(Main2Activity.this,MainActivity.class);
                            startActivity(intentp);

                        }

                    }
                });

    }
    public  void textclick(View view)
    {
        TextView tv1=(TextView)findViewById(R.id.textView2);

        EditText ed1=(EditText)findViewById(R.id.code);
        ImageButton button=(ImageButton)findViewById(R.id.button);
        ProgressBar pb=(ProgressBar)findViewById(R.id.progressBar);
        pb.setVisibility(View.INVISIBLE);
        tv1.setVisibility(View.INVISIBLE);
        ed1.setVisibility(View.VISIBLE);
        button.setVisibility(View.VISIBLE);


    }


}
