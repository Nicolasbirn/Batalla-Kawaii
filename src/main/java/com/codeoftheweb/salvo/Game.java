package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static java.util.stream.Collectors.toList;

@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    LocalDateTime date;

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    Set<GamePlayer> gamePlayers;

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    Set<Score> scores;

    public Game(){
        this.date = LocalDateTime.now();
    }

    public Game(LocalDateTime date){
        this.date = date;
    }

    public List<Player> getPlayers() {return gamePlayers.stream().map(sub -> sub.getPlayer()).collect(toList());}

    public long getId() {
        return id;
    }

    public  LocalDateTime getDate(){
        return date;
    }

    public Set<GamePlayer> getGamePlayers() { return gamePlayers; }

    public void addGamePlayer(GamePlayer gamePlayer){gamePlayer.setGame(this); gamePlayers.add(gamePlayer);}

    public Map<String, Object> toDTO() {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", getId());
        dto.put("created",getDate ());
        dto.put("gamePlayers", getGamePlayers().stream().map(GamePlayer::toDTO).collect(toList()));
        return dto;
    }
}







