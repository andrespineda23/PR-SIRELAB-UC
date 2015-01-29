package com.sirelab.dao.interfacedao;

import com.sirelab.entidades.Persona;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author ANDRES PINEDA
 */
public interface PersonaDAOInterface {

    public void crearPersona(Persona persona);

    public void editarPersona(Persona persona);

    public void eliminarPersona(Persona persona);

    public List<Persona> consultarPersonas();

    public Persona buscarPersonaPorID(BigInteger idRegistro);

    public Persona obtenerUltimaPersonaRegistrada();

    public Persona buscarPersonaPorCorreoYNumeroIdentificacion(String correo, String identificacion);

    public Persona obtenerPersonaLoginUserPassword(String usuario, String password);
}
