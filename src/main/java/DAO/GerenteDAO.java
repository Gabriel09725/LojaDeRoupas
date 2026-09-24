package DAO;

import Model.Gerente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GerenteDAO {

    public boolean cadastrar(Gerente gerente) {
        String sql = "INSERT INTO gerente (nome, cpf, email, departamento, senha) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexaoDAO.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, gerente.getNome());
            stmt.setString(2, gerente.getCpf());
            stmt.setString(3, gerente.getEmail());
            stmt.setString(4, gerente.getDepartamento());
            stmt.setString(5, gerente.getSenha());

            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar gerente: " + e.getMessage());
            return false;
        }
    }

    public List<Gerente> listar() {
        List<Gerente> lista = new ArrayList<>();
        String sql = "SELECT * FROM gerente";

        try (Connection conn = ConexaoDAO.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Gerente g = new Gerente();
                g.setId(rs.getInt("id"));
                g.setNome(rs.getString("nome"));
                g.setCpf(rs.getString("cpf"));
                g.setEmail(rs.getString("email"));
                g.setDepartamento(rs.getString("departamento"));
                g.setSenha(rs.getString("senha"));
                lista.add(g);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar gerentes: " + e.getMessage());
        }
        return lista;
    }

    public Gerente autenticar(String cpfOuNome, String senha) {
        String sql = "SELECT * FROM gerente WHERE (cpf = ? OR nome = ?) AND senha = ?";
        try (Connection conn = ConexaoDAO.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpfOuNome);
            stmt.setString(2, cpfOuNome);
            stmt.setString(3, senha);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Gerente g = new Gerente();
                    g.setId(rs.getInt("id"));
                    g.setNome(rs.getString("nome"));
                    g.setCpf(rs.getString("cpf"));
                    g.setEmail(rs.getString("email"));
                    g.setDepartamento(rs.getString("departamento"));
                    return g;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao autenticar gerente: " + e.getMessage());
        }
        return null;
    }
}