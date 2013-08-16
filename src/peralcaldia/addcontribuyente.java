/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package peralcaldia;

import controller.AbstractDAO;
import java.util.Iterator;
import java.util.List;
import javax.swing.JOptionPane;
import util.codemd5;
import peralcaldia.model.Contribuyentes;
import peralcaldia.model.Usuarios;
import peralcaldia.model.Roles;
import peralcaldia.model.Estados;
/**
 *
 * @author alex
 */
public class addcontribuyente extends javax.swing.JInternalFrame {
        AbstractDAO dao = new AbstractDAO();
    /**
     * Creates new form addcontribuyente
     */
    public addcontribuyente() {
        initComponents();
        fillComboBoxrol();
        fillComboBoxestado();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    private void fillComboBoxrol(){
        List<Roles> lrol=null;
        Roles rolesobj=null;
        lrol = dao.findAll(Roles.class);
        
        Iterator it = lrol.iterator();
        
        while(it.hasNext()){
            rolesobj = (Roles) it.next();
            cmbrol.addItem(rolesobj.getRol());
        }        
    }
    
    private void fillComboBoxestado(){
        List<Estados> lest=null;
        Estados estadosobj=null;
        lest = dao.findAll(Estados.class);
        
        Iterator it = lest.iterator();
        
        while(it.hasNext()){
            estadosobj = (Estados) it.next();
            cmbestado.addItem(estadosobj.getEstado());
        }        
    }
    
    public void guardarcontribuyente(){
        //Declaracion de Variables.
        StringBuilder strBuild = new StringBuilder();
        String key = "";
        Usuarios user= new Usuarios();
        Contribuyentes contribuyente=new Contribuyentes();
        Roles rl=new Roles();
        Estados st=new Estados();

        //Encriptando password.
        strBuild.append(txtpassword.getPassword());
        key=codemd5.getmd5(key);
        
        //Seteando las variables a las valores introducidos por el usuario.
        //Usuario
        rl = (Roles) dao.findByWhereStatementoneobj(Roles.class, "rol='"+cmbrol.getSelectedItem().toString() +"'");
        st = (Estados) dao.findByWhereStatementoneobj(Estados.class, "estado='"+cmbestado.getSelectedItem().toString() +"'");
        user.setAlias(txtusername.getText());
        user.setApellidos(txtapellidos.getText());
        user.setDireccion(txtdireccion.getText());
        user.setEmail(txtemail.getText());
        user.setEstados(st);
        user.setNombres(txtnombres.getText());
        user.setPassword(key);
        user.setRoles(rl);
        user.setTelefonocelular(txtmovil.getText());
        user.setTelefonofijo(txttelefono.getText());
        //Contribuyente       
        contribuyente.setDui(txtdui.getText());
        contribuyente.setNiss(txtniss.getText());
        if (jrnit.isSelected() == true)
            {
            contribuyente.setNit(txtnit.getText());
            }
        else
            {
            contribuyente.setNitjuridico(txtnit.getText());
            }
        contribuyente.setUsuarios(user);
        try {
            Usuarios cmpalias = new Usuarios();
            cmpalias = (Usuarios) dao.findByWhereStatementoneobj(Usuarios.class, "alias='"+ txtusername.getText() +"'");
            if (cmpalias == null) {
                cmpalias= (Usuarios) dao.findByWhereStatementoneobj(Usuarios.class, "email='"+ txtemail.getText() +"'");
                if (cmpalias==null){
                    dao.save(user);
                    dao.save(contribuyente);
                    JOptionPane.showMessageDialog(this, "Guardado Completado");                                        
                }
                else {
                    JOptionPane.showMessageDialog(this, "Correo registrado, Ingresar uno diferente");                                                        
                }
            }
            else{
                JOptionPane.showMessageDialog(this, "El usuario ya existe, por favor ingresar un usuario diferente");
            }
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Hubo un problema al guardar la información, por favor intente mas tarde");
        }
        

    }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtnombres = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtapellidos = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtusername = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtpassword = new javax.swing.JPasswordField();
        jLabel7 = new javax.swing.JLabel();
        txtemail = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txttelefono = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtmovil = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtdireccion = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtdui = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtniss = new javax.swing.JTextField();
        jrnit = new javax.swing.JRadioButton();
        jrnjuridico = new javax.swing.JRadioButton();
        txtnit = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        cmbrol = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        cmbestado = new javax.swing.JComboBox();
        btnguardar = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);

        jLabel3.setText("Nombres:");

