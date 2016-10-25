package org.l3s.iitb;

public class CSAW_IITB_Annotation {

	private String docName = null;
	private String userId = null;
	private String wikiName = null;
	private String offset = null;
	private String length = null;
	
	
	public CSAW_IITB_Annotation(String docName, String userId, String wikiName,
			String offset, String length) {
		super();
		this.docName = docName;
		this.userId = userId;
		this.wikiName = wikiName;
		this.offset = offset;
		this.length = length;
	}


	public CSAW_IITB_Annotation() {
		super();
	}


	/**
	 * @return the docName
	 */
	public String getDocName() {
		return docName;
	}


	/**
	 * @param docName the docName to set
	 */
	public void setDocName(String docName) {
		this.docName = docName;
	}


	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}


	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}


	/**
	 * @return the wikiName
	 */
	public String getWikiName() {
		return wikiName;
	}


	/**
	 * @param wikiName the wikiName to set
	 */
	public void setWikiName(String wikiName) {
		this.wikiName = wikiName;
	}


	/**
	 * @return the offset
	 */
	public String getOffset() {
		return offset;
	}


	/**
	 * @param offset the offset to set
	 */
	public void setOffset(String offset) {
		this.offset = offset;
	}


	/**
	 * @return the length
	 */
	public String getLength() {
		return length;
	}


	/**
	 * @param length the length to set
	 */
	public void setLength(String length) {
		this.length = length;
	}

	
	
	
	
}
