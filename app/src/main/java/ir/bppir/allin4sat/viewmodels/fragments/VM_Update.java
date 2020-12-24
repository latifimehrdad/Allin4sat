package ir.bppir.allin4sat.viewmodels.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Environment;
import androidx.core.content.FileProvider;

import com.yangp.ypwaveview.YPWaveView;

import java.io.File;
import ir.bppir.allin4sat.utility.DownloadTask;
import ir.bppir.allin4sat.viewmodels.VM_Primary;


public class VM_Update extends VM_Primary {


    //______________________________________________________________________________________________ VM_Update
    public VM_Update(Activity context) {
        setContext(context);
    }
    //______________________________________________________________________________________________ VM_Update


    //______________________________________________________________________________________________ downloadFile
    public void downloadFile(String url, String filePath, YPWaveView bar) {

        DownloadTask downloadTask = new DownloadTask(getContext(), filePath, getPublishSubject());
        downloadTask.execute(url);
    }
    //______________________________________________________________________________________________ downloadFile


    //______________________________________________________________________________________________ getTempUri
    public Uri getTempUri(String filename) {
        // Create an image file name
        File imageFile;
        imageFile = new File(Environment.getExternalStorageDirectory()
                + "/allin4sat/", filename);

//        String imagePath = Environment.getExternalStorageDirectory() + "/MyApp/"
//                + "Camera_" + dt + ".jpg";
        Uri imageUri;// = Uri.fromFile(imageFile);

        imageUri = FileProvider.getUriForFile(
                getContext(),
                "ir.bppir.allin4sat.provider", //(use your app signature + ".provider" )
                imageFile);

        return imageUri;
    }
    //______________________________________________________________________________________________ getTempUri


}