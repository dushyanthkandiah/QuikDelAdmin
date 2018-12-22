package Dialogs;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.dushyanth.quikdeladmin.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Fragments.FragmentProducts;
import Models.ClassProducts;
import OtherClasses.ShowDialog;
import OtherClasses.Utils;
import OtherClasses.Validation;
import ServerLink.ServerProduct;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by Dushyanth on 2018-12-15.
 */

@SuppressLint("ValidFragment")
public class DialogProducts extends BaseDialogFragment<DialogProducts.OnDialogFragmentClickListener> {

    private View iView;
    public FragmentProducts fragmentProducts;
    private String type, prdType = "det";
    private EditText txtProductName, txtStock, txtPrice, txtDetails;
    private Button btnSave;
    private Validation vd;
    private TextView lblPrdId;
    private int autoId = 0;
    private Spinner spinnerProductType;
    private ServerProduct serverProduct = new ServerProduct();
    public ClassProducts classProducts;
    public GifImageView progress;

    public DialogProducts(FragmentProducts fragmentProducts, ClassProducts classProducts, String type) {
        this.fragmentProducts = fragmentProducts;
        this.type = type;
        this.classProducts = classProducts;
    }

    public static DialogProducts newInstance(FragmentProducts fragmentProducts, ClassProducts classProducts, String type) {
        DialogProducts dialogSelect = new DialogProducts(fragmentProducts, classProducts, type);
        return dialogSelect;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        iView = inflater.inflate(R.layout.dialog_products, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().getAttributes().windowAnimations = R.style.ForDialogAnim;
        getDialog().setCanceledOnTouchOutside(true);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setRetainInstance(true);

        lblPrdId = iView.findViewById(R.id.lblPrdId);
        txtProductName = iView.findViewById(R.id.txtProductName);
        txtStock = iView.findViewById(R.id.txtStock);
        txtPrice = iView.findViewById(R.id.txtPrice);
        txtDetails = iView.findViewById(R.id.txtDetails);
        progress = iView.findViewById(R.id.progress);
        spinnerProductType = iView.findViewById(R.id.spinnerProductType);
        btnSave = iView.findViewById(R.id.btnSave);

        loadProductType();

        if (type.equals("update")) {
            loadData();
        } else {
            classProducts = new ClassProducts();
            lblPrdId.setVisibility(View.GONE);
            lblPrdId.setText("");
        }

        vd = new Validation(getActivity());

        spinnerProductType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);

                prdType = (String) parent.getItemAtPosition(position);

                if (prdType.equals("Detergent")) {
                    prdType = "det";

                } else {
                    prdType = "sta";
                }

                ((TextView) parent.getSelectedView()).setTextColor(getResources().getColor(R.color.colorPrimary));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });


        return iView;
    }

    @SuppressLint("SetTextI18n")
    void loadData() {
        autoId = classProducts.getId();
        lblPrdId.setText("Product Id - " + classProducts.getId());
        txtProductName.setText("" + classProducts.getName());
        txtStock.setText("" + classProducts.getQuantity());
        txtPrice.setText("" + classProducts.getPrice());
        txtDetails.setText("" + classProducts.getDetails());

        prdType = classProducts.getType();
        String prdName;
        if (prdType.equals("det")) {
            prdName = "Detergent";
        } else {
            prdName = "Stationary";
        }
        Utils.setSpinnerPositionWithoutDash(spinnerProductType, prdName);
    }

    private void validate() {
        String[] fieldName = {"Product Name", "Stock", "Price"};
        EditText[] field = {txtProductName, txtStock, txtPrice};

        if (vd.CheckEmptyText(fieldName, field)) {
            if (Double.parseDouble(txtPrice.getText().toString()) < 1) {
                ShowDialog.showToast(getActivity(), "Please Enter a value more than 0 for Price");
                txtPrice.requestFocus();
            } else
                getFieldValues();
        }
    }

    private void getFieldValues() {
        if (autoId != 0)
            classProducts.setId(autoId);

        classProducts.setName(txtProductName.getText().toString().trim());
        classProducts.setQuantity(Double.parseDouble(txtStock.getText().toString().trim()));
        classProducts.setPrice(Double.parseDouble(txtPrice.getText().toString().trim()));
        classProducts.setType(prdType);

        classProducts.setDetails(txtDetails.getText().toString().trim());


        if (autoId != 0) {
            serverProduct.updateProduct(this);
        } else
            serverProduct.addProduct(this);

    }

    private void loadProductType() {
        String[] sortArray = new String[]{"Detergent", "Stationary"};

        final List<String> sortArrayList = new ArrayList<>(Arrays.asList(sortArray));
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, sortArrayList);

        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerProductType.setAdapter(spinnerArrayAdapter);

    }

    public interface OnDialogFragmentClickListener {

    }
}
