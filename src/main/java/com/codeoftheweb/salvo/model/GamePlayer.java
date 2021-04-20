package com.codeoftheweb.salvo.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
public class GamePlayer {

    @Id //es un numero que se le asigna a cada "game"
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    private LocalDateTime joinDate;

    // Es ManyToOne porque hay muchos gameplayers para cada player o game
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id")// player id es el nombre de la columna
    private Player player; //creo la case con la que la voy a relacionar

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id")// game id es el nombre de la columna
    private Game game;

    @OrderBy
    @OneToMany(mappedBy = "gamePlayers", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Salvo> salvoes ;

    @OneToMany(mappedBy = "gamePlayers", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Ship> ship ;


    //CONSTRUCTOR
    public GamePlayer() { //Contructor vacio.
        this.joinDate = LocalDateTime.now();
        this.ship = new HashSet<>();
        this.salvoes = new HashSet<>();
    }

    public GamePlayer(Player player, Game game) {
        this.joinDate = LocalDateTime.now();
        this.player = player;
        this.game = game;
        this.ship = new HashSet<>();
        this.salvoes = new HashSet<>();
    }

    //GETTER Y SETTER
    public Long getId() {
        return id;
    }

    public LocalDateTime getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(LocalDateTime joinDate) {
        this.joinDate = joinDate;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Set<Ship> getShip() {
        return ship;
    }

    public void setShip(Set<Ship> ship) {
        this.ship = ship;
    }

    public Set<Salvo> getSalvoes() { return salvoes; }

    public void setSalvoes(Set<Salvo> salvoes) { this.salvoes = salvoes; }

    public Optional<Score> getScore() {
        return player.getScore(this.game);
    }

    public void addShips(Set<Ship> ships) {
        ships.forEach(ship -> {
            ship.setGamePlayers(this);
            this.ship.add(ship);
        });
    }

    public void addSalvo(Salvo salvo){
        salvo.setGamePlayers(this);
        this.salvoes.add(salvo);
    }
    // para obtener el oponente //
   public GamePlayer getOpponent(){
        return this.getGame().getGamePlayers().stream().filter(gamePlayer -> gamePlayer.getId()!= this.getId()).findFirst().orElse(new GamePlayer());
   }

    //DTO
    public Map<String, Object> gamePlayerDTO() {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", id);
        dto.put("player", player.playerDTO());

        return dto;
    }


}