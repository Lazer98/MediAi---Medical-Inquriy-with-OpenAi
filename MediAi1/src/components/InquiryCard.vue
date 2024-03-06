<template>
    <v-main class="mx-10">
        <v-card>
            <v-card-title>{{ inquiry.dateSent }}</v-card-title>
            <v-card-text>
                <div>ID of the patient: {{ inquiry.patient_id.id }}</div>
                <div>Answers of the patient: {{ inquiry.contentSendToAi }}</div>
                <div>ChatGPT diagnosis: {{ inquiry.diagnosisFromAi }}</div>
                <router-link :to="`/inquiry/${inquiry.id}`">View Details</router-link>
            </v-card-text>
        </v-card>
    </v-main>
</template>
    
<script>

export default {
    name: 'InquiryCard',
    props: {
    inquiry: {
      type: Object,
      required: true
    }
  },

}
</script>
    
<style>
/* Add any custom styles here */
</style>
    