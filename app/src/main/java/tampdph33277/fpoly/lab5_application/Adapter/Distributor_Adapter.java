package tampdph33277.fpoly.lab5_application.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tampdph33277.fpoly.lab5_application.API.Dis_api;
import tampdph33277.fpoly.lab5_application.DTO.Distributor_DTO;
import tampdph33277.fpoly.lab5_application.R;

public class Distributor_Adapter extends RecyclerView.Adapter<Distributor_Adapter.Viewholder>{
    private Context context;
    private List<Distributor_DTO> list_distributor;

    public Distributor_Adapter(Context context, List<Distributor_DTO> list_distributor) {
        this.context = context;
        this.list_distributor = list_distributor;
    }

    public void setDistributorList(List<Distributor_DTO> newList) {
        this.list_distributor = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Distributor_Adapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_danh_sach, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Distributor_Adapter.Viewholder holder, int position) {
        Distributor_DTO distributor_dto = list_distributor.get(position);
        holder.tv_ten_dis.setText("Name: " + distributor_dto.getName());
        holder.img_edit_dis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                LayoutInflater inflater = LayoutInflater.from(context);
                View dialogView = inflater.inflate(R.layout.dialog_edit_danhsach, null);
                dialogBuilder.setView(dialogView);

                EditText editTextName = dialogView.findViewById(R.id.editTextName);
                Button buttonSave = dialogView.findViewById(R.id.buttonSave);
                AlertDialog alertDialog = dialogBuilder.create();
                editTextName.setText(distributor_dto.getName());

                buttonSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = editTextName.getText().toString();
                        if (name.isEmpty()) {
                            Toast.makeText(context, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Distributor_DTO updatedDistributor = new Distributor_DTO();
                        updatedDistributor.setName(name);

                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl("http://192.168.2.102:3004/")
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        Dis_api api = retrofit.create(Dis_api.class);

                        Call<Distributor_DTO> call = api.updateDistributor(distributor_dto.get_id(), updatedDistributor);

                        call.enqueue(new Callback<Distributor_DTO>() {
                            @Override
                            public void onResponse(Call<Distributor_DTO> call, Response<Distributor_DTO> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(context, "Thành công", Toast.LENGTH_SHORT).show();
                                    notchanglist();
                                    alertDialog.dismiss();
                                } else {
                                    Log.d("ResponseError", "Error body: " + response.errorBody());
                                    Toast.makeText(context, "Lỗi if", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Distributor_DTO> call, Throwable t) {
                                Toast.makeText(context, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();
                            }
                        });
                    }
                });

                alertDialog.show();
            }
        });

        holder.img_xoa_dis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String distributorId = distributor_dto.get_id();
                deleteDistriFromServer(distributorId);
                notchanglist();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list_distributor.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        public TextView tv_id_dis, tv_ten_dis;
        public ImageView img_xoa_dis, img_edit_dis;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            tv_id_dis = itemView.findViewById(R.id.tv_id_dis);
            tv_ten_dis = itemView.findViewById(R.id.tv_ten_dis);
            img_xoa_dis = itemView.findViewById(R.id.img_xoa_dis);
            img_edit_dis = itemView.findViewById(R.id.img_edit_dis);
        }
    }

    private void deleteDistriFromServer(String distributorId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.2.102:3004/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Dis_api api = retrofit.create(Dis_api.class);
        Call<Void> call = api.deleteDistributor(Long.valueOf(distributorId));

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Xóa không thành công", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void notchanglist() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.2.102:3004/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Dis_api api = retrofit.create(Dis_api.class);
        Call<List<Distributor_DTO>> call = api.getDistributors();
        call.enqueue(new Callback<List<Distributor_DTO>>() {
            @Override
            public void onResponse(Call<List<Distributor_DTO>> call, Response<List<Distributor_DTO>> response) {
                if (response.isSuccessful()) {
                    List<Distributor_DTO> updatedList = response.body();
                    setDistributorList(updatedList);
                } else {
                    Toast.makeText(context, "Không thể cập nhật danh sách", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Distributor_DTO>> call, Throwable t) {
                Toast.makeText(context, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
