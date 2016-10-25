package org.l3s.ace;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.l3s.Prior.PriorOnlyModel;

public class Comparator {

	
	public Comparator(){
	}

	public static void main(String[] args) throws IOException, ParseException, CompressorException {
		Comparator cp = new Comparator();
		//args[0] ="/home/renato/workspace/WikiParsing/resource/2016/mentionEntityLinks_PRIOR_100_top1.bz2",
		//args[1] = titles /home/renato/workspace/WikiParsing/resource/2006/Json pagesTitles_REDIRECT.json
		//args[2] ="/home/renato/Documents/LATEX/Temporally_aware_Named_Entity_Linking/ACE2004/Dev/ACE2004.dev.labels.csv"
		//cp.compareToPriorOnly(args[0],args[1],args[2]);
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
	 * This method calculates the accuracy using one single redirections file !!!
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
		Set<String> keys = priorOnlyModelMap.keySet();
		TreeMap<String,JSONArray> titlesRedirectionsMap = POM.loadTitlesRedirectionsMap(titlesRedirection);
		
		BufferedReader bffReader = new BufferedReader(new FileReader("./resource/GT_ACE.txt"));
		String inLine = null;
		while ((inLine = bffReader.readLine()) != null) {
			numMention++;
			String[] temp = inLine.split("\t");
			String mentionGT = temp[0];
			String entityGT = temp[1];
			if(!entityGT.equalsIgnoreCase("none")){
				numAnnotation++;
				
				String priorOnlyEntityLink = priorOnlyModelMap.get(mentionGT);  // this is a perfect String match
				//Checked !!!
				if(( entityGT.equalsIgnoreCase(priorOnlyEntityLink))){	
					truePositive++;
					continue;
				}else{
					
					//if(mentionGT.equalsIgnoreCase(priorOnlyEntityLink)){
					//	truePositive++;
	// >>> DANGER	//	System.out.println(mentionGT+":"+entityGT+" >>> "+priorOnlyEntityLink);
					//	continue;
					//}
					
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
				}

			}else{
				NME++;
			}
		}//end of while loop
		bffReader.close();
		
		double Acc = 0.0;
		Acc = (double)((double)(truePositive) / (double)(numAnnotation)) ;//System.out.println(numMention);
		System.out.println(numMention);
		System.out.println(numAnnotation);
		System.out.println("TP:"+truePositive);
		System.out.println("Accuracy:"+Acc);
	}
		
