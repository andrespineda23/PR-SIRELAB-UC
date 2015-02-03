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
@Table(name = "sede")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Sede.findAll", query = "SELECT s FROM Sede s"),
    @NamedQuery(name = "Sede.findByIdsede", query = "SELECT s FROM Sede s WHERE s.idsede = :idsede"),
    @NamedQuery(name = "Sede.findByNombresede", query = "SELECT s FROM Sede s WHERE s.nombresede = :nombresede"),
    @NamedQuery(name = "Sede.findByDireccionsede", query = "SELECT s FROM Sede s WHERE s.direccionsede = :direccionsede"),
    @NamedQuery(name = "Sede.findByTelefonosede", query = "SELECT s FROM Sede s WHERE s.telefonosede = :telefonosede")})
public class Sede implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idsede")
    private BigInteger idsede;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "nombresede")
    private String nombresede;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "direccionsede")
    private String direccionsede;
    @Size(max = 45)
    @Column(name = "telefonosede")
    private String telefonosede;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sede")
    private Collection<Edificio> edificioCollection;

    public Sede() {
    }

    public Sede(BigInteger idsede) {
        this.idsede = idsede;
    }

    public Sede(BigInteger idsede, String nombresede, String direccionsede) {
        this.idsede = idsede;
        this.nombresede = nombresede;
        this.direccionsede = direccionsede;
    }

    public BigInteger getIdsede() {
        return idsede;
    }

    public void setIdsede(BigInteger idsede) {
        this.idsede = idsede;
    }

    public String getNombresede() {
        return nombresede;
    }

    public void setNombresede(String nombresede) {
        this.nombresede = nombresede;
    }

    public String getDireccionsede() {
        return direccionsede;
    }

    public void setDireccionsede(String direccionsede) {
        this.direccionsede = direccionsede;
    }

    public String getTelefonosede() {
        return telefonosede;
    }

    public void setTelefonosede(String telefonosede) {
        this.telefonosede = telefonosede;
    }

    @XmlTransient
    public Collection<Edificio> getEdificioCollection() {
        return edificioCollection;
    }

    public void setEdificioCollection(Collection<Edificio> edificioCollection) {
        this.edificioCollection = edificioCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idsede != null ? idsede.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Sede)) {
            return false;
        }
        Sede other = (Sede) object;
        if ((this.idsede == null && other.idsede != null) || (this.idsede != null && !this.idsede.equals(other.idsede))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sirelab.entidades.Sede[ idsede=" + idsede + " ]";
    }

}
