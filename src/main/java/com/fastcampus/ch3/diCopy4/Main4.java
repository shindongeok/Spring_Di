package com.fastcampus.ch3.diCopy4;

import com.google.common.reflect.ClassPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component class Car{
    @Resource
    Engine engine;
//    @Resource
    Door door;

    @Override
    public String toString() {
        return "Car{" +
                "engine=" + engine +
                ", door=" + door +
                '}';
    }
}
@Component class SportsCar extends Car{}
@Component class Truck extends Car{}
@Component class Engine{}
@Component class Door{}

class AppContext{
    Map map;

    AppContext() throws IOException, InstantiationException, IllegalAccessException {
        map=new HashMap();

        doComponentScan();
        doAutowired();
        doResource();

    }

    private void doResource() throws IllegalAccessException {
        // map에 저장된 객체의 iv 중에 @Autowired가 붙어 있으면
        // map에서 iv의 타입에 맞는 객체를 찾아서 연결(객체의 주소를 iv에 지정)
        for (Object bean : map.values()) {
            // 객체의 모든 필드를 순회
            for (Field fld : bean.getClass().getDeclaredFields()) {
                // @Autowired가 붙은 필드를 찾음
                if (fld.getAnnotation(Resource.class) != null) {
                    // 필드 접근 가능하게 설정
                    fld.setAccessible(true);
                    // 해당 필드 타입에 맞는 객체를 주입
                    fld.set(bean, getBean(fld.getName()));
                }
            }
        }
    }

    private void doAutowired() throws IllegalAccessException {
        // map에 저장된 객체의 iv 중에 @Autowired가 붙어 있으면
        // map에서 iv의 타입에 맞는 객체를 찾아서 연결(객체의 주소를 iv에 지정)
        for (Object bean : map.values()) {
            // 객체의 모든 필드를 순회
            for (Field fld : bean.getClass().getDeclaredFields()) {
                // @Autowired가 붙은 필드를 찾음
                if (fld.getAnnotation(Autowired.class) != null) {
                    // 필드 접근 가능하게 설정
                    fld.setAccessible(true);
                    // 해당 필드 타입에 맞는 객체를 주입
                    fld.set(bean, getBean(fld.getType()));
                }
            }
        }
    }

    private void doComponentScan() throws IOException, InstantiationException, IllegalAccessException {
        //패키지내의 클래스 목록을 가져온다.
        // 반복문으로 클래스를 하나씩 @Component 읽어와서 이 붙어 있는지 확인
        //
       ClassLoader classLoader = AppContext.class.getClassLoader();
        ClassPath classPath = ClassPath.from(classLoader);

        Set<ClassPath.ClassInfo> set=classPath.getTopLevelClasses("com.fastcampus.ch3.diCopy4");

        for(ClassPath.ClassInfo classInfo : set){
            Class clazz = classInfo.load();
            Component component = (Component) clazz.getAnnotation(Component.class);
            if(component !=null){
                String id = StringUtils.uncapitalize(classInfo.getSimpleName());
                map.put(id, clazz.newInstance());
            }
        }
    }

    Object getBean(String key){
        return map.get(key);
    }
    Object getBean(Class clazz){
        for(Object obj : map.values()){
            if(clazz.isInstance(obj))
                return obj;
        }
        return null;
    }

}
public class Main4 {

    public static void main(String[] args) throws Exception {
        AppContext ac = new AppContext();
        Car car =(Car)ac.getBean("car");

        Engine engine =(Engine) ac.getBean("engine");
        Door door =(Door) ac.getBean(Door.class);

//        car.engine= engine;
//        car.door=door;
        System.out.println(car);
        System.out.println("engine : " + engine);
        System.out.println(door);



    }

}
