package com.depex.okeyclick.user.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.depex.okeyclick.user.GlideApp;
import com.depex.okeyclick.user.R;
import com.depex.okeyclick.user.api.ProjectAPI;
import com.depex.okeyclick.user.contants.Utils;
import com.depex.okeyclick.user.factory.StringConvertFactory;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.stripe.android.model.Card;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ProfileFragment extends Fragment implements View.OnClickListener {

    private static final int PICK_IMAGE = 1;
    private static final int CAMERA_REQUEST_CODE = 2;
    @BindView(R.id.update_image)
    ImageView updateImageView;

    @BindView(R.id.update_image_click)
    ImageView updateImageViewClick;
    private Menu menu;

    @BindView(R.id.first_name_label)
    TextView firstNameLabel;

    @BindView(R.id.firstname)
    TextView firstName;

    @BindView(R.id.first_name_input)
    TextInputLayout firstNameInput;

    @BindView(R.id.last_name_label)
    TextView lastNameLabel;

    @BindView(R.id.last_name)
    TextView lastName;

    @BindView(R.id.last_name_input)
    TextInputLayout lastNameInput;

    @BindView(R.id.email)
    TextView email;

    @BindView(R.id.mobile)
    TextView mobile;


    private Context context;

    SharedPreferences preferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.content_view_profile, container, false);
        ButterKnife.bind(this, view);
        preferences=context.getSharedPreferences(Utils.SERVICE_PREF, Context.MODE_PRIVATE);
        updateImageViewClick.setOnClickListener(this);
        initScreen();
        return view;
    }

    private void initScreen() {
        new Retrofit.Builder()
                .baseUrl(Utils.SITE_URL)
                .addConverterFactory(new StringConvertFactory())
                .build()
                .create(ProjectAPI.class)
                .getServiceProviderDetails(getString(R.string.apikey), preferences.getString("user_id", "0"))
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String responseString=response.body();
                        Log.i("responseData", "View Profile Fragment : "+responseString);
                        try {
                            JSONObject res=new JSONObject(responseString);
                            boolean success=res.getBoolean("successBool");
                            if(success){
                                JSONObject resObj=res.getJSONObject("response");
                                JSONObject userObj=resObj.getJSONObject("List");
                                String firstNameStr=userObj.getString("first_name");
                                firstName.setText(firstNameStr);
                                firstNameInput.getEditText().setText(firstNameStr);
                                String lastNameStr=userObj.getString("last_name");
                                lastName.setText(lastNameStr);
                                String emailStr=userObj.getString("email");
                                String mobileStr=userObj.getString("mobile");
                                email.setText(emailStr);
                                mobile.setText(mobileStr);
                                lastNameInput.getEditText().setText(lastNameStr);
                                String url=userObj.getString("user_images");
                                GlideApp.with(context).load(url).circleCrop().into(updateImageView);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                            Log.e("responseDataError", "Profile Fragment : "+t.toString());
                    }
                });

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.update_image_click:
                showBottomSheet();
                break;
            case R.id.camera_btn:
                pickFromCamera();
                if(dialog!=null){
                    dialog.dismiss();
                }
                break;
            case R.id.gallery_btn:
                pickFromGallery();
                if(dialog!=null){
                    dialog.dismiss();
                }
                break;

        }
    }

    private void pickFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);

        /*
        new MaterialFilePicker()
                .withActivity(getActivity())
                .withRequestCode(PICK_IMAGE)

                .withRootPath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath())
                .withFilter(Pattern.compile(".*\\.jpg$")) // Filtering files and directories by file name using regexp
                .withFilterDirectories(false) // Set directories filterable (false by default)
                .withHiddenFiles(true) // Show hidden files and folders
                .start();*/

    }

    private void pickFromCamera() {

        Intent takingPhotoIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takingPhotoIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takingPhotoIntent, CAMERA_REQUEST_CODE);
        }

    }
    BottomSheetDialog dialog;

    private void showBottomSheet() {
        dialog=new BottomSheetDialog(context);
        View view1=LayoutInflater.from(context).inflate(R.layout.content_bottom_sheet_layout_profile_fragment, null, false);
        Button camera=view1.findViewById(R.id.camera_btn);
        Button gallery=view1.findViewById(R.id.gallery_btn);
        camera.setOnClickListener(this);
        gallery.setOnClickListener(this);
        dialog.setContentView(view1);
        dialog.show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.profile_menu, menu);
        this.menu=menu;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.edit_profile_menu:
                   // updateImageViewClick.setVisibility(View.VISIBLE);
                    setVisibility(View.VISIBLE,  firstNameInput, lastNameInput);
                    setVisibility(View.GONE, firstName, firstNameLabel, lastNameLabel, lastName);
                    item.setVisible(false);
                    menu.findItem(R.id.done_profile_menu).setVisible(true);
                break;

            case R.id.done_profile_menu:
                setVisibility(View.GONE, firstNameInput, lastNameInput);
                setVisibility(View.VISIBLE, firstName, firstNameLabel, lastNameLabel, lastName);
                    item.setVisible(false);
                    menu.findItem(R.id.edit_profile_menu).setVisible(true);
                    doneEdit();
                break;
        }
        return true;
    }

    private void doneEdit() {

        JSONObject requestData=new JSONObject();
        JSONObject data=new JSONObject();

        try {
            data.put("v_code", getString(R.string.v_code));
            data.put("apikey", getString(R.string.apikey));
            data.put("userToken", preferences.getString("userToken", "0"));
            data.put("user_id", preferences.getString("user_id", "0"));
            data.put("first_name", firstNameInput.getEditText().getText().toString());
            data.put("last_name", lastNameInput.getEditText().getText().toString());
            requestData.put("RequestData", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Retrofit.Builder()
                    .baseUrl(Utils.SITE_URL)
                    .addConverterFactory(new StringConvertFactory())
                    .build()
                    .create(ProjectAPI.class)
                    .updateUserDetails(requestData.toString())
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String responseString =response.body();
                        Log.i("responseData", "Profile Fragment : "+responseString);

                        JSONObject res= null;
                        try {
                            res = new JSONObject(responseString);
                            boolean success=res.getBoolean("successBool");
                            if(success){
                                lastName.setText(lastNameInput.getEditText().getText().toString());
                                firstName.setText(firstNameInput.getEditText().getText().toString());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.e("responseDataError", "Profile Fragment Error : "+t.toString());
                    }
                });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }


    private void setVisibility(int visibility, View... views){
        for(View view : views){
            view.setVisibility(visibility);
        }
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode== Activity.RESULT_OK && requestCode==PICK_IMAGE){

            Uri selectedImage = data.getData();
            GlideApp.with(context).load(selectedImage).circleCrop().into(updateImageView);
            GlideApp.with(context).asFile().load(selectedImage).into(new SimpleTarget<File>() {
                @Override
                public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                    Log.i("responseData", "Profile Fragment File Path : "+resource.getPath());
                }
            });
            String filePath=getRealPathFromURI(context, selectedImage);
            Log.i("responseData", "Profile Fragment File Path : "+filePath);
            Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);
            //updateImageView.setImageBitmap(yourSelectedImage);


        }
        if(resultCode== Activity.RESULT_OK && requestCode==CAMERA_REQUEST_CODE){
            Bitmap bitmap=(Bitmap)data.getExtras().get("data");
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            try {
                File image = File.createTempFile(
                        imageFileName,  /* prefix */
                        ".jpg",         /* suffix */
                        storageDir      /* directory */
                );
                FileOutputStream fou=new FileOutputStream(image);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fou);
                GlideApp.with(context).load(image).circleCrop().into(updateImageView);
                createFileUploadInRetrofit2(image);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    private void createFileUploadInRetrofit2(File file) {
        String contentType= MimeTypeMap.getFileExtensionFromUrl(file.getAbsolutePath());
        String mimeType=MimeTypeMap.getSingleton().getMimeTypeFromExtension(contentType);
        RequestBody requestBody =RequestBody.create(MediaType.parse(mimeType), file);
        MultipartBody.Part image = MultipartBody.Part.createFormData("image",file.getName(), requestBody);
        RequestBody v_code=RequestBody.create(MultipartBody.FORM, getString(R.string.v_code));
        RequestBody apikey=RequestBody.create(MultipartBody.FORM, getString(R.string.apikey));
        RequestBody userToken=RequestBody.create(MultipartBody.FORM, preferences.getString("userToken", "0"));
        RequestBody user_id=RequestBody.create(MultipartBody.FORM, preferences.getString("user_id", "0"));
        new Retrofit.Builder()
                .baseUrl(Utils.SITE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ProjectAPI.class)
                .upload(v_code, apikey, image, userToken, user_id)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        ResponseBody responseBody=response.body();
                        try {
                            InputStream io=responseBody.byteStream();
                            BufferedReader reader=new BufferedReader(new InputStreamReader(io));
                            String line;
                            while ((line=reader.readLine())!=null){
                                Log.i("inputStreamReader", line);
                            }
                        } catch (Exception e) {
                            Log.e("responseDataError", "CommentActivity : "+e.toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("responseDataError", "Comment Activity : "+ t.toString());
                    }
                });
    }
}