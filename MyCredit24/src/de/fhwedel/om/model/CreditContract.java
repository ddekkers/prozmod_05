package de.fhwedel.om.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import de.fhwedel.om.types.CreditContractStatus;

@SuppressWarnings("serial")
@Entity
@NamedQueries({
   @NamedQuery(
           name="getAllCreditContracts",
           query="SELECT a FROM CreditContract a"
   )
})
@Table(name="creditcontract",
uniqueConstraints=@UniqueConstraint(columnNames={"credit_contract_id", "contractNumber"}))
public class CreditContract implements BusinessObject<Integer> {

	@Id
   	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CUST_SEQ")
   	@Column(name="credit_contract_id")
   	private Integer credit_contract_id;
	
	@Column(nullable=false) 
   	private String contractNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false) 
	private CreditContractStatus status;
	
    @Column(nullable=false) 
	private Integer runtime;
  
	@Column(nullable=false) 
	private Integer creditAmount;
  
	@Column(nullable=false) 
	private Date contractBegin;
  
	// Tilgungsrate
	@Column(nullable=false) 
	private Integer annuityRental;
  
	// Restschuld
	@Column(nullable=false) 
	private Integer residualDebt;
  
	@Column(nullable=true) 
	private String iban;

	@Column(nullable=true)
	private String bic;
	
	@ManyToOne()
	private Rate rate;
	
	@ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE})
	private Customer customer;

	public CreditContract() {
		this(null, null, null, null, null, null, null, null, null, null, null);
	}
	
	public CreditContract(String contract_number, CreditContractStatus status, Integer runtime, Integer credit_amount, Date contract_begin, Integer annuity_rental, Integer residual_debt, String iban, String bic, Rate rate, Customer customer) {
		
		this.contractNumber = contract_number;
		this.status = status;
		this.runtime = runtime;
		this.creditAmount = credit_amount;
		this.contractBegin = contract_begin;
		this.annuityRental = annuity_rental;
		this.residualDebt = residual_debt;
		this.iban = iban;
		this.bic = bic;	
		this.rate = rate;
		this.customer = customer;
	}

	public Integer getCreditContractId() {
	
		return credit_contract_id;
	}

	public void setCreditContractId(Integer credit_contract_id) {
		this.credit_contract_id = credit_contract_id;
	}
	
	public String getContractNumber() {
		return contractNumber;
	}
	
	public void setContractNumber(String contract_number) {
		this.contractNumber = contract_number;
	}
	
	public CreditContractStatus getStatus() {
		return status;
	}
	
	public void setStatus(CreditContractStatus status) {
		this.status = status;
	}
	
	public Integer getRuntime() {
		return runtime;
	}
	
	public void setRuntime(Integer runtime) {
		this.runtime = runtime;
	}
	
	public Integer getCreditAmount() {
		return creditAmount;
	}
	
	public void setCreditAmount(Integer credit_amount) {
		this.creditAmount = credit_amount;
	}
	
	public Date getContractBegin() {
		return contractBegin;
	}
	
	public void setContractBegin(Date contract_begin) {
		this.contractBegin = contract_begin;
	}
	
	public Integer getAnnuityRental() {
		return annuityRental;
	}
	
	public void setAnnuityRental(Integer annuity_rental) {
		this.annuityRental = annuity_rental;
	}
	
	public Integer getResidualDebt() {
		return residualDebt;
	}
	
	public void setResidualDebt(Integer residual_debt) {
		this.residualDebt = residual_debt;
	}
	
	public String getIban() {
		return iban;
	}
	
	public void setIban(String iban) {
		this.iban = iban;
	}
	
	public String getBic() {
		return bic;
	}
	
	public void setBic(String bic) {
		this.bic = bic;
	}
	  
	public Rate getRate() {
		return rate;
	}

	public void setRate(Rate rate) {
		this.rate = rate;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
   @Override
   public Integer getID() {
	   return credit_contract_id;
   }

	@Override
	public String getCaption() {
		return contractNumber == null ? "" + (status == null ? "" : status.toString()) : contractNumber + " " + (status == null ? "" : status.toString());
	}

	
   
}
