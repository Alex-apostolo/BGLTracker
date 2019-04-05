package com.alexapostolopoulos.bgltracker;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.Date;
import java.util.GregorianCalendar;

public class DateField
{
    View dateView;
    EditText[] txtFields;
    public DateField(View dateView)
    {
        this.dateView = dateView;
        txtFields = new EditText[6];

        txtFields[0] = dateView.findViewById(R.id.dateTime_day);
        txtFields[1] = dateView.findViewById(R.id.dateTime_month);
        txtFields[2] = dateView.findViewById(R.id.dateTime_year);
        txtFields[3] = dateView.findViewById(R.id.dateTime_hour);
        txtFields[4] = dateView.findViewById(R.id.dateTime_minute);
        txtFields[5] = dateView.findViewById(R.id.dateTime_second);
    }

    public void initEvents()
    {
        txtFields[0].setOnFocusChangeListener(new DateFocusListener(txtFields[0],"dd"));
        txtFields[0].addTextChangedListener(new DateTextListener(2,0));

        txtFields[1].setOnFocusChangeListener(new DateFocusListener(txtFields[1],"mm"));
        txtFields[1].addTextChangedListener(new DateTextListener(2,1));

        txtFields[2].setOnFocusChangeListener(new DateFocusListener(txtFields[2],"yyyy"));
        txtFields[2].addTextChangedListener(new DateTextListener(4,2));

        txtFields[3].setOnFocusChangeListener(new DateFocusListener(txtFields[3],"hh"));
        txtFields[3].addTextChangedListener(new DateTextListener(2,3));

        txtFields[4].setOnFocusChangeListener(new DateFocusListener(txtFields[4],"mm"));
        txtFields[4].addTextChangedListener(new DateTextListener(2,4));

        txtFields[5].setOnFocusChangeListener(new DateFocusListener(txtFields[5],"ss"));
        txtFields[5].addTextChangedListener(new DateTextListener(2,5));
    }

    public static int getDaysInMonth(int year, int month) {
        int[] months = new int[12];
        if (year % 4 == 0) {
            months[0] = 31;
            months[1] = 29;
            months[2] = 31;
            months[3] = 30;
            months[4] = 31;
            months[5] = 30;
            months[6] = 31;
            months[7] = 31;
            months[8] = 30;
            months[9] = 31;
            months[10] = 30;
            months[11] = 31;
        } else {
            months[0] = 31;
            months[1] = 28;
            months[2] = 31;
            months[3] = 30;
            months[4] = 31;
            months[5] = 30;
            months[6] = 31;
            months[7] = 31;
            months[8] = 30;
            months[9] = 31;
            months[10] = 30;
            months[11] = 31;
        }
        return months[month - 1];
    }

    public void showDate(Date date)
    {
        String strDate = BGLMain.sqlDateFormat.format(date);
        String[] arrDate = new String[6];
        int strPos = 0;
        int arrPos = 0;
        char[] charDate = strDate.toCharArray();
        for(int rPos = 0; rPos < 6; rPos++)
        {
            arrDate[rPos] = "";
        }
        while(true)
        {
            char curChar = charDate[strPos];
            if(curChar == '-' || curChar == ':' || curChar == ' ')
            {
                arrPos++;
                Log.d("Block position",String.valueOf(arrPos));
            }
            else
            {
                arrDate[arrPos] += curChar;
            }

            if(strPos == 18)
            {
                break;
            }
            strPos++;
        }
        for(int i = 0; i < 6; i++)
        {
            Log.d("Date part:",arrDate[i]);
        }

        txtFields[0].setText(arrDate[2]);
        txtFields[1].setText(arrDate[1]);
        txtFields[2].setText(arrDate[0]);
        txtFields[3].setText(arrDate[3]);
        txtFields[4].setText(arrDate[4]);
        txtFields[5].setText(arrDate[5]);


    }

    public Date parseDate() {
        int defCount = 0;
        for(int fPos = 0; fPos < 6; fPos++)
        {
            if(txtFields[fPos].getText().toString().equals(((DateFocusListener)txtFields[fPos].getOnFocusChangeListener()).getDefaultText()))
            {
                defCount++;
            }
        }
        if(defCount == 6)
        {
            return new Date();
        }
        else if (defCount > 0)
        {
            return null;
        }

        for(int fPos = 0; fPos < 2; fPos++)
        {
            if (txtFields[fPos].getText().length() > 2)
            {
                return null;
            }
        }
        if(txtFields[2].getText().length() < 4)
        {
            return null;
        }
        for(int fPos = 3; fPos < 6; fPos++)
        {
            if (txtFields[fPos].getText().length() > 2)
            {
                return null;
            }
        }

        int dayValue = Integer.parseInt(txtFields[0].getText().toString());
        int monValue = Integer.parseInt(txtFields[1].getText().toString());
        int yearValue = Integer.parseInt(txtFields[2].getText().toString());
        int hourValue = Integer.parseInt(txtFields[3].getText().toString());
        int minValue = Integer.parseInt(txtFields[4].getText().toString());
        int secValue = Integer.parseInt(txtFields[5].getText().toString());

        if(monValue > 12
                || yearValue < 1970
                || dayValue > getDaysInMonth(yearValue,monValue)
                || hourValue == 0
                || minValue == 0
                || secValue == 0
                || monValue == 0
                || hourValue > 23
                || minValue > 60
                || secValue > 60)
        {
            return null;
        }
        Log.d("month",String.valueOf(monValue));

        Date testVal = new GregorianCalendar(yearValue,monValue-1,dayValue,hourValue,minValue,secValue).getTime();
        Log.d("date as parsed",BGLMain.sqlDateFormat.format(testVal));
        return testVal;
    }

    private class DateFocusListener implements View.OnFocusChangeListener
    {
        EditText txtField;
        String defaultText;
        public DateFocusListener(EditText txtField, String defaultText)
        {
            this.txtField = txtField;
            this.defaultText = defaultText;
        }
        public String getDefaultText() {return defaultText;}
        public void onFocusChange(View v, boolean hasFocus)
        {
            if(txtField.hasFocus()) {
                txtField.setText("");
            }
            else if(!txtField.hasFocus() && txtField.getText().length() == 0)
            {
                txtField.setText(defaultText);
            }
        }
    }
    private class DateTextListener implements TextWatcher
    {
        int length;
        int pos;
        public DateTextListener(int length, int pos)
        {
            this.length = length;
            this.pos = pos;
        }
        public void onTextChanged(CharSequence input, int start, int before, int count) {}
        public void beforeTextChanged(CharSequence year, int start, int before, int count) {}
        public void afterTextChanged(Editable text)
        {
            if(pos < 5 && txtFields[pos].length() == length && txtFields[pos].getText().toString().toCharArray()[1] >= '0' && txtFields[pos].getText().toString().toCharArray()[1] <= '9')
            {
                txtFields[pos+1].requestFocus();
            }
        }
    }
}
