package com.example.chatapplication.messaging_with_contact

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatapplication.R
import com.google.firebase.auth.FirebaseAuth
import java.time.LocalDateTime

class MessageAdapter(val context: Context, val messageList:ArrayList<MessageModelForContacts>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class TextMessageWithTimeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val messageTime = itemView.findViewById<TextView>(R.id.messagingTime)
        val leftChatView = itemView.findViewById<LinearLayout>(R.id.left_chat_view)
        val rightChatView = itemView.findViewById<LinearLayout>(R.id.right_chat_view)
        val leftTextView = itemView.findViewById<TextView>(R.id.left_chat_text_view)
        val rightTextView = itemView.findViewById<TextView>(R.id.right_chat_text_view)

        val exactTimeRight = itemView.findViewById<TextView>(R.id.exactTimeRight)
        val exactTimeLeft = itemView.findViewById<TextView>(R.id.exactTimeLeft)
    }

    class TextMessageWithoutTimeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val leftChatView = itemView.findViewById<LinearLayout>(R.id.left_chat_view)
        val rightChatView = itemView.findViewById<LinearLayout>(R.id.right_chat_view)
        val leftTextView = itemView.findViewById<TextView>(R.id.left_chat_text_view)
        val rightTextView = itemView.findViewById<TextView>(R.id.right_chat_text_view)

        val exactTimeRight = itemView.findViewById<TextView>(R.id.exactTimeRight)
        val exactTimeLeft = itemView.findViewById<TextView>(R.id.exactTimeLeft)
    }

    class SentImageMessageWithTimeViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val messageTime = itemView.findViewById<TextView>(R.id.messagingTime)
        val sentImage = itemView.findViewById<ImageView>(R.id.sentImage)
    }
    class SentImageMessageWithoutTimeViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val sentImage = itemView.findViewById<ImageView>(R.id.sentImage)
    }
    class ReceivedImageMessageWithTimeViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val messageTime = itemView.findViewById<TextView>(R.id.messagingTime)
        val receivedImage = itemView.findViewById<ImageView>(R.id.receivedImage)
    }
    class ReceivedImageMessageWithoutTimeViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val receivedImage = itemView.findViewById<ImageView>(R.id.receivedImage)
    }

    val TEXT_MESSAGE_WITHOUT_TIME = 0
    val TEXT_MESSAGE_WITH_TIME = 1

    val SENT_IMAGE_MESSAGE_WITHOUT_TIME = 2
    val SENT_IMAGE_MESSAGE_WITH_TIME = 3

    val RECEIVED_IMAGE_MESSAGE_WITHOUT_TIME = 4
    val RECEIVED_IMAGE_MESSAGE_WITH_TIME = 5


    @RequiresApi(Build.VERSION_CODES.O)
    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]


        val currentDateTime = LocalDateTime.now()

        var hour:Int? = currentDateTime.hour
        var minute:Int?  = currentDateTime.minute
        var month:Int?  = currentDateTime.monthValue
        var day:Int?  = currentDateTime.dayOfMonth
        var year:Int?  = currentDateTime.year



        var isThereTime:Boolean = false
        if (position == 0){
            isThereTime = true
        }else{
            var lastMessage = messageList[position-1]
            if (lastMessage.messageTime?.year != currentMessage.messageTime?.year || lastMessage.messageTime?.month != currentMessage.messageTime?.month || lastMessage.messageTime?.day != currentMessage.messageTime?.day){
                isThereTime = true
            }
        }


        // Time must show only days . Add each of the layouts a time textview
        // Change code below


        if (currentMessage.messageType == Constants.IMAGE_MESSAGE){
            return if (FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderUid)){
                if (isThereTime){
                    SENT_IMAGE_MESSAGE_WITH_TIME
                }else{
                    SENT_IMAGE_MESSAGE_WITHOUT_TIME
                }
            }else{
                if (isThereTime){
                    RECEIVED_IMAGE_MESSAGE_WITH_TIME
                }else{
                    RECEIVED_IMAGE_MESSAGE_WITHOUT_TIME
                }
            }
        }else{
            return if (isThereTime){
                TEXT_MESSAGE_WITH_TIME
            }else{
                TEXT_MESSAGE_WITHOUT_TIME
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {



        when(viewType){
            TEXT_MESSAGE_WITHOUT_TIME->{
                val view: View = LayoutInflater.from(context).inflate(R.layout.message_item_without_time,parent,false)
                return TextMessageWithoutTimeViewHolder(view)
            }
            TEXT_MESSAGE_WITH_TIME->{
                val view: View = LayoutInflater.from(context).inflate(R.layout.message_item_with_time,parent,false)
                return TextMessageWithTimeViewHolder(view)
            }
            SENT_IMAGE_MESSAGE_WITHOUT_TIME->{
                val view: View = LayoutInflater.from(context).inflate(R.layout.sent_photo_recyclerview_item_without_time,parent,false)
                return SentImageMessageWithoutTimeViewHolder(view)
            }
            SENT_IMAGE_MESSAGE_WITH_TIME->{
                val view: View = LayoutInflater.from(context).inflate(R.layout.sent_photo_recyclerview_item_with_time,parent,false)
                return SentImageMessageWithTimeViewHolder(view)
            }
            RECEIVED_IMAGE_MESSAGE_WITHOUT_TIME->{
                val view: View = LayoutInflater.from(context).inflate(R.layout.received_photo_recyclerview_item_without_time,parent,false)
                return ReceivedImageMessageWithoutTimeViewHolder(view)
            }
            else->{
                val view: View = LayoutInflater.from(context).inflate(R.layout.received_photo_recyclerview_item_with_time,parent,false)
                return ReceivedImageMessageWithTimeViewHolder(view)
            }
        }


    }


    override fun getItemCount(): Int {
        return messageList.size
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = messageList[position]

        val auth = FirebaseAuth.getInstance()

        val dateManager = DateManager()

        when(holder){
            is TextMessageWithTimeViewHolder->{


                holder.messageTime.text = currentMessage.messageTime?.day.toString()+","+dateManager.getMonthByNumber(currentMessage.messageTime?.month!!)



                if (currentMessage.senderUid == auth.currentUser?.uid!!){
                    holder.leftChatView.visibility = View.GONE
                    holder.rightChatView.visibility = View.VISIBLE
                    holder.rightTextView.text = currentMessage.messageOrUrl
                    holder.exactTimeRight.text = "${currentMessage.messageTime?.hour}:${currentMessage.messageTime?.minute}"
                }else{
                    holder.leftChatView.visibility = View.VISIBLE
                    holder.rightChatView.visibility = View.GONE
                    holder.leftTextView.text = currentMessage.messageOrUrl
                    holder.exactTimeLeft.text = "${currentMessage.messageTime?.hour}:${currentMessage.messageTime?.minute}"
                }
            }
            is TextMessageWithoutTimeViewHolder->{
                if (currentMessage.senderUid == auth.currentUser?.uid!!){
                    holder.leftChatView.visibility = View.GONE
                    holder.rightChatView.visibility = View.VISIBLE
                    holder.rightTextView.text = currentMessage.messageOrUrl
                    holder.exactTimeRight.text = "${currentMessage.messageTime?.hour}:${currentMessage.messageTime?.minute}"
                }else{
                    holder.leftChatView.visibility = View.VISIBLE
                    holder.rightChatView.visibility = View.GONE
                    holder.leftTextView.text = currentMessage.messageOrUrl
                    holder.exactTimeLeft.text = "${currentMessage.messageTime?.hour}:${currentMessage.messageTime?.minute}"
                }

            }
            is SentImageMessageWithTimeViewHolder->{
                holder.messageTime.text = currentMessage.messageTime?.day.toString()+","+dateManager.getMonthByNumber(currentMessage.messageTime?.month!!)
                Glide.with(context)
                    .load(currentMessage.messageOrUrl)
                    .into(holder.sentImage)
            }
            is SentImageMessageWithoutTimeViewHolder->{
                Glide.with(context)
                    .load(currentMessage.messageOrUrl)
                    .into(holder.sentImage)
            }
            is ReceivedImageMessageWithTimeViewHolder->{
                holder.messageTime.text = currentMessage.messageTime?.day.toString()+","+dateManager.getMonthByNumber(currentMessage.messageTime?.month!!)
                Glide.with(context)
                    .load(currentMessage.messageOrUrl)
                    .into(holder.receivedImage)
            }
            is ReceivedImageMessageWithoutTimeViewHolder->{
                Glide.with(context)
                    .load(currentMessage.messageOrUrl)
                    .into(holder.receivedImage)
            }
        }
    }
}