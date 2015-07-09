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

import de.fhwedel.om.model.CreditContract;
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
//   @Ignore @UiField BOSelectListBox<Rate, Integer> rates;
//   @UiField Button select_rate;
//   @UiField Button safe_rate;

   // Alle Felder zum Payment
   @Path("amount") @UiField IntegerBox amount;
   @Path("dateAsString") @UiField TextBox date;
   @Path("type") @UiField EnumSelectListBox<PaymentType> type;
   // Alle Felder zum Vertrag
   @Path("creditContract.contractNumber") @UiField TextBox c_number;
   @Path("creditContract.status") @UiField EnumSelectListBox<CreditContractStatus> status;
   @Path("creditContract.residualDebt") @UiField IntegerBox residualDebt;
   
   
   public PaymentMask(Payment p, boolean show_only) {        
      initWidget(uiBinder.createAndBindUi(this));
      this.editorDriver.initialize(this);
      this.setBO(p);
      this.refresh();
   }

   @Override
   public void refresh() {
      super.refresh();
   }
   
   @Override
   protected void saveBO() {

   }

   @UiHandler("back")
   protected void onBackClick(ClickEvent event) {
      this.getFlowControl().backward();
   }
}
