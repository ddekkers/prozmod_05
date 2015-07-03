package de.fhwedel.om.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FlushModeType;
import javax.persistence.Persistence;

import org.eclipse.persistence.config.PersistenceUnitProperties;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.fhwedel.om.model.BusinessObject;
import de.fhwedel.om.model.CreditContract;
import de.fhwedel.om.model.Customer;
import de.fhwedel.om.model.Payment;
import de.fhwedel.om.model.Rate;
import de.fhwedel.om.model.SelfDisclosure;
import de.fhwedel.om.services.OMService;
import de.fhwedel.om.types.CreditContractStatus;

@SuppressWarnings("serial")
public class OMServiceImpl 
extends RemoteServiceServlet 
implements OMService {
    
   private static Properties props = new Properties();
   private static EntityManager em;
   
   protected static EntityManager getEM() {
      if(OMServiceImpl.em == null) {
         try {
            if(props.getProperty("regenerate", "0").equals("1")) {
               props.put(PersistenceUnitProperties.DDL_GENERATION, PersistenceUnitProperties.DROP_AND_CREATE);
               props.put(PersistenceUnitProperties.DDL_GENERATION_MODE, PersistenceUnitProperties.DDL_DATABASE_GENERATION); 
            }
            
            EntityManagerFactory emf      = Persistence.createEntityManagerFactory(OMServiceImpl.props.getProperty("persistence_unit", "pu"), props);
            if(emf != null) {
               OMServiceImpl.em           = emf.createEntityManager();
               OMServiceImpl.em.setFlushMode(FlushModeType.COMMIT);         
            }
         } catch (Exception e) {
            e.printStackTrace();
         }
      }
      return OMServiceImpl.em;
   }
   
   static {      
      OMServiceImpl.props = new Properties();
      try {
         OMServiceImpl.props.load( new FileInputStream("om.conf") );
      } catch (IOException e) {
         System.err.println("Failed to load configuration file (" + new File(".\\om.conf").getAbsolutePath() + "), continuing with defaults...");
      }
      
      //Testdatensätze anlegen
      if(OMServiceImpl.props.getProperty("regenerate", "0").equals("1")) {            
         Customer cust = new Customer (7, "Jonas", "Hübner", "Straße 1", "78902", "Hamburg", new ArrayList<CreditContract>(), new SelfDisclosure());
    	 EntityManager em = OMServiceImpl.getEM();
         em.getTransaction().begin();
         em.persist( new CreditContract("1", CreditContractStatus.proposal, 123, 10000, new Date(), 3, 4, "5", "6789", new ArrayList<Payment>(), new Rate(), cust));
         em.persist( new Customer(4, "Daniel", "Dekkers", "Malzweg 21" , "20535", "Hamburg", new ArrayList<CreditContract>() , new SelfDisclosure()) );
         em.persist( new Customer(5, "Daniel", "Hübner", "Blink 128", "12345", "Hetlingen", new ArrayList<CreditContract>() , new SelfDisclosure()) );
         em.persist( new Customer(6, "Daniel", "Terrabusen", "Blink 129", "12345", "Hetlingen", new ArrayList<CreditContract>() , new SelfDisclosure()) ); 
         em.persist( new CreditContract("1", CreditContractStatus.proposal, new Integer(2), new Integer(2), new Date(), new Integer(2), new Integer(2), "meineIBAN", "meineBIC", new ArrayList<Payment>(), new Rate(), cust));
         em.persist( new CreditContract("2", CreditContractStatus.proposal, new Integer(2), new Integer(2), new Date(), new Integer(2), new Integer(2), "meineIBAN", "meineBIC", new ArrayList<Payment>(), new Rate(), cust));
         em.persist( new CreditContract("3", CreditContractStatus.proposal, new Integer(2), new Integer(2), new Date(), new Integer(2), new Integer(2), "meineIBAN", "meineBIC", new ArrayList<Payment>(), new Rate(), cust));
  	   
         em.getTransaction().commit();
      }
   }

   @SuppressWarnings("unchecked")
   @Override
   synchronized public List<Customer> getAllCustomers() {       
      EntityManager em = OMServiceImpl.getEM();
      List<Customer> allCustomers = em.createNamedQuery("getAllCustomers").getResultList();      
      Collections.sort(allCustomers, 
			   (Customer cust1, Customer cust2) -> cust1.getCustomerNumber().compareTo(cust2.getCustomerNumber()));
      return allCustomers;
   }
   
   @SuppressWarnings("unchecked")
   synchronized public <TYPE extends BusinessObject<?>> List<TYPE> getQuery(String query) {
      EntityManager em  = OMServiceImpl.getEM();
      return (List<TYPE>)em.createNamedQuery(query).getResultList();
   }

   @SuppressWarnings("unchecked")
   synchronized public <TYPE extends BusinessObject<?>> TYPE get(String cls, Serializable id) {
      Class<?> cl = null;
      try {
         cl = Class.forName(cls);
      } catch (ClassNotFoundException e) {         
         e.printStackTrace();
      }
      EntityManager em = OMServiceImpl.getEM();
      return (TYPE)em.find(cl, id);
   }
   
   @Override
   synchronized public List<Customer> searchCustomersBy(Integer cust_number, String prename,
		   												String surname) {
	   List<Customer> allCustomers = getAllCustomers();

	   List<Customer> filteredCustomers = allCustomers.stream().filter(c -> c.getCustomerNumber().equals(cust_number)
			   															 || c.getPrename().equals(prename)
			   															 || c.getSurname().equals(surname)).
			   															 collect(Collectors.toList());
	   
	   return filteredCustomers;
   }
   
   @Override
   synchronized public Integer getNextCustumerNumber () {
	   return getAllCustomers().stream().max((cust1, cust2)
			   							  -> (cust1.getCustomerNumber() - cust2.getCustomerNumber()))
			   							.get()
			   							.getCustomerNumber() + 1;
   }
   
   @SuppressWarnings("unchecked")
   @Override
   public List<CreditContract> getAllCreditContracts() {
	   EntityManager em = OMServiceImpl.getEM();
	   List<CreditContract> allCreditContracts = em.createNamedQuery("getAllCreditContracts").getResultList();
	   Collections.sort(allCreditContracts, (CreditContract cc1, CreditContract cc2) -> cc1.getContractNumber().compareTo(cc2.getContractNumber()));
	   return allCreditContracts;
   }

   @Override
   public List<CreditContract> searchCreditContractBy(String credit_contract_number) {
	   
	   return null;
   }
   
   @Override
   synchronized public <TYPE extends BusinessObject<?>> TYPE save(TYPE entity) {
      EntityManager em = OMServiceImpl.getEM();
      em.getTransaction().begin();

      if(entity.getID() != null)
         em.merge(entity);
      else
         em.persist(entity);         
      em.getTransaction().commit();
      return entity;
   }
   
/*   @Override
   synchronized public Order save(Order o) {
      EntityManager em = OMServiceImpl.getEM();
      em.getTransaction().begin();

      if(o.getID() != null)
         o = em.merge(o);
      else {
         em.persist(o);
         Customer c = o.getCustomer();
         //c.getOrders().add(o);
         em.merge(c);
      }
      em.getTransaction().commit();
      return o;
   }
   
   @Override
   synchronized public OrderPosition save(OrderPosition pos) {
      EntityManager em = OMServiceImpl.getEM();
      em.getTransaction().begin();

      if(pos.getID() != null)
         pos = em.merge(pos);
      else {
         em.persist(pos);
         Order o = pos.getOrder();
         o.getPositions().add(pos);
         em.merge(o);
      }
      em.getTransaction().commit();
      return pos;
   }*/
   
}