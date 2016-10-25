package org.l3s.conll;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;




/**
 * 						GROUNDTHRUTH !!!
 * @author renato 
 * 
 * This is just a parser to get the list of annotations (Groundthruth) from
 * 
 *         ("/home/renato/Downloads/CONLL/aida-yago2-dataset/aida-yago2-dataset/AIDA-YAGO2-annotations.tsv")
 */
public class AIDA_YAGO2_annotations_Parser {
	LinkedList<ConllDocument> listOfDocuments = new LinkedList<>();
	/*
	 * example
	 *
	 * -DOCSTART- (2 Rare) 
	 * 1 Jimi_Hendrix http://en.wikipedia.org/wiki/Jimi_Hendrix 16095 /m/01vsy3q
	 * 10 London http://en.wikipedia.org/wiki/London 17867 /m/04jpl
	 * 21 United_States http://en.wikipedia.org/wiki/United_States 3434750 /m/09c7w0
	 * 24 Jimi_Hendrix http://en.wikipedia.org/wiki/Jimi_Hendrix 16095 /m/01vsy3q
	 * 48 --NME--
	 * 62 --NME--
	 */

	public AIDA_YAGO2_annotations_Parser() {
		super();
	}

	
	/*
	
	public HashMap<String,ConllDocument> parseGroundthruth(String inputFile) throws NumberFormatException, IOException{
		HashMap<String,ConllDocument> gtMap = new HashMap<String, ConllDocument>();
		PrintWriter gtMapping = new PrintWriter(new File("./resource/groundthruthConll"));
		BufferedReader bf = new BufferedReader(new FileReader(new File(inputFile)));
		String line = null;

		while ((line = bf.readLine()) != null) {
			if (line.contains("-DOCSTART-")) {
				String[] names = line.split("-DOCSTART-");
				String identifier = names[1].trim();
				gtMapping.println(identifier);
				
				// new documentl
				ConllDocument CD = new ConllDocument();
				while (!(line = bf.readLine()).isEmpty()) {
					//Charset.forName("UTF-8").encode(line);
					String[] elem = line.split("\t");
					if ((elem.length) >= 4) {
						String entity = elem[1];
						if (entity.equalsIgnoreCase("--NME--")) {
								String[] entities = line.split("\t");
								ConllAnnotation ca = new ConllAnnotation(Integer.parseInt(entities[0]), entities[1],entities[1]);
								CD.addAnnotation(ca);
							
						} else {
								String[] entities = line.split("\t");
								String ent = entities[1].replace("\\u0028","(").
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
										replace("\\u0021","!").replace("","");
									 
								
								String wiki = entities[2];
								//System.out.println(entities[1]);
								//entities[1] = entities[1].trim().replaceAll("\u0028","(");
								//entities[1] = entities[1].replace("\u0029",")");
								ConllAnnotation ca = new ConllAnnotation(Integer.parseInt(entities[0]), ent, wiki);
								//System.out.println(ent );
								CD.addAnnotation(ca);
						}
					} else {
						continue;
					}
				}
				//List.add(CD);
				gtMap.put(identifier, CD);
			}
		}
		bf.close();
		gtMapping.flush();
		gtMapping.close();
		return gtMap;
		
		
	}
	
	*/

	/**
	 * ("/home/renato/Downloads/CONLL/aida-yago2-dataset/aida-yago2-dataset/AIDA-YAGO2-annotations.tsv")
	 * "/home/renato/Documents/LATEX/Temporally aware Named Entity Linking/CONLL/aida-yago2-dataset/aida-yago2-dataset/AIDA-YAGO2-annotations.tsv");" 
	 * @param inputFile
	 * @return 
	 * @throws NumberFormatException
	 * @throws IOException
	 */
/*	public LinkedList<ConllDocument> parse(String inputFile) throws NumberFormatException, IOException {
		LinkedList<ConllDocument> List = new LinkedList<>();
		BufferedReader bf = new BufferedReader(new FileReader(new File(inputFile)));
		String line = null;

		while ((line = bf.readLine()) != null) {
			if (line.contains("-DOCSTART-")) {
				// new documentl
				ConllDocument CD = new ConllDocument();
				while (!(line = bf.readLine()).isEmpty()) {
					//Charset.forName("UTF-8").encode(line);
					String[] elem = line.split("\t");
					if ((elem.length) >= 4) {
						String entity = elem[1];
						if (entity.equalsIgnoreCase("--NME--")) {
								String[] entities = line.split("\t");
								ConllAnnotation ca = new ConllAnnotation(Integer.parseInt(entities[0]), entities[1],entities[1]);
								CD.addAnnotation(ca);
							
						} else {
								String[] entities = line.split("\t");
								String ent = entities[1].replace("\\u0028","(").
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
									 
								
								String wiki = entities[2];
								//System.out.println(entities[1]);
								//entities[1] = entities[1].trim().replaceAll("\u0028","(");
								//entities[1] = entities[1].replace("\u0029",")");
								ConllAnnotation ca = new ConllAnnotation(Integer.parseInt(entities[0]), ent, wiki);
								//System.out.println(ent );
								CD.addAnnotation(ca);
						}
					} else {
						continue;
					}
				}
				List.add(CD);
			}
		}
		bf.close();
		return List;
	}
	*/

