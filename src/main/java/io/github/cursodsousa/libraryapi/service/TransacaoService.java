package io.github.cursodsousa.libraryapi.service;

import io.github.cursodsousa.libraryapi.model.Autor;
import io.github.cursodsousa.libraryapi.model.GeneroLivro;
import io.github.cursodsousa.libraryapi.model.Livro;
import io.github.cursodsousa.libraryapi.repository.AutorRepository;
import io.github.cursodsousa.libraryapi.repository.LivroRepository;
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

    @Transactional  // Por estar dentro de uma transacao, ao receber o livro com *find*, estara no estado **Managed**,
    // Ao final da transacao, Hibernate faz o dirty checking, dando um commit em tudo feito apos verificacoes.
    // Caso nao estivesse dentro de uma transacao, precisaria chamar **SAVE()** EXPLICITAMENTE
    // Por causa que o EntityManager nao estaria gerenciando o CICLO DE VIDA da ENTIDADE

    public void atualizacaoSemAtualizar(){
         var livro = livroRepository
                 .findById(UUID.fromString("5607768d-1c3d-49a2-b715-e547d092b831"))
                 .orElse(null);

         // Como tem a transacao aberta e pegamos os dados do BD e alteramos abaixo, nao precisa do metodo save
         livro.setDataPublicacao(LocalDate.of(2024, 9, 9));  // Ele commita esta alteracao no fim
    }

    @Transactional  // Annotation necessaria para caso de erro durante a execucao (RuntimeException), caso seja ...
    // ...  interrompida por ter dados inconsistentes, desfazendo a operacao total (Atomicidade -> tudo ou nada)
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
