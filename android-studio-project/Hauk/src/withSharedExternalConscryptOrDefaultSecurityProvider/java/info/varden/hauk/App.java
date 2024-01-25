package info.varden.hauk;

public class App extends AppBase {

  @Override
  public void onCreate() {
    super.onCreate();

    boolean upgradeProtocols    = true;
    boolean upgradeCipherSuites = true;
    boolean isConscryptLoaded   = SharedConscryptProviderUtils.loadConscrypt( getApplicationContext() );

    if (isConscryptLoaded) {
      upgradeCipherSuites = false;
    }

    upgradeSSLSocketFactory(upgradeProtocols, upgradeCipherSuites);
  }

}
