<template>
  <v-container>
    <v-row justify="center" align="center">
      <v-col cols="12" sm="8" md="6">
        <div class="text-center">
          <logo />
          <vuetify-logo />
        </div>
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
            <p>
              Contestants will use the website to view problems. Contestants will write code to solve problems. An
              in-browser code editor with basic highlighting and autocomplete will be available. Once submitted,
              the contestant's code will be compiled and executed on a judge server against multiple, possibly
              hidden test cases for validity.
            </p>
            <p>
              Contestants' performances are measured by programs' usage of CPU time and RAM. A leader board ranks
              all contestants who have submitted a solution for a question. Only contestants with accepted
              solutions will enter the leader board.
            </p>
            <p>
              <em>
                THIS BUILD IS WORK-IN-PROGRESS!
              </em>
            </p>
          </v-card-text>
          <v-card-actions>
            <v-spacer />
            <v-btn
              color="primary"
              nuxt
              to="/editor"
            >
              Open Editor
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
  components: {
    Logo,
    VuetifyLogo
  }
}
</script>
