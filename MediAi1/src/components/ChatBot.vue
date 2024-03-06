<template>
  <div>
    <div v-if="this.getDatabaseIdOfUser">
      <v-btn color="error" class="btn" @click="openChat">Chat with me!</v-btn>

      <v-dialog v-model="showChat" max-width="500">
        <v-card>
          <v-card-title>
            Chat Bot
          </v-card-title>
          <v-card-text>
            <div class="chat-box-list-container" ref="chatbox">
              <ul class="chat-box-list">
                <li class="message" v-for="(message, idx) in messages" :key="idx" :class="message.author">
                  <p>
                    <span>{{ message.text }}</span>
                  </p>
                </li>
              </ul>
            </div>
            <div class="chat-inputs">
              <v-text-field outlined type="text" v-model="newMessage" @keyup.enter="sendMessage" />
              <v-btn color="error" class="mr-4" @click="sendMessage">Send</v-btn>
            </div>
          </v-card-text>
          <v-card-actions>
            <v-btn @click="closeChat">Close</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>

      <div v-if="showDiagnosis">
        <h2>Here is your diagnosis:</h2>
        <p>{{ this.diagnosis }}</p>


        <v-btn class="mx-2" color="primary" @click="sendEmail">Send me an email</v-btn>
        <v-btn class="mx-2" color="primary" @click="generatePDF">Generate PDF</v-btn>
        <v-btn class="mx-2" color="primary" @click="generateDocument">Generate Document</v-btn>

        <v-row class="justify-center">
          <v-col cols="12" sm="8" md="6">
            <v-sheet width="600" class=" mt-5" color="transparent" style="display:none">
              <form ref="form" @submit.prevent="sendEmail">
                <v-text-field class="white-text" name="user_name" v-model="email_name" label="Your name"
                  required></v-text-field>
                <v-text-field class="white-text" name="user_email" v-model="email_email" label="Your email" type="email"
                  required></v-text-field>
                <v-textarea class="white-text" name="message" v-model="email_message" label="Your message to me!"
                  type="text"></v-textarea>
                <v-btn type="submit" color="primary">Submit</v-btn>
              </form>
            </v-sheet>
          </v-col>
        </v-row>

      </div>

    </div>
    <div v-else>
      <div>You have to login to be here.</div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex';
import { mapMutations } from "vuex";
import repliesJson from '../replies.json';
import axios from 'axios';
import { saveAs } from 'file-saver';
import { PDFDocument, rgb } from 'pdf-lib';
import emailjs from '@emailjs/browser';
import { apiKey, apiUrl } from '../config'; 

