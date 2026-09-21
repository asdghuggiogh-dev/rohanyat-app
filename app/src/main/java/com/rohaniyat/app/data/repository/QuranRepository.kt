package com.rohaniyat.app.data.repository

import com.rohaniyat.app.data.model.Ayah
import com.rohaniyat.app.data.model.Reciter
import com.rohaniyat.app.data.model.Surah

object QuranRepository {

    // (name, ayahCount, isMeccan)
    private val raw = listOf(
        Triple("الفاتحة", 7, true), Triple("البقرة", 286, false), Triple("آل عمران", 200, false),
        Triple("النساء", 176, false), Triple("المائدة", 120, false), Triple("الأنعام", 165, true),
        Triple("الأعراف", 206, true), Triple("الأنفال", 75, false), Triple("التوبة", 129, false),
        Triple("يونس", 109, true), Triple("هود", 123, true), Triple("يوسف", 111, true),
        Triple("الرعد", 43, false), Triple("إبراهيم", 52, true), Triple("الحجر", 99, true),
        Triple("النحل", 128, true), Triple("الإسراء", 111, true), Triple("الكهف", 110, true),
        Triple("مريم", 98, true), Triple("طه", 135, true), Triple("الأنبياء", 112, true),
        Triple("الحج", 78, false), Triple("المؤمنون", 118, true), Triple("النور", 64, false),
        Triple("الفرقان", 77, true), Triple("الشعراء", 227, true), Triple("النمل", 93, true),
        Triple("القصص", 88, true), Triple("العنكبوت", 69, true), Triple("الروم", 60, true),
        Triple("لقمان", 34, true), Triple("السجدة", 30, true), Triple("الأحزاب", 73, false),
        Triple("سبأ", 54, true), Triple("فاطر", 45, true), Triple("يس", 83, true),
        Triple("الصافات", 182, true), Triple("ص", 88, true), Triple("الزمر", 75, true),
        Triple("غافر", 85, true), Triple("فصلت", 54, true), Triple("الشورى", 53, true),
        Triple("الزخرف", 89, true), Triple("الدخان", 59, true), Triple("الجاثية", 37, true),
        Triple("الأحقاف", 35, true), Triple("محمد", 38, false), Triple("الفتح", 29, false),
        Triple("الحجرات", 18, false), Triple("ق", 45, true), Triple("الذاريات", 60, true),
        Triple("الطور", 49, true), Triple("النجم", 62, true), Triple("القمر", 55, true),
        Triple("الرحمن", 78, false), Triple("الواقعة", 96, true), Triple("الحديد", 29, false),
        Triple("المجادلة", 22, false), Triple("الحشر", 24, false), Triple("الممتحنة", 13, false),
        Triple("الصف", 14, false), Triple("الجمعة", 11, false), Triple("المنافقون", 11, false),
        Triple("التغابن", 18, false), Triple("الطلاق", 12, false), Triple("التحريم", 12, false),
        Triple("الملك", 30, true), Triple("القلم", 52, true), Triple("الحاقة", 52, true),
        Triple("المعارج", 44, true), Triple("نوح", 28, true), Triple("الجن", 28, true),
        Triple("المزمل", 20, true), Triple("المدثر", 56, true), Triple("القيامة", 40, true),
        Triple("الإنسان", 31, false), Triple("المرسلات", 50, true), Triple("النبأ", 40, true),
        Triple("النازعات", 46, true), Triple("عبس", 42, true), Triple("التكوير", 29, true),
        Triple("الانفطار", 19, true), Triple("المطففين", 36, true), Triple("الانشقاق", 25, true),
        Triple("البروج", 22, true), Triple("الطارق", 17, true), Triple("الأعلى", 19, true),
        Triple("الغاشية", 26, true), Triple("الفجر", 30, true), Triple("البلد", 20, true),
        Triple("الشمس", 15, true), Triple("الليل", 21, true), Triple("الضحى", 11, true),
        Triple("الشرح", 8, true), Triple("التين", 8, true), Triple("العلق", 19, true),
        Triple("القدر", 5, true), Triple("البينة", 8, false), Triple("الزلزلة", 8, false),
        Triple("العاديات", 11, true), Triple("القارعة", 11, true), Triple("التكاثر", 8, true),
        Triple("العصر", 3, true), Triple("الهمزة", 9, true), Triple("الفيل", 5, true),
        Triple("قريش", 4, true), Triple("الماعون", 7, true), Triple("الكوثر", 3, true),
        Triple("الكافرون", 6, true), Triple("النصر", 3, false), Triple("المسد", 5, true),
        Triple("الإخلاص", 4, true), Triple("الفلق", 5, true), Triple("الناس", 6, true)
    )

    val surahs: List<Surah> = raw.mapIndexed { index, (name, count, meccan) ->
        Surah(number = index + 1, name = name, ayahCount = count, isMeccan = meccan)
    }

    // Demo ayah text for a few surahs only — see README for why the rest are not bundled
    // (this app ships no third-party Quran text database; wire up a real one, such as
    // an offline Tanzil/King Fahd Complex text file, before shipping to production).
    val sampleAyahs: Map<Int, List<String>> = mapOf(
        1 to listOf(
            "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ", "الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ", "الرَّحْمَٰنِ الرَّحِيمِ",
            "مَالِكِ يَوْمِ الدِّينِ", "إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ", "اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ",
            "صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ الْمَغْضُوبِ عَلَيْهِمْ وَلَا الضَّالِّينَ"
        ),
        112 to listOf("قُلْ هُوَ اللَّهُ أَحَدٌ", "اللَّهُ الصَّمَدُ", "لَمْ يَلِدْ وَلَمْ يُولَدْ", "وَلَمْ يَكُنْ لَهُ كُفُوًا أَحَدٌ"),
        113 to listOf(
            "قُلْ أَعُوذُ بِرَبِّ الْفَلَقِ", "مِنْ شَرِّ مَا خَلَقَ", "وَمِنْ شَرِّ غَاسِقٍ إِذَا وَقَبَ",
            "وَمِنْ شَرِّ النَّفَّاثَاتِ فِي الْعُقَدِ", "وَمِنْ شَرِّ حَاسِدٍ إِذَا حَسَدَ"
        ),
        114 to listOf(
            "قُلْ أَعُوذُ بِرَبِّ النَّاسِ", "مَلِكِ النَّاسِ", "إِلَٰهِ النَّاسِ",
            "مِنْ شَرِّ الْوَسْوَاسِ الْخَنَّاسِ", "الَّذِي يُوَسْوِسُ فِي صُدُورِ النَّاسِ", "مِنَ الْجِنَّةِ وَالنَّاسِ"
        )
    )

    fun ayahsFor(surahNumber: Int): List<Ayah> =
        sampleAyahs[surahNumber]?.mapIndexed { i, text -> Ayah(i + 1, text) } ?: emptyList()

    val reciters = listOf(
        Reciter("sudais", "عبدالرحمن السديس"),
        Reciter("afasy", "مشاري العفاسي"),
        Reciter("husary", "محمود خليل الحصري"),
        Reciter("minshawi", "محمد صديق المنشاوي"),
        Reciter("ghamdi", "سعد الغامدي"),
        Reciter("shatri", "أبو بكر الشاطري")
    )
}
