<template>
  <v-card :style="{ backgroundColor: getCardBackgroundColor() }" >
    <v-card-title>{{ message.dateSent }}</v-card-title>
    <v-card-text>
      <div>From: {{ message.sender_name }}</div>
      <div v-if="message.recipient_id">To: {{ message.recipient_id.name }}</div>
      <div v-else>Not associated with any doctor yet. </div>
      <div>Content: {{ message.content }}</div>
      <router-link :to="`/messages/${message.id}`">View Details</router-link>
    </v-card-text>
  </v-card>
</template>
  
<script>
import { mapGetters } from 'vuex';

export default {
  name: 'MessageCard',
  props: {
    message: {
      type: Object,
      required: true
    },
    backgroundColor: {
      type: String,
      required: true,
    },
  },
  computed: {
    ...mapGetters(["getName"]),
  },
  methods: {
    getCardBackgroundColor() {
      return this.backgroundColor === "blue" ? "#f0f0f0" : "#add8e6";
    },
  },

}
</script>
  
<style>
.custom-background {
  background-color: rgb(163, 197, 82); 
}
</style>
  