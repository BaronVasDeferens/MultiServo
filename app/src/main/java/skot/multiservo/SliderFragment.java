package skot.multiservo;

import android.app.ActivityManager;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.Queue;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SliderFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SliderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SliderFragment extends Fragment {

    private String servoId = "NO ID SET";

    private String mParam1;
    private TouchControl touchControl;
    private OnFragmentInteractionListener mListener;
    public Queue<Pair<String, Integer>> commandQueue;

    public SliderFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment SliderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SliderFragment newInstance(String param1, Queue<Pair<String, Integer>> commandQueue) {
        SliderFragment fragment = new SliderFragment();
        Bundle args = new Bundle();
        //args.putString(servoId, param1);
        fragment.setArguments(args);
        fragment.servoId = param1;
        fragment.commandQueue = commandQueue;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(servoId);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_slider, container, false);

        v.removeAllViews();

        touchControl = new TouchControl((MainActivity) getActivity());
        touchControl.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                TouchControl tc = (TouchControl) view;
                int yPosition = tc.setSlider(motionEvent);
                tc.invalidate();

                commandQueue.add(new Pair<>(servoId, yPosition));
                return true;
            }
        });

        v.addView(touchControl);
        return v;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

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
