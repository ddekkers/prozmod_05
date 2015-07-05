package de.fhwedel.om.services;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.fhwedel.om.model.BusinessObject;
import de.fhwedel.om.model.CreditContract;
import de.fhwedel.om.model.Customer;
import de.fhwedel.om.model.Rate;

@RemoteServiceRelativePath("services")
public interface OMService extends RemoteService {
   
   public <TYPE extends BusinessObject<?>> TYPE get(String cls, Serializable id);
   public <TYPE extends BusinessObject<?>> List<TYPE> getQuery(String query);
   public List<Customer> getAllCustomers(); 
   public List<CreditContract> getAllCreditContracts();
   public List<Customer> searchCustomersBy(Integer cust_number, String prename, String surname);
   public List<CreditContract> searchCreditContractBy(String credit_contract_number);
   
   public <TYPE extends BusinessObject<?>> TYPE save(TYPE entity);
   public Integer getNextCustumerNumber();
   public String getNewContractNumber();
   public CreditContract save(CreditContract cc);
   public List<Rate> getPossibleRates(Date c_begin, Integer c_runtime, Integer c_amount);
}
