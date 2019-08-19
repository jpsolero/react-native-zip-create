package com.reactlibrary;

import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class ZipCreateModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;
    private static final String TAG = ZipCreateModule.class.getSimpleName();

    private static final int BUFFER_SIZE = 4096;
    private static final String PROGRESS_EVENT_NAME = "zipCreateProgress";
    private static final String EVENT_KEY_FILENAME = "filePath";
    private static final String EVENT_KEY_PROGRESS = "progress";

    public ZipCreateModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "ZipCreate";
    }

    @ReactMethod
    public void zip(ReadableArray paths, String destDirectory, Promise promise) {
      List<String> filePaths = new ArrayList<>();

      String fromDirectory;
      String fileOrDirectory = "";
      try {
        for (int i = 0; i < paths.size(); i++) {
            fileOrDirectory = paths.getString(i);
            File tmp = new File(fileOrDirectory);
            if (tmp.exists()) {
                if (tmp.isDirectory()) {
                    fromDirectory = fileOrDirectory;
                    List<File> files = getSubFiles(tmp, true);
                    for (int j = 0; j < files.size(); j++) {
                        filePaths.add(files.get(j).getAbsolutePath());
                    }
                } else {
                    filePaths.add(fileOrDirectory);
                }
            } else {
                throw new FileNotFoundException(fileOrDirectory);
            }
        }
      } catch (FileNotFoundException | NullPointerException e) {
        promise.reject(null, "Couldn't open file/directory " + fileOrDirectory + ".");
        return;
      }

      try {
        String[] filePathArray = filePaths.toArray(new String[filePaths.size()]);
        new com.reactlibrary.ZipTask(filePathArray, destDirectory, promise, this).zip();
      } catch (Exception ex) {
        promise.reject(null, ex.getMessage());
        return;
      }
    }

    private List<File> getSubFiles(File baseDir, boolean isContainFolder) {
      List<File> fileList = new ArrayList<>();
      File[] tmpList = baseDir.listFiles();
      for (File file : tmpList) {
        if (file.isFile()) {
          fileList.add(file);
        }
        if (file.isDirectory()) {
          if (isContainFolder) {
            fileList.add(file); //key code
          }
          fileList.addAll(getSubFiles(file, isContainFolder));
        }
      }
      return fileList;
    }

    protected void updateProgress(long extractedBytes, long totalSize, String zipFilePath) {
      // Ensure progress can't overflow 1
      double progress = Math.min((double) extractedBytes / (double) totalSize, 1);
      Log.d(TAG, String.format("updateProgress: %.0f%%", progress * 100));

      WritableMap map = Arguments.createMap();
      map.putString(EVENT_KEY_FILENAME, zipFilePath);
      map.putDouble(EVENT_KEY_PROGRESS, progress);
      getReactApplicationContext().getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
              .emit(PROGRESS_EVENT_NAME, map);
    }
}
