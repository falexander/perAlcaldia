/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package peralcaldia.MantenimientosModificaciones;

import controller.AbstractDAO;
import controller.EstDAO;
import controller.GrDAO;
import controller.NegDAO;
import controller.UserDAO;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import peralcaldia.model.Contribuyentes;
import peralcaldia.model.Estados;
import peralcaldia.model.Giros;
import peralcaldia.model.Negocios;
import peralcaldia.model.Tiposcomercio;
import util.GCMPNativos;

/**
 *
 * @author alex
 */
/*Pantalla Utilizada para modificar las propiedades de un negocio determinado de los ingresados en el sistema*/
public class ModificarNegocio extends javax.swing.JInternalFrame {
    UserDAO dao = new UserDAO();
    Negocios negocio = new Negocios();
    Contribuyentes contribuyente = new Contribuyentes();
    Contribuyentes contribuyenteDos = new Contribuyentes();
    GCMPNativos componentes = new GCMPNativos();
    DefaultComboBoxModel modelocontribuyente = new DefaultComboBoxModel();
    DefaultComboBoxModel modelogiro = new DefaultComboBoxModel();
    DefaultComboBoxModel modeloactividad = new DefaultComboBoxModel();
    DefaultComboBoxModel modelonegocio = new DefaultComboBoxModel();
    DefaultComboBoxModel modeloestado = new DefaultComboBoxModel();
    DefaultComboBoxModel modelootrocontribuyente = new DefaultComboBoxModel();
    AbstractDAO gDao = new AbstractDAO();
    Giros giro = new Giros();
    /**
     * Creates new form ModificarNegocio
     */
    public ModificarNegocio() {
        initComponents();
        modelocontribuyente.addElement("-");
        modelogiro.addElement("-");
        modeloactividad.addElement("-");
        modelonegocio.addElement("-");
        modeloestado.addElement("-");
        modelootrocontribuyente.addElement("-");
        cmbcontribuyente.setModel(modelocontribuyente);
        cmbactividad.setModel(modeloactividad);
        cmbgiro.setModel(modelogiro);
        cmbnegocios.setModel(modelonegocio);
        cmbestado.setModel(modeloestado);
        cmbotrocontribuyente.setModel(modelootrocontribuyente);
        txtnombres1.setEnabled(false);
        txtdui1.setEnabled(false);
        txtnit1.setEnabled(false);
        txtotrodui.setEnabled(false);
        txtotronit.setEnabled(false);
        txtnombres2.setEnabled(false);
        jbtndui2.setEnabled(false);
        jbtnnit2.setEnabled(false);
        jbtnnombres2.setEnabled(false);
        btnbuscar2.setEnabled(false);
        cmbotrocontribuyente.setEnabled(false);
        jdfecharegistro.setDate(new Date());        
        jdfecharegistro.setDate(new Date());
        LlenarComboGiros();
        LlenarComboEstados();
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension ventana = this.getSize();
        this.setLocation((pantalla.width - ventana.width) / 2, (pantalla.height - ventana.height) / 4);        
    }

