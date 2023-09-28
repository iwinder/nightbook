package com.windcoder.nightbook.services.impl;

import com.windcoder.common.utills.DateUtillZ;
import com.windcoder.common.utills.DouBanUtils;
import com.windcoder.common.utills.ReturnResult;
import com.windcoder.common.utills.https.HttpsUtil;
import com.windcoder.nightbook.entity.BookInfo;
import com.windcoder.nightbook.entity.BookSearch;
import com.windcoder.nightbook.services.IBookInfoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Description:
 * User: WindCoder
 * Date: 2017-10-17
 * Time: 20:29 下午
 */
@Service("bookInfoService")
public class BookInfoSericeImpl implements IBookInfoService {

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    private static Logger logger = LogManager.getLogger(BookInfoSericeImpl.class);
    /**
     * 添加
     * @param b
     * @return
     */
    public ReturnResult addBookInfo(BookInfo b){
        ReturnResult r = new ReturnResult();
        b.setCreatetime(DateUtillZ.getTimestampOfNow());
        int bol = 0;
        try {
            bol = sqlSessionTemplate.insert("BookInfo.addBookInfo", b);
            if (bol>0){
                r.setResult(b.getBid());
                r.setMsg("addBookInfo success");
            }else{
                bol = -2;
                r.setMsg("addBookInfo error");
            }
        }catch (Exception e){
            bol = -1;
            logger.error(e);
            r.setMsg("addBookInfo error");
        }
        r.setCode(bol);
        return r;
    }

    public BookInfo findBookInfoBaseByIsbn(String isbn){
        return sqlSessionTemplate.selectOne("BookInfo.findBookInfoBaseByIsbn",isbn);
    }

    public BookInfo findBookInfoByBid(Integer bid){
        return sqlSessionTemplate.selectOne("BookInfo.findBookInfoByBid",bid);
    }
    public Integer findBidByIsbn(String isbn){
        return sqlSessionTemplate.selectOne("BookInfo.findBidByIsbn",isbn);
    }

    public String findIsbnByBid(int bid){
        return sqlSessionTemplate.selectOne("BookInfo.findIsbnByBid",bid);
    }



    public BookInfo findBookInfoBaseByIsbnORNot(String isbn){
       // 查询数据库是否存在该书籍
        BookInfo bookInfoBase = findBookInfoBaseByIsbn(isbn);
        if (bookInfoBase == null){
            //不存在，前往豆瓣获取图书信息，添加booke数据库，添加用户拥有信息，返回对应信息。
            bookInfoBase = findBookInfoFromDouBanByISBN(isbn);
            if (bookInfoBase!=null){
             //不为空，添加到数据库
                addBookInfo(bookInfoBase);
            }
        }
        //存在，添加用户拥有记录，返回相应信息
        return bookInfoBase;


    }




    /**
     * 从豆瓣获取信息--ISBN查询
     * @param isbn
     * @return
     */
    public  BookInfo findBookInfoFromDouBanByISBN(String isbn){
        StringBuffer url = new StringBuffer(DouBanUtils.ISBNURL);
        url.append(isbn);
        JSONObject resultData = HttpsUtil.httpRequest(url.toString(), "GET", "");
        System.out.println("sjs error:"+resultData.toString());
        if(resultData.has("data")) {
            String dataStr = resultData.getString("data");
            JSONObject result = new JSONObject(dataStr);
            BookInfo bookInfo = DouBanUtils.JsonToBookeInfo(result);
            return bookInfo;
        }
        return null;
    }

