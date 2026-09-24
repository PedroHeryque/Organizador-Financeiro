package pedroherique.financas.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuração de segurança da API.
 *
 * CSRF é desativado propositalmente: essa proteção existe para aplicações
 * que usam sessão de navegador com cookies (formulários HTML tradicionais).
 * Nossa API é stateless e usa Basic Auth em cada requisição — não há sessão
 * para um ataque CSRF explorar, então a proteção não se aplica e só bloquearia
 * requisições legítimas de ferramentas como Postman ou um front-end via fetch/axios.
 */
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}