/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package peralcaldia.MantenimientosModificaciones;

import controller.AbstractDAO;
import controller.UserDAO;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import peralcaldia.model.Estados;
import peralcaldia.model.Roles;
import peralcaldia.model.Usuarios;
import util.GCMPNativos;
import util.codemd5;

/**
 *
 * @author alex
 */
/*Pantalla utilizada para la modificacion de los usuarios ingresados en el sistema.*/
public class ModificarUsuarios extends javax.swing.JInternalFrame {
    
    UserDAO dao = new UserDAO();
    GCMPNativos componentes = new GCMPNativos();
    DefaultComboBoxModel rolmodel = new DefaultComboBoxModel();
    DefaultComboBoxModel estmodel = new DefaultComboBoxModel();    
    DefaultComboBoxModel usermodel = new DefaultComboBoxModel();
    Usuarios user = new Usuarios();
    /**
     * Creates new form ModificarUsuarios
     */
    public ModificarUsuarios() {
        initComponents();
        cmbestado.setModel(estmodel);
        cmbrol.setModel(rolmodel);
        usermodel.addElement("-");
        cmbusuario.setModel(usermodel);
        fillComboBoxrol();
        fillComboBoxestado();
        txtusername.setEnabled(false);
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension ventana = this.getSize();
        this.setLocation((pantalla.width - ventana.width) / 2, (pantalla.height - ventana.height) / 4);        
    }
    
