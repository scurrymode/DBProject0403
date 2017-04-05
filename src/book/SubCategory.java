/*
 * DB에서 테이블=틀=객체, 레코드=인스턴스
 * 아래의 클래스는 로직 작성용이 아니다! 레코드를 담기 위한 저장공간용도로만 사용할 클래스이다.
 * 배열과 차이점은? 배열과 객체는 차원이 다른 방식으로 개발되므로 객체로 처리하는 것이 훨씬 더 효과적
 * 
 * */
package book;

public class SubCategory {
	private int subcategory_id;
	private int topcategory_id;
	private String category_name;
	
	public int getSubcategory_id() {
		return subcategory_id;
	}
	public void setSubcategory_id(int subcategory_id) {
		this.subcategory_id = subcategory_id;
	}
	public int getTopcategory_id() {
		return topcategory_id;
	}
	public void setTopcategory_id(int topcategory_id) {
		this.topcategory_id = topcategory_id;
	}
	public String getCategory_name() {
		return category_name;
	}
	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}
	
	
	
}
