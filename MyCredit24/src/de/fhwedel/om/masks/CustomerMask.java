package de.fhwedel.om.masks;

import java.util.Date;
import java.util.Iterator;
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
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.HasWidgets;
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
   @Ignore @UiField BOSelectListBox<CreditContract, Integer> creditContracts;
   
   @Path("customerNumber") @UiField IntegerBox cust_number;
   @Path("prename") @UiField TextBox pname;
   @Path("surname") @UiField TextBox sname;
   @UiField TextBox street;
   @UiField TextBox postcode;
   @UiField TextBox city;
   
   @UiField CaptionPanel selfDisclosure;
   
   //Buttons
   @UiField Button search_customer;   
   @UiField Button select_customer;
   @UiField Button new_customer;
   @UiField Button save_customer;
   @UiField Button edit_selfDisclosure;
   @UiField Button new_creditContract;
   @UiField Button edit_creditContract;
           
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
      setWidgetProperties();
      this.refreshCustomers();
      this.refreshCreditContracts();
      this.setBO(c);
   }
   
   private void setWidgetProperties() {
	   setSearchMode(true);
	   setCustomerDatesMode(false);
	   setContractDatesMode(false);
	   setWidgetMode(selfDisclosure, false);	   
}

private void setContractDatesMode(Boolean bool) {
	creditContracts.setEnabled(bool);
	   new_creditContract.setEnabled(bool);
	   edit_creditContract.setEnabled(bool);
}

private void setWidgetMode(Widget widget, Boolean bool) {
		
//	if (widget instanceof HasWidgets) {
//	Iterator<Widget> iter = ((HasWidgets)widget).iterator();
//	   while (iter.hasNext()) {
//		   Widget next = iter.next();
//		   setWidgetMode(next, bool);
//		   Window.alert(next.getStyleName());
//		   if (next instanceof FocusWidget)
//           {
//			   ((FocusWidget)next).setEnabled(bool);
//           }
//	   }
//	}
}

   private void setCustomerDatesMode(Boolean bool) {
	   cust_number.setEnabled(bool);
	   sname.setEnabled(bool);
	   pname.setEnabled(bool);
	   street.setEnabled(bool);
	   postcode.setEnabled(bool);
	   city.setEnabled(bool);
	   
	   save_customer.setEnabled(bool);
	   edit_selfDisclosure.setEnabled(bool);
}

private void setSearchMode(boolean bool) {
	search_cust_number.setEnabled(bool);
	   search_sname.setEnabled(bool);
	   search_pname.setEnabled(bool);
	   search_customer.setEnabled(bool);
	   customers.setEnabled(bool);
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
      this.creditContracts.setVisible(!show_only);
      
   }
    
   public void setBO(Customer c) {
      super.setBO(c);
      this.refreshCreditContracts();
      this.selfDisclosure.clear();
      this.selfDisclosure.add( new SelfDisclosureMask(this.getBO().getSelfDisclosure(), false));
      this.new_creditContract.setEnabled(this.getBO().getID() != null);
      this.edit_creditContract.setEnabled(this.getBO().getID() != null);
      this.creditContracts.setEnabled(this.getBO().getID() != null);
      this.selfDisclosure.setVisible(this.getBO().getID() != null);
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
	      if(this.getBO() != null && this.cust_number.getValue() != null)
	    	  this.getService().getCreditContractsOf(this.getBO(),new AsyncCallback<List<CreditContract>>() {         
	    	         @Override
	    	         public void onSuccess(List<CreditContract> result) {
	    		          creditContracts.setAcceptableValues(result);	    	                        
	    	         }         
	    	         @Override
	    	         public void onFailure(Throwable caught) {
	    	            Window.alert("Fehler beim Laden der Kreditverträge.");        
	    	         }
	    	      }); 
	       else
	          creditContracts.clear();
   }
   
   @UiHandler("select_customer")
   protected void onSelectCustomerClick(ClickEvent event) {
	  if (this.customers.getValue() != null){
		  this.setBO(this.customers.getValue());
	  } else {
		  Window.alert("Bitte Kunden auswählen");
	  }
	  setCustomerDatesMode(true);
	  setContractDatesMode(true);
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
	   if (this.getBO() != null) {
		   if (!(this.pname.getText().equals("")
			  || this.sname.getText().equals("")
			  || this.street.getText().equals("")
			  || this.postcode.getText().equals("")
			  || this.city.getText().equals(""))) {
			   this.saveBO();
			   
		   } else {
			   Window.alert("Bitte alle Felder ausfüllen.");
		   }
	   } else {
		   Window.alert("Bitte Neuen Kunden anlegen");
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
	  setCustomerDatesMode(true);
	  setContractDatesMode(true);
   }
   
   @UiHandler("edit_selfDisclosure")
   protected void onEditSelfDisclosureClick(ClickEvent event) {
		SelfDisclosure selfDisclosure = this.getBO().getSelfDisclosure();
		if (selfDisclosure == null) {
			selfDisclosure = new SelfDisclosure(new Date(), "ITler", null, null, "", null, 
												new Date(), null, null, null);
		    
	   }
	   selfDisclosure.setCustomer(this.getBO());
	   this.getFlowControl().forward(new SelfDisclosureMask(selfDisclosure, true));
	   }
   
   @UiHandler("new_creditContract")
   protected void onNewCreditContractClick(ClickEvent event) {
	   if (!(this.cust_number.getValue() == null)) {
		   CreditContract creditContract = new CreditContract();
		   creditContract.setCustomer(this.getBO());
		   this.getFlowControl().forward(new CreditContractMask(creditContract, true));  
	   } else {
		   Window.alert("Neu anlegen eines Vertrags nicht möglich. Bitte Kunden wählen.");
	   }
   }

   @UiHandler("edit_creditContract")
   protected void onEditCreditContractClick(ClickEvent event) {
	   editCreditContract();
   }
   
   @UiHandler("creditContracts")
   protected void onCreditContractsDoubleClick(DoubleClickEvent event) {
	   editCreditContract();
   }
   
   private void editCreditContract() {
	   if (!(this.cust_number.getValue() == null
		  || this.creditContracts.getItemCount() == 0
		  || this.creditContracts.getValue() == null)) {
		   CreditContract creditContract = this.creditContracts.getValue();
		   this.getFlowControl().forward(new CreditContractMask(creditContract, false));  
	   } else {
		   Window.alert("Bitte Vertrag auswählen.");
	   }
   }
   @Override
   public void refresh() {
      this.refreshCustomers();
      this.refreshCreditContracts();
      super.refresh();
   }
      
}
