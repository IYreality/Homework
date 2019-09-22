import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;  
import java.util.Collections;  
import java.util.Comparator;  
import java.util.List;  
import java.util.Map;  
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeMap;  
  
//ʵ�ִ��ļ��ж���ͳ�Ƶ��ʸ�������ֵ�Ӵ�С��� 
  
  
public class naiveBayes {  
	public BufferedWriter output;
	public static final int EN = 15;//���������ҳ�ȡ���ʼ����Ƶ�ʵ�15���ʽ��м������ 
	public static Map<String, Double> wordRadio1 = new TreeMap<String,Double>();//���ʺ����������ʼ���ĸ���
	public static Map<String, Double> wordRadio2 = new TreeMap<String,Double>();//���ʺ����������ʼ���ĸ���

	public static void main(String[] args) throws Exception {  
    	train();//ѵ�����
    	test();//������ȫ�����ݲ���һ��ͬʱ�����������ٻ��ʵȵ������outputAll.txt�ļ���
    	
    	/*������Բ���*/
		//���Զ����ļ��͵����ļ�ͳ��
    	while(true) {
        	Scanner scan = new Scanner(System.in);
    		System.out.println("������������ֵ��_%?");
    		int n = scan.nextInt();
        	System.out.println("���������data/test/all�е�һ���ļ������в��ԣ�");
        	String filename = scan.next();
    		String fileName = "data/test/all/"+filename;
        	testOneFile(fileName,n);
    	}
    }
    public static void train() throws IOException {
		//�����ļ���ͳ��
		//��ѵ����ѵ���㷨����train�ļ�ͳ�����wordRadio����
		System.out.println("**********************************************************" );
		wordRadio1 = getFolderWord("data/train/spam-train","output/spam-train-out.txt");
		wordRadio2 = getFolderWord("data/train/nonspam-train","output/nonspam-train-out.txt");
		
		//��ȡȫ�������Լ���Ƶ�Լ�����
		getFolderWord("data/test/spam-test","output/spam-test-out.txt");
		getFolderWord("data/test/nonspam-test","output/nonspam-test-out.txt");
		//����Ѳ������ݵ����Ҳ���������Ϊ����������ԵĶԱ�����
    }
    public static void testOneFile(String filename,int n) throws IOException {
    	Map<String, Integer> wordsCountFile = new TreeMap<String,Integer>(); 
		File file = new File(filename);		
		System.out.println(filename+"���� "+getWord(file,wordsCountFile)+" ������");		
		System.out.println("������ţ�"+filename+"\n���Խ����");
		judge(file,n);
		System.out.println("����out.txt�в鿴ͳ�ƽ��");
    }
    public static boolean judge(File file,int n) throws IOException {
    	double radio = 0;//�������ʼ��ĸ���
    	double radioSum1 = 1,radioSum2 = 1;
    	//��ǰn�����ʵ�pro�������ϸ���
    	//�ҵ�ǰn�����ʺͶ�Ӧ�ĸ���
    	Map<String, Integer> wordsCount = new TreeMap<String,Integer>();
    	ArrayList<Map.Entry<String,Integer>> list = new ArrayList<Map.Entry<String,Integer>>();
    	ArrayList<Double> radiolist = new ArrayList<Double>();//�洢�ź�����ǰn�����ʵĸ���
        int sum = getWord(file,wordsCount);
        list = SortMap(sum,wordsCount,"out.txt");
        for(int i = 0;i < list.size() &&  i< EN; i++){
        	//System.out.print("���ʡ��� "+ list.get(i).getKey());
        	//System.out.println("�ĸ���Ϊ "+ Pro(list.get(i).getKey()));
        	double wordRadio = Pro(list.get(i).getKey());
        	radioSum1 *= wordRadio;//P1P2����Pn
        	radioSum2 *= (1-wordRadio);//(1-P1)(1-P2)����(1-Pn)
            //System.out.println("radioSum1 is "+radioSum1);
            //System.out.println("radioSum2 is "+radioSum2);
        }

        radio = radioSum1/(radioSum1+radioSum2);
        if(radio*100 > n) {
        	System.out.println(file + "�������ʼ��ĸ����ǣ�" + radio+"����"+n+"%���ϵĿ����������ʼ�");
        	return true;
        }
        else {
        	System.out.println(file +"�������ʼ��ĸ����ǣ�" + radio+"����"+n+"%���ϵĿ��ܲ��������ʼ�");
        	return false;
        }
    
    }
    private static double Pro(String word) {
    	//P(S|W) = P(W|S)P(S)/(P(W|S)P(S)+P(W|H)P(H)) = P(W|S)/(P(W|S)+P(W|H))
    	double p1 = wordRadio1.get(word)==null?0:wordRadio1.get(word);//������������ʼ����ֵĸ���
    	//System.out.println("������������ʼ����ֵĸ��� : "+p1/1000);
    	double p2 = wordRadio2.get(word)==null?0:wordRadio2.get(word);//������������ʼ����ֵĸ���
    	//System.out.println("������������ʼ����ֵĸ��� : "+p2/1000);
    	
    	double p = 0.5;
    	if(p2==0 || p1==0)
    		p = 0.5;
    	else
    		p = p1/(p2+p1);
    	
    	//System.out.println("������ʴ���ʱ���������ʼ��ĸ��� : "+ p);   	
    	return p;//�ų���ĳ������ĳ���ļ��д�δ���ֶ�Ӱ���ж�
   }
    public static void test() throws IOException {
		//���������ļ�
		File writename = new File("output/outputAll.txt"); // ���·�������û����Ҫ����һ���µ�txt�ļ�
		writename.createNewFile(); // �������ļ�
		BufferedWriter output = new BufferedWriter(new FileWriter(writename));
		output.write("data/test/�������ļ��Ĳ��Խ��:\n"); 
		output.write("ע�⣺���Դ���������жϳ��Ƿ��������ʼ�\n����msg���������ʼ���spmaga�������ʼ����ɴ����Ա��������\n"); 
		output.write("������ţ�"+"���Խ����  /'��/'�����������ʼ�\n");
		//���������ļ�����ֻ�ǰ���ȫ������һ���������  ��ֵĬ��Ϊ90%
		int n = 90;
		String filename1 = "data/test/spam-test";
		File file1 = new File(filename1);
		File[] files1 = file1.listFiles();
		int sumSpam1 = 0,sumSpam2 = 0;//�жϳ����������ʼ���Ŀ
		//����
		if (files1 != null) {
              for (File f : files1) {//�Ǵ����������ʼ�
            	  String s = "";
            	  if(judge(f,n)) {
            		   s = "��";
            	  }
            	  else {
            		  sumSpam1++;
            		  s = "����";
            	  }
            	  output.write(s+"\n");            	  
              }
		  } 
		
		output.write("�����ʼ�������Ϊ������"+sumSpam1+"��\n");
		
		String filename2 = "data/test/nonspam-test";
		File file2 = new File(filename2);
		File[] files2 = file2.listFiles();
		if (files2 != null) {
            for (File f : files2) {//�Ǵ����������ʼ�
          	  String s = "";
          	  if(judge(f,n)) {
          		   s = "��";
          		  sumSpam2++;
          	  }
          	  else
          		  s = "����";
          	  output.write(s+"\n");           	  
            }
		  } 
		output.write("�����ʼ�������Ϊ�����ʼ���"+sumSpam2+"��\n");
		int wrong = sumSpam1+sumSpam2;//�ܵ��д��=�����ʼ�����������+�����ı��������ʼ���
		output.write("������Ϊ"+wrong*100/260+"%\n");
		output.write("׼ȷ��Ϊ"+(260-wrong)*100/260+"%\n");
		output.write("��ȷ��Ϊ"+(130-sumSpam1)*100/(130-sumSpam1+sumSpam2)+"%\n");
		output.write("�ٻ���Ϊ"+sumSpam1*100/130+"%\n");
		output.flush(); // �ѻ���������ѹ���ļ�
		output.close(); // ���ر��ļ�
    }
    //����һ��Ŀ¼�����е��ļ��ĵ��ʵĴ�Ƶ
    public static Map<String, Double> getFolderWord(String fo,String out) throws IOException {
    	int sum = 0;//ͳ�����е��ʵ���Ŀ
        Map<String, Integer> wordsCount = new TreeMap<String,Integer>(); 
        Map<String, Double> wordRadio = new TreeMap<String,Double>();//���ʺ����ĸ���
        //�洢���ʼ�����Ϣ
        //keyֵΪ����-valueΪ������       
        
		System.out.println("The folder is "+ fo );
    	File folder = new File(fo);
		File[] files = folder.listFiles();
    	//System.out.println("The wordlist is ");
		//����
		if (files != null) {
              for (File f : files) {
              	//�������е��ļ���ĳ���ļ�����
    				//System.out.println("The file is "+ f );
                    sum+=getWord(f,wordsCount);
              }
		  }
		wordRadio = getRadio(sum,SortMap(sum,wordsCount,out));    //��ֵ��������  
		return wordRadio;
    }
    //����һ���ļ��е����еĵ��ʵĴ�Ƶ
    public static int getWord(File f,Map<String, Integer> wordsCount) throws IOException {
    	int sum = 0;//���е��ʵ�����Ŀ
        BufferedReader br = new BufferedReader(new FileReader(f));  
        List<String> lists = new ArrayList<String>();  //�洢���˺󵥴ʵ��б�  
        String readLine = null;
		while((readLine = br.readLine()) != null){  
            String[] wordsArr1 = readLine.split("[^a-zA-Z]");  //���˳�ֻ������ĸ��  
            for (String word : wordsArr1) {
                if(word.length() > 2){  //ֻ����������2����ĸ���ϵĵ���  
                    lists.add(word);  
                }  
            }  
        }      
        br.close();  
                    
        //���ʵĴ�Ƶͳ��  
        for (String li : lists) {  
            if(wordsCount.get(li) != null){  
                wordsCount.put(li,wordsCount.get(li) + 1);  
            }else{  
                wordsCount.put(li,1);  //����´�
            }  
        } 
        sum  = lists.size();
        //System.out.println(sum);
        return sum;
    }
    //��value�Ĵ�С��������  
    public static ArrayList<Map.Entry<String,Integer>> SortMap(int sum,Map<String,Integer> oldmap,String s) throws IOException{  
          
        ArrayList<Map.Entry<String,Integer>> list = new ArrayList<Map.Entry<String,Integer>>(oldmap.entrySet());
        Map<String, Double> wordRadio = new TreeMap<String,Double>();
       // ArrayList<Map.Entry<String,Double>> listRadio;  
  
        Collections.sort(list,new Comparator<Map.Entry<String,Integer>>(){  
            @Override  
            public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {  
                return o2.getValue() - o1.getValue();  //����  
            }  
        });  
 
		/* д���ļ� */
		File writename = new File(s); // ���·�������û����Ҫ����һ���µ�txt�ļ�
		writename.createNewFile(); // �������ļ�
		BufferedWriter out = new BufferedWriter(new FileWriter(writename));
		out.write("ͳ�ƽ��:\n"); 
		out.write("���Ƶ���:"+sum+"��,"+"���ظ����ʣ�"+list.size()+"��\n\n");
		out.write("����\t\t\t���ִ���\t\t\t����\n"); 
		
        for(int i = 0; i<list.size(); i++){
            //System.out.println(list.get(i).getKey()+ ": " +list.get(i).getValue());
        	double radio = 100000 * list.get(i).getValue()/sum;
            out.write(list.get(i).getKey()+ "\t\t" +list.get(i).getValue()+"\t\t\t"+radio/1000+"%\r\n");
        }   
		out.flush(); // �ѻ���������ѹ���ļ�
		out.close(); // ���ر��ļ�
		return list;
    }  
      
    //��value�Ĵ�С��������  
    public static Map<String, Double> getRadio(int sum,ArrayList<Map.Entry<String,Integer>> list) throws IOException{  
        Map<String, Double> wordRadio = new TreeMap<String,Double>();//���ʺ����ĸ���
        for(int i = 0; i<list.size(); i++){
        	double radio = 100000 * list.get(i).getValue()/sum;
        	wordRadio.put(list.get(i).getKey(),radio);
        }   

		return wordRadio;
    }  
  
}  