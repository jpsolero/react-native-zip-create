package com.reactlibrary;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

public class ZipCreateModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;

    public ZipCreateModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "ZipCreate";
    }

    @ReactMethod
    public void zip(String[] fileOrDirectory, String destDirectory, Promise promise) {
      List<String> filePaths = new ArrayList<>();

      String fromDirectory;
      try {
        File tmp = new File(fileOrDirectory);
        if (tmp.exists()) {
          if (tmp.isDirectory()) {
            fromDirectory = fileOrDirectory;
            List<File> files = getSubFiles(tmp, true);
            for (int i = 0; i < files.size(); i++) {
              filePaths.add(files.get(i).getAbsolutePath());
            }
          } else {
            fromDirectory = fileOrDirectory.substring(0, fileOrDirectory.lastIndexOf("/"));
            filePaths.add(fileOrDirectory);
          }
        } else {
          throw new FileNotFoundException(fileOrDirectory);
        }
      } catch (FileNotFoundException | NullPointerException e) {
        promise.reject(null, "Couldn't open file/directory " + fileOrDirectory + ".");
        return;
      }

      try {
        String[] filePathArray = filePaths.toArray(new String[filePaths.size()]);
        new ZipTask(filePathArray, destDirectory, fromDirectory, promise, this).zip();
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
