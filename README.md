# Practice 1 (V2). Data capture from external sources.

Subject: Desarrollo de aplicaciones para ciencia de datos

Course: 2023-2024

Degree: Grado de Ingeniería en Ciencia de Datos

Shool: Escuela Universitaria de Informática

University: Universidad de Las Palmas de Gran Canaria

## Functionality Overview
The WeatherProvider and HotelProvider module is tasked with obtaining meteorological and hotel from the Canary Islands every 6 hours. Additionally, they generate a JSON-formatted event based on the data obtained from both services. The events are sent to the topics named "prediction.Weather" and "search.Hotel" respectively.

The DatalakeBuilder is a module whose purpose is to systematically store consumed events from the broker in a directory based on their temporal order. Therefore, it is subscribed to the aforementioned topics and serializes the events in the datalake following a specific directory structure.

The BusinessUnit module, first tries to take the events from broker, but if there are no events available in the moment of execution, the module is tasked to read the events from the datalake. These events are then deserialized and saved in a datamart. And finally, the information from the datamart is used as a recommendation service to choose to what island to travel and what hotel to go.

### Execution process
To execute the project, there must be an argument in the mains of the "PredictionProvider" and "HotelProvider" modules with the respective user's API key.

Additionally, the "DatalakeBuilder" module should be executed first, so that it waits for messages from the subscribed topics. Consequently, the "PredictionProvider" and "HotelProvider" modules should then be executed to send messages to the broker, while the other module receives them.

Finally, the "BusinessUnit" module then can be executed at any time when it is needed.

## Resources Used
- Development Environments: IntelliJ IDEA
- Version Control Tools: Git
- Documentation Tools: Markdown
- Programming language: Java
- Broker implementation: ActiveMq
- Extern Services: Api from "openweathermap" and api from rapidapi "Hotels Com Provider"

## Design

In this project, there are 4 modules, in the PredictionProvider module there are found 8 different classes:
- The "Main" class, where the "execute()" function is called and runs periodically every 6 hours.
- The "WeatherApiConnector" class, where a function for making API calls is located.
- The "WeatherController" class contains the "execute()" function, where the rest of the functions are called to fulfill the purpose of the practice.
- The classes 'Location' and 'Weather' are used to create instances of themselves in some other implementations to fulfill the intended purpose.
- In the "WeatherProvider" class, which it is used to retrieve data from the API and create the events.
- The "MessageSender" class is responsible for establishing a connection with the broker, creating a topic within it, and sending serialized events.
- Additionally, an interface is created for "WeatherProvider", named "Provider".

In the DatalakeBuilder module there are found 5 different classes:

- The "AMQTopicWeatherSubscriber" class is responsible for subscribing to the topic "prediction.Weather" in the broker where serialized events are sent and subsequently consuming them.
- The "AMQTopicHotelSubscriber" class is responsible for subscribing to the topic "search.Hotel" in the broker where serialized events are sent and subsequently consuming them.
- The "FileEventStore" class is dedicated to creating directories that follow a specific structure, and then storing the consumed events in their respective directories.
- In the "Main" class, the "subscribeToTopic()" function is called to execute the purpose of this module.
- Additionally, there are two interfaces found named "Listener" and "Subscriber."

In the HotelProvider module there are found 7 different classes:

- The "Main" class, where the "execute()" function is called and runs periodically every 6 hours.
- The "HotelApiConnector" class, where a function for making API calls is located.
- The "HotelController" class contains the "execute()" function, where the rest of the functions are called to fulfill the purpose of the practice.
- The class "Hotel" is used to create instances of it in some other implementations to fulfill the intended purpose.
- In the "HotelProvider" class, which it is used to retrieve data from the API and create the events.
- The "MessageSender" class is responsible for establishing a connection with the broker, creating a topic within it, and sending serialized events.
- Additionally, an interface is created for "HotelProvider", named "Provider".

In the BusinessUnit module there are 9 different classes found:

- The "Main" class, where functions are called to achieve the purposes of the app.
- The "AMQTopicWeatherSubscriber" and "AMQTopicHotelSubscriber" classes are meant to complete the same purposes that in the other module.
- The "Datamart" class is used to check if events are received from broker and save them in a deserialize format in the datamart. However, if there are no events available it reads them from the datalake and then saves them in the same format as before in the same place.
- "RecommendationService" is used as a CLI where questions are asked to users to take preferences from them and recommend which island and hotel to travel.
- Additionally, there is an interface called "Subscriber" for the topic subscriber classes.
- Finally, there are the "Hotel", "Location" and "Weather" classes to make instances of themselves in some other implementations to fulfill the intended purpose.


### Patterns and design principles
Regarding design principles:

Firstly, each class has a unique responsibility, such as WeatherApiConnector for API connection, WeatherProvider for weather data retrieval and to create events, FileEventStore to store the serialized events in their respective directories...

Secondly, the code is open to extension, achieved by adding new implementations of interfaces or classes to adapt to different needs.

Furthermore, interfaces are designed for specific roles and do not contain unnecessary methods for their implementers.

Efforts are made to keep the coupling between classes low, as for example the function that is called in the "Main" depends only on the Provider interface, not on specific implementations, and cohesion is high within classes, as each is related to a specific task. Regarding modularity, the code is divided into modular packages and classes, each with a specific responsibility.

In addition, the "Location" and "Weather" classes are immutable, ensuring data stability.

Regarding patterns:

The strategy pattern is employed, where different implementations can be swapped as needed through the use of interfaces.

Another pattern used in this project is the Producer-Consumer pattern, which is useful for handling asynchronous communication between two modules. The event-sending module acts as the producer, while the module that consumes events acts as the consumer.

Additionally, the use of Timer and TimerTask for performing periodic tasks is a form of the observer pattern, where the timer observes and executes the defined tasks.
