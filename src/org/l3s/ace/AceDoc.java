package org.l3s.ace;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.TreeMap;

public class AceDoc {
	// public LinkedList<TreeMap<String, String>> ACEAnnotations = new
	// LinkedList<TreeMap<String,String>>();
	public LinkedList<AceDoc> listOfACEDocs = new LinkedList<AceDoc>();
	public TreeMap<String, LinkedList<AceAnnotation>> AceDocsMap = new TreeMap<String, LinkedList<AceAnnotation>>(String.CASE_INSENSITIVE_ORDER);
	public String titlesMapJson;

	public AceDoc() {
		super();
	}

	/**
	 * 
	 * @param args
	 * @return
	 * @throws IOException
	 */
	public TreeMap<String, LinkedList<AceAnnotation>> parseACEDoc_GT(String GTFile) throws IOException {
		int numdoc = 0;
		String path = GTFile;// "/home/renato/Documents/LATEX/Temporally_aware_Named_Entity_Linking/ACE2004/Dev/ACE2004.dev.labels.csv";
		BufferedReader bffReader = new BufferedReader(new FileReader(path));
		String inpLine = bffReader.readLine();
		while ((inpLine = bffReader.readLine()) != null) {
			String[] elemss = inpLine.split("\",\"");
			String fileName = elemss[0].replace("\"", "");
			String mentions = elemss[2].replace("\"", "");
			String[] entitiesArray = elemss[4].split("\",");
			String entity = entitiesArray[0].trim();
			AceAnnotation aceAnn;
			LinkedList<AceAnnotation> aceAnnList = AceDocsMap.get(fileName);
			if (aceAnnList == null) {
				numdoc++;
				aceAnnList = new LinkedList<AceAnnotation>();
				aceAnn = new AceAnnotation(fileName, "", mentions, "", entity, "", "", "");
				aceAnnList.add(aceAnn);
			} else {
				aceAnn = new AceAnnotation(fileName, "", mentions, "", entity, "", "", "");
				aceAnnList.add(aceAnn);
			}
			AceDocsMap.put(fileName, aceAnnList);
		}
		bffReader.close();
		// System.out.println(numdoc);
		return AceDocsMap;
	}

}
