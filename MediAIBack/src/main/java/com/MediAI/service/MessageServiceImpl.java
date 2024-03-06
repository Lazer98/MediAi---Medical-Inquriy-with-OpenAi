package com.MediAI.service;

// Java Program to Illustrate UserServiceImpl.java
// File

// Importing required packages





import com.MediAI.entity.*;

import com.MediAI.repository.messageRepository.MessageRepository;
import com.MediAI.repository.doctorRepository.DoctorRepository;
import com.MediAI.repository.patientRepository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    @Qualifier("MessageRepository")
    private MessageRepository messageRepository;

    @Autowired
    @Qualifier("DoctorRepository")
    private DoctorRepository doctorRepository;

    @Autowired
    @Qualifier("PatientRepository")
    private PatientRepository patientRepository;

    // Read operation
    @Override
    public List<Message> getAllMessages() {
        return messageRepository.findAll();

    }

    @Override
    public List<Message> getAllMessagesOfUser(Long id) {
        //loop that runs over all inquiries and filters according to fitting user_id
        List<Message> messageTempList= messageRepository.findAll();
        List<Message> messageReturn= new ArrayList<Message>();
        for(int i=0;i< messageTempList.toArray().length;i++){
            System.out.println(messageTempList.get(i));
            if(messageTempList.get(i).getSender_id().getId()==id || messageTempList.get(i).getRecipient_id().getId()==id ){
                messageReturn.add(messageTempList.get(i));
            }
        }
        return messageReturn;

    }

    @Override
    public Message getMessageById(Long id){
        Optional<Message> optionalMessage= messageRepository.findById(id);
        return optionalMessage.orElse(null);    }


    //Post operation

    @Override
    public boolean createMessage(String content, long sender_id, long recipient_id ,String sender_name, LocalDate dateSent){
        Message message=new Message();

        // Retrieve the sender by the id of the sender_id
        Doctor doctor1 = doctorRepository.findById(sender_id).orElse(null);
        Patient patient1 = patientRepository.findById(sender_id).orElse(null);
        if (doctor1 == null && patient1 == null) {
            return false;
        }
        User1 user1= doctor1!=null?doctor1:patient1;
        message.setSender_id(user1);
        // Retrieve the recipient by the id of the recipient_id
        Doctor doctor2 = doctorRepository.findById(recipient_id).orElse(null);
        Patient patient2 = patientRepository.findById(recipient_id).orElse(null);



        if (doctor2 == null && patient2 == null) {
            message.setRecipient_id(null);
        }
        else {
            User1 user2 = doctor2 != null ? doctor2 : patient2;
            message.setRecipient_id(user2);
        }

        message.setContent(content);
        message.setSender_name(sender_name);
        message.setDateSent(dateSent);


        // Save the task to the database
        messageRepository.save(message);

//        // Update the user's message list
//        user.getTasks().add(task);

        return true;

    }

    // Update operation
    @Override
    public boolean updateMessage(String content, long sender_id, long recipient_id ,String sender_name, LocalDate dateSent ,Long messageId) {
        Message messageDB = messageRepository.findById(messageId).orElse(null);

        User1 doctor = doctorRepository.findById(recipient_id).orElse(null);

        if (messageDB == null) {
            throw new IllegalArgumentException("Message not found");
        }
        // Retrieve the sender by the id of the sender_id
        Doctor doctor1 = doctorRepository.findById(sender_id).orElse(null);
        Patient patient1 = patientRepository.findById(sender_id).orElse(null);
        if (doctor1 == null && patient1 == null) {
            return false;
        }
        User1 user1= doctor1!=null ? doctor1 : patient1;
        messageDB.setSender_id(user1);





        if (Objects.nonNull(content) && !"".equalsIgnoreCase(content)) {
            messageDB.setContent(content);
        }
        if (Objects.nonNull(doctor)) {
            messageDB.setRecipient_id(doctor);
        }

        messageRepository.save(messageDB);
        return true;

    }

    // Delete operation
    @Override
    public void deleteMessageById(Long messageId)
    {
        Message depDB =messageRepository.findById(messageId).orElse(null);

        if (depDB == null) {
            // Task not found, handle the error accordingly
            // You can throw an exception, return null, or customize the error message
            throw new IllegalArgumentException("Message not found");
        }



        messageRepository.deleteById(messageId);
    }
}
