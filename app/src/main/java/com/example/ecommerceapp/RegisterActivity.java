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
            Toast.makeText(RegisterActivity.this, "Vui lòng nhập email của bạn", Toast.LENGTH_LONG).show();
            edtEmail.setError("Bắt buộc nhập email");
            utilService.showSnackBar(v,"Vui lòng nhập email của bạn");
            edtEmail.requestFocus();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()) { // khác true
            Toast.makeText(RegisterActivity.this, "Vui lòng nhập lại email của bạn", Toast.LENGTH_LONG).show();
            edtEmail.setError(" email không hợp lệ");
            utilService.showSnackBar(v,"Vui lòng nhập email của bạn");
            edtEmail.requestFocus();

        }      else if (TextUtils.isEmpty(strPassword)) {
            Toast.makeText(RegisterActivity.this, "Vui lòng nhập mật khẩu", Toast.LENGTH_SHORT).show();
            edtPassword.setError("Bắt buộc phải nhập mật khẩu");
            utilService.showSnackBar(v,"Vui lòng nhập mật khẩu");
            edtPassword.requestFocus();
        } else if (strPassword.length() < 6) {
            Toast.makeText(RegisterActivity.this, "Mật khẩu phải có ít nhất 6 kí tự ", Toast.LENGTH_LONG).show();
            edtPassword.setError("Mật khẩu quá yếu");
            utilService.showSnackBar(v,"Mật khẩu phải có ít nhất 6 kí tự");
            edtPassword.requestFocus();
        } else if (TextUtils.isEmpty(strConfirmPassword)) {
            Toast.makeText(RegisterActivity.this, "Vui lòng nhập lại mật khẩu ", Toast.LENGTH_LONG).show();
            edtConfirmPassword.setError("Bắt buộc phải nhập mật khẩu xác nhận");
            utilService.showSnackBar(v,"Vui lòng nhập lại mật khẩu");
            edtConfirmPassword.requestFocus();
        } else if (!strPassword.equals(strConfirmPassword)) {
            Toast.makeText(RegisterActivity.this, "Vui lòng nhập cùng một mật khẩu ", Toast.LENGTH_LONG).show();
            edtConfirmPassword.setError("Bắt buộc phải nhập mật khẩu xác nhận");
            utilService.showSnackBar(v,"Vui lòng nhập cùng một mật khẩu ");
            edtPassword.clearComposingText();
            edtConfirmPassword.clearComposingText();

        } else {

            ClickRegister();

        }

    }

    private void ClickRegister() {
        loadingBar.setTitle("Tạo tài khoản mới");
        loadingBar.setMessage("Vui lòng đợi, trong khi chúng tôi đang tạo Tài khoản mới của bạn...");
        loadingBar.show();
        loadingBar.setCanceledOnTouchOutside(true);
        mAuth.createUserWithEmailAndPassword(strEmail,strPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    assert user != null;
                    if (user.isEmailVerified()) {
                        Toast.makeText(RegisterActivity.this, "Bạn hãy đăng nhập ngay bây giờ", Toast.LENGTH_SHORT).show();

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


//                    Toast.makeText(RegisterActivity.this, "Bạn đã xác thực thành công...", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                } else {
                    String message = task.getException().getMessage();
                    Toast.makeText(RegisterActivity.this, "Xuất hiện lỗi: " + message, Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }
        });
    }
    private void showAlertDialog() {
        //thiết lập trình tạo cảnh báo
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setTitle("Email chưa được xác minh");
        builder.setMessage("Vui lòng xác minh email của bạn ngay bây giờ. Bạn không thể đăng nhập mà không xác minh email. ");


        builder.setPositiveButton("Tiếp tục", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                //Gửi ứng dụng email trong cửa sổ mới và không phải trong ứng dụng của chúng tôi
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