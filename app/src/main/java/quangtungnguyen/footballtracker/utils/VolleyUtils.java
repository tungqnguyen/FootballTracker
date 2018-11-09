package quangtungnguyen.footballtracker.utils;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import quangtungnguyen.footballtracker.app.AppController;

import static com.android.volley.VolleyLog.TAG;

//https://stackoverflow.com/questions/33535435/how-to-create-a-proper-volley-listener-for-cross-class-volley-method-calling
public class VolleyUtils {

    public static void getJsonObject(Context context, final String url, final VolleyCallback callback) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            System.out.println("current request link: " + url);
                            callback.onSuccess(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error : " + error.getMessage());

                    }
                }){

            /**
             * This is the token for API to not limit our request for data
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("X-Auth-Token", "228f34c151a545118a570e48b38ec256"); //authentication token
                return headers;
            }
        };
        // Access the RequestQueue through singleton class.
        AppController.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }


}