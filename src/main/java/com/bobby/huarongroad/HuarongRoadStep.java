package com.bobby.huarongroad;

import java.util.ArrayList;
import java.util.List;

public class HuarongRoadStep {
	
	private final static int CAOCAO = 7;
	private final static int GUANYU = 6;
	private final static int ZHANGFEI = 5;
	private final static int ZHAOYUN = 4;
	private final static int MACHAO = 3;
	private final static int HUANGZHONG = 2;
	private final static int SOLDIER = 1;
	
	private enum Loc{
		LEFT,
		RIGHT,
		UP,
		DOWN
	}
	
	private class Point{
		int x;		//x坐标
		int y;		//y坐标
		int index;	//数组下标
		
		public Point(int x, int y) {
			this.x = x;
			this.y = y;
			this.index = index(x, y);
		}
		
		public Point(int index) {
			this.x = index/4;
			this.y = index%4;
			this.index = index;
		}
	}
	
	private int[] arr;
	
	private Point nullPoint1, nullPoint2;
	
	//移动说明
	private String desc;
	
	public HuarongRoadStep() {
		/*
		 * zf,cc,cc,zy
		 * zf,cc,cc,zy
		 * hz,gy,gy,mc
		 * hz,xb,xb,mc
		 * xb       xb
		 */
		int[] arr = new int[20];
		arr[index(0,1)] = arr[index(0,2)] = arr[index(1,1)] = arr[index(1,2)] = CAOCAO;
		arr[index(2,1)] = arr[index(2,2)] = GUANYU;
		arr[index(0,0)] = arr[index(1,0)] = ZHANGFEI;
		arr[index(0,3)] = arr[index(1,3)] = ZHAOYUN;
		arr[index(2,0)] = arr[index(3,0)] = HUANGZHONG;
		arr[index(2,3)] = arr[index(3,3)] = MACHAO;
		arr[index(4,0)] = arr[index(3,1)] = arr[index(3,2)] = arr[index(4,3)] = SOLDIER;
		this.arr = arr;
		initNullPoint();
	}
	
	private HuarongRoadStep(int[] arr, String desc) {
		this.arr = arr;
		this.desc = desc;
		initNullPoint();
	}
	
	private void initNullPoint() {
		for (int i = 0; i < arr.length; i++) {
			if(arr[i] == 0) {
				if(nullPoint1 == null) {
					nullPoint1 = new Point(i);
				}else {
					nullPoint2 = new Point(i);
				}
			}
		}
	}
	
	public List<HuarongRoadStep> nextSteps() {
		List<HuarongRoadStep> nextSteps = new ArrayList<HuarongRoadStep>();

		/*
		 * 最优解121步,耗时2722
		 * 由于两个空格实质相同，左右对称,对于点1或者点2的左右两个方向，
		 * 随便减少一个方向得到的结果一样
		 * 如果不是求最优解，经过测试注释掉第四个可以缩短时间最短
		 * 所有测试结果都是基于广度优先的单线程搜索
		 */
		addNext(nextSteps, move(nullPoint1, Loc.LEFT));		//注释1为140步,耗时794
		addNext(nextSteps, move(nullPoint2, Loc.LEFT));		//注释2为148步，耗时626
		addNext(nextSteps, move(nullPoint1, Loc.RIGHT));	//注释3为154步，耗时1065
		addNext(nextSteps, move(nullPoint2, Loc.RIGHT));	//注释4为164步，耗时395
		
		addNext(nextSteps, move(nullPoint1, Loc.UP));
		addNext(nextSteps, move(nullPoint2, Loc.UP));
		addNext(nextSteps, move(nullPoint1, Loc.DOWN));
		addNext(nextSteps, move(nullPoint2, Loc.DOWN));
		
		return nextSteps;
	}
	
	private void addNext(List<HuarongRoadStep> nextSteps, HuarongRoadStep next) {
		if(next != null && !nextSteps.contains(next)) {
			nextSteps.add(next);
		}
	}
	
