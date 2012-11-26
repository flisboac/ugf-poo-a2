package br.ugf.poo.a2.modelo.alunos;

import java.util.List;

import br.ugf.poo.a2.modelo.excecoes.ExcecaoDao;
import br.ugf.poo.a2.modelo.excecoes.ExcecaoDlo;
import br.ugf.poo.a2.modelo.excecoes.ExcecaoPersistenciaDlo;
import br.ugf.poo.a2.modelo.excecoes.ExcecaoValidacaoDlo;

/**
 *
 * @author flisboac
 */
public class AlunoDloImpl implements AlunoDlo {

    private AlunoDao dao;
    private ValidadorAluno validador;

    public AlunoDloImpl() {
        this(new AlunoDaoImpl(), new ValidadorAluno());
    }

    public AlunoDloImpl(AlunoDao dao, ValidadorAluno validador) {
        this.dao = dao;
        this.validador = validador;
    }

    protected AlunoDao getDao() {

        AlunoDao retorno = this.dao;

        if (retorno == null) {
            retorno = new AlunoDaoImpl();
        }

        return retorno;
    }

    protected ValidadorAluno getValidador() {

        ValidadorAluno retorno = this.validador;

        if (retorno == null) {
            retorno = new ValidadorAluno();
        }

        return retorno;
    }

    // [ NEGÓCIO ] =============================================================
    @Override
    public void calcularSituacao(Aluno aluno) throws ExcecaoDlo {

        int qtdAvaliacoes = 0;
        Double maior = null;
        Double menor = null;

        if (aluno.getNotaA1() != null) {
            qtdAvaliacoes++;
            maior = aluno.getNotaA1();
        }

        if (aluno.getNotaA2() != null) {
            qtdAvaliacoes++;

            if (maior == null || aluno.getNotaA2().compareTo(maior) > 0) {
                menor = maior;
                maior = aluno.getNotaA2();

            } else if (menor == null || aluno.getNotaA2().compareTo(menor) > 0) {
                menor = aluno.getNotaA2();
            }
        }

        if (aluno.getNotaA3() != null) {
            qtdAvaliacoes++;

            if (maior == null || aluno.getNotaA3().compareTo(maior) > 0) {
                menor = maior;
                maior = aluno.getNotaA3();

            } else if (menor == null || aluno.getNotaA3().compareTo(menor) > 0) {
                menor = aluno.getNotaA3();
            }
        }

        if (qtdAvaliacoes > 1) {

            Double media = (maior + menor) / 2;
            int comparacao = media.compareTo(Aluno.Media);
            aluno.setMedia(media);

            if (comparacao >= 0) {
                aluno.setSituacao(SituacaoAluno.Aprovado);

            } else {

                if (aluno.getNotaA3() == null) {
                    aluno.setSituacao(SituacaoAluno.ProvaFinal);

                } else {
                    aluno.setSituacao(SituacaoAluno.Reprovado);
                }
            }

        } else {
            aluno.setSituacao(SituacaoAluno.Nenhuma);
            aluno.setMedia(null);
        }

    }

