# Online Service BD — Native Android starter

এটি WebView নয়; Java Android Views দিয়ে তৈরি Native Android অ্যাপের প্রথম ধাপ।

## চালানোর নিয়ম
1. ZIP Extract করুন।
2. Android Studio → Open → `OnlineServiceBD` ফোল্ডার নির্বাচন করুন।
3. Gradle Sync সম্পন্ন হলে Run চাপুন। Android Studio-তে JDK 17 নির্বাচন করুন।

## এই সংস্করণে
- ওয়েবসাইটের নেভি ব্লু/সায়ান/গোল্ড থিমে Native Home ও bottom navigation
- Index HTML থেকে পাওয়া ২৪টি `openOrderModal()` সেবার নাম
- সার্ভিস তালিকা ও অর্ডার ফর্মের প্রাথমিক Native UI
- Supabase Auth REST দিয়ে ইমেইল/পাসওয়ার্ড লগইন
- লগইনের পর profiles টেবিল থেকে role যাচাইয়ের ভিত্তি

## Supabase
`MainActivity.java`-এ project URL ও publishable key রাখা হয়েছে। Publishable key অ্যাপ ক্লায়েন্টে ব্যবহারযোগ্য; কখনো service_role key বসাবেন না। Row Level Security (RLS) অবশ্যই চালু ও সঠিক রাখতে হবে।

## গুরুত্বপূর্ণ সীমা
এটি প্রথম Native UI/Auth milestone; এখনো order submit, balance, recharge, notifications এবং সম্পূর্ণ admin CRUD সংযুক্ত নয়। Index-এর সব অর্ডার ফর্মের field-by-field mapping পরবর্তী ধাপে করতে হবে—কোনো generic field-কে আসল ফর্ম বলে ধরে নেওয়া হয়নি। Admin role যাচাইয়ের পূর্ণ server-side policy ও admin screens-ও পরবর্তী ধাপ।
