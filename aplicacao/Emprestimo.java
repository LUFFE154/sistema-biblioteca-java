package com.mycompany.aplicacao;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Emprestimo {
    private int codigo;
    private LocalDate dataEmprestimo;
    private boolean aberto;
    private Leitor leitor;
    private ArrayList<ItemEmprestimo> itens;

    public Emprestimo(int codigo, Leitor leitor) {
        this.codigo = codigo;
        this.leitor = leitor;
        this.dataEmprestimo = LocalDate.now();
        this.aberto = true;
        this.itens = new ArrayList<>();
    }

    public int getCodigo() { return codigo; }
    public LocalDate getDataEmprestimo() { return dataEmprestimo; }
    public boolean isAberto() { return aberto; }
    public Leitor getLeitor() { return leitor; }

    public boolean adicionarLivro(Livro livro) {
        if (livro == null || contemLivro(livro)) return false;
        itens.add(new ItemEmprestimo(livro));
        return true;
    }

    public boolean contemLivro(Livro livro) {
        for (ItemEmprestimo item : itens)
            if (item.getLivro() == livro) return true;
        return false;
    }

    public void finalizar() {
        if (!aberto) return;
        for (ItemEmprestimo item : itens) {
            item.registrarDevolucao();
            item.getLivro().devolver();
        }
        aberto = false;
    }

    @Override
    public String toString() {
        StringBuilder titulos = new StringBuilder();
        for (int i = 0; i < itens.size(); i++) {
            if (i > 0) titulos.append(", ");
            titulos.append(itens.get(i).getLivro().getTitulo());
        }
        return codigo + " - " + leitor.getNome() + " - "
                + dataEmprestimo.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " - "
                + (aberto ? "aberto" : "finalizado") + " - livros: " + titulos;
    }
}
