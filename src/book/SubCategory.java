/*
 * DB���� ���̺�=Ʋ=��ü, ���ڵ�=�ν��Ͻ�
 * �Ʒ��� Ŭ������ ���� �ۼ����� �ƴϴ�! ���ڵ带 ��� ���� ��������뵵�θ� ����� Ŭ�����̴�.
 * �迭�� ��������? �迭�� ��ü�� ������ �ٸ� ������� ���ߵǹǷ� ��ü�� ó���ϴ� ���� �ξ� �� ȿ����
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
