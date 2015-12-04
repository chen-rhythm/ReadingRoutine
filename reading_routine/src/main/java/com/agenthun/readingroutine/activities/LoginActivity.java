package com.agenthun.readingroutine.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import com.agenthun.readingroutine.R;
import com.agenthun.readingroutine.datastore.UserData;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.listener.SaveListener;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    /*    public static final String USER_NAME = "USER_NAME";
        public static final String EMAIL = "EMAIL";*/
    public static String BMOB_APP_ID = "cc4a89ea058246d6693bcc479b1951e2";

    @InjectView(R.id.login_name)
    EditText loginName;
    @InjectView(R.id.login_password)
    EditText loginPassword;

    public static UserData userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //初始化Bmob
        Bmob.initialize(this, BMOB_APP_ID);

        ButterKnife.inject(this);

        userData = UserData.getCurrentUser(this, UserData.class);
        if (userData != null) {
            Intent intent = new Intent(this, MainActivity.class);
/*            intent.putExtra(USER_NAME, userData.getUsername());
            intent.putExtra(EMAIL, userData.getEmail());*/
            startActivity(intent);
            finish();
        } else {
            userData = new UserData();
        }
    }

    public static UserData getUser() {
        return userData;
    }

    @OnClick(R.id.sign_in_button)
    public void onSignInBtnClick() {
        attemptLogin();
    }

    private void attemptLogin() {
        String name = loginName.getText().toString();
        String password = loginPassword.getText().toString();

        if (name.isEmpty()) {
            Toast.makeText(LoginActivity.this, R.string.error_invalid_account, Toast.LENGTH_SHORT).show();
            YoYo.with(Techniques.Shake).duration(500).delay(100).playOn(loginName);
            return;
        } else if (password.isEmpty()) {
            Toast.makeText(LoginActivity.this, R.string.error_invalid_password, Toast.LENGTH_SHORT).show();
            YoYo.with(Techniques.Shake).duration(500).delay(100).playOn(loginPassword);
            return;
        }

        userData.setUsername(name);
        userData.setPassword(password);
        userData.setEmail("hun@126.com");
        userData.login(LoginActivity.this, new SaveListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(LoginActivity.this, R.string.msg_success, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
/*                intent.putExtra(USER_NAME, userData.getUsername());
                intent.putExtra(EMAIL, userData.getEmail());*/
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(LoginActivity.this, R.string.msg_fail, Toast.LENGTH_SHORT).show();
                YoYo.with(Techniques.Shake).duration(500).delay(100).playOn(loginPassword);
            }
        });
    }
}



