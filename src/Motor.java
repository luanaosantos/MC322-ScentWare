public class Motor {

    private boolean ligado;

    public Motor() { 
        ligado = false;
     }

    public void ligar() { 
        this.ligado = true; 
    }
    public void desligar() { 
        this.ligado = false; 
    }
    public boolean estaLigado() {
        return ligado;
    }
}