	private HuarongRoadStep move(Point nullPoint, Loc location) {
		int[] moveArr = null;
		Point rolePoint = null;
		switch (location) {
		case LEFT:
			rolePoint = left(nullPoint);
			moveArr = change(nullPoint, rolePoint, location);
			break;
		case RIGHT:
			rolePoint = right(nullPoint);
			moveArr = change(nullPoint, rolePoint, location);
			break;
		case UP:
			rolePoint = up(nullPoint);
			moveArr = change(nullPoint, rolePoint, location);
			break;
		case DOWN:
			rolePoint = down(nullPoint);
			moveArr = change(nullPoint, rolePoint, location);
			break;
		default:
			break;
		}
		
		if(moveArr != null) {
			return new HuarongRoadStep(moveArr, "" + location);
		}
		
		return null;
	}
	
	private int[] change(Point nullPoint, Point rolePoint, Loc location) {
		if(rolePoint == null) {
			return null;
		}
		
		int role = arr[rolePoint.index];
		if(role == SOLDIER) {
			int[] copyArr = copyArr();
			copyArr[nullPoint.index] = role;
			copyArr[rolePoint.index] = 0;
			return copyArr;
		}else if(role > SOLDIER && role < GUANYU) {
			if(location == Loc.UP || location == Loc.DOWN) {
				int[] copyArr = copyArr();
				int offset = location == Loc.UP ? -8 : 8;
				copyArr[nullPoint.index+offset] = 0;
				copyArr[nullPoint.index] = role;
				return copyArr;
			}else if(location == Loc.LEFT || location == Loc.RIGHT) {
				int offset = location == Loc.LEFT ? -1 : 1;
				Point nullUp = up(nullPoint);
				
				//检查上面一块是否连通可移动
				if(nullUp != null && arr[nullUp.index] == 0 && arr[nullUp.index+offset] == role) {
					int[] copyArr = copyArr();
					copyArr[rolePoint.index] = copyArr[nullUp.index+offset] = 0;
					copyArr[nullPoint.index] = copyArr[nullUp.index] = role;
					//copyArr[nullUp.index+offset] = 0;
					//copyArr[nullUp.index] = role;
					return copyArr;
				}
				
				//检查下面一块是否连通可移动
				Point nullDown = down(nullPoint);
				if(nullDown != null && arr[nullDown.index] == 0 && arr[nullDown.index+offset] == role ) {
					int[] copyArr = copyArr();
					copyArr[rolePoint.index] = copyArr[nullDown.index+offset] = 0;
					copyArr[nullPoint.index] = copyArr[nullDown.index] = role;
					//copyArr[nullDown.index+offset] = 0;
					//copyArr[nullDown.index] = role;
					return copyArr;
				}
			}
			return null;
		}else if(role == GUANYU) {
			if(location == Loc.LEFT || location == Loc.RIGHT) {
				int[] copyArr = copyArr();
				int offset = location == Loc.LEFT ? -2 : 2;
				copyArr[nullPoint.index+offset] = 0;
				copyArr[nullPoint.index] = role;
				return copyArr;
			}else if(location == Loc.UP || location == Loc.DOWN) {
				int offset = location == Loc.UP ? -4 : 4;
				Point nullLeft = left(nullPoint);
				
				//检查左面一块是否连通可移动
				if(nullLeft != null && arr[nullLeft.index] == 0 && arr[nullLeft.index+offset] == role) {
					int[] copyArr = copyArr();
					copyArr[rolePoint.index] = copyArr[nullLeft.index+offset] = 0;
					copyArr[nullPoint.index] = copyArr[nullLeft.index] = role;
					//copyArr[nullLeft.index+offset] = 0;
					//copyArr[nullLeft.index] = role;
					return copyArr;
				}
				
				//检查右面一块是否连通可移动
				Point nullRight = right(nullPoint);
				if(nullRight != null && arr[nullRight.index] == 0 && arr[nullRight.index+offset] == role ) {
					int[] copyArr = copyArr();
					copyArr[rolePoint.index] = copyArr[nullRight.index+offset] = 0;
					copyArr[nullPoint.index] = copyArr[nullRight.index] = role;
					//copyArr[nullRight.index+offset] = 0;
					//copyArr[nullRight.index] = role;
					return copyArr;
				}
			}
			return null;
		}else if(role == CAOCAO) {
			if(location == Loc.LEFT || location == Loc.RIGHT) {
				int offset = location == Loc.LEFT ? -1 : 1;
				Point nullUp = up(nullPoint);
				
				//检查上面一块是否连通可移动
				if(nullUp != null && arr[nullUp.index] == 0 && arr[nullUp.index+offset] == role) {
					int[] copyArr = copyArr();
					copyArr[rolePoint.index] = copyArr[nullUp.index+offset] = 0;
					copyArr[nullPoint.index] = copyArr[nullUp.index] = role;
					//copyArr[nullUp.index+offset] = 0;
					//copyArr[nullUp.index] = role;
					return copyArr;
				}
				
				//检查下面一块是否连通可移动
				Point nullDown = down(nullPoint);
				if(nullDown != null && arr[nullDown.index] == 0 && arr[nullDown.index+offset] == role ) {
					int[] copyArr = copyArr();
					copyArr[rolePoint.index] = copyArr[nullDown.index+offset] = 0;
					copyArr[nullPoint.index] = copyArr[nullDown.index] = role;
					//copyArr[nullDown.index+offset] = 0;
					//copyArr[nullDown.index] = role;
					return copyArr;
				}
			}else if(location == Loc.UP || location == Loc.DOWN) {
				int offset = location == Loc.UP ? -4 : 4;
				
				Point nullLeft = left(nullPoint);
				
				//检查左面一块是否连通可移动
				if(nullLeft != null && arr[nullLeft.index] == 0 && arr[nullLeft.index+offset] == role) {
					int[] copyArr = copyArr();
					copyArr[rolePoint.index] = copyArr[nullLeft.index+offset] = 0;
					copyArr[nullPoint.index] = copyArr[nullLeft.index] = role;
					//copyArr[nullLeft.index+offset] = 0;
					//copyArr[nullLeft.index] = role;
					return copyArr;
				}
				
				//检查右面一块是否连通可移动
				Point nullRight = right(nullPoint);
				if(nullRight != null && arr[nullRight.index] == 0 && arr[nullRight.index+offset] == role ) {
					int[] copyArr = copyArr();
					copyArr[rolePoint.index] = copyArr[nullRight.index+offset] = 0;
					copyArr[nullPoint.index] = copyArr[nullRight.index] = role;
					//copyArr[nullRight.index+offset] = 0;
					//copyArr[nullRight.index] = role;
					return copyArr;
				}
			}
			return null;
		}
		return null;
	}
	
