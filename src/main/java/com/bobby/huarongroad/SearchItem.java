package com.bobby.huarongroad;

public class SearchItem<V> {
	
	//上一步
	private SearchItem<V> lastItem;
	
	/*
	 * 当前步的值
	 * 必须保证唯一性
	 * 用于计算是否重复步骤，剪枝用
	 */
	private V value;
	
	/*
	 * 当前步的名称，输出用 
	 */
	private String step;
	
	public SearchItem(SearchItem<V> lastItem, V value, String step) {
		this.lastItem = lastItem;
		this.value = value;
		this.step = step;
	}

	public SearchItem<V> getLastItem() {
		return lastItem;
	}

	public V getValue() {
		return value;
	}

	public String getStep() {
		return step;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + value.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("unchecked")
		SearchItem<V> other = (SearchItem<V>) obj;
		if (value.hashCode() != other.value.hashCode())
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		if(lastItem != null) {
			//return lastItem.toString() + "-------\n" + value;
			return "" + (Integer.parseInt(lastItem.toString()) + 1);
		}else {
			return "1";
			//return step;
		}
	}
	
}
