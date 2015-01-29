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
@Table(name = "facultad")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Facultad.findAll", query = "SELECT f FROM Facultad f"),
    @NamedQuery(name = "Facultad.findByIdfacultad", query = "SELECT f FROM Facultad f WHERE f.idfacultad = :idfacultad"),
    @NamedQuery(name = "Facultad.findByCodigofacultad", query = "SELECT f FROM Facultad f WHERE f.codigofacultad = :codigofacultad"),
    @NamedQuery(name = "Facultad.findByNombrefacultad", query = "SELECT f FROM Facultad f WHERE f.nombrefacultad = :nombrefacultad")})
public class Facultad implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idfacultad")
    private BigInteger idfacultad;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "codigofacultad")
    private String codigofacultad;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "nombrefacultad")
    private String nombrefacultad;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "facultad")
    private Collection<Departamento> departamentoCollection;

    public Facultad() {
    }

    public Facultad(BigInteger idfacultad) {
        this.idfacultad = idfacultad;
    }

    public Facultad(BigInteger idfacultad, String codigofacultad, String nombrefacultad) {
        this.idfacultad = idfacultad;
        this.codigofacultad = codigofacultad;
        this.nombrefacultad = nombrefacultad;
    }

    public BigInteger getIdfacultad() {
        return idfacultad;
    }

    public void setIdfacultad(BigInteger idfacultad) {
        this.idfacultad = idfacultad;
    }

    public String getCodigofacultad() {
        return codigofacultad;
    }

    public void setCodigofacultad(String codigofacultad) {
        this.codigofacultad = codigofacultad;
    }

    public String getNombrefacultad() {
        return nombrefacultad;
    }

    public void setNombrefacultad(String nombrefacultad) {
        this.nombrefacultad = nombrefacultad;
    }

    @XmlTransient
    public Collection<Departamento> getDepartamentoCollection() {
        return departamentoCollection;
    }

    public void setDepartamentoCollection(Collection<Departamento> departamentoCollection) {
        this.departamentoCollection = departamentoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idfacultad != null ? idfacultad.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Facultad)) {
            return false;
        }
        Facultad other = (Facultad) object;
        if ((this.idfacultad == null && other.idfacultad != null) || (this.idfacultad != null && !this.idfacultad.equals(other.idfacultad))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sirelab.entidades.Facultad[ idfacultad=" + idfacultad + " ]";
    }
    
}
