/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sirelab.dao.interfacedao;

import com.sirelab.entidades.Edificio;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ANDRES PINEDA
 */
public interface EdificioDAOInterface {

    public void crearEdificio(Edificio edificio);

    public void editarEdificio(Edificio edificio);

    public void eliminarEdificio(Edificio edificio);

    public List<Edificio> consultarEdificios();

    public Edificio buscarEdificioPorID(BigInteger idRegistro);

    public List<Edificio> buscarEdificiosPorFiltrado(Map<String, String> filters);

    public List<Edificio> buscarEdificiosPorIDSede(BigInteger idSede);
}
