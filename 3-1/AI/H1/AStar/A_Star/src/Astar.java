import java.util.Stack;

public class Astar{
int MaxWeight=10000;//表示无穷大不可达


public void A_Search(Graph g,int H[],int v0,int end){
	boolean flag=true;
	int x;//表示栈顶元素
	int vex = v0;//寻找目标节点
	int MinF,MinVex= v0;//记录最小的f(n)和对应的节点
	Stack<Integer> stack=new Stack<Integer>();
	int[][]GHF=new int[g.path.length][3];//分别用于存储g(n),h(n),f(n)
	for(int i =0; i < g.path.length; i++){
		GHF[i][0]=0;
		GHF[i][2]=MaxWeight;//对f(n)初始化,1000表示无穷大
	}
	stack.push(v0);//v0入栈
	GHF[v0][0]=0;//g(n)
	GHF[v0][1]=H[v0];//h(n)
	GHF[v0][2]=GHF[v0][0]+GHF[v0][1];//f(n)
	System.out.println(v0 + " " + GHF[v0][0] + " " + GHF[v0][1] + " " + GHF[v0][2]);
	g.mark[v0]=1;
	if(v0 == end)
		flag=false;
	while(flag){
		MinF=MaxWeight;
		x=(Integer) stack.peek();
		//处理第一个子节点
		vex=g.getFirstVex(x);
		if(vex==end){//找到目标节点
			stack.push(vex);
			g.mark[x]=1;
//			flag=false;
			break;
		}
		if(vex!=-1){//子节点能找到，继续
			if(g.mark[vex]==0){//没被访问
				GHF[vex][0]=GHF[x][0]+g.path[x][vex];//节点vex的g(n)
				GHF[vex][1]=H[vex];//节点vex的h(n)
				GHF[vex][2]=GHF[vex][0]+GHF[vex][1];
				if(GHF[vex][2]<MinF){
					MinF=GHF[vex][2];
					MinVex=vex;
				}
			}
	//处理剩下的邻接点（宽度遍历）
			while(flag && vex!=-1){
				vex=g.getNextVex(x, vex);
				if(vex!=-1&&g.mark[vex]==0){//有邻节点
					GHF[vex][0]=GHF[x][0]+g.path[x][vex];//节点vex的g(n)
					GHF[vex][1]=H[vex];//节点vex的h(n)
					GHF[vex][2]=GHF[vex][0]+GHF[vex][1];
					if(GHF[vex][2]<MinF){
						MinF=GHF[vex][2];
						MinVex=vex;
					}
				}
				if(vex==-1){//没有邻接点了，此时确定最小消耗节点，并压栈
					stack.push(MinVex);
					g.mark[MinVex]=1;
					System.out.println(MinVex + " " + GHF[MinVex][0] + " " + GHF[MinVex][1] + " " + GHF[MinVex][2]);
					break;
				}
				if(vex==end){
					stack.push(vex);//压栈目标节点
					g.mark[vex]=1;
					flag=false;
					System.out.println(MinVex + " " + GHF[MinVex][0] + " " + GHF[MinVex][1] + " " + GHF[MinVex][2]);
					break;
				}
			}
		}
		else{//没有子节点或者子节点被访问了，循环出栈
			while(vex==-1){
				stack.pop();
			}
		}
	}
	new Count().show(v0,g, stack);
}
}