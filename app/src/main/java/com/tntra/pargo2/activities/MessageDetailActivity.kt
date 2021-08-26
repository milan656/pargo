package com.tntra.pargo2.activities

import android.animation.LayoutTransition
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.tntra.pargo2.R
import com.tntra.pargo2.adapter.ChatRoomAdapter
import com.tntra.pargo2.common.Common
import com.tntra.pargo2.common.PrefManager
import com.tntra.pargo2.custom.MessageType
import com.tntra.pargo2.model.chat_users.ChatUserListModel
import com.tntra.pargo2.model.chat_users.SendChatData
import com.tntra.pargo2.model.chatmessage.Message
import com.tntra.pargo2.viewmodel.collab.CollabSessionviewModel
import com.vanniktech.emoji.EmojiPopup
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class MessageDetailActivity : AppCompatActivity(), View.OnClickListener {

    private val MAX_LINES_COLLAPSED = 2
    private val INITIAL_IS_COLLAPSED = true
    var userlist: ArrayList<ChatUserListModel>? = ArrayList()
    private val IDLE_ANIMATION_STATE = 1
    private val EXPANDING_ANIMATION_STATE = 2
    private val COLLAPSING_ANIMATION_STATE = 3
    private var mCurrentAnimationState = IDLE_ANIMATION_STATE
    private var isCollapsed = INITIAL_IS_COLLAPSED

    val chatList: ArrayList<Message> = arrayListOf();
    private var chatRoomAdapter: ChatRoomAdapter? = null

    var name: String = ""
    var id: String = ""
    var invitation: String = ""
    private var llparent: LinearLayout? = null
    private var prefManager: PrefManager? = null
    private var tvname: TextView? = null
    private var tvdesc: TextView? = null
    private var tvcount: TextView? = null
    private var tvInvitation: TextView? = null
    private var mSocket: Socket? = null
    private val URL = "http://18.224.66.250:8081/"

    private var chatRecyclerView: RecyclerView? = null
    private var etMessage: EditText? = null
    private var btnSend: ImageView? = null
    private var ivEmoji: ImageView? = null
    private var ivBack: ImageView? = null
    private var rootView: RelativeLayout? = null
    var emojiPopup: EmojiPopup? = null

    private var roomId: String = ""
    private var collabSessionviewModel: CollabSessionviewModel? = null
    private var message_swipe_refresh: SwipeRefreshLayout? = null

    //    token=xDWL5egSBhFHU3Hx6w7HvrB8DfjhEUwGx8XNGbebPz3q3yBjyjcVv42V6rf5HKXkdt47hvkr4pZ5gyPW&userId=1
    val gson: Gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_detail)

        collabSessionviewModel = ViewModelProviders.of(this).get(CollabSessionviewModel::class.java)
        prefManager = PrefManager(this)

        if (intent != null) {
            if (intent.hasExtra("name")) {
                name = intent?.getStringExtra("name")!!
            }
            if (intent.hasExtra("invitation")) {
                if (intent?.getStringExtra("invitation") != null) {
                    invitation = intent?.getStringExtra("invitation")!!
                }
            }
            if (intent.hasExtra("id")) {
                if (intent.getStringExtra("id") != null) {
                    id = intent.getStringExtra("id")!!
                }
            }
        }

        initView()

        try {
            val option = IO.Options()
            val map = HashMap<String, String>()

//            if (prefManager?.getValue("sessionID") != null && !prefManager?.getValue("sessionID").equals("")) {
//                map.put("sessionID", "" + prefManager?.getValue("sessionID")!!)
//            }
            map.put("sessionID", id)
            map.put("username", name)
            map.put("userID", prefManager?.getuserName()!!)
            option.auth = map

            Log.e("TAG", "onCreate: " + map)
            option.path = "/socket.io"
            mSocket = IO.socket("http://c785-144-48-250-250.ngrok.io", option)
            mSocket?.connect()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        mSocket?.on(Socket.EVENT_CONNECT, onConnect)
        mSocket?.on("user connected", connections)
        Log.e("TAG", "onCreate: " + listOf(mSocket?.io()).size)
        mSocket?.on("active_users", connectedUsers)
        mSocket?.on("online", connectedUsers)
//        mSocket?.io()?.on("connection", connectedUsers)
//        mSocket?.on("connection", connections)
        mSocket?.on(Socket.EVENT_CONNECT_ERROR, onConnectError)
        mSocket?.on("private message", onNewMessage);
        mSocket?.on("user disconnected", onDisconnect)
        mSocket?.on("user joined", onUserJoined);
        mSocket?.on("sessions", onUserJoined);
        mSocket?.on("user left", onUserLeft);
        mSocket?.on("typing", onTyping);
        mSocket?.on("stop typing", onStopTyping);

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


    }

    private fun getAllMessage(isRefresh: Boolean) {
        Common.showLoader(this)
        collabSessionviewModel?.callApigetAllMessage(prefManager?.getAccessToken()!!, id.toInt())
        collabSessionviewModel?.getCollabAllMessage()?.observe(this, androidx.lifecycle.Observer {
            Common.hideLoader()
            if (isRefresh) {
                message_swipe_refresh?.post { message_swipe_refresh?.isRefreshing = false }
            }
            if (it != null) {
                if (it.success) {

                    chatList.clear()

                    for (i in it.messages.indices) {
                        var message: Message? = null
                        if (prefManager?.getUserId() == it.messages.get(i).attributes.user_id) {
                            message = Message(
                                    it.messages.get(i).attributes.user_name,
                                    it.messages.get(i).attributes.body, it.messages.get(i).attributes.collab_room_id.toString(),
                                    MessageType.CHAT_MINE.index,
                                    Common.url + it.messages.get(i).attributes.user_profile_img_path)
                        } else {
                            message = Message(
                                    it.messages.get(i).attributes.user_name,
                                    it.messages.get(i).attributes.body, it.messages.get(i).attributes.collab_room_id.toString(),
                                    MessageType.CHAT_PARTNER.index,
                                    Common.url + it.messages.get(i).attributes.user_profile_img_path)
                        }
                        chatList.add(message)
                    }

                    chatRoomAdapter?.notifyDataSetChanged()
                }
            }
        })
    }

    private fun callApiSendMessage() {
//        api/v1/collab_rooms/5/chat_messages
        val json = JsonObject()
        json.addProperty("user_id", prefManager?.getUserId()!!)
        json.addProperty("body", etMessage?.text?.toString()!!)
        collabSessionviewModel?.callApiCollabSendMessage(prefManager?.getAccessToken()!!, json, id.toInt())
        collabSessionviewModel?.getCollabSendMessage()?.observe(this, androidx.lifecycle.Observer {
            if (it != null) {
                if (it.success) {

                }
            }
        })
    }

    private val onUserLeft = Emitter.Listener { args ->
        this.runOnUiThread(Runnable {
            val data = args[0] as JSONObject
            Log.e("TAGleft", ": " + data)
            val username: String
            val numUsers: Int
            try {
                username = data.getString("username")
                numUsers = data.getInt("numUsers")
            } catch (e: JSONException) {
                Log.e("TAG", e.message)
                return@Runnable
            }
//            addLog(resources.getString(R.string.message_user_left, username))
//            addParticipantsLog(numUsers)
//            removeTyping(username)
        })
    }

    private val onTyping = Emitter.Listener { args ->
        this.runOnUiThread(Runnable {
            val data = args[0] as JSONObject
            val username: String
            Log.e("TAGTyping", ": " + data)
            username = try {
                data.getString("username")

            } catch (e: JSONException) {
                Log.e("TAG", e.message)
                return@Runnable
            }
//            addTyping(username)
        })
    }

    private val onStopTyping = Emitter.Listener { args ->
        this.runOnUiThread(Runnable {
            val data = args[0] as JSONObject
            val username: String
            username = try {
                data.getString("username")
            } catch (e: JSONException) {
                Log.e("TAG", e.message)
                return@Runnable
            }
//            removeTyping(username)
        })
    }

    private val onUserJoined = Emitter.Listener { args ->
        this.runOnUiThread(Runnable {
            val data = args[0] as JSONObject
            Log.e("connect::joind", ": " + data)
            val username: String
            val numUsers: Int
            try {
                username = data.getString("username")
                numUsers = data.getInt("numUsers")

            } catch (e: JSONException) {
                Log.e("TAG", e.message)
                return@Runnable
            }
//            addLog(resources.getString(R.string.message_user_joined, username))
//            addParticipantsLog(numUsers)
        })
    }

    private fun initView() {
        message_swipe_refresh = findViewById(R.id.message_swipe_refresh)
        rootView = findViewById(R.id.rootView)
        tvInvitation = findViewById(R.id.tvInvitation)
        ivBack = findViewById(R.id.ivBack)
        ivEmoji = findViewById(R.id.ivEmoji)
        btnSend = findViewById(R.id.btnSend)
        etMessage = findViewById(R.id.etMessage)
        emojiPopup = EmojiPopup.Builder.fromRootView(rootView).build(etMessage!!)
        chatRecyclerView = findViewById(R.id.chatRecyclerView)
        llparent = findViewById(R.id.llparent)
        tvname = findViewById(R.id.tvname)
        tvdesc = findViewById(R.id.tvdesc)
        tvcount = findViewById(R.id.tvcount)

        tvcount?.setOnClickListener(this)
        btnSend?.setOnClickListener(this)
        ivEmoji?.setOnClickListener(this)
        ivBack?.setOnClickListener(this)



        tvname?.text = name
        tvInvitation?.text = "" + invitation + " invitation has sent"

        /*tvdesc?.setOnClickListener {
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
        }*/

        tvdesc?.performClick()
        applyLayoutTransition()
        tvdesc?.text = "We are excited for your collab!\nHere some inportant information to help you set up your collab......"
        updateWithNewText(tvdesc?.text?.toString()!!)

        setChatAdapter()
        getAllMessage(false)

        message_swipe_refresh?.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                this@MessageDetailActivity.onRefresh()
            }
        })
    }

    private fun onRefresh() {
        message_swipe_refresh?.post { message_swipe_refresh?.isRefreshing = true }
        getAllMessage(true)
    }

    private fun setChatAdapter() {
        chatRoomAdapter = ChatRoomAdapter(this, chatList)
        chatRecyclerView?.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        chatRecyclerView?.adapter = chatRoomAdapter
    }

    override fun onClick(v: View?) {
        val id = v?.id
        when (id) {
            R.id.tvcount -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            R.id.btnSend -> {

                if (etMessage?.text?.toString()?.isEmpty()!!) {
                    Toast.makeText(this, "Please enter message", Toast.LENGTH_SHORT).show()
                    return
                }
                try {
                    val model = SendChatData(etMessage?.text?.toString()!!)
                    val obj = JSONObject(gson.toJson(model))
                    val arr = JSONArray()
                    arr.put(obj)
                    Log.e("connect", "onClick: " + obj)
                    mSocket?.emit("private message", obj)

                    callApiSendMessage()
                    val message = Message(prefManager?.getuserName()!!, etMessage?.text?.toString()!!, "roomName", MessageType.CHAT_MINE.index, "")
                    addItemToRecyclerView(message)
                    Log.e("connectsocket", "send message" + " " + roomId)
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                    Log.e("connectsocket", ": " + e.message + " " + e.cause)
                }
                Common.hideKeyboard(this)
            }
            R.id.ivEmoji -> {
                try {
                    emojiPopup?.toggle()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            R.id.ivBack -> {
                onBackPressed()
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

    private fun addItemToRecyclerView(message: Message) {

        //Since this function is inside of the listener,
        // You need to do it on UIThread!
        runOnUiThread {
            chatList.add(message)
            chatRoomAdapter?.notifyItemInserted(chatList.size)
            chatRoomAdapter?.notifyDataSetChanged()
            etMessage?.setText("")
            chatRecyclerView?.scrollToPosition(chatList.size - 1) //move focus on last message

        }
    }

    var onConnect = Emitter.Listener {
        Log.d("TAG", "Socket Connected!")
        Log.e(
                "connectionsocket00",
                ": connected::" + mSocket?.connected() + "| Socket Id::" + mSocket?.id() + " "
        )

        mSocket?.on("onlineUsers") {

            for (i in it?.indices!!) {
                Log.e("connectionAll", ": " + it.get(i)?.toString())
            }
        }

        if (prefManager?.getValue("sessionID") != null &&
                !prefManager?.getValue("sessionID").equals("")) {

        } else {
            prefManager?.setValue("sessionID", mSocket?.id())
        }
    }

    private val connections = Emitter.Listener {
        /*runOnUiThread {
            var count = 0

            for (i in it.indices) {
                Log.e("connectionsocket", ": " + it?.get(i)?.toString())

                val json = JSONObject(it?.get(i)?.toString())
//                val userID = json.getString("userID")
//                roomId = userID
                count = count + 1
                name = json.getString("username")
                userlist?.add(ChatUserListModel(json.getBoolean("connected"), "", json.getString("username")))
//                userlist?.add(json.getString("username"))
            }

            val nameList: ArrayList<String>? = ArrayList<String>()

            for (i in userlist?.indices!!) {
                nameList?.add(userlist?.get(i)?.username!!)
            }
            tvInvitation?.text = "$nameList joined"

            Log.e("connect", ": " + roomId)
        }*/
    }

    private val connectedUsers = Emitter.Listener {
        runOnUiThread {
            var count = 0

            for (i in it.indices) {
                Log.e("connectedsocket", ": " + it?.get(i)?.toString())


            }


        }
    }

    private val onConnectError = Emitter.Listener {

        runOnUiThread {
            for (i in it.indices) {
                Log.e("connectionsocket11", ": " + it?.get(i)?.toString())
            }
        }
    }


    private val onDisconnect = Emitter.Listener {
        runOnUiThread {

            Log.e("connectionsocket00", ": " + it.iterator().toString())
            /*for (i in it.indices) {

                try {
                    for (j in userlist?.indices!!) {
                        Log.e("connectionDisconnect", ": " + userlist?.get(i)?.userID + " == " + it?.get(i)?.toString())
                        if (userlist?.get(j)?.userID == it?.get(i)?.toString()) {
                            userlist?.removeAt(j)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("TAG", ": " + e.message + " " + e.cause)
                }

                tvInvitation?.text = "$userlist Joined"

            }*/
        }
    }

    private val onNewMessage = Emitter.Listener { args ->
        this.runOnUiThread(Runnable {
            val data = args[0] as JSONObject
            Log.e("getmessage", ": " + data.toString())
            try {
                val msg = data.getString("content")
                val username = data.getString("from")
                val message = Message(username, msg, "roomName", MessageType.CHAT_PARTNER.index, "")
                addItemToRecyclerView(message)
            } catch (e: Exception) {
                e.printStackTrace()
            }
//            val username: String
//            val message: String
//            try {
//                username = data.getString("username")
//                message = data.getString("message")
//            } catch (e: JSONException) {
//                return@Runnable
//            }

            // add the message to view
//            addMessage(username, message)
        })
    }
}