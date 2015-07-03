package de.fhwedel.om.masks;

import java.util.List;

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
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import de.fhwedel.om.model.CreditContract;
import de.fhwedel.om.widgets.BOSelectListBox;

public class CreditContractMask extends BusinessMask<CreditContract> implements Editor<CreditContract> {

   private boolean show_only;
   
   // Customer-Editor
   interface CustomerEditorDriver extends SimpleBeanEditorDriver<CreditContract, CreditContractMask> {}
   private final CustomerEditorDriver editorDriver = GWT.create(CustomerEditorDriver.class);

   // UiBinder    
   interface CreditContractMaskUiBinder extends UiBinder<Widget, CreditContractMask> {}
   private final static CreditContractMaskUiBinder uiBinder = GWT.create(CreditContractMaskUiBinder.class);

   @Ignore @UiField BOSelectListBox<CreditContract, Integer> credit_contracts;
   @Ignore @UiField TextBox search_contract_number;

   //Buttons
   @UiField Button back;
   @UiField Button search;
           
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
   }
   
   protected void setMode(boolean show_only) {
      this.show_only = show_only;
   }
    
   public void setBO(CreditContract c) {
      super.setBO(c);
      this.editorDriver.edit(c);
   }

   @UiHandler("back")
   protected void onBackClick(ClickEvent event) {
	   
   }
   
   @UiHandler("search")
   protected void onSearchClick(ClickEvent event) {
	   
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
   
   @Override
   public void refresh() {
      super.refresh();
   }
      
}
