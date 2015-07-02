package de.fhwedel.om.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
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

import de.fhwedel.om.model.Article;
import de.fhwedel.om.model.BusinessObject;
import de.fhwedel.om.model.Customer;
import de.fhwedel.om.model.Order;
import de.fhwedel.om.model.OrderPosition;
import de.fhwedel.om.services.OMService;

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
      
      if(OMServiceImpl.props.getProperty("regenerate", "0").equals("1")) {            
         EntityManager em = OMServiceImpl.getEM();
         em.getTransaction().begin();
         em.persist( new Article("Artikel 1", 100) );
         em.persist( new Article("Artikel 2", 1000) );
         em.persist( new Article("Artikel 3", 500) );
         em.persist( new Customer("Daniel", "Dekkers", 25) );
         em.persist( new Customer("Jonas", "Hübner", 30) );
         em.persist( new Customer("Eugen", "Geist", 55) ); 
         em.getTransaction().commit();
      }      
   }

   @SuppressWarnings("unchecked")
   @Override
   synchronized public List<Article> getAllArticles() {
      EntityManager em = OMServiceImpl.getEM();
      return em.createNamedQuery("getAllArticles").getResultList();      
   }
    
   @SuppressWarnings("unchecked")
   @Override
   synchronized public List<Customer> getAllCustomers() {       
      EntityManager em = OMServiceImpl.getEM();
      return em.createNamedQuery("getAllCustomers").getResultList();      
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
   
   @SuppressWarnings("unchecked")
   @Override
   synchronized public List<Customer> searchCustomersBy(Integer id, String prename,
		   												String surname, int age) {
	   List<Customer> allCustomers = getAllCustomers();

	   List<Customer> filteredCustomers = allCustomers.stream().filter(c -> c.getID().equals(id)
			   															 || c.getPrename().equals(prename)
			   															 || c.getSurname().equals(surname)
			   															 || c.getAge() == age).
			   															 collect(Collectors.toList());

	   return filteredCustomers;
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
   synchronized public Order save(Order o) {
      EntityManager em = OMServiceImpl.getEM();
      em.getTransaction().begin();

      if(o.getID() != null)
         o = em.merge(o);
      else {
         em.persist(o);
         Customer c = o.getCustomer();
         c.getOrders().add(o);
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
   }
   
}