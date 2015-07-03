package de.fhwedel.om.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import de.fhwedel.om.types.CreditContractStatus;

@SuppressWarnings("serial")
@Entity
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
   
   	private String contract_number;

	private CreditContractStatus status;
	
	private Integer runtime;
  
	private Integer credit_amount;
  
	private Date contract_begin;
  
	// Tilgungsrate
	private Integer annuity_rental;
  
	// Restschuld
	private Integer residual_debt;
  
	private String iban;
	
	private String bic;
	
	private List<Payment> payments;

	private Rate rate;
	
	private Customer customer;

	public CreditContract() {
		this(null, CreditContractStatus.proposal, null, null, null, null, null, null, null, new ArrayList<Payment>(), null, null);
	}
	
	public CreditContract(String contract_number, CreditContractStatus status, Integer runtime, Integer credit_amount, Date contract_begin, Integer annuity_rental, Integer residual_debt, String iban, String bic, List<Payment> payments, Rate rate, Customer customer) {
		
		this.contract_number = contract_number;
		this.status = status;
		this.runtime = runtime;
		this.credit_amount = credit_amount;
		this.contract_begin = contract_begin;
		this.annuity_rental = annuity_rental;
		this.residual_debt = residual_debt;
		this.iban = iban;
		this.bic = bic;	
		this.payments = payments;
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
		return contract_number;
	}
	
	public void setContractNumber(String contract_number) {
		this.contract_number = contract_number;
	}
	
	public CreditContractStatus getStatus() {
		return status;
	}
	
	public void setStatus(CreditContractStatus status) {
		this.status = status;
	}
	
	public int getRuntime() {
		return runtime;
	}
	
	public void setRuntime(Integer runtime) {
		this.runtime = runtime;
	}
	
	public int getCreditAmount() {
		return credit_amount;
	}
	
	public void setCreditAmount(Integer credit_amount) {
		this.credit_amount = credit_amount;
	}
	
	public Date getContractBegin() {
		return contract_begin;
	}
	
	public void setContractBegin(Date contract_begin) {
		this.contract_begin = contract_begin;
	}
	
	public int getAnnuityRental() {
		return annuity_rental;
	}
	
	public void setAnnuityRental(Integer annuity_rental) {
		this.annuity_rental = annuity_rental;
	}
	
	public int getResidualDebt() {
		return residual_debt;
	}
	
	public void setResidualDebt(int residual_debt) {
		this.residual_debt = residual_debt;
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
	  
	public List<Payment> getPayments() {
		return payments;
	}

	public void setPayments(List<Payment> payments) {
		this.payments = payments;
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
		return contract_number;
	}
   
}
