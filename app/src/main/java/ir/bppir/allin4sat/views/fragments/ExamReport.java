package ir.bppir.allin4sat.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cunoraz.gifview.library.GifView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import ir.bppir.allin4sat.R;
import ir.bppir.allin4sat.databinding.FragmentExamReportBinding;
import ir.bppir.allin4sat.models.MD_Report;
import ir.bppir.allin4sat.models.MD_SpinnerItem;
import ir.bppir.allin4sat.utility.StaticValues;
import ir.bppir.allin4sat.viewmodels.fragments.VM_ExamReport;
import ir.bppir.allin4sat.views.adapters.AP_LearnReport;

public class ExamReport extends FragmentPrimary implements FragmentPrimary.actionFromObservable,
        AP_LearnReport.ClickItemDetail {


    private VM_ExamReport vm_examReport;
    private Integer rankOfCompanyId;
    private Integer sortPosition;
    private List<MD_Report> md_reports;
    private CompositeDisposable disposable = new CompositeDisposable();
    private NavController navController;


    @BindView(R.id.editTextSearch)
    EditText editTextSearch;

    @BindView(R.id.GifViewLoading)
    GifView GifViewLoading;

    @BindView(R.id.LinearLayoutFiltering)
    LinearLayout LinearLayoutFiltering;

    @BindView(R.id.LinearLayoutReport)
    LinearLayout LinearLayoutReport;

    @BindView(R.id.GifViewReport)
    GifView GifViewReport;

    @BindView(R.id.ImageViewReport)
    ImageView ImageViewReport;

    @BindView(R.id.MaterialSpinnerType)
    MaterialSpinner MaterialSpinnerType;

    @BindView(R.id.RelativeLayoutReport)
    RelativeLayout RelativeLayoutReport;

    @BindView(R.id.MaterialSpinnerSort)
    MaterialSpinner MaterialSpinnerSort;


    @BindView(R.id.RecyclerViewReport)
    RecyclerView RecyclerViewReport;


    //______________________________________________________________________________________________ onCreateView
    @Nullable
    @Override
    public View onCreateView(
            @NotNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        if (getView() == null) {
            FragmentExamReportBinding binding = DataBindingUtil.inflate(
                    inflater, R.layout.fragment_exam_report, container, false);
            vm_examReport = new VM_ExamReport(getActivity());
            binding.setReport(vm_examReport);
            setView(binding.getRoot());
            init();
        }
        return getView();
    }
    //______________________________________________________________________________________________ onCreateView


    //______________________________________________________________________________________________ onStart
    @Override
    public void onStart() {
        super.onStart();
        setObservableForGetAction(
                ExamReport.this,
                vm_examReport.getPublishSubject(),
                vm_examReport);
        assert getView() != null;
        navController = Navigation.findNavController(getView());
    }
    //______________________________________________________________________________________________ onStart


    //______________________________________________________________________________________________ init
    private void init() {

        disposable.add(RxTextView.textChangeEvents(editTextSearch)
                .skipInitialValue()
                .debounce(1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(searchContactsTextWatcher()));

        rankOfCompanyId = -1;
        sortPosition = -1;
        GifViewLoading.setVisibility(View.VISIBLE);
        LinearLayoutFiltering.setVisibility(View.GONE);
        LinearLayoutReport.setVisibility(View.GONE);
        setOnClick();
        vm_examReport.getRecourse();

    }
    //______________________________________________________________________________________________ init


    //______________________________________________________________________________________________ searchContactsTextWatcher
    private DisposableObserver<TextViewTextChangeEvent> searchContactsTextWatcher() {
        return new DisposableObserver<TextViewTextChangeEvent>() {
            @Override
            public void onNext(TextViewTextChangeEvent textViewTextChangeEvent) {
                searchName(textViewTextChangeEvent.text().toString());
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


    //______________________________________________________________________________________________ searchName
    private void searchName(String name) {

        md_reports = new ArrayList<>();
        for (MD_Report report : vm_examReport.getMd_reports()) {
            if (name == null || name.equalsIgnoreCase(""))
                md_reports.add(report);
            else {
                if (report.getFullName().contains(name))
                    md_reports.add(report);
            }
        }

        setReportAdapter();
    }
    //______________________________________________________________________________________________ searchName


    //______________________________________________________________________________________________ getActionFromObservable
    @Override
    public void getActionFromObservable(Byte action) {

        GifViewLoading.setVisibility(View.GONE);
        GifViewReport.setVisibility(View.GONE);
        ImageViewReport.setVisibility(View.VISIBLE);

        if (action.equals(StaticValues.ML_GetReport)) {
            LinearLayoutFiltering.setVisibility(View.GONE);
            LinearLayoutReport.setVisibility(View.VISIBLE);
            searchName(null);
            setReportAdapter();
            return;
        }

        if (action.equals(StaticValues.ML_GetRecourse)) {
            setMaterialSpinnerType();
        }

    }
    //______________________________________________________________________________________________ getActionFromObservable


    //______________________________________________________________________________________________ setOnClick
    private void setOnClick() {

        MaterialSpinnerType.setOnItemSelectedListener((view, position, id, item) -> {
            if (rankOfCompanyId == -1) {
                rankOfCompanyId = vm_examReport.getMd_spinnerItems().get(position - 1).getId();
                MaterialSpinnerType.getItems().remove(0);
                MaterialSpinnerType.setSelectedIndex(MaterialSpinnerType.getItems().size() - 1);
                MaterialSpinnerType.setSelectedIndex(position - 1);
            } else
                rankOfCompanyId = vm_examReport.getMd_spinnerItems().get(position).getId();

            if (getContext() != null) {
                MaterialSpinnerType.setBackgroundColor(getContext().getResources().getColor(R.color.ML_White));
            }

            setMaterialSpinnerSorting();

        });


        MaterialSpinnerSort.setOnItemSelectedListener((view, position, id, item) -> sortPosition = position);


        RelativeLayoutReport.setOnClickListener(v -> {

            if (rankOfCompanyId == -1)
                return;

            GifViewReport.setVisibility(View.VISIBLE);
            ImageViewReport.setVisibility(View.GONE);


            vm_examReport.getLearnReport(rankOfCompanyId, sortPosition);

        });

    }
    //______________________________________________________________________________________________ setOnClick


    //______________________________________________________________________________________________ setMaterialSpinnerType
    private void setMaterialSpinnerType() {
        List<String> buildingTypes = new ArrayList<>();
        assert getContext() != null;
        buildingTypes.add(getContext().getResources().getString(R.string.RankOfCompany));
        for (MD_SpinnerItem item : vm_examReport.getMd_spinnerItems())
            buildingTypes.add(item.getTitle());
        MaterialSpinnerType.setItems(buildingTypes);
        LinearLayoutFiltering.setVisibility(View.VISIBLE);
    }
    //______________________________________________________________________________________________ setMaterialSpinnerType


    //______________________________________________________________________________________________ setMaterialSpinnerSorting
    private void setMaterialSpinnerSorting() {

        List<String> sorting = new ArrayList<>();
        assert getContext() != null;
        sorting.add(getContext().getResources().getString(R.string.SortingNeither));
        sorting.add(getContext().getResources().getString(R.string.SortingAverageGrade));
        sorting.add(getContext().getResources().getString(R.string.SortingTotalScore));
        sorting.add(getContext().getResources().getString(R.string.SortingTotalActivity));
        sortPosition = 0;
        MaterialSpinnerSort.setItems(sorting);
    }
    //______________________________________________________________________________________________ setMaterialSpinnerSorting


    //______________________________________________________________________________________________ setReportAdapter
    private void setReportAdapter() {
        AP_LearnReport ap_report = new AP_LearnReport(md_reports, getContext(), ExamReport.this);
        RecyclerViewReport.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        RecyclerViewReport.setAdapter(ap_report);
    }
    //______________________________________________________________________________________________ setReportAdapter


    //______________________________________________________________________________________________ clickItemPerson
    @Override
    public void clickItemDetail(Integer Position) {
        Bundle bundle = new Bundle();
        assert getContext() != null;
        bundle.putInt(getContext().getResources().getString(R.string.ML_Id),
                vm_examReport.getMd_reports().get(Position).getId());
        bundle.putString(getContext().getResources().getString(R.string.ML_Type),
                getContext().getResources().getString(R.string.ML_MySubsetReport));
        navController.navigate(R.id.action_examReport_to_post, bundle);
    }
    //______________________________________________________________________________________________ clickItemPerson


    //______________________________________________________________________________________________ actionWhenFailureRequest
    @Override
    public void getActionWhenFailureRequest() {
    }
    //______________________________________________________________________________________________ actionWhenFailureRequest


}
