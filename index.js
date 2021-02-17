const firebase = require('firebase');

const firebaseConfig = {
    apiKey: "AIzaSyDhEmb44fuZ43r2Omo3lx7IHlZqIi17pqI",
    authDomain: "visitas-220db.firebaseapp.com",
    databaseURL: "https://visitas-220db-default-rtdb.firebaseio.com",
    projectId: "visitas-220db",
    storageBucket: "visitas-220db.appspot.com",
    messagingSenderId: "389038782662",
    appId: "1:389038782662:web:4a97b19925e6f29fd3ffc7",
    measurementId: "G-G1JFRCC5GC"
};
// Initialize Firebase
firebase.initializeApp(firebaseConfig);

const kafka = require('kafka-node');

const client = new kafka.KafkaClient({kafkaHost: '127.0.0.1:9092'});

const fetch = require("node-fetch");

/* Consumidor */

function fecha(fecha) {
    const s = new Date(fecha);

    let formatted_string = s.getFullYear() + "-";

    if (s.getMonth() < 9) {
        formatted_string += "0";
    }
    formatted_string += (s.getMonth() + 1);
    formatted_string += "-";

    if (s.getDate() < 10) {
        formatted_string += "0";
    }
    formatted_string += s.getDate();
    if (s.getHours() < 10) {
        formatted_string += "0";
    }
    formatted_string += " " + s.getHours();

    if (s.getMinutes() < 10) {
        formatted_string += ":0";
    } else {
        formatted_string += ":";
    }

    formatted_string += s.getMinutes();

    if (s.getSeconds() < 10) {
        formatted_string += ":0";
    } else {
        formatted_string += ":";
    }
    formatted_string += s.getSeconds();
    console.log(formatted_string)
    return formatted_string;
}

/*consumer.on('message', function (message) {
    //console.log(message);
    var json = message;
    if (json.hasOwnProperty("value")) {
        var visita = JSON.parse(json["value"]);
        console.log(visita);
        console.log(visita["created"]["time"]);

        formatted_string = fecha(visita["created"]["time"]);

        console.log(formatted_string);
        console.log(" carga de datos de visita");
        const dato = {
            "visita_establecimiento_id": visita["building_id"],
            "visita_fechaalta": formatted_string,
            "visita_fechaactualizacion": null,
            "visita_nota": visita["note"],
            "visita_tipointervencion": visita["interventionType"],
            "visita_tipooperacion": "A",// aestab["operationType"],
            "visita_tipo": visita["type"],
            "visita_entrevistadonombre": visita["interviewee"],
            "visita_entrevistadocargo": visita["intervieweejob"],
            "visita_dueno": visita["owner"],
            "visita_nombredueno": visita["ownername"],
            "visita_evaluador": visita["inspector"],
            "visita_shtnombre": visita["shtname"],
            "visita_estado": visita["status"],
            "visita_sincronizada": parseInt(visita["sincro"]),
            "visita_presentada": 0,
            "visita_presentada_users_id": 0,
            "visita_tituloitem_codigo": 0,
            "visita_agrupacion_id": 0,
            "visita_rar_contacto": null,
            "visita_rar_tel": null,
            "visita_rar_email": null,
            "visita_rar_resp": null,
            "visita_rar_resp_dni": visita["rar_resp_dni"],
            "visita_cap_tema": visita["cap_theme"],
            "visita_motivo": visita["motive"],
            "visita_resultado": visita["result"],
            "visita_nrovisita": visita["number"],
            "visita_rechazo_id": null,
            "visita_fgen": null,
            "visita_fmod": null,
            "visita_codigo": null,
            "visita_efectiva": null,
            "visita_v_tipo_id": null,
            "visita_tipo_resultado_id": null,
            "visita_codigoagendavisita": null,
            "visita_observacion": null,
            "visita_result_error": null,
            "visita_result_fault": null,
            "visita_codigo_art": visita["codeArt"]
        };

        console.log(dato);
        const url = 'http://localhost:8889/sync/sync/visita/create';

        fetch(url, {
            method: 'POST', // or 'PUT'
            body: JSON.stringify(dato), // data can be `string` or {object}!
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(res => res.json())
            .catch(error => console.error('Error:', error))
            .then(response => console.log('Success:', response));
    }

});*/