    // [ DAO ] =================================================================
    @Override
    public EstatisticaTurma gerarEstatisticaTurma() throws ExcecaoDlo {

        EstatisticaTurma retorno = new EstatisticaTurma();
        List<Aluno> alunos = listar();
        long totalAlunosComMedia = 0;
        Double mediaTurma = 0.0;
        long totalAlunosAcima = 0;
        long totalAlunosAbaixo = 0;
        long totalAlunosAprovados = 0;
        long totalAlunosReprovados = 0;
        long totalAlunosProvaFinal = 0;

        // Calculando a média da turma
        for (Aluno aluno : alunos) {

            if (aluno.getMedia() != null) {
                totalAlunosComMedia++;
                mediaTurma += aluno.getMedia();
            }
        }

        mediaTurma /= totalAlunosComMedia;

        // Calculando alunos acima/abaixo da média
        for (Aluno aluno : alunos) {

            if (aluno.getMedia() != null) {
                if (mediaTurma.compareTo(aluno.getMedia()) >= 0) {
                    totalAlunosAcima++;

                } else {
                    totalAlunosAbaixo++;
                }
            }

            if (aluno.getSituacao() == SituacaoAluno.Aprovado) {
                totalAlunosAprovados++;
            }

            if (aluno.getSituacao() == SituacaoAluno.Reprovado) {
                totalAlunosReprovados++;
            }

            if (aluno.getSituacao() == SituacaoAluno.ProvaFinal) {
                totalAlunosProvaFinal++;
            }
        }

        retorno.setQtdAlunos(alunos.size());
        retorno.setMedia(mediaTurma);
        retorno.setQtdAlunosComMedia(totalAlunosComMedia);
        retorno.setQtdAlunosAcimaDaMedia(totalAlunosAcima);
        retorno.setQtdAlunosAbaixoDaMedia(totalAlunosAbaixo);
        retorno.setQtdAlunosAprovados(totalAlunosAprovados);
        retorno.setQtdAlunosReprovados(totalAlunosReprovados);
        retorno.setQtdAlunosProvaFinal(totalAlunosProvaFinal);

        return retorno;
    }

    @Override
    public void definirNotasPorAvaliacao(Avaliacao avaliacao, Double nota) throws ExcecaoDlo {

        if (avaliacao == null) {
            throw new ExcecaoValidacaoDlo("Nenhuma avaliação passada para alteração.");
        }

        if (nota == null) {
            throw new ExcecaoValidacaoDlo("Nenhuma nota passada para alteração.");
        }

        if (nota.compareTo(Aluno.NotaMaxima) > 0 || nota.compareTo(Aluno.NotaMinima) < 0) {
            throw new ExcecaoValidacaoDlo("Nota excede os limites permitidos.");
        }

        try {
            getDao().definirNotasPorAvaliacao(avaliacao, nota);

        } catch (ExcecaoDao ex) {
            throw new ExcecaoPersistenciaDlo("Erro lançado pela camada de persitência"
                    + " ao definir as avaliações '" + avaliacao.getTitulo() + "'"
                    + " de todos os alunos com a nota '" + nota + "'.", ex);
        }

        List<Aluno> alunos = listar();

        for (Aluno aluno : alunos) {

            calcularSituacao(aluno);
            alterar(aluno);
        }
    }

    @Override
    public void alterar(Aluno aluno) throws ExcecaoDlo {

        getValidador().validarParaAlteracao(aluno);

        try {
            getDao().alterar(aluno);

        } catch (ExcecaoDao ex) {
            throw new ExcecaoPersistenciaDlo("Erro lançado pela camada de persistência ao alterar aluno.", ex);
        }
    }

    @Override
    public void excluir(Aluno aluno) throws ExcecaoDlo {

        getValidador().validarParaConsulta(aluno);

        try {
            getDao().excluir(aluno);

        } catch (ExcecaoDao ex) {
            throw new ExcecaoPersistenciaDlo("Erro lançado pela camada de persistência ao excluir aluno.", ex);
        }
    }

    @Override
    public void excluirPorId(Long id) throws ExcecaoDlo {

        getValidador().validarParaConsulta(id);

        try {
            getDao().excluirPorId(id);

        } catch (ExcecaoDao ex) {
            throw new ExcecaoPersistenciaDlo("Erro lançado pela camada de persistência ao alterar aluno com ID + " + id + ".", ex);
        }
    }

    @Override
    public boolean existe(Aluno aluno) throws ExcecaoDlo {

        boolean retorno;

        getValidador().validarParaConsulta(aluno);

        try {
            retorno = getDao().existe(aluno);

        } catch (ExcecaoDao ex) {
            throw new ExcecaoPersistenciaDlo("Erro lançado pela camada de persistência ao verificar a existência de um aluno.", ex);
        }

        return retorno;
    }

