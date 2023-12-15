package .fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.biometric.BiometricPrompt;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import .Dao.StaffDAO;
import .R;
import .activity.DoanhThuActivity;
import .activity.HoadonActivity;
import .activity.StaffActivity;
import com.google.android.material.textfield.TextInputEditText;

import java.util.concurrent.Executor;

public class FragmentOption extends Fragment {

    private CardView cardViewDoanhThu, cardViewBill, cvStaff;

    public FragmentOption() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cardViewDoanhThu = view.findViewById(R.id.cardViewDoanhThu);
        cardViewBill= view.findViewById(R.id.cardViewBill);
        cvStaff= view.findViewById(R.id.cardViewNhanVien);

        cardViewDoanhThu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DoanhThuActivity.class);
                intent.setAction("AddStaff");
                startActivity(intent);
            }
        });

        cardViewBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HoadonActivity.class);
                startActivity(intent);
            }
        });

        cvStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                LayoutInflater layoutInflater = getLayoutInflater();
                View view1 = layoutInflater.inflate(R.layout.dialog_enter_password, null);
                builder.setView(view1);
                Dialog dialog = builder.create();
                dialog.show();

                //anh xa
                TextInputEditText edtPassword = view1.findViewById(R.id.edtEnterPassword);
                ImageView imgFingerPrint = view1.findViewById(R.id.imgFingerprint);
                TextView tvLogin = view1.findViewById(R.id.tvOkPassword);
                TextView tvChangePassword = view1.findViewById(R.id.tvChangePassword);

                //event
                tvLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String password = edtPassword.getText().toString();
                        StaffDAO staffDAO = new StaffDAO(getContext());
                        boolean check = staffDAO.checkPassword("Quản lý", password);

                        if (check){
                            Intent intent = new Intent(getActivity(), StaffActivity.class);
                            startActivity(intent);
                        }else {
                            edtPassword.setError("Sai mật khẩu");
                        }
                    }
                });

                imgFingerPrint.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Hàm xác thực vân tay
                        fingerPrint(dialog);
                    }
                });

                tvChangePassword.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogChangePassword();
                    }
                });
            }
        });
    }

    private void dialogChangePassword() {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_change_password);

        EditText edtOldPass = dialog.findViewById(R.id.edtOldPassword);
        EditText edtNewPass = dialog.findViewById(R.id.edtNewPassword);
        EditText edtAgainPass = dialog.findViewById(R.id.edtAgainPassword);
        Button btnCancel = dialog.findViewById(R.id.btnCancelChangePassword);
        Button btnChangePass = dialog.findViewById(R.id.btnChangePassword);

        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StaffDAO staffDAO = new StaffDAO(getContext());
                String oldPass = edtOldPass.getText().toString();
                String newPass = edtNewPass.getText().toString();
                String againPass = edtAgainPass.getText().toString();

                if (oldPass.isEmpty() || newPass.isEmpty() || againPass.isEmpty()){
                    edtOldPass.setError("Không để trống ô nào");
                    return;
                }

                boolean check = staffDAO.checkPassword("Quản lý", oldPass);
                if (!check){
                    edtOldPass.setError("Mật khẩu không đúng");
                    return;
                }

                if (!(newPass.equals(againPass))){
                    edtAgainPass.setError("Mật khẩu không giống nhau");
                    return;
                }

                staffDAO.changePassword("Quản lý", newPass);
                Toast.makeText(getContext(), "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void checkError() {

    }

    // Xác thực vân tay
    private void fingerPrint(Dialog dialog) {
        Executor executor = ContextCompat.getMainExecutor(getContext());
        BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor,
                new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                        super.onAuthenticationError(errorCode, errString);
                    }

                    @Override
                    public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        // Intent đến StaffActivity nếu đúng vân tay
                        dialog.dismiss();
                        Intent intent = new Intent(getActivity(), StaffActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                    }
                });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Xác thực người dùng")
                .setDescription("Quét vân tay để xác thực danh tính của bạn")
                .setNegativeButtonText("Thoát") .build();
        biometricPrompt.authenticate(promptInfo);

    }

    public static FragmentOption newInstance(String param1, String param2) {
        FragmentOption fragment = new FragmentOption();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_option, container, false);
    }
}