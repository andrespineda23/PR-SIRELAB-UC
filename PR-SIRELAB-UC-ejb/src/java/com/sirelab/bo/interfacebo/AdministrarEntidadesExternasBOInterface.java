package com.sirelab.bo.interfacebo;

import com.sirelab.entidades.EntidadExterna;
import com.sirelab.entidades.Persona;
import com.sirelab.entidades.Usuario;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ANDRES PINEDA
 */
public interface AdministrarEntidadesExternasBOInterface {

    public List<EntidadExterna> consultarEntidadesExternasPorParametro(Map<String, String> filtros);

    public EntidadExterna obtenerEntidadExternaPorIDEntidadExterna(BigInteger idEntidadExterna);

    public EntidadExterna obtenerEntidadExternaPorCorreoNumDocumento(String correo, String documento);

    public void actualizarInformacionEntidadExterna(EntidadExterna entidadExterna);

    public void almacenarNuevaEntidadExternaEnSistema(Usuario usuarioNuevo, Persona personaNuevo, EntidadExterna entidadNueva);

    public void actualizarInformacionPersona(Persona persona);

    public void actualizarInformacionUsuario(Usuario usuario);

}
