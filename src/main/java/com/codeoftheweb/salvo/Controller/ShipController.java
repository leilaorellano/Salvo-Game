package com.codeoftheweb.salvo.Controller;

import com.codeoftheweb.salvo.Repository.GamePlayerRepository;
import com.codeoftheweb.salvo.Repository.PlayerRepository;
import com.codeoftheweb.salvo.Repository.ShipRepository;
import com.codeoftheweb.salvo.model.GamePlayer;
import com.codeoftheweb.salvo.model.Player;
import com.codeoftheweb.salvo.model.Ship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ShipController {

    @Autowired
    private ShipRepository shipRepository;

    @Autowired
    private GamePlayerRepository gamePlayerRepository;

    @Autowired
    private PlayerRepository playerRepository;

    //Metodos//

    @GetMapping("/games/players/{gamePlayerId}/ships")
    public Map<String,Object> getShip(@PathVariable Long gamePlayerId){
        return makeMap("ships", gamePlayerRepository.getOne(gamePlayerId).getShip().stream().map(ship -> ship.shipDTO()).collect(Collectors.toList()));
    }

    @PostMapping("/games/players/{gamePlayerId}/ships")
    public ResponseEntity<Map<String,Object>> placeShip(@PathVariable Long gamePlayerId, Authentication authentication, @RequestBody Set<Ship> ships){
        Optional<GamePlayer> gamePlayerPlace = gamePlayerRepository.findById(gamePlayerId);
        Player currentPlayer = playerRepository.findByUserName(authentication.getName());
        ResponseEntity<Map<String,Object>> response;

        if (isGuest(authentication)){
            response = new ResponseEntity<>(makeMap("error","no player logged in"), HttpStatus.UNAUTHORIZED);
        }else if (!gamePlayerPlace.isPresent()){
            response = new ResponseEntity<>(makeMap("error","Game Player Id doesn't exist"),HttpStatus.UNAUTHORIZED);
        }else if (gamePlayerPlace.get().getPlayer().getId()!= currentPlayer.getId()){
            response = new ResponseEntity<>(makeMap("error","the player is not authorized"),HttpStatus.UNAUTHORIZED);
        }else if (gamePlayerPlace.get().getShip().size() > 0){
            response = new ResponseEntity<>(makeMap("error","Ships are already placed"),HttpStatus.FORBIDDEN);
        }else {
            if (ships.size()>0){
                gamePlayerPlace.get().addShips(ships);
                gamePlayerRepository.save(gamePlayerPlace.get());
                response = new ResponseEntity<>(makeMap("OK","Sucess"),HttpStatus.CREATED);
            }else{
                response = new ResponseEntity<>(makeMap("error","you don´t send any ship"),HttpStatus.FORBIDDEN);
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
