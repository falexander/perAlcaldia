/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package peralcaldia.Transacciones;

import controller.AbstractDAO;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.math.BigDecimal;
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
/*Pantalla utilizada para la aplicacion de pagos de negocios generados por el departamento
 * de cuentas corrientes.*/
public class pagosnegocios extends javax.swing.JInternalFrame {

    //declaración e inicialización de variables para carga de datos del pago.
    Boleta tck = new Boleta();
    Usuarios user = new Usuarios();
    Negocios neg = new Negocios();
    Set<Pagos> lpagos = new HashSet<Pagos>(0);    
    AbstractDAO dao = new AbstractDAO();
    String comment;
    int tid;    
    /**
     * Creates new form pagosnegocios
     */    
    public pagosnegocios() {
        initComponents();
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension ventana = this.getSize();
        this.setLocation((pantalla.width - ventana.width) / 2, (pantalla.height - ventana.height) / 4);        
    }
    
    /*Busqueda de la boleta que se desea cancelar*/
    public void buscar(int id){
        try {
            txtconcepto.setText("");
            tck =  (Boleta) dao.findByWhereStatementoneobj(Boleta.class, "id =" + id);
            if (tck != null) {
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
                lbdireccion.setText(neg.getDireccion());
                lbnegocio.setText(neg.getNombreempresa());
                lbnmeses.setText(Integer.toString(tck.getMesesapagar()));
                lbtotal.setText(tck.getMontototal().toString());
                lbestado.setText(tck.getEstados().getEstado());
                txtnorecibo.setText(tck.getRecibo());                                
            }
            else{
                JOptionPane.showMessageDialog(this, "El numero de ticket todavia no ha sido emitido");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error en el proceso de carga");            
        }
    
    }
    
    /*Aplicacion del pago*/
    public void aplicarpago(){
        String recibo;
        Calendar fact;
        Date fecha;
        Boleta tmptck = new Boleta();
        Pagosadelantados adelantado = new Pagosadelantados();                    
        Set<Pagosadelantados> ladelantados = null;
        BigDecimal res, val;
        String screcibo;        
        Estados est= new Estados();
        est = (Estados) dao.findByWhereStatementoneobj(Estados.class, "id = 5");
        comment= txtconcepto.getText();
        if (!txtnorecibo.getText().isEmpty()) {
            tmptck = (Boleta) dao.findByWhereStatementoneobj(Boleta.class, "recibo = '" + txtnorecibo.getText() + "'");
            if (tmptck == null) {                
                recibo = txtnorecibo.getText();
                fact = Calendar.getInstance();
                fecha= fact.getTime();
                Iterator it = tck.getPagoes().iterator();
                while (it.hasNext()) {                    
                    Pagos tmppago = new Pagos();
                    //obteniendo el pago
                    tmppago = (Pagos) it.next();
                    //verificando que solo haya un ultimo pago adelantado, obteniendo saldo y actualizando a cero el pago adelantado
                    res = BigDecimal.ZERO;
                    screcibo = "";
                    try {
                        ladelantados = neg.getPadelantados();
                        if (!ladelantados.isEmpty()) {
                            Iterator tp = ladelantados.iterator();
                            while (tp.hasNext()) {                                
                                adelantado = (Pagosadelantados) tp.next();
                                if (adelantado.getSaldo() != BigDecimal.ZERO && adelantado.getEstados().getId() == 1) {
                                    Estados nst = new Estados();
                                    nst = (Estados) dao.findByWhereStatementoneobj(Estados.class, "id = 4");
                                    res = adelantado.getSaldo();
                                    val = adelantado.getSaldo().subtract(res);
                                    screcibo = adelantado.getNorecibo();
                                    adelantado.setSaldo(val);
                                    adelantado.setEstados(nst);
                                    try {
                                        dao.update(adelantado);
                                    } catch (Exception e) {
                                        JOptionPane.showMessageDialog(this, "Error al actualizar pagos adelantados");
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }                                                
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(this, "Error verificando pagos adelantados");
                        e.printStackTrace();                        
                    }
                    res = res.add(tck.getMontototal().divide(new BigDecimal(tck.getMesesapagar()),2,RoundingMode.HALF_EVEN));                    
                    tmppago.setMontopagado(res.setScale(2,RoundingMode.HALF_EVEN));
//                    tmppago.setBoletas(tck);
                    tmppago.setEstadosid(est);
                    tmppago.setFechapago(fecha);
                    if (!comment.isEmpty()) {
                        tmppago.setComentario(comment);
                    }
                    if (!screcibo.isEmpty()) {
                        tmppago.setNorecibo(recibo + "/" + screcibo);                        
                    }
                    else{                        
                        tmppago.setNorecibo(recibo);   
                    }
                    dao.update(tmppago);
                }
                tck.setEstados(est);
                tck.setRecibo(recibo);
                tck.setFechacancelacion(fecha);
                dao.update(tck);
                buscar(tid);
                limpiar();
            JOptionPane.showMessageDialog(this, "Los Pagos se han aplicado con exito");
            }
            else{
                JOptionPane.showMessageDialog(this, "El número de recibo ya ha sido asignado, por favor verifique");
            }
            
        }
        else{
            JOptionPane.showMessageDialog(this, "Ingrese el numero de recibo para el ticket");
        }        
    } 
    
    /*Limpiar la informacion cargada en pantalla*/
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
        lbnegocio = new javax.swing.JLabel();
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
        jLabel4 = new javax.swing.JLabel();
        lbdireccion = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        btnapply = new javax.swing.JButton();
        btnout = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setPreferredSize(new java.awt.Dimension(527, 385));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setText("No. de Ticket:");

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel1.setText("Datos del Pago:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel3.setText("Contribuyente:");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel5.setText("Empresa:");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel6.setText("No. Meses:");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel7.setText("Total a Pagar:");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel8.setText("Estado:");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel9.setText("No de Recibo:");

        btnbuscar.setText("Buscar");
        btnbuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnbuscarActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel10.setText("Concepto:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel4.setText("Dirección:");

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
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbnegocio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbnmcontribuyente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtboleta, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel1))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(71, 71, 71)
                                .addComponent(jLabel7)
                                .addGap(18, 18, 18)
                                .addComponent(lbtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 3, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(btnbuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(lbboletan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(21, 21, 21)
                        .addComponent(lbestado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txtnorecibo, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(txtconcepto)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel4))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(31, 31, 31)
                                .addComponent(lbnmeses, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(43, 43, 43)
                                .addComponent(lbdireccion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
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
                .addGap(13, 13, 13)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(lbdireccion, javax.swing.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lbnegocio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5))
                .addGap(11, 11, 11)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6)
                        .addComponent(jLabel7))
                    .addComponent(lbnmeses, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(lbestado, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtnorecibo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
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
                .addGap(109, 109, 109)
                .addComponent(btnapply, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnout, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(112, 112, 112))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnapply)
                    .addComponent(btnout))
                .addGap(0, 21, Short.MAX_VALUE))
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
    private javax.swing.JLabel lbboletan;
    private javax.swing.JLabel lbdireccion;
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
