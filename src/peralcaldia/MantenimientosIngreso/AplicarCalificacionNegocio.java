/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package peralcaldia.MantenimientosIngreso;

import controller.NegDAO;
import controller.UserDAO;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import peralcaldia.model.Contribuyentes;
import peralcaldia.model.Estados;
import peralcaldia.model.Montosimpuestosnegocios;
import peralcaldia.model.Negocios;
import peralcaldia.model.Usuarios;
import util.GCMPNativos;
import util.codemd5;

/**
 *
 * @author alex
 */
/*Pantalla Destinada a la aplicacion de la calificacion y recalificacion anual
 * de los negocios.*/
public class AplicarCalificacionNegocio extends javax.swing.JInternalFrame {
    
    UserDAO dao = new UserDAO();
    NegDAO negociodao = new NegDAO();
    DefaultComboBoxModel modelocontribuyente = new DefaultComboBoxModel();
    DefaultComboBoxModel modelonegocios = new DefaultComboBoxModel();
    GCMPNativos componentes = new GCMPNativos();
    Usuarios user = new Usuarios();
    Contribuyentes contribuyente;
    Negocios negocio = new Negocios();
    Montosimpuestosnegocios MontoImpNegocio = new Montosimpuestosnegocios();
    Montosimpuestosnegocios MontoImpNegAnt = new Montosimpuestosnegocios();
    /**
     * Creates new form AplicarCalificacionNegocio
     */
    public AplicarCalificacionNegocio() {
        initComponents();
        modelocontribuyente.addElement("-");
        modelonegocios.addElement("-");
        cmbcontribuyente.setModel(modelocontribuyente);
        cmbnegocios.setModel(modelonegocios);
        txtdui.setEnabled(false);
        txtnit.setEnabled(false);
        txtnombres.setEnabled(false);
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension ventana = this.getSize();
        this.setLocation((pantalla.width - ventana.width) / 2, (pantalla.height - ventana.height) / 4);        
    }

