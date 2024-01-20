package info.varden.hauk.system.security;

import android.content.Context;
import android.os.Build;
import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Calendar;

import javax.crypto.Cipher;
import javax.security.auth.x500.X500Principal;

import info.varden.hauk.utils.Log;

/**
 * Helper class that interacts with the Android key store and offers simple methods for encrypting
 * and decrypting data.
 *
 * @author Marius Lindvall
 */
public final class KeyStoreHelper {
    @SuppressWarnings("HardCodedStringLiteral")
    private static final String ANDROID_KEY_STORE = "AndroidKeyStore";
    @SuppressWarnings("HardCodedStringLiteral")
    private static final String TRANSFORMATION = "RSA/ECB/PKCS1Padding";

    private static Context context = null;
    private static KeyStore store = null;
    private KeyPair keyPair = null;

    public static void initStaticResources(Context context) {
        KeyStoreHelper.context = context;
    }

    /**
     * Retrieves a key store helper for the given key store alias.
     *
     * @param alias The alias to retrieve a helper for.
     */
    public KeyStoreHelper(KeyStoreAlias alias) {
        try {
            // Load the key store if not already loaded.
            if (store == null) loadKeyStore();

            // Check if the alias exists. If not, create it.
            if (!store.containsAlias(alias.getAlias())) {
                Log.i("Generating new keypair for alias %s", alias); //NON-NLS

                KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", ANDROID_KEY_STORE);
                AlgorithmParameterSpec spec = this.getAlgorithmParameterSpec(alias);
                generator.initialize(spec);
                this.keyPair = generator.generateKeyPair();
            }
            else {
                Log.i("Loading existing keypair for alias %s", alias); //NON-NLS

                PrivateKey privateKey = (PrivateKey) store.getKey(alias.getAlias(), null);
                PublicKey  publicKey  = (PublicKey)  store.getCertificate(alias.getAlias()).getPublicKey();

                if (privateKey == null || publicKey == null)
                    throw new Exception("Unable to load keypair for alias");

                this.keyPair = new KeyPair(publicKey, privateKey);
            }
        }
        catch (Exception e) {
            Log.e("Unable to load key store or generate keys", e); //NON-NLS
        }
    }

    private AlgorithmParameterSpec getAlgorithmParameterSpec(KeyStoreAlias alias) throws Exception {
        AlgorithmParameterSpec spec = null;

        if (Build.VERSION.SDK_INT >= 23) {
            spec = (AlgorithmParameterSpec) new KeyGenParameterSpec.Builder(alias.getAlias(), KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_ECB)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                .build();
        }
        else if (Build.VERSION.SDK_INT >= 18) {
            if (KeyStoreHelper.context != null) {
                Calendar startDate = Calendar.getInstance();
                Calendar endDate   = Calendar.getInstance(); endDate.add(Calendar.YEAR, 20);

                spec = (AlgorithmParameterSpec) new KeyPairGeneratorSpec.Builder(KeyStoreHelper.context)
                  .setAlias(alias.getAlias())
                  .setSerialNumber(BigInteger.ONE)
                  .setSubject(new X500Principal("CN=" + alias.getAlias() + " CA Certificate"))
                  .setStartDate(startDate.getTime())
                  .setEndDate(endDate.getTime())
                  .build();
            }
            else {
                throw new Exception("Context is null");
            }
        }
        else {
            throw new Exception("AlgorithmParameterSpec subclass is not supported");
        }

        return spec;
    }

    /**
     * Encrypts the given data.
     *
     * @param data The data to encrypt.
     * @return The encrypted data and IV.
     * @throws EncryptionException if there was an error while encrypting.
     */
    private EncryptedData encrypt(byte[] data) throws EncryptionException {
        Log.v("Encrypting data"); //NON-NLS

        // Catch errors during initialization.
        if (this.keyPair == null) throw new EncryptionException(new InvalidKeyException("Encryption keypair is null"));

        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, this.keyPair.getPublic());
            byte[] iv = cipher.getIV();
            byte[] message = cipher.doFinal(data);
            return new EncryptedData(iv, message);
        } catch (Exception e) {
            throw new EncryptionException(e);
        }
    }

    /**
     * Decrypts the given data.
     *
     * @param data The data to decrypt.
     * @return The cleartext data.
     * @throws EncryptionException if there was an error while decrypting.
     */
    private byte[] decrypt(EncryptedData data) throws EncryptionException {
        Log.v("Decrypting data"); //NON-NLS

        // Catch errors during initialization.
        if (this.keyPair == null) throw new EncryptionException(new InvalidKeyException("Decryption keypair is null"));

        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, this.keyPair.getPrivate());
            return cipher.doFinal(data.getMessage());
        } catch (Exception e) {
            throw new EncryptionException(e);
        }
    }

    /**
     * Encrypts the given string.
     *
     * @param data The string to encrypt.
     * @return The encrypted data and IV.
     * @throws EncryptionException if there was an error while encrypting.
     */
    public EncryptedData encryptString(String data) throws EncryptionException {
        return encrypt(data.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Decrypts the given string.
     *
     * @param data The string to decrypt.
     * @return The cleartext string.
     * @throws EncryptionException if there was an error while decrypting.
     */
    public String decryptString(EncryptedData data) throws EncryptionException {
        return new String(decrypt(data), StandardCharsets.UTF_8);
    }

    /**
     * Loads the Android key store.
     *
     * @throws Exception if the loading failed.
     */
    private static void loadKeyStore() throws Exception {
        store = KeyStore.getInstance(ANDROID_KEY_STORE);
        store.load(null);
    }
}
