<template>
  <v-main class="mx-10">
    <div>
      <div v-if="this.getDocid">

        <v-btn color="primary" class="mx-3" @click="this.$router.push('/search-for-patient');"> Search For Patients</v-btn>
        <v-btn color="primary" class="mx-3" @click="this.$router.push('/messages-to-doctor');"> Messages</v-btn>


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

export default {
  name: 'ProfilDoctor',
  data() {
    return {

    };
  },
  components: {

  },
  computed: {
    ...mapGetters(["getDocid"]),

  },
  mounted() {
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
  