	/**
	 * 
	 * @param groundtruthFile
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	public void dumpAnnotations(String groundtruthFile) throws NumberFormatException, IOException{
		int numFiles = 0;
		int numAnnotation = 0;
		int NME = 0;
		PrintWriter outputFileWriter = new PrintWriter(new File("./resource/GT_ACE.txt"));
		AceDoc acd = new AceDoc();
		TreeMap<String,LinkedList<AceAnnotation>> AceDocsMap = acd.parseACEDoc_GT(groundtruthFile);
		Iterator<?> it = AceDocsMap.entrySet().iterator();
		while (it.hasNext()) {
			numFiles++;
			Map.Entry pair = (Map.Entry) it.next();
			String fileName =  (String) pair.getKey();
			LinkedList<AceAnnotation> fileAnnotations =  (LinkedList<AceAnnotation>) pair.getValue();
			for(AceAnnotation ann : fileAnnotations){
				numAnnotation++;
				String mentionFromACE = ann.getMention().trim();
				String entityFromACE = ann.getLink().trim();
				System.out.println(mentionFromACE + "\t" + entityFromACE);
				outputFileWriter.println(mentionFromACE + "\t" + entityFromACE);
			}
		}
		
		outputFileWriter.flush();
		outputFileWriter.close();
		
		System.out.println("Num of Files :"+numFiles);
		System.out.println("Num of Annotations :"+numAnnotation);
		
		
	}
	/**
	 *  This method calculates accuracy using only the redirection for that specific year !!! 
	 *  
	 *  
	 * @param mentionEntityFile
	 * @param titlesRedirection  ATTENTION !!! 
	 * @param groundThruthFile
	 * @throws IOException
	 * @throws CompressorException
	 * @throws ParseException
	 */
	public void calculateAccuracy(String mentionEntityFile, String titlesRedirection) throws IOException, CompressorException, ParseException{
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
		
		BufferedReader bffReader = new BufferedReader(new FileReader("./resource/GT_ACE.txt"));
		String inLine = null;
	
		while ((inLine = bffReader.readLine()) != null) {
			numMention++;
			String[] temp = inLine.split("\t");
			String mentionGT = temp[0];
			String entityGT = temp[1];
			if(!entityGT.equalsIgnoreCase("none")){
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
								
								if(priorFromRed != null){
									if(priorFromRed.equalsIgnoreCase(entityGT)){
										truePositive++;
										match = true;
										break;
									}
						
									
									if(redirectedEntity.equalsIgnoreCase(entityGT)){
										truePositive++;
										match = true;
										break;

									}else{
										match = false;
									}
								}
											
							}else{
								match=false;
							}
						
							if(match){
								continue;
							}else{
								trueNegative++;
								//System.out.println(numMention+" : "+mentionGT + " - ");
							}
						}
					}
				}
			}
		}//end of while loop
		
		bffReader.close();
		double Acc = 0.0;
		Acc = (double)((double)(truePositive) / (double)(numAnnotation)) ;//System.out.println(numMention);
		System.out.println("TOTAL:" +(numMention) );
		System.out.println("Valid Ann:" +(numAnnotation) );
		System.out.println("TP:" + truePositive);
		System.out.println("Acc : " + Acc);

	
	}

	
	/**
	 * 
	 * @param mentionEntityFile
	 * @param titlesRedirection
	 * @param groundThruthFile
	 * @throws IOException
	 * @throws CompressorException
	 * @throws ParseException
	 */
	public void compareToPriorOnly(String mentionEntityFile, String titlesRedirection, String groundThruthFile) throws IOException, CompressorException, ParseException{
		PriorOnlyModel POM  = new PriorOnlyModel();
		
		PrintWriter Pwriter = new PrintWriter("/home/renato/workspace/WikiParsing/resource/2016/results_ACE_priorOnly_2016.txt");
		TreeMap<String, String> priorOnlyModelMap = POM.loaderPriorOnly(mentionEntityFile); //
		int numFiles=0;
		
		AceDoc acd = new AceDoc();
		TreeMap<String,LinkedList<AceAnnotation>> AceDocsMap = acd.parseACEDoc_GT(groundThruthFile);
		
		//need to load the titles redirection JSON file
		Map<String, String> titlesRedMap = POM.loadTitlesRedirectMap(titlesRedirection);
				
				
		Iterator<?> it = AceDocsMap.entrySet().iterator();
		while (it.hasNext()) {
			numFiles++;
			Map.Entry pair = (Map.Entry) it.next();
			String fileName =  (String) pair.getKey();
			LinkedList<AceAnnotation> fileAnnotations =  (LinkedList<AceAnnotation>) pair.getValue();
			int numAnnotations = fileAnnotations.size();
			double truePositive = 0.0;
			double trueNegative = 0.0;
			double falsePositive = 0.0;
			double falseNegative = 0.0;
			int returnedItems =0;
			
			for(AceAnnotation ann : fileAnnotations){
				String mentionFromACE = ann.getMention().trim();
				String entityFromACE = ann.getLink().trim();
				
				String priorOnlyEntityLink = priorOnlyModelMap.get(mentionFromACE);
				
				if(priorOnlyEntityLink!=null){
					returnedItems++;
					if(entityFromACE.equalsIgnoreCase(priorOnlyEntityLink)){	
						truePositive++;
						continue;
					}else{
						//Checking for redirections
						String redirectedEntity = titlesRedMap.get(entityFromACE);
						if((redirectedEntity!=null) && (redirectedEntity.equalsIgnoreCase(priorOnlyEntityLink)) ){
							truePositive++;
							continue;
						}else{
							trueNegative++;
						}
					}
				}else{
				}
			}
			double precision;
			if(returnedItems == 0){
				precision = 0.0;
			}else{
				precision = truePositive / (double)returnedItems;
			}
			double recall = truePositive / (double)numAnnotations;
			double F_measure = (double)2*((precision*recall)/(precision+recall));
			//System.out.println(fileName+"\tP:"+precision+"\tR:"+recall+"\tF:"+F_measure);
			Pwriter.println("P:"+precision+"\tR:"+recall+"\tF:"+F_measure);
			Pwriter.flush();
		
		}
		Pwriter.close();
		//System.out.println(numFiles);
			
		
	}

	/**
	 * 
	 * @param bv
	 * @param numberOfBits
	 */
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
	/**
	 * 
	 * @param input
	 * @return
	 */
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
	
	
	
	/**
	 *
	 * @param fileIn
	 * @return
	 * @throws FileNotFoundException
	 * @throws CompressorException
	 */
	public BufferedReader getBufferedReaderForCompressedFile(String fileIn) throws FileNotFoundException, CompressorException {
		FileInputStream fin = new FileInputStream(fileIn);
		BufferedInputStream bis = new BufferedInputStream(fin);
		CompressorInputStream input = new CompressorStreamFactory().createCompressorInputStream(bis);
		BufferedReader br2 = new BufferedReader(new InputStreamReader(input));
		return br2;
	}

}
