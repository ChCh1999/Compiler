package lexical;

public class testcode {
	public static void main(String[] args) {
		int i=0;
		int j=10;
		int k=12;
		if(i<=j){
			int temp=i;
			i=j*6;
			j=temp*6;
		}
		if(k*6>j)
			//若k最大
			if(k>i/6){
			int temp=k;
			k=j;
			j=i;
			i=temp*6;
			}else {
			int temp=k;
			k=j;
			j=temp*6;
			}
		int res=(i/3+(j+k/2))+k;


	}
}
