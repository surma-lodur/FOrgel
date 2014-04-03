/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forgel.gui;

import forgel.Hearth;
import forgel.MidiCommandThread;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JCheckBox;
import javax.swing.JSpinner;

/**
 *
 * @author mafolz
 */
public class HardwareOptions extends javax.swing.JPanel {

	/**
	 * Creates new form HardwareOptions
	 */
	public HardwareOptions() {
		initComponents();
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    buttonGroup1 = new javax.swing.ButtonGroup();
    floppyCount = new javax.swing.JSpinner();
    jLabel1 = new javax.swing.JLabel();
    jButton1 = new javax.swing.JButton();
    jSeparator1 = new javax.swing.JSeparator();
    jLabel2 = new javax.swing.JLabel();
    jLabel3 = new javax.swing.JLabel();
    jCheckBox1 = new javax.swing.JCheckBox();
    spreadSpinner = new javax.swing.JSpinner();

    floppyCount.setInheritsPopupMenu(true);
    floppyCount.setValue(Hearth.getDriveCount());

    jLabel1.setText("Anzahl Floppies:");

    jButton1.setText("Anwenden");
    jButton1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton1ActionPerformed(evt);
      }
    });

    jLabel2.setText("Spread:");

    jLabel3.setText("Duplizieren:");

    jCheckBox1.setSelected(MidiCommandThread.double_drives);
    jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        DoubleDriveClicked(evt);
      }
    });

    spreadSpinner.setEnabled(jCheckBox1.isSelected());
    spreadSpinner.setValue(MidiCommandThread.spread_drive);
    spreadSpinner.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
      public void propertyChange(java.beans.PropertyChangeEvent evt) {
        spreadSpinnerPropertyChange(evt);
      }
    });

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(jSeparator1)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(layout.createSequentialGroup()
            .addComponent(jLabel1)
            .addGap(18, 18, 18)
            .addComponent(floppyCount, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addComponent(jButton1))
          .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(jLabel3)
              .addComponent(jLabel2))
            .addGap(40, 40, 40)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(jCheckBox1)
              .addComponent(spreadSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(floppyCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel1)
          .addComponent(jButton1))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(layout.createSequentialGroup()
            .addComponent(jLabel3)
            .addGap(18, 18, 18)
            .addComponent(jLabel2))
          .addGroup(layout.createSequentialGroup()
            .addComponent(jCheckBox1)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(spreadSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addGap(0, 13, Short.MAX_VALUE))
    );
  }// </editor-fold>//GEN-END:initComponents

  private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

		Hearth.getInstance((String) MainWindow.getInstance().port.getSelectedItem(), (int) floppyCount.getValue());
  }//GEN-LAST:event_jButton1ActionPerformed

  private void DoubleDriveClicked(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DoubleDriveClicked
		Object element = evt.getSource();
		if (element instanceof JCheckBox) {
			JCheckBox box = (JCheckBox) element;
			boolean old_value = MidiCommandThread.double_drives;
			MidiCommandThread.double_drives = box.isSelected();
			spreadSpinner.setEnabled(box.isSelected());
			if (old_value == false) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException ex) {
					Logger.getLogger(HardwareOptions.class.getName()).log(Level.SEVERE, null, ex);
				}
				Hearth.getInstance().stopAllImediantly();
			}
		}
  }//GEN-LAST:event_DoubleDriveClicked

  private void spreadSpinnerPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_spreadSpinnerPropertyChange
		Object element = evt.getSource();
		if (element instanceof JSpinner) {
			JSpinner spinner = (JSpinner) element;
			MidiCommandThread.spread_drive = (int) spinner.getValue();
		}
  }//GEN-LAST:event_spreadSpinnerPropertyChange

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.ButtonGroup buttonGroup1;
  public static javax.swing.JSpinner floppyCount;
  private javax.swing.JButton jButton1;
  private javax.swing.JCheckBox jCheckBox1;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JSeparator jSeparator1;
  private javax.swing.JSpinner spreadSpinner;
  // End of variables declaration//GEN-END:variables
}
