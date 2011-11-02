import java.io.FileInputStream;
import java.io.IOException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

public class KeyStoreInfo {

	private static KeyStore loadKeyStore(String store, String sPass)
			throws KeyStoreException, NoSuchAlgorithmException,
			CertificateException, IOException {
		KeyStore myKS = KeyStore.getInstance("JKS");
		FileInputStream fis = new FileInputStream(store);
		myKS.load(fis, sPass.toCharArray());
		fis.close();
		return myKS;
	}

	public static PublicKey getPublicKey(String store, String sPass,
			String alias) throws KeyStoreException, NoSuchAlgorithmException,
			CertificateException, IOException {
		KeyStore ks = loadKeyStore(store, sPass);
		java.security.cert.Certificate cert = ks.getCertificate(alias);
		return cert.getPublicKey();
	}

	public static KeyPair getKeyPair(String store, String sPass, String kPass,
			String alias) throws CertificateException, IOException,
			UnrecoverableKeyException, KeyStoreException,
			NoSuchAlgorithmException {
		KeyStore ks = loadKeyStore(store, sPass);
		// KeyPair keyPair = null;
		Key key = null;
		PublicKey publicKey = null;
		PrivateKey privateKey = null;

		if (ks.containsAlias(alias)) {
			key = ks.getKey(alias, kPass.toCharArray());
			if (key instanceof PrivateKey) {
				java.security.cert.Certificate cert = ks.getCertificate(alias);
				publicKey = cert.getPublicKey();
				privateKey = (PrivateKey) key;
				return new KeyPair(publicKey, privateKey);
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public static PrivateKey getPrivateKey(String store, String sPass,
			String kPass, String alias) throws KeyStoreException,
			UnrecoverableKeyException, NoSuchAlgorithmException,
			CertificateException, IOException {
		KeyPair kp = getKeyPair(store, sPass, kPass, alias);
		return kp.getPrivate();
	}
}
