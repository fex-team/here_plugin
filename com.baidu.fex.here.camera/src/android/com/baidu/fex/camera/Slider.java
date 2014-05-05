package com.baidu.fex.camera;

import java.util.ArrayList;
import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class Slider extends Fragment{
	
	private ViewPager pager;
	
	public static Slider instance(ArrayList<String> list){
		
		Slider slider = new Slider();
		Bundle args = new Bundle();
		args.putStringArrayList("list", list);
		slider.setArguments(args);
		return slider;
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		pager = new ViewPager(getActivity());
		pager.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		return pager;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		List<String> list = (List<String>) getArguments().get("list");
		pager.setAdapter(new SliderAdapter(getActivity(), list));
	}
	
	
	private static class SliderAdapter extends PagerAdapter{
		
		private Context context;
		
		private List<String> list;
		
		
		
		public SliderAdapter(Context context, List<String> list) {
			this.context = context;
			this.list = list;
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			MaskView imageView = new MaskView(context);
			imageView.setAlpha(80);
			imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
			imageView.setScaleType(ScaleType.CENTER_CROP);
			Utils.getImageLoader(context).displayImage(list.get(position), imageView);
			container.addView(imageView);
			return imageView;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View)object);
			
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}
		
	}

}
