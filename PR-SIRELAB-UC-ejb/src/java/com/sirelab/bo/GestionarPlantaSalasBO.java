package com.sirelab.bo;

import com.sirelab.bo.interfacebo.GestionarPlantaSalasBOInterface;
import com.sirelab.dao.interfacedao.AreaProfundizacionDAOInterface;
import com.sirelab.dao.interfacedao.DepartamentoDAOInterface;
import com.sirelab.dao.interfacedao.EdificioDAOInterface;
import com.sirelab.dao.interfacedao.LaboratorioDAOInterface;
import com.sirelab.dao.interfacedao.SalaLaboratorioDAOInterface;
import com.sirelab.dao.interfacedao.SedeDAOInterface;
import com.sirelab.entidades.AreaProfundizacion;
import com.sirelab.entidades.Departamento;
import com.sirelab.entidades.Edificio;
import com.sirelab.entidades.Laboratorio;
import com.sirelab.entidades.SalaLaboratorio;
import com.sirelab.entidades.Sede;
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
public class GestionarPlantaSalasBO implements GestionarPlantaSalasBOInterface {
    
    @EJB
    DepartamentoDAOInterface departamentoDAO;
    @EJB
    LaboratorioDAOInterface laboratorioDAO;
    @EJB
    AreaProfundizacionDAOInterface areaProfundizacionDAO;
    @EJB
    SalaLaboratorioDAOInterface salaLaboratorioDAO;
    @EJB
    EdificioDAOInterface edificioDAO;
    @EJB
    SedeDAOInterface sedeDAO;
    
    @Override
    public List<Departamento> consultarDepartamentosRegistrados() {
        try {
            List<Departamento> lista = departamentoDAO.consultarDepartamentos();
            return lista;
        } catch (Exception e) {
            System.out.println("Error GestionarPlantaSalasBO consultarDepartamentosRegistrados : " + e.toString());
            return null;
        }
    }
    
    @Override
    public List<Laboratorio> consultarLaboratoriosPorIDDepartamento(BigInteger departamento) {
        try {
            List<Laboratorio> lista = laboratorioDAO.buscarLaboratorioPorIDDepartamento(departamento);
            return lista;
        } catch (Exception e) {
            System.out.println("Error GestionarPlantaSalasBO consultarLaboratoriosPorIDDepartamento : " + e.toString());
            return null;
        }
    }
    
    @Override
    public List<AreaProfundizacion> consultarAreasProfundizacionPorIDLaboratorio(BigInteger laboratorio) {
        try {
            List<AreaProfundizacion> lista = areaProfundizacionDAO.buscarAreaProfundizacionPorIDLaboratorio(laboratorio);
            return lista;
        } catch (Exception e) {
            System.out.println("Error GestionarPlantaSalasBO consultarAreasProfundizacionPorIDLaboratorio : " + e.toString());
            return null;
        }
    }
    
    @Override
    public List<Sede> consultarSedesRegistradas() {
        try {
            List<Sede> lista = sedeDAO.consultarSedes();
            return lista;
        } catch (Exception e) {
            System.out.println("Error GestionarPlantaSalasBO consultarSedesRegistradas : " + e.toString());
            return null;
        }
    }
    
    @Override
    public List<Edificio> consultarEdificiosPorIDSede(BigInteger sede) {
        try {
            List<Edificio> lista = edificioDAO.buscarEdificiosPorIDSede(sede);
            return lista;
        } catch (Exception e) {
            System.out.println("Error GestionarPlantaSalasBO consultarEdificiosPorIDSede : " + e.toString());
            return null;
        }
    }
    
    @Override
    public List<SalaLaboratorio> consultarSalasLaboratoriosPorParametro(Map<String, String> filtros) {
        try {
            List<SalaLaboratorio> lista = salaLaboratorioDAO.buscarSalasLaboratoriosPorFiltrado(filtros);
            return lista;
        } catch (Exception e) {
            System.out.println("Error GestionarPlantaSalasBO consultarSalasLaboratoriosPorParametro : " + e.toString());
            return null;
        }
    }
    
    @Override
    public void crearNuevaSalaLaboratorio(SalaLaboratorio salaLaboratorio) {
        try {
            salaLaboratorioDAO.crearSalaLaboratorio(salaLaboratorio);
        } catch (Exception e) {
            System.out.println("Error GestionarPlantaSalasBO crearNuevaSalaLaboratorio : " + e.toString());
        }
    }
    
    @Override
    public void modificarInformacionSalaLaboratorio(SalaLaboratorio salaLaboratorio) {
        try {
            salaLaboratorioDAO.editarSalaLaboratorio(salaLaboratorio);
        } catch (Exception e) {
            System.out.println("Error GestionarPlantaSalasBO modificarInformacionSalaLaboratorio : " + e.toString());
        }
    }
    
    @Override
    public SalaLaboratorio obtenerSalaLaboratorioPorIDSalaLaboratorio(BigInteger idSalaLaboratorio) {
        try {
            SalaLaboratorio registro = salaLaboratorioDAO.buscarSalaLaboratorioPorID(idSalaLaboratorio);
            return registro;
        } catch (Exception e) {
            System.out.println("Error GestionarPlantaSalasBO obtenerSalaLaboratorioPorIDSalaLaboratorio : " + e.toString());
            return null;
        }
    }
    
}
