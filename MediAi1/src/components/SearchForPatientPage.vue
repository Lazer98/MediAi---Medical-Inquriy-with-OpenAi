<template>
  <v-main class="mx-10">
    <div>
      <div v-if="this.getDocid">
        <v-form>
          <v-text-field v-model="searchTerm" label="Search" clearable></v-text-field>
          <v-btn class="my-3" @click="searchPatients" color="primary">Search</v-btn>
        </v-form>

        <div v-if="patients.length === 0">No patients found.</div>
        <div v-else>
          <PatientCard v-for="patient in filteredPatients" :key="patient.id" :patient="patient" class="my-3"/>
        </div>
        
      </div>
      <div v-else>
        <div>You have to login to be here.</div>
      </div>
    </div>
  </v-main>
</template>
  
<script>
import PatientCard from './PatientCard.vue';
import axios from 'axios'
import { mapGetters } from 'vuex';
import { mapMutations } from "vuex";

export default {
  name: "SearchForPatientPage",
  data() {
    return {
      searchTerm: '',
      patients: []
    };
  },
  components: {
    PatientCard
  },
  computed: {
    filteredPatients() {
      if (this.searchTerm === '') {
        return this.patients;
      }
      const searchTermLower = this.searchTerm.toLowerCase();
      return this.patients.filter(
        (patient) =>
          patient.name.toLowerCase().includes(searchTermLower) ||
          patient.id.toString().includes(searchTermLower) ||
          patient.email.toLowerCase().includes(searchTermLower) ||
          patient.birthdate.toLowerCase().includes(searchTermLower)
        // Add more conditions based on the properties you want to search for
      );
    },

    ...mapGetters(["getDocid"]),
    isLoggedIn() {
      console.log(this.getDocid);
      return this.getDocid !== null;
    }
  },
  mounted() {
    axios.get("http://localhost:8082/patients").then(result => {
      console.log(result.data);
      this.patients = result.data;
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

    searchPatients() {
      const searchTermLower = this.searchTerm.toLowerCase();
      return this.patients.filter(
        (patient) =>
          patient.name.toLowerCase().includes(searchTermLower) ||
          patient.id.toString().includes(searchTermLower) ||
          patient.email.toLowerCase().includes(searchTermLower)
        // Add more conditions based on the properties you want to search for
      );
    }
  }
}
</script>
  
<style>
/* Add any custom styles here */
</style>
  