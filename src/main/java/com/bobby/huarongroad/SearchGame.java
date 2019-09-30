package com.bobby.huarongroad;

public interface SearchGame<V> {
	
	/**
	 * 搜索最优解
	 */
	public SearchItem<V> searchBest();
	
}
