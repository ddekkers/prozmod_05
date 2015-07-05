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
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DatePicker;

import de.fhwedel.om.model.CreditContract;
import de.fhwedel.om.model.Customer;
import de.fhwedel.om.model.Rate;
import de.fhwedel.om.types.CreditContractStatus;
import de.fhwedel.om.types.ValidityLevel;
import de.fhwedel.om.widgets.BOSelectListBox;
import de.fhwedel.om.widgets.EnumSelectListBox;

public class RateMask extends BusinessMask<Rate> implements Editor<Rate> {

   private boolean show_only;
   
   private boolean isNewCreditContract;
   
   // Customer-Editor
   interface CustomerEditorDriver extends SimpleBeanEditorDriver<Rate, RateMask> {}
   private final CustomerEditorDriver editorDriver = GWT.create(CustomerEditorDriver.class);

   // UiBinder    
   interface CreditContractMaskUiBinder extends UiBinder<Widget, RateMask> {}
   private final static CreditContractMaskUiBinder uiBinder = GWT.create(CreditContractMaskUiBinder.class);

   // Alle Felder zur Suche 
   @Ignore @UiField BOSelectListBox<Rate, Integer> rates;
//   @Ignore @UiField TextBox search_contract_number;
   	@UiField Button search;
   
   // Alle Felder zur Vertragsdetail anzeige
   	@Ignore @UiField IntegerBox contractRuntime;
   	@Ignore @UiField IntegerBox contractAmount;
   	@Ignore @UiField DatePicker contractBegin;

   //Buttons
//   @UiField Button back;
//   @UiField Button save_changes;
//   @UiField Button discard_changes;

   public RateMask(Integer c_runtime, Integer c_amount, Date c_begin, boolean show_only) {        
      initWidget(uiBinder.createAndBindUi(this));
      this.setMode(show_only);
      this.editorDriver.initialize(this);
      this.setBO(new Rate());
      this.contractBegin.setValue(c_begin);
      this.contractAmount.setValue(c_amount);
      this.contractRuntime.setValue(c_runtime);
      this.refresh();
   }
   
   protected void setMode(boolean show_only) {
      this.show_only = show_only;
   }
    
   public void setBO(Rate c) {
      super.setBO(c);
      this.editorDriver.edit(c);
   }
   
   @Override
   protected void saveBO() {
//	   Window.alert(this.getBO().getCustomer().getCaption());
//	       
//      this.editorDriver.flush();
//	   Window.alert(this.getBO().getCustomer().getCaption());
//      this.getService().save(this.getBO(), new AsyncCallback<CreditContract>() {         
//         @Override
//         public void onSuccess(CreditContract result) {
//        	RateMask.this.setBO(result);
//     	   refreshCreditContracts();
//         }         
//         @Override
//         public void onFailure(Throwable caught) {
//            Window.alert("Fehler beim Speichern des Kunden.");        
//         }
//      });
   }

//   @UiHandler("select_credit_contract")
//   protected void onSelectCreditContractClick(ClickEvent event) {
//      this.setBO(this.credit_contracts.getValue());
//      refreshCreditContractStatus();
//      isNewCreditContract = false;
//   }
   
   @UiHandler("search")
   protected void onSearchClick(ClickEvent event) {
	   this.getService().getPossibleRates(		contractBegin.getValue(),
			   									contractRuntime.getValue(),
			   									contractAmount.getValue(),
			   								(new AsyncCallback<List<Rate>>() {         
	         @Override
	         public void onSuccess(List<Rate> result) {
	        	rates.setAcceptableValues(result);            
	         }         
	         @Override
	         public void onFailure(Throwable caught) {
	            Window.alert("Fehler beim Laden der Kunden.");        
	         }
		}));
   }
//   
//   @UiHandler("discard_changes")
//   protected void onDiscardCreditContractClick(ClickEvent event) {
//	   getNewContractNumber();
//   }
//
//private void getNewContractNumber() {
//	this.getService().getNewContractNumber((new AsyncCallback<String>() {         
//				@Override
//				public void onSuccess(String result) {
//					setBO(new CreditContract());
//					contractNumber.setValue(result);
//				}         
//				@Override
//				public void onFailure(Throwable caught) {
//					Window.alert("Fehler beim Laden der Kunden.");        
//				}
//		}));
//}
//   
//   @UiHandler("save_changes")
//   protected void onSaveCreditContractClick(ClickEvent event) {
//	   this.saveBO();
//   }
//   
//  
//  protected void refreshCreditContractStatus() {
//	  this.status.setEnum(CreditContractStatus.class);
//  }
//   
   @Override
   public void refresh() {
      super.refresh();
   }
      
}
