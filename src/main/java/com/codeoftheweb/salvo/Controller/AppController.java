package com.codeoftheweb.salvo.Controller;

import com.codeoftheweb.salvo.Repository.GamePlayerRepository;
import com.codeoftheweb.salvo.Repository.PlayerRepository;
import com.codeoftheweb.salvo.Repository.ScoreRepository;
import com.codeoftheweb.salvo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
//Los controladores REST definen métodos que obtienen datos de una solicitud, modifican opcionalmente datos en una base de datos
// y finalmente devuelven un objeto que se puede convertir a JSON para enviar de vuelta al cliente web.

@RestController
@RequestMapping("/api")
public class AppController {

    @Autowired
    private GamePlayerRepository gamePlayerRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private ScoreRepository scoreRepository;



    @RequestMapping("/game_view/{nn}")
    public ResponseEntity<Map<String, Object>> getGameViewByGamePlayerID(@PathVariable Long nn, Authentication authentication) {

        if (isGuest(authentication)) {
            return new ResponseEntity<>(makeMap("error", "Paso algo"), HttpStatus.UNAUTHORIZED);
        }

        Player player = playerRepository.findByUserName(authentication.getName());
        GamePlayer gamePlayer = gamePlayerRepository.findById(nn).orElse(null);

        if (player == null) {
            return new ResponseEntity<>(makeMap("error", "Player not logged "), HttpStatus.UNAUTHORIZED);
        }

        if (gamePlayer == null) {
            return new ResponseEntity<>(makeMap("error", "couldn't find gamePlayer"), HttpStatus.UNAUTHORIZED);
        }

        if (gamePlayer.getPlayer().getId() != player.getId()) {
            return new ResponseEntity<>(makeMap("error", "Player ID doesn't match with gamePlayer ID"), HttpStatus.CONFLICT);
        }

        Map<String, Object> dto = new LinkedHashMap<>();
        Map<String, Object> hits = new LinkedHashMap<>();
        hits.put("self", hitsAndSinks(gamePlayer, gamePlayer.getOpponent()));
        hits.put("opponent", hitsAndSinks(gamePlayer.getOpponent(), gamePlayer));

        dto.put("id", gamePlayer.getGame().getId());
        dto.put("created", gamePlayer.getGame().getJoinDate());
        dto.put("gameState", getGameState(gamePlayer));

        dto.put("gamePlayers", gamePlayer.getGame().getGamePlayers()
                .stream()
                .map(gamePlayer1 -> gamePlayer1.gamePlayerDTO())
                .collect(Collectors.toList()));
        dto.put("ships", gamePlayer.getShip()
                .stream()
                .map(ship -> ship.shipDTO())
                .collect(Collectors.toList()));
        dto.put("salvoes", gamePlayer.getGame().getGamePlayers()
                .stream()
                .flatMap(gamePlayer1 -> gamePlayer1.getSalvoes()
                        .stream()
                        .map(salvo -> salvo.salvoDTO()))
                .collect(Collectors.toList()));
        dto.put("hits", hits);


        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    //TASK 10
    private List<Map> hitsAndSinks(GamePlayer self, GamePlayer opponent) {

        List<Map> hits = new ArrayList<>();


        //TOTAL DAMAGE
        int carrierHits = 0;
        int battleshipHits = 0;
        int submarineHits = 0;
        int destroyerHits = 0;
        int patrolboatHits = 0;

        //locations
        List<String> carrierLocations = findShipLocations(self, "carrier");
        List<String> battleshipLocations = findShipLocations(self, "battleship");
        List<String> submarineLocations = findShipLocations(self, "submarine");
        List<String> destroyerLocations = findShipLocations(self, "destroyer");
        List<String> patrolboatLocations = findShipLocations(self, "patrolboat");

        for (Salvo salvo : opponent.getSalvoes()) {

            Map<String, Object> damagePerTurn = new LinkedHashMap<>();
            Map<String, Object> hitsPerTurn = new LinkedHashMap<>();
            ArrayList<String> hitCellList = new ArrayList<>();

            //missed
            int missed = salvo.getSalvoLocations().size();

            //Hits per turn
            int carrierTurn = 0;
            int battleshipTurn = 0;
            int submarineTurn = 0;
            int destroyerTurn = 0;
            int patrolboatTurn = 0;

            for (String location : salvo.getSalvoLocations()) {
                if (carrierLocations.contains(location)) {
                    carrierHits++;
                    carrierTurn++;
                    missed--;
                    hitCellList.add(location);
                }
                if (battleshipLocations.contains(location)) {
                    battleshipHits++;
                    battleshipTurn++;
                    missed--;
                    hitCellList.add(location);
                }
                if (submarineLocations.contains(location)) {
                    submarineHits++;
                    submarineTurn++;
                    missed--;
                    hitCellList.add(location);
                }
                if (destroyerLocations.contains(location)) {
                    destroyerHits++;
                    destroyerTurn++;
                    missed--;
                    hitCellList.add(location);
                }
                if (patrolboatLocations.contains(location)) {
                    patrolboatHits++;
                    patrolboatTurn++;
                    missed--;
                    hitCellList.add(location);
                }
            }
            //damage por turno
            damagePerTurn.put("carrierHits", carrierTurn);
            damagePerTurn.put("battleshipHits", battleshipTurn);
            damagePerTurn.put("submarineHits", submarineTurn);
            damagePerTurn.put("destroyerHits", destroyerTurn);
            damagePerTurn.put("patrolboatHits", patrolboatTurn);
            //total damage
            damagePerTurn.put("carrier", carrierHits);
            damagePerTurn.put("battleship", battleshipHits);
            damagePerTurn.put("submarine", submarineHits);
            damagePerTurn.put("destroyer", destroyerHits);
            damagePerTurn.put("patrolboat", patrolboatHits);
            //
            hitsPerTurn.put("turn", salvo.getTurn());
            hitsPerTurn.put("missed", missed);
            hitsPerTurn.put("damages", damagePerTurn);
            hitsPerTurn.put("hitLocations", hitCellList);
            //List
            hits.add(hitsPerTurn);
        }
        return hits;
    }

    private String getGameState(GamePlayer self) {


        String placeShips = "PLACESHIPS";
        String waitingForOpponent = "WAITINGFOROPP";
        String wait = "WAIT";
        String play = "PLAY";
        String won = "WON";
        String lost = "LOST";
        String tie = "TIE";

        float tie1 = 0.5f;
        float win = 1;
        float looser = 0;


        if (self.getShip().size() < 1)
            return placeShips;
        // preguntar gpOpponent == null esta mal, xq el metodo getOpponent trae un gp si o si
        if (self.getGame().getGamePlayers().size() == 1)
            return waitingForOpponent;

        //if (self.getGame().getGamePlayers().size() == 2)
        GamePlayer opponent = self.getOpponent();

        if (opponent.getShip().size() == 0)
            return wait;

        //si esto es true, no puedo disparar, paso a estado wait, si es false, queda en estado PLAY
        if (self.getSalvoes().size() > opponent.getSalvoes().size()) {
            return wait;
        } else if (self.getSalvoes().size() == opponent.getSalvoes().size()) {

            //si entra aca, state = "PLAY"

            // si es true, le hundieron todos los barcos
            boolean gpself = getIfAllSunk(self, opponent);
            boolean gpOpponent = getIfAllSunk(opponent, self);

            //ambos player , all hundido
            if (gpself && gpOpponent) {
                scoreRepository.save(new Score(self.getPlayer(), self.getGame(), tie1, LocalDateTime.now()));
                //scoreRepository.save(new Score(gpOpponent.getPlayer(), gpSelf.getGame(), tie, LocalDateTime.now()));
                return tie;
            }
            //el self todavia tiene barcos, pero el oponent no - self WIN
            if (!gpself && gpOpponent) {
                scoreRepository.save(new Score(self.getPlayer(), self.getGame(), win, LocalDateTime.now()));
                // scoreRepository.save(new Score(gpOpponent.getPlayer(), gpSelf.getGame(), looser, LocalDateTime.now()));
                return won;
            }

            //el opponent todavia tiene barcos, pero el self no - self loose
            if (gpself && !gpOpponent) {
                scoreRepository.save(new Score(self.getPlayer(), self.getGame(), looser, LocalDateTime.now()));
                // scoreRepository.save(new Score(gpOpponent.getPlayer(), gpSelf.getGame(), win, LocalDateTime.now()));
                return lost;
            }
            //si ningun if es true, stat = "PLAY"
        }
        return play;
    }


    private Boolean getIfAllSunk(GamePlayer self, GamePlayer opponent) {

        if (!opponent.getShip().isEmpty() && !self.getSalvoes().isEmpty()) {
            return opponent.getSalvoes().stream().flatMap(salvo -> salvo.getSalvoLocations().stream()).collect(Collectors.toList()).containsAll(self.getShip().stream()
                    .flatMap(ship -> ship.getLocations().stream()).collect(Collectors.toList()));
        }
        return false;
    }

    public List<String> findShipLocations(GamePlayer gamePlayer, String type) {
        Optional<Ship> ship = gamePlayer.getShip().stream().filter(ships -> ships.getType().equals(type)).findFirst();
        if (ship.isPresent()) return ship.get().getLocations();
        else return new ArrayList<>();
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