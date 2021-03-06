package ir.bppir.allin4sat.viewmodels.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Handler;

import java.io.File;

import ir.bppir.allin4sat.R;
import ir.bppir.allin4sat.models.MD_GetAddres;
import ir.bppir.allin4sat.models.MD_Person;
import ir.bppir.allin4sat.models.MD_UserInfo;
import ir.bppir.allin4sat.models.MR_GetAllPerson;
import ir.bppir.allin4sat.models.MR_Primary;
import ir.bppir.allin4sat.models.MR_UserInfoVM;
import ir.bppir.allin4sat.utility.StaticValues;
import ir.bppir.allin4sat.viewmodels.VM_Primary;
import ir.bppir.allin4sat.views.activity.MainActivity;
import ir.bppir.allin4sat.views.application.PishtazanApplication;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static ir.bppir.allin4sat.daggers.retrofit.RetrofitApis.Host;

public class VM_EditPerson extends VM_Primary {

    private MD_GetAddres address;
    private String AddressString;
    private MD_Person person;

    //______________________________________________________________________________________________ VM_EditPerson
    public VM_EditPerson(Activity context) {
        setContext(context);
    }
    //______________________________________________________________________________________________ VM_EditPerson


    //______________________________________________________________________________________________ editProfile
    public void editProfile(
            Byte panelId,
            Integer eId,
            String eFullName,
            String eMobileNumber,
            Byte eLevel,
            String ePhoneNumber,
            String eBirthDateJ,
            String eAddress,
            String eLat,
            String eLang,
            String eNationalCode,
            boolean birthDay,
            String eDescription) {

        if (eBirthDateJ != null)
            eBirthDateJ = PishtazanApplication
                    .getApplication(getContext())
                    .getApplicationUtilityComponent()
                    .getApplicationUtility()
                    .PersianToEnglish(eBirthDateJ);
        else eBirthDateJ = "";

        if (panelId.equals(StaticValues.Customer))
            editCustomerProfile(eId, eFullName, eMobileNumber, eLevel, ePhoneNumber, eBirthDateJ, eAddress, eLat, eLang, eNationalCode, birthDay);
        else if (panelId.equals(StaticValues.Colleague))
            editColleagueProfile(eId, eFullName, eMobileNumber, eLevel, ePhoneNumber, eBirthDateJ, eAddress, eLat, eLang, eNationalCode, birthDay);
        else editUserProfile(getColleagueId(), eFullName,ePhoneNumber,eBirthDateJ,eAddress,eLat,eLang,eMobileNumber,eDescription);
    }
    //______________________________________________________________________________________________ editProfile


    //______________________________________________________________________________________________ editCustomerProfile
    public void editCustomerProfile(
            Integer eId,
            String eFullName,
            String eMobileNumber,
            Byte eLevel,
            String ePhoneNumber,
            String eBirthDateJ,
            String eAddress,
            String eLat,
            String eLang,
            String eNationalCode,
            boolean birthDay) {

        eMobileNumber = PishtazanApplication
                .getApplication(getContext())
                .getApplicationUtilityComponent()
                .getApplicationUtility()
                .PersianToEnglish(eMobileNumber);

        Integer eColleagueId = getColleagueId();
        if (eColleagueId == 0) {
            userIsNotAuthorization();
            return;
        }

        Uri destination = Uri.fromFile(new File(getContext().getExternalCacheDir(), "cropped.jpg"));
        File file = new File(destination.getPath());
        MultipartBody.Part Image = null;

        if (file.exists())
            Image = MultipartBody
                    .Part
                    .createFormData("Image", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));

