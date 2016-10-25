package org.l3s.iitb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.compress.compressors.CompressorException;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.l3s.Prior.PriorOnlyModel;
import org.l3s.ace.AceAnnotation;
import org.l3s.ace.AceDoc;
import org.xml.sax.SAXException;

public class Comparator {

	public Comparator() {

	}

	/**
	 *
	 * @param args
	 * @throws IOException
	 * @throws CompressorException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 *             //args[0] = mentionEntityFile //args[1] = groundthruth XML
	 *             file
	 * @throws ParseException
	 */
	public static void main(String[] args) throws IOException,CompressorException, ParserConfigurationException, SAXException,ParseException {
		Comparator cp = new Comparator();
		// args[0]
		// ="/home/renato/workspace/WikiParsing/resource/2016/mentionEntityLinks_PRIOR_100_top1.bz2",
		// args[1] = titles
		// /home/renato/workspace/WikiParsing/resource/2006/Json //
		// pagesTitles_REDIRECT.json
		// args[2]
		// =/home/renato/Documents/LATEX/Temporally_aware_Named_Entity_Linking/IITB_crawledDocs/CSAW_Annotations.xml;
		// cp.compareToPriorOnly(args[0], args[1], args[2]);
		cp.calculateAccuracy(args[0], args[1]);
		//cp.Accuracy(args[0], args[1]);
	}

	
	
