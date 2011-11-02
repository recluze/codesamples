import java.io.File;
import java.io.FileOutputStream;
import java.security.PrivateKey;
import java.security.Provider;
import java.util.Collections;

import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.dom.DOMStructure;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.XMLObject;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class Sign {

	public static void main(String[] args) {
		try {
			String input = "src/invoice.xml";
			String output = "src/signature.xml";

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);

			// Step 1
			String providerName = System.getProperty("jsr105Provider",
					"org.jcp.xml.dsig.internal.dom.XMLDSigRI");
			XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM",
					(Provider) Class.forName(providerName).newInstance());

			// Step 2
			Reference ref = fac.newReference("#invoice", fac.newDigestMethod(
					DigestMethod.SHA1, null));

			// Step 3
			Document XML = dbf.newDocumentBuilder().parse(new File(input));
			Node invoice = XML.getDocumentElement();
			XMLStructure content = new DOMStructure(invoice);
			XMLObject obj = fac.newXMLObject(
					Collections.singletonList(content), "invoice", null, null);

			// Step 4

			SignedInfo si = fac.newSignedInfo(fac.newCanonicalizationMethod(
					CanonicalizationMethod.INCLUSIVE_WITH_COMMENTS,
					(C14NMethodParameterSpec) null), fac.newSignatureMethod(
					SignatureMethod.DSA_SHA1, null), Collections
					.singletonList(ref));
			// Step 5
			PrivateKey privatekey = KeyStoreInfo.getPrivateKey(
					"src/bizkeystore", "sp1234", "kp1234", "biz");

			// Step 6
			KeyInfo ki = null;

			// Step 7
			XMLSignature signature = fac.newXMLSignature(si, ki, Collections
					.singletonList(obj), null, null);

			// Step 8
			Document doc = dbf.newDocumentBuilder().newDocument();
			DOMSignContext dsc = new DOMSignContext(privatekey, doc);

			// Step 9
			signature.sign(dsc);

			// 
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer trans = tf.newTransformer();
			trans.transform(new DOMSource(doc), new StreamResult(
					new FileOutputStream(output)));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
