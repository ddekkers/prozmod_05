package de.fhwedel.om.masks;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DoubleBox;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DatePicker;

import de.fhwedel.om.model.CreditContract;
import de.fhwedel.om.model.Payment;
import de.fhwedel.om.types.CreditContractStatus;
import de.fhwedel.om.types.PaymentType;
import de.fhwedel.om.types.ValidityLevel;
import de.fhwedel.om.widgets.BOSelectListBox;
import de.fhwedel.om.widgets.EnumSelectListBox;

public class CreditContractMask extends BusinessMask<CreditContract> implements Editor<CreditContract> {

   final static int MIN_DAYS = 28;
   
   private DateHandler dateHandler = new DateHandler();
   
   private Integer numberOfContractsPerDay = 0;
   
   private boolean isNew;

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
   @UiField Button select_credit_contract;
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
   @UiField Button save_changes;
   @UiField Button discard_changes;
   
   //Sonstige Funktionen
   @UiField Button rejected_deadline;
   @UiField Button rejected_validity;
   @UiField Button revocation;
   
   //Zahlungen / Zahlungsfunktionen
   @Ignore @UiField BOSelectListBox<Payment, Integer> payments;
   @UiField Button select_payment;
   @UiField Button outpayment;
   @UiField Button rate;
   @UiField Button repayment;
           
   public CreditContractMask(boolean isNew) {
	   this(new CreditContract(), isNew);	   
   }
   
   public CreditContractMask(CreditContract c, boolean isNew) {
      this(c, false, isNew);
   }
   
   private void setWidgetPropertiesByIsNewAndStatus() {
	   
	   if (isNew) {
		   // Alle Felder zur Suche 
		   credit_contracts.setVisible(true);
		   search_contract_number.setEnabled(false);
		   search.setEnabled(false);
		   select_credit_contract.setEnabled(false);
		   //Vertragsdetails
		   save_changes.setVisible(true);
		   discard_changes.setVisible(true);
		   contractNumber.setReadOnly(true);
		   status.setEnabled(false);
		   runtime.setReadOnly(false);
		   creditAmount.setReadOnly(false);
		   annuityRental.setReadOnly(true);
		   residualDebt.setReadOnly(true);
		   iban.setReadOnly(true);
		   bic.setReadOnly(true);	
		   //Tarif auswahl
		   determine_rate.setVisible(true);
		   //Sonstige Funktion
		   rejected_deadline.setVisible(false);
		   rejected_validity.setVisible(false);;
		   revocation.setVisible(false);
		   //Zahlung
		   payments.setVisible(false);
		   select_payment.setVisible(false);
		   outpayment.setVisible(false);
		   rate.setVisible(false);
		   repayment.setVisible(false);
		   
		   select_credit_contract.setEnabled(true);
		   
		   validity.setEnabled(false);
	   } else {
		   save_changes.setVisible(true);
		   save_changes.setEnabled(true);
		   discard_changes.setVisible(true);
		   discard_changes.setEnabled(true);
		   contractNumber.setReadOnly(true);
		   status.setEnabled(false);
		   search.setEnabled(true);
		   credit_contracts.setVisible(true);
		   search_contract_number.setEnabled(true);
		   determine_rate.setEnabled(false);
		   runtime.setReadOnly(true);
		   creditAmount.setReadOnly(true);
		   annuityRental.setReadOnly(true);
		   residualDebt.setReadOnly(true);
		   iban.setReadOnly(true);
		   bic.setReadOnly(true);	
		   rejected_deadline.setVisible(false);
		   rejected_validity.setVisible(false);;
		   revocation.setVisible(false);
		   payments.setVisible(true);
		   select_payment.setVisible(true);
		   select_payment.setEnabled(true);
		   outpayment.setVisible(false);
		   rate.setVisible(false);
		   repayment.setVisible(false);
		   validity.setEnabled(false);
		   if (getBO() == null || getBO().getStatus() == null) {
			   select_payment.setEnabled(false);
			   save_changes.setEnabled(false);
			   discard_changes.setEnabled(false);
		   } else {
			   
			   switch(getBO().getStatus()) {
				   
				   case Angebot: {
					   
					   rejected_deadline.setVisible(true);
					   rejected_validity.setVisible(true);
					   iban.setReadOnly(false);
					   bic.setReadOnly(false);
					   break;
				   }
				   case Ausgefertigt: {
					   
					   revocation.setVisible(true);
					   outpayment.setVisible(true);
					   break;
				   }
				   case Ausgezahlt: {
					   
					   rate.setVisible(true);
					   repayment.setVisible(true);
					   break;
				   }
				   case Widerruf: 
				   case Abgelehnt_wegen_Bonitaet:
				   case Abgelehnt_wegen_Fristablauf: 
				   case Abgeschlossen: {
			   
					   select_payment.setEnabled(false);
					   save_changes.setEnabled(false);
					   discard_changes.setEnabled(false);
					   break;
					}
					default:
						break;
			   }
		   }
	   }
   }
   
