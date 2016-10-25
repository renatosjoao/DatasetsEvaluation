package org.l3s.conll;

public class ConllAnnotation {

	int position;
	String entity;
	String link;
	
	public ConllAnnotation(){
		super();
	}

	public ConllAnnotation(int position, String entity, String link) {
		super();
		this.position = position;
		this.entity = entity;
		this.link = link;
	}
	
	

	public ConllAnnotation(int position, String entity) {
		super();
		this.position = position;
		this.entity = entity;
	}

	public ConllAnnotation(String entity) {
		super();
		this.entity = entity;
	}

	/**
	 * @return the position
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(int position) {
		this.position = position;
	}

	/**
	 * @return the entity
	 */
	public String getEntity() {
		return entity;
	}

	/**
	 * @param entity the entity to set
	 */
	public void setEntity(String entity) {
		this.entity = entity;
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
	
	
}
