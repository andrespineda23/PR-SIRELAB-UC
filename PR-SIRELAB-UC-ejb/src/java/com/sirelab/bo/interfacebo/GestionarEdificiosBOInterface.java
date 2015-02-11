/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sirelab.bo.interfacebo;

import com.sirelab.entidades.Edificio;
import com.sirelab.entidades.Sede;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ANDRES PINEDA
 */
public interface GestionarEdificiosBOInterface {

    public List<Sede> consultarSedesRegistradas();

    public List<Edificio> consultarEdificiosPorParametro(Map<String, String> filtros);

    public void crearNuevaEdificio(Edificio edificio);

    public void modificarInformacionEdificio(Edificio edificio);

    public Edificio obtenerEdificioPorIDEdificio(BigInteger idEdificio);

}
