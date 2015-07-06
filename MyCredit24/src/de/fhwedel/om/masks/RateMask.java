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
import de.fhwedel.om.model.Rate;
import de.fhwedel.om.types.CreditContractStatus;
import de.fhwedel.om.types.ValidityLevel;
import de.fhwedel.om.widgets.BOSelectListBox;
import de.fhwedel.om.widgets.EnumSelectListBox;

public class RateMask extends BusinessMask<Rate> implements Editor<Rate> {

   private boolean show_only;

   
   // Customer-Editor
   interface CustomerEditorDriver extends SimpleBeanEditorDriver<Rate, RateMask> {}
   private final CustomerEditorDriver editorDriver = GWT.create(CustomerEditorDriver.class);

   // UiBinder    
   interface RateMaskUiBinder extends UiBinder<Widget, RateMask> {}
   private final static RateMaskUiBinder uiBinder = GWT.create(RateMaskUiBinder.class);

   // Alle Felder zur Suche 
   @Ignore @UiField BOSelectListBox<Rate, Integer> rates;
   @UiField Button select_rate;
   @UiField Button safe_rate;
   
   // Alle Felder zur Vertragsdetail anzeige
   @Ignore @UiField IntegerBox contractRuntime;
   @Ignore @UiField IntegerBox contractAmount;
   @Ignore @UiField DatePicker contractBegin;
   
   // Alle Felder zum Tarif
   @Path("rateNumber") @UiField TextBox rateNumber;
   @Path("interestRate") @UiField DoubleBox interest_rate;
   @Path("runtime") @UiField IntegerBox runtime;
   @Path("creditAmountFrom") @UiField IntegerBox amount_from;
   @Path("creditAmountTo") @UiField IntegerBox amount_to;
   @Path("validFrom") @UiField DatePicker valid_from;
   @Path("validTo") @UiField DatePicker valid_to;
   @Path("validityLevel") @UiField EnumSelectListBox<ValidityLevel> validity;   
   
   public RateMask(Integer c_runtime, Integer c_amount, Date c_begin, boolean show_only) {        
      initWidget(uiBinder.createAndBindUi(this));
      this.setMode(show_only);
      this.editorDriver.initialize(this);
      this.setBO(new Rate());
      this.contractBegin.setValue(c_begin);
      this.contractAmount.setValue(c_amount);
      this.contractRuntime.setValue(c_runtime);
      this.refresh();
      this.refreshRates();
   }
   
   protected void setMode(boolean show_only) {
      this.show_only = show_only;
   }
    
   public void setBO(Rate c) {
      super.setBO(c);
      refreshValidityLevel();
      this.editorDriver.edit(c);
   }
   
   @Override
   protected void saveBO() {

   }

   protected void refreshRates() {
	   
	   this.getService().getPossibleRates(contractBegin.getValue(), contractRuntime.getValue(), contractAmount.getValue(), new AsyncCallback<List<Rate>>() {

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Fehler beim Laden der möglichen Tarife.");
		}

		@Override
		public void onSuccess(List<Rate> result) {
			rates.setAcceptableValues(result);
		}
		   
	});
   }
   
   @UiHandler("safe_rate")
   protected void onSafeRateClick(ClickEvent event) {
   }
   
   @UiHandler("select_rate")
   protected void onSelectClick(ClickEvent event) {
	   this.setBO(rates.getValue());
   }

   protected void refreshValidityLevel() {
	   this.validity.setEnum(ValidityLevel.class);
   }

   @Override
   public void refresh() {
      super.refresh();
   }
      
}
