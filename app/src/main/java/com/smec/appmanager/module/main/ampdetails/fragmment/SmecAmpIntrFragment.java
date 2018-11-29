package com.smec.appmanager.module.main.ampdetails.fragmment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smec.appmanager.R;
import com.smec.appmanager.SmecApplication;
import com.smec.appmanager.base.SmecBaseFragment;
import com.smec.appmanager.databinding.FragmentIntroduceBinding;
import com.smec.appmanager.dto.AllApkDto;
import com.smec.appmanager.module.main.ampdetails.SmecAmpDetailsActivity;
import com.smec.appmanager.module.main.ampdetails.SmecAmpDetailsPresenter;
import com.smec.appmanager.module.main.adapter.SmecAmplabelAdapter;
import com.smec.appmanager.widget.ProgressButton;

import static com.smec.appmanager.api.SmecAmpConstant._appOperator;
import static com.smec.appmanager.api.SmecAmpConstant.checkAppStatus;

/**
 * Created by xupeizuo on 2018/7/5.
 */

public class SmecAmpIntrFragment extends SmecBaseFragment<SmecAmpDetailsPresenter>{

    private FragmentIntroduceBinding fragmentIntroduceBinding;
    private SmecAmplabelAdapter smecAmplabelAdapter;
    private AllApkDto allApkDto;

    @Override
    protected SmecAmpDetailsPresenter getPersenter() {
        return ((SmecAmpDetailsActivity)getActivity()).getPresenter();
    }

    public void getlabels(){
        allApkDto=((SmecAmpDetailsActivity)getActivity()).getApkMsg();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getlabels();

        fragmentIntroduceBinding=DataBindingUtil.inflate(inflater, R.layout.fragment_introduce,container,false);
        _initView();
        return fragmentIntroduceBinding.getRoot();
    }

    private void _initView(){
//        fragmentIntroduceBinding.smecLabel.setAdapter(smecAmplabelAdapter);
//        if(allApkDto.getAmpApk().getDescription()!=null){
//            fragmentIntroduceBinding.smecContent.setText(allApkDto.getAmpApk().getDescription());
//        }
//        SmecApplication.initScanInstalledApp();
//        checkAppStatus(getActivity(),allApkDto,fragmentIntroduceBinding.smecOperator);
//        fragmentIntroduceBinding.smecOperator.setOnProgressButtonClickListener(new ProgressButton.OnProgressButtonClickListener() {
//            @Override
//            public void onClickListener() {
//                _appOperator(getActivity(),allApkDto,fragmentIntroduceBinding.smecOperator,null);
//            }
//        });
    }

    @Override
    public void onResume() {
        super.onResume();
//        SmecApplication.initScanInstalledApp();
//        fragmentIntroduceBinding.getRoot().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                checkAppStatus(getActivity(),allApkDto,fragmentIntroduceBinding.smecOperator);
//            }
//        },200);
    }

    public static SmecAmpIntrFragment newInstance() {
        Bundle args = new Bundle();
        SmecAmpIntrFragment fragment = new SmecAmpIntrFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
