package de.fhwedel.om.model;

import java.util.Date;

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
   	private int credit_contract_id;
   
   	private String contract_number;

	private CreditContractStatus status;
	
	private int runtime;
  
	private int credit_amount;
  
	private Date contract_begin;
  
	// Tilgungsrate
	private int annuity_rental;
  
	// Restschuld
	private int residual_debt;
  
	private String iban;
  
	private String bic;
  
	public CreditContract() {
		this("", CreditContractStatus.proposal, 0, 0, null, 0, 0, "", "");
	}
	
	public CreditContract(String contract_number, CreditContractStatus status, int runtime, int credit_amount, Date contract_begin, int annuity_rental, int residual_debt, String iban, String bic) {
		
		this.contract_number = contract_number;
		this.status = status;
		this.runtime = runtime;
		this.credit_amount = credit_amount;
		this.contract_begin = contract_begin;
		this.annuity_rental = annuity_rental;
		this.residual_debt = residual_debt;
		this.iban = iban;
		this.bic = bic;		
	}
   
	public int getCredit_contract_id() {
	
		return credit_contract_id;
	}

	public void setCredit_contract_id(int credit_contract_id) {
		this.credit_contract_id = credit_contract_id;
	}
	
	public String getContract_number() {
		return contract_number;
	}
	
	public void setContract_number(String contract_number) {
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
	
	public void setRuntime(int runtime) {
		this.runtime = runtime;
	}
	
	public int getCredit_amount() {
		return credit_amount;
	}
	
	public void setCredit_amount(int credit_amount) {
		this.credit_amount = credit_amount;
	}
	
	public Date getContract_begin() {
		return contract_begin;
	}
	
	public void setContract_begin(Date contract_begin) {
		this.contract_begin = contract_begin;
	}
	
	public int getAnnuity_rental() {
		return annuity_rental;
	}
	
	public void setAnnuity_rental(int annuity_rental) {
		this.annuity_rental = annuity_rental;
	}
	
	public int getResidual_debt() {
		return residual_debt;
	}
	
	public void setResidual_debt(int residual_debt) {
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
	
   @Override
   public Integer getID() {
	   
	   return credit_contract_id;
   }

	@Override
	public String getCaption() {
		return "";
	}
   
}
