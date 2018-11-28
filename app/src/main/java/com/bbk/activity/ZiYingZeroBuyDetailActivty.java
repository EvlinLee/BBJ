package com.bbk.activity;

import android.animation.ObjectAnimator;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.adapter.login.AlibcLogin;
import com.alibaba.baichuan.android.trade.callback.AlibcLoginCallback;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.android.trade.page.AlibcPage;
import com.alibaba.fastjson.JSON;
import com.bbk.Bean.DemoTradeCallback;
import com.bbk.Bean.JumpBean;
import com.bbk.Bean.NewHomeCzgBean;
import com.bbk.Bean.ShareBean;
import com.bbk.Bean.ShopDetailBean;
import com.bbk.adapter.DetailImageAdapter;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.shopcar.ShopOrderActivity;
import com.bbk.shopcar.Utils.ShopDialog;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.GlideImageGuanggaoLoader;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.NoFastClickUtils;
import com.bbk.util.RSAEncryptorAndroid;
import com.bbk.util.ShareJumpUtil;
import com.bbk.util.ShareZeroBuyUtil;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bbk.util.UpdataDialog;
import com.bbk.view.AdaptionSizeTextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.kepler.jd.login.KeplerApiManager;
import com.kepler.jd.sdk.bean.KeplerAttachParameter;
import com.kepler.jd.sdk.exception.KeplerBufferOverflowException;
import com.logg.Logg;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/6/06/006.
 */

