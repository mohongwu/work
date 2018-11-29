package com.smec.appmanager.module.mine;

import com.smec.appmanager.params.AmpRoleName;
import com.smec.appmanager.params.ModifyPassowordBean;

import java.util.ArrayList;

/**
 * Created by xupeizuo on 2018/6/26.
 */

public interface SmecMineContract {

    void changePassowrdlistener(ModifyPassowordBean modifyPassowordBean,Plistener plistener);

    interface Plistener{
        void success();
        void fail();
    }

    interface Codelistener{
        void fail();
        void success(ArrayList<AmpRoleName> list);
    }
}
