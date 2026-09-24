package DAO;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class ConexaoDAO {

    private static final String NOME_BANCO = "Loja.db";
    private static final String URL = "jdbc:sqlite:" + NOME_BANCO;

    private ConexaoDAO() {
    }

    public static Connection conectar() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("Driver do SQLite não encontrado: " + e.getMessage());
        }

        try {
            Connection conn = DriverManager.getConnection(URL);
            System.out.println("Conexão com o banco SQLite (" + NOME_BANCO + ") estabelecida com sucesso!");
            return conn;
        } catch (SQLException e) {
            System.err.println("Falha ao conectar ao banco de dados SQLite: " + e.getMessage());
            throw e;
        }
    }

    public static String caminhoBanco() {
        return new File(NOME_BANCO).getAbsolutePath();
    }

    public static void inicializarBanco() {
        String sqlCliente = "CREATE TABLE IF NOT EXISTS cliente ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "nome TEXT NOT NULL, "
                + "cpf TEXT UNIQUE NOT NULL, "
                + "email TEXT, "
                + "telefone TEXT, "
                + "senha TEXT NOT NULL"
                + ");";

        String sqlGerente = "CREATE TABLE IF NOT EXISTS gerente ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "nome TEXT NOT NULL, "
                + "cpf TEXT UNIQUE NOT NULL, "
                + "email TEXT, "
                + "departamento TEXT, "
                + "senha TEXT NOT NULL"
                + ");";

        String sqlProduto = "CREATE TABLE IF NOT EXISTS produto ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "nome TEXT NOT NULL, "
                + "categoria TEXT, "
                + "tamanho TEXT, "
                + "cor TEXT, "
                + "preco REAL NOT NULL, "
                + "estoque INTEGER NOT NULL"
                + ");";

        String sqlVenda = "CREATE TABLE IF NOT EXISTS venda ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "cliente_id INTEGER, "
                + "data_venda TEXT NOT NULL, "
                + "total REAL NOT NULL, "
                + "FOREIGN KEY (cliente_id) REFERENCES cliente(id)"
                + ");";

        try (Connection conn = conectar(); Statement stmt = conn.createStatement()) {
            stmt.execute(sqlCliente);
            stmt.execute(sqlGerente);
            stmt.execute(sqlProduto);
            stmt.execute(sqlVenda);
            System.out.println("Tabelas da Loja de Roupas verificadas/criadas com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao criar tabelas no banco de dados: " + e.getMessage());
        }
    }
}