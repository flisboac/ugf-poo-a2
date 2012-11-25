package br.ugf.poo.a2.modelo.alunos;

/**
 *
 * @author flisboac
 */
public class EstatisticaTurma {
    
    private long qtdAlunos;
    private long qtdAlunosComMedia;
    private long qtdAlunosAprovados;
    private long qtdAlunosReprovados;
    private long qtdAlunosProvaFinal;
    private long qtdAlunosAcimaDaMedia;
    private long qtdAlunosAbaixoDaMedia;
    private Double media;

    public long getQtdAlunos() {
        return qtdAlunos;
    }

    public void setQtdAlunos(long qtdAlunos) {
        this.qtdAlunos = qtdAlunos;
    }

    public long getQtdAlunosAprovados() {
        return qtdAlunosAprovados;
    }

    public void setQtdAlunosAprovados(long qtdAlunosAprovados) {
        this.qtdAlunosAprovados = qtdAlunosAprovados;
    }

    public long getQtdAlunosReprovados() {
        return qtdAlunosReprovados;
    }

    public void setQtdAlunosReprovados(long qtdAlunosReprovados) {
        this.qtdAlunosReprovados = qtdAlunosReprovados;
    }

    public long getQtdAlunosAcimaDaMedia() {
        return qtdAlunosAcimaDaMedia;
    }

    public void setQtdAlunosAcimaDaMedia(long qtdAlunosAcimaDaMedia) {
        this.qtdAlunosAcimaDaMedia = qtdAlunosAcimaDaMedia;
    }

    public long getQtdAlunosAbaixoDaMedia() {
        return qtdAlunosAbaixoDaMedia;
    }

    public void setQtdAlunosAbaixoDaMedia(long qtdAlunosAbaixoDaMedia) {
        this.qtdAlunosAbaixoDaMedia = qtdAlunosAbaixoDaMedia;
    }

    public Double getMedia() {
        return media;
    }

    public void setMedia(Double media) {
        this.media = media;
    }

    public long getQtdAlunosComMedia() {
        return qtdAlunosComMedia;
    }

    public void setQtdAlunosComMedia(long qtdAlunosComMedia) {
        this.qtdAlunosComMedia = qtdAlunosComMedia;
    }

    public long getQtdAlunosProvaFinal() {
        return qtdAlunosProvaFinal;
    }

    public void setQtdAlunosProvaFinal(long qtdAlunosProvaFinal) {
        this.qtdAlunosProvaFinal = qtdAlunosProvaFinal;
    }
    
}
