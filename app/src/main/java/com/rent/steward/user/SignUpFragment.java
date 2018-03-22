package com.rent.steward.user;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.rent.steward.R;
import com.rent.steward.general.gui.CustomDialog;

import static com.rent.steward.user.SignUpDialogType.ACCOUNT_DUPLICATED;
import static com.rent.steward.user.SignUpDialogType.ACCOUNT_EMPTY;
import static com.rent.steward.user.SignUpDialogType.SIGN_UP_FINAL_CHECK;
import static com.rent.steward.user.SignUpDialogType.SIGN_UP_SUCCESS;
import static com.rent.steward.user.SignUpDialogType.USER_NAME_EMPTY;
import static com.rent.steward.user.SignUpDialogType.USER_NAME_TOO_SHORT;

/**
 * Created by Corth1545617 on 2017/5/4.
 */

public class SignUpFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "SignUpFragment";

    /*** UI ***/
    private EditText account_et;
    private EditText name_et;
    private DatePicker birth_datePicker;
    private Button signup_btn;

    public SignUpFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        findViewByRId(view);

        return view;
    }

    private void findViewByRId(View view){
        account_et = (EditText) view.findViewById(R.id.account_et);
        name_et = (EditText) view.findViewById(R.id.name_et);
        birth_datePicker = (DatePicker) view.findViewById(R.id.birth_datePicker);
        signup_btn = (Button) view.findViewById(R.id.enter_signup);
        signup_btn.setOnClickListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.enter_signup:
                checkInfoToSignUp();
                break;
        }
    }

    private void checkInfoToSignUp(){
        String account = account_et.getText().toString();
        String userName = name_et.getText().toString();
        if (account.isEmpty())
            getDialogBeforeSignUp(ACCOUNT_EMPTY).show();
        else if (checkAccountDuplicate(account))
            getDialogBeforeSignUp(ACCOUNT_DUPLICATED).show();
        else if (userName.isEmpty())
            getDialogBeforeSignUp(USER_NAME_EMPTY).show();
        else if (userName.length() < 2)
            getDialogBeforeSignUp(USER_NAME_TOO_SHORT).show();
        else {
            getDialogBeforeSignUp(SIGN_UP_FINAL_CHECK).show();
        }
    }

    private boolean checkAccountDuplicate(String account){
        Person person = new PersonInfoDAO(getActivity()).findByAccount(account);
        return person != null;
    }

    private CustomDialog getDialogBeforeSignUp(int op){
        CustomDialog customDialog;
        String title = getString(R.string.dialog_signup_title);
        String warning = "";
        boolean hasCancel = false;
        CustomDialog.CustomDialogClickEvent dialogClickEvent = null;

        switch (op) {
            case ACCOUNT_EMPTY:
                warning = getString(R.string.dialog_signup_warning_account_empty);
                break;
            case ACCOUNT_DUPLICATED:
                warning = getString(R.string.dialog_signup_warning_account_duplicate);
                break;
            case USER_NAME_EMPTY:
                warning = getString(R.string.dialog_signup_warning_name_empty);
                break;
            case USER_NAME_TOO_SHORT:
                warning = getString(R.string.dialog_signup_warning_name_short);
                break;
            case SIGN_UP_FINAL_CHECK:
                warning = getString(R.string.dialog_signup_warning);
                hasCancel = true;
                dialogClickEvent = new CustomDialog.CustomDialogClickEvent() {
                    @Override
                    public void onCDClickOK() {
                        signUpPersonInfo();
                        getDialogBeforeSignUp(SIGN_UP_SUCCESS).show();
                        getActivity().getSupportFragmentManager().popBackStack();
                        onDestroy();
                    }

                    @Override
                    public void onCDClickCancel() {

                    }
                };
                break;
            case SIGN_UP_SUCCESS:
                warning = getString(R.string.dialog_signup_warning_success);
                break;
            default:
                break;
        }

        customDialog = new CustomDialog(getActivity(), title, warning, hasCancel);
        if (dialogClickEvent != null)
            customDialog.setCustomDialogClickEvent(dialogClickEvent);

        return customDialog;
    }

    private void signUpPersonInfo(){

        String account = account_et.getText().toString();
        String userName = name_et.getText().toString();
        String userBirth = getBirth();

        Person person = new Person(account, userName, userBirth, "f");

        // Add a new person record
//        ContentValues values = new ContentValues();
//        values.put(PersonInfoDAO.NAME, userName);
//        values.put(PersonInfoDAO.BIRTHDAY, userBirth);

//        Uri uri = getActivity().getContentResolver().insert(PersonInfoEntry.CONTENT_URI, values);
        person = new PersonInfoDAO(getContext()).insert(person);
        Log.d(TAG, "Inserted: " + person.toString());
    }

    private String getBirth(){
        // 取得DatePicker並轉換字串
        int year = birth_datePicker.getYear();
        int month = birth_datePicker.getMonth()+1;
        int day = birth_datePicker.getDayOfMonth();
        return year + "-" + month + "-" + day;
    }

}
