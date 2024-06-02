package com.example.projekatmobilneaplikacije.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.databinding.ActivityHomeBinding;
import com.example.projekatmobilneaplikacije.fragments.LoginFragment;
import com.example.projekatmobilneaplikacije.fragments.employees.EmployeesPageFragment;
import com.example.projekatmobilneaplikacije.fragments.registration.RegistrationFragment;
import com.example.projekatmobilneaplikacije.model.UserDetails;
import com.example.projekatmobilneaplikacije.model.enumerations.UserRole;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashSet;
import java.util.Set;


public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;
    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private NavController navController;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Set<Integer> topLevelDestinations = new HashSet<>();
    FirebaseAuth auth;

    FirebaseUser user;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("ShopApp", "HomeActivity onCreate()");
        db = FirebaseFirestore.getInstance();

        /* Umesto referenci na resurse unutar nekog layout-a moguće je
         * koristiti ViewBinding. Na osnovu konfiguracije u build.gradle fajlu
         * izgenerisana je "ActivityHomeBinding" klasa koja je povezana sa
         * XML datotekom koja sadrži rasporede elemenata korisničkog interfejsa.
         * Ova klasa sadrži reference na sve elemente unutar rasporeda,
         * omogućavajući vam pristup i manipulaciju tim elementima
         * iz Java koda na jednostavniji način. To eliminiše potrebu
         * za ručnim pronalaženjem i povezivanjem tih elemenata kroz
         * "findViewById" metode. Ukoliko imamo xml datoteku "activity_home.xml",
         * sistem će generisati klasu "ActivityHomeBinding".
         * */



        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        drawer = binding.drawerLayout;
        navigationView = binding.navView;
        toolbar = binding.activityHomeBase.toolbar;
        // Postavljamo toolbar kao glavnu traku za ovu aktivnost
        setSupportActionBar(toolbar);
        // Dobavljamo referencu na glavnu traku za ovu aktivnost
        actionBar = getSupportActionBar();
        if(actionBar != null){
            // postavlja prikazivanje "strelice prema nazad" (back arrow)
            // kao indikatora navigacije na lijevoj strani Toolbar-a.
            actionBar.setDisplayHomeAsUpEnabled(false);
            // postavlja ikonu koja se prikazuje umjesto strelice prema nazad.
            // U ovom slučaju, postavljena je ikona hamburger iz drawable resursa (ic_hamburger).
            actionBar.setHomeAsUpIndicator(R.drawable.ic_hamburger);
            //ovo omogućuje da se klikom na 'home' na Toolbar-u
            // aktivira povratak na prethodnu aktivnost.
            actionBar.setHomeButtonEnabled(false);
        }

        //  ActionBarDrawerToggle se koristi za povezivanje i upravljanje navigation drawer-om
        //  unutar Android aplikacije. ActionBarDrawerToggle je klasa koja olakšava sinhronizaciju
        //  između navigation drawer-a i ActionBar-a (ili Toolbar-a) te omogućava otvaranje
        //  i zatvaranje navigation drawer-a putem ikone u ActionBar-u ili Toolbar-u.
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        // dodajemo navigation drawer-u listener za događaje koji se dese.
        // actionBarDrawerToggle prati promene stanja drawera i reaguje na njih.
        drawer.addDrawerListener(actionBarDrawerToggle);
        // syncState() se koristi kako bi se uskladile ikone (npr. "hamburger" ikona)
        // i stanja između ActionBar-a (ili Toolbar-a) i drawer-a. Ova metoda osigurava
        // da se ikona na ActionBar-u (ili Toolbar-u) pravilno menja u zavisnosti
        // od stanja drawer-a (otvoreno ili zatvoreno).
        actionBarDrawerToggle.syncState();

        topLevelDestinations.add(R.id.nav_language);
        topLevelDestinations.add(R.id.nav_settings);
        // NavigationController se koristi za upravljanje promenama destinacija unutar Android
        // aplikacije korištenjem Android Navigation komponente.
        // Pomoću NavController i OnDestinationChangedListener, prati se promena trenutne
        // destinacije (screen-a/fragmenta) unutar aplikacije.
        navController = Navigation.findNavController(this, R.id.fragment_nav_content_main);
        navController.addOnDestinationChangedListener((navController, navDestination, bundle) -> {
            Log.i("ShopApp", "Destination Changed");
            // Implementacija koja se poziva kada se promijeni destinacija
            // Check if the current destination is a top-level destination (destination outside the drawer)
            int id = navDestination.getId();
            boolean isTopLevelDestination = topLevelDestinations.contains(id);
            /* Logic to determine if the destination is top level */;
            if (!isTopLevelDestination) {
                if (id == R.id.nav_profile) {
                    Toast.makeText(HomeActivity.this, "Profile", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_logout) {
                    Toast.makeText(HomeActivity.this, "Logout", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_employees) {
                Toast.makeText(HomeActivity.this, "Employees", Toast.LENGTH_SHORT).show();
            }
                // Close the drawer if the destination is not a top level destination
                drawer.closeDrawers();
            } else {
                if (id == R.id.nav_settings) {
                    Toast.makeText(HomeActivity.this, "Settings", Toast.LENGTH_SHORT).show();
                }
            }
        });
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (user == null) {
            // Navigacija na glavnu aktivnost
            Menu navMenu = navigationView.getMenu();
            navMenu.findItem(R.id.nav_logout).setVisible(false);
            navMenu.findItem(R.id.nav_login).setVisible(true);
            navMenu.findItem(R.id.nav_reservations).setVisible(false);
            navMenu.findItem(R.id.nav_bundle_reservations).setVisible(false);
        } else {
            // Standardna navigacija na fragment za odjavu
            Menu navMenu = navigationView.getMenu();
            navMenu.findItem(R.id.nav_logout).setVisible(true);
            navMenu.findItem(R.id.nav_login).setVisible(false);
            navMenu.findItem(R.id.nav_reservations).setVisible(true);
            navMenu.findItem(R.id.nav_bundle_reservations).setVisible(true);
        }
if(user!= null) {

    db.collection("userDetails")
            .whereEqualTo("username", user.getEmail())
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        // Ako postoji rezultat, preuzmite prvi dokument (trebalo bi da bude samo jedan)
                        DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                        // Preuzmite UserDetails iz dokumenta
                        UserDetails userDetails = documentSnapshot.toObject(UserDetails.class);
                        if (userDetails != null) {
                            Log.d("UserDetails", "Username: " + userDetails.getUsername());
                            Log.d("UserDetails", "Name: " + userDetails.getName());
                            Log.d("UserDetails", "Surname: " + userDetails.getSurname());
                            Log.d("UserDetails", "Role: " + userDetails.getRole());
                            if (userDetails.getRole().equals(UserRole.Admin)) {
                                Menu navMenu = navigationView.getMenu();
                                navMenu.findItem(R.id.nav_registration_requests).setVisible(true);
                            }else {
                                Menu navMenu = navigationView.getMenu();
                                navMenu.findItem(R.id.nav_registration_requests).setVisible(false);
                            }
                        }
                    } else {
                        Log.d("Firestore", "No documents found for email: " + user.getEmail());
                    }
                } else {
                    Log.w("Firestore", "Error getting documents.", task.getException());
                }
            });
}
        // AppBarConfiguration odnosi se na konfiguraciju ActionBar-a (ili Toolbar-a) u Android aplikaciji
        // kako bi se omogućila navigacija koristeći Android Navigation komponentu.
        // Takođe, postavlja se bočni meni (navigation drawer) u skladu sa
        // konfiguracijom akcione trake i navigacije.
        // Svaki ID menija prosleđuje se kao skup ID-ova jer svaki meni treba smatrati odredištima najvišeg nivoa.
        mAppBarConfiguration = new AppBarConfiguration
                .Builder(R.id.nav_profile, R.id.nav_logout, R.id.nav_settings, R.id.nav_language, R.id.nav_employees)
                .setOpenableLayout(drawer)
                .build();
        // Ova linija koda postavlja navigationView da radi zajedno sa NavController-om.
        // To znači da će NavigationView reagovati na korisničke interakcije i navigaciju kroz aplikaciju putem NavController-a.
        NavigationUI.setupWithNavController(navigationView, navController);
        // Ova linija koda povezuje NavController sa ActionBar-om (ili Toolbar-om) tako da ActionBar (ili Toolbar)
        // može pravilno reagovati na navigaciju kroz različite destinacije koje su navedene unutar mAppBarConfiguration.
        // NavController će upravljati povratnom strelicom i ponašanjem akcione trake u skladu sa postavljenim destinacijama.
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);

    }

    /*
     * Android aktivnosti koja se koristi za inflaciju (kreiranje) menija u aplikaciji.
     * Ova metoda je dio životnog ciklusa aktivnosti i poziva se kada se izrađuje akcijska traka (action bar)
     * za prikazivanje opcija u meniju (menu items) za određenu aktivnost.
     * Ako zelimo da nasa aktivnost/fragment prikaze ikonice unutar toolbar-a
     * to se odvija u nekoliko koraka:
     * 1. Potrebno je da napravimo listu koja specificira koje sve ikonice imamo unutar menija,
     * koje ikone koristimo i da li se uvek prikazuju ili ne.
     * 2. Nakon toga koristeci metodu onCreateOptionsMenu postavljamo ikone na toolbar.
     * */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // menu.clear();
        // koristimo ako je nasa arhitekrura takva da imamo jednu aktivnost
        // i vise fragmentaa gde svaki od njih ima svoj menu unutar toolbar-a

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }


    /*
     * Prestavlja deo aktivnosti unutar Android aplikacije koji se koristi
     * za upravljanje akcijama koje se odvijaju kada korisnik odabere opciju
     * izbornika (menu item) u ActionBar-u (alatnoj traci) ili Toolbar-u aplikacije.
     * Da bi znali na koju ikonicu je korisnik kliknuo
     * treba da iskoristimo jedinstveni identifikator za svaki element u listi.
     * Metoda onOptionsItemSelected ce vratiti MenuItem na koji je korisnik kliknuo.
     * Ako znamo da ce on vratiti i id, tacno znamo na koji element je korisnik
     * kliknuo, i shodno tome reagujemo.
     * */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        int id = item.getItemId();
