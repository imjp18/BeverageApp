package com.example.jayeshkumarassignment1;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    final Calendar myCalender = Calendar.getInstance();
    private EditText date;
    private TextInputLayout customerName, customerEmail, customerPhone;
    private RadioGroup beverageType, beverageSize;
    private CheckBox milk, sugar;
    private Spinner flavourSpinner, stores;
    private AutoCompleteTextView regionAutoComplete;
    private Button submit;
    private ArrayAdapter<String> flavourAdapter, storeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);



        initializeUIComponents();
        setupListeners();
    }
   // initializing the ui components
    private void initializeUIComponents() {
        customerName = findViewById(R.id.customerName);
        customerEmail = findViewById(R.id.customerEmail);
        customerPhone = findViewById(R.id.customerPhone);
        beverageType = findViewById(R.id.beverageType);
        beverageSize = findViewById(R.id.beverageSize);
        milk = findViewById(R.id.milk);
        sugar = findViewById(R.id.sugar);
        flavourSpinner = findViewById(R.id.flavour);
        regionAutoComplete = findViewById(R.id.region);
        stores = findViewById(R.id.stores);
        date = findViewById(R.id.date);
        submit = findViewById(R.id.submit);

        beverageType.check(R.id.beverageType);

        flavourAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item);
        flavourSpinner.setAdapter(flavourAdapter);

        String[] regions = getResources().getStringArray(R.array.regions);
        ArrayAdapter<String> regionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, regions);
        regionAutoComplete.setAdapter(regionAdapter);

        storeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item);
        stores.setAdapter(storeAdapter);
    }
   // listeners for ui components
    @SuppressLint("SimpleDateFormat")
    private void setupListeners() {
        date.setOnClickListener(v -> new DatePickerDialog(MainActivity.this, (view, year, month, dayOfMonth) -> {
            myCalender.set(Calendar.YEAR, year);
            myCalender.set(Calendar.MONTH, month);
            myCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "dd-MMM-yyyy";
            date.setText(new SimpleDateFormat(myFormat).format(myCalender.getTime()));
        }, myCalender.get(Calendar.YEAR), myCalender.get(Calendar.MONTH), myCalender.get(Calendar.DAY_OF_MONTH)).show());

        beverageType.setOnCheckedChangeListener((group, checkedId) -> updateFlavourSpinner());

        regionAutoComplete.setOnItemClickListener((parent, view, position, id) -> updateStoreSpinner());

        submit.setOnClickListener(v -> {
            if (validateInputs()) {
                displayUserReceipt();
            }
        });
    }
   // it updates the flavour spinner for beverage type
    private void updateFlavourSpinner() {
        int selectedBeverageId = beverageType.getCheckedRadioButtonId();
        String[] flavours;
        if (selectedBeverageId == R.id.TOC) {
            flavours = getResources().getStringArray(R.array.coffee_flavours);
        } else {
            flavours = getResources().getStringArray(R.array.tea_flavours);
        }
        flavourAdapter.clear();
        flavourAdapter.addAll(flavours);
        flavourAdapter.notifyDataSetChanged();
    }
   //this method is used to update the store spinner based on the selected region
    private void updateStoreSpinner() {
        String selectedRegion = regionAutoComplete.getText().toString();
        String[] stores;
        switch (selectedRegion) {
            case "Waterloo":
                stores = getResources().getStringArray(R.array.Waterloo);
                break;
            case "London":
                stores = getResources().getStringArray(R.array.London);
                break;
            case "Milton":
                stores = getResources().getStringArray(R.array.Milton);
                break;
            case "Mississauga":
                stores = getResources().getStringArray(R.array.Mississauga);
                break;
            default:
                stores = new String[]{};
                break;
        }
        storeAdapter.clear();
        storeAdapter.addAll(Arrays.asList(stores));
        storeAdapter.notifyDataSetChanged();
    }

    // this method displays the user receipt if validation is successful
    private void displayUserReceipt() {
        String name = Objects.requireNonNull(customerName.getEditText()).getText().toString();
        String email = Objects.requireNonNull(customerEmail.getEditText()).getText().toString();
        String phone = Objects.requireNonNull(customerPhone.getEditText()).getText().toString();
        String region = regionAutoComplete.getText().toString();
        String store = (String) stores.getSelectedItem();

        int selectedBeverageId = beverageType.getCheckedRadioButtonId();
        String beverageType = (selectedBeverageId == R.id.TOC) ? "Coffee" : "Tea";
        boolean addMilk = milk.isChecked();
        boolean addSugar = sugar.isChecked();
        String flavour = (String) flavourSpinner.getSelectedItem();
        String size = null;

        int selectedSizeId = beverageSize.getCheckedRadioButtonId();
        if (selectedSizeId == R.id.BSSmall) {
            size = "Small";
        } else if (selectedSizeId == R.id.BSMedium) {
            size = "Medium";
        } else if (selectedSizeId == R.id.BSLarge) {
            size = "Large";
        }

        BeverageOrder order = new BeverageOrder(name, email, phone, region, store, beverageType, size, addMilk, addSugar, flavour, date.getText().toString());
        String receipt = order.getReceipt();

        new AlertDialog.Builder(this)
                .setTitle("Receipt")
                .setMessage(receipt)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> Toast.makeText(MainActivity.this, "Thank you for your order!", Toast.LENGTH_SHORT).show())
                .show();
    }

    // this method is used to validate the inputs entered by the user
    private boolean validateInputs() {
        boolean isValid = true;

        String name = Objects.requireNonNull(customerName.getEditText()).getText().toString();
        if (TextUtils.isEmpty(name)) {
            customerName.setError("Name is required");
            isValid = false;
        } else {
            customerName.setError(null);
        }

        String email = Objects.requireNonNull(customerEmail.getEditText()).getText().toString();
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            customerEmail.setError("Valid email is required");
            isValid = false;
        } else {
            customerEmail.setError(null);
        }

        String phone = Objects.requireNonNull(customerPhone.getEditText()).getText().toString();
        if (TextUtils.isEmpty(phone) || !Patterns.PHONE.matcher(phone).matches()) {
            customerPhone.setError("Valid phone number is required");
            isValid = false;
        } else {
            customerPhone.setError(null);
        }

        String region = regionAutoComplete.getText().toString();
        if (TextUtils.isEmpty(region)) {
            regionAutoComplete.setError("Region is required");
            isValid = false;
        } else {
            regionAutoComplete.setError(null);
        }

        String store = (String) stores.getSelectedItem();
        if (TextUtils.isEmpty(store)) {
            Toast.makeText(this, "Store is required", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        int selectedSizeId = beverageSize.getCheckedRadioButtonId();
        if (selectedSizeId == -1) {
            Toast.makeText(this, "Beverage size is required", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        String dateText = date.getText().toString();
        if (TextUtils.isEmpty(dateText)) {
            date.setError("Date is required");
            isValid = false;
        } else {
            date.setError(null);
        }
        return isValid;
    }
}
