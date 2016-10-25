package org.l3s.Prior;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class PriorOnlyModel {

	public PriorOnlyModel() {
		super();
	}

	/**
	 * 
	 * @param titlesMapJson
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	public TreeMap<String, JSONArray> loadTitlesRedirectionsMap(
			String titlesMapJson) throws IOException, ParseException {
		TreeMap<String, JSONArray> TitlesMap = new TreeMap<String, JSONArray>();
		FileReader reader = new FileReader(titlesMapJson);
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(reader);
		JSONArray array = (JSONArray) obj;
		int jsonSize = array.size();
		for (int i = 0; i < jsonSize; i++) {
			JSONObject jobject = (JSONObject) array.get(i);
			String pageTitle = (String) jobject.get("title");
			JSONArray redirectPage = (JSONArray) jobject.get("redirections");
			TitlesMap.put(pageTitle, redirectPage);
			// System.out.println("");
		}
		return TitlesMap;
	}

	/**
	 * This method is meant to load the file with mentions/entities pairs. (i.e.
	 * mentionEntityLinks_PRIOR_1_top1.bz2 ) into a TreeMap object.
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException
	 * @throws CompressorException
	 */
	public TreeMap<String, String> loaderPriorOnly(String filePath)
			throws IOException, CompressorException {
		BufferedReader buffReader1 = getBufferedReaderForCompressedFile(filePath);
		TreeMap<String, String> map = new TreeMap<String, String>();
		String line;
		while ((line = buffReader1.readLine()) != null) {
			String[] elem = line.split(" ;-; ");
			String currentMention = elem[0].trim();
			String currentEntity = elem[1].trim();
			// String currentPrior = elem[2];
			map.put(currentMention, currentEntity);
		}
		return map;
	}

	/**
	 * This is just a utility function to load bzip2 files to BufferredReader.
	 *
	 * @param fileIn
	 * @return
	 * @throws FileNotFoundException
	 * @throws CompressorException
	 */
	private BufferedReader getBufferedReaderForCompressedFile(String fileIn)
			throws FileNotFoundException, CompressorException {
		FileInputStream fin = new FileInputStream(fileIn);
		BufferedInputStream bis = new BufferedInputStream(fin);
		CompressorInputStream input = new CompressorStreamFactory()
				.createCompressorInputStream(bis);
		BufferedReader br2 = new BufferedReader(new InputStreamReader(input));
		return br2;
	}

	/**
	 * This method loads the pagesTitles_REDIRECT.json file into a TreeMap
	 * object.
	 * 
	 * @param titlesMapJson
	 * @return
	 * @throws org.json.simple.parser.ParseException
	 * @throws IOException
	 */
	public Map<String, String> loadTitlesRedirectMap(String titlesMapJson)
			throws org.json.simple.parser.ParseException, IOException {
		TreeMap<String, String> TitlesMap = new TreeMap<String, String>();
		FileReader reader = new FileReader(titlesMapJson);
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(reader);
		JSONArray array = (JSONArray) obj;
		int jsonSize = array.size();
		for (int i = 0; i < jsonSize; i++) {
			JSONObject jobject = (JSONObject) array.get(i);
			String pageTitle = (String) jobject.get("title");
			String redirectPage = (String) jobject.get("redirect");
			TitlesMap.put(pageTitle, redirectPage);
		}
		return TitlesMap;
	}

}
