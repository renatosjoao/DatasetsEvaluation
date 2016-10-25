package org.l3s.conll;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.compress.compressors.CompressorException;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.l3s.Prior.PriorOnlyModel;
import org.xml.sax.SAXException;


public class Comparator {

	
	public static void main(String[] args) throws IOException, ParseException, ParserConfigurationException, SAXException, CompressorException {
		Comparator cp = new Comparator();
		//args[0] ="/home/renato/workspace/WikiParsing/resource/2016/mentionEntityLinks_PRIOR_100_top1.bz2",
		 //args[1] = titles /home/renato/workspace/WikiParsing/resource/2006/Json pagesTitles_REDIRECT.json
		 //args[2] = "/home/renato/Documents/LATEX/Temporally_aware_Named_Entity_Linking/CONLL/AIDA-YAGO2-dataset.tsv";
		//cp.compareToPriorOnly(args[0],args[1],args[2]);
		//cp.calculateAccuracy(args[0],args[1],args[2]);
		//cp.Accuracy(args[0],args[1]);
		cp.calculateAccuracy(args[0],args[1]);
		//cp.dumpAnnotations(args[0]);

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
		
		BufferedReader bffReader = new BufferedReader(new FileReader("./resource/GT_AIDA_CONLL.txt"));
		String inLine = null;
		while ((inLine = bffReader.readLine()) != null) {
			numMention++;
			String[] temp = inLine.split("\t");
			String mentionGT = temp[0];
			String entityGT = temp[1];
			//System.out.println(entityGT);
			if(!entityGT.equalsIgnoreCase("--NME--")){
				numAnnotation++;
				
				ArrayList<String> variations = PrintCombinations(mentionGT);
				
				int listSize = variations.size();
				int cont = 0;
				for(String str : variations){
					cont++;
					String priorOnlyEntityLink = priorOnlyModelMap.get(str);  // this is a perfect String match
					if(priorOnlyEntityLink!=null){
				
				
				//String priorOnlyEntityLink = priorOnlyModelMap.get(mentionGT);  // this is a perfect String match
				if(( entityGT.equalsIgnoreCase(priorOnlyEntityLink))){
					variations = null;
					//System.gc();
					//System.out.println(numAnnotation+" : "+mentionGT + " + ");
					truePositive++;
					//System.out.println(mentionGT+":"+entityGT+" >>> "+priorOnlyEntityLink);
					break;
					//continue;
				}else{
					
					if(cont == listSize){
						//System.out.println(numAnnotation+" : "+mentionGT + " - ");
					}
					
					//trueNegative++;
					//System.out.println(mentionGT+":"+entityGT+" != "+priorOnlyEntityLink);
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
								
							//if(redirectedEntity.equalsIgnoreCase(entityGT) || (redirectedEntity.equalsIgnoreCase(priorOnlyEntityLink)) ){	
							if(redirectedEntity.equalsIgnoreCase(entityGT)){
								//System.out.println(mentionGT+":"+entityGT+" >>> "+redirectedEntity+":"+priorOnlyEntityLink);
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
								/// CHECKED !!!
								if(( entityGT.equalsIgnoreCase(priorOnlyEntityLinkFromstrVariation))){
									truePositive++;
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
				NME++;
			}
		}
		bffReader.close();
		double Acc = 0.0;
		Acc = (double)((double)(truePositive) / (double)(numAnnotation)) ;//System.out.println(numMention);
		
		System.out.println("TOTAL:" +numMention );
		System.out.println("Valid annotations:"+numAnnotation);
		System.out.println("--NME--:"+NME);
		System.out.println("TP:"+truePositive);
		System.out.println("TN:"+trueNegative);
		System.out.println("Accuracy:"+Acc);
	
	}
	
	/**
	 * 
	 * @param datasetFile
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	public void dumpAnnotations(String datasetFile) throws NumberFormatException, IOException{
		int numFiles = 0;
		int numAnnotation = 0;
		int NME = 0;
		PrintWriter outputFileWriter = new PrintWriter(new File("./resource/GT_AIDA_CONLL.txt"));
		
		AIDA_YAGO2_annotations_Parser p = new AIDA_YAGO2_annotations_Parser();
		LinkedList<ConllDocument> ConllDataSet  = p.parseDataset(datasetFile);

		for(int i=1162; i < ConllDataSet.size(); i++){ //1163 is the first doc from testb testset b
			ConllDocument CO = ConllDataSet.get(i);
			LinkedList<AIDAYAGOAnnotation> AnnList =  CO.getListOfAnnotation();
			numFiles++;
			for(AIDAYAGOAnnotation Ann : AnnList){
				numAnnotation++;
				String mentionConLL = Ann.getMention();
				String entityConLL = Ann.getEntity();
				outputFileWriter.println(mentionConLL+"\t"+entityConLL);
				if(entityConLL.equalsIgnoreCase("--NME--")){
					NME++;
				}
			}
		}
		outputFileWriter.flush();
		outputFileWriter.close();
		
		System.out.println("Num of Files :"+numFiles);
		System.out.println("Num of Annotations :"+numAnnotation);
		System.out.println("Num NME :"+NME);
		
		
	}
	
	/**
	 * 
	 * @param mentionEntityFile
	 * @param titlesRedirection
	 * @throws IOException
	 * @throws CompressorException 
	 * @throws ParseException 
	 */
	public void calculateAccuracy(String mentionEntityFile,String titlesRedirection) throws IOException, CompressorException, ParseException{

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
		
		BufferedReader bffReader = new BufferedReader(new FileReader("./resource/GT_AIDA_CONLL.txt"));
		String inLine = null;
		while ((inLine = bffReader.readLine()) != null) {
			numMention++;
			String[] temp = inLine.split("\t");
			String mentionGT = temp[0];
			String entityGT = temp[1];
			if(!entityGT.equalsIgnoreCase("--NME--")){
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
							String redirectedEntity = titlesRedirectionsMap.get(str);
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
		} //end of while loop
		bffReader.close();
		double Acc = 0.0;
		Acc = (double)((double)(truePositive) / (double)(numAnnotation)) ;//System.out.println(numMention);
		System.out.println("TOTAL:" +numMention );
		System.out.println("Valid Ann:"+numAnnotation);
		System.out.println("TP:"+truePositive);
		System.out.println("Accuracy:"+Acc);
	}
	/**
	 * 
	 * @param mentionEntityFile
	 * @param titlesRedirection
	 * @param datasetFile
	 * @throws IOException
	 * @throws CompressorException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws ParseException
	 */
	public void calculateAccuracy(String mentionEntityFile,String titlesRedirection, String datasetFile)throws IOException, CompressorException,ParserConfigurationException, SAXException, ParseException {
		PriorOnlyModel POM = new PriorOnlyModel();
		TreeMap<String, String> priorOnlyModelMap = POM	.loaderPriorOnly(mentionEntityFile); // /home/renato/workspace/WikiParsing/resource/2006/mentionEntityLinks_PRIOR_100_top1.bz2
		
		Set<String> keys = priorOnlyModelMap.keySet();
		
		// need to load the titles redirection JSON file
		Map<String, String> titlesRedirectionsMap = POM.loadTitlesRedirectMap(titlesRedirection);
		
		AIDA_YAGO2_annotations_Parser p = new AIDA_YAGO2_annotations_Parser();
		LinkedList<ConllDocument> ConllDataSet  = p.parseDataset(datasetFile);

		double truePositive = 0.0;
		double trueNegative = 0.0;
		double Acc = 0.0;
		boolean match = false;
		int numFiles = 0;
		int numAnnotations = 0;
		int nme = 0;
		
		for(int i=1162; i < ConllDataSet.size(); i++){ //1163 is the first doc from testb testset b
			ConllDocument CO = ConllDataSet.get(i);
			LinkedList<AIDAYAGOAnnotation> AnnList =  CO.getListOfAnnotation();
			numFiles++;
			
			
			for(AIDAYAGOAnnotation Ann : AnnList){
				//
				numAnnotations++;
				String mentionConLL = Ann.getMention();
				String entityConLL = Ann.getEntity();
				String priorOnlyEntityLink = priorOnlyModelMap.get(mentionConLL);
				System.out.println(numAnnotations+":"+mentionConLL+"\t "+entityConLL+"\t "+priorOnlyEntityLink);
				//if(priorOnlyEntityLink!=null){
				//if(!entityConLL.equalsIgnoreCase("--NME--")){
				//	for (String strVariation : keys) {				
				//		if(strVariation.equalsIgnoreCase(mentionConLL)){
				//			priorOnlyEntityLink = priorOnlyModelMap.get(strVariation);
				//			if (priorOnlyEntityLink != null) {						
				//				if ( (entityConLL.equalsIgnoreCase(priorOnlyEntityLink)) ){//|| (mentionConLL.equalsIgnoreCase(priorOnlyEntityLink)) ) {
				//					truePositive++;
				//					break;
				//				} else {
				//					// 	Checking for redirections
				//					String redirectedEntity = titlesRedirectionsMap.get(strVariation);
				//					if (redirectedEntity != null) {
				//						if ((redirectedEntity.equalsIgnoreCase(entityConLL))) {
				//							truePositive++;
				//							match = true;
				//							break;
				//						} else {
				//							match = false;
				//							///System.out.println(mentionConLL + "\t E1: " + entityConLL + "\t E2: " + priorOnlyEntityLink);
				//						}
				//					}
				//				  }
				//		}else { 	
			//				}						
			//		}
			//		if (match) {
			//			break;
			//		}
			//	}
			//	}else{ 	nme++; 		}
			//	}else{
			//		nme++;
			//	}
				
			}
		}
		
		//System.out.println("Num files :"+numFiles);
		//System.out.println("Num Annotations :"+ numAnnotations);
		//System.out.println("Num True Positives :" +truePositive);
		//Acc = (double) truePositive / ( (double) ( numAnnotations - nme) ) ;
		//System.out.println("Acc : " + Acc);
		
		//Acc = (double) truePositive / ( (double) truePositive + (double) trueNegative );
		//System.out.println("Acc : " + Acc);
	
		
	}
	/**
	 * 
	 * @param mentionEntityFile
	 * @param titlesRedirection
	 * @param datasetFile
	 * @throws IOException
	 * @throws CompressorException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws ParseException
	 */
	public void compareToPriorOnly(String mentionEntityFile,String titlesRedirection, String datasetFile)throws IOException, CompressorException,
			ParserConfigurationException, SAXException, ParseException {
		
		PrintWriter Pwriter = new PrintWriter("/home/renato/workspace/WikiParsing/resource/2014/results_CONLL_priorOnly_2014.txt");

		PriorOnlyModel POM = new PriorOnlyModel();
		TreeMap<String, String> priorOnlyModelMap = POM	.loaderPriorOnly(mentionEntityFile); // /home/renato/workspace/WikiParsing/resource/2006/mentionEntityLinks_PRIOR_100_top1.bz2

		// need to load the titles redirection JSON file
		Map<String, String> titlesRedMap = POM.loadTitlesRedirectMap(titlesRedirection);
		
		AIDA_YAGO2_annotations_Parser p = new AIDA_YAGO2_annotations_Parser();
		LinkedList<ConllDocument> ConllDataSet  = p.parseDataset(datasetFile);
	
		for(int i=1162; i < ConllDataSet.size(); i++){ //1163 is the first doc from testb testset b
			ConllDocument CO = ConllDataSet.get(i);
			LinkedList<AIDAYAGOAnnotation> AnnList =  CO.getListOfAnnotation();
			
			int returnedItems = 0;
			double truePositive = 0.0;
			double trueNegative = 0.0;
			double falsePositive = 0.0;
			double falseNegative = 0.0;
			int count=0;
			//System.out.println("\n");
			for(AIDAYAGOAnnotation Ann : AnnList){
				String mentionConLL = Ann.getMention();
				String entityConLL = Ann.getEntity();
				//System.out.println(mentionConLL +"\t"+entityConLL);
				String priorOnlyEntityLink = priorOnlyModelMap.get(mentionConLL.trim());

				if (priorOnlyEntityLink != null) {
					returnedItems++;
					if (entityConLL.equalsIgnoreCase(priorOnlyEntityLink.trim())) {
						truePositive++;
						//continue;
					} else {
						// Checking for redirections
						String redirectedEntity = titlesRedMap.get(entityConLL);
						if ((redirectedEntity != null)
								&& (redirectedEntity.equalsIgnoreCase(priorOnlyEntityLink))) {
							truePositive++;
							//continue;
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
			double F_measure = (double) 2  * ((precision * recall) / (precision + recall));
			// System.out.println(count);
			Pwriter.println("P:" + precision + "\tR:" + recall + "\tF:"	+ F_measure);
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
