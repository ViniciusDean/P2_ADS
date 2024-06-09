package br.com.fatec.model;

public class VendaProduto {

    private Venda venda;
    private Produto produto;
    private int quantidade;

    public VendaProduto() {
    }

    public VendaProduto(Produto produto, int quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
    }

    public VendaProduto(Venda venda, Produto produto, int quantidade) {
        this.venda = venda;
        this.produto = produto;
        this.quantidade = quantidade;
    }

    public Venda getVenda() {
        return venda;
    }

    public void setVenda(Venda venda) {
        this.venda = venda;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (this.venda != null ? this.venda.hashCode() : 0);
        hash = 31 * hash + (this.produto != null ? this.produto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final VendaProduto other = (VendaProduto) obj;
        if (this.venda != null ? !this.venda.equals(other.venda) : other.venda != null) {
            return false;
        }
        return this.produto != null ? this.produto.equals(other.produto) : other.produto == null;
    }

    @Override
    public String toString() {
        return "VendaProduto{" + "venda=" + venda + ", produto=" + produto + ", quantidade=" + quantidade + '}';
    }
}
