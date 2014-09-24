package com.example.calendar;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class CustomTextWatcher implements TextWatcher {

    boolean bracket = false;
    boolean delete = false;
    boolean changeSelection = false;

    int replacePosition = 0;

    private EditText editText;

    public CustomTextWatcher(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
        // Log.d("beforeTextChanged", "s = " + s + ", count = " + count +
        // ", after = " + after);


        if (editText.getSelectionEnd() != s.length()) {
            changeSelection = true;
            replacePosition = editText.getSelectionEnd();
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // Log.d("onTextChanged", "s = " + s + ", start = " + start +
        // ", before= " + before + ", count = " + count);
        int length = s.length();
        if (length == 0) {
            editText.setText("+");
            editText.setSelection(editText.length());
        } else if (length < 1) {
            bracket = true;
            // Log.e("bracket", "true");
        }
        if (before == 1) {
            delete = true;
        }


    }

    @Override
    public void afterTextChanged(Editable s) {
        int length = s.length();


        if (changeSelection) {
            changeSelection = false;
            editText.setSelection(s.length());

            if (delete) {
                s.clear();
            } else {
                s.delete(replacePosition, replacePosition + 1);
            }
        } else {

            if (bracket) {
                bracket = false;
                s.append("(");
            } else if (!delete) {
                if (length == 7) {
                    s.insert(s.length() - 1, ")");

                }

                if (length == 3) {
                    s.insert(s.length() - 1, "(");
                }

                if (length == 11) {
                    s.insert(s.length() - 1, "-");
                }
                if (length == 14) {
                    s.insert(s.length() - 1, "-");
                }
            } else {

                delete = false;

                if (length == 14) {
                    s.delete(13, 14);
                }
                if (length == 11) {
                    s.delete(10, 11);
                }
                if (length == 7) {
                    s.delete(6, 7);
                }
            }
        }
    }

}