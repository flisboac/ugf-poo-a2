package br.ugf.poo.a2.modelo.excecoes;

/**
 *
 * @author flisboac
 */
public class Excecao extends Exception {

	private static final long serialVersionUID = 3702614902619190862L;

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
