<template>
  <v-main class="mx-10">
    <div>
      <div v-if="this.getDocid">
        <h2>{{ inquiry.dateSent }}</h2>
        <v-card>
          <v-card-text>
            <div>Answers of the patient: {{ inquiry.contentSendToAi }}</div>
          </v-card-text>
        </v-card>
        <v-card>
          <v-card-text>
            <div>ChatGPT diagnosis: {{ inquiry.diagnosisFromAi }}</div>
          </v-card-text>
        </v-card>
      </div>
      <div v-else>
        You have to login to be here!
      </div>
    </div>

    <!-- show the inquiry
   -->


  </v-main>
</template>
  
<script>
import { mapGetters } from 'vuex';
import { mapMutations } from "vuex";
import axios from 'axios'


export default {
  name: 'ShowInquiry',
  data() {
    return {
      inquiry:{},
    };
  },
  components: {

  },
  computed: {
    ...mapGetters(["getDocid","getName", "getPassword", "getDatabaseIdOfUser"]),

  },
  mounted() {
    //get request to get the inquiry's information
    axios.get(`http://localhost:8082/inquiries/${this.$route.params.id}`)
      .then(result => {
        console.log(result.data);
        this.inquiry = result.data;
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
  