package ir.bppir.allin4sat.models;

import android.widget.RadioButton;

public class MD_RandomRadio {

    private RadioButton[] radioButton;

    public MD_RandomRadio(RadioButton[] radioButton) {
        this.radioButton = radioButton;
    }

    public RadioButton[] getRadioButton() {
        return radioButton;
    }

    public void setRadioButton(RadioButton[] radioButton) {
        this.radioButton = radioButton;
    }
}
