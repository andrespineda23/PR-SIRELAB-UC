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
@Table(name = "proveedor")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Proveedor.findAll", query = "SELECT p FROM Proveedor p"),
    @NamedQuery(name = "Proveedor.findByIdproveedor", query = "SELECT p FROM Proveedor p WHERE p.idproveedor = :idproveedor"),
    @NamedQuery(name = "Proveedor.findByNitproveedor", query = "SELECT p FROM Proveedor p WHERE p.nitproveedor = :nitproveedor"),
    @NamedQuery(name = "Proveedor.findByNombreproveedor", query = "SELECT p FROM Proveedor p WHERE p.nombreproveedor = :nombreproveedor"),
    @NamedQuery(name = "Proveedor.findByDireccionproveedor", query = "SELECT p FROM Proveedor p WHERE p.direccionproveedor = :direccionproveedor"),
    @NamedQuery(name = "Proveedor.findByTelefonoproveedor", query = "SELECT p FROM Proveedor p WHERE p.telefonoproveedor = :telefonoproveedor"),
    @NamedQuery(name = "Proveedor.findByVendedorproveedor", query = "SELECT p FROM Proveedor p WHERE p.vendedorproveedor = :vendedorproveedor"),
    @NamedQuery(name = "Proveedor.findByTelefonovendedor", query = "SELECT p FROM Proveedor p WHERE p.telefonovendedor = :telefonovendedor")})
public class Proveedor implements Serializable {
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "proveedor")
    private Collection<EquipoElemento> equipoElementoCollection;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idproveedor")
    private BigInteger idproveedor;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "nitproveedor")
    private String nitproveedor;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "nombreproveedor")
    private String nombreproveedor;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "direccionproveedor")
    private String direccionproveedor;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "telefonoproveedor")
    private String telefonoproveedor;
    @Size(max = 45)
    @Column(name = "vendedorproveedor")
    private String vendedorproveedor;
    @Size(max = 45)
    @Column(name = "telefonovendedor")
    private String telefonovendedor;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "proveedor")
    private Collection<Insumo> insumoCollection;

    public Proveedor() {
    }

    public Proveedor(BigInteger idproveedor) {
        this.idproveedor = idproveedor;
    }

    public Proveedor(BigInteger idproveedor, String nitproveedor, String nombreproveedor, String direccionproveedor, String telefonoproveedor) {
        this.idproveedor = idproveedor;
        this.nitproveedor = nitproveedor;
        this.nombreproveedor = nombreproveedor;
        this.direccionproveedor = direccionproveedor;
        this.telefonoproveedor = telefonoproveedor;
    }

    public BigInteger getIdproveedor() {
        return idproveedor;
    }

    public void setIdproveedor(BigInteger idproveedor) {
        this.idproveedor = idproveedor;
    }

    public String getNitproveedor() {
        if (null != nitproveedor) {
            return nitproveedor.toUpperCase();
        }
        return nitproveedor;
    }

    public void setNitproveedor(String nitproveedor) {
        this.nitproveedor = nitproveedor.toUpperCase();
    }

    public String getNombreproveedor() {
        if (null != nombreproveedor) {
            return nombreproveedor.toUpperCase();
        }
        return nombreproveedor;
    }

    public void setNombreproveedor(String nombreproveedor) {
        this.nombreproveedor = nombreproveedor.toUpperCase();
    }

    public String getDireccionproveedor() {
        if (null != direccionproveedor) {
            return direccionproveedor.toUpperCase();
        }
        return direccionproveedor;
    }

    public void setDireccionproveedor(String direccionproveedor) {
        this.direccionproveedor = direccionproveedor.toUpperCase();
    }

    public String getTelefonoproveedor() {
        if (null != telefonoproveedor) {
            return telefonoproveedor.toUpperCase();
        }
        return telefonoproveedor;
    }

    public void setTelefonoproveedor(String telefonoproveedor) {
        this.telefonoproveedor = telefonoproveedor.toUpperCase();
    }

    public String getVendedorproveedor() {
        if (null != vendedorproveedor) {
            return vendedorproveedor.toUpperCase();
        }
        return vendedorproveedor;
    }

    public void setVendedorproveedor(String vendedorproveedor) {
        this.vendedorproveedor = vendedorproveedor.toUpperCase();
    }

    public String getTelefonovendedor() {
        if (null != telefonovendedor) {
            return telefonovendedor.toUpperCase();
        }
        return telefonovendedor;
    }

    public void setTelefonovendedor(String telefonovendedor) {
        this.telefonovendedor = telefonovendedor.toUpperCase();
    }

    @XmlTransient
    public Collection<Insumo> getInsumoCollection() {
        return insumoCollection;
    }

    public void setInsumoCollection(Collection<Insumo> insumoCollection) {
        this.insumoCollection = insumoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idproveedor != null ? idproveedor.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Proveedor)) {
            return false;
        }
        Proveedor other = (Proveedor) object;
        if ((this.idproveedor == null && other.idproveedor != null) || (this.idproveedor != null && !this.idproveedor.equals(other.idproveedor))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sirelab.entidades.Proveedor[ idproveedor=" + idproveedor + " ]";
    }

    @XmlTransient
    public Collection<EquipoElemento> getEquipoElementoCollection() {
        return equipoElementoCollection;
    }

    public void setEquipoElementoCollection(Collection<EquipoElemento> equipoElementoCollection) {
        this.equipoElementoCollection = equipoElementoCollection;
    }

}
