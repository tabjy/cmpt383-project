<template>
  <v-container fill-height style="max-width: none; padding: 0; display: block; ">
    <v-row style="height: 40px; background-color: lightgrey"/>
    <v-row style="height: calc(100% - 80px)">
      <v-col cols="4" style="background-color: grey"/>

      <v-col cols="8" style="background-color: white">
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
      </v-col>
    </v-row>
    <v-row style="height: 40px; background-color: lightgrey"/>
  </v-container>
</template>

<style>
html {
  overflow-y: hidden;
}
</style>

<script>
const source = `#include <stdio.h>

int main() {
   printf("Hello, World!");
   return 0;
}
`

export default {
  data () {
    return {
      loaded: false
    }
  },
  mounted () {
    const editor = document.getElementById('editor-iframe')

    window.onmessage = (e) => {
      let data
      try {
        data = JSON.parse(e.data)
      } catch (err) {
        return
      }

      switch (data.method) {
        case 'gotValue':
          console.log(data.value)
          return
        case 'ready':
          this.loaded = true
          editor.contentWindow.postMessage(JSON.stringify({
            method: 'createEditor',
            value: source,
            language: 'cpp'
          }), '*')
      }
    }
  }
}
</script>
