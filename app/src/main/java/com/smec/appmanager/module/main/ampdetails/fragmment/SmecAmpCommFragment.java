package com.smec.appmanager.module.main.ampdetails.fragmment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smec.appmanager.R;
import com.smec.appmanager.base.SmecBaseFragment;
import com.smec.appmanager.databinding.FragmentCommentBinding;
import com.smec.appmanager.module.main.ampdetails.SmecAmpDetailsActivity;
import com.smec.appmanager.module.main.ampdetails.SmecAmpDetailsPresenter;


/**
 * Created by xupeizuo on 2018/7/5.
 */

public class SmecAmpCommFragment extends SmecBaseFragment<SmecAmpDetailsPresenter>{

    private FragmentCommentBinding fragmentCommentBinding;

    @Override
    protected SmecAmpDetailsPresenter getPersenter() {
        return ((SmecAmpDetailsActivity)getActivity()).getPresenter();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentCommentBinding=DataBindingUtil.inflate(inflater, R.layout.fragment_comment,container,false);
        return fragmentCommentBinding.getRoot();
    }


    public static SmecAmpCommFragment newInstance() {
        Bundle args = new Bundle();
        SmecAmpCommFragment fragment = new SmecAmpCommFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
