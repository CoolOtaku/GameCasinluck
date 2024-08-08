package com.denise_hill_sarah.gamecasinluck.views_controllers;

import static com.denise_hill_sarah.gamecasinluck.views_controllers.MainActivity.onClickAnimation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.SkuDetails;
import com.denise_hill_sarah.gamecasinluck.R;

import java.util.List;

public class AdapterShop extends RecyclerView.Adapter<AdapterShop.ShopViewHolder> {

    private Activity activity;
    private List<SkuDetails> list;
    private BillingClient billingClient;

    public AdapterShop(Activity activity, List<SkuDetails> list, BillingClient billingClient) {
        this.activity = activity;
        this.list = list;
        this.billingClient = billingClient;
    }

    @NonNull
    @Override
    public ShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ShopViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ShopViewHolder holder, @SuppressLint("RecyclerView") int position) {
        SkuDetails itemInfo = list.get(position);

        holder.getTxtMessage().setText(itemInfo.getPrice() + " " + itemInfo.getDescription());

        holder.getItemSopContainer().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(onClickAnimation);
                billingClient.launchBillingFlow(activity, BillingFlowParams.newBuilder().setSkuDetails(itemInfo).build());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ShopViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout itemSopContainer;
        private TextView txtMessage;

        ShopViewHolder(@NonNull View itemView) {
            super(itemView);
            itemSopContainer = itemView.findViewById(R.id.itemSopContainer);
            txtMessage = itemView.findViewById(R.id.txtMessage);
        }

        public TextView getTxtMessage() {
            return txtMessage;
        }

        public void setTxtMessage(TextView txtMessage) {
            this.txtMessage = txtMessage;
        }

        public LinearLayout getItemSopContainer() {
            return itemSopContainer;
        }

        public void setItemSopContainer(LinearLayout itemSopContainer) {
            this.itemSopContainer = itemSopContainer;
        }

    }

}