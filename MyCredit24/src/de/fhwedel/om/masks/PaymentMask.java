package de.fhwedel.om.masks;

import java.util.Date;
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
import de.fhwedel.om.model.Payment;
import de.fhwedel.om.model.Rate;
import de.fhwedel.om.types.CreditContractStatus;
import de.fhwedel.om.types.PaymentType;
import de.fhwedel.om.types.ValidityLevel;
import de.fhwedel.om.widgets.BOSelectListBox;
import de.fhwedel.om.widgets.EnumSelectListBox;

public class PaymentMask extends BusinessMask<Payment> implements Editor<Payment> {

   private boolean show_only;

   
   // Customer-Editor
   interface PaymentEditorDriver extends SimpleBeanEditorDriver<Payment, PaymentMask> {}
   private final PaymentEditorDriver editorDriver = GWT.create(PaymentEditorDriver.class);

   // UiBinder    
   interface PaymentMaskUiBinder extends UiBinder<Widget, PaymentMask> {}
   private final static PaymentMaskUiBinder uiBinder = GWT.create(PaymentMaskUiBinder.class);

   private CreditContract contract;
//   // Alle Felder zur Suche 
//   @Ignore @UiField BOSelectListBox<Rate, Integer> rates;
//   @UiField Button select_rate;
//   @UiField Button safe_rate;

   // Alle Felder zum Payment
   @Path("amount") @UiField IntegerBox amount;
   @Path("date") @UiField DatePicker date;
   @Path("type") @UiField EnumSelectListBox<PaymentType> type;
   // Alle Felder zum Vertrag
   @Path("creditContract.contractNumber") @UiField TextBox c_number;
   @Path("creditContract.status") @UiField EnumSelectListBox<CreditContractStatus> status;
   @Path("creditContract.residualDebt") @UiField IntegerBox residualDebt;
   
   
   
   @UiField Button back;
   
   public PaymentMask(CreditContract c, Payment p, boolean show_only) {        
      initWidget(uiBinder.createAndBindUi(this));
      this.editorDriver.initialize(this);
      p.setCreditContract(c);
      this.setBO(p);
      this.contract = c;
   }

   @UiHandler("back")
   protected void onBackClick(ClickEvent event) {
      this.getFlowControl().backward();
   }
}
