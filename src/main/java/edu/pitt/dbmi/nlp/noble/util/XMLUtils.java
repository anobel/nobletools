package edu.pitt.dbmi.nlp.noble.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * The Class XMLUtils.
 */
public class XMLUtils {
	
	/**
	 * format XML into human readable form.
	 *
	 * @param doc the doc
	 * @param os the os
	 * @throws TransformerException the transformer exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void writeXML(Document doc, OutputStream os) 
		throws TransformerException, IOException{
		// write out xml file
		TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer transformer = tFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        
        //indent XML properly
        //formatXML(doc,doc.getDocumentElement(),"  ");

        //normalize document
        doc.getDocumentElement().normalize();

		 //write XML to file
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(os);
    
        transformer.transform(source, result);
        os.close();
	}
	
	/**
	 * parse XML document.
	 *
	 * @param in the in
	 * @return the document
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static Document parseXML(InputStream in) throws IOException {
		Document document = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		//factory.setValidating(true);
		//factory.setNamespaceAware(true);

		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			//builder.setErrorHandler(new XmlErrorHandler());
			//builder.setEntityResolver(new XmlEntityResolver());
			document = builder.parse(in);
			
			// close input stream
			in.close();
		}catch(Exception ex){
			throw new IOException(ex.getMessage());
		}
		return document;
	}
	
	/**
	 * get single element by tag name.
	 *
	 * @param element the element
	 * @param tag the tag
	 * @return the element by tag name
	 */
	public static Element getElementByTagName(Element element, String tag){
		NodeList list = element.getChildNodes();
		for(int i=0;i<list.getLength();i++){
			Node node = list.item(i);
			if(node instanceof Element && ((Element)node).getTagName().equals(tag)){
				return (Element) node;
			}
		}
		return null;
	}
	
	/**
	 * get single element by tag name.
	 *
	 * @param element the element
	 * @param tag the tag
	 * @return the elements by tag name
	 */
	public static List<Element> getElementsByTagName(Element element, String tag){
		List<Element> elems = new ArrayList<Element>();
		NodeList list = element.getChildNodes();
		for(int i=0;i<list.getLength();i++){
			Node node = list.item(i);
			if(node instanceof Element){
				Element e = (Element) node;
				if(e.getTagName().equals(tag)){
					elems.add(e);
				}
			}
		}
		return elems;
	}
	
	/**
	 * get single element by tag name.
	 *
	 * @param element the element
	 * @return the child elements
	 */
	public static List<Element> getChildElements(Element element){
		return toElements(element.getChildNodes());
	}
	
	/**
	 * get single element by tag name.
	 *
	 * @param element the element
	 * @param tag the tag
	 * @return the child elements
	 */
	public static List<Element> getChildElements(Element element, String tag){
		return toElements(element.getChildNodes(),tag);
	}
	
	/**
	 * To elements.
	 *
	 * @param nodes the nodes
	 * @return the list
	 */
	public static List<Element> toElements(NodeList nodes){
		return toElements(nodes,null);
	}
	
	/**
	 * To elements.
	 *
	 * @param nodes the nodes
	 * @param filter the filter
	 * @return the list
	 */
	private static List<Element> toElements(NodeList nodes, String filter){
		List<Element> list = new ArrayList<Element>();
		for(int i=0;i<nodes.getLength();i++)
			if(nodes.item(i) instanceof Element){
				Element e = (Element)nodes.item(i);
				if(filter == null || e.getTagName().equals(filter)){
					list.add(e);
				}
			}
		return list;
	}
}
