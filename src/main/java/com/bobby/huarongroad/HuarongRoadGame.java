package com.bobby.huarongroad;

import java.util.ArrayList;
import java.util.List;

public class HuarongRoadGame extends AbsSearchGame<HuarongRoadStep>{
	
	private HuarongRoadStep initStep;
	
	public HuarongRoadGame(HuarongRoadStep initStep) {
		this.initStep = initStep;
	}

	@Override
	public SearchItem<HuarongRoadStep> initItem() {
		return new SearchItem<HuarongRoadStep>(null, initStep, "开始");
	}

	@Override
	public List<SearchItem<HuarongRoadStep>> next(SearchItem<HuarongRoadStep> item) {
		List<HuarongRoadStep> nextSteps = item.getValue().nextSteps();
		List<SearchItem<HuarongRoadStep>> result = new ArrayList<SearchItem<HuarongRoadStep>>();
		for (HuarongRoadStep step : nextSteps) {
			SearchItem<HuarongRoadStep> searchItem = new SearchItem<HuarongRoadStep>(item, step, step.getDesc());
			result.add(searchItem);
		}
		return result;
	}

	@Override
	public boolean isSuccess(SearchItem<HuarongRoadStep> item) {
		return item.getValue().isSuccess();
	}
	
	public static void main(String[] args) {
		SearchItem<HuarongRoadStep> item = new HuarongRoadGame(new HuarongRoadStep()).searchBest();
		System.err.println(item);
	}
}