	private int[] copyArr() {
		int[] arr = new int[20];
		System.arraycopy(this.arr, 0, arr, 0, 20);
		return arr;
	}
	
	public boolean isSuccess() {
		return arr[index(3, 1)] == CAOCAO && arr[index(4, 2)] == CAOCAO;
	}
	
	public String getDesc() {
		return desc;
	}

	private int index(int x, int y) {
		return x*4+y;
	}
	
	private Point left(Point point) {
		if(point.y == 0) {
			return null;
		}
		return new Point(point.x, point.y-1);
	}
	
	private Point right(Point point) {
		if(point.y == 3) {
			return null;
		}
		return new Point(point.x, point.y+1);
	}
	
	private Point up(Point point) {
		if(point.x == 0) {
			return null;
		}
		return new Point(point.x-1, point.y);
	}
	
	private Point down(Point point) {
		if(point.x == 4) {
			return null;
		}
		return new Point(point.x+1, point.y);
	}

	private int valueOf(int x, int y) {
		int index = index(x, y);
		if(arr[index] < 2) {
			return arr[index];
		}else if(arr[index] < GUANYU) {
			return 2;
		}else if(arr[index] == GUANYU) {
			return 3;
		}else if(arr[index] == CAOCAO) {
			return 4;
		}else {
			throw new IllegalArgumentException();
		}
	}
	
	@Override
	public int hashCode() {
		long result = 0;
		for (int x = 0; x < 5; x++) {
			for (int y = 0; y < 4; y++) {
				result = result*5+valueOf(x, y);
			}
		}
		
		//左右对称算一样的，增加剪枝的速度
		long result2 = 0;
		for (int x = 0; x < 5; x++) {
			for (int y = 4; y > 0; y--) {
				result2 = result2*5+valueOf(x, y-1);
			}
		}
		
		return Long.hashCode(result>result2 ? result2 : result);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HuarongRoadStep other = (HuarongRoadStep) obj;
		if (hashCode() != other.hashCode())
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int x = 0; x < 5; x++) {
			for (int y = 0; y < 4; y++) {
				sb.append(arr[index(x, y)]);
			}
			sb.append("\n");
		}
		return sb.toString();
	}

}
