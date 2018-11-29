package com.smec.appmanager.manager.SmecRetrofit;


/**
 * Created by xupeizuo on 2017/6/13.
 */

public class NetWorkCodeHelper {



    interface ApiCode {

        String SUCCESS="ok";
        int FAILURE=10002;
    }


    interface HttpErrorCode {

        int UNAUTHORIZED = 401;
        int FORBIDDEN = 403;
        int NOT_FOUND = 404;
        int REQUEST_TIMEOUT = 408;
        int INTERNAL_SERVER_ERROR = 500;
        int BAD_GATEWAY = 502;
        int SERVICE_UNAVAILABLE = 503;
        int GATEWAY_TIMEOUT = 504;

    }

}
