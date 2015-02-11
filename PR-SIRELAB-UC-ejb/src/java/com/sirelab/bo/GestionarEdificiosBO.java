package com.sirelab.bo;

import com.sirelab.bo.interfacebo.GestionarEdificiosBOInterface;
import com.sirelab.dao.interfacedao.EdificioDAOInterface;
import com.sirelab.dao.interfacedao.SedeDAOInterface;
import com.sirelab.entidades.Edificio;
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
public class GestionarEdificiosBO implements GestionarEdificiosBOInterface {

    @EJB
    EdificioDAOInterface edificioDAO;
    @EJB
    SedeDAOInterface sedeDAO;

    //@Override
    public List<Sede> consultarSedesRegistradas() {
        try {
            List<Sede> lista = sedeDAO.consultarSedes();
            return lista;
        } catch (Exception e) {
            System.out.println("Error GestionarEdificioBO consultarSedesRegistradas : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Edificio> consultarEdificiosPorParametro(Map<String, String> filtros) {
        try {
            List<Edificio> lista = edificioDAO.buscarEdificiosPorFiltrado(filtros);
            return lista;
        } catch (Exception e) {
            System.out.println("Error GestionarEdificioBO consultarEdificiosPorParametro : " + e.toString());
            return null;
        }
    }

    @Override
    public void crearNuevaEdificio(Edificio edificio) {
        try {
            edificioDAO.crearEdificio(edificio);
        } catch (Exception e) {
            System.out.println("Error GestionarEdificioBO crearNuevaEdificio : " + e.toString());
        }
    }

    @Override
    public void modificarInformacionEdificio(Edificio edificio) {
        try {
            edificioDAO.editarEdificio(edificio);
        } catch (Exception e) {
            System.out.println("Error GestionarEdificioBO crearNuevaEdificio : " + e.toString());
        }
    }

    @Override
    public Edificio obtenerEdificioPorIDEdificio(BigInteger idEdificio) {
        try {
            Edificio registro = edificioDAO.buscarEdificioPorID(idEdificio);
            return registro;
        } catch (Exception e) {
            System.out.println("Error GestionarEdificioBO obtenerEdificioPorIDEdificio : " + e.toString());
            return null;
        }
    }
}
