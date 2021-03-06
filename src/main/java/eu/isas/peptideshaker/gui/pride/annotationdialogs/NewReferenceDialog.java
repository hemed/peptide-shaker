package eu.isas.peptideshaker.gui.pride.annotationdialogs;

import com.compomics.util.pride.prideobjects.Reference;
import java.awt.Color;

/**
 * A dialog for adding new references or editing old ones.
 * 
 * @author Harald Barsnes
 */
public class NewReferenceDialog extends javax.swing.JDialog {

    /**
     * Reference to the reference group GUI.
     */
    private NewReferenceGroupDialog referenceGroupDialog;
    /**
     * The row index of the references being edited.
     */
    private int rowIndex = -1 ;
    /**
     * The last valid input for contact name.
     */
    private String lastNameInput = "";

    /**
     * Creates a new NewReferenceDialog dialog.
     *
     * @param referenceGroupDialog the NewReferenceGroupDialog parent
     * @param modal if the dialog is to be modal or not
     */
    public NewReferenceDialog(NewReferenceGroupDialog referenceGroupDialog, boolean modal) {
        super(referenceGroupDialog, modal);
        this.referenceGroupDialog = referenceGroupDialog;
        initComponents();
        setTitle("New Reference");
        setLocationRelativeTo(referenceGroupDialog);
        setVisible(true);
    }

