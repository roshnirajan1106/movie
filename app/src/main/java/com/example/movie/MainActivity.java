package com.example.movie;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener{
    LoginButton btn;
    CallbackManager cm;
    EditText email, pass;
    TextView txt1, txt2, txt3, txt;
    Button lgn;
    FirebaseAuth fAuth;

    private SignInButton SignIn;
    private GoogleApiClient googleApiClient;
    private static final int REQ_CODE=900;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = findViewById(R.id.login);
        lgn=findViewById(R.id.button2);
        cm = CallbackManager.Factory.create();

        txt1 = findViewById(R.id.textView2);
        txt2 = findViewById(R.id.textView3);
        txt3 = findViewById(R.id.textView4);
        email = findViewById(R.id.editText);
        pass = findViewById(R.id.editText2);
        fAuth = FirebaseAuth.getInstance();
        txt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(getApplicationContext(), Main3Activity.class);
                startActivity(i);
            }
        });
        lgn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = email.getText().toString().trim();
                String room = pass.getText().toString().trim();
                if (TextUtils.isEmpty((name))) {
                    email.setError("Email is required");
                    return;
                }
                if (TextUtils.isEmpty(room)) {
                    pass.setError("Room no. is must ");
                    return;
                }
                if (room.length() < 6) {
                    pass.setError("password should be of atleast 6 characters");
                    return;
                }
                fAuth.signInWithEmailAndPassword(name, room).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), Main4Activity.class));
                        } else {
                            Toast.makeText(MainActivity.this, "error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();


                        }

                    }
                });
            }
        });



        txt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recoverypassworddialog();
            }
        });



        btn.registerCallback(cm, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
            startActivity(new Intent(getApplicationContext(),Main4Activity.class));
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        SignIn=findViewById(R.id.bn_login);
        ((View) SignIn).setOnClickListener(this);
        GoogleSignInOptions signInOptions=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        googleApiClient=new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions).build();





    }


    private void signin1(){

        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent,REQ_CODE);}

    private void recoverypassworddialog() {
        AlertDialog.Builder build= new AlertDialog.Builder(this);
        LinearLayout l= new LinearLayout(this);
        final EditText email= new EditText(this);
        email.setHint("Email");
        email.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        l.addView(email);
        l.setPadding(10,10,10,10);
        build.setView(l);
        build.setPositiveButton("recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String et= email.getText().toString().trim();
                fAuth.sendPasswordResetEmail(et).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this,"Email sent",Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this," "+ e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        build.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        cm.onActivityResult(requestCode,resultCode,data);

        if(requestCode==REQ_CODE){

            Toast.makeText(this, "Signed In Succesfully", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this,"Sign In Failed",Toast.LENGTH_SHORT).show();
        }
    }






    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bn_login) {
            signin1();
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
