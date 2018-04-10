package net.bucssa.buassist.Ui.Fragments.Mine;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import net.bucssa.buassist.Base.BaseFragment;
import net.bucssa.buassist.R;
import net.bucssa.buassist.Ui.Login.LoginActivity;
import net.bucssa.buassist.Ui.Settings.SettingActivity;
import net.bucssa.buassist.UserSingleton;
import net.bucssa.buassist.Util.ImageUtils;
import net.bucssa.buassist.Util.ToastUtils;

import java.io.File;
import java.util.Date;

import butterknife.BindView;

/**
 * Created by KimuraShin on 17/8/20.
 */

public class MineFragment extends BaseFragment {


    @BindView(R.id.ll_editProfile)
    LinearLayout ll_editProfile;

    @BindView(R.id.rl_logout)
    RelativeLayout rl_logout;

    @BindView(R.id.rl_myFriend)
    RelativeLayout rl_myFriend;

    @BindView(R.id.rl_myCollection)
    RelativeLayout rl_myCollection;

    @BindView(R.id.rl_setting)
    RelativeLayout rl_setting;

    @BindView(R.id.iv_profile)
    ImageView iv_profile;

    @BindView(R.id.profileBar)
    LinearLayout profileBar;

    @BindView(R.id.tv_username)
    TextView tv_username;

    @BindView(R.id.tv_college)
    TextView tv_college;

    @BindView(R.id.iv_sexDisplay)
    ImageView iv_sexDisplay;

    @BindView(R.id.tv_dateOfBirth)
    TextView tv_dateOfBirth;

    @BindView(R.id.tv_self_intro)
    TextView tv_selfIntro;

    @BindView(R.id.tv_loveStatus)
    TextView tv_loveStatus;

    @BindView(R.id.tv_email)
    TextView tv_email;

