package DAO;

import Model.Funcionario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioDAO {

    public boolean cadastrar(Funcionario funcionario) {
        String sql = "INSERT INTO funcionario (nome, cpf, email, telefone, senha) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexaoDAO.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, funcionario.getNome());
            stmt.setString(2, funcionario.getCpf());
            stmt.setString(3, funcionario.getEmail());
            stmt.setString(4, funcionario.getTelefone());
            stmt.setString(5, funcionario.getSenha());

            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar funcionario: " + e.getMessage());
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
                Funcionario c = new Funcionario();
                c.setId(rs.getInt("id"));
                c.setNome(rs.getString("nome"));
                c.setCpf(rs.getString("cpf"));
                c.setEmail(rs.getString("email"));
                c.setTelefone(rs.getString("telefone"));
                c.setSenha(rs.getString("senha"));
                lista.add(c);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar Funcionário: " + e.getMessage());
        }
        return lista;
    }

    public boolean alterar(Funcionario funcionario) {
        String sql = "UPDATE funcionario SET nome = ?, cpf = ?, email = ?, telefone = ?, senha = ? WHERE id = ?";
        try (Connection conn = ConexaoDAO.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, funcionario.getNome());
            stmt.setString(2, funcionario.getCpf());
            stmt.setString(3, funcionario.getEmail());
            stmt.setString(4, funcionario.getTelefone());
            stmt.setString(5, funcionario.getSenha());
            stmt.setInt(6, funcionario.getId());

            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao alterar funcionario: " + e.getMessage());
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
            System.err.println("Erro ao excluir funcionario: " + e.getMessage());
            return false;
        }
    }

    public Funcionario autenticar(String cpfOuNome, String senha) {
        String sql = "SELECT * FROM funcionario WHERE (nome ? OR senha = ?)";
        try (Connection conn = ConexaoDAO.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpfOuNome);
            stmt.setString(2, cpfOuNome);
            stmt.setString(3, senha);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Funcionario c = new Funcionario();
                    c.setId(rs.getInt("id"));
                    c.setNome(rs.getString("nome"));
                    c.setCpf(rs.getString("cpf"));
                    c.setEmail(rs.getString("email"));
                    c.setTelefone(rs.getString("telefone"));
                    return c;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao autenticar Funcionario: " + e.getMessage());
        }
        return null;
    }
}