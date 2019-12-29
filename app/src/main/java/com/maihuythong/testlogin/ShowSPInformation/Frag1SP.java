package com.maihuythong.testlogin.ShowSPInformation;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.maihuythong.testlogin.R;
import com.maihuythong.testlogin.showTourInfo.Member;
import com.maihuythong.testlogin.showTourInfo.MemberAdapter;

import java.util.ArrayList;
import java.util.Collections;

import static com.maihuythong.testlogin.stopPointInfo.StopPointDetailActivity.getDate;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Frag1SP.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Frag1SP#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Frag1SP extends Fragment {
    private SPOverview mSpOverview;

    private OnFragmentInteractionListener mListener;

    private final static String SP_OVERVIEW = "spOverview";

    private View mView;
    private TextView titleView;
    private TextView idView;
    private TextView serviceIdView;
    private TextView addressView;
    private TextView provinceIdView;
    private TextView dateView;
    private TextView priceView;
    private TextView serviceTypeView;

    public Frag1SP() {
        // Required empty public constructor
    }

    public static Frag1SP newInstance(SPOverview spOverview) {
        Frag1SP fragment = new Frag1SP();
        Bundle args = new Bundle();
        args.putParcelable(SP_OVERVIEW, spOverview);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSpOverview = getArguments().getParcelable(SP_OVERVIEW);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_frag1_sp, container, false);
//        titleView = mView.findViewById(R.id.title_stop_point_info);
        idView = mView.findViewById(R.id.stop_point_id);
        serviceIdView = mView.findViewById(R.id.stop_point_serviceId);
        addressView = mView.findViewById(R.id.stop_point_address);
        dateView = mView.findViewById(R.id.stop_point_day);
        serviceTypeView = mView.findViewById(R.id.stop_point_serviceType);
        provinceIdView = mView.findViewById(R.id.stop_point_province);
        priceView = mView.findViewById(R.id.stop_point_price);

        String dateFormat= "dd/MM/yyyy";
        String dateArrival = getDate(mSpOverview.getArrivalAt(),dateFormat);
        String dateLeave = getDate(mSpOverview.getLeaveAt(),dateFormat);
        String[] serviceArray;
        String[] provinceArray;
        serviceArray = getResources().getStringArray(R.array.service_array);
        provinceArray = getResources().getStringArray(R.array.province_array);


//        titleView.setText(mSpOverview.getName());
        serviceIdView.setText(String.valueOf(mSpOverview.getServiceId()));
        addressView.setText(mSpOverview.getAddress());
        dateView.setText(dateArrival + " - " + dateLeave);
        serviceTypeView.setText(serviceArray[mSpOverview.getServiceTypeId() - 1]);
        if(mSpOverview.getProvinceId() < 1|| mSpOverview.getProvinceId() > 63) provinceIdView.setText("");
        else provinceIdView.setText(provinceArray[mSpOverview.getProvinceId()-1]);
        priceView.setText(mSpOverview.getMinCost() + " - " + mSpOverview.getMaxCost());

        return mView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
