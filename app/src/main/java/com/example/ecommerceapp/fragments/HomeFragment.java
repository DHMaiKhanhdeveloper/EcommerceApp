package com.example.ecommerceapp.fragments;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.ecommerceapp.R;
import com.example.ecommerceapp.adapter.CategoryAdapter;
import com.example.ecommerceapp.models.Category;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private  ImageSlider imageSlider;
    private RecyclerView rcvCategory;
    private CategoryAdapter categoryAdapter;
    private List<Category> listCategory;
    private FirebaseFirestore firebaseFirestore;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_home, container, false);

        firebaseFirestore = FirebaseFirestore.getInstance();

        imageSlider = view.findViewById(R.id.image_slider);
        rcvCategory = view.findViewById(R.id.rec_category);



        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.banner1,"Discount On Shoes Items", ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.banner2,"Discount On Perfune", ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.banner3,"70%", ScaleTypes.CENTER_CROP));

        imageSlider.setImageList(slideModels);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false);
        rcvCategory.setLayoutManager(linearLayoutManager);

//        listCategory = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(getActivity(),getCategory());
        rcvCategory.setAdapter(categoryAdapter);


//        firebaseFirestore.collection("Category")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                               Category category = document.toObject(Category.class);
//                                listCategory.add(category);
//                                categoryAdapter.notifyDataSetChanged();
//
//                            }
//                        } else {
//                            Log.w(TAG, "Error getting documents.", task.getException());
//                        }
//                    }
//                });
        return view;
    }

    private List<Category> getCategory() {
        List<Category> listCategory = new ArrayList<>();


//
//        listCourses.add(new Courses(R.drawable.tienganh1,"Ôn luyện thi toán 10 p1"));
//        listCourses.add(new Courses(R.drawable.toan10,"Ôn luyện thi toán 10 p2"));
//        listCourses.add(new Courses(R.drawable.tienganh3,"Ôn luyện thi toán 10 p3"));
//        listCourses.add(new Courses(R.drawable.toan10p1,"Ôn luyện thi toán 10 p4"));

        listCategory.add(new Category(R.drawable.friends,"Men Collection","men"));

        return listCategory;
    }
}