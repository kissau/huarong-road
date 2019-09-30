package com.bobby.huarongroad;

import java.util.ArrayList;
import java.util.List;

public abstract class AbsSearchGame<V> implements SearchGame<V>{
	private List<Integer> searchItems = new ArrayList<Integer>();

	@Override
	public SearchItem<V> searchBest() {
		long start = System.currentTimeMillis();
		SearchItem<V> initItem = initItem();
		if(isSuccess(initItem)) {
			return initItem;
		}
		List<SearchItem<V>> currentItems = new ArrayList<SearchItem<V>>();
		currentItems.add(initItem);
		searchItems.add(initItem.hashCode());
	
		while(currentItems.size() > 0) {
			//System.err.println(currentItems.size());
			List<SearchItem<V>> nextLoopItems = new ArrayList<SearchItem<V>>();
			for (SearchItem<V> current : currentItems) {
				List<SearchItem<V>> nextItems = next(current);
				for (SearchItem<V> next : nextItems) {
					if(isSuccess(next)) {
						//找到最优解则停止
						long time = System.currentTimeMillis() - start;
						System.err.println("耗时"+time);
						return next;
					}else if(!searchItems.contains(next.hashCode())) {
						nextLoopItems.add(next);
						searchItems.add(next.hashCode());
					}
				}
			}
			currentItems = nextLoopItems;
		}
		
		return null;
	}
	
	public abstract SearchItem<V> initItem();
	
	public abstract List<SearchItem<V>> next(SearchItem<V> item);
	
	public abstract boolean isSuccess(SearchItem<V> item);
	
}
