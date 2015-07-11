package de.fhwedel.om.masks;

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
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DatePicker;

import de.fhwedel.om.model.Payment;
import de.fhwedel.om.types.CreditContractStatus;
import de.fhwedel.om.types.PaymentType;
import de.fhwedel.om.widgets.EnumSelectListBox;

public class PaymentMask extends BusinessMask<Payment> implements Editor<Payment> {

   // Customer-Editor
   interface PaymentEditorDriver extends SimpleBeanEditorDriver<Payment, PaymentMask> {}
   private final PaymentEditorDriver editorDriver = GWT.create(PaymentEditorDriver.class);

   // UiBinder    
   interface PaymentMaskUiBinder extends UiBinder<Widget, PaymentMask> {}
   private final static PaymentMaskUiBinder uiBinder = GWT.create(PaymentMaskUiBinder.class);

//   // Alle Felder zur Suche 

   // Alle Felder zum Payment
   @Path("amount") @UiField IntegerBox amount;
   @Path("date") @UiField DatePicker date;
   @Path("type") @UiField EnumSelectListBox<PaymentType> type;
   
   @UiField Button save;
   // Alle Felder zum Vertrag
   @Path("creditContract.contractNumber") @UiField TextBox c_number;
   @Path("creditContract.status") @UiField EnumSelectListBox<CreditContractStatus> status;
   @Path("creditContract.residualDebt") @UiField IntegerBox residualDebt;
   @Path("creditContract.creditAmount") @UiField IntegerBox creditAmount;
   
   
   
   public PaymentMask(Payment p, boolean show_only) {        
      initWidget(uiBinder.createAndBindUi(this));
      this.editorDriver.initialize(this);
      this.refreshCreditContractStatus();
      this.refreshType();
      this.setBO(p);
      this.status.setValue(getBO().getCreditContract().getStatus());
      this.type.setValue(getBO().getType());
      this.setMode(show_only);
   }
   
   
   @UiHandler("save")
   protected void onSaveClick(ClickEvent event) {
	   if (this.amount.getValue() >= 1000) {
		   
		   if (this.amount.getValue() <= (int) this.getBO().getCreditContract().getCreditAmount() * 0.05)  {
			   
			   
			   if (this.amount.getValue() <= this.getBO().getCreditContract().getResidualDebt()) {
				   
				   this.getBO().getCreditContract().setResidualDebt(this.getBO().getCreditContract().getResidualDebt() - this.amount.getValue());
				   this.getBO().setAmount(this.amount.getValue());
				   
				   if (0 == this.residualDebt.getValue()) {
					   
					   this.status.setValue(CreditContractStatus.Abgeschlossen);
					   this.getBO().getCreditContract().setStatus(CreditContractStatus.Abgeschlossen);
				   }
				   
				   this.saveBO();
				   
			   } else {
				   
				   Window.alert("Betrag der Sondertilgung muss kleiner gleich der Restschuld sein");
			   }
		   } else {
			   
			   Window.alert("Betrag der Sondertilgung muss kleiner als 0,05 mal der Kreditsumme sein");
		   }
	   } else {
		   
		   Window.alert("Betrag der Sondertilgung muss größer gleich als 1000 EUR sein");
	   }
   }
   
   
   private void setMode(boolean show_only) {

	   amount.setReadOnly(show_only);
	   save.setVisible(!show_only);
	   type.setEnabled(false);
	   status.setEnabled(false);
   }

   public void setBO(Payment p) {
	      super.setBO(p);
	      this.editorDriver.edit(p);
   }

   @Override
   public void refresh() {
      super.refresh();
   }
   
   @Override
   protected void saveBO() {
	   this.getService().save(getBO(), new AsyncCallback<Payment>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Fehler beim Speichern der Sondertilgung.");
			}

			@Override
			public void onSuccess(Payment payment) {
				setBO(payment);
				Window.alert("Erfolgreich gespeichert.");
				getFlowControl().backward();
			}
			   
		});
   }
   protected void refreshCreditContractStatus() {
		  this.status.setEnum(CreditContractStatus.class);
	  }
   protected void refreshType() {
		  this.type.setEnum(PaymentType.class);
	  }
  
}
