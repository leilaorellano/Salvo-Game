package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.Repository.GameRepository;
import com.codeoftheweb.salvo.Repository.GamePlayerRepository;
import com.codeoftheweb.salvo.Repository.PlayerRepository;
import com.codeoftheweb.salvo.Repository.SalvoRepository;
import com.codeoftheweb.salvo.Repository.ScoreRepository;
import com.codeoftheweb.salvo.Repository.ShipRepository;
import com.codeoftheweb.salvo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;

@SpringBootApplication
public class SalvoApplication extends SpringBootServletInitializer {


    public static void main(String[] args) {
        SpringApplication.run(SalvoApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(PlayerRepository playerRepository, GameRepository gameRepository, GamePlayerRepository gamePlayerRepository,
                                      ShipRepository shipRepository, SalvoRepository salvoRepository, ScoreRepository scoreRepository) {
        return (args) -> {
            //No olvidar poner todos los repositorios en el CommandLineRunner!!!!!!
            //Players
            Player player1 = playerRepository.save(new Player("Jack@gmail.com", passwordEncoder().encode("24")));
            Player player2 = playerRepository.save(new Player("Chloe@gmail.com", passwordEncoder().encode("42")));
            Player player3 = playerRepository.save(new Player("Kim@gmail.com", passwordEncoder().encode("kb")));
            Player player4 = playerRepository.save(new Player("Messi@gmail.com", passwordEncoder().encode("mole")));
            Player player5 = playerRepository.save(new Player("Leila@gmail.com", passwordEncoder().encode("18")));
            Player player6 = playerRepository.save(new Player("Meredith@gmail.com", passwordEncoder().encode("derek")));

            //Games
            Game game1 = gameRepository.save(new Game(LocalDateTime.now()));
            Game game2 = gameRepository.save(new Game(LocalDateTime.now().plusHours(1)));
            Game game3 = gameRepository.save(new Game(LocalDateTime.now().plusHours(2)));
            Game game4 = gameRepository.save(new Game(LocalDateTime.now().plusHours(3)));
            Game game5 = gameRepository.save(new Game(LocalDateTime.now().plusHours(4)));
            Game game6 = gameRepository.save(new Game(LocalDateTime.now().plusHours(5)));
            Game game7 = gameRepository.save(new Game(LocalDateTime.now().plusHours(6)));
            Game game8 = gameRepository.save(new Game(LocalDateTime.now().plusHours(7)));
            //Game game4 = gameRepository.save(new Game(LocalDateTime.now().plusHours(3)));
            //Game game5 = gameRepository.save(new Game(LocalDateTime.now().plusHours(4)));


            //GamePlayers
            GamePlayer gp1 = gamePlayerRepository.save(new GamePlayer(player1, game1));
            GamePlayer gp2 = gamePlayerRepository.save(new GamePlayer(player2, game1));

            GamePlayer gp3 = gamePlayerRepository.save(new GamePlayer(player1, game2));
            GamePlayer gp4 = gamePlayerRepository.save(new GamePlayer(player2, game2));

            GamePlayer gp5 = gamePlayerRepository.save(new GamePlayer(player3, game3));
            GamePlayer gp6 = gamePlayerRepository.save(new GamePlayer(player4, game3));

            GamePlayer gp7 = gamePlayerRepository.save(new GamePlayer(player3, game4));
            GamePlayer gp8 = gamePlayerRepository.save(new GamePlayer(player4, game4));

            GamePlayer gp9  = gamePlayerRepository.save(new GamePlayer(player5, game5));
            GamePlayer gp10 = gamePlayerRepository.save(new GamePlayer(player6, game5));

            GamePlayer gp11 = gamePlayerRepository.save(new GamePlayer(player5, game6));
            GamePlayer gp12 = gamePlayerRepository.save(new GamePlayer(player6, game6));

            GamePlayer gp13 = gamePlayerRepository.save(new GamePlayer(player5, game7));
            GamePlayer gp14 = gamePlayerRepository.save(new GamePlayer(player6, game7));

            GamePlayer gp15 = gamePlayerRepository.save(new GamePlayer(player5, game8));
            GamePlayer gp16 = gamePlayerRepository.save(new GamePlayer(player6, game8));

            //Ships
            ArrayList<String> shipPosition1 = new ArrayList<>(Arrays.asList("B5", "B6", "B7", "B8", "B9"));
            ArrayList<String> shipPosition2 = new ArrayList<>(Arrays.asList("C5", "C6", "C7"));
            ArrayList<String> shipPosition3 = new ArrayList<>(Arrays.asList("F3", "G3", "H3"));
            ArrayList<String> shipPosition4 = new ArrayList<>(Arrays.asList("G4", "G5", "G6", "G7"));
            ArrayList<String> shipPosition5 = new ArrayList<>(Arrays.asList("E1", "F1", "G1"));
            ArrayList<String> shipPosition6 = new ArrayList<>(Arrays.asList("B4", "B5"));
            ArrayList<String> shipPosition7 = new ArrayList<>(Arrays.asList("B8", "B9"));
            ArrayList<String> shipPosition8 = new ArrayList<>(Arrays.asList("G4", "G5", "G6", "G7", "G8"));
            ArrayList<String> shipPosition9 = new ArrayList<>(Arrays.asList("B5", "B6", "B7", "B8", "B9"));
            ArrayList<String> shipPosition10 = new ArrayList<>(Arrays.asList("C5", "C6", "C7"));
            ArrayList<String> shipPosition11 = new ArrayList<>(Arrays.asList("F3", "G3", "H3"));
            ArrayList<String> shipPosition12 = new ArrayList<>(Arrays.asList("G4", "G5", "G6", "G7"));
            ArrayList<String> shipPosition13 = new ArrayList<>(Arrays.asList("E1", "F1", "G1"));
            ArrayList<String> shipPosition14 = new ArrayList<>(Arrays.asList("B4", "B5"));
            ArrayList<String> shipPosition15= new ArrayList<>(Arrays.asList("B8", "B9"));
            ArrayList<String> shipPosition16 = new ArrayList<>(Arrays.asList("G4", "G5", "G6", "G7", "G8"));

            //Salvoes
            ArrayList<String> salvoPosition1 = new ArrayList<>(Arrays.asList("B5", "C5", "F1"));
            ArrayList<String> salvoPosition2 = new ArrayList<>(Arrays.asList("B4", "B5", "B6"));
            ArrayList<String> salvoPosition3 = new ArrayList<>(Arrays.asList("F2", "D5"));
            ArrayList<String> salvoPosition4 = new ArrayList<>(Arrays.asList("E1", "H3", "A2"));
            ArrayList<String> salvoPosition5 = new ArrayList<>(Arrays.asList("A2", "A4", "G6"));
            ArrayList<String> salvoPosition6 = new ArrayList<>(Arrays.asList("B5", "D5", "C7"));
            ArrayList<String> salvoPosition7 = new ArrayList<>(Arrays.asList("A3", "H6"));
            ArrayList<String> salvoPosition8 = new ArrayList<>(Arrays.asList("C5", "C6"));


            Ship ship1 = new Ship("carrier", gp1, shipPosition1);
            Ship ship2 = new Ship("submarine", gp1, shipPosition2);
            Ship ship3 = new Ship("destroyer", gp1, shipPosition3);
            Ship ship4 = new Ship("battleship", gp1, shipPosition4);
            Ship ship5 = new Ship("destroyer", gp3, shipPosition5);
            Ship ship6 = new Ship("patrolboat", gp1, shipPosition6);
            Ship ship7 = new Ship("patrolboat", gp5, shipPosition7);
            Ship ship8 = new Ship("carrier", gp6, shipPosition8);

            Ship ship9 = new Ship("carrier", gp2, shipPosition1);
            Ship ship10 = new Ship("submarine", gp2, shipPosition2);
            Ship ship11= new Ship("destroyer", gp2, shipPosition3);
            Ship ship12 = new Ship("battleship", gp2, shipPosition4);
            Ship ship13 = new Ship("destroyer", gp3, shipPosition5);
            Ship ship14 = new Ship("patrolboat", gp2, shipPosition6);
            Ship ship15= new Ship("patrolboat", gp5, shipPosition7);
            Ship ship16= new Ship("carrier", gp6, shipPosition8);

            Salvo salvo1 = new Salvo(1, gp1, salvoPosition1);
            Salvo salvo2 = new Salvo(2, gp1, salvoPosition2);
            Salvo salvo3 = new Salvo(3, gp2, salvoPosition3);
            Salvo salvo4 = new Salvo(4, gp2, salvoPosition4);
            Salvo salvo5 = new Salvo(5, gp3, salvoPosition5);
            Salvo salvo6 = new Salvo(6, gp4, salvoPosition6);
            Salvo salvo7 = new Salvo(7, gp5, salvoPosition7);
            Salvo salvo8 = new Salvo(8, gp6, salvoPosition8);

            shipRepository.save(ship1);
            shipRepository.save(ship2);
            shipRepository.save(ship3);
            shipRepository.save(ship4);
            shipRepository.save(ship5);
            shipRepository.save(ship6);
            shipRepository.save(ship7);
            shipRepository.save(ship8);
            shipRepository.save(ship9);
            shipRepository.save(ship10);
            shipRepository.save(ship11);
            shipRepository.save(ship12);
            shipRepository.save(ship13);
            shipRepository.save(ship14);
            shipRepository.save(ship15);
            shipRepository.save(ship16);

            salvoRepository.save(salvo1);
            salvoRepository.save(salvo2);
            salvoRepository.save(salvo3);
            salvoRepository.save(salvo4);
            salvoRepository.save(salvo5);
            salvoRepository.save(salvo6);
            salvoRepository.save(salvo7);
            salvoRepository.save(salvo8);
            //Game 1
            Score score1 = scoreRepository.save(new Score(player1, game1, 1, LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires"))));
            Score score2 = scoreRepository.save(new Score(player2, game1, 0, LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires"))));

            //Game 2

            Score score3 = scoreRepository.save(new Score(player3, game2, 0.5, LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires")).plusHours(1)));
            Score score4 = scoreRepository.save(new Score(player4, game2, 0.5, LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires")).plusHours(1)));

            //Game 3
            Score score5 = scoreRepository.save(new Score(player5, game3, 1, LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires")).plusHours(2)));
            Score score6 = scoreRepository.save(new Score(player6, game3, 1, LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires")).plusHours(2)));

        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Configuration
    class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {

        @Autowired
        PlayerRepository playerRepository;

        @Override
        public void init(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(inputName -> {
                Player player = playerRepository.findByUserName(inputName);
                if (player != null) {
                    return new User(player.getUserName(), player.getPassword(),
                            AuthorityUtils.createAuthorityList("USER"));
                } else {
                    throw new UsernameNotFoundException("Unknown user: " + inputName);
                }
            });
        }
    }

    @EnableWebSecurity
    @Configuration
    class WebSecurityConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests()
                    .antMatchers("/web/").permitAll()
                    .antMatchers("/h2-console/").permitAll()
                    .antMatchers("/api/games","/api/login","/api/players").permitAll()
                    .antMatchers("/api/game_view/*").hasAuthority("USER")
                    .antMatchers("/web/game.html").hasAuthority("USER")
                    .and().csrf().ignoringAntMatchers("/h2-console/")
                    .and().headers().frameOptions().sameOrigin();


            http.formLogin()
                    .usernameParameter("name")
                    .passwordParameter("pwd")
                    .loginPage("/api/login");

            http.logout().logoutUrl("/api/logout");

            // turn off checking for CSRF tokens
            http.csrf().disable();

            // if user is not authenticated, just send an authentication failure response
            http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

            // if login is successful, just clear the flags asking for authentication
            http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

            // if login fails, just send an authentication failure response
            http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

            // if logout is successful, just send a success response
            http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
        }

        private void clearAuthenticationAttributes(HttpServletRequest request) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            }

        }
    }
}



