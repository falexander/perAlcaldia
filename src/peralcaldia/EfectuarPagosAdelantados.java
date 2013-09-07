/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package peralcaldia;

import controller.AbstractDAO;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.swing.JOptionPane;
import peralcaldia.model.Boleta;
import peralcaldia.model.Estados;
import peralcaldia.model.Inmuebles;
import peralcaldia.model.Negocios;
import peralcaldia.model.Pagos;
import peralcaldia.model.Pagosadelantados;
import peralcaldia.model.Usuarios;

/**
 *
 * @author alex
 */
public class EfectuarPagosAdelantados extends javax.swing.JInternalFrame {

    //declaración e inicialización de variables para carga de datos del pago.
    Boleta tck = new Boleta();
    Usuarios user = new Usuarios();
    Inmuebles inm = new Inmuebles();
    Negocios neg = new Negocios();
    Set<Pagos> lpagos = new HashSet<Pagos>(0);    
    AbstractDAO dao = new AbstractDAO();
    String comment;
    int tid;
    /**
     * Creates new form EfectuarPagosAdelantados
     */
    public EfectuarPagosAdelantados() {
        initComponents();
    }

    public void buscar(int id){        
        try {
            txtconcepto.setText("");
            tck =  (Boleta) dao.findByWhereStatementoneobj(Boleta.class, "id =" + id);
            if (tck != null) {
                
                if (tck.getInmuebles() != null) {
                    user = tck.getInmuebles().getContribuyentes().getUsuarios();
                    inm = tck.getInmuebles();
                    lpagos = tck.getPagoes();

                    if (tck.getEstados().getId()== 5) {
                       btnapply.setEnabled(false);
                    }
                    else{
                        btnapply.setEnabled(true);
                    }

                    lbnmcontribuyente.setText(user.getNombres() + " " + user.getApellidos());
                    lbdirinmueble.setText(inm.getDireccion());
                    lbnegocio.setText(inm.getDireccion());
                    lbnmeses.setText(Integer.toString(tck.getMesesapagar()));
                    lbtotal.setText(tck.getMontototal().toString());
                    lbestado.setText(tck.getEstados().getEstado());
                    txtnorecibo.setText(tck.getRecibo());
                }
                else if(tck.getNegocios() != null){
                    user = tck.getNegocios().getContribuyentes().getUsuarios();
                    neg = tck.getNegocios();
                    lpagos = tck.getPagoes();

                    if (tck.getEstados().getId()== 5) {
                       btnapply.setEnabled(false);
                    }
                    else{
                        btnapply.setEnabled(true);
                    }
                    lbnmcontribuyente.setText(user.getNombres() + " " + user.getApellidos());
                    lbdirinmueble.setText(neg.getDireccion());
                    lbnegocio.setText(neg.getNombreempresa());
                    lbnmeses.setText(Integer.toString(tck.getMesesapagar()));
                    lbtotal.setText(tck.getMontototal().toString());
                    lbestado.setText(tck.getEstados().getEstado());
                    txtnorecibo.setText(tck.getRecibo());                                
                }
                
            }
            else{
                JOptionPane.showMessageDialog(this, "El numero de ticket todavia no ha sido emitido");
            }            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error en el proceso de carga");
        }

    }
    
    public void aplicarpago(){
        String recibo;
        Calendar fact;
        Date fecha;
        Boleta tmptck = new Boleta();
        Pagosadelantados adelantado = new Pagosadelantados();
        Pagos pago = new Pagos();
        Estados est = new Estados();
        Estados nst = new Estados();
        //Estado de la boleta
        est = (Estados) dao.findByWhereStatementoneobj(Estados.class, "id = 5");
        //Estado del pago adelantado
        nst = (Estados) dao.findByWhereStatementoneobj(Estados.class, "id = 1");
        
        comment= txtconcepto.getText();
        if (!txtnorecibo.getText().isEmpty()) {        
            tmptck = (Boleta) dao.findByWhereStatementoneobj(Boleta.class, "recibo = '" + txtnorecibo.getText() + "'");
            if (tmptck == null) {                
                recibo = txtnorecibo.getText();
                fact = Calendar.getInstance();
                fecha= fact.getTime();
                Iterator it = tck.getPagoes().iterator();
                while (it.hasNext()) {                    
                    pago = (Pagos) it.next();
                    
                    //ingresando el pago adelantado
                    adelantado.setBoleta(tck);
                    adelantado.setEstados(nst);
                    adelantado.setFechapago(fecha);
                    if (tck.getNegocios() != null) {
                        adelantado.setNegocios(neg);
                    }
                    else {
                        adelantado.setInmuebles(inm);
                    }
                    adelantado.setMontodelpago(tck.getMontototal().setScale(2,RoundingMode.HALF_EVEN));
                    adelantado.setNorecibo(recibo);
                    adelantado.setSaldo(tck.getMontototal().setScale(2, RoundingMode.HALF_EVEN));
                    try {
                        dao.save(adelantado);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(this, "Error al ingresar el pago adelantado");
                        e.printStackTrace();
                    }
                    
                    //actualizando el pago
                    pago.setMontopagado(tck.getMontototal().setScale(2,RoundingMode.HALF_EVEN));
                    pago.setEstadosid(est);
                    pago.setMespagado("Varios Adelantado");
                    pago.setFechapago(fecha);
                    if (!comment.isEmpty()) {
                        pago.setComentario(comment);
                    }
                    pago.setNorecibo(recibo);
                    try {
                        dao.update(pago);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(this, "Hubo un error durante la actualización del pago, favor revisar");
                        e.printStackTrace();
                    }
                }
                tck.setEstados(est);
                tck.setRecibo(recibo);
                tck.setFechacancelacion(fecha);
                try {
                    dao.update(tck);
                    buscar(tid);
                    limpiar();
                    JOptionPane.showMessageDialog(this, "El Pago ha sido Aplicado con exito");                                     
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Hubo un error en la actualizacion de la boleta");
                    e.printStackTrace();
                }

            }
            else{
                JOptionPane.showMessageDialog(this, "El número de recibo ya ha sido asignado, por favor verifique");
            }
            
        }
        else{
            JOptionPane.showMessageDialog(this, "Ingrese el numero de recibo para el ticket");
        }                
    }
    
