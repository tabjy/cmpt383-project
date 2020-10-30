<template>
  <v-container fill-height style="max-width: none; padding: 0; display: block; ">
    <v-row style="height: 40px; background-color: lightgrey" />
    <v-row style="height: calc(100% - 80px)">
      <v-col cols="4" style="background-color: grey" />

      <v-col cols="8" style="background-color: white">
        <div ref="editor" style="height: 100%; width: 100%" />
      </v-col>
    </v-row>
    <v-row style="height: 40px; background-color: lightgrey" />
  </v-container>
</template>

<style>
html {
  overflow-y: hidden;
}
</style>

<script>
export default {

}
</script>
