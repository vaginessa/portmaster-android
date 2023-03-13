import { ChangeDetectorRef, Component, EventEmitter, OnInit, Output, ViewChild } from '@angular/core';
import { PluginListenerHandle } from '@capacitor/core';
import { IonAccordionGroup, IonSlides} from '@ionic/angular';
import GoBridge, { GoInterface } from '../plugins/go.bridge';
import JavaBridge from '../plugins/java.bridge';
import { UpdateState } from '../types/spn.types';


enum Slides {
    Welcome = 0,
    Permissions = 1,
    Download = 2,
    Continue = 3,
}

@Component({
  selector: 'welcome-screen',
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.scss'],
})
export class WelcomeComponent implements OnInit {

  @Output() onExit = new EventEmitter();
  @ViewChild('slides') slides: IonSlides;
  @ViewChild('permissionsGroup', { static: true }) permissionsGroup: IonAccordionGroup;

  private readonly EventID = "welcome-screen-updater";
  private Listener: PluginListenerHandle;
  private Update: UpdateState = new UpdateState();

  private NotificationPermissionGranted : boolean = false;
  private VPNPermissionGranted : boolean = false;

  constructor(private changeDetector: ChangeDetectorRef) { }

  public async ngOnInit() {
    var result = await JavaBridge.isNotificationPermissionGranted();
    this.NotificationPermissionGranted = result.granted;

    result = await JavaBridge.isVPNPermissionGranted();
    this.VPNPermissionGranted = result.granted;

    window.addEventListener("vpn-permission", (msg: any) => {
      this.VPNPermissionGranted = msg.granted;
    });
    this.permissionsGroup.value = "vpn";

    this.Listener = await GoInterface.addListener(this.EventID, async (update: any) => {
      this.Update = update;
      this.changeDetector.detectChanges();
    });

    GoBridge.SubscribeToUpdater({eventID: this.EventID}) 
  }

  public async onActiveIndexChange() {
    var index = await this.slides.getActiveIndex();
    if(index == Slides.Download) {
      this.slides.lockSwipeToNext(true);
    } else {
      this.slides.lockSwipeToNext(false);
    }
  }

  public Download() {
    this.slides.lockSwipeToNext(false);
    this.slides.slideNext();
    GoBridge.DownloadPendingUpdates();
    JavaBridge.setWelcomeScreenShowed({showed: true});
  }

  public WaitForWifi() {
    this.slides.lockSwipeToNext(false);
    this.slides.slideNext();
    GoBridge.DownloadUpdatesOnWifiConnected();
    JavaBridge.setWelcomeScreenShowed({showed: true});
  } 

  public async Continue() {
    this.onExit.emit();
  }

  public async RequestVPNPermission() {
    await JavaBridge.requestVPNPermission();
    this.permissionsGroup.value = "notifications";
  }

  public async RequestNotificationPermission() {
    var result = await JavaBridge.requestNotificationsPermission();
    this.NotificationPermissionGranted = result.granted;
    this.permissionsGroup.value = "apps";
  }

  public NetworkState() {
    this.permissionsGroup.value = "netstate";
  }

  public NextSlide() {
    this.slides.slideNext();
  }
}