    public void limpiar(){
        txtboleta.setText("");
        txtnorecibo.setText(tck.getRecibo());
        if (!comment.isEmpty()) {
            txtconcepto.setText(comment);            
        }
        lbboletan.setText("Ticket Numero: " + Integer.toString(tck.getId()));
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
        jLabel2 = new javax.swing.JLabel();
        txtboleta = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lbnmcontribuyente = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        lbdirinmueble = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        lbnmeses = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        lbtotal = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        lbestado = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtnorecibo = new javax.swing.JTextField();
        btnbuscar = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        txtconcepto = new javax.swing.JTextField();
        lbboletan = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        lbnegocio = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        btnapply = new javax.swing.JButton();
        btnout = new javax.swing.JButton();

        jLabel2.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        jLabel2.setText("No. de Ticket:");

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel1.setText("Datos del Pago:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        jLabel3.setText("Contribuyente:");

        jLabel5.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        jLabel5.setText("Inmueble:");

        jLabel6.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        jLabel6.setText("No. Meses:");

        jLabel7.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        jLabel7.setText("Total a Pagar:");

        jLabel8.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        jLabel8.setText("Estado:");

        jLabel9.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        jLabel9.setText("No de Recibo:");

        btnbuscar.setText("Buscar");
        btnbuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnbuscarActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        jLabel10.setText("Concepto:");

        jLabel11.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        jLabel11.setText("Empresa:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbnmcontribuyente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbdirinmueble, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtboleta, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel1))
                        .addGap(21, 21, 21)
                        .addComponent(btnbuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lbboletan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txtnorecibo, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(lbnegocio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtconcepto)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel6))
                        .addGap(26, 26, 26)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(lbnmeses, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(75, 75, 75)
                                .addComponent(jLabel7)
                                .addGap(18, 18, 18)
                                .addComponent(lbtotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(lbestado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtboleta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnbuscar)
                    .addComponent(lbboletan, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbnmcontribuyente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lbdirinmueble, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbnegocio, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbnmeses, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbtotal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbestado, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtnorecibo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtconcepto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        btnapply.setText("Aplicar");
        btnapply.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnapplyActionPerformed(evt);
            }
        });

        btnout.setText("Salir");
        btnout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnoutActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(103, 103, 103)
                .addComponent(btnapply, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 117, Short.MAX_VALUE)
                .addComponent(btnout, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(107, 107, 107))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnapply)
                    .addComponent(btnout))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnbuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbuscarActionPerformed
        // TODO add your handling code here:
        tid=Integer.parseInt(txtboleta.getText());
        buscar(tid);
    }//GEN-LAST:event_btnbuscarActionPerformed

    private void btnapplyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnapplyActionPerformed
        // TODO add your handling code here:
        aplicarpago();
    }//GEN-LAST:event_btnapplyActionPerformed

    private void btnoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnoutActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnoutActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnapply;
    private javax.swing.JButton btnbuscar;
    private javax.swing.JButton btnout;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lbboletan;
    private javax.swing.JLabel lbdirinmueble;
    private javax.swing.JLabel lbestado;
    private javax.swing.JLabel lbnegocio;
    private javax.swing.JLabel lbnmcontribuyente;
    private javax.swing.JLabel lbnmeses;
    private javax.swing.JLabel lbtotal;
    private javax.swing.JTextField txtboleta;
    private javax.swing.JTextField txtconcepto;
    private javax.swing.JTextField txtnorecibo;
    // End of variables declaration//GEN-END:variables
}
