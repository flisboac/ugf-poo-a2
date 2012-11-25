package br.ugf.poo.a2.app;

import br.ugf.poo.a2.modelo.alunos.Aluno;
import br.ugf.poo.a2.modelo.alunos.AlunoDlo;
import br.ugf.poo.a2.modelo.alunos.AlunoDloImpl;
import br.ugf.poo.a2.modelo.alunos.Avaliacao;
import br.ugf.poo.a2.modelo.alunos.EstatisticaTurma;
import br.ugf.poo.a2.modelo.alunos.SituacaoAluno;
import br.ugf.poo.a2.modelo.excecoes.ExcecaoDlo;
import br.ugf.poo.a2.modelo.util.JdbcUtil;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static final String RegexNota = "[0-9]+(\\.[0-9]+)?";
    
    public static AlunoDlo alunoDlo = new AlunoDloImpl();
    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        int opcao = 0;

        do {
            System.out.println();
            System.out.println("<<<<<<< SISTEMA DE BOLETIM WTV >>>>>>>");
            System.out.println(" [0] SAIR");
            System.out.println(" [1] Matricular aluno");
            System.out.println(" [2] Excluir aluno por matrícula");
            System.out.println(" [3] Lançar nota de um aluno");
            System.out.println(" [4] Lançar notas para todos os alunos");
            System.out.println(" [5] Buscar Aluno");
            System.out.println(" [6] Listar Alunos");
            System.out.println(" [7] Exibir estatísticas");
            System.out.println("--------------------------------------");
            System.out.println(" [9] (Re)Criar tabelas");
            System.out.println("=====================================-");
            System.out.print(" Escolha sua opção: ");

            opcao = scanner.nextInt();
            scanner.nextLine();
            
            switch (opcao) {

                case 0:
                    break;
                    
                case 1:
                    matricularAluno();
                    break;
                    
                case 2:
                    excluirAluno();
                    break;
                    
                case 3:
                    lancarNotaAluno();
                    break;
                    
                case 4:
                    lancarNotasParaTodosAlunos();
                    break;
                    
                case 5:
                    buscarAluno();
                    break;
                    
                case 6:
                    listarAlunos();
                    break;
                    
                case 7:
                    exibirEstatisticaTurma();
                    break;
                    
                case 9:
                    criarTabelas();
                    break;
                    
                default:
                    System.out.println("Opção inválida.");
                    System.out.println();
                    break;
            }
            
        } while (opcao != 0);
        
        // Bye!
        System.exit(0);
    }

    private static void matricularAluno() {
        
        Aluno aluno = new Aluno();
        
        System.out.print("Matrícula: ");
        aluno.setMatricula(scanner.nextLine());
        
        System.out.print("Nome: ");
        aluno.setNome(scanner.nextLine());
        
        System.out.print("RG: ");
        aluno.setRg(scanner.nextLine());
        
        try {
            aluno.setId(alunoDlo.inserir(aluno));
            System.out.println("Aluno matriculado com sucesso (ID: " + aluno.getId() + ").");
            mostrarAluno(aluno);
            
        } catch (ExcecaoDlo ex) {
            System.out.println("Erro ao matricular aluno!");
            System.out.println(" * Erro: " + ex.getMessage());
        }
    }

    private static void excluirAluno() {
        
        Aluno aluno = new Aluno();
        
        System.out.print("Matrícula: ");
        aluno.setMatricula(scanner.nextLine());
        
        try {
            alunoDlo.excluir(aluno);
            System.out.println("Aluno excluído com sucesso.");
            
        } catch (ExcecaoDlo ex) {
            System.out.println("Erro ao excluir aluno!");
            System.out.println(" * Erro: " + ex.getMessage());
        }
    }

    private static void lancarNotaAluno() {
        
        Aluno aluno = new Aluno();
        
        System.out.println("* Para não modificar a nota de uma");
        System.out.println("  avaliação específica, deixe o campo");
        System.out.println("  em branco.");
        System.out.println();
        
        System.out.print("Matrícula: ");
        aluno.setMatricula(scanner.nextLine());
        
        System.out.print("Nota da A1: ");
        String strNotaA1 = scanner.nextLine();
        
        System.out.print("Nota da A2: ");
        String strNotaA2 = scanner.nextLine();
        
        System.out.print("Nota da A3: ");
        String strNotaA3 = scanner.nextLine();
        
        // Verifica se o aluno existe.
        try {
            if (!alunoDlo.existe(aluno)) {
                System.out.println("Não existe nenhum aluno com esta matrícula.");
                return;
            }
            
        } catch (ExcecaoDlo ex) {
            System.out.println("Erro ao verificar existência de aluno!");
            System.out.println(" * Erro: " + ex.getMessage());
            return;
        }
        
        // Obtém o aluno, para posteriormente persisti-lo
        try {
            aluno = alunoDlo.obter(aluno);
            
        } catch (ExcecaoDlo ex) {
            System.out.println("Erro ao recuperar dados do aluno!");
            System.out.println(" * Erro: " + ex.getMessage());
            return;
        }
        
        // Convertendo as notas e calculando situação do aluno
        
        if (strNotaA1 != null && !strNotaA1.isEmpty() && strNotaA1.matches(RegexNota)) {
            aluno.setNotaA1(Double.parseDouble(strNotaA1));
        }
        
        if (strNotaA2 != null && !strNotaA2.isEmpty() && strNotaA2.matches(RegexNota)) {
            aluno.setNotaA2(Double.parseDouble(strNotaA2));
        }
        
        if (strNotaA3 != null && !strNotaA3.isEmpty() && strNotaA3.matches(RegexNota)) {
            aluno.setNotaA3(Double.parseDouble(strNotaA3));
        }
        
        try {
            alunoDlo.calcularSituacao(aluno);
            
        } catch (ExcecaoDlo ex) {
            System.out.println("Erro ao calcular situação de aluno!");
            System.out.println(" * Erro: " + ex.getMessage());
            return;
        }
        
        // Persistindo dados alterados
        try {
            alunoDlo.alterar(aluno);
            System.out.println("Notas do aluno lançadas com sucesso.");
            mostrarAluno(aluno);
            
        } catch (ExcecaoDlo ex) {
            System.out.println("Erro ao salvar dados do aluno!");
            System.out.println(" * Erro: " + ex.getMessage());
        }
    }

    private static void lancarNotasParaTodosAlunos() {
        
        System.out.println(" [1] Lançar notas para a A1");
        System.out.println(" [2] Lançar notas para a A2");
        System.out.println(" [3] Lançar notas para a A3");
        System.out.print  ("Escolha: ");
        
        int intAvaliacao = scanner.nextInt();
        scanner.nextLine();
        Avaliacao avaliacao;
        
        switch(intAvaliacao) {
            case 1:
                avaliacao = Avaliacao.A1;
                break;
            case 2:
                avaliacao = Avaliacao.A2;
                break;
            case 3:
                avaliacao = Avaliacao.A3;
                break;
            default:
                System.out.println("Opção inválida para avaliação.");
                return;
        }
        
        System.out.println("Nota: ");
        String strNota = scanner.nextLine();
        
        if (strNota == null || strNota.isEmpty()) {
            System.out.println("Nota nula, digite uma nota.");
            return;
        }
        
        if (!strNota.matches(RegexNota)) {
            System.out.println("Formato inválido para nota.");
            return;
        }
        
        Double nota = Double.parseDouble(strNota);
        
        try {
            alunoDlo.definirNotasPorAvaliacao(avaliacao, nota);
            System.out.println("Notas alteradas com sucesso.");
            System.out.println("Para ver os resultados, escolha");
            System.out.println("uma opção de listagem no menu principal.");
            
        } catch (ExcecaoDlo ex) {
            System.out.println("Erro ao definir notas dos alunos para a " + avaliacao.getTitulo() + " com nota " + nota + "!");
            System.out.println(" * Erro: " + ex.getMessage());
        }
    }

    private static void buscarAluno() {
        
        Aluno aluno = new Aluno();
        
        System.out.print("Matrícula: ");
        aluno.setMatricula(scanner.nextLine());
        
        try {
            aluno = alunoDlo.obter(aluno);
            
        } catch (ExcecaoDlo ex) {
            System.out.println("Erro ao recuperar dados do aluno!");
            System.out.println(" * Erro: " + ex.getMessage());
            return;
        }
        
        if (aluno != null) {
            mostrarAluno(aluno);
            
        } else {
            System.out.println("Não há nenhum aluno com esta matrícula.");
        }
    }

    private static void listarAlunos() {
        
        List<Aluno> alunos = null;
        
        System.out.println(" [0] VOLTAR");
        System.out.println("------------------------------");
        System.out.println(" [1] Exibir todos");
        System.out.println(" [2] Exibir aprovados");
        System.out.println(" [3] Exibir reprovados");
        System.out.println(" [4] Exibir em prova final");
        System.out.println(" [5] Exibir sem situação");
        System.out.println(" [6] Buscar por parte do nome");
        System.out.println("------------------------------");
        System.out.print  ("Escolha: ");
        
        int opcao = scanner.nextInt();
        scanner.nextLine();
        
        try {
            switch(opcao) {
                case 0:
                    return;
                case 1:
                    alunos = alunoDlo.listar();
                    break;
                case 2:
                    alunos = alunoDlo.listarPorSituacao(SituacaoAluno.Aprovado);
                    break;
                case 3:
                    alunos = alunoDlo.listarPorSituacao(SituacaoAluno.Reprovado);
                    break;
                case 4:
                    alunos = alunoDlo.listarPorSituacao(SituacaoAluno.ProvaFinal);
                    break;
                case 5:
                    alunos = alunoDlo.listarPorSituacao(SituacaoAluno.Nenhuma);
                    break;
                case 6:
                    System.out.println("Digite parte ou todo o nome do aluno: ");
                    String parteDoNome = scanner.nextLine();
                    
                    if (parteDoNome == null || parteDoNome.length() < 2) {
                        System.out.println("Digite pelo menos dois caracteres para a pesquisa.");
                        return;
                    }
                    
                    alunos = alunoDlo.listarPorParteDoNome(parteDoNome);
                    break;
            }
            
        } catch (ExcecaoDlo ex) {
            System.out.println("Erro ao listar alunos!");
            System.out.println(" * Erro: " + ex.getMessage());
            return;
        }
        
        if (alunos.isEmpty()) {
            System.out.println("Não há alunos para listar.");
            
        } else {
            for (Aluno aluno : alunos) {

                mostrarAluno(aluno);
            }
        }
    }

    private static void exibirEstatisticaTurma() {
        
        EstatisticaTurma estatistica;
        
        try {
            estatistica = alunoDlo.gerarEstatisticaTurma();
            
        } catch (ExcecaoDlo ex) {
            System.out.println("Erro ao gerar estatísticas para a turma!");
            System.out.println(" * Erro: " + ex.getMessage());
            return;
        }
        
        System.out.println("            Total de alunos : " + estatistica.getQtdAlunos());
        System.out.println("             Média da turma : " + estatistica.getMedia());
        System.out.println("Qtd. alunos abaixo da média : " + estatistica.getQtdAlunosAbaixoDaMedia());
        System.out.println(" Qtd. alunos acima da média : " + estatistica.getQtdAlunosAcimaDaMedia());
        System.out.println("      Qtd. alunos com média : " + estatistica.getQtdAlunosComMedia());
        System.out.println("      Qtd. alunos aprovados : " + estatistica.getQtdAlunosAprovados());
        System.out.println("     Qtd. alunos reprovados : " + estatistica.getQtdAlunosReprovados());
        System.out.println(" Qtd. alunos em prova final : " + estatistica.getQtdAlunosProvaFinal());
    }

    private static void criarTabelas() {
        
        try {
            JdbcUtil.criarTabelas();
            System.out.println("Tabelas (re)criadas com sucesso.");
            
        } catch (SQLException ex) {
            System.out.println("Erro ao (re)criar tabelas!");
            System.out.println(" * Erro: " + ex.getMessage());
        }
    }
    
    private static void mostrarAluno(Aluno aluno) {
        
        System.out.println(" > [ " + aluno.getMatricula() + " ] (" + aluno.getId() + ")");
        System.out.println("       Nome : " + aluno.getNome());
        System.out.println("         RG : " + aluno.getRg());
        System.out.println("   Situação : " + aluno.getSituacao().getTitulo());
        System.out.println("      Média : " + (aluno.getMedia()  != null ? aluno.getMedia()  : "(Sem média)"));
        System.out.println("    Nota A1 : " + (aluno.getNotaA1() != null ? aluno.getNotaA1() : "(Sem nota)" ));
        System.out.println("    Nota A2 : " + (aluno.getNotaA2() != null ? aluno.getNotaA2() : "(Sem nota)" ));
        System.out.println("    Nota A3 : " + (aluno.getNotaA3() != null ? aluno.getNotaA3() : "(Sem nota)" ));
    }
}
