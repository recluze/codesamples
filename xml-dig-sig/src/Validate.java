import java.io.File;
import java.security.Provider;
import java.security.PublicKey;
import java.util.Iterator;

import javax.xml.crypto.dom.DOMStructure;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class Validate {
	public static void main(String args[]) {
		try {
			// Step 1
			String providerName = System.getProperty("jsr105Provider",
					"org.jcp.xml.dsig.internal.dom.XMLDSigRI");
			XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM",
					(Provider) Class.forName(providerName).newInstance());

			// Step 2
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			Document doc = dbf.newDocumentBuilder().parse(
					new File("src/signature.xml"));
			// Step 3
			NodeList nl = doc.getElementsByTagNameNS(XMLSignature.XMLNS,
					"Signature");
			if (nl.getLength() == 0)
				throw new Exception("Cannot find signature");

			// Step 4 .. using 4.3

			PublicKey pKey = KeyStoreInfo.getPublicKey("src/bizkeystore",
					"sp1234", "biz");
			DOMValidateContext valContext = new DOMValidateContext(pKey, nl
					.item(0));

			// Step 5
			XMLSignature signature = fac
					.unmarshalXMLSignature(new DOMStructure(nl.item(0)));

			// Step 6
			boolean coreValidity = signature.validate(valContext);

			if (!coreValidity) {
				System.err.println("Signature Failed Core Validation.");
				boolean sv = signature.getSignatureValue().validate(valContext);
				System.out.println("Signature validation status: " + sv);

				// checking validation status of each refernce
				Iterator i = signature.getSignedInfo().getReferences()
						.iterator();
				for (int j = 0; i.hasNext(); j++) {
					boolean refValid = ((Reference) i.next())
							.validate(valContext);
					System.out.println("Reference: [" + j + "]  - Status: "
							+ refValid);
				}
			} else {
				System.out.println("Signature passed core validation. ");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
