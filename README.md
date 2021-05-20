# Arquitectura de la solución

![alt text](docs/mutant-detector.png)

Stack utilizado: Java + Amazon web services + MongoDB Atlas.  
  
Para el diseño se tuvo en cuenta:  
a) Variación agresiva de tráfico (requerimiento explícito)  
Solución: funciones lambda. Desacople de la solución en 3 módulos:
- dna-analyer: encargado de analizar el adn
- dna-persister: encargado de persistir el adn 
- dna-stats: encargado de entregar las estadísticas  

b) Tamaño de los datos (requerimiento supuesto):
Tratándose de adn se buscó una implementación que permitiera lidiar con 
largas cadenas de texto. 
Se eligió una db no-sql no sólo por el poder de escalamiento sino también por la mejor 
performance a la hora de realizar búsquedas. Y particularmente MongoDB 
(en lugar de DynamoDB, por ejemplo) ya que mongo permite máximos de 16MB por collection.
Se utilizó el cliente extendido de Amazon sqs que permite enviar
mensajes de hasta 2GB valiéndose de S3 como capa de persistencia intermedia.


Requerimientos para la ejecución local:
- jdk 1.8 o superior
- maven
- docker
- aws sam cli
- mongodb

Para simplificar la ejecución, en modo local no se utiliza sqs ni S3 (*dna-analyzer* invoca directamente a *dna-persister*).  
Pasos:  
- Configurar properties de conección a MongoDB, en el módulo **dna-integration-test**:  
*dna-integration-test/resources/app.properties*  

(La app creará database de nombre "dna" por defecto y dos colecciones: "mutant" y "human".
El valor de las properties puede sobreescribirse si se setea una variable de ambiente del mismo
nombre.)

- Ejecutar, en directorio root:
1) *mvn clean install*  
2) *sam build*
3) *sam local start-api* 

Acceso a los endpoints locales:
- POST http://localhost:3000/mutant
- GET http://localhost:3000/stats

Endpoints remotos en AWS:
- https://0t4g1eo04d.execute-api.us-east-1.amazonaws.com/test/stats
- https://0ytar4ltb4.execute-api.us-east-1.amazonaws.com/test/mutant

  
  
Consideraciones para la instalación en AWS si Magneto deseara llevar esto en producción:  
- dna-persister : la cantidad máxima de instancias debe balancearse acorde a la cantidad máxima de conecciones
                  que mongodb nos permite. En la url de la conección a mongo deberá setearse un idle-time adecuado 
                  (ya que las conecciones quedarán abiertas luego que la instancia lambda deje de existir).  
- dna-stats : configurar de caching a nivel de api-gateway con un timeout adecuado. Esto es, lo más grande posible 
dependiendo de los requerimientos del cliente (qué delay soportaría para el refresh de sus datos).
Se asumió que este delay no sería exigente (en el orden de segundos). Si la exigencia fuese mayor y el 
tráfico en este endpoint comenzara a dar problemas con las conecciones a mongo, habría que pensar en desacoplar 
*dna-stats* de mongo, ya sea utilizando una solución de caching con persistencia, como Redis, o empleando otra base de datos de 
alto poder de escalamiento, como DynamoDB, donde *dna-persister* almacenaría exclusivamente los totales.

Solución alternativa:  

![alt text](docs/mutant-detector-redis.png)
