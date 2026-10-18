# Sistema de Biblioteca

Trabalho de Programação Orientada a Objetos em Java. Sistema de terminal para gerenciar leitores, livros, empréstimos e devoluções de uma biblioteca comunitária.

## Como rodar

Precisa ter **Java 11+** instalado.

**Windows:** duplo clique em `executar.bat`.

**Linux / macOS:**
```bash
java -jar aplicativo.jar
```

## O que dá pra fazer

- Cadastrar, consultar, listar, atualizar e remover **leitores**
- Cadastrar, consultar, listar, atualizar e remover **livros**
- Registrar **empréstimos** com um ou mais livros
- Registrar **devoluções**
- Menu com cores, contadores e confirmações

## Flags opcionais

| Flag | Efeito |
|---|---|
| `-Dapp.cores=false` | Desliga as cores |
| `-Dapp.qol=false` | Desliga limpar tela e pausas (bom para testes) |

```bash
java -Dapp.qol=false -jar aplicativo.jar
```

## Estrutura

```text
├── aplicacao/          # código-fonte (.java)
├── aplicativo.jar      # executável pronto
├── executar.bat        # roda no Windows
├── README.md
└── LICENSE
```

## Autor

Luiz

## Licença

MIT — veja [LICENSE](LICENSE).
