<template>
  <v-app class="background-image">
    <v-app-bar>
      <v-toolbar-title>
        <p class="mr-3" v-if="this.getName">Hello {{ this.getName }}!</p>
      </v-toolbar-title>
      <v-spacer></v-spacer>

      <v-btn class="mr-3" to="/">Home</v-btn>
      <v-btn v-if="!this.getName" class="mr-3" to="/registration">Registration</v-btn>
      <!-- BUTTONS FOR DOCTOR -->
      <v-btn v-if="this.getDocid" class="mr-3" to="/profil-doctor">My profil</v-btn>
      <v-btn v-if="this.getDocid" class="mr-3" to="/messages-to-doctor">Messages</v-btn>
      <v-btn v-if="this.getDocid" class="mr-3" to="/search-for-patient">Search for patients</v-btn>
      <!-- BUTTONS FOR PATIENT -->
      <v-btn v-if="this.getPassword" class="mr-3" to="/chat-bot">New Inquiry</v-btn>
      <v-btn v-if="this.getPassword" class="mr-3" to="/messages-to-patient">Messages</v-btn>

      <svg-icon v-if="!this.getName" @click="showPopup = !showPopup" class="mr-7" type="mdi"
        :path="pathLogin">Login</svg-icon>

      <v-dialog v-model="showPopup" max-width="500">
        <v-card>
          <v-card-title>
            Login
          </v-card-title>
          <v-card-text>
            Not registrated yet?
            <v-btn @click="showPopup = false" class="mr-3" to="/registration">Registration</v-btn>

            <!-- DOCTOR FORM  LOGIN-->

            <v-form v-if="choose === 'doctor'" class="mt-8" cols="7" ref="form" v-model="valid" v-on:submit.prevent>
              <v-text-field :rules="emailRules" v-model="doctor.email" label="Email" type="email" required></v-text-field>
              <v-text-field :rules="IDRules" v-model="doctor.docid" label="ID" type="password" required></v-text-field>
              <v-row>
                <v-btn type="submit" color="primary" @click="loginDoctor()" :disabled="!valid">Submit</v-btn>
                <p v-if="error" class="red-text my-3">{{ error }}</p>
              </v-row>

            </v-form>


            <!-- PATIENT FORM  LOGIN-->

            <v-form v-if="choose === 'patient'" class="mt-8" cols="7" ref="form" v-model="valid" v-on:submit.prevent>
              <v-text-field :rules="emailRules" v-model="patient.email" label="Email" type="email"
                required></v-text-field>
              <v-text-field :rules="passwordRules" v-model="patient.password" label="Password" type="password"
                required></v-text-field>
              <v-row>
                <v-btn type="submit" color="primary" @click="loginPatient()" :disabled="!valid">Submit</v-btn>
                <p v-if="error" class="red-text ml-3">{{ error }}</p>

              </v-row>

            </v-form>
            <v-row>
              <v-btn class="btn" @click="choose = 'doctor'">
                Doctor
              </v-btn>
              <v-btn class="btn" @click="choose = 'patient'">
                patient
              </v-btn>
            </v-row>
          </v-card-text>
          <v-card-actions>
            <v-btn @click="showPopup = false">Close</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>

      <svg-icon v-if=getName @click="logout" to="/registration" class="mr-7" type="mdi"
        :path="pathLogout">Logout</svg-icon>

    </v-app-bar>
    <v-main>
      <router-view></router-view>
    </v-main>
  </v-app>
</template>


<script>
import { mapMutations } from "vuex";
import SvgIcon from '@jamescoyle/vue-icon';
import { mdiLogin } from '@mdi/js';
import { mdiLogout } from '@mdi/js';
import { mapGetters } from "vuex";
//import { AES } from 'crypto-js';


