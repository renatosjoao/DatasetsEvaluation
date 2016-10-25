package org.l3s.experiments;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.compress.compressors.CompressorException;
import org.json.simple.parser.ParseException;
import org.l3s.Prior.PriorOnlyModel;
import org.l3s.ace.AceAnnotation;
import org.l3s.ace.AceDoc;
import org.l3s.aquaint.AquaintDOC;
import org.l3s.conll.AIDAYAGOAnnotation;
import org.l3s.conll.AIDA_YAGO2_annotations_Parser;
import org.l3s.conll.ConllDocument;
import org.l3s.iitb.CSAW_IITB;
import org.l3s.iitb.CSAW_IITB_Annotation;
import org.l3s.msnbc.MsnbcDOC;
import org.xml.sax.SAXException;

public class WIKIMINER_Annotations {

	private static Pattern annotationPattern = Pattern.compile("\\[\\[(.*?)\\]\\]",	Pattern.MULTILINE);

	// /home/renato/annotations_Wikiminer/2006/ACE2004/20000715_AFP_ARB.0072.eng.ann
	// ACE2004 AQUAINT CONLL IITB MSNBC

	public static void main(String[] args) throws IOException, CompressorException, ParseException, ParserConfigurationException, SAXException {
		//WIKIMINER_Annotations.evaluateAnnotationsACE(args[0], args[1], args[2]);
		//WIKIMINER_Annotations.evaluateAnnotationsIITB(args[0], args[1], args[2]);
		//WIKIMINER_Annotations.evaluateAnnotationsAquaint(args[0], args[1], args[2]);
		//WIKIMINER_Annotations.evaluateAnnotationsMSNBC(args[0], args[1], args[2]);
		WIKIMINER_Annotations.evaluateAnnotationsCONLL(args[0], args[1], args[2]);
		System.gc();

	}

	
	
