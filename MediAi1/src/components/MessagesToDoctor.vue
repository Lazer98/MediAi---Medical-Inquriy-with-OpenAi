<template>
  <v-main class="mx-10">
    <div>
      <div v-if="this.getDocid">

        Your messages:
        <div class="mt-4" v-if="messagesForYou.length === 0">No previous messages found.</div>
        <div v-else>
          <MessageCard v-for="message in messagesForYou" :key="message.id" :message="message" :backgroundColor="getBackgroundColor(message)" class="my-3" />
        </div>

        Unread messages:
        <div class="mt-4" v-if="messagesUnread.length === 0">No unread messages found.</div>
        <div v-else>
          <MessageCard v-for="message in messagesUnread" :key="message.id" :backgroundColor="getBackgroundColor(message)" :message="message" class="my-3"/>
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
import axios from 'axios'

export default {
  name: 'MessagesToDoctor',
  data() {
    return {
      messagesUnread: [],
      messagesForYou: [],
    };
  },
  components: {
    MessageCard
  },
  computed: {
    ...mapGetters(["getDocid" ,"getDatabaseIdOfUser"]),

  },
  mounted() {
    //get all messages 
    axios.get("http://localhost:8082/messages").then(result => {
      console.log(result.data);
      this.messagesForYou = result.data.filter((message => ( (message.recipient_id) && (message.recipient_id.id === this.getDatabaseIdOfUser)) ));
      this.messagesUnread = result.data.filter(message => (message.recipient_id === null));
    }, error => {
      console.error(error);
    });


    const user = JSON.parse(localStorage.getItem('user'));
    if (user && user.expires > new Date().getTime()) {
      const { patientInLocalStorage, doctorInLocalStorage } = user;
      if (patientInLocalStorage) {
        const { name, email, age, birthdate, password } = patientInLocalStorage;
        this.setName(name);
        this.setEmail(email);
        this.setAge(age);
        this.setBirthdate(birthdate);
        this.setPassword(password);
      } else if (doctorInLocalStorage) {
        const { name, email, docid } = doctorInLocalStorage;
        this.setName(name);
        this.setEmail(email);
        this.setDocid(docid);
      }
    } else if (user && user.expires < new Date().getTime()) {
      localStorage.removeItem('user');
    }
  },
  methods: {
    ...mapMutations(["setAge", "setName", "setEmail", "setBirthdate", "setDocid", "setPassword"]),
    getBackgroundColor(message) {
    return (message.recipient_id && message.recipient_id.docid) ? "blue" : "red";
  },
  }
}
</script>
  
<style>
/* Add any custom styles here */
</style>
  