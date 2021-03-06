package ir.bppir.allin4sat.viewmodels.fragments;

import android.app.Activity;

import java.util.List;

import ir.bppir.allin4sat.models.MD_ExamResultDetail;
import ir.bppir.allin4sat.models.MR_ExamResultDetail;
import ir.bppir.allin4sat.utility.StaticValues;
import ir.bppir.allin4sat.viewmodels.VM_Primary;
import ir.bppir.allin4sat.views.application.PishtazanApplication;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VM_ExamResultDetail extends VM_Primary {

    private List<MD_ExamResultDetail> md_examResultDetails;

    //______________________________________________________________________________________________ VM_ExamResultDetail
    public VM_ExamResultDetail(Activity context) {
        setContext(context);
    }
    //______________________________________________________________________________________________ VM_ExamResultDetail


    //______________________________________________________________________________________________ getExamResultDetails
    public void getExamResultDetails(Integer id) {

        Integer UserInfoId = getUserId();
        if (UserInfoId == 0) {
            userIsNotAuthorization();
            return;
        }

        setPrimaryCall(PishtazanApplication
                .getApplication(getContext())
                .getRetrofitComponent()
                .getRetrofitApiInterface()
                .GET_EXAM_RESULT_DETAILS(id, UserInfoId));

        getPrimaryCall().enqueue(new Callback<MR_ExamResultDetail>() {
            @Override
            public void onResponse(Call<MR_ExamResultDetail> call, Response<MR_ExamResultDetail> response) {
                if (responseIsOk(response)) {
                    setResponseMessage(response.body().getMessage());
                    if (response.body().getStatue() == 1) {
                        md_examResultDetails = response.body().getAnswers();
                        getPublishSubject().onNext(StaticValues.ML_GetExamResultDetail);
                    } else {
                        getPublishSubject().onNext(StaticValues.ML_ResponseError);
                    }
                }
            }

            @Override
            public void onFailure(Call<MR_ExamResultDetail> call, Throwable t) {
                callIsFailure();
            }
        });

    }
    //______________________________________________________________________________________________ getExamResultDetails


    //______________________________________________________________________________________________ getMd_examResultDetails
    public List<MD_ExamResultDetail> getMd_examResultDetails() {
        return md_examResultDetails;
    }
    //______________________________________________________________________________________________ getMd_examResultDetails
}