    /**
     * 从豆瓣获取信息--关键字查询
     * @param bookSearch
     * @return
     */
    public  List<BookInfo> findBookInfoFromDouBanByKyes(BookSearch bookSearch){
        StringBuffer url = new StringBuffer(DouBanUtils.TAGURL);
        url.append("?q=");
        try {
            url.append(URLEncoder.encode(bookSearch.getKeys(),"utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            url.append(bookSearch.getKeys());
        }
        url.append("&count=");
        url.append(bookSearch.getCount());
        url.append("&start=");
        url.append(bookSearch.getStart());


        JSONObject resultData = HttpsUtil.httpRequest(url.toString(), "GET", "");
        System.out.println("sjs error:"+resultData.toString());
        if(resultData.has("data")) {
            String dataStr = resultData.getString("data");
            JSONObject result = new JSONObject(dataStr);
            List<BookInfo> bookInfos = DouBanUtils.JsonArryToBookeInfo(result);
            if (result.has("total")){
                bookSearch.setTotal(result.getInt("total"));
            }
            int bookLen = bookInfos.size();
            for(int i =0;i<bookLen;i++){
                addBookInfo(bookInfos.get(i));
            }
            return bookInfos;
        }
        return null;
    }



    public List<BookInfo> findBookInfoFromDouBanByKyesTest(BookSearch bookSearch){
        String s = "{\n" +
                "count: 10,\n" +
                "start: 0,\n" +
                "total: 16,\n" +
                "books: [\n" +
                "{\n" +
                "rating: {\n" +
                "max: 10,\n" +
                "numRaters: 136,\n" +
                "average: \"9.4\",\n" +
                "min: 0\n" +
                "},\n" +
                "subtitle: \"英文版·第4版\",\n" +
                "author: [\n" +
                "\"埃克尔\"\n" +
                "],\n" +
                "pubdate: \"2007-5-1\",\n" +
                "tags: [\n" +
                "{\n" +
                "count: 105,\n" +
                "name: \"Java\",\n" +
                "title: \"Java\"\n" +
                "},\n" +
                "{\n" +
                "count: 61,\n" +
                "name: \"编程\",\n" +
                "title: \"编程\"\n" +
                "},\n" +
                "{\n" +
                "count: 30,\n" +
                "name: \"计算机\",\n" +
                "title: \"计算机\"\n" +
                "},\n" +
                "{\n" +
                "count: 20,\n" +
                "name: \"程序设计\",\n" +
                "title: \"程序设计\"\n" +
                "},\n" +
                "{\n" +
                "count: 20,\n" +
                "name: \"OOP\",\n" +
                "title: \"OOP\"\n" +
                "},\n" +
                "{\n" +
                "count: 19,\n" +
                "name: \"经典\",\n" +
                "title: \"经典\"\n" +
                "},\n" +
                "{\n" +
                "count: 16,\n" +
                "name: \"编程语言\",\n" +
                "title: \"编程语言\"\n" +
                "},\n" +
                "{\n" +
                "count: 15,\n" +
                "name: \"programming\",\n" +
                "title: \"programming\"\n" +
                "}\n" +
                "],\n" +
                "origin_title: \"Thinking in Java\",\n" +
                "image: \"https://img3.doubanio.com/mpic/s27969801.jpg\",\n" +
                "binding: \"平装\",\n" +
                "translator: [ ],\n" +
                "catalog: \"\",\n" +
                "pages: \"1482\",\n" +
                "images: {\n" +
                "small: \"https://img3.doubanio.com/spic/s27969801.jpg\",\n" +
                "large: \"https://img3.doubanio.com/lpic/s27969801.jpg\",\n" +
                "medium: \"https://img3.doubanio.com/mpic/s27969801.jpg\"\n" +
                "},\n" +
                "alt: \"https://book.douban.com/subject/2061172/\",\n" +
                "id: \"2061172\",\n" +
                "publisher: \"机械工业出版社\",\n" +
                "isbn10: \"7111212509\",\n" +
                "isbn13: \"9787111212508\",\n" +
                "title: \"Java编程思想\",\n" +
                "url: \"https://api.douban.com/v2/book/2061172\",\n" +
                "alt_title: \"Thinking in Java\",\n" +
                "author_intro: \"\",\n" +
                "summary: \"《Java编程思想(英文版•第4版)》内容简介：特色：1.适合初学者与专业人员的经典的面向对象的叙述方式；为更新的Java SE5/6的相关内容增加了新的示例和章节。2.测验框架显示程序输出。3.设计模式贯穿于众多示例中：适配器、桥接器、职责链、命令、装饰器、外观、工厂方法、享元、点名、数据传输对象、空对象、代理、单例、状态、策略、模板方法以及访问者。4.为数据传输引入了XML；为用户界面引入了SWT和Flash。5.重新撰写了有关并发的章节，使您能牢牢地掌握线程的相关知识。6.专门为第4版以及Java SE5/6重写了在700多个编译文件中的500多个程序。7.支持网站包含了所有源代码、带注解的解决方案指南、网络日志以及多媒体学习资料。8.完全覆盖了所有基础知识，同时也论述了高级特性。9.详细彻底地阐述了面向对象原理。10.在线可获得Java讲座CD，包含Bruce Eckel的全部多媒体讲座。\",\n" +
                "series: {\n" +
                "id: \"1895\",\n" +
                "title: \"经典原版书库\"\n" +
                "},\n" +
                "price: \"79.00元\"\n" +
                "},\n" +
                "{\n" +
                "rating: {\n" +
                "max: 10,\n" +
                "numRaters: 601,\n" +
                "average: \"9.0\",\n" +
                "min: 0\n" +
                "},\n" +
                "subtitle: \"第3版\",\n" +
                "author: [\n" +
                "\"[美] Bruce Eckel\"\n" +
                "],\n" +
                "pubdate: \"2005-9\",\n" +
                "tags: [\n" +
                "{\n" +
                "count: 400,\n" +
                "name: \"java\",\n" +
                "title: \"java\"\n" +
                "},\n" +
                "{\n" +
                "count: 177,\n" +
                "name: \"编程\",\n" +
                "title: \"编程\"\n" +
                "},\n" +
                "{\n" +
                "count: 148,\n" +
                "name: \"java编程思想\",\n" +
                "title: \"java编程思想\"\n" +
                "},\n" +
                "{\n" +
                "count: 105,\n" +
                "name: \"计算机\",\n" +
                "title: \"计算机\"\n" +
                "},\n" +
                "{\n" +
                "count: 65,\n" +
                "name: \"软件开发\",\n" +
                "title: \"软件开发\"\n" +
                "},\n" +
                "{\n" +
                "count: 63,\n" +
                "name: \"Programming\",\n" +
                "title: \"Programming\"\n" +
                "},\n" +
                "{\n" +
                "count: 62,\n" +
                "name: \"Java\",\n" +
                "title: \"Java\"\n" +
                "},\n" +
                "{\n" +
                "count: 42,\n" +
                "name: \"技术\",\n" +
                "title: \"技术\"\n" +
                "}\n" +
                "],\n" +
                "origin_title: \"Thinking in Java\",\n" +
                "image: \"https://img1.doubanio.com/mpic/s1320039.jpg\",\n" +
                "binding: \"平装\",\n" +
                "translator: [\n" +
                "\"陈昊鹏\",\n" +
                "\"饶若楠\"\n" +
                "],\n" +
                "catalog: \"\",\n" +
                "pages: \"756\",\n" +
                "images: {\n" +
                "small: \"https://img1.doubanio.com/spic/s1320039.jpg\",\n" +
                "large: \"https://img1.doubanio.com/lpic/s1320039.jpg\",\n" +
                "medium: \"https://img1.doubanio.com/mpic/s1320039.jpg\"\n" +
                "},\n" +
                "alt: \"https://book.douban.com/subject/1313042/\",\n" +
                "id: \"1313042\",\n" +
                "publisher: \"机械工业出版社\",\n" +
                "isbn10: \"711116220X\",\n" +
                "isbn13: \"9787111162209\",\n" +
                "title: \"Java编程思想\",\n" +
                "url: \"https://api.douban.com/v2/book/1313042\",\n" +
                "alt_title: \"Thinking in Java\",\n" +
                "author_intro: \"\",\n" +
                "summary: \"本书赢得了全球程序员的广泛赞誉，即使是最晦涩的概念，在Bruce Eckel的文字亲和力和小而直接的编程示例面前也会化解于无形。从Java的基础语法到最高级特性（深入的面向对象概念、多线程、自动项目构建、单元测试和调试等），本书都能逐步指导你轻松掌握。 从本书获得的各项大奖以及来自世界各地的读者评论中，不难看出这是一本经典之作。本书的作者拥有多年教学经验，对C、C++以及Java语言都有独到、深入的见解，以通俗易懂及小而直接的示例解释了一个个晦涩抽象的概念。本书共22章，包括操作符、控制执行流程、访问权限控制、复用类、多态、接口、通过异常处理错误、字符串、泛型、数组、容器深入研究、Java I/O系统、枚举类型、并发以及图形化用户界面等内容。这些丰富的内容，包含了Java语言基础语法以及高级特性，适合各个层次的Java程序员阅读，同时也是高等院校讲授面向对象程序设计语言以及Java语言的绝佳教材和参考书。\",\n" +
                "series: {\n" +
                "id: \"1163\",\n" +
                "title: \"计算机科学丛书\"\n" +
                "},\n" +
                "price: \"95.00元\"\n" +
                "},\n" +
                "{\n" +
                "rating: {\n" +
                "max: 10,\n" +
                "numRaters: 2830,\n" +
                "average: \"9.1\",\n" +
                "min: 0\n" +
                "},\n" +
                "subtitle: \"\",\n" +
                "author: [\n" +
                "\"[美] Bruce Eckel\"\n" +
                "],\n" +
                "pubdate: \"2007-6\",\n" +
                "tags: [\n" +
                "{\n" +
                "count: 2693,\n" +
                "name: \"Java\",\n" +
                "title: \"Java\"\n" +
                "},\n" +
                "{\n" +
                "count: 1058,\n" +
                "name: \"编程思想\",\n" +
                "title: \"编程思想\"\n" +
                "},\n" +
                "{\n" +
                "count: 819,\n" +
                "name: \"编程\",\n" +
                "title: \"编程\"\n" +
                "},\n" +
                "{\n" +
                "count: 729,\n" +
                "name: \"TinkingInJava\",\n" +
                "title: \"TinkingInJava\"\n" +
                "},\n" +
                "{\n" +
                "count: 653,\n" +
                "name: \"计算机\",\n" +
                "title: \"计算机\"\n" +
                "},\n" +
                "{\n" +
                "count: 511,\n" +
                "name: \"程序设计\",\n" +
                "title: \"程序设计\"\n" +
                "},\n" +
                "{\n" +
                "count: 412,\n" +
                "name: \"经典\",\n" +
                "title: \"经典\"\n" +
                "},\n" +
                "{\n" +
                "count: 314,\n" +
                "name: \"软件开发\",\n" +
                "title: \"软件开发\"\n" +
                "}\n" +
                "],\n" +
                "origin_title: \"Thinking in Java\",\n" +
                "image: \"https://img3.doubanio.com/mpic/s27243455.jpg\",\n" +
                "binding: \"平装\",\n" +
                "translator: [\n" +
                "\"陈昊鹏\"\n" +
                "],\n" +
                "catalog: \"读者评论 前言 简介 第1章 对象导论 1.1 抽象过程 1.2 每个对象都有一个接口 1.3 每个对象都提供服务 1.4 被隐藏的具体实现 1.5 复用具体实现 1.6 继承 1.6.1 “是一个”（is-a）与“像是一个”（is-like-a）关系 1.7 伴随多态的可互换对象 1.8 单根继承结构 1.9 容器 1.9.1 参数化类型（范型） 1.10 对象的创建和生命期 1.11 异常处理：处理错误 1.12 并发编程 1.13 Java与Internet 1.13.1 Web是什么 1.13.2 客户端编程 1.13.3 服务器端编程 1.22 总结 第2章 一切都是对象 2.1 用引用操纵对象 2.2 必须由你创建所有对象 2.2.1 存储到什么地方 2.2.2 特例：基本类型 2.2.3 Java中的数组 2.3 永远不需要销毁对象 2.3.1 作用域 2.3.2 对象的作用域 2.4 创建新的数据类型：类 2.4.1 域和方法 2.4.2 基本成员默认值 2.5 方法、参数和返回值 2.5.1 参数列表 2.6 构建一个Java程序 2.6.1 名字可见性 2.6.2 运用其他构件 2.6.3 static 关键字 2.7 你的第一个Java程序 编译和运行 2.8 注释和嵌入式文档 2.8.1 注释文档 2.8.2 语法 2.8.3 嵌入式HTML 2.8.4 一些标签示例 2.8.5 文档示例 2.9 编码风格 2.10 总结 2.11 练习 第3章 操作符 3.1 更简单的打印语句 3.2 使用Java操作符 3.3 优先级 3.4 赋值 3.4.1 方法调用中的别名问题 3.5 算术操作符 3.5.1 一元加、减操作符 3.6 自动递增和递减 3.7 关系操作符 3.7.1 测试对象的等价性 3.8 逻辑操作符 3.8.1 短路 3.9 直接常量 3.9.1 指数记数法 3.10 按位操作符 3.11 移位操作符 3.12 三元操作符 if-else 3.13 字符串操作符 + 和 += 3.14 使用操作符时常犯的错误 3.15 类型转换操作符 3.15.1 截尾和舍入 3.15.2提升 3.16 Java没有“sizeof” 3.17 操作符小结 3.18 总结 第4章 控制执行流程 4.1 true和false 4.2 if-else 4.3 迭代 4.3.1 do-while 4.3.2 for 4.3.3 逗号操作符 4.4 Foreach语法 4.5 return 4.6 break和 continue 4.7 臭名昭著的“goto” 4.8 switch 4.9 总结 第5章 初始化与清理 5.1 用构造器确保初始化 5.2 方法重载 5.2.1 区分重载方法 5.2.2 涉及基本类型的重载 5.2.3 以返回值区分重载方法 5.3 缺省构造器 5.4 this关键字 5.4.1 在构造器中调用构造器 5.4.2 static的含义 5.5 清理：终结处理和垃圾回收 5.5.1 finalize()的用途何在 5.5.2 你必须实施清理 5.5.3 终结条件 5.5.4 垃圾回收器如何工作 5.6 成员初始化 5.6.1 指定初始化 5.7 构造器初始化 5.7.1 初始化顺序 5.7.2. 静态数据的初始化 5.7.3. 显式的静态初始化 5.7.4. 非静态实例初始化 5.8 数组初始化 5.8.1 可变参数列表 5.9 枚举类型 5.10 总结 第6章 访问权限控制 第7章 复用类 第8章 多态 第9章 接口 第10章 内部类 第11章 持有对象 第12章 通过异常处理错误 第13章 字符串 第14章 类型信息 第15章 泛型 第16章 数组 第17章 容器深入研究 第18章 Java I/O系统 第19章 枚举类型 第20章 注解 第21章 并发 第22章 图形化用户界面 附录A 补充材料 可下载的补充材料 Thinking in C：Java的基础 Java编程思想 研讨课 Hands-on Java研讨课CD Thinking in Objects研讨课 Thinking in Enterprise Java Thinking in Patterns(with Java) Thinking in Patterns研讨课 设计咨询与复审 附录B 资源 软件 编辑器与IDE 书籍 分析与设计 Python 我的著作列表 索引\",\n" +
                "pages: \"880\",\n" +
                "images: {\n" +
                "small: \"https://img3.doubanio.com/spic/s27243455.jpg\",\n" +
                "large: \"https://img3.doubanio.com/lpic/s27243455.jpg\",\n" +
                "medium: \"https://img3.doubanio.com/mpic/s27243455.jpg\"\n" +
                "},\n" +
                "alt: \"https://book.douban.com/subject/2130190/\",\n" +
                "id: \"2130190\",\n" +
                "publisher: \"机械工业出版社\",\n" +
                "isbn10: \"7111213823\",\n" +
                "isbn13: \"9787111213826\",\n" +
                "title: \"Java编程思想 （第4版）\",\n" +
                "url: \"https://api.douban.com/v2/book/2130190\",\n" +
                "alt_title: \"Thinking in Java\",\n" +
                "author_intro: \"Bruce Eckel是MindView公司（www.MindView.net）的总裁，该公司向客户提供软件咨询和培训。他是C++标准委员会拥有表决权的成员之一，拥有应用物理学学士和计算机工程硕士学位。除本书外，他还是《C++编程思想》的作者，并与人合著了《C++编程思想 第2卷》（这两本书的英文影印版及中文版均已由机械工业出版社引进出版）及其他著作。他已经发表了150多篇论文，还经常参加世界各地的研讨会并进行演讲。\",\n" +
                "summary: \"本书赢得了全球程序员的广泛赞誉，即使是最晦涩的概念，在Bruce Eckel的文字亲和力和小而直接的编程示例面前也会化解于无形。从Java的基础语法到最高级特性（深入的面向对象概念、多线程、自动项目构建、单元测试和调试等），本书都能逐步指导你轻松掌握。 从本书获得的各项大奖以及来自世界各地的读者评论中，不难看出这是一本经典之作。本书的作者拥有多年教学经验，对C、C++以及Java语言都有独到、深入的见解，以通俗易懂及小而直接的示例解释了一个个晦涩抽象的概念。本书共22章，包括操作符、控制执行流程、访问权限控制、复用类、多态、接口、通过异常处理错误、字符串、泛型、数组、容器深入研究、Java I/O系统、枚举类型、并发以及图形化用户界面等内容。这些丰富的内容，包含了Java语言基础语法以及高级特性，适合各个层次的Java程序员阅读，同时也是高等院校讲授面向对象程序设计语言以及Java语言的绝佳教材和参考书。 第4版特点： 适合初学者与专业人员的经典的面向对象叙述方式，为更新的Java SE5/6增加了新的示例和章节。 \uF06E 测验框架显示程序输出。\",\n" +
                "series: {\n" +
                "id: \"1163\",\n" +
                "title: \"计算机科学丛书\"\n" +
                "},\n" +
                "price: \"108.00元\"\n" +
                "},\n" +
                "{\n" +
                "rating: {\n" +
                "max: 10,\n" +
                "numRaters: 632,\n" +
                "average: \"8.7\",\n" +
                "min: 0\n" +
                "},\n" +
                "subtitle: \"\",\n" +
                "author: [\n" +
                "\"[美] Bruce Eckel\"\n" +
                "],\n" +
                "pubdate: \"2002-9\",\n" +
                "tags: [\n" +
                "{\n" +
                "count: 445,\n" +
                "name: \"Java\",\n" +
                "title: \"Java\"\n" +
                "},\n" +
                "{\n" +
                "count: 142,\n" +
                "name: \"编程思想\",\n" +
                "title: \"编程思想\"\n" +
                "},\n" +
                "{\n" +
                "count: 104,\n" +
                "name: \"编程\",\n" +
                "title: \"编程\"\n" +
                "},\n" +
                "{\n" +
                "count: 98,\n" +
                "name: \"计算机\",\n" +
                "title: \"计算机\"\n" +
                "},\n" +
                "{\n" +
                "count: 83,\n" +
                "name: \"软件开发\",\n" +
                "title: \"软件开发\"\n" +
                "},\n" +
                "{\n" +
                "count: 78,\n" +
                "name: \"程序设计\",\n" +
                "title: \"程序设计\"\n" +
                "},\n" +
                "{\n" +
                "count: 55,\n" +
                "name: \"经典\",\n" +
                "title: \"经典\"\n" +
                "},\n" +
                "{\n" +
                "count: 40,\n" +
                "name: \"IT\",\n" +
                "title: \"IT\"\n" +
                "}\n" +
                "],\n" +
                "origin_title: \"Thinking in Java\",\n" +
                "image: \"https://img1.doubanio.com/mpic/s1085058.jpg\",\n" +
                "binding: \"平装\",\n" +
                "translator: [\n" +
                "\"侯捷\"\n" +
                "],\n" +
                "catalog: \"出版者的话 专家指导委员会 读者回应 关于《Thinking in C++》 题献 译序 Java环境设定 序言 简介 第一章 对象导论 第二章 万事万物皆对象 第三章 控制程序流程 第四章 初始化与清理 第五章 隐藏实现细目 第六章 重复运用Classes 第七章 多态 第八章 接口与内隐类 …… 附录A 对象的传递和返回 …… 索引\",\n" +
                "pages: \"809\",\n" +
                "images: {\n" +
                "small: \"https://img1.doubanio.com/spic/s1085058.jpg\",\n" +
                "large: \"https://img1.doubanio.com/lpic/s1085058.jpg\",\n" +
                "medium: \"https://img1.doubanio.com/mpic/s1085058.jpg\"\n" +
                "},\n" +
                "alt: \"https://book.douban.com/subject/1101158/\",\n" +
                "id: \"1101158\",\n" +
                "publisher: \"机械工业出版社\",\n" +
                "isbn10: \"7111104412\",\n" +
                "isbn13: \"9787111104414\",\n" +
                "title: \"Java编程思想(第2版)\",\n" +
                "url: \"https://api.douban.com/v2/book/1101158\",\n" +
                "alt_title: \"Thinking in Java\",\n" +
                "author_intro: \"Bruce Eckel是Thinking in C++和《C++编程思想》的作者。他有20年专业编程经验，并自1986年起教育人们如何撰写面向对象程序，足迹遍及全球，成为一位知名的C++教师和顾问，如今兼涉Java。Eckel是C++标准委员会拥有表决权的成员之一，曾经写过另五本面向对象编程书籍，发表过150篇以上的文章，是多本计算机杂志的专栏作家。\",\n" +
                "summary: \"作者根据多年教学实践中发现的问题，通过简练的示例和叙述，阐明了在学习Java中特别容易混淆的诸多概念。与前一版相比，此第2版不但新增了Java2的语言特性，还根据语言的演变作出了彻底的更新，其中最主要的改变是第9章的群集。\",\n" +
                "series: {\n" +
                "id: \"1163\",\n" +
                "title: \"计算机科学丛书\"\n" +
                "},\n" +
                "price: \"99.00元\"\n" +
                "},\n" +
                "{\n" +
                "rating: {\n" +
                "max: 10,\n" +
                "numRaters: 18,\n" +
                "average: \"7.4\",\n" +
                "min: 0\n" +
                "},\n" +
                "subtitle: \"第4版(评注版)\",\n" +
                "author: [\n" +
                "\"Bruce Eckel\"\n" +
                "],\n" +
                "pubdate: \"2011-6-1\",\n" +
                "tags: [\n" +
                "{\n" +
                "count: 13,\n" +
                "name: \"java\",\n" +
                "title: \"java\"\n" +
                "},\n" +
                "{\n" +
                "count: 6,\n" +
                "name: \"Java\",\n" +
                "title: \"Java\"\n" +
                "},\n" +
                "{\n" +
                "count: 4,\n" +
                "name: \"程序设计\",\n" +
                "title: \"程序设计\"\n" +
                "},\n" +
                "{\n" +
                "count: 3,\n" +
                "name: \"计算机\",\n" +
                "title: \"计算机\"\n" +
                "},\n" +
                "{\n" +
                "count: 3,\n" +
                "name: \"经典\",\n" +
                "title: \"经典\"\n" +
                "},\n" +
                "{\n" +
                "count: 3,\n" +
                "name: \"Programming\",\n" +
                "title: \"Programming\"\n" +
                "},\n" +
                "{\n" +
                "count: 1,\n" +
                "name: \"编程\",\n" +
                "title: \"编程\"\n" +
                "},\n" +
                "{\n" +
                "count: 1,\n" +
                "name: \"科技\",\n" +
                "title: \"科技\"\n" +
                "}\n" +
                "],\n" +
                "origin_title: \"Thinking in Java (4th Edition)\",\n" +
                "image: \"https://img3.doubanio.com/mpic/s6618125.jpg\",\n" +
                "binding: \"\",\n" +
                "translator: [ ],\n" +
                "catalog: \"《java编程思想(第4版)(评注版)》 第1部分 基本语法 operators（新增批注30条） 1 simpler print statements 1 using java operators 2 precedence 2 assignment 3 mathematical operators 4 unary minus and plus operators 6 auto increment and decrement 6 relational operators 7 testing object equivalence 7 logical operators 9 literals 10 exponential notation 11 bitwise operators 12 shift operators 13 ternary if-else operator 16 string operator + and += 17 common pitfalls when using .operators 18 casting operators 18 truncation and rounding 19 promotion 20 java has no “sizeof ” 20 summary 20 controlling execution （新增批注21条） 21 true and false 21 if-else 21 iteration 22 do-while 23 for 23 the comma operator 24 foreach syntax 25 return 27 break and continue 27 the infamous “goto” 29 switch 32 summary 34 第2部分 面向对象 initialization & cleanup （新增批注55条） 35 guaranteed initialization with the constructor 35 method overloading 37 distinguishing overloaded methods 39 overloading with primitives 39 overloading on return values 42 default constructors 43 the this keyword 44 calling constructors from constructors 46 the meaning of static 47 cleanup: finalization and garbage collection 47 what is finalize() for? 48 you must perform cleanup 49 the termination condition 50 how a garbage collector works 51 member initialization 54 specifying initialization 55 constructor initialization 56 order of initialization 56 static data initialization 57 explicit static initialization 59 non-static instance initialization 61 array initialization 62 variable argument lists 65 enumerated types 70 summary 72 access control （新增批注21条） 73 package: the library unit 74 code organization 75 creating unique package names 76 a custom tool library 79 java access specifiers 80 package access 80 public: interface access 81 private: you can’t touch that! 82 protected: inheritance access 83 interface and implementation 85 class access 86 summary 87 reusing classes （新增批注35条） 89 composition syntax 89 inheritance syntax 92 initializing the base class 94 delegation 96 combining composition and inheritance 97 guaranteeing proper cleanup 99 name hiding 101 choosing composition vs. inheritance 103 protected 104 upcasting 105 why “upcasting”? 106 composition vs. inheritance revisited 106 the final keyword 107 final data 107 final methods 110 final classes 112 final caution 113 initialization and class loading 113 initialization with inheritance 114 summary 115 interfaces （新增批注16条） 117 abstract classes and methods 117 interfaces 120 “multiple inheritance” in java 123 extending an interface with inheritance 125 name collisions when combining interfaces 127 fields in interfaces 127 initializing fields in interfaces 128 interfaces and factories 129 summary 130 inner classes （新增批注32条） 131 creating inner classes 131 the link to the outer class 133 using .this and .new 134 inner classes and upcasting 135 anonymous inner classes 137 factory method revisited 140 nested classes 142 classes inside interfaces 143 reaching outward from a multiply nested class 145 why inner classes? 145 closures & callbacks 148 inheriting from inner classes 150 can inner classes be overridden? 150 local inner classes 152 inner-class identifiers 153 summary 154 error handling with excep- tions（新增批注52条） 155 basic exceptions 155 exception arguments 156 catching an exception 157 the try block 157 exception handlers . 157 creating your own exceptions 159 exceptions and logging 161 the exception specification 164 catching any exception 164 the stack trace 166 rethrowing an exception 167 exception chaining 169 standard java exceptions 172 special case: runtimeexception 172 performing cleanup with finally 174 what’s finally for? 175 using finally during return 177 pitfall: the lost exception 178 exception restrictions 180 constructors 182 exception matching 187 alternative approaches 188 passing exceptions to the console 189 summary 189 第3部分 数据存储 strings（新增批注53条） 191 immutable strings 191 overloading ‘+’ vs. stringbuilder 192 unintended recursion 195 operations on strings 196 formatting output 199 printf() 199 system.out.format() 199 the formatter class 200 format specifiers 200 formatter conversions 202 string.format() 204 regular expressions 205 basics 206 creating regular expressions 208 quantifiers 210 pattern and matcher 211 split() 218 replace operations 218 reset() 220 regular expressions and java i/o 221 scanning input 222 scanner delimiters 224 scanning with regular expressions 225 stringtokenizer 225 summary 226 arrays（新增批注36条） 227 why arrays are special 227 arrays are first-class objects 228 returning an array 231 multidimensional arrays 232 arrays and generics 235 creating test data 237 arrays.fill() 238 data generators 239 arrays utilities 243 copying an array 243 comparing arrays 244 array element comparisons 245 sorting an array 248 searching a sorted array 249 summary 251 holding your objects （新增批注35条） 253 generics and type-safe containers 254 basic concepts 256 adding groups of elements 258 printing containers 259 list 261 iterator 263 listiterator 266 linkedlist 267 stack 268 set 270 map 273 queue 276 priorityqueue 277 collection vs. iterator 279 foreach and iterators 281 the adapter method idiom 283 summary 286 containers in depth （新增批注102条） 289 full container taxonomy 289 filling containers 290 a generator solution 291 map generators 292 collection functionality 294 optional operations 297 unsupported operations 298 list functionality 300 sets and storage order 302 sortedset 306 queues 307 priority queues 308 deques 309 understanding maps 310 performance 311 sortedmap 314 linkedhashmap 315 hashing and hash codes 316 understanding hashcode() 319 hashing for speed 321 overriding hashcode() 324 choosing an implementation 329 a performance test framework 330 choosing between lists 333 microbenchmarking dangers 338 choosing between sets 339 choosing between maps 341 utilities 344 sorting and searching lists 347 making a collection or map unmodifiable 349 synchronizing a collection or map 350 holding references 351 the weakhashmap 353 java 1.0/1.1 containers 355 vector & enumeration 355 hashtable 356 stack 356 bitset 357 summary 359 第4部分 核心功能 i/o（新增批注89条） 361 the file class 361 a directory lister 361 directory utilities 364 checking for and creating directories 369 input and output 370 types of inputstream 371 types of outputstream 372 adding attributes and useful interfaces 373 reading from an inputstream with filterinputstream 374 writing to an outputstream with filteroutputstream 375 readers & writers 376 sources and sinks of data 377 modifying stream behavior 377 unchanged classes 378 off by itself: randomaccessfile 379 typical uses of i/o streams 379 buffered input file 379 input from memory 380 formatted memory input 381 basic file output 382 storing and recovering data 383 reading and writing random-access files 385 piped streams 386 file reading & writing utilities 386 reading binary files 389 standard i/o 389 reading from standard input 389 changing system.out to a printwriter 390 redirecting standard i/o 391 process control 391 new i/o 393 converting data 396 fetching primitives 398 view buffers 399 data manipulation with buffers 403 buffer details 404 memory-mapped files 406 file locking 409 compression 411 simple compression with gzip 412 multifile storage with zip 413 java archives (jars) 415 object serialization 416 finding the class 419 controlling serialization 420 using persistence 427 xml 432 summary 434 concurrency （新增批注117条） 435 basic threading 435 defining tasks 435 the thread class 436 using executors 438 producing return values from tasks 440 sleeping 442 priority 443 yielding 444 daemon threads 445 coding variations 446 joining a thread 450 catching exceptions 451 sharing resources 454 resolving shared resource contention 454 atomicity and volatility 457 atomic classes 460 critical sections 462 synchronizing on other objects 462 thread local storage 463 terminating tasks 464 the ornamental garden 465 terminating when blocked 467 interruption 469 cooperation between tasks 475 wait() and notifyall() 475 notify() vs. notifyall() 479 producers and consumers 482 producer-consumers and queues 486 using pipes for i/o between tasks 491 deadlock 492 new library components 497 countdownlatch 497 cyclicbarrier 499 delayqueue 501 priorityblockingqueue 503 the greenhouse controller with scheduledexecutor 505 semaphore 508 exchanger 511 simulation 513 bank teller simulation 513 the restaurant simulation 517 distributing work 521 performance tuning 526 comparing mutex technologies 526 lock-free containers 532 readwritelocks 533 active objects 535 summary 537 第5部分 高级特性 type information （新增批注59条） 539 the need for rtti 539 the class object 541 class literals 545 generic class references 547 new cast syntax 549 checking before a cast 550 using class literals 555 a dynamic instanceof 557 counting recursively 558 registered factories 559 instanceof vs. class equivalence 562 reflection: runtime class information 563 a class method extractor 564 dynamic proxies 566 null objects 570 mock objects & stubs 575 interfaces and type information 576 summary 580 generics （新增批注126条） 583 comparison with c++ 584 simple generics 584 a tuple library 586 a stack class 588 randomlist 589 generic interfaces 590 generic methods 593 leveraging type argument inference 594 varargs and generic methods 596 a generic method to use with generators 596 a general-purpose generator 597 simplifying tuple use 598 a set utility 600 anonymous inner classes 603 building complex models 604 the mystery of erasure 606 the c++ approach 607 migration compatibility 609 the problem with erasure 611 the action at the boundaries 612 compensating for erasure 615 creating instances of types 616 arrays of generics 618 bounds 622 wildcards 625 how smart is the compiler? 628 contravariance 629 unbounded wildcards 632 capture conversion 636 issues 637 no primitives as type parameters 637 implementing parameterized interfaces 639 casting and warnings 640 overloading 641 base class hijacks an interface 642 self-bounded types 642 curiously-recurring generics 643 self-bounding 644 argument covariance 646 dynamic type safety 649 exceptions 650 mixins 651 mixins in c++ 651 mixing with interfaces 653 using the decorator pattern 654 mixins with dynamic proxies 655 latent typing 657 compensating for the lack of latent typing 660 reflection 661 applying a method to a sequence 662 when you don’t happen to have the right interface 664 simulating latent typing with adapters 665 using function objects as strategies 668 summary: is casting really so bad? 672 enumerated types （新增批注55条） 675 basic enum features 675 using static imports with enums 676 adding methods to an enum 677 overriding enum methods 678 enums in switch statements 678 the mystery of values() 679 implements, not inherits 681 random selection 682 using interfaces for organization 683 using enumset instead of flags 686 using enummap 688 constant-specific methods 689 chain of responsibility with enums 692 state machines with enums 695 multiple dispatching 700 dispatching with enums 702 using constant-specific methods 704 dispatching with enummaps 705 using a 2-d array 706 summary 707 annotations （新增批注51条） 709 basic syntax 710 defining annotations 710 meta-annotations 712 writing annotation processors 712 annotation elements 713 default value constraints 713 generating external files 714 annotations don’t support inheritance 717 implementing the processor 717 using apt to process annotations 719 using the visitor pattern with apt 723 annotation-based unit testing 726 using @unit with generics 733 no “suites” necessary 735 implementing @unit 735 removing test code 741 sum\",\n" +
                "pages: \"742\",\n" +
                "images: {\n" +
                "small: \"https://img3.doubanio.com/spic/s6618125.jpg\",\n" +
                "large: \"https://img3.doubanio.com/lpic/s6618125.jpg\",\n" +
                "medium: \"https://img3.doubanio.com/mpic/s6618125.jpg\"\n" +
                "},\n" +
                "alt: \"https://book.douban.com/subject/6523999/\",\n" +
                "id: \"6523999\",\n" +
                "publisher: \"电子工业出版社\",\n" +
                "isbn10: \"7121135213\",\n" +
                "isbn13: \"9787121135217\",\n" +
                "title: \"Java编程思想\",\n" +
                "url: \"https://api.douban.com/v2/book/6523999\",\n" +
                "alt_title: \"Thinking in Java (4th Edition)\",\n" +
                "author_intro: \"Bruce Echel是MindView公司的CEO。该公司向客户提供软件咨询和培训。他是《Thinking in C++》一书的作者，并与他人合著了该书的第2卷以及其他图书。20多年来，他已发表了150多篇论文，并在全世界参与教学讲座和研讨，他是C++标准委员会成员，拥有应用物理大学学位和计算机工程硕士学位。 刘中兵：Java研究室首席技术专家 ，应用数学专业，曾任职于清华同方、NEC等企业，长期深入Java／Java EE大型企业应用的架构、设计与开发工作，目前专注于手机无线互联网与网络通信领域的架构设计与研究工作。\",\n" +
                "summary: \"《java编程思想(第4版)(评注版)》作者拥有多年教学经验，对c、c++以及java语言都有独到、深入的见解，书中以通俗易懂且小而直接的示例阐释了一个个晦涩抽象的概念，是一本当之无愧的经典之作。本评注版讲解了java设计、语法和库的各个方面，包括java的运算符、控制逻辑、构造、回收、重用、接口、内部类、存储、异常、字符串、类型、泛型、数组、容器、i/o、注释、并发等内容。 对于国外技术图书，选择翻译版还是影印版，常常让人陷入两难的境地。本评注版力邀国内资深专家执笔，在英文原著基础上增加中文点评与注释，旨在融合二者之长，既保留经典的原创文字与味道，又以先行者的学研心得与实践感悟，对读者阅读与学习加以点拨、指明捷径。 经过评注的版本，更值得反复阅读与体会。希望这《java编程思想(第4版)(评注版)》能够帮助您跨越java的重重险阻，领略高处才有的壮美风光，做一个成功而快乐的java程序员。\",\n" +
                "series: {\n" +
                "id: \"12876\",\n" +
                "title: \"博文视点评注版\"\n" +
                "},\n" +
                "price: \"108.00元\"\n" +
                "},\n" +
                "{\n" +
                "rating: {\n" +
                "max: 10,\n" +
                "numRaters: 6,\n" +
                "average: \"0.0\",\n" +
                "min: 0\n" +
                "},\n" +
                "subtitle: \"\",\n" +
                "author: [\n" +
                "\"(美)里斯 编著.石永鑫 等译\"\n" +
                "],\n" +
                "pubdate: \"2002-04-01\",\n" +
                "tags: [\n" +
                "{\n" +
                "count: 9,\n" +
                "name: \"Java\",\n" +
                "title: \"Java\"\n" +
                "},\n" +
                "{\n" +
                "count: 7,\n" +
                "name: \"JDBC\",\n" +
                "title: \"JDBC\"\n" +
                "},\n" +
                "{\n" +
                "count: 3,\n" +
                "name: \"DB\",\n" +
                "title: \"DB\"\n" +
                "},\n" +
                "{\n" +
                "count: 2,\n" +
                "name: \"数据库\",\n" +
                "title: \"数据库\"\n" +
                "},\n" +
                "{\n" +
                "count: 1,\n" +
                "name: \"计算机科学\",\n" +
                "title: \"计算机科学\"\n" +
                "},\n" +
                "{\n" +
                "count: 1,\n" +
                "name: \"计算机\",\n" +
                "title: \"计算机\"\n" +
                "},\n" +
                "{\n" +
                "count: 1,\n" +
                "name: \"编程\",\n" +
                "title: \"编程\"\n" +
                "},\n" +
                "{\n" +
                "count: 1,\n" +
                "name: \"程序设计\",\n" +
                "title: \"程序设计\"\n" +
                "}\n" +
                "],\n" +
                "origin_title: \"\",\n" +
                "image: \"https://img1.doubanio.com/mpic/s3062697.jpg\",\n" +
                "binding: \"平装\",\n" +
                "translator: [\n" +
                "\"石永鑫\"\n" +
                "],\n" +
                "catalog: \" \",\n" +
                "pages: \"356\",\n" +
                "images: {\n" +
                "small: \"https://img1.doubanio.com/spic/s3062697.jpg\",\n" +
                "large: \"https://img1.doubanio.com/lpic/s3062697.jpg\",\n" +
                "medium: \"https://img1.doubanio.com/mpic/s3062697.jpg\"\n" +
                "},\n" +
                "alt: \"https://book.douban.com/subject/1240774/\",\n" +
                "id: \"1240774\",\n" +
                "publisher: \"中国电力出版社\",\n" +
                "isbn10: \"7508308565\",\n" +
                "isbn13: \"9787508308562\",\n" +
                "title: \"JDBC TM与Java TM数据库编程(第2版) (平装)\",\n" +
                "url: \"https://api.douban.com/v2/book/1240774\",\n" +
                "alt_title: \"\",\n" +
                "author_intro: \"\",\n" +
                "summary: \" \",\n" +
                "price: \"45.0\"\n" +
                "},\n" +
                "{\n" +
                "rating: {\n" +
                "max: 10,\n" +
                "numRaters: 0,\n" +
                "average: \"0.0\",\n" +
                "min: 0\n" +
                "},\n" +
                "subtitle: \"\",\n" +
                "author: [\n" +
                "\"孙卫琴\"\n" +
                "],\n" +
                "pubdate: \"2017-1\",\n" +
                "tags: [ ],\n" +
                "origin_title: \"\",\n" +
                "image: \"https://img1.doubanio.com/mpic/s29582279.jpg\",\n" +
                "binding: \"平装\",\n" +
                "translator: [ ],\n" +
                "catalog: \"第1章面向对象开发方法概述1 1.1结构化的软件开发方法简介3 1.2面向对象的软件开发方法简介6 1.2.1对象模型6 1.2.2UML：可视化建模语言7 1.2.3RationalRose：可视化建模工具7 1.3面向对象开发中的核心思想和概念8 1.3.1问题领域、对象、属性、状态、行为、方法、实现8 1.3.2类、类型10 1.3.3消息、服务12 1.3.4接口13 1.3.5封装、透明14 1.3.6抽象18 1.3.7继承、扩展、覆盖20 1.3.8组合21 1.3.9多态、动态绑定24 1.4UML语言简介26 1.4.1用例图27 1.4.2类框图28 1.4.3时序图29 1.4.4协作图30 1.4.5状态转换图30 1.4.6组件图31 1.4.7部署图32 1.5类之间的关系32 1.5.1关联（Association）33 1.5.2依赖（Dependency）34 1.5.3聚集（Aggregation）35 1.5.4泛化（Generalization）36 1.5.5实现（Realization）36 1.5.6区分依赖、关联和聚集关系36 1.6实现Panel系统39 1.6.1扩展Panel系统42 1.6.2用配置文件进一步提高Panel系统的可维护性43 1.6.3运行Panel系统45 1.7小结45 1.8思考题46 第2章第一个Java应用47 2.1创建Java源文件47 2.1.1Java源文件结构49 2.1.2包声明语句49 2.1.3包引入语句51 2.1.4方法的声明53 2.1.5程序入口main（）方法的声明54 2.1.6给main（）方法传递参数55 2.1.7注释语句55 2.1.8关键字56 2.1.9标识符56 2.1.10编程规范57 2.2用JDK管理Java应用57 2.2.1JDK简介以及安装方法58 2.2.2编译Java源文件60 2.2.3运行Java程序62 2.2.4给Java应用打包65 2.3使用和创建JavaDoc文档66 2.3.1JavaDoc标记68 2.3.2javadoc命令的用法73 2.4Java虚拟机运行Java程序的基本原理75 2.5小结77 2.6思考题78 第3章数据类型和变量81 3.1基本数据类型82 3.1.1boolean类型82 3.1.2byte、short、int和long类型83 3.1.3char类型与字符编码85 3.1.4float和double类型87 3.2引用类型91 3.2.1基本类型与引用类型的区别92 3.2.2用new关键字创建对象94 3.3变量的作用域95 3.3.1实例变量和静态变量的生命周期97 3.3.2局部变量的生命周期100 3.3.3成员变量和局部变量同名101 3.3.4将局部变量的作用域最小化102 3.4对象的默认引用：this103 3.5参数传递105 3.6变量的初始化以及默认值107 3.6.1成员变量的初始化107 3.6.2局部变量的初始化108 3.7直接数109 3.7.1直接数的类型110 3.7.2直接数的赋值111 3.8小结112 3.9思考题113 第4章操作符115 4.1操作符简介115 4.2整型操作符116 4.2.1一元整型操作符117 4.2.2二元整型操作符118 4.3浮点型操作符123 4.4比较操作符和逻辑操作符124 4.5特殊操作符“？：”127 4.6字符串连接操作符“+”127 4.7操作符“==”与对象的equals（）方法129 4.7.1操作符“==”129 4.7.2对象的equals（）方法130 4.8instanceof操作符133 4.9变量的赋值和类型转换135 4.9.1基本数据类型转换136 4.9.2引用类型的类型转换139 4.10小结139 4.11思考题142 第5章流程控制145 5.1分支语句146 5.1.1ifelse语句146 5.1.2switch语句150 5.2循环语句154 5.2.1while语句154 5.2.2dowhile语句156 5.2.3for语句158 5.2.4foreach语句161 5.2.5多重循环162 5.3流程跳转语句162 5.4综合例子：八皇后问题165 5.5小结168 5.6思考题169 第6章继承173 6.1继承的基本语法173 6.2方法重载（Overload）175 6.3方法覆盖（Override）177 6.4方法覆盖与方法重载的异同183 6.5super关键字183 6.6多态185 6.7继承的利弊和使用原则189 6.7.1继承树的层次不可太多190 6.7.2继承树的上层为抽象层190 6.7.3继承关系最大的弱点：打破封装191 6.7.4精心设计专门用于被继承的类193 6.7.5区分对象的属性与继承195 6.8比较组合与继承197 6.8.1组合关系的分解过程对应继承关系的抽象过程197 6.8.2组合关系的组合过程对应继承关系的扩展过程200 6.9小结203 6.10思考题204 第7章Java语言中的修饰符209 7.1访问控制修饰符210 7.2abstract修饰符212 7.3final修饰符214 7.3.1final类215 7.3.2final方法215 7.3.3final变量216 7.4static修饰符220 7.4.1static变量220 7.4.2static方法223 7.4.3static代码块226 7.4.4用static进行静态导入228 7.5小结228 7.6思考题230 第8章接口233 8.1接口的概念和基本特征234 8.2比较抽象类与接口237 8.3与接口相关的设计模式241 8.3.1定制服务模式241 8.3.2适配器模式245 8.3.3默认适配器模式250 8.3.4代理模式251 8.3.5标识类型模式256 8.3.6常量接口模式257 8.4小结258 8.5思考题259 第9章异常处理261 9.1Java异常处理机制概述262 9.1.1Java异常处理机制的优点262 9.1.2Java虚拟机的方法调用栈264 9.1.3异常处理对性能的影响267 9.2运用Java异常处理机制267 9.2.1try—catch语句：捕获异常267 9.2.2finally语句：任何情况下必须执行的代码268 9.2.3throws子句：声明可能会出现的异常270 9.2.4throw语句：抛出异常271 9.2.5异常处理语句的语法规则271 9.2.6异常流程的运行过程274 9.2.7跟踪丢失的异常278 9.3Java异常类280 9.3.1运行时异常282 9.3.2受检查异常（CheckedException）282 9.3.3区分运行时异常和受检查异常283 9.4用户定义异常285 9.4.1异常转译和异常链285 9.4.2处理多样化异常288 9.5异常处理原则289 9.5.1异常只能用于非正常情况290 9.5.2为异常提供说明文档290 9.5.3尽可能地避免异常291 9.5.4保持异常的原子性292 9.5.5避免过于庞大的try代码块294 9.5.6在catch子句中指定具体的异常类型294 9.5.7不要在catch代码块中忽略被捕获的异常294 9.6记录日志295 9.6.1创建Logger对象及设置日志级别296 9.6.2生成日志297 9.6.3把日志输出到文件297 9.6.4设置日志的输出格式298 9.7使用断言299 9.8小结300 9.9思考题301 第10章类的生命周期305 10.1Java虚拟机及程序的生命周期305 10.2类的加载、连接和初始化305 10.2.1类的加载306 10.2.2类的验证307 10.2.3类的准备307 10.2.4类的解析308 10.2.5类的初始化308 10.2.6类的初始化的时机310 10.3类加载器313 10.3.1类加载的父亲委托机制315 10.3.2创建用户自定义的类加载器317 10.3.3URLClassLoader类323 10.4类的卸载324 10.5小结325 10.6思考题326 第11章对象的生命周期327 11.1创建对象的方式327 11.2构造方法330 11.2.1重载构造方法331 11.2.2默认构造方法332 11.2.3子类调用父类的构造方法333 11.2.4构造方法的作用域337 11.2.5构造方法的访问级别337 11.3静态工厂方法338 11.3.1单例类340 11.3.2枚举类342 11.3.3不可变（immutable）类与可变类344 11.3.4具有实例缓存的不可变类348 11.3.5松耦合的系统接口350 11.4垃圾回收351 11.4.1对象的可触及性352 11.4.2垃圾回收的时间354 11.4.3对象的finalize（）方法简介354 11.4.4对象的finalize（）方法的特点355 11.4.5比较finalize（）方法和finally代码块357 11.5清除过期的对象引用358 11.6对象的强、软、弱和虚引用360 11.7小结366 11.8思考题367 第12章内部类371 12.1内部类的基本语法371 12.1.1实例内部类373 12.1.2静态内部类376 12.1.3局部内部类377 12.2内部类的继承379 12.3子类与父类中的内部类同名380 12.4匿名类381 12.5内部接口以及接口中的内部类384 12.6内部类的用途385 12.6.1封装类型385 12.6.2直接访问外部类的成员385 12.6.3回调386 12.7内部类的类文件388 12.8小结389 12.9思考题389 第13章多线程393 13.1Java线程的运行机制393 13.2线程的创建和启动395 13.2.1扩展java.lang.Thread类395 13.2.2实现Runnable接口400 13.3线程的状态转换402 13.3.1新建状态402 13.3.2就绪状态402 13.3.3运行状态402 13.3.4阻塞状态403 13.3.5死亡状态404 13.4线程调度405 13.4.1调整各个线程的优先级406 13.4.2线程睡眠：Thread.sleep（）方法408 13.4.3线程让步：Thead.yield（）方法409 13.4.4等待其他线程结束：join（）410 13.5获得当前线程对象的引用411 13.6后台线程412 13.7定时器413 13.8线程的同步415 13.8.1同步代码块418 13.8.2线程同步的特征422 13.8.3同步与并发425 13.8.4线程安全的类426 13.8.5释放对象的锁427 13.8.6死锁429 13.9线程通信430 13.10中断阻塞435 13.11线程控制436 13.11.1被废弃的suspend（）和resume（）方法437 13.11.2被废弃的stop（）方法438 13.11.3以编程的方式控制线程438 13.12线程组440 13.13处理线程未捕获的异常441 13.14ThreadLocal类443 13.15concurrent并发包445 13.15.1用于线程同步的Lock外部锁446 13.15.2用于线程通信的Condition条件接口447 13.15.3支持异步计算的Callable接口和Future接口450 13.15.4通过线程池来高效管理多个线程452 13.15.5BlockingQueue阻塞队列454 13.16小结457 13.17思考题458 第14章数组461 14.1数组变量的声明461 14.2创建数组对象462 14.3访问数组的元素和长度463 14.4数组的初始化465 14.5多维数组以及不规则数组465 14.6调用数组对象的方法467 14.7把数组作为方法参数或返回值467 14.8数组排序470 14.9数组的二分查找算法471 14.10哈希表472 14.11数组实用类：Arrays477 14.12用符号“…”声明数目可变参数480 14.13小结481 14.14思考题481 第15章Java集合485 15.1Collection和Iterator接口486 15.2集合中直接加入基本类型数据489 15.3Set（集）490 15.3.1Set的一般用法490 15.3.2HashSet类491 15.3.3TreeSet类493 15.4List（列表）497 15.4.1访问列表的元素498 15.4.2为列表排序498 15.4.3ListIterator接口499 15.4.4获得固定长度的List对象500 15.4.5比较Java数组和各种List的性能500 15.5Queue（队列）503 15.5.1Deque（双向队列）504 15.5.2PriorityQueue（优先级队列）505 15.6Map（映射）505 15.7HashSet和HashMap的负载因子507 15.8集合实用类：Collections508 15.9线程安全的集合510 15.10集合与数组的互换511 15.11集合的批量操作512 15.12历史集合类513 15.13枚举类型517 15.13.1枚举类型的构造方法519 15.13.2EnumSet类和EnumMap类520 15.14小结521 15.15思考题521 第16章泛型523 16.1Java集合的泛型523 16.2定义泛型类和泛型接口524 16.3用extends关键字限定类型参数526 16.4定义泛型数组527 16.5定义泛型方法528 16.6使用“？”通配符529 16.7使用泛型的注意事项530 16.8小结531 16.9思考题531 第17章Lambda表达式533 17.1Lambda表达式的基本用法533 17.2用Lambda表达式代替内部类534 17.3Lambda表达式和集合的forEach（）方法535 17.4用Lambda表达式对集合进行排序536 17.5Lambda表达式与StreamAPI联合使用537 17.6Lambda表达式可操纵的变量作用域539 17.7Lambda表达式中的方法引用540 17.8函数式接口（FunctionalInterface）541 17.9总结Java语法糖541 17.10小结542 17.11思考题542 第18章输入与输出（I／O）545 18.1输入流和输出流概述546 18.2输入流547 18.2.1字节数组输入流：ByteArrayInputStream类548 18.2.2文件输入流：FileInputStream类549 18.2.3管道输入流：PipedInputStream551 18.2.4顺序输入流：SequenceInputStream类552 18.3过滤输入流：FilterInputStream552 18.3.1装饰器设计模式553 18.3.2过滤输入流的种类554 18.3.3DataInputStream类555 18.3.4BufferedInputStream类556 18.3.5PushbackInputStream类557 18.4输出流557 18.4.1字节数组输出流：ByteArrayOutputStream类557 18.4.2文件输出流：FileOutputStream558 18.5过滤输出流：FilterOutputStream559 18.5.1DataOutputStream559 18.5.2BufferedOutputStream559 18.5.3PrintStream类561 18.6Reader／Writer概述563 18.7Reader类565 18.7.1字符数组输入流：CharArrayReader类566 18.7.2字符串输入流：StringReader类566 18.7.3InputStreamReader类567 18.7.4FileReader类568 18.7.5BufferedReader类568 18.8Writer类568 18.8.1字符数组输出流：CharArrayWriter类569 18.8.2OutputStreamWriter类570 18.8.3FileWriter类572 18.8.4BufferedWriter类573 18.8.5PrintWriter类573 18.9标准I／O574 18.9.1重新包装标准输入和输出574 18.9.2标准I／O重定向575 18.10随机访问文件类：RandomAccessFile576 18.11新I／O类库577 18.11.1缓冲器Buffer概述578 18.11.2通道Channel概述579 18.11.3字符编码Charset类概述581 18.11.4用FileChannel读写文件581 18.11.5控制缓冲区582 18.11.6字符编码转换583 18.11.7缓冲区视图584 18.11.8文件映射缓冲区：MappedByteBuffer586 18.11.9文件加锁587 18.12对象的序列化与反序列化589 18.13自动释放资源595 18.14用File类来查看、创建和删除文件或目录596 18.15用java.nio.file类库来操作文件系统599 18.15.1复制、移动文件以及遍历、过滤目录树600 18.15.2查看ZIP压缩文件601 18.16小结602 18.17思考题603 第19章图形用户界面605 19.1AWT组件和Swing组件605 19.2创建图形用户界面的基本步骤608 19.3布局管理器610 19.3.1FlowLayout（流式布局管理器）611 19.3.2BorderLayout（边界布局管理器）613 19.3.3GridLayout（网格布局管理器）616 19.3.4CardLayout（卡片布局管理器）619 19.3.5GridBagLayout（网格包布局管理器）620 19.4事件处理626 19.4.1事件处理的软件实现626 19.4.2事件源﹑事件和监听器的类层次和关系632 19.5AWT绘图637 19.5.1Graphics类639 19.5.2Graphics2D类644 19.6AWT线程（事件分派线程）647 19.7小结649 19.8思考题650 第20章常用Swing组件653 20.1边框（Border）653 20.2按钮组件（AbstractButton）及子类654 20.3文本框（JTextField）657 20.4文本区域（JTextArea）与滚动面板（JScrollPane）660 20.5复选框（JCheckBox）与单选按钮（JRadioButton）661 20.6下拉列表（JComboBox）664 20.7列表框（JList）665 20.8页签面板（JTabbedPane）667 20.9菜单（JMenu）669 20.10对话框（JDialog）674 20.11文件对话框（JFileChoose）676 20.12消息框679 20.13制作动画681 20.14播放音频文件683 20.15BoxLayout布局管理器686 20.16设置Swing界面的外观和感觉689 20.17小结691 20.18思考题692 第21章Java常用类693 21.1Object类693 21.2String类和StringBuffer类694 21.2.1String类694 21.2.2“hello”与newString（“hello”）的区别697 21.2.3StringBuffer类698 21.2.4比较String类与StringBuffer类699 21.2.5正则表达式701 21.2.6格式化字符串703 21.3包装类707 21.3.1包装类的构造方法707 21.3.2包装类的常用方法708 21.3.3包装类的自动装箱和拆箱709 21.4Math类710 21.5Random类712 21.6传统的处理日期／时间的类712 21.6.1Date类713 21.6.2DateFormat类713 21.6.3Calendar类715 21.7新的处理日期／时间的类716 21.7.1LocalDate类717 21.7.2LocalTime类718 21.7.3LocalDateTime类718 21.8BigInteger类719 21.9BigDecimal类720 21.10用Optional类避免空指针异常722 21.11小结724 21.12思考题725 第22章Annotation注解727 22.1自定义Annotation注解类型727 22.2在类的源代码中引用注解类型730 22.3在程序中运用反射机制读取类的注解信息732 22.4基本内置注解735 22.5小结736 22.6思考题736\",\n" +
                "pages: \"736\",\n" +
                "images: {\n" +
                "small: \"https://img1.doubanio.com/spic/s29582279.jpg\",\n" +
                "large: \"https://img1.doubanio.com/lpic/s29582279.jpg\",\n" +
                "medium: \"https://img1.doubanio.com/mpic/s29582279.jpg\"\n" +
                "},\n" +
                "alt: \"https://book.douban.com/subject/27174154/\",\n" +
                "id: \"27174154\",\n" +
                "publisher: \"电子工业出版社\",\n" +
                "isbn10: \"7121303140\",\n" +
                "isbn13: \"9787121303142\",\n" +
                "title: \"Java面向对象编程 (第2版)\",\n" +
                "url: \"https://api.douban.com/v2/book/27174154\",\n" +
                "alt_title: \"\",\n" +
                "author_intro: \"孙卫琴，知名IT作家和Java专家，毕业于上海交通大学，随后主要从事基于Java的软件开发工作，在此其间还从事Java方面的培训和咨询工作。2002年开始Java技术领域的创作，文风清晰严谨、深入浅出，深受读者欢迎，其多部作品的销量在同类书籍中一度位居全国榜首。代表著作有： 《Java面向对象编程》 《Java网络编程精解》 《Java逍遥游记》 《Hibernate逍遥游记》 《精通Hibernate: Java对象持久化技术详解》 《精通Struts: 基于MVC的Java Web设计与开发》 《Tomcat与Java Web开发技术详解》 《Java 2认证考试指南与试题解析》\",\n" +
                "summary: \"本书采用由浅入深、与实际应用紧密结合的方式，利用大量经典实用的实例，详细讲解Java面向对象的编程思想、编程语法和设计模式，介绍常见Java类库的用法，总结优化Java编程的各种宝贵经验，深入阐述Java虚拟机执行Java程序的原理。本书的实例都基于最新的JDK8版本。本书的特色是以6条主线贯穿书：面向对象编程思想、Java语言的语法、Java虚拟机执行Java程序的原理、在实际项目中的运用、设计模式和性能优化技巧。另外，本书还贯穿了Oracle公司的OCJP（Oracle Certified Java Programmer）认证的考试要点。\",\n" +
                "price: \"89.00\"\n" +
                "},\n" +
                "{\n" +
                "rating: {\n" +
                "max: 10,\n" +
                "numRaters: 0,\n" +
                "average: \"0.0\",\n" +
                "min: 0\n" +
                "},\n" +
                "subtitle: \"\",\n" +
                "author: [\n" +
                "\"金勇华\"\n" +
                "],\n" +
                "pubdate: \"2001-4\",\n" +
                "tags: [ ],\n" +
                "origin_title: \"\",\n" +
                "image: \"https://img3.doubanio.com/mpic/s9996702.jpg\",\n" +
                "binding: \"平装\",\n" +
                "translator: [ ],\n" +
                "catalog: \"\",\n" +
                "pages: \"493\",\n" +
                "images: {\n" +
                "small: \"https://img3.doubanio.com/spic/s9996702.jpg\",\n" +
                "large: \"https://img3.doubanio.com/lpic/s9996702.jpg\",\n" +
                "medium: \"https://img3.doubanio.com/mpic/s9996702.jpg\"\n" +
                "},\n" +
                "alt: \"https://book.douban.com/subject/1086438/\",\n" +
                "id: \"1086438\",\n" +
                "publisher: \"第1版 (2001年4月1日)\",\n" +
                "isbn10: \"7115092079\",\n" +
                "isbn13: \"9787115092076\",\n" +
                "title: \"Java 网络高级编程 (平装)\",\n" +
                "url: \"https://api.douban.com/v2/book/1086438\",\n" +
                "alt_title: \"\",\n" +
                "author_intro: \"\",\n" +
                "summary: \"《Java 网络高级编程》： 快速帮助读者理解Java语言的精髓，掌握利用Java语言的各种编程技巧。 突出Java网络程序的开发，包括客户机/服务器、浏览器/服务器结构程序的编写。 突出Java小应用程序Applet的开发，使用户的WWW页面更加丰富多彩。 详细讲述Java服务器端小应用程序的开发过程。\",\n" +
                "price: \"54.0\"\n" +
                "},\n" +
                "{\n" +
                "rating: {\n" +
                "max: 10,\n" +
                "numRaters: 0,\n" +
                "average: \"0.0\",\n" +
                "min: 0\n" +
                "},\n" +
                "subtitle: \"第4版\",\n" +
                "author: [\n" +
                "\" \"\n" +
                "],\n" +
                "pubdate: \"2005-8\",\n" +
                "tags: [\n" +
                "{\n" +
                "count: 1,\n" +
                "name: \"编程\",\n" +
                "title: \"编程\"\n" +
                "},\n" +
                "{\n" +
                "count: 1,\n" +
                "name: \"java\",\n" +
                "title: \"java\"\n" +
                "},\n" +
                "{\n" +
                "count: 1,\n" +
                "name: \"CS\",\n" +
                "title: \"CS\"\n" +
                "}\n" +
                "],\n" +
                "origin_title: \"\",\n" +
                "image: \"https://img3.doubanio.com/mpic/s23741076.jpg\",\n" +
                "binding: \"\",\n" +
                "translator: [ ],\n" +
                "catalog: \" \",\n" +
                "pages: \"801\",\n" +
                "images: {\n" +
                "small: \"https://img3.doubanio.com/spic/s23741076.jpg\",\n" +
                "large: \"https://img3.doubanio.com/lpic/s23741076.jpg\",\n" +
                "medium: \"https://img3.doubanio.com/mpic/s23741076.jpg\"\n" +
                "},\n" +
                "alt: \"https://book.douban.com/subject/1434842/\",\n" +
                "id: \"1434842\",\n" +
                "publisher: \"清华大学出版社\",\n" +
                "isbn10: \"730210932X\",\n" +
                "isbn13: \"9787302109327\",\n" +
                "title: \"Java编程原理与实践\",\n" +
                "url: \"https://api.douban.com/v2/book/1434842\",\n" +
                "alt_title: \"\",\n" +
                "author_intro: \"\",\n" +
                "summary: \" \",\n" +
                "price: \"99.00元\"\n" +
                "},\n" +
                "{\n" +
                "rating: {\n" +
                "max: 10,\n" +
                "numRaters: 10,\n" +
                "average: \"7.5\",\n" +
                "min: 0\n" +
                "},\n" +
                "subtitle: \"第6版\",\n" +
                "author: [\n" +
                "\"Herbert Schildt\"\n" +
                "],\n" +
                "pubdate: \"2015-1\",\n" +
                "tags: [\n" +
                "{\n" +
                "count: 26,\n" +
                "name: \"Java\",\n" +
                "title: \"Java\"\n" +
                "},\n" +
                "{\n" +
                "count: 13,\n" +
                "name: \"JDK8\",\n" +
                "title: \"JDK8\"\n" +
                "},\n" +
                "{\n" +
                "count: 10,\n" +
                "name: \"编程\",\n" +
                "title: \"编程\"\n" +
                "},\n" +
                "{\n" +
                "count: 6,\n" +
                "name: \"java入门\",\n" +
                "title: \"java入门\"\n" +
                "},\n" +
                "{\n" +
                "count: 2,\n" +
                "name: \"软件工程\",\n" +
                "title: \"软件工程\"\n" +
                "},\n" +
                "{\n" +
                "count: 2,\n" +
                "name: \"计算机\",\n" +
                "title: \"计算机\"\n" +
                "},\n" +
                "{\n" +
                "count: 2,\n" +
                "name: \"编程语言\",\n" +
                "title: \"编程语言\"\n" +
                "},\n" +
                "{\n" +
                "count: 2,\n" +
                "name: \"技术\",\n" +
                "title: \"技术\"\n" +
                "}\n" +
                "],\n" +
                "origin_title: \"Java: A Beginner's Guide, 6th Ed.\",\n" +
                "image: \"https://img3.doubanio.com/mpic/s28002830.jpg\",\n" +
                "binding: \"\",\n" +
                "translator: [\n" +
                "\"王楚燕\",\n" +
                "\"鱼静\"\n" +
                "],\n" +
                "catalog: \"目录 第1章 Java基础 1 1.1 Java的起源 2 1.1.1 Java与C和C++的关系 3 1.1.2 Java与C#的关系 3 1.2 Java对Internet的贡献 4 1.2.1 Java applet 4 1.2.2 安全性 4 1.2.3 可移植性 5 1.3 Java的魔法：字节码 5 1.4 Java的主要术语 6 1.5 面向对象程序设计 7 1.5.1 封装 8 1.5.2 多态性 8 1.5.3 继承 9 1.6 获得Java开发工具包 9 1.7 第一个简单的程序 10 1.7.1 输入程序 10 1.7.2 编译程序 11 1.7.3 逐行分析第一个程序 11 1.8 处理语法错误 13 1.9 第二个简单程序 14 1.10 另一种数据类型 16 1.11 两条控制语句 18 1.11.1 if语句 18 1.11.2 for循环语句 20 1.12 创建代码块 21 1.13 分号和定位 22 1.14 缩进原则 23 1.15 Java关键字 25 1.16 Java标识符 25 1.17 Java类库 26 第2章 数据类型与运算符 29 2.1 数据类型为什么重要 30 2.2 Java的基本类型 30 2.2.1 整数类型 31 2.2.2 浮点型 32 2.2.3 字符型 33 2.2.4 布尔类型 34 2.3 字面值 36 2.3.1 十六进制、八进制和二进制字面值 36 2.3.2 字符转义序列 37 2.3.3 字符串字面值 37 2.4 变量详解 38 2.4.1 初始化变量 39 2.4.2 动态初始化 39 2.5 变量的作用域和生命期 39 2.6 运算符 42 2.7 算术运算符 42 2.8 关系运算符和逻辑运算符 44 2.9 短路逻辑运算符 46 2.10 赋值运算符 47 2.11 速记赋值 47 2.12 赋值中的类型转换 48 2.13 不兼容类型的强制转换 50 2.14 运算符优先级 51 2.15 表达式 53 2.15.1 表达式中的类型转换 53 2.15.2 间距和圆括号 55 第3章 程序控制语句 57 3.1 从键盘输入字符 58 3.2 if语句 59 3.2.1 嵌套if语句 60 3.2.2 if-else-if阶梯状结构 61 3.3 switch语句 62 3.4 for循环 68 3.4.1 for循环的一些变体 69 3.4.2 缺失部分要素的for循环 70 3.4.3 无限循环 71 3.4.4 没有循环体的循环 72 3.4.5 在for循环内部声明循环控制变量 72 3.4.6 增强型for循环 73 3.5 while循环 73 3.6 do-while循环 75 3.7 使用break语句退出循环 79 3.8 将break语句作为一种goto语句使用 81 3.9 使用continue语句 85 3.10 嵌套循环 89 第4章 类、对象和方法 93 4.1 类的基础知识 94 4.1.1 类的基本形式 94 4.1.2 定义类 95 4.2 如何创建对象 98 4.3 引用变量和赋值 98 4.4 方法 99 4.5 从方法返回值 101 4.6 返回值 102 4.7 使用形参 104 4.8 构造函数 112 4.9 带形参的构造函数 113 4.10 深入介绍new运算符 115 4.11 垃圾回收 115 4.12 this关键字 119 第5章 其他数据类型与运算符 123 5.1 数组 124 5.2 多维数组 129 5.3 不规则数组 130 5.3.1 三维或更多维的数组 131 5.3.2 初始化多维数组 131 5.4 另一种声明数组的语法 132 5.5 数组引用赋值 133 5.6 使用length成员 134 5.7 for-each形式的循环 139 5.7.1 迭代多维数组 142 5.7.2 应用增强型for循环 143 5.8 字符串 144 5.8.1 构造字符串 144 5.8.2 操作字符串 145 5.8.3 字符串数组 147 5.8.4 字符串是不可变的 148 5.8.5 使用String控制switch语句 149 5.9 使用命令行实参 150 5.10 位运算符 151 5.10.1 位运算符的与、或、异 或和非 151 5.10.2 移位运算符 155 5.10.3 位运算符的赋值速记符 157 5.11 ?运算符 160 第6章 方法和类详解 163 6.1 控制对类成员的访问 164 6.2 向方法传递对象 169 6.3 返回对象 173 6.4 方法重载 174 6.5 重载构造函数 179 6.6 递归 184 6.7 理解static关键字 186 6.8 嵌套类和内部类 192 6.9 varargs(可变长度实参) 195 6.9.1 varargs基础 195 6.9.2 重载varargs方法 198 6.9.3 varargs和歧义 199 第7章 继承 203 7.1 继承的基础知识 204 7.2 成员访问与继承 207 7.3 构造函数和继承 209 7.4 使用super调用超类构造函数 211 7.5 使用super访问超类成员 215 7.6 创建多级层次结构 218 7.7 何时调用构造函数 221 7.8 超类引用和子类对象 222 7.9 方法重写 227 7.10 重写的方法支持多态性 229 7.11 为何使用重写方法 231 7.12 使用抽象类 235 7.13 使用final 239 7.13.1 使用final防止重写 239 7.13.2 使用final防止继承 239 7.13.3 对数据成员使用final 240 7.14 Object类 241 第8章 包和接口 243 8.1 包 244 8.1.1 定义包 244 8.1.2 寻找包和CLASSPATH 245 8.1.3 一个简短的包示例 245 8.2 包和成员访问 247 8.3 理解被保护的成员 249 8.4 导入包 251 8.5 Java的类库位于包中 252 8.6 接口 253 8.7 实现接口 254 8.8 使用接口引用 257 8.9 接口中的变量 264 8.10 接口能够被扩展 265 8.11 默认接口方法 266 8.11.1 默认方法的基础知识 266 8.11.2 默认方法的实际应用 268 8.11.3 多继承问题 269 8.12 在接口中使用静态方法 270 8.13 有关包和接口的最后思考 271 第9章 异常处理 273 9.1 异常的层次结构 274 9.2 异常处理基础 274 9.2.1 使用关键字try和catch 275 9.2.2 一个简单的异常示例 276 9.3 未捕获异常的结果 277 9.4 使用多个catch语句 280 9.5 捕获子类异常 281 9.6 try代码块可以嵌套 282 9.7 抛出异常 283 9.8 Throwable详解 285 9.9 使用finally 286 9.10 使用throws语句 288 9.11 新增的3种异常功能 289 9.12 Java的内置异常 291 9.13 创建异常子类 293 第10章 使用I/O 299 10.1 Java的I/O基于流 300 10.2 字节流和字符流 300 10.3 字节流类 301 10.4 字符流类 301 10.5 预定义流 302 10.6 使用字节流 302 10.6.1 读取控制台输入 303 10.6.2 写入控制台输出 304 10.7 使用字节流读写文件 305 10.7.1 从文件输入 305 10.7.2 写入文件 309 10.8 自动关闭文件 311 10.9 读写二进制数据 313 10.10 随机访问文件 317 10.11 使用Java字符流 319 10.11.1 使用字符流的控制台输入 320 10.11.2 使用字符流的控制台输出 323 10.12 使用字符流的文件I/O 324 10.12.1 使用FileWriter 324 10.12.2 使用FileReader 325 10.13 使用Java的类型封装器转换数值字符串 326 第11章 多线程程序设计 337 11.1 多线程的基础知识 338 11.2 Thread类和Runnable接口 339 11.3 创建一个线程 339 11.4 创建多个线程 346 11.5 确定线程何时结束 348 11.6 线程的优先级 351 11.7 同步 354 11.8 使用同步方法 354 11.9 同步语句 357 11.10 使用notify( )、wait( )和notifyAll( )的线程通信 360 11.11 线程的挂起、继续执行和停止 365 第12章 枚举、自动装箱、静态导入和注释 371 12.1 枚举 372 12.2 Java语言中的枚举是类类型 374 12.3 values( )和valueOf( )方法 374 12.4 构造函数、方法、实例变量和枚举 376 12.5 枚举继承enum 378 12.6 自动装箱 384 12.7 类型封装器 385 12.8 自动装箱的基础知识 386 12.9 自动装箱和方法 387 12.10 发生在表达式中的自动装箱/自动拆箱 388 12.11 静态导入 390 12.12 注解(元数据) 393 第13章 泛型 397 13.1 泛型的基础知识 398 13.2 一个简单的泛型示例 399 13.2.1 泛型只能用于引用类型 402 13.2.2 泛型类型是否相同基于其类型实参 402 13.2.3 带有两个类型形参的泛型类 402 13.2.4 泛型类的一般形式 404 13.3 约束类型 404 13.4 使用通配符实参 407 13.5 约束通配符 410 13.6 泛型方法 413 13.7 泛型构造函数 415 13.8 泛型接口 416 13.9 原类型和遗留代码 422 13.10 使用菱形运算符进行类型推断 425 13.11 擦除特性 426 13.12 歧义错误 426 13.13 一些泛型限制 427 13.13.1 类型形参不能实例化 427 13.13.2 对静态成员的限制 428 13.13.3 泛型数组限制 428 13.13.4 泛型异常限制 429 13.14 继续学习泛型 429 第14章 lambda表达式和方法引用 431 14.1 lambda表达式简介 432 14.1.1 lambda表达式的基础知识 432 14.1.2 函数式接口 433 14.1.3 几个lambda表达式示例 435 14.2 块lambda表达式 440 14.3 泛型函数式接口 441 14.4 lambda表达式和变量捕获 447 14.5 从 lambda表达式中抛出异常 448 14.6 方法引用 449 14.6.1 静态方法的方法引用 449 14.6.2 实例方法的方法引用 451 14.7 构造函数引用 455 14.8 预定义的函数式接口 457 第15章 applet、事件和其他主题 461 15.1 applet的基础知识 462 15.2 applet的组织和基本构件 465 15.3 applet架构 465 15.4 完整的applet框架 465 15.5 applet的初始化与终止 467 15.6 请求重绘 467 15.7 使用状态窗口 472 15.8 向applet传递形参 473 15.9 Applet类 474 15.10 事件处理 476 15.11 委派事件模型 476 15.12 事件 476 15.12.1 事件源 476 15.12.2 事件侦听器 477 15.12.3 事件类 477 15.12.4 事件侦听器接口 478 15.13 使用委派事件模型 479 15.13.1 处理鼠标事件和鼠标移动事件 479 15.13.2 一个简单的鼠标事件applet 480 15.14 其他Java关键字 482 15.14.1 transient和volatile修饰符 483 15.14.2 instanceof 483 15.14.3 strictfp 483 15.14.4 assert 483 15.14.5 native方法 484 第16章 Swing介绍 487 16.1 Swing的起源和设计原则 488 16.2 组件和容器 490 16.2.1 组件 490 16.2.2 容器 491 16.2.3 顶级容器窗格 491 16.3 布局管理器 491 16.4 第一个简单的Swing程序 492 16.5 使用JButton 497 16.6 使用JTextField 500 16.7 使用JCheckBox 504 16.8 使用JList 507 15.9 使用匿名内部类或lambda表达式来处理事件 515 16.10 创建Swing applet 517 第17章 JavaFX简介 521 17.1 JavaFX的基本概念 522 17.1.1 JavaFX包 522 17.1.2 Stage和Scene类 523 17.1.3 节点和场景图 523 17.1.4 布局 523 17.1.5 Application类和生命周期方法 523 17.1.6 启动JavaFX应用程序 524 17.2 JavaFX应用程序的骨架 524 17.3 编译和运行JavaFX程序 527 17.4 应用程序线程 527 17.5 使用简单的JavaFX控件Label 528 17.6 使用按钮和事件 530 17.6.1 事件基础 530 17.6.2 按钮控件简介 531 17.6.3 演示事件处理和按钮 531 17.7 其他3个JavaFX控件 534 17.7.1 CheckBox 534 17.7.2 ListView 538 17.7.3 TextField 543 17.8 效果和变换简介 546 17.8.1 效果 546 17.8.2 变换 548 17.8.3 演示效果和变换 549 17.9 进一步学习 552 附录A 自测题答案 555 附录B 使用Java的文档注释 599\",\n" +
                "pages: \"624\",\n" +
                "images: {\n" +
                "small: \"https://img3.doubanio.com/spic/s28002830.jpg\",\n" +
                "large: \"https://img3.doubanio.com/lpic/s28002830.jpg\",\n" +
                "medium: \"https://img3.doubanio.com/mpic/s28002830.jpg\"\n" +
                "},\n" +
                "alt: \"https://book.douban.com/subject/26320992/\",\n" +
                "id: \"26320992\",\n" +
                "publisher: \"清华大学出版社\",\n" +
                "isbn10: \"7302387389\",\n" +
                "isbn13: \"9787302387381\",\n" +
                "title: \"Java 8编程入门官方教程\",\n" +
                "url: \"https://api.douban.com/v2/book/26320992\",\n" +
                "alt_title: \"Java: A Beginner's Guide, 6th Ed.\",\n" +
                "author_intro: \"Herbert Schildt是Java语言的权威，他撰写的程序设计图书大约有30多本，在全世界销售了数百万册，并被翻译成了多种语言。虽然他对计算机的方方面面都很感兴趣，但是主要关注点是计算机语言，包括编译器、解释器和机器人控制语言。他的畅销书有Java: The Complete Reference、Herb Schildt’s Java Programming Cookbook和Swing: A Beginner’s Guide 。\",\n" +
                "summary: \"\",\n" +
                "price: \"69.00\"\n" +
                "}\n" +
                "]\n" +
                "}";

        JSONObject j = new JSONObject(s);
        List<BookInfo> bookInfos = DouBanUtils.JsonArryToBookeInfo(j);
        if (j.has("total")){
            bookSearch.setTotal(j.getInt("total"));
        }
        int bookLen = bookInfos.size();
        for(int i =0;i<bookLen;i++){
            addBookInfo(bookInfos.get(i));
        }
        return  bookInfos;
    }


}
