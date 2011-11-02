import java.io.*;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;

/**
 *This class implements SAX Parser
 */
public class SAXExample1 extends DefaultHandler {
	private int count = 0;

	public static void main(String[] argv) {
		if (argv.length != 1) {
			System.err.println("Usage: SAXExample1 Filename");
			System.exit(1);
		}
		// Create an Object of the SAXExample1 class for SAX event handler
		SAXExample1 saxObject = new SAXExample1();
		// Create an object of SAXParserFactory for validating purpose.
		SAXParserFactory spfactory = SAXParserFactory.newInstance();
		try {
			// Parse the specified ".xml" file
			SAXParser saxParse = spfactory.newSAXParser();
			saxParse.parse(new File(argv[0]), saxObject);
		} catch (SAXParseException spExcept) {
			// Error generated while parsing
			System.out.println("\n** Error occurred while parsing **"
					+ ", line " + spExcept.getLineNumber());
			System.out.println(" " + spExcept.getMessage());
		} catch (SAXException sExcept) {
			// Error generated while initializing the parser.
			Exception Except = sExcept;
			if (sExcept.getException() != null) {
				Except = sExcept.getException();
			}
			Except.printStackTrace();
		} catch (ParserConfigurationException pcExcept) {

			// Parser with specified options cannot be built
			pcExcept.printStackTrace();
		} catch (IOException ioExcept) {
			// I/O error
			ioExcept.printStackTrace();
		} catch (Throwable t) {
			t.printStackTrace();
		}
		System.exit(0);
	}

	/**
	 * The parser calls this method whenever it encounters END of document
	 */
	public void endDocument() throws SAXException {
		System.out.println("\nNumber of Customers: " + count);
	}

	/**
	 * The parser calls this method whenever it encounters START of element
	 */
	public void startElement(String namespaceURI, String simpleName,
			String qualifiedName, Attributes attributeList) throws SAXException {
		if (qualifiedName.equals("Customer"))
			count++;
	}

	/**
	 * This overrides the default Error Handler.
	 */
	public void error(SAXParseException spExcept) throws SAXParseException {
		throw spExcept;
	}
}