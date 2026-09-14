package pedroherique.financas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name =" tb_pessoa")

public class Pessoa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @NotBlank(message = "O nome  é obrigatorio  ")
    @Column(nullable = false)

    private String nome;
    private String profissao;
    private String telefone;

    @Email(message = "E-mail invalido")
    private String email;

    @Past(message = "A data de nascimento deve estar no passado")
    private LocalDate dataNascimento;

    @Column(nullable = false)
    private LocalDateTime dataCadastro;

    public Pessoa() {
    }
    public Pessoa(String pessoaTeste, String profissãoTeste, String s, String mail, Object o) {

    }
    public  Pessoa(Long id, String nome, String profissao, String telefone, String email,LocalDate dataNascimento) {
        this.id = id;
        this.nome = nome;
        this.profissao = profissao;
        this.telefone = telefone;
        this.email = email;
        this.dataNascimento = dataNascimento;
    }

    @PrePersist
    public void salvarData(){
        this.dataCadastro = LocalDateTime.now();
    }

    public void setId( Long id){
        this.id = id;
    }
    public Long getId(){
        return this.id;
    }
    public void setNome(String nome){
        this.nome = nome;
    }
    public String getNome(){
        return this.nome;
    }
    public  void setProfissao(String profissao){
        this.profissao = profissao;
    }
    public String getProfissao(){
        return this.profissao;
    }
    public void setTelefone(String telefone){
        this.telefone = telefone;
    }
    public String getTelefone(){
        return this.telefone;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public String geteEmail(){
        return this.email;
    }
    public void setDataNascimento(LocalDate dataNascimento){
        this.dataNascimento = dataNascimento;
    }
    public LocalDate getDataNascimento(){
        return this.dataNascimento;
    }
}