public class ZiYingZeroBuyDetailActivty extends BaseActivity {
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_dianpu)
    TextView tvDianpu;
    @BindView(R.id.tv_sale)
    TextView tvSale;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.bprice)
    TextView bprice;
    @BindView(R.id.quan)
    TextView quan;
    @BindView(R.id.tv_detail)
    TextView tvDetail;
    @BindView(R.id.back_image)
    ImageView backImage;
    @BindView(R.id.detail_image)
    ImageView detailImage;
    @BindView(R.id.detail_image_list)
    RecyclerView detailImageList;
    @BindView(R.id.guess_like_list)
    RecyclerView guessLikeList;
    @BindView(R.id.tv_zuan)
    TextView tvZuan;
    @BindView(R.id.ll_share)
    LinearLayout llShare;
    @BindView(R.id.ll_lingquan)
    LinearLayout llLingquan;
    @BindView(R.id.tv_mall)
    TextView tvMall;
    @BindView(R.id.image_fenxiang)
    ImageView imageFenxiang;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    @BindView(R.id.tv_qhj)
    TextView tvQhj;
    @BindView(R.id.tv_zuan1)
    TextView tvZuan1;
    @BindView(R.id.rl_detail)
    RelativeLayout rlDetail;
    @BindView(R.id.view)
    View view;
    @BindView(R.id.tv_dikou)
    TextView tvDikou;
    @BindView(R.id.ck_dikou)
    CheckBox ckDikou;
    @BindView(R.id.ll_check)
    LinearLayout llCheck;
    @BindView(R.id.ll_zerobuy)
    LinearLayout llZerobuy;
    @BindView(R.id.ll_shareorlingquan)
    LinearLayout llShareorlingquan;
    @BindView(R.id.rl_guess_like)
    RelativeLayout rlGuessLike;
    private String content;
    private int durationRotate = 700;// 旋转动画时间
    private int durationAlpha = 500;// 透明度动画时间
    private boolean isGlobalMenuShow = true;
    private String url, rowkey, domain, quans, jumpdomain, zuan, tljNumber, isOldUser, title, bPrice, id,gid;
    public static String Flag = "";
    public static String flag = "", LogFlag = "";
    private UpdataDialog updataDialog;
    Bitmap bitmap = null;
    private Handler handler = new Handler();
    public static TextView tvShare;
    public static TextView tvZeroBuy;
    private ShopDetailBean shopDetailBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jump_detail_layout);
        ImmersedStatusbarUtils.initAfterSetContentView(this, null);
        ButterKnife.bind(this);
        id = getIntent().getStringExtra("id");
        gid = getIntent().getStringExtra("gid");
        refresh.setEnableLoadMore(false);
        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                queryProductDetailById();
            }
        });
        quan.setVisibility(View.GONE);
        tvSale.setVisibility(View.GONE);
        llZerobuy.setVisibility(View.VISIBLE);
        tvQhj.setVisibility(View.GONE);
        tvMall.setText("比比鲸");
        rlGuessLike.setVisibility(View.GONE);
        guessLikeList.setVisibility(View.GONE);
        llCheck.setVisibility(View.GONE);
        queryProductDetailById();
    }


    /**
     * 显示菜单；图标动画
     */
    private void showGlobalMenu() {
        if (isGlobalMenuShow) {
            ObjectAnimator.ofFloat(detailImage, "rotation", 0, 180)
                    .setDuration(durationRotate).start();
            ObjectAnimator.ofFloat(detailImage, "rotation", 0, 180)
                    .setDuration(durationRotate).start();
            ObjectAnimator.ofFloat(detailImage, "rotation", 0, 180)
                    .setDuration(durationRotate).start();
            detailImageList.setVisibility(View.VISIBLE);
            ObjectAnimator.ofFloat(detailImageList, "alpha", 0, 1)
                    .setDuration(durationAlpha).start();
        } else {
            ObjectAnimator.ofFloat(detailImage, "rotation", 180, 360)
                    .setDuration(durationRotate).start();
            ObjectAnimator.ofFloat(detailImage, "rotation", 180, 360)
                    .setDuration(durationRotate).start();
            ObjectAnimator.ofFloat(detailImage, "rotation", 180, 360)
                    .setDuration(durationRotate).start();
            ObjectAnimator.ofFloat(detailImageList, "alpha", 1, 0)
                    .setDuration(durationAlpha).start();
            detailImageList.postDelayed(new Runnable() {
                @Override
                public void run() {
                    detailImageList.setVisibility(View.GONE);
                }
            }, durationAlpha);
        }

    }

    @OnClick({R.id.back_image, R.id.detail_image, R.id.ll_share, R.id.ll_lingquan, R.id.ll_zerobuy})
    public void onViewClicked(View view) {
        Intent intent;
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        switch (view.getId()) {
            case R.id.back_image:
                finish();
                break;
            case R.id.detail_image:
                isGlobalMenuShow = !isGlobalMenuShow;
                showGlobalMenu();
                break;
            case R.id.ll_zerobuy:
                if (TextUtils.isEmpty(userID)) {
                    LogFlag = "3";
                    intent = new Intent(this, UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    showZeroBuyDiscountDialog(ZiYingZeroBuyDetailActivty.this);
                }
                break;
        }
    }




    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        switch (arg0) {
            case 1:
                queryProductDetailById1();
                break;
        }
        super.onActivityResult(arg0, arg1, arg2);
    }

    /**
     * 获取当前分享图片的bitmap
     *
     * @param shareBean
     * @param url
     * @return
     */
    public Bitmap returnZeroBuyBitMap(final ShopDetailBean shareBean, final String url) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                URL imageurl = null;
                try {
                    imageurl = new URL(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    HttpURLConnection conn = (HttpURLConnection) imageurl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                    is.close();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            /**
                             * 调起分享dialog
                             */
                            DialogSingleUtil.dismiss(0);
                            List<String> DetailimgUrlList = new ArrayList<>();
                            DetailimgUrlList.add(shareBean.getShareimg());
                            ShareZeroBuyUtil.showShareDialog(llShare, ZiYingZeroBuyDetailActivty.this, shareBean.getTitle(), "", "", shareBean.getShareimg(), "", imageFenxiang, "", bitmap, DetailimgUrlList);
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return bitmap;
    }

    @Override
    protected void onDestroy() {
        if (ZiYingZeroBuyDetailActivty.this != null && ZiYingZeroBuyDetailActivty.this.isFinishing()) {
            DialogSingleUtil.dismiss(0);
        }
        super.onDestroy();
    }


    /**
     * 抢购弹窗
     *
     * @param context
     */
    public void showZeroBuyDiscountDialog(final Context context) {
        if (updataDialog == null || !updataDialog.isShowing()) {
            //初始化弹窗 布局 点击事件的id
            updataDialog = new UpdataDialog(context, R.layout.hehuo_dialog_layout,
                    new int[]{R.id.tv_update_gengxin});
            updataDialog.show();
            updataDialog.setCanceledOnTouchOutside(true);
            TextView title = updataDialog.findViewById(R.id.title);
            TextView tv_update = updataDialog.findViewById(R.id.tv_update);
            tvShare = updataDialog.findViewById(R.id.tv_update_refuse);
            tvZeroBuy = updataDialog.findViewById(R.id.tv_update_gengxin);
            tvShare.setVisibility(View.VISIBLE);
            tvShare.setText("分享");
            tvZeroBuy.setText("下单");
            ImageView img_close = updataDialog.findViewById(R.id.img_close);
            if (isOldUser != null && isOldUser.equals("0")) {
                title.setText("抢单成功");
                tv_update.setText("分享朋友圈立即0元购");
                String isShare = SharedPreferencesUtil.getSharedData(context, "isShare", "isShare");
                //判断是否分享
                if (TextUtils.isEmpty(isShare)) {
                    tvShare.setBackgroundResource(R.drawable.bg_czg1);
                    tvShare.setTextColor(getResources().getColor(R.color.white));
                    tvZeroBuy.setBackgroundResource(R.drawable.bg_update1);
                    tvZeroBuy.setTextColor(getResources().getColor(R.color.tuiguang_color4));
                    tvZeroBuy.setClickable(false);
                    tvZeroBuy.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            updataDialog.dismiss();
                            StringUtil.showToast(context, "分享后才能下单哦");
                        }
                    });
                } else {
                    tvShare.setBackgroundResource(R.drawable.bg_czg1);
                    tvShare.setTextColor(getResources().getColor(R.color.white));
                    tvZeroBuy.setBackgroundResource(R.drawable.bg_czg1);
                    tvZeroBuy.setTextColor(getResources().getColor(R.color.white));
                    tvZeroBuy.setClickable(true);
                    tvZeroBuy.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            updataDialog.dismiss();
                            getZeroBuyOrder();
                        }
                    });
                }
            }
            else {
                tvShare.setBackgroundResource(R.drawable.bg_czg1);
                tvShare.setTextColor(getResources().getColor(R.color.white));
                tvZeroBuy.setBackgroundResource(R.drawable.bg_update1);
                tvZeroBuy.setTextColor(getResources().getColor(R.color.tuiguang_color4));
                title.setText("新用户才能享受此特权哦\n分享给好友0元购吧");
                tv_update.setVisibility(View.GONE);
                tvZeroBuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updataDialog.dismiss();
                        StringUtil.showToast(context, "新用户才能享受此特权");
                    }
                });
            }
            img_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updataDialog.dismiss();
                }
            });
            tvShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updataDialog.dismiss();
