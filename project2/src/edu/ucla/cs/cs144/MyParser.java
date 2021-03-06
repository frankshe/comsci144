/* CS144
 *
 * Parser skeleton for processing item-???.xml files. Must be compiled in
 * JDK 1.5 or above.
 *
 * Instructions:
 *
 * This program processes all files passed on the command line (to parse
 * an entire diectory, type "java MyParser myFiles/*.xml" at the shell).
 *
 * At the point noted below, an individual XML file has been parsed into a
 * DOM Document node. You should fill in code to process the node. Java's
 * interface for the Document Object Model (DOM) is in package
 * org.w3c.dom. The documentation is available online at
 *
 * http://java.sun.com/j2se/1.5.0/docs/api/index.html
 *
 * A tutorial of Java's XML Parsing can be found at:
 *
 * http://java.sun.com/webservices/jaxp/
 *
 * Some auxiliary methods have been written for you. You may find them
 * useful.
 */

package edu.ucla.cs.cs144;

import java.io.*;
import java.text.*;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;

class MyParser {
    private static BufferedWriter items;
    private static BufferedWriter users;
    private static BufferedWriter categories;
    private static BufferedWriter bids;
    
    static final String columnSeparator = "|*|";
    static DocumentBuilder builder;
    
    static final String[] typeName = {
	"none",
	"Element",
	"Attr",
	"Text",
	"CDATA",
	"EntityRef",
	"Entity",
	"ProcInstr",
	"Comment",
	"Document",
	"DocType",
	"DocFragment",
	"Notation",
    };
    
    static class MyErrorHandler implements ErrorHandler {
        
        public void warning(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void error(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void fatalError(SAXParseException exception)
        throws SAXException {
            exception.printStackTrace();
            System.out.println("There should be no errors " +
                               "in the supplied XML files.");
            System.exit(3);
        }
        
    }
    
    /* Non-recursive (NR) version of Node.getElementsByTagName(...)
     */
    static Element[] getElementsByTagNameNR(Element e, String tagName) {
        Vector< Element > elements = new Vector< Element >();
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
            {
                elements.add( (Element)child );
            }
            child = child.getNextSibling();
        }
        Element[] result = new Element[elements.size()];
        elements.copyInto(result);
        return result;
    }
    
    /* Returns the first subelement of e matching the given tagName, or
     * null if one does not exist. NR means Non-Recursive.
     */
    static Element getElementByTagNameNR(Element e, String tagName) {
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
                return (Element) child;
            child = child.getNextSibling();
        }
        return null;
    }
    
    /* Returns the text associated with the given element (which must have
     * type #PCDATA) as child, or "" if it contains no text.
     */
    static String getElementText(Element e) {
        if (e.getChildNodes().getLength() == 1) {
            Text elementText = (Text) e.getFirstChild();
            return elementText.getNodeValue();
        }
        else
            return "";
    }
    
    /* Returns the text (#PCDATA) associated with the first subelement X
     * of e with the given tagName. If no such X exists or X contains no
     * text, "" is returned. NR means Non-Recursive.
     */
    static String getElementTextByTagNameNR(Element e, String tagName) {
        Element elem = getElementByTagNameNR(e, tagName);
        if (elem != null)
            return getElementText(elem);
        else
            return "";
    }
    
    /* Returns the amount (in XXXXX.xx format) denoted by a money-string
     * like $3,453.23. Returns the input if the input is an empty string.
     */
    static String strip(String money) {
        if (money.equals(""))
            return money;
        else {
            double am = 0.0;
            NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
            try { am = nf.parse(money).doubleValue(); }
            catch (ParseException e) {
                System.out.println("This method should work for all " +
                                   "money values you find in our data.");
                System.exit(20);
            }
            nf.setGroupingUsed(false);
            return nf.format(am).substring(1);
        }
    }
    
