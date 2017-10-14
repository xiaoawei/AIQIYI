package com.tsg.xutil.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.tsg.xutil.MainActivity;
import com.tsg.xutil.R;
import com.tsg.xutil.activity.WebPlayActivity;
import com.tsg.xutil.adapter.SearchAdapter;
import com.tsg.xutil.base.BaseFragment;
import com.tsg.xutil.bean.VideoInfo;
import com.tsg.xutil.constant.Constant;
import com.tsg.xutil.constant.RequestApi;
import com.tsg.xutil.util.L;
import com.tsg.xutil.util.T;
import com.tsg.xutil.util.net.BhResponseError;
import com.tsg.xutil.util.net.MyXUtil;
import com.tsg.xutil.util.net.UrlUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by xiaoAwei on 2017/9/25.
 */
@ContentView(R.layout.fragment_search)
public class SearchFragment extends BaseFragment {

    private MainActivity activity;
    @ViewInject(R.id.search_searchEt)
    private EditText searchEt;
    @ViewInject(R.id.search_searchBt)
    private Button searchBt;
    @ViewInject(R.id.search_ListView)
    private ListView searchListView;
    private SearchAdapter searchAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return x.view().inject(this, inflater, container);
    }

    @Override
    protected void initView(View view) {
        activity = (MainActivity) getActivity();
    }

    @Override
    protected void show() {
        super.show();
        activity.setActionBarTitle("搜索");
    }

    @Event(R.id.search_searchBt)
    private void searchResult(View view) {
        String searchStr = searchEt.getText().toString();

        netSearchResult(searchStr);
    }

    private void netSearchResult(String searchStr) {
        MyXUtil myXUtil = new MyXUtil(activity) {
            @Override
            public void loadSuccess(Object responseInfo) {
                L.e(responseInfo);
                try {
                    //从一个URL加载一个Document对象。
                    Document doc = Jsoup.parse((String) responseInfo);
                    //选择“美食天下”所在节点
                    ArrayList videoBeans = new ArrayList();
                    Elements elements = doc.getElementsByClass("list_item");
                    for (Element e : elements) {
                        ArrayList jiList = new ArrayList();
                        VideoInfo videoInfo = new VideoInfo();
                        videoInfo.setImgUrl(e.select("a img").attr("src"));
                        videoInfo.setTitle(e.select("a img").attr("title"));
                        videoInfo.setProduce("\t" + e.getElementsByClass("result_info_txt").text());
                        Elements e1 = e.getElementsByAttributeValue("data-tvlist-elem", "list").select("li");
                        if (e1.size() > 0) {
                            for (Element jiE : e1) {
                                jiList.add(jiE.select("a").attr("href"));
                            }
                        } else {
                            jiList.add(e.select("a").attr("href"));
                        }
                        videoInfo.setJiList(jiList);
                        videoBeans.add(videoInfo);
                    }
                    searchAdapter = new SearchAdapter(activity, videoBeans);
                    searchListView.setAdapter(searchAdapter);
                    searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(activity, WebPlayActivity.class);
                            intent.putExtra(Constant.VIDEOBEAN, (Serializable) searchAdapter.getData().get(position));
                            startActivity(intent);
                        }
                    });
                } catch (Exception e) {
                    L.e("mytag", e.toString());
                }
            }

            @Override
            public void loadError(BhResponseError error) {
            }

            @Override
            public void finish() {

            }
        };
        myXUtil.get(RequestApi.getSearchResultForAi + UrlUtil.getURLEncoderString(searchStr),
                null, false, null, true, "正在获取搜索结果，请稍后...");
    }

}
