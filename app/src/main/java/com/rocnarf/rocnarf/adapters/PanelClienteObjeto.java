package com.rocnarf.rocnarf.adapters;


public class PanelClienteObjeto {

    private String IdCliente = "";
    private String NombreCliente = "";
     private String Representante = "";
     private String Ciudad = "";
     private String Sector = "";

    public String getIdCliente() {
        return IdCliente;
    }
    public void setIdCliente(String IdCliente) {
        this.IdCliente = IdCliente;
    }
    public String getNombreCliente() {
        return NombreCliente;
    }
    public void setNombreCliente(String NombreCliente) {
        this.NombreCliente = NombreCliente;
    }


     public String getRepresentante() {
         return Representante;
     }
     public void setRepresentante(String Representante) {
         this.Representante = Representante;
     }

     public String getCiudad() {
         return Ciudad;
     }
     public void setCiudad(String Ciudad) {
         this.Ciudad = Ciudad;
     }

     public String getSector() {
         return Sector;
     }
     public void setSector(String Sector) {
         this.Sector = Sector;
     }

}

