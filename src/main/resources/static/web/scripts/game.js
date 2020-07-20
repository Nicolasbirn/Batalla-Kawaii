var app = new Vue({
    el: '#app',
    data: {
        rows: ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J"],
        columns: ["🔥", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"],
        gameView: {},
        barcos: [],
        barco: "ElPerlaNegra",
        orientacion: "vertical",
        salvoes: [],
        playerOne: {},
        playerTwo: {},
        gamePlayerId: null,
        history: [],
        enemyHistory: [],
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
                    if (app.gameView.hits.filter(h => h.hits.includes(position)).length > 0) {
                        document.getElementById(position + 's').classList.add("salvos")
                    } else {
                        document.getElementById(position + 's').classList.add("tirosErrados")
                    }
                    document.getElementById(position + 's').innerHTML = x.turno
                } else {
                    if (app.gameView.ships.filter(s => s.location.includes(position)).length > 0) {
                        document.getElementById(position).classList.add("salvos")
                    } else {
                        document.getElementById(position).classList.add("tirosErrados")
                    }
                    document.getElementById(position).innerHTML = x.turno
                }
            }))
        },
        getHistory: function () {
            this.gameView.hits.forEach(hit => {
                var history = {
                    turno: hit.turno,
                    hits: hit.hits,
                    sinks: app.gameView.sinks.filter(sink => sink.turno == hit.turno)[0].sinks
                }
                app.history.push(history)
            })
            this.gameView.enemyHits.forEach(hit => {
                var history = {
                    turno: hit.turno,
                    hits: hit.hits,
                    sinks: app.gameView.enemySink.filter(sink => sink.turno == hit.turno)[0].sinks
                }
                app.enemyHistory.push(history)
            })
        },
        selectSalvos: function (celdaSalvos) {
            if (document.getElementById(celdaSalvos + 's').innerHTML.length == 0) {
                if (this.salvoes.includes(celdaSalvos)) {
                    document.getElementById(celdaSalvos + 's').classList.remove("salvos")
                    app.salvoes = app.salvoes.filter(s => s != celdaSalvos);
                } else if (this.salvoes.length < 5) {
                    document.getElementById(celdaSalvos + 's').classList.add("salvos")
                    app.salvoes.push(celdaSalvos);
                } else {
                    alert("You ran out of shots");
                }
            }
        },
        selectShips: function (celda) {
            var letra = celda[0];
            var numero = parseInt(celda.slice(1));
            var longitud = 0;
            switch (this.barco) {
                case "ElPerlaNegra":
                    longitud = 4;
                    break;
                case "Nautilus":
                    longitud = 3;
                    break;
                case "Nimitz":
                    longitud = 4;
                    break;
                case "Kirov":
                    longitud = 4;
                    break;
                case "ThousandSunny":
                    longitud = 5;
                    break;
                case "Clemenceau":
                    longitud = 6;
                    break;
                default:
                    break;

            }
            if (this.barcos.find(barco => barco.type === this.barco) != undefined) {
                alert("This boat is already on")
            } else if ((letra.charCodeAt(0) - 64) + (longitud - 1) > 10 && this.orientacion == "vertical") {
                alert("You don't have room to put the boat")
            } else if (numero + (longitud - 1) > 10 && this.orientacion == "horizontal") {
                alert("You don't have room to put the boat")
            } else if (this.barcos.filter(barco => barco.location.includes(celda)).length > 0) {
                alert("You're putting one boat on top of the other")
            } else {
                var barco = {
                    type: this.barco,
                    location: []
                };
                if (this.orientacion == "horizontal") {
                    switch (this.barco) {
                        case "ElPerlaNegra":
                            for (let index = 0; index <= 3; index++) {
                                document.getElementById(letra + (numero + index)).classList.add("ElPerlaNegra");
                                barco.location.push(letra + (numero + index));
                            }
                            break;
                        case "Nautilus":
                            for (let index = 0; index <= 2; index++) {
                                document.getElementById(letra + (numero + index)).classList.add("Nautilus");
                                barco.location.push(letra + (numero + index));
                            }
                            break;
                        case "Nimitz":
                            for (let index = 0; index <= 3; index++) {
                                var letraAux = letra.charCodeAt(0);
                                document.getElementById(letra + (numero + index)).classList.add("Nimitz");
                                barco.location.push(letra + (numero + index));
                            }
                            break;
                        case "Clemenceau":
                            for (let index = 0; index <= 5; index++) {
                                var letraAux = letra.charCodeAt(0);
                                document.getElementById(letra + (numero + index)).classList.add("Clemenceau");
                                barco.location.push(letra + (numero + index));
                            }
                            break;
                        case "Kirov":
                            for (let index = 0; index <= 3; index++) {
                                document.getElementById(letra + (numero + index)).classList.add("Kirov");
                                barco.location.push(letra + (numero + index));
                            }
                            break;
                        case "ThousandSunny":
                            for (let index = 0; index <= 4; index++) {
                                document.getElementById(letra + (numero + index)).classList.add("ThousandSunny");
                                barco.location.push(letra + (numero + index));
                            }
                            break;
                        default:
                            break;
                    }
                    this.barcos.push(barco);
                } else {
                    switch (this.barco) {
                        case "ElPerlaNegra":
                            for (let index = 0; index <= 3; index++) {
                                var letraAux = letra.charCodeAt(0);
                                document.getElementById(String.fromCharCode(letraAux + index) + numero).classList.add("ElPerlaNegra");
                                barco.location.push(String.fromCharCode(letraAux + index) + numero);
                            }
                            break;
                        case "Nautilus":
                            for (let index = 0; index <= 2; index++) {
                                var letraAux = letra.charCodeAt(0);
                                document.getElementById(String.fromCharCode(letraAux + index) + numero).classList.add("Nautilus");
                                barco.location.push(String.fromCharCode(letraAux + index) + numero);
                            }
                            break;
                        case "Nimitz":
                            for (let index = 0; index <= 3; index++) {
                                var letraAux = letra.charCodeAt(0);
                                document.getElementById(String.fromCharCode(letraAux + index) + numero).classList.add("Nimitz");
                                barco.location.push(String.fromCharCode(letraAux + index) + numero);
                            }
                            break; Kirov
                        case "Clemenceau":
                            for (let index = 0; index <= 5; index++) {
                                var letraAux = letra.charCodeAt(0);
                                document.getElementById(String.fromCharCode(letraAux + index) + numero).classList.add("Clemenceau");
                                barco.location.push(String.fromCharCode(letraAux + index) + numero);
                            }
                            break;
                        case "Kirov":
                            for (let index = 0; index <= 3; index++) {
                                var letraAux = letra.charCodeAt(0);
                                document.getElementById(String.fromCharCode(letraAux + index) + numero).classList.add("Kirov");
                                barco.location.push(String.fromCharCode(letraAux + index) + numero);
                            }
                            break;
                        case "ThousandSunny":
                            for (let index = 0; index <= 4; index++) {
                                var letraAux = letra.charCodeAt(0);
                                document.getElementById(String.fromCharCode(letraAux + index) + numero).classList.add("ThousandSunny");
                                barco.location.push(String.fromCharCode(letraAux + index) + numero);
                            }
                            break;
                        default:
                            break;

                    }
                    this.barcos.push(barco);
                }
            }
        },
        guardarBarquitos: function () {
            $.post({
                url: "/api/games/players/" + app.gamePlayerId + "/ships",
                data: JSON.stringify(this.barcos),
                dataType: "text",
                contentType: "application/json"
            })
                .done(function (response, status, jqXHR) {
                    alert("Ship added: " + response);
                    location.reload()
                })
                .fail(function (jqXHR, status, httpError) {
                    alert("Failed to add ship: " + textStatus + " " + httpError);
                })
        },
        guardarSalvitos: function () {
            $.post({
                url: "/api/games/players/" + app.gamePlayerId + "/salvoes",
                data: JSON.stringify(this.salvoes),
                dataType: "text",
                contentType: "application/json"
            })
                .done(function (response, status, jqXHR) {
                    alert("Salvo added: " + response);
                    location.reload()
                })

                .fail(function (jqXHR, status, httpError) {
                    alert("Failed to add salvo: " + textStatus + " " + httpError);
                })
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
    },
})
app.gamePlayerId = paramObj(location.search).gp;
fetch("/api/game_view/" + app.gamePlayerId)
    .then((response) => {
        if (response.ok) {
            return response.json();
        } else {
            return Promise.reject(response.json())
        }
    })

    .then(function (myJson) {
        app.gameView = myJson;
        app.playerOne = app.gameView.gameplayers.filter(x => x.id == app.gamePlayerId)[0].player;
        var players = app.gameView.gameplayers.filter(x => x.id != app.gamePlayerId);
        if (players.length > 0)
            app.playerTwo = players[0].player;

        app.paintShips();
        app.paintsSalvos();
        app.getHistory();
        if (app.gameView.gameState == "WAIT" || app.gameView.gameState == "WAITING_OPPONENT") {
            window.setInterval(function () { location.reload() }, 5000);
        }
    });


function paramObj(search) {
    var obj = {};
    var reg = /(?:[?&]([^?&#=]+)(?:=([^&#]*))?)(?:#.*)?/g;
    search.replace(reg, function (match, param, val) {
        obj[decodeURIComponent(param)] = val === undefined ? "" : decodeURIComponent(val);
    });

    return obj;
}

