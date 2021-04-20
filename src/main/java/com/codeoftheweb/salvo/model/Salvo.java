package com.codeoftheweb.salvo.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Salvo {

    @Id //es un numero que se le asigna a cada "player"
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private int turn;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "gamePlayer_id")
    private GamePlayer gamePlayers;

    @ElementCollection
    @Column(name = "positions")
    private List<String> salvoLocations;

//Constructor


    public Salvo() {
        this.salvoLocations=new ArrayList<>();
        this.turn = 0 ;
    }

    public Salvo(int turn, GamePlayer gamePlayers, ArrayList<String> salvoLocations) {
        this();
        this.turn = turn;
        this.gamePlayers = gamePlayers;
        this.salvoLocations = salvoLocations;
    }
/// GETTER Y SETTER
    public long getId() { return id; }

    public int getTurn() { return turn; }

    public void setTurn(int turn) { this.turn = turn; }

    public GamePlayer getGamePlayers() { return gamePlayers; }

    public void setGamePlayers(GamePlayer gamePlayers) { this.gamePlayers = gamePlayers; }

    public List<String> getSalvoLocations() {
        return salvoLocations;
    }

    public void setSalvoLocations(List<String> salvoLocations) {
        this.salvoLocations = salvoLocations;
    }

//DTO

    public Map<String, Object> salvoDTO(){
        Map<String, Object> dto= new HashMap<>();
        dto.put("turn", turn );
        dto.put("player", gamePlayers.getId());
        dto.put("locations", salvoLocations);

        return dto;
    }

}
