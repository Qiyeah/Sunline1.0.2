package com.sunline.qi.utils;

import java.io.File;
import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;

public class BitmapUtils {

	/**
	 * 为图片加载的添加一个软引用缓存，每次图片从缓存中获取图片对象， 若缓存中不存在，才会从Sdcard加载图片，并将该对象加入缓存。
	 * 同时软引用的对象也有助于GC在内存不足的时候回收它们。
	 */
	private HashMap<String, SoftReference<Bitmap>> mImageCache;

	public Bitmap loadBitmapImage(String path) {
		mImageCache = new HashMap<String, SoftReference<Bitmap>>();

		if (mImageCache.containsKey(path)) {

			SoftReference<Bitmap> softReference = mImageCache.get(path);

			Bitmap bitmap = softReference.get();

			if (null != bitmap)

				return bitmap;

		}

		Bitmap bitmap = compressImage(path);

		mImageCache.put(path, new SoftReference<Bitmap>(bitmap));

		return bitmap;

	}

	public Drawable loadDrawableImage(String path) {

		return new BitmapDrawable(loadBitmapImage(path));

	}

	public Bitmap compressImage(String path) {
		BitmapFactory.Options options = new BitmapFactory.Options();

		options.inJustDecodeBounds = true;

		BitmapFactory.decodeFile(path, options);

		if (options.mCancel || options.outWidth == -1
				|| options.outHeight == -1) {

			// Log.d(“OomDemo”, “alert!!!” + String.valueOf(options.mCancel) + ”
			// ” + options.outWidth + options.outHeight);

			return null;

		}

		options.inSampleSize = calculateInSampleSize(options, 900, 600);

		// Log.d(“OomDemo”, “inSampleSize: ” + options.inSampleSize);

		options.inJustDecodeBounds = false;

		options.inDither = false;

		options.inPreferredConfig = Bitmap.Config.ARGB_8888;

		Bitmap bitmap = BitmapFactory.decodeFile(path, options);
		return bitmap;
	}

	/**
	 *
	 * @param id
	 * @return
	 */
	public Bitmap loadBitmapImage(Resources resources, int id) {
		mImageCache = new HashMap<String, SoftReference<Bitmap>>();

		if (mImageCache.containsKey(Integer.toString(id))) {

			SoftReference<Bitmap> softReference = mImageCache.get(Integer.toString(id));

			Bitmap bitmap = softReference.get();

			if (null != bitmap)

				return bitmap;

		}

		Bitmap bitmap = compressImage(resources, id);

		mImageCache.put(Integer.toString(id), new SoftReference<Bitmap>(bitmap));

		return bitmap;

	}

	/**
	 *
	 * @param id
	 * @return
	 */
	public Drawable loadDrawableImage(Resources resources,int id) {

		return new BitmapDrawable(loadBitmapImage(resources,id));

	}

	/**
	 *
	 * @param resources
	 * @param id
	 * @return
	 */
	public Bitmap compressImage(Resources resources, int id) {
		BitmapFactory.Options options = new BitmapFactory.Options();

		options.inJustDecodeBounds = true;

		BitmapFactory.decodeResource(resources,id, options);

		if (options.mCancel || options.outWidth == -1
				|| options.outHeight == -1) {

			// Log.d(“OomDemo”, “alert!!!” + String.valueOf(options.mCancel) + ”
			// ” + options.outWidth + options.outHeight);

			return null;

		}

		options.inSampleSize = calculateInSampleSize(options, 900, 600);

		// Log.d(“OomDemo”, “inSampleSize: ” + options.inSampleSize);

		options.inJustDecodeBounds = false;

		options.inDither = false;

		options.inPreferredConfig = Bitmap.Config.ARGB_8888;

		Bitmap bitmap = BitmapFactory.decodeResource(resources, id, options);

		return bitmap;
	}
	/**
	 * 实现对位图进行比例裁剪
	 *
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
											int reqWidth, int reqHeight) {

		// 解码时得到的位图大小
		final int imageHeight = options.outHeight;
		final int imageWidth = options.outWidth;

		// 如果inSampleSize = 1 ,表示图片按原始比例输出
		int inSampleSize = 1;

		if (imageHeight > reqWidth || imageWidth > reqHeight) {
			// 计算图片的压缩比例
			int heightRatio = Math.round((float) imageHeight / reqHeight);
			int widthRatio = Math.round((float) imageWidth / reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

		}

		return inSampleSize;
	}

	public void releaseImage(String path) {

		if (mImageCache.containsKey(path)) {

			SoftReference<Bitmap> reference = mImageCache.get(path);

			Bitmap bitmap = reference.get();

			if (null != bitmap) {

				// Log.d(“OomDemo”, “recyling ” + path);

				bitmap.recycle();

			}

			mImageCache.remove(path);

		}

	}

	/**
	 *  加载外部存储的路径
	 * @param relativeDir 文件的相对路径
	 * @return 查询文件数组
	 */
	public File[] loadExternalStorageFile(String relativeDir) {
		if (inExternalStorageWritable()) {
			File absoluteDir = new File(Environment.getExternalStorageDirectory(),relativeDir);
			if (absoluteDir.isDirectory()) {
				return absoluteDir.listFiles();
			}else {
				return new File[]{absoluteDir};
			}
		}
		return null;
	}

	/**
	 * 检查外部存储是否可读、可写
	 * @return
	 */
	public static boolean inExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}
}
