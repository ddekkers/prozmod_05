package de.fhwedel.om.services;

import java.io.Serializable;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.fhwedel.om.model.Article;
import de.fhwedel.om.model.BusinessObject;
import de.fhwedel.om.model.Customer;
import de.fhwedel.om.model.Order;
import de.fhwedel.om.model.OrderPosition;

public interface OMServiceAsync {
   
   <TYPE extends BusinessObject<?>> void get(String cls, Serializable id, AsyncCallback<TYPE> callback);
   <TYPE extends BusinessObject<?>> void getQuery(String query, AsyncCallback<List<TYPE>> callback);
   void getAllArticles(AsyncCallback<List<Article>> callback);   
   void getAllCustomers(AsyncCallback<List<Customer>> callback);
   <TYPE extends BusinessObject<?>> void searchCustomersBy(Integer id, String prename, String surname, int age,  AsyncCallback<List<TYPE>> callback);
   
   <TYPE extends BusinessObject<?>> void save(TYPE entity, AsyncCallback<TYPE> callback);
   void save(Order o, AsyncCallback<Order> callback);
   void save(OrderPosition o, AsyncCallback<OrderPosition> callback);
   
}
