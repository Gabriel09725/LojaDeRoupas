package DAO;

import Model.Pagamento;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class PagamentoDAO {

    public boolean salvar(Pagamento pagamento) {
        String sql = "INSERT INTO pagamento (valor, forma_pagamento, data_pagamento) VALUES (?, ?, ?)";

        try (Connection conn = ConexaoDAO.getConectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, pagamento.getValor());
            stmt.setString(2, pagamento.getFormaPagamento());
            stmt.setTimestamp(3, new java.sql.Timestamp(pagamento.getDataPagamento().getTime()));

            stmt.executeUpdate();
            return true;

        } catch (Exception e) {
            System.err.println("Erro ao salvar pagamento: " + e.getMessage());
            return false;
        }
    }
}