   public CreditContractMask(CreditContract c, boolean show_only, boolean isNew) {        
      initWidget(uiBinder.createAndBindUi(this));
      this.editorDriver.initialize(this);
      this.isNew = isNew;
      setWidgetPropertiesByIsNewAndStatus();
      this.refreshCreditContracts();
      this.refreshCreditContractStatus();
      this.setBO(c);
	  this.setNewContractNumber();
	  this.refreshValidityLevel();
	  this.refreshPayments();
   }
    
   public void setBO(CreditContract c) {
      super.setBO(c);
      this.editorDriver.edit(c);
   }
   
   @Override
   protected void saveBO() {
      this.editorDriver.flush();
      this.getService().save(this.getBO(), new AsyncCallback<CreditContract>() {         
         @Override
         public void onSuccess(CreditContract result) {
        	CreditContractMask.this.setBO(result);
        	CreditContractMask.this.fireSaved();
     	   	refreshCreditContracts();
         }       
         @Override
         public void onFailure(Throwable caught) {
            Window.alert("Fehler beim Speichern des Kreditvertrags.");        
         }
      });
   }
   
   @UiHandler("repayment")
   protected void onRepaymentClick(ClickEvent event) {
	   this.getFlowControl().forward(new PaymentMask(new Payment(new Date(), new Integer(0), PaymentType.Sondertilgung, getBO()), false));
   }
   
   @UiHandler("rate")
   protected void onRateClick(ClickEvent event) {
	   Integer min_resDebt_anRental = getBO().getResidualDebt() > getBO().getAnnuityRental() ? getBO().getAnnuityRental() : getBO().getResidualDebt();
	   Integer creditAmount = min_resDebt_anRental + (int) (getBO().getResidualDebt() * getBO().getRate().getInterestRate());
	   Payment payment = new Payment(new Date(), creditAmount, PaymentType.Ratenzahlung, getBO());
	   this.residualDebt.setValue(getBO().getResidualDebt() - min_resDebt_anRental);
	   this.saveBO();
	   this.getService().save(payment, new AsyncCallback<Payment>() {
		   
		   @Override
		   public void onFailure(Throwable caught) {
			   
				Window.alert("Bei der Einziehung der Rate ist etwas schief gegangen.");	 
		   }
		   
		   @Override
		   public void onSuccess(Payment result) {
			   refreshPayments();
		   
		   }});
  
   }
   
   	@UiHandler("outpayment")
   	protected void onOutpaymentClick(ClickEvent event) {
	   
   		Payment payment = new Payment(new Date(), this.creditAmount.getValue(), PaymentType.Auszahlung, getBO());

	   	this.getService().saveOutpayment(payment, new AsyncCallback<Payment>() {

	   		@Override
	   		public void onFailure(Throwable caught) {

				Window.alert("Bei der Erfassung der Auszahlung ist etwas schief gegangen.");	   			
			}

			@Override
			public void onSuccess(Payment result) {
				if (result != null) {
					setStatusAndRefreshWidgets(CreditContractStatus.Ausgezahlt);
					refreshPayments();
					saveBO();
				} else {
					
					Window.alert("Auszahlung bereits vorhanden.");
				}
			}
			   
		   });
   	}
   
	private void refreshPayments() {
		
	     this.getService().getAllPaymentsByCreditContractId(getBO().getID(), new AsyncCallback<List<Payment>>() {         
	         @Override
	         public void onSuccess(List<Payment> result) {
	        	 payments.setAcceptableValues(result);
	         }         
	         @Override
	         public void onFailure(Throwable caught) {
	            Window.alert("Fehler beim Laden der Zahlungen.");        
	         }
	      }); 	
	}
   
   @UiHandler("rejected_deadline")
   protected void onRejectedDeadlineClick(ClickEvent event) {
	   setStatusAndRefreshWidgets(CreditContractStatus.Abgelehnt_wegen_Fristablauf);
	   this.saveBO();
   }
   
   @UiHandler("rejected_deadline")
   protected void onRejectedValidityClick(ClickEvent event) {
	   setStatusAndRefreshWidgets(CreditContractStatus.Abgelehnt_wegen_Bonitaet);
	   this.saveBO();
   }
   
   @UiHandler("revocation")
   protected void onRevocationClick(ClickEvent event) {
	   setStatusAndRefreshWidgets(CreditContractStatus.Widerruf);
	   this.saveBO();
   }
   
   @UiHandler("select_credit_contract")
   protected void onSelectCreditContractClick(ClickEvent event) {
	   this.setBO(this.credit_contracts.getValue());
	   refreshCreditContractStatus();
	   this.status.setValue(getBO().getStatus());
	   refreshPayments();
	   setWidgetPropertiesByIsNewAndStatus();
   }
   
