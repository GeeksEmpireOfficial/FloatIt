/*
 * Copyright 2017 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.geekstools.floatshort.PRO.Util.IAP.skulist.row;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import net.geekstools.floatshort.PRO.R;

/**
 * ViewHolder for quick access to row's views
 */
public final class RowViewHolder extends RecyclerView.ViewHolder {
    public TextView purchaseItemName, purchaseItemDescription, purchaseItemPrice, purchaseItemInfo;
    public MaterialButton purchaseItemButton;
    public ImageView purchaseItemIcon;

    /**
     * Handler for a button click on particular row
     */
    public interface OnButtonClickListener {
        void onButtonClicked(int position);
    }

    public RowViewHolder(final View itemView, final OnButtonClickListener clickListener) {
        super(itemView);
        purchaseItemName = (TextView) itemView.findViewById(R.id.purchaseItemName);
        purchaseItemPrice = (TextView) itemView.findViewById(R.id.purchaseItemPrice);
        purchaseItemDescription = (TextView) itemView.findViewById(R.id.purchaseItemDescription);
        purchaseItemIcon = (ImageView) itemView.findViewById(R.id.purchaseItemIcon);
        purchaseItemButton = (MaterialButton) itemView.findViewById(R.id.purchaseItemButton);
        purchaseItemInfo = (TextView) itemView.findViewById(R.id.purchaseItemInfo);
        if (purchaseItemButton != null) {
            purchaseItemButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onButtonClicked(getAdapterPosition());
                }
            });
        }
    }
}