export default {
  name: 'ChatBot',
  data() {
    return {
      apiKey: config.apiKey,
      apiUrl: config.apiUrl,
      showChat: false,
      messages: [],
      newMessage: '',
      replies: repliesJson.intents,
      reply: '',
      showWindow: false,
      overlay: false,
      contentSendToAi: 'Hi,I have the following protocoll of a Chat bot trying to find the complaints of a sick person:',
      //Urlevel sagt welches Level wir in der Antwort sind: 0,1,2,3,..
      //Level innerhalb des Urlevels ,sagt und wo wir uns innerhalb des Levels befinden ,
      //z.B. 0.1 , 1.1.3 , 2.1.3 
      urLevel: '-1',
      flagUrLevel: 1,  //um zu konvertieren von string auf nummer
      showDiagnosis: false,
      diagnosis: '',
      email_message: 'diagnosis',
      email_email: '',
      email_name: '',
      valid: false,
    };
  },
  methods: {
    ...mapMutations([
      'setDatabaseIdOfUser',
      'setAge',
      'setName',
      'setEmail',
      'setBirthdate',
      'setDocid',
      'setPassword',
    ]),
    openChat() {
      this.showChat = true;
    },
    closeChat() {
      this.showChat = false;
      this.messages = [];
      this.urLevel = '-1';
      this.welcome();
    },
    generateDocument() {
      // Combine the strings as needed
      const content = this.contentSendToAi;

      // Create a Blob with the content and specify the MIME type (e.g., 'text/plain' for a text file)
      const blob = new Blob([content], { type: 'text/plain;charset=utf-8' });

      // Save the blob as a file using FileSaver.js
      saveAs(blob, 'my_diagnosis.txt');
    },
    sendEmail() {
      emailjs
        .sendForm('service_cjsxt7r', 'template_iefbr4w', this.$refs.form, 'tGaoW0J3dCM6nmJF-')
        .then((result) => {
          console.log('SUCCESS!', result.text);
        })
        .catch((error) => {
          console.log('FAILED...', error);
        });
    },
    async generatePDF() {
      const content = this.contentSendToAi;
      // Create a new PDF document
      const pdfDoc = await PDFDocument.create();
      const font = await pdfDoc.embedFont('Helvetica');
      const fontSize = 12;

      // Calculate text dimensions
      const textWidth = font.widthOfTextAtSize(content, fontSize);
      const textHeight = font.heightAtSize(fontSize);

      // Set preferred page size based on text dimensions and margins
      const contentWidth = textWidth + 100;
      const contentHeight = textHeight * 2 + 100;

      // Add a new page to the document
      let page = pdfDoc.addPage([contentWidth, contentHeight]);
      const { width, height } = page.getSize();

      // Calculate the number of lines required to fit the text
      const lines = this.splitTextIntoLines(content, font, fontSize, width - 50);
      const linesPerPage = Math.floor(height / textHeight);

      // Split the text into multiple pages if necessary
      let startIndex = 0;
      while (startIndex < lines.length) {
        const endIndex = Math.min(startIndex + linesPerPage, lines.length);
        const pageLines = lines.slice(startIndex, endIndex);
        const textToDraw = pageLines.join('\n');
        page.drawText(textToDraw, {
          x: 50,
          y: height - 50 - textHeight * (endIndex - startIndex),
          size: fontSize,
          font: font,
          color: rgb(0, 0, 0),
          maxWidth: width - 100,
        });
        startIndex = endIndex;
        if (startIndex < lines.length) {
          page = pdfDoc.addPage([contentWidth, contentHeight]);
        }
      }

      const pdfBytes = await pdfDoc.save();
      const blob = new Blob([pdfBytes], { type: 'application/pdf' });
      saveAs(blob, 'my_document.pdf');
    },
    splitTextIntoLines(text, font, fontSize, maxWidth) {
      const words = text.split(' ');
      const lines = [];
      let currentLine = words[0];

      for (let i = 1; i < words.length; i++) {
        const word = words[i];
        const width = font.widthOfTextAtSize(currentLine + ' ' + word, fontSize);
        if (width <= maxWidth) {
          currentLine += ' ' + word;
        } else {
          lines.push(currentLine);
          currentLine = word;
        }
      }

      lines.push(currentLine);
      return lines;
    },

    findtheRightAnswer(message, replies) {
      let found = false;
      for (let i = 0; i < replies.length && found == false; i++) {
        console.log(this.urLevel);
        if (replies[i].tag === this.urLevel) {
          for (let j = 0; j < replies[i].patterns.length; j++) {
            if (message.toLowerCase() === replies[i].patterns[j].toLowerCase()) {
              this.reply = replies[i].responses[j];
              found = true;
              const point = '.';
              //if the reply is the last question (any other complaints? 
              // yes --> redirecting to the beginning ,no --> ending the program)
              if (this.reply === 'are there any other complaints?') {
                this.urLevel = '-2';
              }
              else {
                if (this.urLevel == '-1') {
                  this.urLevel = '0' + point.toString() + j.toString();
                }
                else {
                  this.flagUrLevel = parseInt(this.urLevel[0]); // Convert the first character of the string to a number
                  this.flagUrLevel++;
                  this.urLevel = this.flagUrLevel.toString() + this.urLevel.slice(1) + point.toString() + j.toString();
                }
              }
            }
          }
        }
      }
      if (!found) {
        this.reply = "Sorry, I didn't understand...";
      }
    },
    sendMessage() {
      const message = this.newMessage;
      this.messages.push({
        text: message,
        author: 'client',
      });
      //add the message of the client to the contentSendToAi
      this.contentSendToAi = this.contentSendToAi + this.newMessage + '. ';
      this.newMessage = '';

      this.findtheRightAnswer(message, this.replies);


      this.messages.push({
        text: this.reply,
        author: 'server',
      });
      //add the message of the server to the contentSendToAi
      this.contentSendToAi = this.contentSendToAi + this.reply + ': ';

      this.$nextTick(() => {
        this.$refs.chatbox.scrollTop = this.$refs.chatbox.scrollHeight;
      });
      if (this.reply === 'redirecting you to the beginning...') {
        this.urLevel = '-1';
        this.welcome();
      }
      else if (this.reply === 'waiting for the diagnosis...') {
        setTimeout(() => {
          // Call the second method after 3 seconds
          this.closeChat();
          this.apiRequestForDiagnosis();
        }, 3000); // Delay the call by 3 seconds (3000 milliseconds)

      }
    },
    async apiRequestForDiagnosis() {
      const apiKey = apiKey; 
      const apiUrl = apiUrl; 
      var partToDelete = "waiting for the diagnosis...: Welcome, please choose the symptoms fitting your state:orthopedia; neurologia; nose, hals, ears; verdauung; eyes; geschlechtsorgan; other. ";
      var modifiedString = this.contentSendToAi.replace(partToDelete, '');
      try {
        const response = await axios.post(apiUrl, {
          messages: [
            {
              role: 'system',
              content: 'You are a helpful assistant.',
            },
            {
              role: 'user',
              content: modifiedString + ' Can you help me to find the right diagnosis for the patient?',
            },
          ],
          model: 'gpt-3.5-turbo',
        }, {
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${apiKey}`,
          },
        });
        const answer = response.data.choices[0].message.content;
        this.showDiagnosis = true;
        this.diagnosis = answer;

        //for the email
        this.email_email = this.getEmail,
          this.email_name = this.getName,
          this.email_message = this.diagnosis,

          console.log('Answer:', answer);
        this.postInquiry();
      } catch (error) {
        console.error(error);
      }

    },
    postInquiry() {
      var partToDelete = "no. waiting for the diagnosis...: Welcome, please choose the symptoms fitting your state:orthopedia; neurologia; nose, hals, ears; verdauung; eyes; geschlechtsorgan; other. ";
      var modifiedString = this.contentSendToAi.replace(partToDelete, '');
      axios.post(`http://localhost:8082/inquiries/${this.getDatabaseIdOfUser}`, {
        "contentSendToAi": modifiedString,
        "diagnosisFromAi": this.diagnosis,
        "dateSent": new Date(),
        "patient_id": null
      }).then(response => {
        console.log(response);
        this.contentSendToAi = '';
      });
    },
    scrollToBottom() {
      this.$nextTick(() => {
        const chatbox = this.$refs.chatbox;
        chatbox.scrollTop = chatbox.scrollHeight;
      });
    },
    generateBotReply(message) {
      // Replace this with your own logic to generate the bot's reply based on the user's message
      // For demonstration purposes, a simple reply is generated here
      return `Bot's reply to "${message}"`;
    },
    welcome() {
      this.messages.push({
        text: 'Welcome ' + this.getName + ', please choose the symptoms fitting your state:',
        author: 'server',
      });
      this.messages.push({
        text:
          'orthopedia; neurologia; nose, hals, ears; verdauung; eyes; geschlechtsorgan; other',
        author: 'server',
      });
      //add the message of the server to the contentSendToAi
      this.contentSendToAi = this.contentSendToAi + 'Welcome, please choose the symptoms fitting your state:orthopedia; neurologia; nose, hals, ears; verdauung; eyes; geschlechtsorgan; other' + '. ';

    },
  },
  computed: {
    // theme() {
    //   return this.$vuetify.theme.dark ? 'dark' : 'light';
    // },
    ...mapGetters(['getName', 'getAge', 'getEmail', 'getNationality', 'getPassword', 'getDatabaseIdOfUser']),

  },
  mounted() {


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
  beforeMount() {
    this.welcome();
  },
};
</script>

<style scoped>
.btn {
  margin: 10px;
}

.chat-box-list-container {
  overflow: auto;
  height: 300px;
}

.chat-box-list {
  list-style-type: none;
  padding: 0;
}

.message {
  margin: 10px 0;
}

.client {
  text-align: left;
}

.server {
  text-align: right;
}

.chat-inputs {
  margin-top: 10px;
}

input {
  width: 80%;
  padding: 5px;
}

.v-card__actions {
  justify-content: flex-end;
}
</style>