//        switch (id) {
//            case R.id.nav_settings:
//                Toast.makeText(HomeActivity.this, "Settings", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.nav_language:
//                Toast.makeText(HomeActivity.this, "Language", Toast.LENGTH_SHORT).show();
//                break;
//        }
        // U ovoj metodi, prvo se pomoću Navigation komponente pronalazi NavController.
        // NavController je odgovoran za upravljanje navigacijom unutar aplikacije
        // koristeći Androidov servis za navigaciju.
        navController = Navigation.findNavController(this, R.id.fragment_nav_content_main);
        // Nakon toga, koristi se NavigationUI.onNavDestinationSelected(item, navController)
        // kako bi se omogućila integracija između MenuItem-a i odredišta unutar aplikacije
        // definisanih unutar navigacionog grafa (NavGraph).
        // Ova funkcija proverava da li je odabrana stavka izbornika povezana s nekim
        // odredištem unutar navigacionog grafa i pokreće tu navigaciju ako postoji
        // odgovarajuće podudaranje.
        return NavigationUI.onNavDestinationSelected(item, navController) || super.onOptionsItemSelected(item);
    }

    // koristi se u Android aktivnosti kako bi se definisalo ponašanje povratka
    // unutar aplikacije kada se koristi navigacija kroz različite fragmente ili destinacije
    // unutar aplikacije.
    // Kada korisnik pritisne "back" ili navigacijsku strelicu unutar aplikacije,
    // ova metoda se poziva kako bi odredila ponašanje povratka.
    @Override
    public boolean onSupportNavigateUp() {
        navController = Navigation.findNavController(this, R.id.fragment_nav_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    private void createNotificationChannel(CharSequence name, String description, String channel_id, int importance) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        NotificationChannel channel = new NotificationChannel(channel_id, name, importance);
        channel.setDescription(description);
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
}