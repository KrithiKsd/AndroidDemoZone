package com.example.inclass_two;
/*a. Assignment 2.
  b. File Name : DiscountActivity.java
  c. Full name of the student1: Krithika Kasaragod
 */

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class DiscountActivity extends AppCompatActivity {


    TextView tvDiscountPrice;
    EditText etTicketPrice;
    RadioGroup rgDiscount;
    Button calculate, btnClear;
    final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount);
        tvDiscountPrice = findViewById(R.id.tvCalculatedPrice);
        btnClear = findViewById(R.id.btnClear);

        etTicketPrice = findViewById(R.id.etTicketPrice);
        rgDiscount = findViewById(R.id.radioDiscount);
        calculate = findViewById(R.id.btnCalculate);


        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    if (etTicketPrice.getText().toString().equals("")) {
                        throw new Exception();
                    } else {

                        double ticketPrice = Double.valueOf(etTicketPrice.getText().toString());
                        double discountedPrice = 0.0;
                        int checkID = rgDiscount.getCheckedRadioButtonId();
                        Log.d(TAG, "onClick: radio " + checkID);

                        if (checkID < 0) {
                            Toast.makeText(DiscountActivity.this, getString(R.string.label_error_radio_msg), Toast.LENGTH_SHORT).show();

                        } else {

                            if (checkID == R.id.radioFive) {
                                discountedPrice = ticketPrice - (ticketPrice * 0.05);

                            } else if (checkID == R.id.radioTen) {
                                discountedPrice = ticketPrice - (ticketPrice * 0.10);

                            } else if (checkID == R.id.radioFifteen) {
                                discountedPrice = ticketPrice - (ticketPrice * 0.15);

                            } else if (checkID == R.id.radioTwenty) {
                                discountedPrice = ticketPrice - (ticketPrice * 0.20);

                            } else if (checkID == R.id.radioFifty) {
                                discountedPrice = ticketPrice - (ticketPrice * 0.50);

                            }

                            String discount = String.format("%.2f", discountedPrice);
                            tvDiscountPrice.setText(discount);

                            Log.d(TAG, "onClick: " + discountedPrice);
                        }
                    }

                } catch (Exception e) {
                    Toast.makeText(DiscountActivity.this, getString(R.string.label_error_msg), Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etTicketPrice.setText("");
                tvDiscountPrice.setText("");
                rgDiscount.clearCheck();
            }
        });

    }
}