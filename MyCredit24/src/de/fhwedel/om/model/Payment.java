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

import de.fhwedel.om.masks.DateHandler;
import de.fhwedel.om.types.PaymentType;

@SuppressWarnings("serial")
@Entity
@NamedQueries({
   @NamedQuery(
           name="getAllPayments",
           query="SELECT a FROM Payment a"
   )
})
public class Payment implements BusinessObject<Integer> {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CUST_SEQ")
	@Column(name="payment_id")
	private int payment_id;
   
	@Column(nullable=false) 
	private Date date;

	@Column(nullable=false) 
	private Integer amount;
   
	@Column(nullable=false) 
	private PaymentType type;
   
	@ManyToOne()
	private CreditContract creditContract;
   
	public Date getDate() {
		return date;
	}

	public void setDateAsString(String date) {
		this.date = new DateHandler("dd.MM.yyyy").parseStringToDate(date);
	}
   
	public String getDateAsString() {
		return new DateHandler("dd.MM.yyyy").getDateAsString(date);
	}
   
	public void setDate(Date date) {
		this.date = date;
	}
	
	public Integer getAmount() {
		return amount;
	}
	
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	
	public PaymentType getType() {
		return type;
	}
	
	public void setType(PaymentType type) {
		this.type = type;
	}
   
	public Payment() {
		this(null, null, null, null);
	}
      
	public Payment(Date date, Integer amount, PaymentType type, CreditContract contract) {
	   
		this.date = date;
		this.amount = amount;
		this.type = type;
		this.creditContract = contract;
	}

	@Override
	public String toString() {
		return this.getCaption();
	}
   
	@Override
	public Integer getID() {
		return this.payment_id;
	}

	@Override
	public String getCaption() {
		return type == null ? "" : type.toString() + 
				" - " + amount == null ? "" : amount.toString() + " EUR - " + 
						(new DateHandler("dd.MM.yyyy").getDateAsString(date));
	}

	public CreditContract getCreditContract() {
		return creditContract;
	}
	
	public void setCreditContract(CreditContract creditContract) {
		this.creditContract = creditContract;
	}
   
}
