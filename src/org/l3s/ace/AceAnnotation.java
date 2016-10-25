package org.l3s.ace;

public class AceAnnotation {

	String fileName; //the file name the annotation belongs too;
	String id_target; //no idea what that means;
	String mention; //the annotated mention;
	String text_sample; //text snippet;
	String link; //The actual Wikipedia link;
	String link_votes; // number of users that agreed on the annotation ?
	String disamb_votes; //
	String total_votes;
	
	
	public AceAnnotation(String fileName, String id_target, String mention,
			String text_sample, String link, String link_votes,
			String disamb_votes, String total_votes) {
		super();
		this.fileName = fileName;
		this.id_target = id_target;
		this.mention = mention;
		this.text_sample = text_sample;
		this.link = link;
		this.link_votes = link_votes;
		this.disamb_votes = disamb_votes;
		this.total_votes = total_votes;
	}


	public AceAnnotation() {
		super();
		// TODO Auto-generated constructor stub
	}


	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}


	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	/**
	 * @return the id_target
	 */
	public String getId_target() {
		return id_target;
	}


	/**
	 * @param id_target the id_target to set
	 */
	public void setId_target(String id_target) {
		this.id_target = id_target;
	}


	/**
	 * @return the mention
	 */
	public String getMention() {
		return mention;
	}


	/**
	 * @param mention the mention to set
	 */
	public void setMention(String mention) {
		this.mention = mention;
	}


	/**
	 * @return the text_sample
	 */
	public String getText_sample() {
		return text_sample;
	}


	/**
	 * @param text_sample the text_sample to set
	 */
	public void setText_sample(String text_sample) {
		this.text_sample = text_sample;
	}


	/**
	 * @return the link
	 */
	public String getLink() {
		return link;
	}


	/**
	 * @param link the link to set
	 */
	public void setLink(String link) {
		this.link = link;
	}


	/**
	 * @return the link_votes
	 */
	public String getLink_votes() {
		return link_votes;
	}


	/**
	 * @param link_votes the link_votes to set
	 */
	public void setLink_votes(String link_votes) {
		this.link_votes = link_votes;
	}


	/**
	 * @return the disamb_votes
	 */
	public String getDisamb_votes() {
		return disamb_votes;
	}


	/**
	 * @param disamb_votes the disamb_votes to set
	 */
	public void setDisamb_votes(String disamb_votes) {
		this.disamb_votes = disamb_votes;
	}


	/**
	 * @return the total_votes
	 */
	public String getTotal_votes() {
		return total_votes;
	}


	/**
	 * @param total_votes the total_votes to set
	 */
	public void setTotal_votes(String total_votes) {
		this.total_votes = total_votes;
	}
	
	
	
}
