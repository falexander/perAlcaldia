/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package peralcaldia;

import controller.AbstractDAO;
import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import peralcaldia.model.Inmuebles;
import peralcaldia.model.Contribuyentes;
import peralcaldia.model.Zonas;
import peralcaldia.model.Estados;
import peralcaldia.model.Usuarios;
import peralcaldia.model.historico_inmueble;
/**
 *
 * @author alex
 */
public class addinmueble extends javax.swing.JInternalFrame {
    AbstractDAO dao = new AbstractDAO();
    /**
     * Creates new form addinmueble
     */
    public addinmueble() {
        initComponents();
        fillComboBoxcontribuyente();
        fillComboBoxrol();
    }

    private void fillComboBoxrol(){
        List<Zonas> lzon= null;
        Zonas zonasobj=new Zonas();
        lzon = dao.findAll(Zonas.class);
        
        Iterator it = lzon.iterator();
        
        while(it.hasNext()){
            zonasobj = (Zonas) it.next();
            cmbzona.addItem(zonasobj.getZona());
        }        
    }    
    
    private void fillComboBoxcontribuyente(){
        List<Usuarios> lusu=null;
        Usuarios usuobj = new Usuarios();
        lusu = dao.findAll(Usuarios.class);
        
        Iterator it = lusu.iterator();
        
        while (it.hasNext()) {            
            usuobj = (Usuarios) it.next();
            cmbcontribuyente.addItem(usuobj.getNombres() +" "+usuobj.getApellidos());
        }        
    }
    
    public void guardarinmueble(){
        Inmuebles ninmueble = new Inmuebles();
        Zonas zninm = new Zonas();
        Estados stinm = new Estados();
        Contribuyentes ctbinm = new Contribuyentes();
        Usuarios usctbinm = new Usuarios();
        Set<Contribuyentes> lctr=null;
        historico_inmueble hstinm = new historico_inmueble();
        
        //Cargando datos para agregar un nuevo inmueble
        zninm = (Zonas) dao.findByWhereStatementoneobj(Zonas.class, "zona = '"+ cmbzona.getSelectedItem().toString() +"'");
        stinm = (Estados) dao.findByWhereStatementoneobj(Estados.class, "estado = 'Activo'");
        usctbinm = (Usuarios) dao.findByWhereStatementoneobj(Usuarios.class, " (nombres || ' ' || apellidos) = '"+ cmbcontribuyente.getSelectedItem().toString() +"'");
        lctr =  usctbinm.getContribuyenteses();
        
        Iterator it = lctr.iterator();
        while (it.hasNext()) {            
            ctbinm=(Contribuyentes) it.next();
        }

        ninmueble.setContribuyentes(ctbinm);
        ninmueble.setDireccion(txtdirinm.getText().toUpperCase());
        ninmueble.setEstadosinm(stinm);
        ninmueble.setFecharegistro(jdfechareg.getDate());
        ninmueble.setFolio1(txtfolio1.getText());
        ninmueble.setFolio2(txtfolio2.getText());
        ninmueble.setTomo1(txttomo1.getText());
        ninmueble.setTomo2(txttomo2.getText());
        ninmueble.setTomoreal(txttomoreal.getText());
        ninmueble.setZonas(zninm);
        ninmueble.setTelefono(txtphone.getText());
        ninmueble.setMetros_cuadrados(Double.parseDouble(txtmcdrs.getText()));
        ninmueble.setMetros_lineales(Double.parseDouble(txtmtln.getText()));
        

        
        try {
            Inmuebles tmpinm= new Inmuebles();
            tmpinm = (Inmuebles) dao.findByWhereStatementoneobj(Inmuebles.class, "direccion = '"+ txtdirinm.getText().toUpperCase() +"'" );
            if (tmpinm == null) {
                dao.save(ninmueble);
                //Cargando datos para agregar en el historico
                hstinm.setContribuyenteshid(ctbinm);
                hstinm.setFecha_inicio(jdfechareg.getDate());
                hstinm.setInmuebleshid(ninmueble);                        
                dao.save(hstinm);
                cmbcontribuyente.removeAllItems();
                cmbzona.removeAllItems();
                fillComboBoxcontribuyente();
                fillComboBoxrol();
                limpiarpantalla();
                JOptionPane.showMessageDialog(this, "Guardado Completo");             
            }
            else {
                JOptionPane.showMessageDialog(this, "Ya Existe un inmueble con esa dirección por favor verifique");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Hubo un error al guardar los datos, por favor intente mas tarde");
            e.printStackTrace();            
        }
        
    }
    
    public void limpiarpantalla(){
        txtdirinm.setText("");
        txtfolio1.setText("");
        txtfolio2.setText("");
        txtmcdrs.setText("");
        txtmtln.setText("");
        txtphone.setText("");
        txttomo1.setText("");
        txttomo2.setText("");
        txttomoreal.setText("");
        jdfechareg.setDate(null);
        
    }    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jpcon = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cmbcontribuyente = new javax.swing.JComboBox();
        cmbzona = new javax.swing.JComboBox();
        jPanel1 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtmtln = new javax.swing.JTextField();
        txtmcdrs = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jdfechareg = new com.toedter.calendar.JDateChooser();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        txttomo1 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txttomo2 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtfolio1 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtfolio2 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txttomoreal = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtphone = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtdirinm = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        btngrdinm = new javax.swing.JButton();
        btnslinm = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);

