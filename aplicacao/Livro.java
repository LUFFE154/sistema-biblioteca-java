package com.mycompany.aplicacao;

public class Livro {
    private int codigo;
    private String titulo;
    private String autor;
    private boolean disponivel;

    public Livro(int codigo, String titulo, String autor) {
        this.codigo = codigo;
        this.titulo = titulo;
        this.autor = autor;
        this.disponivel = true;
    }

    public int getCodigo() { return codigo; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public boolean isDisponivel() { return disponivel; }

    public void atualizarDados(String titulo, String autor) {
        if (titulo != null && !titulo.isBlank() && autor != null && !autor.isBlank()) {
            this.titulo = titulo.trim();
            this.autor = autor.trim();
        }
    }

    public void emprestar() { this.disponivel = false; }

    public void devolver() { this.disponivel = true; }

    @Override
    public String toString() {
        return codigo + " - " + titulo + " (" + autor + ") - "
                + (disponivel ? "disponível" : "emprestado");
    }
}
