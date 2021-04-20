package com.codeoftheweb.salvo.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Ship {

    @Id //es un numero que se le asigna a cada "player"
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private String type;

   @ManyToOne(fetch=FetchType.EAGER)
   @JoinColumn(name = "gamePlayer_id")
   private GamePlayer gamePlayers;

   @ElementCollection
   @Column(name = "positions")
   private List<String> locations;

    //Constructor
    public Ship() {
      this.locations=new ArrayList<>();
      this.type= "UNDEFINED";
    }

    public Ship (String type, GamePlayer gamePlayers, ArrayList<String> shipPositions) {
        this();
        this.type = type;
        this.gamePlayers= gamePlayers;
        this.locations = shipPositions;
    }

    public GamePlayer getGamePlayers() { return gamePlayers; }

    public void setGamePlayers(GamePlayer gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    public long getId() { return id; }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

    public List<String> getLocations() { return locations; }

    public void setLocations(ArrayList<String> shipPositions) { this.locations = shipPositions; }

//DTO
    public Map<String, Object> shipDTO(){
        Map<String, Object> dto= new HashMap<>();
        dto.put("id", id);
        dto.put("type", type);
        dto.put("locations", locations);

        return dto;
    }

}


