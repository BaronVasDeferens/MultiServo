package skot.multiservo;

import android.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements SliderFragment.OnFragmentInteractionListener {

    SliderFragment sliderOne, sliderTwo, sliderThree, sliderFour, sliderFive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragManager = getFragmentManager();

        android.app.FragmentTransaction transaction = fragManager.beginTransaction();

        // NOTE: "Hot load" doesn't clear out old transactions

        sliderOne = SliderFragment.newInstance("s1");
        sliderTwo = SliderFragment.newInstance("s2");
        sliderThree = SliderFragment.newInstance("s3");
        sliderFour = SliderFragment.newInstance("s4");
        sliderFive = SliderFragment.newInstance("s5");

        transaction.add(R.id.activity_main, sliderOne);
        transaction.add(R.id.activity_main, sliderTwo);
        transaction.add(R.id.activity_main, sliderThree);
        transaction.add(R.id.activity_main, sliderFour);
        transaction.add(R.id.activity_main, sliderFive);
        transaction.commit();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
