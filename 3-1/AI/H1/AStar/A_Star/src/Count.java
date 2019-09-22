import java.util.Scanner;
import java.util.Stack;


/**Main�࣬��ӡ�����㷨�Ľ��
* @author dyl
*
*/
class Count{
	int result;
	int xiabiao[]=null;//���ʵ��±�
	public static String[] cities=new String[]{"Arad","Zerind","Oradea","Timisoara","Sibiu","Lugoj",
			"Rimnicu Vilcea","Fagaras","Mehadia","Drobeta","Craiova","Pitesti","Bucharest","Giurgiu","Urziceni","Hirsova",
			"Eforie","Vaslui","Iasi","Neamt"};//������
	public static void main(String[] args){
		Scanner scan = new Scanner(System.in);
		Graph graph=new Graph();
		System.out.println("�յ���У�Bucharest\n");
		System.out.println("������һ�����ִ������������У�\n");
		System.out.println("0-Arad\n"+"1-Zerind\n"+"2-Oradea\n"+"3-Timisoara\n"+
				"4-Sibiu\n"+"5-Lugoj\n"+"6-Rimnicu Vilcea\n"+"7-Fagaras\n"+"8-Mehadia\n"+
				"9-Drobeta\n"+"10-Craiova\n"+"11-Pitesti\n"+"12-Bucharest\n"+"13-Giurgiu\n"+
				"14-Urziceni\n"+"15-Hirsova\n"+"16-Eforie\n"+"17-Vaslui\n"+"18-Iasi\n"+"19-Neamt\n");
		Astar aXing=new Astar();
		int n = 0;
		//ȫ��ӡ
		while(n!=20) {
			System.out.println(cities[n]);
			aXing.A_Search(graph, graph.H,n++,12);
		
		}
		/*
		 //�������
		while(scan.hasNextInt()) {
			int n = scan.nextInt();
				aXing.A_Search(graph, graph.H,n,12);//0-15��Arad����Hirsova
		}*/
	}
	//��ӡ
	public void show(int n,Graph g,Stack stack){
		if(stack.size()==0){
			System.out.println("·������ʧ��");
			return;
		}
		result=0;
		System.out.print("̽���±꣺ ");
		System.out.print(stack.get(0));
		for(int i = 1; i < stack.size(); i++){
			System.out.print("-->"+stack.get(i));
		}
		System.out.print("\n���ʹ��̣� ");
		xiabiao=new int[stack.size()];
		if(stack.isEmpty()){
			System.out.println("����ʧ��");
		}
		else{
			System.out.print(g.cities[(Integer) stack.get(0)]);
			for(int i = 1; i < stack.size(); i++){
				System.out.print("-->"+g.cities[(Integer) stack.get(i)]);
			}
			for(int i =0; i < stack.size()-1; i++){
				result+=g.path[(Integer) stack.get(i)][(Integer) stack.get(i+1)];
			}
			System.out.println("\n�ܳ���Ϊ��"+result+"\n");
			g.markInit();//��շ���
		}
	}
}
//�洢��ͼ
class Graph{
	public int path[][]=new int[][]{
	{0,75,10000,118,140,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000},
	{75,0,71,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000},
	{10000,71,0,10000,151,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000},
	{118,10000,10000,0,10000,111,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000},
	{140,10000,151,10000,0,10000,80,99,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000},
	{10000,10000,10000,111,10000,0,10000,10000,70,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000},
	{10000,10000,10000,10000,80,10000,0,10000,10000,10000,146,97,10000,10000,10000,10000,10000,10000,10000,10000},
	{10000,10000,10000,10000,99,10000,10000,0,10000,10000,10000,10000,211,10000,10000,10000,10000,10000,10000,10000},
	{10000,10000,10000,10000,10000,70,10000,10000,0,75,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000},
	{10000,10000,10000,10000,10000,10000,10000,10000,75,0,120,10000,10000,10000,10000,10000,10000,10000,10000,10000},
	{10000,10000,10000,10000,10000,10000,146,10000,10000,120,0,138,10000,10000,10000,10000,10000,10000,10000,10000},
	{10000,10000,10000,10000,10000,10000,97,10000,10000,10000,138,0,101,10000,10000,10000,10000,10000,10000,10000},
	{10000,10000,10000,10000,10000,10000,10000,211,10000,10000,10000,101,0,90,85,10000,10000,10000,10000,10000},
	{10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,90,0,10000,10000,10000,10000,10000,10000},
	{10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,85,10000,0,98,10000,142,10000,10000},
	{10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,98,0,86,10000,10000,10000},
	{10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,86,0,10000,10000,10000},
	{10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,142,10000,10000,0,92,10000},
	{10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,92,0,87},
	{10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,10000,87,0}};
	//����ʽ���� �������е�Bucharest��ֱ�߾���
	public int[]H=new int[]{366,374,380,329,253,244,193,176,241,242,160,100,0,77,80,151,161,199,226,234};
	public String[] cities=new String[]{"Arad","Zerind","Oradea","Timisoara","Sibiu","Lugoj",
			"Rimnicu Vilcea","Fagaras","Mehadia","Drobeta","Craiova","Pitesti","Bucharest","Giurgiu","Urziceni","Hirsova",
			"Eforie","Vaslui","Iasi","Neamt"};//������
	public int[]mark=new int[20];//���ʱ��
	public Graph(){//�õ�����
		markInit();//��ʼ��
	}
 
	//���ʱ�־��ʼ��
	public void markInit(){
		for(int i =0; i < mark.length; i++){
			mark[i]=0;
		}
	}
//��ȡ��һ������
	public int getFirstVex(int start){
		if(start>=0&&start<path.length){
			for(int j =0; j < path.length; j++)
				if(path[start][j]<10000&&path[start][j]>0)//�й�ϵ
					return j;
		}
		return-1;//��ʾû�к���
	}
	//��һ���ڵ�
	public int getNextVex(int start,int w){
		if(start>=0&&start<path.length&&w>=0&&w<path.length){
			for(int i = w+1; i < path.length; i++)
				if(path[start][i]<10000&&path[start][i]>0)
					return i;//��ʾͼG�ж���i�ĵ�j���ڽӶ������һ���ڽӶ���
		}
		return-1;//����-1����ʾ����û���ڽӵ���
	}
	public int getNumber(){
		return path.length;
	}
}