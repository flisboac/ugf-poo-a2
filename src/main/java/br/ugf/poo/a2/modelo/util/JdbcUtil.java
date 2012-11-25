package br.ugf.poo.a2.modelo.util;

import br.ugf.poo.a2.modelo.alunos.AlunoDaoImpl;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe auxiliar usada para criar conexões a serem usadas nos DAOs
 * da aplicação.
 * @author flisboac
 */
public class JdbcUtil {
    
    // Dados da conexão. Modifique conforme necessário.
    public static final String Driver = "org.postgresql.Driver";
    public static final String Url = "jdbc:postgresql://localhost:5432/";
    public static final String Db = "poo_a2";
    public static final String Usuario = "postgres";
    public static final String Senha = "postgres";
    
    // A classe do driver deve ser carregada na JVM, para que a infraestrutura
    // do JDBC seja capaz de encontra-la e usa-la para criar conexões.
    // Se a classe de driver não puder ser carregada, qualquer funcionalidade
    // relaciopnada à persistência não funcionará.
    // Em especial, o trecho de código abaixo marcado como `static` será
    // executado no momento em que a classe JdbcUtil for carregada pela JVM.
    // Este é um recurso especial da linguagem. Para nossos propósitos,
    // e para simplificar o exercício, só nos interessa que a classe de
    // driver seja carregada.
    static {
        try {        
            Class.forName(Driver);
            
        } catch (ClassNotFoundException ex) {
            System.out.println(ex);
        }
    };
    
    /**
     * Cria uma nova conexão local.
     * @return Uma nova conexão.
     * @throws SQLException Exceção repassada do DriverManager.
     */
    public static Connection criarConexao() throws SQLException {
        
        Connection con;
        con = DriverManager.getConnection(Url + Db, Usuario, Senha);
//        System.out.println("* Driver JDBC para PostgreSQL: " + Driver);
//        System.out.println("* URL da conexão: " + Url + Db);
//        System.out.println("* Uma nova conexão foi criada...");
        return con;
    }
    
    /**
     * Fecha uma conexão local criada com {@link #criarConexao}.
     * @param conexao A conexão a ser fechada.
     */
    public static void fecharConexao(Connection conexao) {
        
        if (conexao != null) {
            try {
                conexao.close();
                
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        }
    }
    
    /**
     * Cria todas as tabelas necessárias no banco de dados.
     * Este método delega a operação para cada DAO de cada entidade.
     * @throws SQLException 
     */
    public static void criarTabelas() throws SQLException {
        
        AlunoDaoImpl.criarTabela();
    }
}
