package serg.xslt;

import java.io.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.InputSource;
import com.sun.org.apache.xpath.internal.CachedXPathAPI;

public class PathSelector {

	public static void main(String arg[]) throws Exception {
		String filename = null;
		String xpath = null;
		filename = arg[0];
		// xpath = arg[1];

		xpath = "//book/following-sibling::*";

		// set up a dom tree
		InputSource in = new InputSource(new FileInputStream(filename));
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		Document doc = dbf.newDocumentBuilder().parse(in);

		System.out.println("Querying Dom using : " + xpath);
		CachedXPathAPI path = new CachedXPathAPI();
		NodeIterator nl = path.selectNodeIterator(doc, xpath);
		// the actual XPath selector

		Transformer trans = TransformerFactory.newInstance().newTransformer();
		trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

		System.out.println("<output>");
		Node n;
		while ((n = nl.nextNode()) != null) {
			trans.transform(new DOMSource(n), new StreamResult(
					new OutputStreamWriter(System.out)));
		}
		System.out.println("</output>");
	}
}
