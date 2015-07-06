package de.fhwedel.om.masks;




import java.sql.Date;

import com.gargoylesoftware.htmlunit.WebConsole.Formatter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.datepicker.client.DatePicker;

import de.fhwedel.om.model.SelfDisclosure;
import de.fhwedel.om.types.ModeOfEmployment;
import de.fhwedel.om.types.ValidityLevel;
import de.fhwedel.om.widgets.DateHandler;
import de.fhwedel.om.widgets.EnumSelectListBox;

public class SelfDisclosureMask extends BusinessMask<SelfDisclosure> implements Editor<SelfDisclosure> {
	   private boolean show_only;
	   
	   // SelfDisclosure-Editor
	   interface SelfDisclosureEditorDriver extends SimpleBeanEditorDriver<SelfDisclosure, SelfDisclosureMask> {}
	   private final SelfDisclosureEditorDriver editorDriver = GWT.create(SelfDisclosureEditorDriver.class);

	   // UiBinder    
	   interface SelfDisclosureMaskUiBinder extends UiBinder<Widget, SelfDisclosureMask> {}
	   private final static SelfDisclosureMaskUiBinder uiBinder = GWT.create(SelfDisclosureMaskUiBinder.class);

	   DateHandler handler = new DateHandler("d.M.y");
	   @UiField DatePicker registration;
	   @UiField TextBox occupation;
	   @UiField EnumSelectListBox<ModeOfEmployment> modeOfEmployment;
	   @UiField CheckBox terminable;
	   @UiField TextBox employer;
	   @UiField IntegerBox monthNet;
	   @UiField DatePicker classification;
	   @UiField IntegerBox creditLimit;
	   @UiField EnumSelectListBox<ValidityLevel> validity;
	   
	   //Buttons
	   @UiField Button save_selfDisclosure;
	   @UiField Button eval_validity;
	   	   
	   public SelfDisclosureMask(SelfDisclosure s) {
		   initWidget(uiBinder.createAndBindUi(this));
	       this.editorDriver.initialize(this);
	       this.refreshModeOfEmployment();
	       this.refreshValidity();
		   this.setBo(s);
	   }
	   
	   protected void refreshModeOfEmployment(){
		   this.modeOfEmployment.setEnum(ModeOfEmployment.class);     
	   }
	   
	   protected void refreshValidity() {
		   this.validity.setEnum(ValidityLevel.class);
	   }
	   
	   public void setBo(SelfDisclosure s) {
		   super.setBO(s);
		   this.editorDriver.edit(s);
	   }
	   
	   @Override
	   protected void saveBO() {
	      this.editorDriver.flush();
	      this.getService().save(this.getBO(), new AsyncCallback<SelfDisclosure>() {         
	         @Override
	         public void onSuccess(SelfDisclosure result) {
	        	SelfDisclosureMask.this.setBO(result);
	        	SelfDisclosureMask.this.fireSaved();            
	         }         
	         @Override
	         public void onFailure(Throwable caught) {
	            Window.alert("Fehler beim Speichern (" + SelfDisclosureMask.this.getBO().getClass().getSimpleName() + ").");        
	         }
	      });
	   }
	   
	   @UiHandler("save_selfDisclosure")
	   protected void onSaveSelfDisclosureClick(ClickEvent event) {
		   this.saveBO();
	   }
	   
	   @UiHandler("eval_validity")
	   protected void onEvalValidity(ClickEvent event) {
		   
		   this.getService().evaluate(modeOfEmployment.getValue().getEvaluation(), monthNet.getValue(), new AsyncCallback<Integer>() {
			   @Override
			   public void onSuccess (Integer result) {
				   Window.alert(result.toString());
			   }
			   @Override
			   public void onFailure (Throwable caught) {
				   Window.alert("Fehler beim Bewerten der Bonität.");        
			   }
		});
	   }
}
