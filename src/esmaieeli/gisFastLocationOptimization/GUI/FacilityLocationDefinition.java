/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package esmaieeli.gisFastLocationOptimization.GUI;

import esmaieeli.gisFastLocationOptimization.GIS3D.LocationNode;
import java.util.concurrent.Callable;

/**
 *
 * @author Amir72c
 */
public class FacilityLocationDefinition extends javax.swing.JPanel {

    /**
     * Creates new form locationNodeDefinition
     */
    public FacilityLocationDefinition(NumericController nC, int order) {
        initComponents();
        numericController = nC;
        myOrder = order;
    }
    NumericController numericController;
    int myOrder;
    LocationNode myNode=null;

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        IDText = new javax.swing.JFormattedTextField();
        deleteButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        CapacityText = new javax.swing.JFormattedTextField();
        mapPickButton = new javax.swing.JButton();

        jLabel1.setText("NodeId:");

        IDText.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        IDText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IDTextActionPerformed(evt);
            }
        });

        deleteButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/esmaieeli/gisFastLocationOptimization/GUI/delete.png"))); // NOI18N
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        jLabel2.setText("Capacity:");

        CapacityText.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        CapacityText.setText("2");

        mapPickButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/esmaieeli/gisFastLocationOptimization/GUI/mapPick1.png"))); // NOI18N
        mapPickButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mapPickButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(IDText, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(CapacityText)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mapPickButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(deleteButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(deleteButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(IDText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mapPickButton)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(CapacityText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        // TODO add your handling code here:
        if (numericController != null) {
            numericController.locationDefinitions.remove(myOrder);
            numericController.refreshFacilityLocationList();
            //numericController.isEditNeeded=true;
        }
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void mapPickButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mapPickButtonActionPerformed
        // TODO add your handling code here:
        numericController.passedOrder = myOrder;
        numericController.parentApp.enqueue(new Callable() {
            public Object call() throws Exception {
                // call methods that modify the scene graph here
                
                numericController.parentApp.isPickingNode = true;
                numericController.parentApp.showCrosshair();
                numericController.parentApp.resizeCrossHair();
                return null;
            }
        });
        numericController.canvas.requestFocus();
    }//GEN-LAST:event_mapPickButtonActionPerformed

    private void IDTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IDTextActionPerformed
        // TODO add your handling code here:
        numericController.checkIDValidity();
    }//GEN-LAST:event_IDTextActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JFormattedTextField CapacityText;
    public javax.swing.JFormattedTextField IDText;
    private javax.swing.JButton deleteButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JButton mapPickButton;
    // End of variables declaration//GEN-END:variables
}
