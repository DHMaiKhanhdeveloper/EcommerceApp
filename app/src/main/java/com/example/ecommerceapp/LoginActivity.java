package com.example.ecommerceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ecommerceapp.utils_service.UtilService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin;
    private EditText edtEmail, edtPassword;
    private String strEmail, strPassword;

    private UtilService utilService;
    private boolean passwordVisible;

    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init(){
        mAuth = FirebaseAuth.getInstance();

        btnLogin = findViewById(R.id.btn_login);
        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);


        utilService = new UtilService();
        loadingBar = new ProgressDialog(this);


        edtPassword.setOnTouchListener((v, event) -> {
            final int Right = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= edtPassword.getRight() - edtPassword.getCompoundDrawables()[Right].getBounds().width()) {
                    int selection = edtPassword.getSelectionEnd();
                    if (passwordVisible) {
                        edtPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_visibility_off_24, 0);
                        edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        passwordVisible = false;
                    } else {
                        edtPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_visibility_24, 0);
                        edtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        passwordVisible = true;
                    }
                    edtPassword.setSelection(selection);
                    return true;
                }
            }

            return false;
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utilService.hideKeyboard(v, LoginActivity.this);
                clickSignIn(v);
            }
        });


    }

    private void clickSignIn(View view) {

        strEmail = edtEmail.getText().toString().trim();
        strPassword = edtPassword.getText().toString().trim();


        if (TextUtils.isEmpty(strEmail)) {
            Toast.makeText(LoginActivity.this, "Vui l??ng nh???p email c???a b???n", Toast.LENGTH_SHORT).show();
            utilService.showSnackBar(view,"Vui l??ng nh???p email c???a b???n");
            edtEmail.setError("B???t bu???c ph???i nh???p email");
            edtEmail.requestFocus();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()) {
            Toast.makeText(LoginActivity.this, "Vui l??ng nh???p l???i email c???a b???n", Toast.LENGTH_LONG).show();
            utilService.showSnackBar(view,"Vui l??ng nh???p email c???a b???n");
            edtEmail.setError("B???t bu???c ph???i nh???p email");
            edtEmail.requestFocus();
        } else if (TextUtils.isEmpty(strPassword)) {
            Toast.makeText(LoginActivity.this, "Vui l??ng nh???p m???t kh???u", Toast.LENGTH_SHORT).show();
            utilService.showSnackBar(view,"Vui l??ng nh???p m???t kh???u");
            edtEmail.setError("B???t bu???c ph???i nh???p m???t kh???u");
            edtEmail.requestFocus();
        } else {

            ShowSignIn();
        }
    }



    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
//        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);
        finish();
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null)
        {
            SendUserToMainActivity();
        }
    }

    private void ShowSignIn() {
        loadingBar.setTitle("????ng nh???p");
        loadingBar.setMessage("Vui l??ng ?????i trong khi ch??ng t??i cho ph??p b???n ????ng nh???p v??o T??i kho???n c???a m??nh.....");
        loadingBar.setCanceledOnTouchOutside(true);
        loadingBar.show();

        mAuth.signInWithEmailAndPassword(strEmail ,strPassword ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
//                    Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
////                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(mainIntent);
//                    overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);

//                    Toast.makeText(LoginActivity.this, "B???n ???? ????ng nh???p th??nh c??ng", Toast.LENGTH_SHORT).show();
//                    SendUserToMainActivity();
//                    loadingBar.dismiss();

                    FirebaseUser user = mAuth.getCurrentUser();
                    assert user != null;
                    if (user.isEmailVerified()) {
                        Toast.makeText(LoginActivity.this, "B???n h??y ????ng nh???p ngay b??y gi???", Toast.LENGTH_SHORT).show();

                        SendUserToMainActivity();
//                        startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
//                        overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);
                    }else {

                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this, "Email verification link send", Toast.LENGTH_SHORT).show();
                                    showAlertDialog();
                                }
                            }
                        });
                    }
                }
                else {
                    String message = task.getException().getMessage();
                    Toast.makeText(LoginActivity.this, "Kh??ng c?? t??n ng?????i ????ng nh???p " + message, Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }
        });
    }

    private void showAlertDialog() {
        //thi???t l???p tr??nh t???o c???nh b??o
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Email ch??a ???????c x??c minh");
        builder.setMessage("Vui l??ng x??c minh email c???a b???n ngay b??y gi???. B???n kh??ng th??? ????ng nh???p m?? kh??ng x??c minh email. ");


        builder.setPositiveButton("Ti???p t???c", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                //G???i ???ng d???ng email trong c???a s??? m???i v?? kh??ng ph???i trong ???ng d???ng c???a ch??ng t??i
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    public void onLoginClick(View View){
        startActivity(new Intent(this,RegisterActivity.class));
        overridePendingTransition(R.anim.slide_in_right,R.anim.stay);

    }
}