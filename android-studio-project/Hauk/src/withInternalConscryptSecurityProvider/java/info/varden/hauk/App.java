package info.varden.hauk;

import java.security.Security;

import org.conscrypt.Conscrypt;

public class App extends AppBase {

  @Override
  public void onCreate() {
    super.onCreate();

    boolean upgradeProtocols     = true;
    boolean upgradeCipherSuites  = true;
    boolean isConscryptAvailable = Conscrypt.isAvailable();

    if (isConscryptAvailable) {
      upgradeCipherSuites = false;

      Security.insertProviderAt(Conscrypt.newProvider(), 1);
    }

    upgradeSSLSocketFactory(upgradeProtocols, upgradeCipherSuites);
  }

}
