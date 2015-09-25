package appewtc.masterung.ungrestaurant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by masterUNG on 9/25/15 AD.
 */
public class MyAdapter extends BaseAdapter{

    private Context objContext;
    private String[] iconStrings, foodStrings, priceStrings;

    public MyAdapter(Context objContext, String[] iconStrings, String[] foodStrings, String[] priceStrings) {
        this.objContext = objContext;
        this.iconStrings = iconStrings;
        this.foodStrings = foodStrings;
        this.priceStrings = priceStrings;
    }

    @Override
    public int getCount() {
        return foodStrings.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater objLayoutInflater = (LayoutInflater) objContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view1 = objLayoutInflater.inflate(R.layout.food_listview, viewGroup, false);

        //for Title
        TextView foodTextView = (TextView) view1.findViewById(R.id.textView2);
        foodTextView.setText(foodStrings[i]);

        //for Price
        TextView priceTextView = (TextView) view1.findViewById(R.id.textView3);
        priceTextView.setText(priceStrings[i]);

        //for Image
        ImageView iconImageView = (ImageView) view1.findViewById(R.id.imageView2);
       // iconImageView.setImageResource(R.drawable.restaurant);
        Picasso.with(objContext).load(iconStrings[i]).resize(120,120).into(iconImageView);

        return view1;
    }
}   // Main Class
