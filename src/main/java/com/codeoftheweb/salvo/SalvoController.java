package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;
import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GamePlayerRepository gamePlayerRepository;

    @Autowired
    private PlayerRepository playerRepository;
    
    @Autowired
    private ScoreRepository scoreRepository;
    
    @Autowired
    PasswordEncoder passwordEncoder;

   @RequestMapping("/games")
    public Map<String, Object> getAll(Authentication authentication) {
        Map<String, Object> dto = new LinkedHashMap<>();
        if (!this.isGuest(authentication)) {
            dto.put("player", playerRepository.findByUserName(authentication.getName()).toDTO());
        } else {
            dto.put("player",null);
        }
        dto.put("games", gameRepository.findAll().stream().map(Game::toDTO).collect(toList()));

        return dto;
    }

    @RequestMapping("/game_view/{gamePlayerId}")
    public ResponseEntity<Map<String, Object>> gamePlayerId(@PathVariable long gamePlayerId) {
        GamePlayer gp = gamePlayerRepository.findById(gamePlayerId).orElse(null);
        if (gp != null) {
            return new  ResponseEntity <>(gp.gameViewDTO(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(makeMap("error","Complete all fields!"), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/players", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> newPlayer(@RequestParam String userName, @RequestParam String password) {
        ResponseEntity<Map<String, Object>> response;
        Player player = playerRepository.findByUserName(userName);
        if (userName.isEmpty() || password.isEmpty()) {
            response = new ResponseEntity<>(makeMap("error","Complete all fields!"), HttpStatus.BAD_REQUEST);
        }else if(player != null ){
            response = new ResponseEntity<>(makeMap("error","Username already exist X_x"),HttpStatus.FORBIDDEN);
        }else {
            Player newPlayer = new Player(userName,passwordEncoder.encode(password));
            playerRepository.save(newPlayer);
            response = new ResponseEntity<>(makeMap("ID",newPlayer.getId()),HttpStatus.CREATED);
        }
        return response;
   }

   @PostMapping ("/games/{gameId}/players")
    public ResponseEntity<Map<String, Object>> joinGame(@PathVariable long gameId, Authentication authentication) {
      ResponseEntity<Map<String, Object>> response;
        if (this.isGuest(authentication)) {
                response = new ResponseEntity<>(makeMap("error", "You must log in to join a game"), HttpStatus.UNAUTHORIZED);
        } else {
            Game game = gameRepository.findById(gameId).orElse(null);
            if(game == null){
                response = new ResponseEntity<>(makeMap("error","Game not found"),HttpStatus.NOT_FOUND);
            } else if (game.getGamePlayers().size() > 1){
                response = new ResponseEntity<>(makeMap("error", "Full game"),HttpStatus.FORBIDDEN);
            } else {
                Player player = playerRepository.findByUserName(authentication.getName());
                if(game.getGamePlayers().stream().allMatch(gp -> gp.getPlayer().getId() == player.getId())) {
                    response = new ResponseEntity<>(makeMap("error", "Can't play against yourself"), HttpStatus.FORBIDDEN);
                } else{
                   GamePlayer newGamePlayer = new GamePlayer(game,player);
                   gamePlayerRepository.save(newGamePlayer);
                   response = new ResponseEntity<>(makeMap("gpid",newGamePlayer.getId()),HttpStatus.CREATED);
                }
            }
        }
        return response;
    }

    @RequestMapping(path = "/games/players/{gamePlayerId}/ships", method = RequestMethod.POST)
    public ResponseEntity<Map<String,Object>> meterBarcos (Authentication authentication, @PathVariable Long gamePlayerId,@RequestBody List<Ship> ships){
       ResponseEntity<Map<String,Object>> response;
       if (isGuest(authentication)){
           response = new ResponseEntity<>(makeMap("error","you must be logged in"), HttpStatus.UNAUTHORIZED);
       }else {
           GamePlayer gamePlayer = gamePlayerRepository.findById(gamePlayerId).orElse(null);
           Player player = playerRepository.findByUserName(authentication.getName());
           if (gamePlayer == null){
               response = new ResponseEntity<>(makeMap("error","no such game"), HttpStatus.NOT_FOUND);
           }else if (gamePlayer.getPlayer().getId() != player.getId()){
               response = new ResponseEntity<>(makeMap("error","this is not your game"), HttpStatus.UNAUTHORIZED);
           }else if (gamePlayer.getShips().size() > 0){
               response = new ResponseEntity<>(makeMap("error","you already have ships"), HttpStatus.FORBIDDEN);
           }else if (ships == null || ships.size() != 6){
               response = new ResponseEntity<>(makeMap("error","you must add 5 ships"), HttpStatus.FORBIDDEN);
           //}else if(ships.stream().anyMatch(ship -> this.isOutOfRange(ship))){
                  //response = new ResponseEntity<>(makeMap("error","you have ships out of range"), HttpStatus.FORBIDDEN);
            }else {
               ships.forEach(ship -> gamePlayer.meterBarcos(ship));

               gamePlayerRepository.save(gamePlayer);

               response = new ResponseEntity<>(makeMap("success","ship added"),HttpStatus.CREATED);
           }
       }
       return response;
    }
    @PostMapping("/games/players/{gamePlayerId}/salvoes")
    public ResponseEntity<Map<String,Object>> meterSalvo(Authentication authentication, @PathVariable Long gamePlayerId,@RequestBody List<String> shoots){
       ResponseEntity<Map<String,Object>> response;
       if (isGuest(authentication)){
           response = new ResponseEntity<>(makeMap("error","you must be logged in"), HttpStatus.UNAUTHORIZED);
       }else {
           GamePlayer gamePlayer = gamePlayerRepository.findById(gamePlayerId).orElse(null);
           Player player = playerRepository.findByUserName(authentication.getName());
           if (gamePlayer == null){
            response = new ResponseEntity<>(makeMap("error","no such game"), HttpStatus.NOT_FOUND);
           }else if (gamePlayer.getPlayer().getId()!= player.getId()){
               response = new ResponseEntity<>(makeMap("error","this is not your game"), HttpStatus.UNAUTHORIZED);
           }else {
               int turno = gamePlayer.getSalvos().size()+ 1;
               Salvo salvo = new Salvo(turno,shoots);
               gamePlayer.addSalvo(salvo);
               gamePlayerRepository.save(gamePlayer);
               if(gamePlayer.getState()==GameState.PLAYER_WINS){
                   scoreRepository.save(new Score(1,LocalDateTime.now(),gamePlayer.getPlayer(),gamePlayer.getGame()));
                    scoreRepository.save(new Score(0,LocalDateTime.now(),gamePlayer.getOpponent().getPlayer(),gamePlayer.getOpponent().getGame()));
               }else if(gamePlayer.getState() == GameState.PLAYER_LOSE){
                    scoreRepository.save(new Score(0,LocalDateTime.now(),gamePlayer.getPlayer(),gamePlayer.getGame()));
                    scoreRepository.save(new Score(1,LocalDateTime.now(),gamePlayer.getOpponent().getPlayer(),gamePlayer.getOpponent().getGame()));
               }else if(gamePlayer.getState() == GameState.PLAYERS_TIE){
                    scoreRepository.save(new Score(0.5,LocalDateTime.now(),gamePlayer.getPlayer(),gamePlayer.getGame()));
                    scoreRepository.save(new Score(0.5,LocalDateTime.now(),gamePlayer.getOpponent().getPlayer(),gamePlayer.getOpponent().getGame()));
               }
               response = new ResponseEntity<>(makeMap("SUCCESS","SALVO ADDED"),HttpStatus.CREATED);
           }
       }
       return response;
    }


private boolean isOutOfRange(Ship ship){

       for (String cell : ship.getLocation()){
           if (!(cell instanceof String || cell.length() < 2 )){
               return true;
           }
           char y = cell.substring(0,1).charAt(1);
           Integer x;
           try {
               x = Integer.parseInt(cell.substring(1));
           }catch (NumberFormatException e){
               x = 99;
           };
           if (x < 1 || x > 10 || y > 'A' || y <'J' ){
               return true;
           }
       }
       return false;
    }

    @PostMapping("/games")
    public ResponseEntity<Map<String, Object>> crearGame(Authentication authentication) {
        ResponseEntity<Map<String, Object>> response;
        if(this.isGuest(authentication)){
            response = new ResponseEntity<>(makeMap("error", "You must log in to create a game"), HttpStatus.UNAUTHORIZED);
        }
        else{
            Game newGame = new Game ();
            Player player  = playerRepository.findByUserName(authentication.getName());
            GamePlayer newGamePlayer = new GamePlayer(newGame,player);
            gameRepository.save(newGame);
            gamePlayerRepository.save(newGamePlayer);
            response = new ResponseEntity<>(makeMap("gpid",newGamePlayer.getId()),HttpStatus.CREATED);
        }
        return response;
    }
    
    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

    private Map<String, Object> makeMap(String key, Object value){
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }
}

