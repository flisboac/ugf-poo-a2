package br.ugf.poo.a2.modelo.excecoes;

/**
 * Exceção que é lançada pelos Dao's da aplicação.
 *
 * @author flisboac
 */
public class ExcecaoDao extends Excecao {
    
    public ExcecaoDao() {
    }
    
    public ExcecaoDao(String msg) {
        super(msg);
    }
    
    public ExcecaoDao(Throwable ex) {
        super(ex);
    }
    
    public ExcecaoDao(String msg, Throwable ex) {
        super(msg, ex);
    }
}
