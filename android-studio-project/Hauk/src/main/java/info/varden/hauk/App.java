package info.varden.hauk;

import info.varden.hauk.http.security.NoSSLv3SocketFactory;
import info.varden.hauk.http.security.TLSSocketFactory;
import info.varden.hauk.system.security.KeyStoreHelper;

import android.app.Application;
import android.os.Build;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

public class App extends Application {
  @Override
  public void onCreate() {
    super.onCreate();

    if (
      (Build.VERSION.SDK_INT >= 16) &&
      (Build.VERSION.SDK_INT <  20)
    ) {
      try {
        SSLSocketFactory socketFactory = (SSLSocketFactory) new NoSSLv3SocketFactory( new TLSSocketFactory() );

        HttpsURLConnection.setDefaultSSLSocketFactory(socketFactory);
      }
      catch(Exception e) {}
    }

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
}
