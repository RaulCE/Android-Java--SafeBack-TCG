package com.raulcasanova.safeback;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.raulcasanova.safeback.Configuraciones.Configuracion;
import com.raulcasanova.safeback.databinding.ActivityLogBinding;

public class LogActivity extends AppCompatActivity {

    private final int GOOGLE_SIGN_IN = 1;
    private ActivityLogBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLogBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mAuth = FirebaseAuth.getInstance();

        binding.btnLogLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.txtEmailLogIn.getText().toString().isEmpty()
                        && !binding.txtPasswordLogIn.getText().toString().isEmpty()){
                    doLogin(binding.txtEmailLogIn.getText().toString(),
                            binding.txtPasswordLogIn.getText().toString());
                }
            }
        });

        binding.btnRegistrarLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.txtEmailLogIn.getText().toString().isEmpty()
                        && !binding.txtPasswordLogIn.getText().toString().isEmpty()){
                    doRegister(binding.txtEmailLogIn.getText().toString(),
                            binding.txtEmailLogIn.getText().toString());
                }
            }
        });

        binding.btnGoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              GoogleSignInOptions gso =
                      new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                      .requestIdToken(getString(R.string.default_web_client_id))
                      .requestEmail()
                      .build();

                GoogleSignInClient googleClient= GoogleSignIn.getClient(LogActivity.this,gso);
                startActivityForResult(googleClient.getSignInIntent(),GOOGLE_SIGN_IN);
            }
        });
       // startActivity(new Intent(this, MainActivity.class));
        //finish();

    }
    private void doRegister(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "createUserWithEmail:success");
                            Configuracion.userLoged = mAuth.getCurrentUser();
                            updateUI(Configuracion.userLoged);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LogActivity.this, "Register failed.",
                                    Toast.LENGTH_SHORT).show();
                            Configuracion.userLoged = null;
                            updateUI(null);
                        }
                    }
                });
    }

    private void doLogin(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithEmail:success");
                            Configuracion.userLoged = mAuth.getCurrentUser();
                            updateUI(Configuracion.userLoged);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LogActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            Configuracion.userLoged = null;
                            updateUI(null);
                        }
                    }
                });
    }
    private void updateUI(FirebaseUser userLoged) {
        if (userLoged != null) {
            // Abrir nueva ventana
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            Configuracion.userLoged = mAuth.getCurrentUser();
                            updateUI(Configuracion.userLoged);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LogActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            Configuracion.userLoged = null;
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Configuracion.userLoged = mAuth.getCurrentUser();
        updateUI(Configuracion.userLoged);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==GOOGLE_SIGN_IN){
            GoogleSignInResult result=
                    Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()){
                GoogleSignInAccount account=result.getSignInAccount();
                firebaseAuthWithGoogle(account.getIdToken());
            }else {
                Toast.makeText(LogActivity.this, "ERROR G", Toast.LENGTH_SHORT).show();
            }

            }
    }
}