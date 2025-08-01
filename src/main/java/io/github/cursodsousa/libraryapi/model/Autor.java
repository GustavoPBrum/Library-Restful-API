package io.github.cursodsousa.libraryapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity  // Obrigatorio para dizer que eh um mapeamento JPA dessa entidade
@Table(name = "autor", schema = "public")
@Getter
@Setter  // --> Em tempo de COMPILACAO sera gerado os GETTERS E SETTER dessa entidade
@ToString(exclude = "livros")
@EntityListeners(AuditingEntityListener.class) // Fica escutando toda vez que fizer op na entidade e olhar as annotations
public class Autor {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)  // Diz que esse id vai ser gerado automaticamente
    private UUID id;

    // Essas config nao sao OBRIGATORIAS, no script caso esquecermos de mandar uma propriedade ou maior que 100 caracteres
    // Daria erro de SQL. MAS faz uma BOA definicao de ENTIDADE
    @Column(name = "nome", length = 100, nullable = false)  // Tamanho maximo e nao pode ser nulo
    private String nome;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @Column(name = "nacionalidade", length = 50, nullable = false)
    private String nacionalidade;

    // Se nao houver um construtor, automaticamente tera um construtor vazio

    // mappedBy diz que nao eh uma coluna e sim apenas um mapeamento OneToMany
    // Por padrao OneToMany eh Lazy
    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Livro> livros;

    // Guarda a data e hora diferentemente de LocalDate
    @CreatedDate  // --> Ja persiste a data atual nesse campo
    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro;

    @LastModifiedDate
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @Column(name = "id_usuario")
    private UUID idUsuario;
}
