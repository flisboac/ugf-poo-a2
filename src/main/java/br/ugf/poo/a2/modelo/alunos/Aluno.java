package br.ugf.poo.a2.modelo.alunos;

/**
 * Classe de entidade representando um aluno.
 * 
 * <p>Como entidade, a <em>identidade</em> de um aluno é dada apenas pela sua 
 * {@link #matricula}. A identidade é o equivalente ao "unique" do banco de
 * dados, mas em termos da orientação a objetos. A identidade dita a forma
 * como os alunos são comparados e/ou ordenados (veja {@link #equals} e
 * {@link #hashCode}.</p>
 * 
 * <p>O {@link #id} do aluno é a sua <em>identificação</em>, que em termos de
 * banco de dados, é sua <em>chave primária</em>. A identificação não
 * deverá ser necessariamente igual à identidade.</p>
 * 
 * @author flisboac
 */
public class Aluno {
    
    /**
     * Média a ser atingida pelos alunos para aprovação.
     */
    public static final double Media = 6.0;
    
    /**
     * Nota mínima que pode ser atribuída às avaliações.
     */
    public static final double NotaMinima = 0.0;
    
    /**
     * Nota máxima que pode ser atribuída às avaliações.
     */
    public static final double NotaMaxima = 10.0;
    
    /**
     * O ID da entidade, para recuperação mais rápida no banco de dados.
     */
    private Long id;
    
    /**
     * A matrícula do aluno, apenas dígitos.
     */
    private String matricula;
    
    /**
     * A identidade de um aluno, apenas dígitos.
     */
    private String rg;
    
    /** 
     * O nome do aluno.
     */
    private String nome;
    
    /**
     * A situação atual, ou final, do aluno.
     */
    private SituacaoAluno situacao = SituacaoAluno.Nenhuma;
    
    /**
     * Nota da primeira avaliação.
     */
    private Double notaA1;
    
    /**
     * Nota da segunda avaliação.
     */
    private Double notaA2;
    
    /**
     * Nota da terceira avaliação, se for necessária.
     */
    private Double notaA3;
    
    /**
     * Média final do aluno.
     * Calculado apenas a partir das notas já lançadas.
     */
    private Double media;
    
    
    // [ GETTER | SETTER ] =====================================================

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String identidade) {
        this.rg = identidade;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public SituacaoAluno getSituacao() {
        return situacao;
    }

    public void setSituacao(SituacaoAluno situacao) {
        this.situacao = situacao;
    }

    public Double getNotaA1() {
        return notaA1;
    }

    public void setNotaA1(Double notaA1) {
        this.notaA1 = notaA1;
    }

    public Double getNotaA2() {
        return notaA2;
    }

    public void setNotaA2(Double notaA2) {
        this.notaA2 = notaA2;
    }

    public Double getNotaA3() {
        return notaA3;
    }

    public void setNotaA3(Double notaA3) {
        this.notaA3 = notaA3;
    }

    public Double getMedia() {
        return media;
    }

    public void setMedia(Double media) {
        this.media = media;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    // [ EQUALS | HASHCODE ] ===================================================

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + (this.matricula != null ? this.matricula.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Aluno other = (Aluno) obj;
        if ((this.matricula == null) ? (other.matricula != null) : !this.matricula.equals(other.matricula)) {
            return false;
        }
        return true;
    }
    
    // [ UTIL ] ================================================================

    @Override
    public String toString() {
        return "Aluno{" 
                + "id=" + id 
                + ", matricula=" + matricula 
                + ", rg=" + rg 
                + ", nome=" + nome 
                + ", situacao=" + situacao 
                + ", notaA1=" + notaA1 
                + ", notaA2=" + notaA2 
                + ", notaA3=" + notaA3 
                + ", media=" + media 
                + '}';
    }
    
}
