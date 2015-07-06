package de.fhwedel.om.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import de.fhwedel.om.types.ValidityLevel;

@SuppressWarnings("serial")
@Entity
@NamedQueries({
   @NamedQuery(
           name="getAllRates",
           query="SELECT a FROM Rate a"
   )
})
public class Rate implements BusinessObject<Integer> {

   @Id
   @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CUST_SEQ")
   @Column(name="rate_id")
   private Integer rate_id;
   
   private String rateNumber;
   
   private Double interestRate;
   
   private Integer runtime;
   
   private Integer creditAmountFrom;
   
   private Integer creditAmountTo;
   
   private ValidityLevel validityLevel;
   
   private Date validFrom;
   
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
