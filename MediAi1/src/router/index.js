import {
  createRouter,
  createWebHistory
} from 'vue-router';
import ChatBot from '../components/ChatBot.vue'
import Registration from '../components/Registration.vue'
import HomePage from '../components/HomePage.vue'

import SearchForPatientPage from '../components/SearchForPatientPage.vue'
import ShowInquiry from '../components/ShowInquiry.vue'
import ProfilPatient from '../components/ProfilPatient.vue'
import MessagesToDoctor from '../components/MessagesToDoctor.vue'
import ProfilDoctor from '../components/ProfilDoctor.vue'
import ForgotPassword from '../components/ForgotPassword.vue'
import MessagesToPatient from '../components/MessagesToPatient.vue'
import ShowMessage from '../components/ShowMessage.vue'


const routes = [{
    path: '/',
    component: HomePage,
  },
  {
    path: '/registration',
    component: Registration,

  }, 
  {
    path: '/profil-doctor',
    component: ProfilDoctor,
  },

  {
    path: '/forgot-password',
    component: ForgotPassword,
  },
  {
    path: '/newpassword',
    component: ForgotPassword,
  },
  {
    path: '/messages-to-doctor',
    component: MessagesToDoctor,
  },
  {
    path: '/chat-bot',
    component: ChatBot,
  },
  {
    path: '/messages-to-patient',
    component: MessagesToPatient,
  },
  {
    path: '/profil-patient/:id',
    component: ProfilPatient,
  },
  {
    path: '/inquiry/:id',
    component: ShowInquiry,
  },
  {
    path: '/messages/:id',
    component: ShowMessage,
  },
  {
    path: '/search-for-patient',
    component: SearchForPatientPage,
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;