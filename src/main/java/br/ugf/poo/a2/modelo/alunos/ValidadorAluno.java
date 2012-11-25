package br.ugf.poo.a2.modelo.alunos;

import br.ugf.poo.a2.modelo.excecoes.ExcecaoValidacaoDlo;

/**
 * Validador para a entidade {@link Aluno}, a ser usado primariamente em
 * {@link AlunoDlo}.
 * 
 * @author flisboac
 */
public class ValidadorAluno {
    
    /**
     * Valida um aluno a ser usado em uma operação {@code insert}.
     * 
     * @param aluno O aluno a ser validado.
     * @throws ExcecaoValidacaoDlo Se algum erro de validação for encontrado.
     */
    public void validarParaInsercao(Aluno aluno) throws ExcecaoValidacaoDlo {
        
        if (aluno == null) {
            throw new ExcecaoValidacaoDlo("Aluno não pode ser nulo.");
        }
        
        
        // DADOS ---------------------------------------------------------------
        
        
        if (aluno.getRg() == null || aluno.getRg().isEmpty()) {
            throw new ExcecaoValidacaoDlo("Aluno deve possuir uma identidade.");
        }
        
//        if (!aluno.getRg().matches("\\d{9}")) {
//            throw new ExcecaoValidacaoDlo("Identidade do aluno deve possuir exatamente 9 dígitos.");
//        }
        
        if (aluno.getMatricula() == null || aluno.getMatricula().isEmpty()) {
            throw new ExcecaoValidacaoDlo("Aluno deve possuir uma matrícula.");
        }
        
//        if (!aluno.getMatricula().matches("\\d{11}")) {
//            throw new ExcecaoValidacaoDlo("Matrícula do aluno deve possuir exatamente 11 dígitos.");
//        }
        
        if (aluno.getNome() == null || aluno.getNome().isEmpty()) {
            throw new ExcecaoValidacaoDlo("Aluno deve possuir um nome.");
        }
        
        
        // NOTAS ---------------------------------------------------------------
        
        
        if (aluno.getNotaA1() != null && 
                (aluno.getNotaA1().compareTo(Aluno.NotaMaxima) > 0
                || aluno.getNotaA1().compareTo(Aluno.NotaMinima) < 0)
        ) {
            throw new ExcecaoValidacaoDlo("Nota da A1 fora dos limites permitidos.");
        }
        
        if (aluno.getNotaA2() != null && 
                (aluno.getNotaA2().compareTo(Aluno.NotaMaxima) > 0
                || aluno.getNotaA2().compareTo(Aluno.NotaMinima) < 0)
        ) {
            throw new ExcecaoValidacaoDlo("Nota da A2 fora dos limites permitidos.");
        }
        
        // Aluno pode fazer a A3 no lugar de outra em caso de falta
//        if (aluno.getNotaA1() == null && aluno.getNotaA2() != null) {
//            throw new ExcecaoValidacaoDlo("Aluno possui A2 mas não possui A1.");
//        }
        
        if (aluno.getNotaA3() != null && 
                (aluno.getNotaA3().compareTo(Aluno.NotaMaxima) > 0
                || aluno.getNotaA3().compareTo(Aluno.NotaMinima) < 0)
        ) {
            throw new ExcecaoValidacaoDlo("Nota da A3 fora dos limites permitidos.");
        }
        
        // Aluno pode fazer a A3 no lugar de outra em caso de falta
//        if (aluno.getNotaA2() == null && aluno.getNotaA3() != null) {
//            throw new ExcecaoValidacaoDlo("Aluno possui A3 mas não possui A2.");
//        }
        
        if (aluno.getMedia() != null && 
                (aluno.getMedia().compareTo(Aluno.NotaMaxima) > 0
                || aluno.getMedia().compareTo(Aluno.NotaMinima) < 0)
        ) {
            throw new ExcecaoValidacaoDlo("Média fora dos limites permitidos.");
        }
        
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
        
        if (aluno.getMedia() != null && qtdAvaliacoes < 2) {
            throw new ExcecaoValidacaoDlo("Aluno deve possuir no mínimo duas avaliações para possuir uma média.");
        }
        
        if (aluno.getMedia() != null) {
            double media = (maior + menor) / 2;
            
            if (aluno.getMedia().compareTo(media) != 0) {
            throw new ExcecaoValidacaoDlo("Média '" + aluno.getMedia() 
                    + "' errada para as notas "
                    + "(máxima: " + maior + ", mínima: " + menor + ", esperada: " + media + ").");
            }
        }
        
        
        // SITUAÇÃO ------------------------------------------------------------
        
        
        if (aluno.getSituacao() == null) {
            throw new ExcecaoValidacaoDlo("Aluno não pode deixar de ter uma situação.");
        }
        
        if (aluno.getSituacao().isTerminal() && aluno.getMedia() == null) {
            throw new ExcecaoValidacaoDlo("Aluno possui situação final mas não possui média.");
        }
        
        if (aluno.getSituacao() == SituacaoAluno.Aprovado &&
                aluno.getMedia().compareTo(Aluno.Media) < 0) {
            throw new ExcecaoValidacaoDlo("Aluno aprovado com média abaixo da necessária.");
        }
        
        if (aluno.getSituacao() == SituacaoAluno.Reprovado &&
                aluno.getMedia().compareTo(Aluno.Media) >= 0) {
            throw new ExcecaoValidacaoDlo("Aluno reprovado com média igual ou acima da necessária.");
        }
        
        // A A3 pode ser feita caso haja uma falta na A1 ou A2.
//        if (aluno.getSituacao() == SituacaoAluno.ProvaFinal &&
//                (aluno.getNotaA1() == null || aluno.getNotaA2() == null)) {
//            throw new ExcecaoValidacaoDlo("Aluno em prova final sem notas da A1 ou A2.");
//        }
    }
    
    /**
     * Valida um aluno a ser usado em uma operação {@code update}.
     * 
     * @param aluno O aluno a ser validado.
     * @throws ExcecaoValidacaoDlo Se algum erro de validação for encontrado.
     */
    public void validarParaAlteracao(Aluno aluno) throws ExcecaoValidacaoDlo {
        
        validarParaInsercao(aluno);
        validarParaConsulta(aluno.getId());
    }
    
    /**
     * Valida uma ID a ser usada em uma operação {@code select}.
     * 
     * @param id A identificação a ser validada.
     * @throws ExcecaoValidacaoDlo Se algum erro de validação for encontrado.
     */
    public void validarParaConsulta(Long id) throws ExcecaoValidacaoDlo {
        
        if (id == null) {
            throw new ExcecaoValidacaoDlo("ID não pode ser nulo.");
        }
    }
    
    /**
     * Valida um aluno a ser usado em uma operação {@code select}.
     * A operação deverá usar apenas propriedades que sejam parte de sua
     * identidade.
     * 
     * @param aluno O aluno a ser validado.
     * @throws ExcecaoValidacaoDlo Se algum erro de validação for encontrado.
     */
    public void validarParaConsulta(Aluno aluno) throws ExcecaoValidacaoDlo {
        
        if (aluno == null) {
            throw new ExcecaoValidacaoDlo("Aluno não pode ser nulo.");
        }
        
        if (aluno.getMatricula() == null || aluno.getMatricula().isEmpty()) {
            throw new ExcecaoValidacaoDlo("Aluno deve possuir uma matrícula.");
        }
    }
}
