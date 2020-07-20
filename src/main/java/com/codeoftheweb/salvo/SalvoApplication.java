package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class SalvoApplication {

	@Autowired
	PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(PlayerRepository playerRepository, GameRepository gameRepository, GamePlayerRepository gamePlayerRepository, ShipRepository shipRepository, SalvoRepository salvoRepository, ScoreRepository scoreRepository){
		return (args) -> {
			// save a couple of customers
		// 	Player player1 = new Player("Jack", "Bauer", "j.bauer@ctu.gov",passwordEncoder.encode("hola"));
		// 	Player player2 = new Player("Chloe", "O'Brian" , "c.obrian@ctu.gov",passwordEncoder.encode("666"));
		// 	Player player3 = new Player("Kim", "Bauer", "kim_bauer@gmail.com",passwordEncoder.encode("12345"));
		// 	Player player4 = new Player("Tony", "Almeida","t.almeida@ctu.gov",passwordEncoder.encode("9876"));
		// 	Player player5 = new Player("Nicolas", "Birnbach", "nicolasbirnbach.n@gmail.com",passwordEncoder.encode("9222"));
			
		// 	playerRepository.save(player1);
		// 	playerRepository.save(player2);
		// 	playerRepository.save(player3);
		// 	playerRepository.save(player4);
		// 	playerRepository.save(player5);
			
		// 	Game game1 = new Game();
		//  Game game2 = new Game((LocalDateTime.now().plusHours(1)));
		// 	Game game3 = new Game(LocalDateTime.now().plusHours(2));
		// 	Game game4 = new Game(LocalDateTime.now().plusHours(3));
		// 	Game game5 = new Game(LocalDateTime.now().plusHours(4));
		    
		// 	gameRepository.save(game1);
		// 	gameRepository.save(game2);
		// 	gameRepository.save(game3);
		// 	gameRepository.save(game4);
		// 	gameRepository.save(game5);

		// 	GamePlayer gamePlayer1 = new GamePlayer(game1, player1);
		// 	GamePlayer gamePlayer2 = new GamePlayer(game1, player2);
		// 	GamePlayer gamePlayer3 = new GamePlayer(game2, player2);
		// 	GamePlayer gamePlayer4 = new GamePlayer(game2, player3);
		// 	GamePlayer gamePlayer5 = new GamePlayer(game3, player3);
		// 	GamePlayer gamePlayer6 = new GamePlayer(game3, player4);
		// 	GamePlayer gamePlayer7 = new GamePlayer(game4, player4);
		// 	GamePlayer gamePlayer8 = new GamePlayer(game4, player1);
		// 	GamePlayer gamePlayer9 = new GamePlayer(game5, player5);
			
		// 	gamePlayerRepository.save(gamePlayer1);
        //  gamePlayerRepository.save(gamePlayer2);
		// 	gamePlayerRepository.save(gamePlayer3);
        //  gamePlayerRepository.save(gamePlayer4);
		// 	gamePlayerRepository.save(gamePlayer5);
        //  gamePlayerRepository.save(gamePlayer6);
        //  gamePlayerRepository.save(gamePlayer7);
        //  gamePlayerRepository.save(gamePlayer8);
		// 	gamePlayerRepository.save(gamePlayer9);

		// 	Ship shipOne = new Ship("ElPerlaNegra", Arrays.asList("A2","B2","B3","B4"), gamePlayer1);   //negro					//4
		// 	Ship shipTwo = new Ship("Nautilus", Arrays.asList("F5","G5"), gamePlayer1); //azul									//6
		// 	Ship shipThree = new Ship("Nimitz", Arrays.asList("C6","C7","C8","C9"), gamePlayer1); //verde						//4
		// 	Ship shipFour = new Ship("Clemenceau", Arrays.asList("E10","F10","G10","H10","I10","J10"), gamePlayer1); //amarillo //6
		// 	Ship shipFive = new Ship("Kirov", Arrays.asList("E1","E2"), gamePlayer1); //naranja									//2
		// 	Ship shipSix = new Ship("ThousandSunny", Arrays.asList("H1","I1","J1"), gamePlayer1); //violeta						//3
			
		// 	shipRepository.save(shipOne);
		// 	shipRepository.save(shipTwo);
		// 	shipRepository.save(shipThree);
		// 	shipRepository.save(shipFour);
		// 	shipRepository.save(shipFive);
        //  shipRepository.save(shipSix);
			
		// 	Salvo salvoone =new Salvo(1,gamePlayer1, Arrays.asList("A1","A2","A3"));
        //  Salvo salvotwo =new Salvo(1,gamePlayer2, Arrays.asList("F1","F2","F3"));
		// 	Salvo salvoThree =new Salvo(1,gamePlayer1, Arrays.asList("E5","E6","E7"));

		// 	salvoRepository.save(salvoone);
        //  salvoRepository.save(salvotwo);
		// 	salvoRepository.save(salvoThree);
			
		// 	Score score1 = new Score(1,LocalDateTime.now(), player1,game1);
		// 	Score score2 = new Score(0, LocalDateTime.now(), player2, game1);
		// 	scoreRepository.save(score1);
		// 	scoreRepository.save(score2);
		 };
	}
}