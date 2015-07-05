package de.fhwedel.om.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
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
         em.persist( new Customer(4, "Daniel", "Dekkers", "Malzweg 21" , "20535", "Hamburg", new ArrayList<CreditContract>() , new SelfDisclosure()) );
         em.persist( new Customer(5, "Daniel", "Hübner", "Blink 128", "12345", "Hetlingen", new ArrayList<CreditContract>() , new SelfDisclosure()) );
         em.persist( new Customer(6, "Daniel", "Terrabusen", "Blink 129", "12345", "Hetlingen", new ArrayList<CreditContract>() , new SelfDisclosure()) ); 
         em.persist( new CreditContract("1", CreditContractStatus.Angebot, new Integer(2), new Integer(2), new Date(16082015), new Integer(2), new Integer(2), "meineIBAN", "meineBIC", new ArrayList<Payment>(), new Rate(), cust));
         em.persist( new CreditContract("2", CreditContractStatus.Angebot, new Integer(2), new Integer(2), new Date(17082015), new Integer(2), new Integer(2), "meineIBAN", "meineBIC", new ArrayList<Payment>(), new Rate(), cust));
         em.persist( new CreditContract("3", CreditContractStatus.Angebot, new Integer(2), new Integer(2), new Date(18082015), new Integer(2), new Integer(2), "meineIBAN", "meineBIC", new ArrayList<Payment>(), new Rate(), cust));
         
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
   
   @Override
   synchronized public String getNewContractNumber () {
	   List<CreditContract> todaysContracts = getAllCreditContracts().stream().
			   filter(c -> c.getContractBegin().getDay()== LocalDate.now().getDayOfMonth()
			   			&& c.getContractBegin().getMonth() == LocalDate.now().getMonthValue())
			   			.collect(Collectors.toList());
	   return Integer.toString(LocalDate.now().getDayOfMonth())
			+ Integer.toString(LocalDate.now().getMonthValue())
			+ Integer.toString(todaysContracts.size() + 1);
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
	   
	   List<CreditContract> allCreditContracts = getAllCreditContracts();
	   
	   List<CreditContract> filteredCreditContracts = allCreditContracts.stream().filter(c -> c.getContractNumber().equals(credit_contract_number)).collect(Collectors.toList());
	   return filteredCreditContracts;
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
   
   @Override
   synchronized public CreditContract save(CreditContract cc) {
      EntityManager em = OMServiceImpl.getEM();
      em.getTransaction().begin();

      if(cc.getID() != null)
         cc = em.merge(cc);
      else {
         em.persist(cc);
         Customer c = cc.getCustomer();
         c.getCreditContracts().add(cc);
         em.merge(c);
      }
      em.getTransaction().commit();
      return cc;
   }
   
/*   @Override
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