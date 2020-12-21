# CMPT-383 Project: Online Judge System

![](https://i.imgur.com/RSEcWFA.png)

If you are doing a similar assignment for academic purposes and studying my source code, please be sure to acknowledge. This is most likely required to protect your academic integrity. For all other situations, MIT license applies.

# Features

- [x] Modern full-featured in-browser code editor (powered by [monaco-editor](https://github.com/microsoft/monaco-editor))
- [x] C, C++, Java, JavaScript, and Python code building and running
- [x] Compilation and test case execution results sent back to contestants
- [x] Fancy leaderboard (with avatars!) for comparing scores with your peers
- [x] Sortable and filterable problem list to find suitable problems
- [x] Safely sandboxed environments for executing untrusted user code
- [x] Extensible system allowing easily add new problems to database (look in [src/seeds](src/seeds))
- [x] Real competitive programming problems!

## Project Idea

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

## Get Started

Make sure a docker daemon (version >= 17.09.0) is running and `docker-compose` tool is available in
`$PATH`.

```
$ git clone https://csil-git1.cs.surrey.sfu.ca/arvinx/cmpt383-project
$ cd cmpt383-project
$ docker-compose up
```

That's it!. Now, with your favourite browser, which is hopefully not the Internet Explorer, open
[http://localhost:8080/](http://localhost:8080/).

## Programming Languages
1. JavaScript

    For the web front-end. The system uses [Vue.js](https://cn.vuejs.org/index.html) framework and 
    [Vuetify](https://vuetifyjs.com/en/) UI component library.

2. Java

    For the application server and judge server. The application server handles problem and user 
    data. Once a solution is received by an application server, it will be sent to a judge server 
    for validation. The judge server spawns child containers for compiling and running. The runner
    containers are isolated with limited capabilities and resource constraints.

    Judge server is designed as a separate process so multiple judge server can distributively share 
    the workload.

3. C/C++/Python...

    Basically any languages the online judge system supports. Currently, only C, C++, and JavaScript 
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

## Design

The system architects as follows:

![](https://i.imgur.com/UByQTtz.jpg)
