package cy.com.talaiporoi.authauto;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MyVehiclesAdapter extends RecyclerView.Adapter<MyVehiclesAdapter.MyViewHolder> {

    private Context mContext;
    private List<Vehicle> vehicleList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, subtitle;
        public ImageView photo;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.text_title);
            subtitle = (TextView) view.findViewById(R.id.text_subtitle);
            photo = (ImageView) view.findViewById(R.id.photo);
            //overflow = (ImageView) view.findViewById(R.id.overflow);
        }
    }


    public MyVehiclesAdapter(Context mContext, List<Vehicle> vehicleList) {
        this.mContext = mContext;
        this.vehicleList = vehicleList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vehicle_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Vehicle vehicle = vehicleList.get(position);
        holder.title.setText(vehicle.getManufacturer());
        holder.subtitle.setText(vehicle.getModel());

        // loading vehicle cover using Glide library
        Glide.with(mContext).load(vehicle.getPhoto()).into(holder.photo);

        /*holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return vehicleList.size();
    }

}
