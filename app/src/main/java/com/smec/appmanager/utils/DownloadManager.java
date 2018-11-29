package com.smec.appmanager.utils;

import android.content.Intent;
import android.graphics.LinearGradient;
import android.net.Uri;
import android.util.Log;

import com.smec.appmanager.SmecApplication;
import com.smec.appmanager.beans.AmpApk;
import com.smec.appmanager.beans.DownloadInfo;
import com.smec.appmanager.manager.SmecRealm.RealmManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 下载管理器-单例
 */
public class DownloadManager {

	public static final int STATE_NONE = 0;// 下载未开始
	public static final int STATE_WAITING = 1;// 等待下载
	public static final int STATE_DOWNLOAD = 2;// 正在下载
	public static final int STATE_PAUSE = 3;// 下载暂停
	public static final int STATE_ERROR = 4;// 下载失败
	public static final int STATE_INSTALL = 5;// 下载成功
	public static final int STATE_SUCCESS = 6;// 安载成功


	// 所有观察者的集合
	//private ArrayList<DownloadObserver> mObservers = new ArrayList<DownloadObserver>();
	private ConcurrentHashMap<String, DownloadObserver> mObservers = new ConcurrentHashMap<String, DownloadObserver>();

	public ConcurrentHashMap<String, DownloadObserver> getObservers() {
		return mObservers;
	}

	// 下载对象的集合, ConcurrentHashMap是线程安全的HashMap
	private ConcurrentHashMap<String, DownloadInfo> mDownloadInfoMap = new ConcurrentHashMap<String, DownloadInfo>();
	// 下载任务集合, ConcurrentHashMap是线程安全的HashMap
	private ConcurrentHashMap<String, DownloadTask> mDownloadTaskMap = new ConcurrentHashMap<String, DownloadTask>();

	private static DownloadManager sInstance = new DownloadManager();

	private static OkHttpClient client = new OkHttpClient();

	private DownloadManager() {
	}

	public static DownloadManager getInstance() {
		return sInstance;
	}

	// 2. 注册观察者
	public synchronized void registerObserver(AmpApk appinfo,DownloadObserver observer) {
		if (observer != null) {
			//Log.i("mohongwu","observer: "+observer.hashCode());
			mObservers.put(appinfo.getPackageName(),observer);
		}
	}

	// 3. 注销观察者
	public synchronized void unregisterObserver(DownloadObserver observer) {
		if (observer != null && mObservers.contains(observer)) {
			mObservers.remove(observer);
		}
	}

	// 4. 通知下载状态变化
	public synchronized void notifyDownloadStateChanged(DownloadInfo info) {
		//for (DownloadObserver observer : mObservers) {
		DownloadObserver observer = mObservers.get(info.packageName);
		if (observer != null){
			observer.onDownloadStateChanged(info);
		}
		//}
	}

	// 5. 通知下载进度变化
	public synchronized void notifyDownloadProgressChanged(DownloadInfo info) {
		/*for (DownloadObserver observer : mObservers) {
			observer.onDownloadProgressChanged(info);
		}*/

		DownloadObserver observer = mObservers.get(info.packageName);
		if (observer != null){
			observer.onDownloadProgressChanged(info);
		}
	}

	/**
	 * 1. 定义下载观察者接口
	 */
	public interface DownloadObserver {
		// 下载状态发生变化
		public void onDownloadStateChanged(DownloadInfo info);

		// 下载进度发生变化
		public void onDownloadProgressChanged(DownloadInfo info);
	}

	/**
	 * 开始下载
	 */
	public synchronized void download(AmpApk appInfo) {
		if (appInfo != null) {
			DownloadInfo downloadInfo = mDownloadInfoMap.get(appInfo.getPackageName());

			// 如果downloadInfo不为空,表示之前下载过, 就无需创建新的对象, 要接着原来的下载位置继续下载,也就是断点续传
			if (downloadInfo == null) {// 如果为空,表示第一次下载, 需要创建下载对象, 从头开始下载
				downloadInfo = DownloadInfo.copy(appInfo);
				// 将下载对象保存在集合中
				mDownloadInfoMap.put(appInfo.getPackageName(), downloadInfo);
			}



			// 下载状态更为正在等待
			downloadInfo.currentState = STATE_WAITING;
			// 通知状态发生变化,各观察者根据此通知更新主界面
			notifyDownloadStateChanged(downloadInfo);

			// 初始化下载任务
			DownloadTask task = new DownloadTask(downloadInfo);
			// 启动下载任务
			ThreadManager.getThreadPool().execute(task);
			//ThreadPoolExecutorUtil.getSingleThreadExecutor().execute(task);
			// 将下载任务对象维护在集合当中
			mDownloadTaskMap.put(downloadInfo.packageName, task);
		}
	}

