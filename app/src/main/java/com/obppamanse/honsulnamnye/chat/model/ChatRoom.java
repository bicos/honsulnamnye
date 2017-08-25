package com.obppamanse.honsulnamnye.chat.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ravy on 2017. 8. 9..
 */

public class ChatRoom {

    public String key;

    public String name;

    public Map<String, String> userList;

    public ChatRoom() {
    }

    public ChatRoom(String key, String name) {
        this.key = key;
        this.name = name;
        this.userList = new HashMap<>();
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("key", key);
        map.put("name", name);
        map.put("userList", userList);
        return map;
    }
}