    /* Process one items-???.xml file.
     */
    static void processFile(File xmlFile) {
        Document doc = null;
        try {
            doc = builder.parse(xmlFile);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(3);
        }
        catch (SAXException e) {
            System.out.println("Parsing error on file " + xmlFile);
            System.out.println("  (not supposed to happen with supplied XML files)");
            e.printStackTrace();
            System.exit(3);
        }
        
        /* At this point 'doc' contains a DOM representation of an 'Items' XML
         * file. Use doc.getDocumentElement() to get the root Element. */
        System.out.println("Successfully parsed - " + xmlFile);
        
        /* Fill in code here (you will probably need to write auxiliary
            methods). */
	Element[] itemArray = getElementsByTagNameNR(doc.getDocumentElement(), "Item");	        
        try {
		for(int i = 0; i < itemArray.length; i++)
		{
			parseItems(itemArray[i]);
			parseBids(itemArray[i]);
			parseUsers(itemArray[i]);
			parseCategories(itemArray[i]);
		}
	}
	catch (IOException e) { e.printStackTrace(); } 
        /**************************************************************/
        
    }
    public static void parseItems(Element item) throws IOException
    {
	File file = new File("items.dat");
	try {
	FileWriter fw = new FileWriter(file, true);
	items = new BufferedWriter(fw);
	
	items.write(item.getAttribute("ItemID") + columnSeparator);
	items.write(getElementTextByTagNameNR(item, "Name") + columnSeparator);
	items.write(strip(getElementTextByTagNameNR(item, "Currently")) + columnSeparator);
	items.write(strip(getElementTextByTagNameNR(item, "Buy_Price")) + columnSeparator);
	items.write(strip(getElementTextByTagNameNR(item, "First_Bid")) + columnSeparator);
	items.write(getElementTextByTagNameNR(item, "Number_of_Bids") + columnSeparator);
	//Started	
	String oldDateString = getElementTextByTagNameNR(item, "Started");
	SimpleDateFormat sdf = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
	try{
	        Date parsed = sdf.parse(oldDateString);
		sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
		items.write(sdf.format(parsed) + columnSeparator);
	} catch (ParseException e) {}

	//Ends
	sdf = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
	oldDateString = getElementTextByTagNameNR(item, "Ends");
	try{
		Date parsed = sdf.parse(oldDateString);
		sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
		items.write(sdf.format(parsed) + columnSeparator);
	} catch (ParseException e) {}
	//Location and Country
	Element itemLocation = getElementByTagNameNR(item, "Location");
	Element itemCountry = getElementByTagNameNR(item, "Country");
	String location = "";
	String country = "";
	if ( itemLocation != null )
		location = getElementText(itemLocation);
	if ( itemCountry != null )
		country = getElementText(itemCountry);
	items.write(location + columnSeparator + country + columnSeparator);
	//Seller ID
	Element seller = getElementByTagNameNR(item, "Seller");
	items.write( seller.getAttribute("UserID") + columnSeparator);
	//Description
	String desc = getElementTextByTagNameNR(item, "Description");
	if ( desc.length() > 4000)
		desc = desc.substring(0, 4000);
	items.write(desc);
	items.newLine();
	items.close();
	} catch (IOException e)
	{
		e.printStackTrace();
	}

    }

    public static void parseBids(Element item) throws IOException
    {
	File file = new File("bids.dat");
	try {
	FileWriter fw = new FileWriter(file, true);
	bids = new BufferedWriter(fw);

	Element bid = getElementByTagNameNR(item, "Bids");
	Element[] bidArray = getElementsByTagNameNR(bid, "Bid");
	
	for(int i = 0; i < bidArray.length; i++)
	{
		bids.write(item.getAttribute("ItemID") + columnSeparator);
		Element bidder = getElementByTagNameNR(bidArray[i], "Bidder");
 		bids.write(bidder.getAttribute("UserID") + columnSeparator);
		bids.write(strip(getElementTextByTagNameNR(bidArray[i], "Amount")) + columnSeparator);
		//Time
		String oldDateString = getElementTextByTagNameNR(bidArray[i], "Time");
		SimpleDateFormat sdf = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
		try {
			if(oldDateString != "")
			{
				Date parsed = sdf.parse(oldDateString);
				sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
				bids.write(sdf.format(parsed));
			}
		} catch (ParseException e) { e.printStackTrace(); }

		bids.newLine();
	}
	
	bids.close();
	} catch (IOException e){
		e.printStackTrace();
	}
    }
    public static void parseUsers(Element item) throws IOException {
	File file = new File("users.dat");
	try {
		FileWriter fw = new FileWriter(file, true);
		users = new BufferedWriter(fw);
		//Add user Sellers
		Element user = getElementByTagNameNR(item, "Seller");
		String userID = user.getAttribute("UserID");
		String rating = user.getAttribute("Rating");

		users.write(userID + columnSeparator + rating);
		users.newLine();
		//Add user Bidders
		Element[] bidArray = getElementsByTagNameNR(getElementByTagNameNR(item, "Bids"), "Bid");
		for (int i = 0; i < bidArray.length; i++)
		{
			Element bidder = getElementByTagNameNR(bidArray[i], "Bidder");
			userID = bidder.getAttribute("UserID");
			rating = bidder.getAttribute("Rating");
			users.write(userID + columnSeparator + rating);
			users.newLine();
		}
		users.close();
	} catch (IOException e) { e.printStackTrace(); }
    }
    public static void parseCategories(Element item) throws IOException
    {
	File file = new File("categories.dat");
	try {
		FileWriter fw = new FileWriter(file,true);
		categories = new BufferedWriter(fw);	
		String itemID = item.getAttribute("ItemID");
		Element[] categoryArray = getElementsByTagNameNR(item, "Category");
		for (int i = 0; i < categoryArray.length; i++)
		{
			String categoryName = getElementText(categoryArray[i]);
			categories.write(itemID+ columnSeparator + categoryName);
			categories.newLine();
		}
		categories.close();
	} catch (IOException e) { e.printStackTrace();}
    }

    public static void main (String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java MyParser [file] [file] ...");
            System.exit(1);
        }
        
        /* Initialize parser. */
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setIgnoringElementContentWhitespace(true);      
            builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new MyErrorHandler());
        }
        catch (FactoryConfigurationError e) {
            System.out.println("unable to get a document builder factory");
            System.exit(2);
        } 
        catch (ParserConfigurationException e) {
            System.out.println("parser was unable to be configured");
            System.exit(2);
        }
        
        /* Process all files listed on command line. */
        for (int i = 0; i < args.length; i++) {
            File currentFile = new File(args[i]);
            processFile(currentFile);
        }
    }
}
