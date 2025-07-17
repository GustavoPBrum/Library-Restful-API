package io.github.cursodsousa.libraryapi.service;

import io.spring.springdatajpa.libraryapi.model.Autor;
import io.spring.springdatajpa.libraryapi.model.GeneroLivro;
import io.spring.springdatajpa.libraryapi.model.Livro;
import io.spring.springdatajpa.libraryapi.repository.AutorRepository;
import io.spring.springdatajpa.libraryapi.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class TransacaoService {

    @Autowired
    AutorRepository autorRepository;
    @Autowired
    LivroRepository livroRepository;

    @Transactional
    public void salarLivroComFoto(){
        // salva o livro
        // repository.save(livro);

        // pega o id do livro = livro.getId();
        // var id = livro.getId();

        // salvar foto do livro -> bucket na nuvem
        // bucketService.salvar(livro.getFoto(), id + ".png");

        // atualizar o nome do arquivo que foi salvo
        //--> Qualquer alteracao nessa entidade no estado MANAGED eh persistido no final quando commita pro BD
        // livro.setNomeArquivoFoto(id + ".png");
    }

    @Transactional
    public void atualizacaoSemAtualizar(){
         var livro = livroRepository
                 .findById(UUID.fromString("5607768d-1c3d-49a2-b715-e547d092b831"))
                 .orElse(null);

         // Como tem a transacao aberta e pegamos os dados do BD e alteramos abaixo, nao precisa do metodo save
         livro.setDataPublicacao(LocalDate.of(2024, 9, 9));  // Ele commita esta alteracao no fim
    }

    @Transactional
    public void executar(){
        // Salva o autor
        Autor autor =  new Autor();
        autor.setNome("Elidio");
        autor.setNacionalidade("Brasil");
        autor.setDataNascimento(LocalDate.of(1970, 4, 15));

        // Salva o autor primeiro para entao setar posteriormente
        //autorRepository.saveAndFlush(autor);
        autorRepository.save(autor);

        // Livro
        Livro livro = new Livro();
        livro.setIsbn("312321-321321");
        livro.setPreco(BigDecimal.valueOf(110));
        livro.setGenero(GeneroLivro.FANTASIA);
        livro.setTitulo("Como Ser um Gajo de Acao");
        livro.setDataPublicacao(LocalDate.of(2025, 6, 19));

        livro.setAutor(autor);

        //livroRepository.saveAndFlush(livro);  // Manda para o BD mas ainda dara rollback
        livroRepository.save(livro);

        if(autor.getNome().equals("Elidio")){
            throw new RuntimeException("Rollback!");
        }

    }
}
