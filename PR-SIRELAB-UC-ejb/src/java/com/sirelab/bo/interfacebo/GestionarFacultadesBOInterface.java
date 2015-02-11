/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sirelab.bo.interfacebo;

import com.sirelab.entidades.Facultad;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ANDRES PINEDA
 */
public interface GestionarFacultadesBOInterface {

    public List<Facultad> consultarFacultadesPorParametro(Map<String, String> filtros);

    public void crearNuevaFacultad(Facultad facultad);

    public void modificarInformacionFacultad(Facultad facultad);

    public Facultad obtenerFacultadPorIDFacultad(BigInteger idFacultad);
}
