/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
@Table(name = "estadoequipo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EstadoEquipo.findAll", query = "SELECT e FROM EstadoEquipo e"),
    @NamedQuery(name = "EstadoEquipo.findByIdestadoequipo", query = "SELECT e FROM EstadoEquipo e WHERE e.idestadoequipo = :idestadoequipo"),
    @NamedQuery(name = "EstadoEquipo.findByNombreestadoequipo", query = "SELECT e FROM EstadoEquipo e WHERE e.nombreestadoequipo = :nombreestadoequipo")})
public class EstadoEquipo implements Serializable {
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estadoequipo")
    private Collection<EquipoElemento> equipoElementoCollection;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idestadoequipo")
    private BigInteger idestadoequipo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "nombreestadoequipo")
    private String nombreestadoequipo;

    public EstadoEquipo() {
    }

    public EstadoEquipo(BigInteger idestadoequipo) {
        this.idestadoequipo = idestadoequipo;
    }

    public EstadoEquipo(BigInteger idestadoequipo, String nombreestadoequipo) {
        this.idestadoequipo = idestadoequipo;
        this.nombreestadoequipo = nombreestadoequipo;
    }

    public BigInteger getIdestadoequipo() {
        return idestadoequipo;
    }

    public void setIdestadoequipo(BigInteger idestadoequipo) {
        this.idestadoequipo = idestadoequipo;
    }

    public String getNombreestadoequipo() {
        if (null != nombreestadoequipo) {
            return nombreestadoequipo.toUpperCase();
        }
        return nombreestadoequipo;
    }

    public void setNombreestadoequipo(String nombreestadoequipo) {
        this.nombreestadoequipo = nombreestadoequipo.toUpperCase();
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idestadoequipo != null ? idestadoequipo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EstadoEquipo)) {
            return false;
        }
        EstadoEquipo other = (EstadoEquipo) object;
        if ((this.idestadoequipo == null && other.idestadoequipo != null) || (this.idestadoequipo != null && !this.idestadoequipo.equals(other.idestadoequipo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sirelab.entidades.EstadoEquipo[ idestadoequipo=" + idestadoequipo + " ]";
    }

    @XmlTransient
    public Collection<EquipoElemento> getEquipoElementoCollection() {
        return equipoElementoCollection;
    }

    public void setEquipoElementoCollection(Collection<EquipoElemento> equipoElementoCollection) {
        this.equipoElementoCollection = equipoElementoCollection;
    }

}
