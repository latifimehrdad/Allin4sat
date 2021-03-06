package ir.bppir.allin4sat.viewmodels.fragments;

import android.app.Activity;

import java.util.List;

import ir.bppir.allin4sat.models.MD_Policy;
import ir.bppir.allin4sat.models.MR_Policy;
import ir.bppir.allin4sat.utility.StaticValues;
import ir.bppir.allin4sat.viewmodels.VM_Primary;
import ir.bppir.allin4sat.views.application.PishtazanApplication;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VM_PolicyList extends VM_Primary {

    private List<MD_Policy> md_policies;

    //______________________________________________________________________________________________ VM_PolicyList
    public VM_PolicyList(Activity context) {
        setContext(context);
    }
    //______________________________________________________________________________________________ VM_PolicyList


    //______________________________________________________________________________________________ getAllPolicies
    public void getAllPolicies(Integer CustomerId, Byte PolicyStatus) {

        Integer UserInfoId = getUserId();
        if (UserInfoId == 0) {
            userIsNotAuthorization();
            return;
        }

        setPrimaryCall(PishtazanApplication
                .getApplication(getContext())
                .getRetrofitComponent()
                .getRetrofitApiInterface()
                .GET_ALL_POLICIES(
                        UserInfoId,
                        CustomerId,
                        PolicyStatus,
                        false));


        getPrimaryCall().enqueue(new Callback<MR_Policy>() {
            @Override
            public void onResponse(Call<MR_Policy> call, Response<MR_Policy> response) {
                if (responseIsOk(response)) {
                    setResponseMessage(response.body().getMessage());
                    if (response.body().getStatue() == 0)
                        sendMessageToObservable(StaticValues.ML_ResponseError);
                    else {
                        md_policies = response.body().getPolicies();
                        sendMessageToObservable(StaticValues.ML_GetAllPolicy);
                    }
                }
            }

            @Override
            public void onFailure(Call<MR_Policy> call, Throwable t) {
                callIsFailure();
            }
        });

    }
    //______________________________________________________________________________________________ getAllPolicies





    //______________________________________________________________________________________________ getAllPoliciesColleague
    public void getAllPoliciesColleague(Integer ColleagueId, Byte PolicyStatus) {

        Integer UserInfoId = getUserId();
        if (UserInfoId == 0) {
            userIsNotAuthorization();
            return;
        }

        setPrimaryCall(PishtazanApplication
                .getApplication(getContext())
                .getRetrofitComponent()
                .getRetrofitApiInterface()
                .GET_ALL_POLICIES_Colleague(
                        UserInfoId,
                        ColleagueId,
                        PolicyStatus,
                        false));


        getPrimaryCall().enqueue(new Callback<MR_Policy>() {
            @Override
            public void onResponse(Call<MR_Policy> call, Response<MR_Policy> response) {
                if (responseIsOk(response)) {
                    setResponseMessage(response.body().getMessage());
                    if (response.body().getStatue() == 0)
                        sendMessageToObservable(StaticValues.ML_ResponseError);
                    else {
                        md_policies = response.body().getPolicies();
                        sendMessageToObservable(StaticValues.ML_GetAllPolicy);
                    }
                }
            }

            @Override
            public void onFailure(Call<MR_Policy> call, Throwable t) {
                callIsFailure();
            }
        });

    }
    //______________________________________________________________________________________________ getAllPoliciesColleague




    //______________________________________________________________________________________________ getMd_policies
    public List<MD_Policy> getMd_policies() {
        return md_policies;
    }
    //______________________________________________________________________________________________ getMd_policies

}
