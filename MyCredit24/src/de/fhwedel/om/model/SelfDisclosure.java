package de.fhwedel.om.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

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
	private Integer self_disclosure_id;

	@Column(nullable=false) 
	private Date registration;
	
	@Column(nullable=false) 
	private String occupation;
	
	@Column(nullable=false) 
	private ModeOfEmployment modeEfEmployment;
	
	@Column(nullable=true) 
	private Boolean terminable;
	
	@Column(nullable=true) 
	private String employer;
	
	@Column(nullable=false) 
	private Integer monthNet;
	
	@Column(nullable=true) 
	private Date classification;
	
	@Column(nullable=true) 
	private Integer creditLimit;
	
	@Column(nullable=true) 
	private ValidityLevel validity;
	
	@OneToOne
	private Customer customer;
	
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
		return modeEfEmployment;
	}

	public void setModeOfEmployment(ModeOfEmployment mode_of_employment) {
		this.modeEfEmployment = mode_of_employment;
	}

	public Boolean isTerminable() {
		return terminable;
	}

	public void setTerminable(Boolean terminable) {
		this.terminable = terminable;
	}

	public String getEmployer() {
		return employer;
	}

	public void setEmployer(String employer) {
		this.employer = employer;
	}

	public Integer getMonthNet() {
		return monthNet;
	}

	public void setMonthNet(Integer month_net) {
		this.monthNet = month_net;
	}

	public Date getClassification() {
		return classification;
	}

	public void setClassification(Date classification) {
		this.classification = classification;
	}

	public Integer getCreditLimit() {
		return creditLimit;
	}

	public void setCreditLimit(Integer credit_limit) {
		this.creditLimit = credit_limit;
	}

	public ValidityLevel getValidity() {
		return validity;
	}
	
	public Customer getCustomer() {
		return customer;
	}
	
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	public void setValidity(ValidityLevel validity) {
		this.validity = validity;
	}
	
	public SelfDisclosure() {
		this(null, null, null, null, null, null, null, null, null, null);
	}
	
	public SelfDisclosure(Date registration, String occupation, ModeOfEmployment mode_of_employment, Boolean terminable, String employer, Integer month_net, Date classification, Integer credit_limit, ValidityLevel validity, Customer cust) {
		
		this.registration = registration;
		this.occupation = occupation;
		this.modeEfEmployment = mode_of_employment;
		this.terminable = terminable;
		this.employer = employer;
		this.monthNet = month_net;
		this.classification = classification;
		this.creditLimit = credit_limit;
		this.validity = validity;
		this.customer = cust;
	}

	@Override
	public Integer getID() {
		return self_disclosure_id;
	}

	@Override
	public String getCaption() {
		return "Selbstauskunft von " + this.getCustomer().getCaption() ;
	}
	
	
}
