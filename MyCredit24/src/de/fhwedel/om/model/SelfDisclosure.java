package de.fhwedel.om.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import de.fhwedel.om.types.ModeOfEmployment;
import de.fhwedel.om.types.ValidityLevel;

@SuppressWarnings("serial")
@Entity
@NamedQueries({
   @NamedQuery(
         name="getAllSelfDisclosure",
         query="SELECT c FROM SelfDisclosure c"
   )
})

public class SelfDisclosure implements BusinessObject<Integer>{
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CUST_SEQ")
	@Column(name="self_disclosure_id")
	private int self_disclosure_id;

	private Date registration;
	
	private String occupation;
	
	private ModeOfEmployment mode_of_employment;
	
	private boolean terminable;
	
	private String employer;
	
	private int month_net;
	
	private Date classification;
	
	private int credit_limit;
	
	private ValidityLevel validity;
	
	public Date getRegistration() {
		return registration;
	}

	public void setRegistration(Date registration) {
		this.registration = registration;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public ModeOfEmployment getModeOfEmployment() {
		return mode_of_employment;
	}

	public void setModeOfEmployment(ModeOfEmployment mode_of_employment) {
		this.mode_of_employment = mode_of_employment;
	}

	public boolean isTerminable() {
		return terminable;
	}

	public void setTerminable(boolean terminable) {
		this.terminable = terminable;
	}

	public String getEmployer() {
		return employer;
	}

	public void setEmployer(String employer) {
		this.employer = employer;
	}

	public int getMonthNet() {
		return month_net;
	}

	public void setMonthNet(int month_net) {
		this.month_net = month_net;
	}

	public Date getClassification() {
		return classification;
	}

	public void setClassification(Date classification) {
		this.classification = classification;
	}

	public int getCreditLimit() {
		return credit_limit;
	}

	public void setCreditLimit(int credit_limit) {
		this.credit_limit = credit_limit;
	}

	public ValidityLevel getValidity() {
		return validity;
	}

	public void setValidity(ValidityLevel validity) {
		this.validity = validity;
	}

	@Override
	public Integer getID() {
		return self_disclosure_id;
	}

	@Override
	public String getCaption() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}
