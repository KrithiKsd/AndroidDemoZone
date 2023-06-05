package com.example.homework01;
/*
a. Assignment: Homework01
b. File Name: TipCalculatorActivity.java
c. Full name of the student1: Krithika Kasaragod
*/
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class TipCalculatorActivity extends AppCompatActivity {


    final String TAG = "TipCalculatorActivity";
    SeekBar seekBar;
    TextView tvSeekBarValue, tvTipValue, tvTotalValue, tvTP_Value;
    RadioGroup radioGroupTip, radioSplit;
    RadioButton rdCustom, rdTen, rdOne, rdFifteen, rdEighteen;
    EditText etBillTotal;
    Button btnClear;
    double tip = 0.0, tipTotal = 0.0, totalPerson = 0.0, tipProgressValue = 40;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip_calculator);
        seekBar = findViewById(R.id.seekBar);
        tvSeekBarValue = findViewById(R.id.tvProgress);
        radioGroupTip = findViewById(R.id.radioTip);
        rdCustom = findViewById(R.id.radioCustom);
        tvTipValue = findViewById(R.id.tvTipValue);
        tvTotalValue = findViewById(R.id.tvTotalValue);
        tvTP_Value = findViewById(R.id.tvTP_Value);
        etBillTotal = findViewById(R.id.etBillTotal);
        rdTen = findViewById(R.id.radioTen);
        radioSplit = findViewById(R.id.radioSplit);
        btnClear = findViewById(R.id.btnClear);
        rdOne = findViewById(R.id.radioOne);
        rdFifteen = findViewById(R.id.radioFifteen);
        rdEighteen = findViewById(R.id.radioEighteen);

        seekBar.setEnabled(false);

        etBillTotal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    if (etBillTotal.getText().toString().trim().equals("")) {
                        tvTipValue.setText(R.string.label_tip_value_def);
                        tvTotalValue.setText(R.string.label_total_def);
                        tvTP_Value.setText(R.string.label_total_def);
                        seekBar.setEnabled(false);
                        throw new Exception();
                    }

                    double billTotal = Double.parseDouble(String.valueOf(charSequence));
                    if (rdTen.isChecked()) {
                        if (charSequence.equals("")) {
                            throw new Exception();
                        } else {
                            tip = billTotal * 0.10;
                            tipTotal = billTotal + (billTotal * 0.10);
                            showCalculation(tip, tipTotal);
                        }
                    } else if (rdCustom.isChecked()) {
                        seekBar.setEnabled(true);
                        methodSeekBar(Double.parseDouble(charSequence.toString()));

                    } else if (rdEighteen.isChecked()) {
                        tip = billTotal * 0.18;
                        tipTotal = billTotal + (billTotal * 0.18);
                        showCalculation(tip, tipTotal);
                        seekBar.setEnabled(false);
                    } else if (rdFifteen.isChecked()) {
                        tip = billTotal * 0.15;
                        tipTotal = billTotal + (billTotal * 0.15);
                        showCalculation(tip, tipTotal);
                        seekBar.setEnabled(false);
                    }
                } catch (Exception e) {
                    Toast.makeText(TipCalculatorActivity.this, getString(R.string.label_error_msg), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        radioGroupTip.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                try {
                    double billTotal = Double.valueOf(etBillTotal.getText().toString());
                    tip = billTotal * 0.10;
                    tipTotal = billTotal + (billTotal * 0.10);
                    showCalculation(tip, tipTotal);

                    if (etBillTotal.getText().toString().equals("")) {
                        throw new Exception();
                    } else {
                        int checkID = radioGroupTip.getCheckedRadioButtonId();
                        if (checkID == R.id.radioTen) {
                            tip = billTotal * 0.10;
                            tipTotal = billTotal + (billTotal * 0.10);
                            showCalculation(tip, tipTotal);
                            seekBar.setEnabled(false);

                        } else if (checkID == R.id.radioFifteen) {
                            tip = billTotal * 0.15;
                            tipTotal = billTotal + (billTotal * 0.15);
                            showCalculation(tip, tipTotal);
                            seekBar.setEnabled(false);

                        } else if (checkID == R.id.radioEighteen) {
                            tip = billTotal * 0.18;
                            tipTotal = billTotal + (billTotal * 0.18);
                            showCalculation(tip, tipTotal);
                            seekBar.setEnabled(false);

                        } else if (checkID == R.id.radioCustom) {

                            seekBar.setEnabled(true);
                            tip = billTotal * (tipProgressValue / 100);
                            tipTotal = billTotal + (billTotal * (tipProgressValue / 100));
                            showCalculation(tip, tipTotal);

                            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                @Override
                                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                                    tvSeekBarValue.setText(i + getString(R.string.label_percent));
                                    tipProgressValue = i;
                                    tip = billTotal * (tipProgressValue / 100);
                                    tipTotal = billTotal + (billTotal * (tipProgressValue / 100));
                                    showCalculation(tip, tipTotal);
                                }

                                @Override
                                public void onStartTrackingTouch(SeekBar seekBar) {
                                    rdCustom.setChecked(true);
                                }

                                @Override
                                public void onStopTrackingTouch(SeekBar seekBar) {

                                }
                            });
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(TipCalculatorActivity.this, getString(R.string.label_error_msg), Toast.LENGTH_SHORT).show();
                }

            }
        });

        radioSplit.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                int checkID = radioSplit.getCheckedRadioButtonId();
                try {
                    if (etBillTotal.getText().toString().equals("")) {
                        throw new Exception();
                    } else {
                        if (checkID == R.id.radioOne) {
                            tvTP_Value.setText(String.format("%.2f", tipTotal));
                        } else if (checkID == R.id.radioTwo) {
                            totalPerson = (tipTotal / 2);
                            tvTP_Value.setText(String.format("%.2f", totalPerson));
                        } else if (checkID == R.id.radioThree) {
                            totalPerson = (tipTotal / 3);
                            tvTP_Value.setText(String.format("%.2f", totalPerson));

                        } else if (checkID == R.id.radioFour) {
                            totalPerson = (tipTotal / 4);
                            tvTP_Value.setText(String.format("%.2f", totalPerson));
                        }
                       // Log.d(TAG, "showCalculation: totalPerson"+totalPerson);
                    }
                } catch (Exception e) {
                    Toast.makeText(TipCalculatorActivity.this, getString(R.string.label_error_msg), Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etBillTotal.setText("");
                rdTen.setChecked(true);
                seekBar.setProgress(Integer.parseInt(getResources().getString(R.string.label_40)));
                seekBar.setEnabled(false);
                tvTipValue.setText(R.string.label_tip_value_def);
                tvTotalValue.setText(R.string.label_total_def);
                rdOne.setChecked(true);
                tvTP_Value.setText(R.string.label_total_def);
            }
        });
    }

    public void showCalculation(double tip, double tipTotal) {
        tvTipValue.setText(String.format("%.2f", tip));
        tvTotalValue.setText(String.format("%.2f", tipTotal));
        tvTP_Value.setText(String.format("%.2f", tipTotal));
        if(totalPerson>0.0){
            int checkID = radioSplit.getCheckedRadioButtonId();
            if (checkID == R.id.radioOne) {
                tvTP_Value.setText(String.format("%.2f", tipTotal));
            } else if (checkID == R.id.radioTwo) {
                totalPerson = (tipTotal / 2);
                tvTP_Value.setText(String.format("%.2f", totalPerson));
            } else if (checkID == R.id.radioThree) {
                totalPerson = (tipTotal / 3);
                tvTP_Value.setText(String.format("%.2f", totalPerson));

            } else if (checkID == R.id.radioFour) {
                totalPerson = (tipTotal / 4);
                tvTP_Value.setText(String.format("%.2f", totalPerson));
            }
        }
    }

    public void methodSeekBar(double billTotal) {
        tip = billTotal * (tipProgressValue / 100);
        tipTotal = billTotal + (billTotal * (tipProgressValue / 100));
        showCalculation(tip, tipTotal);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvSeekBarValue.setText(i + getString(R.string.label_percent));
                tipProgressValue = i;
                Log.d(TAG, "onCheckedChanged:Inside " + tipProgressValue);
                tip = billTotal * (tipProgressValue / 100);
                tipTotal = billTotal + (billTotal * (tipProgressValue / 100));
                showCalculation(tip, tipTotal);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}