        jLabel1.setText("Contribuyente:");

        jLabel2.setText("Zona:");

        javax.swing.GroupLayout jpconLayout = new javax.swing.GroupLayout(jpcon);
        jpcon.setLayout(jpconLayout);
        jpconLayout.setHorizontalGroup(
            jpconLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpconLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cmbcontribuyente, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cmbzona, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jpconLayout.setVerticalGroup(
            jpconLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpconLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpconLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(cmbcontribuyente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbzona, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel12.setText("Metros Lineales :");

        jLabel13.setText("Metros Cuadrados:");

        jLabel10.setText("Fecha de Registro:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addGap(20, 20, 20)
                        .addComponent(txtmtln, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel13))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jdfechareg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtmcdrs)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel12)
                        .addComponent(txtmtln, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel13)
                        .addComponent(txtmcdrs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel10)
                    .addComponent(jdfechareg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel5.setText("Tomo 1:");

        jLabel7.setText("Tomo 2:");

        jLabel6.setText("Folio 1:");

        jLabel8.setText("Folio 2:");

        jLabel9.setText("Tomo Real :");

        jLabel4.setText("Telefono :");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txttomoreal))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addGap(28, 28, 28)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtfolio1)
                            .addComponent(txttomo1))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtfolio2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                    .addComponent(txttomo2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtphone))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txttomo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(txttomo2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtfolio1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(txtfolio2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txttomoreal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(txtphone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jLabel3.setText("Dirección:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(txtdirinm)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtdirinm, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btngrdinm.setText("Guardar");
        btngrdinm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btngrdinmActionPerformed(evt);
            }
        });

        btnslinm.setText("Salir");
        btnslinm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnslinmActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(113, 113, 113)
                .addComponent(btngrdinm)
                .addGap(76, 76, 76)
                .addComponent(btnslinm, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btngrdinm)
                    .addComponent(btnslinm))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jpcon, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpcon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnslinmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnslinmActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnslinmActionPerformed

    private void btngrdinmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btngrdinmActionPerformed
        // TODO add your handling code here:
        guardarinmueble();
    }//GEN-LAST:event_btngrdinmActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btngrdinm;
    private javax.swing.JButton btnslinm;
    private javax.swing.JComboBox cmbcontribuyente;
    private javax.swing.JComboBox cmbzona;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
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
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private com.toedter.calendar.JDateChooser jdfechareg;
    private javax.swing.JPanel jpcon;
    private javax.swing.JTextField txtdirinm;
    private javax.swing.JTextField txtfolio1;
    private javax.swing.JTextField txtfolio2;
    private javax.swing.JTextField txtmcdrs;
    private javax.swing.JTextField txtmtln;
    private javax.swing.JTextField txtphone;
    private javax.swing.JTextField txttomo1;
    private javax.swing.JTextField txttomo2;
    private javax.swing.JTextField txttomoreal;
    // End of variables declaration//GEN-END:variables
}
