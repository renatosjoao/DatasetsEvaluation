package org.l3s.experiments;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.l3s.conll.AIDA_YAGO2_annotations_Parser;
import org.l3s.conll.ConllAnnotation;
import org.l3s.conll.ConllDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class CoNLLParser {
/**
	
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, ParseException, CompressorException{
		//parseAIDAJson("/home/renato/annotations_AIDA/CONLL/json/");
		HashMap<String,String> priorOnlyMap = new HashMap<String, String>();
		priorOnlyMap = CoNLLParser.loaderPriorOnly(args[0]);
		//Groundthruth		
		AIDA_YAGO2_annotations_Parser groundthruth = new AIDA_YAGO2_annotations_Parser();
		PrintWriter Pwriter = new PrintWriter("./resource/results_PRIOR_only_Conll", "UTF-8");
		String groundThruthInputFile = "/home/renato/Documents/LATEX/Temporally_aware_Named_Entity_Linking/CONLL/aida-yago2-dataset/aida-yago2-dataset/AIDA-YAGO2-annotations.tsv";
		LinkedList<ConllDocument> groundthruthList = groundthruth.parse(groundThruthInputFile);
		String ConLLfiles = "./resource/listCONLL";
		BufferedReader bf = new BufferedReader(new FileReader(ConLLfiles));
		String line;
		int count = 0 ;
		while((line = bf.readLine()) != null){
			//String filepathString = "/home/renato/annotations_DBPedia/CONLL/"+line;
			//LinkedList<String> annotations = parseDoc_annotated_by_Spotlight(filepathString);
			LinkedList<ConllAnnotation> groundDocAnnotations = groundthruthList.get(count).getListOfConllAnnotation();
			//int annotationsSize = annotations.size();
			int groundThruthSize = groundDocAnnotations.size();
			count++;
			
			double truePositive = 0.0;
			//double trueNegative = 0.0;
			//double falsePositive = 0.0;
			//double falseNegative = 0.0;
			
			for(ConllAnnotation annotationFromGT : groundDocAnnotations){
			//	for(int j=0; j< groundDocAnnotations.size(); j++ ){
			//		String entity2 = groundDocAnnotations.get(j).getEntity().replaceAll("_"," ");
				String entityFromGT = annotationFromGT.getEntity();
				String linkFromGT = annotationFromGT.getLink();
				
				String entityFromPriorMap = priorOnlyMap.get(entityFromGT);
				if(entityFromPriorMap.equalsIgnoreCase(entityFromGT)){
						truePositive++;
			//			groundDocAnnotations.remove(j);
			//			break;
					}
			//	}
			}
			//falsePositive = annotationsSize - truePositive;
			//falseNegative =  groundThruthSize - truePositive;
			//trueNegative = annotations.size() - truePositive;
			
			//double precision = (double)truePositive / annotationsSize;
			//double recall = (double) truePositive / groundThruthSize;
			//double F_measure = (double)2*((precision*recall)/(precision+recall));
			//double accuracy = (truePositive + trueNegative)/(truePositive + trueNegative + falsePositive + Math.abs(falseNegative));
			//Pwriter.println(count + ":" +line+"\tP :"+precision+"\tR :"+recall+"\tF :"+F_measure+"\tAcc :"+accuracy);
			//System.out.println((count + ":" +line+"\tP :"+precision+"\tR :"+recall+"\tF :"+F_measure+"\tAcc :"+accuracy));
		
		}
		
		
		
		System.out.println();
	}
	
	
	/**
	 *  This function calculates Precision, Recall, Accuracy and F-measure for the ConLL-YAGO dataset annotated with TAgME and compare against the
	 *  groundthruth (AIDA-YAGO2-annotations.tsv).
	 *  
	 * @param args
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 
	public static void executeExperiment_TAGME_CONLL(String[] args) throws IOException, ParserConfigurationException, SAXException{
		PrintWriter Pwriter = new PrintWriter("./resource/results_PRFA_TagME_Conll", "UTF-8");
		
		//Groundthruth		
		AIDA_YAGO2_annotations_Parser groundthruth = new AIDA_YAGO2_annotations_Parser();
		//"/home/renato/Documents/LATEX/Temporally aware Named Entity Linking/CONLL/aida-yago2-dataset/aida-yago2-dataset/AIDA-YAGO2-annotations.tsv");"

		String inputFile = "/home/renato/Documents/LATEX/Temporally aware Named Entity Linking/CONLL/aida-yago2-dataset/aida-yago2-dataset/AIDA-YAGO2-annotations.tsv";
		LinkedList<ConllDocument> groundthruthList = groundthruth.parse(inputFile);
		
		
		String filespathString = "/home/renato/annotations_TAGME/CONLL/epsilon_05/";
		//I am reading the listCONLL file to make comparisons in the same order as the annotations that come from the groundthruth
		String files = "./resource/listCONLL";
		BufferedReader bf = new BufferedReader(new FileReader(files));
		String line;
		int count = 0 ;
		
		while((line = bf.readLine()) != null){
			LinkedList<String> annotations = parseDoc(filespathString+line+".xml");
			LinkedList<ConllAnnotation> groundDocAnnotations = groundthruthList.get(count).getListOfConllAnnotation();
			
			int annotationsSize = annotations.size();
			int groundThruthSize = groundDocAnnotations.size();
			count++;
			
			
			
			double truePositive = 0.0;
			double trueNegative = 0.0;
			double falsePositive = 0.0;
			double falseNegative = 0.0;
			
			for(String annotationTagME : annotations){
				
				for(int j=0; j< groundDocAnnotations.size(); j++ ){
					String entity2 = groundDocAnnotations.get(j).getEntity().replaceAll("_"," ");
					if(annotationTagME.equalsIgnoreCase(entity2)){
						truePositive++;
						groundDocAnnotations.remove(j);
						//ConllAnnotation obj = groundDocAnnotations.get(j);
						//groundDocAnnotations.remove(obj);
						//;
						break;
					}
				//	System.out.println(groundDocAnnotations.get(j).getEntity());
				}
				//falsePositive++;
			}
			falsePositive = annotationsSize - truePositive;
			falseNegative =  groundThruthSize - truePositive;
			
			trueNegative = annotations.size() - truePositive;
			
			
			double precision = (double)truePositive / annotationsSize;
			double recall = (double) truePositive / groundThruthSize;
			double F_measure = (double)2*((precision*recall)/(precision+recall));
			double accuracy = (truePositive + trueNegative)/(truePositive + trueNegative + falsePositive + Math.abs(falseNegative));
			Pwriter.println(line+"\tP :"+precision+"\tR :"+recall+"\tF :"+F_measure+"\tAcc :"+accuracy);
			System.out.println((line+"\tP :"+precision+"\tR :"+recall+"\tF :"+F_measure+"\tAcc :"+accuracy));
			
		}
		
		Pwriter.flush();
		Pwriter.close();
		
	}
	
	
	/**
	 * 
	 * 	This function is meant to parse ConLL annotated (by TAGME) files and it will return a list of annotations.
	 * 
	 * @param fXmlFile
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static LinkedList<String> parseDoc(String fXmlFile) throws ParserConfigurationException, SAXException, IOException{
	    //String fXmlFile = "/home/renato/annotations_TAGME/CONLL/epsilon_05/241588newsML.xml.xml"; 
		LinkedList<String> listAnnotations = new LinkedList<String>();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setValidating(false);
		dbf.setNamespaceAware(true);
		dbf.setFeature("http://xml.org/sax/features/namespaces", false);
		dbf.setFeature("http://xml.org/sax/features/validation", false);
		dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar",false);
		dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd",false);
		// input
		DocumentBuilder dBuilder = dbf.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
		doc.getDocumentElement().normalize();
		//System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
		NodeList nList = doc.getElementsByTagName("annotation");
		//System.out.println("----------------------------");
        for(int temp=0; temp < nList.getLength(); temp++) {
        	Node nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
               Element eElement = (Element) nNode;
               String title =  eElement.getElementsByTagName("title").item(0).getTextContent();
               String rho = eElement.getElementsByTagName("rho").item(0).getTextContent();
               if((Double.parseDouble(rho)>=0.1) && (Double.parseDouble(rho)<=0.3) ){
               //if((Double.parseDouble(rho)>=0.25) ){
            	   listAnnotations.add(title);//System.out.println("ID : "   + eElement.getElementsByTagName("id").item(0).getTextContent());
               }else{
            	   continue;
               }
         }
        }
		return listAnnotations;
	}
	
	
	/**
	 * 	This function is meant to parse ConLL annotated (by Spotlight) files and it will return a list of annotations.
	 * @param fXmlFile
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static LinkedList<String> parseDoc_annotated_by_Spotlight(String fXmlFile) throws ParserConfigurationException, SAXException, IOException{
		LinkedList<String> listAnnotations = new LinkedList<String>();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setValidating(false);
		dbf.setNamespaceAware(true);
		dbf.setFeature("http://xml.org/sax/features/namespaces", false);
		dbf.setFeature("http://xml.org/sax/features/validation", false);
		dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar",false);
		dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd",false);
		// input
		DocumentBuilder dBuilder = dbf.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
		doc.getDocumentElement().normalize();
		//System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
		NodeList nList = doc.getElementsByTagName("Resource");
        for(int temp=0; temp < nList.getLength(); temp++) {
        	Node nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
               Element eElement = (Element) nNode;
               String URI = eElement.getAttribute("URI");
               String[] segments = URI.split("/");
               String link = segments[segments.length-1];
               //String surfaceForm =  eElement.getAttribute("surfaceForm");
               listAnnotations.add(link.replaceAll("_"," "));
         }
        }
		return listAnnotations;
	}

	
	/**
	 * This function parses the JSON files annotated by AIDA and creates a hashmap with the name of the document (id) and the list of annotations 
	 * for each document.
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	public static HashMap<String,LinkedList<String>> parseAIDAJson(String file) throws IOException, ParseException{
		//file:///home/renato/annotations_AIDA/CONLL/json/7592newsML.txt.json
		HashMap<String,LinkedList<String>> mapOfEntities = new HashMap<String, LinkedList<String>>();
		int count=0;
		File filespath = new File(file);
		File[] listOfFiles = filespath.listFiles();
		//if(!filespath.isDirectory()){
			//listOfFiles = new File[1];
		//	listOfFiles[0] = filespath;
	//	}else{			
//			listOfFiles = filespath.listFiles();
		//}
		for (File annotatedFile: listOfFiles) {
			BufferedReader bf = new BufferedReader(new FileReader(annotatedFile));
			String line;
			//int count = 0 ;
			JSONParser jparser = new JSONParser();
			LinkedList<String> listOfEntities = new LinkedList<>();
			while((line = bf.readLine()) != null){
				JSONObject jobj = new JSONObject();
				jobj = (JSONObject) jparser.parse(line);
				//String originalFileName = (String) jobj.get("originalFileName");
				//String originalText = (String) jobj.get("originalText");
				//String overallTime = (String) jobj.get("overallTime");
				JSONArray array = (JSONArray)jobj.get("allEntities");
				
				for(int i=0; i<array.size();i++){
					String entity = array.get(i).toString().split("YAGO:")[1].replace("\\u0028","(").
							replace("\\u0029",")").replace("\\u0027","'").replace("\\u00fc","ü").replace("\\u002c",",").
							replace("\\u0163","ţ").replace("\\u00e1s","á").replace("\\u0159","ř").replace("\\u00e9","é").
							replace("\\u00ed","í").replace("\\u00e1","á").replace("\\u2013","-").replace("\\u0107","ć").
							replace("\\u002e",".").replace("\\u00f3","ó").replace("\\u002d","-").replace("\\u00e1","Ž").
							replace("\\u0160","Š").replace("\\u0105","ą").replace("\\u00eb","ë").replace("\\u017d","Ž").
							replace("\\u00e7","ç").replace("\\u00f8","ø").replace("\\u0161","š").replace("\\u0107","ć").
							replace("\\u00f6","ö").replace("\\u010c","Č").replace("\\u00fd","ý").replace("\\u00d6","Ö").
							replace("\\u00c0","À").replace("\\u0026","&").replace("\\u00df","ß").replace("\\u00ea","ê").
							replace("\\u017","ž").replace("\\u011b","ě").replace("\\u00f6","ö").replace("\\u00e3","ã").
							replace("\\u0103","ă").replace("\\u00c1","Á").replace("\\u002f","/").replace("\\u00e4","ä").
							replace("\\u00c5","Å").replace("\\u0142","ł").replace("\\u0117","ė").replace("\\u00ff","ÿ").
							replace("\\u00f1","ñ").replace("\\u015f","ş").replace("\\u015e","Ş").replace("\\u0131","ı").
							replace("\\u0131k","Ç").replace("\\u0144","ń").replace("\\u0119","ę").replace("\\u00c9","É").
							replace("\\u0111","đ").replace("\\u00e2","â").replace("\\u010d","č").replace("\\u015a","Ś").
							replace("\\u0141","Ł").replace("\\u00e8","è").replace("\\u00c9","É").replace("\\u00e5","å").
							replace("\\u014d","ō").replace("\\u00e6","æ").replace("\\u00d3","Ó").replace("\\u00da","Ú").
							replace("\\u0151","ő").replace("\\u0148","ň").replace("\\u00fa","ú").replace("\\u00ee","î").
							replace("\\u015b","ś").replace("\\u00c7","Ç").replace("\\u00f4","ô").replace("\\u013d","Ľ").
							replace("\\u013e","ľ").replace("\\u011f","ğ").replace("\\u00e0","à").replace("\\u00dc","Ü").
							replace("\\u0021","!").replace("_"," ");
					listOfEntities.add(entity);
				}
			}
			bf.close();
			String[] segments = annotatedFile.getAbsolutePath().split("/");
            String fileName = segments[segments.length-1].substring(0, segments[segments.length-1].lastIndexOf('.'));
            fileName = fileName.substring(0,fileName.lastIndexOf('.'));
            System.out.println(count + " = " +fileName);
            count++;
			mapOfEntities.put(fileName, listOfEntities);
		}
		
		return mapOfEntities;

	}
	
	
	/**
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException
	 * @throws CompressorException
	 */
	public static HashMap<String,String> loaderPriorOnly(String filePath) throws IOException, CompressorException{
		BufferedReader buffReader1 = getBufferedReaderForCompressedFile(filePath);
		HashMap<String,String> map = new HashMap<String, String>();
		String line ;
		while((line = buffReader1.readLine()) != null){
			String[] elem = line.split(" ;-; ");
			String currentMention = elem[0];
			String currentEntity = elem[1];
			String currentPrior = elem[2];
			map.put(currentMention,currentEntity);
		}
		return map;
	}

	/**
	 * @param fileIn
	 * @return
	 * @throws FileNotFoundException
	 * @throws CompressorException
	 */
	public static BufferedReader getBufferedReaderForCompressedFile(String fileIn)
			throws FileNotFoundException, CompressorException {
		FileInputStream fin = new FileInputStream(fileIn);
		BufferedInputStream bis = new BufferedInputStream(fin);
		CompressorInputStream input = new CompressorStreamFactory().createCompressorInputStream(bis);
		BufferedReader br2 = new BufferedReader(new InputStreamReader(input));
		return br2;
	}

}
