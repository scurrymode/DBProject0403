//������ Ŭ������ �ν��Ͻ��� ���� 1���� �����!

package oracle;

public class Dog {

	static private Dog instance;
	
	//new�� ���� ������ ����!!
	private Dog(){
		
	}
	
	static public Dog getInstance() {
		if(instance == null){
			instance = new Dog();
		}
		return instance;
	}
	
	
}
