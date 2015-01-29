package com.sirelab.bo;

import com.sirelab.bo.interfacebo.AdministrarEstudiantesBOInterface;
import com.sirelab.dao.interfacedao.CarreraDAOInterface;
import com.sirelab.dao.interfacedao.DepartamentoDAOInterface;
import com.sirelab.dao.interfacedao.EstudianteDAOInterface;
import com.sirelab.dao.interfacedao.FacultadDAOInterface;
import com.sirelab.dao.interfacedao.PersonaDAOInterface;
import com.sirelab.dao.interfacedao.PlanEstudiosDAOInterface;
import com.sirelab.entidades.Carrera;
import com.sirelab.entidades.Departamento;
import com.sirelab.entidades.Estudiante;
import com.sirelab.entidades.Facultad;
import com.sirelab.entidades.PlanEstudios;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author ANDRES PINEDA
 */
@Stateless
public class AdministrarEstudiantesBO implements AdministrarEstudiantesBOInterface {

    @EJB
    FacultadDAOInterface facultadDAO;
    @EJB
    CarreraDAOInterface carreraDAO;
    @EJB
    PlanEstudiosDAOInterface planEstudiosDAO;
    @EJB
    DepartamentoDAOInterface departamentoDAO;
    @EJB
    PersonaDAOInterface personaDAO;
    @EJB
    EstudianteDAOInterface estudianteDAO;

    //@Override
    public List<Departamento> obtenerListasDepartamentos() {
        try {
            List<Departamento> lista = departamentoDAO.consultarDepartamentos();
            return lista;
        } catch (Exception e) {
            System.out.println("Error GestionarLoginSistemaBO obtenerListasDepartamentos : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Facultad> obtenerListaFacultades() {
        try {
            List<Facultad> lista = facultadDAO.consultarFacultades();
            return lista;
        } catch (Exception e) {
            System.out.println("Error GestionarLoginSistemaBO obtenerListaFacultades : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Departamento> obtenerDepartamentosPorIDFacultad(BigInteger idFacultad) {
        try {
            List<Departamento> lista = departamentoDAO.buscarDepartamentosPorIDFacultad(idFacultad);
            return lista;
        } catch (Exception e) {
            System.out.println("Error GestionarLoginSistemaBO obtenerListasCarrerasPorDepartamento : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Carrera> obtenerListasCarrerasPorDepartamento(BigInteger idDepartamento) {
        try {
            List<Carrera> lista = carreraDAO.consultarCarrerasPorDepartamento(idDepartamento);
            return lista;
        } catch (Exception e) {
            System.out.println("Error GestionarLoginSistemaBO obtenerListasCarrerasPorDepartamento : " + e.toString());
            return null;
        }
    }

    @Override
    public List<PlanEstudios> obtenerListasPlanesEstudioPorCarrera(BigInteger idCarrera) {
        try {
            List<PlanEstudios> lista = planEstudiosDAO.consultarPlanesEstudiosPorCarrera(idCarrera);
            return lista;
        } catch (Exception e) {
            System.out.println("Error GestionarLoginSistemaBO obtenerListasPlanesEstudioPorCarrera : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Estudiante> consultarEstudiantesPorParametro(Map<String, String> filtros) {
        try {
            List<Estudiante> lista = estudianteDAO.buscarEstudiantesPorFiltrado(filtros);
            return lista;
        } catch (Exception e) {
            System.out.println("Error GestionarLoginSistemaBO consultarEstudiantesPorParametro : " + e.toString());
            return null;
        }
    }

    @Override
    public Estudiante obtenerEstudiantePorIDEstudiante(BigInteger idEstudiante) {
        try {
            Estudiante registro = estudianteDAO.buscarEstudiantePorID(idEstudiante);
            return registro;
        } catch (Exception e) {
            System.out.println("Error GestionarLoginSistemaBO obtenerEstudiantePorIDEstudiante : " + e.toString());
            return null;
        }
    }

    @Override
    public Estudiante obtenerEstudiantePorCorreoNumDocumento(String correo, String documento) {
        try {
            Estudiante registro = estudianteDAO.buscarEstudiantePorDocumentoYCorreo(correo, documento);
            return registro;
        } catch (Exception e) {
            System.out.println("Error GestionarLoginSistemaBO obtenerEstudiantePorCorreoNumDocumento : " + e.toString());
            return null;
        }
    }

    @Override
    public void actualizarInformacionEstudiante(Estudiante estudiante) {
        try {
            estudianteDAO.editarEstudiante(estudiante);
        } catch (Exception e) {
            System.out.println("Error GestionarLoginSistemaBO actualizarInformacionEstudiante : " + e.toString());
        }
    }
}
