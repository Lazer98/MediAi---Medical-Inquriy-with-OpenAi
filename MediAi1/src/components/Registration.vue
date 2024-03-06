<template>
  <v-main>
    <v-container fluid fill-height>
      <v-row justify="center" align="center">
        <v-col>
          <v-card elevation="4" class="pa-6 text-center borderless-card" color="transparent">

            <!-- DOCTOR FORM  REGISTRATION-->

            <v-form v-if="choose === 'doctor'" class="mt-8" cols="7" ref="form" v-model="valid" v-on:submit.prevent>
              <v-text-field :rules="nameRules" v-model="doctor.name" label="Name" required></v-text-field>
              <v-text-field :rules="emailRules" v-model="doctor.email" label="Email" type="email" required></v-text-field>
              <v-text-field :rules="IDRules" v-model="doctor.docid" label="ID" required></v-text-field>
              <v-btn type="submit" color="primary" @click="postDoctor()" :disabled="!valid">Submit</v-btn>
            </v-form>


            <!-- PATIENT FORM  REGISTRATION-->

            <v-form v-if="choose === 'patient'" class="mt-8" cols="7" ref="form" v-model="valid" v-on:submit.prevent>
              <v-text-field :rules="nameRules" v-model="patient.name" label="Name" required></v-text-field>
              <v-text-field :rules="emailRules" v-model="patient.email" label="Email" type="email"
                required></v-text-field>
              <v-text-field :rules="ageRules" v-model="patient.age" label="Age" required></v-text-field>
              <v-text-field :rules="birthdateRules" v-model="patient.birthdate" label="Birthdate" required></v-text-field>
              <v-text-field :rules="passwordRules" v-model="patient.password" label="Password" required></v-text-field>
              <v-btn type="submit" color="primary" @click="postPatient()" :disabled="!valid">Submit</v-btn>
            </v-form>
            <p v-if="error"  class="red-text my-3">{{ error }}</p>
          </v-card>
        </v-col>
      </v-row>
      <v-row>
        <v-btn class="btn mx-3" color="primary" @click="choose = 'doctor'">
          Doctor
        </v-btn>
        <v-btn class="btn mx-3" color="primary" @click="choose = 'patient'">
          patient
        </v-btn>
      </v-row>
    </v-container>






  </v-main>
</template>
 
<script>

import { mapMutations } from "vuex";
import axios from "axios";


export default {
  name: 'RegistrationWord',
  data() {
    return {
      choose: 'doctor',
      error: null,
      patient: {
        id: '',
        name: '',
        email: '',
        password: '',
        age: '',
        birthdate: ''
      },
      doctor: {
        id: '',
        name: '',
        email: '',
        docid: ''

      },
      valid: false,
      nameRules: [
        v => !!v || 'Name is required',
        v => /^[a-zA-Z]+$/.test(v) || 'Name must not contain numbers or special characters',
        v => (v && v.length >= 2) || 'Name must be at least 2 characters long',
      ],
      passwordRules: [
        v => !!v || 'Password is required',
        v => (v && v.length >= 2) || 'Password must be at least 2 characters long',
      ],
      emailRules: [
        v => !!v || 'E-mail is required',
        v => /.+@.+\..+/.test(v) || 'E-mail must be valid',
        v => (v && v !== 'patient@example.com') || 'E-mail must not be already in use',
      ],
      ageRules: [
        v => !!v || 'Age is required',
        v => /^\d+$/.test(v) || 'Age must be a number',
      ],
      birthdateRules: [
        v => !!v || 'Birthdate is required',
        v => /\d{4}-\d{2}-\d{2}/.test(v) || 'Invalid date format (YYYY-MM-DD)',
        v => {
          const birthdate = new Date(v);
          const today = new Date();
          return birthdate < today || 'Birthdate cannot be in the future';
        },
      ],
      IDRules: [
        v => !!v || 'ID is required',
        v => (v && v.length >= 3) || 'ID must be at least 3 letters long',
      ],
    }
  },
  methods: {
    ...mapMutations(["setDatabaseIdOfUser", "setAge", "setName", "setEmail", "setBirthdate", "setDocid", "setPassword"]),
    postPatient() {
      axios.post("http://localhost:8082/patients",

        {
          "name": this.patient.name,
          "email": this.patient.email,
          "birthdate": this.patient.birthdate,
          "age": this.patient.age,
          "password": this.patient.password
        }

      ).then(response => {
        console.log(response);
        this.patient = response.data;
        this.setName(this.patient.name);
        this.setAge(this.patient.age);
        this.setBirthdate(this.patient.birthdate);
        this.setEmail(this.patient.email);
        this.setPassword(this.patient.password);
        this.setDatabaseIdOfUser(this.patient.id);

        // Save the login details to local storage
        const patientInLocalStorage = {
          "name": this.patient.name, "email": this.patient.email, "age": this.patient.age,
          "birthdate": this.patient.birthdate, "password": this.patient.password, "databaseIdOfUser": this.patient.id
        };
        const expirationTime = new Date().getTime() + (60 * 60 * 1000); // 60 minutes in milliseconds
        const item = { patientInLocalStorage, expires: expirationTime };
        localStorage.setItem('user', JSON.stringify(item));

        this.$router.push('/');
      }).catch(error => {
        this.error = "Error occurred while registering as a patient."; // Set the error message
        console.error(error); // Log the error for debugging
      });

    },
    postDoctor() {
      axios.post("http://localhost:8082/doctors",

        {
          "name": this.doctor.name,
          "email": this.doctor.email,
          "docid": this.doctor.docid
        }

      ).then(response => {
        console.log(response);
        this.doctor = response.data;
        this.setName(this.doctor.name);
        this.setEmail(this.doctor.email);
        this.setDocid(this.doctor.docid);
        this.setDatabaseIdOfUser(this.doctor.id);

        // Save the login details to local storage
        const doctorInLocalStorage = {
          "name": this.doctor.name, "email": this.doctor.email,
          "docid": this.doctor.docid, "databaseIdOfUser": this.doctor.id
        };
        const expirationTime = new Date().getTime() + (60 * 60 * 1000); // 60 minutes in milliseconds
        const item = { doctorInLocalStorage, expires: expirationTime };
        localStorage.setItem('user', JSON.stringify(item));

        this.$router.push('/profil-doctor');


      }).catch(error => {
        this.error = "Error occurred while registering as a doctor."; // Set the error message
        console.error(error); // Log the error for debugging
      });

    },

    // generateToken() {
    //   // Perform authentication and get the patient's credentials
    //   const { email, password } = this;

    //   // Send the credentials to the server for authentication
    //   // On successful authentication, the server can generate a token

    //   // Assuming the server returns a token
    //   const secretKey = 'your-secret-key'; // Replace with your actual secret key
    //   const payload = { email,password }; // Include any additional data in the payload

    //   // Generate the token
    //   const token = jwt.sign(payload, secretKey);

    //   const tokenString = enc.Base64.stringify(enc.Utf8.parse(token));


    //   // Do something with the token, such as storing it in local storage or passing it to another component
    //   console.log(tokenString);
    //   this.setpatient(this.name);
    //   this.setToken(token);
    // },
  },

}
</script>
 
<style>
.background {
  background-color: cornflowerblue
}
.red-text{
  color:red
}
</style>
 