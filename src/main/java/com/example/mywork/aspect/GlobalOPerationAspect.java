package com.example.mywork.aspect;

import com.example.mywork.annotation.GlobalInterceptor;
import com.example.mywork.annotation.VerfiyParam;
import com.example.mywork.utils.StringUtils;
import com.example.mywork.utils.VerifyUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Aspect
@Component
public class GlobalOPerationAspect {

    private final String TYPE_STRING = "java.lang.String";

    private final String TYPE_INTEGER = "java.lang.Integer";

    private final String TYPE_LONG = "java.lang.Long";

    @Pointcut("@annotation(com.example.mywork.annotation.GlobalInterceptor)")
    private void requestInterceptor(){

    }
    @Before("requestInterceptor()")
    private void interceptorDo(JoinPoint joinPoint) throws NoSuchMethodException, ClassNotFoundException, IllegalAccessException {
        Object target = joinPoint.getTarget();
        Object[] args = joinPoint.getArgs();
        String name = joinPoint.getSignature().getName();
        Class<?>[] parameterTypes = ((MethodSignature) joinPoint.getSignature()).getMethod().getParameterTypes();
        Method method = target.getClass().getMethod(name, parameterTypes);
        GlobalInterceptor annotation = method.getAnnotation(GlobalInterceptor.class);

        if (annotation.checkParasm()){
            validateParams(method,args);
        }
    }

    private void validateParams(Method m,Object[] args) throws ClassNotFoundException, IllegalAccessException {
        Parameter[] parameters = m.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Object value = args[i];
            VerfiyParam annotation = parameter.getAnnotation(VerfiyParam.class);
            if (annotation==null){
                continue;
            }
            //基本数据类型
            if (TYPE_STRING.equals(parameter.getParameterizedType().getTypeName())||
                    TYPE_INTEGER.equals(parameter.getParameterizedType().getTypeName())||
                    TYPE_LONG.equals(parameter.getParameterizedType().getTypeName())){
                checkValue(value,annotation);
            }else {
                checkObjValue(parameter,value);
            }

        }

    }

    private void checkObjValue(Parameter parameter,Object value) throws ClassNotFoundException, IllegalAccessException {
        String typeName = parameter.getParameterizedType().getTypeName();
        Class<?> aClass = Class.forName(typeName);
        Field[] fields = aClass.getDeclaredFields();
        for (Field field : fields) {
            VerfiyParam annotation = field.getAnnotation(VerfiyParam.class);
            if (annotation==null){
                continue;
            }
            field.setAccessible(true);
            Object resultValue=field.get(value);
            checkValue(resultValue,annotation);
        }
    }
    private void checkValue(Object value,VerfiyParam annotation){
       Boolean isEmpty= value == null || StringUtils.isEmpty(String.valueOf(value));
       Integer length=value==null?0:value.toString().length();
        /**
         * 校验空
         */
        if (isEmpty&&annotation.required()){
            throw new RuntimeException();
        }
        /**
         * 校验长度
         */
        if (!isEmpty&&annotation.max()!=-1&&(annotation.max()<length||annotation.min()!=-1&&annotation.min()>length)){
            throw new RuntimeException();
        }
        /**
         * 校验正则
         */
        if (isEmpty&& !StringUtils.isEmpty(annotation.regex().getRegex())&& VerifyUtils.verify(annotation.regex(),String.valueOf(value))){
            throw new RuntimeException();
        }

    }
}