    /*Metodo de Busqueda de Contribuyentes por Nombres*/
    public void BuscarContribuyenteNombres() {
        List lusuarios = null;
        try {
            /*Buscando Usuarios que coincidan con el nombre o porcion indicada en el Textbox*/
            if (!txtnombres.getText().isEmpty()) {
                lusuarios = dao.find_usuarios_whereStatement(" a.nombres || ' ' || a.apellidos || ' - DUI:' || b.dui like '%" + txtnombres.getText().toUpperCase() + "%' and a.roles = 3");
                if (lusuarios != null && lusuarios.size() > 0) {
                    componentes.llenarmodelo(modelocontribuyente, lusuarios);
                    contribuyente = null;
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error en la busqueda de usuarios");
            System.out.println(e.toString());
        }
    }
    
    /*Metodo de Busqueda de Contribuyentes por DUI*/
    public void BuscarContribuyentesDui() {
        List lusuarios = null;
        try {
            if (!txtdui.getText().isEmpty() && !txtdui.getText().equals("-")) {
                contribuyente = (Contribuyentes) dao.findByWhereStatementoneobj(Contribuyentes.class, "dui = '" + this.txtdui.getText() + "'");
                if (contribuyente != null) {
                    lusuarios = new ArrayList();
                    lusuarios.add(contribuyente.getUsuarios().getNombres() + " " + contribuyente.getUsuarios().getApellidos());
                    componentes.llenarmodelo(modelocontribuyente, lusuarios);
                } else {
                    JOptionPane.showMessageDialog(this, "No Existen Contribuyentes registrados con el DUI ingresado");

                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error en la busqueda de usuarios");
            System.out.println(e.toString());
        }

    }

    /*Metodo de busqueda de contribuyentes por NIT*/
    public void BuscarContribuyenteNit() {
        List lusuarios = null;
        try {
            if (!txtnit.getText().isEmpty() && !txtnit.getText().equals("----")) {
                contribuyente = (Contribuyentes) dao.findByWhereStatementoneobj(Contribuyentes.class, "nit = '" + this.txtnit.getText() + "'");
                if (contribuyente != null) {
                    lusuarios = new ArrayList();
                    lusuarios.add(contribuyente.getUsuarios().getNombres() + " " + contribuyente.getUsuarios().getApellidos());
                    componentes.llenarmodelo(modelocontribuyente, lusuarios);
                } else {
                    JOptionPane.showMessageDialog(this, "No Existen Contribuyentes registrados con el NIT ingresado");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error en la busqueda de usuarios");
            System.out.println(e.toString());
        }
    }
    
    /*Metodo para realizar la carga de Negocios de acuerdo a un contribuyente en especifico, es decir
     * se Cargan los Negocios pertenecientes al Contribuyente Seleccionado*/
    public void CargarNegocios(){
        List LNegocios = null;
        try {
            if (contribuyente == null) {
                contribuyente = (Contribuyentes) dao.findByWhereStatementOneJoinObj(Contribuyentes.class, " as b inner join b.usuarios as a where (a.nombres || ' ' || a.apellidos || ' - DUI:' || b.dui) = '" + cmbcontribuyente.getSelectedItem().toString() + "'");
//                user = contribuyente.getUsuarios();
            }
            LNegocios = negociodao.findnameswhereStatement(" contribuyentes_id = " + contribuyente.getId() + " and estados_id = 1 ");
            if (LNegocios.size() != 0 || LNegocios != null) {
            componentes.llenarmodelo(modelonegocios, LNegocios);
            cmbnegocios.setModel(modelonegocios);
        }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error en la carga de Negocios");
            System.out.println(e.toString());
        }
        
    }
    
    /*Metodo para Limpiar la información cargada en pantalla*/
    public void LimpiarComponente(){
        try {
            componentes.limpiarmodelo(modelonegocios);
            modelonegocios.addElement("-");
            componentes.limpiarmodelo(modelocontribuyente);
            modelocontribuyente.addElement("-");
        } catch (Exception e) {
            System.out.println(e.toString());
        }        
        txtmonto.setText("");
        jdfechaapp.setDate(new Date());
        
    }
    
    /*Metodo para Limpiar algunos componentes que aparecen en pantalla*/
    public void LimpiarComponenteDos(){
        try {
            
            modelocontribuyente.removeAllElements();
            modelocontribuyente.addElement("-");
            componentes.limpiarmodelo(modelonegocios);
            modelonegocios.addElement("-");
        } catch (Exception e) {
            System.out.println(e.toString());
        }        
        txtmonto.setText("");
        jdfechaapp.setDate(new Date());
        
    }    
    
    /*Metodo para realizar la carga del monto actual (en uso) con el cual se ha calificado al Negocio*/
    public void CargarMontoImpuestoActual(){
        negocio = (Negocios) dao.findByWhereStatementoneobj(Negocios.class, " nombreempresa = '" + cmbnegocios.getSelectedItem().toString() + "'");
        MontoImpNegAnt = (Montosimpuestosnegocios) dao.findByWhereStatementmaximoobjeto(Montosimpuestosnegocios.class, " negocios_id = " + negocio.getId() + "and uso <> 'OBSOLETO'");
        if (MontoImpNegAnt != null) {
            lbmontocalificacion.setText(MontoImpNegAnt.getMonto().setScale(2,RoundingMode.HALF_EVEN).toString());
            lbfechaapp.setText(MontoImpNegAnt.getFechainicio());
        }else{
            lbmontocalificacion.setText("");
            lbfechaapp.setText("");
        }
    }
    
    /*Metodo utilizado para la aplicacion de la nueva calificacion, seteando fecha fin al monto anterior y registrando la nueva calificacion*/
    public void AplicarCalificacion(){
        Estados Activo, Inactivo;
        List<Montosimpuestosnegocios> LTmpMntImpNeg = null;
        if (MontoImpNegAnt == null) {
            if (!txtmonto.getText().isEmpty()) {
                /*Inicializando la Instancias y Propiedades de la Calificacion en el caso de que sea la calificacion inicial*/
                MontoImpNegocio = new Montosimpuestosnegocios();
                Activo = (Estados) dao.findByWhereStatementoneobj(Estados.class, " id = 1");
                MontoImpNegocio.setEstados(Activo);
                MontoImpNegocio.setFechainicio(codemd5.getmesanio(jdfechaapp.getDate()));
                MontoImpNegocio.setMonto(new BigDecimal(txtmonto.getText()));
                MontoImpNegocio.setNegocios(negocio);
                MontoImpNegocio.setUso("NO OBSOLETO");
                try {
                    dao.save(MontoImpNegocio);
                    JOptionPane.showMessageDialog(this, "Datos Guardados con Exito");
                    txtmonto.setText("");
                    jdfechaapp.setDate(new Date());
                    CargarMontoImpuestoActual();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Error en el Guardado de los Datos");
                    System.out.println(e.toString());
                }
            }
        }else{
            if (!txtmonto.getText().isEmpty()) {
                Activo = (Estados) dao.findByWhereStatementoneobj(Estados.class, " id = 1");
                Inactivo = (Estados) dao.findByWhereStatementoneobj(Estados.class, " id = 4");
                /*Verificando que no exista calificacion para el periodo ingresado*/
                LTmpMntImpNeg = dao.findByWhereStatement(Montosimpuestosnegocios.class, " fecha_inicio = '" + codemd5.getmesanio(jdfechaapp.getDate()) + "' and negocios_id = " + negocio.getId() + " and uso = 'NO OBSOLETO'");
                if (LTmpMntImpNeg.size() <= 0 || LTmpMntImpNeg == null) {
                    /*Inicializando el nuevo Monto*/
                    MontoImpNegocio = new Montosimpuestosnegocios();
                    /*Desactivando el monto anterior y llenando el nuevo monto*/
                    MontoImpNegAnt.setEstados(Inactivo);
                    MontoImpNegAnt.setFechafin(codemd5.GetMesAnteriorAnio(jdfechaapp.getDate()));
                    /*Creando el Nuevo Monto*/
                    MontoImpNegocio.setEstados(Activo);
                    MontoImpNegocio.setFechainicio(codemd5.getmesanio(jdfechaapp.getDate()));
                    MontoImpNegocio.setMonto(new BigDecimal(txtmonto.getText()));
                    MontoImpNegocio.setNegocios(negocio);
                    MontoImpNegocio.setUso("NO OBSOLETO");
                    /*Actualizando en el sistema la nueva calificación y desactivando la anterior */
                    try {
                        dao.update(MontoImpNegAnt);
                        dao.save(MontoImpNegocio);
                        JOptionPane.showMessageDialog(this, "Datos Guardados con Exito");
                        CargarMontoImpuestoActual();
                        txtmonto.setText("");
                        jdfechaapp.setDate(new Date());
                    } catch (Exception e) {
                        JOptionPane.showConfirmDialog(this, "Error en el Guardado de los Datos");
                        System.out.println(e.toString());
                    }
                    
                } else {
                    JOptionPane.showMessageDialog(this, "Ya Hay una Calificacion Registrada Para el Periodo Indicado." + "\n En caso de error ir a Modificar la Calificacion Para Este Periodo");
                }


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

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtnombres = new componentesheredados.LettersJTextField();
        jLabel3 = new javax.swing.JLabel();
        txtdui = new componentesheredados.DUITextField();
        jLabel4 = new javax.swing.JLabel();
        txtnit = new componentesheredados.NitJTextField();
        btnbuscar = new javax.swing.JButton();
        jbtnnombres = new javax.swing.JRadioButton();
        jbtndui = new javax.swing.JRadioButton();
        jbtnnit = new javax.swing.JRadioButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        cmbcontribuyente = new javax.swing.JComboBox();
        cmbnegocios = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        txtmonto = new componentesheredados.DecimalJTextField();
        jLabel8 = new javax.swing.JLabel();
        jdfechaapp = new com.toedter.calendar.JDateChooser();
        jPanel4 = new javax.swing.JPanel();
        btnaplicar = new javax.swing.JButton();
        btnsalir = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        lbmontocalificacion = new javax.swing.JLabel();
        lbfechaapp = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setText("Criterios de Busqueda");

        jLabel2.setText("Nombres:");

        jLabel3.setText("DUI:");

        try {
            txtdui.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("########-#")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabel4.setText("NIT:");

        try {
            txtnit.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("####-######-###-#")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        btnbuscar.setText("Buscar");
        btnbuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnbuscarActionPerformed(evt);
            }
        });

        buttonGroup1.add(jbtnnombres);
        jbtnnombres.setText("Nombres y Apellidos");
        jbtnnombres.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnnombresActionPerformed(evt);
            }
        });

        buttonGroup1.add(jbtndui);
        jbtndui.setText("DUI");
        jbtndui.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnduiActionPerformed(evt);
            }
        });

        buttonGroup1.add(jbtnnit);
        jbtnnit.setText("NIT");
        jbtnnit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnnitActionPerformed(evt);
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
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(txtnombres, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(42, 42, 42)
                        .addComponent(txtdui, javax.swing.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(txtnit, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jbtnnombres)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jbtndui)
                                .addGap(42, 42, 42)
                                .addComponent(jbtnnit)
                                .addGap(42, 42, 42)
                                .addComponent(btnbuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnbuscar)
                    .addComponent(jbtnnombres)
                    .addComponent(jbtndui)
                    .addComponent(jbtnnit))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtnombres, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtdui, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(txtnit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jLabel5.setText("Contribuyente:");

        jLabel6.setText("Negocios:");

        cmbcontribuyente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbcontribuyenteActionPerformed(evt);
            }
        });

