package de.fhwedel.om.masks;




import java.sql.Date;

import com.gargoylesoftware.htmlunit.WebConsole.Formatter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.dom.client.ChangeEvent;
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
	   	   
	   public SelfDisclosureMask(SelfDisclosure s, Boolean show_only) {
		   initWidget(uiBinder.createAndBindUi(this));
		   setMode(show_only);
	       this.editorDriver.initialize(this);
	       this.refreshModeOfEmployment();
	       this.refreshValidity();
		   this.setBo(s);
	   }
	   
	   protected void setMode(Boolean show_only) {
		   this.occupation.setEnabled(show_only);
		   this.monthNet.setEnabled(show_only);			   
		   this.modeOfEmployment.setEnabled(show_only);
		   this.creditLimit.setEnabled(show_only);
		   this.validity.setEnabled(show_only);
		   this.save_selfDisclosure.setVisible(show_only);
		   this.employer.setEnabled(show_only && modeOfEmployment.getValue() != null && modeOfEmployment.getValue() == ModeOfEmployment.employee);
		   this.terminable.setEnabled(show_only && modeOfEmployment.getValue() != null &&  modeOfEmployment.getValue() == ModeOfEmployment.employee);
		   this.eval_validity.setEnabled(show_only || modeOfEmployment.getValue() != null && monthNet.getValue() != null);
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
	        	Window.alert("Selbstauskunft erfolgreich gespeichert.");
	         }         
	         @Override
	         public void onFailure(Throwable caught) {
	            Window.alert("Fehler beim Speichern (" + SelfDisclosureMask.this.getBO().getClass().getSimpleName() + ").");        
	         }
	      });
	   }
	   
	   @UiHandler("save_selfDisclosure")
	   protected void onSaveSelfDisclosureClick(ClickEvent event) {
		   if (getBO().getValidity() != ValidityLevel.X) {
			   this.saveBO();	
		   };
			   
	   }
	   
	   @UiHandler("eval_validity")
	   protected void onEvalValidity(ClickEvent event) {
		   
		   this.getService().evaluate(modeOfEmployment.getValue().getEvaluation(), monthNet.getValue(), new AsyncCallback<Integer>() {
			   @Override
			   public void onSuccess (Integer result) {
				   if (result < 200) {
					   creditLimit.setValue(new Integer(1000000));
					   validity.setValue(ValidityLevel.A);
				   }
				   if (result >= 200 && result < 250) {
					   creditLimit.setValue(new Integer(500000));
					   validity.setValue(ValidityLevel.A);
					   
				   }
				   if (result >= 250 && result < 300) {
					   creditLimit.setValue(new Integer(250000));
					   validity.setValue(ValidityLevel.B);					   
					   
				   }
				   if (result >= 300 && result < 350) {
					   creditLimit.setValue(new Integer(200000));
					   validity.setValue(ValidityLevel.B);
					   
				   }
				   if (result >= 350 && result < 400) {
					   creditLimit.setValue(new Integer(100000));
					   validity.setValue(ValidityLevel.C);
					   
				   }
				   if (result > 400) {
					   creditLimit.setValue(new Integer(0));
					   validity.setValue(ValidityLevel.X);
					   
				   }
			   }
			   @Override
			   public void onFailure (Throwable caught) {
				   Window.alert("Fehler beim Bewerten der Bonität.");        
			   }
		});
	   }
	   
	   @UiHandler("modeOfEmployment")
	   protected void onModeOfEmploymentChange(ChangeEvent event) {
		   employer.setEnabled((modeOfEmployment.getValue() == ModeOfEmployment.employee));
		   terminable.setEnabled((modeOfEmployment.getValue() == ModeOfEmployment.employee));
		   if (modeOfEmployment.getValue() == ModeOfEmployment.employee) {
			   employer.setValue("");
		   }
		   
		   eval_validity.setEnabled((monthNet.getValue() != null) && (modeOfEmployment.getValue() != null));
	   }
	   
	   @UiHandler("monthNet")
	   protected void onMonthNetChange(ChangeEvent event) {
		   eval_validity.setEnabled((monthNet.getValue() != null) && (modeOfEmployment.getValue() != null));
	   }
}
