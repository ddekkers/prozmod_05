package de.fhwedel.om.masks;

import java.util.Date;
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
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import de.fhwedel.om.model.CreditContract;
import de.fhwedel.om.model.Customer;
import de.fhwedel.om.model.SelfDisclosure;
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
   @Ignore @UiField IntegerBox search_cust_number;
   @Ignore @UiField TextBox search_sname;
   @Ignore @UiField TextBox search_pname;
   @Ignore @UiField BOSelectListBox<Customer, Integer> customers;
   
   @Path("customerNumber") @UiField IntegerBox cust_number;
   @Path("prename") @UiField TextBox pname;
   @Path("surname") @UiField TextBox sname;
   @UiField TextBox street;
   @UiField TextBox postcode;
   @UiField TextBox city;
   
   @UiField CaptionPanel selfDisclosure;
   @UiField BOSelectListBox<CreditContract, Integer> creditContracts;
   
   //Buttons
   @UiField Button search_customer;   
   @UiField Button select_customer;
   @UiField Button new_customer;
   @UiField Button save_customer;
   @UiField Button edit_selfDisclosure;
   @UiField Button new_creditContract;
           
   //Labels
   public CustomerMask() {
	   this(new Customer());	   
   }
   
   public CustomerMask(Customer c) {
      this(c, false);
   }

   public CustomerMask(Customer c, boolean show_only) {        
      initWidget(uiBinder.createAndBindUi(this));
      this.setMode(show_only);
      this.editorDriver.initialize(this);
      this.refreshCustomers();
      this.refreshCreditContracts();
      this.setBO(c);
   }
   
   protected void setMode(boolean show_only) {
      this.show_only = show_only;
      
      this.pname.setReadOnly(show_only);
      this.sname.setReadOnly(show_only);
      this.search_customer.setVisible(!show_only);
      this.new_customer.setVisible(!show_only);
      this.save_customer.setVisible(!show_only);
      this.edit_selfDisclosure.setVisible(!show_only);
      this.new_creditContract.setVisible(!show_only);
   }
    
   public void setBO(Customer c) {
      super.setBO(c);
      this.selfDisclosure.clear();
      this.selfDisclosure.add( new SelfDisclosureMask(this.getBO().getSelfDisclosure()) );
      this.editorDriver.edit(c);
   }
    
   @Override
   protected void saveBO() {
      this.editorDriver.flush();
      this.getService().save(this.getBO(), new AsyncCallback<Customer>() {         
         @Override
         public void onSuccess(Customer result) {
            CustomerMask.this.refreshCustomers();  
            CustomerMask.this.refreshCreditContracts();
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
   
   protected void refreshCreditContracts() {
	      if(this.getBO() != null)
	          creditContracts.setAcceptableValues(this.getBO().getCreditContracts());
	       else 
	          creditContracts.clear();
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
			   								search_pname.getText(),
			   								search_sname.getText(),
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

   @UiHandler("save_customer")
   protected void onSaveCustomerClick(ClickEvent event) {
	   if (!(this.pname.getText().equals("")
		  || this.sname.getText().equals("")
		  || this.street.getText().equals("")
		  || this.postcode.getText().equals("")
		  || this.city.getText().equals(""))) {
		   this.saveBO();
		   
	   } else {
		   Window.alert("Bitte alle Felder ausfüllen.");
	   }
   }

   @UiHandler("new_customer")
   protected void onNewCustomerClick(ClickEvent event) {
      this.setBO(new Customer());
      this.getService().getNextCustumerNumber(new AsyncCallback<Integer>() {         
	         @Override
	         public void onSuccess(Integer result) {
	        	cust_number.setValue(result);
	         }         
	         @Override
	         public void onFailure(Throwable caught) {
	            Window.alert("Fehler beim Laden der neuen Kundennummer.");        
	         }
	   });
   }
   
   @UiHandler("edit_selfDisclosure")
   protected void onEditSelfDisclosureClick(ClickEvent event) {
		SelfDisclosure selfDisclosure = this.getBO().getSelfDisclosure();
		if (selfDisclosure == null) {
			selfDisclosure = new SelfDisclosure(new Date(), "ITler", null, null, "", null, 
												new Date(), null, null, null);
		    
	   }
	   selfDisclosure.setCustomer(this.getBO());
	   this.getFlowControl().forward(new SelfDisclosureMask(selfDisclosure));
	   }
   @UiHandler("new_creditContract")
   protected void onNewCreditContractClick(ClickEvent event) {
      CreditContract creditContract = new CreditContract();
      creditContract.setCustomer(this.getBO());
      this.getFlowControl().forward(new CreditContractMask(creditContract, true));         
   }
   
   @Override
   public void refresh() {
      this.refreshCustomers();
      this.refreshCreditContracts();
      super.refresh();
   }
      
}
