package lexical;

import java.io.*;
import java.util.ArrayList;

public class Lexical {
	//data
	private ArrayList<String> keywords;
	private ArrayList<String> symbols;
	private ArrayList<Integer> nums;
	private ArrayList<Token> tokens;
	private char iChar;
	private int lineNum=0;
	//tool
	private BufferedReader reader;
	private FileWriter tokensWriter;
	private FileWriter symbolsWriter;
	private FileWriter numbersWriter;
	//private String line;
	public Lexical(){
		//init lists
		keywords=new ArrayList<>();
		symbols=new ArrayList<>();
		nums=new ArrayList<>();
		tokens=new ArrayList<>();
		init();
	}

	//init the lexical.Lexical
	public void init(){
		//init keywords form txt
		try {
			BufferedReader keywordsReader=new BufferedReader(new InputStreamReader(new FileInputStream(new File("res/keyword.txt"))));
			String line;
			int i=0;
			while ((line=keywordsReader.readLine())!=null){
				//System.out.println("keywords.put(\""+line+"\","+i+");");
				keywords.add(line);
				i++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			reader=new BufferedReader(new InputStreamReader(new FileInputStream(new File("res/testcode.txt"))));
			tokensWriter=new FileWriter("res/tokens.txt",false);
			symbolsWriter=new FileWriter("res/symbol.txt",false);
			numbersWriter=new FileWriter("res/number.txt",false);

			tokensWriter.write("<类别码，属性值,行号> 字符串\n");
			symbolsWriter.write("id，符号\n");
			numbersWriter.write("id,值\n");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}


	private void outputToken(Token t) throws IOException{
		if(t.getType()<10)
			tokensWriter.write(' ');
		if(t.getValue()!=-1){
			tokensWriter.write("<"+t.getType()+", "+t.getValue()+", "+t.getLine()+"> "+t.getContent()+'\n');
		}else {
			tokensWriter.write("<"+t.getType()+", _, "+t.getLine()+"> "+t.getContent()+"\n");
		}

	}

	private void outputNum(int index,int value) throws IOException{
		numbersWriter.write(index+","+value+'\n');
	}

	private void outputSym(int index,String value) throws IOException{
		symbolsWriter.write(index+","+value+'\n');
	}

	public void getAllToken() throws IOException,LexicalException {
		String line;
		while ((line=reader.readLine())!=null) {
			lineNum++;
			getTokenInLine(line);
			if(iChar=='#')
				break;
		}
		if(iChar!='#'){
			System.out.println("错误的结尾");
			throw new LexicalException();
		}

		for (Token t:tokens){
			outputToken(t);
		}
		tokensWriter.close();
		for(Integer i:nums){
			outputNum(nums.indexOf(i),i);
		}
		numbersWriter.close();
		for (String id:symbols){
			outputSym(symbols.indexOf(id),id);
		}
		symbolsWriter.close();
	}

	public void getTokenInLine(String line) throws LexicalException{
		for(int i=0;i<line.length();i++){
			iChar=line.charAt(i);

			if(iChar=='/'&&line.charAt(i+1)=='/')
				return;
			if(iChar=='#'){
				tokens.add(new Token(0,"#",lineNum));
				return;
			}

			//识别数字
			if(iChar>='0'&&iChar<='9'){
				i=getNum(line,i);
				continue;
			}
			//识别单词
			if ((iChar>='A'&&iChar<='Z')||(iChar>='a'&&iChar<='z')){
				i=getSym(line,i);
				continue;
			}
			//<= >= ==
			if((iChar=='<'||iChar=='>'||iChar=='=')&&line.charAt(i+1)=='='){
				String ss=iChar+"=";
				Token result=new Token(keywords.indexOf(ss),ss,lineNum);
				tokens.add(result);
				i++;
				continue;
			}
			//!=
			if(iChar=='!'){
				if(line.charAt(i+1)=='='){
					String ss=iChar+"=";
					Token result=new Token(keywords.indexOf(ss),ss,lineNum);
					tokens.add(result);
					i++;
					continue;
				}else {
					//System.out.println("错误的序列，意外的‘！’");
					throw new LexicalException("!");
				}
			}

			if(keywords.contains(String.valueOf(iChar))){
				Token result=new Token(keywords.indexOf(String.valueOf(iChar)),String.valueOf(iChar),lineNum);
				tokens.add(result);
				continue;
			}
			if(iChar!=' '&&iChar!='\n'&&iChar!='\t'){
//				System.out.println("未知的字符"+'"'+iChar+'"');
				throw new LexicalException(String.valueOf(iChar));
//				break;
			}


		}
	}

	public int getNum(String code,int index){
		StringBuilder sb=new StringBuilder();
		char iChar=code.charAt(index);
		while (iChar>='0'&&iChar<='9'){
			sb.append(iChar);
			index++;
			if(index<code.length()){
				iChar=code.charAt(index);
			}else {
				break;
			}

		}
		int num=Integer.parseInt(sb.toString());
		if(!nums.contains(num)){
			nums.add(num);
		}
		Token result=new Token(keywords.indexOf("_num"),nums.indexOf(num),String.valueOf(num),lineNum);
		tokens.add(result);
		return index-1;
	}

	public int getSym(String code,int index){
		StringBuilder sb=new StringBuilder();
		char iChar=code.charAt(index);
		while ((iChar>='0'&&iChar<='9')||(iChar>='A'&&iChar<='Z')||(iChar>='a'&&iChar<='z')){
			sb.append(iChar);
			index++;
			if(index<code.length())
				iChar=code.charAt(index);
			else
				break;
		}
		String symbol=sb.toString();
		if(keywords.contains(symbol)){
			Token result=new Token(keywords.indexOf(symbol),symbol,lineNum);
			tokens.add(result);
		}else {
			if(!symbols.contains(symbol)){
				symbols.add(symbol);

			}
			Token result=new Token(keywords.indexOf("_id"),symbols.indexOf(symbol),symbol,lineNum);
			tokens.add(result);
		}
		return index-1;
	}

	public void printAll(){
		for(Token t:tokens){
			System.out.println(t.getType() + "," + t.getContent());
		}
	}

	public static void main(String[] args) {
		Lexical l=new Lexical();
		try {
			l.getAllToken();
			l.printAll();
		} catch (IOException e) {
			e.printStackTrace();
		}catch (LexicalException we){
			we.printStackTrace();
		}
	}

}
