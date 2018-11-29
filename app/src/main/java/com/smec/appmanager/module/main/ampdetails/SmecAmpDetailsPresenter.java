package com.smec.appmanager.module.main.ampdetails;

import android.content.Context;

import com.smec.appmanager.params.AmpRole;
import com.smec.appmanager.params.AmpRoleDto;
import com.smec.appmanager.params.AmpRoleName;
import com.smec.appmanager.base.SmecBasePresenter;
import com.smec.appmanager.manager.SmecRetrofit.BaseSubscriber;
import com.smec.appmanager.manager.SmecRetrofit.HttpResult;
import com.smec.appmanager.manager.SmecSession.SmecSession;
import com.smec.appmanager.module.mine.SmecMineContract;

import java.util.ArrayList;

import rx.Observable;

/**
 * Created by xupeizuo on 2018/7/4.
 */

public class SmecAmpDetailsPresenter extends SmecBasePresenter{

    public SmecAmpDetailsPresenter(Context mContext) {
        super(mContext);
    }

    /**
     * 通过角色code获取角色名称
     * @param codelistener
     */
    public void getNamgeByCode(final SmecMineContract.Codelistener codelistener){
        if(SmecSession.getSmecUser().getUserRole()!=null) {
            ArrayList<AmpRole> ampRoles=new ArrayList<>();
            AmpRoleDto ampRoleDto=new AmpRoleDto();
            String[] roles = SmecSession.getSmecUser().getUserRole().split(",");
            for(int i=0;i<roles.length;i++){
                ampRoles.add(new AmpRole(roles[i]));
            }
            ampRoleDto.setCodeList(ampRoles);
            Observable<HttpResult<ArrayList<AmpRoleName>>> observable=smecAppManagerService.getRoleNameByCode(ampRoleDto);
            schedulerThread(observable).subscribe(new BaseSubscriber<HttpResult<ArrayList<AmpRoleName>>>() {

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    codelistener.fail();
                }

                @Override
                public void onNext(HttpResult<ArrayList<AmpRoleName>> ampRoleNameHttpResult) {
                    codelistener.success(ampRoleNameHttpResult.getData());
                }
            });
        }
    }

}
