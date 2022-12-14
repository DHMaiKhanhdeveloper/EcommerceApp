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
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ecommerceapp.data_local.ShareDataLocalManager;
import com.example.ecommerceapp.utils_service.UtilService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private EditText edtEmail, edtPassword, edtConfirmPassword;
    private String  strEmail, strPassword , strConfirmPassword;
    private Button btnRegister;

    private ProgressDialog loadingBar;
    private UtilService utilService;
    private boolean passwordVisible;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        changeStatusBarColor();
        init();
    }
        @SuppressLint("ClickableViewAccessibility")
        private void init(){
            mAuth = FirebaseAuth.getInstance();

            edtEmail = findViewById(R.id.edt_email);
            edtPassword = findViewById(R.id.edt_password);
            edtConfirmPassword = findViewById(R.id.edt_confirm_password);
            btnRegister =  findViewById(R.id.btn_register);
//        progressBar = findViewById(R.id.progressBar);
            loadingBar = new ProgressDialog(this);
            utilService = new UtilService();

            edtPassword.setOnTouchListener((v, event) -> {
                final int Right = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= edtPassword.getRight() - edtPassword.getCompoundDrawables()[Right].getBounds().width()) {
                        @SuppressLint("ClickableViewAccessibility") int selection = edtPassword.getSelectionEnd();
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
            edtConfirmPassword.setOnTouchListener((v, event) -> {
                final int Right = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= edtConfirmPassword.getRight() - edtConfirmPassword.getCompoundDrawables()[Right].getBounds().width()) {
                        @SuppressLint("ClickableViewAccessibility") int selection = edtConfirmPassword.getSelectionEnd();
                        if (passwordVisible) {
                            edtConfirmPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_visibility_off_24, 0);
                            edtConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passwordVisible = false;
                        } else {
                            edtConfirmPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_visibility_24, 0);
                            edtConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passwordVisible = true;
                        }
                        edtConfirmPassword.setSelection(selection);
                        return true;
                    }
                }

                return false;
            });
            btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    utilService.hideKeyboard(v, RegisterActivity.this);
                    clickSignUp(v);
                }
            });
            if(!ShareDataLocalManager.getFirstInstall()){
            Toast.makeText(RegisterActivity.this,"First Install App",Toast.LENGTH_LONG).show();
            ShareDataLocalManager.putFirstInstall(true);
                Intent i = new Intent(RegisterActivity.this,OnBoardingActivity.class);
                startActivity(i);
                finish();

            }
        }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null)
        {
            SendUserToLoginActivity();
        }
    }

    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    private void SendUserToLoginActivity(){
        Intent mainIntent = new Intent(RegisterActivity.this, LoginActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);
        finish();
    }

    private void clickSignUp(View v) {
        strEmail = edtEmail.getText().toString().trim();

        strPassword = edtPassword.getText().toString().trim();
        strConfirmPassword = edtConfirmPassword.getText().toString().trim();
        Toast.makeText(RegisterActivity.this,strPassword,Toast.LENGTH_LONG).show();





        if (TextUtils.isEmpty((strEmail))) {
            Toast.makeText(RegisterActivity.this, "Vui l??ng nh???p email c???a b???n", Toast.LENGTH_LONG).show();
            edtEmail.setError("B???t bu???c nh???p email");
            utilService.showSnackBar(v,"Vui l??ng nh???p email c???a b???n");
            edtEmail.requestFocus();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()) { // kh??c true
            Toast.makeText(RegisterActivity.this, "Vui l??ng nh???p l???i email c???a b???n", Toast.LENGTH_LONG).show();
            edtEmail.setError(" email kh??ng h???p l???");
            utilService.showSnackBar(v,"Vui l??ng nh???p email c???a b???n");
            edtEmail.requestFocus();

        }      else if (TextUtils.isEmpty(strPassword)) {
            Toast.makeText(RegisterActivity.this, "Vui l??ng nh???p m???t kh???u", Toast.LENGTH_SHORT).show();
            edtPassword.setError("B???t bu???c ph???i nh???p m???t kh???u");
            utilService.showSnackBar(v,"Vui l??ng nh???p m???t kh???u");
            edtPassword.requestFocus();
        } else if (strPassword.length() < 6) {
            Toast.makeText(RegisterActivity.this, "M???t kh???u ph???i c?? ??t nh???t 6 k?? t??? ", Toast.LENGTH_LONG).show();
            edtPassword.setError("M???t kh???u qu?? y???u");
            utilService.showSnackBar(v,"M???t kh???u ph???i c?? ??t nh???t 6 k?? t???");
            edtPassword.requestFocus();
        } else if (TextUtils.isEmpty(strConfirmPassword)) {
            Toast.makeText(RegisterActivity.this, "Vui l??ng nh???p l???i m???t kh???u ", Toast.LENGTH_LONG).show();
            edtConfirmPassword.setError("B???t bu???c ph???i nh???p m???t kh???u x??c nh???n");
            utilService.showSnackBar(v,"Vui l??ng nh???p l???i m???t kh???u");
            edtConfirmPassword.requestFocus();
        } else if (!strPassword.equals(strConfirmPassword)) {
            Toast.makeText(RegisterActivity.this, "Vui l??ng nh???p c??ng m???t m???t kh???u ", Toast.LENGTH_LONG).show();
            edtConfirmPassword.setError("B???t bu???c ph???i nh???p m???t kh???u x??c nh???n");
            utilService.showSnackBar(v,"Vui l??ng nh???p c??ng m???t m???t kh???u ");
            edtPassword.clearComposingText();
            edtConfirmPassword.clearComposingText();

        } else {

            ClickRegister();

        }

    }

    private void ClickRegister() {
        loadingBar.setTitle("T???o t??i kho???n m???i");
        loadingBar.setMessage("Vui l??ng ?????i, trong khi ch??ng t??i ??ang t???o T??i kho???n m???i c???a b???n...");
        loadingBar.show();
        loadingBar.setCanceledOnTouchOutside(true);
        mAuth.createUserWithEmailAndPassword(strEmail,strPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    assert user != null;
                    if (user.isEmailVerified()) {
                        Toast.makeText(RegisterActivity.this, "B???n h??y ????ng nh???p ngay b??y gi???", Toast.LENGTH_SHORT).show();

                        SendUserToLoginActivity();
//                        startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
//                        overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);
                    }else {

                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(RegisterActivity.this, "Email verification link send", Toast.LENGTH_SHORT).show();
                                    showAlertDialog();
                                }
                            }
                        });
                    }
//                    uploadUser(user,name,email);


//                    Toast.makeText(RegisterActivity.this, "B???n ???? x??c th???c th??nh c??ng...", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                } else {
                    String message = task.getException().getMessage();
                    Toast.makeText(RegisterActivity.this, "Xu???t hi???n l???i: " + message, Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }
        });
    }
    private void showAlertDialog() {
        //thi???t l???p tr??nh t???o c???nh b??o
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
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

    private void changeStatusBarColor() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
    //            window.setStatusBarColor(Color.TRANSPARENT);
                window.setStatusBarColor(getResources().getColor(R.color.register_bk_color));
            }
    }

    public void onLoginClick(View view){
        startActivity(new Intent(this,LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);

    }
}