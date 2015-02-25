package com.sirelab.dao;

import com.sirelab.dao.interfacedao.ProveedorDAOInterface;
import com.sirelab.entidades.Proveedor;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * @author ANDRES PINEDA
 */
@Stateless
public class ProveedorDAO implements ProveedorDAOInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
     */
    @PersistenceContext(unitName = "SIRELAB-UC-ejbPU")
    private EntityManager em;

    @Override
    public void crearProveedor(Proveedor proveedor) {
        try {
            em.persist(proveedor);
        } catch (Exception e) {
            System.out.println("Error crearProveedor ProveedorDAO : " + e.toString());
        }
    }

    @Override
    public void editarProveedor(Proveedor proveedor) {
        try {
            em.merge(proveedor);
        } catch (Exception e) {
            System.out.println("Error editarProveedor ProveedorDAO : " + e.toString());
        }
    }

    @Override
    public void eliminarProveedor(Proveedor proveedor) {
        try {
            em.remove(em.merge(proveedor));
        } catch (Exception e) {
            System.out.println("Error eliminarProveedor ProveedorDAO : " + e.toString());
        }
    }

    @Override
    public List<Proveedor> consultarProveedores() {
        try {
            em.clear();
            Query query = em.createQuery("SELECT p FROM Proveedor p");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Proveedor> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            System.out.println("Error consultarProveedores ProveedorDAO : " + e.toString());
            return null;
        }
    }

    @Override
    public Proveedor buscarProveedorPorID(BigInteger idRegistro) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT p FROM Proveedor p WHERE p.idproveedor=:idRegistro");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            query.setParameter("idRegistro", idRegistro);
            Proveedor registro = (Proveedor) query.getSingleResult();
            return registro;
        } catch (Exception e) {
            System.out.println("Error buscarProveedorPorID ProveedorDAO : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Proveedor> buscarProveedoresPorFiltrado(Map<String, String> filters) {
        try {
            final String alias = "a";
            final StringBuilder jpql = new StringBuilder();
            String jpql2;
            jpql.append("SELECT a FROM ").append(Proveedor.class.getSimpleName()).append(" " + alias);
            //
            jpql2 = adicionarFiltros(jpql.toString(), filters, alias);
            //
            System.out.println("jpql2.toString() : " + jpql2.toString());
            TypedQuery<Proveedor> tq = em.createQuery(jpql2.toString(), Proveedor.class);
            tq = asignarValores(tq, filters);
            return tq.getResultList();
        } catch (Exception e) {
            System.out.println("Error buscarProveedoresPorFiltrado ProveedorDAO : " + e.toString());
            return null;
        }
    }

    private String adicionarFiltros(String jpql, Map<String, String> filters, String alias) {
        final StringBuilder wheres = new StringBuilder();
        int camposFiltro = 0;
        if (null != filters && !filters.isEmpty()) {
            wheres.append(" WHERE ");
            for (Map.Entry<String, String> entry : filters.entrySet()) {
                if (null != entry.getValue() && !entry.getValue().isEmpty()) {
                    if (camposFiltro > 0) {
                        wheres.append(" AND ");
                    }
                    if ("parametroNIT".equals(entry.getKey())) {
                        wheres.append("UPPER(").append(alias)
                                .append(".nitproveedor")
                                .append(") Like :parametroNIT");
                        camposFiltro++;
                    }
                    if ("parametroNombre".equals(entry.getKey())) {
                        wheres.append("UPPER(").append(alias)
                                .append(".nombreproveedor")
                                .append(") Like :parametroNombre");
                        camposFiltro++;
                    }
                    if ("parametroDireccion".equals(entry.getKey())) {
                        wheres.append("UPPER(").append(alias)
                                .append(".direccionproveedor")
                                .append(") Like :parametroDireccion");
                        camposFiltro++;
                    }
                    if ("parametroTelefono".equals(entry.getKey())) {
                        wheres.append("UPPER(").append(alias)
                                .append(".telefonoproveedor")
                                .append(") Like :parametroTelefono");
                        camposFiltro++;
                    }
                }
            }
        }
        jpql = jpql + wheres /*+ " ORDER BY " + alias + ".id ASC"*/;
        System.out.println(jpql);
        if (jpql.trim()
                .endsWith("WHERE")) {
            jpql = jpql.replace("WHERE", "");
        }
        return jpql;
    }

    private TypedQuery<Proveedor> asignarValores(TypedQuery<Proveedor> tq, Map<String, String> filters) {
        for (Map.Entry<String, String> entry : filters.entrySet()) {
            if (null != entry.getValue() && !entry.getValue().isEmpty()) {
                if (("parametroNIT".equals(entry.getKey()))
                        || ("parametroNombre".equals(entry.getKey()))
                        || ("parametroDireccion".equals(entry.getKey()))
                        || ("parametroTelefono".equals(entry.getKey()))) {
                    //
                    tq.setParameter(entry.getKey(), "%" + entry.getValue().toUpperCase() + "%");
                }
            }
        }
        return tq;
    }

}
