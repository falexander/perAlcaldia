/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.util.Vector;
/**
 *
 * @author alex
 */
/*Interfaz Destinada Para la Generalización de la Carga de los Jtable, Por Medio de la Implementación de esta al momento
  de Llenar un Modelo, se recuperará el tipo de la clase y los valores correspondientes a Ingresar en las Tablas.*/
public interface IModelTable {
    Object[] obtainFields();    
}
