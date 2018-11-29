package com.smec.appmanager.module.main;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.footer.LoadingView;
import com.smec.appmanager.R;
import com.smec.appmanager.SmecApplication;
import com.smec.appmanager.api.SmecAmpConstant;
import com.smec.appmanager.base.SmecBaseFragment;
import com.smec.appmanager.databinding.FragmentHomeBinding;
import com.smec.appmanager.dto.AllApkDto;
import com.smec.appmanager.module.main.adapter.HomePageAdapter;
import com.smec.appmanager.utils.CommonUtils;
import com.smec.appmanager.utils.KmpUtil;

import java.util.ArrayList;

/**
 * Created by xupeizuo on 2018/6/26.
 */

public class SmecHomePageFragment extends SmecBaseFragment<SmecHomePagePresenter> implements SmecHomePageContract{

    private FragmentHomeBinding fragmentHomeBinding;
    private SmecHomePageViewModel smecHomePageViewModel;
    private HomePageAdapter homePageAdapter;
    private ArrayList<AllApkDto> allApkDtos=new ArrayList<>();

    @Override
    protected SmecHomePagePresenter getPersenter() {
        return new SmecHomePagePresenter(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentHomeBinding=DataBindingUtil.inflate(inflater, R.layout.fragment_home,container,false);
        smecHomePageViewModel=new SmecHomePageViewModel();
        fragmentHomeBinding.setViewModel(smecHomePageViewModel);
        _initView();
        return fragmentHomeBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        SmecApplication.initScanInstalledApp();
        fragmentHomeBinding.getRoot().postDelayed(new Runnable() {
            @Override
            public void run() {
                homePageAdapter.notifyDataSetChanged();
            }
        },200);
    }

    private void _initView(){
        homePageAdapter=new HomePageAdapter(getActivity());
        fragmentHomeBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        fragmentHomeBinding.recyclerView.setAdapter(homePageAdapter);
        fragmentHomeBinding.refreshLayout.setHeaderView(SmecAmpConstant.getSinaRefreshView());
        fragmentHomeBinding.refreshLayout.setBottomView(new LoadingView(getContext()));
        fragmentHomeBinding.refreshLayout.setAutoLoadMore(true);
        fragmentHomeBinding.refreshLayout.setEnableOverScroll(false);
        fragmentHomeBinding.refreshLayout.setEnableLoadmore(false);
        fragmentHomeBinding.refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                SmecApplication.initScanInstalledApp();
                getAllApks(new Apkslistener() {
                    @Override
                    public void failure() {
                        fragmentHomeBinding.refreshLayout.finishRefreshing();
                    }

                    @Override
                    public void success(ArrayList<AllApkDto> list) {
                        allApkDtos=list;
                        homePageAdapter.setList(list);
                        fragmentHomeBinding.refreshLayout.finishRefreshing();
                    }
                });
            }
        });
        fragmentHomeBinding.getRoot().postDelayed(new Runnable() {
            @Override
            public void run() {
                fragmentHomeBinding.refreshLayout.startRefresh();
            }
        },700);
        fragmentHomeBinding.smecApkSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s!=null && !s.toString().trim().equals("")){
                    fragmentHomeBinding.ivClear.setVisibility(View.VISIBLE);
                }else {
                    fragmentHomeBinding.ivClear.setVisibility(View.INVISIBLE);
                }
            }
        });


        fragmentHomeBinding.smecApkSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER){
                    CommonUtils.hideImmManager(fragmentHomeBinding.getRoot());
                    homePageAdapter.setList(apkSerach(fragmentHomeBinding.smecApkSearch.getText().toString()));
                }
                return false;
            }
        });

        fragmentHomeBinding.ivClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentHomeBinding.smecApkSearch.setText("");
                //CommonUtils.hideImmManager(fragmentHomeBinding.getRoot());
                homePageAdapter.setList(apkSerach(fragmentHomeBinding.smecApkSearch.getText().toString()));
            }
        });
        fragmentHomeBinding.smecIconSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.hideImmManager(fragmentHomeBinding.getRoot());
                homePageAdapter.setList(allApkDtos);
            }
        });
    }

    @Override
    public void getAllApks(Apkslistener apkslistener) {
        mPresenter.getAllApks(apkslistener);
    }

    /**
     * 内容检索
     * @param keyWord
     */
    private ArrayList<AllApkDto> apkSerach(String keyWord){
        ArrayList<AllApkDto> list1=new ArrayList<>();
        if(keyWord ==null || keyWord.trim().equals("")){
            return allApkDtos;
        }
        if(CommonUtils.notEmpty(allApkDtos)){
            for(int i=0;i<allApkDtos.size();i++){
                AllApkDto allApkDto=allApkDtos.get(i);
                if(KmpUtil.Kmp(allApkDto.getAmpApk().getName(),keyWord)){
                    list1.add(allApkDto);
                }
            }
            return list1;
        }else {
            return list1;
        }
    }

    public static SmecHomePageFragment newInstance() {
        Bundle args = new Bundle();
        SmecHomePageFragment fragment = new SmecHomePageFragment();
        fragment.setArguments(args);
        return fragment;
    }

}
