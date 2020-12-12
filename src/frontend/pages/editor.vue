<template>
  <v-container fill-height style="max-width: none; padding: 0; display: block; ">
    <v-row style="height: 100%">
      <v-col style="background-color: white; padding: 0px 12px 12px 24px;" cols="4">
        <v-row style="height: 60px; background-color: #eee">
          <v-slide-group
            show-arrows
            mandatory
            style="padding: 12px 0"
          >
            <v-slide-item>
              <v-btn
                class="mx-2"
                :input-value="view === 'overview'"
                active-class="primary white--text"
                rounded
                depressed
                @click="view = 'overview'"
              >
                Overview
              </v-btn>
            </v-slide-item>

            <v-slide-item>
              <v-btn
                class="mx-2"
                :input-value="view === 'leaderboard'"
                active-class="primary white--text"
                rounded
                depressed
                @click="view = 'leaderboard'"
              >
                Leaderboard
              </v-btn>
            </v-slide-item>

            <v-slide-item>
              <v-btn
                class="mx-2"
                :input-value="false"
                active-class="primary white--text"
                rounded
                depressed
                @click="open('/problems', '_blank')"
              >
                More problems
              </v-btn>
            </v-slide-item>
          </v-slide-group>
        </v-row>
        <template v-if="view === 'overview'">
          <v-row v-show="problem.id" style="padding: 12px; height: 0">
            <div v-html="problem.description"/>
          </v-row>
          <v-row v-show="!problem.id" align="center" justify="center" style="height: 100%">
            <div class="text-center">
              <v-progress-circular
                indeterminate
                color="primary"
              />
              <p>Loading overview...</p>
            </div>
          </v-row>
        </template>
        <template v-if="view === 'leaderboard'">
          <v-row v-show="leaderboardLoaded && leaderboard.length !== 0" style="height: 0">
            <v-list
              subheader
              two-line
              style="width: 100%"
            >
              <v-subheader v-show="acceptedLeaders.length > 0" inset>Accepted</v-subheader>

              <v-list-item
                v-for="leader in acceptedLeaders"
                :key="leader.id"
              >
                <v-list-item-avatar>
                  <img
                    :src="`https://www.gravatar.com/avatar/${leader.gravatarHash}?d=identicon`"
                  >
                </v-list-item-avatar>

                <v-list-item-content>
                  <v-list-item-title v-text="leader.username"></v-list-item-title>

                  <v-list-item-subtitle>{{ leader.runtime }}ms - {{
                      (new Date(leader.datetime)).toLocaleString()
                    }}
                  </v-list-item-subtitle>
                </v-list-item-content>

                <v-list-item-action>
                  <v-btn icon>
                    <v-icon color="grey lighten-1">mdi-language-{{ leader.language }}</v-icon>
                  </v-btn>
                </v-list-item-action>
              </v-list-item>

              <v-divider inset v-show="acceptedLeaders.length > 0 && rejectedLeaders.length > 0"></v-divider>

              <v-subheader v-show="rejectedLeaders.length > 0" inset>Submitted but not accepted</v-subheader>

              <v-list-item
                v-for="leader in leaderboard.filter(l => l.acceptance !== 'ac')"
                :key="leader.id"
              >
                <v-list-item-avatar>
                  <img
                    :src="`https://www.gravatar.com/avatar/${leader.gravatarHash}?d=identicon`"
                  >
                </v-list-item-avatar>

                <v-list-item-content>
                  <v-list-item-title v-text="leader.username"></v-list-item-title>

                  <v-list-item-subtitle>{{ acceptanceToString(leader.acceptance) }} -
                    {{ (new Date(leader.datetime)).toLocaleString() }}
                  </v-list-item-subtitle>
                </v-list-item-content>

                <v-list-item-action>
                  <v-btn icon>
                    <v-icon color="grey lighten-1">mdi-language-{{ leader.language }}</v-icon>
                  </v-btn>
                </v-list-item-action>
              </v-list-item>
            </v-list>
          </v-row>
          <v-row v-show="leaderboardLoaded && leaderboard.length === 0" align="center" justify="center"
                 style="height: 100%">
            <div class="text-center">
              <p>Be the first to submit a solution!</p>
            </div>
          </v-row>
          <v-row v-show="!leaderboardLoaded" align="center" justify="center" style="height: 100%">
            <div class="text-center">
              <v-progress-circular
                indeterminate
                color="primary"
              />
              <p>Loading leaderboard...</p>
            </div>
          </v-row>
        </template>
      </v-col>
      <v-col cols="8" style="background-color: white; padding-top: 0; padding-bottom: 0">
        <v-row style="height: 60px; background-color: lightgrey">
          <v-col cols="2">
            <v-select
              v-model="language"
              label="Language"
              :items="problem.templates.map((t) => t.language)"
              dense
              solo
            />
          </v-col>
          <v-spacer></v-spacer>
          <v-col
            cols="6"
            class="text-right"
          >
            <v-btn
              style="margin-right: 12px;"
              :disabled="testing"
              @click="test"
            >
              Test
            </v-btn>
            <v-dialog
              v-model="dialog"
              width="500"
            >
              <template v-slot:activator="{ on, attrs }">
                <v-btn
                  color="primary"
                  style="margin-right: 12px;"
                  :disabled="testing"
                  v-bind="attrs"
                  v-on="on"
                >
                  Submit
                </v-btn>
              </template>

              <v-card>
                <v-card-title class="headline grey lighten-2">
                  {{ !record.id ? 'Submitting your solution' : 'Solution submitted!' }}
                </v-card-title>

                <v-card-text style="margin-top: 16px">
                  <template v-if="!submitting && !record.id">
                    <p>
                      By submitting your solution, you automatically enters the leaderboard for this problem.
                      You can submit as many times as you want, but all submissions will be recorded.
                    </p>
                    <p>
                      The email input is optional. Only the hash value for your address is stored for displaying a
                      <a
                        href="https://gravatar.com/"
                        target="_blank">Gravatar
                      </a> profile picture (if any).
                    </p>

                    <v-form
                      ref="form"
                      v-model="valid"
                      lazy-validation
                    >
                      <v-text-field
                        v-model="username"
                        :counter="10"
                        :rules="usernameRules"
                        label="Username*"
                        required
                      ></v-text-field>

                      <v-text-field
                        v-model="email"
                        :rules="emailRules"
                        label="E-mail"
                        required
                      ></v-text-field>
                    </v-form>
                  </template>

                  <div
                    v-if="submitting"
                    class="text-center">
                    <v-progress-circular
                      indeterminate
                      color="primary"
                    />
                    <p>Grading your solution...</p>
                  </div>

                  <template v-if="record.id">
                    <h3>Your grade:</h3>
                    <br />
                    <br />
                    <p>Acceptance: {{
                        `${record.acceptance.toUpperCase()} (${acceptanceToString(record.acceptance)})`
                      }}</p>
                    <p>Language: {{ record.language }}</p>
                    <p v-if="record.acceptance === 'ac'">Runtime: {{ record.runtime }}ms</p>
                    <p>Date: {{ (new Date(record.datetime)).toLocaleString() }}</p>
                  </template>

                </v-card-text>

                <v-divider></v-divider>

                <v-card-actions>
                  <v-spacer></v-spacer>
                  <v-btn
                    text
                    @click="dialog = false"
                  >
                    Cancel
                  </v-btn>
                  <v-btn
                    color="primary"
                    text
                    :disabled="!valid || !username || submitting || record.id"
                    @click="submit"
                  >
                    Submit
                  </v-btn>
                </v-card-actions>
              </v-card>
            </v-dialog>
          </v-col>
        </v-row>
        <v-row style="height: calc(100% - 150px - 60px); padding: 0; padding-top: 12px">
          <iframe
            v-show="editorLoaded"
            id="editor-iframe"
            src="/editor-iframe.html"
            style="height: 100%; width: 100%; border: none;"
          />
          <v-row v-show="!editorLoaded" align="center" justify="center" style="height: 100%">
            <div class="text-center">
              <v-progress-circular
                indeterminate
                color="primary"
              />
              <p>Loading code editor...</p>
            </div>
          </v-row>
        </v-row>
        <v-row
          style="height: 150px; background-color: #333; padding: 12px; display: block; overflow-y: scroll; font-family: monospace; font-size: 10pt; color: lightgrey">
          <pre v-if="testing > 0" style="width: 100%">Evaluating...</pre>
          <pre
            v-for="(result, index) in results"
            :key="result.runtime"
            style="width: 100%;"
            :style="{color: result.acceptance === 'ac' ? 'lightgreen' : 'red'}">{{
              (() => {
                switch (result.acceptance) {
                  case 'ac':
                    return `AC: Test case #${index + 1} accepted! (${result.runtime}ms)`
                  case 'wa':
                    return `WA: Test case #${index + 1} received a wrong answer! (${result.runtime}ms)\nExpect: ${result.expected}\nActual: ${result.actual}`
                  case 're':
                    return `RE: Test case #${index + 1} exited with a non-zero exit code! (${result.runtime}ms)`
                  case 'ce':
                    return `CE: Test case #${index + 1} encountered a compiler error! (${result.runtime}ms)\n${result.actual}`
                }
                return ''
              })()
            }}</pre>
          <pre v-if="results.length > 0" style="width: 100%">{{ results.filter(r => r.acceptance === 'ac').length }} out of {{
              results.length
            }} test cases passed!</pre>
        </v-row>
      </v-col>
    </v-row>
  </v-container>
