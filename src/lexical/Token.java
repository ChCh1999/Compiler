package lexical;

public class Token {
	private int type;
	private int value;
	private int line;

	public int getLine() {
		return line;
	}

	public String getContent() {
		return content;
	}

	private String content;
	public Token(int type,String content,int line){
		this.type=type;
		this.content=content;
		this.line=line;
		value=-1;
	}
	public Token(int type,int value,String content,int line){
		this.type=type;
		this.content=content;
		this.value=value;
		this.line=line;
	}
	public Token(int type,int line){
		this.type=type;
		this.line=line;
	}
	public int getType() {
		return type;
	}

	public int getValue() {
		return value;
	}
}