        RequestBody Id = RequestBody.create(MediaType.parse("multipart/form-data"), eId.toString());
        RequestBody FullName = RequestBody.create(MediaType.parse("multipart/form-data"), eFullName);
        RequestBody MobileNumber = RequestBody.create(MediaType.parse("multipart/form-data"), eMobileNumber);
        RequestBody ColleagueId = RequestBody.create(MediaType.parse("multipart/form-data"), eColleagueId.toString());
        RequestBody Level = RequestBody.create(MediaType.parse("multipart/form-data"), eLevel.toString());
        RequestBody PhoneNumber = RequestBody.create(MediaType.parse("multipart/form-data"), ePhoneNumber);
        RequestBody BirthDateJ = RequestBody.create(MediaType.parse("multipart/form-data"), eBirthDateJ);
        RequestBody Address = RequestBody.create(MediaType.parse("multipart/form-data"), eAddress);
        RequestBody Lat = RequestBody.create(MediaType.parse("multipart/form-data"), eLat);
        RequestBody Lang = RequestBody.create(MediaType.parse("multipart/form-data"), eLang);
        RequestBody NationalCode = RequestBody.create(MediaType.parse("multipart/form-data"), eNationalCode);
        RequestBody BirthDay = RequestBody.create(MediaType.parse("multipart/form-data"), "0");
        if (birthDay)
            BirthDay = RequestBody.create(MediaType.parse("multipart/form-data"), "1");
//        RequestBody BirthDay = RequestBody.create(MediaType.parse("text/plain"), Boolean.TRUE.toString());

        setPrimaryCall(PishtazanApplication
                .getApplication(getContext())
                .getRetrofitComponent()
                .getRetrofitApiInterface()
                .EDIT_CUSTOMER(
                        Image,
                        Id,
                        FullName,
                        PhoneNumber,
                        MobileNumber,
                        BirthDateJ,
                        Address,
                        Lat,
                        Lang,
                        ColleagueId,
                        NationalCode,
                        Level,
                        BirthDay));

