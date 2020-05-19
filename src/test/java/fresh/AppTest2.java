package fresh;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

public class AppTest2 {
	public static void main(String[] args) {
		String strs[] = {"哈哈哈啊","嘻嘻嘻呀","中国有嘻哈","我喜欢呀","程序员写程序写的烦死了","程序员不想写代码的一天"};
		String word = "哈哈嘻嘻嘿嘿成功百度股份有限公司公司阿里巴巴集团腾讯股份";
		List<String> list = analyzerCnStr(word);
		List<String> result = new ArrayList<>();
		for(String kw : list){
			for(String str : strs){
				if(str.contains(kw)){
					result.add(str);
				}
			}
		}
		System.out.println(list);
		System.out.println(result);
		System.out.println(result.stream().distinct().collect(Collectors.toList()));	//list去重 
	}

	public static List<String> analyzerCnStr(String str) {
		List<String> list = new ArrayList<String>();
		
		try {
			SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer();		//使用中文分词器
			
			TokenStream tokenStream = analyzer.tokenStream("field", str);
			CharTermAttribute term = tokenStream.addAttribute(CharTermAttribute.class);
			tokenStream.reset();
			while (tokenStream.incrementToken()) {
				list.add(term.toString());
			}
			tokenStream.end();
			tokenStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
}
