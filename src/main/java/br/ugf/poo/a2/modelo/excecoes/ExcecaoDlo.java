package br.ugf.poo.a2.modelo.excecoes;

/**
 * Exceção que é lançada pelos Dlo's da aplicação.
 * 
 * @author flisboac
 */
public class ExcecaoDlo extends Excecao {
    
    public ExcecaoDlo() {
    }
    
    public ExcecaoDlo(String msg) {
        super(msg);
    }
    
    public ExcecaoDlo(Throwable ex) {
        super(ex);
    }
    
    public ExcecaoDlo(String msg, Throwable ex) {
        super(msg, ex);
    }
}
