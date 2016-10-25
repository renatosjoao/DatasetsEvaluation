package org.l3s.iitb;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**
 * This class returns a map that contains the name of the documents and the list of annotations per document.
 * IITB  (CSAW) - Kulkarni09
 * @author renato
 *
 */
public class CSAW_IITB {

	
	
	public CSAW_IITB() {
		super();
	}


	/**
	 * 	Utility method to parse XML file from ground thruth and return a TreeMap with annotations.
	 *
	 * @param args
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public TreeMap<String, ArrayList<CSAW_IITB_Annotation>> getAnnotations(String groundThruthXMLFile) throws ParserConfigurationException,SAXException, IOException {
		TreeMap<String, ArrayList<CSAW_IITB_Annotation>> mapOfAnnotations = new TreeMap<String, ArrayList<CSAW_IITB_Annotation>>(String.CASE_INSENSITIVE_ORDER);

		int read = 0;
		int written = 0;
		String filespathString = groundThruthXMLFile;//"/home/renato/Documents/LATEX/Temporally_aware_Named_Entity_Linking/IITB_crawledDocs/CSAW_Annotations.xml";

		File filespath = new File(filespathString);
		File[] listOfFiles = filespath.listFiles();
		if (!filespath.isDirectory()) {
			listOfFiles = new File[1];
			listOfFiles[0] = filespath;
		} else {
			listOfFiles = filespath.listFiles();
		}
		for (File fXmlFile : listOfFiles) {
			read++;
			if (!fXmlFile.canRead()) {
				System.out.println("I cannot read " + fXmlFile.getName()+ ". Sorry, going on with the next one !!!");
				continue;
			}

			String ext = FilenameUtils.getExtension(fXmlFile.getAbsolutePath());

			if (ext.equalsIgnoreCase("xml")) {
				String fTemp = fXmlFile.getName();
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				dbf.setValidating(false);
				dbf.setNamespaceAware(true);
				dbf.setFeature("http://xml.org/sax/features/namespaces", false);
				dbf.setFeature("http://xml.org/sax/features/validation", false);
				dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar",	false);
				dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd",false);
				// input
				DocumentBuilder dBuilder = dbf.newDocumentBuilder();
				Document doc = dBuilder.parse(fXmlFile);
				doc.getDocumentElement().normalize();

				NodeList headlineList = doc.getElementsByTagName("annotation");
				int hL = headlineList.getLength();
				if (hL != 0) {
					for (int nodes = 0; nodes < hL; nodes++) {
						Node tempNode = headlineList.item(nodes);
						if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
							Element tmpElmnt = (Element) tempNode;
							String docName = tmpElmnt.getElementsByTagName("docName").item(0).getTextContent();
							String userId = tmpElmnt.getElementsByTagName("userId").item(0).getTextContent();
							String wikiName = tmpElmnt.getElementsByTagName("wikiName").item(0).getTextContent();
							String offset = tmpElmnt.getElementsByTagName("offset").item(0).getTextContent();
							String length = tmpElmnt.getElementsByTagName("length").item(0).getTextContent();
							CSAW_IITB_Annotation ann = new CSAW_IITB_Annotation(docName, userId, wikiName, offset, length);
							ArrayList<CSAW_IITB_Annotation> annotationsList = mapOfAnnotations.get(docName);

							if (annotationsList == null) {
								annotationsList = new ArrayList<CSAW_IITB_Annotation>();
								//annotationsList.add(wikiName);
								ann = new CSAW_IITB_Annotation(docName, userId, wikiName, offset, length);
								annotationsList.add(ann);
							} else {
								ann = new CSAW_IITB_Annotation(docName, userId, wikiName, offset, length);
								annotationsList.add(ann);
							}
							mapOfAnnotations.put(docName, annotationsList);
						}
					}

				}

				//int count = 0;

				//for (Map.Entry<String, ArrayList<CSAW_IITB_Annotation>> entry : mapOfAnnotations.entrySet()) {
				//	count++;
				//	String keyMention1 = entry.getKey();
				//	ArrayList<CSAW_IITB_Annotation> list = mapOfAnnotations.get(keyMention1);
				//	BufferedReader buf = new BufferedReader(new FileReader(new File("/home/renato/Documents/LATEX/Temporally_aware_Named_Entity_Linking/IITB_crawledDocs/"+keyMention1)));
				//	String line;
				//	int i;
				//	int c;
				//	char lineArray[] = null;
				//	StringBuffer textContentbuff = new StringBuffer();
				//	while((line = buf.readLine()) != null){
				//		textContentbuff.append(line);
				//		textContentbuff.append("\n");
				//	}
				//	buf.close();
				//	
				//	String textContent = textContentbuff.toString();
				//	for (CSAW_IITB_Annotation s : list) {
				//		//System.out.println(s.getOffset());
				//		//System.out.println(s.getLength());
				//		int initialPos =Integer.parseInt(s.getOffset());
				///		int length = Integer.parseInt(s.getLength());
				//		int finalPos = initialPos+length;
				//		System.out.println(keyMention1+ "==" +textContent.substring(initialPos,finalPos) +"\t entityLink=="+s.getWikiName());
				//	}

				//}
				//System.out.println(count);

			}

		}
		return mapOfAnnotations;
	}
	
	
	public String returnMention(String string1,String offset,String length) throws IOException{
		String result =   string1.substring(Integer.parseInt(offset), Integer.parseInt(offset)+Integer.parseInt(length+1));
		return result;
	}
}
