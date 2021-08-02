package net.simplifiedlearning.logbooksystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;


public class ProductsAdapter2 extends RecyclerView.Adapter<ProductsAdapter2.ProductViewHolder> {


    private Context mCtx;
    private List<Product2> productList2;





    public ProductsAdapter2(Context mCtx, List<Product2> productList2) {
        this.mCtx = mCtx;
        this.productList2 = productList2;


    }



    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.logbook_list, null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Product2 product2 = productList2.get(position);

        //loading the image


        holder.textViewTitle.setText(product2.getTitle());
        holder.textViewShortDesc.setText(product2.getShortdesc());
        holder.textViewRating.setText(String.valueOf(product2.getRating()));
        holder.textViewPrice.setText(String.valueOf(product2.getPrice()));
        holder.textViewPrice2.setText(String.valueOf(product2.getPrice()));
    }

    @Override
    public int getItemCount() {
        return productList2.size();
    }

    public interface RecyclerViewClickListener{
        void onClick(View v , int position);
    }

    class ProductViewHolder extends RecyclerView.ViewHolder   {

        TextView textViewTitle, textViewShortDesc, textViewRating, textViewPrice,textViewPrice2;
        ImageView imageView;


        public ProductViewHolder(View itemView ) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.textViewTitleLog);
            textViewShortDesc = itemView.findViewById(R.id.textViewShortDescLog);
            textViewRating = itemView.findViewById(R.id.textViewRatingLog);
            textViewPrice = itemView.findViewById(R.id.textViewPriceLog);
            textViewPrice2 = itemView.findViewById(R.id.textViewPriceLog2);





        }



    }

}
