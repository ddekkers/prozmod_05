package de.fhwedel.om.model;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name="OrderTab")
@NamedQueries({
   @NamedQuery(
           name="getAllOrders",
           query="SELECT o FROM Order o"
   )
})
public class Order implements BusinessObject<Integer> {
   
   public enum PaymentMethod { 
      
      Bar("Bar"), CreditCard ("Kreditkarte"), Invoice ("Rechnung");
      
      private final String caption;
      private PaymentMethod(String caption) {
         this.caption = caption;
      };
      public String toString() {
         return this.caption;
      }
   };
   
   @Id
   @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CUST_SEQ")
   @Column(name="order_id")
   private Integer order_id;
   
   private String order_caption;

   @Enumerated(EnumType.STRING)
   private PaymentMethod payment_method;

   @ManyToOne(cascade={CascadeType.REFRESH})
   private Customer customer;
   
   @OneToMany(fetch=FetchType.EAGER, mappedBy="order", cascade={CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.REFRESH})
   private List<OrderPosition> positions;
   
   public Order() {
      this(null, "<neu>");      
   }

   public Order(Customer customer, String order_caption) {
      this.customer = customer;
      this.order_caption = order_caption;
      this.payment_method = PaymentMethod.Bar;
      this.positions = new LinkedList<OrderPosition>();
   }

   public Customer getCustomer() {
      return customer;
   }

   public void setCustomer(Customer customer) {
      this.customer = customer;
   }

   public String getOrderCaption() {
      return order_caption;
   }

   public void setOrderCaption(String order_caption) {
      this.order_caption = order_caption;
   }
   
   public PaymentMethod getPaymentMethod() {
      return payment_method;
   }

   public void setPaymentMethod(PaymentMethod payment_method) {
      this.payment_method = payment_method;
   }

   public List<OrderPosition> getPositions() {
      return positions;
   }

   public void setPositions(List<OrderPosition> positions) {
      this.positions = positions;
   }
   
   // Object-Methoden
   
   @Override
   public String toString() {
      return this.getCaption();
   }

   // BusinessObject-Methoden 
    
   @Override
   public Integer getID() {
      return this.order_id;
   }

   @Override
   public String getCaption() {
      return "[Auftrag " +  this.getOrderCaption() + "]";
   }   

}
