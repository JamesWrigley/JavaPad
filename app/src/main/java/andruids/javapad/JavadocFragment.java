package andruids.javapad;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link JavadocFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link JavadocFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JavadocFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private WebView viu;

    private static String url = "https://docs.oracle.com/javase/8/docs/api/";
    private static List<String> urls = new ArrayList<String>();
    public static Map<String, Integer> history = new HashMap<>();

    private OnFragmentInteractionListener mListener;

    public JavadocFragment() {
        // Required empty public constructor
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(isVisibleToUser){
            viu.loadUrl(url);
        }
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment JavadocFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static JavadocFragment newInstance(String param1, String param2) {
        JavadocFragment fragment = new JavadocFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public WebView getWebView(View view) {

        return (WebView) view.findViewById(R.id.webView);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_javadoc, container, false);

        viu = getWebView(view);
        // Enable Javascript
        WebSettings webSettings = getWebView(view).getSettings();
        webSettings.setJavaScriptEnabled(true);
        getWebView(view).getSettings().setUseWideViewPort(true);
        getWebView(view).getSettings().setBuiltInZoomControls(true);
        getWebView(view).loadUrl(url);
        getWebView(view).setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;
            }

            @Override
            public void onLoadResource(WebView view, String u) {
                //do something
                urls.add(u);
            }
        });

        Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.javaVer_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                String item = parentView.getItemAtPosition(position).toString();
                if(item.equals("Java 8")){
                    url = "https://docs.oracle.com/javase/8/docs/api/";
                }
                if(item.equals("Java 7")){
                    url = "https://docs.oracle.com/javase/7/docs/api/";
                }
                if(item.equals("Java 6")){
                    url = "https://docs.oracle.com/javase/6/docs/api/";
                }
                if(item.equals("Java 5")){
                    url = "http://docs.oracle.com/javase/1.5.0/docs/api/";
                }

                viu.loadUrl(url);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    public static List<String> getUrls() {
        return urls;
    }

    public static Map<String, Integer> readFile(Context context, String filename) {
        try {
            FileInputStream fis = context.openFileInput(filename);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(isr);
            String line;

            while ((line = bufferedReader.readLine()) != null) {

                if(JavadocFragment.history.containsKey(line)){

                    JavadocFragment.history.put(line, JavadocFragment.history.get(line) + 1);
                }
                else{
                    JavadocFragment.history.put(line, 1);
                }
            }

            return JavadocFragment.history;

        } catch (FileNotFoundException e) {
            return null;
        } catch (UnsupportedEncodingException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    public static void setUrl(String u) {
        url = u;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
