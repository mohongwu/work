package com.smec.appmanager.module.main;

import android.graphics.Bitmap;

import com.smec.appmanager.params.AmpRoleName;
import com.smec.appmanager.dto.AllApkDto;

import java.util.ArrayList;

/**
 * Created by xupeizuo on 2018/6/26.
 */

public interface SmecHomePageContract {

    void getAllApks(Apkslistener apkslistener);

    interface Apkslistener{
        void failure();
        void success(ArrayList<AllApkDto> list);
    }

    interface Imagelistener{
        void failure();
        void success(Bitmap bitmap);
    }

    interface Codelistener{
        void fail();
        void success(ArrayList<AmpRoleName> list);
    }
}
