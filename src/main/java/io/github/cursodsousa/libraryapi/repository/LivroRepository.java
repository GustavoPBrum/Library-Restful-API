package io.github.cursodsousa.libraryapi.repository;

import io.spring.springdatajpa.libraryapi.model.Autor;
import io.spring.springdatajpa.libraryapi.model.GeneroLivro;
import io.spring.springdatajpa.libraryapi.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 *  @see LivroRepositoryTest
 */
public interface LivroRepository  extends JpaRepository<Livro, UUID> {

    // Query method  --> declaracao de method
    // Apenas definimos e o JPA ira implementar para nos em tempo de execucao
    List<Livro> findByAutor(Autor autor);  // vai selecionar o livro where l.id_autor == autor (no caso seu id)

    // select * from livro where titulo = titulo
    List<Livro> findByTitulo(String titulo);

    List<Livro> findByIsbn(String isbn);

    List<Livro> findByTituloAndPreco(String titulo, BigDecimal preco);

    List<Livro> findByTituloOrIsbnOrderByTitulo(String titulo, String isbn);

    // select * from livro where data_publicacao between ? and ?
    List<Livro> findByDataPublicacaoBetween(LocalDate inicio, LocalDate fim);

    //  JPQL -> referencia as entidades e suas propriedades
    @Query(" select l from Livro as l order by l.titulo, l.preco ")
    List<Livro> listarTodosOrdernadoTituloAndPreco();

    /**
     * select a.*
     *  from livro l
     *  join autor a on a.id = l.id_autor
     */
    @Query("select a from Livro l join l.autor a")
    List<Autor> listarAutoresDoslivros();

    @Query("select distinct l.titulo from Livro l")
    List<String> listarNomesDiferentesLivros();

    @Query("""
            select l.genero 
            from Livro l
            join l.autor a
            where a.nacionalidade = 'Brasil'
            order by l.genero
            """)
    List<String> listarGenerosAutoresBrasileiros();

    // named parameters -> parametros nomeados
    @Query("select l from Livro l where l.genero = :genero order by :paramOrdenacao")// ':nomeDoParametro'
    List<Livro> findByGenero(
            @Param("genero") GeneroLivro generoLivro,
            @Param("paramOrdenacao") String nomePropriedade);

    // positional parameters (para Querys mais *SIMPLES* e *MENORES* eh o recomendado
    @Query("select l from Livro l where l.genero = ?1 order by ?2")
    List<Livro> findByGeneroPositionalParameters(GeneroLivro generoLivro, String nomePropriedade);

    // <-------------- METODOS DE ***ESCRITA*** DENTRO DO JPA REPOSITORY -------------->
    @Modifying
    @Transactional
    @Query(" delete from Livro where genero = ?1 ")  // -> Positional parameter
    void deleteByGenero(GeneroLivro generoLivro);

    // <-------------- METODOS DE ***ESCRITA*** DENTRO DO JPA REPOSITORY -------------->
    @Modifying
    @Transactional
    @Query(" update Livro set dataPublicacao = ?1 ")
    void updateDataPublicacao(LocalDate novaData);
}
