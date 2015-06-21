package de.fhwedel.om.masks;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Widget;

import de.fhwedel.om.model.Article;
import de.fhwedel.om.model.OrderPosition;
import de.fhwedel.om.widgets.BOSelectListBox;

public class OrderPositionMask extends BusinessMask<OrderPosition> implements Editor<OrderPosition> {
   
   // OrderPosition-Editor
   interface OrderPositionEditorDriver extends SimpleBeanEditorDriver<OrderPosition, OrderPositionMask> {}
   private final OrderPositionEditorDriver editorDriver = GWT.create(OrderPositionEditorDriver.class);
    
   // UiBinder    
   interface OrderPositionMaskUiBinder extends UiBinder<Widget, OrderPositionMask> {}
   private final static OrderPositionMaskUiBinder uiBinder = GWT.create(OrderPositionMaskUiBinder.class);    
    
   // UiBinder (Widget-Instanzvariablen) 
   @UiField IntegerBox amount;
   @Ignore @UiField BOSelectListBox<Article, Integer> article;
    
   @UiField Button save_position;
   @UiField Button cancel;
       
   public OrderPositionMask(OrderPosition pos) {
      initWidget(uiBinder.createAndBindUi(this));
      this.editorDriver.initialize(this);
      this.refreshArticles();
      this.setBO(pos);
   }
    
   public void setBO(OrderPosition pos) {
      super.setBO(pos);
      this.editorDriver.edit(pos);
      this.article.setValue(this.getBO().getArticle());
   }

   @Override
   protected void saveBO() {
      this.editorDriver.flush();
      this.getBO().setArticle(this.article.getValue());
      this.getService().save(this.getBO(), new AsyncCallback<OrderPosition>() {         
         @Override
         public void onSuccess(OrderPosition result) {
            OrderPositionMask.this.setBO(result);
            OrderPositionMask.this.fireSaved();            
         }         
         @Override
         public void onFailure(Throwable caught) {
            Window.alert("Fehler beim Speichern (" + OrderPositionMask.this.getBO().getClass().getSimpleName() + ").");        
         }
      });
   }   
    
   @UiHandler("save_position")
   protected void saveOrderPosition(ClickEvent e) {      
      if(this.article.getValue() != null) {
         this.saveBO();
      }
   }
    
   @UiHandler("cancel")
   protected void cancel(ClickEvent e) {
      this.fireCancelled();
   }
    
   protected void refreshArticles() {
      this.getService().getAllArticles(new AsyncCallback<List<Article>>() {         
         @Override
         public void onSuccess(List<Article> result) {
            result.add(0, null);
            OrderPositionMask.this.article.setAcceptableValues(result);
            if(OrderPositionMask.this.getBO() != null)
               OrderPositionMask.this.article.setValue(OrderPositionMask.this.getBO().getArticle());
         }         
         @Override
         public void onFailure(Throwable caught) {
            Window.alert("Fehler beim Laden der Artikel.");        
         }
      });      
   }
   
}
