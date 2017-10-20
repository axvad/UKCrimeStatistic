package ru.sam.ukcrimestat.filters;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import ru.sam.ukcrimestat.DataModel;
import ru.sam.ukcrimestat.R;

/**
 * Created by sam on 15.10.17.
 */

public class DialogDate extends AppCompatDialogFragment {

    private DatePicker datePicker;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_date, null);

        datePicker = (DatePicker) v.findViewById(R.id.dialog_date_date_picker);

        DataModel model = DataModel.getInstance();

        CrimePeriod period = model.getDate();

        if (period != null){
            datePicker.init(period.getYear(),period.getMonth()-1,1,null);
        }

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                //.setTitle(R.string.title_dialog_date)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int year = datePicker.getYear();
                                int month = datePicker.getMonth()+1;

                                ((FilterSelectListener) getActivity()).onPeriodSelected(new CrimePeriod(year,month));
                            }
                        })
                .create();
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
    }
}
