/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package peralcaldia.formularios;

import peralcaldia.Transacciones.*;
import beans.Verpagosnocancelados;
import beans.Vpgnocansincheckbox;
import beans.boletamap;
import controller.AbstractDAO;
import controller.InmDAO;
import controller.NegDAO;
import controller.PgDAO;
import controller.UserDAO;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import peralcaldia.model.Contribuyentes;
import peralcaldia.model.Inmuebles;
import peralcaldia.model.Negocios;
import peralcaldia.model.Usuarios;
import componentesheredados.CheckCell;
import componentesheredados.CheckRender;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.type.OrientationEnum;
import net.sf.jasperreports.swing.JRViewer;
import net.sf.jasperreports.view.JasperViewer;
import org.hibernate.lob.ReaderInputStream;
import peralcaldia.SLorenzoParent;
import peralcaldia.model.Boleta;
import util.GCMPNativos;
import util.GenerarBoletaRepository;
import util.codemd5;

/**
 *
 * @author alex
 */
public class FUATM3 extends javax.swing.JInternalFrame {

    InmDAO dao = new InmDAO();
    NegDAO daoneg = new NegDAO();
    PgDAO daopago = new PgDAO();
    UserDAO daouser = new UserDAO();
    Inmuebles inmueble = null;
    Negocios negocio = null;
    GCMPNativos componentes = new GCMPNativos();
    DefaultComboBoxModel modelocnt = new DefaultComboBoxModel();
    DefaultComboBoxModel modeloinmneg = new DefaultComboBoxModel();
    DefaultComboBoxModel modelonmeses = new DefaultComboBoxModel();
    DefaultTableModel tablam = new DefaultTableModel();
    Contribuyentes contribuyente = new Contribuyentes();
    

    /**
     * Creates new form GenerarBoleta
     */
    public FUATM3() {
        initComponents();
        modelocnt.addElement("-");
        cmbcontrib.setModel(modelocnt);
        modeloinmneg.addElement("-");
        cmbinmneg.setModel(modeloinmneg);
        modelonmeses.addElement("-");
       
        txtdui.setEnabled(false);
        txtnit.setEnabled(false);
        txtnombres.setEnabled(false);
    }

