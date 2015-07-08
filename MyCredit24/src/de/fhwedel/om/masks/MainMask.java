package de.fhwedel.om.masks;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor.Ignore;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import de.fhwedel.om.model.CreditContract;
import de.fhwedel.om.model.Payment;
import de.fhwedel.om.types.TransactionType;
import de.fhwedel.om.widgets.EnumSelectListBox;

public class MainMask implements EntryPoint, FlowControl {
   
   private List<BusinessMask<?>> history = new LinkedList<BusinessMask<?>>();
   
   
   // UiBinder    
   interface MainMaskUiBinder extends UiBinder<Widget, MainMask> {}
   private final static MainMaskUiBinder uiBinder = GWT.create(MainMaskUiBinder.class);
   
   
   @UiField Button customers;
   @UiField Button credit_contract;
   @Ignore @UiField EnumSelectListBox<TransactionType> transactionType;
   @UiField Button back;
   @UiField Label history_label;
   @UiField FlowPanel content;
   
	public void onModuleLoad() {
	   RootPanel.get().add( uiBinder.createAndBindUi(this) );
	   this.refreshTransactionType();
	}
	
	    	
   private void refreshTransactionType() {
	   this.transactionType.setEnum(TransactionType.class);
	}

@UiHandler("customers")
   protected void onCustomersClick(ClickEvent event) {
      this.forward( new CustomerMask(transactionType.getValue()));
   }
   
   @UiHandler("credit_contract")
   protected void onCreditContractClick(ClickEvent event) {
	   this.forward(new CreditContractMask(transactionType.getValue())); 
   }

   @UiHandler("back")
   protected void onBackClick(ClickEvent event) {
      this.backward();
   }
   
   protected void updateHistoryUI() {
      this.back.setVisible( this.history.size() > 1 );
      this.history_label.setText(this.createHistoryLabel());      
   }
   
   @Override
   public void update(BusinessMask<?> mask) {
      this.updateHistoryUI();
   }
   
   @Override
   public void forward(BusinessMask<?> mask) {
      mask.setFlowControl(this);
      this.history.add(mask);
      this.updateHistoryUI();
      content.clear();
      content.add(mask);      
   }

   @Override
   public void backward() {
      if(this.history.size() > 1) {
         this.history.remove(this.history.size()-1);
         this.updateHistoryUI();
         BusinessMask<?> mask = this.history.get(this.history.size()-1);
         mask.refresh();
         content.clear();
         content.add(mask);
      }
   }
   
   protected String createHistoryLabel() {
      StringBuilder result = new StringBuilder();
      for(BusinessMask<?> mask : this.history) {
         result.append(" >>> " + mask.getBO().getCaption());
      }
      return result.toString();
   }
   
}
