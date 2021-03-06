package ir.bppir.allin4sat.viewmodels.fragments;

import android.app.Activity;
import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

import ir.bppir.allin4sat.models.MD_Person;
import ir.bppir.allin4sat.models.MR_GetAllPerson;
import ir.bppir.allin4sat.models.MR_Primary;
import ir.bppir.allin4sat.utility.StaticValues;
import ir.bppir.allin4sat.viewmodels.VM_Primary;
import ir.bppir.allin4sat.views.application.PishtazanApplication;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VM_Panel extends VM_Primary {

    private List<MD_Person> personList;

    //______________________________________________________________________________________________ VM_Panel
    public VM_Panel(Activity context) {
        setContext(context);
    }
    //______________________________________________________________________________________________ VM_Panel


    //______________________________________________________________________________________________ getPerson
    public void getPerson(int panelType, Byte PersonType, boolean isDeleted, String fullName, boolean sort) {

        cancelRequest();

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            if (panelType == StaticValues.Customer)
                getAllCustomers(PersonType, isDeleted, fullName, sort);
            else
                getAllColleagues(PersonType, isDeleted, fullName, sort);
        }, 200);

    }
    //______________________________________________________________________________________________ getPerson


    //______________________________________________________________________________________________ getAllCustomers
    private void getAllCustomers(Byte PersonType, boolean isDeleted, String fullName, boolean sort) {

        Integer UserInfoId = getUserId();
        if (UserInfoId == 0) {
            userIsNotAuthorization();
            return;
        }

        setPrimaryCall(PishtazanApplication
                .getApplication(getContext())
                .getRetrofitComponent()
                .getRetrofitApiInterface()
                .GET_ALL_CUSTOMERS(UserInfoId, PersonType, isDeleted, fullName, sort));

        getPrimaryCall().enqueue(new Callback<MR_GetAllPerson>() {
            @Override
            public void onResponse(Call<MR_GetAllPerson> call, Response<MR_GetAllPerson> response) {
                if (responseIsOk(response)) {
                    //setResponseMessage(response.body().getMessage());
                    if (response.body().getStatue() == 1) {
                        setResponseMessage("");
                        personList = response.body().getCustomers();
                        getPublishSubject().onNext(StaticValues.ML_GetPerson);
                    } else {
                        setResponseMessage(response.body().getMessage());
                        getPublishSubject().onNext(StaticValues.ML_ResponseError);
                    }
                }
            }

            @Override
            public void onFailure(Call<MR_GetAllPerson> call, Throwable t) {
                callIsFailure();
            }
        });


    }
    //______________________________________________________________________________________________ getAllCustomers


    //______________________________________________________________________________________________ getAllColleagues
    private void getAllColleagues(Byte PersonType, boolean isDeleted, String fullName, boolean sort) {

        Integer UserInfoId = getUserId();
        if (UserInfoId == 0) {
            userIsNotAuthorization();
            return;
        }

        setPrimaryCall(PishtazanApplication
                .getApplication(getContext())
                .getRetrofitComponent()
                .getRetrofitApiInterface()
                .GET_ALL_COLLEAGUES(UserInfoId, PersonType, isDeleted, fullName, sort));

        getPrimaryCall().enqueue(new Callback<MR_GetAllPerson>() {
            @Override
            public void onResponse(Call<MR_GetAllPerson> call, Response<MR_GetAllPerson> response) {
                if (responseIsOk(response)) {
                    if (response.body().getStatue() == 1) {
                        setResponseMessage("");
                        personList = response.body().getColleagues();
                        getPublishSubject().onNext(StaticValues.ML_GetPerson);
                    } else {
                        setResponseMessage(response.body().getMessage());
                        getPublishSubject().onNext(StaticValues.ML_ResponseError);
                    }
                }
            }

            @Override
            public void onFailure(Call<MR_GetAllPerson> call, Throwable t) {
                callIsFailure();
            }
        });

    }
    //______________________________________________________________________________________________ getAllColleagues


    //______________________________________________________________________________________________ getPersonList
    public List<MD_Person> getPersonList() {
        if (personList == null)
            personList = new ArrayList<>();
        return personList;
    }
    //______________________________________________________________________________________________ getPersonList


    //______________________________________________________________________________________________ saveCustomerReminder
    public void saveCustomerReminder(
            Byte ReminderTypes,
            Integer Position,
            String stringDate,
            String stringTime,
            String Name,
            Integer PersonId) {

        String result = "";
        StringBuilder title = new StringBuilder();
        if (Position == null)
            title.append(Name);
        else {
            title.append(personList.get(Position).getFullName());
            PersonId = personList.get(Position).getId();
        }
        result = title.toString();

/*        if (ReminderTypes.equals(StaticValues.Call))
            title.append(getContext().getResources().getString(R.string.CallWith));
        else
            title.append(getContext().getResources().getString(R.string.MeetingWith));
        title.append(" ");
        if (Position == null) {
            title.append(Name);
        } else {
            PersonId = personList.get(Position).getId();
            title.append(personList.get(Position).getFullName());
        }*/

/*        if (ReminderTypes.equals(StaticValues.Meeting)) {
            title.append(" در ");
            title.append(getContext().getResources().getString(R.string.Clock));
            title.append(" ");
            title.append(stringTime);

            result = getContext().getResources().getString(R.string.ResultTitleMeet1);
            result = result + " " + personList.get(Position).getFullName();
            result = result + " " + getContext().getResources().getString(R.string.ResultTitleMeet2);

        } else {
            result = getContext().getResources().getString(R.string.ResultTitleCall1);
            result = result + " " + personList.get(Position).getFullName();
            result = result + " " + getContext().getResources().getString(R.string.ResultTitleCall2);
        }*/

        setPrimaryCall(PishtazanApplication
                .getApplication(getContext())
                .getRetrofitComponent()
                .getRetrofitApiInterface()
                .CREATE_REMINDER_CUSTOMER(
                        stringDate,
                        stringTime,
                        title.toString(),
                        StaticValues.Customer,
                        ReminderTypes,
                        0,
                        PersonId,
                        stringDate,
                        result
                ));

        getPrimaryCall().enqueue(new Callback<MR_Primary>() {
            @Override
            public void onResponse(Call<MR_Primary> call, Response<MR_Primary> response) {
                if (responseIsOk(response)) {
                    setResponseMessage(response.body().getMessage());
                    if (response.body().getStatue() == 0)
                        getPublishSubject().onNext(StaticValues.ML_ResponseError);
                    else {
                        getPublishSubject().onNext(StaticValues.ML_SaveReminder);
                    }
                }
            }

            @Override
            public void onFailure(Call<MR_Primary> call, Throwable t) {
                callIsFailure();
            }
        });

    }
    //______________________________________________________________________________________________ saveCustomerReminder


    //______________________________________________________________________________________________ saveColleagueReminder
    public void saveColleagueReminder(
            Byte ReminderTypes,
            Integer Position,
            String stringDate,
            String stringTime,
            String Name,
            Integer PersonId) {

        String result = "";
        StringBuilder title = new StringBuilder();
        if (Position == null)
            title.append(Name);
        else {
            title.append(personList.get(Position).getFullName());
            PersonId = personList.get(Position).getId();
        }
        result = title.toString();

/*        if (ReminderTypes.equals(StaticValues.Call))
            title.append(getContext().getResources().getString(R.string.CallWith));
        else
            title.append(getContext().getResources().getString(R.string.MeetingWith));
        title.append(" ");
        if (Position == null) {
            title.append(Name);
        } else {
            PersonId = personList.get(Position).getId();
            title.append(personList.get(Position).getFullName());
        }

        if (ReminderTypes.equals(StaticValues.Meeting)) {
            title.append(" در ");
            title.append(getContext().getResources().getString(R.string.Clock));
            title.append(" ");
            title.append(stringTime);

            result = getContext().getResources().getString(R.string.ResultTitleMeet1);
            result = result + " " + personList.get(Position).getFullName();
            result = result + " " + getContext().getResources().getString(R.string.ResultTitleMeet2);

        } else {
            result = getContext().getResources().getString(R.string.ResultTitleCall1);
            result = result + " " + personList.get(Position).getFullName();
            result = result + " " + getContext().getResources().getString(R.string.ResultTitleCall2);
        }*/

        setPrimaryCall(PishtazanApplication
                .getApplication(getContext())
                .getRetrofitComponent()
                .getRetrofitApiInterface()
                .CREATE_REMINDER_COLLEAGUE(
                        stringDate,
                        stringTime,
                        title.toString(),
                        StaticValues.Colleague,
                        ReminderTypes,
                        0,
                        PersonId,
                        stringDate,
                        result
                ));

        getPrimaryCall().enqueue(new Callback<MR_Primary>() {
            @Override
            public void onResponse(Call<MR_Primary> call, Response<MR_Primary> response) {
                if (responseIsOk(response)) {
                    setResponseMessage(response.body().getMessage());
                    if (response.body().getStatue() == 0)
                        getPublishSubject().onNext(StaticValues.ML_ResponseError);
                    else {
                        getPublishSubject().onNext(StaticValues.ML_SaveReminder);
                    }
                }
            }

            @Override
            public void onFailure(Call<MR_Primary> call, Throwable t) {
                callIsFailure();
            }
        });

    }
    //______________________________________________________________________________________________ saveColleagueReminder


    //______________________________________________________________________________________________ deletePerson
    public void deletePerson(int panelType, Integer Position) {
        if (panelType == StaticValues.Customer)
            deleteCustomer(Position);
        else
            deleteColleague(Position);
    }
    //______________________________________________________________________________________________ deletePerson


    //______________________________________________________________________________________________ deleteCustomer
    private void deleteCustomer(Integer Position) {

        Integer Id = getUserId();
        if (Id == 0) {
            userIsNotAuthorization();
            return;
        }

        setPrimaryCall(PishtazanApplication
                .getApplication(getContext())
                .getRetrofitComponent()
                .getRetrofitApiInterface()
                .DELETE_CUSTOMER(personList.get(Position).getId(), Id));

        getPrimaryCall().enqueue(new Callback<MR_Primary>() {
            @Override
            public void onResponse(Call<MR_Primary> call, Response<MR_Primary> response) {
                if (responseIsOk(response)) {
                    setResponseMessage(response.body().getMessage());
                    if (response.body().getStatue() == 1)
                        getPublishSubject().onNext(StaticValues.ML_DeletePerson);
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
    //______________________________________________________________________________________________ deleteCustomer


    //______________________________________________________________________________________________ deleteColleague
    private void deleteColleague(Integer Position) {

        Integer Id = getUserId();
        if (Id == 0) {
            userIsNotAuthorization();
            return;
        }

        setPrimaryCall(PishtazanApplication
                .getApplication(getContext())
                .getRetrofitComponent()
                .getRetrofitApiInterface()
                .DELETE_COLLEAGUE(personList.get(Position).getId(), Id));

        getPrimaryCall().enqueue(new Callback<MR_Primary>() {
            @Override
            public void onResponse(Call<MR_Primary> call, Response<MR_Primary> response) {
                if (responseIsOk(response)) {
                    setResponseMessage(response.body().getMessage());
                    if (response.body().getStatue() == 1)
                        getPublishSubject().onNext(StaticValues.ML_DeletePerson);
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
    //______________________________________________________________________________________________ deleteColleague


    //______________________________________________________________________________________________ deletePersonFromArchive
    public void deletePersonFromArchive(int panelType, Integer Position) {
        if (panelType == StaticValues.Customer)
            deleteCustomerFromArchive(Position);
        else
            deleteColleagueFromArchive(Position);
    }
    //______________________________________________________________________________________________ deletePersonFromArchive


    //______________________________________________________________________________________________ deleteCustomerFromArchive
    private void deleteCustomerFromArchive(Integer Position) {

        Integer Id = getUserId();
        if (Id == 0) {
            userIsNotAuthorization();
            return;
        }

        setPrimaryCall(PishtazanApplication
                .getApplication(getContext())
                .getRetrofitComponent()
                .getRetrofitApiInterface()
                .DELETE_CUSTOMER_ARCHIVE(personList.get(Position).getId(), Id));

        getPrimaryCall().enqueue(new Callback<MR_Primary>() {
            @Override
            public void onResponse(Call<MR_Primary> call, Response<MR_Primary> response) {
                if (responseIsOk(response)) {
                    setResponseMessage(response.body().getMessage());
                    if (response.body().getStatue() == 1)
                        getPublishSubject().onNext(StaticValues.ML_DeleteArchive);
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
    //______________________________________________________________________________________________ deleteCustomerFromArchive


    //______________________________________________________________________________________________ deleteColleagueFromArchive
    private void deleteColleagueFromArchive(Integer Position) {

        Integer Id = getUserId();
        if (Id == 0) {
            userIsNotAuthorization();
            return;
        }

        setPrimaryCall(PishtazanApplication
                .getApplication(getContext())
                .getRetrofitComponent()
                .getRetrofitApiInterface()
                .DELETE_COLLEAGUE_ARCHIVE(personList.get(Position).getId(), Id));

        getPrimaryCall().enqueue(new Callback<MR_Primary>() {
            @Override
            public void onResponse(Call<MR_Primary> call, Response<MR_Primary> response) {
                if (responseIsOk(response)) {
                    setResponseMessage(response.body().getMessage());
                    if (response.body().getStatue() == 1)
                        getPublishSubject().onNext(StaticValues.ML_DeleteArchive);
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
    //______________________________________________________________________________________________ deleteColleagueFromArchive


    //______________________________________________________________________________________________ moveToPossible
    public void moveToPossible(int panelType, Integer Position) {
        if (panelType == StaticValues.Customer)
            moveToPossibleCustomer(Position);
        else
            moveToPossibleColleague(Position);
    }
    //______________________________________________________________________________________________ moveToPossible


    //______________________________________________________________________________________________ moveToPossibleColleague
    public void moveToPossibleColleague(Integer Position) {

        Integer Id = getUserId();
        if (Id == 0) {
            userIsNotAuthorization();
            return;
        }

        setPrimaryCall(PishtazanApplication
                .getApplication(getContext())
                .getRetrofitComponent()
                .getRetrofitApiInterface()
                .CONVERT_TO_POSSIBLE_COLLEAGUE(personList.get(Position).getId(), Id));

        getPrimaryCall().enqueue(new Callback<MR_Primary>() {
            @Override
            public void onResponse(Call<MR_Primary> call, Response<MR_Primary> response) {
                if (responseIsOk(response)) {
                    setResponseMessage(response.body().getMessage());
                    if (response.body().getStatue() == 1)
                        getPublishSubject().onNext(StaticValues.ML_ConvertPerson);
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
    //______________________________________________________________________________________________ moveToPossibleColleague


    //______________________________________________________________________________________________ moveToPossibleCustomer
    public void moveToPossibleCustomer(Integer Position) {

        Integer Id = getUserId();
        if (Id == 0) {
            userIsNotAuthorization();
            return;
        }

        setPrimaryCall(PishtazanApplication
                .getApplication(getContext())
                .getRetrofitComponent()
                .getRetrofitApiInterface()
                .CONVERT_TO_POSSIBLE_CUSTOMER(personList.get(Position).getId(), Id));

        getPrimaryCall().enqueue(new Callback<MR_Primary>() {
            @Override
            public void onResponse(Call<MR_Primary> call, Response<MR_Primary> response) {
                if (responseIsOk(response)) {
                    setResponseMessage(response.body().getMessage());
                    if (response.body().getStatue() == 1)
                        getPublishSubject().onNext(StaticValues.ML_ConvertPerson);
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
    //______________________________________________________________________________________________ moveToPossibleCustomer



    //______________________________________________________________________________________________ moveToCertain
    public void moveToCertain(int panelType, Integer Position) {
        if (panelType == StaticValues.Customer)
            moveToCertainCustomer(Position);
        else
            moveToCertainColleague(Position);
    }
    //______________________________________________________________________________________________ moveToCertain


    //______________________________________________________________________________________________ moveToPossibleColleague
    public void moveToCertainColleague(Integer Position) {

        Integer Id = getUserId();
        if (Id == 0) {
            userIsNotAuthorization();
            return;
        }

        setPrimaryCall(PishtazanApplication
                .getApplication(getContext())
                .getRetrofitComponent()
                .getRetrofitApiInterface()
                .CONVERT_TO_CERTAIN_COLLEAGUE(personList.get(Position).getId(), Id));

        getPrimaryCall().enqueue(new Callback<MR_Primary>() {
            @Override
            public void onResponse(Call<MR_Primary> call, Response<MR_Primary> response) {
                if (responseIsOk(response)) {
                    setResponseMessage(response.body().getMessage());
                    if (response.body().getStatue() == 1) {
                        setResponseMessage(response.body().getMessage());
                        getPublishSubject().onNext(StaticValues.ML_ConvertPerson);
                    } else {
                        setResponseMessage(getResponseMessages(response));
                        getPublishSubject().onNext(StaticValues.ML_ResponseError);
                    }
                }
            }

            @Override
            public void onFailure(Call<MR_Primary> call, Throwable t) {
                callIsFailure();
            }
        });

    }
    //______________________________________________________________________________________________ moveToPossibleColleague


    //______________________________________________________________________________________________ moveToPossibleCustomer
    public void moveToCertainCustomer(Integer Position) {

        Integer Id = getUserId();
        if (Id == 0) {
            userIsNotAuthorization();
            return;
        }

        setPrimaryCall(PishtazanApplication
                .getApplication(getContext())
                .getRetrofitComponent()
                .getRetrofitApiInterface()
                .CONVERT_TO_CERTAIN_CUSTOMER(personList.get(Position).getId(), Id));

        getPrimaryCall().enqueue(new Callback<MR_Primary>() {
            @Override
            public void onResponse(Call<MR_Primary> call, Response<MR_Primary> response) {
                if (responseIsOk(response)) {
                    setResponseMessage(response.body().getMessage());
                    if (response.body().getStatue() == 1) {
                        setResponseMessage(response.body().getMessage());
                        getPublishSubject().onNext(StaticValues.ML_ConvertPerson);
                    } else {
                        setResponseMessage(getResponseMessages(response));
                        getPublishSubject().onNext(StaticValues.ML_ResponseError);
                    }
                }
            }

            @Override
            public void onFailure(Call<MR_Primary> call, Throwable t) {
                callIsFailure();
            }
        });

    }
    //______________________________________________________________________________________________ moveToPossibleCustomer



}
