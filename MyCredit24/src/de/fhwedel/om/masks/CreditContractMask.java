package de.fhwedel.om.masks;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DateLabel;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DatePicker;

import de.fhwedel.om.model.CreditContract;
import de.fhwedel.om.model.Customer;
import de.fhwedel.om.types.CreditContractStatus;
import de.fhwedel.om.types.ValidityLevel;
import de.fhwedel.om.widgets.BOSelectListBox;
import de.fhwedel.om.widgets.EnumSelectListBox;

public class CreditContractMask extends BusinessMask<CreditContract> implements Editor<CreditContract> {

   private boolean show_only;
   
   // Customer-Editor
   interface CustomerEditorDriver extends SimpleBeanEditorDriver<CreditContract, CreditContractMask> {}
   private final CustomerEditorDriver editorDriver = GWT.create(CustomerEditorDriver.class);

   // UiBinder    
   interface CreditContractMaskUiBinder extends UiBinder<Widget, CreditContractMask> {}
   private final static CreditContractMaskUiBinder uiBinder = GWT.create(CreditContractMaskUiBinder.class);

   // Alle Felder zur Suche
   @Ignore @UiField BOSelectListBox<CreditContract, Integer> credit_contracts;
   @Ignore @UiField TextBox search_contract_number;
   @UiField Button search;
   
   // Alle Felder zur Vertragsdetail anzeige
   	@Path("contractNumber") @UiField TextBox contractNumber;
   	@Path("status") @UiField EnumSelectListBox<CreditContractStatus> status;
   	@Path("runtime") @UiField IntegerBox runtime;
   	@Path("creditAmount") @UiField IntegerBox creditAmount;
   	@Path("contractBegin") @UiField DatePicker contractBegin;
   	@Path("annuityRental") @UiField IntegerBox annuityRental;
   	@Path("residualDebt") @UiField IntegerBox residualDebt;
   	@Path("iban") @UiField TextBox iban;
   	@Path("bic") @UiField TextBox bic;	

   //Buttons
   @UiField Button back;
   @UiField Button save_credit_contract;
           
   public CreditContractMask() {
	   this(new CreditContract());	   
   }
   
   public CreditContractMask(CreditContract c) {
      this(c, false);
   }

   public CreditContractMask(CreditContract c, boolean show_only) {        
      initWidget(uiBinder.createAndBindUi(this));
      this.setMode(show_only);
      this.editorDriver.initialize(this);
      this.setBO(c);
      this.refresh();
      this.refreshCreditContracts();
      this.refreshCreditContractStatus();
   }
   
   protected void setMode(boolean show_only) {
      this.show_only = show_only;
   }
    
   public void setBO(CreditContract c) {
      super.setBO(c);
      this.editorDriver.edit(c);
   }
   
   @Override
   protected void saveBO() {
	   Window.alert(this.getBO().getCustomer().getCaption());
	       
      this.editorDriver.flush();
	   Window.alert(this.getBO().getCustomer().getCaption());
      this.getService().save(this.getBO(), new AsyncCallback<CreditContract>() {         
         @Override
         public void onSuccess(CreditContract result) {
      	   Window.alert(result.getCaption());
            CreditContractMask.this.setBO(result);
         }         
         @Override
         public void onFailure(Throwable caught) {
            Window.alert("Fehler beim Speichern des Kunden.");        
         }
      });
   }


   @UiHandler("back")
   protected void onBackClick(ClickEvent event) {
	   
   }
   
   @UiHandler("select_credit_contract")
   protected void onSelectCreditContractClick(ClickEvent event) {
      this.setBO(this.credit_contracts.getValue());
      refreshCreditContractStatus();
   }
   
   @UiHandler("search")
   protected void onSearchClick(ClickEvent event) {
	   this.getService().searchCreditContractBy( search_contract_number.getValue(),
			   								(new AsyncCallback<List<CreditContract>>() {         
	         @Override
	         public void onSuccess(List<CreditContract> result) {
	        	credit_contracts.setAcceptableValues(result);            
	         }         
	         @Override
	         public void onFailure(Throwable caught) {
	            Window.alert("Fehler beim Laden der Kunden.");        
	         }
		}));
   }
   
   @UiHandler("save_credit_contract")
   protected void onSaveCreditContractClick(ClickEvent event) {
	   this.saveBO();
   }
   
  protected void refreshCreditContracts() {
     this.getService().getAllCreditContracts(new AsyncCallback<List<CreditContract>>() {         
        @Override
        public void onSuccess(List<CreditContract> result) {
           credit_contracts.setAcceptableValues(result);            
        }         
        @Override
        public void onFailure(Throwable caught) {
           Window.alert("Fehler beim Laden der Verträge.");        
        }
     });      
  }
  
  protected void refreshCreditContractStatus() {
	  this.status.setEnum(CreditContractStatus.class);
  }
   
   @Override
   public void refresh() {
      super.refresh();
   }
      
}
