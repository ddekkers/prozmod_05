package de.fhwedel.om.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

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
   
   
   public String getRate_number() {
	return rate_number;
   }

   public void setRate_number(String rate_number) {
	   this.rate_number = rate_number;
   }

   public float getInterest_rate() {
	   return interest_rate;
   }
	
   public void setInterest_rate(float interest_rate) {
	   this.interest_rate = interest_rate;
   }
	
   public int getRuntime() {
	   return runtime;
   }
	
   public void setRuntime_in_years(int runtime) {
		this.runtime = runtime;
   }
	
   public int getCredit_amount_from() {
	   return credit_amount_from;
   }
	
   public void setCredit_amount_from(int credit_amount_from) {
	   this.credit_amount_from = credit_amount_from;
   }
	
   public int getCredit_amount_to() {
	   return credit_amount_to;
   }
	
   public void setCredit_amount_to(int credit_amount_to) {
	   this.credit_amount_to = credit_amount_to;
   }
	
   public ValidityLevel getValidity_level() {
	   return validity_level;
	}
	
   public void setValidity_level(ValidityLevel validity_level) {
	   this.validity_level = validity_level;
   }
	
   public Date getValid_from() {
	   return valid_from;
   }
	
   public void setValid_from(Date valid_from) {
	   this.valid_from = valid_from;
   }
	
   public Date getValid_to() {
	   return valid_to;
   }
	
   public void setValid_to(Date valid_to) {
	   this.valid_to = valid_to;
   }

   public Rate() {
	   this("", (float) 0.0, 0, 0, 0, ValidityLevel.A, null, null);
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
