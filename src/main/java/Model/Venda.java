
package Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Venda {
    private int idVenda;
    private Date dataVenda;
    private double valorTotal;
    private String formaPagamento;
    private Cliente cliente;
    private List<ItemVenda> itens;

    public Venda() {
        this.itens = new ArrayList<>();
        this.dataVenda = new Date();
    }

    public int getIdVenda() {
        return idVenda;
    }

    public void setIdVenda(int idVenda) {
        this.idVenda = idVenda;
    }

    public Date getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(Date dataVenda) {
        this.dataVenda = dataVenda;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<ItemVenda> getItens() {
        return itens;
    }

    public void setItens(List<ItemVenda> itens) {
        this.itens = itens;
    }

    public void adicionarItem(ItemVenda item) {
        this.itens.add(item);
        calcularTotal();
    }

    public void calcularTotal() {
        double total = 0.0;
        for (ItemVenda item : itens) {
            total += item.getSubtotal();
        }
        this.valorTotal = total;
    }
}