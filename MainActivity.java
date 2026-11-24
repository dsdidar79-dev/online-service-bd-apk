package com.onlineservicebd.app;

import android.app.*;
import android.os.*;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.content.*;
import android.widget.*;
import org.json.*;
import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends Activity {
    private static final String SUPABASE_URL = "https://dzjldurvekflugrcvziv.supabase.co";
    private static final String SUPABASE_KEY = "sb_publishable_W3_LwB3etjp4shhf6";
    private static final int NAVY=Color.rgb(6,27,58), DEEP=Color.rgb(3,18,43), BLUE=Color.rgb(0,139,219), CYAN=Color.rgb(0,190,235), GOLD=Color.rgb(255,202,40), WHITE=Color.WHITE, INK=Color.rgb(13,28,51), PALE=Color.rgb(239,247,253);
    private final ExecutorService io=Executors.newSingleThreadExecutor();
    private LinearLayout root, body, nav;
    private String accessToken="", userEmail="", userRole="user", userName="ব্যবহারকারী";
    private boolean loggedIn=false;
    private final String[] services={
      "NID PDF Copy","হারানো NID উদ্ধার","NID Server Copy","ছবি ও ঠিকানা দিয়ে NID PDF","নতুন আইডি কার্ড আবেদন","নতুন ভোটার নিবন্ধন স্লিপ",
      "নতুন জন্ম নিবন্ধন আবেদন","জন্ম নিবন্ধন অনলাইন কপি","জন্ম সনদ তথ্য সংশোধন","মৃত্যু নিবন্ধন সনদ আবেদন","জন্ম নিবন্ধন ইংরেজি ও বাংলা সনদ রি-ইস্যু",
      "নতুন TIN সার্টিফিকেট তৈরি","দ্রুত নতুন ই-টিন (e-TIN) রেজিস্ট্রেশন","হারানো TIN সার্টিফিকেট উত্তোলন","পুরাতন বা হারিয়ে যাওয়া টিন সার্টিফিকেট রি-প্রিন্ট বা উদ্ধার","রিটার্ন দাখিল ও সার্টিফিকেট আপডেট",
      "অনলাইন খতিয়ান অনুসন্ধান","সার্টিফাইড পর্চা কপি আবেদন","নামজারি (Mutation) আবেদন","জমির ম্যাপ ও নকশা সংগ্রহ",
      "প্রফেশনাল CV তৈরি (PDF/Word)","Word ফাইল PDF রূপান্তর","পাসপোর্ট সাইজ ছবি এডিটিং ও ব্যাকগ্রাউন্ড রিমুভ","PDF Merge, Split & Password Remove"
    };

    @Override public void onCreate(Bundle b){super.onCreate(b);getWindow().setStatusBarColor(NAVY);getWindow().setNavigationBarColor(NAVY);showHome();}
    private GradientDrawable bg(int color,int radius,int stroke){GradientDrawable d=new GradientDrawable();d.setColor(color);d.setCornerRadius(radius);if(stroke!=0)d.setStroke(1,stroke);return d;}
    private TextView text(String s,int size,int color,boolean bold){TextView t=new TextView(this);t.setText(s);t.setTextSize(size);t.setTextColor(color);if(bold)t.setTypeface(Typeface.DEFAULT,Typeface.BOLD);t.setGravity(Gravity.CENTER_VERTICAL);return t;}
    private LinearLayout column(){LinearLayout l=new LinearLayout(this);l.setOrientation(LinearLayout.VERTICAL);return l;}
    private LinearLayout row(){LinearLayout l=new LinearLayout(this);l.setOrientation(LinearLayout.HORIZONTAL);l.setGravity(Gravity.CENTER_VERTICAL);return l;}
    private void pad(View v,int a){v.setPadding(a,a,a,a);}
    private void init(String title){
      root=column();root.setBackgroundColor(PALE);setContentView(root);
      LinearLayout header=column();header.setPadding(18,14,18,14);header.setBackground(bg(NAVY,0,0));
      TextView brand=text("◉  অনলাইন সার্ভিস বিডি",22,WHITE,true);header.addView(brand);
      TextView sub=text("ONLINE SERVICE BD",12,GOLD,true);sub.setPadding(42,0,0,0);header.addView(sub);
      TextView page=text(title,16,WHITE,true);page.setPadding(0,13,0,0);header.addView(page);root.addView(header);
      ScrollView scroll=new ScrollView(this);body=column();body.setPadding(14,14,14,18);scroll.addView(body);root.addView(scroll,new LinearLayout.LayoutParams(-1,0,1));
      nav=row();nav.setBackground(bg(DEEP,0,0));String[] labels={"⌂\nহোম","▦\nসার্ভিস","🛒\nঅর্ডার","♧\nনোটিফাই","●\nপ্রোফাইল"};
      for(int i=0;i<labels.length;i++){final int n=i;TextView b=text(labels[i],12,i==0?CYAN:WHITE,true);b.setGravity(Gravity.CENTER);b.setPadding(2,12,2,12);nav.addView(b,new LinearLayout.LayoutParams(0,-2,1));b.setOnClickListener(v->{if(n==0)showHome();else if(n==1)showServices();else if(n==2)showOrders();else if(n==3)showNotifications();else showProfile();});}root.addView(nav);
    }
    private void section(String s){TextView t=text(s,17,NAVY,true);t.setPadding(3,12,3,8);body.addView(t);}
    private void card(View v){LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2);p.bottomMargin=10;v.setBackground(bg(WHITE,18,0xffd8e6f1));v.setElevation(3);v.setPadding(14,13,14,13);body.addView(v,p);}
    private TextView button(String s){TextView b=text(s,15,WHITE,true);b.setGravity(Gravity.CENTER);b.setPadding(15,13,15,13);b.setBackground(bg(BLUE,28,0));return b;}
    private void showHome(){init("হোম");
      TextView hello=text("👋  আসসালামু আলাইকুম, "+(loggedIn?userName:"স্বাগতম"),18,NAVY,true);hello.setPadding(4,8,4,14);body.addView(hello);
      LinearLayout bal=row();bal.setBackground(new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,new int[]{0xff0078c8,0xff04b9e8}));bal.setPadding(16,18,16,18);LinearLayout left=column();left.addView(text("আমার ব্যালেন্স",16,WHITE,true));left.addView(text("৳  —",28,GOLD,true));bal.addView(left,new LinearLayout.LayoutParams(0,-2,1));TextView recharge=button("＋ রিচার্জ করুন");bal.addView(recharge);recharge.setOnClickListener(v->showLogin("রিচার্জ করতে লগইন করুন"));body.addView(bal);
      EditText search=new EditText(this);search.setSingleLine(true);search.setHint("আপনার প্রয়োজনীয় সেবা খুঁজুন...");search.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_menu_search,0,0,0);LinearLayout.LayoutParams sp=new LinearLayout.LayoutParams(-1,-2);sp.setMargins(0,14,0,8);body.addView(search,sp);
      section("জনপ্রিয় সেবা");String[] popular={services[0],services[6],services[11],services[16],services[20],services[22]};for(String s:popular){TextView t=text("▣   "+s+"     ›",16,INK,true);card(t);t.setOnClickListener(v->showService(s));}
      section("সকল সেবা");TextView all=button("সব সেবা দেখুন  →");body.addView(all);all.setOnClickListener(v->showServices());
      section("সাম্প্রতিক অর্ডার");TextView orders=text(loggedIn?"আপনার অর্ডার দেখতে এখানে চাপ দিন":"অর্ডার দেখতে প্রথমে লগইন করুন",15,NAVY,true);card(orders);orders.setOnClickListener(v->showOrders());
    }
    private void showServices(){init("সকল সেবা");EditText q=new EditText(this);q.setSingleLine(true);q.setHint("সেবা খুঁজুন...");body.addView(q);LinearLayout list=column();body.addView(list);Runnable fill=()->{list.removeAllViews();String term=q.getText().toString().toLowerCase();for(String s:services)if(s.toLowerCase().contains(term)){TextView t=text("▣   "+s+"     ›",15,INK,true);cardIn(list,t);t.setOnClickListener(v->showService(s));}};q.addTextChangedListener(new android.text.TextWatcher(){public void beforeTextChanged(CharSequence s,int st,int c,int a){}public void onTextChanged(CharSequence s,int st,int before,int count){fill.run();}public void afterTextChanged(android.text.Editable e){}});fill.run();}
    private void cardIn(LinearLayout parent,View v){LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2);p.bottomMargin=9;v.setPadding(14,15,14,15);v.setBackground(bg(WHITE,15,0xffd8e6f1));parent.addView(v,p);}
    private void showService(String service){init("সেবা অর্ডার");section(service);body.addView(text("এই সেবার অর্ডার দিতে নিচের তথ্য পূরণ করুন।",14,INK,false));EditText details=new EditText(this);details.setHint("অর্ডারের প্রয়োজনীয় তথ্য লিখুন");details.setMinLines(4);details.setGravity(Gravity.TOP);card(details);EditText phone=new EditText(this);phone.setHint("মোবাইল নম্বর");phone.setInputType(3);card(phone);TextView send=button("লগইন করে অর্ডার দিন");body.addView(send);send.setOnClickListener(v->{if(!loggedIn)showLogin("অর্ডার জমা দিতে লগইন করুন");else Toast.makeText(this,"পরবর্তী সংস্করণে এই সেবার নির্দিষ্ট ফর্ম ও অর্ডার সাবমিট যুক্ত হবে।",Toast.LENGTH_LONG).show();});}
    private void showLogin(String title){init(title);section("আপনার অ্যাকাউন্টে প্রবেশ করুন");EditText email=new EditText(this);email.setHint("ইমেইল ঠিকানা");email.setInputType(33);card(email);EditText pass=new EditText(this);pass.setHint("পাসওয়ার্ড");pass.setInputType(129);card(pass);TextView login=button("লগইন করুন");body.addView(login);TextView note=text("Supabase Auth দিয়ে নিরাপদ লগইন",12,BLUE,false);body.addView(note);login.setOnClickListener(v->{String e=email.getText().toString().trim(),p=pass.getText().toString();if(e.isEmpty()||p.isEmpty()){Toast.makeText(this,"ইমেইল ও পাসওয়ার্ড দিন",Toast.LENGTH_SHORT).show();return;}login(e,p);});}
    private void login(String email,String password){Toast.makeText(this,"লগইন হচ্ছে...",Toast.LENGTH_SHORT).show();io.execute(()->{try{JSONObject payload=new JSONObject();payload.put("email",email);payload.put("password",password);JSONObject result=new JSONObject(request("/auth/v1/token?grant_type=password","POST",payload.toString(),null));String token=result.optString("access_token");String uid=result.optJSONObject("user")!=null?result.getJSONObject("user").optString("id"):"";if(token.isEmpty())throw new Exception("লগইন টোকেন পাওয়া যায়নি");String profile="[]";try{profile=request("/rest/v1/profiles?id=eq."+URLEncoder.encode(uid,"UTF-8")+"&select=name,role","GET",null,token);}catch(Exception ignored){}JSONArray arr=new JSONArray(profile);String role=arr.length()>0?arr.getJSONObject(0).optString("role","user"):"user";String name=arr.length()>0?arr.getJSONObject(0).optString("name","ব্যবহারকারী"):"ব্যবহারকারী";runOnUiThread(()->{accessToken=token;userEmail=email;userRole=role;userName=name;loggedIn=true;Toast.makeText(this,"লগইন সফল",Toast.LENGTH_SHORT).show();showHome();});}catch(Exception ex){runOnUiThread(()->Toast.makeText(this,"লগইন ব্যর্থ: "+ex.getMessage(),Toast.LENGTH_LONG).show());}});}
    private String request(String path,String method,String data,String token)throws Exception{HttpURLConnection c=(HttpURLConnection)new URL(SUPABASE_URL+path).openConnection();c.setRequestMethod(method);c.setConnectTimeout(15000);c.setReadTimeout(15000);c.setRequestProperty("apikey",SUPABASE_KEY);c.setRequestProperty("Content-Type","application/json");c.setRequestProperty("Accept","application/json");if(token!=null)c.setRequestProperty("Authorization","Bearer "+token);if(data!=null){c.setDoOutput(true);try(OutputStream o=c.getOutputStream()){o.write(data.getBytes(StandardCharsets.UTF_8));}}int code=c.getResponseCode();InputStream in=code>=200&&code<300?c.getInputStream():c.getErrorStream();ByteArrayOutputStream out=new ByteArrayOutputStream();byte[] b=new byte[4096];int n;while((n=in.read(b))!=-1)out.write(b,0,n);String response=out.toString("UTF-8");if(code<200||code>=300)throw new Exception(response);return response;}
    private void showOrders(){if(!loggedIn){showLogin("অর্ডার দেখতে লগইন করুন");return;}init("আমার অর্ডার");section("অর্ডার হিস্টোরি");body.addView(text("আপনার অর্ডার তালিকা পরবর্তী ডাটাবেজ ইন্টিগ্রেশন ধাপে যুক্ত হবে।",14,INK,false));}
    private void showNotifications(){if(!loggedIn){showLogin("নোটিফিকেশন দেখতে লগইন করুন");return;}init("নোটিফিকেশন");section("আপনার নোটিফিকেশন");body.addView(text("নোটিফিকেশন সেন্টার পরবর্তী ধাপে সংযুক্ত হবে।",14,INK,false));}
    private void showProfile(){if(!loggedIn){showLogin("প্রোফাইল দেখতে লগইন করুন");return;}init("প্রোফাইল");section("অ্যাকাউন্ট তথ্য");card(text("নাম: "+userName+"\nইমেইল: "+userEmail+"\nরোল: "+userRole,15,INK,true));if("admin".equalsIgnoreCase(userRole)){TextView a=button("অ্যাডমিন প্যানেল");body.addView(a);a.setOnClickListener(v->showAdmin());}TextView logout=button("লগআউট");body.addView(logout);logout.setOnClickListener(v->{loggedIn=false;accessToken="";userEmail="";showHome();});}
    private void showAdmin(){init("অ্যাডমিন প্যানেল");section("অ্যাডমিন ড্যাশবোর্ড");body.addView(text("অর্ডার, রিচার্জ, ইউজার ও ব্যালেন্স ব্যবস্থাপনা—পরবর্তী ধাপে যুক্ত হবে।",14,INK,false));}
    @Override protected void onDestroy(){super.onDestroy();io.shutdown();}
}
