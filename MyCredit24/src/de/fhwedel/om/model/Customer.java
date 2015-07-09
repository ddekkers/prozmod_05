package de.fhwedel.om.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@SuppressWarnings("serial")
@Entity
@NamedQueries({
   @NamedQuery(
         name="getAllCustomers",
         query="SELECT c FROM Customer c"
   )
})
public class Customer implements BusinessObject<Integer> {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CUST_SEQ")
	@Column(name="customer_id")
	private int customer_id;
	   
	private Integer customerNumber;
	   
	private String surname;
   
	private String prename;
   
	private String street;
   
	private String postcode;
   
	private String city;
	
	@OneToOne(fetch=FetchType.EAGER, mappedBy = "customer", cascade = CascadeType.PERSIST)
	private SelfDisclosure selfDisclosure;
   
	public Integer getCustomerNumber() {
		return customerNumber;
	}
   
	public void setCustomerNumber(int customer_number) {
		this.customerNumber = customer_number;
	}
   
	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getPrename() {
		return prename;
	}

	public void setPrename(String prename) {
		this.prename = prename;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}


	public SelfDisclosure getSelfDisclosure() {
		return selfDisclosure;
	}

	public void setSelfDisclosure(SelfDisclosure self_disclosure) {
		this.selfDisclosure = self_disclosure;
	}
	   
	public Customer() {
		this(null, "", "", "", "", "", null);
	}
      
	public Customer(Integer customer_number, String prename, String surname,  String street, String postcode, String city, SelfDisclosure self_disclosure) {
		this.customerNumber = customer_number;
		this.surname = surname;
		this.prename = prename;
		this.street = street;
		this.postcode = postcode;
		this.city = city;
		this.selfDisclosure = self_disclosure;
	}

	// Object-Methoden

	@Override
	public String toString() {
		return this.getCaption();
	}
   
	// BusinessObject-Methoden 
   
	@Override
	public Integer getID() {
		return this.customer_id;
	}

	@Override
	public String getCaption() {
		return "[" + this.getCustomerNumber() + "] " 
			  + this.getSurname()
			  + ", "
			  + this.getPrename();
	}
}
