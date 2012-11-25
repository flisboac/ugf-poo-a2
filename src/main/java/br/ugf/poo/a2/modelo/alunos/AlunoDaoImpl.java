package br.ugf.poo.a2.modelo.alunos;

import br.ugf.poo.a2.modelo.excecoes.ExcecaoDao;
import br.ugf.poo.a2.modelo.util.JdbcUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Implementação de {@link AlunoDao}.
 *
 * @author flisboac
 */
public class AlunoDaoImpl implements AlunoDao {

    private Connection connection;
    
    public AlunoDaoImpl() {}
    
    public AlunoDaoImpl(Connection conexao) {
        this.connection = conexao;
    }
    
    public static void criarTabela() throws SQLException {

        boolean existe = false;
        String sql = "DROP TABLE aluno; CREATE TABLE aluno ("
                + "	aluno_id SERIAL NOT NULL"
                + "	, aluno_matr char(10) NOT NULL"
                + "	, aluno_nome varchar(60) NOT NULL"
                + "	, aluno_rg CHAR(9) NOT NULL"
                + "	, aluno_sit int not null"
                + "	, aluno_a1 numeric"
                + "	, aluno_a2 numeric"
                + "	, aluno_a3 numeric"
                + "	, aluno_med numeric"
                + "	, CONSTRAINT aluno_pk PRIMARY KEY (aluno_id)"
                + "	, CONSTRAINT aluno_uq UNIQUE (aluno_matr)"
                + ");";

        Connection conexao = JdbcUtil.criarConexao();

//        try {
//            Statement stmt = conexao.createStatement();
//            ResultSet rs = stmt.executeQuery("select 1 from aluno");
//            existe = true;
//
//        } catch (SQLException e) {
//        }

        if (!existe) {
            try {
                Statement stmt = conexao.createStatement();
                stmt.executeUpdate(sql);

            } catch (SQLException e) {
                throw e;

            } finally {
                JdbcUtil.fecharConexao(conexao);
            }
        }
    }

    public static void criarCargaInicial(AlunoDaoImpl alunoDao, int numeroAlunos) throws ExcecaoDao {
        
        for (int i = 0; i < numeroAlunos; i++) {
            Aluno aluno = new Aluno();
            aluno.setMatricula(String.format("%011d", i));
            aluno.setRg(String.format("%09d", i));
            aluno.setNome("Aluno " + i);
            aluno.setNotaA1(Math.random() * 10.0);
            aluno.setNotaA2(Math.random() * 10.0);
            aluno.setNotaA3(Math.random() * 10.0);
            alunoDao.inserir(aluno);
        }
    }
    
    protected Connection criarConexao() throws ExcecaoDao {
        Connection con = this.connection;

        if (con == null) {
            try {
                con = JdbcUtil.criarConexao();

            } catch (SQLException ex) {
                throw new ExcecaoDao("Não foi possível criar uma nova conexão com o banco de dados.");
            }
        }
        
        return con;
    }

    protected void fecharConexao(Connection con) {
        
        if (this.connection == null) {
            JdbcUtil.fecharConexao(con);
        }
    }

    protected Aluno preencherParaConsulta(Aluno aluno, ResultSet rs) throws SQLException {

        aluno.setId(rs.getLong("aluno_id"));
        aluno.setMatricula(rs.getString("aluno_matr"));
        aluno.setNome(rs.getString("aluno_nome"));
        aluno.setRg(rs.getString("aluno_rg"));
        aluno.setSituacao(
                SituacaoAluno.buscarPorCodigo(rs.getInt("aluno_sit")));
        aluno.setNotaA1(rs.getDouble("aluno_a1"));
        if (rs.wasNull()) {
            aluno.setNotaA1(null);
        }
        aluno.setNotaA2(rs.getDouble("aluno_a2"));
        if (rs.wasNull()) {
            aluno.setNotaA2(null);
        }
        aluno.setNotaA3(rs.getDouble("aluno_a3"));
        if (rs.wasNull()) {
            aluno.setNotaA3(null);
        }
        aluno.setMedia(rs.getDouble("aluno_med"));
        if (rs.wasNull()) {
            aluno.setMedia(null);
        }
        return aluno;
    }