/* Productor */

/*var producer = new kafka.Producer(client);

producer.on('ready', function () {
    /!*setInterval(function () {
        producer.send([{topic: "test", messages: "Mensaje automático cada 5 seg."}], function (err, data) {
        });
    }, 5000);*!/
    dbRefObject.on('child_added', function (data) {
        producer.send([{topic: "visita", messages: JSON.stringify(data.val())}], function (err, data) {
        });

        console.log(data.val());
    });
});

var consumer2 = new kafka.Consumer(client, [{topic: 'visitadelete'}]);

consumer2.on('message', function (message) {
    console.log("segundo consumidor, evento eliminar");
    var json = message;
    if (json.hasOwnProperty("value")) {
        var visita = JSON.parse(json["value"]);
        console.log("eliminando visita");
        console.log(visita["codeArt"]);
        var respuesta;

        fetch('http://localhost:8889/sync/sync/visita/deleteVisita/' + visita["codeArt"], {
            method: 'DELETE',
        })
            .then(res => res.text()) // or res.json()
            .then(res => console.log(res))
    }
});*/

// productor logSync
const producer3 = new kafka.Producer(client);

const dbRefObject2 = firebase.database().ref().child('logsync');

producer3.on('ready', function () {
    dbRefObject2.on('child_added', function (data) {
        producer3.send([{topic: "logsync", messages: JSON.stringify(data), key: data.key}], function (err, data) {
        });
    });
});


