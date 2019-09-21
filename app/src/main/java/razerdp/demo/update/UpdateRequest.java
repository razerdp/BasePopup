package razerdp.demo.update;


import okhttp3.ResponseBody;
import razerdp.basepopup.R;
import razerdp.demo.base.interfaces.SimpleCallback;
import razerdp.demo.update.entity.UpdateInfo;
import razerdp.demo.utils.StringUtil;
import razerdp.demo.utils.gson.GsonUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by 大灯泡 on 2019/9/22.
 * https://fir.im/docs/version_detection
 */
public class UpdateRequest {

    private static final String BASE_URL = "http://api.fir.im";

    public void checkUpdate(final SimpleCallback<UpdateInfo> listener) {
        Update update = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .build()
                .create(Update.class);
        Call<ResponseBody> call = update.checkUpdate(StringUtil.getString(R.string.token));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    UpdateInfo updateInfo = GsonUtil.INSTANCE.toObject(response.body().string(), UpdateInfo.class);
                    if (listener != null) {
                        listener.onCall(updateInfo);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (listener != null) {
                        listener.onCall(null);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (listener != null) {
                    listener.onCall(null);
                }
            }
        });
    }

    interface Update {
        @GET("/apps/latest/5c2db97eca87a86a8a9d2acf")
        Call<ResponseBody> checkUpdate(@Query("api_token") String token);
    }
}
