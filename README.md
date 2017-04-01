# mySpring


一个小程序，旨在模拟实现 Spring的功能。
层次如下：

![enter image description here](http://oimbmvqt3.bkt.clouddn.com/%E6%9E%B6%E6%9E%84.PNG)

---

- 面向抽象编程：

   - 为了灵活地面对不同的数据库，在 userService 的 userDAO 里面，不把 DAO 写死。把 userDAO 写成 interface。
   - 再写 DAO 的实现：userDAOImpletation，让它实现 userDAO 接口。
   - 这样，当使用 userService 时，是使用接口编程。
   - 可以多写几个 UserDAO的实现，针对不同的数据库 mysql、Oracle等，需要用哪个就 new 哪个。

---

模拟 Spring 中的：
 
- BeanFactory：

   - 需要将来用到的各种各样的 DAO  对象的工厂，要用时去拿。
   - 提供 getBean 方法。

- ClassPathXmlApplicationContext：

```java
public class ClassPathXmlApplicationContext implements BeanFactory {
	
	private Map<String , Object> beans = new HashMap<String, Object>();
	
	
	//IOC Inverse of Control DI Dependency Injection
	public ClassPathXmlApplicationContext() throws Exception {
		SAXBuilder sb=new SAXBuilder();
	    
	    Document doc=sb.build(this.getClass().getClassLoader().getResourceAsStream("beans.xml")); //构造文档对象
	    Element root=doc.getRootElement(); //获取根元素HD
	    List list=root.getChildren("bean");//取名字为disk的所有元素
	    for(int i=0;i<list.size();i++){
	       Element element=(Element)list.get(i);
	       String id=element.getAttributeValue("id");
	       String clazz=element.getAttributeValue("class");
	       Object o = Class.forName(clazz).newInstance();
	       beans.put(id, o);
	       
	       for(Element propertyElement : (List<Element>)element.getChildren("property")) {
	    	   String name = propertyElement.getAttributeValue("name"); //userDAO
	    	   String bean = propertyElement.getAttributeValue("bean"); //u
	    	   Object beanObject = beans.get(bean);//UserDAOImpl instance
	    	   
	    	   String methodName = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
	    	   System.out.println("method name = " + methodName);
	    	   
	    	   Method m = o.getClass().getMethod(methodName, beanObject.getClass().getInterfaces()[0]);
	    	   m.invoke(o, beanObject);
	       }	       
	    }  	  
	}

	public Object getBean(String id) {
		return beans.get(id);
	}

}

```

- BeanFactory 的实现类；
   - 有一个 Map  容器；
   - 根据读取 xml 的方式；
   - 读取根元素 beans；
   - 拿到所有子元素 bean；
   - 把 bean 里面是属性 ID 拿出来，class 拿出来；
   - 利用反射机制，将 class 生成对象；
   - 将 ID  和对应的对象放到 Map 容器中。

   - 一个 getBean 方法，从容器中取出。

---

将来要用时：

```java
public class UserServiceTest {

	@Test
	public void testAdd() throws Exception {
	    //把容器 new 出来
		BeanFactory applicationContext = new ClassPathXmlApplicationContext();
				
		UserService service = (UserService)applicationContext.getBean("userService");
		
		//从配置文件中，把 UserDAO 拿出来 		
		UserDAO userDAO = (UserDAO)factory.getBean("u");
		service.setUserDAO(userDAO);
		
		User u = new User();
		service.add(u);
	}

}
```

----------

实现动态装配：

 


