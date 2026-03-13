package com.example.interceptor;

import com.example.utils.CurrentHolder;
import com.example.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        //1.获取 请求路径 url
        String URL = request.getRequestURI().toString();
        log.info("preHandle，请求路径: {}", URL);

        //2.判断url中是否包含login
        if("/login".equals(URL)){
            log.info("登录请求，放行");
            return true;
        }

        //3.获取请求头中的jwt
        String JWT = request.getHeader("token");
        //4. 判断令牌是否存在，如果不存在，返回错误结果（未登录）。
        if(!StringUtils.hasLength(JWT)) {//jwt为空
            log.info("获取到jwt令牌为空, 返回错误结果");
            response.setStatus(HttpStatus.SC_UNAUTHORIZED);
            return false;
        }
        //5. 解析token，如果解析失败，返回错误结果（未登录）。
        try {
            Claims claims = JwtUtils.parseJWT(JWT);
            Integer empId = Integer.valueOf(claims.get("id").toString());
            CurrentHolder.setCurrentId(empId);
            //6. 放行。
            log.info("token解析成功，当前登录用户id={}", empId);
            return true;
        } catch (Exception e) {
            log.error("解析令牌失败", e);
            response.setStatus(HttpStatus.SC_UNAUTHORIZED);
            return false;
        }

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("postHandle 放行后操作... ");
    }

    //视图渲染完毕后执行，最后执行
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //7. 清空当前线程绑定的id
        CurrentHolder.remove();
        log.info("afterCompletion... 已清理当前线程用户id");
    }
}
