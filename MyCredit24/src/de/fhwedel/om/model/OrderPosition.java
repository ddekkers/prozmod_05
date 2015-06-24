package de.fhwedel.om.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@SuppressWarnings("serial")
@Entity
@NamedQueries({
   @NamedQuery(
           name="getAllOrderPositions",
           query="SELECT op FROM OrderPosition op"
   )
})
public class OrderPosition implements BusinessObject<Integer> {
   
   @Id
   @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CUST_SEQ")
   @Column(name="orderpos_id")
   private Integer orderpos_id;
   
   private int amount;
   
   @ManyToOne(cascade={CascadeType.REFRESH})   
   private Order order;
   
   @ManyToOne(cascade={CascadeType.REFRESH})
   private Article article;

   public OrderPosition() {
      super();
   }

   public OrderPosition(Order o, Article a, int amount) {
      this.order = o;
      this.article = a;
      this.amount = amount;
   }

   public Order getOrder() {
      return order;
   }

   public void setOrder(Order order) {
      this.order = order;
   }

   public Article getArticle() {   
      return article;
   }

   public void setArticle(Article article) {
      this.article = article;
   }

   public int getAmount() {
      return amount;
   }

   public void setAmount(int amount) {
      this.amount = amount;
   }
    
   // Object-Methoden
    
   @Override
   public String toString() {
      return this.getCaption();
   }
   
   // BusinessObject-Methoden 

   @Override
   public Integer getID() {
      return this.orderpos_id;
   }   

   @Override
   public String getCaption() { 
      return this.amount + "x " + (this.article != null ? this.article.getCaption() : "-");
   }
   
}
