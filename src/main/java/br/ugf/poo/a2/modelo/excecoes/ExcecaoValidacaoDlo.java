/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ugf.poo.a2.modelo.excecoes;

/**
 * Especialização de {@link ExcecaoDlo}, lançada em operações de validação.
 * 
 * @author flisboac
 */
public class ExcecaoValidacaoDlo extends ExcecaoDlo {
    
    public ExcecaoValidacaoDlo() {
    }
    
    public ExcecaoValidacaoDlo(String msg) {
        super(msg);
    }
    
    public ExcecaoValidacaoDlo(Throwable ex) {
        super(ex);
    }
    
    public ExcecaoValidacaoDlo(String msg, Throwable ex) {
        super(msg, ex);
    }
}
