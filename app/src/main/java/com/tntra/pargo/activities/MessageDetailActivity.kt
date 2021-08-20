package com.tntra.pargo.activities

import android.animation.LayoutTransition
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.tntra.pargo.R
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter

class MessageDetailActivity : AppCompatActivity(), View.OnClickListener {

    private val MAX_LINES_COLLAPSED = 2
    private val INITIAL_IS_COLLAPSED = true

    private val IDLE_ANIMATION_STATE = 1
    private val EXPANDING_ANIMATION_STATE = 2
    private val COLLAPSING_ANIMATION_STATE = 3
    private var mCurrentAnimationState = IDLE_ANIMATION_STATE
    private var isCollapsed = INITIAL_IS_COLLAPSED

    var name: String = ""
    private var llparent: LinearLayout? = null
    private var tvname: TextView? = null
    private var tvdesc: TextView? = null
    private var tvcount: TextView? = null
    private var mSocket: Socket? = null
    private val URL = "http://18.224.66.250:8081/"

    //    token=xDWL5egSBhFHU3Hx6w7HvrB8DfjhEUwGx8XNGbebPz3q3yBjyjcVv42V6rf5HKXkdt47hvkr4pZ5gyPW&userId=1
    val gson: Gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_detail)


        initView()
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
            val json = JsonObject()
            json.addProperty("usename", "user1")
            json.addProperty("room", "room1")
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
        llparent = findViewById(R.id.llparent)
        tvname = findViewById(R.id.tvname)
        tvdesc = findViewById(R.id.tvdesc)
        tvcount = findViewById(R.id.tvcount)
        tvcount?.setOnClickListener(this)

        if (intent != null) {
            if (intent.hasExtra("name")) {
                name = intent?.getStringExtra("name")!!
            }
        }

        tvname?.text = name

        tvdesc?.setOnClickListener {
            if (if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        isActivityTransitionRunning
                    } else {
                        TODO("VERSION.SDK_INT < O")
                    }) {
                llparent?.setLayoutTransition(llparent?.getLayoutTransition())
            }
            if (isCollapsed) {
                mCurrentAnimationState = EXPANDING_ANIMATION_STATE
                tvdesc?.setMaxLines(Int.MAX_VALUE)
            } else {
                mCurrentAnimationState = COLLAPSING_ANIMATION_STATE
                tvdesc?.setMaxLines(MAX_LINES_COLLAPSED)
                tvdesc?.post(Runnable { tvdesc?.setMaxLines(Int.MAX_VALUE) })
            }
            isCollapsed = !isCollapsed
        }
        applyLayoutTransition();
        tvdesc?.text = "We are excited for your collab!\\n\\nHere some inportant information to help you set up your collab\\nLorem ipsum dolor sir miit Lorem ipsum dolor sir miit Lorem ipsum dolor sir miit...Read more"
        updateWithNewText(tvdesc?.text?.toString()!!)
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

    private fun applyLayoutTransition() {
        val transition = LayoutTransition()
        transition.setDuration(300)
        transition.enableTransitionType(LayoutTransition.CHANGING)
        llparent?.setLayoutTransition(transition)
        transition.addTransitionListener(object : LayoutTransition.TransitionListener {
            override fun startTransition(transition: LayoutTransition,
                                         container: ViewGroup, view: View, transitionType: Int) {
                //todo
            }

            override fun endTransition(transition: LayoutTransition,
                                       container: ViewGroup, view: View, transitionType: Int) {
                if (COLLAPSING_ANIMATION_STATE === mCurrentAnimationState) {
                    tvdesc?.setMaxLines(MAX_LINES_COLLAPSED)
                }
                mCurrentAnimationState = IDLE_ANIMATION_STATE
            }
        })
    }

    private fun isIdle(): Boolean {
        return mCurrentAnimationState === IDLE_ANIMATION_STATE
    }

    private fun isRunning(): Boolean {
        return !isIdle()
    }

    private fun isTextUnlimited(): Boolean {
        return tvdesc?.getMaxLines()!! === Int.MAX_VALUE
    }

    private fun canBeCollapsed(): Boolean {
        return tvdesc?.getLineCount()!! <= MAX_LINES_COLLAPSED
    }

    private fun isTrimmedWithLimitLines(): Boolean {
        return tvdesc?.getLineCount()!! <= tvdesc?.getMaxLines()!!
    }

    private fun updateWithNewText(text: String) {
        tvdesc?.setText(text)
        tvdesc?.getViewTreeObserver()?.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (isTextUnlimited()) {
                    if (canBeCollapsed()) {
                        tvdesc?.setClickable(false)
                        tvdesc?.setEllipsize(null)
                    } else {
                        tvdesc?.setClickable(true)
                        tvdesc?.setEllipsize(TextUtils.TruncateAt.END)
                    }
                } else {
                    if (isTrimmedWithLimitLines()) {
                        tvdesc?.setClickable(false)
                        tvdesc?.setEllipsize(null)
                    } else {
                        tvdesc?.setClickable(true)
                        tvdesc?.setEllipsize(TextUtils.TruncateAt.END)
                    }
                }
                tvdesc?.getViewTreeObserver()?.removeOnGlobalLayoutListener(this)
            }
        })
    }
}