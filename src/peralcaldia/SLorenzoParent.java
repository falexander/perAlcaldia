/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package peralcaldia;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author alex
 */
public class SLorenzoParent extends javax.swing.JFrame {

    /**
     * Creates new form SLorenzoParent
     */
    public SLorenzoParent() {
        initComponents();
        this.setExtendedState(this.MAXIMIZED_BOTH);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jpprincipal = new javax.swing.JPanel();
        jbarramenu = new javax.swing.JMenuBar();
        jmarchivo = new javax.swing.JMenu();
        jnuevo = new javax.swing.JMenu();
        jmcontribuyente = new javax.swing.JMenuItem();
        jmusuario = new javax.swing.JMenuItem();
        jmimpuesto = new javax.swing.JMenuItem();
        jminm = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        jmzona = new javax.swing.JMenuItem();
        jmdf = new javax.swing.JMenu();
        jmmnt = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jmpsalir = new javax.swing.JMenuItem();
        jmeditar = new javax.swing.JMenu();
        jmtrans = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem11 = new javax.swing.JMenuItem();

        jMenuItem1.setText("jMenuItem1");

        jMenuItem3.setText("jMenuItem3");

        jMenuItem6.setText("jMenuItem6");

        jMenuItem7.setText("jMenuItem7");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setName("FormularioPrincipal"); // NOI18N

        jpprincipal.setAutoscrolls(true);

        javax.swing.GroupLayout jpprincipalLayout = new javax.swing.GroupLayout(jpprincipal);
        jpprincipal.setLayout(jpprincipalLayout);
        jpprincipalLayout.setHorizontalGroup(
            jpprincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 924, Short.MAX_VALUE)
        );
        jpprincipalLayout.setVerticalGroup(
            jpprincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 713, Short.MAX_VALUE)
        );

        jbarramenu.setAutoscrolls(true);

        jmarchivo.setText("Archivo");

        jnuevo.setText("Nuevo");

        jmcontribuyente.setText("Contribuyente");
        jmcontribuyente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmcontribuyenteActionPerformed(evt);
            }
        });
        jnuevo.add(jmcontribuyente);

        jmusuario.setLabel("Usuario");
        jmusuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmusuarioActionPerformed(evt);
            }
        });
        jnuevo.add(jmusuario);

        jmimpuesto.setText("Impuesto");
        jmimpuesto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmimpuestoActionPerformed(evt);
            }
        });
        jnuevo.add(jmimpuesto);

        jminm.setText("Inmueble");
        jminm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jminmActionPerformed(evt);
            }
        });
        jnuevo.add(jminm);

        jMenuItem10.setText("Negocio");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jnuevo.add(jMenuItem10);

        jmzona.setText("Zona");
        jmzona.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmzonaActionPerformed(evt);
            }
        });
        jnuevo.add(jmzona);

        jmarchivo.add(jnuevo);

        jmdf.setText("Establecer");

        jmmnt.setText("Monto Impuesto");
        jmmnt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmmntActionPerformed(evt);
            }
        });
        jmdf.add(jmmnt);

        jMenuItem2.setText("Impuestos por Inmueble");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jmdf.add(jMenuItem2);

        jmarchivo.add(jmdf);

        jmpsalir.setText("Salir");
        jmpsalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmpsalirActionPerformed(evt);
            }
        });
        jmarchivo.add(jmpsalir);

        jbarramenu.add(jmarchivo);

        jmeditar.setText("Editar");
        jbarramenu.add(jmeditar);

        jmtrans.setText("Transacciones");

        jMenuItem4.setText("Generar Boleta de Pago");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jmtrans.add(jMenuItem4);

        jMenuItem5.setText("Carga Inicial");
        jmtrans.add(jMenuItem5);

        jMenuItem8.setText("Aplicar Pagos Inmuebles");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jmtrans.add(jMenuItem8);

        jMenuItem9.setText("Aplicar Pagos Negocios");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jmtrans.add(jMenuItem9);

        jMenuItem11.setText("Aplicar Pagos Clesa");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        jmtrans.add(jMenuItem11);

        jbarramenu.add(jmtrans);

        setJMenuBar(jbarramenu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpprincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpprincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jmcontribuyenteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmcontribuyenteActionPerformed
        addcontribuyente addc = new addcontribuyente();
        try {
            this.jpprincipal.add(addc);
            addc.setSelected(true);
            addc.toFront();            
            addc.setVisible(true);            
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar el formulario");                        
            e.printStackTrace();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_jmcontribuyenteActionPerformed

    private void jmusuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmusuarioActionPerformed
        // TODO add your handling code here:
        addusuario aduser = new addusuario();
        try {
            this.jpprincipal.add(aduser);
            aduser.setSelected(true);
            aduser.toFront();
            aduser.setVisible(true);
        } 
        catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar el formulario");            
            e.printStackTrace();
        }
    }//GEN-LAST:event_jmusuarioActionPerformed

    private void jmpsalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmpsalirActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jmpsalirActionPerformed

    private void jmzonaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmzonaActionPerformed
        // TODO add your handling code here:
        addzonas adzon = new addzonas();
        try {
            this.jpprincipal.add(adzon);
            adzon.setSelected(true);
            adzon.toFront();
            adzon.setVisible(true);
        } 
        catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar el formulario");
            e.printStackTrace();
        }
    }//GEN-LAST:event_jmzonaActionPerformed

    private void jmimpuestoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmimpuestoActionPerformed
        // TODO add your handling code here:
        addimpuestos adimp = new addimpuestos();
        try {
            this.jpprincipal.add(adimp);
            adimp.setSelected(true);
            adimp.toFront();
            adimp.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar el formulario");
            e.printStackTrace();
        }
    }//GEN-LAST:event_jmimpuestoActionPerformed

    private void jminmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jminmActionPerformed
        // TODO add your handling code here:
        addinmueble adinm = new addinmueble();
        try {
            this.jpprincipal.add(adinm);
            adinm.setSelected(true);
            adinm.toFront();
            adinm.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar el formulario");
            e.printStackTrace();
        }
    }//GEN-LAST:event_jminmActionPerformed

    private void jmmntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmmntActionPerformed
        // TODO add your handling code here:
        addmntimpuestos admnti = new addmntimpuestos();
        try {
            this.jpprincipal.add(admnti);
            admnti.setSelected(true);
            admnti.toFront();
            admnti.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar el formulario");
            e.printStackTrace();
        }
    }//GEN-LAST:event_jmmntActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
        addimpuestosinmuebles adiminm = new addimpuestosinmuebles();
        try {
            this.jpprincipal.add(adiminm);
            adiminm.setSelected(true);
            adiminm.toFront();
            adiminm.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar el formulario");
            e.printStackTrace();
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        // TODO add your handling code here:
        generarpagos boleta = new generarpagos();
        try {
            this.jpprincipal.add(boleta);
            boleta.setSelected(true);
            boleta.toFront();
            boleta.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar el formulario");
            e.printStackTrace();            
        }
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        // TODO add your handling code here:
        aplicarpagos pg = new aplicarpagos();
        try {
            this.jpprincipal.add(pg);
            pg.setSelected(true);
            pg.toFront();
            pg.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar el formulario");
            e.printStackTrace();
        }
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        // TODO add your handling code here:
        addnegocio ngc = new addnegocio();
        try {
            this.jpprincipal.add(ngc);
            ngc.setSelected(true);
            ngc.toFront();
            ngc.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar el formulario");
            e.printStackTrace();
        }
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        // TODO add your handling code here:
        pagosnegocios pgneg = new pagosnegocios();
        try {
            this.jpprincipal.add(pgneg);
            pgneg.setSelected(true);
            pgneg.toFront();
            pgneg.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar el formulario");
            e.printStackTrace();            
        }
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
        // TODO add your handling code here:
        clesapagos clpagos = new clesapagos();
        try {
            this.jpprincipal.add(clpagos);
            clpagos.setSelected(true);
            clpagos.toFront();
            clpagos.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar el formulario");
            e.printStackTrace();
        }
    }//GEN-LAST:event_jMenuItem11ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SLorenzoParent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SLorenzoParent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SLorenzoParent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SLorenzoParent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SLorenzoParent().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JMenuBar jbarramenu;
    private javax.swing.JMenu jmarchivo;
    private javax.swing.JMenuItem jmcontribuyente;
    private javax.swing.JMenu jmdf;
    private javax.swing.JMenu jmeditar;
    private javax.swing.JMenuItem jmimpuesto;
    private javax.swing.JMenuItem jminm;
    private javax.swing.JMenuItem jmmnt;
    private javax.swing.JMenuItem jmpsalir;
    private javax.swing.JMenu jmtrans;
    private javax.swing.JMenuItem jmusuario;
    private javax.swing.JMenuItem jmzona;
    private javax.swing.JMenu jnuevo;
    private javax.swing.JPanel jpprincipal;
    // End of variables declaration//GEN-END:variables
}
