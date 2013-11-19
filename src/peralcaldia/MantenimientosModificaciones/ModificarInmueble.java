/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package peralcaldia.MantenimientosModificaciones;

import controller.EstDAO;
import controller.InmDAO;
import controller.UserDAO;
import controller.ZnDAO;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import peralcaldia.model.Contribuyentes;
import peralcaldia.model.Estados;
import peralcaldia.model.Inmuebles;
import peralcaldia.model.Usuarios;
import peralcaldia.model.Zonas;
import peralcaldia.model.historico_inmueble;
import util.GCMPNativos;

/**
 *
 * @author alex
 */
/*Pantalla Destinada a la Modificacion de las propiedades de los inmuebles*/
public class ModificarInmueble extends javax.swing.JInternalFrame {
    InmDAO daoInm = new InmDAO();
    UserDAO dao = new UserDAO();
    Contribuyentes contribuyente = new Contribuyentes();
    Contribuyentes contribuyenteDos = new Contribuyentes();
    Inmuebles inmueble = new Inmuebles();
    historico_inmueble historicoinmant = new historico_inmueble();
    historico_inmueble historiconuevo = new historico_inmueble();
    GCMPNativos componentes = new GCMPNativos();
    DefaultComboBoxModel modelocontribuyente = new DefaultComboBoxModel();
    DefaultComboBoxModel modelozonas = new DefaultComboBoxModel();
    DefaultComboBoxModel modeloinmuebles = new DefaultComboBoxModel();
    DefaultComboBoxModel modeloestado = new DefaultComboBoxModel();
    DefaultComboBoxModel modelootrocontribuyente = new DefaultComboBoxModel();
    
    
    /**
     * Creates new form ModificarInmueble
     */
    public ModificarInmueble() {
        initComponents();
        modelocontribuyente.addElement("-");
        modelozonas.addElement("-");
        modeloinmuebles.addElement("-");
        modelootrocontribuyente.addElement("-");
        modeloestado.addElement("-");
        cmbcontribuyente.setModel(modelocontribuyente);
        cmbzona.setModel(modelozonas);
        cmbinmuebles.setModel(modeloinmuebles);
        cmbotrocontribuyente.setModel(modelootrocontribuyente);
        cmbestado.setModel(modeloestado);
        LlenarComboZonas();
        LlenarComboEstados();
        txtdui.setEnabled(false);
        txtnit.setEnabled(false);
        txtnombres.setEnabled(false);
        txtotrodui.setEnabled(false);
        txtotronit.setEnabled(false);
        txtnombres2.setEnabled(false);
        jbtndui2.setEnabled(false);
        jbtnnit2.setEnabled(false);
        jbtnnombres2.setEnabled(false);
        btnbuscar2.setEnabled(false);
        cmbotrocontribuyente.setEnabled(false);
        jdfechareg.setDate(new Date());
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension ventana = this.getSize();
        this.setLocation((pantalla.width - ventana.width) / 2, (pantalla.height - ventana.height) / 4);        
    } 

    /*Carga de las zonas ingresadas en el sistema*/
    public void LlenarComboZonas(){
        ZnDAO zdao = new ZnDAO();
        List Zlist = null;
        Zlist = zdao.find_all_zonas();
        if (Zlist.size() != 0  && Zlist != null) {
            componentes.llenarmodelo(modelozonas, Zlist);
        }
    }
    
    /*Carga de los estados validos para un inmueble*/
    public void LlenarComboEstados(){
        EstDAO daoestado = new EstDAO();
        List lista = null;
        lista= daoestado.find_estados_whereStatement(" id = 1 or id = 4");
        if (lista.size() != 0 && lista != null) {
            componentes.llenarmodelo(modeloestado, lista);
        }
    }
    
    /*Busqueda por nombre del contribuyente propietario original*/
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
    
    /*Busqueda por DUI del contribuyente propietario original*/
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

    /*Busqueda por NIT del contribuyente propietario original*/
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
    