	/**
	 * 
	 * @param mentionEntityFile
	 * @param titlesRedirection
	 * @throws IOException
	 * @throws CompressorException
	 * @throws ParseException
	 */
	public void Accuracy(String mentionEntityFile,String titlesRedirection) throws IOException, CompressorException, ParseException{
		int numAnnotation = 0;
		int NME = 0;
		int truePositive = 0;
		int trueNegative = 0;
		int numMention = 0;
		boolean match = false;
		
		PriorOnlyModel POM  = new PriorOnlyModel();
		TreeMap<String, String> priorOnlyModelMap = POM.loaderPriorOnly(mentionEntityFile); //
		//Set<String> keys = priorOnlyModelMap.keySet();
		//TreeMap<String,JSONArray> titlesRedirectionsMap = POM.loadTitlesRedirectionsMap(titlesRedirection);
		BufferedReader bffReader = new BufferedReader(new FileReader("./resource/GT_IITB.txt"));
		String inLine = null;
		while ((inLine = bffReader.readLine()) != null) {
			numMention++;
			String[] temp = inLine.split("\t");
			String mentionGT = temp[0];
			
			if(temp.length > 1){
				String entityGT = temp[1];
				numAnnotation++;
				//System.out.println(numAnnotation+" : "+mentionGT);
				ArrayList<String> variations = PrintCombinations(mentionGT);
				int listSize = variations.size();
				int cont = 0;
				
				for(String str : variations){
					cont++;

					String priorOnlyEntityLink = priorOnlyModelMap.get(str);  // this is a perfect String match
					//String priorOnlyEntityLink = priorOnlyModelMap.get(mentionGT);  // this is a perfect String match
					
					if(priorOnlyEntityLink!=null){
						
					if(( entityGT.equalsIgnoreCase(priorOnlyEntityLink))){
						variations = null;
						truePositive++;
						//System.out.println(numAnnotation+" : "+mentionGT + " + ");
						break;
						//continue;
					}else{
					//trueNegative++;
					//System.out.println(mentionGT+":"+entityGT+" != "+priorOnlyEntityLink);

						if(cont == listSize){
							System.out.println(numAnnotation+" : "+mentionGT + " - ");
						}
						
					/*
					JSONArray redirectedEntityArray = titlesRedirectionsMap.get(mentionGT);
					if(redirectedEntityArray!=null){
						for(int i=0; i< redirectedEntityArray.size(); i++){
							String redirectedEntity  = (String) redirectedEntityArray.get(i);
							
							String priorFromRed = priorOnlyModelMap.get(redirectedEntity);
							if(priorFromRed!=null){
								if(priorFromRed.equalsIgnoreCase(entityGT)){
									//System.out.println(mentionGT+":"+entityGT+" >>> "+redirectedEntity+":"+priorFromRed);
									truePositive++;
									match=true;
									break;
								}
							
							
							
							if(redirectedEntity.equalsIgnoreCase(entityGT)){
								truePositive++;
								match=true;
								break;
							}else{
								match=false;
							}
							}
						}
					}else{
						match=false;
					}
					if(match){
							continue;
					}else{
						trueNegative++;
					}
					
					

					for(String strVariation : keys){
						if(strVariation.equalsIgnoreCase(mentionGT)){
							String priorOnlyEntityLinkFromstrVariation = priorOnlyModelMap.get(strVariation);
							if(priorOnlyEntityLinkFromstrVariation != null){

								if(( entityGT.equalsIgnoreCase(priorOnlyEntityLinkFromstrVariation))){
									truePositive++;
									//System.out.println(mentionGT+":"+entityGT+" >>> "+strVariation+":"+priorOnlyEntityLinkFromstrVariation);
									trueNegative--;
									match = true;
									break;
								}else{
								
									//if(mentionGT.equalsIgnoreCase(priorOnlyEntityLinkFromstrVariation)){
									//	truePositive++;
			// >>> DANGER			//	trueNegative--;
									//	System.out.println(mentionGT+":"+entityGT+" >>> "+strVariation+":"+priorOnlyEntityLinkFromstrVariation);
									////	match = true;
									//	break;
									//}
	
										//Checking for redirections
										redirectedEntityArray = titlesRedirectionsMap.get(strVariation);
										if(redirectedEntityArray!=null){
											for(int i=0; i< redirectedEntityArray.size(); i++){
											String redirectedEntity  = (String) redirectedEntityArray.get(i);
											if(redirectedEntity.equalsIgnoreCase(entityGT)){
												truePositive++;
												trueNegative--;
												match=true;
												//System.out.println(mentionGT + " : " + entityGT + "\t >>> \t" +strVariation+" : "+redirectedEntity+":"+ priorOnlyEntityLinkFromstrVariation);
												break;
											}else{
												match=false;
												}
											}
											if(match){
												break;
											}
											
										}else{
											match = false;
										}
									}
							}else{
								match = false;
							}
						}
					}
					*/
			}
				}
				}

		}else{
				
			}
		
		
		}
		bffReader.close();
		
		double Acc = 0.0;
		System.out.println(numMention);
		System.out.println(numAnnotation);		
		System.out.println("TP:"+truePositive);
		//System.out.println("TN:"+trueNegative);
		Acc = (double)((double)(truePositive) / (double)(numAnnotation)) ;//System.out.println(numMention);

		//System.out.println("Non Matching entities :"+nomatching);
		//System.out.println("TOTAL:" +(numMention-NME) );
		System.out.println("Accuracy:"+Acc);
	}
	
