package de.fhwedel.om.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import de.fhwedel.om.types.CreditContractStatus;

@SuppressWarnings("serial")
@Entity
@Table(name="creditcontract")
@NamedQueries({
   @NamedQuery(
           name="getAllCreditContracts",
           query="SELECT a FROM CreditContract a"
   )
})
public class CreditContract implements BusinessObject<Integer> {

	@Id
   	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CUST_SEQ")
   	@Column(name="credit_contract_id")
   	private Integer credit_contract_id;
   
   	private String contractNumber;

    @Enumerated(EnumType.STRING)
	private CreditContractStatus status;
	
	private Integer runtime;
  
	private Integer creditAmount;
  
	private Date contractBegin;
  
	// Tilgungsrate
	private Integer annuityRental;
  
	// Restschuld
	private Integer residualDebt;
  
	private String iban;
	
	private String bic;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	private Rate rate;
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
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
