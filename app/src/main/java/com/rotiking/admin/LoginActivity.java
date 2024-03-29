package com.rotiking.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.rotiking.admin.common.auth.Auth;
import com.rotiking.admin.common.auth.AuthPreferences;
import com.rotiking.admin.common.db.Database;
import com.rotiking.admin.common.security.AES128;
import com.rotiking.admin.utils.Promise;
import com.rotiking.admin.utils.Validator;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private AppCompatButton loginBtn;
    private TextView forgetPasswordTxt, termsPrivacy;
    private EditText email_eTxt, password_eTxt;
    private CircularProgressIndicator loginProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email_eTxt = findViewById(R.id.email_e_txt);
        password_eTxt = findViewById(R.id.password_e_txt);
        loginBtn = findViewById(R.id.login_btn);
        loginProgress = findViewById(R.id.login_progress);
        forgetPasswordTxt = findViewById(R.id.forget_password);
        termsPrivacy = findViewById(R.id.terms_privacy);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loginBtn.setOnClickListener(view -> {
            String email = email_eTxt.getText().toString();
            String password = password_eTxt.getText().toString();

            if (Validator.isEmpty(email)) {
                email_eTxt.setError("Required Field");
                return;
            }

            if (!Validator.isEmail(email)) {
                email_eTxt.setError("Invalid Email");
                return;
            }

            if (Validator.isEmpty(password)) {
                password_eTxt.setError("Required Field");
                return;
            }

            Object[] isPassword = Validator.isPassword(password);

            if (!((boolean) isPassword[0])) {
                String err = (String) isPassword[1];
                password_eTxt.setError(err);
                return;
            }

            loginBtn.setVisibility(View.INVISIBLE);
            loginProgress.setVisibility(View.VISIBLE);

            Auth.getInstance().signInWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
                loginProgress.setVisibility(View.VISIBLE);
                Database.getInstance().collection("user").document(Objects.requireNonNull(authResult.getUser()).getUid())
                        .collection("data").document("profile").get().addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                if (!documentSnapshot.get("accType", String.class).equals("ADMIN")) {
                                    Toast.makeText(LoginActivity.this, "You have no Permission to login to this app.", Toast.LENGTH_SHORT).show();
                                    Auth.getInstance().signOut();
                                    loginBtn.setVisibility(View.VISIBLE);
                                    loginProgress.setVisibility(View.INVISIBLE);
                                    return;
                                }

                                String authToken = documentSnapshot.get("authToken", String.class);
                                String encKey = AES128.decrypt(AES128.NATIVE_ENCRYPTION_KEY, documentSnapshot.get("encKey", String.class));

                                AuthPreferences preferences = new AuthPreferences(this);
                                preferences.setAuthToken(authToken);
                                preferences.setEncryptionKey(encKey);

                                loginProgress.setVisibility(View.GONE);

                                Intent intent = new Intent(this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            } else {
                                loginBtn.setVisibility(View.VISIBLE);
                                loginProgress.setVisibility(View.INVISIBLE);

                                Toast.makeText(LoginActivity.this, "No Account Exists.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }).addOnFailureListener(e -> {
                loginBtn.setVisibility(View.VISIBLE);
                loginProgress.setVisibility(View.INVISIBLE);
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        });

        forgetPasswordTxt.setOnClickListener(view -> {
            Intent intent = new Intent(this, RecoverPasswordActivity.class);
            startActivity(intent);
        });

        termsPrivacy.setOnClickListener(view -> {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://rotiking.co.in/privacy/"));
            startActivity(i);
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (Auth.isUserAuthenticated(this)) {
            Auth.getMessaging().getToken().addOnSuccessListener(s -> Auth.Login.updateMessageToken(this, s, new Promise<String>() {
                @Override
                public void resolving(int progress, String msg) {
                }

                @Override
                public void resolved(String o) {
                }

                @Override
                public void reject(String err) {
                }
            }));
        }
    }
}