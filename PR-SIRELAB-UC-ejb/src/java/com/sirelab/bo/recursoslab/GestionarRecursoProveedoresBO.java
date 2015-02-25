package com.sirelab.bo.recursoslab;

import com.sirelab.bo.interfacebo.GestionarRecursoProveedoresBOInterface;
import com.sirelab.dao.interfacedao.ProveedorDAOInterface;
import com.sirelab.entidades.Proveedor;
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
public class GestionarRecursoProveedoresBO implements GestionarRecursoProveedoresBOInterface {

    @EJB
    ProveedorDAOInterface proveedorDAO;

    @Override
    public List<Proveedor> consultarProveedoresPorParametro(Map<String, String> filtros) {
        try {
            List<Proveedor> lista = proveedorDAO.buscarProveedoresPorFiltrado(filtros);
            return lista;
        } catch (Exception e) {
            System.out.println("Error GestionarRecursoProveedoresBO consultarProveedoresPorParametro : " + e.toString());
            return null;
        }
    }

    @Override
    public void crearNuevoProveedor(Proveedor proveedor) {
        try {
            proveedorDAO.crearProveedor(proveedor);
        } catch (Exception e) {
            System.out.println("Error GestionarRecursoProveedoresBO crearNuevoProveedor : " + e.toString());
        }
    }

    @Override
    public void modificarInformacionProveedor(Proveedor proveedor) {
        try {
            proveedorDAO.editarProveedor(proveedor);
        } catch (Exception e) {
            System.out.println("Error GestionarRecursoProveedoresBO modificarInformacionProveedor : " + e.toString());
        }
    }

    @Override
    public Proveedor obtenerProveedorPorIDProveedor(BigInteger idProveedor) {
        try {
            Proveedor registro = proveedorDAO.buscarProveedorPorID(idProveedor);
            return registro;
        } catch (Exception e) {
            System.out.println("Error GestionarRecursoProveedoresBO obtenerProveedorPorIDProveedor : " + e.toString());
            return null;
        }
    }

}
