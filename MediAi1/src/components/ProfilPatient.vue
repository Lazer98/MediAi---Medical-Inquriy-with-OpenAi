<template>
  <v-main class="mx-10">
    <div>
      <div v-if="this.getDocid">

        show the personal information :
        <h1>Profil Patient</h1>
        <div>Name: {{ patient.name }}</div>
        <div>Email: {{ patient.email }}</div>
        <div>Age: {{ patient.age }}</div>
        <div>Birthdate: {{ patient.birthdate }}</div>


       <h4 >show all messages from this patient: </h4> 
        <div class="mt-4" v-if="messages.length === 0">No previous messages found.</div>
        <div v-else>
          <MessageCard v-for="message in messages" :key="message.id" :message="message" />
        </div>

        <h4>show all inquiries :</h4>
        <div class="mt-4" v-if="inquiries.length === 0">No previous inquiries found.</div>
        <div v-else>
          <InquiryCard v-for="inquiry in inquiries" :key="inquiry.id" :inquiry="inquiry" />
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
import InquiryCard from './InquiryCard.vue';

import axios from 'axios'

export default {
  name: 'PatientProfil',
  data() {
    return {
        patient:{},
        messages:[],
        inquiries:[]

    };
  },
  components: {
    MessageCard,
    InquiryCard
  },
  computed: {
    ...mapGetters(["getName", "getPassword", "getDatabaseIdOfUser","getDocid"]),

  },
  mounted() {
    //get all messages 
    axios.get("http://localhost:8082/messages").then(result => {
      console.log(result.data);
      this.messages = result.data;
      //.filter(message => ((message.sender_id.id === this.$route.params.id) || (message.recipient_id.id == this.$route.params.id)));
    }, error => {
      console.error(error);
    });

    //get all inquiries 
    axios.get(`http://localhost:8082/inquiries/user/${this.$route.params.id}`).then(result => {
      this.inquiries = result.data;
      //.filter(inquiry => ((inquiry.patient_id.id === this.$route.params.id));
    }, error => {
      console.error(error);
    });


    //get request to get the user's information
    axios.get(`http://localhost:8082/patients/${this.$route.params.id}`)
      .then(result => {
        console.log(result.data);
        this.patient = result.data;
      }).catch(error => {
        console.error(error);
      });


      const user = JSON.parse(localStorage.getItem('user'));
    if (user && user.expires > new Date().getTime()) {
      const { patientInLocalStorage, doctorInLocalStorage } = user;
      if (patientInLocalStorage) {
        const { name, email, age, birthdate, password ,databaseIdOfUser } = patientInLocalStorage;
        this.setName(name);
        this.setEmail(email);
        this.setAge(age);
        this.setBirthdate(birthdate);
        this.setPassword(password);
        this.setDatabaseIdOfUser(databaseIdOfUser);

      } else if (doctorInLocalStorage) {
        const { name, email, docid,databaseIdOfUser } = doctorInLocalStorage;
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
    ...mapMutations(["setDatabaseIdOfUser","setAge", "setName", "setEmail", "setBirthdate", "setDocid", "setPassword"]),

  }
}
</script>
    
<style>
/* Add any custom styles here */
</style>