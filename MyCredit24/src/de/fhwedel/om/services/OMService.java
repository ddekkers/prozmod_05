package de.fhwedel.om.services;

import java.io.Serializable;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.fhwedel.om.model.Article;
import de.fhwedel.om.model.BusinessObject;
import de.fhwedel.om.model.Customer;
import de.fhwedel.om.model.Order;
import de.fhwedel.om.model.OrderPosition;

@RemoteServiceRelativePath("services")
public interface OMService extends RemoteService {
   
   public <TYPE extends BusinessObject<?>> TYPE get(String cls, Serializable id);
   public <TYPE extends BusinessObject<?>> List<TYPE> getQuery(String query);
   public List<Article> getAllArticles();
   public List<Customer> getAllCustomers(); 
   public List<Customer> searchCustomersBy(Integer id, String prename, String surname, int age);
   
   public <TYPE extends BusinessObject<?>> TYPE save(TYPE entity);
   public Order save(Order o);
   public OrderPosition save(OrderPosition o);
   
}
