package com.mycompany.aplicacao;

public class ItemEmprestimo {
    private Livro livro;
    private boolean devolvido;

    public ItemEmprestimo(Livro livro) {
        this.livro = livro;
        this.devolvido = false;
    }

    public Livro getLivro() { return livro; }
    public boolean isDevolvido() { return devolvido; }

    public void registrarDevolucao() { this.devolvido = true; }
}
