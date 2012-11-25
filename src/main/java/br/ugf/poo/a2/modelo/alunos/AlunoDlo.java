package br.ugf.poo.a2.modelo.alunos;

import br.ugf.poo.a2.modelo.excecoes.ExcecaoDlo;
import java.util.List;

/**
 * DLO para a entidade {@code Aluno}.
 * 
 * @author flisboac
 * @see Aluno
 */
public interface AlunoDlo {

    void alterar(Aluno aluno) throws ExcecaoDlo;
    
    void calcularSituacao(Aluno aluno) throws ExcecaoDlo;
    
    void definirNotasPorAvaliacao(Avaliacao avaliacao, Double nota) throws ExcecaoDlo;

    void excluir(Aluno aluno) throws ExcecaoDlo;

    void excluirPorId(Long id) throws ExcecaoDlo;

    boolean existe(Aluno aluno) throws ExcecaoDlo;

    boolean existePorId(Long id) throws ExcecaoDlo;

    EstatisticaTurma gerarEstatisticaTurma() throws ExcecaoDlo;

    Long inserir(Aluno aluno) throws ExcecaoDlo;

    List<Aluno> listar() throws ExcecaoDlo;

    List<Aluno> listarPorParteDoNome(String parteDoNome) throws ExcecaoDlo;

    List<Aluno> listarPorSituacao(SituacaoAluno situacao) throws ExcecaoDlo;

    Aluno obter(Aluno aluno) throws ExcecaoDlo;

    Aluno obterPorId(Long id) throws ExcecaoDlo;
    
}
