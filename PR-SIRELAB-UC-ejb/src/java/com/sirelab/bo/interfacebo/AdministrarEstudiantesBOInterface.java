package com.sirelab.bo.interfacebo;

import com.sirelab.entidades.Carrera;
import com.sirelab.entidades.Departamento;
import com.sirelab.entidades.Estudiante;
import com.sirelab.entidades.Facultad;
import com.sirelab.entidades.PlanEstudios;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ANDRES PINEDA
 */
public interface AdministrarEstudiantesBOInterface {

    public List<Departamento> obtenerListasDepartamentos();

    public List<Carrera> obtenerListasCarrerasPorDepartamento(BigInteger idDepartamento);

    public List<PlanEstudios> obtenerListasPlanesEstudioPorCarrera(BigInteger idCarrera);

    public List<Estudiante> consultarEstudiantesPorParametro(Map<String, String> filtros);

    public Estudiante obtenerEstudiantePorIDEstudiante(BigInteger idEstudiante);

    public List<Facultad> obtenerListaFacultades();

    public List<Departamento> obtenerDepartamentosPorIDFacultad(BigInteger idFacultad);

    public Estudiante obtenerEstudiantePorCorreoNumDocumento(String correo, String documento);

    public void actualizarInformacionEstudiante(Estudiante estudiante);
}
