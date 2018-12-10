package com.sourcey.materiallogindemo;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.BottomNavigationView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;



public class one_project_fragment extends Fragment {
    public static String response;
    Project project;
    public static String token;
    public static String id;
    private static String bid;
    TextView title;
    TextView description;
    TextView status;
    TextView price_range;
    TextView deadline;
    Button bidButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View oneProjectView;
        oneProjectView = inflater.inflate(R.layout.fragment_one_project_page, container, false);
        Bundle args = getArguments();
        id = args.getString("pk",""); //Record the id of project
        token = args.getString("token",""); //Record token of person
        title=oneProjectView.findViewById(R.id.title);
        description=oneProjectView.findViewById(R.id.description);
        status=oneProjectView.findViewById(R.id.status);
        deadline=oneProjectView.findViewById(R.id.deadline);
        price_range=oneProjectView.findViewById(R.id.price_range);
        bidButton=oneProjectView.findViewById(R.id.bidButton);
        bidButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(((HomepageActivity)getActivity()));
                builder.setTitle("How much do you want to bid?");

                    // Set up the input
                final EditText input = new EditText(((HomepageActivity)getActivity()));
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                builder.setView(input);

// Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        bid = input.getText().toString();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        try {
            getProject();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }



        return oneProjectView;
    }
    public void getProject() throws ExecutionException, InterruptedException, JSONException, ParseException {
        HttpGetAsyncTask myTask = new HttpGetAsyncTask(getActivity(),token);
        String theResponse = myTask.execute("http://52.59.230.90/projects/"+id+"/").get();
        int statusCode = Integer.parseInt(theResponse.substring(0, 3));
        response = theResponse.substring(3);
        if (statusCode >= HttpURLConnection.HTTP_BAD_REQUEST) {
            onGetProjectFailure();
        }
        else if(statusCode==HttpURLConnection.HTTP_OK){
            onGetProjectSuccess();
        }

    }

    public void onGetProjectFailure() {
        String str="";
        String field=response.substring(2,response.indexOf("\":"));
        try {
            str = ((HomepageActivity) getActivity()).JSONObjectToString(response, field); //Getting error field: Non-field-errors etc...
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(getActivity().getApplicationContext(), field+"\n"+str, Toast.LENGTH_LONG).show();
    }
    public void onGetProjectSuccess() throws JSONException, ParseException {
        JSONObject obj = new JSONObject(response);
        String deadline_str = obj.getString("deadline");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date deadlinee = sdf.parse(deadline_str);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strdate= dateFormat.format(deadlinee);

        title.setText(obj.getString("title"));
        description.setText(obj.getString("description"));
        deadline.setText("Deadline: "+strdate);
        status.setText("Status: "+obj.getString("status"));
        if(obj.getString("status").equals("active")){
            status.setTextColor(Color.GREEN);
        }
        else{
            status.setTextColor(Color.RED);
        }
        String pricerange="Price range: "+obj.getString("min_price")+"-"+obj.getString("max_price");
        price_range.setText(pricerange);
        price_range.setTextColor(Color.YELLOW);
    }

}