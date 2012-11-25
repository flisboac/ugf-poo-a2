package br.ugf.poo.a2.modelo.alunos;

/**
 *
 * @author flisboac
 */
public enum Avaliacao {
    
    A1("A1", "aluno_a1"),
    A2("A2", "aluno_a2"),
    A3("A3", "aluno_a3");
    
    private String titulo;
    private String campo;

    private Avaliacao(String titulo, String campo) {
        this.titulo = titulo;
        this.campo = campo;
    }
    
    public String getTitulo() {
        return titulo;
    }

    public String getCampo() {
        return campo;
    }
    
}