    /*Limpieza de la informacion cargada en pantalla*/
    public void LimpiarPantalla(){
        chxmodcontr.setSelected(false);
        if (cmbzona.getSelectedItem() != null) {
            cmbzona.setSelectedIndex(0);
        }
        if (cmbcontribuyente.getSelectedItem() != null) {
            componentes.limpiarmodelo2(modelocontribuyente);
        }
        if (cmbotrocontribuyente.getSelectedItem() != null) {
            componentes.limpiarmodelo2(modelootrocontribuyente);
        }
        if (cmbinmuebles.getSelectedItem() != null) {
            componentes.limpiarmodelo2(modeloinmuebles);
        }
        if (cmbestado.getSelectedItem() != null) {
            cmbestado.setSelectedIndex(0);
        }
        txtctacorriente.setText("");
        txtMCdrs.setText("");
        txtMtLN.setText("");
        txtParcela.setText("");
        txtdirinm.setText("");
        txtfolio1.setText("");
        txtfolio2.setText("");
        txtniss.setText("");
        txtphone.setText("");
        txtsector.setText("");
        txttomo1.setText("");
        txttomo2.setText("");
        txttomoreal.setText("");
        jdfechareg.setDate(new Date());
    }
    
    /*Reincio de los valores en caso de haber cambio de contribuyente*/
    public void ReiniciarComboOtroContribuyente(){
        if (cmbotrocontribuyente.getSelectedItem() != null) {
            componentes.limpiarmodelo2(modelootrocontribuyente);
            contribuyenteDos = null;
        }
        
    }
    
    /*Buscar por nombre nuevo contribuyente propietario*/
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
    
    /*Buscar por DUI nuevo contribuyetne propietario*/
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

    /*Buscar por NIT nuevo contribuyente propietario*/
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
    