const consuuer3 = new kafka.Consumer(client, [{topic: 'logsync'}]);
consuuer3.on('message', function (message) {
    const json = message;
    if (json.hasOwnProperty("value")) {
        const logSync = JSON.parse(json["value"]);
        const dato = {
            "codigo": logSync["id"],
            "entidad": logSync["entidad"],
            "datosJson": logSync["datosJson"],
            "usuarioId": logSync["usuarioId"],
            "accion": logSync["accion"],
            "updateServer": fecha(logSync["updateServer"]["time"]),
            "updatePub": fecha(logSync["updatePub"]["time"])
        };

        const url = 'http://localhost:8889/sync/sync/logsync/create';

        fetch(url, {
            method: 'POST', // or 'PUT'
            body: JSON.stringify(dato), // data can be `string` or {object}!
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(res => res.json())
            .catch(error => console.error('Error:', error))
            .then(response => {
                if (response["entidad"] === "visita") {
                    const visita = JSON.parse(response["datosJson"]);
                    if (response["accion"] === "INSERT" || response["accion"] === "UPDATE") {
                        altaVisita(visita)
                    } else if (response["accion"] === "DELETE") {
                        eliminarVisita(visita)
                    }
                } else if (response["entidad"] === "visita_incumplimiento") {
                    const visitaIncumplimiento = JSON.parse(response["datosJson"]);
                    if (response["accion"] === "INSERT" || response["accion"] === "UPDATE") {
                        altaVisitaIncumplimiento(visitaIncumplimiento)
                    } else if (response["accion"] === "DELETE") {
                        eliminarVisitaIncumplimiento(visitaIncumplimiento)
                    }
                }
            });
    }
})

function altaVisita(visita) {
    const today = new Date(visita["created"]);
    console.log(" carga de datos de visita");
    const dato = {
        "visita_establecimiento_id": visita["building_id"],
        "visita_fechaalta": today.toISOString(),
        "visita_fechaactualizacion": null,
        "visita_nota": visita["note"],
        "visita_tipointervencion": visita["interventionType"],
        "visita_tipooperacion": "A",// aestab["operationType"],
        "visita_tipo": visita["type"],
        "visita_entrevistadonombre": visita["interviewee"],
        "visita_entrevistadocargo": visita["intervieweejob"],
        "visita_dueno": visita["owner"],
        "visita_nombredueno": visita["ownername"],
        "visita_evaluador": visita["inspector"],
        "visita_shtnombre": visita["shtname"],
        "visita_estado": visita["status"],
        "visita_sincronizada": parseInt(visita["sincro"]),
        "visita_presentada": 0,
        "visita_presentada_users_id": 0,
        "visita_tituloitem_codigo": 0,
        "visita_agrupacion_id": 0,
        "visita_rar_contacto": null,
        "visita_rar_tel": null,
        "visita_rar_email": null,
        "visita_rar_resp": null,
        "visita_rar_resp_dni": visita["rar_resp_dni"],
        "visita_cap_tema": visita["cap_theme"],
        "visita_motivo": visita["motive"],
        "visita_resultado": visita["result"],
        "visita_nrovisita": visita["number"],
        "visita_rechazo_id": null,
        "visita_fgen": null,
        "visita_fmod": null,
        "visita_codigo": null,
        "visita_efectiva": null,
        "visita_v_tipo_id": null,
        "visita_tipo_resultado_id": null,
        "visita_codigoagendavisita": null,
        "visita_observacion": null,
        "visita_result_error": null,
        "visita_result_fault": null,
        "visita_codigo_art": visita["codeArt"]
    };

    console.log(dato);
    const url = 'http://localhost:8889/sync/sync/visita/create';

    fetch(url, {
        method: 'POST', // or 'PUT'
        body: JSON.stringify(dato), // data can be `string` or {object}!
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(res => res.json())
        .catch(error => console.error('Error:', error))
        .then(response => console.log('Success:', response));
}

function eliminarVisita(visita) {
    console.log("eliminando visita");
    console.log(visita["codeArt"]);

    fetch('http://localhost:8889/sync/sync/visita/deleteVisita/' + visita["codeArt"], {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(res => res.text()) // or res.json()
        .catch(error => console.error('Error:', error))
        .then(res => console.log(res))
}

function altaVisitaIncumplimiento(registro) {
    //var fnotificacion = new Date(registro["fechanotificacion"]);
    console.log(" carga de datos de visitaIncumplimiento");
    console.log(registro);
    let nota;
    console.log("tipo de dato")
    console.log(typeof registro)
    if (registro.hasOwnProperty("nota"))
        nota = registro["nota"]
    else
        nota = null

    const dato = {
        "visita_incumplimiento_visita_id": registro["visitaId"],
        "visita_incumplimiento_incumplimiento_id": registro["incumplimientoId"],
        "visita_incumplimiento_fechanotificacion": new Date(registro["fechanotificacion"]).toISOString(),
        "visita_incumplimiento_tipo": registro["tipo"],
        "visita_incumplimiento_tipooperacion": registro["tipooperacion"],
        "visita_incumplimiento_nota": nota,
        "visita_incumplimiento_codigo_art": registro["codeArt"],
    };

    console.log(dato);

    const url = 'http://localhost:8889/sync/sync/visitaincumplimiento/create';

    fetch(url, {
        method: 'POST', // or 'PUT'
        body: JSON.stringify(dato), // data can be `string` or {object}!
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(res => res.json())
        .catch(error => console.error('Error:', error))
        .then(response => console.log('Success:', response));
}

function eliminarVisitaIncumplimiento(visitaIncumplimiento) {
    console.log("eliminando visita");
    console.log(visitaIncumplimiento["codeArt"]);

    fetch('http://localhost:8889/sync/sync/visitaincumplimiento/deletevisitaincumplimiento/' + visitaIncumplimiento["codeArt"] + '/' + visitaIncumplimiento["incumplimientoId"], {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(res => res.text()) // or res.json()
        .catch(error => console.error('Error:', error))
        .then(res => console.log(res))
}