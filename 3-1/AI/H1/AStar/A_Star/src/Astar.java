import java.util.Stack;

public class Astar{
int MaxWeight=10000;//��ʾ����󲻿ɴ�


public void A_Search(Graph g,int H[],int v0,int end){
	boolean flag=true;
	int x;//��ʾջ��Ԫ��
	int vex = v0;//Ѱ��Ŀ��ڵ�
	int MinF,MinVex= v0;//��¼��С��f(n)�Ͷ�Ӧ�Ľڵ�
	Stack<Integer> stack=new Stack<Integer>();
	int[][]GHF=new int[g.path.length][3];//�ֱ����ڴ洢g(n),h(n),f(n)
	for(int i =0; i < g.path.length; i++){
		GHF[i][0]=0;
		GHF[i][2]=MaxWeight;//��f(n)��ʼ��,1000��ʾ�����
	}
	stack.push(v0);//v0��ջ
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
		//�����һ���ӽڵ�
		vex=g.getFirstVex(x);
		if(vex==end){//�ҵ�Ŀ��ڵ�
			stack.push(vex);
			g.mark[x]=1;
//			flag=false;
			break;
		}
		if(vex!=-1){//�ӽڵ����ҵ�������
			if(g.mark[vex]==0){//û������
				GHF[vex][0]=GHF[x][0]+g.path[x][vex];//�ڵ�vex��g(n)
				GHF[vex][1]=H[vex];//�ڵ�vex��h(n)
				GHF[vex][2]=GHF[vex][0]+GHF[vex][1];
				if(GHF[vex][2]<MinF){
					MinF=GHF[vex][2];
					MinVex=vex;
				}
			}
	//����ʣ�µ��ڽӵ㣨��ȱ�����
			while(flag && vex!=-1){
				vex=g.getNextVex(x, vex);
				if(vex!=-1&&g.mark[vex]==0){//���ڽڵ�
					GHF[vex][0]=GHF[x][0]+g.path[x][vex];//�ڵ�vex��g(n)
					GHF[vex][1]=H[vex];//�ڵ�vex��h(n)
					GHF[vex][2]=GHF[vex][0]+GHF[vex][1];
					if(GHF[vex][2]<MinF){
						MinF=GHF[vex][2];
						MinVex=vex;
					}
				}
				if(vex==-1){//û���ڽӵ��ˣ���ʱȷ����С���Ľڵ㣬��ѹջ
					stack.push(MinVex);
					g.mark[MinVex]=1;
					System.out.println(MinVex + " " + GHF[MinVex][0] + " " + GHF[MinVex][1] + " " + GHF[MinVex][2]);
					break;
				}
				if(vex==end){
					stack.push(vex);//ѹջĿ��ڵ�
					g.mark[vex]=1;
					flag=false;
					System.out.println(MinVex + " " + GHF[MinVex][0] + " " + GHF[MinVex][1] + " " + GHF[MinVex][2]);
					break;
				}
			}
		}
		else{//û���ӽڵ�����ӽڵ㱻�����ˣ�ѭ����ջ
			while(vex==-1){
				stack.pop();
			}
		}
	}
	new Count().show(v0,g, stack);
}
}