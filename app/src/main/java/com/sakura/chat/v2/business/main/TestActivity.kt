package com.sakura.chat.v2.business.main

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.sakura.chat.databinding.ActTestBinding
import com.sakura.chat.v2.base.component.BaseActivityV2
import com.sakura.chat.v2.ext.animateTyping

class TestActivity : BaseActivityV2() {
    private val vb by lazyVB<ActTestBinding>()
    override fun getContentVB(): ViewBinding? {
        return vb
    }

    override fun bindData() {
    }

    override fun init(savedInstanceState: Bundle?) {
        vb.btnTextAnim.setOnClickListener {
            vb.tat.setText("")
            vb.tat.animateTyping(
                "我和嫡姐双双落水，众人大呼救嫡姐，我默默游上岸，从此开始大杀四方\n" +
                        "有个雌竞脑的大学室友有多恶心？\n" +
                        "被骗去柬埔寨的人有多惨? 器官被明码标价\n" +
                        "被迫当小三反击渣男极品一家\n" +
                        "我首富之女的身份居然被人偷了！\n" +
                        "拿校草当挡箭牌气心机闺蜜和前男友，没想到校草当真了\n" +
                        "前世渣男把我迷晕还叫我别怕，转世彻底黑化的我复仇反杀\n" +
                        "妹妹过失杀人，警察来时，我捡起了那把滴血的刀\n" +
                        "我被校霸堵在巷口，却发现他是我谈了三个月的网恋对象\n" +
                        "我和网恋对象奔现，却发现他是我们学校赫赫有名的海王"
            )

        }
    }

}