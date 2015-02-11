package com.sirelab.dao.interfacedao;

import com.sirelab.entidades.Carrera;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ANDRES PINEDA
 */
public interface CarreraDAOInterface {

    public void crearCarrera(Carrera carrera);

    public void editarCarrera(Carrera carrera);

    public void eliminarCarrera(Carrera carrera);

    public List<Carrera> consultarCarreras();

    public Carrera buscarCarreraPorID(BigInteger idRegistro);

    public List<Carrera> consultarCarrerasPorDepartamento(BigInteger idDepartamento);

    public List<Carrera> buscarCarrerasPorFiltrado(Map<String, String> filters);

}
