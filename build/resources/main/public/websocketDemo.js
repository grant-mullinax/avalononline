// small helper function for selecting element by id
let id = id => document.getElementById(id);

//Establish the WebSocket connection and set up event handlers
let ws = null;
let username = null;

// Add event listeners to button and input field
id("connect").addEventListener("click", function () {
    ws = new WebSocket("ws://" + location.hostname + ":" + location.port + "/games/" + id("lobbyid").value);
    ws.onopen = function () {

        username = id("username").value
        var msg = {
            type: "createUser",
            username: username
        };
        ws.send(JSON.stringify(msg));
    }

    ws.onmessage = function (msg) {
        let data = JSON.parse(msg.data);
        console.log(data);
        switch (data.type) {
            case "usernameUpdate":
                id("userlist").innerHTML = data.statuses.map(status =>
                    "<li>" +
                    status.username + (status.connected ? "" : " -dc") + (status.username === username ? " <b>(you)</b>" : "") +
                    "</li>"
                ).join("");
                break;

            case "connectionSuccess":
                id("connectionForm").hidden = true;
                id("inLobby").hidden = false;
                break;
        }
    }

    ws.onclose = () => console.log("WebSocket connection closed");
});

id("startGame").addEventListener("click", function () {
    var msg = {
        type: "startGame"
    };
    ws.send(JSON.stringify(msg));
});
