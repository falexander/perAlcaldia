/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package peralcaldia;

import controller.AbstractDAO;
import java.math.BigDecimal;
import java.math.BigInteger;
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
        lusu = dao.findByWhereStatement(Usuarios.class, "roles_id=3");
        
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
        if (txtmcdrs.getText().isEmpty()) {
            ninmueble.setMetros_cuadrados(new BigDecimal(BigInteger.ONE));  
        }
        else{
            ninmueble.setMetros_cuadrados(new BigDecimal(txtmcdrs.getText()));            
        }
        
        if(txtmtln.getText().isEmpty()){
            ninmueble.setMetros_lineales(new BigDecimal(BigInteger.ONE));
        }
        else{
            ninmueble.setMetros_lineales(new BigDecimal(txtmtln.getText()));            
        }
        
        if (txtadoquinado.getText().isEmpty()) {
            ninmueble.setAdoquinado(new BigDecimal(BigInteger.ONE));
        }
        else{
            ninmueble.setAdoquinado(new BigDecimal(txtadoquinado.getText()));   
        }
        ninmueble.setSector(txtsector.getText().toUpperCase());
        ninmueble.setParcela(txtparcela.getText().toUpperCase());

        //Generando Correlativo Zona
        try {
            int corr;
            String crzona;
            corr=zninm.getCorrelativozona();
            crzona = zninm.getZonecode()+"-"+Integer.toString(corr);
            ninmueble.setCorrelativoinmueble(crzona);            
            zninm.setCorrelativozona((corr + 1));
            dao.update(zninm);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error en la generacion del correlativo");
            e.printStackTrace();
        }
        
        try {
            Inmuebles tmpinm= new Inmuebles();
            tmpinm = (Inmuebles) dao.findByWhereStatementoneobj(Inmuebles.class, "direccion = '"+ txtdirinm.getText().toUpperCase() +"'" );
            if (tmpinm == null) {
                tmpinm = (Inmuebles) dao.findByWhereStatementoneobj(Inmuebles.class, "cuentacorriente = '"+ txtctacorriente.getText().toUpperCase() +"'" );
                if (tmpinm == null) {
                    ninmueble.setCuentacorriente(txtctacorriente.getText().toUpperCase());
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
                else{
                    JOptionPane.showMessageDialog(this, "El número de cuenta ya ha sido asignado");
                }

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
        txtmcdrs.setText("1");
        txtmtln.setText("1");
        txtadoquinado.setText("1");
        txtphone.setText("");
        txttomo1.setText("");
        txttomo2.setText("");
        txttomoreal.setText("");
        jdfechareg.setDate(null);
        txtctacorriente.setText("");
        txtsector.setText("");
        txtparcela.setText("");
        
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
        jLabel16 = new javax.swing.JLabel();
        txtadoquinado = new javax.swing.JTextField();
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
        jPanel5 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        txtctacorriente = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtsector = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtparcela = new javax.swing.JTextField();

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
                .addGap(18, 18, 18)
                .addComponent(cmbzona, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

        txtmtln.setText("1");

        txtmcdrs.setText("1");

        jLabel10.setText("Fecha de Registro:");

        jLabel16.setText("Adoquinado:");

        txtadoquinado.setText("1");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(txtmtln, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel13))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(txtadoquinado)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel10)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtmcdrs)
                    .addComponent(jdfechareg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel16)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel12)
                                .addComponent(txtmtln, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel13)
                                .addComponent(txtmcdrs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jdfechareg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel10)
                                .addComponent(txtadoquinado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
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

        jLabel11.setText("Cta. Corriente:");

        jLabel14.setText("Sector:");

        jLabel15.setText("Parcela:");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtctacorriente, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtsector, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtparcela, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txtctacorriente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(txtsector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15)
                    .addComponent(txtparcela, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpcon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jpcon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
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
    private javax.swing.JPanel jPanel5;
    private com.toedter.calendar.JDateChooser jdfechareg;
    private javax.swing.JPanel jpcon;
    private javax.swing.JTextField txtadoquinado;
    private javax.swing.JTextField txtctacorriente;
    private javax.swing.JTextField txtdirinm;
    private javax.swing.JTextField txtfolio1;
    private javax.swing.JTextField txtfolio2;
    private javax.swing.JTextField txtmcdrs;
    private javax.swing.JTextField txtmtln;
    private javax.swing.JTextField txtparcela;
    private javax.swing.JTextField txtphone;
    private javax.swing.JTextField txtsector;
    private javax.swing.JTextField txttomo1;
    private javax.swing.JTextField txttomo2;
    private javax.swing.JTextField txttomoreal;
    // End of variables declaration//GEN-END:variables
}
