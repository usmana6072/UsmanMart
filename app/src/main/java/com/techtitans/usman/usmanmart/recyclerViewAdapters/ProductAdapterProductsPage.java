package com.techtitans.usman.usmanmart.recyclerViewAdapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.techtitans.usman.usmanmart.CreateSaleActivity;
import com.techtitans.usman.usmanmart.ProductDetailsActivity;
import com.techtitans.usman.usmanmart.R;
import com.techtitans.usman.usmanmart.forms.EditProductActivity;
import com.techtitans.usman.usmanmart.forms.OrderCreateForm;
import com.techtitans.usman.usmanmart.models.Product;

import java.util.ArrayList;

public class ProductAdapterProductsPage extends RecyclerView.Adapter<ProductAdapterProductsPage.ViewHolder>{

    ArrayList<Product> list;
    Context context;

    public ProductAdapterProductsPage(ArrayList<Product> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.sample_product_productpage,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product=list.get(position);
        Picasso.get().load(product.getMainImage()).placeholder(R.drawable.image).into(holder.mainImage);
        holder.title.setText(product.getProductTitle());
        holder.id.setText(product.getProductId());
        holder.price.setText(""+product.getPrice());
        holder.quantity.setText(""+product.getQuantity());

        holder.itemView.setOnClickListener(e->{
            Intent intent = getIntent(product);
            context.startActivity(intent);
        });

        holder.btnMenu.setOnClickListener(v->{
            PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
            popupMenu.inflate(R.menu.products_sample_view_menu);

            popupMenu.show();
            popupMenu.setOnMenuItemClickListener(item->{
                if(item.getItemId()==R.id.saleItem){
                    Intent intent=new Intent(context, CreateSaleActivity.class);
                    intent.putExtra("product",product);
                    context.startActivity(intent);
                }else if(item.getItemId()==R.id.manualOrderItem){
                    Intent intent=new Intent(context, OrderCreateForm.class);
                    intent.putExtra("product",product);
                    context.startActivity(intent);
                } else if (item.getItemId()==R.id.editItem) {
                    Intent intent=new Intent(context, EditProductActivity.class);
                    intent.putExtra("product",product);
                    intent.putExtra("type",1);
                    context.startActivity(intent);
                } else if (item.getItemId()==R.id.deleteItem) {
                    Intent intent=new Intent(context, EditProductActivity.class);
                    intent.putExtra("product",product);
                    intent.putExtra("type",2);
                    context.startActivity(intent);
                }
                return true;
            });
        });
    }


    @NonNull
    private Intent getIntent(Product product) {
        Intent intent = new Intent(context, ProductDetailsActivity.class);
        intent.putExtra("product",product);
        return intent;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView mainImage,btnMenu;
        TextView title,id,price,quantity;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mainImage=itemView.findViewById(R.id.productMainImage);
            title=itemView.findViewById(R.id.tvProductTitle);
            id=itemView.findViewById(R.id.tvProductId);
            price=itemView.findViewById(R.id.tvPrice);
            quantity=itemView.findViewById(R.id.tvProductQuantity);
            btnMenu=itemView.findViewById(R.id.ivMenu);
        }
    }
}
