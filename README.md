# 7563-DHT
Distributed Hash Table

## Requerimientos

- Java 8
- Maven 3

## Ejemplos

Primero clonar el proyecto:

- `git clone https://github.com/gguzelj/7563-DHT.git && cd 7563-DHT`

Buildear proyecto

- `mvn clean install`

### Levantar un nodo semilla

- `java -jar -Dserver.port=8080 controller/target/controller-0.1-SNAPSHOT.jar`

Luego podemos consultar el estado del anillo:

- `curl -X GET "http://localhost:8080/events"`

```json
[
    {
        "timestamp": 1553463685898,
        "type": "ADD",
        "nodeId": "26efe3ae-027d-43da-8171-d2aaad1f617c",
        "name": "Ted",
        "uri": "http://127.0.0.1:8080",
        "tokens": [
            {
                "value": "a0f1490a20d0211c997b44bc357e1972deab8ae3"
            },
            {
                "value": "adad2ca7ab313add6e955f704719e03d5229e4d0"
            }
        ]
    }
]
```

La base de datos genera un archivo en /tmp/{PORT} y puede consultarse a través del frontend ubicado en http://localhost:8080/h2
 
### Agregar un nuevo nodo

- `java -jar -Dserver.port=8081 -Ddht.ring.seeds="http://127.0.0.1:8080" -Ddht.node.amount-tokens=3 controller/target/controller-0.1-SNAPSHOT.jar`


### Agregar información

Podemos agregar información al cluster consumiendo el siguiente endpoint:

- `curl -X POST "http://localhost:8080" -H "accept: */*" -H "Content-Type: application/json" -d "{ \"type\": \"PUT\", \"key\": \"key_1\", \"value\": \"value_1\" }"`

Para leerla, simplemente hay que cambiar el tipo de operación:

- `curl -X POST "http://localhost:8080" -H "accept: */*" -H "Content-Type: application/json" -d "{ \"type\": \"GET\", \"key\": \"key_1\"}"`

Tener en cuenta que si se agrega un tercer nodo, la información se migra automáticamente:

- `java -jar -Dserver.port=8082 -Ddht.ring.seeds="http://127.0.0.1:8080" -Ddht.node.amount-tokens=3 controller/target/controller-0.1-SNAPSHOT.jar`

### Eliminar un nodo

Eliminar un nodo del cluster puede lograrse realizando la siguiente request:

- `curl -X POST "http://localhost:8082/shutdown"`

 