export default {
  name: 'app',
  data() {
    return {
      valid: true,
      error: null,
      pathLogin: mdiLogin,
      pathLogout: mdiLogout,
      showPopup: false,
      choose: 'doctor',
      patient: {
        name: '',
        email: '',
        password: '',
        age: '',
        birthdate: ''

      },
      doctor: {
        name: '',
        email: '',
        docid: ''

      },
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
    async loginPatient() {
      try {
        const response = await fetch('http://localhost:8082/patients/email', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({
            email: this.patient.email,
            passwordOrDocId: this.patient.password
          })
        });

        if (response.ok) {
          const result = await response.json();
          const patientLoggedIn = result;
          // Process the user ID or perform additional actions
          console.log('Login successful! User ID:', patientLoggedIn.id);

          //update the setters
          this.setName(patientLoggedIn.name);
          this.setAge(patientLoggedIn.age);
          this.setBirthdate(patientLoggedIn.birthdate);
          this.setEmail(patientLoggedIn.email);
          this.setPassword(patientLoggedIn.password);
          this.setDatabaseIdOfUser(patientLoggedIn.id);

          //delete the local variables
          this.patient.password = null;
          this.patient.email = null;

          this.showPopup = false;

          // Save the login details to local storage
         // const encryptedPassword = AES.encrypt(patientLoggedIn.password, 'your-secret-key').toString();
          const patientInLocalStorage = {
            "name": patientLoggedIn.name, "email": patientLoggedIn.email, "age": patientLoggedIn.age,
            "birthdate": patientLoggedIn.birthdate, "password": patientLoggedIn.password, "databaseIdOfUser": patientLoggedIn.id
          };
          const expirationTime = new Date().getTime() + (60 * 60 * 1000); // 60 minutes in milliseconds
          const item = { patientInLocalStorage, expires: expirationTime };
          localStorage.setItem('user', JSON.stringify(item));

          this.$router.push('/');

        } else {
          console.error('Login failed:', response.status);
          this.error = 'Invalid email or password. Please try again.';
        }
      } catch (error) {
        console.error('An error occurred during login:', error);
        this.error = 'An error occurred during login:' + error;
      }
    },
    async loginDoctor() {
      try {
        const response = await fetch('http://localhost:8082/doctors/email', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({
            email: this.doctor.email,
            passwordOrDocId: this.doctor.docid
          })
        });

        if (response.ok) {
          const result = await response.json();
          const doctorLoggedIn = result;
          // Process the user ID or perform additional actions
          console.log('Login successful! User ID:', doctorLoggedIn.id);
          //update the setters
          this.setName(doctorLoggedIn.name);
          this.setEmail(doctorLoggedIn.email);
          this.setDocid(doctorLoggedIn.docid);

          //delete the local variables
          this.doctor.docid = null;
          this.doctor.email = null;

          this.showPopup = false;

          // Save the login details to local storage
          const doctorInLocalStorage = { "name": doctorLoggedIn.name, "email": doctorLoggedIn.email, "docid": doctorLoggedIn.docid, "databaseIdOfUser": doctorLoggedIn.id };
          const expirationTime = new Date().getTime() + (60 * 60 * 1000); // 60 minutes in milliseconds
          const item = { doctorInLocalStorage, expires: expirationTime };
          localStorage.setItem('user', JSON.stringify(item));

          //push to another route
          this.$router.push('/profil-doctor');

        } else {
          console.error('Login failed:', response.status);
          this.error = 'Invalid email or password. Please try again.';
        }
      } catch (error) {
        console.error('An error occurred during login:', error);
        this.error = 'An error occurred during login:' + error;
      }
    },
    logout() {
      console.log("now logging out...");
      this.setName(null);
      this.setAge(null);
      this.setBirthdate(null);
      this.setEmail(null);
      this.setPassword(null);
      this.setDocid(null);
      this.setDatabaseIdOfUser(null);
      //removing the local storage data
      localStorage.removeItem('user');

      this.$router.push('/');
    },
    // decryptPassword(encryptedPassword) {
    //   try {
    //     const bytes = AES.decrypt(encryptedPassword, 'your-secret-key');
    //     return bytes.toString(CryptoJS.enc.Utf8);
    //   } catch (error) {
    //     console.error('Error while decrypting password:', error);
    //     throw error;
    //   }
    // }

  },
  components: {
    SvgIcon
  },
  computed: {
    ...mapGetters(["getName", "getDocid", "getPassword"]),
  },
  mounted() {
    const user = JSON.parse(localStorage.getItem('user'));
    if (user && user.expires > new Date().getTime()) {
      const { patientInLocalStorage, doctorInLocalStorage } = user;
      if (patientInLocalStorage) {
        const { name, email, age, birthdate, password, databaseIdOfUser } = patientInLocalStorage;
        //const decryptedPassword = this.decryptPassword(password);
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
  }
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}

.error-message {
  color: red;
}

.background-image {

  background: url('./assets/pic1.jpg') no-repeat center fixed !important;
  min-height: 100%;
  /* min-width: 1296px; */


}
</style>