package Dialogs;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.dushyanth.quikdeladmin.R;

@SuppressLint({"ValidFragment", "NewApi"})
public class DialogAboutDeveloper extends BaseDialogFragment<DialogAboutDeveloper.OnDialogFragmentClickListener> {

    private View iView;
    private TextView lblOk;

    public static DialogAboutDeveloper newInstance() {
        DialogAboutDeveloper dialogSelectProduct = new DialogAboutDeveloper();
        return dialogSelectProduct;
    }

    public DialogAboutDeveloper() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        iView = inflater.inflate(R.layout.dialog_developer, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().getAttributes().windowAnimations = R.style.ForDialogAnim;
        getDialog().setCanceledOnTouchOutside(true);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setRetainInstance(true);

        lblOk = iView.findViewById(R.id.lblOk);

        lblOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return iView;
    }



    public interface OnDialogFragmentClickListener {

    }
}
