package com.sirelab.bo.universidad;

import com.sirelab.bo.interfacebo.GestionarFacultadesBOInterface;
import com.sirelab.dao.interfacedao.FacultadDAOInterface;
import com.sirelab.entidades.Facultad;
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
public class GestionarFacultadesBO implements GestionarFacultadesBOInterface {

    @EJB
    FacultadDAOInterface facultadDAO;

    @Override
    public List<Facultad> consultarFacultadesPorParametro(Map<String, String> filtros) {
        try {
            List<Facultad> lista = facultadDAO.buscarFacultadesPorFiltrado(filtros);
            return lista;
        } catch (Exception e) {
            System.out.println("Error GestionarFacultadBO consultarFacultadesPorParametro : " + e.toString());
            return null;
        }
    }

    @Override
    public void crearNuevaFacultad(Facultad facultad) {
        try {
            facultadDAO.crearFacultad(facultad);
        } catch (Exception e) {
            System.out.println("Error GestionarFacultadBO crearNuevaFacultad : " + e.toString());
        }
    }

    @Override
    public void modificarInformacionFacultad(Facultad facultad) {
        try {
            facultadDAO.editarFacultad(facultad);
        } catch (Exception e) {
            System.out.println("Error GestionarFacultadBO crearNuevaFacultad : " + e.toString());
        }
    }

    @Override
    public Facultad obtenerFacultadPorIDFacultad(BigInteger idFacultad) {
        try {
            Facultad registro = facultadDAO.buscarFacultadPorID(idFacultad);
            return registro;
        } catch (Exception e) {
            System.out.println("Error GestionarFacultadBO obtenerFacultadPorIDFacultad : " + e.toString());
            return null;
        }
    }

}
