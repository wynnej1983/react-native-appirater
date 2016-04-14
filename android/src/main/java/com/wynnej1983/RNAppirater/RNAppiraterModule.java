package com.wynnej1983.RNAppirater;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.Callback;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RNAppiraterModule extends ReactContextBaseJavaModule {
  private static final String PREF_LAUNCH_COUNT = "launch_count";
  private static final String PREF_EVENT_COUNT = "event_count";
  private static final String PREF_RATE_CLICKED = "rateclicked";
  private static final String PREF_DONT_SHOW = "dontshow";
  private static final String PREF_DATE_REMINDER_PRESSED = "date_reminder_pressed";
  private static final String PREF_DATE_FIRST_LAUNCHED = "date_firstlaunch";
  private static final String PREF_APP_VERSION_CODE = "versioncode";
  private static final String PREF_APP_LOVE_CLICKED= "loveclicked";

  private static ReactApplicationContext reactContext;

  public RNAppiraterModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "RNAppirater";
  }

  @ReactMethod
  public static void setAppId(String val) {
    return;
  }

  @ReactMethod
  public static void setDaysUntilPrompt(String val) {
    return;
  }

  @ReactMethod
  public static void setUsesUntilPrompt(String val) {
    return;
  }

  @ReactMethod
  public static void setSignificantEventsUntilPrompt(String val) {
    return;
  }

  @ReactMethod
  public static void setTimeBeforeReminding(String val) {
    return;
  }

  @ReactMethod
  public static void setDebug(String val) {
    return;
  }

  @ReactMethod
  public static void appEnteredForeground() {
    return;
  }

  @ReactMethod
  public static void appLaunched() {
    boolean testMode = reactContext.getResources().getBoolean(R.bool.appirator_test_mode);
      SharedPreferences prefs = reactContext.getSharedPreferences(reactContext.getPackageName()+".appirater", 0);
      if(!testMode && (prefs.getBoolean(PREF_DONT_SHOW, false) || prefs.getBoolean(PREF_RATE_CLICKED, false))) {return;}
      
      SharedPreferences.Editor editor = prefs.edit();
      
      if(testMode){
          if (prefs.getBoolean(PREF_APP_LOVE_CLICKED, false)) {
              showRateDialog(reactContext,editor);
          }
        return;
      }
      
      // Increment launch counter
      long launch_count = prefs.getLong(PREF_LAUNCH_COUNT, 0);

      // Get events counter
      long event_count = prefs.getLong(PREF_EVENT_COUNT, 0);

      // Get date of first launch
      long date_firstLaunch = prefs.getLong(PREF_DATE_FIRST_LAUNCHED, 0);
      
  // Get reminder date pressed
  long date_reminder_pressed = prefs.getLong(PREF_DATE_REMINDER_PRESSED, 0);
      
      try{
        int appVersionCode = reactContext.getPackageManager().getPackageInfo(reactContext.getPackageName(), 0).versionCode;
        if(prefs.getInt(PREF_APP_VERSION_CODE, 0)  != appVersionCode){
          //Reset the launch and event counters to help assure users are rating based on the latest version. 
          launch_count = 0;
          event_count = 0;
          editor.putLong(PREF_EVENT_COUNT, event_count);
        }
        editor.putInt(PREF_APP_VERSION_CODE, appVersionCode);
      }catch(Exception e){
        //do nothing
      }
      
      launch_count++;
      editor.putLong(PREF_LAUNCH_COUNT, launch_count);

      if (date_firstLaunch == 0) {
          date_firstLaunch = System.currentTimeMillis();
          editor.putLong(PREF_DATE_FIRST_LAUNCHED, date_firstLaunch);
      }
      
      // Wait at least n days or m events before opening
      if (launch_count >= reactContext.getResources().getInteger(R.integer.appirator_launches_until_prompt)) {
    long millisecondsToWait = reactContext.getResources().getInteger(R.integer.appirator_days_until_prompt) * 24 * 60 * 60 * 1000L;           
    if (System.currentTimeMillis() >= (date_firstLaunch + millisecondsToWait) || event_count >= reactContext.getResources().getInteger(R.integer.appirator_events_until_prompt)) {
      if(date_reminder_pressed == 0){
                  if (prefs.getBoolean(PREF_APP_LOVE_CLICKED, false)){
                      showRateDialog(reactContext, editor);
                  }
      }else{
        long remindMillisecondsToWait = reactContext.getResources().getInteger(R.integer.appirator_days_before_reminding) * 24 * 60 * 60 * 1000L;
        if(System.currentTimeMillis() >= (remindMillisecondsToWait + date_reminder_pressed)){
                      if (prefs.getBoolean(PREF_APP_LOVE_CLICKED, false)) {
                          showRateDialog(reactContext, editor);
                      }
        }
      }
    }
      }
      
      editor.commit();
  }   

  @ReactMethod
  public static void rateApp()
  {
      SharedPreferences prefs = reactContext.getSharedPreferences(reactContext.getPackageName()+".appirater", 0);
      SharedPreferences.Editor editor = prefs.edit();
      rateApp(reactContext, editor);
  }

  @TargetApi(Build.VERSION_CODES.GINGERBREAD)
  @ReactMethod
  public static void userDidSignificantEvent() {
      boolean testMode = reactContext.getResources().getBoolean(R.bool.appirator_test_mode);
      SharedPreferences prefs = reactContext.getSharedPreferences(reactContext.getPackageName()+".appirater", 0);
      if(!testMode && (prefs.getBoolean(PREF_DONT_SHOW, false) || prefs.getBoolean(PREF_RATE_CLICKED, false))) {return;}

      long event_count = prefs.getLong(PREF_EVENT_COUNT, 0);
      event_count++;
      prefs.edit().putLong(PREF_EVENT_COUNT, event_count).apply();
  }

  private static void rateApp(ReactApplicationContext reactContext, final SharedPreferences.Editor editor) {
      reactContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(String.format(reactContext.getString(R.string.appirator_market_url), reactContext.getPackageName()))));
      if (editor != null) {
          editor.putBoolean(PREF_RATE_CLICKED, true);
          editor.commit();
      }
  }

  @SuppressLint("NewApi")
  private static void showRateDialog(final ReactApplicationContext reactContext, final SharedPreferences.Editor editor) {
      String appName = reactContext.getString(R.string.appirator_app_title);
        final Dialog dialog = new Dialog(reactContext);
        
        if (Build.VERSION.RELEASE.startsWith("1.") || Build.VERSION.RELEASE.startsWith("2.0") || Build.VERSION.RELEASE.startsWith("2.1")){
          //No dialog title on pre-froyo devices
          dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        }else if(reactContext.getResources().getDisplayMetrics().densityDpi == DisplayMetrics.DENSITY_LOW || reactContext.getResources().getDisplayMetrics().densityDpi == DisplayMetrics.DENSITY_MEDIUM){
          Display display = ((WindowManager) reactContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
          int rotation = display.getRotation();
          if(rotation == 90 || rotation == 270){
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
          }else{
            dialog.setTitle(String.format(reactContext.getString(R.string.rate_title), appName));
          }
        }else{
          dialog.setTitle(String.format(reactContext.getString(R.string.rate_title), appName));
        }

        LinearLayout layout = (LinearLayout)LayoutInflater.from(reactContext).inflate(R.layout.appirater, null);
        
        TextView tv = (TextView) layout.findViewById(R.id.message);
        tv.setText(String.format(reactContext.getString(R.string.rate_message), appName));
        
        Button rateButton = (Button) layout.findViewById(R.id.rate);
        rateButton.setText(String.format(reactContext.getString(R.string.rate), appName));
        rateButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                rateApp(reactContext, editor);
                dialog.dismiss();
            }
        });

        Button rateLaterButton = (Button) layout.findViewById(R.id.rateLater);
        rateLaterButton.setText(reactContext.getString(R.string.rate_later));
        rateLaterButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
              if (editor != null) {
            editor.putLong(PREF_DATE_REMINDER_PRESSED,System.currentTimeMillis());
            editor.commit();
        }
                dialog.dismiss();
            }
        });

        Button cancelButton = (Button) layout.findViewById(R.id.cancel);
        cancelButton.setText(reactContext.getString(R.string.rate_cancel));
        cancelButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (editor != null) {
                    editor.putBoolean(PREF_DONT_SHOW, true);
                    editor.commit();
                }
                dialog.dismiss();
            }
        });

        dialog.setContentView(layout);        
        dialog.show();        
    }
}
