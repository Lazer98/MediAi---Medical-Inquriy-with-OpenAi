<template>
    <v-main class="mx-10">
        <h2>Please enter your old password and choose a new one</h2>
        <v-form v-if="choose === 'doctor'" class="mt-8" cols="7" ref="form" v-model="valid" v-on:submit.prevent>
            <v-text-field :rules="passwordRules" v-model="oldPassword" label="Old Password" required></v-text-field>
            <v-text-field :rules="passwordRules" v-model="newPassword" label="New Password" required></v-text-field>
            <v-btn type="submit" color="primary" @click="postNewPassword()" :disabled="!valid">Submit</v-btn>
        </v-form>


    </v-main>
</template>
    
<script>
import { mapMutations } from "vuex";
import { mapGetters } from "vuex";
export default {
    name: 'ForgotPassword',
    data() {
        return {
            email: '',
            valid: false,
            passwordRules: [
                v => !!v || 'Password is required',
                v => (v && v.length >= 2) || 'Password must be at least 2 characters long',
            ],
            oldPassword: '',
            newPassword: ''

        }
    },
    methods: {
        ...mapMutations(["setDatabaseIdOfUser", "setAge", "setName", "setEmail", "setBirthdate", "setDocid", "setPassword"]),
        async  getTheTokenViaEmail() {
            try {
                const response = await fetch('http://localhost:8082/new-password', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({
                        oldPassword: this.oldPassword,
                        newPassword: this.newPassword
                    })
                });

                if (response.ok) {
                    //const result = await response.json();
                    this.$router.push('/');

                } else {
                    console.error('Failed:', response.status);
                }
            } catch (error) {
                console.error('An error occurred', error);
            }
        },
        async  postNewPassword() {
            try {
                const response = await fetch('http://localhost:8082/new-password', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({
                        oldPassword: this.oldPassword,
                        newPassword: this.newPassword
                    })
                });

                if (response.ok) {
                    //const result = await response.json();
                    this.$router.push('/');

                } else {
                    console.error('Failed:', response.status);
                }
            } catch (error) {
                console.error('An error occurred', error);
            }
        },
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
/* Add any custom styles here */
</style>
    