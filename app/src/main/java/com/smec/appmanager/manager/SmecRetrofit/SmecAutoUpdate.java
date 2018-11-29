package com.smec.appmanager.manager.SmecRetrofit;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import com.smec.appmanager.widget.ProgressButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by xupeizuo on 2018/3/26.
 */

public class SmecAutoUpdate {

    private static final String TAG=SmecAutoUpdate.class.getSimpleName();
    public static final int REQUEST_INSTALL_CODE=1001;

    private static SmecAutoUpdate smecAutoUpdate;
    private static OkHttpClient client;
    private static Request request;
    private static Byte[] syncObj=new Byte[0];

    /**
     * 初始化下载客户端
     * @param url
     * @return
     */
    public static SmecAutoUpdate initAutoUpdate(String url){
        if(smecAutoUpdate == null){
            synchronized (syncObj){
                if(smecAutoUpdate == null){
                    client=new OkHttpClient();
                    smecAutoUpdate=new SmecAutoUpdate();
                }
            }
        }
        request = new Request.Builder().get().url(url).build();
        return smecAutoUpdate;
    }

    /**
     * 开始下载
     */
    public static File startDownload(ProgressButton progressButton){

        InputStream is =null;
        FileOutputStream fos =null;
        File apkFile=null;
        try {

            Response response = client.newCall(request).execute();
            if(response.isSuccessful()){
                Log.e(TAG,"开始下载apk");
                //获取内容总长度
                long contentLength = response.body().contentLength();
                if(progressButton!=null){
                    progressButton.setMax((int)contentLength/1024);
                }

                //保存到sd卡
                apkFile = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".apk");
                fos = new FileOutputStream(apkFile);
                //获得输入流
                is = response.body().byteStream();
                //定义缓冲区大小
                byte[] bys = new byte[1024];
                int progress = 0;
                int len = -1;
                while ((len = is.read(bys)) != -1) {
                    try {
                        Thread.sleep(1);
                        fos.write(bys, 0, len);
                        fos.flush();
                        progress += len;
                        //设置进度
                        progressButton.setProgress(progress/1024);
                    } catch (InterruptedException e) {
                       e.printStackTrace();
                    }
                }
                return apkFile;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //关闭io流
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                is = null;
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                fos = null;
            }
        }
//        if(dialog !=null && dialog.isShowing()){
//            dialog.dismiss();
//        }
        return apkFile;
    }

    /**
     * 下载完成,提示用户安装
     */
    public static void installApk(Activity activity,File file) {
        //调用系统安装程序
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        activity.startActivityForResult(intent,REQUEST_INSTALL_CODE);
    }

    /**
     * 销毁该单例
     */
    public static void updateDistroy(){
        smecAutoUpdate=null;
        System.gc();
    }
}
