/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package peralcaldia;

import com.sun.org.apache.bcel.internal.generic.LCONST;
import controller.AbstractDAO;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.swing.JOptionPane;
import peralcaldia.model.Usuarios;
import peralcaldia.model.Contribuyentes;
import peralcaldia.model.Negocios;
import peralcaldia.model.Estados;
import peralcaldia.model.Tiposcomercio;
import peralcaldia.model.Giros;
/**
 *
 * @author alex
 */
public class addnegocio extends javax.swing.JInternalFrame {
    
    AbstractDAO dao = new AbstractDAO();
    Giros giro = new Giros();

    /**
     * Creates new form addnegocio
     */
    public addnegocio() {
        initComponents();
        cmbcontribuyente.addItem("-");
        fillcomboboxgiros();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    public void cargarcontribuyentes(){
        List<Usuarios> luser = null;
        Usuarios user = new Usuarios();
        
        try { 
            if (rbtnnombres.isSelected()) {
                luser = dao.findByWhereStatement(Usuarios.class, "nombres || ' '||apellidos like '%" + txtbuscarcnt.getText().toUpperCase() + "%' and roles_id= 3");                
            }
            else if(rbtnapellidos.isSelected()){
                luser = dao.findByWhereStatement(Usuarios.class, "apellidos like '%" + txtbuscarcnt.getText().toUpperCase() + "%' and roles_id=3");
            }
            
            if (!luser.isEmpty()) {
                Iterator it = luser.iterator();
                
                cmbcontribuyente.addItem("-");
                while (it.hasNext()) {
                    user = (Usuarios) it.next();
                    cmbcontribuyente.addItem(user.getNombres() + " " + user.getApellidos());
                }
            }
            else{
                JOptionPane.showMessageDialog(this, "No se encuentran usuarios registrados con el valor ingresado");
            }

            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error durante la busqueda");
            e.printStackTrace();
        }        
    }
    
    public void fillcomboboxgiros(){
        List<Giros> lgiros = null;
        Giros giro = new Giros();
        
        try {
            lgiros = dao.findAll(Giros.class);
            
            Iterator it = lgiros.iterator();
            
            cmbgiro.addItem("-");
            while (it.hasNext()) {
                giro = (Giros) it.next();
                cmbgiro.addItem(giro.getDescripcion());
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error en la carga de los giros de las empresas");
            e.printStackTrace();
        }
        
    }
    
    public void fillcomboboxactividad(){
        List<Tiposcomercio> ltipos = null;
        Tiposcomercio tipo = new Tiposcomercio();
        Giros tmpgiro = new Giros();
        cmbactividad.addItem("-");
        try {
            tmpgiro = (Giros) dao.findByWhereStatementoneobj(Giros.class, "descripcion like '%" + cmbgiro.getSelectedItem().toString() + "%' and estados_id = 1");
            giro = tmpgiro;
            ltipos = dao.findByWhereStatement(Tiposcomercio.class, "giros_id = ' " + tmpgiro.getId() + "' and estados_id=1");
            
            Iterator it = ltipos.iterator();
            
            while (it.hasNext()) {
                tipo = (Tiposcomercio) it.next();
                cmbactividad.addItem(tipo.getActividadeconomica());
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error en la carga de los tipos relacionados al giro");
            e.printStackTrace();
        }
        
    }
    
    public void guardarnegocio(){
        Tiposcomercio tipo = new Tiposcomercio();
        Usuarios users = new Usuarios();
        Set<Contribuyentes> lcont = null;
        Contribuyentes cont = new Contribuyentes();
        Estados est = new Estados();        
        Negocios negocio = new Negocios();
        Negocios tneg = new Negocios();        
                
        try {
            est = (Estados) dao.findByWhereStatementoneobj(Estados.class, "id = 1");
            users = (Usuarios) dao.findByWhereStatementoneobj(Usuarios.class, " (nombres || ' ' || apellidos) = '"+ cmbcontribuyente.getSelectedItem().toString() +"'");
            lcont = users.getContribuyenteses();
            
            Iterator it = lcont.iterator();
            while (it.hasNext()) {
                cont = (Contribuyentes) it.next();                
            }
            
            tipo = (Tiposcomercio) dao.findByWhereStatementoneobj(Tiposcomercio.class, "actividadeconomica = '"+ cmbactividad.getSelectedItem().toString() + "'");
            
            negocio.setContribuyentes(cont);
            negocio.setEstados(est);
            negocio.setGiros(giro);            
            negocio.setTelefono(txttelefono.getText());
            negocio.setTipos(tipo);
            negocio.setFecha_registro(jdfecharegistro.getDate());                        
            
            try {
                tneg = (Negocios) dao.findByWhereStatementoneobj(Negocios.class, "direccion like '%" + txtdireccion.getText().toUpperCase() + "%' and estados_id =1");
                if (tneg == null) {
                    negocio.setDireccion(txtdireccion.getText().toUpperCase());
                    tneg = (Negocios) dao.findByWhereStatementoneobj(Negocios.class, "nombreempresa like '%" + txtnempresa.getText().toUpperCase() + "%' and estados_id =1");                                
                    if (tneg == null) {
                        negocio.setNombreempresa(txtnempresa.getText().toUpperCase());                                        
                            dao.save(negocio);
                            JOptionPane.showMessageDialog(this, "Guardado Completo");
                            try {
                                limpiar();
                            } catch (Exception e) {
                                JOptionPane.showMessageDialog(this, "Error durante limpieza de los datos");
                            }                        
                    }
                    else{
                        JOptionPane.showMessageDialog(this, "El nombre de la empresa ya se encuentra registrado");                        
                    }                    
                }
                else {
                    JOptionPane.showMessageDialog(this, "Esta direccion ya se encuentra registrada para un negocio activo, favor verificar");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Hubo un error en el guardado de los datos");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error durante la carga de datos a guardar");
        }
        
    }
    
    public void limpiar(){
        cmbcontribuyente.removeAllItems();
        cmbactividad.removeAllItems();
        txtbuscarcnt.setText("");
        txtnempresa.setText("");
        txttelefono.setText("");
        txtdireccion.setText("");
        cmbgiro.setSelectedIndex(0);
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jTextField1 = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtbuscarcnt = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        cmbcontribuyente = new javax.swing.JComboBox();
        rbtnnombres = new javax.swing.JRadioButton();
        rbtnapellidos = new javax.swing.JRadioButton();
        jLabel2 = new javax.swing.JLabel();
        txtnempresa = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtdireccion = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        cmbgiro = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        cmbactividad = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        txttelefono = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jdfecharegistro = new com.toedter.calendar.JDateChooser();
        jPanel3 = new javax.swing.JPanel();
        btnguardar = new javax.swing.JButton();
        btnsalir = new javax.swing.JButton();

        jTextField1.setText("jTextField1");

        setClosable(true);
        setIconifiable(true);

        jLabel1.setText("Contribuyente :");

        jButton1.setText("Cargar Contribuyentes");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        buttonGroup1.add(rbtnnombres);
        rbtnnombres.setSelected(true);
        rbtnnombres.setText("Nombres");

        buttonGroup1.add(rbtnapellidos);
        rbtnapellidos.setText("Apellidos");

        jLabel2.setText("Nombre Empresa:");

        jLabel7.setText("Dirección:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(rbtnnombres)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(rbtnapellidos)
                        .addGap(233, 233, 233))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cmbcontribuyente, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtbuscarcnt, javax.swing.GroupLayout.PREFERRED_SIZE, 377, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel7))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtnempresa)
                                    .addComponent(txtdireccion))))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbtnnombres)
                    .addComponent(rbtnapellidos))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtbuscarcnt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbcontribuyente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtnempresa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtdireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jLabel3.setText("Giro:");

        cmbgiro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbgiroActionPerformed(evt);
            }
        });

        jLabel4.setText("Actividad:");

        jLabel5.setText("Telefono:");

        jLabel6.setText("Fecha Registro:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbgiro, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbactividad, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txttelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jdfecharegistro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(txttelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel6))
                    .addComponent(jdfecharegistro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cmbgiro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(cmbactividad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

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
                .addGap(99, 99, 99)
                .addComponent(btnguardar, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(83, 83, 83)
                .addComponent(btnsalir, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnguardar)
                    .addComponent(btnsalir))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        cmbcontribuyente.removeAllItems();
        cargarcontribuyentes();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnsalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsalirActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnsalirActionPerformed

    private void cmbgiroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbgiroActionPerformed
        // TODO add your handling code here:
        cmbactividad.removeAllItems();
        if (!cmbgiro.getSelectedItem().toString().equals("-")) {
            fillcomboboxactividad();
        }
    }//GEN-LAST:event_cmbgiroActionPerformed

    private void btnguardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnguardarActionPerformed
        // TODO add your handling code here:
        guardarnegocio();
    }//GEN-LAST:event_btnguardarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnguardar;
    private javax.swing.JButton btnsalir;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox cmbactividad;
    private javax.swing.JComboBox cmbcontribuyente;
    private javax.swing.JComboBox cmbgiro;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTextField jTextField1;
    private com.toedter.calendar.JDateChooser jdfecharegistro;
    private javax.swing.JRadioButton rbtnapellidos;
    private javax.swing.JRadioButton rbtnnombres;
    private javax.swing.JTextField txtbuscarcnt;
    private javax.swing.JTextField txtdireccion;
    private javax.swing.JTextField txtnempresa;
    private javax.swing.JTextField txttelefono;
    // End of variables declaration//GEN-END:variables
}