    /**
     * Creates a new NewReferenceDialog dialog.
     *
     * @param referenceGroupDialog the NewReferenceGroupDialog parent
     * @param modal if the dialog is to be modal or not
     * @param reference the reference
     * @param rowIndex the row index
     */
    public NewReferenceDialog(NewReferenceGroupDialog referenceGroupDialog, boolean modal, Reference reference, int rowIndex) {
        super(referenceGroupDialog, modal);
        this.referenceGroupDialog = referenceGroupDialog;
        this.rowIndex = rowIndex;
        initComponents();

        setTitle("Edit Reference");
        referenceJTextArea.setText(reference.getReference());
        pmidIDJTextField.setText(reference.getPmid());
        doiJTextField.setText(reference.getDoi());
        validateInput();

        setLocationRelativeTo(referenceGroupDialog);
        setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        backgroundPanel = new javax.swing.JPanel();
        referenceDetailsPanel = new javax.swing.JPanel();
        referenceLabel = new javax.swing.JLabel();
        referenceScrollPane = new javax.swing.JScrollPane();
        referenceJTextArea = new javax.swing.JTextArea();
        pmidLabel = new javax.swing.JLabel();
        doiLabel = new javax.swing.JLabel();
        doiJTextField = new javax.swing.JTextField();
        pmidIDJTextField = new javax.swing.JTextField();
        okJButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("New Reference");
        setResizable(false);

        backgroundPanel.setBackground(new java.awt.Color(230, 230, 230));

        referenceDetailsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Reference"));
        referenceDetailsPanel.setOpaque(false);

        referenceLabel.setForeground(new java.awt.Color(255, 0, 0));
        referenceLabel.setText("Reference*");

        referenceJTextArea.setColumns(20);
        referenceJTextArea.setFont(referenceJTextArea.getFont());
        referenceJTextArea.setLineWrap(true);
        referenceJTextArea.setRows(3);
        referenceJTextArea.setWrapStyleWord(true);
        referenceJTextArea.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                referenceJTextAreaKeyReleased(evt);
            }
        });
        referenceScrollPane.setViewportView(referenceJTextArea);

        pmidLabel.setText("PMID");
        pmidLabel.setToolTipText("PubMed ID");

        doiLabel.setText("DOI");
        doiLabel.setToolTipText("Digital Object Identifier");

        doiJTextField.setToolTipText("Digital Object Identifier");
        doiJTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                doiJTextFieldKeyReleased(evt);
            }
        });

        pmidIDJTextField.setToolTipText("PubMed ID");
        pmidIDJTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                pmidIDJTextFieldKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout referenceDetailsPanelLayout = new javax.swing.GroupLayout(referenceDetailsPanel);
        referenceDetailsPanel.setLayout(referenceDetailsPanelLayout);
        referenceDetailsPanelLayout.setHorizontalGroup(
            referenceDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(referenceDetailsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(referenceDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(referenceDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(referenceDetailsPanelLayout.createSequentialGroup()
                            .addComponent(pmidLabel)
                            .addGap(35, 35, 35))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, referenceDetailsPanelLayout.createSequentialGroup()
                            .addComponent(referenceLabel)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                    .addGroup(referenceDetailsPanelLayout.createSequentialGroup()
                        .addComponent(doiLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(referenceDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(referenceScrollPane, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 432, Short.MAX_VALUE)
                    .addComponent(pmidIDJTextField)
                    .addComponent(doiJTextField, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap())
        );
        referenceDetailsPanelLayout.setVerticalGroup(
            referenceDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(referenceDetailsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(referenceDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(referenceLabel)
                    .addComponent(referenceScrollPane))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(referenceDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pmidLabel)
                    .addComponent(pmidIDJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(referenceDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(doiJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(doiLabel))
                .addContainerGap())
        );

        okJButton.setText("OK");
        okJButton.setEnabled(false);
        okJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okJButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout backgroundPanelLayout = new javax.swing.GroupLayout(backgroundPanel);
        backgroundPanel.setLayout(backgroundPanelLayout);
        backgroundPanelLayout.setHorizontalGroup(
            backgroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(backgroundPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(backgroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(referenceDetailsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, backgroundPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(okJButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton)))
                .addContainerGap())
        );

        backgroundPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cancelButton, okJButton});

        backgroundPanelLayout.setVerticalGroup(
            backgroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(backgroundPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(referenceDetailsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(backgroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(okJButton)
                    .addComponent(cancelButton))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(backgroundPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(backgroundPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Enables/disables the OK button.
     * 
     * @param evt 
     */
    private void referenceJTextAreaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_referenceJTextAreaKeyReleased
        validateInput();
    }//GEN-LAST:event_referenceJTextAreaKeyReleased

    /**
     * Enables/disables the OK button.
     * 
     * @param evt 
     */
    private void doiJTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_doiJTextFieldKeyReleased
        referenceJTextAreaKeyReleased(null);
    }//GEN-LAST:event_doiJTextFieldKeyReleased

    /**
     * Enables/disables the OK button.
     * 
     * @param evt 
     */
    private void pmidIDJTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pmidIDJTextFieldKeyReleased
        referenceJTextAreaKeyReleased(null);
    }//GEN-LAST:event_pmidIDJTextFieldKeyReleased

    /**
     * Adds/updates the reference.
     * 
     * @param evt 
     */
    private void okJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okJButtonActionPerformed
        
        String tempPmid = null;
        String tempDoi = null;

        if (pmidIDJTextField.getText().length() > 0) {
            tempPmid = pmidIDJTextField.getText();
        }

        if (doiJTextField.getText().length() > 0) {
            tempDoi = doiJTextField.getText();
        }

        if (rowIndex != -1) {
            referenceGroupDialog.editReference(new Reference(referenceJTextArea.getText(), tempPmid, tempDoi), rowIndex);
        } else {
            referenceGroupDialog.insertReference(new Reference(referenceJTextArea.getText(), tempPmid, tempDoi));
        }

        dispose();  
    }//GEN-LAST:event_okJButtonActionPerformed

     /**
     * Close the dialog without saving.
     * 
     * @param evt 
     */
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel backgroundPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JTextField doiJTextField;
    private javax.swing.JLabel doiLabel;
    private javax.swing.JButton okJButton;
    private javax.swing.JTextField pmidIDJTextField;
    private javax.swing.JLabel pmidLabel;
    private javax.swing.JPanel referenceDetailsPanel;
    private javax.swing.JTextArea referenceJTextArea;
    private javax.swing.JLabel referenceLabel;
    private javax.swing.JScrollPane referenceScrollPane;
    // End of variables declaration//GEN-END:variables

    /**
     * Validate the input and enable/disable the OK button.
     */
    private void validateInput() {
        
//        String input = nameJTextField.getText();
//        for (String forbiddenCharacter : Util.forbiddenCharacters) {
//            if (input.contains(forbiddenCharacter)) {
//                JOptionPane.showMessageDialog(null, "'" + forbiddenCharacter + "' is not allowed in reference name.",
//                    "Forbidden Character", JOptionPane.WARNING_MESSAGE);
//                nameJTextField.setText(lastNameInput);
//                return;
//            }
//        }
//        lastNameInput = input;
        
        okJButton.setEnabled(referenceJTextArea.getText().length() > 0);
        
        if (referenceJTextArea.getText().length() > 0) {
            referenceLabel.setForeground(Color.BLACK);
        } else {
            referenceLabel.setForeground(Color.RED);
        }
    }
}
