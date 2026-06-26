var DB_NAME = 'pokedexDB';
var DB_VERSION = 1;
var db = null;

function initDB(callback) {
  var request = indexedDB.open(DB_NAME, DB_VERSION);

  request.onupgradeneeded = function(e) {
    var database = e.target.result;
    if (!database.objectStoreNames.contains('entrenadores')) {
      database.createObjectStore('entrenadores', { keyPath: 'id', autoIncrement: true });
    }
    if (!database.objectStoreNames.contains('equipos')) {
      database.createObjectStore('equipos', { keyPath: 'id', autoIncrement: true });
    }
  };

  request.onsuccess = function(e) {
    db = e.target.result;
    if (callback) callback(db);
  };

  request.onerror = function() {
    console.error('Error abriendo IndexedDB');
  };
}

function agregarRegistro(store, data, callback) {
  var tx = db.transaction(store, 'readwrite');
  var req = tx.objectStore(store).add(data);
  req.onsuccess = function() { if (callback) callback(req.result); };
}

function obtenerTodos(store, callback) {
  var tx = db.transaction(store, 'readonly');
  var req = tx.objectStore(store).getAll();
  req.onsuccess = function() { callback(req.result); };
}

function obtenerPorId(store, id, callback) {
  var tx = db.transaction(store, 'readonly');
  var req = tx.objectStore(store).get(id);
  req.onsuccess = function() { callback(req.result); };
}