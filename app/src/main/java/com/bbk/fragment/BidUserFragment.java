package com.bbk.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bbk.activity.BidHomeActivity;
import com.bbk.activity.BidListDetailActivity;
import com.bbk.activity.BidMyListDetailActivity;
import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.activity.UserAccountActivity;
import com.bbk.activity.UserLoginNewActivity;
import com.bbk.flow.DataFlow6;
import com.bbk.flow.ResultEvent;
import com.bbk.util.ImmersionUtil;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.view.CircleImageView1;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * 我的_01_首页
 */

public class BidUserFragment extends BaseViewPagerFragment implements ResultEvent ,View.OnClickListener{
    private View mView;
    private DataFlow6 dataFlow;
    private LinearLayout mmybid,mmyfabid;
    private RelativeLayout newpinglun;
    private RelativeLayout mshenhe,mjie,mpl,mcomplete,mbidjie,mbidpl,mbidcomplete;
    private TextView mshenhenum,mjietext,mpltext,mbidjietext,mbidpltext,mnewmsg,musername;
    private ImageView muserimg;
    private View data_head;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // return super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.fragment_bid_user, null);
        data_head = mView.findViewById(R.id.data_head);
        ImmersionUtil.initstateView(getActivity(),data_head);
        dataFlow = new DataFlow6(getActivity());
        initView();
        initData();

        return mView;
    }
    public void initView(){
        mmybid = (LinearLayout) mView.findViewById(R.id.mmybid);
        mmyfabid = (LinearLayout) mView.findViewById(R.id.mmyfabid);
        newpinglun = (RelativeLayout) mView.findViewById(R.id.newpinglun);
        mshenhe = (RelativeLayout) mView.findViewById(R.id.mshenhe);
        mjie = (RelativeLayout) mView.findViewById(R.id.mjie);
        mpl = (RelativeLayout) mView.findViewById(R.id.mpl);
        mcomplete = (RelativeLayout) mView.findViewById(R.id.mcomplete);
        mbidjie = (RelativeLayout) mView.findViewById(R.id.mbidjie);
        mbidpl = (RelativeLayout) mView.findViewById(R.id.mbidpl);
        mbidcomplete = (RelativeLayout) mView.findViewById(R.id.mbidcomplete);
        mshenhenum = (TextView) mView.findViewById(R.id.mshenhenum);
        mjietext = (TextView) mView.findViewById(R.id.mjietext);
        mpltext = (TextView) mView.findViewById(R.id.mpltext);
        mbidjietext = (TextView) mView.findViewById(R.id.mbidjietext);
        mbidpltext = (TextView) mView.findViewById(R.id.mbidpltext);
        mnewmsg = (TextView) mView.findViewById(R.id.mnewmsg);
        musername = (TextView) mView.findViewById(R.id.musername);
        muserimg = (ImageView) mView.findViewById(R.id.muserimg);



        mmybid.setOnClickListener(this);
        mmyfabid.setOnClickListener(this);
        mshenhe.setOnClickListener(this);
        mjie.setOnClickListener(this);
        mpl.setOnClickListener(this);
        mcomplete.setOnClickListener(this);
        mbidjie.setOnClickListener(this);
        mbidpl.setOnClickListener(this);
        musername.setOnClickListener(this);
        muserimg.setOnClickListener(this);
        newpinglun.setOnClickListener(this);

}
    public void initData(){
        HashMap<String, String> paramsMap = new HashMap<>();
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(),"userInfor", "userID");
        paramsMap.put("userid",userID);
        dataFlow.requestData(1, "bid/queryMyBiaoMsg", paramsMap, this,false);
    }


    @Override
    public void lazyLoad() {

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.mmyfabid:
                intent = new Intent(getActivity(), BidListDetailActivity.class);
                startActivity(intent);
                break;
            case R.id.mshenhe:
                intent = new Intent(getActivity(), BidListDetailActivity.class);
                intent.putExtra("status","1");
                startActivity(intent);
                break;
            case R.id.mjie:
                intent = new Intent(getActivity(), BidListDetailActivity.class);
                intent.putExtra("status","2");
                startActivity(intent);
                break;
            case R.id.mpl:
                intent = new Intent(getActivity(), BidListDetailActivity.class);
                intent.putExtra("status","3");
                startActivity(intent);
                break;
            case R.id.mcomplete:
                intent = new Intent(getActivity(), BidListDetailActivity.class);
                intent.putExtra("status","4");
                startActivity(intent);
                break;
            case R.id.mmybid:
                intent = new Intent(getActivity(), BidMyListDetailActivity.class);
                startActivity(intent);
                break;
            case R.id.mbidjie:
                intent = new Intent(getActivity(), BidMyListDetailActivity.class);
                intent.putExtra("status","1");
                startActivity(intent);
                break;
            case R.id.mbidpl:
                intent = new Intent(getActivity(), BidMyListDetailActivity.class);
                intent.putExtra("status","2");
                startActivity(intent);
                break;
            case R.id.mbidcomplete:
                intent = new Intent(getActivity(), BidMyListDetailActivity.class);
                intent.putExtra("status","3");
                startActivity(intent);
                break;
            case R.id.muserimg:
                islogin();
                break;
            case R.id.musername:
                islogin();
                break;
            case R.id.newpinglun:
                BidHomeActivity.initThree();
                break;

        }
    }
    public void islogin(){
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(),"userInfor", "userID");
        if (TextUtils.isEmpty(userID)){
            Intent intent = new Intent(getActivity(), UserLoginNewActivity.class);
            startActivity(intent);
        }else {
            Intent intent = new Intent(getActivity(), UserAccountActivity.class);
            startActivity(intent);
        }
    }
    public void setnum(TextView textView,String num){
        if ("0".equals(num)){
            textView.setVisibility(View.GONE);
        }else {
            textView.setText(num);
            textView.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onResultData(int requestCode, String api, JSONObject dataJo, String content){
        try {
            String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(),"userInfor", "userID");
            if (TextUtils.isEmpty(userID)){
                musername.setText("请登录");
                CircleImageView1.getImg(getActivity(),R.mipmap.logo_01,muserimg);
            }else {
                String imgUrl = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(),"userInfor", "imgUrl");
                String nickname = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(),"userInfor", "nickname");
                musername.setText(nickname);
                CircleImageView1.getImg(getActivity(),imgUrl,muserimg);
            }
            JSONObject object = new JSONObject(content);
            setnum(mshenhenum,object.optString("shenhe"));
            setnum(mjietext,object.optString("jie"));
            setnum(mpltext,object.optString("pl"));
            setnum(mbidjietext,object.optString("bidjie"));
            setnum(mbidpltext,object.optString("bidpl"));
            setnum(mnewmsg,object.optString("sysmsg"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }
}