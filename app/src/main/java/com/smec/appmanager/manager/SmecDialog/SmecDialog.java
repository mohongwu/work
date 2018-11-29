package com.smec.appmanager.manager.SmecDialog;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;

import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;

/**
 * Created by xupeizuo on 2017/8/30.
 */

public final class SmecDialog {


    public static NormalDialog getInstance(Context mContext, String title){

        final NormalDialog normalDialog=new NormalDialog(mContext);
        normalDialog.isTitleShow(false)
                .bgColor(Color.parseColor("#383838"))
                .cornerRadius(5)
                .content(title)
                .contentGravity(Gravity.CENTER)//
                .contentTextColor(Color.parseColor("#ffffff"))
                .dividerColor(Color.parseColor("#222222"))
                .btnTextSize(15.5f, 15.5f)
                .btnTextColor(Color.parseColor("#ffffff"), Color.parseColor("#ffffff"))
                .btnPressColor(Color.parseColor("#2B2B2B"))
                .widthScale(0.85f)
                .dismiss();


        normalDialog.setOnBtnClickL(
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {

                    }
                },
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        normalDialog.dismiss();
                    }
                });

        return normalDialog;
    }



    public static NormalDialog getCheckUpdate(Context mContext, String title){

        NormalDialog normalDialog=new NormalDialog(mContext);
        normalDialog.isTitleShow(false)
                .bgColor(Color.parseColor("#383838"))
                .cornerRadius(5)
                .content(title)
                .contentGravity(Gravity.CENTER)//
                .contentTextColor(Color.parseColor("#ffffff"))
                .dividerColor(Color.parseColor("#222222"))
                .btnTextSize(15.5f, 15.5f)
                .btnTextColor(Color.parseColor("#ffffff"), Color.parseColor("#ffffff"))
                .btnPressColor(Color.parseColor("#2B2B2B"))
                .btnText("下次再说","立即更新")
                .widthScale(0.85f)
                .dismiss();

        normalDialog.setCanceledOnTouchOutside(false);

        return normalDialog;
    }

    public static NormalDialog getCheckUpdateNow(Context mContext, String title){

        NormalDialog normalDialog=new NormalDialog(mContext);


        normalDialog.isTitleShow(false)
                .bgColor(Color.parseColor("#383838"))
                .cornerRadius(5)
                .content(title)
                .contentGravity(Gravity.CENTER)//
                .contentTextColor(Color.parseColor("#ffffff"))
                .dividerColor(Color.parseColor("#222222"))
                .btnNum(1)
                .btnTextSize(15.5f, 15.5f)
                .btnTextColor(Color.parseColor("#ffffff"), Color.parseColor("#ffffff"))
                .btnPressColor(Color.parseColor("#2B2B2B"))
                .btnText("立即更新")
                .widthScale(0.85f)
                .dismiss();

        normalDialog.setCanceledOnTouchOutside(false);

        return normalDialog;
    }

}
