package com.onlineservicebd.app;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.*;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;
public class MainActivity extends Activity {
 private static final String HOME_URL="https://dsdidar79-dev.github.io/onlineservicebd/";
 private WebView webView; private ValueCallback<Uri[]> fileCallback; private static final int FILE_REQUEST=1001;
 @Override protected void onCreate(Bundle state){super.onCreate(state);getWindow().setStatusBarColor(0xff0f172a);getWindow().setNavigationBarColor(0xff0f172a);webView=new WebView(this);setContentView(webView);
 WebSettings s=webView.getSettings();s.setJavaScriptEnabled(true);s.setDomStorageEnabled(true);s.setDatabaseEnabled(true);s.setAllowFileAccess(true);s.setAllowContentAccess(true);s.setJavaScriptCanOpenWindowsAutomatically(true);s.setSupportMultipleWindows(false);s.setLoadsImagesAutomatically(true);s.setMixedContentMode(WebSettings.MIXED_CONTENT_NEVER_ALLOW);
 CookieManager cm=CookieManager.getInstance();cm.setAcceptCookie(true);cm.setAcceptThirdPartyCookies(webView,true);
 webView.setWebViewClient(new WebViewClient(){@Override public boolean shouldOverrideUrlLoading(WebView v,WebResourceRequest r){return route(r.getUrl().toString());}@SuppressWarnings("deprecation") @Override public boolean shouldOverrideUrlLoading(WebView v,String u){return route(u);}});
 webView.setWebChromeClient(new WebChromeClient(){@Override public boolean onShowFileChooser(WebView v,ValueCallback<Uri[]> cb,FileChooserParams p){if(fileCallback!=null)fileCallback.onReceiveValue(null);fileCallback=cb;try{startActivityForResult(p.createIntent(),FILE_REQUEST);return true;}catch(Exception e){fileCallback=null;Toast.makeText(MainActivity.this,"ফাইল নির্বাচন করা যাচ্ছে না",Toast.LENGTH_SHORT).show();return false;}}});
 webView.setDownloadListener((u,ua,cd,mt,len)->{try{startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(u)));}catch(Exception e){Toast.makeText(this,"ডাউনলোড লিংক খোলা যাচ্ছে না",Toast.LENGTH_SHORT).show();}});
 if(state==null)webView.loadUrl(HOME_URL);else webView.restoreState(state);
 }
 private boolean route(String raw){Uri u=Uri.parse(raw);String scheme=u.getScheme()==null?"":u.getScheme();if("http".equalsIgnoreCase(scheme)||"https".equalsIgnoreCase(scheme)){String h=u.getHost()==null?"":u.getHost().toLowerCase();if(h.equals("dsdidar79-dev.github.io")||h.equals("supabase.co")||h.endsWith(".supabase.co"))return false;}try{startActivity(new Intent(Intent.ACTION_VIEW,u));}catch(Exception e){Toast.makeText(this,"এই লিংকটি খোলা যাচ্ছে না",Toast.LENGTH_SHORT).show();}return true;}
 @Override protected void onActivityResult(int req,int res,Intent data){super.onActivityResult(req,res,data);if(req==FILE_REQUEST&&fileCallback!=null){fileCallback.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(res,data));fileCallback=null;}}
 @Override protected void onSaveInstanceState(Bundle out){super.onSaveInstanceState(out);if(webView!=null)webView.saveState(out);}
 @Override public void onBackPressed(){if(webView!=null&&webView.canGoBack())webView.goBack();else super.onBackPressed();}
 @Override protected void onDestroy(){if(fileCallback!=null){fileCallback.onReceiveValue(null);fileCallback=null;}if(webView!=null){webView.stopLoading();webView.destroy();}super.onDestroy();}
}