</template>

<style>
html {
  overflow-y: hidden;
}
</style>

<script>
const baseUrl = document.location.origin
// const baseUrl = 'http://0.0.0.0:8080'

let editor
let resolveFunc = () => {}
export default {
  head () {
    return {
      title: 'Editor'
    }
  },
  data () {
    return {
      problem: {
        id: null,
        title: null,
        difficulty: null,
        description: null,
        testCases: [],
        templates: []
      },
      language: null,
      testing: false,
      submitting: false,
      editorLoaded: false,
      problemLoaded: false,
      results: [],
      record: {
        id: null,
        problemId: null,
        submissionId: null,
        acceptance: null,
        username: null,
        gravatarHash: null,
        datetime: null,
        runtime: 0
      },
      dialog: false,
      valid: false,
      username: '',
      email: '',
      usernameRules: [
        v => !!v || 'Username is required',
        v => v.length <= 10 || 'Name must be less than 10 characters'
      ],
      emailRules: [
        (v) => {
          if (!v) {
            return true
          }
          return /.+@.+\..+/.test(v) || 'E-mail must be valid'
        }
      ],
      view: 'overview',
      leaderboardLoaded: false,
      leaderboard: []
    }
  },
  computed: {
    acceptedLeaders () {
      return this.leaderboard.filter(l => l.acceptance === 'ac').sort((lhs, rhs) => lhs.runtime - rhs.runtime)
    },
    rejectedLeaders () {
      return this.leaderboard.filter(l => l.acceptance !== 'ac')
    }
  },
  watch: {
    language (event) {
      this.reloadTemplate()
    },
    dialog (event) {
      if (!this.dialog) {
        this.record = {}
        this.submitting = false
      }
    }
  },
  mounted () {
    // eslint-disable-next-line no-unused-vars
    editor = document.getElementById('editor-iframe')
    window.onmessage = (e) => {
      let data
      try {
        data = JSON.parse(e.data)
      } catch (err) {
        return
      }

      switch (data.method) {
        case 'gotValue':
          resolveFunc(data.value)
          return
        case 'ready':
          this.editorLoaded = true
          this.reloadTemplate()
      }
    }
    const url = new URL(document.location.href)
    this.$axios.get(`${baseUrl}/api/problems/${url.searchParams.get('problemId')}`).then((resp) => {
      this.problem = resp.data
      this.language = this.problem.templates[0].language
    }).catch((err) => {
      alert(err.response ? err.response.data.details : 'Cannot communicate with the server!')
    })

    this.refreshLeaderboard()
  },
  methods: {
    open (...args) {
      window.open(...args)
    },
    refreshLeaderboard () {
      this.leaderboard = []
      this.leaderboardLoaded = false

      const url = new URL(document.location.href)
      this.$axios.get(`${baseUrl}/api/problems/${url.searchParams.get('problemId')}/leaderboard`).then((resp) => {
        this.leaderboard = resp.data
      }).catch((err) => {
        alert(err.response ? err.response.data.details : 'Cannot communicate with the server!')
      }).finally(() => {
        this.leaderboardLoaded = true
      })
    },
    acceptanceToString (acceptance) {
      switch (acceptance) {
        case 'ac':
          return 'Accepted'
        case 'wa':
          return 'Wrong Answer'
        case 'tle':
          return 'Time Limit Exceeded'
        case 'mle':
          return 'Memory Limit Exceeded'
        case 're':
          return 'Runtime Error'
        case 'ce':
          return 'Compile Error'
        default:
          return acceptance.toUpperCase()
      }
    },
    reloadTemplate () {
      if (!this.language || !this.editorLoaded) {
        return
      }

      const params = {
        method: 'createEditor',
        value: this.problem.templates.find(t => t.language === this.language).files.find(f => f.editable).content,
        language: this.language
      }
      editor.contentWindow.postMessage(JSON.stringify(params), '*')
    },
    test (event) {
      this.testing = true
      this.results = []
      const p = new Promise((resolve, reject) => { resolveFunc = resolve })
      editor.contentWindow.postMessage(JSON.stringify({ method: 'getValue' }), '*')
      p.then((value) => {
        const url = new URL(document.location.href)
        const params = {
          problemId: url.searchParams.get('problemId'),
          language: this.language,
          files: [{
            path: this.problem.templates.find(t => t.language === this.language).files.find(f => f.editable).path,
            content: value,
            editable: true
          }]
        }
        this.$axios.post(`${baseUrl}/api/submission/test`, params).then((resp) => {
          this.results = resp.data
        }).catch((err) => {
          alert(err.response ? err.response.data.details : 'Cannot communicate with the server!')
        }).finally(() => {
          this.testing = false
        })
      })
    },
    submit (event) {
      this.submitting = true
      this.record = {}
      const p = new Promise((resolve, reject) => { resolveFunc = resolve })
      editor.contentWindow.postMessage(JSON.stringify({ method: 'getValue' }), '*')
      p.then((value) => {
        const url = new URL(document.location.href)
        const params = {
          problemId: url.searchParams.get('problemId'),
          language: this.language,
          files: [{
            path: this.problem.templates.find(t => t.language === this.language).files.find(f => f.editable).path,
            content: value,
            editable: true
          }]
        }
        this.$axios.post(`${baseUrl}/api/submission?username=${encodeURIComponent(this.username)}&email=${encodeURIComponent(this.email)}`, params).then((resp) => {
          this.record = resp.data
        }).catch((err) => {
          alert(err.response ? err.response.data.details : 'Cannot communicate with the server!')
        }).finally(() => {
          this.submitting = false
          this.refreshLeaderboard()
        })
      })
    }
  }
}
</script>
