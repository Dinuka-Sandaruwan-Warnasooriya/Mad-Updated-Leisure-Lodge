package extended.ui.leisurelodgehotelbooking;

import android.widget.Filter;

import java.util.ArrayList;

public class FilterHotel extends Filter {


    private AdapterHotelSeller adapter;
    private ArrayList<ModelHotel> filterList;


    public FilterHotel(AdapterHotelSeller adapter, ArrayList<ModelHotel> filterList) {
        this.adapter = adapter;
        this.filterList = filterList;
    }


    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        if (constraint != null && constraint.length() > 0){

            constraint = constraint.toString().toUpperCase();
            ArrayList<ModelHotel> filteredModels = new ArrayList<>();
            for (int i = 0;i<filterList.size();i++){
                if (filterList.get(i).getHotelTitle().toUpperCase().contains(constraint)||
                        filterList.get(i).getHotelCategory().toUpperCase().contains(constraint)
                ){
                    filteredModels.add(filterList.get(i));
                }
            }
            results.count = filteredModels.size();
            results.values = filteredModels;
        }
        else {
            results.count = filterList.size();
            results.values = filterList;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {

        adapter.hotelList = (ArrayList<ModelHotel>)results.values;
        adapter.notifyDataSetChanged();
    }
}
