/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sirelab.dao.interfacedao;

import com.sirelab.entidades.EstadoEquipo;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author ANDRES PINEDA
 */
public interface EstadoEquipoDAOInterface {

    public void crearEstadoEquipo(EstadoEquipo estadoEquipo);

    public void editarEstadoEquipo(EstadoEquipo estadoEquipo);

    public void eliminarEstadoEquipo(EstadoEquipo estadoEquipo);

    public List<EstadoEquipo> consultarEstadosEquipos();

    public EstadoEquipo buscarEstadoEquipoPorID(BigInteger idRegistro);

    public EstadoEquipo buscarEstadoEquipoPorNombre(String nombreEstadoEquipo);
}
