package com.sirelab.dao.interfacedao;

import com.sirelab.entidades.Docente;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ANDRES PINEDA
 */
public interface DocenteDAOInterface {

    public void crearDocente(Docente docente);

    public void editarDocente(Docente docente);

    public void eliminarDocente(Docente docente);

    public List<Docente> consultarDocentes();

    public Docente buscarDocentePorID(BigInteger idRegistro);

    public Docente buscarDocentePorIDPersona(BigInteger idPersona);

    public Docente obtenerDocentePorCorreoDocumento(String correo, String documento);

    public List<Docente> buscarDocentesPorFiltrado(Map<String, String> filters);

}