    protected PreparedStatement preencherParaPersistencia(Aluno aluno, PreparedStatement ps) throws SQLException {

        ps.setString(1, aluno.getMatricula());
        ps.setString(2, aluno.getNome());
        ps.setString(3, aluno.getRg());
        ps.setInt(4, aluno.getSituacao().getCodigo());
        
        if (aluno.getNotaA1() == null) {
            ps.setNull(5, Types.DOUBLE);
            
        } else {
            ps.setDouble(5, aluno.getNotaA1());
        }
        
        if (aluno.getNotaA2() == null) {
            ps.setNull(6, Types.DOUBLE);
            
        } else {
            ps.setDouble(6, aluno.getNotaA2());
        }
        
        if (aluno.getNotaA3() == null) {
            ps.setNull(7, Types.DOUBLE);
            
        } else {
            ps.setDouble(7, aluno.getNotaA3());
        }
        
        if (aluno.getMedia() == null) {
            ps.setNull(8, Types.DOUBLE);
            
        } else {
            ps.setDouble(8, aluno.getMedia());
        }
        
        return ps;
    }
    
    @Override
    public void definirNotasPorAvaliacao(Avaliacao avaliacao, Double nota) throws ExcecaoDao {
        
        Connection con = criarConexao();
        
        try {
            String sql = "update aluno set \n"
                    + "  aluno_med = null"
                    + ", aluno_sit = ?"
                    + ", " + avaliacao.getCampo() + " = ?";
                    
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, SituacaoAluno.Nenhuma.getCodigo());
            stmt.setDouble(2, nota);

            if (stmt.executeUpdate() <= 0) {
                throw new ExcecaoDao("Não há alunos na base de dados.");
            }

        } catch (SQLException e) {
            throw new ExcecaoDao("Erro ao definir notas da avaliação " + avaliacao.getTitulo() + " como '" + nota + "'.", e);

        } finally {
            fecharConexao(con);
        }
    }
    
    @Override
    public Aluno obterPorId(Long id) throws ExcecaoDao {

        Aluno retorno = null;
        Connection con = criarConexao();

        try {
            String sql = "select aluno_id \n"
                    + " , aluno_matr \n"
                    + " , aluno_nome \n"
                    + " , aluno_rg \n"
                    + " , aluno_sit \n"
                    + " , aluno_a1 \n"
                    + " , aluno_a2 \n"
                    + " , aluno_a3 \n"
                    + " , aluno_med \n"
                    + " from aluno \n"
                    + " where aluno_id = ?";

            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                retorno = preencherParaConsulta(new Aluno(), rs);
            }

        } catch (SQLException e) {
            throw new ExcecaoDao("Erro ao obter aluno com ID " + id, e);

        } finally {
            fecharConexao(con);
        }

        return retorno;
    }

    @Override
    public Aluno obter(Aluno aluno) throws ExcecaoDao {

        Aluno retorno = null;
        Connection con = criarConexao();
        try {
            String sql = "select aluno_id \n"
                    + " , aluno_matr \n"
                    + " , aluno_nome \n"
                    + " , aluno_rg \n"
                    + " , aluno_sit \n"
                    + " , aluno_a1 \n"
                    + " , aluno_a2 \n"
                    + " , aluno_a3 \n"
                    + " , aluno_med \n"
                    + " from aluno \n"
                    + " where aluno_matr = ?";

            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, aluno.getMatricula());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                retorno = preencherParaConsulta(new Aluno(), rs);
            }

        } catch (SQLException e) {
            throw new ExcecaoDao("Erro ao obter aluno com matrícula " + aluno.getMatricula(), e);

        } finally {
            fecharConexao(con);
        }

        return retorno;
    }

    @Override
    public Long inserir(Aluno aluno) throws ExcecaoDao {

        Long retorno = null;
        Connection con = criarConexao();

        try {
            String sql = "insert into aluno ("
                    + "   aluno_matr \n"
                    + " , aluno_nome \n"
                    + " , aluno_rg \n"
                    + " , aluno_sit \n"
                    + " , aluno_a1 \n"
                    + " , aluno_a2 \n"
                    + " , aluno_a3 \n"
                    + " , aluno_med \n"
                    + ") values (?, ?, ?, ?, ?, ?, ?, ?)"
                    + " returning aluno_id";
            
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = preencherParaPersistencia(aluno, ps).executeQuery();
            
            if (rs.next()) {
                retorno = rs.getLong("aluno_id");
                
            } else {
                throw new ExcecaoDao("Nenhuma entidade afetada no banco de dados.");
            }

        } catch (SQLException e) {
            throw new ExcecaoDao("Erro ao alterar aluno com matrícula '" + aluno.getMatricula() + "'.", e);

        } finally {
            fecharConexao(con);
        }
        
        return retorno;
    }
    
    @Override
    public void alterar(Aluno aluno) throws ExcecaoDao {

        Connection con = criarConexao();

        try {
            String sql = "update aluno set"
                    + "   aluno_matr = ?\n"
                    + " , aluno_nome = ? \n"
                    + " , aluno_rg = ? \n"
                    + " , aluno_sit = ? \n"
                    + " , aluno_a1 = ? \n"
                    + " , aluno_a2 = ? \n"
                    + " , aluno_a3 = ? \n"
                    + " , aluno_med = ? \n"
                    + " where aluno_id = ?";
            
            PreparedStatement ps = con.prepareStatement(sql);
            preencherParaPersistencia(aluno, ps).setLong(9, aluno.getId());
            
            if (ps.executeUpdate() <= 0) {
                throw new ExcecaoDao("Nenhuma entidade afetada no banco de dados.");
            }

        } catch (SQLException e) {
            throw new ExcecaoDao("Erro ao alterar aluno com matrícula '" + aluno.getMatricula() + "'.", e);

        } finally {
            fecharConexao(con);
        }
    }

    @Override
    public void excluir(Aluno aluno) throws ExcecaoDao {
        
        Connection con = criarConexao();
        
        try {
            String sql = "delete from aluno \n"
                    + " where aluno_matr = ?";
            
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, aluno.getMatricula());
            
            if (ps.executeUpdate() <= 0) {
                throw new ExcecaoDao("Nenhuma entidade afetada no banco de dados.");
            }

        } catch (SQLException e) {
            throw new ExcecaoDao("Erro ao excluir aluno com matrícula '" + aluno.getMatricula() + "'.", e);

        } finally {
            fecharConexao(con);
        }
    }

    @Override
    public void excluirPorId(Long id) throws ExcecaoDao {
        Connection con = criarConexao();
        
        try {
            String sql = "delete from aluno \n"
                    + " where aluno_id = ?";
            
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setLong(1, id);
            
            if (ps.executeUpdate() <= 0) {
                throw new ExcecaoDao("Nenhuma entidade afetada no banco de dados.");
            }

        } catch (SQLException e) {
            throw new ExcecaoDao("Erro ao excluir aluno com ID " + id + ".", e);

        } finally {
            fecharConexao(con);
        }
    }

    @Override
    public boolean existe(Aluno aluno) throws ExcecaoDao {

        boolean retorno = false;
        Connection con = criarConexao();
        
        try {
            String sql = "select 1 from aluno \n"
                    + " where aluno_matr = ?";
            
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, aluno.getMatricula());
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                retorno = true;
            }

        } catch (SQLException e) {
            throw new ExcecaoDao("Erro ao verificar a existência de aluno com matrícula '" + aluno.getMatricula() + "'.", e);

        } finally {
            fecharConexao(con);
        }
        
        return retorno;
    }

    @Override
    public boolean existePorId(Long id) throws ExcecaoDao {

        boolean retorno = false;
        Connection con = criarConexao();
        
        try {
            String sql = "select 1 from aluno \n"
                    + " where aluno_id = ?";
            
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                retorno = true;
            }

        } catch (SQLException e) {
            throw new ExcecaoDao("Erro ao verificar a existência de aluno com ID " + id + ".", e);

        } finally {
            fecharConexao(con);
        }
        
        return retorno;
    }
    
    @Override
    public List<Aluno> listar() throws ExcecaoDao {
        
        List<Aluno> retorno = null;
        Connection con = criarConexao();
        try {
            String sql = "select aluno_id \n"
                    + " , aluno_matr \n"
                    + " , aluno_nome \n"
                    + " , aluno_rg \n"
                    + " , aluno_sit \n"
                    + " , aluno_a1 \n"
                    + " , aluno_a2 \n"
                    + " , aluno_a3 \n"
                    + " , aluno_med \n"
                    + " from aluno";

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            retorno = new ArrayList<Aluno>();

            while (rs.next()) {
                Aluno aluno = preencherParaConsulta(new Aluno(), rs);
                retorno.add(aluno);
            }

        } catch (SQLException e) {
            throw new ExcecaoDao("Erro ao listar alunos.", e);

        } finally {
            fecharConexao(con);
        }

        return retorno;
    }
    
    @Override
    public List<Aluno> listarPorSituacao(SituacaoAluno situacao) throws ExcecaoDao {

        List<Aluno> retorno = null;
        Connection con = criarConexao();
        try {
            String sql = "select aluno_id \n"
                    + " , aluno_matr \n"
                    + " , aluno_nome \n"
                    + " , aluno_rg \n"
                    + " , aluno_sit \n"
                    + " , aluno_a1 \n"
                    + " , aluno_a2 \n"
                    + " , aluno_a3 \n"
                    + " , aluno_med \n"
                    + " from aluno \n"
                    + " where aluno_sit = ?";

            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, situacao.getCodigo());
            ResultSet rs = stmt.executeQuery();
            retorno = new ArrayList<Aluno>();

            while (rs.next()) {
                Aluno aluno = preencherParaConsulta(new Aluno(), rs);
                retorno.add(aluno);
            }

        } catch (SQLException e) {
            throw new ExcecaoDao("Erro ao listar alunos por situação '" + situacao.getTitulo() + "'.", e);

        } finally {
            fecharConexao(con);
        }

        return retorno;
    }
    
    @Override
    public List<Aluno> listarPorParteDoNome(String parteDoNome) throws ExcecaoDao {

        List<Aluno> retorno = null;
        Connection con = criarConexao();
        try {
            String sql = "select aluno_id \n"
                    + " , aluno_matr \n"
                    + " , aluno_nome \n"
                    + " , aluno_rg \n"
                    + " , aluno_sit \n"
                    + " , aluno_a1 \n"
                    + " , aluno_a2 \n"
                    + " , aluno_a3 \n"
                    + " , aluno_med \n"
                    + " from aluno \n"
                    + " where lower(aluno_nome) like ?";

            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, "%" + parteDoNome.toLowerCase() + "%");
            ResultSet rs = stmt.executeQuery();
            retorno = new ArrayList<Aluno>();

            while (rs.next()) {
                Aluno aluno = preencherParaConsulta(new Aluno(), rs);
                retorno.add(aluno);
            }

        } catch (SQLException e) {
            throw new ExcecaoDao("Erro ao listar alunos por parte de nome '" + parteDoNome + "'.", e);

        } finally {
            fecharConexao(con);
        }

        return retorno;
    }
    
    @Override
    public EstatisticaTurma gerarEstatisticaTurma() throws ExcecaoDao {
        
        throw new NotImplementedException();
    }
}
