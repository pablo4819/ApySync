/*
// Your web app's Firebase configuration
// For Firebase JS SDK v7.20.0 and later, measurementId is optional
var firebaseConfig = {
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
firebase.analytics();

//Obtener elementos
const preObject = document.getElementById('visita');

//Crear Referencias
const dbRefObject = firebase.database().ref().child('visita');

dbRefObject.on('child_added', function (data) {
    preObject.innerHTML += data.child("codeArt").val() + ' - ';
    preObject.innerHTML += data.child("created").child("time").val() + '<br>';
    console.log(data.val());
    console.log(data.child("created").val());
    /!*fetch('http://localhost:8889/api/api/visita/create/')
        .then(response => {
            //return response.json();
            console.log(response)
        })
        .then(users => {
            console.log(users);
        });*!/

    /!*fetch('http://localhost:8889/api/api/visita/getAll')
        .then(response => response.json())
        .then(data => console.log(data));*!/

    const url = 'http://localhost:8889/api/api/visita/create';
    //const dato = {datos: data.val()};
    const s = new Date(data.child("created").child("time").val());
    //const t = data.child("created").child("time").val();
    //const s = data.child("created").val();
    //const s = new Date(data.child("created").child("time").val()).toISOString().replace('T',' ').slice(0,19)
    const fecha = s.getFullYear() + '-' + s.getMonth() + '-' + s.getDay() + ' ' + s.getHours() + ':' + s.getMinutes()
    //console.log(t)
    //const s = new Date(data.child("created").child("time").val());
    const t = data.child("created").child("time").val();
    const dato = {
        "visita_establecimiento_id": data.child("building_id"),
        "visita_fechaalta": null,
        "visita_timestamp": t,
        "visita_fechaactualizacion": null,
        "visita_nota": data.child("note"),
        "visita_tipointervencion": data.child("interventionType"),
        "visita_tipooperacion": "A",// adata.child("operationType"),
        "visita_tipo": data.child("type"),
        "visita_entrevistadonombre": data.child("interviewee"),
        "visita_entrevistadocargo": data.child("intervieweejob"),
        "visita_dueno": data.child("owner"),
        "visita_nombredueno": data.child("ownername"),
        "visita_evaluador": data.child("inspector"),
        "visita_shtnombre": data.child("shtname"),
        "visita_estado": data.child("status"),
        "visita_sincronizada": 0,
        "visita_presentada": 0,
        "visita_presentada_users_id": 0,
        "visita_tituloitem_codigo": 0,
        "visita_agrupacion_id": 0,
        "visita_rar_contacto": null,
        "visita_rar_tel": null,
        "visita_rar_email": null,
        "visita_rar_resp": null,
        "visita_rar_resp_dni": data.child("rar_resp_dni"),
        "visita_cap_tema": data.child("cap_theme"),
        "visita_motivo": data.child("motive"),
        "visita_resultado": data.child("result"),
        "visita_nrovisita": data.child("number"),
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
        "visita_codigo_art": data.child("codeArt")
    };

    fetch(url, {
        method: 'POST', // or 'PUT'
        body: JSON.stringify(dato), // data can be `string` or {object}!
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(res => res.json())
        .catch(error => console.error('Error:', error))
        .then(response => console.log('Success:', response));
});*/

var firebase = require('firebase');

var firebaseConfig = {
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

const dbRefObject = firebase.database().ref().child('visita');


dbRefObject.on('child_added', function (data) {
    console.log(data.val());
});


const kafka = require('kafka-node');


const client = new kafka.KafkaClient({kafkaHost: '127.0.0.1:9092'});

const fetch = require("node-fetch");

/* Consumidor */

var consumer = new kafka.Consumer(client, [{topic: 'visita'}], null);

consumer.on('message', function (message) {
    console.log('message', message);
    var estab = message["value"];
    console.log(JSON.parse(estab))
    var s = new Date(estab["created"]["time"]);

    var formatted_string = s.getFullYear() + "-";

    if (s.getMonth() < 9) {
        formatted_string += "0";
    }
    formatted_string += (s.getMonth() + 1);
    formatted_string += "-";

    if (s.getDate() < 10) {
        formatted_string += "0";
    }
    formatted_string += s.getDate();

    console.log(formatted_string);
    /*const url = 'http://localhost:8889/api/api/visita/create';

    fetch(url, {
        method: 'POST', // or 'PUT'
        body: JSON.stringify(message), // data can be `string` or {object}!
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(res => res.json())
        .catch(error => console.error('Error:', error))
        .then(response => console.log('Success:', response)); */
});


/* Productor */
var producer = new kafka.Producer(client);

const s = new Date(data.child("created").child("time").val());
const fecha = s.getFullYear() + '-' + s.getMonth() + '-' + s.getDay() + ' ' + s.getHours() + ':' + s.getMinutes()

producer.on('ready', function () {
    /*setInterval(function(){
        producer.send( [ { topic: "test", messages: "Mensaje automático cada 5 seg." } ], function (err,data) {} );
    }, 5000);*/
    dbRefObject.on('child_added', function (data) {
        producer.send([{topic: "visita", messages: JSON.stringify(data.val())}], function (err, data) {
        });
        console.log(data.val());
    });
});

ref.on("child_changed", function(snapshot) {
    var changedPost = snapshot.val();
    console.log("The updated post title is " + changedPost.title);
});

var consumer2 = new kafka.Consumer(client, [{topic: 'deletevisita'}]);

consumer2.on('message', function (message) {
    console.log(message);
    var json = message;
    if (json.hasOwnProperty("value")) {
        var estab = JSON.parse(json["value"]);
        console.log("eliminando visita");
        console.log(estab);
        console.log(estab["id"]);
        const url = 'http://localhost:8889/api/api/visita/delete/'+estab["id"];

        var resp = fetch(url, {
            method: 'POST', // or 'PUT'
            headers: {
                'Content-Type': 'application/json'
            }
        });
        console.log(resp)
    }

});