package com.smec.appmanager.module.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.smec.appmanager.R;
import com.smec.appmanager.beans.AmpLabel;
import com.smec.appmanager.manager.SmecSession.SmecSession;
import com.smec.appmanager.utils.CommonUtils;

import java.util.ArrayList;

/**
 * Created by xupeizuo on 2018/7/5.
 */

public class SmecAmplabelAdapter extends BaseAdapter{

    private ArrayList<AmpLabel> labels=new ArrayList<>();
    private Context mContext;
    private boolean isUser=false;


    public SmecAmplabelAdapter(Context context,ArrayList<AmpLabel> labels,ArrayList<AmpLabel> u) {
        this.labels = labels;
        mContext=context;
        checkIsHave(u);
    }

    public SmecAmplabelAdapter(Context context,ArrayList<AmpLabel> labels,boolean is) {
        if(CommonUtils.notEmpty(labels)){
            this.labels = labels;
        }
        mContext=context;
        this.isUser=is;
    }

    @Override
    public int getCount() {
        return labels.size();
    }

    @Override
    public Object getItem(int position) {
        return labels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView= LayoutInflater.from(mContext).inflate(R.layout.item_labels,null);
        TextView label=(TextView)convertView.findViewById(R.id.smec_label_text);
        AmpLabel ampLabel=labels.get(position);
        label.setText(ampLabel.getName());
        if("T".equals(ampLabel.getIshave())||isUser){
            label.setTextColor(mContext.getResources().getColor(R.color.green));
        }else {
            label.setTextColor(mContext.getResources().getColor(R.color.text_red));
        }
        return convertView;
    }

    private void checkIsHave(ArrayList<AmpLabel> u){
        if(CommonUtils.notEmpty(labels)){
            if(CommonUtils.notEmpty(u)){
                for(int i=0;i<labels.size();i++){
                    AmpLabel ampLabel=labels.get(i);
                    for(int j=0;j<u.size();j++){
                        AmpLabel ua=u.get(j);
                        if(ampLabel.getName().equals(ua.getName())){
                            ampLabel.setIshave("T");
                        }
                    }
                }
            }
        }
    }
}