    /*Carga de Inmuebles segun propietario*/
    public void LlenarInmuebles() {
        List lista = null;
        lista = new ArrayList();
        if (!cmbcontribuyente.getSelectedItem().toString().equals("-")) {
            try {
                if (contribuyente == null && jbtnnombres.isSelected()) {
                    contribuyente = (Contribuyentes) dao.findByWhereStatementOneJoinObj(Contribuyentes.class, " as b inner join b.usuarios as a where (a.nombres || ' ' || a.apellidos || ' - DUI:' || b.dui) = '" + cmbcontribuyente.getSelectedItem().toString() + "'");
                }
                lista = daoInm.find_direcciones_whereStatement(" contribuyentes_id = " + contribuyente.getId());
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
        if (lista.size() != 0 || lista != null) {
            componentes.llenarmodelo(modeloinmuebles, lista);
            cmbinmuebles.setModel(modeloinmuebles);
        }
    }
    
    /*Carga de los datos del inmueble seleccionado*/
    public void CargarDatosInmueble(){        
        if (cmbinmuebles.getSelectedItem() != null && !cmbinmuebles.getSelectedItem().toString().equals("-")) {
            inmueble = (Inmuebles) dao.findByWhereStatementoneobj(Inmuebles.class, "direccion = '" + cmbinmuebles.getSelectedItem().toString() + "'");
            if (inmueble != null) {
                txtMCdrs.setText(inmueble.getMetros_cuadrados().toString());
                txtMtLN.setText(inmueble.getMetros_lineales().toString());
                txtParcela.setText(inmueble.getParcela());
                txtctacorriente.setText(inmueble.getCuentacorriente());
                txtdirinm.setText(inmueble.getDireccion());
                txtfolio1.setText(inmueble.getFolio1());
                txtfolio2.setText(inmueble.getFolio2());
                txtniss.setText(inmueble.getNis());
                txtphone.setText(inmueble.getTelefono());
                txtsector.setText(inmueble.getSector());
                txttomo1.setText(inmueble.getTomo1());
                txttomo2.setText(inmueble.getTomo2());
                txttomoreal.setText(inmueble.getTomoreal());
                if (inmueble.getZonas() != null) {
                    cmbzona.setSelectedItem(inmueble.getZonas().getZona());
                }
                jdfechareg.setDate(inmueble.getFecharegistro());
                cmbestado.setSelectedItem(inmueble.getEstadosinm().getEstado());
            }
        }
    }
    
    /*Modificacion del Inmueble seleccionado*/
    public void ModificandoInmueble() {
        Estados estado = new Estados();
        Zonas zona = new Zonas();
        try {
            if (!txtctacorriente.getText().isEmpty() && !txtniss.getText().isEmpty() && !txtdirinm.getText().isEmpty()) {
                /*Valores Generales*/
                zona = (Zonas) dao.findByWhereStatementoneobj(Zonas.class, " zona = '" + cmbzona.getSelectedItem().toString() + "'");
                estado = (Estados) dao.findByWhereStatementoneobj(Estados.class, " estado = '" + cmbestado.getSelectedItem().toString() + "'");
                /*Creando Los historicos en caso de cambio de contribuyente*/
                if (contribuyenteDos != null && contribuyenteDos.getUsuarios() != null) {
                    /*Inicializando el nuevo historio y finalizando el anterior*/
                    historiconuevo = new historico_inmueble();
                    historicoinmant = (historico_inmueble) dao.findByWhereStatementmaximoobjeto(historico_inmueble.class, " inmuebles_id =" + inmueble.getId());
                    if (jdfechareg.getDate().compareTo(inmueble.getFecharegistro()) == 0) {
                        historicoinmant.setFecha_fin(new Date());
                        historiconuevo.setFecha_inicio(new Date());
                    } else {
                        historicoinmant.setFecha_fin(jdfechareg.getDate());
                        historiconuevo.setFecha_inicio(jdfechareg.getDate());
                    }
                    historiconuevo.setContribuyenteshid(contribuyenteDos);
                    historiconuevo.setInmuebleshid(inmueble);
                    inmueble.setContribuyentes(contribuyenteDos);
                }
                /*Chequeando si se Cambio la Fecha de Registro del Inmueble*/
                if (jdfechareg.getDate().compareTo(inmueble.getFecharegistro()) != 0 && contribuyenteDos.getUsuarios() == null) {
                    historicoinmant = (historico_inmueble) dao.findByWhereStatementmaximoobjeto(historico_inmueble.class, " inmuebles_id =" + inmueble.getId());
                    historicoinmant.setFecha_inicio(jdfechareg.getDate());
                }else{
                    historicoinmant = (historico_inmueble) dao.findByWhereStatementmaximoobjeto(historico_inmueble.class, " inmuebles_id =" + inmueble.getId());
                }
                /*Chequeando si se ha realizado cambio de Direccion*/
                if (!txtdirinm.getText().toUpperCase().equals(inmueble.getDireccion())) {
                    Inmuebles tmpinm = new Inmuebles();
                    tmpinm = (Inmuebles) daoInm.findByWhereStatementoneobj(Inmuebles.class, "direccion = '" + txtdirinm.getText().toUpperCase() + "'");
                    if (tmpinm == null) {
                        inmueble.setDireccion(txtdirinm.getText().toUpperCase());
                    }else{
                        JOptionPane.showMessageDialog(this, "La Nueva Dirección Ingresada ya se encuentra en uso" + "\nFavor Verificar Antes de Modificar");
                        return;
                    }                    
                }
                /*Chequeando si se ha realizado cambio en la cuenta corriente*/
                if (!txtctacorriente.getText().toUpperCase().equals(inmueble.getCuentacorriente())) {
                    Inmuebles tmpinm = new Inmuebles();
                    tmpinm = (Inmuebles) daoInm.findByWhereStatementoneobj(Inmuebles.class, "cuentacorriente = '" + txtctacorriente.getText().toUpperCase() + "'");
                    if (tmpinm == null) {
                        inmueble.setCuentacorriente(txtctacorriente.getText().toUpperCase());
                    }else{
                        JOptionPane.showMessageDialog(this, "El Número de Cuenta Corriente Ingresado Ya ha Sido Asignado" + "\nFavor Verificar Antes de Modificar");
                        return;
                    }
                }
                /*Chequeando si se ha realizado cambio en el Nis*/
                if (!txtniss.getText().equals(inmueble.getNis())) {
                    Inmuebles tmpinm = new Inmuebles();
                    tmpinm = (Inmuebles) daoInm.findByWhereStatementoneobj(Inmuebles.class, "nis = '" + txtniss.getText() + "'");
                    if (tmpinm == null) {
                        inmueble.setNis(txtniss.getText());
                    }else{
                        JOptionPane.showMessageDialog(this, "El Nis Ingresado Ya se Encuentra Asignado a un Inmueble" + "\nFavor Verificar Antes de Modificar");
                        return;
                    }
                }
                /*Seteando las demás variables*/
                if (txtMCdrs.getText().equals("0") || txtMCdrs.getText().isEmpty()) {
                    inmueble.setMetros_cuadrados(BigDecimal.ONE);
                } else {
                    inmueble.setMetros_cuadrados(new BigDecimal(txtMCdrs.getText()));
                }
                if (txtMtLN.getText().equals("0") || txtMtLN.getText().isEmpty()) {
                    inmueble.setMetros_lineales(BigDecimal.ONE);
                } else {
                    inmueble.setMetros_lineales(new BigDecimal(txtMtLN.getText()));
                }
                inmueble.setEstadosinm(estado);
                inmueble.setFolio1(txtfolio1.getText());
                inmueble.setFolio2(txtfolio2.getText());
                inmueble.setParcela(txtParcela.getText());
                inmueble.setSector(txtsector.getText());
                inmueble.setTelefono(txtphone.getText());
                inmueble.setTomo1(txttomo1.getText());
                inmueble.setTomo2(txttomo2.getText());
                inmueble.setTomoreal(txttomoreal.getText());
                inmueble.setZonas(zona);
                /*Realizando el Guardado de Datos*/
                if (contribuyenteDos != null && contribuyenteDos.getUsuarios() != null) {
                    dao.update(historicoinmant);
                    dao.update(inmueble);
                    dao.save(historiconuevo);
                    JOptionPane.showMessageDialog(this, "Actualización Aplicada");                    
                } else {
                    dao.update(historicoinmant);
                    dao.update(inmueble);
                    JOptionPane.showMessageDialog(this, "Actualización Aplicada");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error Durante la Actualización" + "\nIntentelo mas Tarde");
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jPanel6 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        txtnombres = new componentesheredados.LettersJTextField();
        jLabel18 = new javax.swing.JLabel();
        txtdui = new componentesheredados.DUITextField();
        jLabel19 = new javax.swing.JLabel();
        txtnit = new componentesheredados.NitJTextField();
        btnbuscar = new javax.swing.JButton();
        jbtnnombres = new javax.swing.JRadioButton();
        jbtndui = new javax.swing.JRadioButton();
        jbtnnit = new javax.swing.JRadioButton();
        jpcon = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cmbcontribuyente = new javax.swing.JComboBox();
        cmbzona = new javax.swing.JComboBox();
        jLabel24 = new javax.swing.JLabel();
        cmbinmuebles = new javax.swing.JComboBox();
        jLabel26 = new javax.swing.JLabel();
        cmbestado = new javax.swing.JComboBox();
        jPanel8 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtfolio1 = new componentesheredados.NumberJTextField();
        txtfolio2 = new componentesheredados.NumberJTextField();
        txtphone = new componentesheredados.PhoneJTextField();
        jPanel7 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txttomo1 = new componentesheredados.NumberJTextField();
        txttomo2 = new componentesheredados.NumberJTextField();
        txttomoreal = new componentesheredados.NumberJTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        txtctacorriente = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtsector = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtParcela = new componentesheredados.NumberJTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jdfechareg = new com.toedter.calendar.JDateChooser();
        txtMtLN = new componentesheredados.NumberJTextField();
        txtMCdrs = new componentesheredados.NumberJTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtdirinm = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        txtniss = new componentesheredados.NumberJTextField();
        jPanel4 = new javax.swing.JPanel();
        btngrdinm = new javax.swing.JButton();
        btnslinm = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        chxmodcontr = new javax.swing.JCheckBox();
        jbtnnombres2 = new javax.swing.JRadioButton();
        jbtndui2 = new javax.swing.JRadioButton();
        jbtnnit2 = new javax.swing.JRadioButton();
        btnbuscar2 = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();
        txtnombres2 = new componentesheredados.LettersJTextField();
        jLabel22 = new javax.swing.JLabel();
        txtotrodui = new componentesheredados.DUITextField();
        jLabel23 = new javax.swing.JLabel();
        txtotronit = new componentesheredados.NitJTextField();
        jLabel25 = new javax.swing.JLabel();
        cmbotrocontribuyente = new javax.swing.JComboBox();

        setClosable(true);
        setIconifiable(true);

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel16.setText("Criterios de Busqueda");

        jLabel17.setText("Nombres:");

        jLabel18.setText("DUI:");

        try {
            txtdui.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("########-#")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabel19.setText("NIT:");

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

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addGap(42, 42, 42)
                        .addComponent(txtdui, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel19)
                        .addGap(18, 18, 18)
                        .addComponent(txtnit, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addGap(18, 18, 18)
                        .addComponent(txtnombres, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jbtnnombres)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                        .addComponent(jbtndui)
                        .addGap(42, 42, 42)
                        .addComponent(jbtnnit)
                        .addGap(42, 42, 42)
                        .addComponent(btnbuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnbuscar)
                    .addComponent(jbtnnombres)
                    .addComponent(jbtndui)
                    .addComponent(jbtnnit))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(txtnombres, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(txtdui, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19)
                    .addComponent(txtnit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jLabel1.setText("* Contribuyente:");

        jLabel2.setText("* Zona:");

        cmbcontribuyente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbcontribuyenteActionPerformed(evt);
            }
        });

        jLabel24.setText("* Inmueble:");

        cmbinmuebles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbinmueblesActionPerformed(evt);
            }
        });

        jLabel26.setText("* Estado:");

        javax.swing.GroupLayout jpconLayout = new javax.swing.GroupLayout(jpcon);
        jpcon.setLayout(jpconLayout);
        jpconLayout.setHorizontalGroup(
            jpconLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpconLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpconLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpconLayout.createSequentialGroup()
                        .addGroup(jpconLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel24))
                        .addGap(18, 18, 18)
                        .addGroup(jpconLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbcontribuyente, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmbinmuebles, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jpconLayout.createSequentialGroup()
                        .addGroup(jpconLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel26))
                        .addGap(54, 54, 54)
                        .addGroup(jpconLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbzona, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmbestado, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jpconLayout.setVerticalGroup(
            jpconLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpconLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpconLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cmbcontribuyente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jpconLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbinmuebles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24))
                .addGap(18, 18, 18)
                .addGroup(jpconLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(cmbzona, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jpconLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(cmbestado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        jLabel6.setText("Folio 1:");

        jLabel8.setText("Folio 2:");

        jLabel4.setText("Telefono :");

        try {
            txtphone.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jLabel8)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtfolio1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtfolio2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtphone, javax.swing.GroupLayout.DEFAULT_SIZE, 349, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtfolio1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtfolio2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtphone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel5.setText("Tomo 1:");

        jLabel7.setText("Tomo 2:");

        jLabel9.setText("Tomo Real :");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(18, 18, 18)
                        .addComponent(txttomoreal, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txttomo1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txttomo2, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txttomo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txttomo2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txttomoreal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel11.setText("* Cta. Corriente:");

        jLabel14.setText("Sector:");

        jLabel15.setText("Parcela:");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtctacorriente, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(jLabel15))
                        .addGap(44, 44, 44)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtsector)
                            .addComponent(txtParcela, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE))))
                .addContainerGap(13, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txtctacorriente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtsector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(txtParcela, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                .addComponent(jLabel12)
                .addGap(18, 18, 18)
                .addComponent(txtMtLN, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(jLabel13)
                .addGap(8, 8, 8)
                .addComponent(txtMCdrs, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(jLabel10)
                .addGap(18, 18, 18)
                .addComponent(jdfechareg, javax.swing.GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel12)
                                .addComponent(txtMtLN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel13)
                                .addComponent(jLabel10)
                                .addComponent(txtMCdrs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jdfechareg, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jLabel3.setText("* Dirección:");

        jLabel20.setText(" NISS:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(txtdirinm, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(jLabel20)
                .addGap(27, 27, 27)
                .addComponent(txtniss, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtdirinm, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20)
                    .addComponent(txtniss, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btngrdinm.setText("Modificar");
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
                .addGap(201, 201, 201)
                .addComponent(btngrdinm, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(180, 180, 180)
                .addComponent(btnslinm, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btngrdinm)
                    .addComponent(btnslinm))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        chxmodcontr.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        chxmodcontr.setText("Modificar Propietario del Inmueble");
        chxmodcontr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chxmodcontrActionPerformed(evt);
            }
        });

        buttonGroup2.add(jbtnnombres2);
        jbtnnombres2.setText("Nombres y Apellidos");
        jbtnnombres2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnnombres2ActionPerformed(evt);
            }
        });

        buttonGroup2.add(jbtndui2);
        jbtndui2.setText("DUI");
        jbtndui2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtndui2ActionPerformed(evt);
            }
        });

