package io.github.cursodsousa.libraryapi.repository;

import io.spring.springdatajpa.libraryapi.service.TransacaoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TransacoesTest {

    @Autowired
    TransacaoService transacaoService;

    /**
     *  Commit -> confirmar as alteracoes
     *  Rollback -> desfazer as alteracoes
     */
    @Test
    void transacaoSimples(){  // Para cada metodo que vai fazzer uma operacao de escrita(alterar BD) no BD precisa do @Transactional
        transacaoService.executar();
    }

    @Test
    void transacaoEstadoManaged(){
        transacaoService.atualizacaoSemAtualizar();
    }
}
