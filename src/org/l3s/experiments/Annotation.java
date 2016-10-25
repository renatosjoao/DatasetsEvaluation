package org.l3s.experiments;

public class Annotation {
	String mention;
	String entity;

	/**
	 * @return the mention
	 */
	public String getMention() {
		return mention;
	}

	/**
	 * @param mention
	 *            the mention to set
	 */
	public void setMention(String mention) {
		this.mention = mention;
	}

	/**
	 * @return the entity
	 */
	public String getEntity() {
		return entity;
	}

	/**
	 * @param entity
	 *            the entity to set
	 */
	public void setEntity(String entity) {
		this.entity = entity;
	}

	public Annotation(String mention, String entity) {
		super();
		this.mention = mention;
		this.entity = entity;
	}

	public Annotation() {
		super();
		// TODO Auto-generated constructor stub
	}

}
