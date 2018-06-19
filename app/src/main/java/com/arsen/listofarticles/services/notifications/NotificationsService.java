package com.arsen.listofarticles.services.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.arsen.listofarticles.App;
import com.arsen.listofarticles.R;
import com.arsen.listofarticles.activity.ArticlesListActivity;
import com.arsen.listofarticles.rest.models.FilmsResponse;
import com.arsen.listofarticles.rest.services.ArticlesService;
import com.arsen.listofarticles.util.Constants;
import com.arsen.listofarticles.util.helper.NetworkHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class NotificationsService extends Service {

    private static final Logger LOGGER = Logger.getLogger(NotificationsService.class.getSimpleName());

    private int serviceId = 1;

    private NotificationManagerCompat notificationManager;

    @Inject
    CompositeDisposable compositeDisposable;

    @Inject
    ArticlesService articlesService;

    private Timer timer;

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = NotificationManagerCompat.from(this);

        ((App) getApplication()).getNetComponent().inject(this);

        timer = new Timer();

        if (NetworkHelper.isNetworkAvailable(getBaseContext())) {
            schedule();
        }
    }

    private void schedule() {
        timer.schedule(new NotificationTask(), Constants.NEW_ITEM_CHECK_TIME);
    }

    private void checkForNewArticles() {
        Calendar calendar = Calendar.getInstance();
        String dayBefore = String.format(Locale.ENGLISH, "%d-%02d-%02d", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH) - 1);

        compositeDisposable.add(
                articlesService
                        .getFilms(
                                Constants.Q,
                                Constants.TAGS,
                                dayBefore,
                                Constants.FIELDS,
                                Constants.ORDER_BY,
                                Constants.API_KEY,
                                1)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(filmsResponse -> filmsResponse)
                        .subscribe(
                                filmsResponse -> {
                                    ArrayList<FilmsResponse.Response.Film.Field> fields = filmsResponse.getResponse().getFields();

                                    if (fields != null && !fields.isEmpty())
                                        sendNotification(fields.get(fields.size() - 1).getCategory(), fields.get(fields.size() - 1).getTitle());
                                },
                                Throwable::printStackTrace
                        )
        );

        schedule();
    }

    private void sendNotification(String title, String content) {
        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_article);

        Intent intent = new Intent(this, ArticlesListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "default")
                .setSmallIcon(R.drawable.ic_article)
                .setLargeIcon(icon)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        Notification notification = mBuilder.build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("default", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null)
                notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(serviceId, notification);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class NotificationTask extends TimerTask {
        @Override
        public void run() {
            checkForNewArticles();
        }
    }
}
