com/example/
├── MainActivity.kt               # نقطة الانطلاق وإعدادات وضع الشاشة الكاملة (Edge-to-Edge)
├── model/
│   └── Models.kt                 # نماذج البيانات (الفيديوهات، السور، الأذكار، القراء)
├── data/
│   └── SampleData.kt             # مستودع بيانات القرآن الكريم، الأدعية، والمقاطع
├── media/
│   └── AudioPlayerManager.kt     # مشغل الصوت للتلاوات والآيات القرآنية
├── ui/
│   ├── viewmodel/
│   │   └── RouhaniyatViewModel.kt # إدارة الحالة وحفظ المفضلة والتنقل
│   ├── theme/                    # الألوان والخطوط الإسلامية (الزمردي والذهبي)
│   └── screens/
│       ├── MainScreen.kt         # الهيكل العام وشريط التنقل السفلي
│       ├── HomeScreen.kt         # الصفحة الرئيسية (فيديوهات، قراء، بطاقات Shorts)
│       ├── ShortsFeedScreen.kt   # مشغل فيديوهات Shorts العمودية (سحب لأعلى ولأسفل)
│       ├── QuranScreen.kt        # المصحف الشريف والتلاوات
│       ├── AdhkarScreen.kt       # أذكار الصباح والمساء والأدعية
│       ├── TasbeehScreen.kt      # المسبحة الإلكترونية التفاعلية
│       ├── VideoDetailScreen.kt  # مشغل الفيديو والتعليقات والمقاطع المشابهة
│       ├── SearchScreen.kt       # البحث الذكي في المحتوى والسور
│       ├── CreatePostScreen.kt   # إضافة منشور أو مقطع جديد
│       └── ProfileScreen.kt      # الملف الشخصي والإعدادات