        txtnombres.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtnombresKeyTyped(evt);
            }
        });

        jLabel4.setText("Apellidos:");

        txtapellidos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtapellidosKeyTyped(evt);
            }
        });

        jLabel5.setText("Username:");

        jLabel6.setText("Password:");

        jLabel7.setText("Email:");

        jLabel8.setText("Telefono:");

        txttelefono.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txttelefonoKeyTyped(evt);
            }
        });

        jLabel9.setText("Celular:");

        txtmovil.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtmovilKeyTyped(evt);
            }
        });

        jLabel10.setText("Dirección:");

        jLabel11.setText("DUI:");

        txtdui.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtduiKeyTyped(evt);
            }
        });

        jLabel13.setText("NISS:");

        txtniss.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtnissKeyTyped(evt);
            }
        });

        buttonGroup1.add(jrnit);
        jrnit.setSelected(true);
        jrnit.setText("NIT");

        buttonGroup1.add(jrnjuridico);
        jrnjuridico.setText("NIT JURIDICO");

        jLabel1.setText("ROL:");

        cmbrol.setName(""); // NOI18N

        jLabel2.setText("ESTADO:");

        btnguardar.setText("Guardar");
        btnguardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnguardarActionPerformed(evt);
            }
        });

        jButton1.setLabel("Salir");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
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
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtnit)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(18, 18, 18)
                                .addComponent(cmbrol, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cmbestado, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jrnit)
                                .addGap(54, 54, 54)
                                .addComponent(jrnjuridico))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel5)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(txtusername))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel8)
                                    .addGap(18, 18, 18)
                                    .addComponent(txttelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(171, 171, 171)
                                    .addComponent(jLabel9)
                                    .addGap(18, 18, 18)
                                    .addComponent(txtmovil))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel6)
                                        .addComponent(jLabel7))
                                    .addGap(14, 14, 14)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtemail)
                                        .addComponent(txtpassword)))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel10)
                                        .addComponent(jLabel11))
                                    .addGap(18, 18, 18)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtdireccion)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(txtdui, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(48, 48, 48)
                                            .addComponent(jLabel13)
                                            .addGap(29, 29, 29)
                                            .addComponent(txtniss, javax.swing.GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE))))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel3)
                                        .addComponent(jLabel4))
                                    .addGap(18, 18, 18)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtapellidos)
                                        .addComponent(txtnombres)))))
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(158, 158, 158)
                .addComponent(btnguardar, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(84, 84, 84)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 229, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtnombres, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtapellidos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtusername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtpassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtemail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txttelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(txtmovil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtdireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txtdui, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtniss, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jrnit)
                    .addComponent(jrnjuridico))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtnit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cmbrol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(cmbestado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnguardar)
                    .addComponent(jButton1))
                .addContainerGap(35, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnguardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnguardarActionPerformed
        // TODO add your handling code here:
        guardarcontribuyente();
    }//GEN-LAST:event_btnguardarActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void txttelefonoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txttelefonoKeyTyped
        // TODO add your handling code here:
        char caracter = evt.getKeyChar();

        // Verificar si la tecla pulsada no es una letra y esta entre 0 y 9
        if(((caracter < '0') || (caracter > '9')) )
            evt.consume();
      
    }//GEN-LAST:event_txttelefonoKeyTyped

    private void txtnombresKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtnombresKeyTyped
        // TODO add your handling code here:
        char caracter = evt.getKeyChar();

       // Verificar si la tecla pulsada no es un numero y es una lera de la "a" a la "z" o de la "A" a la "Z"
      if(((caracter < 'a') || (caracter > 'z')) && ((caracter < 'A') || (caracter > 'Z')) && (caracter != ' ') )
          evt.consume();
      
        
    }//GEN-LAST:event_txtnombresKeyTyped

    private void txtapellidosKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtapellidosKeyTyped
        // TODO add your handling code here:
        char caracter = evt.getKeyChar();

      // Verificar si la tecla pulsada no es un numero y es una lera de la "a" a la "z" o de la "A" a la "Z"
      if(((caracter<'a')||(caracter>'z'))&&((caracter < 'A')||(caracter>'Z'))&& (caracter != ' ')) 
          evt.consume();
      
        
    }//GEN-LAST:event_txtapellidosKeyTyped

    private void txtduiKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtduiKeyTyped
        // TODO add your handling code here:
        
        char caracter = evt.getKeyChar();

        // Verificar si la tecla pulsada no es una letra y esta entre 0 y 9 ademas de un guion
        if(((caracter < '0') || (caracter > '9'))&& (caracter != '-') )
            evt.consume();
    }//GEN-LAST:event_txtduiKeyTyped

    private void txtmovilKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtmovilKeyTyped
        // TODO add your handling code here:
        
        char caracter = evt.getKeyChar();

        // Verificar si la tecla pulsada no es una letra y esta entre 0 y 9
        if(((caracter < '0') || (caracter > '9')) )
            evt.consume();
    }//GEN-LAST:event_txtmovilKeyTyped

    private void txtnissKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtnissKeyTyped
        // TODO add your handling code here:
        char caracter = evt.getKeyChar();

        // Verificar si la tecla pulsada no es una letra y esta entre 0 y 9
        if(((caracter < '0') || (caracter > '9')) )
            evt.consume();
    }//GEN-LAST:event_txtnissKeyTyped

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnguardar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox cmbestado;
    private javax.swing.JComboBox cmbrol;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JRadioButton jrnit;
    private javax.swing.JRadioButton jrnjuridico;
    private javax.swing.JTextField txtapellidos;
    private javax.swing.JTextField txtdireccion;
    private javax.swing.JTextField txtdui;
    private javax.swing.JTextField txtemail;
    private javax.swing.JTextField txtmovil;
    private javax.swing.JTextField txtniss;
    private javax.swing.JTextField txtnit;
    private javax.swing.JTextField txtnombres;
    private javax.swing.JPasswordField txtpassword;
    private javax.swing.JTextField txttelefono;
    private javax.swing.JTextField txtusername;
    // End of variables declaration//GEN-END:variables
}
