package view_RegistroCliente;

public class cls_Clientes {
    private String nombre;
    private boolean clienteHabitual;
    private int numPersonas;
    private String mesero;
    private String estancia;
    private int numMesa;

    // Constructor
    public cls_Clientes(String nombre, boolean clienteHabitual, int numPersonas,
                        String mesero, String estancia, int numMesa) {
        this.nombre = nombre;
        this.clienteHabitual = clienteHabitual;
        this.numPersonas = numPersonas;
        this.mesero = mesero;
        this.estancia = estancia;
        this.numMesa = numMesa;
    }

    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {
        this.nombre = nombre;}

    public boolean isClienteHabitual() {return clienteHabitual;}
    public void setClienteHabitual(boolean clienteHabitual) {
        this.clienteHabitual = clienteHabitual;}

    public int getNumPersonas() {return numPersonas;}
    public void setNumPersonas(int numPersonas) {
        this.numPersonas = numPersonas;}

    public String getMesero() {return mesero;}
    public void setMesero(String mesero) {
        this.mesero = mesero;}

    public String getEstancia() {return estancia;}
    public void setEstancia(String estancia) {
        this.estancia = estancia;}

    public int getNumMesa() {return numMesa;}
    public void setNumMesa(int numMesa) {
        this.numMesa = numMesa;}
}
