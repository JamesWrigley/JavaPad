package andruids.javapad;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.HashMap;
import java.util.List;

import prettify.PrettifyParser;
import syntaxhighlight.ParseResult;
import syntaxhighlight.Parser;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PadFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PadFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PadFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    private Parser parser;
    private String pattern;
    private HashMap<String, String> colors;

    public PadFragment() {
        parser = new PrettifyParser();
        pattern = "<font color=\"#%s\">%s</font>";
        colors = new HashMap<>();
        colors.put("typ", "9473a5");
        colors.put("kwd", "ff4500");
        colors.put("lit", "6592b4");
        colors.put("com", "7e7e7e");
        colors.put("str", "008e00");
        colors.put("pun", "000000");
        colors.put("pln", "292929");
    }

    private String getColor(String type) {
        return colors.containsKey(type) ? colors.get(type) : colors.get("pln");
    }

    private Spanned highlight(String text) {
        StringBuilder highlightedText = new StringBuilder();
        List<ParseResult> results = parser.parse("java", text);
        for (ParseResult result : results) {
            String type = result.getStyleKeys().get(0);
            String content = text.substring(result.getOffset(), result.getOffset() + result.getLength());
            String[] parts = content.split("((?<=\\n)|(?=\\n))", -1);
            for (String token : parts) {
                if (token.equals("\n")) {
                    highlightedText.append("<br>");
                } else {
                    highlightedText.append(String.format(pattern, getColor(type), Html.escapeHtml(token)));
                }
            }
        }

        String foo = Html.fromHtml(highlightedText.toString()).toString();
        return Html.fromHtml(highlightedText.toString());
    }

    public String getText() {
        EditText e = (EditText) getView().findViewById(R.id.edittext);
        return e.getText().toString();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PadFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PadFragment newInstance(String param1, String param2) {
        PadFragment fragment = new PadFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        View view = inflater.inflate(R.layout.fragment_pad, container, false);
        // Get the EditText to reformat itself after every text change
        final EditText e = (EditText) view.findViewById(R.id.edittext);
        e.addTextChangedListener(new TextWatcher() {
            private boolean test = true;

            @Override
            public void afterTextChanged(Editable editable) {
                if (test) {
                    test = false;
                    int i = e.getSelectionStart();
                    Spanned text = highlight(editable.toString());
                    if (text.length() >= 2) {
                        String chars = e.getText().toString();
                        String lastChars = chars.substring(chars.length() - 2);
                        if (lastChars.equals("\n ")) {
                            e.setText(text);
                            e.append(" ");
                        } else {
                            e.setText(text);
                        }
                    } else {
                        e.setText(text);
                    }

                    e.setSelection(i);
                    test = true;
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
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
