package com.mycompany.aplicacao;

import java.util.ArrayList;

public class Biblioteca {
    private ArrayList<Leitor> leitores = new ArrayList<>();
    private ArrayList<Livro> livros = new ArrayList<>();
    private ArrayList<Emprestimo> emprestimos = new ArrayList<>();

    public boolean cadastrarLeitor(Leitor leitor) {
        if (leitor == null || leitor.getCodigo() <= 0
                || leitor.getNome() == null || leitor.getNome().isBlank()
                || leitor.getEmail() == null || leitor.getEmail().isBlank()
                || consultarLeitor(leitor.getCodigo()) != null) return false;
        leitores.add(leitor);
        return true;
    }

    public Leitor consultarLeitor(int codigo) {
        for (Leitor leitor : leitores)
            if (leitor.getCodigo() == codigo) return leitor;
        return null;
    }

    public void listarLeitores() {
        if (leitores.isEmpty()) System.out.println("Nenhum leitor cadastrado.");
        for (Leitor leitor : leitores) System.out.println(leitor);
    }

    public boolean removerLeitor(Leitor leitor) {
        if (leitor == null || !leitores.contains(leitor)) return false;
        for (Emprestimo e : emprestimos)
            if (e.isAberto() && e.getLeitor() == leitor) return false;
        return leitores.remove(leitor);
    }

    public boolean cadastrarLivro(Livro livro) {
        if (livro == null || livro.getCodigo() <= 0
                || livro.getTitulo() == null || livro.getTitulo().isBlank()
                || livro.getAutor() == null || livro.getAutor().isBlank()
                || consultarLivro(livro.getCodigo()) != null) return false;
        livros.add(livro);
        return true;
    }

    public Livro consultarLivro(int codigo) {
        for (Livro livro : livros)
            if (livro.getCodigo() == codigo) return livro;
        return null;
    }

    public void listarLivros() {
        if (livros.isEmpty()) System.out.println("Nenhum livro cadastrado.");
        for (Livro livro : livros) System.out.println(livro);
    }

    public boolean removerLivro(Livro livro) {
        if (livro == null || !livros.contains(livro)) return false;
        for (Emprestimo e : emprestimos)
            if (e.isAberto() && e.contemLivro(livro)) return false;
        return livros.remove(livro);
    }

    public boolean registrarEmprestimo(int codigo, Leitor leitor, ArrayList<Livro> livrosEmprestimo) {
        if (codigo <= 0 || buscarEmprestimo(codigo) != null) return false;
        if (leitor == null || !leitores.contains(leitor)) return false;
        if (livrosEmprestimo == null || livrosEmprestimo.isEmpty()) return false;
        ArrayList<Livro> vistos = new ArrayList<>();
        for (Livro livro : livrosEmprestimo) {
            if (livro == null || !livros.contains(livro) || !livro.isDisponivel()
                    || vistos.contains(livro)) return false;
            vistos.add(livro);
        }
        Emprestimo emprestimo = new Emprestimo(codigo, leitor);
        for (Livro livro : livrosEmprestimo) {
            if (emprestimo.adicionarLivro(livro)) livro.emprestar();
        }
        emprestimos.add(emprestimo);
        return true;
    }

    public void listarEmprestimos() {
        if (emprestimos.isEmpty()) System.out.println("Nenhum empréstimo registrado.");
        for (Emprestimo e : emprestimos) System.out.println(e);
    }

    public boolean registrarDevolucao(int codigo) {
        Emprestimo emprestimo = buscarEmprestimo(codigo);
        if (emprestimo == null || !emprestimo.isAberto()) return false;
        emprestimo.finalizar();
        return true;
    }

    private Emprestimo buscarEmprestimo(int codigo) {
        for (Emprestimo e : emprestimos)
            if (e.getCodigo() == codigo) return e;
        return null;
    }
}
