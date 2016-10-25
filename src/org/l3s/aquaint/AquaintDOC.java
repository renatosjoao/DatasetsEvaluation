package org.l3s.aquaint;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is meant to read the files from AQUAINT dataset and return a map with mention and entity for each file.
 * 
 * @author renato
 *
 */
public class AquaintDOC {

	private String AquaDOC = null;// "/home/renato/Documents/LATEX/Temporally_aware_Named_Entity_Linking/AQUAINT/OriginalData/APW19980603_0791.htm";
	private Pattern annotationPattern = Pattern.compile("\\[\\[(.*?)\\]\\]",Pattern.MULTILINE);
	private int numAnnotations = 0;

	/**
	 * 
	 * @param AquaDOC
	 */
	public AquaintDOC() {
		super();
	}

	/**
	 *	This method reads one files from AQUAINT dataset and return a map with mention and entity.
	 *
	 * @param AquaDOC
	 * @return
	 * @throws IOException
	 */
	public TreeMap<String, String> getAnnotations(File AquaDOC) throws IOException {
		TreeMap<String, String> AquaDOCAnnotations = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
		BufferedReader bffReader = new BufferedReader(new FileReader(AquaDOC));
		StringBuffer inputTextBuffer = new StringBuffer();
		String inpLine = null;
		while ((inpLine = bffReader.readLine()) != null) {
			inputTextBuffer.append(inpLine);
			inputTextBuffer.append("\n");
		}

		String inputText = inputTextBuffer.toString();
		Matcher mRedirect = annotationPattern.matcher(inputText);
		while (mRedirect.find()) {
			String mention = null;
			String entityLink = null;
			numAnnotations++;
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
			AquaDOCAnnotations.put(mention, entityLink);
		}
		bffReader.close();
		return AquaDOCAnnotations;
	}

	/**
	 * 
	 * @return
	 */
	public int getNumAnnotations() {
		return numAnnotations;
	}

	/**
	 * @return the aquaDOC
	 */
	public String getAquaDOC() {
		return AquaDOC;
	}

	/**
	 * @param aquaDOC
	 *            the aquaDOC to set
	 */
	public void setAquaDOC(String aquaDOC) {
		AquaDOC = aquaDOC;
	}

}