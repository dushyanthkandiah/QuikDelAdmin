package OtherClasses;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.Spinner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Utils {

    public static byte[] getImageBytes(Bitmap bitmap, int quality) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, quality, stream);
        return stream.toByteArray();
    }

    public static Bitmap getImage(byte[] image) {

        return BitmapFactory.decodeByteArray(image, 0, image.length);

    }

    public static byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public static byte[] getBytes(InputStream inputStream, ImageView iv) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }

        iv.setImageBitmap(getImage(byteBuffer.toByteArray()));

        return byteBuffer.toByteArray();
    }


    // to set a spinner's position using it's matching name
    public static void setSpinnerPosition(Spinner spinner, String input) {
        Adapter adapter = spinner.getAdapter();

        int n = adapter.getCount();

        ArrayList<String> items = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            String words = adapter.getItem(i) + "";
            String[] strArr = words.split("-");
            String output = strArr[0].trim();

            items.add(output);
        }

        int spinnerPos = 0;
        for (int i = 0; i < items.size(); i++) {
            if (input.equals(items.get(i))) {
                spinnerPos = i;
                break;
            }
        }
        spinner.setSelection(spinnerPos);
    }

    public static void setSpinnerPositionWithoutDash(Spinner spinner, String input) {
        Adapter adapter = spinner.getAdapter();

        int n = adapter.getCount();

        ArrayList<String> items = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            items.add((String) adapter.getItem(i));
        }

        int spinnerPos = 0;
        for (int i = 0; i < items.size(); i++) {
            if (input.equals(items.get(i))) {
                spinnerPos = i;
                break;
            }
        }
        spinner.setSelection(spinnerPos);
    }

}