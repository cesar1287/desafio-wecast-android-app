package cesar1287.com.github.desafiowecast.view

import android.Manifest
import android.app.DownloadManager
import android.app.ProgressDialog
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import cesar1287.com.github.desafiowecast.extensions.getTimeString
import cesar1287.com.github.desafiowecast.utils.GlideApp

import java.io.IOException

import cesar1287.com.github.desafiowecast.utils.KEY_EXTRA_AUDIO_URL
import cesar1287.com.github.desafiowecast.utils.KEY_EXTRA_TITLE_URL
import kotlinx.android.synthetic.main.activity_media_player.*
import android.content.Context
import android.content.pm.PackageManager
import android.os.Environment
import androidx.core.app.ActivityCompat
import cesar1287.com.github.desafiowecast.R
import cesar1287.com.github.desafiowecast.utils.MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE
import java.util.*


class URLMediaPlayerActivity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null
    private val mHandler = Handler()
    private var audioTitle: String? = null
    private var audioFile: String? = null

    private val mRunnable = object : Runnable {
        override fun run() {
            mediaPlayer?.let { mediaPlayer ->
                //set max value
                val mDuration = mediaPlayer.duration
                seekBar.max = mDuration

                //set progress to current position
                val mCurrentPosition = mediaPlayer.currentPosition
                seekBar.progress = mCurrentPosition

                //update current time text view
                currentTime.text = mCurrentPosition.toLong().getTimeString()

                //update total time text view
                totalTime.text = (mDuration - mCurrentPosition).toLong().getTimeString()
            }

            //repeat above code every second
            mHandler.postDelayed(this, 10)
        }
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // inflate layout
        setContentView(R.layout.activity_media_player)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // get data from main activity intent
        audioFile = intent.getStringExtra(KEY_EXTRA_AUDIO_URL)

        audioTitle = intent.getStringExtra(KEY_EXTRA_TITLE_URL)
        now_playing_text.text = audioTitle

        // create a media player
        mediaPlayer = MediaPlayer()

        // try to load data and play
        try {
            mediaPlayer?.let { mediaPlayer ->
                // give data to mediaPlayer
                mediaPlayer.setDataSource(audioFile)
                // media player asynchronous preparation
                mediaPlayer.prepareAsync()

                // create a progress dialog (waiting media player preparation)
                val dialog = ProgressDialog.show(this@URLMediaPlayerActivity, "",
                    getString(R.string.loading), true, false)

                /// Load cover image (we use Glide Library)
                // Get image view
                GlideApp.with(applicationContext).load(ContextCompat.getDrawable(this, R.drawable.mrg_itunes))
                    .into(coverImage)
                ///

                // execute this code at the end of asynchronous media player preparation
                mediaPlayer.setOnPreparedListener { mp ->
                    //start media player
                    mp.start()

                    //update seekbar
                    mRunnable.run()

                    //dismiss dialog
                    dialog.dismiss()
                }
            } ?: run {
                finish()
                Toast.makeText(this, getString(R.string.error_load_media_player), Toast.LENGTH_SHORT).show()
            }
        } catch (e: IOException) {
            finish()
            Toast.makeText(this, getString(R.string.file_not_found), Toast.LENGTH_SHORT).show()
        }

        play.setOnClickListener {
            play()
        }

        pause.setOnClickListener {
            pause()
        }

        forward.setOnClickListener {
            seekForward()
        }

        backward.setOnClickListener {
            seekBackward()
        }

        //handle drag on seekbar
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    mediaPlayer?.let { mediaPlayer -> {
                        if (fromUser) {
                            mediaPlayer.seekTo(progress)
                        }
                    }
                }
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                stop()
                finish()
            }
            R.id.action_download -> {
                if (checkPermission()) {
                    downloadEpisode()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkPermission(): Boolean {
        return if (ContextCompat.checkSelfPermission(this,  Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE)
                false
            } else {
                true
            }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    downloadEpisode()
                }
            }
        }
    }

    private fun downloadEpisode() {
        val requestDownloadEpisode = DownloadManager.Request(Uri.parse(audioFile))
        //appears the same in Notification bar while downloading
        requestDownloadEpisode.setDescription(getString(R.string.description_download))
        requestDownloadEpisode.setTitle(audioTitle)
        requestDownloadEpisode.setVisibleInDownloadsUi(false)
        requestDownloadEpisode.allowScanningByMediaScanner()
        requestDownloadEpisode.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

        requestDownloadEpisode.setDestinationInExternalFilesDir(applicationContext, Environment
            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath , audioTitle)

        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        Objects.requireNonNull(downloadManager).enqueue(requestDownloadEpisode)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.player, menu)
        return true
    }

    private fun play() {
        mediaPlayer?.start()
    }

    private fun pause() {
        mediaPlayer?.pause()
    }

    private fun stop() {
        mediaPlayer?.let { mediaPlayer ->
            mediaPlayer.reset()
            mediaPlayer.release()
        }

        mediaPlayer = null
    }

    private fun seekForward() {
        mediaPlayer?.let { mediaPlayer ->
            //set seek time
            val seekForwardTime = 5000

            // get current song position
            val currentPosition = mediaPlayer.currentPosition
            // check if seekForward time is lesser than song duration
            if (currentPosition + seekForwardTime <= mediaPlayer.duration) {
                // forward song
                mediaPlayer.seekTo(currentPosition + seekForwardTime)
            } else {
                // forward to end position
                mediaPlayer.seekTo(mediaPlayer.duration)
            }
        }
    }

    private fun seekBackward() {
        mediaPlayer?.let { mediaPlayer ->
            //set seek time
            val seekBackwardTime = 5000

            // get current song position
            val currentPosition = mediaPlayer.currentPosition
            // check if seekBackward time is greater than 0 sec
            if (currentPosition - seekBackwardTime >= 0) {
                // forward song
                mediaPlayer.seekTo(currentPosition - seekBackwardTime)
            } else {
                // backward to starting position
                mediaPlayer.seekTo(0)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        stop()
        finish()
    }
}
