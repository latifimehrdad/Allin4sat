package ir.bppir.allin4sat.views.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cunoraz.gifview.library.GifView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import ir.bppir.allin4sat.R;
import ir.bppir.allin4sat.databinding.FragmentAddPersonBinding;
import ir.bppir.allin4sat.models.MD_Contact;
import ir.bppir.allin4sat.utility.StaticValues;
import ir.bppir.allin4sat.viewmodels.fragments.VM_AddPerson;
import ir.bppir.allin4sat.views.activity.MainActivity;
import ir.bppir.allin4sat.views.adapters.AP_Contact;

public class AddPerson extends FragmentPrimary implements FragmentPrimary.actionFromObservable {

    private int panelType;
    private VM_AddPerson vm_addPerson;
    private Dialog dialogContact;
    private AP_Contact ap_contact;
    private CompositeDisposable disposable = new CompositeDisposable();
    private List<MD_Contact> md_contacts;
    private RecyclerView recyclerViewContact;
    private Byte degree = -1;


    @BindView(R.id.RelativeLayoutAdd)
    RelativeLayout RelativeLayoutAdd;

    @BindView(R.id.ImageViewAdd)
    ImageView ImageViewAdd;

    @BindView(R.id.TextViewAdd)
    TextView TextViewAdd;

    @BindView(R.id.GifViewAdd)
    GifView GifViewAdd;

    @BindView(R.id.EditTextName)
    EditText EditTextName;

    @BindView(R.id.EditTextPhoneNumber)
    EditText EditTextPhoneNumber;

    @BindView(R.id.LinearLayoutCancel)
    LinearLayout LinearLayoutCancel;

    @BindView(R.id.LinearLayoutSave)
    LinearLayout LinearLayoutSave;

    @BindView(R.id.ImageViewSend)
    ImageView ImageViewSend;

    @BindView(R.id.GifViewSend)
    GifView GifViewSend;

    @BindView(R.id.TextViewSend)
    TextView TextViewSend;

    @BindView(R.id.LinearLayoutNormal)
    LinearLayout LinearLayoutNormal;

    @BindView(R.id.LinearLayoutPeach)
    LinearLayout LinearLayoutPeach;

    @BindView(R.id.LinearLayoutGiant)
    LinearLayout LinearLayoutGiant;

    //______________________________________________________________________________________________ AddPerson
    public AddPerson() {
    }
    //______________________________________________________________________________________________ AddPerson


    //______________________________________________________________________________________________ onCreateView
    @Nullable
    @Override
    public View onCreateView(
            @NotNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        if (getView() == null) {
            vm_addPerson = new VM_AddPerson(getActivity());
            FragmentAddPersonBinding binding = DataBindingUtil.inflate(
                    inflater, R.layout.fragment_add_person, container, false);
            binding.setAddPerson(vm_addPerson);
            setView(binding.getRoot());
            ButterKnife.bind(this, getView());
            setTextWatcher();
            setClick();
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
                AddPerson.this,
                vm_addPerson.getPublishSubject(),
                vm_addPerson);
        assert getArguments() != null;
        assert getContext() != null;
        panelType = getArguments().getInt(getContext().getString(R.string.ML_PanelType), StaticValues.Customer);
    }
    //______________________________________________________________________________________________ init


    //______________________________________________________________________________________________ getMessageFromObservable
    @Override
    public void getActionFromObservable(Byte action) {

        finishLoadingSend();
        finishWaiting();

        if (action.equals(StaticValues.ML_AddPerson)) {
            degree = -1;
            EditTextName.getText().clear();
            EditTextPhoneNumber.getText().clear();
            EditTextName.requestFocus();
            LinearLayoutGiant.setBackground(null);
            LinearLayoutNormal.setBackground(null);
            LinearLayoutPeach.setBackground(null);
            getContext().onBackPressed();
            return;
        }

        if (action.equals(StaticValues.ML_GetContact)) {
            md_contacts = vm_addPerson.getMd_contacts();
            if (ap_contact == null) {
                showContactDialog();
            } else {
                if (dialogContact == null || !dialogContact.isShowing())
                    showContactDialog();
                else {
                    setContactAdapter();
                }
            }
            return;
        }

        if (action.equals(StaticValues.ML_GetContactFilter)) {
            md_contacts = vm_addPerson.getMd_contactsFilter();
            setContactAdapter();
        }


    }
    //______________________________________________________________________________________________ getMessageFromObservable


