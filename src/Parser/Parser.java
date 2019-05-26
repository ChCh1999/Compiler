package Parser;

import lexical.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Parser {
	ArrayList<String> keywords=new ArrayList<>();
	ArrayList<Token> targetTokens=new ArrayList<>();
	ArrayList<String> symbolStack=new ArrayList<>();
	private int symbolTag=-1;
	ArrayList<Integer> stateStack =new ArrayList<>();
	private int statetag=0;
	private ArrayList<HashMap<String,String>>AnalysisTable=new ArrayList<>();

	private boolean acc=false;
	private Token nextToken;
	private String nextSymbol;
	private String currentSymbol;
	private int currentState;
	public Parser(){
		Lexical l=new Lexical();
//		l.init();
		try {
			l.getAllToken();
		} catch (IOException e) {
			e.printStackTrace();
		}catch (LexicalException we){
			we.printStackTrace();
		}
		try {
			//读取keyword
			BufferedReader keyInput=new BufferedReader(new FileReader("res/keyword.txt"));
			String nextline;
			while ((nextline=keyInput.readLine())!=null){
				keywords.add(nextline);
			}
			BufferedReader tokenInput=new BufferedReader(new FileReader("res/tokens.txt"));
			nextline=tokenInput.readLine();
			while ((nextline=tokenInput.readLine())!=null){
				int index1=nextline.indexOf('<');
				int index2=nextline.indexOf(',');
				int index3=nextline.indexOf(',',index2+1);
				int index4=nextline.indexOf('>');
				int Type=Integer.parseInt(nextline.substring(index1+1,index2));
				int line=Integer.parseInt(nextline.substring(index3+2,index4));
				if(Type>=0&&Type<keywords.size()&&line>0){
					targetTokens.add(new Token(Type,line));
				}else {
					throw new IOException("输入格式有误");
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		stateStack.add(0);
		System.out.println("init success");
	}
	public boolean Run(){
		try {
			nextToken=targetTokens.get(0);
			nextSymbol=keywords.get(nextToken.getType());

			targetTokens.remove(0);
			while (true){
				JudgeSymbol();
				if(acc){
					System.out.println("识别成功");
					return true;
				}
			}
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}

	}
	private void JudgeSymbol() throws Exception {
//		nextSymbol=symbolStack.get(symbolTag);
		currentState=stateStack.get(statetag);
		switch (currentState){
			case 0:
				switch (nextSymbol){
					case "public":
						shift(25);
						break;
					case "program":
						if(keywords.get(nextToken.getType()).equals("#"))
							acc=true;
						break;
					case  "beginning":
						shift(35);
						return;
						default:
							throw new Exception("意外的"+nextSymbol+"出现在开头，在第"+nextToken.getLine()+"行");
				}
				break;
			case 1:
				switch (nextSymbol){
					case "_id":
						shift(32);
						break;
					case "if":
						shift(29);
						break;
					case  "statementstring":
						shift(26);
						break;
					case  "statement":
						shift(3);
						break;
					case  "assignmentstatement":
						shift(currentState);
						reduce(1,"statement");
						break;
					case  "ifstatement":
						shift(currentState);
						reduce(1,"statement");
						break;
					case  "ifelsestatement":
						shift(currentState);
						reduce(1,"statement");
						break;
					default:
						throw new Exception("意外的"+nextSymbol+"出现在if语句块开头，在第"+nextToken.getLine()+"行");
				}
				break;
			case 2:
				switch (nextSymbol){
					case "_id":
						shift(32);
						break;
					case "if":
						shift(29);
						break;
					case  "statementstring":
						shift(27);
						break;
					case  "statement":
						shift(3);
						break;
					case  "assignmentstatement":
						shift(currentState);
						reduce(1,"statement");
						break;
					case  "ifstatement":
						shift(currentState);
						reduce(1,"statement");
						break;
					case  "ifelsestatement":
						shift(currentState);
						reduce(1,"statement");
						break;
					default:
						throw new Exception("意外的"+nextSymbol+"出现在else语句块开头，在第"+nextToken.getLine()+"行");
				}
				break;
			case 3:
				switch (nextSymbol){
					case "_id":
						shift(32);
						break;
					case "if":
						shift(29);
						break;
					case  "statementstring":
						shift(26);
						reduce(2,"statementstring");
						break;
					case  "statement":
						shift(3);
						break;
					case  "assignmentstatement":
						shift(currentState);
						reduce(1,"statement");
						break;
					case  "ifstatement":
						shift(currentState);
						reduce(1,"statement");
						break;
					case  "ifelsestatement":
						shift(currentState);
						reduce(1,"statement");
						break;
					default:
						reduce(1,"statementstring");
				}
				break;
			case 4:
				switch (nextSymbol){
					case "_id":
						shift(32);
						break;
					case "if":
						shift(29);
						break;
					case  "statementstring":
						shift(34);
						break;
					case  "statement":
						shift(3);
						break;
					case  "assignmentstatement":
						shift(currentState);
						reduce(1,"statement");
						break;
					case  "ifstatement":
						shift(currentState);
						reduce(1,"statement");
						break;
					case  "ifelsestatement":
						shift(currentState);
						reduce(1,"statement");
						break;
					default:
						throw new Exception("意外的"+nextSymbol+"出现在主程序块开头，在第"+nextToken.getLine()+"行");
				}
				break;
			case 5:
				switch (nextSymbol){
					case "(":
						shift(10);
						break;
					case "_id":
						shift(currentState);
						reduce(1,"object");
						break;
					case  "_num":
						shift(currentState);
						reduce(1,"object");
						break;
					case  "true":
						shift(currentState);
						reduce(1,"condition");
						break;
					case  "false":
						shift(currentState);
						reduce(1,"condition");
						break;
					case  "expression":
						shift(15);
						break;
					case  "item":
						shift(20);
						break;
					case  "object":
						shift(19);
						break;
					case  "condition":
						shift(28);
						break;
					default:
						throw new Exception("意外的"+nextSymbol+"出现在条件表达式中，在第"+nextToken.getLine()+"行");
				}
				break;
			case 6:
				switch (nextSymbol){
					case "{":
						shift(2);
						break;
					case "_id":
						shift(32);
						break;
					case "if":
						shift(29);
						break;
					case  "statement":
						shift(currentState);
						reduce(2,"elseclause");
						break;
					case  "assignmentstatement":
						shift(currentState);
						reduce(1,"statement");
						break;
					case  "ifstatement":
						shift(currentState);
						reduce(1,"statement");
						break;
					case  "ifelsestatement":
						shift(currentState);
						reduce(1,"statement");
						break;
					default:
						throw new Exception("意外的"+nextSymbol+"出现在else语句开头，在第"+nextToken.getLine()+"行");
				}
				break;
			case 7:
				switch (nextSymbol){
					case "{":
						shift(1);
						break;
					case "_id":
						shift(32);
						break;
					case  "if":
						shift(29);
						break;
					case "statement":
						shift(currentState);
						reduce(5,"ifstatement");
						break;
					case "assignmentstatement":
						shift(18);
						break;
					case  "ifstatement":
						shift(currentState);
						reduce(1,"statement");
						break;
					case  "ifelsestatement":
						shift(17);
						break;
						default:
							throw new Exception("意外的"+nextSymbol+"出现在if语句中，在第"+nextToken.getLine()+"行");

				}
				break;
			case 8:
				switch (nextSymbol){
					case "(":
						shift(10);
						break;
					case "_id":
						shift(currentState);
						reduce(1,"object");
						break;
					case  "_num":
						shift(currentState);
						reduce(1,"object");
						break;
					case "expression":
						shift(currentState);
						reduce(3,"condition");
						break;
					case  "item":
						shift(20);
						break;
					case  "object":
						shift(19);
						break;
					default:
						throw new Exception("意外的"+nextSymbol+"出现在关系运算符后的表达式中，在第"+nextToken.getLine()+"行");

				}
				break;
			case 9:
				switch (nextSymbol){
					case "(":
						shift(10);
						break;
					case "_id":
						shift(currentState);
						reduce(1,"object");
						break;
					case  "_num":
						shift(currentState);
						reduce(1,"object");
						break;
					case "expression":
						shift(currentState);
						reduce(3,"expression");
						break;
					case  "item":
						shift(20);
						break;
					case  "object":
						shift(19);
						break;
					default:
						throw new Exception("意外的"+nextSymbol+"出现在-后表达式中，在第"+nextToken.getLine()+"行");

				}
				break;
			case 10:
				switch (nextSymbol){
					case "(":
						shift(10);
						break;
					case "_id":
						shift(currentState);
						reduce(1,"object");
						break;
					case  "_num":
						shift(currentState);
						reduce(1,"object");
						break;
					case "expression":
						shift(30);
						break;
					case  "item":
						shift(20);
						break;
					case  "object":
						shift(19);
						break;
					default:
						throw new Exception("意外的"+nextSymbol+"出现在(后表达式开头，在第"+nextToken.getLine()+"行");

				}
				break;
			case 11:
				switch (nextSymbol){
					case "(":
						shift(10);
						break;
					case "_id":
						shift(currentState);
						reduce(1,"object");
						break;
					case  "_num":
						shift(currentState);
						reduce(1,"object");
						break;
					case "expression":
						shift(currentState);
						reduce(3,"expression");
						break;
					case  "item":
						shift(20);
						break;
					case  "object":
						shift(19);
						break;
					default:
						throw new Exception("意外的"+nextSymbol+"出现在+后表达式中，在第"+nextToken.getLine()+"行");

				}
				break;
			case 12:
				switch (nextSymbol){
					case "(":
						shift(10);
						break;
					case "_id":
						shift(currentState);
						reduce(1,"object");
						break;
					case  "_num":
						shift(currentState);
						reduce(1,"object");
						break;
					case "expression":
						shift(31);
						break;
					case  "item":
						shift(20);
						break;
					case  "object":
						shift(19);
						break;
					default:
						throw new Exception("意外的"+nextSymbol+"出现在=后表达式开头，在第"+nextToken.getLine()+"行");

				}
				break;
			case 13:
				switch (nextSymbol){
					case "(":
						shift(10);
						break;
					case "_id":
						shift(currentState);
						reduce(1,"object");
						break;
					case  "_num":
						shift(currentState);
						reduce(1,"object");
						break;
					case  "item":
						shift(20);
						reduce(3,"item");
						break;
					case  "object":
						shift(19);
						break;
					default:
						throw new Exception("意外的"+nextSymbol+"出现在/后，在第"+nextToken.getLine()+"行");
				}
				break;
			case 14:
				switch (nextSymbol){
					case "(":
						shift(10);
						break;
					case "_id":
						shift(currentState);
						reduce(1,"object");
						break;
					case  "_num":
						shift(currentState);
						reduce(1,"object");
						break;
					case  "item":
						shift(20);
						reduce(3,"item");
						break;
					case  "object":
						shift(19);
						break;
					default:
						throw new Exception("意外的"+nextSymbol+"出现在*后，在第"+nextToken.getLine()+"行");
				}
				break;
			case 15:
				switch (nextSymbol){
					case "==":
						shift(10);
						reduce(1,"relationaloperator");
						break;
					case "!=":
						shift(10);
						reduce(1,"relationaloperator");
						break;
					case "<=":
						shift(10);
						reduce(1,"relationaloperator");
						break;
					case ">=":
						shift(10);
						reduce(1,"relationaloperator");
						break;
					case "<":
						shift(10);
						reduce(1,"relationaloperator");
						break;
					case ">":
						shift(10);
						reduce(1,"relationaloperator");
						break;
					case "relationaloperator":
						shift(8);
						break;
					default:
						throw new Exception("意外的"+nextSymbol+"出现在条件表达式中，在第"+nextToken.getLine()+"行");
				}
				break;
			case 16:
				switch (nextSymbol){
					case "else":
						shift(6);
						break;
					case "elseclause":
						shift(currentState);
						reduce(8,"ifelsestatement");
						break;
					default:
						reduce(7,"ifstatement");
						}
				break;
			case 17:
				switch (nextSymbol){
					case "else":
						shift(6);
						break;
					case "elseclause":
						shift(currentState);
						reduce(6,"ifelsestatement");
						break;
					default:
						reduce(1,"statement");
				}
				break;
			case 18:
				switch (nextSymbol){
					case "else":
						shift(6);
						break;
					case "elseclause":
						shift(currentState);
						reduce(6,"ifelsestatement");
						break;
					default:
						reduce(1,"statement");
				}
				break;
			case 19:
				switch (nextSymbol){
					case "*":
						shift(14);
						break;
					case "/":
						shift(13);
						break;
					default:
						reduce(1,"item");
				}
				break;
			case 20:
				switch (nextSymbol){
					case "+":
						shift(11);
						break;
					case "-":
						shift(9);
						break;
					default:
						reduce(1,"expression");
				}
				break;
			case 21:
				switch (nextSymbol){
					case ")":
						shift(currentState);
						reduce(6,"beginning");
						break;

					default:
						throw new Exception("意外的"+nextSymbol+"出现在开头，在第"+nextToken.getLine()+"行");
				}
				break;
			case 22:
				switch (nextSymbol){
					case "(":
						shift(21);
						break;

					default:
						throw new Exception("意外的"+nextSymbol+"出现在开头，在第"+nextToken.getLine()+"行");
				}
				break;
			case 23:
				switch (nextSymbol){
					case "main":
						shift(22);
						break;

					default:
						throw new Exception("意外的"+nextSymbol+"出现在开头，在第"+nextToken.getLine()+"行");
				}
				break;
			case 24:
				switch (nextSymbol){
					case "void":
						shift(23);
						break;

					default:
						throw new Exception("意外的"+nextSymbol+"出现在开头，在第"+nextToken.getLine()+"行");
				}
				break;
			case 25:
				switch (nextSymbol){
					case "static":
						shift(24);
						break;

					default:
						throw new Exception("意外的"+nextSymbol+"出现在开头，在第"+nextToken.getLine()+"行");
				}
				break;
			case 26:
				switch (nextSymbol){
					case "}":
						shift(16);
						break;

					default:
						throw new Exception("意外的"+nextSymbol+"出现在if语句块末尾，在第"+nextToken.getLine()+"行");
				}
				break;
			case 27:
				switch (nextSymbol){
					case "}":
						shift(currentState);
						reduce(4,"elseclause");
						break;

					default:
						throw new Exception("意外的"+nextSymbol+"出现在else语句块末尾，在第"+nextToken.getLine()+"行");
				}
				break;
			case 28:
				switch (nextSymbol){
					case ")":
						shift(7);
						break;

					default:
						throw new Exception("意外的"+nextSymbol+"出现在条件语句末尾，在第"+nextToken.getLine()+"行");
				}
				break;
			case 29:
				switch (nextSymbol){
					case "(":
						shift(5);
						break;

					default:
						throw new Exception("意外的"+nextSymbol+"出现在if之后（需求“(”），在第"+nextToken.getLine()+"行");
				}
				break;
			case 30:
				switch (nextSymbol){
					case ")":
						shift(currentState);
						reduce(3,"object");
						break;

					default:
						throw new Exception("意外的"+nextSymbol+"出现在表达式中（需求“)”），在第"+nextToken.getLine()+"行");
				}
				break;
			case 31:
				switch (nextSymbol){
					case ";":
						shift(currentState);
						reduce(4,"assignmentstatement");
						break;

					default:
						throw new Exception("意外的"+nextSymbol+"出现在表达式末尾（需求“;”），在第"+nextToken.getLine()+"行");
				}
				break;
			case 32:
				switch (nextSymbol){
					case "=":
						shift(12);
						break;

					default:
						throw new Exception("意外的"+nextSymbol+"出现在表达式中（需求“=”），在第"+nextToken.getLine()+"行");
				}
				break;
			case 33:
				switch (nextSymbol){
					case "#":
						reduce(4,"program");
						break;

					default:
						throw new Exception("意外的"+nextSymbol+"出现在文件尾（需求“#”），在第"+nextToken.getLine()+"行");
				}
				break;
			case 34:
				switch (nextSymbol){
					case "}":
						shift(33);
						break;

					default:
						throw new Exception("意外的"+nextSymbol+"出现在程序尾（需求“}”），在第"+nextToken.getLine()+"行");
				}
				break;
			case 35:
				switch (nextSymbol){
					case "{":
						shift(4);
						break;

					default:
						throw new Exception("意外的"+nextSymbol+"出现在main()后（需求“{”），在第"+nextToken.getLine()+"行");
				}
				break;
				default:
					throw new Exception("意外的状态");

		}

	}
	private void shift(int state) throws Exception {
		if(!targetTokens.isEmpty()){
			symbolStack.add(nextSymbol);
			nextToken=targetTokens.get(0);
			targetTokens.remove(0);
			nextSymbol=keywords.get(nextToken.getType());
			symbolTag++;
			stateStack.add(state);
			statetag++;
		}
		else {
			throw new Exception("意外的结尾");
		}

	}
	private void reduce(int num,String target) throws Exception{
		targetTokens.add(0,nextToken);
		symbolTag-=num;
		statetag-=num;
		for(int i=0;i<num;i++){
			symbolStack.remove(symbolTag+1);
			stateStack.remove(statetag+1);
		}
		nextSymbol=target;
		JudgeSymbol();
	}
	public static void main(String[] args) {
		Parser test=new Parser();
		test.Run();
	}
}
