package org.l3s.conll;

import java.util.LinkedList;

public class ConllDocument {
	LinkedList<AIDAYAGOAnnotation> listOfAIDAYAGOAnnotation = null;
	//LinkedList<ConllAnnotation> listOfConllAnnotation = null;

	public ConllDocument() {
		super();
		listOfAIDAYAGOAnnotation = new LinkedList<>();
		//listOfConllAnnotation = new LinkedList<>();
	}

		public void addAnnotation(AIDAYAGOAnnotation ann) {
		listOfAIDAYAGOAnnotation.add(ann);
	}

	/**
	 * @return the listOfAnnotation
	 */
	public LinkedList<AIDAYAGOAnnotation> getListOfAnnotation() {
		return listOfAIDAYAGOAnnotation;
	}

	/**
	 * @param listOfAnnotation
	 *            the listOfAnnotation to set
	 */
	public void setListOfAnnotation(LinkedList<AIDAYAGOAnnotation> listOfAnnotation) {
		this.listOfAIDAYAGOAnnotation = listOfAnnotation;
	}

}