package com.example.coen268project.Model;
import com.example.coen268project.Firebase.CallBack;
import com.example.coen268project.Firebase.FirebaseChildCallback;
import com.example.coen268project.Firebase.FirebaseRequestModel;

public interface MessagesRepository {
    void createMessage(String sellerId, String buyerId, String uid, String name, String messageContent, CallBack callBack);
    void getMessage(String sellerId, String buyerId, String messageId, CallBack callBack);
    void getAllMessages(String sellerId, String buyerId, CallBack callBack);
    void getAllBuyers(String sellerId, CallBack callBack);
    FirebaseRequestModel getAllMessageByChildEvent(String sellerId, String buyerId, FirebaseChildCallback firebaseChildCallBack);
}
