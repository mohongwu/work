package com.smec.appmanager.module.mine;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smec.appmanager.R;
import com.smec.appmanager.base.SmecBaseFragment;
import com.smec.appmanager.databinding.FragmentMineBinding;
import com.smec.appmanager.manager.SmecSession.SmecSession;

/**
 * Created by xupeizuo on 2018/6/26.
 */

public class SmecMineFragment extends SmecBaseFragment<SmecMinePresenter>{


    private FragmentMineBinding fragmentMineBinding;
    private SmecMineViewModel smecMineViewModel;

    @Override
    protected SmecMinePresenter getPersenter() {
        return new SmecMinePresenter(getContext());
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentMineBinding=DataBindingUtil.inflate(inflater, R.layout.fragment_mine,container,false);
        smecMineViewModel=new SmecMineViewModel(getActivity());
        smecMineViewModel.setSmecSession(SmecSession.initSmecSession());
        fragmentMineBinding.setViewModel(smecMineViewModel);
        return fragmentMineBinding.getRoot();
    }

    public static SmecMineFragment newInstance() {
        Bundle args = new Bundle();
        SmecMineFragment fragment = new SmecMineFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
