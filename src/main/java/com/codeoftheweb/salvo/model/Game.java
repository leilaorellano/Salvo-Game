package com.codeoftheweb.salvo.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Game {

    @Id //es un numero que se le asigna a cada "gameplayer"
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

//Es OneToMany porque un juego puede tener muchos GamePlayers

    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    private Set<GamePlayer> gamePlayers; //Variable declarada de GamePlayer en Game

    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    private Set<Score> scores;

    private LocalDateTime joinDate;

    // Constructor //
    public Game() {
    }

    public Game(LocalDateTime joinDate) {
        this.joinDate = joinDate;
    }


    //Getter y Setter
    public Long getId() {
        return id;
    }

    public LocalDateTime getJoinDate() {
        return joinDate;
    }

    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public Set<Score> getScores() {
        return scores;
    }

    public void setScores(Set<Score> scores) {
        this.scores = scores;
    }

    // Esto es para el OneToMany
    public void addGamePlayer(GamePlayer gamePlayer) {
        gamePlayer.setGame(this);
        gamePlayers.add(gamePlayer);
    }


    //DTOs
    public Map<String, Object> gameDTO() {
        Map<String, Object> dto = new HashMap<>();
        dto.put("id", getId());
        dto.put("created", getJoinDate());
        dto.put("gamePlayers", gamePlayers.stream().map(GamePlayer::gamePlayerDTO).collect(Collectors.toList()));
        List<Map<String, Object>> scores = gamePlayers.stream()
                .map(gp -> gp.getScore()).filter(score -> score.isPresent()).map(score -> score.get().scoreDTO())
                .collect(Collectors.toList());
        dto.put("scores", scores);

        return dto;
    }

}
