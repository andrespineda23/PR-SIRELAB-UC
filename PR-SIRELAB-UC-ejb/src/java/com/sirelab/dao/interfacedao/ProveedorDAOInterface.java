/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sirelab.dao.interfacedao;

import com.sirelab.entidades.Proveedor;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ANDRES PINEDA
 */
public interface ProveedorDAOInterface {

    public void crearProveedor(Proveedor proveedor);

    public void editarProveedor(Proveedor proveedor);

    public void eliminarProveedor(Proveedor proveedor);

    public List<Proveedor> consultarProveedores();

    public Proveedor buscarProveedorPorID(BigInteger idRegistro);

    public List<Proveedor> buscarProveedoresPorFiltrado(Map<String, String> filters);

}
