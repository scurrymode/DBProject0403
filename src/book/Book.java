/*
 * 이클래스는 로직을 작성하기 위함이 아니라 오직 데이터베이스의 레코드 1건을 담기 위한 객체이다. 이런 목적으로 설계된 클래스를 가리켜
 * 설계 분야게서는 DTO(데이터 전달 객체), VO(값 객체)라고 한다.
 * */

package book;

public class Book {
	private int book_id;
	private String book_name;
	private int price;
	private String img;
	private int subcategory_id;
	
	public int getBook_id() {
		return book_id;
	}
	public void setBook_id(int book_id) {
		this.book_id = book_id;
	}
	public String getBook_name() {
		return book_name;
	}
	public void setBook_name(String book_name) {
		this.book_name = book_name;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public int getSubcategory_id() {
		return subcategory_id;
	}
	public void setSubcategory_id(int subcategory_id) {
		this.subcategory_id = subcategory_id;
	}
	
	

}
