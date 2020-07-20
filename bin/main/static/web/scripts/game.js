var app = new Vue({
    el: '#app',
    data: {
        rows: ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J"],
        columns: ["🔥", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"],
        gameView: {},
        playerOne: {},
        playerTwo: {},
    },
    methods: {
        paintShips: function () {
            this.gameView.ships.forEach(x => x.location.forEach(position => {
                document.getElementById(position).classList.add(x.type)
            }))
        },
        paintsSalvos: function () {
            this.gameView.salvos.forEach(x => x.salvolocation.forEach(position => {
                if (x.player == app.playerOne.id) {
                    document.getElementById(position + 's').classList.add("salvos")
                    document.getElementById(position).innerHTML = x.turno
                } else {
                    document.getElementById(position).classList.add("salvos")
                    document.getElementById(position).innerHTML = x.turno
                }

            }))
        },
        fondoOjos: function () {
            document.getElementById("body").classList.add("fondoOjos")
            document.getElementById("body").classList.remove("fondoEcena")

        },
        fondoEcenas: function () {
            document.getElementById("body").classList.add("fondoEcena")
            document.getElementById("body").classList.remove("fondoOjos")

        },
        fondoImagenDeFondo: function () {
            document.getElementById("body").classList.add("body")
            document.getElementById("body").classList.remove("fondoOjos")
            document.getElementById("body").classList.remove("fondoEcena")
        },
        fondoOjosHomepage: function () {
            document.getElementById("bodyHomepage").classList.add("fondoHomepage")
            document.getElementById("bodyHomepage").classList.remove("fondoHomepage2")

        },
        fondoEcenasHomepage: function () {
            document.getElementById("bodyHomepage").classList.add("fondoHomepage2")
            document.getElementById("bodyHomepage").classList.remove("fondoHomepage")

        },
        fondoImagenDeFondoA: function () {
            document.getElementById("bodyHomepage").classList.add("bodyHomepageC")
            document.getElementById("bodyHomepage").classList.remove("fondoHomepage2")
            document.getElementById("bodyHomepage").classList.remove("fondoHomepage")
        },




    },
})
var params = paramObj(location.search);
fetch("/api/game_view/" + params.gp)
    .then((response) => {
        return response.json();
    })
    .then(function (myJson) {
        app.gameView = myJson;
        app.playerOne = app.gameView.gameplayers.filter(x => x.id == params.gp)[0].player;
        var players = app.gameView.gameplayers.filter(x => x.id != params.gp);
        if (players.length > 0)
            app.playerTwo = players[0].player;
        app.paintShips();
        app.paintsSalvos();
    });

function paramObj(search) {
    var obj = {};
    var reg = /(?:[?&]([^?&#=]+)(?:=([^&#]*))?)(?:#.*)?/g;
    search.replace(reg, function (match, param, val) {
        obj[decodeURIComponent(param)] = val === undefined ? "" : decodeURIComponent(val);
    });

    return obj;
}