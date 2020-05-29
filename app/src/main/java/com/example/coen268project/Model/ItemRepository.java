package com.example.coen268project.Model;
import com.example.coen268project.Firebase.CallBack;

import java.util.ArrayList;
import java.util.HashMap;

public interface ItemRepository {
    void createItem(String itemName, String category, String location, String price, String description, String pictureName, CallBack callBack);
    void updateItem(String itemId, HashMap map, CallBack callBack);
    void deleteItem(String itemId, CallBack callBack);
    void getItem(String itemId, CallBack callBack);
    void getSpecificItems(String category, String location, CallBack callBack);
    void getAllItems(CallBack callBack);
    void getAllLocations(CallBack callBack);
}