	/**
	 * 下载任务
	 */
	class DownloadTask implements Runnable {

		private DownloadInfo downloadInfo;
		//private HttpHelper.HttpResult httpResult;

		public DownloadTask(DownloadInfo downloadInfo) {
			this.downloadInfo = downloadInfo;
		}

		@Override
		public void run() {
			Request request;
			// 更新下载状态
			downloadInfo.currentState = STATE_DOWNLOAD;
			notifyDownloadStateChanged(downloadInfo);

			File file = new File(downloadInfo.path);

			if (!file.exists() || file.length() != downloadInfo.currentPos
					|| downloadInfo.currentPos == 0) {// 文件不存在, 或者文件长度和对象的长度不一致,
				// 或者对象当前下载位置是0
				file.delete();// 移除无效文件
				downloadInfo.currentPos = 0;// 文件当前位置归零
				request = new Request.Builder().get().url(downloadInfo.downloadUrl).build();
				//httpResult = HttpHelper.download(HttpHelper.URL
				//		+ "download?name=" + downloadInfo.downloadUrl);// 从头开始下载文件
			} else {
				// 需要断点续传
				request = new Request.Builder().get()
						.addHeader("RANGE","bytes=" + file.length()+"-" + downloadInfo.size)
						.url(downloadInfo.downloadUrl).build();
						//.url(downloadInfo.downloadUrl + "&range=" + file.length()).build();
				/*httpResult = HttpHelper.download(HttpHelper.URL
						+ "download?name=" + downloadInfo.downloadUrl
						+ "&range=" + file.length());*/
			}

			InputStream in = null;
			FileOutputStream out = null;
			try {
				Response response = client.newCall(request).execute();

				if (response != null && response.isSuccessful()) {
					in = response.body().byteStream();
					out = new FileOutputStream(file, true);//在源文件基础上追加
					if (downloadInfo.size == 0){
						downloadInfo.size = response.body().contentLength();
					}


					int len = 0;
					byte[] buffer = new byte[1024];
					long temp = 0;
					while ((len = in.read(buffer)) != -1
							&& downloadInfo.currentState == STATE_DOWNLOAD) {// 只有在下载的状态才读取文件,如果状态变化,就立即停止读写文件
						out.write(buffer, 0, len);
						out.flush();

						downloadInfo.currentPos += len;// 更新当前文件下载位置
						//Log.i("mohongwu","currentPos: "+downloadInfo.currentPos+"size: "+downloadInfo.size);
						if (downloadInfo.currentPos - temp > 50*1024){
							temp = downloadInfo.currentPos;
							notifyDownloadProgressChanged(downloadInfo);// 通知进度更新
						}else if (downloadInfo.currentPos == downloadInfo.size){
							notifyDownloadProgressChanged(downloadInfo);// 通知进度更新
						}

					}


					// 下载结束, 判断文件是否完整
					if (file.length() == downloadInfo.size) {
						// 下载完毕
						downloadInfo.currentState = STATE_INSTALL;
                        notifyDownloadStateChanged(downloadInfo);
						//pauseOtherWork(downloadInfo);
						RealmManager.getInstance().insertOrUpdate(downloadInfo);
					} else if (downloadInfo.currentState == STATE_PAUSE) {
						// 中途暂停
						notifyDownloadStateChanged(downloadInfo);
					} else {
						// 下载失败
						downloadInfo.currentState = STATE_ERROR;
						downloadInfo.currentPos = 0;
						notifyDownloadStateChanged(downloadInfo);
						// 删除无效文件
						file.delete();
					}

				} else {
					// 下载失败
					downloadInfo.currentState = STATE_ERROR;
					downloadInfo.currentPos = 0;
					notifyDownloadStateChanged(downloadInfo);
					// 删除无效文件
					file.delete();
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				IOUtils.close(in);
				IOUtils.close(out);
			}

			// 不管下载成功,失败还是暂停, 下载任务已经结束,都需要从当前任务集合中移除
			mDownloadTaskMap.remove(downloadInfo.packageName);
		}

	}

	/**
	 * 下载暂停
	 */
	public synchronized void pause(AmpApk appInfo) {
		if (appInfo != null) {
			DownloadInfo downloadInfo = mDownloadInfoMap.get(appInfo.getPackageName());
			if (downloadInfo != null) {
				int state = downloadInfo.currentState;
				// 如果当前状态是等待下载或者正在下载, 需要暂停当前任务
				if (state == STATE_WAITING || state == STATE_DOWNLOAD) {
					// 停止当前的下载任务
					DownloadTask downloadTask = mDownloadTaskMap
							.get(appInfo.getPackageName());
					if (downloadTask != null) {
						ThreadManager.getThreadPool().cancel(downloadTask);
					}

					// 更新下载状态为暂停
					downloadInfo.currentState = STATE_PAUSE;
					notifyDownloadStateChanged(downloadInfo);
				}
			}
		}
	}

