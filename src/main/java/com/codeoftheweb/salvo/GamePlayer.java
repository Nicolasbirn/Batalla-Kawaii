package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Entity
public class GamePlayer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id" )
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id" )
    private Game game;

    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER, cascade = CascadeType.ALL )
    private Set<Ship> ships = new HashSet<>();

    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER, cascade = CascadeType.ALL )
    private Set<Salvo> salvos= new HashSet<>();

    private LocalDateTime joinDate;

    public GamePlayer(){}

    public GamePlayer(Game game, Player player){
        this.game = game;
        this.player = player;
        this.joinDate = LocalDateTime.now();
    }

    public long getId() { return id;}

    public Player getPlayer() {
        return player;
    }

    public Game getGame() {
        return game;
    }

    public LocalDateTime getJoinDate() {
        return joinDate;
    }

    public void setPlayer(Player player){this.player = player;}

    public void setGame(Game game){this.game = game;}

    public Set<Ship> getShips() { return ships; }

    public void setShips(Set<Ship> ships) {
        this.ships = ships;
    }

    public void setSalvos(Set<Salvo> salvos) { this.salvos = salvos; }

    public Set<Salvo> getSalvos() {
        return salvos;
    }

    public Score getScore(){ return this.player.getScoreByGame(this.game); }

    public void meterBarcos(Ship ship){
        this.ships.add(ship);
        ship.setGamePlayer(this);
    }

    public void addSalvo(Salvo salvo){
        this.salvos.add(salvo);
        salvo.setGamePlayer(this);
    }

    public GamePlayer getOpponent() { return this.getGame().getGamePlayers().stream().filter(x -> x.getId() != this.getId()).findFirst().orElse(null); }

    public GameState getState(){
            GameState gameState = GameState.PLAYER_PLACES_SALVOES;
            if (getOpponent() == null){
                gameState = GameState.WAITING_OPPONENT;
            }else {
                if(this.ships.size() != 6){
                    gameState = GameState.PLAYER_PLACES_SHIPS;
                }else {
                    if (this.salvos.size() > getOpponent().getSalvos().size()){
                        gameState = GameState.WAIT;
                    }else if (this.salvos.size() > 0 && this.salvos.size() == getOpponent().getSalvos().size()){
                        List<String> sunks = this.salvos.stream().filter(salvo -> salvo.getTurno() == this.salvos.size()).findFirst().get().getSinks();
                        List<String> oppSunks = this.getOpponent().getSalvos().stream().filter(salvo -> salvo.getTurno() == this.getOpponent().getSalvos().size()).findFirst().get().getSinks();
                        if (sunks.size() == 6 && oppSunks.size() == 6){
                            gameState = GameState.PLAYERS_TIE;
                        }else if (sunks.size() == 6 && oppSunks.size() < 6){
                            gameState = GameState.PLAYER_WINS;
                        }else if (sunks.size() < 6 && oppSunks.size()==6) {
                            gameState = GameState.PLAYER_LOSE;
                        }
                    }
                }
            }


            return gameState;
    }

    public Map<String, Object> toDTO() {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", getId());
        dto.put("player", player.toDTO());
        Score score = this.getScore();
        if(score != null)
            dto.put("score",score.getScore());
        else
            dto.put("score",null);
        return dto;
    }

    public  Map<String, Object> gameViewDTO(){
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id",this.getGame().getId());
        dto.put("created",this.getGame().getDate());
        dto.put("gameplayers",this.getGame().getGamePlayers().stream().map(GamePlayer::toDTO));
        dto.put("ships",this.getShips().stream().map(Ship::shipDTO));
        dto.put("salvos",this.getGame().getGamePlayers().stream().flatMap(gp->gp.getSalvos().stream().map(Salvo::toDTO)));
        if (this.getOpponent()!= null){
            dto.put("hits",this.salvos.stream().map(Salvo::toHitsDTO).collect(toList()));
            dto.put("sinks",this.salvos.stream().map(Salvo::toSinksDTO).collect(toList()));
            dto.put("enemyHits",this.getOpponent().getSalvos().stream().map(Salvo::toHitsDTO).collect(toList()));
            dto.put("enemySink",this.getOpponent().getSalvos().stream().map(Salvo::toSinksDTO).collect(toList()));
        }else{
            dto.put("hits",new ArrayList<>());
            dto.put("sinks",new ArrayList<>());
            dto.put("enemyHits",new ArrayList<>());
            dto.put("enemySink",new ArrayList<>());
        }
        dto.put("gameState",this.getState());
        return dto;
    }

}