    //______________________________________________________________________________________________ setClick
    private void setClick() {

        RelativeLayoutAdd.setOnClickListener(view -> {
            if (MainActivity.mainActivity.setPermission()) {
                hideKeyboard();
                showWaiting();
                Handler handler = new Handler();
                handler.postDelayed(() -> vm_addPerson.getContact(), 1000);
            }

        });

        LinearLayoutCancel.setOnClickListener(view -> {
            hideKeyboard();
            if (getContext() != null)
                getContext().onBackPressed();
        });


        LinearLayoutSave.setOnClickListener(view -> {
            String phone = EditTextPhoneNumber.getText().toString();
            phone = phone.replaceAll(" ", "");
            phone = phone.replaceAll("-", "");
            EditTextPhoneNumber.setText(phone);
            if (checkEmpty()) {
                hideKeyboard();
                showLoadingSend();
                vm_addPerson.addPerson(
                        EditTextName.getText().toString(),
                        EditTextPhoneNumber.getText().toString(),
                        degree,
                        panelType
                );
            }
        });


        LinearLayoutNormal.setOnClickListener(view -> {
            LinearLayoutPeach.setBackground(null);
            LinearLayoutGiant.setBackground(null);
            assert getContext() != null;
            LinearLayoutNormal.setBackground(getContext().getResources().getDrawable(R.drawable.dw_back_recycler));
            degree = StaticValues.DegreeNormal;
        });


        LinearLayoutPeach.setOnClickListener(view -> {
            LinearLayoutNormal.setBackground(null);
            LinearLayoutGiant.setBackground(null);
            assert getContext() != null;
            LinearLayoutPeach.setBackground(getContext().getResources().getDrawable(R.drawable.dw_back_recycler));
            degree = StaticValues.DegreePeach;
        });


        LinearLayoutGiant.setOnClickListener(view -> {
            LinearLayoutNormal.setBackground(null);
            LinearLayoutPeach.setBackground(null);
            assert getContext() != null;
            LinearLayoutGiant.setBackground(getContext().getResources().getDrawable(R.drawable.dw_back_recycler));
            degree = StaticValues.DegreeGiant;
        });

    }
    //______________________________________________________________________________________________ setClick


    //______________________________________________________________________________________________ setTextWatcher
    private void setTextWatcher() {
        EditTextPhoneNumber.addTextChangedListener(textChangeForChangeBack(EditTextPhoneNumber));
        EditTextName.addTextChangedListener(textChangeForChangeBack(EditTextName));
    }
    //______________________________________________________________________________________________ setTextWatcher


    //______________________________________________________________________________________________ showLoadingSend
    private void showLoadingSend() {
        ImageViewSend.setVisibility(View.GONE);
        GifViewSend.setVisibility(View.VISIBLE);
        assert getContext() != null;
        TextViewSend.setText(getContext().getResources().getString(R.string.PleaseWait));
    }
    //______________________________________________________________________________________________ showLoadingSend


    //______________________________________________________________________________________________ finishLoadingSend
    private void finishLoadingSend() {
        ImageViewSend.setVisibility(View.VISIBLE);
        GifViewSend.setVisibility(View.GONE);
        assert getContext() != null;
        TextViewSend.setText(getContext().getResources().getString(R.string.Save));
    }
    //______________________________________________________________________________________________ finishLoadingSend


    //______________________________________________________________________________________________ showWaiting
    private void showWaiting() {
        GifViewAdd.setVisibility(View.VISIBLE);
        ImageViewAdd.setVisibility(View.GONE);
        assert getContext() != null;
        TextViewAdd.setText(getContext().getString(R.string.PleaseWait));
        RelativeLayoutAdd.setBackground(getContext().getResources().getDrawable(R.drawable.dw_back_bottom_connection));
    }
    //______________________________________________________________________________________________ showWaiting


    //______________________________________________________________________________________________ finishWaiting
    private void finishWaiting() {
        GifViewAdd.setVisibility(View.GONE);
        ImageViewAdd.setVisibility(View.VISIBLE);
        assert getContext() != null;
        TextViewAdd.setText(getContext().getString(R.string.AddFromContact));
        RelativeLayoutAdd.setBackground(getContext().getResources().getDrawable(R.drawable.dw_back_bottom));
    }
    //______________________________________________________________________________________________ finishWaiting