    private final int LOGIN = 111;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected String getTAG() {
        return this.toString();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!UserSingleton.getInstance().isLogin(context)) {
                    Intent intent = new Intent(context, LoginActivity.class);
                    intent.putExtra("preActivity", 1);
                    startActivityForResult(intent, LOGIN);
                }
            }
        });

        rl_logout.setVisibility(View.GONE);
        profileBar.setVisibility(View.GONE);
        rl_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.logout(context);
                Intent intent = new Intent(context, LoginActivity.class);
                intent.putExtra("preActivity", 1);
                startActivityForResult(intent, LOGIN);
            }
        });

        ll_editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserSingleton.getInstance().isLogin(context)) {
                    Intent intent = new Intent(getActivity(), ProfileActivity.class);
                    startActivity(intent);
                } else {
                    ToastUtils.showToast(context, "请先登录后再查看个人信息哦~");
                }
            }
        });

        if (UserSingleton.getInstance().isLogin(context)) {
            rl_logout.setVisibility(View.VISIBLE);
            setProfileValues();
        }

        rl_myFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyFriendActivity.launch((Activity) context);
            }
        });

        rl_myCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyCollectionActivity.launch((Activity) context);
            }
        });

        rl_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingActivity.launch((Activity) context);
            }
        });

    }

    private void setProfileValues() {
        Glide.with(context)
                .asBitmap()
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.DATA))
                .load(UserSingleton.bigAvatar)
                .into(iv_profile);
        tv_username.setText(UserSingleton.USERINFO.getUsername());
        tv_college.setText(UserSingleton.USERINFO.getCollege());
        tv_dateOfBirth.setText(UserSingleton.USERINFO.getDateOfBirth());
        tv_loveStatus.setText(UserSingleton.USERINFO.getAffectivestatus());
        tv_selfIntro.setText(UserSingleton.USERINFO.getBio());
        switch (UserSingleton.USERINFO.getGender()) {
            case 0:
                iv_sexDisplay.setVisibility(View.GONE);
            case 1:
                iv_sexDisplay.setSelected(true);
                break;
            case 2:
                iv_sexDisplay.setSelected(false);
                break;
        }
    }

    private static final int REQUEST_SELECT_PICTURE = 0x01;
    private static final String SAMPLE_CROPPED_IMAGE_NAME = "SampleCropImage.png";



    private void pickFromGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
                    getString(R.string.permission_read_storage_rationale),
                    REQUEST_STORAGE_READ_ACCESS_PERMISSION);
        } else {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(Intent.createChooser(intent, getString(R.string.label_select_picture)), REQUEST_SELECT_PICTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == -1) {
            switch (requestCode) {
                case LOGIN:
                    rl_logout.setVisibility(View.VISIBLE);
                    Picasso.with(context).load(UserSingleton.bigAvatar).error(R.drawable.profile_photo).into(iv_profile);
                    break;
                case REQUEST_SELECT_PICTURE:
                    final Uri selectedUri = data.getData();
                    if (selectedUri != null) {
                        startCropActivity(data.getData());
                    } else {
                        ToastUtils.showToast(context, "照片读取出错！");
                    }
                    break;
                case UCrop.REQUEST_CROP:
                    handleCropResult(data);
                    break;
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            handleCropError(data);
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_STORAGE_READ_ACCESS_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickFromGallery();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void handleCropResult(@NonNull Intent result) {
        final Uri resultUri = UCrop.getOutput(result);
        if (resultUri != null) {
            iv_profile.setImageURI(resultUri);
            Bitmap bitmap = ImageUtils.getBitmapFromUri(context, resultUri);
        } else {
            ToastUtils.showToast(context, "读取图片错误");
        }
    }

    private void handleCropError(@NonNull Intent result) {
        final Throwable cropError = UCrop.getError(result);
        if (cropError != null) {
            Log.e(getTAG(), "handleCropError: ", cropError);
            Toast.makeText(context, cropError.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "不明错误~呜呜呜", Toast.LENGTH_SHORT).show();
        }
    }

    private void startCropActivity(@NonNull Uri uri) {
        Date time = new Date();
        String destinationFileName = String.valueOf(time.getTime())+".png";

        UCrop.Options options = new UCrop.Options();
        options.setCompressionFormat(Bitmap.CompressFormat.PNG);
        options.setHideBottomControls(true);
        options.setFreeStyleCropEnabled(false);


        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(((Activity) context).getCacheDir(), destinationFileName)));
        uCrop.withAspectRatio(1, 1)
                .withMaxResultSize(800, 800);

        uCrop.withOptions(options);
        uCrop.start((Activity) context, this, UCrop.REQUEST_CROP);
    }




    protected static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101;
    protected static final int REQUEST_STORAGE_WRITE_ACCESS_PERMISSION = 102;

    private AlertDialog mAlertDialog;


    /**
     * Requests given permission.
     * If the permission has been denied previously, a Dialog will prompt the user to grant the
     * permission, otherwise it is requested directly.
     */
    protected void requestPermission(final String permission, String rationale, final int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permission)) {
            showAlertDialog(getString(R.string.permission_title_rationale), rationale,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context,
                                    new String[]{permission}, requestCode);
                        }
                    }, getString(R.string.label_ok), null, getString(R.string.label_cancel));
        } else {
            ActivityCompat.requestPermissions((Activity) context, new String[]{permission}, requestCode);
        }
    }

    /**
     * This method shows dialog with given title & message.
     * Also there is an option to pass onClickListener for positive & negative button.
     *
     * @param title                         - dialog title
     * @param message                       - dialog message
     * @param onPositiveButtonClickListener - listener for positive button
     * @param positiveText                  - positive button text
     * @param onNegativeButtonClickListener - listener for negative button
     * @param negativeText                  - negative button text
     */
    protected void showAlertDialog(@Nullable String title, @Nullable String message,
                                   @Nullable DialogInterface.OnClickListener onPositiveButtonClickListener,
                                   @NonNull String positiveText,
                                   @Nullable DialogInterface.OnClickListener onNegativeButtonClickListener,
                                   @NonNull String negativeText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positiveText, onPositiveButtonClickListener);
        builder.setNegativeButton(negativeText, onNegativeButtonClickListener);
        mAlertDialog = builder.show();
    }

}
