package com.gizwits.opensource.appkit.UserModule;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizWifiSDKListener;
import com.gizwits.opensource.appkit.CommonModule.GosBaseActivity;
import com.gizwits.opensource.appkit.R;

import wdy.business.listen.NoDoubleClickListener;
import wdy.business.util.CodeUtil;
import wdy.business.util.StatusBarUtil;

public class GosChangeUserPasswordActivity extends GosBaseActivity {

    private EditText oldpass;
    private EditText newpass;
    private EditText confrimpass;
    private AppCompatImageView imageView_back;
    private TextView textView_title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setActionBar(true, true, R.string.edit_password);

        setContentView(R.layout.activity_gos_change_password);
        StatusBarUtil.setStatusBarDark(getWindow(), false);
        initView();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        GizWifiSDK.sharedInstance().setListener(listener);
    }

    GizWifiSDKListener listener = new GizWifiSDKListener() {

        public void didChangeUserPassword(
                GizWifiErrorCode result) {

            if (result.getResult() == 0) {
                Toast.makeText(GosChangeUserPasswordActivity.this,
                        getResources().getString(R.string.passsuccess), toastTime)
                        .show();

                finish();

                spf.edit().putString("PassWord", newpass.getText().toString())
                        .commit();
            } else {

                if (result.getResult() == 9020) {
                    Toast.makeText(GosChangeUserPasswordActivity.this,
                            getResources().getString(R.string.oldpasserror),
                            toastTime).show();
                } else {
                    Toast.makeText(GosChangeUserPasswordActivity.this,
                            getResources().getString(R.string.passerror), toastTime)
                            .show();
                }

            }

        }

        ;
    };
    private CheckBox oldcheck;
    private CheckBox newcheck;
    private CheckBox concheck;

    private void initView() {
        oldpass = (EditText) findViewById(R.id.oldpass);
        newpass = (EditText) findViewById(R.id.newpass);
        confrimpass = (EditText) findViewById(R.id.confrimpass);
        confrimpass.setTransformationMethod(PasswordTransformationMethod
                .getInstance());
        oldpass.setTransformationMethod(PasswordTransformationMethod
                .getInstance());
        newpass.setTransformationMethod(PasswordTransformationMethod
                .getInstance());

        oldcheck = (CheckBox) findViewById(R.id.oldcheck);
        newcheck = (CheckBox) findViewById(R.id.newcheck);
        concheck = (CheckBox) findViewById(R.id.concheck);

        oldcheck.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                String psw = oldpass.getText().toString();

                if (isChecked) {
                    // oldpass.setInputType(0x90);
                    oldpass.setTransformationMethod(HideReturnsTransformationMethod
                            .getInstance());
                } else {
                    // oldpass.setInputType(0x81);
                    oldpass.setTransformationMethod(PasswordTransformationMethod
                            .getInstance());
                }
                oldpass.setSelection(psw.length());
            }
        });
        keyboard();
        newcheck.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                String psw = newpass.getText().toString();

                if (isChecked) {
                    // newpass.setInputType(0x90);
                    newpass.setTransformationMethod(HideReturnsTransformationMethod
                            .getInstance());
                } else {
                    // newpass.setInputType(0x81);
                    newpass.setTransformationMethod(PasswordTransformationMethod
                            .getInstance());
                }
                newpass.setSelection(psw.length());
            }
        });

        concheck.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                String psw = confrimpass.getText().toString();

                if (isChecked) {
                    // confrimpass.setInputType(0x90);
                    confrimpass
                            .setTransformationMethod(HideReturnsTransformationMethod
                                    .getInstance());
                } else {
                    // confrimpass.setInputType(0x81);
                    confrimpass
                            .setTransformationMethod(PasswordTransformationMethod
                                    .getInstance());
                }
                confrimpass.setSelection(psw.length());
            }
        });
        imageView_back = (AppCompatImageView) findViewById(R.id.imageView_back);
        imageView_back.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                onBackPressed();
            }
        });
        textView_title = (TextView) findViewById(R.id.textView_title);
        textView_title.setText("修改密码");
    }

    public void confirm(View v) {

        if (TextUtils.isEmpty(oldpass.getText().toString())
                || TextUtils.isEmpty(newpass.getText().toString())
                || TextUtils.isEmpty(confrimpass.getText().toString())) {

            if (TextUtils.isEmpty(oldpass.getText().toString())) {

                Toast.makeText(
                        this,
                        getResources().getString(
                                R.string.enter_current_password), toastTime).show();

                return;
            }

            if (TextUtils.isEmpty(newpass.getText().toString())) {
                Toast.makeText(this,
                        getResources().getString(R.string.enter_new_password),
                        toastTime).show();

                return;
            }

            if (TextUtils.isEmpty(confrimpass.getText().toString())) {
                Toast.makeText(
                        this,
                        getResources()
                                .getString(R.string.re_enter_new_password),
                        toastTime).show();
                return;
            }

        } else {
            String npass = newpass.getText().toString();
            String cpass = confrimpass.getText().toString();
            if (npass.equals(cpass)) {
                GizWifiSDK.sharedInstance().changeUserPassword(
                        spf.getString("Token", ""),
                        oldpass.getText().toString(), npass);
            } else {
                Toast.makeText(this,
                        getResources().getString(R.string.nosamepass), toastTime)
                        .show();
            }
        }
    }

    protected Boolean isShowKeyboard = false;
    LinearLayout main_layout;

    private void keyboard() {
        main_layout = findViewById(R.id.main_layout);
        main_layout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                main_layout.getWindowVisibleDisplayFrame(r);
                if (main_layout.getRootView().getHeight() - (r.bottom - r.top) > CodeUtil.dip2px(GosChangeUserPasswordActivity.this, 100)) {
                    isShowKeyboard = true;

                } else {
                    isShowKeyboard = false;

                }
            }
        });
    }

    /**
     * 关闭软键盘
     */
    protected void closeSoftKeyboard() {
        // 关闭软键盘
        if (isShowKeyboard) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        closeSoftKeyboard();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                finish();
                break;
        }
        return true;
    }
}
