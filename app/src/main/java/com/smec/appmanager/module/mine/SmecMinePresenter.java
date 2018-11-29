package com.smec.appmanager.module.mine;

import android.content.Context;

import com.smec.appmanager.params.AmpRole;
import com.smec.appmanager.params.AmpRoleDto;
import com.smec.appmanager.params.AmpRoleName;
import com.smec.appmanager.params.ModifyPassowordBean;
import com.smec.appmanager.base.SmecBasePresenter;
import com.smec.appmanager.manager.SmecRetrofit.BaseSubscriber;
import com.smec.appmanager.manager.SmecRetrofit.HttpResult;
import com.smec.appmanager.manager.SmecSession.SmecSession;

import java.util.ArrayList;

import rx.Observable;

/**
 * Created by xupeizuo on 2018/6/26.
 */

public class SmecMinePresenter extends SmecBasePresenter implements SmecMineContract{

    public SmecMinePresenter(Context mContext) {
        super(mContext);
    }


    @Override
    public void changePassowrdlistener(ModifyPassowordBean modifyPassowordBean, final Plistener plistener) {
        modifyPassowordBean.setUsername(SmecSession.getSmecUser().getUserName());
        Observable<HttpResult> observable=smecAppManagerService.changePassword(modifyPassowordBean);
        schedulerThread(observable).subscribe(new BaseSubscriber<HttpResult>() {

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                plistener.fail();
            }

            @Override
            public void onNext(HttpResult httpResult) {
                plistener.success();
            }
        });
    }


    /**
     * 通过角色code获取角色名称
     * @param codelistener
     */
    public void getNamgeByCode(final Codelistener codelistener){
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
