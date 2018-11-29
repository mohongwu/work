package com.smec.appmanager.beans;

import android.os.Environment;


import com.smec.appmanager.utils.DownloadManager;

import java.io.File;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * 下载对象封装
 */
public class DownloadInfo extends RealmObject {

	@PrimaryKey
	public String id;// apk唯一标识
	public long size;// apk大小
	public String downloadUrl;// 下载链接
	public String name;// apk名称
	public String packageName;//apk包名
	private String versionName;//apk 版本号
	public int currentState;// 当前下载状态
	public long currentPos;// 当前下载位置

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String path;// apk下载在本地的路径

	@Ignore
	public static final String APPMANAGER = "appmanager";// 文件夹名称
	@Ignore
	public static final String DOWNLOAD = "download";// 子文件夹名称

	// 获取当前下载进度
	public float getProgress() {
		if (size == 0) {
			return 0;
		}

		return (float) currentPos / size;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	/**
	 * 根据应用信息,拷贝出一个下载对象
	 */
	public static DownloadInfo copy(AmpApk appInfo) {
		DownloadInfo info = new DownloadInfo();
		info.id = String.valueOf(appInfo.getId());
		info.size = 0;

		info.downloadUrl = appInfo.getUrl();
		info.name = appInfo.getName();
		info.packageName = appInfo.getPackageName();
		info.versionName = appInfo.getVersionName();
		info.currentState = DownloadManager.STATE_NONE;
		info.currentPos = 0;
		info.path = getFilePath(info.name);

		return info;
	}

	/**
	 * 获取apk文件的本地下载路径
	 */
	private static String getFilePath(String name) {
		StringBuffer sb = new StringBuffer();
		String sdcardPath = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		sb.append(sdcardPath);
		sb.append(File.separator);
		sb.append(APPMANAGER);
		sb.append(File.separator);
		sb.append(DOWNLOAD);

		if (createDir(sb.toString())) {
			return sb.toString() + File.separator + name + ".apk";
		}

		return null;
	}


	// 创建文件夹
	private static boolean createDir(String dirPath) {
		File dirFile = new File(dirPath);
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			// 如果文件夹不存在,或者此文件不是一个文件夹,需要创建文件夹
			return dirFile.mkdirs();
		}

		return true;// 文件夹已经存在
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCurrentState() {
		return currentState;
	}

	public void setCurrentState(int currentState) {
		this.currentState = currentState;
	}

	public long getCurrentPos() {
		return currentPos;
	}

	public void setCurrentPos(long currentPos) {
		this.currentPos = currentPos;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
