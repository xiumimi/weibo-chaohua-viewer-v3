package com.local.weibocalendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends Activity {
    private static final String UID = "7305739004";
    private static final String HOME_URL = "https://www.weibo.com/u/" + UID;
    private static final String SEARCH_URL =
            "https://www.weibo.com/u/" + UID + "?profile_ftype=1&is_all=1&is_search=1&key_word={query}";

    private Calendar selected = Calendar.getInstance(Locale.CHINA);
    private WebView webView;
    private ScrollView scrollView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        scrollView = new ScrollView(this);
        scrollView.setFillViewport(true);
        scrollView.setBackgroundColor(Color.rgb(247, 245, 239));

        LinearLayout page = new LinearLayout(this);
        page.setOrientation(LinearLayout.VERTICAL);
        page.setBackgroundColor(Color.rgb(247, 245, 239));
        scrollView.addView(page, new ScrollView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        CalendarView calendarView = new CalendarView(this);
        calendarView.setDate(selected.getTimeInMillis(), false, true);
        calendarView.setBackgroundColor(Color.rgb(247, 245, 239));
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selected.set(year, month, dayOfMonth, 0, 0, 0);
            openSelectedDate();
        });
        page.addView(calendarView, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dp(350)));

        webView = new WebView(this);
        webView.setNestedScrollingEnabled(false);
        CookieManager.getInstance().setAcceptCookie(true);
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setSupportMultipleWindows(false);
        settings.setJavaScriptCanOpenWindowsAutomatically(false);
        settings.setUserAgentString(settings.getUserAgentString() + " WeiboChaohuaCalendar/0.2");

        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return handleUrl(view, request.getUrl());
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return handleUrl(view, Uri.parse(url));
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                simplifyWeiboPage();
            }
        });

        page.addView(webView, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                getResources().getDisplayMetrics().heightPixels * 3));

        setContentView(scrollView);
        webView.loadUrl(HOME_URL);
    }

    private boolean handleUrl(WebView view, Uri uri) {
        String scheme = uri.getScheme();
        if ("http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme)) {
            view.loadUrl(uri.toString());
            return true;
        }
        return true;
    }

    private void openSelectedDate() {
        String query = formatDotDate();
        String url = SEARCH_URL.replace("{query}", encode(query));
        webView.loadUrl(url);
        scrollView.postDelayed(() -> scrollView.smoothScrollTo(0, dp(330)), 350);
    }

    private String formatDotDate() {
        return new SimpleDateFormat("yyyy.M.d", Locale.CHINA).format(selected.getTime());
    }

    private String encode(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8").replace("+", "%20");
        } catch (Exception e) {
            return value;
        }
    }

    private void simplifyWeiboPage() {
        String js = "(function(){"
                + "var css='header,nav,aside,[class*=Side],[class*=sidebar],[class*=woo-box-flex][role=navigation]{display:none!important;}'"
                + "+'body{background:#fff!important;}';"
                + "var style=document.getElementById('chaohua-clean-style');"
                + "if(!style){style=document.createElement('style');style.id='chaohua-clean-style';document.head.appendChild(style);}"
                + "style.textContent=css;"
                + "var mark=document.getElementById('chaohua-date-marker');if(mark)mark.remove();"
                + "})();";
        webView.evaluateJavascript(js, null);
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
