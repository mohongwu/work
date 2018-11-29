package com.smec.appmanager.dialog;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.smec.appmanager.R;
import com.smec.appmanager.databinding.DialogCheckPasswordBinding;

/**
 * Created by xupeizuo on 2018/7/4.
 */

public class CheckPassword extends Dialog{

    public interface Passwordlistener{
        void cancle(Dialog dialog);
        void makeSure(String password,Dialog dialog);
    }

    private DialogCheckPasswordBinding dialogCheckPasswordBinding;
    private Passwordlistener listener;

    public CheckPassword(Context context, int themeResId,Passwordlistener passwordlistener) {
        super(context, themeResId);
        this.listener=passwordlistener;
        dialogCheckPasswordBinding= DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_check_password,null,false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(dialogCheckPasswordBinding.getRoot());
        _initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        dialogCheckPasswordBinding.setPassword("");
    }

    private void _initView(){
        //dialogCheckPasswordBinding.cancle.setOnClickListener(v -> listener.cancle(CheckPassword.this));
        dialogCheckPasswordBinding.cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.cancle(CheckPassword.this);
            }
        });
        dialogCheckPasswordBinding.makeSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.makeSure(dialogCheckPasswordBinding.getPassword(),CheckPassword.this);
            }
        });
    }
}
