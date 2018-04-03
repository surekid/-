package com.cx.HeadLine.controller;

import com.cx.HeadLine.model.User;
import com.cx.HeadLine.service.HeadLineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
//通过注解的方式来说明类的用途
public class IndexController {

    private static  final Logger logger=LoggerFactory.getLogger(IndexController.class);
    @Autowired
    //就加了@Autowired和@service，就可以初始化service对象
    private  HeadLineService service;
    //传统的方式
//    public IndexController(){
//        service=new HeadLineService();
//    }

    //指定首页的url,表明一旦访问首页就用这个函数来处理
    @RequestMapping(path={"/","/index"})
    @ResponseBody
    //首页
//    public String index(){
//        return "Hello,HeadLine";
//    }
    public String index(HttpSession session){
        logger.info("Visit Index");
        //打开首页会打印logger
        return "Hello,HeadLine!!!"+session.getAttribute("msg")+"<br> Say:"+service.say();
    }

    //value和path是一样的
    @RequestMapping(value={"/profile/{groupId}/{userId}"})
    @ResponseBody
    //用户的个人信息,比如输入http://localhost:8080/profile/1/2?type=2222&key=sure
    //PathVariable值得是地址中的参数
    //RequestParam值得是?后面的
    public String profile(@PathVariable("groupId") String groundId,
                          @PathVariable("userId") int userId,
                          @RequestParam(value="type",defaultValue="1") int type,
                          @RequestParam(value="key",defaultValue="HeadLine") String key){
        //会有很多的service
        return  String.format("GID{%s},UID{%d},TYPE{%d},KEY{%s}",groundId,userId,type,key);
    }

    @RequestMapping(value = {"/vm"})
    //使用一个网页的模板
    //Model后端与渲染之间交互存储数据的模型,可以在页面上显示(类似以前的session/Attribute)
    public  String news(Model model){
        model.addAttribute("value1","vv1");
        List<String> cols= Arrays.asList(new String[]{"red","green","blue"});

        Map<String,String> map=new HashMap<>();
        for(int i=0;i<4;i++){
            map.put(String.valueOf(i),String.valueOf(i*i));
        }
        model.addAttribute("cols",cols);
        model.addAttribute("map",map);

        model.addAttribute("user",new User("chenxu"));
        //前端页面
        return "news";
    }

    //Request对象的用法
    @RequestMapping(value={"/request"})
    @ResponseBody
    //可以直接将标准的类型，注入进来
    public  String request(HttpServletRequest request,
                           HttpServletResponse response,
                           HttpSession session){
        StringBuilder sb=new StringBuilder();
        //获得http request中所有的头文件
        Enumeration<String> headerNames=request.getHeaderNames();
        while(headerNames.hasMoreElements()){
            String head=headerNames.nextElement();
            sb.append(head+":"+request.getHeader(head)+"<br>");
        }
        sb.append("-----------------------------------------"+"<br>");
        //JSESSIONID用来标识是否是同一次http请求
        for(Cookie cookie:request.getCookies()){
            sb.append("Cookies:");
            sb.append(cookie.getName());
            sb.append(":");
            sb.append(cookie.getValue());
            sb.append("<br>");
        }
        sb.append("-----------------------------------------"+"<br>");

        //get or post
        sb.append("getMethod():"+request.getMethod()+"<br>");
        sb.append("getPathInfo:"+request.getPathInfo()+"<br>");
        //指的是？后面的内容
        sb.append("getQueryString:"+request.getQueryString()+"<br>");
        //指的是服务器后面的内容
        sb.append("getRequestURI():"+request.getRequestURI()+"<br>");

        return  sb.toString();
    }

    //Response对象的用法
    @RequestMapping(value={"/response"})
    @ResponseBody
    public  String response(@CookieValue(value="nowcoderid",defaultValue = "a") String nowcoderId,
                            @RequestParam(value="key",defaultValue = "key") String key,
                            @RequestParam(value = "value",defaultValue = "value") String value,
                            HttpServletResponse response){
        //Set-Cookie: cx=2222222
        response.addCookie(new Cookie(key,value));
        //会以key:value的形式加入到response的Header里 cx: 2222222
        response.addHeader(key,value);
        //将key和value写入到cookie
        //获得的是Cookie中的nowcoderId的value
        return  "NowCoderId from Cookie:"+nowcoderId;
    }

    //页面的重定向
    @RequestMapping("/redirect/{code}")
    //RedirectView指的是返回值的信息
//    public RedirectView redirect(@PathVariable("code") int code){
       //只要浏览器不关闭，所有的会话用的都是同一个session
    public String redirect(@PathVariable("code") int code,
                           HttpSession session){
        /**
        RedirectView red=new RedirectView("/",true);
        //301表示永久转移
        if(code == 301){
            red.setStatusCode(HttpStatus.PERMANENT_REDIRECT);
        }
        return  red;
         */

        //同一次请求中不同页面之间传递数据，使用session
        session.setAttribute("msg","Jump from redirect.");
        return  "redirect:/";
    }


    @RequestMapping("/admin")
    @ResponseBody
    public  String admin(@RequestParam(value="key",required = false) String key){
        if("admin".equals(key)){
            return  "hello admin";
        }
        throw new IllegalArgumentException("Key 错误");
    }

    //自定义的错误页面（错误会通过注解传递过来的）
    @ExceptionHandler
    @ResponseBody
    public String error(Exception e){
        return  "error:"+e.getMessage();
    }

}
