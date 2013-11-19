/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package peralcaldia.MantenimientosIngreso;

import beans.VerTiposComercio;
import controller.GrDAO;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import peralcaldia.model.Estados;
import peralcaldia.model.Giros;
import peralcaldia.model.Tiposcomercio;
import util.GCMPNativos;

/**
 *
 * @author alex
 */
/*Pantalla Utilizada Para el Guardado de Nuevos Tipos de Comercio Segun los 
 * Giros Comerciales Existentes*/
public class AgregarTiposComercio extends javax.swing.JInternalFrame {

    DefaultComboBoxModel modelogiros = new DefaultComboBoxModel();
    DefaultTableModel tablam = new DefaultTableModel();
    GrDAO daogiro = new GrDAO();
    VerTiposComercio vtc = new VerTiposComercio();
    List otls = new ArrayList();
    Giros giro = new Giros();
    Tiposcomercio tipo = new Tiposcomercio();
    GCMPNativos componentes = new GCMPNativos();
    /**
     * Creates new form AgregarTiposComercio
     */
    public AgregarTiposComercio() {
        initComponents();
        modelogiros.addElement("-");
        cmbgiros.setModel(modelogiros);
        LlenarGiros();
        LlenarTabla();
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension ventana = this.getSize();
        this.setLocation((pantalla.width - ventana.width) / 2, (pantalla.height - ventana.height) / 4);        
    }

    /*Metodo Utilizado para la carga de los Giros en su correspondiente combo*/
    public void LlenarGiros(){
        List lista = null;
        try {
            lista = daogiro.find_giros_whereStatement(" estados_id = 1");
            if (lista.size() >0 && !lista.isEmpty()) {
                componentes.llenarmodelo(modelogiros, lista);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error en la carga de Giros");
            System.out.println(e.toString());
        }
    }
    
    /*Metodo Utilizado para Cargar los datos de los giros existentes en la BD junto a sus 
     * Caracterisitcas en la Tabla que se muestra en Pantalla*/
    public void LlenarTabla() {
        List<Tiposcomercio> ltipos = null;
        Tiposcomercio tmptipo = new Tiposcomercio();
        vtc = new VerTiposComercio();
        try {
            ltipos = daogiro.findAll(Tiposcomercio.class);
            if (ltipos.size() > 0 && !ltipos.isEmpty()) {
                otls = new ArrayList();
                Iterator it = ltipos.iterator();
                while (it.hasNext()) {
                    tmptipo = (Tiposcomercio) it.next();
                    vtc = new VerTiposComercio();
                    vtc.setACTIVIDAD_ECONOMICA(tmptipo.getActividadeconomica());
                    vtc.setESTADO(tmptipo.getEstados().getEstado());
                    vtc.setGIRO(tmptipo.getGiros().getDescripcion());
                    otls.add(vtc);
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        tablam = componentes.limpiartabla(tablam);
        tablam = componentes.llenartabla(tablam, otls, VerTiposComercio.class);
        jttiposcomercio.setModel(tablam);
        componentes.centrarcabeceras(jttiposcomercio);
    }
    
    /*Limpiando pantalla*/
    public void LimpiarPantalla(){
        cmbgiros.setSelectedIndex(0);
        txtactividad.setText("");
    }
    
    /*Metodo utilizado para la verificacion, creacion y guardado de los nuevos tipos de comercio*/
    public void GuardarTipoComercio(){
        Tiposcomercio tmptipos = new Tiposcomercio();
        Estados estado = new Estados();
        tipo = new Tiposcomercio();
        try {
            if (!txtactividad.getText().isEmpty() && !cmbgiros.getSelectedItem().toString().equals("-")) {
                tmptipos = (Tiposcomercio) daogiro.findByWhereStatementoneobj(Tiposcomercio.class, " actividadeconomica = '" + txtactividad.getText().toUpperCase() + "'");
                if (tmptipos == null) {
                    /*Recuperando el Giro y Estado de la Nueva Actividad*/
                    giro = (Giros) daogiro.findByWhereStatementoneobj(Giros.class, " descripcion = '" + cmbgiros.getSelectedItem().toString() + "'");
                    estado = (Estados) daogiro.findByWhereStatementoneobj(Estados.class, " id = 1");
                    /*Seteando las variables*/
                    tipo.setActividadeconomica(txtactividad.getText().toUpperCase());
                    tipo.setEstados(estado);
                    tipo.setGiros(giro);
                    
                    /*Guardando la Nueva Instancia*/
                    daogiro.save(tipo);
                    JOptionPane.showMessageDialog(this, "Guardado Completado");
                    LimpiarPantalla();
                }else{
                    JOptionPane.showMessageDialog(this, "El Nombre Ingresado ya ha sido asignado a un Tipo de Comercio \n Favor Verificar Antes de Guardar");
                }
                
            }else{
                JOptionPane.showMessageDialog(this, "Ingrese los Datos Necesarios para el Guardado de Datos");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error en el Guardado del Nuevo Tipo");
            System.out.println(e.toString());
        }
        
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jttiposcomercio = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cmbgiros = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        txtactividad = new componentesheredados.LettersJTextField();
        jPanel3 = new javax.swing.JPanel();
        btnguardar = new javax.swing.JButton();
        btnsalir = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jttiposcomercio.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(jttiposcomercio);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 380, -1));

        jLabel1.setText("Giro:");

        jLabel2.setText("Actividad Economica:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(cmbgiros, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(28, 28, 28)
                        .addComponent(txtactividad, javax.swing.GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cmbgiros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtactividad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 173, 380, -1));

        btnguardar.setText("Guardar");
        btnguardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnguardarActionPerformed(evt);
            }
        });

        btnsalir.setText("Salir");
        btnsalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsalirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnguardar, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(103, 103, 103)
                .addComponent(btnsalir, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(93, 93, 93))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnguardar)
                    .addComponent(btnsalir))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 265, 380, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnsalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsalirActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnsalirActionPerformed

    private void btnguardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnguardarActionPerformed
        // TODO add your handling code here:
        GuardarTipoComercio();
        LlenarTabla();
    }//GEN-LAST:event_btnguardarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnguardar;
    private javax.swing.JButton btnsalir;
    private javax.swing.JComboBox cmbgiros;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jttiposcomercio;
    private componentesheredados.LettersJTextField txtactividad;
    // End of variables declaration//GEN-END:variables
}
