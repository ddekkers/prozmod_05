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
   private int rate_id;
   
   private String rate_number;
   
   private float interest_rate;
   
   private int runtime;
   
   private int credit_amount_from;
   
   private int credit_amount_to;
   
   private ValidityLevel validity_level;
   
   private Date valid_from;
   
   private Date valid_to;
   
   private List<CreditContract> creditContracts;
   
   
   public String getRateNumber() {
	return rate_number;
   }

   public void setRateNumber(String rate_number) {
	   this.rate_number = rate_number;
   }

   public float getInterestRate() {
	   return interest_rate;
   }
	
   public void setInterestRate(float interest_rate) {
	   this.interest_rate = interest_rate;
   }
	
   public int getRuntime() {
	   return runtime;
   }
	
   public void setRuntimeInYears(int runtime) {
		this.runtime = runtime;
   }
	
   public int getCreditAmountFrom() {
	   return credit_amount_from;
   }
	
   public void setCreditAmountFrom(int credit_amount_from) {
	   this.credit_amount_from = credit_amount_from;
   }
	
   public int getCreditAmountTo() {
	   return credit_amount_to;
   }
	
   public void setCreditAmountTo(int credit_amount_to) {
	   this.credit_amount_to = credit_amount_to;
   }
	
   public ValidityLevel getValidityLevel() {
	   return validity_level;
	}
	
   public void setValidityLevel(ValidityLevel validity_level) {
	   this.validity_level = validity_level;
   }
	
   public Date getValidFrom() {
	   return valid_from;
   }
	
   public void setValidFrom(Date valid_from) {
	   this.valid_from = valid_from;
   }
	
   public Date getValidTo() {
	   return valid_to;
   }
	
   public void setValidTo(Date valid_to) {
	   this.valid_to = valid_to;
   }

   public Rate() {
	   this("", (float) 0.0, 0, 0, 0, null, new Date(), new Date());
   }
      
   public Rate(String rate_number, float interest_rate, int runtime, int credit_amount_from, int credit_amount_to, ValidityLevel validity_level, Date valid_from, Date valid_to) {
	   this.rate_number = rate_number;
	   this.interest_rate = interest_rate;
	   this.runtime = runtime;
	   this.credit_amount_from = credit_amount_from;
	   this.credit_amount_from = credit_amount_from;
	   this.validity_level = validity_level;
	   this.valid_from = valid_from;
	   this.valid_to = valid_to;
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
      return "";
   }
   
}