   @UiHandler("search")
   protected void onSearchClick(ClickEvent event) {
	   if (!isNew) {
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
	   
	   if (isNew) {	
		   CreditContract cc = this.getBO();
		   cc.setContractBegin(this.contractBegin.getValue());
		   if (cc.getContractBegin() == null) {
				Window.alert("Geben Sie einen Vertragsbeginn ein.");      
		   } else {
			   
			   cc.setCreditAmount(this.creditAmount.getValue());
			   if (cc.getCreditAmount() == null) {
				   Window.alert("Geben Sie eine Kreditsumme ein.");      				   
			   } else {
				   
				   cc.setAnnuityRental(this.annuityRental.getValue());   
				   cc.setRuntime(this.runtime.getValue());
				   if (cc.getRuntime() == null) 
					   Window.alert("Geben Sie eine Laufzeit ein.");
				   else {
					   if ((contractBegin.getValue() != null)
							   && dateHandler.daysBetween(new Date(), contractBegin.getValue()) >= MIN_DAYS) {
						   cc.setAnnuityRental((int) cc.getCreditAmount() / cc.getRuntime());
						   this.getFlowControl().forward(new RateMask(isNew, this.getBO(), false));
					   } else {
						   Window.alert("Bitte einen Vertragsbeginn w�hlen, der mind. 28 Tage in der Zukunft liegt.");
					   }
				   }
					   
			   }
			   
		   }
	   }
   }
   
   @UiHandler("discard_changes")
   protected void onDiscardCreditContractClick(ClickEvent event) {
		setStatusAndRefreshWidgets(null);
		CreditContract cc = new CreditContract();
		cc.setCustomer(this.getBO().getCustomer());
		this.setBO(cc);
		setNewContractNumber();
		   
   }

   private void setNewContractNumber() {
	   dateHandler.setFormat("yyMM");
	   this.getService().getAllCreditContracts((new AsyncCallback<List<CreditContract>>() {         
		   
		   @Override
		   public void onSuccess(List<CreditContract>result) {
			   List<CreditContract> thisMonthsContracts = new ArrayList<CreditContract>();
			   
			   String currYearMonth = dateHandler.getDateAsString(new Date());
			   for (CreditContract creditContract : result) {
				   if (creditContract.getContractNumber().length()>=4
						   && creditContract.getContractNumber().substring(0, 4).equals(currYearMonth)) {
					   thisMonthsContracts.add(creditContract);
				   }
			   }
			   numberOfContractsPerDay = thisMonthsContracts.size();
			   NumberFormat formatter = NumberFormat.getFormat("000");
			   contractNumber.setValue(currYearMonth + formatter.format(numberOfContractsPerDay + 1));

		   }         
		   @Override
		   public void onFailure(Throwable caught) {
					Window.alert("Fehler beim Laden der neuen Kundennummer.");        
				}
		}));

   }
   
   private void setStatusAndRefreshWidgets(CreditContractStatus status) {

		this.getBO().setStatus(status);
		this.status.setValue(status);	
		setWidgetPropertiesByIsNewAndStatus();
   }
   
   @UiHandler("save_changes")
   protected void onSaveCreditContractClick(ClickEvent event) {
	   	getFlowControl().update(this);
	   	if (isNew) {
	   	
	   		if (this.getBO().getRate() != null) {
	   				
	   			isNew = false;
	   			setStatusAndRefreshWidgets(CreditContractStatus.Angebot);
	   			this.getBO().setResidualDebt(this.getBO().getCreditAmount());
	   			this.residualDebt.setValue(this.getBO().getCreditAmount());
	   			this.saveBO(); 
	   			Window.alert("Kreditangebotsunterlagen versandt!");
	   		} else
	   			Window.alert("Sie m�ssen vorher einen Tarif ausw�hlen");
	   	} else {
	   		
	   		switch(this.getBO().getStatus()) {
	   			case Angebot:
	   				if (getBO().getCustomer() != null && getBO().getCustomer().getSelfDisclosure() != null && getBO().getCustomer().getSelfDisclosure().getValidity() != null) {
	   					
	   					this.getBO().setBic(bic.getValue());
	   					this.getBO().setIban(iban.getValue());
	   					if (this.getBO().getBic() != null && !this.getBO().getBic().isEmpty() && this.getBO().getIban() != null && !this.getBO().getIban().isEmpty()) {
	   				
	   						setStatusAndRefreshWidgets(CreditContractStatus.Ausgefertigt);
	   						this.saveBO();
	   						Window.alert("Kreditvertrag ausgefertigt!");	   				
	   					} else
	   						Window.alert("Geben Sie eine IBAN und eine BIC ein!");
	   				} else {
	   					
	   					Window.alert("Sie m�ssen zuerst die Selbstauskunft des Kunden vollst�ndig erfassen.");
	   				}
	   			break;
	   			
	   			default:
	   				Window.alert("Keine �nderungen m�glich.");   
	   			break;
	   			}
	   		}	   
   }
   
  protected void refreshCreditContracts() {
     this.getService().getAllCreditContracts(new AsyncCallback<List<CreditContract>>() {         
        @Override
        public void onSuccess(List<CreditContract> result) {
           credit_contracts.setAcceptableValues(result);            
        }         
        @Override
        public void onFailure(Throwable caught) {
           Window.alert("Fehler beim Laden der Vertr�ge.");        
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
