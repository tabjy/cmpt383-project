<template>
  <v-container fill-height style="max-width: none; padding: 0; display: block; ">
    <v-row style="height: 64px; background-color: lightgrey">
      <v-col cols="4" style="background-color: #eee"/>
      <v-col cols="2">
        <v-select
          v-model="language"
          :items="languageTemplates.map(template => template.display)"
          label="Language"
          dense
          solo
          @change="onLanguageChange"
        />
      </v-col>
      <v-col
        cols="6"
        class="text-right"
      >
        <v-btn
          depressed
          color="primary"
          style="margin-right: 12px;"
          :disabled="evaluating"
          @click="submit"
        >
          Submit
        </v-btn>
      </v-col>
    </v-row>
    <v-row style="height: calc(100% - 64px)">
      <v-col cols="4" style="background-color: #eee; padding: 12px 12px 12px 24px;">
        <h2>Problem title</h2>
        <hr/>
        <p>This side bar is for problem description, which is currently empty.</p>
        <p>Try modifying the code however you like. Hit submit to evaluate it on the judge server.</p>
        <p>Currently only C, C++, and JavaScript are supported. Other languages will be added.</p>
        <pre>HAPPY CODING!
        </pre>
      </v-col>
      <v-col cols="8" style="background-color: white; padding-bottom: 0">
        <v-row style="height: calc(100% - 150px); padding: 0">
          <iframe
            v-show="loaded"
            id="editor-iframe"
            src="/editor-iframe.html"
            style="height: 100%; width: 100%; border: none;"
          />
          <v-row v-show="!loaded" align="center" justify="center" style="height: 100%">
            <div class="text-center">
              <v-progress-circular
                indeterminate
                color="primary"
              />
              <p>Loading code editor...</p>
            </div>
          </v-row>
        </v-row>
        <v-row style="height: 150px; background-color: #333">
          <pre
            style="height: 100%; width: 100%; padding: 12px; color: #eee; font-family: monospace; font-size: 10pt; overflow-y: auto">{{
              output
            }}</pre>
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
let editor

export default {
  data () {
    return {
      loaded: false,
      evaluating: false,
      output: '',
      language: 'C',
      languageTemplates: [
        {
          display: 'C',
          value: `#include <stdio.h>

int main() {
   printf("Hello, world!");
   return 0;
}
`,
          language: 'c',
          lintingLanguage: 'cpp'
        }, {
          display: 'C++',
          value: `#include <iostream>

int main() {
    std::cout << "Hello, world!";
    return 0;
}
`,
          language: 'cpp',
          lintingLanguage: 'cpp'
        }, {
          display: 'JavaScript',
          value: `(function() {
    console.log("Hello, world!");
})()
`,
          language: 'javascript',
          lintingLanguage: 'javascript'
        }
      ]
    }
  },
  mounted () {
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
          this.gotValue(data.value)
          return
        case 'ready':
          this.onReady()
      }
    }
  },
  methods: {
    onLanguageChange (event) {
      const { value, lintingLanguage } = this.languageTemplates.find(template => template.display === this.language)
      editor.contentWindow.postMessage(JSON.stringify({
        method: 'createEditor',
        value,
        language: lintingLanguage
      }), '*')
    },
    onReady () {
      this.loaded = true
      this.onLanguageChange(null)
    },
    submit (event) {
      this.evaluating = true
      this.output = 'Evaluating...'
      editor.contentWindow.postMessage(JSON.stringify({
        method: 'getValue'
      }), '*')
    },
    gotValue (value) {
      const { language } = this.languageTemplates.find(template => template.display === this.language)
      this.$axios.get(document.location.origin + '/api/evaluate', {
        params: {
          language,
          value
        }
      }).then((resp) => {
        this.evaluating = false
        this.output = resp.data
        console.log(resp)
      }).catch((err) => {
        this.evaluating = false
        this.output = ''

        alert(err.response.data)
      })
    }
  }
}
</script>
