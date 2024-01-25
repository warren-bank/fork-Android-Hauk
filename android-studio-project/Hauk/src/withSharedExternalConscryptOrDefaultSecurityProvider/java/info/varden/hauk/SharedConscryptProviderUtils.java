package info.varden.hauk;

/*
 * https://github.com/mendhak/Conscrypt-Provider
 * https://github.com/mendhak/Conscrypt-Provider/releases
 *
 * https://f-droid.org/packages/com.mendhak.conscryptprovider/
 *
 * minSdk: 16 (Android 4.1, Jelly Bean)
 *   note: this is more restrictive than Conscrypt,
 *         which supports API 9+ (Android 2.3, Gingerbread)
 */

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

import java.io.ByteArrayInputStream;
import java.lang.Class;
import java.lang.ClassLoader;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.reflect.Method;
import java.lang.String;
import java.lang.StringBuilder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class SharedConscryptProviderUtils {

  private static String sharedApplicationId  = "com.mendhak.conscryptprovider";
  private static String validSignatureAuthor = "C7:90:8D:17:33:76:1D:F3:CD:EB:56:67:16:C8:00:B5:AF:C5:57:DB";
  private static String validSignatureFdroid = "9D:E1:4D:DA:20:F0:5A:58:01:BE:23:CC:53:34:14:11:48:76:B7:5E";

  public static boolean loadConscrypt(Context context) {
    boolean didLoad = false;

    try {
      if (useSharedApplication(context)) {
        Context targetContext   = context.createPackageContext(sharedApplicationId, Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY);
        ClassLoader classLoader = targetContext.getClassLoader();
        Class installClass      = classLoader.loadClass(sharedApplicationId + ".ConscryptProvider");
        Method installMethod    = installClass.getMethod("install", new Class[]{});
        installMethod.invoke(null);

        didLoad = true;
      }
    }
    catch(Exception e) {}

    return didLoad;
  }

  private static boolean useSharedApplication(Context context) throws Exception {
    String signature = getPackageSignature(sharedApplicationId, context);

    return ((signature != null) && (signature.equals(validSignatureAuthor) || signature.equals(validSignatureFdroid)));
  }

  private static String getPackageSignature(String targetPackage, Context context) throws PackageManager.NameNotFoundException, CertificateException, NoSuchAlgorithmException {
    Signature sig         = context.getPackageManager().getPackageInfo(targetPackage, PackageManager.GET_SIGNATURES).signatures[0];
    CertificateFactory cf = CertificateFactory.getInstance("X.509");
    X509Certificate cert  = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(sig.toByteArray()));
    String hexString      = null;
    MessageDigest md      = MessageDigest.getInstance("SHA1");
    byte[] publicKey      = md.digest(cert.getEncoded());
    hexString             = byte2HexFormatted(publicKey);
    return hexString;
  }

  private static String byte2HexFormatted(byte[] arr) {
    StringBuilder str = new StringBuilder(arr.length * 2);
    for (int i = 0; i < arr.length; i++) {
      String h = Integer.toHexString(arr[i]);
      int l = h.length();
      if (l == 1) h = "0" + h;
      if (l > 2) h = h.substring(l - 2, l);
      str.append(h.toUpperCase());
      if (i < (arr.length - 1)) str.append(':');
    }
    return str.toString();
  }

}
