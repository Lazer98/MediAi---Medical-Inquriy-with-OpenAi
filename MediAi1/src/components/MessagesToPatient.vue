<template>
  <v-main class="mx-10">
    <div>
      <div v-if="this.getPassword">
        <h2>Write a new message:</h2>
        <v-form class="mt-8" cols="7" ref="form" v-model="valid" v-on:submit.prevent>
          <v-text-field :rules="contentRules" v-model="content" label="Your message!" required></v-text-field>
          <v-btn class="my-4" type="submit" color="primary" @click="postMessage()" :disabled="!valid">Submit</v-btn>
        </v-form>

        <!-- show all messages connected to the patient -->
        <div class="mt-4" v-if="messages.length === 0">No previous messages found.</div>
        <div v-else class="my-2">
          <MessageCard v-for="message in messages" :key="message.id" :backgroundColor="getBackgroundColor(message)" :message="message" class="my-3"/>
        </div>

      </div>
      <div v-else>
        <div>You have to login to be here.</div>
      </div>
    </div>
  </v-main>
</template>
    
<script>
import { mapGetters } from 'vuex';
import { mapMutations } from "vuex";
import MessageCard from './MessageCard.vue';
import axios from 'axios';


export default {
  name: 'MessagesToPatient',
  data() {
    return {
      content: '',
      valid: false,
      messages: [],
      contentRules: [
        v => !!v || 'Message is required',
        v => (v && v.length >= 3) || 'Message must be at least 3 characters long',
      ],
    };
  },
  components: {
    MessageCard
  },
  computed: {
    ...mapGetters(["getName", "getPassword", "getDatabaseIdOfUser"]),

  },
  mounted() {
    //get all messages 
    axios.get("http://localhost:8082/messages").then(result => {
      console.log(result.data);
      this.messages = result.data.filter(message => ((message.sender_id.id === this.getDatabaseIdOfUser) || (message.recipient_id.id == this.getDatabaseIdOfUser)));
    }, error => {
      console.error(error);
    });

    const user = JSON.parse(localStorage.getItem('user'));
    if (user && user.expires > new Date().getTime()) {
      const { patientInLocalStorage, doctorInLocalStorage } = user;
      if (patientInLocalStorage) {
        const { name, email, age, birthdate, password, databaseIdOfUser } = patientInLocalStorage;
        this.setName(name);
        this.setEmail(email);
        this.setAge(age);
        this.setBirthdate(birthdate);
        this.setPassword(password);
        this.setDatabaseIdOfUser(databaseIdOfUser);

      } else if (doctorInLocalStorage) {
        const { name, email, docid, databaseIdOfUser } = doctorInLocalStorage;
        this.setName(name);
        this.setEmail(email);
        this.setDocid(docid);
        this.setDatabaseIdOfUser(databaseIdOfUser);
      }
    } else if (user && user.expires < new Date().getTime()) {
      localStorage.removeItem('user');
    }
  },
  methods: {
    ...mapMutations(["setDatabaseIdOfUser", "setAge", "setName", "setEmail", "setBirthdate", "setDocid", "setPassword"]),
    postMessage() {
      axios.post("http://localhost:8082/messages", {
        "content": this.content,
        "recipient_id": null,
        "sender_id": this.getDatabaseIdOfUser,
        "sender_name": this.getName,
        "dateSent": new Date()
      }).then(response => {
        console.log(response);
        this.content = '';
        this.valid = false; // Reset the form validation state
        this.$refs.form.reset(); // Reset the form fields and validation state
      });
    },
    getBackgroundColor(message) {
    return (message.recipient_id && message.recipient_id.docid) ? "blue" : "red";
  },
  },

}
</script>
    
<style>
/* Add any custom styles here */
</style>
    