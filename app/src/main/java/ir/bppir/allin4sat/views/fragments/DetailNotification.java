package ir.bppir.allin4sat.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import org.jetbrains.annotations.NotNull;

import butterknife.ButterKnife;
import ir.bppir.allin4sat.R;
import ir.bppir.allin4sat.databinding.NotificationDetailBinding;
import ir.bppir.allin4sat.databinding.NotificationsBinding;
import ir.bppir.allin4sat.viewmodels.fragments.VM_NotificationDetail;

public class DetailNotification extends FragmentPrimary implements FragmentPrimary.actionFromObservable {


    private VM_NotificationDetail vm_notificationDetail;


    //______________________________________________________________________________________________ DetailNotification
    public DetailNotification() {
    }
    //______________________________________________________________________________________________ DetailNotification




    //______________________________________________________________________________________________ onCreateView
    @Nullable
    @Override
    public View onCreateView(
            @NotNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        if (getView() == null) {
            vm_notificationDetail = new VM_NotificationDetail(getActivity());
            NotificationDetailBinding binding = DataBindingUtil.inflate(
                    inflater, R.layout.notification_detail, container, false);
            binding.setDetail(vm_notificationDetail);
            setView(binding.getRoot());
            ButterKnife.bind(this, getView());
        }
        return getView();
    }
    //______________________________________________________________________________________________ onCreateView


    //______________________________________________________________________________________________ onStart
    @Override
    public void onStart() {
        super.onStart();
        init();
    }
    //______________________________________________________________________________________________ onStart


    //______________________________________________________________________________________________ init
    private void init() {
        setObservableForGetAction(
                DetailNotification.this,
                vm_notificationDetail.getPublishSubject(),
                vm_notificationDetail);

        String title = getArguments().getString(getContext().getResources().getString(R.string.ML_Title));
        String description = getArguments().getString(getContext().getResources().getString(R.string.ML_Description));
        String url = getArguments().getString(getContext().getResources().getString(R.string.ML_ImageUrl));
        String date = getArguments().getString(getContext().getResources().getString(R.string.ML_Date));
        vm_notificationDetail.setValue(title,description,url,date);

    }
    //______________________________________________________________________________________________ init


    //______________________________________________________________________________________________ getMessageFromObservable
    @Override
    public void getActionFromObservable(Byte action) {

    }
    //______________________________________________________________________________________________ getMessageFromObservable



    //______________________________________________________________________________________________ actionWhenFailureRequest
    @Override
    public void getActionWhenFailureRequest() {

    }
    //______________________________________________________________________________________________ actionWhenFailureRequest



}
