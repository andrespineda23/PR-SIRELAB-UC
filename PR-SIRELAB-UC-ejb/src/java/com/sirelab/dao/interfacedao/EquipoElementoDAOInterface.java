/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sirelab.dao.interfacedao;

import com.sirelab.entidades.EquipoElemento;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ANDRES PINEDA
 */
public interface EquipoElementoDAOInterface {

    public void crearEquipoElemento(EquipoElemento equipoelemento);

    public void editarEquipoElemento(EquipoElemento equipoelemento);

    public void eliminarEquipoElemento(EquipoElemento equipoelemento);

    public List<EquipoElemento> consultarEquiposElementos();

    public EquipoElemento buscarEquipoElementoPorID(BigInteger idRegistro);

    public List<EquipoElemento> buscarEquiposElementosPorFiltrado(Map<String, String> filters);
}
