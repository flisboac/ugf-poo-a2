package br.ugf.poo.a2.modelo.alunos;

/**
 *
 * @author flisboac
 */
public enum SituacaoAluno {
    
    Nenhuma("Nenhuma", 0, false),
    Aprovado("Aprovado", 1, true),
    Reprovado("Reprovado", 2, true),
    ProvaFinal("Prova Final", 3, false);
    
    private String titulo;
    private int codigo;
    private boolean terminal;

    private SituacaoAluno(String titulo, int codigo, boolean terminal) {
        this.titulo = titulo;
        this.codigo = codigo;
        this.terminal = terminal;
    }

    public String getTitulo() {
        return titulo;
    }

    public int getCodigo() {
        return codigo;
    }

    public boolean isTerminal() {
        return terminal;
    }
    
    public static SituacaoAluno buscarPorCodigo(int codigo) {
        
        SituacaoAluno retorno = null;
        
        for (SituacaoAluno situacaoAluno : values()) {
            if (situacaoAluno.getCodigo() == codigo) {
                retorno = situacaoAluno;
                break;
            }
        }
        
        return retorno;
    }
}
