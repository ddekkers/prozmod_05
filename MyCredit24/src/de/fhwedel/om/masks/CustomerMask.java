package de.fhwedel.om.masks;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import de.fhwedel.om.model.Customer;
import de.fhwedel.om.widgets.BOSelectListBox;

public class CustomerMask extends BusinessMask<Customer> implements Editor<Customer> {

   private boolean show_only;
   
   // Customer-Editor
   interface CustomerEditorDriver extends SimpleBeanEditorDriver<Customer, CustomerMask> {}
   private final CustomerEditorDriver editorDriver = GWT.create(CustomerEditorDriver.class);

   // UiBinder    
   interface CustomerMaskUiBinder extends UiBinder<Widget, CustomerMask> {}
   private final static CustomerMaskUiBinder uiBinder = GWT.create(CustomerMaskUiBinder.class);

   // UiBinder (Widget-Instanzvariablen)    
   @Path("prename") @UiField TextBox pname;
   @Path("surname") @UiField TextBox sname;
   @Ignore @UiField BOSelectListBox<Customer, Integer> customers;

   @Ignore @UiField IntegerBox search_cust_number;
   @Ignore @UiField TextBox search_sname;
   @Ignore @UiField TextBox search_pname;
      
   
   
   //Buttons
   @UiField Button search_customer;
   
   @UiField Button select_customer;
           
   //Labels
   public CustomerMask() {
	   this(new Customer());	   
   }
   
   public void onAddClick(ClickEvent event) {
	   Window.alert("Hallo!");
   }
   
   public CustomerMask(Customer c) {
      this(c, false);
   }

   public CustomerMask(Customer c, boolean show_only) {        
      initWidget(uiBinder.createAndBindUi(this));
      this.setMode(show_only);
      this.editorDriver.initialize(this);
      this.refreshCustomers();
      this.setBO(c);
   }
   
   protected void setMode(boolean show_only) {
      this.show_only = show_only;
      this.pname.setReadOnly(show_only);
      this.sname.setReadOnly(show_only);
      //this.age.setEnabled(!show_only);

      
      this.search_customer.setVisible(!show_only);
   }
    
   public void setBO(Customer c) {
      super.setBO(c);
      this.editorDriver.edit(c);
   }
    
   @Override
   protected void saveBO() {
      this.editorDriver.flush();
      this.getService().save(this.getBO(), new AsyncCallback<Customer>() {         
         @Override
         public void onSuccess(Customer result) {
            CustomerMask.this.refreshCustomers();          
            CustomerMask.this.setBO(result);
         }         
         @Override
         public void onFailure(Throwable caught) {
            Window.alert("Fehler beim Speichern des Kunden.");        
         }
      });
   }
    
   protected void refreshCustomers() {
      this.getService().getAllCustomers(new AsyncCallback<List<Customer>>() {         
         @Override
         public void onSuccess(List<Customer> result) {
            customers.setAcceptableValues(result);            
         }         
         @Override
         public void onFailure(Throwable caught) {
            Window.alert("Fehler beim Laden der Kunden.");        
         }
      });      
   }
   
   
   @UiHandler("select_customer")
   protected void onSelectCustomerClick(ClickEvent event) {
      this.setBO(this.customers.getValue());
   }

   @UiHandler("customers")
   protected void onSelectCustomerClick(DoubleClickEvent event) {
      this.setBO(this.customers.getValue());
   }

   
   @UiHandler("search_customer")
   protected void onSearchCustomerClick(ClickEvent event) {
	   
	   this.getService().searchCustomersBy( search_cust_number.getValue(),
			   								search_sname.getText(),
			   								search_pname.getText(),
			   								(new AsyncCallback<List<Customer>>() {         
	         @Override
	         public void onSuccess(List<Customer> result) {
	            customers.setAcceptableValues(result);            
	         }         
	         @Override
	         public void onFailure(Throwable caught) {
	            Window.alert("Fehler beim Laden der Kunden.");        
	         }
	   }));
   }
   
   @Override
   public void refresh() {
      this.refreshCustomers();
      super.refresh();
   }
      
}
