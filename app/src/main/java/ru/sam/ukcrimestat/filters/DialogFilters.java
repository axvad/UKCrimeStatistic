package ru.sam.ukcrimestat.filters;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;

import android.widget.Switch;
import android.widget.TextView;

import java.util.List;


import ru.sam.ukcrimestat.view_tools.CategoryIcon;
import ru.sam.ukcrimestat.DataModel;
import ru.sam.ukcrimestat.My;
import ru.sam.ukcrimestat.R;

/**
 * Created by sam on 15.10.17.
 */

public class DialogFilters extends AppCompatDialogFragment {

    private RecyclerView listFilters;
    private FiltersAdapter adapterFilters;

    // create dialog and set listener onOk
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_filter_recview, null);

        listFilters = (RecyclerView) v.findViewById(R.id.filter_recycler_view);

        listFilters.setLayoutManager(new LinearLayoutManager(getActivity()));

        Switch sw = (Switch) v.findViewById(R.id.switchAll);

        if (sw != null) {
            sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    //change all categories
                    for (int i = 0; i < adapterFilters.listCrimeCategories.size(); i++) {
                        adapterFilters.listCrimeCategories.get(i).setShow(isChecked);
                    }

                    showFilters();
                }
            });
        }

        showFilters();

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                //.setTitle(R.string.title_dialog_filter)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sendResult(which);
                            }
                })
                .create();

    }

    // send result to activity
    private void sendResult(int which) {

        List<CrimeCategory> result = null;

        if (adapterFilters != null) {
            result = adapterFilters.listCrimeCategories;
        }

        ((FilterSelectListener) getActivity()).onFilterChoosed(result);
    }

    // update view
    public void showFilters(){

        if (My.DEBUG) Log.i(this.getClass().getSimpleName(), String.format("Start show filter. Adapter is %s, size=%d", adapterFilters, (adapterFilters!=null) ? adapterFilters.getItemCount():0));

        // for fisrt dialog call - load data from model;
        if (adapterFilters == null) {

            DataModel model = DataModel.getInstance();
            List<CrimeCategory> crimeCategs = model.getFilterCategories();

            if (crimeCategs.get(0).getUrl().startsWith("all-")) crimeCategs.remove(0);

            if(My.DEBUG) Log.i(this.getClass().getSimpleName(),"From model: "+My.getCrimeListInfo(crimeCategs));

            adapterFilters = new FiltersAdapter(crimeCategs);
        }

        // update view
        listFilters.swapAdapter(adapterFilters,true);
    }

    //call back listener for button switch
    private static interface IFilterCheckClick {

        public void onChangeCheck(int adapterPos, boolean isChecked);
    }

    // represent item in view and init onClick event
    private class FiltersHolder extends RecyclerView.ViewHolder implements  CompoundButton.OnCheckedChangeListener {

        public ImageView iconCategory;
        public TextView titleCategory;
        public TextView descCategory;
        public Switch showCategory;

        private IFilterCheckClick listener;

        public FiltersHolder(View itemView, @NonNull IFilterCheckClick listener) {
            super(itemView);

            iconCategory = (ImageView) itemView.findViewById(R.id.iconCategory);
            titleCategory = (TextView) itemView.findViewById(R.id.titleCategory);
            descCategory = (TextView) itemView.findViewById(R.id.desccriptionCategory);
            showCategory = (Switch) itemView.findViewById(R.id.switchOnOf);

            showCategory.setOnCheckedChangeListener(this);
            this.listener = listener;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            listener.onChangeCheck(this.getAdapterPosition(), isChecked);
        }
    }

    // save data and change it
    private class FiltersAdapter extends RecyclerView.Adapter<FiltersHolder> implements IFilterCheckClick {

        List<CrimeCategory> listCrimeCategories;

        public FiltersAdapter(List<CrimeCategory> listCrimeCategories) {
            this.listCrimeCategories = listCrimeCategories;
        }

        @Override
        public FiltersHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater inflater = LayoutInflater.from(getActivity());

            View item = inflater.inflate(R.layout.dialog_filter_item,parent,false);

            return new FiltersHolder(item, this);
        }

        @Override
        public void onBindViewHolder(FiltersHolder holder, int position) {
            if (listCrimeCategories == null)
                throw new AssertionError();

            CrimeCategory crimeCat = listCrimeCategories.get(position);

            // add icon for category from inner repository, exclude "all..."
            if (!crimeCat.getName().startsWith("All ")) {
                holder.iconCategory.setImageBitmap(
                        new CategoryIcon(getActivity(),crimeCat.getUrl())
                                .getBitmap(true));
            }
            else {
                holder.iconCategory.setImageResource(R.drawable.police_18_2x);
            }

            holder.titleCategory.setText(crimeCat.getName());
            holder.descCategory.setText(crimeCat.getDesc());
            holder.showCategory.setChecked(crimeCat.getShow());
        }

        @Override
        public int getItemCount() {
            return (listCrimeCategories == null) ? 0: listCrimeCategories.size();
        }

        // change 'checked' for CrimeCategory
        @Override
        public void onChangeCheck(int adapterPos, boolean isChecked) {

            CrimeCategory it = listCrimeCategories.get(adapterPos);

            it.setShow(isChecked);
        }
    }
}


