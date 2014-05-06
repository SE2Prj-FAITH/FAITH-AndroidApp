package ch.hsr.faith.android.app.activities.adapters;

import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import ch.hsr.faith.android.app.R;
import ch.hsr.faith.android.app.activities.components.FurnitureCategoryListItem;
import ch.hsr.faith.android.app.activities.components.GotoParentCategoryListItem;
import ch.hsr.faith.android.app.activities.components.PieceOfFurnitureListItem;
import ch.hsr.faith.android.app.activities.components.SelectPieceOfFurnitureListItem;
import ch.hsr.faith.android.app.util.LocaleUtil;

public class PieceOfFurnituresAdapter extends ArrayAdapter<SelectPieceOfFurnitureListItem> {

	private Context context;

	public PieceOfFurnituresAdapter(Context context, int textViewResourceId, List<SelectPieceOfFurnitureListItem> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		SelectPieceOfFurnitureListItem listItem = getItem(position);
		convertView = LayoutInflater.from(getContext()).inflate(R.layout.select_piece_of_furniture_popup_listview_item, null);
		ImageView image = (ImageView) convertView.findViewById(R.id.item_icon);
		TextView textView = (TextView) convertView.findViewById(R.id.item_title);
		if (listItem instanceof FurnitureCategoryListItem) {
			FurnitureCategoryListItem furnitureCategoryListItem = (FurnitureCategoryListItem) listItem;
			image.setImageResource(android.R.drawable.ic_menu_more);
			textView.setTypeface(null, Typeface.BOLD);
			textView.setText(furnitureCategoryListItem.getFurnitureCategory().getName().getText(LocaleUtil.getCurrentLocale()));
		} else if (listItem instanceof PieceOfFurnitureListItem) {
			PieceOfFurnitureListItem pieceOfFurnitureListItem = (PieceOfFurnitureListItem) listItem;
			image.setImageResource(R.drawable.ic_go);
			textView.setText(pieceOfFurnitureListItem.getPieceOfFurniture().getName().getText(LocaleUtil.getCurrentLocale()));
		} else if (listItem instanceof GotoParentCategoryListItem) {
			GotoParentCategoryListItem gotoParentCategoryListItem = (GotoParentCategoryListItem) listItem;
			image.setImageResource(R.drawable.ic_menu_top);
			textView.setTypeface(null, Typeface.BOLD);
			if (gotoParentCategoryListItem.getFurnitureCategory() == null) {
				textView.setText(context.getString(R.string.select_piece_of_furniture_top_item_root_text));
			} else {
				textView.setText(context.getString(R.string.select_piece_of_furniture_top_item_text) + " '"
						+ gotoParentCategoryListItem.getFurnitureCategory().getName().getText(LocaleUtil.getCurrentLocale()) + "'");
			}
		}
		return convertView;
	}

	@Override
	public long getItemId(int position) {
		SelectPieceOfFurnitureListItem listItem = super.getItem(position);
		return listItem.getId();
	}
}
