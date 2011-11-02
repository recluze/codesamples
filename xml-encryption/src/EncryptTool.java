import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.security.Key;

import javax.crypto.SecretKey;
import javax.crypto.KeyGenerator;

import org.apache.xml.security.keys.KeyInfo;
import org.apache.xml.security.encryption.XMLCipher;
import org.apache.xml.security.encryption.EncryptedData;
import org.apache.xml.security.encryption.EncryptedKey;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.OutputKeys;

/**
 * The EncryptTool class reads input from a file, encrypts the contents of the
 * file, and then stores the encrypted file to disk. In order to accomplish
 * this, the tool uses the Apache XML framework to create two symmetric keys for
 * the following purposes: 1) to encrypt the actual XML-file data 2) to encrypt
 * the key used to encrypt the XML-file data
 * 
 * The encrypted data is written to disk and the key used to encrypt the
 * data-encryption key is also stored to disk.
 * 
 * @author <a href="mailto:jeff@jeffhanson.com">Jeff Hanson</a>
 * @version $Revision: 1.1 $ <p/>
 *          <p>
 *          <b>Revisions:</b> <p/>
 *          <p>
 *          <b>Jul 6, 2005 jhanson:</b>
 *          <ul>
 *          <li> Created file.
 *          </ul>
 */

public class EncryptTool {
	static {
		org.apache.xml.security.Init.init();
	}

	private static Document parseFile(String fileName) throws Exception {
		javax.xml.parsers.DocumentBuilderFactory dbf = javax.xml.parsers.DocumentBuilderFactory
				.newInstance();
		dbf.setNamespaceAware(true);
		javax.xml.parsers.DocumentBuilder db = dbf.newDocumentBuilder();
		Document document = db.parse(fileName);

		return document;
	}

	private static SecretKey GenerateKeyEncryptionKey() throws Exception {
		String jceAlgorithmName = "DESede";
		KeyGenerator keyGenerator = KeyGenerator.getInstance(jceAlgorithmName);
		SecretKey keyEncryptKey = keyGenerator.generateKey();

		return keyEncryptKey;
	}

	private static void storeKeyFile(Key keyEncryptKey) throws IOException {
		byte[] keyBytes = keyEncryptKey.getEncoded();
		File keyEncryptKeyFile = new File("keyEncryptKey");
		FileOutputStream outStream = new FileOutputStream(keyEncryptKeyFile);
		outStream.write(keyBytes);
		outStream.close();

		System.out.println("Key encryption key stored in: "
				+ keyEncryptKeyFile.toURL().toString());
	}

	private static SecretKey GenerateSymmetricKey() throws Exception {
		String jceAlgorithmName = "AES";
		KeyGenerator keyGenerator = KeyGenerator.getInstance(jceAlgorithmName);
		keyGenerator.init(128);
		return keyGenerator.generateKey();
	}

	private static void writeEncryptedDocToFile(Document doc, String fileName)
			throws Exception {
		File encryptionFile = new File(fileName);
		FileOutputStream outStream = new FileOutputStream(encryptionFile);

		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer = factory.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(outStream);
		transformer.transform(source, result);

		outStream.close();

		System.out.println("Encrypted XML document written to: "
				+ encryptionFile.toURL().toString());
	}

	private static void usage() {
		System.err.println("usage - java EncryptTool "
				+ "infilename outfilename elementtoencrypt");
		System.err.println("example - java EncryptTool "
				+ "test.xml encrypted.xml CreditCardNumber");
	}

	public static void main(String args[]) throws Exception {
		if (args.length < 2) {
			usage();
			System.exit(1);
		}

		// parse file into document
		Document document = parseFile(args[0]);

		// generate symmetric key
		Key symmetricKey = GenerateSymmetricKey();

		// Get a key to be used for encrypting the symmetric key
		Key keyEncryptKey = GenerateKeyEncryptionKey();

		// Write the key to a file
		storeKeyFile(keyEncryptKey);

		// initialize cipher
		XMLCipher keyCipher = XMLCipher
				.getInstance(XMLCipher.TRIPLEDES_KeyWrap);
		keyCipher.init(XMLCipher.WRAP_MODE, keyEncryptKey);

		// encrypt symmetric key
		EncryptedKey encryptedKey = keyCipher
				.encryptKey(document, symmetricKey);

		// specify the element to encrypt
		Element rootElement = document.getDocumentElement();
		Element elementToEncrypt = rootElement;
		if (args.length > 2) {
			elementToEncrypt = (Element) rootElement.getElementsByTagName(
					args[2]).item(0);
			if (elementToEncrypt == null) {
				System.err.println("Unable to find element: " + args[2]);
				System.exit(1);
			}
		}

		// initialize cipher
		XMLCipher xmlCipher = XMLCipher.getInstance(XMLCipher.AES_128);
		xmlCipher.init(XMLCipher.ENCRYPT_MODE, symmetricKey);

		// add key info to encrypted data element
		EncryptedData encryptedDataElement = xmlCipher.getEncryptedData();
		KeyInfo keyInfo = new KeyInfo(document);
		keyInfo.add(encryptedKey);
		encryptedDataElement.setKeyInfo(keyInfo);

		// do the actual encryption
		boolean encryptContentsOnly = true;
		xmlCipher.doFinal(document, elementToEncrypt, encryptContentsOnly);

		// write the results to a file
		writeEncryptedDocToFile(document, args[1]);
	}
}
