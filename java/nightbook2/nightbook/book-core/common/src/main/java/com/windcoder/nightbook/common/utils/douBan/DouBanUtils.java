package com.windcoder.nightbook.common.utils.douBan;

import com.windcoder.nightbook.common.entity.Book;
import com.windcoder.nightbook.common.utils.https.HttpsUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * User: WindCoder
 * Date: 2017-10-17
 * Time: 23:16 下午
 */
public class DouBanUtils {
    public static final String ISBNURL = "https://api.douban.com/v2/book/isbn/";
    public static final String TAGURL = "https://api.douban.com/v2/book/search";





    public static List<Book> JsonArryToBookeInfo(JSONObject j){
        List<Book> bookInfos = new ArrayList<Book>();
        if(j.has("books")){
            JSONArray books = j.getJSONArray("books");
            int booksLwen = books.length();
            for (int i=0;i<booksLwen;i++){
                bookInfos.add(JsonToBookeInfo(books.getJSONObject(i)));
            }
        }
        return bookInfos;
    }
    public static Book JsonToBookeInfo(JSONObject j){
        if(j.has("title")) {
            Book bookInfo = new Book();
            bookInfo.setTitle(j.getString("title"));
            bookInfo.setOriginTitle(j.getString("origin_title"));
            bookInfo.setAuthor(JsonArryToString(j.getJSONArray("author")));
            bookInfo.setTranslator( JsonArryToString(j.getJSONArray("translator")));
            bookInfo.setHeadImage(j.getString("image"));
            bookInfo.setSummary(j.getString("summary"));
            bookInfo.setPrice(j.getString("price"));
            bookInfo.setIsbn(j.getString("isbn13"));
            bookInfo.setPublisher(j.getString("publisher"));
            bookInfo.setPubdate(j.getString("pubdate"));
            bookInfo.setPages(j.getString("pages"));
            bookInfo.setAverage(j.getJSONObject("rating").getString("average"));
            return bookInfo;
        }

        return null;
    }

    public static String JsonArryToString(JSONArray jsonArray){
        StringBuffer str = new StringBuffer();
        int len = jsonArray.length();
        int tmpLen = len-1;
        for (int i =0;i<len;i++){
            str.append(jsonArray.get(i));
            if (i!=tmpLen){
                str.append(" / ");
            }
        }
        return str.toString();
    }

    /**
     * 根据isbn查询
     * @param isbn
     * @return
     */
    public static Book findBookInfoFromDouBanByISBN(String isbn){
        StringBuffer url = new StringBuffer(ISBNURL);
        url.append(isbn);
        JSONObject resultData = HttpsUtil.httpRequest(url.toString(), "GET", "");
        if(resultData.has("data")) {
            String dataStr = resultData.getString("data");
            JSONObject result = new JSONObject(dataStr);
            Book bookInfo = DouBanUtils.JsonToBookeInfo(result);
            return bookInfo;
        }
        return null;
    }


    public static List<Book> findBookInfoFromDouBanByKeys(DouBanBookSearch bookSearch){
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
        if(resultData.has("data")) {
            String dataStr = resultData.getString("data");
            JSONObject result = new JSONObject(dataStr);
            List<Book> bookInfos = DouBanUtils.JsonArryToBookeInfo(result);
            if (result.has("total")){
                bookSearch.setTotal(result.getLong("total"));
            }
            return bookInfos;
        }
        return null;
    }







}
