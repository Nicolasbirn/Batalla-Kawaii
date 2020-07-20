var app = new Vue({
  el: '#app',
  data: {
    games: [],
    board: [],
    mail: "",
    password: "",
    jugadorAct: null,
  },
  methods: {
    fillBoard: function () {
      app.games.forEach(game => {
        game.gamePlayers.forEach(gp => {
          var playerIndex = app.board.findIndex(p => p.username == gp.player.userName);
          if (playerIndex == -1) {

            var player = {
              username: "",
              totalPoints: 0,
              wins: 0,
              losses: 0,
              ties: 0,
            };

            player.username = gp.player.userName;
            player.totalPoints = gp.score;

            if (gp.score == 1.0) {
              player.wins = 1;

            } else if (gp.score == 0.5) {
              player.ties = 1;

            } else if (gp.score == 0.0) {
              player.losses = 1;
            }

            app.board.push(player);

          } else {
            app.board[playerIndex].totalPoints += gp.score;

            if (gp.score == 1.0) {
              app.board[playerIndex].wins += 1;

            } else if (gp.score == 0.5) {
              app.board[playerIndex].ties += 1;

            } else if (gp.score == 0.0) {
              app.board[playerIndex].losses += 1;
            }
          }
        })
      })
    },
    fondoOjosScore: function () {
      document.getElementById("bodyScore").classList.add("fondoScore")
      document.getElementById("bodyScore").classList.remove("fondoScore2")

    },
    fondoEcenasScore: function () {
      document.getElementById("bodyScore").classList.add("fondoScore2")
      document.getElementById("bodyScore").classList.remove("fondoScore")

    },
    fondoImagenDeFondo: function () {
      document.getElementById("bodyScore").classList.add("bodyScoreC")
      document.getElementById("bodyScore").classList.remove("fondoScore")
      document.getElementById("bodyScore").classList.remove("fondoScore2")
    },
    logIn: function () {
      $.post("/api/login", {
          username: app.mail,
          password: app.password,
        })
        .done(function () {
          alert("Yōkoso 👺");
          location.reload(true);

        })
        .fail(function (Baka) {
          alert("Incorrect ❌")
        })
    },
    logOut: function () {
      $.post("/api/logout")
        .done(function () {
          alert("Sayōnara 👋");
          location.reload(true);
        })
    },
    signUp: function () {
      $.post("/api/players", {
          userName: app.mail,
          password: app.password,
        })
        .done(function () {
          alert("welcome stars the tirra says hello !!");
          app.logIn();
        })
    },

    backToGame: function (gamePlayerId) {
      location.href = '/web/game.html?gp=' + gamePlayerId;
    },
    joinGame: function (gameId) {
      $.post("/api/games/" + gameId + "/players")
        .done(function (asnwerdeJson) {
          location.href = '/web/game.html?gp=' + asnwerdeJson.gpid;
        })
        .fail(function (error) {
          alert(error);
        })
    },
    crearGame: function(){
    $.post("/api/games/")
       .done(function (asnwerdeJson) {
         location.href = '/web/game.html?gp=' + asnwerdeJson.gpid;
            })
          .fail(function (error) {
           alert(error);
    })
    }
   
  },
})
fetch("/api/games")
  .then((response) => {
    return response.json();
  })
  .then(function (myJson) {
    app.games = myJson.games;
    app.jugadorAct = myJson.player;

    app.fillBoard();
  });