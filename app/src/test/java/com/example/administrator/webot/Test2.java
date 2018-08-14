package com.example.administrator.webot;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Test2 {
    private String tagliststr = "area,base,basefont,br,col,frame,hr,img,input,link,meta,param,embed,command,keygen,source,track,wbr,br,a,code,address,article,applet,aside,audio,blockquote,button,canvas,center,dd,del,dir,div,dl,dt,fieldset,figcaption,figure,footer,form,frameset,h1,h2,h3,h4,h5,h6,header,hgroup,hr,iframe,ins,isindex,li,map,menu,noframes,noscript,object,ol,output,p,pre,section,script,table,tbody,td,tfoot,th,thead,tr,ul,video,abbr,acronym,applet,b,basefont,bdo,big,button,cite,del,dfn,em,font,i,iframe,img,input,ins,kbd,label,map,object,q,s,samp,script,select,small,span,strike,strong,sub,sup,textarea,tt,u,var,checked,compact,declare,defer,disabled,ismap,multiple,nohref,noresize,noshade,nowrap,readonly,selected";
    private String[] taglist = tagliststr.split(",");
    private String attrstr = "onfocus,ALT,scrolldelay,type,align,scrolling,rowspan,rel,action,href,id,scrollamount,hspace,selected,height,border,method,onclick,topmargin,alt,valign,quality,bgcolor,size,background,name,pluginspage,style,HTTP-EQUIV,color,SRC,WIDTH,onmouseover,colspan,cellpadding,allowtransparency,frameborder,CONTENT,onsubmit,marginheight,value,class,onblur,src,leftmargin,HEIGHT,target,marginwidth,classid,onmouseout,codebase,width,cellspacing,onLoad";
    private String[] attrlist =  attrstr.split(",");
    @Test
    public void test3() throws Exception {

        String htmlBody = "<span> <h2 id=\"activity-name\"><span style=\"font-size: 14px; line-height: 40px;\">刘佳琦</span><span style=\"font-size: 14px; line-height: 40px;\">&nbsp;</span><span id=\"profileBt\" style=\"font-size: 14px; line-height: 40px;\"><a href=\"javascript:void(0);\">汤氏物流</a></span><span style=\"font-size: 14px; line-height: 40px;\">&nbsp;</span><em id=\"publish_time\" style=\"font-size: 14px; line-height: 40px;\">1月17日</em><br></h2> \n" +
                "   <div id=\"js_content\"> \n" +
                "    <section> \n" +
                "     <section> \n" +
                "      <section> \n" +
                "       <section> \n" +
                "        <p><br></p> \n" +
                "       </section> \n" +
                "      </section> \n" +
                "     </section> \n" +
                "     <section> \n" +
                "      <section> \n" +
                "       <section> \n" +
                "        <section> \n" +
                "         <section> \n" +
                "          <section> \n" +
                "           <p>“古之立大事者，不惟有超世之才，亦必有坚忍不拔之志”——苏轼</p> \n" +
                "          </section> \n" +
                "         </section> \n" +
                "        </section> \n" +
                "       </section> \n" +
                "      </section> \n" +
                "     </section> \n" +
                "     <section> \n" +
                "      <section> \n" +
                "       <section> \n" +
                "        <p style=\"text-align: center;\"><img src=\"https://www.betern.com/tswl_admin/assets/upload/2018/05/31/1527728337.jpg\" style=\"max-width:100%;\"><br></p> \n" +
                "       </section> \n" +
                "      </section> \n" +
                "     </section> \n" +
                "     <section> \n" +
                "      <p>2018年1月16日，在这寒风凛冽的季节里，我们迎来了汤氏物流第六届第三期军训，大家集结在杭州汤氏恒通车厢制造有限公司，换上军装一同迎接着五天四夜的艰苦训练。</p> \n" +
                "     </section> \n" +
                "     <section> \n" +
                "      <section> \n" +
                "       <section> \n" +
                "        <section> \n" +
                "         <section> \n" +
                "          <section style=\"text-align: center;\"> \n" +
                "           <img src=\"https://www.betern.com/tswl_admin/assets/upload/2018/05/31/1527728338.jpg\" style=\"max-width:100%;\"> \n" +
                "           <img src=\"https://www.betern.com/tswl_admin/assets/upload/2018/05/31/1527728339.jpg\" style=\"max-width: 100%;\"> \n" +
                "           <img src=\"https://www.betern.com/tswl_admin/assets/upload/2018/05/31/1527728340.jpg\" style=\"max-width: 100%;\"> \n" +
                "           <br> \n" +
                "          </section> \n" +
                "         </section> \n" +
                "        </section> \n" +
                "       </section> \n" +
                "      </section> \n" +
                "     </section> \n" +
                "     <section> \n" +
                "      <section> \n" +
                "       <section> \n" +
                "        <section> \n" +
                "         <section> \n" +
                "          <section> \n" +
                "           <p>汤氏集团总裁办副总宋荣生宋总在为新兵班开营讲话并为他们授旗，此时就是一个班，一个集体，一家人。</p> \n" +
                "          </section> \n" +
                "         </section> \n" +
                "        </section> \n" +
                "       </section> \n" +
                "      </section> \n" +
                "     </section> \n" +
                "     <section> \n" +
                "      <section> \n" +
                "       <section> \n" +
                "        <p><br></p> \n" +
                "       </section> \n" +
                "      </section> \n" +
                "     </section> \n" +
                "     <section> \n" +
                "      <section> \n" +
                "       <section> \n" +
                "        <p>一天的军训还未结束，汤董晚饭都没有吃，就在百忙之中抽时间与我们新学员分享自己的亲身经历和汤氏的前世今生。用几十年的经验来告诉我们今天工作环境条件是多么的幸福！</p> \n" +
                "        <p style=\"text-align: center;\"><img src=\"https://www.betern.com/tswl_admin/assets/upload/2018/05/31/1527728341.jpg\" style=\"max-width:100%;\"><br></p> \n" +
                "       </section> \n" +
                "      </section> \n" +
                "     </section> \n" +
                "     <section> \n" +
                "      <section> \n" +
                "       <section> \n" +
                "        <section> \n" +
                "         <section> \n" +
                "          <section></section> \n" +
                "         </section> \n" +
                "        </section> \n" +
                "       </section> \n" +
                "      </section> \n" +
                "     </section> \n" +
                "     <section> \n" +
                "      <section> \n" +
                "       <section> \n" +
                "        <p>通过一天的训练，每个新兵都进行了风采展示，将团队协作表现的淋漓尽致，这期我们会通过微信公众号投票的形式来参与本期标兵团队的评比，我们会时刻记录每个班级的训练瞬间，期望大家能为我们新兵班投上宝贵的一票。<br></p> \n" +
                "        <p><br></p> \n" +
                "       </section> \n" +
                "      </section> \n" +
                "     </section> \n" +
                "     <section> \n" +
                "      <section> \n" +
                "       <section> \n" +
                "        <section> \n" +
                "         <section> \n" +
                "          <section> \n" +
                "           <section> \n" +
                "            <p>一班</p> \n" +
                "           </section> \n" +
                "          </section> \n" +
                "         </section> \n" +
                "        </section> \n" +
                "       </section> \n" +
                "      </section> \n" +
                "     </section> \n" +
                "     <section> \n" +
                "      <section style=\"text-align: center;\"> \n" +
                "       <img src=\"https://www.betern.com/tswl_admin/assets/upload/2018/05/31/1527728342.jpg\" style=\"max-width: 100%;\"> \n" +
                "       <img src=\"https://www.betern.com/tswl_admin/assets/upload/2018/05/31/1527728343.jpg\" style=\"max-width: 100%;\"> \n" +
                "       <img src=\"https://www.betern.com/tswl_admin/assets/upload/2018/05/31/1527728344.jpg\" style=\"max-width: 100%;\"> \n" +
                "       <img src=\"https://www.betern.com/tswl_admin/assets/upload/2018/05/31/1527728345.jpg\" style=\"max-width: 100%;\">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; \n" +
                "      </section> \n" +
                "     </section> \n" +
                "    </section> \n" +
                "    <section> \n" +
                "     <section> \n" +
                "      <section> \n" +
                "       <p>&nbsp;<span id=\"js_tx_video_container_0.02011799532920122\"><iframe frameborder=\"0\" width=\"666\" height=\"374.625\" src=\"https://v.qq.com/txp/iframe/player.html?origin=https%3A%2F%2Fmp.weixin.qq.com&amp;vid=k0534fvq8z6&amp;autoplay=false&amp;full=true&amp;show1080p=false\"></iframe></span><br></p> \n" +
                "       <p><br></p> \n" +
                "       <p>各位领导以及同事们好，我们是汤氏物流第六届第二期来参加军训特训营军训的队员们。</p> \n" +
                "       <p><br></p> \n" +
                "       <p>先做个自我介绍，我叫向彬，是一班的副班长，我们的队名很有霸气，叫飞龙队，呵呵，这是我们刚成立的队伍，我们是一班，请记住哦！我们班的口号是：勇敢向前，战无不胜。要的就是这个气势！</p> \n" +
                "       <p><br></p> \n" +
                "       <p>2018年1月16日是汤氏物流第六届第三期正式训练的第一天，经过一天高强度的训练，晚上汤董亲自花一个半小时给我们授旗，讲解汤氏的发展历程，他的每一句话都说的我们温暖，在我们宣誓的那一刻，我们不光感动，更多的是激动，为我们汤董的努力而心生敬意，更为我们是汤氏的一员而幸福。</p> \n" +
                "       <p><br></p> \n" +
                "       <p>今天我们每个新兵班都进行了他们的团队风采展示，团队合作精神在这一刻被体现的淋漓尽致，我们一班的每个队员都尽量发挥自己的最大力量，给我们班争取荣誉，都只为了一个目标，将我们的汤氏员工精神展现出来，我们一次一次的失败重来，被罚了很多次，我们从不放弃，累了，我们班所有队员一起抗，疼了我们坚持再坚持，我们的执行力和团队精神在这一刻也打动了我，那一刻，我心中只有一个想法，就是要努力做到最好，不辜负汤氏对我们的栽培，不光是为了团队荣誉，也为了完善自己。</p> \n" +
                "       <p><br></p> \n" +
                "       <p>瞧！我们一个个帅气的英姿，帅气吗？我们每个都在全力以赴，这就是我们队友经过无数次的练习换来的，虽然过程很辛苦，流过汗，流过泪，但我们整个队伍都不曾放弃，因为我们拼过！为了梦想，我们会一直坚持到底，这期我们会通过微信公众号投票的形式来参与本期标兵团队的评比，看看哪个班会是我们最终的标兵团队，希望大家多关注我们第三期一班，给我们神圣而又宝贵的一票。</p> \n" +
                "       <p>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; —— 一班副班长&nbsp; 向彬</p> \n" +
                "       <p><br></p> \n" +
                "      </section> \n" +
                "     </section> \n" +
                "     <section> \n" +
                "      <section> \n" +
                "       <section> \n" +
                "        <section> \n" +
                "         <section> \n" +
                "          <section> \n" +
                "           <section> \n" +
                "            <p>二班</p> \n" +
                "           </section> \n" +
                "          </section> \n" +
                "         </section> \n" +
                "        </section> \n" +
                "       </section> \n" +
                "      </section> \n" +
                "     </section> \n" +
                "     <section> \n" +
                "      <section> \n" +
                "       <section> \n" +
                "        <section> \n" +
                "         <section> \n" +
                "          <section> \n" +
                "           <section> \n" +
                "            <section> \n" +
                "             <section> \n" +
                "              <section> \n" +
                "               <section> \n" +
                "                <section> \n" +
                "                 <section> \n" +
                "                  <section> \n" +
                "                   <section style=\"text-align: center;\">\n" +
                "                     &nbsp; \n" +
                "                    <img src=\"https://www.betern.com/tswl_admin/assets/upload/2018/05/31/1527728346.jpg\" style=\"max-width: 100%;\"> \n" +
                "                    <img src=\"https://www.betern.com/tswl_admin/assets/upload/2018/05/31/1527728347.jpg\" style=\"max-width: 100%;\"> \n" +
                "                    <img src=\"https://www.betern.com/tswl_admin/assets/upload/2018/05/31/1527728348.jpg\" style=\"max-width: 100%;\"> \n" +
                "                    <img src=\"https://www.betern.com/tswl_admin/assets/upload/2018/05/31/1527728349.jpg\" style=\"max-width: 100%;\"> \n" +
                "                   </section> \n" +
                "                  </section> \n" +
                "                 </section> \n" +
                "                </section> \n" +
                "               </section> \n" +
                "              </section> \n" +
                "             </section> \n" +
                "            </section> \n" +
                "           </section> \n" +
                "          </section> \n" +
                "         </section> \n" +
                "        </section> \n" +
                "       </section> \n" +
                "      </section> \n" +
                "     </section> \n" +
                "     <section> \n" +
                "      <section> \n" +
                "       <section> \n" +
                "        <p>今天是开训的第一天，教官的严格让我们领教到了军训的艰苦，特别是班长被罚做俯卧撑，一次十个二十个四十个再到八十个……班长都一直咬紧牙关坚持着没有放弃。这种精神值得我们全队学习。特别是当我们受罚做完俯卧撑后，其他队员们纷纷过来搀扶我们，这种团结的精神使我相信，在今后的训练中没有我们完不成的任务。今天我们给全队取了一个飞鱼队的队名，飞鱼飞鱼勇拿第一。我相信在班长的带领下，我们一定能拿第一，无论多苦多累。谢谢公司给了我们这个机会，通过军训更好的挖掘自己，在以后的工作中为公司尽自己一份微薄的力量。谢谢大家！</p> \n" +
                "        <p>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;——二班副班长&nbsp; 马力</p> \n" +
                "        <p><br></p> \n" +
                "       </section> \n" +
                "      </section> \n" +
                "     </section> \n" +
                "     <section> \n" +
                "      <section> \n" +
                "       <section> \n" +
                "        <section> \n" +
                "         <section> \n" +
                "          <section> \n" +
                "           <section> \n" +
                "            <p>三班</p> \n" +
                "           </section> \n" +
                "          </section> \n" +
                "         </section> \n" +
                "        </section> \n" +
                "       </section> \n" +
                "      </section> \n" +
                "     </section> \n" +
                "     <section> \n" +
                "      <section> \n" +
                "       <section> \n" +
                "        <section> \n" +
                "         <section> \n" +
                "          <section> \n" +
                "           <section> \n" +
                "            <section> \n" +
                "             <section> \n" +
                "              <section> \n" +
                "               <section> \n" +
                "                <section> \n" +
                "                 <section> \n" +
                "                  <section> \n" +
                "                   <section style=\"text-align: center;\">\n" +
                "                     &nbsp; \n" +
                "                    <img src=\"https://www.betern.com/tswl_admin/assets/upload/2018/05/31/1527728350.jpg\" style=\"max-width: 100%;\"> \n" +
                "                    <img src=\"https://www.betern.com/tswl_admin/assets/upload/2018/05/31/1527728351.jpg\" style=\"max-width: 100%;\"> \n" +
                "                    <img src=\"https://www.betern.com/tswl_admin/assets/upload/2018/05/31/1527728352.jpg\" style=\"max-width: 100%;\"> \n" +
                "                    <img src=\"https://www.betern.com/tswl_admin/assets/upload/2018/05/31/1527728353.jpg\" style=\"max-width: 100%;\"> \n" +
                "                   </section> \n" +
                "                  </section> \n" +
                "                 </section> \n" +
                "                </section> \n" +
                "               </section> \n" +
                "              </section> \n" +
                "             </section> \n" +
                "            </section> \n" +
                "           </section> \n" +
                "          </section> \n" +
                "         </section> \n" +
                "        </section> \n" +
                "       </section> \n" +
                "      </section> \n" +
                "     </section> \n" +
                "     <section> \n" +
                "      <section> \n" +
                "       <section> \n" +
                "        <p>今天是公司第六届第三期军事特训的第一天，同时也是我们三班伙伴们难忘的一天。在今天军训中，包含着我们的酸甜苦辣。站军姿，起步走，跑步，正步走，蹲坐。我们一遍又一遍地重复着枯燥的动作;喊口令，我们用全身的力气吼叫，却总不能让教官满意。腰酸了，必须撑着;背疼了，还要挺直;喊哑了嗓子，喉咙还依然忍痛嘶叫。但我们坚信一个信念:坚持就是胜利！明天将全力以赴，比今天更胜一筹！</p> \n" +
                "        <p>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; ——三班副班长&nbsp; 陈定芳</p> \n" +
                "        <p style=\"text-align: center;\"><img src=\"https://www.betern.com/tswl_admin/assets/upload/2018/05/31/1527728354.jpg\" style=\"max-width:100%;\"><br></p> \n" +
                "       </section> \n" +
                "      </section> \n" +
                "     </section> \n" +
                "     <section> \n" +
                "      <section> \n" +
                "       <section> \n" +
                "        <section> \n" +
                "         <section> \n" +
                "          <section> \n" +
                "           <p>三班副班长，新兵班唯一的女副班，用着纤细的身躯与班长一起趴在地上做俯卧撑，为了女副班长也要更加的努力！</p> \n" +
                "          </section> \n" +
                "         </section> \n" +
                "        </section> \n" +
                "       </section> \n" +
                "      </section> \n" +
                "     </section> \n" +
                "     <section> \n" +
                "      <section> \n" +
                "       <section> \n" +
                "        <p><br></p> \n" +
                "       </section> \n" +
                "      </section> \n" +
                "     </section> \n" +
                "     <section> \n" +
                "      <section> \n" +
                "       <section></section> \n" +
                "      </section> \n" +
                "     </section> \n" +
                "     <section> \n" +
                "      <section> \n" +
                "       <section> \n" +
                "        <p>如果他们中间有你的同事、家人，或者虽然你不认识，但你认可他们，那请你让我们一起见证他们的成长，为他们投上宝贵的一票！</p> \n" +
                "       </section> \n" +
                "      </section> \n" +
                "     </section> \n" +
                "     <section> \n" +
                "      <section> \n" +
                "       <section> \n" +
                "        <section></section> \n" +
                "        <section></section> \n" +
                "       </section> \n" +
                "       <section> \n" +
                "        <section> \n" +
                "         <section>\n" +
                "           未 \n" +
                "         </section> \n" +
                "        </section> \n" +
                "        <section> \n" +
                "         <section>\n" +
                "           完 \n" +
                "         </section> \n" +
                "        </section> \n" +
                "        <section> \n" +
                "         <section>\n" +
                "           待 \n" +
                "         </section> \n" +
                "        </section> \n" +
                "        <section> \n" +
                "         <section>\n" +
                "           续 \n" +
                "         </section> \n" +
                "        </section> \n" +
                "       </section> \n" +
                "      </section> \n" +
                "     </section> \n" +
                "     <section> \n" +
                "      <section> \n" +
                "       <section> \n" +
                "        <section> \n" +
                "         <section> \n" +
                "          <section> \n" +
                "           <p><iframe scrolling=\"no\" frameborder=\"0\" src=\"https://mp.weixin.qq.com/mp/newappmsgvote?action=show&amp;__biz=MzAwNzU3MTEzNA==&amp;supervoteid=500079641&amp;uin=MjEzODE3MDIxMQ%3D%3D&amp;key=4ffd8f45e8787f67dafb613c1387f40cdbe01f2a07d1362877f28629c796791229c4b5940f4ce8910d1275fee1cf6a2ff71d7b23ed84a6c3a8328619f97eff23cc6502c29b10da75288b22613a40932d&amp;pass_ticket=3JM2VzTf6GIBqhoZS52AAmqTZhyHdQXkzRkChVP3HuJlyjEPU3KSK7siK5uO7%252Fgy&amp;wxtoken=777&amp;mid=2657468985&amp;idx=1&amp;appmsg_token=959_iPBc46i8xra5PSY4G6WDk-QzwdtFRK-PS_MX5cJ1QVybZzlNdCQ37N1WHqqVPVz4ERdW8Ruwg20gB90s\"></iframe>.</p> \n" +
                "          </section> \n" +
                "         </section> \n" +
                "        </section> \n" +
                "       </section> \n" +
                "      </section> \n" +
                "     </section> \n" +
                "    </section> \n" +
                "   </div> <p><br></p> </span>";
        Document doc = Jsoup.parse(htmlBody);
        Elements eles = doc.getAllElements();
        for(Element ele : eles){
            if (ele.tagName().equals("style") || ele.tagName().equals("script")){
                ele.empty();
                continue;
            }
            if(!useList(taglist,ele.tagName()) && !ele.tagName().equals("html") && !ele.tagName().equals("body")){
                ele.tagName("span");
            }
            List<Attribute> arr = ele.attributes().asList();
            for(Attribute item:arr){
                if (!useList(attrlist,item.getKey())){
                    ele.attributes().removeIgnoreCase(item.getKey());
                }
            }
        }

        int flag =0;
        while (flag<10){
            eles = doc.getAllElements();
            int num = eles.size();
            for(Element ele : eles){
                if(ele.html().replaceAll("\\s","").equals("") && !ele.tagName().equals("img")){ele.remove();continue;}
                if (ele.parent()==null || ele.parent().parent()==null || ele.parent().parent().parent() == null){continue;}
                if (!ele.parent().tagName().equals(ele.tagName())){
                    continue;
                }
                if (!ele.parent().parent().tagName().equals(ele.tagName())){
                    continue;
                }
                if (ele.attributes().size() != 0){
                    continue;
                }
                if (ele.parent().attributes().size() != 0){
                    continue;
                }
                if (ele.parent().parent().attributes().size() != 0){
                    continue;
                }
                if (ele.parent().children().size() != 1){
                    continue;
                }
                if (ele.parent().parent().children().size() != 1){
                    continue;
                }
                ele.parent().parent().parent().appendChild(ele);
            }
            if (doc.getAllElements().size() == num){
                flag += 1;
            }
        }

        String newsBody = doc.getElementsByTag("body").html();
        System.out.println(newsBody);
    }
    public static boolean useList(String[] arr, String targetValue) {
        return Arrays.asList(arr).contains(targetValue);
    }
}