    @Override
    public boolean existePorId(Long id) throws ExcecaoDlo {

        boolean retorno;

        getValidador().validarParaConsulta(id);

        try {
            retorno = getDao().existePorId(id);

        } catch (ExcecaoDao ex) {
            throw new ExcecaoPersistenciaDlo("Erro lançado pela camada de persistência ao verificar a existência de um aluno com ID " + id + ".", ex);
        }

        return retorno;
    }

    @Override
    public Long inserir(Aluno aluno) throws ExcecaoDlo {

        Long retorno = null;

        getValidador().validarParaInsercao(aluno);

        try {
            retorno = getDao().inserir(aluno);

        } catch (ExcecaoDao ex) {
            throw new ExcecaoPersistenciaDlo("Erro lançado pela camada de persistência ao inserir aluno.", ex);
        }

        if (retorno == null) {
            throw new ExcecaoPersistenciaDlo("Valor nulo retornado pela camada de persistência.");
        }

        return retorno;
    }

    @Override
    public List<Aluno> listar() throws ExcecaoDlo {

        List<Aluno> retorno = null;

        try {
            retorno = getDao().listar();

        } catch (ExcecaoDao ex) {
            throw new ExcecaoPersistenciaDlo("Erro lançado pela camada de persistência ao listar alunos.", ex);
        }

        if (retorno == null) {
            throw new ExcecaoPersistenciaDlo("Valor nulo retornado pela camada de persistência.");
        }

        return retorno;
    }

    @Override
    public List<Aluno> listarPorParteDoNome(String parteDoNome) throws ExcecaoDlo {

        List<Aluno> retorno = null;

        if (parteDoNome == null || parteDoNome.isEmpty()) {
            throw new ExcecaoValidacaoDlo("Uma parte do nome deve ser passada para ser pesquisada.");
        }

        try {
            retorno = getDao().listarPorParteDoNome(parteDoNome);

        } catch (ExcecaoDao ex) {
            throw new ExcecaoPersistenciaDlo("Erro lançado ao listar alunos por parte de seus nomes, com parte: '" + parteDoNome + "'.", ex);
        }

        if (retorno == null) {
            throw new ExcecaoPersistenciaDlo("Valor nulo retornado pela camada de persistência.");
        }

        return retorno;
    }

    @Override
    public List<Aluno> listarPorSituacao(SituacaoAluno situacao) throws ExcecaoDlo {

        List<Aluno> retorno = null;

        if (situacao == null) {
            throw new ExcecaoValidacaoDlo("Situação não pode ser nula.");
        }

        try {
            retorno = getDao().listarPorSituacao(situacao);

        } catch (ExcecaoDao ex) {
            throw new ExcecaoPersistenciaDlo("Erro lançado pela camada de persistência ao listar alunos com a situação '" + situacao.getTitulo() + "'.", ex);
        }

        if (retorno == null) {
            throw new ExcecaoPersistenciaDlo("Valor nulo retornado pela camada de persistência.");
        }

        return retorno;
    }

    @Override
    public Aluno obter(Aluno aluno) throws ExcecaoDlo {

        Aluno retorno = null;

        getValidador().validarParaConsulta(aluno);

        try {
            retorno = getDao().obter(aluno);

        } catch (ExcecaoDao ex) {
            throw new ExcecaoPersistenciaDlo("Erro lançado pela camada de persistência ao obter aluno.", ex);
        }

        return retorno;
    }

    @Override
    public Aluno obterPorId(Long id) throws ExcecaoDlo {

        Aluno retorno = null;

        getValidador().validarParaConsulta(id);

        try {
            retorno = getDao().obterPorId(id);

        } catch (ExcecaoDao ex) {
            throw new ExcecaoPersistenciaDlo("Erro lançado pela camada de persistência ao obter aluno com ID " + id + ".", ex);
        }

        return retorno;
    }
}