    /*Buscar Contribuyentes Propietario Original por Nombre*/
    public void BuscarContribuyenteNombres() {
        List lusuarios = null;
        try {
            /*Buscando Usuarios que coincidan con el nombre o porcion indicada en el Textbox*/
            if (!txtnombres1.getText().isEmpty()) {
                lusuarios = dao.find_usuarios_whereStatement(" a.nombres || ' ' || a.apellidos || ' - DUI:' || b.dui like '%" + txtnombres1.getText().toUpperCase() + "%' and a.roles = 3");
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

    /*Buscar Contribuyente Propietario Original por DUI*/
    public void BuscarContribuyentesDui() {
        List lusuarios = null;
        try {
            if (!txtdui1.getText().isEmpty() && !txtdui1.getText().equals("-")) {
                contribuyente = (Contribuyentes) dao.findByWhereStatementoneobj(Contribuyentes.class, "dui = '" + this.txtdui1.getText() + "'");
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

    /*Buscar Contribuyente Propietario Original por NIT*/
    public void BuscarContribuyenteNit() {
        List lusuarios = null;
        try {
            if (!txtnit1.getText().isEmpty() && !txtnit1.getText().equals("----")) {
                contribuyente = (Contribuyentes) dao.findByWhereStatementoneobj(Contribuyentes.class, "nit = '" + this.txtnit1.getText() + "'");
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

    /*Carga de Los Giros Ingresados y Activos en el sistema*/
    public void LlenarComboGiros() {
        List lista = null;
        GrDAO misGiros = new GrDAO();

        try {
            lista = misGiros.find_giros_whereStatement(" estados_id = 1");
            componentes.llenarmodelo(modelogiro, lista);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error en la Carga de Giros de las Empresas");
            System.out.println(e.toString());
        }
    }

    /*Carga de los tipos de Actividades comerciales ingresadas en el sistema de acuerdo a los giros registrados*/
    public void LlenarComboActividad() {
        List listaActividad = null;
        try {
            giro = (Giros) dao.findByWhereStatementoneobj(Giros.class, "descripcion = '" + cmbgiro.getSelectedItem().toString() + "' and estados_id = 1");
            listaActividad = dao.findByWhereStatement("select actividadeconomica from Tiposcomercio where giros_id = ' " + giro.getId() + "' and estados_id=1");
            componentes.llenarmodelo(modeloactividad, listaActividad);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error en la carga de los tipos relacionados al giro");
            e.printStackTrace();
        }
    }
    
    /*Carga de los Estados Validos para los Negocios*/
    public void LlenarComboEstados(){
        EstDAO daoestado = new EstDAO();
        List lista = null;
        lista= daoestado.find_estados_whereStatement(" id = 1 or id = 4");
        if (lista.size() != 0 && lista != null) {
            componentes.llenarmodelo(modeloestado, lista);
        }
    }    
    
    /*Metodo para Limpiar Pantalla*/
    public void LimpiarPantalla() {
        componentes.limpiarmodelo(modelocontribuyente);
        modelocontribuyente.addElement("-");
        componentes.limpiarmodelo(modeloactividad);
        componentes.limpiarmodelo2(modelonegocio);
        cmbestado.setSelectedIndex(0);
        modeloactividad.addElement("-");
        txtnempresa.setText("");
        txttelefono.setText("");
        txtdireccion.setText("");
        txtnis.setText("");
        txtctacorriente.setText("");
        cmbgiro.setSelectedIndex(0);
        jdfecharegistro.setDate(new Date());
    }    
    
    /*Reinico de los componentes de ubicacion del nuevo propietario en caso de modificacion*/
    public void ReiniciarComboOtroContribuyente(){
        if (cmbotrocontribuyente.getSelectedItem() != null) {
            componentes.limpiarmodelo2(modelootrocontribuyente);
            contribuyenteDos = null;
        }
        
    }
    
    /*Busqueda de Nuevo Contribuyente Propietario por Nombres*/
    public void BuscarContribuyenteNombresOtro() {
        List lusuarios = null;
        try {
            /*Buscando Usuarios que coincidan con el nombre o porcion indicada en el Textbox*/
            if (!txtnombres2.getText().isEmpty()) {
                lusuarios = dao.find_usuarios_whereStatement(" a.nombres || ' ' || a.apellidos || ' - DUI:' || b.dui like '%" + txtnombres2.getText().toUpperCase() + "%' and a.roles = 3");
                if (lusuarios != null && lusuarios.size() > 0) {
                    componentes.llenarmodelo(modelootrocontribuyente, lusuarios);
                    contribuyenteDos = null;
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error en la busqueda de usuarios");
            System.out.println(e.toString());
        }
    }
    
    /*Busqueda de Nuevo Contribuyente Propietario por DUI*/
    public void BuscarContribuyentesDuiOtro() {
        List lusuarios = null;
        try {
            if (!txtotrodui.getText().isEmpty() && !txtotrodui.getText().equals("-")) {
                contribuyenteDos = (Contribuyentes) dao.findByWhereStatementoneobj(Contribuyentes.class, "dui = '" + this.txtotrodui.getText() + "'");
                if (contribuyenteDos != null) {
                    lusuarios = new ArrayList();
                    lusuarios.add(contribuyenteDos.getUsuarios().getNombres() + " " + contribuyenteDos.getUsuarios().getApellidos());
                    componentes.llenarmodelo(modelootrocontribuyente, lusuarios);
                } else {
                    JOptionPane.showMessageDialog(this, "No Existen Contribuyentes registrados con el DUI ingresado");

                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error en la busqueda de usuarios");
            System.out.println(e.toString());
        }

    }

    /*Busqueda de Nuevo Contribuyente Propietario por NIT*/
    public void BuscarContribuyenteNitOtro() {
        List lusuarios = null;
        try {
            if (!txtotronit.getText().isEmpty() && !txtotronit.getText().equals("----")) {
                contribuyenteDos = (Contribuyentes) dao.findByWhereStatementoneobj(Contribuyentes.class, "nit = '" + this.txtotronit.getText() + "'");
                if (contribuyenteDos != null) {
                    lusuarios = new ArrayList();
                    lusuarios.add(contribuyenteDos.getUsuarios().getNombres() + " " + contribuyenteDos.getUsuarios().getApellidos());
                    componentes.llenarmodelo(modelootrocontribuyente, lusuarios);
                } else {
                    JOptionPane.showMessageDialog(this, "No Existen Contribuyentes registrados con el NIT ingresado");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error en la busqueda de usuarios");
            System.out.println(e.toString());
        }
    }    
    
    /*Carga de Datos del Negocio Seleccionado*/
    public void CargarDatosNegocios() {
        if (!cmbnegocios.getSelectedItem().toString().equals("-")) {
            try {
                negocio = (Negocios) dao.findByWhereStatementoneobj(Negocios.class, " nombreempresa = '" + cmbnegocios.getSelectedItem().toString() + "'");
                txtctacorriente.setText(negocio.getCuentacorriente());
                txtdireccion.setText(negocio.getDireccion());
                txtnempresa.setText(negocio.getNombreempresa());
                txttelefono.setText(negocio.getTelefono());
                txtnis.setText(negocio.getNis());
                cmbestado.setSelectedItem(negocio.getEstados().getEstado());
                cmbgiro.setSelectedItem(negocio.getGiros().getDescripcion());
                jdfecharegistro.setDate(negocio.getFecha_registro());
                cmbactividad.setSelectedItem(negocio.getTipos().getActividadeconomica());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error en la Carga de Datos del Negocio");
                System.out.println(e.toString());
            }
        }
    }
    
    public void CargarNegocios(){
        NegDAO daonegocio = new NegDAO();
        List lista = null;
        lista = daonegocio.findnameswhereStatement(" contribuyentes_id = " + contribuyente.getId());
        if (lista.size() != 0 && lista != null) {
            componentes.llenarmodelo(modelonegocio, lista);
        }
    }
    
    /*Modificando Las Propiedades del Negocio*/
    public void ModificarNegocio() {
        Negocios tmpnegocio = new Negocios();
        Estados estado = new Estados();
        Tiposcomercio tipo = new Tiposcomercio();
        Giros giro = new Giros();
        if (!cmbgiro.getSelectedItem().toString().equals("-") && !cmbactividad.getSelectedItem().toString().equals("-") && !cmbestado.getSelectedItem().toString().equals("-") && !txtctacorriente.getText().isEmpty() && !txtdireccion.getText().isEmpty() && !txtnempresa.getText().isEmpty()) {
            estado = (Estados) dao.findByWhereStatementoneobj(Estados.class, " estado = '" + cmbestado.getSelectedItem().toString() + "'");
            giro = (Giros) dao.findByWhereStatementoneobj(Giros.class, "descripcion = '" + cmbgiro.getSelectedItem().toString() + "' and estados_id = 1");
            tipo = (Tiposcomercio) dao.findByWhereStatementoneobj(Tiposcomercio.class, "actividadeconomica = '" + cmbactividad.getSelectedItem().toString() + "'");            
            if (contribuyenteDos != null && contribuyenteDos.getUsuarios() != null) {
                negocio.setContribuyentes(contribuyenteDos);
            }
            
            /*Chequeando si La cuenta corriente se modifico y si es Unica*/
            if (!txtctacorriente.getText().equals(negocio.getCuentacorriente())) {
                tmpnegocio = (Negocios) dao.findByWhereStatementoneobj(Negocios.class, " cuentacorriente= '" + txtctacorriente.getText() +"'");
                if (tmpnegocio == null) {
                    negocio.setCuentacorriente(txtctacorriente.getText());
                }else{
                    JOptionPane.showMessageDialog(this, "El Número de Cuenta Corriente ya ha Sido Asignado a Otro Negocio" + "\nFavor Verificar Antes de Guardar");
                    return;
                }
            }
            /*Chequeando si el Nis se modifico y si este es Unico*/
            if (!txtnis.getText().equals(negocio.getNis())) {
                tmpnegocio = (Negocios) dao.findByWhereStatementoneobj(Negocios.class, " nis= '" + txtnis.getText() +"'");
                if (tmpnegocio == null) {
                    negocio.setNis(txtnis.getText());
                }else{
                    JOptionPane.showMessageDialog(this, "El Número de Nis ya ha Sido Asignado a Otro Negocio" + "\nFavor Verificar Antes de Guardar");
                    return;
                }
            }
            /*Chequeando si cambio el nombre de la empresa y si este es Unico*/
            if (!txtnempresa.getText().equals(negocio.getNombreempresa())) {
                tmpnegocio = (Negocios) dao.findByWhereStatementoneobj(Negocios.class, " nombreempresa= '" + txtnempresa.getText() +"'");
                if (tmpnegocio == null) {
                    negocio.setNombreempresa(txtnempresa.getText());
                }else{
                    JOptionPane.showMessageDialog(this, "El Nombre Ingresado ya ha Sido Asignado a Otro Negocio" + "\nFavor Verificar Antes de Guardar");
                    return;
                }
            }
            /*Chequeando si cambio la direccion de la empresa y si esta es unica*/
            if (!txtdireccion.getText().equals(negocio.getDireccion())) {
                tmpnegocio = (Negocios) dao.findByWhereStatementoneobj(Negocios.class, " direccion= '" + txtdireccion.getText() +"'");
                if (tmpnegocio == null) {
                    negocio.setDireccion(txtdireccion.getText());
                }else{
                    JOptionPane.showMessageDialog(this, "La Dirección Ingresada ya ha Sido Asignada a Otro Negocio" + "\nFavor Verificar Antes de Guardar");
                    return;
                }
            }
            
            /*Seteo de las Propiedades Restantes*/
            negocio.setEstados(estado);
            negocio.setFecha_registro(jdfecharegistro.getDate());
            negocio.setGiros(giro);
            negocio.setTelefono(txttelefono.getText());
            negocio.setTipos(tipo);
            
            /*Realizando Actualización del Negocio*/
            dao.update(negocio);
            JOptionPane.showMessageDialog(this, "Actualización completa");
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
        buttonGroup2 = new javax.swing.ButtonGroup();
        jPanel7 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        txtnombres1 = new componentesheredados.LettersJTextField();
        jLabel22 = new javax.swing.JLabel();
        txtdui1 = new componentesheredados.DUITextField();
        jLabel23 = new javax.swing.JLabel();
        txtnit1 = new componentesheredados.NitJTextField();
        btnbuscar1 = new javax.swing.JButton();
        jbtnnombres1 = new javax.swing.JRadioButton();
        jbtndui1 = new javax.swing.JRadioButton();
        jbtnnit1 = new javax.swing.JRadioButton();
        jPanel1 = new javax.swing.JPanel();
        cmbcontribuyente = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        cmbnegocios = new javax.swing.JComboBox();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        cmbgiro = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        cmbactividad = new javax.swing.JComboBox();
        jLabel11 = new javax.swing.JLabel();
        txtnis = new componentesheredados.NumberJTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jdfecharegistro = new com.toedter.calendar.JDateChooser();
        txttelefono = new componentesheredados.PhoneJTextField();
        jLabel12 = new javax.swing.JLabel();
        txtctacorriente = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtnempresa = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtdireccion = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        cmbestado = new javax.swing.JComboBox();
        jPanel6 = new javax.swing.JPanel();
        chxmodnegocio = new javax.swing.JCheckBox();
        jbtnnombres2 = new javax.swing.JRadioButton();
        jbtndui2 = new javax.swing.JRadioButton();
        jbtnnit2 = new javax.swing.JRadioButton();
        btnbuscar2 = new javax.swing.JButton();
        jLabel24 = new javax.swing.JLabel();
        txtnombres2 = new componentesheredados.LettersJTextField();
        jLabel25 = new javax.swing.JLabel();
        txtotrodui = new componentesheredados.DUITextField();
        jLabel26 = new javax.swing.JLabel();
        txtotronit = new componentesheredados.NitJTextField();
        jLabel27 = new javax.swing.JLabel();
        cmbotrocontribuyente = new javax.swing.JComboBox();
        jPanel8 = new javax.swing.JPanel();
        btngrdinm = new javax.swing.JButton();
        btnslinm = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);

        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel20.setText("Criterios de Busqueda");
        jPanel7.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, -1, -1));

        jLabel21.setText("Nombres:");
        jPanel7.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, -1, -1));
        jPanel7.add(txtnombres1, new org.netbeans.lib.awtextra.AbsoluteConstraints(74, 67, 329, -1));

        jLabel22.setText("DUI:");
        jPanel7.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 101, -1, -1));

        try {
            txtdui1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("########-#")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jPanel7.add(txtdui1, new org.netbeans.lib.awtextra.AbsoluteConstraints(74, 98, 132, -1));

        jLabel23.setText("NIT:");
        jPanel7.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(224, 101, -1, -1));

        try {
            txtnit1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("####-######-###-#")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jPanel7.add(txtnit1, new org.netbeans.lib.awtextra.AbsoluteConstraints(263, 98, 140, -1));

        btnbuscar1.setText("Buscar");
        btnbuscar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnbuscar1ActionPerformed(evt);
            }
        });
        jPanel7.add(btnbuscar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(293, 33, 100, -1));

        buttonGroup1.add(jbtnnombres1);
        jbtnnombres1.setText("Nombres y Apellidos");
        jbtnnombres1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnnombres1ActionPerformed(evt);
            }
        });
        jPanel7.add(jbtnnombres1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 33, -1, -1));

        buttonGroup1.add(jbtndui1);
        jbtndui1.setText("DUI");
        jbtndui1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtndui1ActionPerformed(evt);
            }
        });
        jPanel7.add(jbtndui1, new org.netbeans.lib.awtextra.AbsoluteConstraints(162, 33, -1, -1));

        buttonGroup1.add(jbtnnit1);
        jbtnnit1.setText("NIT");
        jbtnnit1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnnit1ActionPerformed(evt);
            }
        });
        jPanel7.add(jbtnnit1, new org.netbeans.lib.awtextra.AbsoluteConstraints(235, 33, -1, -1));

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        cmbcontribuyente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbcontribuyenteActionPerformed(evt);
            }
        });
        jPanel1.add(cmbcontribuyente, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 318, -1));

        jLabel8.setText("* Contribuyente:");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        jLabel1.setText("Negocio :");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 61, -1, -1));

        cmbnegocios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbnegociosActionPerformed(evt);
            }
        });
        jPanel1.add(cmbnegocios, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 81, 318, -1));

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setText("* Giro:");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 14, -1, -1));

        cmbgiro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbgiroActionPerformed(evt);
            }
        });
        jPanel2.add(cmbgiro, new org.netbeans.lib.awtextra.AbsoluteConstraints(85, 11, 308, -1));

        jLabel4.setText("* Actividad:");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 52, -1, -1));

        cmbactividad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbactividadActionPerformed(evt);
            }
        });
        jPanel2.add(cmbactividad, new org.netbeans.lib.awtextra.AbsoluteConstraints(85, 49, 308, -1));

        jLabel11.setText(" NIS:");
        jPanel2.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 83, -1, -1));
        jPanel2.add(txtnis, new org.netbeans.lib.awtextra.AbsoluteConstraints(85, 80, 308, -1));

        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setText("Telefono:");
        jPanel5.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 14, -1, -1));

        jLabel6.setText("Fecha Registro:");
        jPanel5.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 51, -1, -1));
        jPanel5.add(jdfecharegistro, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 51, 218, -1));

        try {
            txttelefono.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jPanel5.add(txttelefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 11, 218, -1));

        jLabel12.setText("* Cta. Corriente:");
        jPanel5.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 85, -1, -1));
        jPanel5.add(txtctacorriente, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 82, 218, -1));

        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setText("* Nombre Empresa:");
        jPanel4.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 14, -1, -1));
        jPanel4.add(txtnempresa, new org.netbeans.lib.awtextra.AbsoluteConstraints(122, 11, 271, -1));

        jLabel7.setText("* Dirección:");
        jPanel4.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 52, -1, -1));
        jPanel4.add(txtdireccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(122, 49, 271, -1));

        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setText("Estado:");
        jPanel3.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, -1, -1));

        jPanel3.add(cmbestado, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 43, 318, -1));

        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        chxmodnegocio.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        chxmodnegocio.setText("Modificar Propietario del Negocio");
        chxmodnegocio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chxmodnegocioActionPerformed(evt);
            }
        });
        jPanel6.add(chxmodnegocio, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 7, -1, -1));

        buttonGroup2.add(jbtnnombres2);
        jbtnnombres2.setText("Nombres y Apellidos");
        jbtnnombres2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnnombres2ActionPerformed(evt);
            }
        });
        jPanel6.add(jbtnnombres2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 37, -1, -1));

        buttonGroup2.add(jbtndui2);
        jbtndui2.setText("DUI");
        jbtndui2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtndui2ActionPerformed(evt);
            }
        });
        jPanel6.add(jbtndui2, new org.netbeans.lib.awtextra.AbsoluteConstraints(157, 37, -1, -1));

        buttonGroup2.add(jbtnnit2);
        jbtnnit2.setText("NIT");
        jbtnnit2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnnit2ActionPerformed(evt);
            }
        });
        jPanel6.add(jbtnnit2, new org.netbeans.lib.awtextra.AbsoluteConstraints(229, 37, -1, -1));

        btnbuscar2.setText("Buscar");
        btnbuscar2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnbuscar2ActionPerformed(evt);
            }
        });
        jPanel6.add(btnbuscar2, new org.netbeans.lib.awtextra.AbsoluteConstraints(298, 37, 103, -1));

        jLabel24.setText("Nombres:");
        jPanel6.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(419, 41, -1, -1));
        jPanel6.add(txtnombres2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 38, 227, -1));

        jLabel25.setText("DUI:");
        jPanel6.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 74, -1, -1));

        try {
            txtotrodui.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("########-#")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jPanel6.add(txtotrodui, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 71, 123, -1));

        jLabel26.setText("NIT:");
        jPanel6.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 74, -1, -1));

        try {
            txtotronit.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("####-######-###-#")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jPanel6.add(txtotronit, new org.netbeans.lib.awtextra.AbsoluteConstraints(247, 71, 154, -1));

        jLabel27.setText("Contribuyente:");
        jPanel6.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(419, 74, -1, -1));

        cmbotrocontribuyente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbotrocontribuyenteActionPerformed(evt);
            }
        });
        jPanel6.add(cmbotrocontribuyente, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 71, 227, 27));

        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btngrdinm.setText("Modificar");
        btngrdinm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btngrdinmActionPerformed(evt);
            }
        });
        jPanel8.add(btngrdinm, new org.netbeans.lib.awtextra.AbsoluteConstraints(176, 11, 133, -1));

        btnslinm.setText("Salir");
        btnslinm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnslinmActionPerformed(evt);
            }
        });
        jPanel8.add(btnslinm, new org.netbeans.lib.awtextra.AbsoluteConstraints(441, 11, 131, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 403, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 338, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 747, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 747, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17)
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, 53, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnbuscar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbuscar1ActionPerformed
        // TODO add your handling code here:
        LimpiarPantalla();
        if (jbtnnombres1.isSelected()) {
            BuscarContribuyenteNombres();
        } else if (jbtndui1.isSelected()) {
            BuscarContribuyentesDui();
        } else if (jbtnnit1.isSelected()) {
            BuscarContribuyenteNit();
        } else {
            JOptionPane.showMessageDialog(this, "Elija un criterio de Busqueda e Ingrese el valor a buscar");
        }
    }//GEN-LAST:event_btnbuscar1ActionPerformed

    private void jbtnnombres1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnnombres1ActionPerformed
        // TODO add your handling code here:
        txtnombres1.setEnabled(true);
        txtdui1.setEnabled(false);
        txtnit1.setEnabled(false);
        txtdui1.setText("");
        txtnit1.setText("");
        LimpiarPantalla();
    }//GEN-LAST:event_jbtnnombres1ActionPerformed

    private void jbtndui1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtndui1ActionPerformed
        // TODO add your handling code here:
        txtdui1.setEnabled(true);
        txtnit1.setEnabled(false);
        txtnombres1.setEnabled(false);
        txtnit1.setText("");
        txtnombres1.setText("");
        LimpiarPantalla();
    }//GEN-LAST:event_jbtndui1ActionPerformed

    private void jbtnnit1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnnit1ActionPerformed
        // TODO add your handling code here:
        txtnit1.setEnabled(true);
        txtnombres1.setEnabled(false);
        txtdui1.setEnabled(false);
        txtnombres1.setText("");
        txtdui1.setText("");
        LimpiarPantalla();
    }//GEN-LAST:event_jbtnnit1ActionPerformed

    private void cmbcontribuyenteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbcontribuyenteActionPerformed
        // TODO add your handling code here:
        if (cmbcontribuyente.getSelectedItem() != null) {
            if (!cmbcontribuyente.getSelectedItem().toString().equals("-")) {
                if (jbtnnombres1.isSelected()) {
                    contribuyente = (Contribuyentes) dao.findByWhereStatementOneJoinObj(Contribuyentes.class, " as b inner join b.usuarios as a where (a.nombres || ' ' || a.apellidos || ' - DUI:' || b.dui) = '" + cmbcontribuyente.getSelectedItem().toString() + "'");
                }
                CargarNegocios();
            }
            cmbcontribuyente.setToolTipText(cmbcontribuyente.getSelectedItem().toString());
        }
    }//GEN-LAST:event_cmbcontribuyenteActionPerformed

    private void cmbgiroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbgiroActionPerformed
        // TODO add your handling code here:
        if (cmbgiro.getSelectedItem() != null) {
            if (!cmbgiro.getSelectedItem().toString().equals("-")) {
                componentes.limpiarmodelo(modeloactividad);
                LlenarComboActividad();
                cmbgiro.setToolTipText(cmbgiro.getSelectedItem().toString());
            } else {
                componentes.limpiarmodelo(modeloactividad);
                modeloactividad.addElement("-");
                cmbgiro.setToolTipText(cmbgiro.getSelectedItem().toString());
            }

        }
    }//GEN-LAST:event_cmbgiroActionPerformed

    private void cmbactividadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbactividadActionPerformed
        // TODO add your handling code here:
        if (cmbactividad.getSelectedItem() != null) {
            cmbactividad.setToolTipText(cmbactividad.getSelectedItem().toString());
        }
    }//GEN-LAST:event_cmbactividadActionPerformed

    private void chxmodnegocioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chxmodnegocioActionPerformed
        // TODO add your handling code here:
        buttonGroup2.clearSelection();
        if (chxmodnegocio.isSelected()) {
            jbtnnombres2.setEnabled(true);
            jbtndui2.setEnabled(true);
            jbtnnit2.setEnabled(true);
            cmbotrocontribuyente.setEnabled(true);
            btnbuscar2.setEnabled(true);
        }else{
            txtnombres2.setText("");
            txtotrodui.setText("");
            txtotronit.setText("");
            ReiniciarComboOtroContribuyente();
            jbtnnombres2.setEnabled(false);
            jbtndui2.setEnabled(false);
            jbtnnit2.setEnabled(false);
            txtnombres2.setEnabled(false);
            txtotrodui.setEnabled(false);
            txtotronit.setEnabled(false);            
            cmbotrocontribuyente.setEnabled(false);
            btnbuscar2.setEnabled(false);

        }
    }//GEN-LAST:event_chxmodnegocioActionPerformed

    private void jbtnnombres2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnnombres2ActionPerformed
        // TODO add your handling code here:
        txtnombres2.setEnabled(true);
        txtotrodui.setEnabled(false);
        txtotronit.setEnabled(false);
        txtotrodui.setText("");
        txtotronit.setText("");
        ReiniciarComboOtroContribuyente();
    }//GEN-LAST:event_jbtnnombres2ActionPerformed

    private void jbtndui2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtndui2ActionPerformed
        // TODO add your handling code here:
        txtotrodui.setEnabled(true);
        txtnombres2.setEnabled(false);
        txtotronit.setEnabled(false);
        txtnombres2.setText("");
        txtotronit.setText("");
        ReiniciarComboOtroContribuyente();
    }//GEN-LAST:event_jbtndui2ActionPerformed

    private void jbtnnit2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnnit2ActionPerformed
        // TODO add your handling code here:
        txtotronit.setEnabled(true);
        txtnombres2.setEnabled(false);
        txtotrodui.setEnabled(false);
        txtnombres2.setText("");
        txtotrodui.setText("");
        ReiniciarComboOtroContribuyente();
    }//GEN-LAST:event_jbtnnit2ActionPerformed

    private void btnbuscar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbuscar2ActionPerformed
        // TODO add your handling code here:
        componentes.limpiarmodelo(modelootrocontribuyente);
        modelootrocontribuyente.addElement("-");
        ReiniciarComboOtroContribuyente();
        if (jbtnnombres2.isSelected()) {
            BuscarContribuyenteNombresOtro();
        }else if(jbtndui2.isSelected()){
            BuscarContribuyentesDuiOtro();
        }else if(jbtnnit2.isSelected()){
            BuscarContribuyenteNitOtro();
        }else{
            JOptionPane.showMessageDialog(this, "Elija un criterio de Busqueda e Ingrese el valor a buscar");
        }
    }//GEN-LAST:event_btnbuscar2ActionPerformed

    private void cmbotrocontribuyenteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbotrocontribuyenteActionPerformed
        // TODO add your handling code here:
        if (cmbotrocontribuyente.getSelectedItem() != null) {
            if (!cmbotrocontribuyente.getSelectedItem().toString().equals("-")) {
                if (jbtnnombres2.isSelected()) {
                    contribuyenteDos = (Contribuyentes) dao.findByWhereStatementOneJoinObj(Contribuyentes.class, " as b inner join b.usuarios as a where (a.nombres || ' ' || a.apellidos || ' - DUI:' || b.dui) = '" + cmbotrocontribuyente.getSelectedItem().toString() + "'");
                }
            }
            cmbotrocontribuyente.setToolTipText(cmbotrocontribuyente.getSelectedItem().toString());
        }
    }//GEN-LAST:event_cmbotrocontribuyenteActionPerformed

    private void btngrdinmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btngrdinmActionPerformed
        // TODO add your handling code here:
        ModificarNegocio();
        CargarDatosNegocios();
        if (contribuyenteDos != null) {
            componentes.limpiarmodelo2(modelocontribuyente);
            if (chxmodnegocio.isSelected()) {
                chxmodnegocio.setSelected(false);
                txtnombres2.setText("");
                txtotrodui.setText("");
                txtotronit.setText("");
                jbtnnombres2.setEnabled(false);
                jbtndui2.setEnabled(false);
                jbtnnit2.setEnabled(false);
                txtnombres2.setEnabled(false);
                txtotrodui.setEnabled(false);
                txtotronit.setEnabled(false);
                cmbotrocontribuyente.setEnabled(false);
                btnbuscar2.setEnabled(false);
            }
        }else{
            chxmodnegocio.setSelected(false);
            txtnombres2.setText("");
            txtotrodui.setText("");
            txtotronit.setText("");
            cmbotrocontribuyente.setSelectedIndex(0);
            jbtnnombres2.setEnabled(false);
            jbtndui2.setEnabled(false);
            jbtnnit2.setEnabled(false);
            txtnombres2.setEnabled(false);
            txtotrodui.setEnabled(false);
            txtotronit.setEnabled(false);
            cmbotrocontribuyente.setEnabled(false);
            btnbuscar2.setEnabled(false);
        }

    }//GEN-LAST:event_btngrdinmActionPerformed

    private void btnslinmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnslinmActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnslinmActionPerformed

    private void cmbnegociosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbnegociosActionPerformed
        // TODO add your handling code here:
        if (cmbnegocios.getSelectedItem() != null) {
            CargarDatosNegocios();
        }
    }//GEN-LAST:event_cmbnegociosActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnbuscar1;
    private javax.swing.JButton btnbuscar2;
    private javax.swing.JButton btngrdinm;
    private javax.swing.JButton btnslinm;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JCheckBox chxmodnegocio;
    private javax.swing.JComboBox cmbactividad;
    private javax.swing.JComboBox cmbcontribuyente;
    private javax.swing.JComboBox cmbestado;
    private javax.swing.JComboBox cmbgiro;
    private javax.swing.JComboBox cmbnegocios;
    private javax.swing.JComboBox cmbotrocontribuyente;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
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
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JRadioButton jbtndui1;
    private javax.swing.JRadioButton jbtndui2;
    private javax.swing.JRadioButton jbtnnit1;
    private javax.swing.JRadioButton jbtnnit2;
    private javax.swing.JRadioButton jbtnnombres1;
    private javax.swing.JRadioButton jbtnnombres2;
    private com.toedter.calendar.JDateChooser jdfecharegistro;
    private javax.swing.JTextField txtctacorriente;
    private javax.swing.JTextField txtdireccion;
    private componentesheredados.DUITextField txtdui1;
    private javax.swing.JTextField txtnempresa;
    private componentesheredados.NumberJTextField txtnis;
    private componentesheredados.NitJTextField txtnit1;
    private componentesheredados.LettersJTextField txtnombres1;
    private componentesheredados.LettersJTextField txtnombres2;
    private componentesheredados.DUITextField txtotrodui;
    private componentesheredados.NitJTextField txtotronit;
    private componentesheredados.PhoneJTextField txttelefono;
    // End of variables declaration//GEN-END:variables
}