        cmbnegocios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbnegociosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbcontribuyente, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmbnegocios, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(cmbcontribuyente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(cmbnegocios, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel7.setText("Monto:");

        jLabel8.setText("Fecha de Aplicación:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addGap(18, 18, 18)
                .addComponent(txtmonto, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel8)
                .addGap(18, 18, 18)
                .addComponent(jdfechaapp, javax.swing.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel7)
                        .addComponent(txtmonto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel8))
                    .addComponent(jdfechaapp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnaplicar.setText("Aplicar");
        btnaplicar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnaplicarActionPerformed(evt);
            }
        });

        btnsalir.setText("Salir");
        btnsalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsalirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addComponent(btnaplicar, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnsalir, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(57, 57, 57))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnaplicar)
                    .addComponent(btnsalir))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel9.setText("Datos de la Calificación Actual del Negocio");

        jLabel10.setText("Calificación Actual:");

        jLabel11.setText("Fecha Inicio de Aplicación:");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addGap(18, 18, 18)
                        .addComponent(lbfechaapp, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(18, 18, 18)
                        .addComponent(lbmontocalificacion, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addGap(10, 10, 10)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(lbmontocalificacion, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(lbfechaapp, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /*Verificando seleccion para Criterios de Busqueda*/
    private void jbtnnombresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnnombresActionPerformed
        // TODO add your handling code here:
        txtnombres.setEnabled(true);
        txtdui.setEnabled(false);
        txtnit.setEnabled(false);
        txtdui.setText("");
        txtnit.setText("");
        LimpiarComponenteDos();        
    }//GEN-LAST:event_jbtnnombresActionPerformed

    /*Verificando seleccion para Criterios de Busqueda*/
    private void jbtnduiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnduiActionPerformed
        // TODO add your handling code here:
        txtdui.setEnabled(true);
        txtnit.setEnabled(false);
        txtnombres.setEnabled(false);
        txtnit.setText("");
        txtnombres.setText("");
        LimpiarComponenteDos();        
    }//GEN-LAST:event_jbtnduiActionPerformed

    /*Verificando seleccion para Criterios de Busqueda*/
    private void jbtnnitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnnitActionPerformed
        // TODO add your handling code here:
        txtnit.setEnabled(true);
        txtnombres.setEnabled(false);
        txtdui.setEnabled(false);
        txtnombres.setText("");
        txtdui.setText("");
        LimpiarComponenteDos();        
    }//GEN-LAST:event_jbtnnitActionPerformed

    /*Verificando el metodo de busqueda seleccionado en pantalla*/
    private void btnbuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbuscarActionPerformed
        // TODO add your handling code here:
        LimpiarComponente();
        if (jbtnnombres.isSelected()) {
            BuscarContribuyenteNombres();
        }else if(jbtndui.isSelected()){
            BuscarContribuyentesDui();
        }else if(jbtnnit.isSelected()){
            BuscarContribuyenteNit();
        }else{
            JOptionPane.showMessageDialog(this, "Elija un criterio de Busqueda e Ingrese el valor a buscar");
        }
    }//GEN-LAST:event_btnbuscarActionPerformed

    private void btnsalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsalirActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnsalirActionPerformed

    private void btnaplicarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaplicarActionPerformed
        // TODO add your handling code here:
        AplicarCalificacion();
    }//GEN-LAST:event_btnaplicarActionPerformed

    private void cmbcontribuyenteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbcontribuyenteActionPerformed
        // TODO add your handling code here:
        if (cmbcontribuyente.getSelectedItem() != null) {
            if (!cmbcontribuyente.getSelectedItem().toString().equals("-")) {
                if (jbtnnombres.isSelected()) {
                    contribuyente = (Contribuyentes) dao.findByWhereStatementOneJoinObj(Contribuyentes.class, " as b inner join b.usuarios as a where (a.nombres || ' ' || a.apellidos || ' - DUI:' || b.dui) = '" + cmbcontribuyente.getSelectedItem().toString() + "'");
                }
                CargarNegocios();
                txtmonto.setText("");
                jdfechaapp.setDate(new Date());
            }else{
                LimpiarComponente();
            }
        }
    }//GEN-LAST:event_cmbcontribuyenteActionPerformed

    private void cmbnegociosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbnegociosActionPerformed
        // TODO add your handling code here:
        if (cmbnegocios.getSelectedItem() != null) {
            if (!cmbnegocios.getSelectedItem().toString().equals("-")) {
                CargarMontoImpuestoActual();
            }else{
                lbfechaapp.setText("");
                lbmontocalificacion.setText("");
            }   
        }
    }//GEN-LAST:event_cmbnegociosActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnaplicar;
    private javax.swing.JButton btnbuscar;
    private javax.swing.JButton btnsalir;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox cmbcontribuyente;
    private javax.swing.JComboBox cmbnegocios;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
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
    private javax.swing.JRadioButton jbtndui;
    private javax.swing.JRadioButton jbtnnit;
    private javax.swing.JRadioButton jbtnnombres;
    private com.toedter.calendar.JDateChooser jdfechaapp;
    private javax.swing.JLabel lbfechaapp;
    private javax.swing.JLabel lbmontocalificacion;
    private componentesheredados.DUITextField txtdui;
    private componentesheredados.DecimalJTextField txtmonto;
    private componentesheredados.NitJTextField txtnit;
    private componentesheredados.LettersJTextField txtnombres;
    // End of variables declaration//GEN-END:variables
}
