package io.safing.portmaster.android.ui;

// DO NOT EDIT THIS FILE!
// The file was autogenerated by go/codegen/gen.go

import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

@CapacitorPlugin(name = "GoBridge")
public class GoBridge extends Plugin {

    @PluginMethod()
    public void EnableSPN(PluginCall call) {
        engine.Engine.enableSPN(new GoPluginCall(call));
    }

    @PluginMethod()
    public void DisableSPN(PluginCall call) {
        engine.Engine.disableSPN(new GoPluginCall(call));
    }

    @PluginMethod()
    public void GetTunnelState(PluginCall call) {
        engine.Engine.getTunnelState(new GoPluginCall(call));
    }

    @PluginMethod()
    public void GetUser(PluginCall call) {
        engine.Engine.getUser(new GoPluginCall(call));
    }

    @PluginMethod()
    public void Login(PluginCall call) {
        engine.Engine.login(new GoPluginCall(call));
    }

    @PluginMethod()
    public void Logout(PluginCall call) {
        engine.Engine.logout(new GoPluginCall(call));
    }

    @PluginMethod()
    public void UpdateUserInfo(PluginCall call) {
        engine.Engine.updateUserInfo(new GoPluginCall(call));
    }

    @PluginMethod()
    public void GetSPNStatus(PluginCall call) {
        engine.Engine.getSPNStatus(new GoPluginCall(call));
    }

    @PluginMethod()
    public void GetLogs(PluginCall call) {
        engine.Engine.getLogs(new GoPluginCall(call));
    }

    @PluginMethod()
    public void GetDebugInfoFile(PluginCall call) {
        engine.Engine.getDebugInfoFile(new GoPluginCall(call));
    }

}
