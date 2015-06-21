package de.fhwedel.om.model;

import java.util.LinkedList;
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
   private Integer customer_id;
   
   @Column(name="name")
   private String prename;
   
   private String surname;
   
   private int age;
   
   @OneToMany(fetch=FetchType.EAGER, mappedBy="customer", cascade={CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.REFRESH})
   private List<Order> orders;
   
   public Customer() {
      this("<neu>", "<neu>", 0);
   }

   public Customer(String prename, String surname, int age) {
      this.prename = prename;
      this.surname = surname;
      this.age = age;
      this.orders = new LinkedList<Order>();
   }
    
   public String getPrename() {
      return prename;
   }

   public void setPrename(String prename) {
      this.prename = prename;
   }

   public String getSurname() {
      return surname;
   }

   public void setSurname(String surname) {
      this.surname = surname;
   }

   public int getAge() {
      return age;
   }

   public void setAge(int age) {
      this.age = age;
   }

   public List<Order> getOrders() {
      return orders;
   }

   public void setOrders(List<Order> orders) {
      this.orders = orders;
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
      return "[Kunde " + this.getSurname() + ", Alter: " + this.getAge() + ", ID: " + this.getID() + "]";
   }
       
}
