package org.l3s.msnbc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.TreeMap;

public class MsnbcDOC {

	// 1 doc = 1 list (several hashmaps)
	public LinkedList<TreeMap<String, String>> MSNBCAnnotations;
	public LinkedList<MsnbcDOC> listOfMSNBCdocs = new LinkedList<MsnbcDOC>();
	private int numAnnotations = 0;
	private String docTitle;
	
	public MsnbcDOC() {
		super();
	}

	public LinkedList<MsnbcDOC> parseMSNBC_GT(String args) throws IOException {
		// ~~~ BOD
		// ### EOD
		BufferedReader bf = new BufferedReader(new FileReader(new File(args)));
		String line;
		while ((line = bf.readLine()) != null) {
			if (line.contains("~~~ BOD")) {
				MsnbcDOC doc1 = new MsnbcDOC();
				String title = line.split("\t")[1].trim();
				doc1.setDocTitle(title);
				// new doc
				LinkedList<TreeMap<String, String>> MSNBCAnnotations = new LinkedList<TreeMap<String, String>>();
				while (!(line = bf.readLine()).contains("### EOD")) {
					String mention = line.split("\t")[0].trim();
					String entity = line.split("\t")[1].trim();
					TreeMap<String, String> oneNode = new TreeMap<String, String>();
					oneNode.put(mention, entity);
					MSNBCAnnotations.add(oneNode);
				}
				
				doc1.setMSNBCAnnotations(MSNBCAnnotations);
				listOfMSNBCdocs.add(doc1);
			}
		}
		bf.close();
		return listOfMSNBCdocs;
	}
	/**
	 * @param mSNBCAnnotations
	 *            the mSNBCAnnotations to set
	 */
	public void setMSNBCAnnotations(LinkedList<TreeMap<String, String>> mSNBCAnnotations) {
		MSNBCAnnotations = mSNBCAnnotations;
	}

	public int getNumAnnotations() {
		return numAnnotations;
	}

	/**
	 * @return the docTitle
	 */
	public String getDocTitle() {
		return docTitle;
	}

	/**
	 * @param docTitle the docTitle to set
	 */
	public void setDocTitle(String docTitle) {
		this.docTitle = docTitle;
	}
	
	
}