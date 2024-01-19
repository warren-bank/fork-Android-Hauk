package info.varden.hauk;

import info.varden.hauk.http.security.TLSSocketFactory;

import android.app.Application;
import android.os.Build;

import javax.net.ssl.HttpsURLConnection;

public class App extends Application {
  @Override
  public void onCreate() {
    super.onCreate();

    if (
      (Build.VERSION.SDK_INT >= 16) &&
      (Build.VERSION.SDK_INT <  20)
    ) {
      try {
        TLSSocketFactory socketFactory = new TLSSocketFactory();

        HttpsURLConnection.setDefaultSSLSocketFactory(socketFactory);
      }
      catch(Exception e) {}
    }
  }
}
