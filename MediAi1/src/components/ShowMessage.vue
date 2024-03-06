<template>
    <v-main class="mx-10">
        <div>
            <div v-if="this.getDatabaseIdOfUser">
                <v-card>
                    <v-card-text>
                        <div>From: {{ message.sender_name }}</div>
                    </v-card-text>
                </v-card>
                <v-card>
                    <v-card-text>
                        <div v-if="message.recipient_id">To: {{ message.recipient_id.name }}</div>
                        <div v-else>Not associated with any doctor yet. </div>
                    </v-card-text>
                    <v-card-text>
                        <div>Content: {{ message.content }}</div>
                    </v-card-text>
                </v-card>
                <v-btn class="my-2" color="primary" @click="showAnswer = !showAnswer">Answer </v-btn>
                <v-form v-if="showAnswer" class="mt-8" cols="7" ref="form" v-model="valid" v-on:submit.prevent>
                    <v-text-field :rules="contentRules" v-model="content" label="Your message!" required></v-text-field>
                    <v-btn type="submit" color="primary" @click="postMessage()" :disabled="!valid">Submit</v-btn>
                </v-form>
            </div>
            <div v-else>
                You have to login to be here!
            </div>
        </div>




    </v-main>
</template>
    
<script>
import { mapGetters } from 'vuex';
import { mapMutations } from "vuex";
import axios from 'axios'


export default {
    name: 'ShowMessage',
    data() {
        return {
            message: {},
            recipient: {},
            showAnswer: false,
            content: '',
            valid: false,
            contentRules: [
                v => !!v || 'Message is required',
                v => (v && v.length >= 3) || 'Message must be at least 3 characters long',
            ],
        };
    },
    components: {

    },
    computed: {
        ...mapGetters(["getDocid", "getName", "getPassword", "getDatabaseIdOfUser"]),

    },
    mounted() {
        //get request to get the inquiry's information
        axios.get(`http://localhost:8082/messages/${this.$route.params.id}`)
            .then(result => {
                console.log(result.data);
                this.message = result.data;
            }).catch(error => {
                console.error(error);
            });

        //get the recipient if there is one
        if (this.message.recipient_id) {
            axios.get(`http://localhost:8082/patients/${this.$route.params.id}`)
                .then(result => {
                    console.log(result.data);
                    this.recipient = result.data;
                }).catch(error => {
                    console.error(error);
                    //not a patient
                    axios.get(`http://localhost:8082/doctors/${this.$route.params.id}`)
                        .then(result => {
                            console.log(result.data);
                            this.recipient = result.data;
                        }).catch(error => {
                            console.error(error);
                            //neither a doctor nor a patient ,simply an error
                        });
                });
        }

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
                "recipient_id": this.message.sender_id.id,
                "sender_id": this.getDatabaseIdOfUser,
                "sender_name": this.getName,
                "dateSent": new Date()
            }).then(response => {
                console.log(response);
                this.content = '';
                this.valid = false; // Reset the form validation state
                this.$refs.form.reset(); // Reset the form fields and validation state
                //update the message to set the recipient id
                if (this.message.recipient_id === null) {
                    axios.put(`http://localhost:8082/messages/${this.$route.params.id}`, {
                        "content": this.message.content,
                        "sender_id": this.message.sender_id.id,
                        "recipient_id": this.getDatabaseIdOfUser,
                        "sender_name": this.message.sender_name,
                        "dateSent": this.message.dateSent
                    }).then(response => {
                        console.log("update:"+response);


                    });
                }

            });
        }
    }
}
</script>
    
<style>
/* Add any custom styles here */
</style>
    