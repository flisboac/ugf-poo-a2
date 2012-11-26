/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ugf.poo.a2.modelo.excecoes;

/**
 * Especialização de {@link ExcecaoDlo}, usada para abstrair exceções dos DAO's.
 *
 * @author flisboac
 */
public class ExcecaoPersistenciaDlo extends ExcecaoDlo {

	private static final long serialVersionUID = 3089565777284312316L;

	public ExcecaoPersistenciaDlo() {
    }

    public ExcecaoPersistenciaDlo(String msg) {
        super(msg);
    }

    public ExcecaoPersistenciaDlo(Throwable ex) {
        super(ex);
    }

    public ExcecaoPersistenciaDlo(String msg, Throwable ex) {
        super(msg, ex);
    }
}
