package de.fhwedel.om.services;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.fhwedel.om.model.BusinessObject;
import de.fhwedel.om.model.CreditContract;
import de.fhwedel.om.model.Customer;
import de.fhwedel.om.model.Payment;
import de.fhwedel.om.model.Rate;
import de.fhwedel.om.model.SelfDisclosure;

public interface OMServiceAsync {
   
   <TYPE extends BusinessObject<?>> void get(String cls, Serializable id, AsyncCallback<TYPE> callback);
   <TYPE extends BusinessObject<?>> void getQuery(String query, AsyncCallback<List<TYPE>> callback);
   void getAllCustomers(AsyncCallback<List<Customer>> callback);
   void getAllCreditContracts(AsyncCallback<List<CreditContract>> callback);
   void getCreditContractsOf(Customer cust, AsyncCallback<List<CreditContract>> callback);
   void getPossibleRates(Date c_begin, Integer c_runtime, Integer c_amount,
		AsyncCallback<List<Rate>> callback);
   <TYPE extends BusinessObject<?>> void searchCustomersBy(Integer cust_number, String prename, String surname, 
		   AsyncCallback<List<TYPE>> callback);
   
   <TYPE extends BusinessObject<?>> void save(TYPE entity, AsyncCallback<TYPE> callback);
   void getNextCustumerNumber(AsyncCallback<Integer> callback);
   void save(CreditContract cc, AsyncCallback<CreditContract> callback);
   void save(Customer cust, AsyncCallback<Customer> callback);
   void save(SelfDisclosure sd, AsyncCallback<SelfDisclosure> callback);
   void searchCreditContractBy(String credit_contract_number, AsyncCallback<List<CreditContract>> callback);
   void evaluate(Integer modeOfEmployment, Integer monthNet, AsyncCallback<Integer> callback);
   void getAllPaymentsByCreditContractId(Integer id, AsyncCallback<List<Payment>> asyncCallback);
   void saveOutpayment(Payment payment, AsyncCallback<Payment> asyncCallback);
}
