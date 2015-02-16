package com.sirelab.entidades;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author ANDRES PINEDA
 */
@Entity
@Table(name = "areaprofundizacion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AreaProfundizacion.findAll", query = "SELECT a FROM AreaProfundizacion a"),
    @NamedQuery(name = "AreaProfundizacion.findByIdareaprofundizacion", query = "SELECT a FROM AreaProfundizacion a WHERE a.idareaprofundizacion = :idareaprofundizacion"),
    @NamedQuery(name = "AreaProfundizacion.findByNombrearea", query = "SELECT a FROM AreaProfundizacion a WHERE a.nombrearea = :nombrearea"),
    @NamedQuery(name = "AreaProfundizacion.findByCodigoarea", query = "SELECT a FROM AreaProfundizacion a WHERE a.codigoarea = :codigoarea")})
public class AreaProfundizacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idareaprofundizacion")
    private BigInteger idareaprofundizacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "nombrearea")
    private String nombrearea;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "codigoarea")
    private String codigoarea;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "areaprofundizacion")
    private Collection<SalaLaboratorio> salaLaboratorioCollection;
    @JoinColumn(name = "laboratorio", referencedColumnName = "idlaboratorio")
    @ManyToOne(optional = false)
    private Laboratorio laboratorio;

    public AreaProfundizacion() {
    }

    public AreaProfundizacion(BigInteger idareaprofundizacion) {
        this.idareaprofundizacion = idareaprofundizacion;
    }

    public AreaProfundizacion(BigInteger idareaprofundizacion, String nombrearea, String codigoarea) {
        this.idareaprofundizacion = idareaprofundizacion;
        this.nombrearea = nombrearea;
        this.codigoarea = codigoarea;
    }

    public BigInteger getIdareaprofundizacion() {
        return idareaprofundizacion;
    }

    public void setIdareaprofundizacion(BigInteger idareaprofundizacion) {
        this.idareaprofundizacion = idareaprofundizacion;
    }

    public String getNombrearea() {
        if (null != nombrearea) {
            return nombrearea.toUpperCase();
        }
        return nombrearea;
    }

    public void setNombrearea(String nombrearea) {
        this.nombrearea = nombrearea.toUpperCase();
    }

    public String getCodigoarea() {
        if (null != codigoarea) {
            return codigoarea.toUpperCase();
        }
        return codigoarea;
    }

    public void setCodigoarea(String codigoarea) {
        this.codigoarea = codigoarea.toUpperCase();
    }

    @XmlTransient
    public Collection<SalaLaboratorio> getSalaLaboratorioCollection() {
        return salaLaboratorioCollection;
    }

    public void setSalaLaboratorioCollection(Collection<SalaLaboratorio> salaLaboratorioCollection) {
        this.salaLaboratorioCollection = salaLaboratorioCollection;
    }

    public Laboratorio getLaboratorio() {
        return laboratorio;
    }

    public void setLaboratorio(Laboratorio laboratorio) {
        this.laboratorio = laboratorio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idareaprofundizacion != null ? idareaprofundizacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AreaProfundizacion)) {
            return false;
        }
        AreaProfundizacion other = (AreaProfundizacion) object;
        if ((this.idareaprofundizacion == null && other.idareaprofundizacion != null) || (this.idareaprofundizacion != null && !this.idareaprofundizacion.equals(other.idareaprofundizacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sirelab.entidades.AreaProfundizacion[ idareaprofundizacion=" + idareaprofundizacion + " ]";
    }

}
