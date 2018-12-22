package OtherClasses;

import android.content.Context;
import android.widget.EditText;


public class Validation {

    private Context c;

    public Validation(Context c) {
        this.c = c;
    }

    public boolean CheckEmptyText(String[] fieldName, EditText[] field) {
        boolean value = true;
        for (int i = 0; i < field.length; i++) {
            Boolean b = CheckTextEmpty(field[i].getText().toString().trim(), fieldName[i]);
            if (!b) {
                field[i].requestFocus();
                value = false;
                break;
            } else {
                value = true;
            }
        }

        return value;
    }

    // Check if text boxes are empty
    private boolean CheckTextEmpty(String text, String type) {

        if (text.equals("")) {
            ShowDialog.showToast(c, "Please Fill your " + type + " Properly");

            return false;
        } else {
            return true;
        }

    }

    // Method for phone number validation
    public boolean PhoneCheck(String number) {
        if (number.length() < 9) {  // will check the char in phone is less than 9
            ShowDialog.showToast(c, "Phone Number must be more than or equal 9 digits");
            return false;
        } else {

            if (!number.matches("^[0-9\\-]*$")) {
                ShowDialog.showToast(c, "Phone number cannot contain any other symbol other than '-'");
                return false;
            } else {
                return true;
            }
        }
    }

    public boolean PhoneCheckWithoutToast(String number) {
        if (number.length() < 9 || number.length() > 11) {
            return false;
        } else {

            if (!number.matches("^[0-9\\-]*$")) {
                return false;
            } else {
                return true;
            }
        }
    }

    // Method for Email Validation
    public boolean EmailCheck(String email) {

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (!email.matches(emailPattern)) {
            ShowDialog.showToast(c, "Please Re-Check the Email address");
            return false;
        } else {
            return true;
        }

    }


    // Method for password validation
    public boolean PasswordCheck(EditText txtPassword, EditText txtConfirmPassword) {
        String word = txtPassword.getText().toString();
        String confirm = txtConfirmPassword.getText().toString();

        if (word.length() < 6) {
            ShowDialog.showToast(c, "Password should be more than or equal to 6 character");
            return false;
        } else if (!word.equals(confirm)) {
            ShowDialog.showToast(c, "Your Passwords doesn't match each other");
            txtConfirmPassword.requestFocus();
            return false;
        } else {
            return true;
        }


    }

    // Method for password validation
    public boolean PasswordCheck(EditText txtPassword) {
        String word = txtPassword.getText().toString();
        if (word.length() < 6) {
            ShowDialog.showToast(c, "Password should be more than or equal to 6 character");
            return false;
        } else {
            return true;
        }

    }

    // Method for National Identity Card Validation
    public boolean NicCheck(String nic) {

        if (nic.length() != 10 ) {
            ShowDialog.showToast(c, "National Identity card should have exactly 10/12 characters");
            return false;
        } else if (!nic.trim().toLowerCase().matches("^[0-9]{9}[vVxX]$")) {
            ShowDialog.showToast(c, "National Identity Card can have only numbers and letters 'v' or 'x'");
            return false;
        } else if ((Integer.parseInt(nic.substring(2, 5)) > 365 && Integer.parseInt(nic.substring(2, 5)) <= 500) || Integer.parseInt(nic.substring(2, 5)) > 865 || Integer.parseInt(nic.substring(2, 5)) < 1) {
            ShowDialog.showToast(c, "Your NIC Number is Incorrect");
            return false;
        } else {
            return true;
        }

    }

    // Method for Checking 12 digit NIC number
    public boolean NicCheck12(String nic) {

        if (nic.length() != 12) {
            ShowDialog.showToast(c, "National Identity card should have exactly 10/12 characters");
            return false;
        } else if (!nic.trim().toLowerCase().matches("^[0-9]{12}$")) {
            ShowDialog.showToast(c, "National Identity Card can have only numbers and letters 'v' or 'x'");
            return false;
        } else if ((Integer.parseInt(nic.substring(4, 7)) > 365 && Integer.parseInt(nic.substring(4, 7)) <= 500) || Integer.parseInt(nic.substring(4, 7)) > 865  || Integer.parseInt(nic.substring(2, 5)) < 1) {
            ShowDialog.showToast(c, "Your NIC Number is Incorrect");
            return false;
        } else {
            return true;
        }

    }

    // Method to get date of birth from National Identity card Number
    public String getBirthDate(String nic) {
        if (nic.length() > 10){
            nic  = nic.substring(nic.length()-10, nic.length());
        }
        String date1;
        int[] month = { 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

        String mo = "", da = "", yr = nic.substring(0, 2);
        int days = getDays(nic);
        for (int i = 0; i < month.length; i++) {
            if (days < month[i]) {
                mo = (i + 1) + "";
                da = days + "";
                break;
            } else {
                days = days - month[i];
            }
        }

        if (mo.length() == 1) mo = "0" + mo;
        if (da.length() == 1) da = "0" + da;

        date1 = "19" + yr + "-" + mo + "-" + da;

        return date1;

    }

    private int getDays(String id) {
        int d = Integer.parseInt(id.substring(2, 5));
        if (d > 500) {
            return (d - 500);
        } else {
            return d;
        }
    }

    // Method to get gender from National Identity Card
    public String getGender(String nic) {
        if (nic.length() > 10){
            nic  = nic.substring(nic.length()-10, nic.length());
        }
        int d = Integer.parseInt(nic.substring(2, 5));
        if (d > 500) {
            return "Female";
        } else {
            return "Male";
        }
    }


}