    public void llenarcomboboxpornegocio() {
        List lista = null;
        Usuarios users = new Usuarios();
        Set<Contribuyentes> lcont = null;
        Contribuyentes cont = new Contribuyentes();
        lista = new ArrayList();
        if (!cmbcontrib.getSelectedItem().toString().equals("-")) {
            try {
                if (contribuyente == null) {
                    contribuyente = (Contribuyentes) dao.findByWhereStatementOneJoinObj(Contribuyentes.class, " as b inner join b.usuarios as a where (a.nombres || ' ' || a.apellidos || ' - DUI:' || b.dui) = '" + cmbcontrib.getSelectedItem().toString() + "'");
                }
                lista = daoneg.findnameswhereStatement(" contribuyentes_id = " + contribuyente.getId() + " and estados_id = 1 ");
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
        if (lista.size() != 0 || lista != null) {
            componentes.llenarmodelo(modeloinmneg, lista);
            cmbinmneg.setModel(modeloinmneg);
        }
    }


    public void llenargrid() {
        List<Vpgnocansincheckbox> lista = null;
        Vpgnocansincheckbox vpn;
        List otls;
        Object[] unamas;
        int cont;
        GenerarBoletaRepository mora = new GenerarBoletaRepository();
        double valormora=0;
        BigDecimal monto= BigDecimal.ZERO;
        BigDecimal montomes= BigDecimal.ZERO;
        BigDecimal fiesta = BigDecimal.valueOf(0.05);
        BigDecimal total= BigDecimal.ZERO;
        boolean primer=true;
        boolean prmonto=true;
        String desde ="";
        String hasta ="";

        if (cmbinmneg.getModel().getSelectedItem() != null) {
            if (!cmbinmneg.getSelectedItem().toString().equals("-")) {
               
                    inmueble = null;
                    negocio = new Negocios();
                    negocio = (Negocios) daoneg.findByWhereStatementoneobj(Negocios.class, " nombreempresa = '" + cmbinmneg.getSelectedItem().toString() + "'");
                    lista = daopago.fin_pagos_whereStatement("negocios_id = " + Integer.toString(negocio.getId()) + " and estados_id = 6 and mespagado <> 'VARIOS ADELANTADO'");
                    otls = new ArrayList();
                    
                    if (!lista.isEmpty() || lista.size() > 0 || lista != null) {
                        unamas = new Object[3];
                        Iterator it = lista.iterator();
                        while (it.hasNext()) {
                            unamas = (Object[]) it.next();
                            cont = 0;
                            vpn = new Vpgnocansincheckbox();
                            for (Object object : unamas) {
                                if (cont == 0) {
                                    vpn.setPERIODO(object.toString());
                                    if(primer == true)
                                    { desde= object.toString();
                                      primer=false;
                                      
                                    }hasta=object.toString();
                                    cont++;
                                } else if (cont == 1) {
                                    vpn.setMONTO_IMPUESTOS(new BigDecimal(object.toString()));
                                    monto = monto.add((new BigDecimal(object.toString())).setScale(2, RoundingMode.HALF_EVEN));
                                    if (prmonto==true){
                                        montomes = montomes.add((new BigDecimal(object.toString())).setScale(2, RoundingMode.HALF_EVEN));
                                        
                                    prmonto=false;
                                    }
                                    cont++;
                                } else {
                                    vpn.setESTADO_DEL_COBRO(object.toString());
                                    cont = 0;
                                    otls.add(vpn);
                                    vpn = new Vpgnocansincheckbox();
                                }
                            }
                        }
                        jtpagos.setModel(componentes.llenartabla(tablam, otls, Vpgnocansincheckbox.class));
                        componentes.centrarcabeceras(jtpagos);
                        componentes.centrarcolumnas(jtpagos, 2);
//                        llenarcombomesesapagar(lista.size());
                        fiesta = fiesta.multiply(montomes).setScale(2, RoundingMode.HALF_EVEN);
                        jTextPropietario.setText(negocio.getContribuyentes().getUsuarios().getNombres()+ " "+ negocio.getContribuyentes().getUsuarios().getApellidos()); 
                        jTextDui.setText(negocio.getContribuyentes().getDui().toString());
                        jTextEmpresa.setText(negocio.getNombreempresa());
                        jTextImpuesto.setText(montomes.toString());
                        jTextFiesta.setText(fiesta.toString());
                        monto=montomes.add(fiesta);
                        jTextTotaIimpustos.setText(monto.toString());
                        
                        
                    }
                    if (lista.isEmpty() || lista.size() <= 0) {
  //                      llenarcombomesesapagar(0);
                    }
                else {
                    System.out.println("Error en la carga");
                }
            }
        }
    }

    


    public void BuscarContribuyenteNombres() {
        List lusuarios = null;
        try {
            /*Buscando Usuarios que coincidan con el nombre o porcion indicada en el Textbox*/
            if (!txtnombres.getText().isEmpty()) {
                lusuarios = daouser.find_usuarios_whereStatement(" a.nombres || ' ' || a.apellidos || ' - DUI:' || b.dui like '%" + txtnombres.getText().toUpperCase() + "%' and a.roles = 3");
                if (lusuarios != null && lusuarios.size() > 0) {
                    componentes.llenarmodelo(modelocnt, lusuarios);
                    contribuyente = null;
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error en la busqueda de usuarios");
            System.out.println(e.toString());
        }
    }
    
    public void BuscarContribuyentesDui() {
        List lusuarios = null;
        try {
            if (!txtdui.getText().isEmpty() && !txtdui.getText().equals("-")) {
                contribuyente = (Contribuyentes) dao.findByWhereStatementoneobj(Contribuyentes.class, "dui = '" + this.txtdui.getText() + "'");
                if (contribuyente != null) {
                    lusuarios = new ArrayList();
                    lusuarios.add(contribuyente.getUsuarios().getNombres() + " " + contribuyente.getUsuarios().getApellidos());
                    componentes.llenarmodelo(modelocnt, lusuarios);
                } else {
                    JOptionPane.showMessageDialog(this, "No Existen Contribuyentes registrados con el DUI ingresado");

                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error en la busqueda de usuarios");
            System.out.println(e.toString());
        }

    }

    public void BuscarContribuyenteNit() {
        List lusuarios = null;
        try {
            if (!txtnit.getText().isEmpty() && !txtnit.getText().equals("----")) {
                contribuyente = (Contribuyentes) dao.findByWhereStatementoneobj(Contribuyentes.class, "nit = '" + this.txtnit.getText() + "'");
                if (contribuyente != null) {
                    lusuarios = new ArrayList();
                    lusuarios.add(contribuyente.getUsuarios().getNombres() + " " + contribuyente.getUsuarios().getApellidos());
                    componentes.llenarmodelo(modelocnt, lusuarios);
                } else {
                    JOptionPane.showMessageDialog(this, "No Existen Contribuyentes registrados con el NIT ingresado");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error en la busqueda de usuarios");
            System.out.println(e.toString());
        }
    }
    
    public void LimpiarPantalla(){
        if (cmbcontrib.getSelectedItem() != null && cmbinmneg.getSelectedItem() != null) {
            try {
                cmbcontrib.removeAllItems();
                modelocnt.addElement("-");
                componentes.limpiarmodelo(modeloinmneg);
                modeloinmneg.addElement("-");
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }        
    }

     public  String  mestotext (Calendar fecha) {
           int month = fecha.get(Calendar.MONTH)+1;
        String mestext;
        switch (month) {
            case 1:  mestext = "Enero";
                     break;
            case 2:  mestext = "Febrero";
                     break;
            case 3:  mestext = "Marzo";
                     break;
            case 4:  mestext = "Abril";
                     break;
            case 5:  mestext = "Mayo";
                     break;
            case 6:  mestext = "Junio";
                     break;
            case 7:  mestext = "Julio";
                     break;
            case 8:  mestext = "Agosto";
                     break;
            case 9:  mestext = "Septiembre";
                     break;
            case 10: mestext = "Octubre";
                     break;
            case 11: mestext = "Noviembre";
                     break;
            case 12: mestext = "Diciembre";
                     break;
            default: mestext = "Mes invalido";
                     break;
        }
        
        return mestext;
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
        jPanel1 = new javax.swing.JPanel();
        cmbinmneg = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        cmbcontrib = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jtablelistpg = new javax.swing.JScrollPane();
        jtpagos = new javax.swing.JTable();
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
        jPanel7 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabelNombre1 = new javax.swing.JLabel();
        jLabelDireccion1 = new javax.swing.JLabel();
        jTextPropietario = new javax.swing.JTextField();
        jLabelMonto1 = new javax.swing.JLabel();
        jLabelDesde1 = new javax.swing.JLabel();
        jLabelAlumbrado1 = new javax.swing.JLabel();
        jLabelAseo1 = new javax.swing.JLabel();
        jLabelPavimento1 = new javax.swing.JLabel();
        jLabelAgua1 = new javax.swing.JLabel();
        jLabelAlcantarillado1 = new javax.swing.JLabel();
        jLabelFiestas1 = new javax.swing.JLabel();
        jLabelInteres1 = new javax.swing.JLabel();
        jLabelHasta1 = new javax.swing.JLabel();
        btnsalir2 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jTextDui = new javax.swing.JTextField();
        jTextEmpresa = new javax.swing.JTextField();
        jTextActivosInventario = new javax.swing.JTextField();
        jTextActivosInvertidos = new javax.swing.JTextField();
        jTextTotalbalance = new javax.swing.JTextField();
        jTextDepreciacion = new javax.swing.JTextField();
        jTextOtros = new javax.swing.JTextField();
        jTextImpuesto = new javax.swing.JTextField();
        jTextFiesta = new javax.swing.JTextField();
        jTextTotaIimpustos = new javax.swing.JTextField();
        jTextInteres = new javax.swing.JTextField();
        jTextResolucion = new javax.swing.JTextField();
        jdfini = new com.toedter.calendar.JDateChooser();
        jLabel7 = new javax.swing.JLabel();
        jdfini1 = new com.toedter.calendar.JDateChooser();
        jLabel9 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setResizable(true);

        cmbinmneg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbinmnegActionPerformed(evt);
            }
        });

        jLabel4.setText("Contribuyente:");

        cmbcontrib.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbcontribActionPerformed(evt);
            }
        });

        jLabel2.setText("Negocios:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cmbcontrib, 0, 403, Short.MAX_VALUE)
                    .addComponent(cmbinmneg, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(127, 127, 127))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cmbcontrib, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbinmneg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jtpagos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jtablelistpg.setViewportView(jtpagos);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jtablelistpg)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jtablelistpg, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

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

        buttonGroup2.add(jbtnnombres);
        jbtnnombres.setText("Nombres y Apellidos");
        jbtnnombres.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnnombresActionPerformed(evt);
            }
        });

        buttonGroup2.add(jbtndui);
        jbtndui.setText("DUI");
        jbtndui.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnduiActionPerformed(evt);
            }
        });

        buttonGroup2.add(jbtnnit);
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
                        .addComponent(jLabel17)
                        .addGap(18, 18, 18)
                        .addComponent(txtnombres, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addGap(42, 42, 42)
                        .addComponent(txtdui, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel19)
                        .addGap(18, 18, 18)
                        .addComponent(txtnit, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jbtnnombres)
                        .addGap(72, 72, 72)
                        .addComponent(jbtndui)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jbtnnit)
                        .addGap(61, 61, 61)
                        .addComponent(btnbuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addGap(0, 0, Short.MAX_VALUE))))
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

        jLabel5.setText("Resolucion numero:");

        jLabelNombre1.setText("Propietario:");

        jLabelDireccion1.setText("Dui:");

        jTextPropietario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextPropietarioActionPerformed(evt);
            }
        });

        jLabelMonto1.setText("Empresa:");

        jLabelDesde1.setText("Valor de activos según inventario realizado");

        jLabelAlumbrado1.setText("Total balance:");

        jLabelAseo1.setText("Mas Depreciación acumulada de edificios:");

        jLabelPavimento1.setText("Otros:");

        jLabelAgua1.setText("Impuesto mensual a pagar:");

        jLabelAlcantarillado1.setText("Mas: 5% de fiestas patronales:");

        jLabelFiestas1.setText("Total del impuesto mensual a pagar:");

        jLabelInteres1.setText("Más: Multa por extemporaneidad:");

        jLabelHasta1.setText("Menos: Activos invertidos en otra jurisdicción:");

        btnsalir2.setText("Salir");
        btnsalir2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsalir2ActionPerformed(evt);
            }
        });

        jButton2.setText("Generar Aviso");
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton2MouseClicked(evt);
            }
        });
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jTextDui.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextDuiActionPerformed(evt);
            }
        });

        jTextEmpresa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextEmpresaActionPerformed(evt);
            }
        });

        jTextActivosInventario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextActivosInventarioActionPerformed(evt);
            }
        });

        jTextActivosInvertidos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextActivosInvertidosActionPerformed(evt);
            }
        });

        jTextTotalbalance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextTotalbalanceActionPerformed(evt);
            }
        });

        jTextDepreciacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextDepreciacionActionPerformed(evt);
            }
        });

        jTextOtros.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextOtrosActionPerformed(evt);
            }
        });

        jTextImpuesto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextImpuestoActionPerformed(evt);
            }
        });

        jTextFiesta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFiestaActionPerformed(evt);
            }
        });

        jTextTotaIimpustos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextTotaIimpustosActionPerformed(evt);
            }
        });

        jTextInteres.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextInteresActionPerformed(evt);
            }
        });

        jTextResolucion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextResolucionActionPerformed(evt);
            }
        });

        jdfini.setDateFormatString("dd-MM-yyyy");

        jLabel7.setText("Fecha de Resolucion");

        jdfini1.setDateFormatString("dd-MM-yyyy");

        jLabel9.setText("Balance presentado al:");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelHasta1)
                            .addComponent(jLabelDesde1)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelMonto1)
                            .addComponent(jLabelDireccion1)
                            .addComponent(jLabelNombre1)
                            .addComponent(jLabel7)
                            .addComponent(jLabel5)
                            .addComponent(jLabelAlumbrado1)
                            .addComponent(jLabelAseo1)
                            .addComponent(jLabelPavimento1)
                            .addComponent(jLabelAgua1)
                            .addComponent(jLabelAlcantarillado1)
                            .addComponent(jLabelFiestas1)
                            .addComponent(jLabelInteres1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextPropietario, javax.swing.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE)
                            .addComponent(jTextDui, javax.swing.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE)
                            .addComponent(jTextEmpresa, javax.swing.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE)
                            .addComponent(jTextActivosInventario, javax.swing.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE)
                            .addComponent(jTextActivosInvertidos, javax.swing.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE)
                            .addComponent(jTextTotalbalance, javax.swing.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE)
                            .addComponent(jTextDepreciacion, javax.swing.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE)
                            .addComponent(jTextOtros, javax.swing.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE)
                            .addComponent(jTextImpuesto, javax.swing.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE)
                            .addComponent(jTextFiesta, javax.swing.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE)
                            .addComponent(jTextTotaIimpustos, javax.swing.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE)
                            .addComponent(jTextInteres, javax.swing.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE)
                            .addComponent(jTextResolucion, javax.swing.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE)
                            .addComponent(jdfini1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jdfini, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(160, 160, 160)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 178, Short.MAX_VALUE)
                        .addComponent(btnsalir2, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(43, 43, 43))))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextResolucion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel7)
                    .addComponent(jdfini, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextPropietario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelNombre1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextDui, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelDireccion1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelMonto1))
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(jLabel9))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jdfini1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextActivosInventario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelDesde1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextActivosInvertidos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelHasta1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextTotalbalance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelAlumbrado1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextDepreciacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelAseo1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextOtros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelPavimento1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextImpuesto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelAgua1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFiesta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelAlcantarillado1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextTotaIimpustos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelFiestas1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextInteres, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelInteres1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(btnsalir2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 538, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmbinmnegActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbinmnegActionPerformed
        // TODO add your handling code here:
        tablam = componentes.limpiartabla(tablam);
        jtpagos.setModel(tablam);
        llenargrid();
    }//GEN-LAST:event_cmbinmnegActionPerformed

    private void cmbcontribActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbcontribActionPerformed
        // TODO add your handling code here:
        componentes.limpiarmodelo(modeloinmneg);
        modeloinmneg.addElement("-");
        cmbinmneg.setModel(modeloinmneg);
        tablam = componentes.limpiartabla(tablam);
        jtpagos.setModel(tablam);
        inmueble = null;
        negocio = null;
        if (cmbcontrib.getSelectedItem() != null) {
            if (!cmbcontrib.getSelectedItem().toString().equals("-")) {
                if (jbtnnombres.isSelected()) {
                    contribuyente = (Contribuyentes) dao.findByWhereStatementOneJoinObj(Contribuyentes.class, " as b inner join b.usuarios as a where (a.nombres || ' ' || a.apellidos || ' - DUI:' || b.dui) = '" + cmbcontrib.getSelectedItem().toString() + "'");
                    cmbcontrib.setToolTipText(cmbcontrib.getSelectedItem().toString());
                }
                llenarcomboboxpornegocio();
            }
        }
    }//GEN-LAST:event_cmbcontribActionPerformed

    private void btnbuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbuscarActionPerformed
        // TODO add your handling code here:
        componentes.limpiarmodelo(modelocnt);
        modelocnt.addElement("-");
        LimpiarPantalla();
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
    }//GEN-LAST:event_jbtnnombresActionPerformed

    private void jbtnduiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnduiActionPerformed
        // TODO add your handling code here:
        txtdui.setEnabled(true);
        txtnit.setEnabled(false);
        txtnombres.setEnabled(false);
        txtnit.setText("");
        txtnombres.setText("");
        LimpiarPantalla();
    }//GEN-LAST:event_jbtnduiActionPerformed

    private void jbtnnitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnnitActionPerformed
        // TODO add your handling code here:
        txtnit.setEnabled(true);
        txtnombres.setEnabled(false);
        txtdui.setEnabled(false);
        txtnombres.setText("");
        txtdui.setText("");
        LimpiarPantalla();
    }//GEN-LAST:event_jbtnnitActionPerformed

    private void jTextPropietarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextPropietarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextPropietarioActionPerformed

    private void btnsalir2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsalir2ActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnsalir2ActionPerformed

    private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseClicked
        // TODO add your handling code here:
        Calendar fechav = new GregorianCalendar();
        Calendar fechabalan = new GregorianCalendar();
        fechav.setTime(jdfini.getDate());
        fechabalan.setTime(jdfini1.getDate());
        String dia = Integer.toString(fechav.get(Calendar.DAY_OF_MONTH));
        String mes = mestotext(fechav);
        String anio = Integer.toString(fechav.get(Calendar.YEAR));
        String fechabalance = Integer.toString(fechabalan.get(Calendar.DAY_OF_MONTH)) + "-" +Integer.toString(fechabalan.get(Calendar.MONTH )+1)+ "-" +Integer.toString(fechabalan.get(Calendar.YEAR));
        String nresolucion = jTextResolucion.getText();
        String empresa = jTextEmpresa.getText();
        String propietario = jTextPropietario.getText();
        String dui = jTextDui.getText();
        

        try{
            Map<String, Object> params = new HashMap<String, Object>();
            String reporteLocation ="src/formularios/report3.jrxml";
            params.put("nresolucion",nresolucion);
            params.put("dia",dia);
            params.put("mes",mes);
            params.put("anio",anio);
            params.put("empresa",empresa);
            params.put("propietario",propietario);
            params.put("dui",dui);
            params.put("fechabalance",fechabalance);
            params.put("activosinventario",jTextActivosInventario.getText());
            params.put("activosinvertidos",jTextActivosInvertidos.getText());
            params.put("totalbalance",jTextTotalbalance.getText());
            params.put("depreciacion",jTextDepreciacion.getText());
            params.put("otros",jTextOtros.getText());
            params.put("impuesto",jTextImpuesto.getText());
            params.put("fiestas",jTextFiesta.getText());
            params.put("totalimpuestos",jTextTotaIimpustos.getText());
            params.put("multa",jTextInteres.getText());

            JasperReport reporte=   JasperCompileManager.compileReport(reporteLocation);
            //JasperPrint print = JasperFillManager.fillReport(reporte,null,coneccionSQL());
            JasperPrint print = JasperFillManager.fillReport(reporte,params,new JREmptyDataSource());
            JasperViewer.viewReport(print,false);
            //   JRViewer viewer = new JRViewer(print);
            //viewer.setOpaque(true);
            // viewer.setVisible(true);
            //    add(viewer);
            //JasperExportManager.exportReportToPdfFile(print, "Informefuatm1.pdf");
        }catch(Exception e   ){
            System.out.print(e.getMessage());
        }
    }//GEN-LAST:event_jButton2MouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTextDuiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextDuiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextDuiActionPerformed

    private void jTextEmpresaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextEmpresaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextEmpresaActionPerformed

    private void jTextActivosInventarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextActivosInventarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextActivosInventarioActionPerformed

    private void jTextActivosInvertidosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextActivosInvertidosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextActivosInvertidosActionPerformed

    private void jTextTotalbalanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextTotalbalanceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextTotalbalanceActionPerformed

    private void jTextDepreciacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextDepreciacionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextDepreciacionActionPerformed

    private void jTextOtrosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextOtrosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextOtrosActionPerformed

    private void jTextImpuestoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextImpuestoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextImpuestoActionPerformed

    private void jTextFiestaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFiestaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFiestaActionPerformed

    private void jTextTotaIimpustosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextTotaIimpustosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextTotaIimpustosActionPerformed

    private void jTextInteresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextInteresActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextInteresActionPerformed

    private void jTextResolucionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextResolucionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextResolucionActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnbuscar;
    private javax.swing.JButton btnsalir2;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JComboBox cmbcontrib;
    private javax.swing.JComboBox cmbinmneg;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelAgua1;
    private javax.swing.JLabel jLabelAlcantarillado1;
    private javax.swing.JLabel jLabelAlumbrado1;
    private javax.swing.JLabel jLabelAseo1;
    private javax.swing.JLabel jLabelDesde1;
    private javax.swing.JLabel jLabelDireccion1;
    private javax.swing.JLabel jLabelFiestas1;
    private javax.swing.JLabel jLabelHasta1;
    private javax.swing.JLabel jLabelInteres1;
    private javax.swing.JLabel jLabelMonto1;
    private javax.swing.JLabel jLabelNombre1;
    private javax.swing.JLabel jLabelPavimento1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JTextField jTextActivosInventario;
    private javax.swing.JTextField jTextActivosInvertidos;
    private javax.swing.JTextField jTextDepreciacion;
    private javax.swing.JTextField jTextDui;
    private javax.swing.JTextField jTextEmpresa;
    private javax.swing.JTextField jTextFiesta;
    private javax.swing.JTextField jTextImpuesto;
    private javax.swing.JTextField jTextInteres;
    private javax.swing.JTextField jTextOtros;
    private javax.swing.JTextField jTextPropietario;
    private javax.swing.JTextField jTextResolucion;
    private javax.swing.JTextField jTextTotaIimpustos;
    private javax.swing.JTextField jTextTotalbalance;
    private javax.swing.JRadioButton jbtndui;
    private javax.swing.JRadioButton jbtnnit;
    private javax.swing.JRadioButton jbtnnombres;
    private com.toedter.calendar.JDateChooser jdfini;
    private com.toedter.calendar.JDateChooser jdfini1;
    private javax.swing.JScrollPane jtablelistpg;
    private javax.swing.JTable jtpagos;
    private componentesheredados.DUITextField txtdui;
    private componentesheredados.NitJTextField txtnit;
    private componentesheredados.LettersJTextField txtnombres;
    // End of variables declaration//GEN-END:variables
}
