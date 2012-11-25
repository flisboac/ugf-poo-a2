package br.ugf.poo.a2.modelo.excecoes;

/**
 *
 * @author flisboac
 */
public class Excecao extends Exception {
    
    public Excecao() {
    }
    
    public Excecao(String msg) {
        super(msg);
    }
    
    public Excecao(Throwable ex) {
        super(ex);
    }
    
    public Excecao(String msg, Throwable ex) {
        super(msg, ex);
    }
}
