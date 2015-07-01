package de.fhwedel.om.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@SuppressWarnings("serial")
@Entity
@NamedQueries({
   @NamedQuery(
           name="getAllDebtors",
           query="SELECT a FROM Debtor a"
   )
})
public class Debtor implements BusinessObject<Integer> {

   @Id
   @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CUST_SEQ")
   @Column(name="debtor_id")
   private int debtor_id;
   
   private int debtor_number;
   
   private String surname;
   
   private String name;
   
   private String street;
   
   private String postcode;
   
   private String city;
   
   public int getDebtor_number() {
	   return debtor_number;
   }

   public void setDebtor_number(int debtor_number) {
	   this.debtor_number = debtor_number;
   }

   public String getSurname() {
	   return surname;
   }

   public void setSurname(String surname) {
	   this.surname = surname;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
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

   public Debtor() {
	   this(0, "", "", "", "", "");
   }
      
   public Debtor(int debtor_number, String surname, String name,  String street, String postcode, String city) {
	   this.debtor_number = debtor_number;
	   this.surname = surname;
	   this.name = name;
	   this.street = street;
	   this.postcode = postcode;
	   this.city = city;
   }

   // Object-Methoden

   @Override
   public String toString() {
      return this.getCaption();
   }
   
   // BusinessObject-Methoden 
   
   @Override
   public Integer getID() {
      return this.debtor_id;
   }

   @Override
   public String getCaption() {
      return "";
   }
   
}
