package com.codeoftheweb.salvo.Controller;

import com.codeoftheweb.salvo.Repository.GamePlayerRepository;
import com.codeoftheweb.salvo.Repository.PlayerRepository;
import com.codeoftheweb.salvo.model.GamePlayer;
import com.codeoftheweb.salvo.model.Player;
import com.codeoftheweb.salvo.model.Salvo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired
    private GamePlayerRepository gamePlayerRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @GetMapping("/games/players/{gamePlayerId}/salvoes")
    public Map<String,Object> getSalvo(@PathVariable Long gamePlayerId){
        return makeMap("salvoes", gamePlayerRepository.getOne(gamePlayerId).getSalvoes().stream().map(salvo -> salvo.salvoDTO()).collect(Collectors.toList()));
    }

    @PostMapping("/games/players/{gamePlayerId}/salvoes")
    public ResponseEntity<Map<String,Object>> storeSalvo(@PathVariable Long gamePlayerId, Authentication authentication, @RequestBody Salvo salvo) {
        Optional<GamePlayer> gamePlayerPlace = gamePlayerRepository.findById(gamePlayerId);
        Player currentPlayer = playerRepository.findByUserName(authentication.getName());
        ResponseEntity<Map<String, Object>> response;

        if (isGuest(authentication)) {
            response = new ResponseEntity<>(makeMap("error", "no player logged in"), HttpStatus.UNAUTHORIZED);
        } else if (!gamePlayerPlace.isPresent()) {
            response = new ResponseEntity<>(makeMap("error", "Game Player Id doesn't exist"), HttpStatus.UNAUTHORIZED);
        } else if (gamePlayerPlace.get().getPlayer().getId() != currentPlayer.getId()) {
            response = new ResponseEntity<>(makeMap("error", "the player is not authorized"), HttpStatus.UNAUTHORIZED);
        } else {
            Optional<GamePlayer> enemigo = gamePlayerPlace.get().getGame().getGamePlayers().stream().filter(gp->gp.getId()!= gamePlayerPlace.get().getId()).findFirst();
            if (gamePlayerPlace.get().getSalvoes().size()<= enemigo.get().getSalvoes().size()){
                salvo.setTurn(gamePlayerPlace.get().getSalvoes().size() + 1);
                gamePlayerPlace.get().addSalvo(salvo);
                gamePlayerRepository.save(gamePlayerPlace.get());
                response = new ResponseEntity<>(makeMap("OK","Sucess"),HttpStatus.CREATED);
            }else{
                response = new ResponseEntity<>(makeMap("error","the user already has submitted a salvo"),HttpStatus.FORBIDDEN);
            }
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
