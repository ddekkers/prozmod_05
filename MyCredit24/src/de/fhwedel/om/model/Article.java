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
           name="getAllArticles",
           query="SELECT a FROM Article a"
   )
})
public class Article implements BusinessObject<Integer> {

   @Id
   @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CUST_SEQ")
   @Column(name="article_id")
   private int article_id;
   
   private String name;
   
   private int price;
   
   public Article() {
      this("<neu>", 0);
   }
      
   public Article(String name, int price) {
      this.name = name;
      this.price = price;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public int getPrice() {
      return price;
   }

   public void setPrice(int price) {
      this.price = price;
   }
   
   // Object-Methoden

   @Override
   public String toString() {
      return this.getCaption();
   }
   
   // BusinessObject-Methoden 
   
   @Override
   public Integer getID() {
      return this.article_id;
   }

   @Override
   public String getCaption() {
      return this.getName() + " (EUR " + (this.getPrice() / 100) + "." + (this.getPrice() % 100) + ")";
   }
   
}
