# Intersection of Shapes.

In this repository you'll find a small CLI CLI Application in Java that finds the intersections between rectangles.
The list of rectangles will be read from a list whose path is passed as unique parameter.

For calculating the intersection(or collission) between the Rectangles we're using the java.awt library. 
In this way it would be easy to extends the functionalities to the whole Shapes hierarchy. 
For example, the Implementation of the Intersection calculation service doesn't rely on concrete implementations. In my opinion, without any change it could work with every polygonal shape. Perhaps a bit more difficult if it could work also with curvilinear shapes. 
All the Services we implemented indeed work on the abstract type Shape.

# Building

# Building with docker

It's included a docker-compose.yml and Dockerfile that will build the application and automatically run with an example
file provided in the root directory shapes.json.

```
docker-compose up
```
or the equivalent via make:
```
make docker-up
```

At the end of the process you'll the output of the Intersection calculation applied to the content of shapes.json found on the root folder.

# Building with gradle.

```
./gradlew clean build
java -jar ./build/libs/spatial-0.0.1-SNAPSHOT.jar
```
or the equivalent with make:
```
make gradle-build
```

run the test:
```
make tests
```