	/**
	 *
	 * @param text
	 * @return
	 */
	public ArrayList<String> stringVariations(String text) {
		String word = text;
		ArrayList<String> variantsList = new ArrayList<>();
		word = word.toLowerCase();    
		int combinations = 1 << word.length();
		for (int i = 0; i < combinations; i++) {
			char[] result = word.toCharArray();
			for (int j = 0; j < word.length(); j++) {
				if (((i >> j) & 1) == 1 ) {
					result[j] = Character.toUpperCase(word.charAt(j));
		        }   
		     }
		        //System.out.println(new String(result));
		        variantsList.add(new String(result));
		}
		return variantsList;
	}
	
	
	/**
	 * 
	 * @param groundtruthXMLFile
	 * @throws NumberFormatException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	public void dumpAnnotations(String groundtruthXMLFile) throws NumberFormatException, IOException, ParserConfigurationException, SAXException{
		int numFiles = 0;
		int numAnnotation = 0;

		PrintWriter outputFileWriter = new PrintWriter(new File("./resource/GT_IITB.txt"));
		CSAW_IITB iitb = new CSAW_IITB();
		TreeMap<String, ArrayList<CSAW_IITB_Annotation>> mapOfAnnotations = iitb.getAnnotations(groundtruthXMLFile);
		numFiles = mapOfAnnotations.keySet().size();
		
		for (Map.Entry<String, ArrayList<CSAW_IITB_Annotation>> entry : mapOfAnnotations.entrySet()) {
			String docName = entry.getKey();
			ArrayList<CSAW_IITB_Annotation> iitb_annotations_list = mapOfAnnotations.get(docName);
			BufferedReader buf = new BufferedReader(new FileReader(new File("/home/renato/Documents/LATEX/Temporally_aware_Named_Entity_Linking/IITB_crawledDocs/"+ docName)));
			String line;

			StringBuffer textContentbuff = new StringBuffer();
			while ((line = buf.readLine()) != null) {
				textContentbuff.append(line);
				textContentbuff.append("\n");
			}
			buf.close();

			String textContent = textContentbuff.toString();
			for (CSAW_IITB_Annotation s : iitb_annotations_list) {
				numAnnotation++;
				int initialPos = Integer.parseInt(s.getOffset());
				int length = Integer.parseInt(s.getLength());
				int finalPos = initialPos + length;
				String mentionIITB = textContent.substring(initialPos, finalPos).trim();
				String entityIITB = s.getWikiName().trim();
				outputFileWriter.println(mentionIITB + "\t" + entityIITB);
			}
		}
		
		outputFileWriter.flush();
		outputFileWriter.close();
		
		System.out.println("Num of Files :"+numFiles);
		System.out.println("Num of Annotations :"+numAnnotation);
		
		
	}
	
	
	
	
	/**
	 * This method is meant to calculate Accuracy on the IITB dataset for the  prior only model
	 *
	 * @param mentionEntityFile
	 * @param titlesRedirection
	 * @param groundThruthXMLFile
	 * @throws IOException
	 * @throws CompressorException
	 * @throws ParseException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	public void calculateAccuracy(String mentionEntityFile,	String titlesRedirection)throws IOException, CompressorException, ParseException,ParserConfigurationException, SAXException {
		int numAnnotation = 0;
		int NME = 0;
		int truePositive = 0;
		int trueNegative = 0;
		int numMention = 0;
		boolean match = false;
		
		PriorOnlyModel POM  = new PriorOnlyModel();
		TreeMap<String, String> priorOnlyModelMap = POM.loaderPriorOnly(mentionEntityFile); //
		Set<String> keys = priorOnlyModelMap.keySet();
		Map<String,String> titlesRedirectionsMap = POM.loadTitlesRedirectMap(titlesRedirection);
		BufferedReader bffReader = new BufferedReader(new FileReader("./resource/GT_IITB.txt"));
		String inLine = null;
		while ((inLine = bffReader.readLine()) != null) {
			numMention++;
			String[] temp = inLine.split("\t");
			String mentionGT = temp[0];
			
			if(temp.length > 1){
				String entityGT = temp[1];
				numAnnotation++;
				
				ArrayList<String> variations = PrintCombinations(mentionGT);
				int listSize = variations.size();
				int cont = 0;
				for(String str : variations){
					cont++;
				
		
					String priorOnlyEntityLink = priorOnlyModelMap.get(str);  // this is a perfect String match
				
					if(priorOnlyEntityLink!=null){
						
						if(( entityGT.equalsIgnoreCase(priorOnlyEntityLink))){																		
							variations = null;
							truePositive++;
							break;
						}else{
							//it is not a perfect string match. lets check redirections
							String redirectedEntity= titlesRedirectionsMap.get(str);
							if(redirectedEntity!=null){
								String priorFromRed = priorOnlyModelMap.get(redirectedEntity);
								if(priorFromRed!=null){
									if(priorFromRed.equalsIgnoreCase(entityGT)){
										truePositive++;
										match=true;
										break;
									}
									
									
									if(redirectedEntity.equalsIgnoreCase(entityGT)){
										truePositive++;
										match=true;
										break;
									}else{
										match=false;
									}
								}
							}else{
								match=false;
								}
							if(match){
								continue;
							}else{
								trueNegative++;
							}
						}
					}
				}
			}
		}// end of while loop
		bffReader.close();
		double Acc = 0.0;
		Acc = (double)((double)(truePositive) / (double)(numAnnotation)) ;//System.out.println(numMention);
		System.out.println("TOTAL:" +(numMention) );
		System.out.println("Valid Ann:" +(numAnnotation) );
		System.out.println("TP:" + truePositive);
		System.out.println("Acc : " + Acc);
		
						/***********************************************************************************************
						 * Kullkarni if(priorOnlyEntityLink!=null){
						 * 
						 * if(priorOnlyEntityLink.equalsIgnoreCase(entityIITB)){
						 * //A ----> A : algorithm picks right label
						 * truePositive++;
						 * 
						 * }else{ // Checking for redirections String
						 * redirectedEntity = titlesRedMap.get(entityIITB); if
						 * ((redirectedEntity != null) &&
						 * (redirectedEntity.equalsIgnoreCase
						 * (priorOnlyEntityLink))) { truePositive++; } else {
						 * //A --/--> A : algorithm picks wrong label
						 * trueNegative++; } } }else{ //A --/--> NA : algorithm
						 * picks NA (No attachement) numNA++; }
						 ***********************************************************************************************/
				// System.out.println(truePositive);
				// System.out.println(trueNegative);
				// Acc += (double) truePositive / ( (double) truePositive +
				// (double) trueNegative );
				// MacroP += (double) truePositive / ( (double) truePositive +
				// (double) trueNegative + (double) numNA) ;
				// MacroR += (double) truePositive/ ( (double) truePositive +
				// (double) trueNegative ) ;
				// System.out.println(" ##################################  ");
				// System.out.println( "Precision : "+(double) truePositive / (
				// (double) truePositive + (double) trueNegative + (double)
				// numNA) );
				// System.out.println( "Recall : " + (double) truePositive/ (
				// (double) truePositive + (double) trueNegative ));
	
	}

	/**
	 * This method is calculating the precision/recall on a per file basis. It
	 * will be useful when I want to calculate the MACRO Precision and MACRO
	 * Recall.
	 *
	 * @param args
	 * @throws CompressorException
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 * @throws ParseException
	 */
	public void compareToPriorOnly(String mentionEntityFile,
			String titlesRedirection, String groundThruthXMLFile)
			throws IOException, CompressorException,
			ParserConfigurationException, SAXException, ParseException {

		PrintWriter Pwriter = new PrintWriter(
				"/home/renato/workspace/WikiParsing/resource/2016/results_IITB_priorOnly_2016.txt");
		PriorOnlyModel POM = new PriorOnlyModel();
		TreeMap<String, String> priorOnlyModelMap = POM
				.loaderPriorOnly(mentionEntityFile); // /home/renato/workspace/WikiParsing/resource/2006/mentionEntityLinks_PRIOR_100_top1.bz2

		// need to load the titles redirection JSON file
		Map<String, String> titlesRedMap = POM
				.loadTitlesRedirectMap(titlesRedirection);

		CSAW_IITB iitb = new CSAW_IITB();
		TreeMap<String, ArrayList<CSAW_IITB_Annotation>> mapOfAnnotations = iitb
				.getAnnotations(groundThruthXMLFile);
		int numDocs = 0;
		for (Map.Entry<String, ArrayList<CSAW_IITB_Annotation>> entry : mapOfAnnotations
				.entrySet()) {
			numDocs++;

			int count = 0;
			String docName = entry.getKey();
			ArrayList<CSAW_IITB_Annotation> iitb_annotations_list = mapOfAnnotations
					.get(docName);
			BufferedReader buf = new BufferedReader(
					new FileReader(
							new File(
									"/home/renato/Documents/LATEX/Temporally_aware_Named_Entity_Linking/IITB_crawledDocs/"
											+ docName)));
			System.out.println(docName);
			String line;
			StringBuffer textContentbuff = new StringBuffer();
			while ((line = buf.readLine()) != null) {
				textContentbuff.append(line);
				textContentbuff.append("\n");
			}
			buf.close();
			//
			int returnedItems = 0;
			double truePositive = 0.0;
			double trueNegative = 0.0;
			double falsePositive = 0.0;
			double falseNegative = 0.0;
			String textContent = textContentbuff.toString();
			for (CSAW_IITB_Annotation s : iitb_annotations_list) {
				int initialPos = Integer.parseInt(s.getOffset());
				int length = Integer.parseInt(s.getLength());
				int finalPos = initialPos + length;
				String mentionIITB = textContent
						.substring(initialPos, finalPos).trim();
				String entityIITB = s.getWikiName().trim();
				String priorOnlyEntityLink = priorOnlyModelMap.get(mentionIITB
						.trim());

				if (priorOnlyEntityLink != null) {
					returnedItems++;
					if (entityIITB.equalsIgnoreCase(priorOnlyEntityLink.trim())) {
						truePositive++;
						continue;
					} else {
						// Checking for redirections
						String redirectedEntity = titlesRedMap.get(entityIITB);
						if ((redirectedEntity != null)
								&& (redirectedEntity
										.equalsIgnoreCase(priorOnlyEntityLink))) {
							truePositive++;
							continue;
						} else {
							trueNegative++;
						}
					}

				} else {
				}
				count++;
			}
			double precision = truePositive / (double) returnedItems;
			double recall = truePositive / (double) count;
			double F_measure = (double) 2
					* ((precision * recall) / (precision + recall));
			Pwriter.println("P:" + precision + "\tR:" + recall + "\tF:"
					+ F_measure);
			Pwriter.flush();
		}
		Pwriter.close();
	}

	public void DecrementBitVector(BitSet bv, int numberOfBits) {
	    int currentBit = numberOfBits - 1;          
	    while(currentBit >= 0) {
	        bv.flip(currentBit);

	        // If the bit became a 0 when we flipped it, then we're done. 
	        // Otherwise we have to continue flipping bits
	        if(!bv.get(currentBit))
	            break;
	        currentBit--;
	    }
	}
	
	public ArrayList<String> PrintCombinations(String input) {
		char[] currentCombo = input.toCharArray();
		ArrayList<String> variantsList = new ArrayList<>();
		if(currentCombo.length <= 25){
		
		// Create a bit vector the same length as the input, and set all of the
		// bits to 1
		BitSet bv = new BitSet(input.length());
		bv.set(0, currentCombo.length);

		// While the bit vector still has some bits set
		while (!bv.isEmpty()) {
			// Loop through the array of characters and set each one to
			// uppercase or lowercase,
			// depending on whether its corresponding bit is set
			for (int i = 0; i < currentCombo.length; ++i) {
				if (bv.get(i)) // If the bit is set
					currentCombo[i] = Character.toUpperCase(currentCombo[i]);
				else
					currentCombo[i] = Character.toLowerCase(currentCombo[i]);
			}
			// Print the current combination
			//System.out.println(currentCombo);
			variantsList.add(new String(currentCombo));
			// Decrement the bit vector
			DecrementBitVector(bv, currentCombo.length);
		}
		// Now the bit vector contains all zeroes, which corresponds to all of the letters being lowercase.
		// Simply print the input as lowercase for the final combination
		//System.out.println(input.toLowerCase());
		variantsList.add(new String(input.toLowerCase()));
		}else{
			variantsList.add(new String(input));
			return variantsList;
		}
		return variantsList;
	}
	
}