	/**
	 * @return the listOfDocuments
	 */
	public LinkedList<ConllDocument> getListOfDocuments() {
		return listOfDocuments;
	}

	/**
	 * @param listOfDocuments
	 *            the listOfDocuments to set
	 */
	public void setListOfDocuments(LinkedList<ConllDocument> listOfDocuments) {
		this.listOfDocuments = listOfDocuments;
	}

	
	
	
	/**
	 * 
	 * @param dataset AIDA-YAGO2-dataset.tsv
	 * @return
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	public LinkedList<ConllDocument> parseDataset(String dataset) throws NumberFormatException, IOException{
		//String dataset = "/home/renato/Documents/LATEX/Temporally_aware_Named_Entity_Linking/CONLL/AIDA-YAGO2-dataset.tsv";
		BufferedReader bf = new BufferedReader(new FileReader(dataset));
		BufferedReader bf_AUX = new BufferedReader(new FileReader(dataset));
		int numDocs = 0;
		String line = null;
		String line_aux = bf_AUX.readLine();
		LinkedList<ConllDocument> ConllDocsList = new LinkedList<ConllDocument>();
		ConllDocument CoNLLDoc = null;
		while ((line = bf.readLine())  != null) {
			if (line.contains("-DOCSTART-")) {
				AIDAYAGOAnnotation annotation = null;
				CoNLLDoc = new ConllDocument();
				numDocs++;
				line_aux = bf_AUX.readLine();
				while ( !line_aux.contains("-DOCSTART-") ){
					line_aux = bf_AUX.readLine();	
					line = bf.readLine();
					if(line_aux == null ){
						break;
					}else {
						Charset.forName("UTF-8").encode(line);
						String[] elem = line.split("\t");
						if ((elem.length) >= 4) {
							String mention = elem[2].trim();
							String entity = elem[3].replace("\\u0028","(").
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
							//System.out.println(mention);
							if(elem[1].trim().equalsIgnoreCase("I")){ continue;}
							
						if (entity.equalsIgnoreCase("--NME--")) {
							annotation = new AIDAYAGOAnnotation(mention,entity,"","","");
							CoNLLDoc.addAnnotation(annotation);

						} else {
							String wikiLink = elem[4].trim();
							String entityId = elem[5].trim();
							annotation = new AIDAYAGOAnnotation(mention,entity,entityId,wikiLink,"");
							CoNLLDoc.addAnnotation(annotation);

							}
						
						}
						}
				}
			}
			ConllDocsList.add(CoNLLDoc);
		}
		bf.close();
		bf_AUX.close();
	//	System.out.println(numDocs);
	//	System.out.println(ConllDocsList.size());
		return ConllDocsList;
	}
	
	
	/**
	 * 
	 * @param args
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	public static void main_old(String[] args) throws NumberFormatException, IOException {
		String ConLLfiles = "./resource/listCONLL";
		String groundthruthConll = "./resource/groundthruthConll";
		PrintWriter pmapp = new PrintWriter(new File("./resource/conllMapping"));
		BufferedReader bf = new BufferedReader(new FileReader(ConLLfiles));
		BufferedReader bf2 = new BufferedReader(new FileReader(groundthruthConll));
		String line;
		String line2;
		int count = 0 ;
		while((line = bf.readLine()) != null){
			line2 = bf2.readLine();
			pmapp.println(line + " \t "+ line2);
		}
		
		pmapp.flush();
		pmapp.close();
		bf.close();
		bf2.close();
		
		
		//AIDA_YAGO2_annotations_Parser p = new AIDA_YAGO2_annotations_Parser();
		//HashMap<String,ConllDocument> cd = p.parseGroundthruth("/home/renato/Documents/LATEX/Temporally_aware_Named_Entity_Linking/CONLL/aida-yago2-dataset/aida-yago2-dataset/AIDA-YAGO2-annotations.tsv");
		//System.out.println(cd.size());
		//LinkedList<ConllDocument> list = p.parse("/home/renato/Documents/LATEX/Temporally aware Named Entity Linking/CONLL/aida-yago2-dataset/aida-yago2-dataset/AIDA-YAGO2-annotations.tsv");
		//for (int i=0; i<list.size(); i++){
		//	//System.out.println(list.get(i).listOfConllAnnotation.get(0).getEntity());
		//}
		
	}
}