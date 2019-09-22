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
  
//实现从文件中读入统计单词个数并按值从大到小输出 
  
  
public class naiveBayes {  
	public BufferedWriter output;
	public static final int EN = 15;//假设这里我抽取新邮件最高频率的15个词进行计算概率 
	public static Map<String, Double> wordRadio1 = new TreeMap<String,Double>();//单词和他在垃圾邮件里的概率
	public static Map<String, Double> wordRadio2 = new TreeMap<String,Double>();//单词和他在正常邮件里的概率

	public static void main(String[] args) throws Exception {  
    	train();//训练结果
    	test();//批量将全部数据测试一遍同时计算误判率召回率等等输出到outputAll.txt文件中
    	
    	/*随机测试部分*/
		//测试独立文件和单个文件统计
    	while(true) {
        	Scanner scan = new Scanner(System.in);
    		System.out.println("请输入您的阈值：_%?");
    		int n = scan.nextInt();
        	System.out.println("请随机输入data/test/all中的一个文件名进行测试：");
        	String filename = scan.next();
    		String fileName = "data/test/all/"+filename;
        	testOneFile(fileName,n);
    	}
    }
    public static void train() throws IOException {
		//批量文件夹统计
		//用训练集训练算法——train文件统计填充wordRadio数据
		System.out.println("**********************************************************" );
		wordRadio1 = getFolderWord("data/train/spam-train","output/spam-train-out.txt");
		wordRadio2 = getFolderWord("data/train/nonspam-train","output/nonspam-train-out.txt");
		
		//获取全部单词以及词频以及概率
		getFolderWord("data/test/spam-test","output/spam-test-out.txt");
		getFolderWord("data/test/nonspam-test","output/nonspam-test-out.txt");
		//这里把测试数据的情况也做了输出作为待会随机测试的对比数据
    }
    public static void testOneFile(String filename,int n) throws IOException {
    	Map<String, Integer> wordsCountFile = new TreeMap<String,Integer>(); 
		File file = new File(filename);		
		System.out.println(filename+"共有 "+getWord(file,wordsCountFile)+" 个单词");		
		System.out.println("样本编号："+filename+"\n测试结果：");
		judge(file,n);
		System.out.println("请在out.txt中查看统计结果");
    }
    public static boolean judge(File file,int n) throws IOException {
    	double radio = 0;//是垃圾邮件的概率
    	double radioSum1 = 1,radioSum2 = 1;
    	//用前n个单词的pro计算联合概率
    	//找到前n个单词和对应的概率
    	Map<String, Integer> wordsCount = new TreeMap<String,Integer>();
    	ArrayList<Map.Entry<String,Integer>> list = new ArrayList<Map.Entry<String,Integer>>();
    	ArrayList<Double> radiolist = new ArrayList<Double>();//存储排好序后的前n个单词的概率
        int sum = getWord(file,wordsCount);
        list = SortMap(sum,wordsCount,"out.txt");
        for(int i = 0;i < list.size() &&  i< EN; i++){
        	//System.out.print("单词—— "+ list.get(i).getKey());
        	//System.out.println("的概率为 "+ Pro(list.get(i).getKey()));
        	double wordRadio = Pro(list.get(i).getKey());
        	radioSum1 *= wordRadio;//P1P2……Pn
        	radioSum2 *= (1-wordRadio);//(1-P1)(1-P2)……(1-Pn)
            //System.out.println("radioSum1 is "+radioSum1);
            //System.out.println("radioSum2 is "+radioSum2);
        }

        radio = radioSum1/(radioSum1+radioSum2);
        if(radio*100 > n) {
        	System.out.println(file + "是垃圾邮件的概率是：" + radio+"，有"+n+"%以上的可能是垃圾邮件");
        	return true;
        }
        else {
        	System.out.println(file +"是垃圾邮件的概率是：" + radio+"，有"+n+"%以上的可能不是垃圾邮件");
        	return false;
        }
    
    }
    private static double Pro(String word) {
    	//P(S|W) = P(W|S)P(S)/(P(W|S)P(S)+P(W|H)P(H)) = P(W|S)/(P(W|S)+P(W|H))
    	double p1 = wordRadio1.get(word)==null?0:wordRadio1.get(word);//这个词在垃圾邮件出现的概率
    	//System.out.println("这个词在垃圾邮件出现的概率 : "+p1/1000);
    	double p2 = wordRadio2.get(word)==null?0:wordRadio2.get(word);//这个词在正常邮件出现的概率
    	//System.out.println("这个词在正常邮件出现的概率 : "+p2/1000);
    	
    	double p = 0.5;
    	if(p2==0 || p1==0)
    		p = 0.5;
    	else
    		p = p1/(p2+p1);
    	
    	//System.out.println("这个单词存在时它是垃圾邮件的概率 : "+ p);   	
    	return p;//排除掉某个词在某类文件中从未出现而影响判断
   }
    public static void test() throws IOException {
		//测试所有文件
		File writename = new File("output/outputAll.txt"); // 相对路径，如果没有则要建立一个新的txt文件
		writename.createNewFile(); // 创建新文件
		BufferedWriter output = new BufferedWriter(new FileWriter(writename));
		output.write("data/test/里所有文件的测试结果:\n"); 
		output.write("注意：可以从样本编号判断出是否是垃圾邮件\n含有msg的是正常邮件，spmaga是垃圾邮件，由此来对比误判情况\n"); 
		output.write("样本编号："+"测试结果：  /'是/'代表是垃圾邮件\n");
		//测试所有文件——只是按序全部测试一遍随机测试  阈值默认为90%
		int n = 90;
		String filename1 = "data/test/spam-test";
		File file1 = new File(filename1);
		File[] files1 = file1.listFiles();
		int sumSpam1 = 0,sumSpam2 = 0;//判断出来的垃圾邮件数目
		//遍历
		if (files1 != null) {
              for (File f : files1) {//是代表是垃圾邮件
            	  String s = "";
            	  if(judge(f,n)) {
            		   s = "是";
            	  }
            	  else {
            		  sumSpam1++;
            		  s = "不是";
            	  }
            	  output.write(s+"\n");            	  
              }
		  } 
		
		output.write("垃圾邮件被错判为正常的"+sumSpam1+"个\n");
		
		String filename2 = "data/test/nonspam-test";
		File file2 = new File(filename2);
		File[] files2 = file2.listFiles();
		if (files2 != null) {
            for (File f : files2) {//是代表是垃圾邮件
          	  String s = "";
          	  if(judge(f,n)) {
          		   s = "是";
          		  sumSpam2++;
          	  }
          	  else
          		  s = "不是";
          	  output.write(s+"\n");           	  
            }
		  } 
		output.write("正常邮件被错判为垃圾邮件的"+sumSpam2+"个\n");
		int wrong = sumSpam1+sumSpam2;//总的判错的=垃圾邮件被判正常的+正常的被判垃圾邮件的
		output.write("误判率为"+wrong*100/260+"%\n");
		output.write("准确率为"+(260-wrong)*100/260+"%\n");
		output.write("精确率为"+(130-sumSpam1)*100/(130-sumSpam1+sumSpam2)+"%\n");
		output.write("召回率为"+sumSpam1*100/130+"%\n");
		output.flush(); // 把缓存区内容压入文件
		output.close(); // 最后关闭文件
    }
    //计算一个目录下所有的文件的单词的词频
    public static Map<String, Double> getFolderWord(String fo,String out) throws IOException {
    	int sum = 0;//统计所有单词的数目
        Map<String, Integer> wordsCount = new TreeMap<String,Integer>(); 
        Map<String, Double> wordRadio = new TreeMap<String,Double>();//单词和他的概率
        //存储单词计数信息
        //key值为单词-value为单词数       
        
		System.out.println("The folder is "+ fo );
    	File folder = new File(fo);
		File[] files = folder.listFiles();
    	//System.out.println("The wordlist is ");
		//遍历
		if (files != null) {
              for (File f : files) {
              	//遍历所有的文件对某个文件操作
    				//System.out.println("The file is "+ f );
                    sum+=getWord(f,wordsCount);
              }
		  }
		wordRadio = getRadio(sum,SortMap(sum,wordsCount,out));    //按值进行排序  
		return wordRadio;
    }
    //计算一个文件中的所有的单词的词频
    public static int getWord(File f,Map<String, Integer> wordsCount) throws IOException {
    	int sum = 0;//所有单词的总数目
        BufferedReader br = new BufferedReader(new FileReader(f));  
        List<String> lists = new ArrayList<String>();  //存储过滤后单词的列表  
        String readLine = null;
		while((readLine = br.readLine()) != null){  
            String[] wordsArr1 = readLine.split("[^a-zA-Z]");  //过滤出只含有字母的  
            for (String word : wordsArr1) {
                if(word.length() > 2){  //只保留长度在2个字母以上的单词  
                    lists.add(word);  
                }  
            }  
        }      
        br.close();  
                    
        //单词的词频统计  
        for (String li : lists) {  
            if(wordsCount.get(li) != null){  
                wordsCount.put(li,wordsCount.get(li) + 1);  
            }else{  
                wordsCount.put(li,1);  //添加新词
            }  
        } 
        sum  = lists.size();
        //System.out.println(sum);
        return sum;
    }
    //按value的大小进行排序  
    public static ArrayList<Map.Entry<String,Integer>> SortMap(int sum,Map<String,Integer> oldmap,String s) throws IOException{  
          
        ArrayList<Map.Entry<String,Integer>> list = new ArrayList<Map.Entry<String,Integer>>(oldmap.entrySet());
        Map<String, Double> wordRadio = new TreeMap<String,Double>();
       // ArrayList<Map.Entry<String,Double>> listRadio;  
  
        Collections.sort(list,new Comparator<Map.Entry<String,Integer>>(){  
            @Override  
            public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {  
                return o2.getValue() - o1.getValue();  //降序  
            }  
        });  
 
		/* 写入文件 */
		File writename = new File(s); // 相对路径，如果没有则要建立一个新的txt文件
		writename.createNewFile(); // 创建新文件
		BufferedWriter out = new BufferedWriter(new FileWriter(writename));
		out.write("统计结果:\n"); 
		out.write("共计单词:"+sum+"个,"+"不重复单词："+list.size()+"个\n\n");
		out.write("单词\t\t\t出现次数\t\t\t概率\n"); 
		
        for(int i = 0; i<list.size(); i++){
            //System.out.println(list.get(i).getKey()+ ": " +list.get(i).getValue());
        	double radio = 100000 * list.get(i).getValue()/sum;
            out.write(list.get(i).getKey()+ "\t\t" +list.get(i).getValue()+"\t\t\t"+radio/1000+"%\r\n");
        }   
		out.flush(); // 把缓存区内容压入文件
		out.close(); // 最后关闭文件
		return list;
    }  
      
    //按value的大小进行排序  
    public static Map<String, Double> getRadio(int sum,ArrayList<Map.Entry<String,Integer>> list) throws IOException{  
        Map<String, Double> wordRadio = new TreeMap<String,Double>();//单词和他的概率
        for(int i = 0; i<list.size(); i++){
        	double radio = 100000 * list.get(i).getValue()/sum;
        	wordRadio.put(list.get(i).getKey(),radio);
        }   

		return wordRadio;
    }  
  
}  