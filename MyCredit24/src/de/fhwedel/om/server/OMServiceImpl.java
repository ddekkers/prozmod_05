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
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FlushModeType;
import javax.persistence.Persistence;

import org.eclipse.persistence.config.PersistenceUnitProperties;

import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ibm.icu.text.SimpleDateFormat;

import de.fhwedel.om.masks.DateHandler;
import de.fhwedel.om.model.BusinessObject;
import de.fhwedel.om.model.CreditContract;
import de.fhwedel.om.model.Customer;
import de.fhwedel.om.model.Payment;
import de.fhwedel.om.model.Rate;
import de.fhwedel.om.model.SelfDisclosure;
import de.fhwedel.om.services.OMService;
import de.fhwedel.om.types.CreditContractStatus;
import de.fhwedel.om.types.ModeOfEmployment;
import de.fhwedel.om.types.PaymentType;
import de.fhwedel.om.types.ValidityLevel;

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
         SelfDisclosure sd1 = new SelfDisclosure(new Date(), "Student", ModeOfEmployment.employee, false, "FH Wedel", 1000, null, null, null, null);
         SelfDisclosure sd2 = new SelfDisclosure(new Date(), "Marketing - Experte", ModeOfEmployment.employee, false, "otto", 10000, null, null, null, null);
         SelfDisclosure sd3 = new SelfDisclosure(new Date(), "Anwendungsentwickler", ModeOfEmployment.employee, false, "innovas", 5000, null, null, null, null);
    	 EntityManager em = OMServiceImpl.getEM();
         em.getTransaction().begin();
         Customer c1 = new Customer(4, "Daniel", "Dekkers", "Malzweg 21" , "20535", "Hamburg", sd1);
         Customer c2 = new Customer(5, "Daniel", "Hübner", "Blink 128", "12345", "Hetlingen", sd2);
         Customer c3 = new Customer(6, "Daniel", "Terrabusen", "Blink 129", "12345", "Hetlingen", sd3); 
         
         c1.setSelfDisclosure(sd1);         
         c2.setSelfDisclosure(sd2);         
         c3.setSelfDisclosure(sd3);
         
         em.persist( c1 );
         em.persist( c2 );
         em.persist( c3 ); 
         
         GregorianCalendar cal = new GregorianCalendar(2015, 01, 01);
         Date begin = cal.getTime();
         cal = new GregorianCalendar(2015, 12, 31);
         Date end = cal.getTime();
         
         Rate test_rate_1 = new Rate("R1_2", 0.11, 2, 1000, 1000000, ValidityLevel.A, begin, end);
         em.persist( new Rate("R1_10", 0.12, 10, 1000, 1000000, ValidityLevel.A, begin, end));
         em.persist( new Rate("R1_9", 0.13, 9, 1000, 1000000, ValidityLevel.A, begin, end));
         em.persist( new Rate("R1_8", 0.14, 8, 1000, 1000000, ValidityLevel.A, begin, end));
         em.persist( new Rate("R1_7", 0.15, 7, 1000, 1000000, ValidityLevel.A, begin, end));
         em.persist( new Rate("R1_6", 0.16, 6, 1000, 1000000, ValidityLevel.A, begin, end));
         em.persist( new Rate("R1_5", 0.17, 5, 1000, 1000000, ValidityLevel.A, begin, end));
         em.persist( new Rate("R1_4", 0.18, 4, 1000, 1000000, ValidityLevel.A, begin, end));
         em.persist( new Rate("R1_3", 0.19, 3, 1000, 1000000, ValidityLevel.A, begin, end));
         em.persist( test_rate_1);
         em.persist( new Rate("R1_1", 0.2, 1, 1000, 1000000, ValidityLevel.A, begin, end));
         
         em.persist( new Rate("R1_10", 0.2, 10, 1000, 1000000, ValidityLevel.B, begin, end));
         em.persist( new Rate("R1_9", 0.21, 9, 1000, 1000000, ValidityLevel.B, begin, end));
         em.persist( new Rate("R1_8", 0.22, 8, 1000, 1000000, ValidityLevel.B, begin, end));
         em.persist( new Rate("R1_7", 0.23, 7, 1000, 1000000, ValidityLevel.B, begin, end));
         em.persist( new Rate("R1_6", 0.24, 6, 1000, 1000000, ValidityLevel.B, begin, end));
         em.persist( new Rate("R1_5", 0.25, 5, 1000, 1000000, ValidityLevel.B, begin, end));
         em.persist( new Rate("R1_4", 0.26, 4, 1000, 1000000, ValidityLevel.B, begin, end));
         em.persist( new Rate("R1_3", 0.27, 3, 1000, 1000000, ValidityLevel.B, begin, end));
         em.persist( new Rate("R1_2", 0.28, 2, 1000, 1000000, ValidityLevel.B, begin, end));
         em.persist( new Rate("R1_1", 0.29, 1, 1000, 1000000, ValidityLevel.B, begin, end));
         
         em.persist( new Rate("R1_10", 0.2, 10, 1000, 1000000, ValidityLevel.C, begin, end));
         em.persist( new Rate("R1_9", 0.21, 9, 1000, 1000000, ValidityLevel.C, begin, end));
         em.persist( new Rate("R1_8", 0.22, 8, 1000, 1000000, ValidityLevel.C, begin, end));
         em.persist( new Rate("R1_7", 0.23, 7, 1000, 1000000, ValidityLevel.C, begin, end));
         em.persist( new Rate("R1_6", 0.24, 6, 1000, 1000000, ValidityLevel.C, begin, end));
         em.persist( new Rate("R1_5", 0.25, 5, 1000, 1000000, ValidityLevel.C, begin, end));
         em.persist( new Rate("R1_4", 0.26, 4, 1000, 1000000, ValidityLevel.C, begin, end));
         em.persist( new Rate("R1_3", 0.27, 3, 1000, 1000000, ValidityLevel.C, begin, end));
         em.persist( new Rate("R1_2", 0.28, 2, 1000, 1000000, ValidityLevel.C, begin, end));
         em.persist( new Rate("R1_1", 0.29, 1, 1000, 1000000, ValidityLevel.C, begin, end));
         
         
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
   synchronized public Integer evaluate(Integer modeOfEmployment, Integer monthNet){
	   monthNet = monthNet > 8000 ? 8000 : monthNet;
	   Integer monthNetEval = new Double(((8000 - monthNet) / 1000)).intValue();
	   monthNetEval = monthNetEval == 0 ? 1 : monthNetEval;
	   Integer value = modeOfEmployment * monthNetEval;
	   double valueSqrt = Math.sqrt((double) value) * 100;
	   return Math.round((float)valueSqrt);
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
   public List<CreditContract> getCreditContractsOf(Customer cust) {
	   List<CreditContract> allContracts = getAllCreditContracts();
	   return allContracts.stream().filter(c -> c.getCustomer().getCustomerNumber() == cust.getCustomerNumber())
			   .collect(Collectors.toList());
   }

   @Override
   public List<CreditContract> searchCreditContractBy(String credit_contract_number) {
	   
	   List<CreditContract> allCreditContracts = getAllCreditContracts();
	   
	   List<CreditContract> filteredCreditContracts = allCreditContracts.stream().filter(c -> c.getContractNumber().equals(credit_contract_number))
			   .collect(Collectors.toList());
	   return filteredCreditContracts;
   }
   	
   @Override
   synchronized public <TYPE extends BusinessObject<?>> TYPE save(TYPE entity) {
      EntityManager em = OMServiceImpl.getEM();
      em.getTransaction().begin();
      

      if(entity.getID() != null) {
         em.merge(entity);
         
      }
      else {
    	  em.persist(entity);   
      
      }
      
      em.getTransaction().commit();
      return entity;
   }

   @Override
   synchronized public CreditContract save(CreditContract cc) {
      EntityManager em = OMServiceImpl.getEM();
      em.getTransaction().begin();
      if(cc.getID() != null) {
         cc = em.merge(cc);
      }
      else {
         em.persist(cc);
      }
      em.getTransaction().commit();
      return cc;
   }
   
   @Override
   synchronized public Customer save(Customer cust) {
	   EntityManager em = OMServiceImpl.getEM();
	   em.getTransaction().begin();
	   
	   if(cust.getID() != null) {
		   cust = em.merge(cust);
	   } else {
		   em.persist(cust);
	   }
	   em.getTransaction().commit();
	   return cust;
   }
   
   @Override
   synchronized public SelfDisclosure save(SelfDisclosure sd) {
	   EntityManager em = OMServiceImpl.getEM();
	   em.getTransaction().begin();
	   if(sd.getID() != null) {
		   sd = em.merge(sd);
	   } else {
		   em.persist(sd);
	   }
	   em.getTransaction().commit();
	   return sd;
   }
   
   @SuppressWarnings("unchecked")
   	@Override
	public List<Rate> getPossibleRates(Date c_begin, Integer c_runtime, Integer c_amount) {
	   EntityManager em = OMServiceImpl.getEM();
	   List<Rate> allRates = em.createNamedQuery("getAllRates").getResultList();
	   List<Rate> filteredRates = new ArrayList<Rate>();
	   for (int i = 0; i < allRates.size(); ++i) {
		   Rate act = allRates.get(i);
		   boolean unlimited_begin = act.getDateValidFrom() == null;
		   boolean unlimited_end = act.getDateValidTo() == null;
		   if (		act.getRuntime() == c_runtime && 
				   	(act.getDateValidFrom().before(c_begin) || unlimited_begin) &&  (c_begin.before(act.getDateValidTo()) || unlimited_end) &&
				   	act.getCreditAmountFrom() <= c_amount && c_amount < act.getCreditAmountTo() && 
				   	act.getValidityLevel().equals(ValidityLevel.A)) {
			   
			   filteredRates.add(act);
		   }
	   }
	   return filteredRates;
	}

	@Override
	public List<Payment> getAllPaymentsByCreditContractId(Integer id) {
	   EntityManager em = OMServiceImpl.getEM();
	   List<Payment> allPayments = em.createNamedQuery("getAllPayments").getResultList();
	   List<Payment> filteredPayments = new ArrayList<Payment>();
		
	   for (int i = 0; i < allPayments.size() && id != null; ++i) {
		   
		   if (id.equals(allPayments.get(i).getCreditContract().getID())) {
			   
			   filteredPayments.add(allPayments.get(i));
		   }
		   
	   }
	   return filteredPayments;
	}

	@Override
	public Payment saveOutpayment(Payment payment) {
		List<Payment> payments = getAllPaymentsByCreditContractId(payment.getCreditContract().getID());
		for (int i = 0; i < payments.size(); ++i) 
			if (payments.get(i).getType().equals(PaymentType.Auszahlung))
				return null;
		return save(payment);
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