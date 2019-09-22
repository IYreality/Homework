import java.util.Scanner;
import java.util.Stack;


/**Main类，打印各个算法的结果
* @author dyl
*
*/
class Count{
	int result;
	int xiabiao[]=null;//访问的下标
	public static String[] cities=new String[]{"Arad","Zerind","Oradea","Timisoara","Sibiu","Lugoj",
			"Rimnicu Vilcea","Fagaras","Mehadia","Drobeta","Craiova","Pitesti","Bucharest","Giurgiu","Urziceni","Hirsova",
			"Eforie","Vaslui","Iasi","Neamt"};//城市名
	public static void main(String[] args){
		Scanner scan = new Scanner(System.in);
		Graph graph=new Graph();
		System.out.println("终点城市：Bucharest\n");
		System.out.println("请输入一个数字代表您的起点城市：\n");
		System.out.println("0-Arad\n"+"1-Zerind\n"+"2-Oradea\n"+"3-Timisoara\n"+
				"4-Sibiu\n"+"5-Lugoj\n"+"6-Rimnicu Vilcea\n"+"7-Fagaras\n"+"8-Mehadia\n"+
				"9-Drobeta\n"+"10-Craiova\n"+"11-Pitesti\n"+"12-Bucharest\n"+"13-Giurgiu\n"+
				"14-Urziceni\n"+"15-Hirsova\n"+"16-Eforie\n"+"17-Vaslui\n"+"18-Iasi\n"+"19-Neamt\n");
		Astar aXing=new Astar();
		int n = 0;
		//全打印
		while(n!=20) {
			System.out.println(cities[n]);
			aXing.A_Search(graph, graph.H,n++,12);
		
		}
		/*
		 //随机测试
		while(scan.hasNextInt()) {
			int n = scan.nextInt();
				aXing.A_Search(graph, graph.H,n,12);//0-15即Arad到达Hirsova
		}*/
	}
	//打印
	public void show(int n,Graph g,Stack stack){
		if(stack.size()==0){
			System.out.println("路径搜索失败");
			return;
		}
		result=0;
		System.out.print("探索下标： ");
		System.out.print(stack.get(0));
		for(int i = 1; i < stack.size(); i++){
			System.out.print("-->"+stack.get(i));
		}
		System.out.print("\n访问过程： ");
		xiabiao=new int[stack.size()];
		if(stack.isEmpty()){
			System.out.println("搜索失败");
		}
		else{
			System.out.print(g.cities[(Integer) stack.get(0)]);
			for(int i = 1; i < stack.size(); i++){
				System.out.print("-->"+g.cities[(Integer) stack.get(i)]);
			}
			for(int i =0; i < stack.size()-1; i++){
				result+=g.path[(Integer) stack.get(i)][(Integer) stack.get(i+1)];
			}
			System.out.println("\n总长度为："+result+"\n");
			g.markInit();//清空访问
		}
	}
}
//存储地图
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
	//启发式函数 各个城市到Bucharest的直线距离
	public int[]H=new int[]{366,374,380,329,253,244,193,176,241,242,160,100,0,77,80,151,161,199,226,234};
	public String[] cities=new String[]{"Arad","Zerind","Oradea","Timisoara","Sibiu","Lugoj",
			"Rimnicu Vilcea","Fagaras","Mehadia","Drobeta","Craiova","Pitesti","Bucharest","Giurgiu","Urziceni","Hirsova",
			"Eforie","Vaslui","Iasi","Neamt"};//城市名
	public int[]mark=new int[20];//访问标记
	public Graph(){//得到数据
		markInit();//初始化
	}
 
	//访问标志初始化
	public void markInit(){
		for(int i =0; i < mark.length; i++){
			mark[i]=0;
		}
	}
//获取第一个孩子
	public int getFirstVex(int start){
		if(start>=0&&start<path.length){
			for(int j =0; j < path.length; j++)
				if(path[start][j]<10000&&path[start][j]>0)//有关系
					return j;
		}
		return-1;//表示没有孩子
	}
	//下一个节点
	public int getNextVex(int start,int w){
		if(start>=0&&start<path.length&&w>=0&&w<path.length){
			for(int i = w+1; i < path.length; i++)
				if(path[start][i]<10000&&path[start][i]>0)
					return i;//表示图G中顶点i的第j个邻接顶点的下一个邻接顶点
		}
		return-1;//返回-1，表示后面没有邻接点了
	}
	public int getNumber(){
		return path.length;
	}
}