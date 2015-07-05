package de.fhwedel.om.services;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.fhwedel.om.model.BusinessObject;
import de.fhwedel.om.model.CreditContract;
import de.fhwedel.om.model.Customer;
import de.fhwedel.om.model.Rate;

public interface OMServiceAsync {
   
   <TYPE extends BusinessObject<?>> void get(String cls, Serializable id, AsyncCallback<TYPE> callback);
   <TYPE extends BusinessObject<?>> void getQuery(String query, AsyncCallback<List<TYPE>> callback);
   void getAllCustomers(AsyncCallback<List<Customer>> callback);
   void getAllCreditContracts(AsyncCallback<List<CreditContract>> callback);
   void getPossibleRates(Date c_begin, Integer c_runtime, Integer c_amount,
		AsyncCallback<List<Rate>> callback);
   <TYPE extends BusinessObject<?>> void searchCustomersBy(Integer cust_number, String prename, String surname, 
		   AsyncCallback<List<TYPE>> callback);
   
   <TYPE extends BusinessObject<?>> void save(TYPE entity, AsyncCallback<TYPE> callback);
   void getNextCustumerNumber(AsyncCallback<Integer> callback);
   void getNewContractNumber(AsyncCallback<String> callback);
   void save(CreditContract cc, AsyncCallback<CreditContract> callback);
   void searchCreditContractBy(String credit_contract_number, AsyncCallback<List<CreditContract>> callback);
   
}