    /*Carga de los roles que pueden ser asignados a los usuarios.*/
    private void fillComboBoxrol() {
        List lista = null;
        try {
            lista = dao.findByWhereStatement("Select rol from Roles where rol <> 'CONTRIBUYENTE' and rol <> 'USUARIO' ");
            componentes.llenarmodelo(rolmodel, lista);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    /*Carga de los estados validos para los usuarios*/
    private void fillComboBoxestado() {
        List lista = null;        
        try {
            lista = dao.findByWhereStatement("Select estado from Estados where estado = 'ACTIVO' or estado='INACTIVO' ");
            componentes.llenarmodelo(estmodel, lista);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    
    /*Busqueda de usuarios según alias*/
    public void buscarusuarios(){
        List luser = null;
        try {
            luser = dao.find_alias_usuarios(" alias like '%" + txtcontribuyente.getText().toUpperCase() + "%' and roles_id <> 3 and roles_id <> 1");
            if (!luser.isEmpty()) {
                componentes.llenarmodelo(usermodel, luser);
                cmbusuario.setModel(usermodel);
                fillComboBoxrol();
                fillComboBoxestado();
            } else {
                JOptionPane.showMessageDialog(this, "No se encuentran usuarios registrados con el valor ingresado");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error en la busqueda de usuarios");
            System.out.println(e.toString());
        }                
    }
    
    /*Limpiar pantalla*/
    public void limpiarstage(){
        fillComboBoxestado();
        fillComboBoxrol();
        txtapellidos.setText("");
        txtdireccion.setText("");
        txtemail.setText("");
        txtmovil.setText("");
        txtnombres.setText("");
        txtpassword.setText("");
        txttelefono.setText("");
        txtusername.setText("");
    }
    
    /*Carga de Datos del Usuario seleccionado*/
    public void cargardatosusuario(){
        //variables de identificacion de rol y estado del usuario.
        Estados st = new Estados();
        Roles rl = new Roles();
        //Recuperando el usuario seleccionado en el combo
        user = (Usuarios) dao.findByWhereStatementoneobj(Usuarios.class, " alias = '"+ cmbusuario.getSelectedItem().toString() +"'");
        //Recuperando datos y realizando la carga en pantalla
        st = user.getEstados();
        rl = user.getRoles();
        txtapellidos.setText(user.getApellidos());
        txtdireccion.setText(user.getDireccion());
        txtemail.setText(user.getEmail());
        txtmovil.setText(user.getTelefonocelular());
        txtnombres.setText(user.getNombres());
        txtpassword.setText(user.getPassword());
        txttelefono.setText(user.getTelefonofijo());
        txtusername.setText(user.getAlias());
        cmbestado.setSelectedItem(st.getEstado());
        cmbrol.setSelectedItem(rl.getRol());
    }
    
    /*Modificacion del Usuario seleccionado*/
    public void modificarusuario(){
        //Declaracion de Variables
        Estados estado = new Estados();
        Roles rol = new Roles();
        String key;        
        //Probando el llenado de todos los campos.
        if (!txtapellidos.getText().isEmpty() && !txtdireccion.getText().isEmpty() && !txtemail.getText().isEmpty() && !txtmovil.getText().isEmpty() && !txtnombres.getText().isEmpty() && !txtpassword.getText().isEmpty() && !txttelefono.getText().isEmpty() && !cmbestado.getSelectedItem().toString().equals("-") && !cmbrol.getSelectedItem().toString().equals("-")) {
            //Verificando la contraseña en caso de cambio.
            if (!txtpassword.getText().equals(user.getPassword())) {
                key = codemd5.getmd5(txtpassword.getText());
                user.setPassword(key);
            }            
            //Seteando las variables
            estado = (Estados) dao.findByWhereStatementoneobj(Estados.class, "estado='" + cmbestado.getSelectedItem().toString() + "'");
            rol = (Roles) dao.findByWhereStatementoneobj(Roles.class, "rol='" + cmbrol.getSelectedItem().toString() + "'");
            user.setApellidos(txtapellidos.getText().toUpperCase());
            user.setDireccion(txtdireccion.getText().toUpperCase());
            user.setEstados(estado);
            user.setRoles(rol);
            user.setNombres(txtnombres.getText().toUpperCase());
            user.setTelefonocelular(txtmovil.getText());
            user.setTelefonofijo(txttelefono.getText());
            try {
                Usuarios cmpalias = new Usuarios();
                if (!txtemail.getText().equals(user.getEmail())){
                    cmpalias = (Usuarios) dao.findByWhereStatementoneobj(Usuarios.class, "email='" + txtemail.getText() + "'");
                    if (cmpalias == null) {
                        user.setEmail(txtemail.getText());
                        dao.update(user);
                        JOptionPane.showMessageDialog(this, "Guardado Completado");
                        cargardatosusuario();
                    } else{
                        JOptionPane.showMessageDialog(this, "El Email Ingresado ya se Encuentra en Uso");
                    }
                } else{
                    dao.update(user);
                    JOptionPane.showMessageDialog(this, "Guardado Completado");
                    cargardatosusuario();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Hay un error en el guardado de la información");
                System.out.println(e.toString());
            }
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
        jLabel1 = new javax.swing.JLabel();
        cmbrol = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        cmbestado = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtdireccion = new javax.swing.JTextField();
        txtnombres = new componentesheredados.LettersJTextField();
        txtapellidos = new componentesheredados.LettersJTextField();
        txtusername = new javax.swing.JTextField();
        txtpassword = new javax.swing.JPasswordField();
        txtemail = new componentesheredados.MailTextField();
        txttelefono = new componentesheredados.PhoneJTextField();
        txtmovil = new componentesheredados.PhoneJTextField();
        btnmodificar = new javax.swing.JButton();
        jbsalir = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        txtcontribuyente = new javax.swing.JTextField();
        btnbuscar = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        cmbusuario = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();

        jLabel1.setText("* ROL:");

        cmbrol.setName(""); // NOI18N
        cmbrol.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbrolActionPerformed(evt);
            }
        });

        jLabel2.setText("* ESTADO:");

        cmbestado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbestadoActionPerformed(evt);
            }
        });

        jLabel3.setText("* Nombres:");

        jLabel4.setText("* Apellidos:");

        jLabel5.setText("* Username:");

        jLabel6.setText("* Password:");

        jLabel7.setText("* Email:");

        jLabel8.setText("* Telefono:");

        jLabel9.setText("* Celular:");

        jLabel10.setText("* Dirección:");

        try {
            txttelefono.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        try {
            txtmovil.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        btnmodificar.setText("Modificar");
        btnmodificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnmodificarActionPerformed(evt);
            }
        });

        jbsalir.setLabel("Salir");
        jbsalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbsalirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbrol, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbestado, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel5)
                                .addComponent(jLabel4)
                                .addComponent(jLabel3)
                                .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING))
                            .addComponent(jLabel10))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txttelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtmovil, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(txtemail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtdireccion)
                            .addComponent(txtpassword)
                            .addComponent(txtusername)
                            .addComponent(txtapellidos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtnombres, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(66, 66, 66)
                .addComponent(btnmodificar, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jbsalir, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(58, 58, 58))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(cmbrol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbestado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtnombres, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtapellidos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtusername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtpassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtemail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel8)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txttelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel9)
                        .addComponent(txtmovil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtdireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnmodificar)
                    .addComponent(jbsalir))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel11.setText("Buscar Usuarios:");

        btnbuscar.setText("Buscar");
        btnbuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnbuscarActionPerformed(evt);
            }
        });

        jLabel12.setText("Usuario:");

        cmbusuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbusuarioActionPerformed(evt);
            }
        });

        jButton1.setText("Cargar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtcontribuyente)
                    .addComponent(cmbusuario, 0, 184, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnbuscar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(btnbuscar)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(txtcontribuyente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12)
                            .addComponent(cmbusuario))))
                .addGap(22, 22, 22))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnmodificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnmodificarActionPerformed
        // TODO add your handling code here:
        modificarusuario();
    }//GEN-LAST:event_btnmodificarActionPerformed

    private void jbsalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbsalirActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jbsalirActionPerformed

    private void btnbuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbuscarActionPerformed
        // TODO add your handling code here:
        //Limpiando modelos cada vez que se realiza una nueva busqueda de contribuyentes.
        componentes.limpiarmodelo(usermodel);
        componentes.limpiarmodelo(rolmodel);
        componentes.limpiarmodelo(estmodel);
        usermodel.addElement("-");
        cmbusuario.setModel(usermodel);
        rolmodel.addElement("-");
        cmbrol.setModel(rolmodel);
        estmodel.addElement("-");
        cmbestado.setModel(estmodel);
        buscarusuarios();
    }//GEN-LAST:event_btnbuscarActionPerformed

    private void cmbusuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbusuarioActionPerformed
        // TODO add your handling code here:
        if (cmbusuario.getSelectedItem() != null) {
            limpiarstage();
            cmbusuario.setToolTipText(cmbusuario.getSelectedItem().toString());
        }
        
    }//GEN-LAST:event_cmbusuarioActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        if (!cmbusuario.getSelectedItem().toString().equals("-")) {
            cargardatosusuario();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void cmbrolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbrolActionPerformed
        // TODO add your handling code here:
        if (cmbrol.getSelectedItem() != null) {
            cmbrol.setToolTipText(cmbrol.getSelectedItem().toString());
        }
    }//GEN-LAST:event_cmbrolActionPerformed

    private void cmbestadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbestadoActionPerformed
        // TODO add your handling code here:
        if (cmbestado.getSelectedItem() != null) {
            cmbestado.setToolTipText(cmbestado.getSelectedItem().toString());
        }
    }//GEN-LAST:event_cmbestadoActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnbuscar;
    private javax.swing.JButton btnmodificar;
    private javax.swing.JComboBox cmbestado;
    private javax.swing.JComboBox cmbrol;
    private javax.swing.JComboBox cmbusuario;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JButton jbsalir;
    private componentesheredados.LettersJTextField txtapellidos;
    private javax.swing.JTextField txtcontribuyente;
    private javax.swing.JTextField txtdireccion;
    private componentesheredados.MailTextField txtemail;
    private componentesheredados.PhoneJTextField txtmovil;
    private componentesheredados.LettersJTextField txtnombres;
    private javax.swing.JPasswordField txtpassword;
    private componentesheredados.PhoneJTextField txttelefono;
    private javax.swing.JTextField txtusername;
    // End of variables declaration//GEN-END:variables
}
