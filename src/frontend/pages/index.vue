<template>
  <v-container>
    <v-row justify="center" align="center">
      <v-col cols="12" sm="8" md="6">
        <v-card>
          <v-card-title class="headline">
            Welcome to my CMPT-383 project!
          </v-card-title>
          <v-card-text>
            <p>
              The project tries to implement a modern online judge system for competitive programming contests and
              practice. The system has a set of programming questions mainly on topics of algorithm and data
              structures.
            </p>

            <h3>Languages used in this project</h3>
            <ul>
              <li>JavaScript: the web front-end. This project uses Vue.js framework and Vuetify UI component library.</li>
              <li>Java: the application server and judge server. The server is built with Quarkus and is compatible with GraalVM native-image (although default in JVM mode to save your compiling time).</li>
              <li>C/C++/Java/JavaScript/Python: There are the languages supported for solving problems. I've done quite amount of code in these languages for each question to handle IO.</li>
            </ul>

            <h3>Inter-languages communication used in this project</h3>
            <ul>
              <li>REST: used by the web front-end and the application server.</li>
              <li>Standard Streams: used by judge server to communicate runner/builder containers, and with your solution code.</li>
            </ul>

            <h3>Complexity</h3>
            <p>
              The amount of work required for this project is non-trivial. I've logged 50+ hours in making of project with ~40 commits.
            </p>
          </v-card-text>
          <v-card-actions>
            <v-spacer />
            <v-btn
              color="primary"
              nuxt
              to="/editor?problemId=5fd2a2f10dfb1770642e13d4"
            >
              Try the easiest problem
            </v-btn>
            <v-btn
              color="primary"
              nuxt
              to="/problems"
            >
              Show me all you got
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import Logo from '~/components/Logo.vue'
import VuetifyLogo from '~/components/VuetifyLogo.vue'

export default {
  head () {
    return {
      title: 'Welcome'
    }
  }
}
</script>
