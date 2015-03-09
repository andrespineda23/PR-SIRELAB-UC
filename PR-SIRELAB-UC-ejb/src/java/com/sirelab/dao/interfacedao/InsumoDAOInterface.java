/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sirelab.dao.interfacedao;

import com.sirelab.entidades.Insumo;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ANDRES PINEDA
 */
public interface InsumoDAOInterface {

    public void crearInsumo(Insumo insumo);

    public void editarInsumo(Insumo insumo);

    public void eliminarInsumo(Insumo insumo);

    public List<Insumo> consultarInsumos();

    public Insumo buscarInsumoPorID(BigInteger idRegistro);

    public List<Insumo> buscarInsumosPorFiltrado(Map<String, String> filters);
}
