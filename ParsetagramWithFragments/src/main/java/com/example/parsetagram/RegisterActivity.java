package com.example.parsetagram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class RegisterActivity extends AppCompatActivity {
    public static final String TAG = "RegisterActivity";
    EditText etUsername;
    EditText etPassword;
    EditText etConfirmPassword;
    TextView tvMessage;
    Button btCreateAccount;
    ImageView ivLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        tvMessage = findViewById(R.id.tvMessage);
        btCreateAccount = findViewById(R.id.btCreateAccount);
        ivLogo = findViewById(R.id.ivLogo);

        btCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick login button");
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                String checkPassword = etConfirmPassword.getText().toString();
                makeAccount(username, password, checkPassword);
            }
        });

    }

    private void makeAccount(String username, String password, String checkPassword) {
        if (!checkPassword.equals(password)) {
            Toast.makeText(RegisterActivity.this, "The passwords aren't equal to each other! Retype your password.", Toast.LENGTH_SHORT).show();
            Log.i(TAG, checkPassword);
            Log.i(TAG, password);
            return;
        }
        registerAccount(username, password);
    }

    private void registerAccount(String username, String password) {
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    // TODO: better error handling
                    Toast.makeText(RegisterActivity.this, "Issue with registering :(", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Issue with registering", e);
                    return;
                }
                try {
                    user.save();
                    ParsetagramHelper.loginUser(username, password, RegisterActivity.this);
                } catch (ParseException i) {
                    e.printStackTrace();
                    Log.e(TAG, "couldn't make account", i);
                    Toast.makeText(RegisterActivity.this, "Issue with creating account :(", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

}