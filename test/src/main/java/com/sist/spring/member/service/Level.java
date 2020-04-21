/**
 * 
 */
package com.sist.spring.member.service;

public enum Level {
	
	//BASIC(1),SILVER(2),GOLD(3); //?�� 개의 ?��?�� ?��브젝?�� 	?��름으�? ?��근�??��?���??���? ?�� ?��?��
	GOLD(3, null),SILVER(2, GOLD), BASIC(1,SILVER);		//?��?��?�� �??�� 만듬
	
	private final int value;
	
	private final Level next;//?��?��?��계의 Level
	
	//?��?��?��?�� ?��만든?��. 그냥?��벨�? 만들�? ?��?��
	
	Level(int value,Level next) {
		//?��?��?���? ?��?��?�� 1,2,3?�� 만듬 (enum?��?�� public ?��?��?��?�� 불�??��)
		this.value = value;
		this.next = next;
	}
	
	/**
	 * Next Level
	 * @return Level
	 */
	public Level getNextLevel() { //getter
		return this.next;
	}
	
	
	//value �??��?��?�� 방법(vo?��?�� getLevel �??��?���? ?��?��)
	public int intValue() {
		return value;
	}
	
	
	//DB?��?�� int값을 Level?��?���? �??��(vo?��?�� setLevel?��?�� ?��?��)
	public static Level valueOf(int value) {
		switch(value) {
			case 1: return BASIC;
			case 2: return SILVER;
			case 3: return GOLD;
			default: throw new AssertionError("Unknown value");
		}
	}
	
}
