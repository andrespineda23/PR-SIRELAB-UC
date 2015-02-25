/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sirelab.bo.interfacebo;

import com.sirelab.entidades.Proveedor;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ANDRES PINEDA
 */
public interface GestionarRecursoProveedoresBOInterface {

    public List<Proveedor> consultarProveedoresPorParametro(Map<String, String> filtros);

    public void crearNuevoProveedor(Proveedor proveedor);

    public void modificarInformacionProveedor(Proveedor proveedor);

    public Proveedor obtenerProveedorPorIDProveedor(BigInteger idProveedor);

}
