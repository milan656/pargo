package com.tntra.pargo.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.tntra.pargo.R
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter

class MessageDetailActivity : AppCompatActivity(), View.OnClickListener {

    var name: String = ""
    private var tvname: TextView? = null
    private var tvcount: TextView? = null
    private var mSocket: Socket? = null
    private val URL = "http://18.224.66.250:8081/"
    val gson: Gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_detail)


        try {
            mSocket = IO.socket(this.URL)
        } catch (URISyntaxException: Exception) {
            Log.d("TAGGsocket", "Failed to connect")
            throw RuntimeException()
        }

        val onConnect = Emitter.Listener {

            Log.e("TAGGsocket", "onCreate: ")
            //After getting a Socket.EVENT_CONNECT which indicate socket has been connected to server,
            //send userName and roomName so that they can join the room.
            val json=JsonObject()
            json.addProperty("usename","user1")
            json.addProperty("room","room1")
            val jsonData = gson.toJson(json) // Gson changes data object to Json type.
            mSocket?.emit("subscribe", jsonData)

            sendMessage()
        }

        val onNewUser = Emitter.Listener {
            val name = it[0] as String //This pass the userName!
//            val chat = Message(name, "", roomName, MessageType.USER_JOIN.index)
//            addItemToRecyclerView(chat)
            Log.d("TAGGsocket", "on New User triggered.")
        }

        val onUserLeft = Emitter.Listener {
            val leftUserName = it[0] as String
//            val chat: Message = Message(leftUserName, "", "", MessageType.USER_LEAVE.index)
//            addItemToRecyclerView(chat)
        }

        val onUpdateChat = Emitter.Listener {
//            val chat: Message = gson.fromJson(it[0].toString(), Message::class.java)
//            chat.viewType = MessageType.CHAT_PARTNER.index
//            addItemToRecyclerView(chat)
        }

        mSocket?.connect()
        //Register all the listener and callbacks here.
        mSocket?.on(Socket.EVENT_CONNECT, onConnect)
        mSocket?.on("newUserToChatRoom", onNewUser) // To know if the new user entered the room.
        mSocket?.on("updateChat", onUpdateChat) // To update if someone send a message to chatroom
        mSocket?.on("userLeftChatRoom", onUserLeft)


        initView()
    }

    private fun initView() {
        tvname = findViewById(R.id.tvname)
        tvcount = findViewById(R.id.tvcount)
        tvcount?.setOnClickListener(this)

        if (intent != null) {
            if (intent.hasExtra("name")) {
                name = intent?.getStringExtra("name")!!
            }
        }

        tvname?.text = name
    }

    private fun sendMessage() {
        val content = "content"
        val json = JsonObject()
        json.addProperty("username", "user")
        val jsonData = gson.toJson(json)
        mSocket?.emit("newMessage", jsonData)

//        val message = Message(userName, content, roomName, MessageType.CHAT_MINE.index)
//        addItemToRecyclerView(message)
    }

    override fun onClick(v: View?) {
        val id = v?.id
        when (id) {
            R.id.tvcount -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
//        val data = initialData(userName, roomName)
//        val jsonData = gson.toJson(data)

        //Before disconnecting, send "unsubscribe" event to server so that
        //server can send "userLeftChatRoom" event to other users in chatroom
//        mSocket?.emit("unsubscribe", jsonData)
        mSocket?.disconnect()
    }
}