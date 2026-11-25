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
 webView.setWebViewClient(new WebViewClient(){@Override public boolean shouldOverrideUrlLoading(WebView v,WebResourceRequest r){return route(r.getUrl().toString());}@SuppressWarnings("deprecation") @Override public boolean shouldOverrideUrlLoading(WebView v,String u){return route(u);}@Override public void onPageFinished(WebView v,String url){super.onPageFinished(v,url);if(url.startsWith(HOME_URL))installAppDesign();}});
 webView.setWebChromeClient(new WebChromeClient(){@Override public boolean onShowFileChooser(WebView v,ValueCallback<Uri[]> cb,FileChooserParams p){if(fileCallback!=null)fileCallback.onReceiveValue(null);fileCallback=cb;try{startActivityForResult(p.createIntent(),FILE_REQUEST);return true;}catch(Exception e){fileCallback=null;Toast.makeText(MainActivity.this,"ফাইল নির্বাচন করা যাচ্ছে না",Toast.LENGTH_SHORT).show();return false;}}});
 webView.setDownloadListener((u,ua,cd,mt,len)->{try{startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(u)));}catch(Exception e){Toast.makeText(this,"ডাউনলোড লিংক খোলা যাচ্ছে না",Toast.LENGTH_SHORT).show();}});
 if(state==null)webView.loadUrl(HOME_URL);else webView.restoreState(state);
 }

 private void installAppDesign(){
  String js = "(function(){"+
   "if(!document.getElementById('osbAppStyle')){var s=document.createElement('style');s.id='osbAppStyle';s.textContent='"+
   "body{background:#eaf4fc!important;color:#10233f!important;padding-bottom:90px!important}"+
   "header{background:linear-gradient(135deg,#06234b,#063b78)!important}"+
   ".dashboard-container{max-width:100%!important;margin:0!important;padding:8px 10px!important}"+
   ".main-flex-layout{display:flex!important;flex-direction:column!important;height:auto!important;gap:10px!important}"+
   ".left-sidebar,.right-content{display:contents!important;height:auto!important;overflow:visible!important}"+
   ".dash-box{background:#fff!important;color:#10233f!important;border:1px solid #d7e6f5!important;border-radius:18px!important;box-shadow:0 5px 14px rgba(7,38,77,.1)!important;margin:0 0 10px!important}"+
   ".sidebar-nav-box{display:none!important}.service-list{display:grid!important;grid-template-columns:repeat(2,minmax(0,1fr))!important;gap:9px!important}"+
   ".service-item-link{background:#f4f9ff!important;border:1px solid #d7e7f7!important;border-radius:15px!important;padding:10px!important}"+
   ".app-bottom-nav{position:fixed;z-index:5000;left:10px;right:10px;bottom:8px;height:66px;border-radius:20px;background:linear-gradient(120deg,#061d3c,#082e60);display:flex;align-items:center;justify-content:space-around;box-shadow:0 8px 28px rgba(0,0,0,.28)}"+
   ".app-bottom-nav button{border:0;background:transparent;color:#e7f2ff;font:600 11px Hind Siliguri,sans-serif;display:flex;flex-direction:column;align-items:center;gap:2px}.app-bottom-nav b{font-size:22px}.app-bottom-nav .active{color:#18c7ef}"+
   ".hero{padding:14px 12px!important}.auth-card{padding:26px 18px!important;border-radius:20px!important}';document.head.appendChild(s);}"+
   "function addNav(){if(document.getElementById('osbAppNav')||!document.getElementById('dashboardContainer'))return;"+
   "var n=document.createElement('nav');n.id='osbAppNav';n.className='app-bottom-nav';"+
   "n.innerHTML='<button class=active data-go=home><b>⌂</b>হোম</button><button data-go=services><b>▦</b>সার্ভিস</button><button data-go=orders><b>🛒</b>অর্ডার</button><button data-go=notify><b>♧</b>নোটিফিকেশন</button><button data-go=profile><b>●</b>প্রোফাইল</button>';document.body.appendChild(n);"+
   "n.onclick=function(e){var b=e.target.closest('button[data-go]');if(!b)return;n.querySelectorAll('button').forEach(function(x){x.classList.toggle('active',x===b)});var g=b.dataset.go;"+
   "if(g==='home')window.scrollTo({top:0,behavior:'smooth'});"+
   "if(g==='services'){var x=document.querySelector('.service-list');if(x)x.scrollIntoView({behavior:'smooth'});}"+
   "if(g==='orders'){var x=document.getElementById('userOrderHistoryTable')||document.querySelector('.right-content');if(x)x.scrollIntoView({behavior:'smooth'});}"+
   "if(g==='notify'){var x=document.getElementById('notifBtn');if(x)x.click();}if(g==='profile'){var x=document.getElementById('profileDetailsBtn');if(x)x.click();}};}"+
   "new MutationObserver(addNav).observe(document.body,{childList:true,subtree:true,attributes:true,attributeFilter:['style','class']});addNav();})();";
  webView.evaluateJavascript(js,null);
 }
\n private boolean route(String raw){Uri u=Uri.parse(raw);String scheme=u.getScheme()==null?"":u.getScheme();if("http".equalsIgnoreCase(scheme)||"https".equalsIgnoreCase(scheme)){String h=u.getHost()==null?"":u.getHost().toLowerCase();if(h.equals("dsdidar79-dev.github.io")||h.equals("supabase.co")||h.endsWith(".supabase.co"))return false;}try{startActivity(new Intent(Intent.ACTION_VIEW,u));}catch(Exception e){Toast.makeText(this,"এই লিংকটি খোলা যাচ্ছে না",Toast.LENGTH_SHORT).show();}return true;}
 @Override protected void onActivityResult(int req,int res,Intent data){super.onActivityResult(req,res,data);if(req==FILE_REQUEST&&fileCallback!=null){fileCallback.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(res,data));fileCallback=null;}}
 @Override protected void onSaveInstanceState(Bundle out){super.onSaveInstanceState(out);if(webView!=null)webView.saveState(out);}
 @Override public void onBackPressed(){if(webView!=null&&webView.canGoBack())webView.goBack();else super.onBackPressed();}
 @Override protected void onDestroy(){if(fileCallback!=null){fileCallback.onReceiveValue(null);fileCallback=null;}if(webView!=null){webView.stopLoading();webView.destroy();}super.onDestroy();}
}