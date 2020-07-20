package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class Salvo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private int turno;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gamePlayer_id")
    private GamePlayer gamePlayer;

    public Salvo (){ }

    @ElementCollection
    private List<String> salvolocation = new ArrayList<>();

    public Salvo(int turno, GamePlayer gamePlayer, List<String> salvolocation) {
        this.turno = turno;
        this.salvolocation = salvolocation;
        this.gamePlayer = gamePlayer;
    }

    public Salvo(int turno,List<String> salvolocation){
        this.turno = turno;
        this.salvolocation = salvolocation;
    }

    public void setTurno(int turno) { this.turno = turno; }

    public void setGamePlayer(GamePlayer gamePlayer) { this.gamePlayer = gamePlayer; }

    public void setSalvolocation(List<String> salvolocation) { this.salvolocation = salvolocation; }

    public long getId() { return id; }

    public int getTurno() { return turno; }

    public GamePlayer getGamePlayer() { return gamePlayer; }

    public List<String> getSalvolocation() { return salvolocation; }

    public List <String> getHits (){

        List <String> shots = getSalvolocation();

        GamePlayer opp = this.getGamePlayer().getOpponent();

        Set<Ship> enemyShips = opp.getShips();

        List <String> enemyLocs = new ArrayList<>();

        enemyShips.forEach(ship -> enemyLocs.addAll(ship.getLocation()));

        return shots.stream().filter(shot -> enemyLocs.stream().anyMatch(loc -> loc.equals(shot))).collect(Collectors.toList());
    }

    public List <String> getSinks (){

        List <Salvo> shots = this.gamePlayer.getSalvos().stream().filter(salvo -> salvo.turno <= this.turno).collect(Collectors.toList());

        GamePlayer opp = this.getGamePlayer().getOpponent();

        Set<Ship> enemyShips = opp.getShips();

        List <String> salvoLocs = new ArrayList<>();

        shots.forEach(shot -> salvoLocs.addAll(shot.getSalvolocation()));

        return enemyShips.stream().filter(ship ->salvoLocs.containsAll(ship.getLocation())).map(ship -> ship.getType()).collect(Collectors.toList());
    }

    public Map<String, Object> toDTO(){
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("turno",this.getTurno());
        dto.put("salvolocation", this.getSalvolocation());
        dto.put("player",this.gamePlayer.getPlayer().getId());
        return dto;
    }
    public Map<String, Object> toHitsDTO(){
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("turno",this.getTurno());
        dto.put("hits", this.getHits());
        return dto;
    }

    public Map<String, Object> toSinksDTO(){
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("turno",this.getTurno());
        dto.put("sinks", this.getSinks());
        return dto;
    }
}