        buttonGroup2.add(jbtnnit2);
        jbtnnit2.setText("NIT");
        jbtnnit2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnnit2ActionPerformed(evt);
            }
        });

        btnbuscar2.setText("Buscar");
        btnbuscar2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnbuscar2ActionPerformed(evt);
            }
        });

        jLabel21.setText("Nombres:");

        jLabel22.setText("DUI:");

        try {
            txtotrodui.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("########-#")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabel23.setText("NIT:");

        try {
            txtotronit.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("####-######-###-#")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabel25.setText("Contribuyente:");

        cmbotrocontribuyente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbotrocontribuyenteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(chxmodcontr)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel22)
                                .addGap(18, 18, 18)
                                .addComponent(txtotrodui, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27)
                                .addComponent(jLabel23)
                                .addGap(26, 26, 26)
                                .addComponent(txtotronit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jbtnnombres2)
                                .addGap(26, 26, 26)
                                .addComponent(jbtndui2)
                                .addGap(29, 29, 29)
                                .addComponent(jbtnnit2)
                                .addGap(26, 26, 26)
                                .addComponent(btnbuscar2, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel25)
                            .addComponent(jLabel21))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtnombres2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmbotrocontribuyente, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(chxmodcontr)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbtnnombres2)
                    .addComponent(jbtndui2)
                    .addComponent(jbtnnit2)
                    .addComponent(btnbuscar2)
                    .addComponent(jLabel21)
                    .addComponent(txtnombres2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel22)
                        .addComponent(txtotrodui, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel23)
                        .addComponent(txtotronit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel25))
                    .addComponent(cmbotrocontribuyente, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpcon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jpcon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnbuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbuscarActionPerformed
        // TODO add your handling code here:
        componentes.limpiarmodelo(modelocontribuyente);
        modelocontribuyente.addElement("-");
        LimpiarPantalla();
        inmueble = null;
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

    private void jbtnnombresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnnombresActionPerformed
        // TODO add your handling code here:
        txtnombres.setEnabled(true);
        txtdui.setEnabled(false);
        txtnit.setEnabled(false);
        txtdui.setText("");
        txtnit.setText("");
        LimpiarPantalla();
        inmueble = null;
    }//GEN-LAST:event_jbtnnombresActionPerformed

    private void jbtnduiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnduiActionPerformed
        // TODO add your handling code here:
        txtdui.setEnabled(true);
        txtnit.setEnabled(false);
        txtnombres.setEnabled(false);
        txtnit.setText("");
        txtnombres.setText("");
        LimpiarPantalla();
        inmueble = null;
    }//GEN-LAST:event_jbtnduiActionPerformed

    private void jbtnnitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnnitActionPerformed
        // TODO add your handling code here:
        txtnit.setEnabled(true);
        txtnombres.setEnabled(false);
        txtdui.setEnabled(false);
        txtnombres.setText("");
        txtdui.setText("");
        LimpiarPantalla();
        inmueble = null;
    }//GEN-LAST:event_jbtnnitActionPerformed

    private void cmbcontribuyenteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbcontribuyenteActionPerformed
        // TODO add your handling code here:
        if (cmbcontribuyente.getSelectedItem() != null) {
            if (!cmbcontribuyente.getSelectedItem().toString().equals("-")) {
                if (jbtnnombres.isSelected()) {
                    contribuyente = (Contribuyentes) dao.findByWhereStatementOneJoinObj(Contribuyentes.class, " as b inner join b.usuarios as a where (a.nombres || ' ' || a.apellidos || ' - DUI:' || b.dui) = '" + cmbcontribuyente.getSelectedItem().toString() + "'");
                }
                LlenarInmuebles();
            }
            cmbcontribuyente.setToolTipText(cmbcontribuyente.getSelectedItem().toString());
        }
    }//GEN-LAST:event_cmbcontribuyenteActionPerformed

    private void btngrdinmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btngrdinmActionPerformed
        // TODO add your handling code here:
        ModificandoInmueble();
        CargarDatosInmueble();
        if (contribuyenteDos != null) {
            componentes.limpiarmodelo2(modelocontribuyente);
            if (chxmodcontr.isSelected()) {
                chxmodcontr.setSelected(false);
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
                chxmodcontr.setSelected(false);
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

    private void chxmodcontrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chxmodcontrActionPerformed
        // TODO add your handling code here:
        buttonGroup2.clearSelection();
        if (chxmodcontr.isSelected()) {
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
    }//GEN-LAST:event_chxmodcontrActionPerformed

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

    private void cmbinmueblesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbinmueblesActionPerformed
        // TODO add your handling code here:
        if (cmbinmuebles.getSelectedItem() != null) {
            CargarDatosInmueble();
        }
    }//GEN-LAST:event_cmbinmueblesActionPerformed

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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnbuscar;
    private javax.swing.JButton btnbuscar2;
    private javax.swing.JButton btngrdinm;
    private javax.swing.JButton btnslinm;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JCheckBox chxmodcontr;
    private javax.swing.JComboBox cmbcontribuyente;
    private javax.swing.JComboBox cmbestado;
    private javax.swing.JComboBox cmbinmuebles;
    private javax.swing.JComboBox cmbotrocontribuyente;
    private javax.swing.JComboBox cmbzona;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
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
    private javax.swing.JRadioButton jbtndui;
    private javax.swing.JRadioButton jbtndui2;
    private javax.swing.JRadioButton jbtnnit;
    private javax.swing.JRadioButton jbtnnit2;
    private javax.swing.JRadioButton jbtnnombres;
    private javax.swing.JRadioButton jbtnnombres2;
    private com.toedter.calendar.JDateChooser jdfechareg;
    private javax.swing.JPanel jpcon;
    private componentesheredados.NumberJTextField txtMCdrs;
    private componentesheredados.NumberJTextField txtMtLN;
    private componentesheredados.NumberJTextField txtParcela;
    private javax.swing.JTextField txtctacorriente;
    private javax.swing.JTextField txtdirinm;
    private componentesheredados.DUITextField txtdui;
    private componentesheredados.NumberJTextField txtfolio1;
    private componentesheredados.NumberJTextField txtfolio2;
    private componentesheredados.NumberJTextField txtniss;
    private componentesheredados.NitJTextField txtnit;
    private componentesheredados.LettersJTextField txtnombres;
    private componentesheredados.LettersJTextField txtnombres2;
    private componentesheredados.DUITextField txtotrodui;
    private componentesheredados.NitJTextField txtotronit;
    private componentesheredados.PhoneJTextField txtphone;
    private javax.swing.JTextField txtsector;
    private componentesheredados.NumberJTextField txttomo1;
    private componentesheredados.NumberJTextField txttomo2;
    private componentesheredados.NumberJTextField txttomoreal;
    // End of variables declaration//GEN-END:variables
}
