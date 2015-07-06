package de.fhwedel.om.types;

public enum ModeOfEmployment {

	employee (1),
	freelance (2), 
	pensioner (3), 
	unemployed (4);
	
	private Integer evaluation;
	
	ModeOfEmployment(Integer evaluation) {
		this.evaluation = evaluation;
	}
	
	public Integer getEvaluation(){
		return evaluation;
	}
}
