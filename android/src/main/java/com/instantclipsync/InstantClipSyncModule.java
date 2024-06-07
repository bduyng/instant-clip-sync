package com.instantclipsync;

import androidx.annotation.NonNull;
import android.content.Context;
import com.google.android.gms.instantapps.InstantApps;
import android.util.Log;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.module.annotations.ReactModule;

import java.util.Map;
import java.util.HashMap;

@ReactModule(name = InstantClipSyncModule.NAME)
public class InstantClipSyncModule extends ReactContextBaseJavaModule {
  public static final String NAME = "InstantClipSync";
  public static final String TAG = "InstantClipSync";

  public InstantClipSyncModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  @NonNull
  public String getName() {
    return NAME;
  }


  @ReactMethod
  public void storeStringInInstantAppCookie(String data, Promise promise) {
    ReactApplicationContext context = getReactApplicationContext();
    byte[] bytes = data.getBytes();
    // Get the maximum cookie size
    int maxCookieSize = InstantApps.getPackageManagerCompat(context).getInstantAppCookieMaxSize();
    // Ensure that cookie is not over the limit
    if (bytes.length > maxCookieSize) {
      Log.w(TAG, "Attempt to set a cookie that is too large.");
      promise.reject("Failed to store Instant App cookie", "Attempt to set a cookie that is too large.");
      return;
    }

    // PackageManager can throw exceptions, so it's important to handle them
    try {
      boolean isStored = InstantApps.getPackageManagerCompat(context).setInstantAppCookie(bytes);

      // Print out the result
      if (isStored) {
        // Log.i(TAG, "Successfully stored cookie.");
        promise.resolve(true);
      } else {
        // Log.i(TAG, "Failed to store cookie.");
        promise.reject("Failed to store Instant App cookie", "");
      }
    } catch (Exception e) {
      // Log.e(TAG, "Failed to set Instant App cookie", e);
      promise.reject("Failed to set Instant App cookie", e);
    }
  }

  @ReactMethod
  public void retrieveStringInInstantAppCookie(Promise promise) {
    String retrievedData = "";
    ReactApplicationContext context = getReactApplicationContext();
    
    try {
      byte[] bytes = InstantApps.getPackageManagerCompat(context).getInstantAppCookie();

      if (bytes == null) {
        promise.resolve(retrievedData);
        return
      }
      // Convert the bytes back to string
      retrievedData = new String(bytes);
      Log.i(TAG, "Instant App cookie bytes.length " + bytes.length + " " + retrievedData);
    } catch (Exception e) {
      Log.e(TAG, "Failed to get Instant App cookie", e);
      promise.reject("Failed to get Instant App cookie", e);
    }

    promise.resolve(retrievedData);
  }
}
