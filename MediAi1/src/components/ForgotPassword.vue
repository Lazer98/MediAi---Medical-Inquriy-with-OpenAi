<template>
  <v-main class="mx-10">
    <h2>Did you forgot your password?</h2>
    <h2>Please enter your email adress in order to get a link per email.</h2>
    <v-form class="mt-8" cols="7" ref="form" v-model="valid" v-on:submit.prevent>
      <v-text-field :rules="emailRules" v-model="email" label="Email" required></v-text-field>
      <v-btn type="submit" color="primary" @click="sendEmail()" :disabled="!valid">Submit</v-btn>
    </v-form>


  </v-main>
</template>
  
<script>
export default {
  name: 'ForgotPassword',
  data() {
    return {
      email:'',
      valid:false,
      emailRules: [
        v => !!v || 'E-mail is required',
        v => /.+@.+\..+/.test(v) || 'E-mail must be valid',
        v => (v && v !== 'patient@example.com') || 'E-mail must not be already in use',
      ],

    }
  },
  methods:{
    async sendEmail(){
            try {
                const response = await fetch('http://localhost:8082/forgot-password', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({
                        email: this.email,
                        password: this.getPassword
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
    }
  }

</script>
  
<style>
/* Add any custom styles here */
</style>
  