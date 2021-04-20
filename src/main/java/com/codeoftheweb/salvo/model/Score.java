package com.codeoftheweb.salvo.model;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Entity
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;

    private double score;

    private LocalDateTime finishDate;

 //Contructores

    public Score() {
    }

    public Score(Player player, Game game, double score, LocalDateTime finishDate) {
        this.player = player;
        this.game = game;
        this.score = score;
        this.finishDate = finishDate;
    }
// Getter y Setter
    public long getId() { return id; }

    public Player getPlayer() { return player; }

    public void setPlayer(Player player) { this.player = player; }

    public Game getGame() { return game; }

    public void setGame(Game game) { this.game = game; }

    public double getPoints() { return score; }

    public void setPoints(double points) { this.score = points; }

    public LocalDateTime getFinishDate() { return finishDate; }

    public void setFinishDate(LocalDateTime finishDate) { this.finishDate = finishDate; }

    public double getScore(){
        return this.score;
    }
   public Map<String,Object> scoreDTO(){
         Map<String, Object> dto= new LinkedHashMap<>();
         dto.put("finishDate",getFinishDate());
         dto.put("player", getPlayer().getId());
         dto.put("score", getScore());//cambiarle el nombre de mierda
        // dto.put("player", getPlayer().getId());

        return dto;
    }

}





