<template>
  <v-card class="custom-card">
    <v-card-title>{{ patient.name }}</v-card-title>
    <v-card-text>
      <div>ID: {{ patient.id }}</div>
      <div>Email: {{ patient.email }}</div>
      <div>Age: {{ patient.age }}</div>
      <div>Birthdate: {{ patient.birthdate }}</div>
      <router-link :to="`/profil-patient/${patient.id}`">View Details</router-link>
    </v-card-text>
  </v-card>
</template>
  
<script>

export default {
  name: 'PatientCard',
  props: {
    patient: {
      type: Object,
      required: true
    }
  },

}
</script>
  
<style>
.custom-card{
  background-color: #f0f0f0;
}
</style>
  