package de.fhwedel.om.masks;

import java.time.LocalDate;
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
import com.google.gwt.user.client.ui.DoubleBox;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DatePicker;

import de.fhwedel.om.model.CreditContract;
import de.fhwedel.om.model.Customer;
import de.fhwedel.om.model.SelfDisclosure;
import de.fhwedel.om.types.CreditContractStatus;
import de.fhwedel.om.types.ValidityLevel;
import de.fhwedel.om.widgets.BOSelectListBox;
import de.fhwedel.om.widgets.DateHandler;
import de.fhwedel.om.widgets.EnumSelectListBox;

public class CreditContractMask extends BusinessMask<CreditContract> implements Editor<CreditContract> {

   private boolean show_only;
   
   private boolean isNewCreditContract;
   
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
   @UiField Button determine_rate;
   
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
   	
    // Alle Felder zum Tarif
    @Path("rate.rateNumber") @UiField TextBox rateNumber;
    @Path("rate.interestRate") @UiField DoubleBox interest_rate;
    @Path("rate.runtime") @UiField IntegerBox rate_runtime;
    @Path("rate.creditAmountFrom") @UiField IntegerBox amount_from;
    @Path("rate.creditAmountTo") @UiField IntegerBox amount_to;
    @Path("rate.validFrom") @UiField TextBox valid_from;
    @Path("rate.validTo") @UiField TextBox valid_to;
    @Path("rate.validityLevel") @UiField EnumSelectListBox<ValidityLevel> validity;

   //Buttons
   @UiField Button back;
   @UiField Button save_changes;
   @UiField Button discard_changes;
   @UiField Button new_contract;
           
   public CreditContractMask(boolean isNewContract) {
	   this(new CreditContract(), isNewContract);	   
   }
   
   public CreditContractMask(CreditContract c, boolean isNewContract) {
      this(c, false, isNewContract);
   }

   public CreditContractMask(CreditContract c, boolean show_only, boolean isNewContract) {        
      initWidget(uiBinder.createAndBindUi(this));
      this.editorDriver.initialize(this);
      this.isNewCreditContract = isNewContract;
      this.refreshCreditContracts();
      this.refreshCreditContractStatus();
      this.setBO(c);
	  this.getNewContractNumber();
	  this.refreshValidityLevel();
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
      this.editorDriver.flush();
      this.getBO().setResidualDebt(this.getBO().getCreditAmount());
      getNewContractNumber();
      this.getService().save(this.getBO(), new AsyncCallback<CreditContract>() {         
         @Override
         public void onSuccess(CreditContract result) {
        	CreditContractMask.this.setBO(result);
        	CreditContractMask.this.fireSaved();
//     	   refreshCreditContracts();
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
	  if (!isNewCreditContract) {
		  this.setBO(this.credit_contracts.getValue());
      	refreshCreditContractStatus();
      	isNewCreditContract = false;
	  }
   }
   
   @UiHandler("search")
   protected void onSearchClick(ClickEvent event) {
	   if (!isNewCreditContract) {
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
   }
   
   @UiHandler("determine_rate")
   protected void onDetermineRateClick(ClickEvent event) {
	   DateHandler handler;
	   
	   if (isNewCreditContract) {	
		   CreditContract cc = this.getBO();
		   cc.setContractBegin(new LocalDate());
		   if (cc.getContractBegin() == null) {
				Window.alert("Geben Sie einen Vertragsbeginn ein.");      
		   } else {
			   
			   cc.setCreditAmount(this.creditAmount.getValue());
			   if (cc.getCreditAmount() == null) {
				   Window.alert("Geben Sie eine Kreditsumme ein.");      				   
			   } else {
				   
				   cc.setAnnuityRental(this.annuityRental.getValue());
				   if (cc.getAnnuityRental() == null) {					   
					   Window.alert("Geben Sie eine Tilgungsrate ein.");      				   
					   
				   } else {					   
					   
					   cc.setRuntime(this.runtime.getValue());
					   if (cc.getRuntime() == null) 
						   Window.alert("Geben Sie eine Laufzeit ein.");
					   else
						   this.getFlowControl().forward(new RateMask(this.getBO(), false));
				   }
					   
			   }
			   
		   }
	   }
   }
   
   @UiHandler("discard_changes")
   protected void onDiscardCreditContractClick(ClickEvent event) {
	   if (isNewCreditContract) {
		   CreditContract cc = new CreditContract();
		   cc.setCustomer(this.getBO().getCustomer());
		   this.setBO(cc);
		   getNewContractNumber();
	   }
   }
   
   @UiHandler("new_contract")
   protected void onNewContractClick(ClickEvent event) {
      this.setBO(new CreditContract());
      this.getBO().setCustomer(new Customer(123, "DD", "Dekk", "", "", "", null, new SelfDisclosure()));
      this.getService().getNewContractNumber(new AsyncCallback<String>() {         
	         @Override
	         public void onSuccess(String result) {
	        	contractNumber.setValue(result);
	         }         
	         @Override
	         public void onFailure(Throwable caught) {
	            Window.alert("Fehler beim Laden der neuen Kundennummer.");        
	         }
	   });
   
   }
   

   private void getNewContractNumber() {
	   this.getService().getNewContractNumber((new AsyncCallback<String>() {         
				@Override
				public void onSuccess(String result) {
					contractNumber.setValue(result);
				}         
				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Fehler beim Laden der Kunden.");        
				}
		}));
   }
   
   @UiHandler("save_changes")
   protected void onSaveCreditContractClick(ClickEvent event) {
	   if (isNewCreditContract) {
		   if (this.getBO().getRate() != null) {
			   
			   this.saveBO();   
		   } else
			   Window.alert("Sie müssen vorher einen Tarif auswählen");
		   
	   } else
			Window.alert("Keine Änderungen möglich.");      
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

  protected void refreshValidityLevel() {
	   this.validity.setEnum(ValidityLevel.class);
  }
   @Override
   public void refresh() {
      super.refresh();
   }
      
}
