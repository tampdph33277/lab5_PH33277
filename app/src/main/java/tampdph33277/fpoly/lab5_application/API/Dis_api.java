package tampdph33277.fpoly.lab5_application.API;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import tampdph33277.fpoly.lab5_application.DTO.Distributor_DTO;
import retrofit2.http.Query;
public interface Dis_api {

    @PUT("distributors/{id}")
    Call<Distributor_DTO> updateDistributor(@Path("id") String distributorId, @Body Distributor_DTO distributor);

    @DELETE("distributors/{id}")
    Call<Void> deleteDistributor(@Path("id") Long distributorId);

    @GET("distributors")
    Call<List<Distributor_DTO>> getDistributors();

    @GET("distributors/search")
    Call<List<Distributor_DTO>> searchDistributors(@Query("name") String searchKeyword);

    @POST("distributors")
    Call<Distributor_DTO> addDistributor(@Body Distributor_DTO distributor);}
