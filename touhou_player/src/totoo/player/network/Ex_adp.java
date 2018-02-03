package totoo.player.network;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Ex_adp extends BaseExpandableListAdapter {
	List<downLoad_item_Info> mListKind;
	static List<List<downLoad_item_Info>> mListAll;
	Context mContext;

	public Ex_adp(List<downLoad_item_Info> ListParent,
			List<List<downLoad_item_Info>> mList, Context mContext) {
		super();
		this.mListKind = ListParent;
		mListAll = mList;
		this.mContext = mContext;
	}

	public Ex_adp(Context mContext2) {

	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return mListKind.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return mListAll.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return mListKind.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return (mListAll.get(groupPosition)).get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		TextView textView = new TextView(mContext);
		textView.setTextSize(20);
		downLoad_item_Info downLoad_item_Info = mListKind.get(groupPosition);
		textView.setText(downLoad_item_Info.name_title);
		return textView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		downLoad_item_Info downLoad_item_Info = mListAll.get(groupPosition)
				.get(childPosition);
		LinearLayout linearLayout = new LinearLayout(mContext);
		TextView textView_tip = new TextView(mContext);
		textView_tip.setText(downLoad_item_Info.string_say);
		linearLayout.addView(textView_tip);
		EditText textView = new EditText(mContext);
		textView.setText(downLoad_item_Info.name_title);
		textView.setTextSize(18);
		textView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

			}
		});
		linearLayout.addView(textView);
		return linearLayout;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

}
