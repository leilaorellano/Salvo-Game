package com.codeoftheweb.salvo.Controller;


import com.codeoftheweb.salvo.Repository.GamePlayerRepository;
import com.codeoftheweb.salvo.Repository.GameRepository;
import com.codeoftheweb.salvo.Repository.PlayerRepository;
import com.codeoftheweb.salvo.model.Game;
import com.codeoftheweb.salvo.model.GamePlayer;
import com.codeoftheweb.salvo.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class GameController {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private GamePlayerRepository gamePlayerRepository;

    //Metodos//

    //Necesito encontrar findAll() métodos para retornar una lista/set
    @RequestMapping("/games")
    public Map<String, Object> getGamesId(Authentication authentication) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("games", gameRepository.findAll()
                .stream().map(game -> game.gameDTO())
                .collect(Collectors.toList()));
        data.put("player", getMap(authentication) == null ? "Guest" : getMap(authentication));
        return data;
    }

    // UNIRSE AL JUEGO
    @PostMapping("/game/{gameId}/players")
    public ResponseEntity<Map<String, Object>> joinGame(@PathVariable long gameId, Authentication authentication) {
        if (!isGuest(authentication)) {
            Optional<Game> game = gameRepository.findById(gameId);
            if (game.isPresent()) {
                if (game.get().getGamePlayers().size() == 2) {
                    return new ResponseEntity<>(makeMap("Problem", "Game is full"), HttpStatus.FORBIDDEN);
                }else{
                    Player player = playerRepository.findByUserName(authentication.getName());
                    GamePlayer gamePlayer=gamePlayerRepository.save(new GamePlayer(player,game.get()));
                    return new ResponseEntity<>(makeMap("gpid",gamePlayer.getId()),HttpStatus.OK);
                }
            } else {
                return new ResponseEntity<>(makeMap("Problem", "No such game"), HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>(makeMap("Problem", "Player is Unauthorized"), HttpStatus.UNAUTHORIZED);
        }
    }

    //CREAR EL JUEGO
    @PostMapping("/games")
    public ResponseEntity<Map<String,Object>> createGame(Authentication authentication){
        Map<String, Object> map = new HashMap<>();
        ResponseEntity response;
        if(!isGuest(authentication)){
            Player playerx = playerRepository.findByUserName(authentication.getName());
            Game gamex= gameRepository.save(new Game(LocalDateTime.now()));
            GamePlayer gp= gamePlayerRepository.save(new GamePlayer(playerRepository.save(playerx),gamex));
            response = new ResponseEntity<>(makeMap("gpid",gp.getId()),HttpStatus.CREATED);

        }else{
            response = new ResponseEntity<>(makeMap("player","Guest"),HttpStatus.UNAUTHORIZED);
        }
        return response;
    }

    private Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

    private Map<String, Object> getMap(Authentication authentication) {
        if (!isGuest(authentication)) {
            return playerRepository.findByUserName(authentication.getName()).playerDTO();
        } else {
            return null;
        }
    }

}
