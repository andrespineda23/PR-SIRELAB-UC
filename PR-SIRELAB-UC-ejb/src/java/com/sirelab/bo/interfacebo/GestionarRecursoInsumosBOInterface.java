/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sirelab.bo.interfacebo;

import com.sirelab.entidades.Insumo;
import com.sirelab.entidades.Proveedor;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ANDRES PINEDA
 */
public interface GestionarRecursoInsumosBOInterface {

    public List<Insumo> consultarInsumosPorParametro(Map<String, String> filtros);

    public void crearNuevoInsumo(Insumo insumo);

    public void modificarInformacionInsumo(Insumo insumo);

    public Insumo obtenerInsumoPorIDInsumo(BigInteger idInsumo);

    public List<Proveedor> consultarProveedoresRegistrados();
}
