package com.example.sig;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.ViewPager;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import pl.droidsonroids.gif.GifImageButton;
import pl.droidsonroids.gif.GifImageView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.media.Rating;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sig.Adapter.ImageAdapter;
import com.example.sig.Objects.Note;
import com.example.sig.Objects.Object;
import com.example.sig.Objects.Picture;
import com.example.sig.Objects.Report;
import com.example.sig.Objects.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonElement;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.expressions.Expression;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import static com.mapbox.mapboxsdk.style.expressions.Expression.bool;
import static com.mapbox.mapboxsdk.style.expressions.Expression.get;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static com.mapbox.mapboxsdk.style.expressions.Expression.gte;
import static com.mapbox.mapboxsdk.style.expressions.Expression.has;
import static com.mapbox.mapboxsdk.style.expressions.Expression.literal;
import static com.mapbox.mapboxsdk.style.expressions.Expression.lt;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textField;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textOffset;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textSize;

public class MainActivity extends AppCompatActivity implements
        OnMapReadyCallback, PermissionsListener, MapboxMap.OnMapClickListener {

    //private static final LatLng BOUND_CORNER_NW = new LatLng(47.934377, 1.861456);
    private static final LatLng BOUND_CORNER_NW = new LatLng(47.985880, 1.746736);
    //private static final LatLng BOUND_CORNER_SE = new LatLng(47.811187, 1.971275);
    private static final LatLng BOUND_CORNER_SE = new LatLng(47.788809, 2.100004);
    private static final LatLngBounds RESTRICTED_BOUNDS_AREA = new LatLngBounds.Builder()
            .include(BOUND_CORNER_NW)
            .include(BOUND_CORNER_SE)
            .build();
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");


    private String url = "90.20.196.207";
    private String HOST_MAP = "http://"+url+":8080";
    private String HOST_API = "http://"+url;
    private String HOST_API_PICTURES = "http://"+url+"/";
    private String URL_TEST_API = "/detentApi/index/index";
    private String URL_TEST_BDD = "/detentApi/index/testDB";
    private String URL_TEST_JWT = "/detentApi/index/testJWT";
    private String URL_TO_LOGIN = "/detentApi/login/login";
    private String URL_TO_REFRECHE = "/detentApi/login/login";
    private String URL_TO_LOGOUT = "/detentApi/login/logout";
    private String URL_TO_INSCRIPTION = "/detentApi/user/create";
    private String URL_TO_GET_USER_BY_LOGIN = "/detentApi/user/selectByLogin";
    private String URL_TO_GET_USER_BY_ID = "/detentApi/user/selectByIdUser";
    private String URL_TO_UPDATE_USER = "/detentApi/user/update";
    private String URL_TO_DELETE_USER = "/detentApi/user/delete";

    private String URL_OBJECT_SELECTBYID = "/detentApi/objet/selectById";
    private String URL_OBJECT_CREATE = "/detentApi/objet/create";
    private String URL_OBJECT_UPDATE = "/detentApi/objet/update";
    private String URL_OBJECT_DELETE = "/detentApi/objet/delete";

    private String URL_NOTE_SELECT = "/detentApi/note/select";
    private String URL_NOTE_CREATE = "/detentApi/note/create";

    private String URL_REPORT_CREATE = "/detentApi/report/create";
    private String URL_REPORT_DELETE = "/detentApi/report/delete";
    private String URL_REPORT_SELECTBYID = "/detentApi/report/selectByIdReport";
    private String URL_REPORT_SELECTBYOBJECT = "/detentApi/report/selectByIdObjet";

    private String URL_PICTURE_SELECTBYID = "/detentApi/picture/selectById";
    private String URL_PICTURE_SELECTBYOBJECT = "/detentApi/picture/selectByObjet";
    private String URL_PICTURE_CREATE = "/detentApi/picture/create";

    private PermissionsManager permissionsManager;
    private MapboxMap mapboxMap;
    private WebView webView;
    private MapView mapView;
    private boolean[] listLieubool;
    private String[] listLieu;
    private String[] listLieuPropositions;
    private boolean[] optionsAffichagelist;
    private Context context;
    private int resultat;
    private LatLng nouveauPoint;
    private ArrayList<Bitmap> icons;
    private ArrayList<String> listUrlGeoJson;
    private AlertDialog erreurUpdate;
    private Menu menuPrincipal;
    private boolean connect_to_account;
    private boolean connected_to_internet;
    private boolean connect_to_server;
    private OkHttpClient clientAPI;
    private String messageCreateNote;
    private File selectedFile;
    private ArrayList<Bitmap> listDrawableImage;

    private int resultat_choix_saison;

    private User user;
    private Object selectedObject;
    private Report selectedReport;
    private Note selectedNote;
    private List<Picture> listPictures;
    private List<String> listFilePictures;
    private List<Integer> listIdPictures;
    private View view_picture_storage;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, "pk.eyJ1IjoiYmxhY3BpZWNlIiwiYSI6ImNrMmo4aHc3bjEydmczY3BjcHBscTlmdXAifQ.IHBqHwBHXhaWvwv1fDkbjQ");
        setContentView(R.layout.activity_main);

        context = this;

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replacerCamera();
            }
        });
        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                modifIPServer();
                return true;
            }
        });

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);


        listLieu = new String[]{"arbres","arbres_propositions","arbres_signalement",
                "bancs","bancs_propositions","bancs_signalement",
                "toilettes","toilettes_propositions","toilettes_signalement",
                "poubelles","poubelles_propositions","poubelles_signalement",
                "verres","verres_propositions","verres_signalement"};
        listLieuPropositions = new String[]{"arbres_propositions","bancs_propositions","toilettes_propositions","poubelles_propositions","verres_propositions"};
        listLieubool = new boolean[]{true,false,false,true,false,false,true,false,false,true,false,false,true,false,false};
        optionsAffichagelist = new boolean[]{true,false,false,true};
        connect_to_account = false;
        connected_to_internet =false;

        connect_to_server = false;
        listUrlGeoJson = new ArrayList<>();
        clientAPI = new OkHttpClient();
        user = new User();
        selectedObject = new Object();
        selectedNote = new Note();
        selectedReport = new Report();

        erreurUpdate = new AlertDialog.Builder(this)
                .setView(R.layout.erreur_update)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).create();

        //remplissage addresse to get geojson
        listUrlGeoJson.add(HOST_MAP+"/geoserver/detentTest/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=detentTest%3Atree_view&outputFormat=application%2Fjson");
        listUrlGeoJson.add(HOST_MAP+"/geoserver/detentTest/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=detentTest%3Atree_suggestion_view&outputFormat=application%2Fjson");
        listUrlGeoJson.add(HOST_MAP+"/geoserver/detentTest/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=detentTest%3Atree_reported_view&outputFormat=application%2Fjson");

        listUrlGeoJson.add(HOST_MAP+"/geoserver/detentTest/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=detentTest%3Abanc_view&outputFormat=application%2Fjson");
        listUrlGeoJson.add(HOST_MAP+"/geoserver/detentTest/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=detentTest%3Abanc_suggestion_view&outputFormat=application%2Fjson");
        listUrlGeoJson.add(HOST_MAP+"/geoserver/detentTest/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=detentTest%3Abanc_reported_view&outputFormat=application%2Fjson");

        listUrlGeoJson.add(HOST_MAP+"/geoserver/detentTest/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=detentTest%3Atoilet_view&outputFormat=application%2Fjson");
        listUrlGeoJson.add(HOST_MAP+"/geoserver/detentTest/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=detentTest%3Atoilet_suggestion_view&outputFormat=application%2Fjson");
        listUrlGeoJson.add(HOST_MAP+"/geoserver/detentTest/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=detentTest%3Atoilet_reported_view&outputFormat=application%2Fjson");

        listUrlGeoJson.add(HOST_MAP+"/geoserver/detentTest/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=detentTest%3Atrash_view&outputFormat=application%2Fjson");
        listUrlGeoJson.add(HOST_MAP+"/geoserver/detentTest/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=detentTest%3Atrash_suggestion_view&outputFormat=application%2Fjson");
        listUrlGeoJson.add(HOST_MAP+"/geoserver/detentTest/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=detentTest%3Atrash_reported_view&outputFormat=application%2Fjson");

        listUrlGeoJson.add(HOST_MAP+"/geoserver/detentTest/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=detentTest%3Apav_verre_view&outputFormat=application%2Fjson");
        listUrlGeoJson.add(HOST_MAP+"/geoserver/detentTest/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=detentTest%3Apav_verre_suggestion_view&outputFormat=application%2Fjson");
        listUrlGeoJson.add(HOST_MAP+"/geoserver/detentTest/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=detentTest%3Apav_verre_reported_view&outputFormat=application%2Fjson");

        askPermission();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void askPermission(){
        int res = 0;
        List<String> list = new ArrayList<>();

        if(ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            list.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            list.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            list.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED){
            list.add(Manifest.permission.INTERNET);
        }
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            list.add(Manifest.permission.CAMERA);
        }

        if (list.size() != 0){
            String[] fine = new String[list.size()];
            for(int i = 0 ;i<list.size();i++){
                fine[i] = list.get(i);
            }
            MainActivity.this.requestPermissions(fine,res);
        }
    }

    //replace la caméra sur la localisation de l'utilisateur
    public void replacerCamera(){
        mapboxMap.setCameraPosition(new CameraPosition.Builder()
                .target(new LatLng(mapboxMap.getLocationComponent().getLastKnownLocation().getLatitude(), mapboxMap.getLocationComponent().getLastKnownLocation().getLongitude()))
                .zoom(12)
                .bearing(0)
                .build());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menuPrincipal = menu;
        getMenuInflater().inflate(R.menu.ma_carte, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_couches :
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("Couches");
                alertDialog.setMultiChoiceItems(listLieu, listLieubool, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            listLieubool[which] = true;
                            mapboxMap.getStyle().getLayer(listLieu[which]).setProperties(PropertyFactory.visibility(Property.VISIBLE));
                        }
                        else{
                            listLieubool[which] = false;
                            mapboxMap.getStyle().getLayer(listLieu[which]).setProperties(PropertyFactory.visibility(Property.NONE));
                        }
                    }
                });
                alertDialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.create();
                alertDialog.show();
                break;
            case R.id.action_affichage :
                AlertDialog.Builder optionsAffichage = new AlertDialog.Builder(this);
                optionsAffichage.setTitle("option d'affichage");
                View view = getLayoutInflater().inflate(R.layout.option_affichage,null);
                Switch clustering_points = (Switch) view.findViewById(R.id.clustering_point);
                Switch clustering_labels = (Switch) view.findViewById(R.id.clustering_label);
                final Switch donnee_local = (Switch) view.findViewById(R.id.donneesLocal);

                clustering_points.setChecked(optionsAffichagelist[0]);
                clustering_labels.setChecked(optionsAffichagelist[1]);
                donnee_local.setChecked(optionsAffichagelist[2]);
                if(optionsAffichagelist[2]){
                    donnee_local.setText("en local");
                }

                clustering_points.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        optionsAffichagelist[0] = isChecked;
                        for(Layer l : mapboxMap.getStyle().getLayers()){
                            l.setProperties(PropertyFactory.iconIgnorePlacement(isChecked));
                        }
                    }
                });

                clustering_labels.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        optionsAffichagelist[1] = isChecked;
                        for(Layer l : mapboxMap.getStyle().getLayers()){
                            l.setProperties(PropertyFactory.textIgnorePlacement(isChecked));
                        }
                    }
                });

                donnee_local.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        optionsAffichagelist[2] = isChecked;
                        if(isChecked) {
                            MenuItem i = menuPrincipal.getItem(2);
                            i.setIcon(R.drawable.baseline_cloud_off_white_18dp);
                            donnee_local.setText("en local");
                            addDataLocal(mapboxMap.getStyle());
                        }
                        else{
                            if(connected_to_internet){
                                MenuItem i = menuPrincipal.getItem(2);
                                i.setIcon(R.drawable.baseline_sync_white_18dp);
                                try {
                                    donnee_local.setText("en ligne");
                                    addData(mapboxMap.getStyle());
                                } catch (IOException e) {
                                    donnee_local.setText("en local");
                                    e.printStackTrace();
                                }
                            }
                            else{
                                optionsAffichagelist[2] = true;
                                donnee_local.setChecked(true);
                                Toast.makeText(context,"vous n'êtes pas connecté à internet",Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });

                optionsAffichage.setView(view);
                optionsAffichage.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                optionsAffichage.create().show();

                break;
            case R.id.action_rafraichir:
                if(connected_to_internet){
                    MenuItem i = menuPrincipal.getItem(2);
                    i.setIcon(R.drawable.baseline_sync_white_18dp);
                    optionsAffichagelist[2] = false;
                    onMapReady(mapboxMap);
                }
                else {
                    Toast.makeText(this,"vous n'êtes pas connecté à internet",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.action_connexion :
                final MenuItem m = menuPrincipal.getItem(4);
                if(m.getIcon().getConstantState().equals(getDrawable(R.drawable.baseline_lock_white_18dp).getConstantState()) && connected_to_internet && connect_to_server && !optionsAffichagelist[2]){
                    connexion();
                }
                else{
                    if(connected_to_internet && connect_to_server){
                        if(selectUserById()){
                            profil();
                        }
                        else {
                            Toast.makeText(context,"Vous êtes en mode hors ligne",Toast.LENGTH_LONG).show();
                        }

                    }
                    else{
                        if(!connect_to_server){
                            AlertDialog.Builder message_excuse = new AlertDialog.Builder(this);
                            message_excuse.setTitle("Oups !");
                            message_excuse.setMessage("Le serveur qui s'occupe des services du mode connecté est actuellement indisponnible.");
                            message_excuse.create().show();
                        }
                        else{
                            Toast.makeText(this,"vous n'êtes pas connecté à internet",Toast.LENGTH_LONG).show();
                        }

                    }

                }
                break;
            case R.id.action_status :
                MenuItem m2 = menuPrincipal.getItem(2);
                if(!connected_to_internet && m2.getIcon().getConstantState().equals(getDrawable(R.drawable.baseline_cloud_off_white_18dp).getConstantState())){
                    ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                    if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                        connected_to_internet = true;
                        Toast.makeText(this,"vous êtes de nouveau en ligne",Toast.LENGTH_LONG).show();
                    }
                    else {
                        connected_to_internet = false;
                        Toast.makeText(this,"vous êtes toujours hors ligne",Toast.LENGTH_LONG).show();
                    }
                    optionsAffichagelist[2] = !connected_to_internet;
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;

        //all test for connexion
        testInternet();
        testAPI();

        mapboxMap.setStyle(Style.MAPBOX_STREETS,
                new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        enableLocationComponent(style);
                        mapboxMap.setLatLngBoundsForCameraTarget(RESTRICTED_BOUNDS_AREA);
                        mapboxMap.setMinZoomPreference(2);

                        icons = new ArrayList<>();
                        icons.add(BitmapFactory.decodeResource(getResources(), R.drawable.icon_arbre));
                        icons.add(BitmapFactory.decodeResource(getResources(), R.drawable.icon_arbre_proposition));
                        icons.add(BitmapFactory.decodeResource(getResources(), R.drawable.icon_arbre_signalement));
                        icons.add(BitmapFactory.decodeResource(getResources(), R.drawable.icon_banc));
                        icons.add(BitmapFactory.decodeResource(getResources(), R.drawable.icon_banc_proposition));
                        icons.add(BitmapFactory.decodeResource(getResources(), R.drawable.icon_banc_signalement));
                        icons.add(BitmapFactory.decodeResource(getResources(), R.drawable.icon_toilette));
                        icons.add(BitmapFactory.decodeResource(getResources(), R.drawable.icon_toilette_proposition));
                        icons.add(BitmapFactory.decodeResource(getResources(), R.drawable.icon_toilette_signalement));
                        icons.add(BitmapFactory.decodeResource(getResources(), R.drawable.icon_poubelle));
                        icons.add(BitmapFactory.decodeResource(getResources(), R.drawable.icon_poubelle_proposition));
                        icons.add(BitmapFactory.decodeResource(getResources(), R.drawable.icon_poubelle_signalement));
                        icons.add(BitmapFactory.decodeResource(getResources(), R.drawable.icon_trie_verre));
                        icons.add(BitmapFactory.decodeResource(getResources(), R.drawable.icon_trie_verre_proposition));
                        icons.add(BitmapFactory.decodeResource(getResources(), R.drawable.icon_trie_verre_signalement));

                        for(int i = 0;i<listLieu.length;i++){
                            Bitmap icon = icons.get(i);
                            style.addImage("marker_"+listLieu[i], icon);

                            SymbolLayer symbolLayer = new SymbolLayer(listLieu[i], "source_"+listLieu[i]);
                            symbolLayer.setProperties(PropertyFactory.iconImage("marker_"+listLieu[i]));

                            if(listLieubool[i]){
                                symbolLayer.setProperties(PropertyFactory.visibility(Property.VISIBLE));
                            }
                            else{
                                symbolLayer.setProperties(PropertyFactory.visibility(Property.NONE));
                            }
                            style.addLayer(symbolLayer);
                        }

                        //load data
                        try {
                            addData(style);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

        mapboxMap.addOnMapLongClickListener(new MapboxMap.OnMapLongClickListener() {
            @Override
            public boolean onMapLongClick(@NonNull LatLng point) {
                if(connected_to_internet && connect_to_account){
                    nouveauPoint = point;
                    proposition();
                }
                else {
                    if(!connected_to_internet){
                        Toast.makeText(context,"vous n'êtes pas connecté à internet",Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(context,"vous n'êtes pas connecté à votre compte",Toast.LENGTH_LONG).show();
                    }
                }
                return true;
            }
        });

        mapboxMap.addOnMapClickListener(MainActivity.this);
    }

    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            LocationComponent locationComponent = mapboxMap.getLocationComponent();
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(this, loadedMapStyle).build());
            locationComponent.setLocationComponentEnabled(true);
            locationComponent.setCameraMode(CameraMode.TRACKING);
            locationComponent.setRenderMode(RenderMode.COMPASS);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 0){
            onMapReady(mapboxMap);
        }
        else{
            permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, "coucou", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);
                }
            });
        } else {
            Toast.makeText(this, "coucou2", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    public void addDataLocal(@NonNull final Style loadedMapStyle){
        MenuItem u = menuPrincipal.getItem(2);
        u.setIcon(R.drawable.baseline_cloud_off_white_18dp);
        View view = getLayoutInflater().inflate(R.layout.option_affichage,null);
        Switch donnee_local = (Switch) view.findViewById(R.id.donneesLocal);
        donnee_local.setChecked(true);
        donnee_local.setText("en local");

        //"arbres","arbres_propositions"
        //"bancs","bancs_propositions"
        //"toilettes","toilettes_propositions"
        //"poubelles","poubelles_propositions"
        //"verres","verres_propositions"

        for(int i = 0;i<listUrlGeoJson.size()/3;i++){
            loadedMapStyle.removeLayer(listLieu[(i*3)]);
            loadedMapStyle.removeSource("source_"+listLieu[(i*3)]);

            try {
                mapboxMap.getStyle().addSource(
                        new GeoJsonSource("source_"+listLieu[(i*3)],
                                FeatureCollection.fromJson(convertStreamToString(context.getAssets().open(listLieu[(i*3)]+".geojson"))), new GeoJsonOptions()
                                .withCluster(true)
                                .withClusterMaxZoom(16)
                                .withClusterRadius(50))
                );
            } catch (IOException e) {
                e.printStackTrace();
            }

            SymbolLayer symbolLayer = new SymbolLayer(listLieu[(i*3)], "source_"+listLieu[(i*3)]);
            symbolLayer.setProperties(PropertyFactory.iconImage("marker_"+listLieu[(i*3)]));
            symbolLayer.setProperties(
                    iconIgnorePlacement(optionsAffichagelist[0]),
                    textField(Expression.toString(get("point_count"))),
                    textSize(12f),
                    textColor(Color.BLACK),
                    textIgnorePlacement(true),
                    textOffset(new Float[] {0f, .5f}),
                    textAllowOverlap(true)
            );

            if(listLieubool[i*3]){
                symbolLayer.setProperties(PropertyFactory.visibility(Property.VISIBLE));
            }
            else{
                symbolLayer.setProperties(PropertyFactory.visibility(Property.NONE));
            }
            loadedMapStyle.addLayer(symbolLayer);
        }
    }

    public void addData(@NonNull final Style loadedMapStyle) throws IOException {
        //"arbres","arbres_propositions"
        //"bancs","bancs_propositions"
        //"toilettes","toilettes_propositions"
        //"poubelles","poubelles_propositions"
        //"verres","verres_propositions"

        for(int i = 0;i<listUrlGeoJson.size();i++){
            loadedMapStyle.removeLayer(listLieu[i]);
            loadedMapStyle.removeSource("source_"+listLieu[i]);

            try {
                loadedMapStyle.addSource(
                        new GeoJsonSource("source_"+listLieu[i],
                                new URI(listUrlGeoJson.get(i)),
                                new GeoJsonOptions()
                                        .withCluster(true)
                                        .withClusterMaxZoom(16)
                                        .withClusterRadius(50)
                        )
                );
            } catch (Exception e) {
                e.printStackTrace();
            }



            SymbolLayer symbolLayer = new SymbolLayer(listLieu[i], "source_"+listLieu[i]);
            symbolLayer.setProperties(PropertyFactory.iconImage("marker_"+listLieu[i]));
            symbolLayer.setProperties(
                    iconIgnorePlacement(optionsAffichagelist[0]),
                    textField(Expression.toString(get("point_count"))),
                    textSize(12f),
                    textColor(Color.BLACK),

                    textIgnorePlacement(true),
                    textOffset(new Float[] {0f, .5f}),
                    textAllowOverlap(true)
            );

            if(listLieubool[i]){
                symbolLayer.setProperties(PropertyFactory.visibility(Property.VISIBLE));
            }
            else{
                symbolLayer.setProperties(PropertyFactory.visibility(Property.NONE));
            }
            loadedMapStyle.addLayer(symbolLayer);
        }

    }

    public String convertStreamToString(InputStream is) {
        Scanner scanner = new Scanner(is).useDelimiter("\\A");
        return scanner.hasNext() ? scanner.next() : "";
    }

    public boolean onMapClick(@NonNull LatLng point) {
        Log.i("test","click");
        return handleClickIcon(mapboxMap.getProjection().toScreenLocation(point));
    }

    private boolean handleClickIcon(PointF screenPoint) {
        List<Feature> features = mapboxMap.queryRenderedFeatures(screenPoint);
        if (!features.isEmpty()) {
            //detection d'un point à afficher id_object
            selectedObject = new Object();
            int index = getIndexPoint(features);
            if(index != -1){
                Feature feature = features.get(index);
                if (feature.properties() != null) {
                    for (Map.Entry<String, JsonElement> entry : feature.properties().entrySet()) {
                        if(entry.getKey().contains("id_object")){
                            selectedObject.setId_object(entry.getValue().getAsInt());
                            break;
                        }
                    }
                    for (Map.Entry<String, JsonElement> entry : feature.properties().entrySet()) {
                        if(entry.getKey().contains("type")){
                            selectedObject.setType(entry.getValue().getAsString());
                            if(selectedObject.getType().contains("suggestion") || selectedObject.getType().contains("proposition")){
                                if(connect_to_server && connect_to_account){
                                    Log.i("recup info id object",""+selectedObject.getId_object());
                                    if(selectNote(""+selectedObject.getId_object())){
                                        Log.i("recup info","select note recup");
                                    }
                                    else{
                                        selectedNote.setNote(0.0);
                                        Log.i("recup info","select note pas recup");
                                    }
                                }
                                else{
                                    selectedNote.setNote(0.0);
                                }
                            }
                            if(selectedObject.getType().contains("report")){
                                if(connect_to_server && connect_to_account){
                                    if(selectReportByObject(""+selectedObject.getId_object())){
                                        Log.i("recup info","select report recup");
                                    }
                                    else{
                                        selectedReport.setDescription("Aucune description");
                                        Log.i("recup info","select report pas recup");
                                    }
                                }
                                else{
                                    selectedReport.setDescription("Aucune description");
                                }
                            }
                        }
                        if(entry.getKey().contains("description")){
                            try{
                                selectedObject.setDescription(entry.getValue().getAsString());
                                selectedObject.setDescription(replaceALaLigne(selectedObject.getDescription()));
                            }catch (Exception e){
                                selectedObject.setDescription("Aucune description");
                            }
                        }
                        else{
                            selectedObject.setDescription("Aucune description");
                        }
                    }
                }

                TextView typePoint = null;
                TextView descriptionPoint =  null;
                View view = null;

                if(!connect_to_server || !connected_to_internet || !connect_to_account){
                    //pas de conneixon au serveur ou pas de connexion à internet
                    view = getLayoutInflater().inflate(R.layout.affichage_point_horsligne,null);
                    typePoint = (TextView) view.findViewById(R.id.textView_type_point);
                    descriptionPoint = (TextView) view.findViewById(R.id.textView_description_point);
                    typePoint.setText(selectedObject.getType());
                    descriptionPoint.setText(selectedObject.getDescription());
                }
                else{
                    //si proposition
                    if(selectedObject.getType().contains("suggestion") || selectedObject.getType().contains("proposition")){
                        view = getLayoutInflater().inflate(R.layout.affichage_point_proposition,null);
                        typePoint = (TextView) view.findViewById(R.id.textView_type_point);
                        descriptionPoint = (TextView) view.findViewById(R.id.textView_description_point);
                        RatingBar note_moyenne = (RatingBar) view.findViewById(R.id.note_point);
                        note_moyenne.setRating((float) selectedNote.getNote());
                        typePoint.setText(selectedObject.getType());
                        descriptionPoint.setText(selectedObject.getDescription());
                        affichePointProposition(view);
                    }
                    else{
                        //si report
                        if(selectedObject.getType().contains("report")){
                            view = getLayoutInflater().inflate(R.layout.affichage_point_signalement,null);
                            typePoint = (TextView) view.findViewById(R.id.textView_type_point);
                            descriptionPoint = (TextView) view.findViewById(R.id.textView_description_point);
                            Button bouton_unreport = (Button) view.findViewById(R.id.button_unreport_point);
                            TextView description_report = (TextView) view.findViewById(R.id.textView_description_report);
                            typePoint.setText(selectedObject.getType());
                            descriptionPoint.setText(selectedObject.getDescription());
                            bouton_unreport.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(deleteReport(""+selectedReport.getId_report())){
                                        Toast.makeText(context,"Votre indication a bien été pris en compte",Toast.LENGTH_LONG).show();
                                    }
                                    else{
                                        Toast.makeText(context,"Une erreur est survenue.",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                        else{
                            //si arbre
                            if(selectedObject.getType().contains("tree")){
                                view = getLayoutInflater().inflate(R.layout.affichage_point_arbre,null);
                                typePoint = (TextView) view.findViewById(R.id.textView_type_point);
                                descriptionPoint = (TextView) view.findViewById(R.id.textView_description_point);
                                ImageButton bt_add_picture_storage = (ImageButton) view.findViewById(R.id.bt_ajout_photo_album_storage);
                                ImageButton bt_add_picture_camera = (ImageButton) view.findViewById(R.id.bt_ajout_photo_album_camera);
                                bt_add_picture_storage.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        pickFromGallery();
                                    }
                                });
                                bt_add_picture_camera.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        captureFromCamera();
                                    }
                                });
                                typePoint.setText(selectedObject.getType());
                                descriptionPoint.setText(selectedObject.getDescription());
                                affichePointArbre(view);
                            }
                            else{
                                //tous autre
                                view = getLayoutInflater().inflate(R.layout.affichage_point_base,null);
                                typePoint = (TextView) view.findViewById(R.id.textView_type_point);
                                descriptionPoint = (TextView) view.findViewById(R.id.textView_description_point);
                                Button bouton_report = (Button) view.findViewById(R.id.button_report_point);
                                typePoint.setText(selectedObject.getType());
                                descriptionPoint.setText(selectedObject.getDescription());
                                bouton_report.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        reportPoint();
                                    }
                                });
                            }
                        }
                    }

                }



                AlertDialog.Builder a = new AlertDialog.Builder(this);
                a.setView(view);
                a.setPositiveButton("fermer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                a.create();
                a.show();
            }
        } else {
            //Toast.makeText(this, "oups une erreur est survenu", Toast.LENGTH_SHORT).show();
        }

        return true;
    }

    public String replaceALaLigne(String word){
        char[] monMot = new char[word.length()];
        boolean remplace = false;
        for(int i = 0;i<word.length();i++){
            monMot[i] = word.charAt(i);
            if(!remplace && word.charAt(i) == '\\'){
                monMot[i] = '\n';
                monMot[i++] = '\n';
                remplace = true;
            }
        }
        return String.valueOf(monMot);
    }

    public int getIndexPoint(List<Feature> features){
        int index = 0;
        for(Feature feature : features){
            if (feature.properties() != null) {
                for (Map.Entry<String, JsonElement> entry : feature.properties().entrySet()) {
                    if(entry.getKey().contains("id_object") ||
                            entry.getValue().toString().equals("\"tree\"") ||
                            entry.getValue().toString().equals("\"trash\"") ||
                            entry.getValue().toString().equals("\"toilet\"") ||
                            entry.getKey().contains("banc")||
                            entry.getValue().toString().contains("banc") ||
                            entry.getValue().toString().contains("Banc") ||
                            entry.getValue().toString().equals("\"pav_verre\"")
                    ){
                        return index;
                    }
                }
            }
            index++;
        }
        return -1;
    }

    private boolean res;

    public void changepassword(){
        final View view = getLayoutInflater().inflate(R.layout.change_mdp, null);
        AlertDialog.Builder chpw = new AlertDialog.Builder(context);
        chpw.setTitle("Changer votre mot de passe");
        chpw.setView(view);
        chpw.setPositiveButton("changer mon mot de passe", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String ancmdp = ((EditText) view.findViewById(R.id.editText_ancien_mdp)).getText().toString();
                String nvmdp = ((EditText) view.findViewById(R.id.editText_nv_mdp)).getText().toString();
                String cfnvmdp = ((EditText) view.findViewById(R.id.editText_cf_nv_mdp)).getText().toString();

                //nvmdp != cfnvmdp
                if(!nvmdp.equals(cfnvmdp) || ancmdp.equals("") || nvmdp.equals("") || cfnvmdp.equals("") || ancmdp.equals("")){
                    Toast.makeText(context,"Les informations données sont incorrectes",Toast.LENGTH_LONG).show();
                    changepassword();
                }
                else{
                    if(updateUser(user.getLogin(),ancmdp,nvmdp,user.getMail())){
                        Toast.makeText(context,"Votre mdp à bien été mis a jours",Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(context,"Les informations données sont incorrectes",Toast.LENGTH_LONG).show();
                        changepassword();
                    }
                }
            }
        });
        chpw.setNegativeButton("annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        chpw.create().show();
    }

    private void connexion() {
        final MenuItem m = menuPrincipal.getItem(4);
        final View view1 = getLayoutInflater().inflate(R.layout.connexion,null);
        final AlertDialog.Builder connexion = new AlertDialog.Builder(this);
        connexion.setTitle("Page de connexion");
        connexion.setView(view1);
        connexion.setPositiveButton("Connexion", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String login = ((EditText) view1.findViewById(R.id.editText_login)).getText().toString();
                String mdp = ((EditText) view1.findViewById(R.id.editText_mdp)).getText().toString();
                if(!login.equals("") && !mdp.equals("")){
                    if(connect_to_account = testConnexion(login,mdp)){
                        Toast.makeText(context,"Vous êtes désormé connecté !",Toast.LENGTH_LONG).show();
                        m.setIcon(R.drawable.baseline_person_outline_white_18dp);
                        user.setLogin(login);
                    }
                    else {
                        Toast.makeText(context,"Les informations données ne sont pas correctes",Toast.LENGTH_LONG).show();
                        connexion();
                    }
                }
                else{
                    Toast.makeText(context,"Un des champs est resté vide",Toast.LENGTH_LONG).show();
                    connexion();
                }
            }
        });
        connexion.setNegativeButton("Inscription", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                inscription();
            }
        });
        connexion.create().show();
    }

    private void inscription(){
        final View view = getLayoutInflater().inflate(R.layout.inscription,null);
        final AlertDialog.Builder inscription = new AlertDialog.Builder(this);
        inscription.setTitle("Page d'inscription");
        inscription.setView(view);
        inscription.setPositiveButton("inscription", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String login = ((EditText) view.findViewById(R.id.editText_inscription_login)).getText().toString();
                String mail = ((EditText) view.findViewById(R.id.editText_inscription_mail)).getText().toString();
                String mdp = ((EditText) view.findViewById(R.id.editText_inscription_mdp)).getText().toString();
                String cfmdp = ((EditText) view.findViewById(R.id.editText_inscription_cfmdp)).getText().toString();

                if(login.equals("") || mail.equals("") || mdp.equals("") || cfmdp.equals("") || !mdp.equals(cfmdp)){
                    Toast.makeText(context,"Un champ n'est pas remplis ou des informations sont incorrectes",Toast.LENGTH_LONG).show();
                    inscription();
                }
                else{
                    if(createUser(login,mdp,mail)){
                        Toast.makeText(context,"Vous vous êtes bien enregistré\nVeuillez vous connecter à présent.",Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(context,"Un utilisateur est déjà enregistré sous ce login",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        inscription.create().show();
    }

    public void profil(){
        final View view2 = getLayoutInflater().inflate(R.layout.profil,null);
        TextView text_login = (TextView) view2.findViewById(R.id.text_login);
        TextView text_mail = (TextView) view2.findViewById(R.id.text_mail);
        text_login.setText(user.getLogin());
        text_mail.setText(user.getMail());
        final AlertDialog.Builder profil = new AlertDialog.Builder(this);
        profil.setTitle("Votre profil");
        profil.setView(view2);
        Button changemdp = (Button) view2.findViewById(R.id.bt_changer_mdp);
        Button supprimercpt = (Button) view2.findViewById(R.id.bt_supp_cpt);
        changemdp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(connect_to_server){
                    changepassword();
                }else{
                    Toast.makeText(context,"désoler le serveur est hors ligne, actualisez pour savoir si il est de nouveau en ligne",Toast.LENGTH_LONG).show();
                }

            }
        });

        supprimercpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suppCompte();
            }
        });
        profil.setPositiveButton("Fermer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        profil.setNegativeButton("Deconnexion", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(deconnexion()){
                    connect_to_account = false;
                    menuPrincipal.getItem(4).setIcon(R.drawable.baseline_lock_white_18dp);
                    user = new User();
                    Toast.makeText(context,"vous êtes deconnecté !",Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(context,"oups nous n'avons pas pu vous déconnecter",Toast.LENGTH_LONG).show();
                }
            }
        });
        profil.create().show();
    }

    private void suppCompte() {
        AlertDialog.Builder supprimerCompte = new AlertDialog.Builder(context);
        supprimerCompte.setTitle("Suppression de compte");
        supprimerCompte.setMessage("Etes-vous sûr de  vouloir supprimer votre compte ?");
        supprimerCompte.setPositiveButton("oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(deleteUser()){
                    Toast.makeText(context,"Votre compte à bien été supprimé",Toast.LENGTH_LONG).show();
                    connect_to_account = false;
                    menuPrincipal.getItem(4).setIcon(R.drawable.baseline_lock_white_18dp);
                    user = new User();
                }
                else{
                    Toast.makeText(context,"Nous n'avons pas pu supprimer votre compte veuillez réessayer",Toast.LENGTH_LONG).show();
                }
            }
        });
        supprimerCompte.setNegativeButton("non", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        supprimerCompte.create().show();
    }

    public void modifIPServer(){
        View view = getLayoutInflater().inflate(R.layout.change_url,null);
        final EditText newUrl = (EditText) view.findViewById(R.id.editText_change_url);
        AlertDialog.Builder modif = new AlertDialog.Builder(context)
                .setView(view)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        url = newUrl.getText().toString();
                        Toast.makeText(context,"URL change"+url,Toast.LENGTH_LONG).show();
                    }
                });
        modif.create().show();
    }

    private Bitmap d;
    public Bitmap LoadImageFromWebOperations(final String url){
        d=null;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder().url(url).build();
                try {
                    Response response = clientAPI.newCall(request).execute();
                    if(response.isSuccessful()){
                        InputStream stream = response.body().byteStream();
                        d = BitmapFactory.decodeStream(stream);
                    }
                    else{
                        Log.i("loadImageFromUrl","ko : "+response.message());
                    }
                } catch (IOException e) {
                    Log.i("loadImageFromUrl","ko : "+e);
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return d;
    }

    public void affichePointProposition(View view){
        RatingBar notePoint = (RatingBar) view.findViewById(R.id.note_point);
        Button noter = (Button) view.findViewById(R.id.bt_noter_point);

        notePoint.setRating((float) selectedNote.getNote());

        noter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(connect_to_account){
                    View view1 = getLayoutInflater().inflate(R.layout.noter_point,null);
                    final RatingBar notedonne = (RatingBar) view1.findViewById(R.id.note_point_user);

                    AlertDialog.Builder noterpoint = new AlertDialog.Builder(context).setView(view1);
                    noterpoint.setPositiveButton("noter", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(createNote(""+selectedObject.getId_object(),""+notedonne.getRating())){
                                Toast.makeText(context,"votre note a été pris en compte",Toast.LENGTH_LONG ).show();
                            }
                            else{
                                Toast.makeText(context,messageCreateNote,Toast.LENGTH_LONG ).show();
                            }
                        }
                    });
                    noterpoint.setNegativeButton("fermer", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    noterpoint.create().show();
                }
                else {
                    Toast.makeText(context,"vous n'êtes pas connecté !",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private String resultat_saison;

    public void affichePointArbre(final View view) {
        //final ArrayList<String> listUrlImage = new ArrayList<>();
        listPictures = new ArrayList<>();
        listIdPictures = new ArrayList<>();
        listFilePictures = new ArrayList<>();
        if (selectPictureByObject("" + selectedObject.getId_object())) {
            for(int i = 0 ; i< listIdPictures.size();i++){
                if(selectPictureById(listIdPictures.get(i),i)){
                    Log.i("recup images","images : "+i+", bien recup");
                }
                else{
                    Log.i("recup images","images : "+i+", pas recup");
                }
            }
            Log.i("recup images","nb images recup : "+listIdPictures.size());
            Log.i("recup images","images bien recup");
        }
        else{
            Toast.makeText(context,"Aucune photo n'est lié à arbre",Toast.LENGTH_LONG).show();
            Log.i("recup images","pas d'images recup");
        }

        final String[] saisons = new String[]{"Toutes les photos","Eté", "Printemps", "Hiver", "Automne"};
        resultat_saison = saisons[0];
        final Spinner spinner = (Spinner) view.findViewById(R.id.choix_saison);
        ArrayAdapter<String> dataAdapterR = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, saisons);
        dataAdapterR.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapterR);
        spinner.setEnabled(false);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                resultat_saison = saisons[position];

                ViewPager viewPager = view.findViewById(R.id.album_arbres);
                switch (resultat_saison){
                    case "Toutes les photos" :
                        ImageAdapter adapter = new ImageAdapter(context, listDrawableImage);
                        viewPager.setAdapter(adapter);
                        break;
                    case "Eté" :
                        ArrayList<Bitmap> listPicturesEte = new ArrayList<>();
                        for(int i = 0;i<listPictures.size();i++){
                            String saison = listPictures.get(i).getSaison();
                            if(saison.contains("ete") || saison.contains("Ete") || saison.contains("eté") || saison.contains("Eté")){
                                listPicturesEte.add(listDrawableImage.get(i));
                            }
                        }
                        if(listPicturesEte.size() == 0){
                            listPicturesEte.add(BitmapFactory.decodeResource(getResources(),R.drawable.icons_pas_de_photo));
                        }
                        ImageAdapter adapterEte = new ImageAdapter(context, listPicturesEte);
                        viewPager.setAdapter(adapterEte);
                        break;
                    case "Printemps":
                        ArrayList<Bitmap> listPicturesPrintemps = new ArrayList<>();
                        for(int i = 0;i<listPictures.size();i++){
                            String saison = listPictures.get(i).getSaison();
                            if(saison.contains("Printemps") || saison.contains("printemps")){
                                listPicturesPrintemps.add(listDrawableImage.get(i));
                            }
                        }
                        if(listPicturesPrintemps.size() == 0){
                            listPicturesPrintemps.add(BitmapFactory.decodeResource(getResources(),R.drawable.icons_pas_de_photo));
                        }
                        ImageAdapter adapterPrintemps = new ImageAdapter(context, listPicturesPrintemps);
                        viewPager.setAdapter(adapterPrintemps);
                        break;
                    case "Hiver":
                        ArrayList<Bitmap> listPicturesHiver = new ArrayList<>();
                        for(int i = 0;i<listPictures.size();i++){
                            String saison = listPictures.get(i).getSaison();
                            if(saison.contains("Hiver") || saison.contains("hiver")){
                                listPicturesHiver.add(listDrawableImage.get(i));
                            }
                        }
                        if(listPicturesHiver.size() == 0){
                            listPicturesHiver.add(BitmapFactory.decodeResource(getResources(),R.drawable.icons_pas_de_photo));
                        }
                        ImageAdapter adapterHiver = new ImageAdapter(context, listPicturesHiver);
                        viewPager.setAdapter(adapterHiver);
                        break;
                    case "Automne":
                        ArrayList<Bitmap> listPicturesAutomne = new ArrayList<>();
                        for(int i = 0;i<listPictures.size();i++){
                            String saison = listPictures.get(i).getSaison();
                            if(saison.contains("Automne") || saison.contains("automne")){
                                listPicturesAutomne.add(listDrawableImage.get(i));
                            }
                        }
                        if(listPicturesAutomne.size() == 0){
                            listPicturesAutomne.add(BitmapFactory.decodeResource(getResources(),R.drawable.icons_pas_de_photo));
                        }
                        ImageAdapter adapterAutomne = new ImageAdapter(context, listPicturesAutomne);
                        viewPager.setAdapter(adapterAutomne);
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                resultat_saison = saisons[0];
            }
        });
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                listDrawableImage = new ArrayList<>();
                for (String image : listFilePictures) {
                    Log.i("recup images",image);
                    listDrawableImage.add(LoadImageFromWebOperations(image));
                    Log.i("recup images","ok");
                }

                if (listDrawableImage.size() == 0) {
                    listDrawableImage.add(BitmapFactory.decodeResource(getResources(),R.drawable.icons_pas_de_photo));
                }

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        GifImageButton g = (GifImageButton) view.findViewById(R.id.gif_chargement);
                        ViewPager viewPager = view.findViewById(R.id.album_arbres);
                        ImageAdapter adapter = new ImageAdapter(context, listDrawableImage);
                        viewPager.setAdapter(adapter);
                        viewPager.getLayoutParams().height = g.getLayoutParams().height;
                        g.getLayoutParams().height = 0;
                        g.requestLayout();
                        g.setVisibility(View.INVISIBLE);
                        viewPager.requestLayout();
                        viewPager.setVisibility(View.VISIBLE);
                        spinner.setEnabled(true);
                    }
                });
            }
        });
        t.start();
    }

    private int GALLERY_REQUEST_CODE = 0;
    private String galleryFillPath;

    private void pickFromGallery(){
        galleryFillPath ="";
        view_picture_storage = getLayoutInflater().inflate(R.layout.take_picture_storage,null);
        AlertDialog.Builder picture_int_storage = new AlertDialog.Builder(context).setView(view_picture_storage);
        GifImageView g = view_picture_storage.findViewById(R.id.gif_chargement);
        final String[] saisons = new String[]{"Eté", "Printemps", "Hiver", "Automne"};
        resultat_saison = saisons[0];
        final Spinner spinner = (Spinner) view_picture_storage.findViewById(R.id.choix_saison);
        ArrayAdapter<String> dataAdapterR = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, saisons);
        dataAdapterR.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapterR);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                resultat_saison = saisons[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                resultat_saison = saisons[0];
            }
        });
        g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create an Intent with action as ACTION_PICK
                Intent intent=new Intent(Intent.ACTION_PICK);
                // Sets the type as image/*. This ensures only components of type image are selected
                intent.setType("image/*");
                //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
                String[] mimeTypes = {"image/jpeg", "image/png"};
                intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
                // Launching the Intent
                startActivityForResult(intent,GALLERY_REQUEST_CODE);
            }
        });
        picture_int_storage.setPositiveButton("ajouter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(galleryFillPath.equals("")){
                    Toast.makeText(context,"vous n'avez pas selectionne de photo",Toast.LENGTH_LONG).show();
                }
                else{
                    if(createPicture(selectedObject.getId_object(),resultat_saison,galleryFillPath)){
                        Toast.makeText(context,"votre photo a bien été ajouté",Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(context,"une erreur est survenue lors de la mise en ligne de votre image",Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
        picture_int_storage.setNegativeButton("annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        picture_int_storage.create().show();

        //Create an Intent with action as ACTION_PICK
        Intent intent=new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        // Launching the Intent
        startActivityForResult(intent,GALLERY_REQUEST_CODE);
    }

    private int CAMERA_REQUEST_CODE = 1;
    private String cameraFilePath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        //This is the directory in which the file will be created. This is the default location of Camera photos
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for using again
        selectedFile = image;
        cameraFilePath = image.getAbsolutePath();
        return image;
    }

    private void captureFromCamera() {
        view_picture_storage = getLayoutInflater().inflate(R.layout.take_picture_storage,null);
        AlertDialog.Builder picture_int_storage = new AlertDialog.Builder(context).setView(view_picture_storage);
        GifImageView g = view_picture_storage.findViewById(R.id.gif_chargement);

        Spinner choix_saison = (Spinner) view_picture_storage.findViewById(R.id.choix_saison);
        ArrayAdapter<String> dataAdapterR = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,new String[]{"Eté", "Printemps", "Hiver", "Automne"});
        dataAdapterR.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        choix_saison.setAdapter(dataAdapterR);
        choix_saison.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                resultat_choix_saison = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                resultat_choix_saison = 0;
            }
        });
        g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", createImageFile()));
                    startActivityForResult(intent, CAMERA_REQUEST_CODE);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        picture_int_storage.setPositiveButton("ajouter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String saison = "";
                switch (resultat_choix_saison){
                    case 0:
                        saison = "automne";
                        break;
                    case 1:
                        saison = "été";
                        break;
                    case 2:
                        saison = "printemps";
                        break;
                    case 3:
                        saison = "hiver";
                        break;
                }
                if(createPicture(selectedObject.getId_object(),saison,cameraFilePath)){
                    Toast.makeText(context,"photo bien ajouté",Toast.LENGTH_LONG).show();
                    View view = getLayoutInflater().inflate(R.layout.affichage_point_arbre,null);
                    affichePointArbre(view);
                }
                else{
                    Toast.makeText(context,"Oups, un problème est survenu lors de la mise en ligne de votre photo ...",Toast.LENGTH_LONG).show();
                }
            }
        });
        picture_int_storage.setNegativeButton("annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        picture_int_storage.create().show();
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", createImageFile()));
            startActivityForResult(intent, CAMERA_REQUEST_CODE);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void onActivityResult(int requestCode,int resultCode,Intent data){
        GifImageView gifImageView = (GifImageView) view_picture_storage.findViewById(R.id.gif_chargement);
        ImageView imageView = (ImageView) view_picture_storage.findViewById(R.id.imageView_storage);
        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode){
                case 0:
                    //data.getData returns the content URI for the selected Image
                    Uri selectedImage = data.getData();
                    File file = new File(getRealPathFromURI(selectedImage));
                    selectedFile = file;
                    galleryFillPath = file.getAbsolutePath();
                    if(gifImageView != null){
                        gifImageView.setVisibility(View.INVISIBLE);
                    }
                    imageView.setImageURI(selectedImage);
                    imageView.getLayoutParams().height = 1500;
                    gifImageView.getLayoutParams().height = 0;
                    break;
                case 1:
                    if(gifImageView != null){
                        gifImageView.setVisibility(View.INVISIBLE);
                    }
                    imageView.setImageURI(Uri.parse(cameraFilePath));
                    imageView.getLayoutParams().height = 1500;
                    gifImageView.getLayoutParams().height = 0;
                    break;
            }
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public void proposition(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.new_point, null);

        //CONF SPINNER
        final Spinner spinner = (Spinner) view.findViewById(R.id.categorieList);
        ArrayAdapter<String> dataAdapterR = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,listLieuPropositions);
        dataAdapterR.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapterR);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                resultat = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                resultat = 0;
            }
        });
        //END CONF SPINNER

        final EditText description_edit = view.findViewById(R.id.editText_proposition_description);

        builder.setView(view)
                // Add action buttons
                .setPositiveButton("proposer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if(createObject(listLieuPropositions[resultat],description_edit.getText().toString(),nouveauPoint)){
                            Toast.makeText(context,"proposition bien effectuée",Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(context,"une erreur est survenue",Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton("annuler", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        builder.create();
        builder.show();
    }

    public void reportPoint(){
        View view = getLayoutInflater().inflate(R.layout.report_point,null);
        final EditText description_report = view.findViewById(R.id.editText_report_point);
        AlertDialog.Builder report = new AlertDialog.Builder(context)
                .setView(view);
        report.setPositiveButton("signaler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(createReport(""+selectedObject.getId_object(),description_report.getText().toString())){
                    Toast.makeText(context,"Report bien pris en compte",Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(context,"Une erreur est survenue.",Toast.LENGTH_LONG).show();
                }
            }
        });
        report.setNegativeButton("annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        report.create();
        report.show();
    }

    /*****************************************************************************************************************************************************************************************************/

    @Override
    @SuppressWarnings( {"MissingPermission"})
    protected void onStart() {
        super.onStart();
        mapView.onStart();
        try {
            MenuItem m2 = menuPrincipal.getItem(2);
            if (!connected_to_internet && m2.getIcon().getConstantState().equals(getDrawable(R.drawable.baseline_cloud_off_white_18dp).getConstantState()) && m2 != null) {
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    connected_to_internet = true;
                    Toast.makeText(this, "vous êtes de nouveau en ligne", Toast.LENGTH_LONG).show();
                } else {
                    connected_to_internet = false;
                    Toast.makeText(this, "vous êtes toujours hors ligne", Toast.LENGTH_LONG).show();
                }
                optionsAffichagelist[2] = !connected_to_internet;
            }
        }catch (Exception e){

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        try {
            MenuItem m2 = menuPrincipal.getItem(2);
            if (!connected_to_internet && m2.getIcon().getConstantState().equals(getDrawable(R.drawable.baseline_cloud_off_white_18dp).getConstantState()) && m2 != null) {
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    connected_to_internet = true;
                    Toast.makeText(this, "vous êtes de nouveau en ligne", Toast.LENGTH_LONG).show();
                } else {
                    connected_to_internet = false;
                    Toast.makeText(this, "vous êtes toujours hors ligne", Toast.LENGTH_LONG).show();
                }
                optionsAffichagelist[2] = !connected_to_internet;
            }
        }catch (Exception e){

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    public void testInternet(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected_to_internet = true;
        }
        else {
            connected_to_internet = false;
            Toast.makeText(this,"vous êtes hors ligne",Toast.LENGTH_LONG).show();
        }
        optionsAffichagelist[2] = !connected_to_internet;
    }

    public void testAPI(){
        Request request = new Request.Builder().url(HOST_API+URL_TEST_API).build();
        clientAPI.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MenuItem i = menuPrincipal.getItem(2);
                        if(!optionsAffichagelist[2] && !i.getIcon().getConstantState().equals(getDrawable(R.drawable.baseline_report_problem_white_18dp).getConstantState())) {
                            i.setIcon(R.drawable.baseline_report_problem_white_18dp);
                            erreurUpdate.show();
                            connect_to_server = false;
                        }
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MenuItem i = menuPrincipal.getItem(2);
                        i.setIcon(R.drawable.baseline_done_white_18dp);
                        connect_to_server = true;
                    }
                });
            }
        });
    }

    private boolean resultat_testJWT;

    public boolean testJWT(){
        final Request request = new Request.Builder().url(HOST_API+URL_TEST_JWT).addHeader("Authorization","BearerToken "+user.getSecret_token()).build();
        resultat_testJWT=false;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = null;
                try {
                    response = clientAPI.newCall(request).execute();
                    if(response.isSuccessful()){
                        resultat_testJWT=true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return resultat_testJWT;

    }

    public boolean testConnexion(final String login,final String mdp){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("login",login).addFormDataPart("password",mdp).build();
                Request request = new Request.Builder().url(HOST_API+URL_TO_LOGIN).post(requestBody).build();
                Response response = null;
                try {
                    response = clientAPI.newCall(request).execute();
                    if(response.isSuccessful()){
                        JSONObject jsonObject = new JSONObject(response.body().string());

                        user.setLogin(login);
                        user.setSecret_token(jsonObject.getString("jwt"));
                        user.setSecret_token_refrech(jsonObject.getString("jwtRefresh"));
                        connect_to_account = true;
                    }
                    else{
                        connect_to_account = false;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    connect_to_account = false;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return connect_to_account;
    }

    private boolean resultat_refreshToken;

    public boolean refreshToken(){
        final Request request = new Request.Builder().url(HOST_API+URL_TO_REFRECHE).addHeader("Authorization","BearerToken "+user.getSecret_token_refrech()).build();
        resultat_refreshToken=false;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = null;
                try {
                    response = clientAPI.newCall(request).execute();
                    if(response.isSuccessful()){
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        user.setSecret_token(jsonObject.getString("jwt"));
                        resultat_refreshToken = true;
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return resultat_refreshToken;
    }

    private boolean resultat_deconnexion;

    public boolean deconnexion(){
        if(!testJWT()){
            if(!refreshToken()){
                Toast.makeText(context,"Votre session n'a pas reussis à ce mettre à jour",Toast.LENGTH_LONG).show();
            }
        }

        final Request request = new Request.Builder().url(HOST_API+URL_TO_LOGOUT).addHeader("Authorization","BearerToken "+user.getSecret_token()).build();
        resultat_deconnexion = false;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = null;
                try {
                    response = clientAPI.newCall(request).execute();
                    if(response.isSuccessful()){
                        resultat_deconnexion = true;
                    }
                    else {
                        resultat_deconnexion = false;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return resultat_deconnexion;
    }
    private boolean resultat_inscription;

    public boolean createUser(String login,String password, String mail){
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("login",login).addFormDataPart("password",password).addFormDataPart("mail",mail).build();
        final Request request = new Request.Builder().url(HOST_API+URL_TO_INSCRIPTION).post(requestBody).build();
        resultat_inscription = false;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = clientAPI.newCall(request).execute();
                    if(response.isSuccessful()){
                        resultat_inscription = true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return resultat_inscription;
    }

    private boolean resultat_delete_user;

    public boolean deleteUser(){
        if(!testJWT()){
            if(!refreshToken()){
                Toast.makeText(context,"Votre session n'a pas reussis à ce mettre à jour",Toast.LENGTH_LONG).show();
            }
        }
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("id_user",""+user.getId_user()).build();
        final Request request = new Request.Builder().url(HOST_API+URL_TO_DELETE_USER).addHeader("Authorization","BearerToken "+user.getSecret_token()).post(requestBody).build();
        resultat_delete_user = false;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = clientAPI.newCall(request).execute();
                    if(response.isSuccessful()){
                        resultat_delete_user = true;
                        Toast.makeText(context,"Votre compte a bien été supprimé !",Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(context,"Oups ! nous n'avons pas réussis à supprimer votre compte",Toast.LENGTH_LONG).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return resultat_delete_user;
    }

    private boolean resultat_selectUserById;

    public boolean selectUserById(){
        if(!testJWT()){
            if(!refreshToken()){
                Toast.makeText(context,"Votre session n'a pas reussis à ce mettre à jour",Toast.LENGTH_LONG).show();
            }
        }
        RequestBody requestBody = RequestBody.create(null,new byte[0]);
        final Request request = new Request.Builder().url(HOST_API+URL_TO_GET_USER_BY_ID).addHeader("Authorization","BearerToken "+user.getSecret_token()).post(requestBody).build();
        resultat_selectUserById = false;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final Response response = clientAPI.newCall(request).execute();
                    if (response.isSuccessful()) {
                        resultat_selectUserById = true;
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        user.setId_user(Integer.parseInt(jsonObject.getString("id_user")));
                        user.setLogin(jsonObject.getString("login"));
                        user.setMail(jsonObject.getString("mail"));

                        Log.i("testGetById",user.getId_user()+","+user.getLogin()+","+user.getMail());
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return resultat_selectUserById;
    }

    private boolean resultat_updateUser;

    public boolean updateUser(final String newLg, String oldPw, String newPw, final String newMail){
        if(!testJWT()){
            if(!refreshToken()){
                Toast.makeText(context,"Votre session n'a pas reussis à ce mettre à jour",Toast.LENGTH_LONG).show();
            }
        }
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("newLogin",newLg)
                .addFormDataPart("oldPassword",oldPw)
                .addFormDataPart("newPassword",newPw)
                .addFormDataPart("newMail",newMail)
                .build();
        final Request request = new Request.Builder().url(HOST_API+URL_TO_UPDATE_USER)
                .addHeader("Authorization","BearerToken "+user.getSecret_token()).post(requestBody).build();
        resultat_updateUser = false;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = clientAPI.newCall(request).execute();
                    if(response.isSuccessful()){
                        resultat_updateUser = true;
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        user.setId_user(Integer.parseInt(jsonObject.getString("id_user")));
                        user.setLogin(newLg);
                        user.setMail(newMail);
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return resultat_updateUser;
    }

    private boolean resultat_selectObjectById;

    public boolean selectObjectById(final String id_object, final LatLng point){
        if(!testJWT()){
            if(!refreshToken()){
                Toast.makeText(context,"Votre session n'a pas reussis à ce mettre à jour",Toast.LENGTH_LONG).show();
            }
        }

        selectedObject = new Object();
        resultat_selectObjectById = false;
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("id_object",id_object)
                .build();
        final Request request = new Request.Builder().url(HOST_API+URL_OBJECT_SELECTBYID)
                .addHeader("Authorization","BearerToken "+user.getSecret_token())
                .post(requestBody)
                .build();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = clientAPI.newCall(request).execute();
                    if(response.isSuccessful()){
                        resultat_selectObjectById = true;
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            selectedObject.setId_object(Integer.parseInt(id_object));
                            selectedObject.setType(jsonObject.getString("type"));
                            selectedObject.setDescription(jsonObject.getString("description"));
                            selectedObject.setGoem(point);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else{

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return resultat_selectObjectById;
    }

    private boolean resultat_createObject;

    public boolean createObject(String type,String description,LatLng point){
        if(!testJWT()){
            if(!refreshToken()){
                Toast.makeText(context,"Votre session n'a pas reussis à ce mettre à jour",Toast.LENGTH_LONG).show();
            }
        }
        resultat_createObject = false;

        switch (type){
            case "arbres_propositions":
                type="tree_suggestion";
                break;
            case "bancs_propositions":
                type="banc_suggestion";
                break;
            case "toilettes_propositions":
                type="toilet_suggestion";
                break;
            case "poubelles_propositions":
                type="trash_suggestion";
                break;
            case "verres_propositions":
                type="pav_verre_suggestion";
                break;
        }

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("type",type)
                .addFormDataPart("description",description)
                .addFormDataPart("latitude",""+point.getLatitude())
                .addFormDataPart("longitude",""+point.getLongitude())
                .build();

        final Request request = new Request.Builder()
                .url(HOST_API+URL_OBJECT_CREATE)
                .addHeader("Authorization","BearerToken "+user.getSecret_token())
                .post(requestBody)
                .build();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = clientAPI.newCall(request).execute();
                    if(response.isSuccessful()){
                        resultat_createObject =true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return resultat_createObject;
    }

    private boolean resultat_updateObject;

    public boolean updateObject(String type,String description){
        if(!testJWT()){
            if(!refreshToken()){
                Toast.makeText(context,"Votre session n'a pas reussis à ce mettre à jour",Toast.LENGTH_LONG).show();
            }
        }
        resultat_updateObject = false;
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("type",type)
                .addFormDataPart("description",description)
                .build();

        final Request request = new Request.Builder()
                .url(HOST_API+URL_OBJECT_UPDATE)
                .addHeader("Authorization","BearerToken "+user.getSecret_token())
                .post(requestBody)
                .build();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = clientAPI.newCall(request).execute();
                    if(response.isSuccessful()){
                        resultat_updateObject = true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return resultat_updateObject;
    }

    private boolean resultat_deleteObject;

    public boolean deleteObject(String id_object){
        if(!testJWT()){
            if(!refreshToken()){
                Toast.makeText(context,"Votre session n'a pas reussis à ce mettre à jour",Toast.LENGTH_LONG).show();
            }
        }
        resultat_deleteObject =false;
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("id_object",id_object)
                .build();

        final Request request = new Request.Builder()
                .url(HOST_API+URL_OBJECT_DELETE)
                .addHeader("Authorization","BearerToken "+user.getSecret_token())
                .post(requestBody)
                .build();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = clientAPI.newCall(request).execute();
                    if(response.isSuccessful()){
                        resultat_deleteObject = true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return resultat_deleteObject;
    }

    private boolean resultat_selectNote;

    public boolean selectNote(String id_object){
        if(!testJWT()){
            if(!refreshToken()){
                Toast.makeText(context,"Votre session n'a pas reussis à ce mettre à jour",Toast.LENGTH_LONG).show();
            }
        }
        resultat_selectNote = false;
        selectedNote = new Note();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("id_objet",id_object)
                .build();

        final Request request = new Request.Builder()
                .url(HOST_API+URL_NOTE_SELECT)
                .addHeader("Authorization","BearerToken "+user.getSecret_token())
                .post(requestBody)
                .build();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = clientAPI.newCall(request).execute();
                    if(response.isSuccessful()){
                        resultat_selectNote = true;
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            selectedNote.setNote(jsonObject.getDouble("note"));
                            Log.i("recup info note",""+selectedNote.getNote());
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i("recup info erreur",""+e.getMessage());
                        }
                    }
                    else{
                        Log.i("recup info erreur",""+response.message());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i("recup info erreur",""+e.getMessage());
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return resultat_selectNote;
    }

    private boolean resultat_createNote;

    public boolean createNote(String id_object,String note){
        if(!testJWT()){
            if(!refreshToken()){
                Toast.makeText(context,"Votre session n'a pas reussis à ce mettre à jour",Toast.LENGTH_LONG).show();
            }
        }
        resultat_createNote = false;
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("id_objet",id_object)
                .addFormDataPart("note",note)
                .build();

        final Request request = new Request.Builder()
                .url(HOST_API+URL_NOTE_CREATE)
                .addHeader("Authorization","BearerToken "+user.getSecret_token())
                .post(requestBody)
                .build();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = clientAPI.newCall(request).execute();
                    if(response.isSuccessful()){
                        resultat_createNote = true;
                    }
                    else{
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        messageCreateNote = jsonObject.getString("msg");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return resultat_createNote;
    }

    private boolean resultat_createReport;

    public boolean createReport(String id_object,String description){
        if(!testJWT()){
            if(!refreshToken()){
                Toast.makeText(context,"Votre session n'a pas reussis à ce mettre à jour",Toast.LENGTH_LONG).show();
            }
        }
        resultat_createReport=false;

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("id_objet",id_object)
                .addFormDataPart("description",description)
                .build();

        final Request request = new Request.Builder()
                .url(HOST_API+URL_REPORT_CREATE)
                .addHeader("Authorization","BearerToken "+user.getSecret_token())
                .post(requestBody)
                .build();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = clientAPI.newCall(request).execute();
                    if(response.isSuccessful()){
                        resultat_createReport=true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return resultat_createReport;
    }

    private boolean resultat_deleteReport;

    public boolean deleteReport(String id_report){
        if(!testJWT()){
            if(!refreshToken()){
                Toast.makeText(context,"Votre session n'a pas reussis à ce mettre à jour",Toast.LENGTH_LONG).show();
            }
        }
        resultat_deleteReport = false;

        Log.i("recup info id report",id_report);

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("id_report",id_report)
                .build();

        final Request request = new Request.Builder()
                .url(HOST_API+URL_REPORT_DELETE)
                .addHeader("Authorization","BearerToken "+user.getSecret_token())
                .post(requestBody)
                .build();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = clientAPI.newCall(request).execute();
                    if(response.isSuccessful()){
                        resultat_deleteReport=true;
                    }
                    else{
                        Log.i("recup info unreport",response.message());
                        Log.i("recup info unreport",response.body().toString());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return resultat_deleteReport;
    }

    private boolean resultat_selectReportByID;

    public boolean selectReportByID(String id_report){
        if(!testJWT()){
            if(!refreshToken()){
                Toast.makeText(context,"Votre session n'a pas reussis à ce mettre à jour",Toast.LENGTH_LONG).show();
            }
        }
        selectedReport = new Report();
        resultat_selectReportByID = false;
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("id_report",id_report)
                .build();

        final Request request = new Request.Builder()
                .url(HOST_API+URL_REPORT_SELECTBYID)
                .addHeader("Authorization","BearerToken "+user.getSecret_token())
                .post(requestBody)
                .build();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = clientAPI.newCall(request).execute();
                    if(response.isSuccessful()){
                        resultat_selectReportByID=true;
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            selectedReport.setId_report(jsonObject.getInt("id_report"));
                            selectedReport.setId_object(jsonObject.getInt("id_objet"));
                            selectedReport.setId_user(jsonObject.getInt("id_user"));
                            selectedReport.setDescription(jsonObject.getString("description"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return resultat_selectReportByID;
    }

    private boolean resultat_selectReportByObject;

    private boolean selectReportByObject(String id_object){
        if(!testJWT()){
            if(!refreshToken()){
                Toast.makeText(context,"Votre session n'a pas reussis à ce mettre à jour",Toast.LENGTH_LONG).show();
            }
        }
        selectedReport = new Report();
        resultat_selectReportByObject = false;
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("id_objet",id_object)
                .build();

        final Request request = new Request.Builder()
                .url(HOST_API+URL_REPORT_SELECTBYOBJECT)
                .addHeader("Authorization","BearerToken "+user.getSecret_token())
                .post(requestBody)
                .build();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = clientAPI.newCall(request).execute();
                    if(response.isSuccessful()){
                        resultat_selectReportByObject=true;
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            selectedReport.setId_report(jsonObject.getInt("id_report"));
                            selectedReport.setId_object(jsonObject.getInt("id_objet"));
                            selectedReport.setId_user(jsonObject.getInt("id_user"));
                            selectedReport.setDescription(jsonObject.getString("description"));

                            Log.i("recup info get rep",""+selectedReport.getId_report());
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i("recup info get rep",e.getMessage());
                        }
                    }
                    else{
                        Log.i("recup info report",response.message());
                        Log.i("recup info report",response.body().string());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return resultat_selectReportByObject;
    }

    private boolean resultat_selectPictureById;

    private boolean selectPictureById(int id_picture,final int index){
        if(!testJWT()){
            if(!refreshToken()){
                Toast.makeText(context,"Votre session n'a pas reussis à ce mettre à jour",Toast.LENGTH_LONG).show();
            }
        }
        resultat_selectPictureById = false;

        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("id_picture",""+id_picture)
                .build();

        final Request request = new Request.Builder()
                .url(HOST_API+URL_PICTURE_SELECTBYID)
                .addHeader("Authorization","BearerToken "+user.getSecret_token())
                .post(requestBody)
                .build();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = clientAPI.newCall(request).execute();
                    if(response.isSuccessful()){
                        resultat_selectPictureById = true;
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            listPictures.add(new Picture());
                            listPictures.get(index).setId_picture(jsonObject.getInt("id_picture"));
                            listPictures.get(index).setId_object(jsonObject.getInt("id_objet"));
                            listPictures.get(index).setId_user(jsonObject.getInt("id_user"));
                            listPictures.get(index).setSaison(jsonObject.getString("saison"));
                            listPictures.get(index).setFile(HOST_API_PICTURES+jsonObject.getString("file"));
                            listFilePictures.add(listPictures.get(index).getFile());
                            Log.i("recup images",listPictures.get(index).getSaison());
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i("recup image",e.getMessage());
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i("recup image",e.getMessage());
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return resultat_selectPictureById;
    }

    private boolean resultat_selectPictureByObject;

    public boolean selectPictureByObject(String id_object){
        if(!testJWT()){
            if(!refreshToken()){
                Toast.makeText(context,"Votre session n'a pas reussis à ce mettre à jour",Toast.LENGTH_LONG).show();
            }
        }
        resultat_selectPictureByObject = false;
        listIdPictures = new ArrayList<>();

        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("id_objet",id_object)
                .build();

        final Request request = new Request.Builder()
                .url(HOST_API+URL_PICTURE_SELECTBYOBJECT)
                .addHeader("Authorization","BearerToken "+user.getSecret_token())
                .post(requestBody)
                .build();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = clientAPI.newCall(request).execute();
                    if(response.isSuccessful()){
                        resultat_selectPictureByObject = true;
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            for(int i=1;i<jsonObject.length()+1;i++){
                                listIdPictures.add(jsonObject.getInt("id_picture_"+i));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.i("recup images",e.getMessage());
                        }
                    }
                    else{
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        Log.i("recup images",jsonObject.getString("msg"));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i("recup images",e.getStackTrace().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("recup images",e.getMessage());
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return resultat_selectPictureByObject;
    }

    private boolean resultat_createPicture;

    public boolean createPicture(int id_object,String saison,String file){
        if(!testJWT()){
            if(!refreshToken()){
                Toast.makeText(context,"Votre session n'a pas reussis à ce mettre à jour",Toast.LENGTH_LONG).show();
            }
        }
        resultat_createPicture = false;

        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("id_objet", ""+id_object)
                .addFormDataPart("saison", saison)
                .addFormDataPart("file", selectedFile.getName(),RequestBody.create(MediaType.parse("application/octet-stream"),new File(file)))
                .build();

        final Request request = new Request.Builder()
                .url(HOST_API+URL_PICTURE_CREATE)
                .addHeader("Authorization","BearerToken "+user.getSecret_token())
                .post(requestBody)
                .build();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = clientAPI.newCall(request).execute();
                    if(response.isSuccessful()){
                        resultat_createPicture = true;
                    }
                    else{
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        Log.i("recup info image",jsonObject.getString("msg"));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i("recup info image",""+e);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("recup info image","erreur 2");
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return resultat_createPicture;

    }
}
