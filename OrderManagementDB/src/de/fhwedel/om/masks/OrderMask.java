package de.fhwedel.om.masks;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import de.fhwedel.om.model.Order;
import de.fhwedel.om.model.Order.PaymentMethod;
import de.fhwedel.om.model.OrderPosition;
import de.fhwedel.om.widgets.BOSelectListBox;
import de.fhwedel.om.widgets.EnumSelectListBox;

public class OrderMask extends BusinessMask<Order> implements Editor<Order> {

   // Order-Editor
   interface OrderEditorDriver extends SimpleBeanEditorDriver<Order, OrderMask> {}
   private final OrderEditorDriver editorDriver = GWT.create(OrderEditorDriver.class);
    
   // UiBinder    
   interface OrderMaskUiBinder extends UiBinder<Widget, OrderMask> {}
   private final static OrderMaskUiBinder uiBinder = GWT.create(OrderMaskUiBinder.class);
    
   // UiBinder (Widget-Instanzvariablen) 
   @UiField TextBox orderCaption;        
   @UiField EnumSelectListBox<PaymentMethod> paymentMethod;
   @Ignore @UiField BOSelectListBox<OrderPosition, Integer> positions;
    
   @UiField CaptionPanel customer;
   @UiField Button save_order;
   @UiField Button new_position;
   @UiField Button edit_position;
    
   public OrderMask(Order o) {
      initWidget(uiBinder.createAndBindUi(this));
      this.editorDriver.initialize(this);
      this.refreshPaymentMethods();
      this.setBO(o);
   }

   public void setBO(Order o) {
      super.setBO(o);
      this.customer.clear();
      this.customer.add( new CustomerMask(this.getBO().getCustomer(), true) );
      this.refreshOrderPositions();
      this.new_position.setVisible( this.getBO().getID() != null );
      this.editorDriver.edit(o);
   }
    
   @Override
   protected void saveBO() {
      this.editorDriver.flush();
      this.getService().save(this.getBO(), new AsyncCallback<Order>() {         
         @Override
         public void onSuccess(Order result) {
            OrderMask.this.setBO(result);
            OrderMask.this.fireSaved();            
         }         
         @Override
         public void onFailure(Throwable caught) {
            Window.alert("Fehler beim Speichern (" + OrderMask.this.getBO().getClass().getSimpleName() + ").");        
         }
      });
   }
   
   protected void refreshPaymentMethods() {
      this.paymentMethod.setEnum(PaymentMethod.class);     
   }
   
   protected void refreshOrderPositions() {
      if(this.getBO() != null)
         positions.setAcceptableValues(this.getBO().getPositions());
      else 
         positions.clear();
   }
    
   @UiHandler("save_order")
   protected void onSaveOrderClick(ClickEvent event) {
      this.saveBO();
   }
   
   @UiHandler("positions")
   protected void onSelectOrderPositionClick(ClickEvent event) {
      this.edit_position.setVisible( this.positions.getValue() != null );
   }
     
   @UiHandler("edit_customer")
   protected void onEditCustomerClick(ClickEvent event) {
      this.getFlowControl().forward( new CustomerMask(this.getBO().getCustomer()) );
   }

   @UiHandler("new_position")
   protected void onNewOrderPositionClick(ClickEvent event) {
      OrderPosition new_pos = new OrderPosition();
      new_pos.setOrder(this.getBO());
      this.editOrderPosition(new_pos);
   }
   
   @UiHandler("positions")
   protected void onEditOrderPositionClick(DoubleClickEvent event) {
      this.editOrderPosition(this.positions.getValue());
   }
   
   @UiHandler("edit_position")
   protected void onEditOrderPositionClick(ClickEvent event) {
      this.editOrderPosition(this.positions.getValue());
   }
  
   protected void editOrderPosition(final OrderPosition pos) {
      final OrderPositionMask opm = new OrderPositionMask(pos);        
      final DialogBox dialog = new DialogBox();
      dialog.setAnimationEnabled(true);
      dialog.setGlassEnabled(true);
      dialog.setModal(true);
      dialog.setText("Auftragsposition bearbeiten");

      MaskListener ml = new MaskListener() {
         @Override
         public void completed() {
            dialog.hide();            
            OrderMask.this.setBO(opm.getBO().getOrder());
         }   
         @Override
         public void cancelled() {
            dialog.hide();
         }
      };

      opm.addMaskListener(ml);        
      dialog.setWidget(opm);
      dialog.setPixelSize(350, 150);        
      dialog.center();
      dialog.show();
   }
   
}
