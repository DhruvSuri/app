package com.app.azazte.azazte.Utils;

import com.app.azazte.azazte.Adapters.NewsAdapter;
import com.app.azazte.azazte.Beans.NewsCard;
import com.app.azazte.azazte.Database.Connector;
import com.app.azazte.azazte.ListAdapter;
import com.app.azazte.azazte.Utils.Registry.Registry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by sprinklr on 13/04/16.
 */
public class Categories {
    public static final String INCOME_TAX = "INCOME_TAX";
    public static final String SERVICE_TAX_VAT = "SERVICE_TAX_VAT";
    public static final String EXCISE_CUSTOMS = "EXCISE_CUSTOMS";
    public static final String COMPANY_LAW = "COMPANY_LAW";
    public static final String ECONOMY_FINANCE = "ECONOMY_FINANCE";
    public static final String REGULATORY = "REGULATORY";
    public static final String OTHERS = "OTHERS";
    public static final String DEFAULT = "default";
    public static String category = "default";
    public static Integer intCategory;

    public static void filterNewsByCategory(NewsAdapter newsCardAdapter, Connector connector) {
        ArrayList<NewsCard> filteredNews = new ArrayList<>();
        ArrayList<NewsCard> news = connector.getAllNews();

        switch (category) {
            case INCOME_TAX:
                intCategory = 1;
                break;
            case SERVICE_TAX_VAT:
                intCategory = 2;
                break;
            case EXCISE_CUSTOMS:
                intCategory = 3;
                break;
            case COMPANY_LAW:
                intCategory = 4;
                break;
            case ECONOMY_FINANCE:
                intCategory = 5;
                break;
            case REGULATORY:
                intCategory = 6;
                break;
            case OTHERS:
                intCategory = 7;
                break;
            default:
                Collections.reverse(news);
                newsCardAdapter.clear();
                newsCardAdapter.addAll(news);
                return;
        }

        for (NewsCard newsCard : news) {
            int cardCategory;
            try {
                cardCategory = Integer.valueOf(newsCard.category);
            } catch (Exception e) {
                cardCategory = 0;
            }
            if (cardCategory == intCategory) {
                filteredNews.add(newsCard);
            }
        }
        Collections.reverse(filteredNews);
        newsCardAdapter.clear();
        newsCardAdapter.addAll(filteredNews);
    }


    public static void filterNewsByCategories(){

        final ArrayList<NewsCard> allNews = Connector.getInstance().getAllNews();
        if (allNews.size() == 0){
            return;
        }
        final Map<Integer,ListAdapter> registeredAdapters = Registry.getInstance().getRegisteredAdapters();
        Collections.reverse(allNews);
        List<NewsCard> newsCards = new ArrayList<>();
        newsCards.addAll(allNews);
        registeredAdapters.get(0).setNewsCardList(newsCards);
        Collections.reverse(allNews);
        for (Map.Entry<Integer, ListAdapter> entry : registeredAdapters.entrySet()) {
            int category = entry.getKey();
            if (category == 0){
                continue;
            }
            newsCards = new ArrayList<>();
            for (NewsCard newsCard : allNews) {

                int cardCategory;
                try {
                    cardCategory = Integer.valueOf(newsCard.category);
                } catch (Exception e) {
                    cardCategory = 0;
                }
                if (category == cardCategory){
                    newsCards.add(newsCard);
                }
            }
            final ListAdapter value = entry.getValue();
            Collections.reverse(newsCards);
            value.setNewsCardList(newsCards);
        }
    }
}