//                    shareCpsZeroBuyInfo();
                    returnZeroBuyBitMap(shopDetailBean,shopDetailBean.getShareimg());
                }
            });
        }
    }

    /**
     * 商品详情
     */
    private void queryProductDetailById() {
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("id", gid);
        maps.put("userid", userID);
        RetrofitClient.getInstance(this).createBaseApi().queryProductDetailById(
                maps, new BaseObserver<String>(this) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.optString("status").equals("1")) {
                                shopDetailBean = JSON.parseObject(jsonObject.optString("content"), ShopDetailBean.class);
                                isOldUser = shopDetailBean.getIsOldUser();
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ZiYingZeroBuyDetailActivty.this,
                                        LinearLayoutManager.VERTICAL, false) {
                                    @Override
                                    public boolean canScrollVertically() {
                                        return false;
                                    }

                                };
                                detailImageList.setHasFixedSize(true);
                                detailImageList.setLayoutManager(linearLayoutManager);
                                title = shopDetailBean.getTitle();
                                bPrice = shopDetailBean.getBprice();
                                tvTitle.setText(title);
                                tvDianpu.setText(shopDetailBean.getDianpu());
                                tvSale.setText(shopDetailBean.getSale() + "人付款");
                                if (shopDetailBean.getBprice() != null && !shopDetailBean.getBprice().equals("null")) {
                                    bprice.setVisibility(View.VISIBLE);
                                    bprice.setText("¥" + shopDetailBean.getBprice());
                                } else {
                                    bprice.setVisibility(View.GONE);
                                }
                                bprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); // 设置中划线并加清晰
                                List<Object> DetailimgUrlList = new ArrayList<>();
                                /**
                                 * 加载banner
                                 */
                                List<Object> imgUrlList = new ArrayList<>();
                                JSONArray imgs = null;
                                try {
                                    if (shopDetailBean.getDetailImgs() != null) {
                                        JSONArray detailImags = new JSONArray(shopDetailBean.getDetailImgs());
                                        for (int i = 0; i < detailImags.length(); i++) {
                                            String imgUrl = detailImags.getString(i);
                                            DetailimgUrlList.add(imgUrl);
                                        }
                                        if (DetailimgUrlList.size() > 0) {
                                            detailImageList.setAdapter(new DetailImageAdapter(ZiYingZeroBuyDetailActivty.this, DetailimgUrlList));
                                            rlDetail.setVisibility(View.VISIBLE);
                                            detailImageList.setVisibility(View.VISIBLE);
                                            view.setVisibility(View.VISIBLE);
                                        } else {
                                            rlDetail.setVisibility(View.GONE);
                                            detailImageList.setVisibility(View.GONE);
                                            view.setVisibility(View.GONE);
                                        }
                                    }
                                    if (shopDetailBean.getImgs() != null) {
                                        imgs = new JSONArray(shopDetailBean.getImgs());
                                        for (int i = 0; i < imgs.length(); i++) {
                                            String imgUrl = imgs.getString(i);
                                            imgUrlList.add(imgUrl);
                                        }
                                        banner.setImages(imgUrlList)
                                                .setImageLoader(new GlideImageGuanggaoLoader())
                                                .setOnBannerListener(new OnBannerListener() {
                                                    @Override
                                                    public void OnBannerClick(int position) {

                                                    }
                                                })
                                                .setDelayTime(3000)
                                                .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                                                .setIndicatorGravity(BannerConfig.CENTER)
                                                .start();
                                    }

                                    refresh.finishRefresh();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void hideDialog() {
                        refresh.finishRefresh();
                        DialogSingleUtil.dismiss(0);
                    }

                    @Override
                    protected void showDialog() {
                        DialogSingleUtil.show(ZiYingZeroBuyDetailActivty.this);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        refresh.finishRefresh();
                        StringUtil.showToast(ZiYingZeroBuyDetailActivty.this, e.message);
                    }
                });
    }

    private void queryProductDetailById1() {
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("id", gid);
        maps.put("userid", userID);
        RetrofitClient.getInstance(this).createBaseApi().queryProductDetailById(
                maps, new BaseObserver<String>(this) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.optString("status").equals("1")) {
                                shopDetailBean = JSON.parseObject(jsonObject.optString("content"), ShopDetailBean.class);
                                isOldUser = shopDetailBean.getIsOldUser();
                                switch (LogFlag) {
                                    case "3":
                                        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
                                        if (!TextUtils.isEmpty(userID)) {
                                            showZeroBuyDiscountDialog(ZiYingZeroBuyDetailActivty.this);
                                        }
                                        break;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void hideDialog() {
                        DialogSingleUtil.dismiss(0);
                    }

                    @Override
                    protected void showDialog() {
                        DialogSingleUtil.show(ZiYingZeroBuyDetailActivty.this);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        StringUtil.showToast(ZiYingZeroBuyDetailActivty.this, e.message);
                    }
                });
    }
    /**
     * 0元购支付接口
     */
    private void getZeroBuyOrder() {
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("userid", userID);
        maps.put("id", id);
        RetrofitClient.getInstance(this).createBaseApi().getZeroBuyOrder(
                maps, new BaseObserver<String>(this) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.optString("status").equals("2")) {
                               Logg.json(jsonObject);
                               Intent intent = new Intent(ZiYingZeroBuyDetailActivty.this,ShopOrderActivity.class);
                               intent.putExtra("status", "2");
                               startActivity(intent);
                               StringUtil.showToast(ZiYingZeroBuyDetailActivty.this,"购买成功");
                            } else {
                                DialogSingleUtil.dismiss(0);
                                StringUtil.showToast(ZiYingZeroBuyDetailActivty.this, jsonObject.optString("errmsg"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void hideDialog() {

                    }

                    @Override
                    protected void showDialog() {
                        DialogSingleUtil.show(ZiYingZeroBuyDetailActivty.this);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        StringUtil.showToast(ZiYingZeroBuyDetailActivty.this, e.message);
                    }
                });
    }

}