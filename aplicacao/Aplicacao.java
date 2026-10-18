package com.mycompany.aplicacao;

import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Aplicacao {
    private static final int LARGURA = 46;

    private static final boolean CORES =
            Boolean.parseBoolean(System.getProperty("app.cores", "true"));
    private static final boolean QOL =
            Boolean.parseBoolean(System.getProperty("app.qol", "true"));
    private static final String RESET    = "\u001B[0m";
    private static final String NEGRITO  = "\u001B[1m";
    private static final String CIANO    = "\u001B[36m";
    private static final String AMARELO  = "\u001B[33m";
    private static final String VERDE    = "\u001B[32m";
    private static final String VERMELHO = "\u001B[31m";
    private static final String CINZA    = "\u001B[90m";

    private final Scanner scanner;
    private final Biblioteca biblioteca = new Biblioteca();

    private int totalLeitores = 0;
    private int totalLivros = 0;
    private int emprestimosAbertos = 0;

    public Aplicacao() {
        configurarSaida();
        this.scanner = new Scanner(System.in, StandardCharsets.UTF_8);
    }

    public static void main(String[] args) {
        new Aplicacao().executar();
    }

    private static void configurarSaida() {
        try {
            System.setOut(new PrintStream(System.out, true, detectarEncoding()));
        } catch (Exception ignored) {
        }
    }

    private static Charset detectarEncoding() {
        if (!System.getProperty("os.name", "").toLowerCase().contains("win")) {
            return StandardCharsets.UTF_8;
        }
        try {
            Process p = new ProcessBuilder("cmd", "/c", "chcp")
                    .redirectErrorStream(true).start();
            String saida = new String(p.getInputStream().readAllBytes(),
                    StandardCharsets.ISO_8859_1);
            p.waitFor();
            Matcher m = Pattern.compile("(\\d{3,5})").matcher(saida);
            if (m.find()) {
                int cp = Integer.parseInt(m.group(1));
                if (cp == 65001) return StandardCharsets.UTF_8;
                for (String nome : new String[]{"windows-" + cp, "Cp" + cp}) {
                    try { return Charset.forName(nome); } catch (Exception ignored) {}
                }
            }
        } catch (Exception ignored) {}
        try { return Charset.forName("Cp850"); } catch (Exception ignored) {}
        return StandardCharsets.UTF_8;
    }

    public void executar() {
        boolean sair = false;
        while (!sair) {
            limparConsole();
            exibirMenu();
            try {
                int opcao = Integer.parseInt(scanner.nextLine().trim());
                sair = executarOpcao(opcao);
                if (!sair) pausar();
            } catch (NumberFormatException e) {
                falha("Opção inválida.");
                pausar();
            }
        }
        scanner.close();
    }

    private boolean executarOpcao(int opcao) {
        switch (opcao) {
            case 0:
                if (confirmar("Deseja realmente sair")) {
                    sucesso("Aplicação encerrada.");
                    return true;
                }
                aviso("Retornando ao menu.");
                return false;
            case 1:  cadastrarLeitor();              return false;
            case 2:  consultarLeitor();              return false;
            case 3:  biblioteca.listarLeitores();    return false;
            case 4:  atualizarLeitor();              return false;
            case 5:  removerLeitor();                return false;
            case 6:  cadastrarLivro();               return false;
            case 7:  consultarLivro();               return false;
            case 8:  biblioteca.listarLivros();      return false;
            case 9:  atualizarLivro();               return false;
            case 10: removerLivro();                 return false;
            case 11: registrarEmprestimo();          return false;
            case 12: biblioteca.listarEmprestimos(); return false;
            case 13: registrarDevolucao();           return false;
            default: falha("Opção inválida.");       return false;
        }
    }

    private void limparConsole() {
        if (!QOL) return;
        try {
            if (System.getProperty("os.name", "").toLowerCase().contains("win")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            for (int i = 0; i < 30; i++) System.out.println();
        }
    }

    private void pausar() {
        if (!QOL) return;
        System.out.print(c(CINZA, "\nPressione ENTER para continuar..."));
        scanner.nextLine();
    }

    private boolean confirmar(String msg) {
        if (!QOL) return true;
        System.out.print(c(AMARELO, msg + "? (S/N): "));
        String r = scanner.nextLine().trim();
        return r.equalsIgnoreCase("S") || r.equalsIgnoreCase("SIM");
    }

    private static String c(String ansi, String txt) {
        return CORES ? ansi + txt + RESET : txt;
    }

    private void sucesso(String msg) { System.out.println(c(VERDE, msg)); }
    private void falha(String msg)   { System.out.println(c(VERMELHO, msg)); }
    private void aviso(String msg)   { System.out.println(c(AMARELO, msg)); }
    private void info(String msg)    { System.out.println(c(CINZA, msg)); }

    private void borda() {
        System.out.println(c(CIANO, "+" + "-".repeat(LARGURA) + "+"));
    }

    private void centro(String txt, String cor) {
        int sobra = LARGURA - txt.length();
        int esq = Math.max(0, sobra / 2);
        int dir = Math.max(0, sobra - esq);
        String conteudo = (cor == null) ? txt : c(cor, txt);
        System.out.println(c(CIANO, "|")
                + " ".repeat(esq) + conteudo + " ".repeat(dir)
                + c(CIANO, "|"));
    }

    private void exibirMenu() {
        borda();
        centro("SISTEMA BIBLIOTECA", NEGRITO + AMARELO);
        borda();
        centro(LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")), CINZA);
        borda();

        String resumo = totalLeitores + " leitor(es)  |  "
                      + totalLivros + " livro(s)  |  "
                      + emprestimosAbertos + " em aberto";
        centro(resumo, CINZA);
        borda();
        System.out.println();

        caixa("LEITOR",
                " 1 - Cadastrar leitor",
                " 2 - Consultar leitor",
                " 3 - Listar leitores",
                " 4 - Atualizar leitor",
                " 5 - Remover leitor");

        caixa("LIVROS",
                " 6 - Cadastrar livro",
                " 7 - Consultar livro",
                " 8 - Listar livros",
                " 9 - Atualizar livro",
                "10 - Remover livro");

        caixa("EMPRÉSTIMO",
                "11 - Registrar empréstimo",
                "12 - Listar empréstimos",
                "13 - Registrar devolução");

        caixa("SAIR",
                " 0 - Sair");

        System.out.print(c(NEGRITO, "Opção: "));
    }

    private void caixa(String titulo, String... opcoes) {
        borda();
        centro(titulo, NEGRITO + AMARELO);
        borda();
        for (String o : opcoes) centro(o, null);
        borda();
        System.out.println();
    }

    private int lerInt(String msg) {
        System.out.print(msg);
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private String lerTexto(String msg) {
        System.out.print(msg);
        return scanner.nextLine().trim();
    }

    private void cadastrarLeitor() {
        int codigo = lerInt("Código: ");
        String nome = lerTexto("Nome: ");
        String email = lerTexto("E-mail: ");
        if (biblioteca.cadastrarLeitor(new Leitor(codigo, nome, email))) {
            totalLeitores++;
            sucesso("Leitor cadastrado.");
        } else {
            falha("Não foi possível cadastrar o leitor.");
        }
    }

    private void consultarLeitor() {
        Leitor l = biblioteca.consultarLeitor(lerInt("Código: "));
        if (l != null) info(l.toString());
        else falha("Registro não encontrado.");
    }

    private void atualizarLeitor() {
        Leitor l = biblioteca.consultarLeitor(lerInt("Código: "));
        if (l == null) { falha("Registro não encontrado."); return; }
        info(l.toString());
        String nome = lerTexto("Novo nome: ");
        String email = lerTexto("Novo e-mail: ");
        if (nome.isBlank() || email.isBlank()) { falha("Dados inválidos."); return; }
        l.atualizarDados(nome, email);
        sucesso("Dados do leitor atualizados.");
    }

    private void removerLeitor() {
        Leitor l = biblioteca.consultarLeitor(lerInt("Código: "));
        if (l == null) { falha("Registro não encontrado."); return; }
        info(l.toString());
        if (!confirmar("Confirma remoção do leitor")) {
            aviso("Operação cancelada.");
            return;
        }
        if (biblioteca.removerLeitor(l)) {
            totalLeitores--;
            sucesso("Leitor removido.");
        } else {
            falha("Leitor não removido. Há empréstimo aberto.");
        }
    }

    private void cadastrarLivro() {
        int codigo = lerInt("Código: ");
        String titulo = lerTexto("Título: ");
        String autor = lerTexto("Autor: ");
        if (biblioteca.cadastrarLivro(new Livro(codigo, titulo, autor))) {
            totalLivros++;
            sucesso("Livro cadastrado.");
        } else {
            falha("Não foi possível cadastrar o livro.");
        }
    }

    private void consultarLivro() {
        Livro l = biblioteca.consultarLivro(lerInt("Código: "));
        if (l != null) info(l.toString());
        else falha("Registro não encontrado.");
    }

    private void atualizarLivro() {
        Livro l = biblioteca.consultarLivro(lerInt("Código: "));
        if (l == null) { falha("Registro não encontrado."); return; }
        info(l.toString());
        String titulo = lerTexto("Novo título: ");
        String autor = lerTexto("Novo autor: ");
        if (titulo.isBlank() || autor.isBlank()) { falha("Dados inválidos."); return; }
        l.atualizarDados(titulo, autor);
        sucesso("Dados do livro atualizados.");
    }

    private void removerLivro() {
        Livro l = biblioteca.consultarLivro(lerInt("Código: "));
        if (l == null) { falha("Registro não encontrado."); return; }
        info(l.toString());
        if (!confirmar("Confirma remoção do livro")) {
            aviso("Operação cancelada.");
            return;
        }
        if (biblioteca.removerLivro(l)) {
            totalLivros--;
            sucesso("Livro removido.");
        } else {
            falha("Livro não removido. Há empréstimo aberto.");
        }
    }

    private void registrarEmprestimo() {
        int codigo = lerInt("Código do empréstimo: ");
        Leitor leitor = biblioteca.consultarLeitor(lerInt("Código do leitor: "));
        if (leitor == null) { falha("Registro não encontrado."); return; }
        info(leitor.toString());

        ArrayList<Livro> livros = new ArrayList<>();
        while (true) {
            int cod = lerInt("Código do livro (0 para encerrar): ");
            if (cod == 0) break;
            if (cod < 0) { falha("Código inválido."); continue; }

            Livro livro = biblioteca.consultarLivro(cod);
            if (livro == null) {
                falha("Registro não encontrado.");
            } else if (livros.contains(livro)) {
                aviso("Livro já adicionado.");
            } else {
                livros.add(livro);
                sucesso("Adicionado: " + livro.getTitulo());
            }
        }

        if (biblioteca.registrarEmprestimo(codigo, leitor, livros)) {
            emprestimosAbertos++;
            sucesso("Empréstimo registrado.");
            info("Total: " + livros.size() + " livro(s) no empréstimo " + codigo + ".");
        } else {
            falha("Não foi possível registrar o empréstimo.");
        }
    }

    private void registrarDevolucao() {
        if (biblioteca.registrarDevolucao(lerInt("Código do empréstimo: "))) {
            emprestimosAbertos--;
            sucesso("Devolução registrada.");
        } else {
            falha("Não foi possível registrar a devolução.");
        }
    }
}