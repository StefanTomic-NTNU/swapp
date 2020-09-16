# Source code for persistence layer
Persistence layer uses the Jackson-library to serialize objects to JSON
Each domain-class uses a corresponding java-class for serializing and deserializing. 
JacksonConfigurator consiosts of methods for creating and configurering ObjectMapper- and SimpleModule-objects needed to serialize and deserialize objects. 
