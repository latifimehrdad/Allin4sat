package ir.bppir.allin4sat.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cunoraz.gifview.library.GifView;

import butterknife.BindView;
import ir.bppir.allin4sat.R;
import ir.bppir.allin4sat.databinding.FragmentPostBinding;
import ir.bppir.allin4sat.utility.StaticValues;
import ir.bppir.allin4sat.viewmodels.fragments.VM_Post;
import ir.bppir.allin4sat.views.adapters.AP_Post;

public class Post extends FragmentPrimary implements FragmentPrimary.actionFromObservable,
        AP_Post.clickItemTutorial {


    private VM_Post vm_post;
    private NavController navController;
    public static Integer ExamResultId = 0;
    public static Integer tutorialId = 0;
    private String postType;
    private Integer personId;


    @BindView(R.id.RecyclerViewPost)
    RecyclerView RecyclerViewPost;

    @BindView(R.id.GifViewLoading)
    GifView GifViewLoading;

    @BindView(R.id.LinearLayoutNewQuiz)
    LinearLayout LinearLayoutNewQuiz;

    @BindView(R.id.ImageViewNew)
    ImageView ImageViewNew;

    @BindView(R.id.GifViewLoadingNew)
    GifView GifViewLoadingNew;

    @BindView(R.id.LinearLayoutExam)
    LinearLayout LinearLayoutExam;

    @BindView(R.id.linearLayoutHistory)
    LinearLayout linearLayoutHistory;


    //______________________________________________________________________________________________ onCreateView
    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        if (getView() == null) {
            FragmentPostBinding binding = DataBindingUtil.inflate(
                    inflater, R.layout.fragment_post, container, false);
            vm_post = new VM_Post(getActivity());
            binding.setPost(vm_post);
            setView(binding.getRoot());
            postType = getArguments().getString(getContext().getResources().getString(R.string.ML_Type),
                    getContext().getResources().getString(R.string.ML_MyReport));
            if (postType.equals("null"))
                postType = getContext().getResources().getString(R.string.ML_MyReport);

            if (postType.equalsIgnoreCase(getContext().getResources().getString(R.string.ML_MySubsetReport)))
                personId = getArguments().getInt(getContext().getResources().getString(R.string.ML_Id), 0);
            else
                personId = null;

            init();
            setClick();
        }
        return getView();
    }
    //______________________________________________________________________________________________ onCreateView


    //______________________________________________________________________________________________ onStart
    @Override
    public void onStart() {
        super.onStart();
        setObservableForGetAction(
                Post.this,
                vm_post.getPublishSubject(),
                vm_post);
        navController = Navigation.findNavController(getView());
        if (ExamResultId == -1) {
            ExamResultId = 0;
            Bundle bundle = new Bundle();
            bundle.putInt(getContext().getResources().getString(R.string.ML_TutorialId), tutorialId);
            bundle.putString(getContext().getResources().getString(R.string.ML_Type), getContext().getResources().getString(R.string.ML_LastExam));
            navController.navigate(R.id.action_post_to_tutorialMovie, bundle);
        } else if (ExamResultId != 0) {
            Bundle bundle = new Bundle();
            bundle.putInt(getContext().getResources().getString(R.string.ML_Id), ExamResultId);
            bundle.putInt(getContext().getResources().getString(R.string.ML_TutorialId), tutorialId);
            bundle.putString(getContext().getResources().getString(R.string.ML_Type),
                    getContext().getResources().getString(R.string.ML_LastExam));
            navController.navigate(R.id.action_post_to_examResults, bundle);
        }
    }
    //______________________________________________________________________________________________ onStart


    //______________________________________________________________________________________________ init
    private void init() {

        RecyclerViewPost.setVisibility(View.GONE);
        if (postType.equalsIgnoreCase(getContext().getResources().getString(R.string.ML_MyReport))) {
            LinearLayoutExam.setVisibility(View.VISIBLE);
            linearLayoutHistory.setVisibility(View.VISIBLE);
        } else {
            GifViewLoading.setVisibility(View.VISIBLE);
            LinearLayoutExam.setVisibility(View.GONE);
            linearLayoutHistory.setVisibility(View.GONE);
            vm_post.GetPost(personId);
        }


    }
    //______________________________________________________________________________________________ init


    //______________________________________________________________________________________________ setClick
    private void setClick() {

        LinearLayoutNewQuiz.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt(getContext().getResources().getString(R.string.ML_Id), 0);
            bundle.putString(getContext().getResources().getString(R.string.ML_Type), getContext().getResources().getString(R.string.ML_LastExam));
            navController.navigate(R.id.action_post_to_tutorialMovie, bundle);
        });

        linearLayoutHistory.setOnClickListener(v -> {
            navController.navigate(R.id.action_post_to_exam);
            /*GifViewLoading.setVisibility(View.VISIBLE);
            RecyclerViewPost.setAdapter(null);
            vm_post.GetPost(personId);*/
        });
    }
    //______________________________________________________________________________________________ setClick


    //______________________________________________________________________________________________ getMessageFromObservable
    @Override
    public void getActionFromObservable(Byte action) {

        GifViewLoading.setVisibility(View.GONE);
        ImageViewNew.setVisibility(View.VISIBLE);
        GifViewLoadingNew.setVisibility(View.GONE);

        if (action.equals(StaticValues.ML_GetPost)) {
            setAdapter();
            return;
        }

    }
    //______________________________________________________________________________________________ getMessageFromObservable


    //______________________________________________________________________________________________ setAdapter
    private void setAdapter() {
        AP_Post ap_post = new AP_Post(vm_post.getMd_educationCategoryVms(), Post.this);
        RecyclerViewPost.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        RecyclerViewPost.setAdapter(ap_post);
        RecyclerViewPost.setVisibility(View.VISIBLE);
    }
    //______________________________________________________________________________________________ setAdapter


    //______________________________________________________________________________________________ clickItemTutorial
    @Override
    public void clickTutorial(Integer Position, View view) {

        if (postType.equalsIgnoreCase(getContext().getResources().getString(R.string.ML_MyReport))) {
            Bundle bundle = new Bundle();
            bundle.putInt(getContext().getResources().getString(R.string.ML_Id), vm_post.getMd_educationCategoryVms().get(Position).getId());
            bundle.putString(getContext().getResources().getString(R.string.ML_Type), getContext().getResources().getString(R.string.ML_ExamHistory));
            bundle.putString(getContext().getResources().getString(R.string.ML_Description), vm_post.getMd_educationCategoryVms().get(Position).getTitle());
            navController.navigate(R.id.action_post_to_tutorial, bundle);
        } else {
            Bundle bundle = new Bundle();
            bundle.putInt(getContext().getResources().getString(R.string.ML_personId), personId);
            bundle.putInt(getContext().getResources().getString(R.string.ML_Id), vm_post.getMd_educationCategoryVms().get(Position).getId());
            bundle.putString(getContext().getResources().getString(R.string.ML_Type), postType);
            bundle.putString(getContext().getResources().getString(R.string.ML_Description), vm_post.getMd_educationCategoryVms().get(Position).getTitle());
            navController.navigate(R.id.action_post_to_tutorial, bundle);
        }
    }
    //______________________________________________________________________________________________ clickItemTutorial



    //______________________________________________________________________________________________ actionWhenFailureRequest
    @Override
    public void getActionWhenFailureRequest() {
    }
    //______________________________________________________________________________________________ actionWhenFailureRequest


}
