/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sirelab.dao.interfacedao;

import com.sirelab.entidades.TipoActivo;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author ANDRES PINEDA
 */
public interface TipoActivoDAOInterface {

    public void crearTipoActivo(TipoActivo tipoActivo);

    public void editarTipoActivo(TipoActivo tipoActivo);

    public void eliminarTipoActivo(TipoActivo tipoActivo);

    public List<TipoActivo> consultarTiposActivos();

    public TipoActivo buscarTipoActivoPorID(BigInteger idRegistro);

    public TipoActivo buscarTipoActivoPorNombre(String nombreTipoActivo);

}
