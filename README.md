# tlias_day13

# day13代码（JWT令牌，Inception拦截器，AOP自动化）

## 1.JWT（Json Web Token)

  用于身份认证和信息传递的令牌格式
  由三部分组成：Header.Payload.Signature
    **Header**：说明令牌类型和签名算法
    **Payload**：存放用户信息和声明
    **Signature**：对前两部分签名，防止被篡改（所以不会对信息进行加密，只能防篡改）

## 2.Inception拦截器

浏览器请求
     		↓
    	Filter（拦截所有资源，包括静态资源）
    		 ↓
    	SpringMVC DispatcherServlet
     		↓
    	LoginInterceptor   ← 这里拦截（拦截SpringBoot中的内容）
     		↓
   	 Controller

### 实现：

#### 		1.在utils包下新建xxxInception,并继承HandlerInterceptor，重写。

```java
   public class LoginInterception implements HandlerInterceptor {
    //快捷键 ctrl+i
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1.获取session
        HttpSession session = request.getSession();
        //2.获取session中的用户
        Object user = session.getAttribute("user");
        //3.判断用户是否存在
        if(user == null){
            response.setStatus(401);
            return false;
        }
        //4.存在，保存用户信息到ThreadLocal
        UserHolder.saveUser((UserDTO) user);
   //5.放行
    return true;
}

@Override
public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
    //移除用户信息，防止信息泄露
    UserHolder.removeUser();
}
```

#### 	2.在config包下新建拦截器配置，进行拦截器的注册，包括配置拦截器拦截哪些接口，放行哪些。

```java
@Configuration
public class MvcConfig implements WebMvcConfigurer {
    //添加拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //registry是拦截器的注册器
        //添加登录拦截器
        registry.addInterceptor(new LoginInterception());
    }
}
```

## 3.AOP自动化

常用于：日志记录，权限校验，性能统计等

可以记录开始时间，执行业务，记录结束时间，保存日志

### 1.核心概念

#### 连接点(所有可能被拦截的方法)

程序运行时，可以被AOP插手的位置，例如方法

```java
public void save() {}
public void update() {}
public void delete() {}
```

#### 切点(真正被选中的方法)

从连接点中，选出真正要拦截的

```java
@Around("@annotation(log)")//只拦截带@LogOperation注解的方法
```

```java
@LogOperation
@PostMapping
public Result save(@RequestBody Emp emp){
	log.info("新增员工：{}",emp);
     empService.save(emp);
     return Result.success();
} 
```

save方法会被拦截

#### 通知

在切点执行的某个时刻，额外要做的事情

常见通知类型：

- `@Before`：方法执行前
- `@After`：方法执行后
- `@AfterReturning`：方法正常返回后
- `@AfterThrowing`：方法抛异常后
- `@Around`：环绕整个方法执行前后

#### 切面（切点＋通知）

```java
@Aspect//告诉 Spring AOP：这里面有切点和通知。
@Component//把这个类交给 Spring 容器管理。
public class OperationLogAspect {
}
```



###   2.自定义注解：

```java
@Target(ElementType.METHOD)//这个注解只能放在方法上。
@Retention(RetentionPolicy.RUNTIME)//这个注解会保留到运行时。
public @interface LogOperation {}
```



###   3.执行流程：

```java
@Around("@annotation(log)")
public Object around(ProceedingJoinPoint joinPoint, LogOperation log) throws Throwable {
    long startTime = System.currentTimeMillis();

    Object result = joinPoint.proceed();

    long endTime = System.currentTimeMillis();
    long costTime = endTime - startTime;

    OperateLog operateLog = new OperateLog();
    operateLog.setOperateEmpId(getCurrentUserId());
    operateLog.setOperateTime(LocalDateTime.now());
    operateLog.setClassName(joinPoint.getTarget().getClass().getName());
    operateLog.setMethodName(joinPoint.getSignature().getName());
    operateLog.setMethodParams(Arrays.toString(joinPoint.getArgs()));
    operateLog.setReturnValue(result == null ? null : result.toString());
    operateLog.setCostTime(costTime);

    operateLogMapper.insert(operateLog);
    return result;
}

```

拦截带 `@LogOperation` 的方法--->记录开始时间--->执行原方法：`joinPoint.proceed()`--->拿到返回值--->统计耗时--->封装日志对象 `OperateLog`--->插入数据库--->把原方法返回结果继续返回给前端

（其中**ProceedingJoinPoint** joinPoint 可以理解成：当前被 AOP 拦截到的那个方法对象 + 这个方法执行现场的信息。所以可以进行执行原方法proceed()，也可以拿信息，比如getName()...
  而**JoinPoint**：只能拿信息，不可以执行）    

## 4. 拦截器和 AOP 的配合关系

你的登录拦截器做了两件重要事：

 **在 `preHandle` 里解析 token**

```java
Claims claims = JwtUtils.parseJWT(jwt);
Integer empId = Integer.valueOf(claims.get("id").toString());
CurrentHolder.setCurrentId(empId);
```

作用是：**把当前登录用户 id 放进 ThreadLocal。**

------

**在 `afterCompletion` 里清理**

```java
CurrentHolder.remove();
```

作用是：**请求结束后清空当前线程数据，避免线程复用导致脏数据。**
