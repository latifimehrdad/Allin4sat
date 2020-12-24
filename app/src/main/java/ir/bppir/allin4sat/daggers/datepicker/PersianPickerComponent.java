package ir.bppir.allin4sat.daggers.datepicker;

import dagger.Component;
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog;

@Component(modules = {PersianPickerModule.class})
public interface PersianPickerComponent {
    PersianDatePickerDialog getPersianDatePickerDialog();
}