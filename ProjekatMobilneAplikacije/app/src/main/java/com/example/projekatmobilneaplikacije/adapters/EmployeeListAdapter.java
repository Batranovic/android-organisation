package com.example.projekatmobilneaplikacije.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.projekatmobilneaplikacije.R;
import com.example.projekatmobilneaplikacije.activities.employees.EmployeeProfileActivity;
import com.example.projekatmobilneaplikacije.model.Employee;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class EmployeeListAdapter extends ArrayAdapter<Employee> {
    private ArrayList<Employee> aEmployees;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Context mContext;

    public EmployeeListAdapter(Context context, ArrayList<Employee> employees){
        super(context, R.layout.employee_card, employees);
        aEmployees = employees;
        mContext = context;
    }

    @Override
    public int getCount() {
        return aEmployees.size();
    }

    /*
     * Ova metoda vraca pojedinacan element na osnovu pozicije
     * */
    @Nullable
    @Override
    public Employee getItem(int position) {
        return aEmployees.get(position);
    }

    /*
     * Ova metoda vraca jedinstveni identifikator, za adaptere koji prikazuju
     * listu ili niz, pozicija je dovoljno dobra da bude identifikator.
     * Naravno mozemo iskoristiti i jedinstveni identifikator objekta, ako on postoji.
     * */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /*
     * Ova metoda popunjava pojedinacan element ListView-a podacima.
     * Ako adapter cuva listu od n elemenata, adapter ce u petlji ici
     * onoliko puta koliko getCount() vrati. Prilikom svake iteracije
     * uzece java objekat sa odredjene pozicije (model) koji cuva podatke,
     * i layout koji treba da prikaze te podatke (view) npr R.layout.product_card.
     * Kada adapter ima model i view, prosto ce uzeti podatke iz modela,
     * popuniti view podacima i poslati listview da prikaze, i nastavice
     * sledecu iteraciju.
     * */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Employee employee = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.employee_card,
                    parent, false);
        }
        LinearLayout employeeCard = convertView.findViewById(R.id.employee_card_item);
        ImageView imageView = convertView.findViewById(R.id.employee_image);
        TextView employeeCommonName = convertView.findViewById(R.id.employee_common_name);
        TextView employeeEmail = convertView.findViewById(R.id.employee_email);

        loadImageFromBase64String(employee.getImage(), imageView);

        if(employee != null){
           // imageView.setImageResource(employee.getImage());
            employeeCommonName.setText(employee.getCommonName());
            employeeEmail.setText(employee.getEmail());
            employeeCard.setOnClickListener(v -> {
                // Handle click on the item at 'position'
                Log.i("App", "Clicked: " + employee.getCommonName() + ", id: " +
                        employee.getId());
                Toast.makeText(getContext(), "Clicked: " + employee.getCommonName() +
                        ", id: " + employee.getId(), Toast.LENGTH_SHORT).show();
            });
        }

        View itemView = convertView;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click event
                Intent intent = new Intent(v.getContext(), EmployeeProfileActivity.class);
                intent.putExtra("id", employee.getId());
                intent.putExtra("name", employee.getName());
                intent.putExtra("surname", employee.getSurname());
                intent.putExtra("commonName", employee.getCommonName());
                intent.putExtra("address", employee.getAddress());
                intent.putExtra("phoneNumber", employee.getPhoneNumber());
                intent.putExtra("image", employee.getImage());
                v.getContext().startActivity(intent);
            }
        });

        return convertView;
    }

    private void loadImageFromBase64String(String base64Image, ImageView imageView) {
        if (base64Image != null && !base64Image.isEmpty()) {
            // Dekodirajte Base64 string u byte[]
            byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);

            // Pretvorite byte[] u bitmapu
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            // Postavite bitmapu na ImageView
            imageView.setImageBitmap(decodedBitmap);
        } else {
            // Ako je base64Image null ili prazan, možete postaviti neku podrazumevanu sliku ili poruku
            // Na primer, postaviti ikonicu "slike nije dostupna"
            imageView.setImageResource(R.drawable.add_photo);
        }
    }

}