    //______________________________________________________________________________________________ showContactDialog
    private void showContactDialog() {
        if (md_contacts == null)
            return;

        if (dialogContact != null)
            if (dialogContact.isShowing())
                dialogContact.dismiss();
        dialogContact = null;
        assert getContext() != null;
        dialogContact = new Dialog(getContext());
        dialogContact.setCancelable(true);
        dialogContact.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogContact.setContentView(R.layout.dialog_contact);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialogContact.getWindow();
        assert window != null;
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);

        recyclerViewContact = (RecyclerView)
                dialogContact.findViewById(R.id.RecyclerViewContact);

        md_contacts = vm_addPerson.getMd_contacts();
        setContactAdapter();

        LinearLayout LinearLayoutCancel = (LinearLayout)
                dialogContact.findViewById(R.id.LinearLayoutCancel);
        LinearLayoutCancel.setOnClickListener(view -> dialogContact.dismiss());

        EditText EditTextName = (EditText)
                dialogContact.findViewById(R.id.EditTextName);

        disposable.add(RxTextView.textChangeEvents(EditTextName)
                .skipInitialValue()
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(searchContactsTextWatcher()));

        dialogContact.show();
    }
    //______________________________________________________________________________________________ showContactDialog


    //______________________________________________________________________________________________ searchContactsTextWatcher
    private DisposableObserver<TextViewTextChangeEvent> searchContactsTextWatcher() {
        return new DisposableObserver<TextViewTextChangeEvent>() {
            @Override
            public void onNext(TextViewTextChangeEvent textViewTextChangeEvent) {
                hideKeyboard();
                String text = textViewTextChangeEvent.text().toString();
                vm_addPerson.filterContact(text);
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {

            }
        };
    }
    //______________________________________________________________________________________________ searchContactsTextWatcher


    //______________________________________________________________________________________________ setContactAdapter
    private void setContactAdapter() {
        ap_contact = new AP_Contact(md_contacts, AddPerson.this);
        recyclerViewContact.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerViewContact.setAdapter(ap_contact);
    }
    //______________________________________________________________________________________________ setContactAdapter


    //______________________________________________________________________________________________ clickContact
    public void clickContact(Integer position) {
        dialogContact.dismiss();
        dialogContact = null;
        ap_contact = null;
        hideKeyboard();
        String phone = md_contacts.get(position).getPhone();
        phone = phone.replaceAll(" ", "");
        phone = phone.replaceAll("-", "");
        EditTextPhoneNumber.setText(phone);
        EditTextName.setText(md_contacts.get(position).getName());
    }
    //______________________________________________________________________________________________ clickContact


    //______________________________________________________________________________________________ checkEmpty
    private Boolean checkEmpty() {

        boolean name = true;
        boolean mobile = true;

        if (degree == -1) {
            assert getContext() != null;
            showMessage(
                    getContext().getResources().getString(R.string.ChoosePersonDegree),
                    getResources().getColor(R.color.ML_Dialog),
                    getResources().getDrawable(R.drawable.ic_baseline_warning),
                    getResources().getColor(R.color.ML_Red));
            return false;
        }

        if (EditTextPhoneNumber.getText().length() < 11) {
            EditTextPhoneNumber.setBackgroundResource(R.drawable.dw_edit_empty_background);
            EditTextPhoneNumber.setError(getResources().getString(R.string.EmptyMobileNumber));
            EditTextPhoneNumber.requestFocus();
            mobile = false;
        }

        if (EditTextName.getText().length() == 0) {
            EditTextName.setBackgroundResource(R.drawable.dw_edit_empty_background);
            EditTextName.setError(getResources().getString(R.string.EmptyContactName));
            EditTextName.requestFocus();
            name = false;
        }

        return mobile && name;
    }
    //______________________________________________________________________________________________ checkEmpty


    //______________________________________________________________________________________________ actionWhenFailureRequest
    @Override
    public void getActionWhenFailureRequest() {
    }
    //______________________________________________________________________________________________ actionWhenFailureRequest


}
