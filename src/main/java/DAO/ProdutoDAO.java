package DAO;

import Model.Produto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    public ProdutoDAO() {
        criarTabelaESeedIniciais();
    }

    private void criarTabelaESeedIniciais() {
        String sqlTabela = "CREATE TABLE IF NOT EXISTS produto ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "nome TEXT NOT NULL, "
                + "categoria TEXT NOT NULL, "
                + "tamanho TEXT, "
                + "cor TEXT, "
                + "preco REAL NOT NULL, "
                + "estoque INTEGER NOT NULL"
                + ");";
        try (Connection conn = ConexaoDAO.conectar();
             PreparedStatement stmtTabela = conn.prepareStatement(sqlTabela)) {
            stmtTabela.execute();
            
            // Só carrega os dados padrão se a tabela estiver vazia
            if (tabelaEstaVazia(conn)) {
                carregarNovasOpcoesIniciais(conn);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao inicializar tabela de produtos: " + e.getMessage());
        }
    }

    private boolean tabelaEstaVazia(Connection conn) throws SQLException {
        String sql = "SELECT COUNT(*) FROM produto";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
        }
        return true;
    }

    private void carregarNovasOpcoesIniciais(Connection conn) throws SQLException {
        String sqlInsert = "INSERT INTO produto (nome, categoria, tamanho, cor, preco, estoque) VALUES (?, ?, ?, ?, ?, ?)";
        
        // Novas opções variadas de vestuário
        Object[][] novasRoupas = {
            {"Camiseta Oversized Streetwear", "Camiseta", "G", "Preta", 79.90, 15},
            {"Camiseta Basic Crewneck", "Camiseta", "M", "Branca", 49.90, 25},
            {"Camiseta Regata Esportiva", "Camiseta", "P", "Azul Marinho", 39.90, 20},
            {"Calça Jeans Wide Leg", "Calças", "38", "Azul Claro", 149.90, 10},
            {"Calça Cargo Tactical", "Calças", "40", "Verde Militar", 169.90, 8},
            {"Calça Jogger Moletom", "Calças", "42", "Cinza Mescla", 119.90, 12},
            {"Vestido Estampado Floral", "Vestidos", "M", "Vermelho", 159.90, 7},
            {"Vestido Elegante de Gala", "Vestidos", "P", "Preto", 249.90, 5},
            {"Jaqueta Puffer Impermeável", "Casacos", "GG", "Preta", 289.90, 6},
            {"Moletom Canguru Oversized", "Casacos", "G", "Bege", 179.90, 10},
            {"Tênis Street Casual", "Calçados", "41", "Branco/Preto", 229.90, 9},
            {"Bota Coturno Couro", "Calçados", "39", "Café", 299.90, 4}
        };

        try (PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert)) {
            for (Object[] prod : novasRoupas) {
                stmtInsert.setString(1, (String) prod[0]);
                stmtInsert.setString(2, (String) prod[1]);
                stmtInsert.setString(3, (String) prod[2]);
                stmtInsert.setString(4, (String) prod[3]);
                stmtInsert.setDouble(5, (Double) prod[4]);
                stmtInsert.setInt(6, (Integer) prod[5]);
                stmtInsert.executeUpdate();
            }
            System.out.println("Novas opções de produtos carregadas com sucesso!");
        }
    }

    public boolean cadastrar(Produto produto) {
        String sql = "INSERT INTO produto (nome, categoria, tamanho, cor, preco, estoque) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexaoDAO.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getCategoria());
            stmt.setString(3, produto.getTamanho());
            stmt.setString(4, produto.getCor());
            stmt.setDouble(5, produto.getPreco());
            stmt.setInt(6, produto.getEstoque());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar produto: " + e.getMessage());
            return false;
        }
    }

    public List<Produto> listar() {
        List<Produto> lista = new ArrayList<>();
        String sql = "SELECT * FROM produto";
        try (Connection conn = ConexaoDAO.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Produto p = new Produto();
                p.setId(rs.getInt("id"));
                p.setNome(rs.getString("nome"));
                p.setCategoria(rs.getString("categoria"));
                p.setTamanho(rs.getString("tamanho"));
                p.setCor(rs.getString("cor"));
                p.setPreco(rs.getDouble("preco"));
                p.setEstoque(rs.getInt("estoque"));
                lista.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar produtos: " + e.getMessage());
        }
        return lista;
    }

    public boolean alterar(Produto produto) {
        String sql = "UPDATE produto SET nome=?, categoria=?, tamanho=?, cor=?, preco=?, estoque=? WHERE id=?";
        try (Connection conn = ConexaoDAO.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getCategoria());
            stmt.setString(3, produto.getTamanho());
            stmt.setString(4, produto.getCor());
            stmt.setDouble(5, produto.getPreco());
            stmt.setInt(6, produto.getEstoque());
            stmt.setInt(7, produto.getId());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao alterar produto: " + e.getMessage());
            return false;
        }
    }

    public boolean excluir(int id) {
        String sql = "DELETE FROM produto WHERE id=?";
        try (Connection conn = ConexaoDAO.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao excluir produto: " + e.getMessage());
            return false;
        }
    }

    public List<Produto> buscarPorCategoria(String categoria) {
        List<Produto> lista = new ArrayList<>();
        String sql = "SELECT * FROM produto WHERE categoria LIKE ?";
        try (Connection conn = ConexaoDAO.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + categoria + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Produto p = new Produto();
                    p.setId(rs.getInt("id"));
                    p.setNome(rs.getString("nome"));
                    p.setCategoria(rs.getString("categoria"));
                    p.setTamanho(rs.getString("tamanho"));
                    p.setCor(rs.getString("cor"));
                    p.setPreco(rs.getDouble("preco"));
                    p.setEstoque(rs.getInt("estoque"));
                    lista.add(p);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar produtos por categoria: " + e.getMessage());
        }
        return lista;
    }
}