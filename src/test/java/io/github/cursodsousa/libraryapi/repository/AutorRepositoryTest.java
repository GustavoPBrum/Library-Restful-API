package io.github.cursodsousa.libraryapi.repository;

import io.github.cursodsousa.libraryapi.model.Autor;
import io.github.cursodsousa.libraryapi.model.GeneroLivro;
import io.github.cursodsousa.libraryapi.model.Livro;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
public class AutorRepositoryTest {
    @Autowired
    AutorRepository repository;

    @Autowired
    LivroRepository livroRepository;

    @Test
    public void salvarTest(){
        Autor autor =  new Autor();
        autor.setNome("Mario");
        autor.setNacionalidade("Mongolia");
        autor.setDataNascimento(LocalDate.of(1900, 6, 11));

        var autorSalvo = repository.save(autor);  // save retorna um obj do tipo autor
        System.out.println("Autor salvo: " + autorSalvo);
    }

    @Test
    public void atualizarTest(){
        var id = UUID.fromString("bd45a574-4a1e-4665-b395-3e7e7928cfeb");

        Optional<Autor> possivelAutor = repository.findById(id);  // Pode nao existir esse autor

        if(possivelAutor.isPresent()) {

            Autor autorEncontrado = possivelAutor.get();
            System.out.println("Dados do autor:");
            System.out.println(possivelAutor.get());

            autorEncontrado.setDataNascimento(LocalDate.of(1978, 2, 22));

            repository.save(autorEncontrado);  // Tanto salva como atualiza
        }
    }

    @Test
    public void listarTest(){
        List<Autor> lista = repository.findAll();
        lista.forEach(System.out::println);  // Reference method, primeiro a classe e depois de '::' o metodo
    }

    @Test
    public void countTest() {
        System.out.println("Contagem de autores: " + repository.count());
    }

    @Test
    public void deletePorIdTest() {
        var id = UUID.fromString("bd45a574-4a1e-4665-b395-3e7e7928cfeb");

        repository.deleteById(id);
    }

    @Test
    public void deleteTest() {
        var id = UUID.fromString("130693b6-14a0-416e-9c91-80b637c6efe4");
        var maria = repository.findById(id).get();  // Ter cuidado e sempre verificar se esta presente

        repository.delete(maria);  // Deleta o objeto
    }

    @Test
    void salvarAutorComLivrosTest(){
        Autor autor =  new Autor();
        autor.setNome("Monteiro Lobato");
        autor.setNacionalidade("Brasil");
        autor.setDataNascimento(LocalDate.of(1926, 8, 17));

        Livro livro = new Livro();
        livro.setIsbn("32813-31239");
        livro.setPreco(BigDecimal.valueOf(290));
        livro.setGenero(GeneroLivro.CIENCIA);
        livro.setTitulo("Turma da Monica");
        livro.setDataPublicacao(LocalDate.of(1961, 3, 1));
        livro.setAutor(autor);  // Temos que salvar o autor em livro antes de adiciona-lo a lista

        Livro livro2 = new Livro();
        livro2.setIsbn("3274-7544");
        livro2.setPreco(BigDecimal.valueOf(140));
        livro2.setGenero(GeneroLivro.FICCAO);
        livro2.setTitulo("Historia da Turma da Moncia");
        livro2.setDataPublicacao(LocalDate.of(1969, 2, 10));
        livro2.setAutor(autor);

        autor.setLivros(new ArrayList<>());
        autor.getLivros().add(livro);
        autor.getLivros().add(livro2);

        // Ao salvar em modo cascade, ele salvara o Autor no BD junto com os livros associados a ele.
        // Fazendo mais sentido neste caso
        repository.save(autor);

        //livroRepository.saveAll(autor.getLivros());
    }

    @Test
    //@Transactional
    void listarLivrosAutor() {  // Melhor forma de carregar os dados de uma Entidade lazy *SEMPRE USAR LAZY*
        var id = UUID.fromString("3ec3a840-839c-4e70-9775-8e90e17b4bbb");
        var autor = repository.findById(id).get();

        // Buscar os livros do autor
        List<Livro> livrosLista = livroRepository.findByAutor(autor);
        autor.setLivros(livrosLista);

        autor.getLivros().forEach(System.out::println);
    }
}
