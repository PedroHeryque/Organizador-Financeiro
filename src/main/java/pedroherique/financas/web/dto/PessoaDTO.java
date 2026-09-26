package pedroherique.financas.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;



public class PessoaDTO {

    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    private String profissao;

    private String telefone;

    @Email(message = "E-mail inválido")
    private String email;

    @Past(message = "A data de nascimento deve estar no passado")
    private LocalDate dataNascimento;

    public PessoaDTO() {
    }

    public PessoaDTO(Long id, String nome, String profissao, String telefone, String email, LocalDate dataNascimento) {
        this.id = id;
        this.nome = nome;
        this.profissao = profissao;
        this.telefone = telefone;
        this.email = email;
        this.dataNascimento = dataNascimento;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getProfissao() {
        return profissao;
    }

    public void setProfissao(String profissao) {
        this.profissao = profissao;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
}