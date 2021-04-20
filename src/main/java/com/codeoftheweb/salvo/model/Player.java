package com.codeoftheweb.salvo.model;


import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity //Esto le dice a Hibernate que haga una tabla de esta clase
public class Player {

    @Id //es un numero que se le asigna a cada "player"
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private String userName;


    //Es OneToMany porque un juego puede tener varios gameplayers!
    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    private Set<GamePlayer> gamePlayers = new HashSet<>();

    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    private Set<Score> scores = new HashSet<>();

    private String password;

    // CONSTRUCTOR //
    public Player() {
    }

    public Player(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    //GETTER Y SETTER
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }


    public void setScores(Set<Score> scores) {
        this.scores = scores;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // METODOS
    public void addGamePlayer(GamePlayer gamePlayer) {
        gamePlayer.setPlayer(this);
        gamePlayers.add(gamePlayer);
    }

    @JsonIgnore
    public List<Game> getGames() {
        return gamePlayers.stream().map(GamePlayer::getGame).collect(Collectors.toList());
    }

    //DTOs
    public Map<String, Object> playerDTO() {
        Map<String, Object> dto = new HashMap<>();
        dto.put("id", id);
        dto.put("email", userName);

        return dto;
    }

    public Optional<Score> getScore(Game game) {
        return scores.stream().filter(score -> score.getGame().getId() == game.getId()).findFirst();
    }

}
