package io.github.cursodsousa.libraryapi.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "livro")
@Data  // --> Composto por GetterSetter, ToString, EqualsAndHashCode e RequiredArgsConstructor
//@NoArgsConstructor --> construtor sem argumento
//@AllArgsConstructor --> construtor com todos argumentos
//@RequiredArgsConstructor --> construtor com as propriedades final
@ToString(exclude = "autor")  // Loop infinito, imprimia o toString do autor, o autor imprimia o proprio toString, que chamava o livro de novo7...
@EntityListeners(AuditingEntityListener.class)  // Escuta toda vez que fizer op na entidade e olhar as annotations
public class Livro {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "isbn", length = 20, nullable = false)
    private String isbn;

    @Column(name = "titulo", length = 150, nullable = false)
    private String titulo;

    @Column(name = "data_publicacao")
    private LocalDate dataPublicacao;

    @Enumerated(EnumType.STRING)
    @Column(name = "genero", length = 30, nullable = false)
    private GeneroLivro genero;

    @Column(name = "preco", precision = 18, scale = 2)  // 18 posicoes onde 2 sao decimais
    private BigDecimal preco;
    // private BigDecimal;  --> Melhor para trablhar com valores monetario(precisao maior)

    // --> muitos livros para 1 autor (many da entidade atual) (one da entidade mapeada)
    @ManyToOne(//cascade = CascadeType.ALL
            fetch = FetchType.LAZY)  // Em relacionamento ...ToOne, nao vai trazer os dados do autor (neste caso), apenas do livro
    @JoinColumn(name = "id_autor")
    private Autor autor;

    @CreatedDate  // --> Ja persiste a data atual nesse campo
    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro;

    @LastModifiedDate
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @Column(name = "id_usuario")
    private UUID idUsuario;
}