	/**
	 * 
	 * @param annotatedFilesDir
	 * @param groundThruthMSNBC
	 * @param titlesRedirection
	 * @throws ParseException
	 * @throws IOException
	 */
	public static void evaluateAnnotationsMSNBC(String annotatedFilesDir, String groundThruthMSNBC, String titlesRedirection) throws ParseException, IOException{
		int numDocs = 0;
		PrintWriter Pwriter = new PrintWriter("/home/renato/workspace/WikiParsing/resource/results_WIKIMINER_MSNBC_2016.txt");
		PriorOnlyModel POM = new PriorOnlyModel();
		Map<String, String> titlesRedMap = POM.loadTitlesRedirectMap(titlesRedirection);	// need to load the titles redirection JSON file
		MsnbcDOC msnbcDOC = new MsnbcDOC();
		LinkedList<MsnbcDOC>  docs = msnbcDOC.parseMSNBC_GT(groundThruthMSNBC);
	
		
		for(int i=0;i<docs.size();i++){
			numDocs++;
			double truePositive = 0.0;
			
			MsnbcDOC dd =  docs.get(i);
			LinkedList<TreeMap<String,String>> MsnbcAnnotations_fromGT = dd.MSNBCAnnotations;
			String docName = dd.getDocTitle();
			// Annotations from Wikiminer //
			BufferedReader buf = new BufferedReader(new FileReader(annotatedFilesDir+"/" +docName+".ann"));
			LinkedList<Annotation> listOfAnnotationsFromWikiminer = new LinkedList<Annotation>();
			String line;
			StringBuffer textContentbuff = new StringBuffer();
			while ((line = buf.readLine()) != null) {
				textContentbuff.append(line);
				textContentbuff.append("\n");
			}
			buf.close();
			String textContent = textContentbuff.toString();
			Matcher mRedirect = annotationPattern.matcher(textContent);
			while (mRedirect.find()) {
				String mention = null;
				String entityLink = null;
				String[] temp = mRedirect.group(1).split("\\|");
				if (temp == null || temp.length == 0) {
					continue;
				} else {
						if (temp.length > 1) {
							mention = temp[1].trim();
							entityLink = temp[0].trim();
						} else {
							mention = temp[0].trim();
							entityLink = temp[0].trim();
						}
					}
				Annotation ann = new Annotation(mention, entityLink); 
				listOfAnnotationsFromWikiminer.add(ann);
			}// END of  while (mRedirect.find()) {
			// End of Annotations from Wikiminer //
			
			
			int returneditems = MsnbcAnnotations_fromGT.size();
			int mentionsMatches = 0;
			//Lets do comparisons with ground truth
			for(TreeMap<String,String> elem : MsnbcAnnotations_fromGT){
		//		//annotations from 1 doc
		//		count++;
				Iterator<?> it = elem.entrySet().iterator();
				while (it.hasNext()) {
					@SuppressWarnings("rawtypes")
					Map.Entry pair = (Map.Entry) it.next();
					String msnbcMention =  (String) pair.getKey();
					String msnbcEntity = (String) pair.getValue();
					
					
					for(Annotation annWIKI : listOfAnnotationsFromWikiminer){
						
						String mentionWIKI = annWIKI.getMention().trim();
						String entityWIKI = annWIKI.getEntity().trim();

						if(!msnbcMention.equalsIgnoreCase(mentionWIKI)){
							continue;
						}else{
								//matched the mention
							mentionsMatches++;
							if(msnbcEntity.equalsIgnoreCase(entityWIKI)){ //matched the entity linked
								truePositive++;
								//listOfAnnotationsFromWikiminer.remove(annWIKI);
								break;
							}else{//did not match the entity linked
								//if(mentionGT.equalsIgnoreCase(entityWIKI)){
								//	truePositive++;
								//	break;
								//}
								//check redirection
								String redirectedEntity = titlesRedMap.get(msnbcEntity);
								if ((redirectedEntity != null) && (redirectedEntity.equalsIgnoreCase(entityWIKI))) { 
									truePositive++;
									break;
								}
								if ((redirectedEntity != null) && (redirectedEntity.equalsIgnoreCase(msnbcEntity))) { 
									truePositive++;
									break;
								}
								redirectedEntity = titlesRedMap.get(entityWIKI);
								if ((redirectedEntity != null) && (redirectedEntity.equalsIgnoreCase(entityWIKI))) { 
									truePositive++;
									break;
								}
								if ((redirectedEntity != null) && (redirectedEntity.equalsIgnoreCase(msnbcEntity))) { 
									truePositive++;
									//System.out.println(mentionGT + "\t" + entityGT +"\t "+ entityWIKI);
									break;
								}else{
									//System.out.println(redirectedEntity);
									//System.out.println(docName+"\t"+ msnbcMention + "\t" + msnbcEntity +"\t "+ entityWIKI);
									}
							}
							//returneditems--;
							//System.out.println(docName+"\t"+"nonmatching :"+msnbcMention);
						}
					}		
					
				}
				
				
				}
			double precision = truePositive / (double)mentionsMatches;
			double recall = truePositive / (double)MsnbcAnnotations_fromGT.size();;
			double F_measure = (double)2*((precision*recall)/(precision+recall));
			//System.out.println(c);
			Pwriter.println(docName+"\t"+"P:"+precision+"\tR:"+recall+"\tF:"+F_measure);
			Pwriter.flush();
				
			}
		Pwriter.close();
		
	}
	
	
	
