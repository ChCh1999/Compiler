package lexical;

public class LexicalException extends Exception{
	String Symbol=null;
	public LexicalException(){
	}

	public LexicalException(String symbol){
		Symbol=symbol;
	}

	@Override
	public void printStackTrace() {
		super.printStackTrace();
		if(Symbol!=null)
			System.out.println("相关字符："+'"'+Symbol+'"');
	}
}