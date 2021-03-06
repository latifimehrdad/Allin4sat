package ir.bppir.allin4sat.viewmodels.fragments;

import android.app.Activity;

import io.realm.Realm;
import io.realm.RealmResults;
import ir.bppir.allin4sat.daggers.retrofit.RetrofitComponent;
import ir.bppir.allin4sat.database.DB_UserInfo;
import ir.bppir.allin4sat.models.MR_GenerateCode;
import ir.bppir.allin4sat.models.MRVerifyCode;
import ir.bppir.allin4sat.models.MR_UserInfoVM;
import ir.bppir.allin4sat.utility.StaticValues;
import ir.bppir.allin4sat.viewmodels.VM_Primary;
import ir.bppir.allin4sat.views.activity.MainActivity;
import ir.bppir.allin4sat.views.application.PishtazanApplication;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static ir.bppir.allin4sat.daggers.retrofit.RetrofitApis.Host;

public class VM_Verify extends VM_Primary {

    public VM_Verify(Activity context) {//__________________________________________________________ VM_Verify
        setContext(context);
    }//_____________________________________________________________________________________________ VM_Verify


    public void sendNationalCode(String nationalCode) {//__________________________________________________ sendNationalCode

        nationalCode = PishtazanApplication
                .getApplication(getContext())
                .getApplicationUtilityComponent()
                .getApplicationUtility()
                .PersianToEnglish(nationalCode);


        RetrofitComponent retrofitComponent = PishtazanApplication
                .getApplication(getContext())
                .getRetrofitComponent();

        setPrimaryCall(retrofitComponent
                .getRetrofitApiInterface()
                .REQUEST_GENERATE_CODE_CALL(nationalCode));

        getPrimaryCall().enqueue(new Callback<MR_GenerateCode>() {
            @Override
            public void onResponse(Call<MR_GenerateCode> call, Response<MR_GenerateCode> response) {
                if (responseIsOk(response)) {
                    setResponseMessage(response.body().getMessage());
                    if (response.body().getStatue() == 1)
                        getPublishSubject().onNext(StaticValues.ML_ReTrySensSms);
                    else
                        getPublishSubject().onNext(StaticValues.ML_ResponseError);
                }
            }

            @Override
            public void onFailure(Call<MR_GenerateCode> call, Throwable t) {
                callIsFailure();
            }
        });

    }//_____________________________________________________________________________________________ sendNationalCode


    public void verifyNationalCode(String nationalCode, String VerifyCode) {//_____________________________ verifyNationalCode
        nationalCode = PishtazanApplication
                .getApplication(getContext())
                .getApplicationUtilityComponent()
                .getApplicationUtility()
                .PersianToEnglish(nationalCode);

        VerifyCode = PishtazanApplication
                .getApplication(getContext())
                .getApplicationUtilityComponent()
                .getApplicationUtility()
                .PersianToEnglish(VerifyCode);

        RetrofitComponent retrofitComponent = PishtazanApplication
                .getApplication(getContext())
                .getRetrofitComponent();

        setPrimaryCall(retrofitComponent
                .getRetrofitApiInterface()
                .REQUEST_VERIFY_CODE_CALL(nationalCode, getFirebaseToken(), VerifyCode));

        getPrimaryCall().enqueue(new Callback<MRVerifyCode>() {
            @Override
            public void onResponse(Call<MRVerifyCode> call, Response<MRVerifyCode> response) {
                if (responseIsOk(response)) {
                    setResponseMessage(response.body().getMessage());
                    if (response.body().getStatue() == 1)
                        SaveLogin(response.body());
                    else
                        getPublishSubject().onNext(StaticValues.ML_ResponseError);
                }
            }

            @Override
            public void onFailure(Call<MRVerifyCode> call, Throwable t) {
                callIsFailure();
            }
        });

    }//_____________________________________________________________________________________________ verifyNationalCode


    private void SaveLogin(MRVerifyCode md) {//_____________________________________________ SaveLogin
        Realm realm = Realm.getDefaultInstance();
        try {
            RealmResults<DB_UserInfo> delete = realm.where(DB_UserInfo.class).findAll();
            realm.beginTransaction();
            delete.deleteAllFromRealm();
            realm.copyToRealm(md.getUserInfo());
            realm.commitTransaction();
        } finally {
            if (realm != null)
                realm.close();
        }
        getUserInfoVM();
    }//_____________________________________________________________________________________________ SaveLogin



    //______________________________________________________________________________________________ getUserInfoVM
    public void getUserInfoVM() {

        Integer userInfoId = getUserId();
        if (userInfoId == 0) {
            userIsNotAuthorization();
            return;
        }

        String host = Host + "/api/GetUserInformation/" + userInfoId.toString();

        setPrimaryCall(PishtazanApplication
                .getApplication(getContext())
                .getRetrofitComponent()
                .getRetrofitApiInterface()
                .userSidebarInformation(host));

        if (getPrimaryCall() == null)
            return;

        getPrimaryCall().enqueue(new Callback<MR_UserInfoVM>() {
            @Override
            public void onResponse(Call<MR_UserInfoVM> call, Response<MR_UserInfoVM> response) {
                if (responseIsOk(response)) {
                    if (response.body() != null) {
                        if (response.body().getStatue() == 1) {
                            if (response.body().getUserInfoVM() != null) {
                                MainActivity.mainActivity.md_userInfoVM = response.body().getUserInfoVM();
                                getPublishSubject().onNext(StaticValues.ML_GotoHome);
                            } else
                                getPublishSubject().onNext(StaticValues.ML_ResponseError);
                        } else
                            getPublishSubject().onNext(StaticValues.ML_ResponseError);
                    } else
                        getPublishSubject().onNext(StaticValues.ML_ResponseError);
                }
            }

            @Override
            public void onFailure(Call<MR_UserInfoVM> call, Throwable t) {
                callIsFailure();
            }
        });

    }
    //______________________________________________________________________________________________ getUserInfoVM

}
