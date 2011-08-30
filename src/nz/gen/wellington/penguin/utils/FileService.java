package nz.gen.wellington.penguin.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Date;

import android.content.Context;
import android.util.Log;

public class FileService {

	private static final String TAG = "FileService";

	public static boolean existsLocally(Context context, String filename) {
		File localFile = new File(getCacheDir(context), filename);
		boolean result = localFile.exists() && localFile.canRead();
		Log.i(TAG, "Checking for local cache file at '" + localFile.getAbsolutePath() + "': " + result);
		return result;
	}

	public static FileInputStream getFileInputStream(Context context, String filename) throws FileNotFoundException {
		File file = new File(getCacheDir(context) + "/" + filename);
		Log.i(TAG, "Opening input stream to: " + file.getAbsolutePath());
		return new FileInputStream(file);
	}
	

	public static FileOutputStream getFileOutputStream(Context context, String filename) throws FileNotFoundException {
		Log.i(TAG, "Opening output stream to: " + filename);
		File file = new File(getCacheDir(context) + "/" + filename);
		return new FileOutputStream(file);
	}

	public static Date getModificationTime(Context context, String filename) {
		Log.i(TAG, "Checking mod time for file at: " + filename);
		File localFile = new File(getCacheDir(context), filename);
		if (localFile.exists()) {
			return new Date(localFile.lastModified());
		}
		return null;
	}
	
	private static File getCacheDir(Context context) {
		return context.getCacheDir();
	}
	
}