	/**
	 * 下载暂停
	 */
	public synchronized void pauseOtherWork(DownloadInfo info) {

        for (ConcurrentHashMap.Entry<String, DownloadInfo> entry : mDownloadInfoMap.entrySet()) {
            //System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());

            DownloadInfo downloadInfo = entry.getValue();
            if (info.packageName.equals(downloadInfo.packageName)) {
                continue;
            }

            if (downloadInfo != null) {
                int state = downloadInfo.currentState;
                // 如果当前状态是等待下载或者正在下载, 需要暂停当前任务
                if (state == STATE_WAITING || state == STATE_DOWNLOAD) {
                    // 停止当前的下载任务
                    DownloadTask downloadTask = mDownloadTaskMap
                            .get(entry.getKey());
                    if (downloadTask != null) {
                        ThreadManager.getThreadPool().cancel(downloadTask);
                    }

                    // 更新下载状态为暂停
                    downloadInfo.currentState = STATE_PAUSE;
                    notifyDownloadStateChanged(downloadInfo);
                }
            }

        }
    }

	/**
	 * 安装apk
	 */
	public synchronized void install(AmpApk appInfo) {
		DownloadInfo downloadInfo = mDownloadInfoMap.get(appInfo.getPackageName());
		if (downloadInfo != null) {
			// 跳到系统的安装页面进行安装
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setDataAndType(Uri.parse("file://" + downloadInfo.path),
					"application/vnd.android.package-archive");
			//UIUtils.getContext().startActivity(intent);
			SmecApplication.getContext().startActivity(intent);
		}
	}

	/**
	 * 安装apk
	 */
	public synchronized void install(DownloadInfo appInfo) {
		DownloadInfo downloadInfo = mDownloadInfoMap.get(appInfo.packageName);
		if (downloadInfo != null) {
			// 跳到系统的安装页面进行安装
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setDataAndType(Uri.parse("file://" + downloadInfo.path),
					"application/vnd.android.package-archive");
			//UIUtils.getContext().startActivity(intent);
			SmecApplication.getContext().startActivity(intent);
		}
	}

	// 根据应用对象,获取对应的下载对象
	public DownloadInfo getDownloadInfo(AmpApk data) {
		if (data != null) {
			return mDownloadInfoMap.get(data.getPackageName());
		}

		return null;
	}

	// 根据应用对象,获取对应的下载对象
	public void deleteDownloadInfo(String key) {
		if (key != null) {
			mDownloadInfoMap.remove(key);
		}
	}

	public List<DownloadInfo> getAllDownloadInfo(){
		ArrayList<DownloadInfo> infos = null;
		if (mDownloadInfoMap != null){
			infos = new ArrayList<DownloadInfo>();
			for (ConcurrentHashMap.Entry<String, DownloadInfo> entry : mDownloadInfoMap.entrySet()) {
				infos.add(entry.getValue());
			}
		}
		return infos;
	}


	public void initDownInfo(List<DownloadInfo> downloadInfos) {
		mDownloadInfoMap.clear();
		if (downloadInfos != null) {
			for (DownloadInfo info : downloadInfos) {
				if (new File(info.path).exists()) {
					mDownloadInfoMap.put(info.packageName, info);
				}
			}
		}
	}


	public void deleteApkFileByName(String packageName){
		/*Iterator<Map.Entry<String, DownloadInfo>> iterator = mDownloadInfoMap.entrySet().iterator();
		while (iterator.hasNext()){
			Map.Entry<String, DownloadInfo> next = iterator.next();
			if (next.getValue().packageName.equals(packageName)){
				File file = new File(next.getValue().path);
				if (file.exists()){
					file.delete();
				}
				//mDownloadInfoMap.remove(entry.getKey());
				iterator.remove();
				break;
			}
		}*/

		for (Map.Entry<String, DownloadInfo> entry :mDownloadInfoMap.entrySet()){
			if (entry.getValue().packageName.equals(packageName)){
				File file = new File(entry.getValue().path);
				if (file.exists()){
					file.delete();
				}
				String key = entry.getKey();
				mDownloadInfoMap.remove(key);
				//Log.i("mohongwu",mDownloadInfoMap.get(key) == null ? "null": mDownloadInfoMap.get(key).path);
				break;
			}
		}
	}



}
