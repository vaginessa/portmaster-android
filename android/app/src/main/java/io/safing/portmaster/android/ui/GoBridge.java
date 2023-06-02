package io.safing.portmaster.android.ui;

// DO NOT EDIT THIS FILE!
// The file was autogenerated by go/codegen/gen.go

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

import org.json.JSONException;

@CapacitorPlugin(name = "GoBridge")
public class GoBridge extends Plugin {

	@PluginMethod()
	public void IsTunnelActive(PluginCall call) {
		exported.Exported.isTunnelActive(new GoPluginCall(this, call));
	}

	@PluginMethod()
	public void EnableTunnel(PluginCall call) {
		exported.Exported.enableTunnel(new GoPluginCall(this, call));
	}

	@PluginMethod()
	public void RestartTunnel(PluginCall call) {
		exported.Exported.restartTunnel(new GoPluginCall(this, call));
	}

	@PluginMethod()
	public void GetLogs(PluginCall call) {
		exported.Exported.getLogs(new GoPluginCall(this, call));
	}

	@PluginMethod()
	public void GetDebugInfoFile(PluginCall call) {
		exported.Exported.getDebugInfoFile(new GoPluginCall(this, call));
	}

	@PluginMethod()
	public void GetDebugInfo(PluginCall call) {
		exported.Exported.getDebugInfo(new GoPluginCall(this, call));
	}

	@PluginMethod()
	public void Shutdown(PluginCall call) {
		exported.Exported.shutdown(new GoPluginCall(this, call));
	}

	@PluginMethod()
	public void CreateIssue(PluginCall call) {
		exported.Exported.createIssue(new GoPluginCall(this, call));
	}

	@PluginMethod()
	public void CreateTicket(PluginCall call) {
		exported.Exported.createTicket(new GoPluginCall(this, call));
	}

	@PluginMethod()
	public void IsGeoIPDataAvailable(PluginCall call) {
		exported.Exported.isGeoIPDataAvailable(new GoPluginCall(this, call));
	}

	@PluginMethod()
	public void NewApkAvaliable(PluginCall call) {
		exported.Exported.newApkAvaliable(new GoPluginCall(this, call));
	}

	@PluginMethod()
	public void PerformRequest(PluginCall call) {
		exported.Exported.performRequest(new GoPluginCall(this, call));
	}

	@PluginMethod()
	public void DatabaseMessage(PluginCall call) {
		exported.Exported.databaseMessage(new GoPluginCall(this, call));
	}

	@PluginMethod()
	public void SubscribeToDatabase(PluginCall call) {
		exported.Exported.subscribeToDatabase(new GoPluginCall(this, call));
	}

	@PluginMethod()
	public void DownloadPendingUpdates(PluginCall call) {
		exported.Exported.downloadPendingUpdates(new GoPluginCall(this, call));
	}

	@PluginMethod()
	public void DownloadUpdatesOnWifiConnected(PluginCall call) {
		exported.Exported.downloadUpdatesOnWifiConnected(new GoPluginCall(this, call));
	}

	@PluginMethod()
	public void IsOnWifiNetwork(PluginCall call) {
		exported.Exported.isOnWifiNetwork(new GoPluginCall(this, call));
	}

	public void notifyListener(String name, String data) throws JSONException {
		notifyListeners(name, new JSObject(data));
	}
}
