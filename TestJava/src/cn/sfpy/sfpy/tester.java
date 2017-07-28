package cn.sfpy.sfpy;

import cn.sfpy.pojo.TB_SFPY_USER;
import cn.sfpy.session.JaveSession;

public class tester {
	public static void main(String args[]) throws Exception {
        //获得NetJavaSession对象
		JaveSession session = new JaveSession();
        //创建一个UserInfo对象
        TB_SFPY_USER user = new TB_SFPY_USER();
        //设置对象的属性
        user.setUser_name("zhangkai");
        user.setUser_phone("15996211994");
        user.setUser_pwd("1234");
        //user.setUser_id(1);
        //将对象保存到数据库中
        String sql = session.getSaveObjectSql(user);
        System.out.println("保存对象的sql语句：" + sql);
        
        session.saveObject(user);
        System.out.println("插入成功！");
       /* //查找对象
        TB_SFPY_USER userInfo = (TB_SFPY_USER) session.getObject( "cn.sfpy.pojo.TB_SFPY_USER", 6988);
        System.out.println("获取到的信息：" + userInfo);*/
 
    }
}
