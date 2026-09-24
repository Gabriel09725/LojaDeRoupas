/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Rafael
 */
public final class Sessao {

    private static boolean clienteLogado;
    private static boolean adminLogado;

    private Sessao() {
    }

    public static void loginCliente() {
        clienteLogado = true;
        adminLogado = false;
    }

    public static void loginAdmin() {
        adminLogado = true;
        clienteLogado = false;
    }

    public static void logout() {
        clienteLogado = false;
        adminLogado = false;
    }

    public static boolean isClienteLogado() {
        return clienteLogado;
    }

    public static boolean isAdminLogado() {
        return adminLogado;
    }
}
