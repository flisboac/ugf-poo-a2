package br.ugf.poo.a2.modelo.alunos;

import br.ugf.poo.a2.modelo.excecoes.ExcecaoDao;
import java.util.List;

/**
 * DAO para a entidade {@link Aluno}.
 * 
 * @author flisboac
 * @see Aluno
 */
public interface AlunoDao {
    
    /**
     * Persiste no banco de dados as alterações feitas em um aluno já existente.
     * 
     * @param aluno O aluno a ser persistido.
     * @throws ExcecaoDao Se ocorrer algum erro.
     */
    void alterar(Aluno aluno) throws ExcecaoDao;
    
    /**
     * Define todas as notas de todos os alunos para uma avaliação específica.
     * Note que este método remove também a situação e a média de todos os
     * alunos, sendo necessário recalcula-los.
     * 
     * @param avaliacao A avaliação a ser redefinida.
     * @param nota A nota a ser atribuída.
     * @throws ExcecaoDao Se ocorrer algum erro.
     */
    void definirNotasPorAvaliacao(Avaliacao avaliacao, Double nota) throws ExcecaoDao;

    /**
     * Exclui um aluno a partir de sua identidade.
     * 
     * @param aluno A identidade a ser excluída.
     * @throws ExcecaoDao Se ocorrer algum erro.
     * @see Aluno
     */
    void excluir(Aluno aluno) throws ExcecaoDao;

    /**
     * Exclui um aluno a partir de sua chave primária.
     * 
     * @param id A ID do aluno.
     * @throws ExcecaoDao Se ocorrer algum erro.
     * @see Aluno
     */
    void excluirPorId(Long id) throws ExcecaoDao;

    /**
     * Verifica se existe um aluno com a identidade passada.
     * 
     * @param aluno A identidade a ser verificada.
     * @return {@code true} se o aluno existe, {@code false} caso contrário.
     * @throws ExcecaoDao Se ocorrer algum erro.
     */
    boolean existe(Aluno aluno) throws ExcecaoDao;
    
    /**
     * Verifica se existe um aluno com a chave primária passada.
     * 
     * @param id A ID do aluno.
     * @return {@code true} se o aluno existe, {@code false} caso contrário.
     * @throws ExcecaoDao Se ocorrer algum erro.
     */
    boolean existePorId(Long id) throws ExcecaoDao;

    /**
     * Gera as estatísticas gerais da turma, usando todos os alunos.
     * 
     * @return A estatística da turma.
     * @throws ExcecaoDao Se ocorrer algum erro.
     */
    EstatisticaTurma gerarEstatisticaTurma() throws ExcecaoDao;

    /**
     * Inclui um novo aluno no banco de dados.
     * 
     * @param aluno O aluno a ser incluído.
     * @return A ID do aluno recém inserido.
     * @throws ExcecaoDao Se ocorrer algum erro.
     */
    Long inserir(Aluno aluno) throws ExcecaoDao;

    /**
     * Lista todos os alunos existentes.
     * 
     * @return Uma lista contendo todos os alunos existentes.
     * @throws ExcecaoDao Se ocorrer algum erro.
     */
    List<Aluno> listar() throws ExcecaoDao;

    /**
     * Lista todos os alunos cujos nomes contenham parte ou toda a string
     * passada.
     * 
     * @param parteDoNome A parte do nome a ser checada.
     * @return Uma lista com todos os alunos que atendem ao requisito.
     * @throws ExcecaoDao Se ocorrer algum erro.
     */
    List<Aluno> listarPorParteDoNome(String parteDoNome) throws ExcecaoDao;

    /**
     * Lista todos os alunos na situação passada.
     * 
     * @param situacao A situação a ser usada na pesquisa.
     * @return Uma lista com todos os alunos que atendem ao requisito.
     * @throws ExcecaoDao Se ocorrer algum erro.
     */
    List<Aluno> listarPorSituacao(SituacaoAluno situacao) throws ExcecaoDao;

    /**
     * Obtém um aluno a partir de sua identidade.
     * 
     * @param aluno A identidade a ser usada na busca.
     * @return O aluno encontrado, ou {@code null} caso não exista.
     * @throws ExcecaoDao Se ocorrer algum erro.
     */
    Aluno obter(Aluno aluno) throws ExcecaoDao;

    /**
     * Obtém um aluno a partir de sua chave primária.
     * 
     * @param id A ID do aluno.
     * @return O aluno encontrado, ou {@code null} caso não exista.
     * @throws ExcecaoDao Se ocorrer algum erro.
     */
    Aluno obterPorId(Long id) throws ExcecaoDao;
    
}
