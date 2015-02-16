/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sirelab.dao.interfacedao;

import com.sirelab.entidades.AreaProfundizacion;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ANDRES PINEDA
 */
public interface AreaProfundizacionDAOInterface {

    public void crearAreaProfundizacion(AreaProfundizacion areaprofundizacion);

    public void editarAreaProfundizacion(AreaProfundizacion areaprofundizacion);

    public void eliminarAreaProfundizacion(AreaProfundizacion areaprofundizacion);

    public List<AreaProfundizacion> consultarAreasProfundizacion();

    public AreaProfundizacion buscarAreaProfundizacionPorID(BigInteger idRegistro);

    public List<AreaProfundizacion> buscarAreaProfundizacionPorIDLaboratorio(BigInteger idLaboratorio);

    public List<AreaProfundizacion> buscarAreasProfundizacionPorFiltrado(Map<String, String> filters);
}