        getPrimaryCall().enqueue(new Callback<MR_Primary>() {
            @Override
            public void onResponse(Call<MR_Primary> call, Response<MR_Primary> response) {
                if (responseIsOk(response)) {
                    setResponseMessage(response.body().getMessage());
                    if (response.body().getStatue() == 1)
                        getPublishSubject().onNext(StaticValues.ML_EditSuccess);
                    else
                        getPublishSubject().onNext(StaticValues.ML_ResponseError);
                }
            }

            @Override
            public void onFailure(Call<MR_Primary> call, Throwable t) {
                callIsFailure();
            }
        });

    }
    //______________________________________________________________________________________________ editCustomerProfile


    //______________________________________________________________________________________________ editColleagueProfile
    public void editColleagueProfile(
            Integer eId,
            String eFullName,
            String eMobileNumber,
            Byte eLevel,
            String ePhoneNumber,
            String eBirthDateJ,
            String eAddress,
            String eLat,
            String eLang,
            String eNationalCode,
            boolean birthDay) {

        eMobileNumber = PishtazanApplication
                .getApplication(getContext())
                .getApplicationUtilityComponent()
                .getApplicationUtility()
                .PersianToEnglish(eMobileNumber);

        Integer eUserInfoId = getUserId();
        if (eUserInfoId == 0) {
            userIsNotAuthorization();
            return;
        }

        Uri destination = Uri.fromFile(new File(getContext().getExternalCacheDir(), "cropped.jpg"));
        File file = new File(destination.getPath());
        MultipartBody.Part Image = null;

        if (file.exists())
            Image = MultipartBody
                    .Part
                    .createFormData("Image", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));

        RequestBody Id = RequestBody.create(MediaType.parse("multipart/form-data"), eId.toString());
        RequestBody FullName = RequestBody.create(MediaType.parse("multipart/form-data"), eFullName);
        RequestBody MobileNumber = RequestBody.create(MediaType.parse("multipart/form-data"), eMobileNumber);
        RequestBody UserInfoId = RequestBody.create(MediaType.parse("multipart/form-data"), eUserInfoId.toString());
        RequestBody Level = RequestBody.create(MediaType.parse("multipart/form-data"), eLevel.toString());
        RequestBody PhoneNumber = RequestBody.create(MediaType.parse("multipart/form-data"), ePhoneNumber);
        RequestBody BirthDateJ = RequestBody.create(MediaType.parse("multipart/form-data"), eBirthDateJ);
        RequestBody Address = RequestBody.create(MediaType.parse("multipart/form-data"), eAddress);
        RequestBody Lat = RequestBody.create(MediaType.parse("multipart/form-data"), eLat);
        RequestBody Lang = RequestBody.create(MediaType.parse("multipart/form-data"), eLang);
        RequestBody NationalCode = RequestBody.create(MediaType.parse("multipart/form-data"), eNationalCode);

        setPrimaryCall(PishtazanApplication
                .getApplication(getContext())
                .getRetrofitComponent()
                .getRetrofitApiInterface()
                .EDIT_COLLEAGUE(
                        Image,
                        Id,
                        FullName,
                        PhoneNumber,
                        MobileNumber,
                        BirthDateJ,
                        Address,
                        Lat,
                        Lang,
                        UserInfoId,
                        NationalCode,
                        Level));

        getPrimaryCall().enqueue(new Callback<MR_Primary>() {
            @Override
            public void onResponse(Call<MR_Primary> call, Response<MR_Primary> response) {
                if (responseIsOk(response)) {
                    setResponseMessage(response.body().getMessage());
                    if (response.body().getStatue() == 1)
                        getPublishSubject().onNext(StaticValues.ML_EditSuccess);
                    else
                        getPublishSubject().onNext(StaticValues.ML_ResponseError);
                }
            }

            @Override
            public void onFailure(Call<MR_Primary> call, Throwable t) {
                callIsFailure();
            }
        });

    }
    //______________________________________________________________________________________________ editColleagueProfile


    //______________________________________________________________________________________________ getCustomer
    public void getCustomer(Integer personId) {

        Integer UserInfoId = getUserId();
        if (UserInfoId == 0) {
            userIsNotAuthorization();
            return;
        }

        setPrimaryCall(PishtazanApplication
                .getApplication(getContext())
                .getRetrofitComponent()
                .getRetrofitApiInterface()
                .GET_CUSTOMERS_ID(UserInfoId, personId));

        getPrimaryCall().enqueue(new Callback<MR_GetAllPerson>() {
            @Override
            public void onResponse(Call<MR_GetAllPerson> call, Response<MR_GetAllPerson> response) {
                if (responseIsOk(response)) {
                    setResponseMessage(response.body().getMessage());
                    if (response.body().getStatue() == 0)
                        getPublishSubject().onNext(StaticValues.ML_ResponseError);
                    else {
                        person = response.body().getCustomer();
                        setResponseMessage("");
                        sendMessageToObservable(StaticValues.ML_GetPerson);
                    }
                }
            }

            @Override
            public void onFailure(Call<MR_GetAllPerson> call, Throwable t) {
                callIsFailure();
            }
        });

    }
    //______________________________________________________________________________________________ getCustomer




    //______________________________________________________________________________________________ getUser
    public void getUser() {

        Integer Id = getColleagueId();
        if (Id == 0) {
            userIsNotAuthorization();
            return;
        }

        setPrimaryCall(PishtazanApplication
                .getApplication(getContext())
                .getRetrofitComponent()
                .getRetrofitApiInterface()
                .GET_User_Info(Id));

        getPrimaryCall().enqueue(new Callback<MR_GetAllPerson>() {
            @Override
            public void onResponse(Call<MR_GetAllPerson> call, Response<MR_GetAllPerson> response) {
                if (responseIsOk(response)) {
                    setResponseMessage(response.body().getMessage());
                    if (response.body().getStatue() == 0)
                        getPublishSubject().onNext(StaticValues.ML_ResponseError);
                    else {
                        MD_UserInfo info = response.body().getUserInfo();
                        MD_Person p = new MD_Person(
                             info.getId(),
                             info.getFullName(),
                             info.getLocationStateId(),
                             info.getPhoneNumber(),
                             info.getMobileNumber()
                             ,info.getDescription(),
                             null,
                             null,
                             null,
                             info.getBrithDateJ(),
                             null,
                             info.getAddress(),
                             info.getLat(),
                             info.getLang(),
                             info.getImage(),
                             info.getUserInfoId(),
                             info,
                             info.getNationalCode(),
                             info.isDelete(),
                                null,
                                0,
                                null,
                                false,
                                null,
                                null,
                                false
                        );
                        person = p;
                        setResponseMessage("");
                        sendMessageToObservable(StaticValues.ML_GetPerson);
                    }
                }
            }

            @Override
            public void onFailure(Call<MR_GetAllPerson> call, Throwable t) {
                callIsFailure();
            }
        });

    }
    //______________________________________________________________________________________________ getUser




    //______________________________________________________________________________________________ editColleagueProfile
    public void editUserProfile(
            Integer eId,
            String eFullName,
            String ePhoneNumber,
            String eBirthDateJ,
            String eAddress,
            String eLat,
            String eLang,
            String eMobileNumber,
            String eDescription) {


        Integer eUserInfoId = getUserId();
        if (eUserInfoId == 0) {
            userIsNotAuthorization();
            return;
        }

        Uri destination = Uri.fromFile(new File(getContext().getExternalCacheDir(), "cropped.jpg"));
        File file = new File(destination.getPath());
        MultipartBody.Part Image = null;

        if (file.exists())
            Image = MultipartBody
                    .Part
                    .createFormData("Image", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));

        RequestBody Id = RequestBody.create(MediaType.parse("multipart/form-data"), eId.toString());
        RequestBody FullName = RequestBody.create(MediaType.parse("multipart/form-data"), eFullName);
        RequestBody PhoneNumber = RequestBody.create(MediaType.parse("multipart/form-data"), ePhoneNumber);
        RequestBody BirthDateJ = RequestBody.create(MediaType.parse("multipart/form-data"), eBirthDateJ);
        RequestBody Address = RequestBody.create(MediaType.parse("multipart/form-data"), eAddress);
        RequestBody Lat = RequestBody.create(MediaType.parse("multipart/form-data"), eLat);
        RequestBody Lang = RequestBody.create(MediaType.parse("multipart/form-data"), eLang);
        RequestBody MobileNumber = RequestBody.create(MediaType.parse("multipart/form-data"), eMobileNumber);
        RequestBody Description = RequestBody.create(MediaType.parse("multipart/form-data"), eDescription);

        setPrimaryCall(PishtazanApplication
                .getApplication(getContext())
                .getRetrofitComponent()
                .getRetrofitApiInterface()
                .EDIT_User(
                        Image,
                        Id,
                        FullName,
                        PhoneNumber,
                        BirthDateJ,
                        Address,
                        Lat,
                        Lang,
                        MobileNumber,
                        Description));

        getPrimaryCall().enqueue(new Callback<MR_Primary>() {
            @Override
            public void onResponse(Call<MR_Primary> call, Response<MR_Primary> response) {
                if (responseIsOk(response)) {
                    setResponseMessage(response.body().getMessage());
                    if (response.body().getStatue() == 1)
                        getPublishSubject().onNext(StaticValues.ML_EditSuccess);
                    else
                        getPublishSubject().onNext(StaticValues.ML_ResponseError);
                }
            }

            @Override
            public void onFailure(Call<MR_Primary> call, Throwable t) {
                callIsFailure();
            }
        });

    }
    //______________________________________________________________________________________________ editColleagueProfile




    //______________________________________________________________________________________________ getColleague
    public void getColleague(Integer personId) {

        Integer UserInfoId = getUserId();
        if (UserInfoId == 0) {
            userIsNotAuthorization();
            return;
        }

        setPrimaryCall(PishtazanApplication
                .getApplication(getContext())
                .getRetrofitComponent()
                .getRetrofitApiInterface()
                .GET_COLLEAGUE_ID(UserInfoId, personId));

        getPrimaryCall().enqueue(new Callback<MR_GetAllPerson>() {
            @Override
            public void onResponse(Call<MR_GetAllPerson> call, Response<MR_GetAllPerson> response) {
                if (responseIsOk(response)) {
                    setResponseMessage(response.body().getMessage());
                    if (response.body().getStatue() == 0)
                        getPublishSubject().onNext(StaticValues.ML_ResponseError);
                    else {
                        person = response.body().getColleague();
                        setResponseMessage("");
                        sendMessageToObservable(StaticValues.ML_GetPerson);
                    }
                }
            }

            @Override
            public void onFailure(Call<MR_GetAllPerson> call, Throwable t) {
                callIsFailure();
            }
        });

    }
    //______________________________________________________________________________________________ getColleague



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
                                MainActivity.mainActivity.setProfile();
                                sendMessageToObservable(StaticValues.ML_GotoHome);
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<MR_UserInfoVM> call, Throwable t) {
                callIsFailure();
            }
        });

    }
    //______________________________________________________________________________________________ getUserInfoVM




    //______________________________________________________________________________________________ setAddressString
    public void setAddressString() {

        if (address != null && address.getAddress() != null) {
            StringBuilder addressString = new StringBuilder();

            String country = address.getAddress().getCountry();


            if (country != null &&
                    !country.equalsIgnoreCase("null") &&
                    !country.equalsIgnoreCase("")) {
                addressString.append(country);
                addressString.append("، ");

                if (!country.equalsIgnoreCase("ایران")) {
                    setResponseMessage(getContext().getResources().getString(R.string.OutOfIran));
                    getPublishSubject().onNext(StaticValues.ML_AddressFromMapOutOfIran);
                    return;
                }

            } else {
                setResponseMessage(getContext().getResources().getString(R.string.OutOfIran));
                getPublishSubject().onNext(StaticValues.ML_AddressFromMapOutOfIran);
                return;
            }

            String state = address.getAddress().getState();
            if (state != null &&
                    !state.equalsIgnoreCase("null") &&
                    !state.equalsIgnoreCase("")) {
                addressString.append(state);
                addressString.append("، ");
            }

            String county = address.getAddress().getCounty();
            if (county != null &&
                    !county.equalsIgnoreCase("null") &&
                    !county.equalsIgnoreCase("")) {
                addressString.append(county);
                addressString.append("، ");
            }

            String city = address.getAddress().getCity();
            if (city != null &&
                    !city.equalsIgnoreCase("null") &&
                    !city.equalsIgnoreCase("")) {
                addressString.append("شهر");
                addressString.append(" ");
                addressString.append(city);
                addressString.append("، ");
            }

            String neighbourhood = address.getAddress().getNeighbourhood();
            if (neighbourhood != null &&
                    !neighbourhood.equalsIgnoreCase("null") &&
                    !neighbourhood.equalsIgnoreCase("")) {
                addressString.append(neighbourhood);
                addressString.append("، ");
            }

            String suburb = address.getAddress().getSuburb();
            if (suburb != null &&
                    !suburb.equalsIgnoreCase("null") &&
                    !suburb.equalsIgnoreCase("") &&
                    !suburb.equalsIgnoreCase(neighbourhood)) {
                addressString.append(suburb);
                addressString.append("، ");
            }

            String road = address.getAddress().getRoad();
            if (road != null &&
                    !road.equalsIgnoreCase("null") &&
                    !road.equalsIgnoreCase("")) {
                addressString.append("خیابان");
                addressString.append(" ");
                addressString.append(road);
            }

            AddressString = addressString.toString();
        } else {
            AddressString = "";
        }

        Handler handler = new Handler();
        handler.postDelayed(() -> getPublishSubject().onNext(StaticValues.ML_AddressFromMap), 500);


    }
    //______________________________________________________________________________________________ setAddressString


    //______________________________________________________________________________________________ getAddress
    public MD_GetAddres getAddress() {
        return address;
    }
    //______________________________________________________________________________________________ getAddress


    //______________________________________________________________________________________________ setAddress
    public void setAddress(MD_GetAddres address) {
        this.address = address;
    }
    //______________________________________________________________________________________________ setAddress


    //______________________________________________________________________________________________ getAddressString
    public String getAddressString() {
        return AddressString;
    }
    //______________________________________________________________________________________________ getAddressString


    //______________________________________________________________________________________________ getPerson
    public MD_Person getPerson() {
        if (person == null)
            person = new MD_Person();
        return person;
    }
    //______________________________________________________________________________________________ getPerson


}
