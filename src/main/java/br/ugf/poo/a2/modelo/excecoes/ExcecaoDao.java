package br.ugf.poo.a2.modelo.excecoes;

/**
 * Exceção que é lançada pelos Dao's da aplicação.
 *
 * @author flisboac
 */
public class ExcecaoDao extends Excecao {

	private static final long serialVersionUID = -3804968923585345783L;

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
