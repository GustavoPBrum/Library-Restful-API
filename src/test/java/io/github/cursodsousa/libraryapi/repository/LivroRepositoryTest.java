package io.github.cursodsousa.libraryapi.repository;

import io.spring.springdatajpa.libraryapi.model.Autor;
import io.spring.springdatajpa.libraryapi.model.GeneroLivro;
import io.spring.springdatajpa.libraryapi.model.Livro;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@SpringBootTest
class LivroRepositoryTest {  // Nao ha necessidade de Test ser publico

    @Autowired
    LivroRepository repository;

    @Autowired
    AutorRepository autorRepository;

    @Test
    void salvarTest() {
        Livro livro = new Livro();
        livro.setIsbn("21312-25324");
        livro.setPreco(BigDecimal.valueOf(121));
        livro.setGenero(GeneroLivro.MISTERIO);
        livro.setTitulo("Livro Substituto");
        livro.setDataPublicacao(LocalDate.of(1950, 1, 21));

        // Busca um autor ja salvo para salvar posteriormente com livro
        Autor autor = autorRepository.findById(UUID.fromString("55e59feb-ef27-4bf7-8b9c-ffb95233fc33")).orElse((null));
        ///livro.setAutor(autor);

        repository.save(livro);
    }

    @Test
    void salvarAutorELivroTest() {  // Opcao padrao
        Livro livro = new Livro();
        livro.setIsbn("312321-321321");
        livro.setPreco(BigDecimal.valueOf(110));
        livro.setGenero(GeneroLivro.FANTASIA);
        livro.setTitulo("Junim Plays");
        livro.setDataPublicacao(LocalDate.of(1980, 2, 12));

        Autor autor =  new Autor();
        autor.setNome("Pedrin");
        autor.setNacionalidade("Brasil");
        autor.setDataNascimento(LocalDate.of(1900, 1, 3));

        // Salva o autor primeiro para entao setar posteriormente
        autorRepository.save(autor);
        livro.setAutor(autor);

        repository.save(livro);
    }

    @Test
    void salvarCascadeTest() {
        Livro livro = new Livro();
        livro.setIsbn("21312-25324");
        livro.setPreco(BigDecimal.valueOf(121));
        livro.setGenero(GeneroLivro.FANTASIA);
        livro.setTitulo("UFO");
        livro.setDataPublicacao(LocalDate.of(1980, 1, 21));

        Autor autor =  new Autor();
        autor.setNome("Joao");
        autor.setNacionalidade("Brasil");
        autor.setDataNascimento(LocalDate.of(1980, 2, 22));

        livro.setAutor(autor);

        repository.save(livro);
    }

    @Test
    void atualizarAutorLivro() {
        UUID id = UUID.fromString("fbbc0275-22e0-4df9-9bd3-6506b34127bb");
        var livroParaAtualizar = repository.findById(id).orElse(null);

        UUID idAutor = UUID.fromString("b4c7ba60-5a91-417a-90c4-c52579fc5b1f");
        Autor autor = autorRepository.findById(idAutor).orElse(null);  // Autor mario

        livroParaAtualizar.setAutor(autor);  // Fazemos a troca de autores

        repository.save(livroParaAtualizar);
    }

    @Test
    void deletar() {
        UUID id = UUID.fromString("69555ab5-6e0a-41ac-8d71-f90e744e0ad6");
        repository.deleteById(id);
    }

    @Test
    void deletarCascade() {  // Alem de deletar o livro, deleta o autor junto
        UUID id = UUID.fromString("69555ab5-6e0a-41ac-8d71-f90e744e0ad6");
        repository.deleteById(id);
    }

    @Test
    @Transactional  // Executa operacoes no banco, so fecha a transacao quando terminar o metodo
    void buscarLivroTest(){
        UUID id = UUID.fromString("fbbc0275-22e0-4df9-9bd3-6506b34127bb");
        Livro livro = repository.findById(id).orElse(null);

        System.out.println("Livro:");
        System.out.println(livro.getTitulo());

        System.out.println("Autor:");
        // Vai se precisar, acessar o banco *BUSCANDO APENAS O AUTOR APENAS SE ESTIVER DENTRO DE UMA TRANSACAO*
        System.out.println(livro.getAutor().getNome());
    }

    @Test
    void pesquisaPorTituloTest(){
        List<Livro> lista = repository.findByTitulo("Turma da Monica");
        lista.forEach(System.out::println);
    }

    @Test
    void pesquisaPorIsbnTest(){
        List<Livro> lista = repository.findByIsbn("3274-7544");
        lista.forEach(System.out::println);
    }

    @Test
    void pesquisaPorTituloAndPrecoTest(){
        var preco = BigDecimal.valueOf(290.00);

        List<Livro> lista = repository.findByTituloAndPreco("Turma da Monica", preco);
        lista.forEach(System.out::println);
    }

    @Test
    void listarLivrosComQueryJPQLTest(){
        var resultado = repository.listarTodosOrdernadoTituloAndPreco();
        resultado.forEach(System.out::println);
    }

    @Test
    void listarAutoresDosLivrosTest() {
        var resultado = repository.listarAutoresDoslivros();
        resultado.forEach(System.out::println);
    }

    @Test
    void listarLivrosTitulosDiferentesTest() {
        var resultado = repository.listarNomesDiferentesLivros();
        resultado.forEach(System.out::println);
    }

    @Test
    void listarGenerosAutoresBrasileirosTest(){
        var resultado = repository.listarGenerosAutoresBrasileiros();
        resultado.forEach(System.out::println);
    }

    @Test
    void listarLivrosGenerosQueryParamTest(){
        var resultado = repository.findByGenero(GeneroLivro.FICCAO, "preco");
        resultado.forEach(System.out::println);
    }

    @Test
    void listarLivrosGenerosPositionalTest(){
        var resultado = repository.findByGenero(GeneroLivro.FICCAO, "preco");
        resultado.forEach(System.out::println);
    }

    @Test
    void deletePorGeneroTest(){
        repository.deleteByGenero(GeneroLivro.FANTASIA);
    }

    @Test
    void updateDataPublicacaoTest(){
        repository.updateDataPublicacao(LocalDate.of(2003, 1, 15));
    }
}