package com.example.suburbselectorfinal;

//import static com.example.suburbselectorfinal.BuildConfig.MAPS_API_KEY;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.suburbselectorfinal.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;



public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {

    public static final String MAPS_API_KEY="AIzaSyA1v4y_trhQCqDmKV86bhcBPr1iOSOH3OA";
    private GoogleMap mMap;

    private University university;

    private PlacesClient placesClient;

    private TextView placeText;

    private HashMap<Integer, LatLng> placeCoordinates = new HashMap<Integer, LatLng>();

    private ArrayList<Suburb> results;

    private DatabaseReference reference;
    private FirebaseUser user;
    private String userID;
    private List<String> favourites;
    private User userProfile;

    private Button result1Button;
    private Button result2Button;
    private Button result3Button;

    private ImageView favourite1IV;
    private ImageView favourite2IV;
    private ImageView favourite3IV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.suburbselectorfinal.databinding.ActivityMapsBinding binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (!Places.isInitialized()){
            Places.initialize(getApplicationContext(),MAPS_API_KEY);
        }

        placesClient = Places.createClient(this);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userID);
        reference.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                userProfile = dataSnapshot.getValue(User.class);

                favourites = userProfile.favourites;
            }
        });

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment1 = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment1);
        // Specify the types of place data to return.
        autocompleteFragment1.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME));
        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment1.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onError(@NonNull Status status) {

            }

            @Override
            public void onPlaceSelected(@NonNull Place place) {
                mMap.clear();

                final LatLng latLng = place.getLatLng();

                placeCoordinates.put(1, latLng);

                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.addMarker(new MarkerOptions().position(latLng).title("Marker at " + place.getName()));
                mMap.moveCamera(CameraUpdateFactory.zoomTo(13.0f));

                placeText.setText(place.getName());
            }
        });
        autocompleteFragment1.getView().findViewById(com.google.android.libraries.places.R.id.places_autocomplete_clear_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        placeCoordinates.remove(1);
                        autocompleteFragment1.setText("");
                        view.setVisibility(View.GONE);
                    }
                });
        View fView1 = autocompleteFragment1.getView();
        EditText etTextInput1 = fView1.findViewById(com.google.android.libraries.places.R.id.places_autocomplete_search_input);
        etTextInput1.setTextSize(16f);

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment2 = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment2);
        // Specify the types of place data to return.
        autocompleteFragment2.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME));
        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment2.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onError(@NonNull Status status) {

            }

            @Override
            public void onPlaceSelected(@NonNull Place place) {
                mMap.clear();

                final LatLng latLng = place.getLatLng();

                placeCoordinates.put(2, latLng);

                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.addMarker(new MarkerOptions().position(latLng).title("Marker at " + place.getName()));
                mMap.moveCamera(CameraUpdateFactory.zoomTo(13.0f));

                placeText.setText(place.getName());
            }
        });
        autocompleteFragment2.getView().findViewById(com.google.android.libraries.places.R.id.places_autocomplete_clear_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        placeCoordinates.remove(2);
                        autocompleteFragment2.setText("");
                        view.setVisibility(View.GONE);
                    }
                });
        View fView2 = autocompleteFragment2.getView();
        EditText etTextInput2 = fView2.findViewById(com.google.android.libraries.places.R.id.places_autocomplete_search_input);
        etTextInput2.setTextSize(16f);

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment3 = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment3);
        // Specify the types of place data to return.
        autocompleteFragment3.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME));
        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment3.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onError(@NonNull Status status) {

            }

            @Override
            public void onPlaceSelected(@NonNull Place place) {
                mMap.clear();

                final LatLng latLng = place.getLatLng();

                placeCoordinates.put(3, latLng);

                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.addMarker(new MarkerOptions().position(latLng).title("Marker at " + place.getName()));
                mMap.moveCamera(CameraUpdateFactory.zoomTo(13.0f));

                placeText.setText(place.getName());
            }
        });
        autocompleteFragment3.getView().findViewById(com.google.android.libraries.places.R.id.places_autocomplete_clear_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        placeCoordinates.remove(3);
                        autocompleteFragment3.setText("");
                        view.setVisibility(View.GONE);
                    }
                });
        View fView3 = autocompleteFragment3.getView();
        EditText etTextInput3 = fView3.findViewById(com.google.android.libraries.places.R.id.places_autocomplete_search_input);
        etTextInput3.setTextSize(16f);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        Button showResults = findViewById(R.id.showResults);
        showResults.setOnClickListener(this);

        placeText = findViewById(R.id.place_text);

        //get the spinner from the xml.
        Spinner dropdown = findViewById(R.id.spinner1);
        //create a list of items for the spinner.
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.unis, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int i, long id) {
                mMap.clear();

                if (i == 0) { // Melbourne CBD
                    LatLng CBD = new LatLng(-37.8136, 144.9631);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(CBD));
                    mMap.addMarker(new MarkerOptions().position(CBD).title("Marker at CBD"));
                    mMap.moveCamera(CameraUpdateFactory.zoomTo(10.0f));

                    String melbourne = "Melbourne CBD";
                    placeText.setText(melbourne);

                    showResults.setEnabled(false);

                    return;
                }

                String universityName = (String) dropdown.getItemAtPosition(i);
                placeText.setText(universityName);

                UniversityFactory universityFactory = new UniversityFactory();
                university = universityFactory.createUni(universityName);

                LatLng uniCoordinates = new LatLng((float) university.getLocation().getLatitude(),(float) university.getLocation().getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLng(uniCoordinates));

                mMap.addMarker(new MarkerOptions().position(uniCoordinates).title("Marker at " + uniCoordinates));
                mMap.moveCamera(CameraUpdateFactory.zoomTo(13.0f));

                showResults.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        this, R.raw.style_json));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.showResults:

                SeekBar seekbar1 = (SeekBar) findViewById(R.id.seekBar1);
                SeekBar seekbar2 = (SeekBar) findViewById(R.id.seekBar2);
                SeekBar seekbar3 = (SeekBar) findViewById(R.id.seekBar3);

                int priority1 = seekbar1.getProgress();
                int priority2 = seekbar2.getProgress();
                int priority3 = seekbar3.getProgress();

                Collection<LatLng> placesCoordinatesCollection = placeCoordinates.values();
                ArrayList<LatLng> placesCoordinatesArray = new ArrayList<>(placesCoordinatesCollection);

                results = university.getSuburbs(priority1, priority2, priority3, placesCoordinatesArray);

                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        MapsActivity.this, R.style.BottomSheetDialogTheme
                );
                View bottomSheetView = LayoutInflater.from(getApplicationContext())
                        .inflate(
                                R.layout.layout_bottom_sheet,
                                (LinearLayout) findViewById(R.id.bottomSheetContainer)
                        );

                result1Button = (Button) bottomSheetView.findViewById(R.id.result1);
                result1Button.setOnClickListener(this);
                result2Button = (Button) bottomSheetView.findViewById(R.id.result2);;
                result2Button.setOnClickListener(this);
                result3Button = (Button) bottomSheetView.findViewById(R.id.result3);
                result3Button.setOnClickListener(this);

                favourite1IV = (ImageView) bottomSheetView.findViewById(R.id.favourite1IV);
                favourite2IV = (ImageView) bottomSheetView.findViewById(R.id.favourite2IV);
                favourite3IV = (ImageView) bottomSheetView.findViewById(R.id.favourite3IV);

                result1Button.setText(capitalise(results.get(0).getName()));
                String result1Text = results.get(0).getName();
                if(favourites != null && favourites.contains(result1Text)) {
                    favourite1IV.setImageResource(R.drawable.ic_baseline_favorite_24);
                }
                result2Button.setText(capitalise(results.get(1).getName()));
                String result2Text = results.get(1).getName();
                if(favourites != null && favourites.contains(result2Text)) {
                    favourite2IV.setImageResource(R.drawable.ic_baseline_favorite_24);
                }
                result3Button.setText(capitalise(results.get(2).getName()));
                String result3Text = results.get(2).getName();
                if(favourites != null && favourites.contains(result3Text)) {
                    favourite3IV.setImageResource(R.drawable.ic_baseline_favorite_24);
                }

                final TextView result1Rent = (TextView) bottomSheetView.findViewById(R.id.result1Rent);
                String result1RentString = "Rent: $" + String.valueOf((int) results.get(0).getMedianWeeklyRent());
                result1Rent.setText(result1RentString);
                final TextView result1Crime = (TextView) bottomSheetView.findViewById(R.id.result1Crime);
                BigDecimal results1CrimeBD = new BigDecimal(results.get(0).getCrime() * 10);
                results1CrimeBD = results1CrimeBD.round(new MathContext(2)); // desired significant digits
                double rounded1Crime = results1CrimeBD.doubleValue();
                String result1CrimeString = "Crime: " + String.valueOf(rounded1Crime) + "/10";
                result1Crime.setText(result1CrimeString);
                final TextView result1Transport = (TextView) bottomSheetView.findViewById(R.id.result1Transport);
                BigDecimal results1TransportBD = new BigDecimal(results.get(0).getTransport() * 10);
                results1TransportBD = results1TransportBD.round(new MathContext(2)); // desired significant digits
                double rounded1Transport = results1TransportBD.doubleValue();
                String result1TransportString = "Transport: " + String.valueOf(rounded1Transport) + "/10";
                result1Transport.setText(result1TransportString);

                final TextView result2Rent = (TextView) bottomSheetView.findViewById(R.id.result2Rent);
                String result2RentString = "Rent: $" + String.valueOf((int) results.get(1).getMedianWeeklyRent());
                result2Rent.setText(result2RentString);
                final TextView result2Crime = (TextView) bottomSheetView.findViewById(R.id.result2Crime);
                BigDecimal results2CrimeBD = new BigDecimal(results.get(1).getCrime() * 10);
                results2CrimeBD = results2CrimeBD.round(new MathContext(2)); // desired significant digits
                double rounded2Crime = results2CrimeBD.doubleValue();
                String result2CrimeString = "Crime: " + String.valueOf(rounded2Crime) + "/10";
                result2Crime.setText(result2CrimeString);
                final TextView result2Transport = (TextView) bottomSheetView.findViewById(R.id.result2Transport);
                BigDecimal results2TransportBD = new BigDecimal(results.get(1).getTransport() * 10);
                results2TransportBD = results2TransportBD.round(new MathContext(2)); // desired significant digits
                double rounded2Transport = results2TransportBD.doubleValue();
                String result2TransportString = "Transport: " + String.valueOf(rounded2Transport) + "/10";
                result2Transport.setText(result2TransportString);

                final TextView result3Rent = (TextView) bottomSheetView.findViewById(R.id.result3Rent);
                String result3RentString = "Rent: $" + String.valueOf((int) results.get(2).getMedianWeeklyRent());
                result3Rent.setText(result3RentString);
                final TextView result3Crime = (TextView) bottomSheetView.findViewById(R.id.result3Crime);
                BigDecimal results3CrimeBD = new BigDecimal(results.get(2).getCrime() * 10);
                results3CrimeBD = results3CrimeBD.round(new MathContext(2)); // desired significant digits
                double rounded3Crime = results3CrimeBD.doubleValue();
                String result3CrimeString = "Crime: " + String.valueOf(rounded3Crime) + "/10";
                result3Crime.setText(result3CrimeString);
                final TextView result3Transport = (TextView) bottomSheetView.findViewById(R.id.result3Transport);
                BigDecimal results3TransportBD = new BigDecimal(results.get(2).getTransport() * 10);
                results3TransportBD = results3TransportBD.round(new MathContext(2)); // desired significant digits
                double rounded3Transport = results3TransportBD.doubleValue();
                String result3TransportString = "Transport: " + String.valueOf(rounded3Transport) + "/10";
                result3Transport.setText(result3TransportString);

                LinearLayout favourite1 = (LinearLayout) bottomSheetView.findViewById(R.id.favourite1);
                favourite1.setOnClickListener(this);
                LinearLayout favourite2 = (LinearLayout) bottomSheetView.findViewById(R.id.favourite2);
                favourite2.setOnClickListener(this);
                LinearLayout favourite3 = (LinearLayout) bottomSheetView.findViewById(R.id.favourite3);
                favourite3.setOnClickListener(this);

                bottomSheetDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();

            case R.id.result1:
                mMap.clear();

                double latitude1 = results.get(0).getLocation().getLatitude();
                double longitude1 = results.get(0).getLocation().getLongitude();
                LatLng latLng1 = new LatLng(latitude1, longitude1);

                String result1Name = capitalise(results.get(0).getName());

                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng1));
                mMap.addMarker(new MarkerOptions().position(latLng1).title("Marker at " + result1Name));
                mMap.moveCamera(CameraUpdateFactory.zoomTo(11.0f));

                placeText.setText(result1Name);
                break;

            case R.id.result2:

                mMap.clear();

                double latitude2 = results.get(1).getLocation().getLatitude();
                double longitude2 = results.get(1).getLocation().getLongitude();
                LatLng latLng2 = new LatLng(latitude2, longitude2);

                String result2Name = capitalise(results.get(1).getName());

                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng2));
                mMap.addMarker(new MarkerOptions().position(latLng2).title("Marker at " + result2Name));
                mMap.moveCamera(CameraUpdateFactory.zoomTo(11.0f));

                placeText.setText(result2Name);

                break;

            case R.id.result3:
                mMap.clear();

                double latitude3 = results.get(2).getLocation().getLatitude();
                double longitude3 = results.get(2).getLocation().getLongitude();
                LatLng latLng3 = new LatLng(latitude3, longitude3);

                String result3Name = capitalise(results.get(2).getName());

                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng3));
                mMap.addMarker(new MarkerOptions().position(latLng3).title("Marker at " + result3Name));
                mMap.moveCamera(CameraUpdateFactory.zoomTo(11.0f));

                placeText.setText(result3Name);

                break;

            case R.id.favourite1:
                result1Text = result1Button.getText().toString();
                if (favourites != null && favourites.contains(result1Text.toLowerCase())) {
                    favourites.remove(result1Text.toLowerCase());
                    reference.child("favourites").setValue(favourites);

                    favourite1IV.setImageResource(R.drawable.ic_baseline_favorite_border_24);

                    Toast.makeText(MapsActivity.this, "You removed " + result1Text + " from your favorites!", Toast.LENGTH_LONG).show();
                } else {
                    if(favourites == null) {
                        String[] favouritesList = {result1Text.toLowerCase()};
                        favourites = Arrays.asList(favouritesList);

                    } else {
                        ArrayList<String> favouritesArray = new ArrayList<>(favourites);
                        favouritesArray.add(result1Text.toLowerCase());
                        favourites = (List<String>) favouritesArray;
                    }
                    reference.child("favourites").setValue(favourites);

                    favourite1IV.setImageResource(R.drawable.ic_baseline_favorite_24);

                    Toast.makeText(MapsActivity.this, "You added " + result1Text + " to your favorites!", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.favourite2:
                result2Text = (String) result2Button.getText();
                if (favourites != null && favourites.contains(result2Text.toLowerCase())) {
                    favourites.remove(result2Text.toLowerCase());
                    reference.child("favourites").setValue(favourites);

                    favourite2IV.setImageResource(R.drawable.ic_baseline_favorite_border_24);

                    Toast.makeText(MapsActivity.this, "You removed " + result2Text + " from your favorites!", Toast.LENGTH_SHORT).show();
                } else {
                    if(favourites == null) {
                        String[] favouritesList = {result2Text.toLowerCase()};
                        favourites = Arrays.asList(favouritesList);

                    } else {
                        ArrayList<String> favouritesArray = new ArrayList<>(favourites);
                        favouritesArray.add(result2Text.toLowerCase());
                        favourites = (List<String>) favouritesArray;
                    }
                    reference.child("favourites").setValue(favourites);

                    favourite2IV.setImageResource(R.drawable.ic_baseline_favorite_24);

                    Toast.makeText(MapsActivity.this, "You added " + result2Text + " to your favorites!", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.favourite3:
                result3Text = (String) result3Button.getText();
                if (favourites != null && favourites.contains(result3Text.toLowerCase())) {
                    favourites.remove(result3Text.toLowerCase());
                    reference.child("favourites").setValue(favourites);

                    favourite3IV.setImageResource(R.drawable.ic_baseline_favorite_border_24);

                    Toast.makeText(MapsActivity.this, "You removed " + result3Text + " from your favorites!", Toast.LENGTH_SHORT).show();
                } else {
                    if(favourites == null) {
                        String[] favouritesList = {result3Text.toLowerCase()};
                        favourites = Arrays.asList(favouritesList);

                    } else {
                        ArrayList<String> favouritesArray = new ArrayList<>(favourites);
                        favouritesArray.add(result3Text.toLowerCase());
                        favourites = (List<String>) favouritesArray;
                    }
                    reference.child("favourites").setValue(favourites);

                    favourite3IV.setImageResource(R.drawable.ic_baseline_favorite_24);

                    Toast.makeText(MapsActivity.this, "You added " + result3Text + " to your favorites!", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    static String capitalise(String name) {
        List<String> nameWords = new ArrayList<String>(Arrays.asList(name.split(" ")));
        String nameCapitalised = "";
        for(String word: nameWords) {
            nameCapitalised += word.substring(0,1).toUpperCase(Locale.ROOT) + word.substring(1,word.length());
            if(word != nameWords.get(nameWords.size()-1)){
                nameCapitalised += ' ';
            }
        }
        return nameCapitalised;
    }
}