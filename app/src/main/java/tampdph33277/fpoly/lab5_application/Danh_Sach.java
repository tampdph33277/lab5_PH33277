package tampdph33277.fpoly.lab5_application;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tampdph33277.fpoly.lab5_application.API.Dis_api;
import tampdph33277.fpoly.lab5_application.Adapter.Distributor_Adapter;
import tampdph33277.fpoly.lab5_application.DTO.Distributor_DTO;




public class Danh_Sach extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Distributor_Adapter adapter;

    SearchView seachview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach);
        recyclerView = findViewById(R.id.recyclerViewStudents);
        ImageView img_cuon = findViewById(R.id.img_cuon);
        seachview = findViewById(R.id.seachview);
        nodchanglist();
        seachview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                performSearch(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Không cần xử lý ở đây vì bạn muốn tìm kiếm sau khi người dùng nhấn enter
                return false;
            }
        });
        img_cuon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }


    private void nodchanglist() {
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
                    List<Distributor_DTO> studentList = response.body();
                    adapter = new Distributor_Adapter(Danh_Sach.this, studentList);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(Danh_Sach.this));
                } else {
                    Toast.makeText(Danh_Sach.this, "Không thể lấy dữ liệu từ máy chủ", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<Distributor_DTO>> call, Throwable t) {
                Toast.makeText(Danh_Sach.this, "Đã xảy ra lỗi: " , Toast.LENGTH_SHORT).show();
                Log.e("Danh_Sach", "Đã xảy ra lỗi: " + t.getMessage(), t);
            }
        });
    }
    public void showDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Danh_Sach.this);
        LayoutInflater inflater = LayoutInflater.from(Danh_Sach.this);
        View dialogView = inflater.inflate(R.layout.dialog_add_danhsach, null);

        dialogBuilder.setView(dialogView);

        EditText editTextName = dialogView.findViewById(R.id.editTextName_add);
//        EditText editTextStudentId = dialogView.findViewById(R.id.editTextStudentId_add);
//        EditText editTextStudentDtb = dialogView.findViewById(R.id.editTextStudentDtb_add);

       // ImageView img_avatar = dialogView.findViewById(R.id.img_avatar_add);
      //  Button buttonChooseImage = dialogView.findViewById(R.id.buttonChooseImage_add);
        Button buttonSave = dialogView.findViewById(R.id.buttonSave_add);
        AlertDialog alertDialog = dialogBuilder.create();

        // Set onClickListener for the button to choose image




        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get student information from EditTexts
                String name = editTextName.getText().toString();
               // String studentId = editTextStudentId.getText().toString();
            //    String diemtb = editTextStudentDtb.getText().toString();
                if (name.isEmpty() ) {
                    Toast.makeText(Danh_Sach.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return; // Thoát khỏi phương thức nếu có trường thông tin trống
                }
                Distributor_DTO newItem = new Distributor_DTO();
                newItem.setName(name);
                addStudentToDatabase(newItem);
                alertDialog.dismiss();
                // Kiểm tra xem diemtb có phải là số hay không

            }
        });

        alertDialog.show();
    }
    private void addStudentToDatabase(Distributor_DTO distributor) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.2.102:3004/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Dis_api api = retrofit.create(Dis_api.class);
        Call<Distributor_DTO> call = api.addDistributor(distributor);

        call.enqueue(new Callback<Distributor_DTO>() {
            @Override
            public void onResponse(Call<Distributor_DTO> call, Response<Distributor_DTO> response) {
                if (response.isSuccessful()) {
                    // Handle successful addition of student
                    Distributor_DTO addedStudent = response.body();
                    Toast.makeText(Danh_Sach.this, "Thêm sinh viên thành công", Toast.LENGTH_SHORT).show();
                    // Refresh student list or perform other necessary actions
                    nodchanglist();
                } else {
                    // Handle unsuccessful addition of student
                    Toast.makeText(Danh_Sach.this, "Thêm sinh viên không thành công", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Distributor_DTO> call, Throwable t) {
                // Handle connection failure or request processing failure
                Toast.makeText(Danh_Sach.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void performSearch(String query) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.2.102:3004/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Dis_api api = retrofit.create(Dis_api.class);
        Call<List<Distributor_DTO>> call = api.searchDistributors(query);

        call.enqueue(new Callback<List<Distributor_DTO>>() {
            @Override
            public void onResponse(Call<List<Distributor_DTO>> call, Response<List<Distributor_DTO>> response) {
                if (response.isSuccessful()) {
                    List<Distributor_DTO> searchResult = response.body();
                    if (searchResult != null && !searchResult.isEmpty()) {
                        adapter = new Distributor_Adapter(Danh_Sach.this, searchResult);
                        recyclerView.setAdapter(adapter);
                    } else {
                        Toast.makeText(Danh_Sach.this, "Không tìm thấy kết quả phù hợp", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("API Error", "Failed to get search results: " + response.message());
                    Toast.makeText(Danh_Sach.this, "Đã xảy ra lỗi khi tìm kiếm", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Distributor_DTO>> call, Throwable t) {
                Log.e("API Error", "Search request failed: " + t.getMessage(), t);
                Toast.makeText(Danh_Sach.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
