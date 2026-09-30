package DAO;

import Model.Funcionario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioDAO {

    public FuncionarioDAO() {
        criarTabelaESeedIniciais();
    }

    /**
     * Garante a criação da tabela no SQLite e insere um funcionário padrão se estiver vazia.
     */
    private void criarTabelaESeedIniciais() {
        String sqlTabela = "CREATE TABLE IF NOT EXISTS funcionario ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "nome TEXT NOT NULL, "
                + "cpf TEXT, "
                + "email TEXT, "
                + "telefone TEXT, "
                + "senha TEXT, "
                + "cargo TEXT, "
                + "idade INTEGER, "
                + "salario REAL"
                + ");";

        String sqlCheck = "SELECT COUNT(*) FROM funcionario";
        String sqlInsert = "INSERT INTO funcionario (nome, cpf, email, telefone, senha, cargo, idade, salario) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoDAO.conectar();
             PreparedStatement stmtTabela = conn.prepareStatement(sqlTabela)) {

            stmtTabela.execute();

            try (PreparedStatement stmtCheck = conn.prepareStatement(sqlCheck);
                 ResultSet rs = stmtCheck.executeQuery()) {

                if (rs.next() && rs.getInt(1) == 0) {
                    try (PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert)) {
                        stmtInsert.setString(1, "Administrador");
                        stmtInsert.setString(2, "000.000.000-00");
                        stmtInsert.setString(3, "admin@loja.com");
                        stmtInsert.setString(4, "(51) 99999-9999");
                        stmtInsert.setString(5, "1234");
                        stmtInsert.setString(6, "Gerente");
                        stmtInsert.setInt(7, 30);
                        stmtInsert.setDouble(8, 3500.00);
                        stmtInsert.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao inicializar tabela de funcionários: " + e.getMessage());
        }
    }

    public boolean cadastrar(Funcionario funcionario) {
        String sql = "INSERT INTO funcionario (nome, cpf, email, telefone, senha, cargo, idade, salario) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexaoDAO.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, funcionario.getNome());
            stmt.setString(2, funcionario.getCpf());
            stmt.setString(3, funcionario.getEmail());
            stmt.setString(4, funcionario.getTelefone());
            stmt.setString(5, funcionario.getSenha());
            stmt.setString(6, funcionario.getCargo());
            stmt.setInt(7, funcionario.getIdade());
            stmt.setDouble(8, funcionario.getSalario());

            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar funcionário: " + e.getMessage());
            return false;
        }
    }

    public List<Funcionario> listar() {
        List<Funcionario> lista = new ArrayList<>();
        String sql = "SELECT * FROM funcionario";

        try (Connection conn = ConexaoDAO.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Funcionario f = new Funcionario();
                f.setId(rs.getInt("id"));
                f.setNome(rs.getString("nome"));
                f.setCpf(rs.getString("cpf"));
                f.setEmail(rs.getString("email"));
                f.setTelefone(rs.getString("telefone"));
                f.setSenha(rs.getString("senha"));
                f.setCargo(rs.getString("cargo"));
                f.setIdade(rs.getInt("idade"));
                f.setSalario(rs.getDouble("salario"));
                lista.add(f);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar funcionários: " + e.getMessage());
        }
        return lista;
    }

    public boolean alterar(Funcionario funcionario) {
        String sql = "UPDATE funcionario SET nome = ?, cpf = ?, email = ?, telefone = ?, senha = ?, cargo = ?, idade = ?, salario = ? WHERE id = ?";
        try (Connection conn = ConexaoDAO.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, funcionario.getNome());
            stmt.setString(2, funcionario.getCpf());
            stmt.setString(3, funcionario.getEmail());
            stmt.setString(4, funcionario.getTelefone());
            stmt.setString(5, funcionario.getSenha());
            stmt.setString(6, funcionario.getCargo());
            stmt.setInt(7, funcionario.getIdade());
            stmt.setDouble(8, funcionario.getSalario());
            stmt.setInt(9, funcionario.getId());

            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao alterar funcionário: " + e.getMessage());
            return false;
        }
    }

    public boolean excluir(int id) {
        String sql = "DELETE FROM funcionario WHERE id = ?";
        try (Connection conn = ConexaoDAO.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao excluir funcionário: " + e.getMessage());
            return false;
        }
    }

    public Funcionario autenticar(String cpfOuNome, String senha) {
        String sql = "SELECT * FROM funcionario WHERE (nome = ? OR cpf = ?) AND senha = ?";
        try (Connection conn = ConexaoDAO.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpfOuNome);
            stmt.setString(2, cpfOuNome);
            stmt.setString(3, senha);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Funcionario f = new Funcionario();
                    f.setId(rs.getInt("id"));
                    f.setNome(rs.getString("nome"));
                    f.setCpf(rs.getString("cpf"));
                    f.setEmail(rs.getString("email"));
                    f.setTelefone(rs.getString("telefone"));
                    f.setSenha(rs.getString("senha"));
                    f.setCargo(rs.getString("cargo"));
                    f.setIdade(rs.getInt("idade"));
                    f.setSalario(rs.getDouble("salario"));
                    return f;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao autenticar funcionário: " + e.getMessage());
        }
        return null;
    }
}