# CMPT-383 Project: Online Judge System

## Topic idea

The project tries to implement a modern online judge system for competitive programming contests and 
practice. The system has a set of programming questions mainly on topics of algorithm and data 
structures. 

Contestants will use the website to view problems. Contestants will write code to solve problems. An 
in-browser code editor with basic highlighting and autocomplete will be available. Once submitted, 
the contestant's code will be compiled and executed on a judge server against multiple, possibly 
hidden test cases for validity.

Contestants' performances are measured by programs' usage of CPU time and RAM. A leader board ranks
all contestants who have submitted a solution for a question. Only contestants with accepted 
solutions will enter the leader board.

The system architects as follow:

![](https://i.imgur.com/UByQTtz.jpg)
 
## Programming Languages
1. JavaScript

    For web front-end. The system uses [Vue.js](https://cn.vuejs.org/index.html) framework and 
    [Vuetify](https://vuetifyjs.com/en/) UI component library.

2. Java

    For the application server and judge server. The application server handles problem and user 
    data. Once a solution is received by an application server, it will be sent to a judge server 
    for validation. The judge server spawns child containers for compiling and running. The runner
    containers are isolated with limited capabilities and resource constraints.

    Judge server is designed as a separate process so multiple judge server can distributedly share 
    the workload.

3. C/C++/Go/Python...

    Basically any languages the online judge system supports. Currently only C, C++, and JavaScript 
    are supported. More language compilers and runtimes will be added.

## Inter-language Communication

1. REST API

    Used by the web front-end and the application server. REST API handles all data a contestant 
    will see or submit.

2. Standard Streams

    Used by judge server and runner/builder containers. Any log or error messages are collected as 
    sent back to the contestants.


Additionally, the Remote Method Invocation (RMI) powered by remote Java Management Extensions (rJMX) 
is used for communication between the application server and judge servers. However, since both 
types of servers are implemented in Java, it is only counted as inter-process communication (IPC),
not inter-language communication.

## Deployment Technology

Multi-container stack deployed with `docker-compose`.

### Get Started

Make sure a docker daemon (version >= 17.09.0) is running and `docker-compose` tool is available in 
`$PATH`.

```
$ git clone https://csil-git1.cs.surrey.sfu.ca/arvinx/cmpt383-project
$ cd cmpt383-project
$ docker-compose up
```

That's it!. Now, with your favourite browser, which is hopefully not the Internet Explorer, open 
[http://localhost:8080/](http://localhost:8080/).


## What Works

![](https://i.imgur.com/frYtsGc.jpg)

- [x] In-browser code editor (powered by 
[monaco-editor](https://github.com/microsoft/monaco-editor))
- [x] C, C++, JavaScript compiling and running
- [x] Compilation and execution result sent back to contestants

## What Does Not Yet Work

- [ ] The application server and judge servers are not yet separated processed
- [ ] There is no problems yet
- [ ] Memory and CPU time limit is not yet implemented
- [ ] Runner containers are not yet well-isolated (will look into 
[gvisor](https://github.com/google/gvisor))

## Application Server Development

### Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

### Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `cmpt383-project-1.0.0-SNAPSHOT-runner.jar` file in the `/target` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/lib` directory.

If you want to build an _über-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application is now runnable using `java -jar target/cmpt383-project-1.0.0-SNAPSHOT-runner.jar`.

### Creating a native executable

You can create a native executable using: 
```shell script
./mvnw package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/cmpt383-project-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.html.
