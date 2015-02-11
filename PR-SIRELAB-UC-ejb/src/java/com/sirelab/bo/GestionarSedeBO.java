package com.sirelab.bo;

import com.sirelab.bo.interfacebo.GestionarSedeBOInterface;
import com.sirelab.dao.interfacedao.SedeDAOInterface;
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
public class GestionarSedeBO implements GestionarSedeBOInterface {

    @EJB
    SedeDAOInterface sedeDAO;

    @Override
    public List<Sede> consultarSedesPorParametro(Map<String, String> filtros) {
        try {
            List<Sede> lista = sedeDAO.buscarSedesPorFiltrado(filtros);
            return lista;
        } catch (Exception e) {
            System.out.println("Error GestionarSedeBO consultarSedesPorParametro : " + e.toString());
            return null;
        }
    }

    @Override
    public void crearNuevaSede(Sede sede) {
        try {
            sedeDAO.crearSede(sede);
        } catch (Exception e) {
            System.out.println("Error GestionarSedeBO crearNuevaSede : " + e.toString());
        }
    }

    @Override
    public void modificarInformacionSede(Sede sede) {
        try {
            sedeDAO.editarSede(sede);
        } catch (Exception e) {
            System.out.println("Error GestionarSedeBO crearNuevaSede : " + e.toString());
        }
    }

    @Override
    public Sede obtenerSedePorIDSede(BigInteger idSede) {
        try {
            Sede registro = sedeDAO.buscarSedePorID(idSede);
            return registro;
        } catch (Exception e) {
            System.out.println("Error GestionarSedeBO obtenerSedePorIDSede : " + e.toString());
            return null;
        }
    }
}
