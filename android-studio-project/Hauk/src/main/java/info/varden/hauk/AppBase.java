package info.varden.hauk;

import info.varden.hauk.http.security.SSLSocketFactoryCompat;
import info.varden.hauk.system.security.KeyStoreHelper;

import android.app.Application;
import android.os.Build;

import javax.net.ssl.HttpsURLConnection;

public class AppBase extends Application {

  @Override
  public void onCreate() {
    super.onCreate();

    if (
      (Build.VERSION.SDK_INT >= 18) &&
      (Build.VERSION.SDK_INT <  23)
    ) {
      try {
        KeyStoreHelper.initStaticResources(
          getApplicationContext()
        );
      }
      catch(Exception e) {}
    }
  }

  protected void upgradeSSLSocketFactory(boolean upgradeProtocols, boolean upgradeCipherSuites) {
    try {
      SSLSocketFactoryCompat socketFactory = new SSLSocketFactoryCompat(upgradeProtocols, upgradeCipherSuites);

      HttpsURLConnection.setDefaultSSLSocketFactory(socketFactory);
    }
    catch(Exception e) {}
  }

}
