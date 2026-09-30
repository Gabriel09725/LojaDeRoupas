package DAO;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class ConexaoDAO {
    private static final String NOME_BANCO = "Lojaa.db";
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
        String sqlFuncionario = "CREATE TABLE IF NOT EXISTS funcionario ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "nome TEXT NOT NULL, "
                + "idade INTEGER, "
                + "cpf TEXT UNIQUE NOT NULL, "
                + "email TEXT, "
                + "telefone TEXT, "
                + "senha TEXT NOT NULL, "
                + "cargo TEXT, "
                + "salario REAL"
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
        String sqlPagamento = "CREATE TABLE IF NOT EXISTS pagamento ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "valor REAL NOT NULL, "
                + "forma_pagamento TEXT NOT NULL, "
                + "data_pagamento TEXT DEFAULT CURRENT_TIMESTAMP"
                + ");";
        try (Connection conn = conectar(); Statement stmt = conn.createStatement()) {
            stmt.execute(sqlCliente);
            stmt.execute(sqlFuncionario);
            stmt.execute(sqlGerente);
            stmt.execute(sqlProduto);
            stmt.execute(sqlPagamento);
            try { stmt.execute("ALTER TABLE funcionario ADD COLUMN idade INTEGER;"); } catch (SQLException e) { /* Coluna já existe */ }
            try { stmt.execute("ALTER TABLE funcionario ADD COLUMN cargo TEXT;"); } catch (SQLException e) { /* Coluna já existe */ }
            try { stmt.execute("ALTER TABLE funcionario ADD COLUMN salario REAL;"); } catch (SQLException e) { /* Coluna já existe */ }
            System.out.println("Tabelas da Loja de Roupas verificadas/criadas com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao criar tabelas no banco de dados: " + e.getMessage());
        }
    }
}