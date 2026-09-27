package DAO;

import Model.Produto;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProdutoDAOTest {
    
    public ProdutoDAOTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
        ConexaoDAO.inicializarBanco();
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testCadastrar() {
        System.out.println("cadastrar");
        
        Produto produto = new Produto();
        produto.setNome("Camiseta Teste JUnit 5");
        produto.setCategoria("Vestuário");
        produto.setTamanho("G");
        produto.setCor("Preto");
        produto.setPreco(79.90);
        produto.setEstoque(15);
        
        ProdutoDAO instance = new ProdutoDAO();
        boolean result = instance.cadastrar(produto);
        
        assertTrue(result, "O cadastro deve retornar true ao salvar o produto.");
    }

    @Test
    public void testListar() {
        System.out.println("listar");
        
        ProdutoDAO instance = new ProdutoDAO();
        List<Produto> result = instance.listar();
        
        assertNotNull(result, "A lista de produtos não deve ser nula.");
        assertFalse(result.isEmpty(), "A lista deve conter pelo menos um produto.");
    }

    @Test
    public void testAlterar() {
        System.out.println("alterar");
        
        ProdutoDAO instance = new ProdutoDAO();
        List<Produto> lista = instance.listar();
        
        if (!lista.isEmpty()) {
            Produto produto = lista.get(lista.size() - 1);
            produto.setNome("Camiseta Nome Editado");
            produto.setPreco(89.90);
            
            boolean result = instance.alterar(produto);
            assertTrue(result, "A alteração deve retornar true.");
        }
    }

    @Test
    public void testExcluir() {
        System.out.println("excluir");
        
        ProdutoDAO instance = new ProdutoDAO();
        List<Produto> lista = instance.listar();
        
        if (!lista.isEmpty()) {
            Produto produto = lista.get(lista.size() - 1);
            boolean result = instance.excluir(produto.getId());
            
            assertTrue(result, "A exclusão do produto deve retornar true.");
        }
    }
}