	/**
	 * 
	 * @param annotatedFilesDir
	 * @param groundThruthXMLFile
	 * @param titlesRedirection
	 * @throws ParseException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	public static void evaluateAnnotationsIITB(String annotatedFilesDir, String groundThruthXMLFile, String titlesRedirection) throws ParseException, IOException, ParserConfigurationException, SAXException{
		int numDocs = 0;
		PrintWriter Pwriter = new PrintWriter("/home/renato/workspace/WikiParsing/resource/results_WIKIMINER_IITB_2016.txt");

		PriorOnlyModel POM = new PriorOnlyModel();
		Map<String, String> titlesRedMap = POM.loadTitlesRedirectMap(titlesRedirection);	// need to load the titles redirection JSON file

		CSAW_IITB iitb = new CSAW_IITB();
		TreeMap<String, ArrayList<CSAW_IITB_Annotation>> mapOfAnnotations = iitb.getAnnotations(groundThruthXMLFile);
	
		for (Map.Entry<String, ArrayList<CSAW_IITB_Annotation>> entry : mapOfAnnotations.entrySet()) {
			numDocs++;
			
			String docName = entry.getKey();
			ArrayList<CSAW_IITB_Annotation> iitb_annotations_list_from_GT = mapOfAnnotations.get(docName);
			//BufferedReader buf = new BufferedReader(new FileReader(new File("/home/renato/Documents/LATEX/Temporally_aware_Named_Entity_Linking/IITB_crawledDocs/"	+ docName)));
						
			//##########################################//
			//This is only meant to find out the mention//
			BufferedReader bff = new BufferedReader(new FileReader(new File("/home/renato/Documents/LATEX/Temporally_aware_Named_Entity_Linking/IITB_crawledDocs/"	+ docName)));
			String lineCrawledDoc;
			StringBuffer textbuff = new StringBuffer();
			while ((lineCrawledDoc = bff.readLine()) != null) {
				textbuff.append(lineCrawledDoc);
				textbuff.append("\n");
			}
			bff.close();
			
			String textRaw =textbuff.toString(); 
			//#########################//
			
			
			// Annotations from Wikiminer //
			BufferedReader buf = new BufferedReader(new FileReader(annotatedFilesDir+"/" +docName+".ann"));
			LinkedList<Annotation> listOfAnnotationsFromWikiminer = new LinkedList<Annotation>();
			String line;
			StringBuffer textContentbuff = new StringBuffer();
			while ((line = buf.readLine()) != null) {
				textContentbuff.append(line);
				textContentbuff.append("\n");
			}
			buf.close();
			
			String textContent = textContentbuff.toString();
			
			Matcher mRedirect = annotationPattern.matcher(textContent);
			while (mRedirect.find()) {
				String mention = null;
				String entityLink = null;
				String[] temp = mRedirect.group(1).split("\\|");
				if (temp == null || temp.length == 0) {
					continue;
				} else {
						if (temp.length > 1) {
							mention = temp[1].trim();
							entityLink = temp[0].trim();
						} else {
							mention = temp[0].trim();
							entityLink = temp[0].trim();
						}
					}
				
				Annotation ann = new Annotation(mention, entityLink); 
				listOfAnnotationsFromWikiminer.add(ann);
			}// END of  while (mRedirect.find()) {
			// End of Annotations from Wikiminer //

			
			 double truePositive = 0.0;
			
			for (CSAW_IITB_Annotation s : iitb_annotations_list_from_GT) {
				int initialPos = Integer.parseInt(s.getOffset());
				int length = Integer.parseInt(s.getLength());
				int finalPos = initialPos + length;
				String mentionIITB_fromGT = textRaw.substring(initialPos, finalPos).trim();
				String entityIITB_fromGT = s.getWikiName().trim();
				
				if(entityIITB_fromGT !=""){
					for(Annotation a : listOfAnnotationsFromWikiminer){
						String mentionWikiminer = a.getMention();
						String entityWikiminer = a.getEntity();
						
						if(entityIITB_fromGT.equalsIgnoreCase(entityWikiminer)){
							truePositive++;
							break;
						}else{
							//did not match the entity linked
							//if(mentionGT.equalsIgnoreCase(entityWIKI)){
							//	truePositive++;
							//	break;
							//}
							//check redirection
							String redirectedEntity = titlesRedMap.get(entityIITB_fromGT);
							if ((redirectedEntity != null) && (redirectedEntity.equalsIgnoreCase(entityWikiminer))) { 
								truePositive++;
								break;
							}
							if ((redirectedEntity != null) && (redirectedEntity.equalsIgnoreCase(entityIITB_fromGT))) { 
								truePositive++;
								break;
							}
							redirectedEntity = titlesRedMap.get(entityWikiminer);
							if ((redirectedEntity != null) && (redirectedEntity.equalsIgnoreCase(entityWikiminer))) { 
								truePositive++;
								break;
							}
							if ((redirectedEntity != null) && (redirectedEntity.equalsIgnoreCase(entityIITB_fromGT))) { 
								truePositive++;
								//System.out.println(mentionGT + "\t" + entityGT +"\t "+ entityWIKI);
								break;
							}else{
							//	//System.out.println(redirectedEntity);
								//System.out.println(mentionIITB_fromGT + "\t" + entityIITB_fromGT +"\t "+ entityWikiminer);
								}
							}
					}
				}else{
					continue;
				}
				
				
				
			}//for (CSAW_IITB_Annotation s : iitb_annotations_list_from_GT) {
			
			//here calculate P, R, F
			double precision = truePositive / (double) iitb_annotations_list_from_GT.size();
			//	double recall = truePositive / (double) listOfAnnotationsFromGT.size();
			//	double F_measure = (double) 2* ((precision * recall) / (precision + recall));
			System.out.println(precision);
			//	Pwriter.println(f.getName()+"\tP:" + precision + "\tR:" + recall + "\tF:"	+ F_measure);
			Pwriter.println(docName+"\tP:" + precision);
			Pwriter.flush();
				
			
		}// END of for (Map.Entry<String, ArrayList<CSAW_IITB_Annotation>> entry : mapOfAnnotations.entrySet()) {
		Pwriter.close();
		
	}
	
	/***
	 * 
	 * @param annotatedFilesDir
	 * @param groundThruth		AIDA-YAGO2-dataset.tsv
	 * @param titlesRedirection
	 * @throws NumberFormatException
	 * @throws IOException
	 * @throws ParseException
	 */
	public static void evaluateAnnotationsCONLL(String annotatedFilesDir, String groundThruth, String titlesRedirection) throws NumberFormatException, IOException, ParseException{
		PrintWriter Pwriter = new PrintWriter("/home/renato/workspace/WikiParsing/resource/results_WIKIMINER_CONLL_2016.txt");
		PriorOnlyModel POM = new PriorOnlyModel();
		// need to load the titles redirection JSON file
		Map<String, String> titlesRedMap = POM.loadTitlesRedirectMap(titlesRedirection);
		
		AIDA_YAGO2_annotations_Parser p = new AIDA_YAGO2_annotations_Parser();
		LinkedList<ConllDocument> ConllDataSet  = p.parseDataset(groundThruth);

		String ConLLfiles = "./resource/listCONLL";
		LinkedList<String> listOfConllDocs = new LinkedList<String>();
		BufferedReader bf = new BufferedReader(new FileReader(ConLLfiles));
		String line;
		while((line = bf.readLine()) != null){
			listOfConllDocs.add(line);
		}
		bf.close();
		
		for(int i=1162; i < ConllDataSet.size(); i++){ //1163 is the first doc from testb testset b
			ConllDocument CO = ConllDataSet.get(i);
			LinkedList<AIDAYAGOAnnotation> AnnList =  CO.getListOfAnnotation();
			
			String currentFile = listOfConllDocs.get(i);
			currentFile = currentFile.substring(0,currentFile.lastIndexOf("."));
			
			BufferedReader bff = new BufferedReader(new FileReader(annotatedFilesDir+"/" +currentFile+".txt.ann"));
			LinkedList<Annotation> listOfAnnotationsFromWikiminer = new LinkedList<Annotation>();
			StringBuffer inputTxtBuffer = new StringBuffer();
			while ((line = bff.readLine()) != null) {
				inputTxtBuffer.append(line);
				inputTxtBuffer.append("\n");
			}
			bff.close();
			String inputText = inputTxtBuffer.toString();
			Matcher mRedirect = annotationPattern.matcher(inputText);
			while (mRedirect.find()) {
				String mention = null;
				String entityLink = null;
				String[] temp = mRedirect.group(1).split("\\|");
				if (temp == null || temp.length == 0) {
					continue;
				} else {
						if (temp.length > 1) {
							mention = temp[1].trim();
							entityLink = temp[0].trim();
						} else {
							mention = temp[0].trim();
							entityLink = temp[0].trim();
						}
					}
				Annotation ann = new Annotation(mention, entityLink); 
				listOfAnnotationsFromWikiminer.add(ann);
			}// END of  while (mRedirect.find()) {


			 double truePositive = 0.0;
			 for(AIDAYAGOAnnotation annGT : AnnList){
				String mentionGT = annGT.getMention().trim();
				String entityGT = annGT.getEntity().trim();
				
				for(Annotation annWIKI : listOfAnnotationsFromWikiminer){
					String mentionWIKI = annWIKI.getMention().trim();
					String entityWIKI = annWIKI.getEntity().trim();
					
					if(mentionGT.equalsIgnoreCase(mentionWIKI)){  //matched the mention
						if(entityGT.equalsIgnoreCase(entityWIKI)){ //matched the entity linked
							truePositive++;
							listOfAnnotationsFromWikiminer.remove(annWIKI);
							break;
						}else{//did not match the entity linked
							//if(mentionGT.equalsIgnoreCase(entityWIKI)){
							//	truePositive++;
							//	break;
							//}
							//check redirection
							String redirectedEntity = titlesRedMap.get(entityGT);
							if ((redirectedEntity != null) && (redirectedEntity.equalsIgnoreCase(entityWIKI))) { 
								truePositive++;
								break;
							}
							if ((redirectedEntity != null) && (redirectedEntity.equalsIgnoreCase(entityGT))) { 
								truePositive++;
								break;
							}
							redirectedEntity = titlesRedMap.get(entityWIKI);
							if ((redirectedEntity != null) && (redirectedEntity.equalsIgnoreCase(entityWIKI))) { 
								truePositive++;
								break;
							}
							if ((redirectedEntity != null) && (redirectedEntity.equalsIgnoreCase(entityGT))) { 
								truePositive++;
								//System.out.println(mentionGT + "\t" + entityGT +"\t "+ entityWIKI);
								break;
							}else{
								//System.out.println(redirectedEntity);
								System.out.println(mentionGT + "\t\t" + entityGT +"\t "+ entityWIKI);
								}
						}
					}
				}
				
			}
			double precision = truePositive / (double) AnnList.size();
			double recall = truePositive / (double) AnnList.size();
			double F_measure = (double) 2* ((precision * recall) / (precision + recall));
			// System.out.println(count);
			Pwriter.println(currentFile+"\tP:" + precision + "\tR:" + recall + "\tF:"	+ F_measure);
			Pwriter.flush();
			
		}// END of for(int i=1162; i < ConllDataSet.size(); i++){
		Pwriter.close();
	}
	
	
	/***
	 * 
	 * @param annotatedFilesDir
	 * @param groundThruthDir
	 * @param titlesRedirection
	 * @throws IOException
	 * @throws ParseException
	 */
	public static void evaluateAnnotationsAquaint(String annotatedFilesDir, String groundThruthDir, String titlesRedirection) throws IOException, ParseException{
		PriorOnlyModel POM = new PriorOnlyModel(); //need to load the titles redirection JSON file
		Map<String, String> titlesRedMap = POM.loadTitlesRedirectMap(titlesRedirection);
		PrintWriter Pwriter = new PrintWriter("/home/renato/workspace/WikiParsing/resource/results_WIKIMINER_AQUAINT_2016.txt");
//############################ Files from the GT ########################## 		
		int numFiles = 0;
		File filespath = new File(groundThruthDir);
		File[] listOfFiles = filespath.listFiles();		
		for(File f:listOfFiles){
			if(f.getAbsolutePath().endsWith(".htm")){
				numFiles++;
				
				BufferedReader buf = new BufferedReader(new FileReader(f));
				StringBuffer inputTxtBuffer = new StringBuffer();
				String line = null;
				while ((line = buf.readLine()) != null) {
					inputTxtBuffer.append(line);
					inputTxtBuffer.append("\n");
				}
				buf.close();
				String inputText = inputTxtBuffer.toString();
				LinkedList<Annotation> listOfAnnotationsFromGT = new LinkedList<Annotation>();
				
				Matcher mRedirect = annotationPattern.matcher(inputText);
				while (mRedirect.find()) {
					String mention = null;
					String entityLink = null;
					String[] temp = mRedirect.group(1).split("\\|");
					if (temp == null || temp.length == 0) {
						continue;
					} else {
							if (temp.length > 1) {
								if (temp.length > 2) {
									double agreement = Double.parseDouble(temp[2]);
									if(agreement > 0.6){
										mention = temp[1].trim();
										entityLink = temp[0].trim();
									}else{
										continue;
									}
								}else{
									mention = temp[1].trim();
									entityLink = temp[0].trim();
								}
							} else {
								mention = temp[0].trim();
								entityLink = temp[0].trim();
							}
						}
					Annotation ann = new Annotation(mention, entityLink); 
					listOfAnnotationsFromGT.add(ann);//temporary structure to hold annotations from the GT
				}// END of  while (mRedirect.find()) {
			
//#########################################################################################################//
			
			
//############################ Files Annotated with Wikiminer ########################## 		
			String annotatedFileName = annotatedFilesDir+"/"+f.getName()+".ann";
			BufferedReader buff2 = new BufferedReader(new FileReader(annotatedFileName));
			StringBuffer inputTxtBuffer2 = new StringBuffer();
			String line2 = null;
			while ((line2 = buff2.readLine()) != null) {
				inputTxtBuffer2.append(line2);
				inputTxtBuffer2.append("\n");
			}
			buff2.close();
			String inputText2 = inputTxtBuffer2.toString();
			LinkedList<Annotation> listOfAnnotationsFromWikiminer = new LinkedList<Annotation>();
			
			Matcher mRedirect2 = annotationPattern.matcher(inputText2);
			while (mRedirect2.find()) {
				String mention = null;
				String entityLink = null;
				String[] temp = mRedirect2.group(1).split("\\|");
				if (temp == null || temp.length == 0) {
					continue;
				} else {
						if (temp.length > 1) {
							mention = temp[1].trim();
							entityLink = temp[0].trim();
						} else {
							mention = temp[0].trim();
							entityLink = temp[0].trim();
						}
					}
				Annotation ann = new Annotation(mention, entityLink); 
				listOfAnnotationsFromWikiminer.add(ann);//temporary structure to hold annotations from Wikiminer
			}// END of  while (mRedirect.find()) {
//#########################################################################################################//
			
			
			double truePositive = 0.0;
			//double trueNegative = 0.0;
			
			for(Annotation annGT : listOfAnnotationsFromGT){
				String mentionGT = annGT.getMention().trim();
				String entityGT = annGT.getEntity().trim();
				
				for(Annotation annWIKI : listOfAnnotationsFromWikiminer){
					String mentionWIKI = annWIKI.getMention().trim();
					String entityWIKI = annWIKI.getEntity().trim();
					
					if(mentionGT.equalsIgnoreCase(mentionWIKI)){  //matched the mention
						if(entityGT.equalsIgnoreCase(entityWIKI)){ //matched the entity linked
							truePositive++;
							listOfAnnotationsFromWikiminer.remove(annWIKI);
							break;
						}else{//did not match the entity linked
							 
							//check redirection
							String redirectedEntity = titlesRedMap.get(entityGT);
							if ((redirectedEntity != null) && (redirectedEntity.equalsIgnoreCase(entityWIKI))) { 
								truePositive++;
								break;
							}
							if ((redirectedEntity != null) && (redirectedEntity.equalsIgnoreCase(entityGT))) { 
								truePositive++;
								break;
							}
							redirectedEntity = titlesRedMap.get(entityWIKI);
							if ((redirectedEntity != null) && (redirectedEntity.equalsIgnoreCase(entityWIKI))) { 
								truePositive++;
								break;
							}
							if ((redirectedEntity != null) && (redirectedEntity.equalsIgnoreCase(entityGT))) { 
								truePositive++;
								//System.out.println(mentionGT + "\t" + entityGT +"\t "+ entityWIKI);
								break;
							}else{
								//System.out.println(redirectedEntity);
								System.out.println(mentionGT + "\t" + entityGT +"\t "+ entityWIKI);
								}
						}
					}
				}
				
			}
			double precision = truePositive / (double) listOfAnnotationsFromGT.size();
			double recall = truePositive / (double) listOfAnnotationsFromGT.size();
			double F_measure = (double) 2* ((precision * recall) / (precision + recall));
			// System.out.println(count);
			Pwriter.println(f.getName()+"\tP:" + precision + "\tR:" + recall + "\tF:"	+ F_measure);
			Pwriter.flush();
			
			
			} // END of  if(f.getAbsolutePath().endsWith(".htm")){
			
	}
		Pwriter.close();
	}
	
	
	
	
	
	
	/***
	 * 
	 * @param groundThruthFile
	 * @param titlesRedirection
	 * @param filesPathDir
	 * @throws IOException
	 * @throws CompressorException
	 * @throws ParseException
	 */
	public static void evaluateAnnotationsACE(String filesPathDir,String groundThruthFile,String titlesRedirection) throws IOException,
			CompressorException, ParseException {
		
		PrintWriter Pwriter = new PrintWriter("/home/renato/workspace/WikiParsing/resource/results_WIKIMINER_ACE_2016.txt");

		PriorOnlyModel POM = new PriorOnlyModel(); //need to load the titles redirection JSON file
		Map<String, String> titlesRedMap = POM.loadTitlesRedirectMap(titlesRedirection);

		AceDoc ACE_GroundThruth = new AceDoc();
		TreeMap<String, LinkedList<AceAnnotation>> AceDocsMap_GT = ACE_GroundThruth.parseACEDoc_GT(groundThruthFile);
		int numFiles = 0;
		int numFilesFoundInGT = 0;
		//for (Map.Entry<String, LinkedList<AceAnnotation>> entry :AceDocsMap_GT.entrySet()) {
		//	System.out.println(entry.getKey());
		//}
		File filespath = new File(filesPathDir);
		File[] listOfFiles = filespath.listFiles();
		//
 		for (File f : listOfFiles) {
 			numFiles++;
 			LinkedList<AceAnnotation> wikiminer_annotationsList = new LinkedList<AceAnnotation>();
 			
 			String filename = f.getName();
			filename = filename.substring(0, filename.lastIndexOf("."));
			
			LinkedList<AceAnnotation> groundThruth_annotationsList = AceDocsMap_GT.get(filename);
			
			if(groundThruth_annotationsList != null){	
				int numWikiminerAnnotations = 0;

				numFilesFoundInGT++;
				int numAnnotGroundThruth = groundThruth_annotationsList.size();
		
				BufferedReader buf = new BufferedReader(new FileReader(f));
				StringBuffer inputTxtBuffer = new StringBuffer();
				String line = null;
				while ((line = buf.readLine()) != null) { // Text is read to buffer !!!
					inputTxtBuffer.append(line);
					inputTxtBuffer.append("\n");
				}
				buf.close();

				String inputText = inputTxtBuffer.toString();
				//System.out.println(inputText);
				Matcher mRedirect = annotationPattern.matcher(inputText);

				while (mRedirect.find()) {
					String mention = null;
					String entityLink = null;
					numWikiminerAnnotations++;
				String[] temp = mRedirect.group(1).split("\\|");
				if (temp == null || temp.length == 0) {
		//			continue;
				} else {
					if (temp.length > 1) {
						mention = temp[1].trim();
						entityLink = temp[0].trim();
					} else {
						mention = temp[0].trim();
						entityLink = temp[0].trim();
					}
				}
		//			
				AceAnnotation acc = new AceAnnotation(filename, "", mention,"",entityLink,"","","");
				wikiminer_annotationsList.add(acc);
		} // end of matching while
			
			// Now I have a list of annotations for a single file !!!
			// calculate precision,recall,fmeasure for this file.
				
				//System.out.println("File : "+filename + "\t" +"numAnnotation :"+numWikiminerAnnotations);
				
			
			double truePositive = 0.0;
			//double trueNegative = 0.0;
			
			int numAnnotWikiminer = wikiminer_annotationsList.size();
			for(AceAnnotation annGT: groundThruth_annotationsList){
			//lets check the annotations against the ground thruth
				String mentionGT = annGT.getMention();
				String entityGT = annGT.getLink();
				for(AceAnnotation annWiki : wikiminer_annotationsList){	
					String mentionWikiminer = annWiki.getMention();
					String entityWikiminer = annWiki.getLink();					
					if(mentionWikiminer.equalsIgnoreCase(mentionGT)){
						//matched the mention annotated
						//now lets compare the entity linked.
						if(entityWikiminer.equalsIgnoreCase(entityGT)){
							//matched the linked entity as well
							truePositive++;
							//groundThruth_annotationsList.remove
							break;
						}else{
							//check redirection
							String redirectedEntity = titlesRedMap.get(entityGT);
							if ((redirectedEntity != null) && (redirectedEntity.equalsIgnoreCase(entityWikiminer))) { 
								truePositive++;
								break;
							}
							if ((redirectedEntity != null) && (redirectedEntity.equalsIgnoreCase(entityGT))) { 
								truePositive++;
								break;
							}
							redirectedEntity = titlesRedMap.get(entityWikiminer);
							if ((redirectedEntity != null) && (redirectedEntity.equalsIgnoreCase(entityWikiminer))) { 
								truePositive++;
								break;
							}
							if ((redirectedEntity != null) && (redirectedEntity.equalsIgnoreCase(entityGT))) { 
								truePositive++;
								//System.out.println(mentionGT + "\t" + entityGT +"\t "+ entityWIKI);
								break;
							}else{
								//System.out.println(redirectedEntity);
								//System.out.println(mentionGT + "\t" + entityGT +"\t "+ entityWikiminer);
								}
							//trueNegative++;
						}
					}
					
				}
				
			}
				
				//double precision = truePositive / (double)returnedItems;
				double precision = truePositive / (double) numAnnotGroundThruth;
				double recall = truePositive / (double) numAnnotGroundThruth;
				double F_measure = (double)2*((precision*recall)/(precision+recall));
				Pwriter.println(f.getName()+"\tP:"+precision+"\tR:"+recall+"\tF:"+F_measure);
				System.out.println(f.getName()+"\tP:"+precision+"\tR:"+recall+"\tF:"+F_measure);
				Pwriter.flush();
				

			}else{
				//doc not found in the groundthruth
				//System.out.println(filename);
			}
			
 		}
 		Pwriter.close();
		//System.out.println(numFiles);
		//System.out.println(numFilesFoundInGT);
	
	}
}