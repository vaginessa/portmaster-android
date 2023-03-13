package io.safing.portmaster.android.ui;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.Uri;

import com.getcapacitor.BridgeActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import engine.Engine;
import io.safing.portmaster.android.R;
import io.safing.portmaster.android.go_interface.Function;
import io.safing.portmaster.android.go_interface.GoInterface;
import io.safing.portmaster.android.os.NetworkProxy;
import io.safing.portmaster.android.os.OSFunctions;
import io.safing.portmaster.android.settings.Settings;
import io.safing.portmaster.android.util.CancelNotification;
import io.safing.portmaster.android.util.DebugInfoDialog;
import io.safing.portmaster.android.util.MinimizeApp;
import io.safing.portmaster.android.util.ShowNotification;
import io.safing.portmaster.android.util.ServiceCommand;
import io.safing.portmaster.android.util.UIEvent;

public class MainActivity extends BridgeActivity {

  public static final int REQUEST_VPN_PERMISSION = 1;
  public static final int ENABLE_VPN = 2;
  public static final int EXPORT_DEBUG_INFO = 3;

  public static final String CHANNEL_ID = "Portmaster";

  // Function objects that are called from go
  private ServiceCommand serviceCommand;
  private Function sendUIEvent;
  private DebugInfoDialog getDebugInfoDialogFunction;
  private Function minimizeApp;

  private Function showNotification;
  private Function cancelNotification;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    Log.v("MainActivity", "OnCreate");
    // Register plugins for UI.
    registerPlugin(GoBridge.class);
    registerPlugin(JavaBridge.class);

    if(!Engine.isEngineInitialized()) {
      // Setup event only once before engine is initialized.
      // Unregister needs to be called if System.exit is not called on exit.
      setupEvents();
    }

    // Send OS functions to go.
    Engine.setOSFunctions(OSFunctions.get());

    // Call parent onCreate
    super.onCreate(savedInstanceState);
    initEngine();
  }

  @Override
  public void onStart() {
    super.onStart();
  }

  @Override
  public void onStop() {
    super.onStop();
  }

  public void initEngine() {
    // Create notification channel if it's not created and the welcome screen is not active.
    boolean showWelcomeScreen = Settings.ShouldShowWelcomeScreen(this);
    if(!showWelcomeScreen && !isNotificationChannelCreated()) {
      createNotificationChannel();
    }

    // Prepare UI functions
    GoInterface uiInterface = new GoInterface();

    // UI
    this.serviceCommand = new ServiceCommand("SendServiceCommand", this);
    uiInterface.registerFunction(this.serviceCommand);

    this.sendUIEvent = new UIEvent("SendUIEvent", this.getBridge());
    uiInterface.registerFunction(this.sendUIEvent);

    this.minimizeApp = new MinimizeApp("MinimizeApp", this);
    uiInterface.registerFunction(this.minimizeApp);

    // Debug info
    this.getDebugInfoDialogFunction = new DebugInfoDialog("ExportDebugInfo", this, EXPORT_DEBUG_INFO);
    uiInterface.registerFunction(this.getDebugInfoDialogFunction);

    // Notifications
    this.showNotification = new ShowNotification("ShowNotification", this);
    uiInterface.registerFunction(this.showNotification);

    this.cancelNotification = new CancelNotification("CancelNotification", this);
    uiInterface.registerFunction(this.cancelNotification);

    Engine.setActivityFunctions(uiInterface);
    Log.v("MainActivity", "Engine.onCreate");
    Engine.onCreate(this.getFilesDir().getAbsolutePath());
  }

  @Override
  public void onResume() {
    super.onResume();
    Log.v("MainActivity", "Enable the VPN Service");
  }

  @Override
  public void onPause() {
    super.onPause();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    Engine.onActivityDestroy();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if(resultCode != RESULT_OK) {
      return;
    }

    if(requestCode == REQUEST_VPN_PERMISSION) {
      bridge.triggerJSEvent("vpn-permission", "window", "{\"granted\": true}");
    }

    if(requestCode == ENABLE_VPN) {
      this.serviceCommand.send("keep_alive");
    }

    if(requestCode == EXPORT_DEBUG_INFO) {
      Uri uri = data.getData();
      getDebugInfoDialogFunction.writeToFile(uri);
    }
  }

  public void createNotificationChannel() {
    // Create the NotificationChannel, but only on API 26+ because
    // the NotificationChannel class is new and not in the support library
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      String name = getString(R.string.notification_channel_name);
      String description = getString(R.string.notification_channel_description);
      int importance = NotificationManager.IMPORTANCE_DEFAULT;
      NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
      channel.setDescription(description);
      // Register the channel with the system; you can't change the importance
      // or other notification behaviors after this
      NotificationManager notificationManager = getSystemService(NotificationManager.class);
      notificationManager.createNotificationChannel(channel);
    }
  }

  public boolean isNotificationChannelCreated() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      NotificationManager notificationManager = getSystemService(NotificationManager.class);
      return notificationManager.getNotificationChannel(CHANNEL_ID) != null;
    }
    return true;
  }

  public void setupEvents() {
    ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
        @Override
        public void onAvailable(@NonNull Network network) {
            super.onAvailable(network);
            Engine.onNetworkConnected();
        }

        @Override
        public void onLost(@NonNull Network network) {
            super.onLost(network);
            Engine.onNetworkDisconnected();
        }

        @Override
        public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
            super.onCapabilitiesChanged(network, networkCapabilities);
            Engine.onNetworkCapabilitiesChanged(new NetworkProxy(networkCapabilities));
        }
    };

    ConnectivityManager connectivityManager = null;
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
      connectivityManager = getApplicationContext().getSystemService(ConnectivityManager.class);
      connectivityManager.registerDefaultNetworkCallback(networkCallback);
    }
  }
}
