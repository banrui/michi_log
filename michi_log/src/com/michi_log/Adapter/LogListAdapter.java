package com.michi_log.Adapter;

import java.util.List;

import com.michi_log.Dto.LogListDto;
import com.michi_log.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class LogListAdapter extends ArrayAdapter<LogListDto> {

	private List<LogListDto> items;
	private LayoutInflater inflater;

	public LogListAdapter(Context context, int textViewResourceId,
			List<LogListDto> items) {
		super(context, textViewResourceId, items);
		this.items = items;
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View logListview = convertView;
		if (logListview == null) {
			logListview = inflater.inflate(R.layout.log_list_row, null);
		}

		// 表示データの取得
		LogListDto companyListDto = (LogListDto) items.get(position);
		if (companyListDto != null) {
			// データのセット
			TextView logName = (TextView) logListview
					.findViewById(R.id.log_name);
			logName.setText(companyListDto.getLogName());
		}
		return logListview;
	}
}
