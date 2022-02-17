package com.example.myapplication

import android.app.*
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomnavigation.BottomNavigationView

class nextscreen : AppCompatActivity(){
//    ----------------------------------------------------------------------------------------
    lateinit var tvLabel:TextView
    lateinit var notificationManager : NotificationManager
    lateinit var notificationChannel : NotificationChannel
    lateinit var builder : Notification.Builder
    lateinit var btnNotify: Button
    lateinit var remoteCollapsedViews: RemoteViews
    lateinit var remoteExpandedViews:RemoteViews
    lateinit var pendingIintent: PendingIntent
    lateinit var soundUri: Uri
    lateinit var audioAttr: AudioAttributes
    lateinit var remoteInput: RemoteInput
    private val channelId = "My Channel Id"
    private val description = "No Notification received till the time"
    private val title = "No-Notification"
    val myKey = "Remote Key"
    val notificationId = 1234
    private var i = 0
//    -----------------------------------------------------------------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nextscreen)

//----------------------------------------------------------------------------------------------
        var start=findViewById<Button>(R.id.button)
        var Rstart=findViewById<Button>(R.id.RStart)
        var cancel=findViewById<Button>(R.id.cancel_button)
        var Etext=findViewById<EditText>(R.id.time)
        var alarmManager: AlarmManager
        val intent= Intent(this,AlarmManagerBroadcast::class.java)
        val pendingIntent= PendingIntent.getBroadcast(this,234,intent,0)
        var playing:Boolean=false
        var toolbar = findViewById<Toolbar>(R.id.toolbar)
        val simpleRatingBar = findViewById<RatingBar>(R.id.simpleRatingBar)
        val submitButton = findViewById<Button>(R.id.submitButton)
        val pgsBar1 = findViewById<ProgressBar>(R.id.pBarCircular)
        val txtView = findViewById<TextView>(R.id.tView)
        val btn = findViewById<Button>(R.id.btnShow)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        tvLabel = findViewById(R.id.tvLabel)
        pgsBar1.visibility = View.INVISIBLE
        btnNotify = findViewById(R.id.btnNotify)
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

//        ------------------------------------------------------------------------------------------
        btn.setOnClickListener {
            pgsBar1.visibility = View.VISIBLE
            Thread{
                while(i<100){
                    i = i+1
                    Handler(Looper.getMainLooper()).post {
                        txtView.text = i.toString() + "/" + 100
                        if (i == 100) {
                            pgsBar1.visibility = View.INVISIBLE
                        }
                    }
                    try {
                        Thread.sleep(100)
                    }
                    catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
            }.start()
        }
//        -----------------------------------------------------------------------------------------
        btnNotify.setOnClickListener {
            val intent = Intent(this,nextscreen::class.java)
            pendingIintent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)

            soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"+ applicationContext.packageName+"/"+R.raw.notification)
            audioAttr = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build();
            myNotificationChannel()


            remoteInput = RemoteInput.Builder(myKey).setLabel("Replying...")
                .build()
            val action: Notification.Action =
                Notification.Action.Builder(R.drawable.check, "Reply", pendingIntent)
                    .addRemoteInput(remoteInput).build()
            builder.addAction(action)

            notificationManager.notify(notificationId,builder.build())
        }

//-----------------------------------------------------------------------------------------------
        submitButton.setOnClickListener {
            val rating = "Rating: " + simpleRatingBar.rating
            val totalStars = " Out of  " + simpleRatingBar.numStars
            Toast.makeText(this, """ $rating$totalStars""".trimIndent(),
                Toast.LENGTH_LONG).show()
        }

//    -------------------------------------------------------------------------------------------
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_home -> {
                    setContent("Home")
                    true
                }
                R.id.menu_notification -> {
                    setContent("Notification")
                    true
                }
                R.id.menu_search -> {
                    setContent("Search")
                    true
                }
                R.id.menu_profile -> {
                    setContent("Profile")
                    true
                }
                else -> false
            }
        }

//        ---------------------------------------------------------------------------------------
        start.setOnClickListener{
            var i=Etext.text.toString().toInt()
            alarmManager=getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+(i*1000),pendingIntent)
            Toast.makeText(this,"Alarm set in $i seconds", Toast.LENGTH_LONG).show()
        }
        Rstart.setOnClickListener{
            alarmManager=getSystemService(ALARM_SERVICE) as AlarmManager
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),5000,pendingIntent)
            Toast.makeText(this,"Repeating Alarm 5 seconds", Toast.LENGTH_LONG).show()
        }
        cancel.setOnClickListener{
            alarmManager=getSystemService(ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(pendingIntent)
            Toast.makeText(this,"Alarm Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

//    ---------------------------------------------------------------------------------------------
//    ---------------------------------------------------------------------------------------------
    private fun setContent(content: String) {
        setTitle(content)
        tvLabel.text = content
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id:Int = item.itemId
        if(id==R.id.action_settings)
        {
            Toast.makeText(applicationContext, "Settings Menu", Toast.LENGTH_LONG).show()
            return true
        }
        else if(id == R.id.action_email)
        {
            Toast.makeText(applicationContext, "Email", Toast.LENGTH_LONG).show()
            return true
        }
        else if (id == R.id.action_add)
        {
            Toast.makeText(applicationContext, "Add", Toast.LENGTH_LONG).show()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun myNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(channelId,description,NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(false)
            notificationChannel.setSound(soundUri,audioAttr)
            notificationManager.createNotificationChannel(notificationChannel)

            builder = Notification.Builder(this,channelId)
                .setSmallIcon(R.drawable.ic_baseline_announcement)
                .setContentTitle(title)
                .setContentText(description)
                .setLargeIcon(BitmapFactory.decodeResource(this.resources,R.drawable.logo_toolbar))
                .setContentIntent(pendingIintent)

                .setAutoCancel(true)

        }

        else{
            builder = Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_baseline_announcement)
                .setContentTitle(title)
                .setContentText(description)
                .setLargeIcon(BitmapFactory.decodeResource(this.resources,R.drawable.logo_toolbar))
                .setContentIntent(pendingIintent)
                .setAutoCancel(true)
        }
    }
//    ----------------------------------------------------------------------------------------------
}