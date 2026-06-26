// Declaración de variables globales de conexión
var db;
var dbName = "pokedexDB";
var dbVersion = 1;

// Inicialización de la IndexedDB
function iniciarBaseDatos(onListo) {
  var solicitud = indexedDB.open(dbName, dbVersion);

  solicitud.onerror = function (evento) {
    console.error("Error al abrir la base de datos:", evento.target.error);
  };

  solicitud.onsuccess = function (evento) {
    db = evento.target.result;
    if (onListo) onListo();
  };

  solicitud.onupgradeneeded = function (evento) {
    var base = evento.target.result;

    // Almacén para los Perfiles de Entrenadores
    if (!base.objectStoreNames.contains("entrenadores")) {
      base.createObjectStore("entrenadores", {
        keyPath: "id",
        autoIncrement: true,
      });
    }

    // Almacén para los Equipos Pokémon
    if (!base.objectStoreNames.contains("equipos")) {
      base.createObjectStore("equipos", {
        keyPath: "id",
        autoIncrement: true,
      });
    }
  };
}

// Métodos requeridos: Agregar y Consultar
function agregarDato(storeName, data, onSuccess, onError) {
  if (!db) {
    if (onError) onError("La base de datos no está inicializada.");
    return;
  }

  var transaccion = db.transaction([storeName], "readwrite");
  var almacen = transaccion.objectStore(storeName);
  var peticion = almacen.add(data);

  peticion.onsuccess = function (evento) {
    if (onSuccess) onSuccess(evento.target.result); // Devuelve el ID generado
  };

  peticion.onerror = function (evento) {
    if (onError) onError(evento.target.error);
  };
}

function obtenerTodos(storeName, onSuccess, onError) {
  if (!db) {
    if (onError) onError("La base de datos no está inicializada.");
    return;
  }

  var transaccion = db.transaction([storeName], "readonly");
  var almacen = transaccion.objectStore(storeName);
  var peticion = almacen.getAll();

  peticion.onsuccess = function (evento) {
    if (onSuccess) onSuccess(evento.target.result);
  };

  peticion.onerror = function (evento) {
    if (onError) onError(evento.target.error);
  };
}

function obtenerPorId(storeName, id, onSuccess, onError) {
  if (!db) {
    if (onError) onError("La base de datos no está inicializada.");
    return;
  }

  var transaccion = db.transaction([storeName], "readonly");
  var almacen = transaccion.objectStore(storeName);
  var peticion = almacen.get(Number(id));

  peticion.onsuccess = function (evento) {
    if (onSuccess) onSuccess(evento.target.result);
  };

  peticion.onerror = function (evento) {
    if (onError) onError(evento.target.error);
  };
}
