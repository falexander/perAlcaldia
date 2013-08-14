/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package peralcaldia;

import controller.AbstractDAO;
import java.util.Iterator;
import java.util.List;
import peralcaldia.model.Categorias;
import peralcaldia.model.Impuestos;
import peralcaldia.model.Inmuebles;
import peralcaldia.model.Zonas;

/**
 *
 * @author luis
 */
public class PerAlcaldia {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        AbstractDAO dao = new AbstractDAO();
        Categorias cat = null;
        List<Categorias> lCat = null;
        Zonas zona = new Zonas();
        lCat = dao.findAll(Categorias.class);
        Inmuebles in = new Inmuebles();
        
        Iterator it = lCat.iterator();
        
        while(it.hasNext()){
            cat = (Categorias) it.next();
            System.out.println(cat.getCategoria());
        }
        
//        zona.setId(10);
        zona.setZona("Zona 1");
        dao.save(zona);
        
//        in.setId(10);
        in.setTelefono("2222222");
        in.setDireccion("dir1");
        in.setFolio1("fol1");
        in.setZonas(zona);
        
        dao.save(in);
    }
}
