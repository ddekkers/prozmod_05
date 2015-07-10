package de.fhwedel.om.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import de.fhwedel.om.types.ValidityLevel;

@SuppressWarnings("serial")
@Entity
@NamedQueries({
   @NamedQuery(
           name="getAllRates",
           query="SELECT a FROM Rate a"
   )
})
@Table(name="rate",
uniqueConstraints=@UniqueConstraint(columnNames={"rate_id", "rateNumber"}))
public class Rate implements BusinessObject<Integer> {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CUST_SEQ")
	@Column(name="rate_id")
	private Integer rate_id;
   
	@Column(nullable=false) 
	private String rateNumber;
   
	@Column(nullable=false) 
	private Double interestRate;
   
	@Column(nullable=false) 
	private Integer runtime;
   
	@Column(nullable=false) 
	private Integer creditAmountFrom;
   
	@Column(nullable=false) 
	private Integer creditAmountTo;
   
	@Column(nullable=false) 
	private ValidityLevel validityLevel;
   
	@Column(nullable=true) 
	private Date validFrom;
   
	@Column(nullable=true) 
	private Date validTo;
   
	public String getRateNumber() {
		return rateNumber;
	}

	public void setRateNumber(String rate_number) {
		this.rateNumber = rate_number;
	}

	public Double getInterestRate() {
		return interestRate;
	}
	
	public void setInterestRate(Double interest_rate) {
		this.interestRate = interest_rate;
	}
	
	public Integer getRuntime() {
		return runtime;
	}
	
	public void setRuntimeInYears(int runtime) {
		this.runtime = runtime;
	}
	
	public Integer getCreditAmountFrom() {
		return creditAmountFrom;
	}
	
	public void setCreditAmountFrom(int credit_amount_from) {
		this.creditAmountFrom = credit_amount_from;
	}
	
	public Integer getCreditAmountTo() {
		return creditAmountTo;
	}
	
	public void setCreditAmountTo(int credit_amount_to) {
		this.creditAmountTo = credit_amount_to;
	}
	
	public ValidityLevel getValidityLevel() {
		return validityLevel;
	}
	
	public void setValidityLevel(ValidityLevel validity_level) {
		this.validityLevel = validity_level;
	}
	
	public String getValidFrom() {
		return validFrom == null ? "" : validFrom.toString();
	}
	
	public void setValidFrom(String valid_from) {
	}
   
	public Date getDateValidFrom() {
		return validFrom;
	}
   
	public Date getDateValidTo() {
		return validTo;
	}
	
	public String getValidTo() {
		return validTo == null ? "" : validTo.toString();
	}
	
	public void setValidTo(String valid_to) {
	}

	public Rate() {
		this(null, null, null, null, null, null, null, null);
	}
      
	public Rate(String rate_number, Double interest_rate, Integer runtime, Integer credit_amount_from, Integer credit_amount_to, ValidityLevel validity_level, Date valid_from, Date valid_to) {
		this.rateNumber = rate_number;
		this.interestRate = interest_rate;
		this.runtime = runtime;
		this.creditAmountFrom = credit_amount_from;
		this.creditAmountTo = credit_amount_to;
		this.validityLevel = validity_level;
		this.validFrom = valid_from;
		this.validTo = valid_to;
	}

	// Object-Methoden

	@Override
	public String toString() {
		return this.getCaption();
	}
   
	// BusinessObject-Methoden 
   
	@Override
	public Integer getID() {
		return this.rate_id;
	}

	@Override
	public String getCaption() {
		return rateNumber;
	}
   
}
