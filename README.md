# tlias_day13
day13代码（JWT令牌，Inception拦截器，AOP自动化）
1.JWT（Json Web Token)
  用于身份认证和信息传递的令牌格式
  由三部分组成：Header.Payload.Signature
    Header：说明令牌类型和签名算法
    Payload：存放用户信息和声明
    Signature：对前两部分签名，防止被篡改（所以不会对信息进行加密，只能防篡改）

2.Inception拦截器
   浏览器请求
     ↓
    Filter（拦截所有资源，包括静态资源）
     ↓
    SpringMVC DispatcherServlet
     ↓
    LoginInterceptor   ← 这里拦截（拦截SpringBoot中的内容）
     ↓
    Controller

3.AOP自动化
  1.注解类：
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface LogOperation {}
  2.切面类：
    @Aspect
    @Component
（其中ProceedingJoinPoint joinPoint 可以理解成：当前被 AOP 拦截到的那个方法对象 + 这个方法执行现场的信息。所以可以进行执行原方法proceed()，也可以拿信息，比如getName()...
  而JoinPoint：只能拿信息，不可以执行）
    public class OperationLogAspect {
      @Autowired
      private OperateLogMapper operateLogMapper;
      3a.进行标注
      @Around("@annotation(log)")
      public Object around(ProceedingJoinPoint joinPoint, LogOperation log) throws Throwable{}
      
      ...对实体注入内容的操作}

  3b.标注要记录的方法
    @LogOperation
    @PostMapping
    public Result save(@RequestBody Emp emp){
        log.info("新增员工：{}",emp);
        empService.save(emp);
        return Result.success();
    }
